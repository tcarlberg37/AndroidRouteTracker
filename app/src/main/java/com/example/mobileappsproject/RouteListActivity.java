package com.example.mobileappsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RouteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);

        Button btnHome = findViewById(R.id.btnHomePage);
        final Button btnRoute1 = findViewById(R.id.btnRoute1);
        final Button btnRoute2 = findViewById(R.id.btnRoute2);
        final Button btnRoute3 = findViewById(R.id.btnRoute3);
        final Button btnRoute4 = findViewById(R.id.btnRoute4);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        btnRoute1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RouteInfoActivity.class);
                String name = btnRoute1.getText().toString();
                i.putExtra("route_name", name);
                startActivity(i);
            }
        });

        btnRoute2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RouteInfoActivity.class);
                String name = btnRoute2.getText().toString();
                i.putExtra("route_name", name);
                startActivity(i);
            }
        });

        btnRoute3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RouteInfoActivity.class);
                String name = btnRoute3.getText().toString();
                i.putExtra("route_name", name);
                startActivity(i);
            }
        });

        btnRoute4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RouteInfoActivity.class);
                String name = btnRoute4.getText().toString();
                i.putExtra("route_name", name);
                startActivity(i);
            }
        });
    }
}
