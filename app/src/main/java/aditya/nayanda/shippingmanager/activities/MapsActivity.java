package aditya.nayanda.shippingmanager.activities;

import android.app.ProgressDialog;
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
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
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
import java.util.Collections;
import java.util.List;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activities.helper.ActivityHelper;
import aditya.nayanda.shippingmanager.activities.helper.MapHelper;
import aditya.nayanda.shippingmanager.fragments.dialog.JobDetailsDialogFragment;
import aditya.nayanda.shippingmanager.model.Job;
import aditya.nayanda.shippingmanager.model.Receiver;
import aditya.nayanda.shippingmanager.util.Utilities;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static boolean isPermissionGranted;
    private final ArrayList<Job> jobs = new ArrayList<>();
    float DEFAULT_ZOOM = 14;
    private GoogleMap googleMap;
    private GeoDataClient geoDataClient;
    private PlaceDetectionClient placeDetectionClient;
    private FusedLocationProviderClient locationProviderClient;
    private Location lastKnownLocation;
    private Marker myMarker;
    private ProgressDialog progressDialog;

    private static void setOnLongPressListener(GoogleMap googleMap, OnLongPressListener onLongPressListener) {

        //Simulate long click
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Job job = (Job) marker.getTag();
                if (job == null) return;
                marker.setPosition(job.getLocation());
                onLongPressListener.onLongPress(marker);
                marker.showInfoWindow();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Job job = (Job) marker.getTag();
                if (job == null) return;
                marker.setPosition(job.getLocation());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Job job = (Job) marker.getTag();
                if (job == null) return;
                marker.setPosition(job.getLocation());
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView actionBarTitle = ActivityHelper.setToCustomActionBar(this);
        actionBarTitle.setText(R.string.title_activity_maps);

        setContentView(R.layout.activity_maps);
        Collections.addAll(jobs, getJobsFromIntent());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geoDataClient = Places.getGeoDataClient(this, null);
        placeDetectionClient = Places.getPlaceDetectionClient(this, null);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        findViewById(R.id.button_my_location).setOnClickListener(view -> getDeviceLocation());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        setMarkerListener(googleMap);

        if (jobs.size() == 0) {
            Collections.addAll(jobs, getJobsFromIntent());
        }
        for (Job job : jobs) {
            addMarker(job);
        }
        if (jobs.size() > 0) {
            findViewById(R.id.button_start_navigation).setOnClickListener(view -> {
                Uri gMapIntentUri = MapHelper.createGoogleMapRouteIntentUri(jobs.get(0).getLocation());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gMapIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            });
        }
        getLocationPermission();
        if (isPermissionGranted) getDeviceLocation();

    }

    private void setMarkerListener(GoogleMap googleMap) {
        setOnLongPressListener(googleMap, marker -> {
            Job thisJob = (Job) marker.getTag();
            if (thisJob == null) return;
            FragmentManager fragmentManager = MapsActivity.this.getSupportFragmentManager();
            JobDetailsDialogFragment dialogFragment = JobDetailsDialogFragment.newInstance(0.9f, thisJob);
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
        mainIntent.putExtra("JOBS", jobs);
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
                        calculateRoute(latLng, jobs);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("ERROR", e.toString());
        }
    }

    private void calculateRoute(LatLng origin, List<Job> jobs) {
        if (jobs.size() > 0) {
            ArrayList<LatLng> params = new ArrayList<>();
            params.add(origin);
            for (Job job : jobs) {
                params.add(job.getLocation());
            }
            new RouteRequester(MapHelper.RouteOrder.OPTIMIZE, this.getString(R.string.google_maps_key))
                    .execute(params.toArray(new LatLng[params.size()]));
        }
    }

    private void addMarker(Job job) {
        LatLng latLng = job.getLocation();
        Receiver receiver = job.getReceiver();
        String title = receiver.getFirstName() + " " + receiver.getLastName();
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(title).draggable(true);
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(job);
    }

    private Job[] getJobsFromIntent() {
        try {
            Job[] jobs = Utilities.castParcelableToJobs(getIntent().getParcelableArrayExtra("JOBS"));
            return jobs;
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        return new Job[0];
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
            List<LatLng> destinations = Arrays.asList(latLngs).subList(1, latLngs.length);
            publishProgress(30);
            try {
                JSONObject result = MapHelper.requestRoute(origin, destinations, order, apiKey);
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
