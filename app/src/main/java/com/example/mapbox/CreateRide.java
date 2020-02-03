package com.example.mapbox;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateRide extends AppCompatActivity {
    TextView cloc, dloc, cdate, ctime;
    Button search;
    SharedPreferences sp1;
    SharedPreferences.Editor ed;
    private int mYear, mMonth, mDay, mHour, mMinute;
    double slatitude1, slongitude1, dlatitude, dlongitude;
    float km;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);
        cloc = findViewById(R.id.cloc);
        dloc = findViewById(R.id.dloc);
        cdate = findViewById(R.id.tdate);
        ctime = findViewById(R.id.ttime);
        search = findViewById(R.id.Route);
        SharedPreferences sp = getSharedPreferences("loc", Context.MODE_PRIVATE);
        sp1 = getSharedPreferences("doc", Context.MODE_PRIVATE);
        ed = sp1.edit();
        cloc.setText(sp.getString("address", ""));
        Toast.makeText(this, sp.getString("address", ""), Toast.LENGTH_SHORT).show();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        dloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlacePicker.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken())
                        .placeOptions(
                                PlacePickerOptions.builder()
                                        .statingCameraPosition(
                                                new CameraPosition.Builder()
                                                        .target(new LatLng(21.7679, 78.8718))
                                                        .zoom(3)
                                                        .build())
                                        .build())
                        .build(CreateRide.this);
                startActivityForResult(intent, 1);
            }
        });
        cdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });
        ctime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiemPicker();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            // String attributions = data.getData().getPath();


                dloc.setText(feature.text());
               // Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
                try {
                    String location = feature.text();
                    Geocoder gc = new Geocoder(this);
                    List<Address> addresses = gc.getFromLocationName(location, 5); // get the found Address Objects

                    List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                    for (Address a : addresses) {
                        if (a.hasLatitude() && a.hasLongitude()) {
                            ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                        }
                    }
                    Log.d("@@@@@",ll.size()+"");
                    if(ll.size()>0){

                        ed.putString("lat", ll.get(0).getLatitude() + "");
                        ed.putString("lon", ll.get(0).getLongitude() + "");
                        ed.commit();
                        }else {

                        Toast.makeText(this, "please drag map and choose one place", Toast.LENGTH_SHORT).show();

                    }
                    // Toast.makeText(this, a.getLatitude()+a.getLongitude()+"", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    // handle the exception
                }
                //   Toast.makeText(this, attributions, Toast.LENGTH_LONG).show();

        }
    }

    private void datePicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateRide.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear < 10 && dayOfMonth < 10) {

                            cdate.setText("0"+dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                        }
                        if (monthOfYear < 10 && dayOfMonth >= 10) {
                            cdate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                        }
                        if (monthOfYear >= 10 && dayOfMonth < 10) {
                            cdate.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                        if (monthOfYear >= 10 && dayOfMonth >= 10) {
                            cdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                        //*************Call Time Picker Here ********************

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void tiemPicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateRide.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        ctime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
