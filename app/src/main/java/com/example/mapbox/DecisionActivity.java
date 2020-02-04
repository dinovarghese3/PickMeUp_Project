package com.example.mapbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mapbox.JoinRide.JoinRideActivity;

public class DecisionActivity extends AppCompatActivity {
    Button create, join;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        create = findViewById(R.id.create_ride);
        join = findViewById(R.id.search_Ride);
        sp = getSharedPreferences("udata", Context.MODE_PRIVATE);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("ustatus", "create");
                ed.commit();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("ustatus", "join");
                ed.commit();
                startActivity(new Intent(getApplicationContext(), JoinRideActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
    }
}
