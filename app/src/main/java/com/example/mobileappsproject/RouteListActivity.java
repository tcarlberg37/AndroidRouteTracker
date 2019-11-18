package com.example.mobileappsproject;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mobileappsproject.Route.RouteContent;

public class RouteListActivity extends FragmentActivity implements RouteFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);

        RouteDbHelper dbHelper = new RouteDbHelper(this);
        RouteContent.resetRoutes();
        Cursor cursor = dbHelper.getRoutes(""); // "" to get all Routes, "route_name" to get one route
        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(RouteContract.RouteEntity._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(RouteContract.RouteEntity.COLUMN_ROUTE_NAME));
            float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(RouteContract.RouteEntity.COLUMN_RATING));
            String tags = cursor.getString(cursor.getColumnIndexOrThrow(RouteContract.RouteEntity.COLUMN_TAGS));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(RouteContract.RouteEntity.COLUMN_DATE));

            RouteContent.Route route = new RouteContent.Route(id, name, rating, tags, date);
            RouteContent.ROUTES.add(route);
        }
        cursor.close();

        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RouteTrackingActivity.class);
                startActivityForResult(i, 1);
            }
        });

        Button btnAboutUs = findViewById(R.id.btnAboutUs);
        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AboutUsActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onListFragmentInteraction(RouteContent.Route item) {
        Intent i = new Intent(this, RouteInfoActivity.class);
        i.putExtra("route", item);
        //startActivity(i);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                FragmentManager manager = getSupportFragmentManager();
                RouteFragment fragment = (RouteFragment) manager.findFragmentById(R.id.fragmentList);
                fragment.updateView();
            }
        }
    }
}
