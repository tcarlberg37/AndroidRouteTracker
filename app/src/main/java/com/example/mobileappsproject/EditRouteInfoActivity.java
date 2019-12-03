package com.example.mobileappsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mobileappsproject.Route.RouteContent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EditRouteInfoActivity extends FragmentActivity implements MapFragment.OnFragmentInteractionListener, OnMapReadyCallback {

    // A list of LatLng points from the database that we will use to create the polyline on the map
    List<LatLng> pointsList = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route_info);
        final RouteDbHelper dbHelper = new RouteDbHelper(this);

        // if route was passed in intent set it = route, else null
        final RouteContent.Route route = getIntent().getExtras() == null ? null : (RouteContent.Route)getIntent().getExtras().getSerializable("route");
        Log.d("route info", "ID: " + route.id + " Name: " + route.route_name);

        // load the Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Cursor pointsCursor = dbHelper.getPoints(route.id);
        if (pointsCursor.getCount() > 0) {
            while (pointsCursor.moveToNext()) {
                // Points table column order: id, route_id, timestamp, lat, long
                Log.d("points: ", pointsCursor.getString(1) + " lat: " + pointsCursor.getDouble(3) + " long: " + pointsCursor.getDouble(4));
                LatLng latLng = new LatLng(pointsCursor.getDouble(3), pointsCursor.getDouble(4));
                pointsList.add(latLng);
            }
        } else {
            Log.d("no points", "cursor found no points");
        }

        final TextView txtRouteName = findViewById(R.id.txtRouteName);
        final EditText editRouteName = findViewById(R.id.editRouteName);
        final EditText editTags = findViewById(R.id.editTags);
        final EditText editDate = findViewById(R.id.editDate);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);

        if (route != null){
            txtRouteName.setText(route.route_name);
            editRouteName.setText(route.route_name);
            editTags.setText(route.tags);
            editDate.setText(route.date);
            ratingBar.setRating(route.rating);        }


        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_name = txtRouteName.getText().toString();
                String new_name = editRouteName.getText().toString();
                float rating = ratingBar.getRating();
                String tags = editTags.getText().toString();
                String date = editDate.getText().toString();
                // route already created in RouteTrackingActivity so we just need to update
                dbHelper.updateRoute(old_name, new_name, rating, tags, date);
                // redirect back to the list page once the edits are saved
                Intent i = new Intent(v.getContext(), RouteListActivity.class);
                setResult(RESULT_OK);
                finish();
            }
        });

        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteRoute(route.route_name);
                dbHelper.deletePoints(route.id);
                Intent i = new Intent(v.getContext(), RouteListActivity.class);
                setResult(RESULT_OK);
                finish();
            }
        });

        FloatingActionButton btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Route Name: " + route.route_name + " Tags: " + route.tags
                                    + " Rating: " + route.rating + " Date: " + route.date);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // add the Route Polyline to the map
        Polyline polyline = googleMap.addPolyline(new PolylineOptions().color(Color.RED));
        polyline.setPoints(pointsList);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointsList.get(0), 10));
    }
}
