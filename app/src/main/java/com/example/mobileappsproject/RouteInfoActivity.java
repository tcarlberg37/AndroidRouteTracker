package com.example.mobileappsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RouteInfoActivity extends FragmentActivity implements MapFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);

        // set the title of the page to the name of the route they clicked on in the Route List
        final String route_name = getIntent().getExtras().getString("route_name");
        TextView txtRouteName = findViewById(R.id.txtRouteName);
        txtRouteName.setText(route_name);

        final TextView txtTags = findViewById(R.id.txtTags);
        final TextView txtDate = findViewById(R.id.txtDate);
        Button btnEditRoute = findViewById(R.id.btnEditRoute);

        btnEditRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditRouteInfoActivity.class);
                Bundle infoBundle = new Bundle();
                infoBundle.putString("route_name", route_name);
                infoBundle.putString("tags", txtTags.getText().toString());
                infoBundle.putString("date", txtDate.getText().toString());
                i.putExtras(infoBundle); // pass the bundle of key-value pairs to the intent for the edit route page
                startActivity(i);
            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
