package com.example.mobileappsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mobileappsproject.Route.RouteContent;

public class EditRouteInfoActivity extends FragmentActivity implements MapFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route_info);
        final RouteDbHelper dbHelper = new RouteDbHelper(this);

        // if route was passed in intent set it = route, else null
        final RouteContent.Route route = getIntent().getExtras() == null ? null : (RouteContent.Route)getIntent().getExtras().getSerializable("route");

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
            ratingBar.setRating(route.rating);
        }


        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_name = txtRouteName.getText().toString();
                String new_name = editRouteName.getText().toString();
                float rating = ratingBar.getRating();
                String tags = editTags.getText().toString();
                String date = editDate.getText().toString();
                if (route != null){ // if the route exists already, update
                    dbHelper.updateRoute(old_name, new_name, rating, tags, date);
                } else { // if route doesn't exist, insert new row
                    dbHelper.insert(new_name, rating, tags, date);
                }
                // redirect back to the list page once the edits are saved
                Intent i = new Intent(v.getContext(), RouteListActivity.class);
                // i.putExtra("reload", true);
                setResult(1);
                startActivity(i);
                finish();
            }
        });

        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteRoute(route.route_name); // 1 if row deleted, 0 if not
                Intent i = new Intent(v.getContext(), RouteListActivity.class);
                // i.putExtra("reload", true);
                setResult(1);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
