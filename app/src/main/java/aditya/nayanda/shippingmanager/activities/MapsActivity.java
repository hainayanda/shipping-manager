package aditya.nayanda.shippingmanager.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activities.helper.ActivityHelper;
import aditya.nayanda.shippingmanager.activities.helper.MapHelper;
import aditya.nayanda.shippingmanager.fragments.dialog.JobDetailsDialogFragment;
import aditya.nayanda.shippingmanager.model.Job;
import aditya.nayanda.shippingmanager.model.ListOfJobs;
import aditya.nayanda.shippingmanager.model.Locator;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static boolean isPermissionGranted;
    private final ArrayList<Job> jobs = new ArrayList<>();
    float DEFAULT_ZOOM = 14;
    private ListOfJobs listOfJobs;
    private GoogleMap googleMap;
    private FusedLocationProviderClient locationProviderClient;
    private Location lastKnownLocation;
    private Marker myMarker;
    private ProgressDialog progressDialog;

    private static void setOnLongPressListener(GoogleMap googleMap, OnLongPressListener onLongPressListener) {

        //Simulate long click
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Locator locator = (Locator) marker.getTag();
                if (locator == null) return;
                marker.setPosition(locator.getLocation());
                onLongPressListener.onLongPress(marker);
                marker.showInfoWindow();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Locator locator = (Locator) marker.getTag();
                if (locator == null) return;
                marker.setPosition(locator.getLocation());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Locator locator = (Locator) marker.getTag();
                if (locator == null) return;
                marker.setPosition(locator.getLocation());
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView actionBarTitle = ActivityHelper.setToCustomActionBar(this);
        actionBarTitle.setText(R.string.title_activity_maps);

        setContentView(R.layout.activity_maps);
        if (listOfJobs == null) fetchListOfJobs();
        if (listOfJobs == null) {
            onBackPressed();
            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        findViewById(R.id.button_my_location).setOnClickListener(view -> getDeviceLocation());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        setMarkerListener(googleMap);

        if (listOfJobs == null) fetchListOfJobs();
        if (listOfJobs == null) {
            onBackPressed();
            return;
        }
        addMarker(listOfJobs.getWareHouse());
        for (Job job : jobs) {
            addMarker(job);
        }
        if (jobs.size() > 0) {
            findViewById(R.id.button_start_navigation).setOnClickListener(view -> {
                LatLng origin = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                LatLng lastDestination = listOfJobs.getWareHouse().getLocation();
                List<LatLng> destinations = new LinkedList<>();
                for (Job job : jobs) {
                    destinations.add(job.getLocation());
                }
                Uri gMapIntentUri = MapHelper.createGoogleMapRouteIntentUri(origin, lastDestination, destinations);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gMapIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                try {
                    startActivity(mapIntent);
                } catch (ActivityNotFoundException e) {
                    Log.e("ERROR", e.toString());
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.toast_ask_gmap_installation, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        }
        getLocationPermission();
        if (isPermissionGranted) getDeviceLocation();

    }

    private void fetchListOfJobs() {
        listOfJobs = getJobsFromIntent();
        jobs.addAll(listOfJobs.getJobs());
    }

    private void setMarkerListener(GoogleMap googleMap) {
        setOnLongPressListener(googleMap, marker -> {
            Locator locator = (Locator) marker.getTag();
            if (!(locator instanceof Job)) return;
            FragmentManager fragmentManager = MapsActivity.this.getSupportFragmentManager();
            JobDetailsDialogFragment dialogFragment = JobDetailsDialogFragment.newInstance(0.9f, (Job) locator);
            dialogFragment.show(fragmentManager, "details_dialog");
        });

        googleMap.setOnMarkerClickListener(marker -> {
            if (marker.getTag() != null) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.toast_long_press_info, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 342);
                toast.show();
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        mainIntent.putExtra("JOBS", listOfJobs);
        mainIntent.putExtra("INDEX", 0);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        isPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (isPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("ERROR", e.toString());
        }
    }

    private void getDeviceLocation() {
        try {
            if (isPermissionGranted) {
                Task locationResult = locationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        lastKnownLocation = (Location) task.getResult();
                        LatLng latLng = new LatLng(lastKnownLocation.getLatitude(),
                                lastKnownLocation.getLongitude());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                latLng, DEFAULT_ZOOM));
                        Marker myMarker = MapsActivity.this.myMarker;
                        if (myMarker == null) {
                            myMarker = googleMap.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    .title(getString(R.string.title_marker_my_location)));
                            MapsActivity.this.myMarker = myMarker;
                        } else {
                            myMarker.setPosition(latLng);
                        }
                        calculateRoute(latLng, listOfJobs.getWareHouse().getLocation(), jobs);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("ERROR", e.toString());
        }
    }

    private void calculateRoute(LatLng origin, LatLng lastDestination, List<Job> jobs) {
        if (jobs.size() > 0) {
            ArrayList<LatLng> params = new ArrayList<>();
            params.add(origin);
            params.add(lastDestination);
            for (Job job : jobs) {
                params.add(job.getLocation());
            }
            new RouteRequester(MapHelper.RouteOrder.OPTIMIZE, this.getString(R.string.google_maps_key))
                    .execute(params.toArray(new LatLng[params.size()]));
        }
    }

    private void addMarker(Locator locator) {
        LatLng latLng = locator.getLocation();
        String title = locator.getDescription();
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(title).draggable(true);
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(locator);
    }

    private ListOfJobs getJobsFromIntent() {
        try {
            ListOfJobs jobs = getIntent().getParcelableExtra("JOBS");
            return jobs;
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        return null;
    }

    private interface OnLongPressListener {
        void onLongPress(Marker marker);
    }

    private class RouteRequester extends AsyncTask<LatLng, Integer, List<LatLng>> {

        private final MapHelper.RouteOrder order;
        private final String apiKey;

        RouteRequester(MapHelper.RouteOrder order, String apiKey) {
            this.order = order;
            this.apiKey = apiKey;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle(R.string.title_calculating_route);
            progressDialog.setMessage(getString(R.string.message_calculating_route));
            progressDialog.setIndeterminate(false);
            progressDialog.show();
            publishProgress(0);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (progressDialog != null) {
                progressDialog.setProgress(values[0]);
            }
        }

        @Override
        protected List<LatLng> doInBackground(LatLng... latLngs) {
            publishProgress(15);
            LatLng origin = latLngs[0];
            LatLng lastDestination = latLngs[1];
            List<LatLng> destinations = Arrays.asList(latLngs).subList(2, latLngs.length);
            publishProgress(30);
            try {
                JSONObject result = MapHelper.requestRoute(origin, lastDestination, destinations, order, apiKey);
                publishProgress(55);
                List<LatLng> polyLine = MapHelper.parseJsonRoute(result);
                List<Integer> routeOrder = MapHelper.getWayPointsOrder(result);
                publishProgress(70);
                if (routeOrder.size() == jobs.size()) {
                    List<Job> jobsClone = new ArrayList<>();
                    jobsClone.addAll(jobs);
                    jobs.clear();
                    publishProgress(85);
                    for (Integer order : routeOrder) {
                        jobs.add(jobsClone.get(order));
                    }
                }
                publishProgress(100);
                return polyLine;
            } catch (IOException | JSONException e) {
                Log.e("REQUEST ERROR", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<LatLng> result) {
            if (result != null) {
                if (result.size() > 2) {
                    PolylineOptions lineOptions;
                    lineOptions = new PolylineOptions();
                    lineOptions.addAll(result);
                    lineOptions.width(15);
                    lineOptions.color(getColor(R.color.colorPrimary));
                    MapsActivity.this.googleMap.addPolyline(lineOptions);
                } else showFailedGetRouteMessage();
            }
            if (result == null) showFailedGetRouteMessage();
            if (progressDialog != null) progressDialog.dismiss();
        }

        private void showFailedGetRouteMessage() {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.message_failed_get_route, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
