package com.example.mapbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mapbox.Adapter.ViewrequestAdapter;
import com.example.mapbox.model.viewRequest_model;
import com.example.mapbox.webservice.Apiclient;
import com.example.mapbox.webservice.Apiinterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiderNotification extends AppCompatActivity {
    RecyclerView myride;
    List<viewRequest_model> ride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_notification);
        myride =findViewById(R.id.riderreq);
        myride.setLayoutManager(new LinearLayoutManager(this));
        ride = new ArrayList<>();
        viewfed();
    }
    private void viewfed() {
        final SharedPreferences sharedPreferences = getSharedPreferences("logindata", Context.MODE_PRIVATE);

        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<List<viewRequest_model>> call = apiinterface.getRequest("getRequests",sharedPreferences.getString("uid",""));
        call.enqueue(new Callback<List<viewRequest_model>>() {
            @Override
            public void onResponse(Call<List<viewRequest_model>> call, Response<List<viewRequest_model>> response) {
                ride = response.body();
                ViewrequestAdapter mya = new ViewrequestAdapter(getApplicationContext(), ride);
                myride.setAdapter(mya);
            }

            @Override
            public void onFailure(Call<List<viewRequest_model>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));finish();
    }
}
