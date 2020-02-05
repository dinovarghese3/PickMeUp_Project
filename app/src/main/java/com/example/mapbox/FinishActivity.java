package com.example.mapbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mapbox.model.Login_Model;
import com.example.mapbox.webservice.Apiclient;
import com.example.mapbox.webservice.Apiinterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinishActivity extends AppCompatActivity {
     EditText num;
    Button seat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        final SharedPreferences sp=getSharedPreferences("finish", Context.MODE_PRIVATE);



        num = findViewById(R.id.num);
        seat = findViewById(R.id.seat);
        seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
                final Call<Login_Model> call = apiinterface.finishride("finishride",num.getText().toString(), sp.getString("uid",""),sp.getString("reqid",""));
                call.enqueue(new Callback<Login_Model>() {
                    @Override
                    public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getApplicationContext(), RiderNotification.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    }

                    @Override
                    public void onFailure(Call<Login_Model> call, Throwable t) {

                    }
                });
            }
        });
    }
}
