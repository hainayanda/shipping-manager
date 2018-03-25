package aditya.nayanda.shippingmanager.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
import com.google.android.gms.tasks.Task;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.fragments.dialog.JobDetailsDialogFragment;
import aditya.nayanda.shippingmanager.model.Job;
import aditya.nayanda.shippingmanager.model.Receiver;
import aditya.nayanda.shippingmanager.util.Utilities;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static boolean isPermissionGranted;
    float DEFAULT_ZOOM = 14;
    private GoogleMap googleMap;
    private GeoDataClient geoDataClient;
    private PlaceDetectionClient placeDetectionClient;
    private FusedLocationProviderClient locationProviderClient;
    private Job[] jobs;
    private Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        jobs = getJobsFromIntent();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geoDataClient = Places.getGeoDataClient(this, null);
        placeDetectionClient = Places.getPlaceDetectionClient(this, null);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(thisMarker -> {
            Job thisJob = (Job) thisMarker.getTag();
            if (thisJob == null) return false;
            FragmentManager fragmentManager = MapsActivity.this.getSupportFragmentManager();
            JobDetailsDialogFragment dialogFragment = JobDetailsDialogFragment.newInstance(0.9f, thisJob);
            dialogFragment.show(fragmentManager, "details_dialog");
            return true;
        });
        getLocationPermission();
        if (isPermissionGranted) getDeviceLocation();
        if (jobs == null) jobs = getJobsFromIntent();
        for (Job job : jobs) {
            addMarker(job);
        }
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
            Log.e("Exception: %s", e.getMessage());
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
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                latLng, DEFAULT_ZOOM));
                        googleMap.addMarker(new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .title(getString(R.string.title_marker_my_location)));
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void addMarker(Job job) {
        LatLng latLng = job.getLocation();
        Receiver receiver = job.getReceiver();
        String title = receiver.getFirstName() + " " + receiver.getLastName();
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(title);
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
}
