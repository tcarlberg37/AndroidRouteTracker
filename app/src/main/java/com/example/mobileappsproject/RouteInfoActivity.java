package com.example.mobileappsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mobileappsproject.Route.RouteContent;

public class RouteInfoActivity extends FragmentActivity implements MapFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);

        final RouteContent.Route route = getIntent().getExtras() == null ? null : (RouteContent.Route)getIntent().getExtras().getSerializable("route");
        TextView txtRouteName = findViewById(R.id.txtRouteName);
        final TextView txtTags = findViewById(R.id.txtTags);
        final TextView txtDate = findViewById(R.id.txtDate);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);

        if (route != null){
            txtRouteName.setText(route.route_name);
            txtTags.setText(route.tags);
            txtDate.setText(route.date);
            ratingBar.setRating(route.rating);
        }

        Button btnEditRoute = findViewById(R.id.btnEditRoute);

        btnEditRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditRouteInfoActivity.class);
                i.putExtra("route", route);
                startActivity(i);
            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
