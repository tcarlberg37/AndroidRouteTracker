package com.example.mobileappsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobileappsproject.Route.RouteContent;
import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;

public class RouteTrackingActivity extends FragmentActivity implements MapFragment.OnFragmentInteractionListener, OnMapReadyCallback {

    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;
    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_tracking);

        // check for location permission, if not given, ask for it
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // load the Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final RouteDbHelper dbHelper = new RouteDbHelper(this);
        // first we must insert a route into the database so that when we insert Points to the table there is a
        // route_id that exists in the Routes table and can be referenced as a foreign key in the Points table);
        dbHelper.insertRoute("edit route name", (float) 0.0, "", Calendar.getInstance().getTime().toString());
        Cursor cursor = dbHelper.getID("edit route name");
        cursor.moveToNext();
        // get the Route's id in the db so that we have the foreign key to include when inserting Points
        final int route_id = cursor.getPosition();
        // create a Route object to pass to intent
        final RouteContent.Route route = new RouteContent.Route(route_id, "edit route name", (float) 0.0, "", Calendar.getInstance().getTime().toString());

        locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                    Log.d("locClient onSuccess", "locClient location received");
                }
            }
        });

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Log.d("locCallback onSuccess", "location received: Timestamp=" + location.getTime() + " Latitude=" + location.getLatitude() + " Longitude=" + location.getLongitude());
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                // add new point to the database referencing the route_id created when starting the activity
                dbHelper.insertPoint(route_id, location.getTime(), location.getLatitude(), location.getLongitude());
            }
        };

        Button btnStartTracking = findViewById(R.id.btnStartTracking);
        btnStartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTracking();
            }
        });

        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTracking();
                Intent i = new Intent(v.getContext(), EditRouteInfoActivity.class);
                i.putExtra("route", route);
                startActivity(i);
                finish();
            }
        });

        Button btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTracking();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // make the google map usable outside of this function
        gMap = googleMap;
        Log.d("message", "RouteTrackingActivity onMapReady launched");
        Task<Location> locationTask = locationClient.getLastLocation();
        // task will always fail if we try to access it before it is complete so wrap in OnCompleteListener
        locationTask.addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()){
                    Location location = task.getResult();
                    LatLng startLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    // set the map's starting position to GPS location when you first open the Activity
                    // zoom level goes from 2.0 (out) to 21.0 (in)
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                    gMap.addMarker(new MarkerOptions().position(startLocation).title("Starting Location"));
                    Log.d("location task passed", "location task passed");
                } else {
                    Log.d("location task failed", "location task failed");
                    // default to showing marker at GBC
                    LatLng gbc = new LatLng(43.6775069,-79.4121207); // Coordinates of GBC
                    gMap.addMarker(new MarkerOptions().position(gbc).title("George Brown College"));
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gbc, 15));
                }
            }
        });
    }

    private void startTracking() {
        LocationRequest lr = new LocationRequest();
        lr.setInterval(5000); // get location every 5 seconds
        lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationClient.requestLocationUpdates(lr, locationCallback, null);
    }

    private void stopTracking() {
        locationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onPause() {
        // stops location tracking if the user leaves the activity
        stopTracking();
        super.onPause();
    }

    /*
    @Override
    protected void onResume(){
        startTracking();
        super.onResume();
    }
    */
}
