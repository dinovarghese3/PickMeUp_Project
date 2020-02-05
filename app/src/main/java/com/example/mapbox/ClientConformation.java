package com.example.mapbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.mapbox.Adapter.MyRiderAdapter;
import com.example.mapbox.JoinRide.JoinRideActivity;
import com.example.mapbox.model.Request_model;
import com.example.mapbox.webservice.Apiclient;
import com.example.mapbox.webservice.Apiinterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientConformation extends AppCompatActivity {
    RecyclerView myride;
    List<Request_model> ride;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_conformation);
        sharedPreferences =getSharedPreferences("logindata", Context.MODE_PRIVATE);

        myride = findViewById(R.id.notirecycle);
        myride.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ride = new ArrayList<>();
        viewfed();
    }
    private void viewfed() {
        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<List<Request_model>> call = apiinterface.getMyride("myRides",sharedPreferences.getString("uid",""));
        call.enqueue(new Callback<List<Request_model>>() {
            @Override
            public void onResponse(Call<List<Request_model>> call, Response<List<Request_model>> response) {
                Log.d("@@@",response.body().get(0).getAddress());
                ride = response.body();
                MyRiderAdapter mya = new MyRiderAdapter(getApplicationContext(), ride);
                myride.setAdapter(mya);
            }

            @Override
            public void onFailure(Call<List<Request_model>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), JoinRideActivity.class));finish();
    }
}
