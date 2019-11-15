package com.example.mobileappsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditRouteInfoActivity extends FragmentActivity implements MapFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route_info);

        Bundle route_info = getIntent().getExtras();
        String route_name = route_info.getString("route_name");
        String tags = route_info.getString("tags");
        String date = route_info.getString("date");

        TextView txtRouteName = findViewById(R.id.txtRouteName);
        EditText editRouteName = findViewById(R.id.editRouteName);
        EditText editTags = findViewById(R.id.editTags);
        EditText editDate = findViewById(R.id.editDate);

        txtRouteName.setText(route_name);
        editRouteName.setText(route_name);
        editTags.setText(tags);
        editDate.setText(date);

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect back to the home page once the edits are saved
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
