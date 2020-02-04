package com.example.mapbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mapbox.JoinRide.JoinRideActivity;
import com.example.mapbox.model.Profile_pojo;
import com.example.mapbox.webservice.Apiclient;
import com.example.mapbox.webservice.Apiinterface;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiderProfile extends AppCompatActivity {
    ImageView image;
    TextView name, email, phone, address, common;
    RatingBar rat;
    Button req;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_profile);
        SharedPreferences sharedPreferences = getSharedPreferences("logindata", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");
        image = findViewById(R.id.image);
        name = findViewById(R.id.Name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.addressView);
        common = findViewById(R.id.common);
        rat = findViewById(R.id.ratingBar);
        req = findViewById(R.id.Send);
        getProfile();

    }

    public void getProfile() {
        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<Profile_pojo> call = apiinterface.getprofile("myprofile", uid);
       call.enqueue(new Callback<Profile_pojo>() {
           @Override
           public void onResponse(Call<Profile_pojo> call, Response<Profile_pojo> response) {
               Profile_pojo p=response.body();
               name.setText("Name\t:\t"+p.getName());
               email.setText("Email\t:\t"+p.getEmail());
               phone.setText("Phone\t:\t"+p.getPhone());
               address.setText("Address\t:\t"+p.getAddress());
               if(p.getUtype().equals("Rider")) {
                   common.setVisibility(View.VISIBLE);
                   rat.setVisibility(View.VISIBLE);
                   common.setText("Vehicle Name\t:\t"+p.getVname()+"\n\n"+
                           "Vehicle Number\t:\t"+p.getVno()+"\n\nSeat Capacity\t:\t"+p.getSeat());
               }
               ByteArrayOutputStream baos = new ByteArrayOutputStream();
               byte[] imageBytes = baos.toByteArray();
               imageBytes = Base64.decode(p.getPhoto(), Base64.DEFAULT);
               Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
               image.setImageBitmap(decodedImage);

           }

           @Override
           public void onFailure(Call<Profile_pojo> call, Throwable t) {

           }
       });
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sp = getSharedPreferences("udata", Context.MODE_PRIVATE);
        if(sp.getString("ustatus","").equals("create")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        else{
                startActivity(new Intent(getApplicationContext(), JoinRideActivity.class));
                finish();
            }
    }
}
