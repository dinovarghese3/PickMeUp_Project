package com.example.mapbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.example.mapbox.model.Login_Model;
import com.example.mapbox.webservice.Apiclient;
import com.example.mapbox.webservice.Apiinterface;
import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class CreateRide extends AppCompatActivity implements OnMapReadyCallback {
    TextView cloc, dloc, cdate, ctime;
    Button search;
    SharedPreferences sp1;
    String dlat, dlon;
    SharedPreferences.Editor ed;
    private int mYear, mMonth, mDay, mHour, mMinute;
    double slatitude1, slongitude1, dlatitude, dlongitude;
    float km;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    SharedPreferences sharedPreferences;
    SharedPreferences sp;


    private MapView mapView;
    private MapboxMap mapboxMap;
    private CarmenFeature home;
    private CarmenFeature work;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1Ijoibml0aGluYmFidTEyMyIsImEiOiJjazY2OHloancweW9uM2RtcDk2aWh6cHVhIn0.8XYmWjda9Mdj6O4BYxjvKg");
        setContentView(R.layout.activity_create_ride);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        cloc = findViewById(R.id.cloc);
        dloc = findViewById(R.id.dloc);
        cdate = findViewById(R.id.tdate);
        ctime = findViewById(R.id.ttime);
        search = findViewById(R.id.Route);
        sp = getSharedPreferences("loc", Context.MODE_PRIVATE);
        sp1 = getSharedPreferences("doc", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("logindata", Context.MODE_PRIVATE);

        ed = sp1.edit();
        cloc.setText(sp.getString("address", ""));
        // Toast.makeText(this, sp.getString("address", ""), Toast.LENGTH_SHORT).show();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cloc.getText().toString().isEmpty()){
                    cloc.setError("please enter your source");
                }
                if(dloc.getText().toString().isEmpty()){
                    dloc.setError("please enter your destination");
                }
                if(cdate.getText().toString().isEmpty()){
                    cdate.setError("please pick your ride date");
                }
                if(ctime.getText().toString().isEmpty()){
                    ctime.setError("please pick your ride time");
                }
                else {
                    dloc.setError(null);
                    cdate.setError(null);
                    ctime.setError(null);
                    startRide();
                }
            }
        });
        dloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSearchFab();
//                Intent intent = new PlacePicker.IntentBuilder()
//                        .accessToken(Mapbox.getAccessToken())
//                        .placeOptions(
//                                PlacePickerOptions.builder()
//                                        .statingCameraPosition(
//                                                new CameraPosition.Builder()
//                                                        .target(new LatLng(21.7679, 78.8718))
//                                                        .zoom(3)
//                                                        .build())
//                                        .build())
//                        .build(CreateRide.this);
//                startActivityForResult(intent, 1);
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
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
             //   initSearchFab();

                addUserLocations();

                // Add the symbol layer icon to map for future use
                style.addImage(symbolIconId, BitmapFactory.decodeResource(
                        CreateRide.this.getResources(), R.drawable.marker));

                // Create an empty GeoJSON source using the empty feature collection
                setUpSource(style);

                // Set up a new symbol layer for displaying the searched location's feature coordinates
                setupLayer(style);
            }
        });
    }
    private void addUserLocations() {
        home = CarmenFeature.builder().text("Mapbox SF Office")
                .geometry(Point.fromLngLat(-122.3964485, 37.7912561))
                .placeName("50 Beale St, San Francisco, CA")
                .id("mapbox-sf")
                .properties(new JsonObject())
                .build();

        work = CarmenFeature.builder().text("Mapbox DC Office")
                .placeName("740 15th Street NW, Washington DC")
                .geometry(Point.fromLngLat(-77.0338348, 38.899750))
                .id("mapbox-dc")
                .properties(new JsonObject())
                .build();
    }

    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }

    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[] {0f, -8f})
        ));
    }
    private void startRide() {
        final ProgressDialog progressDoalog = new ProgressDialog(CreateRide.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Make your trip..");
        progressDoalog.setTitle("Please wait");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<Login_Model> call = apiinterface.getRide("createride", cloc.getText().toString(),
                dloc.getText().toString(), cdate.getText().toString(), ctime.getText().toString()
                , sp.getString("clat", ""), sp.getString("clon", ""), dlat, dlon, sharedPreferences.getString("uid", ""));
        call.enqueue(new Callback<Login_Model>() {
            @Override
            public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                if (response.body().getMessage().equals("success")) {
                    Toast.makeText(CreateRide.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    finish();
                } else {
                    Toast.makeText(CreateRide.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login_Model> call, Throwable t) {

            }
        });
        progressDoalog.dismiss();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
////
//
//            //zoom map to find location
////            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
////            // String attributions = data.getData().getPath();
////
////
////            dloc.setText(feature.text());
////            // Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
////            try {
////                String location = feature.text();
////                Geocoder gc = new Geocoder(this);
////                List<Address> addresses = gc.getFromLocationName(location, 5); // get the found Address Objects
////
////                List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
////                for (Address a : addresses) {
////                    if (a.hasLatitude() && a.hasLongitude()) {
////                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
////                    }
////                }
////                Log.d("@@@@@", ll.size() + "");
////                if (ll.size() > 0) {
////                    dlat = ll.get(0).getLatitude() + "";
////                    dlon = ll.get(0).getLongitude() + "";
////                    ed.putString("lat", ll.get(0).getLatitude() + "");
////                    ed.putString("lon", ll.get(0).getLongitude() + "");
////                    ed.commit();
////                } else {
////                    AlertDialog.Builder builder;
////                    builder = new AlertDialog.Builder(this);
////
////                    //Setting message manually and performing action on button click
////                    builder.setMessage("Destination latlang not found.please try once again.")
////                            .setCancelable(false)
////                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
////                                public void onClick(DialogInterface dialog, int id) {
////
////                                    dialog.dismiss();
////                                }
////                            });
////
////                    //Creating dialog box
////                    AlertDialog alert = builder.create();
////                    //Setting the title manually
////                    alert.setTitle("Destination Not Found");
////                    alert.show();
////                }
//                // Toast.makeText(this, a.getLatitude()+a.getLongitude()+"", Toast.LENGTH_SHORT).show();
//
////            } catch (IOException e) {
////                // handle the exception
////            }
//            //   Toast.makeText(this, attributions, Toast.LENGTH_LONG).show();
//
//        }
//    }
    private void initSearchFab() {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.access_token))
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .addInjectedFeature(home)
                                .addInjectedFeature(work)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(CreateRide.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            dloc.setText(selectedCarmenFeature.text());
//            // Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
            try {
                String location = selectedCarmenFeature.text();
                Geocoder gc = new Geocoder(this);
                List<Address> addresses = gc.getFromLocationName(location, 5); // get the found Address Objects

                List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
                Log.d("@@@@@", ll.size() + "");
                if (ll.size() > 0) {
                    dlat = ll.get(0).getLatitude() + "";
                    dlon = ll.get(0).getLongitude() + "";
                    ed.putString("lat", ll.get(0).getLatitude() + "");
                    ed.putString("lon", ll.get(0).getLongitude() + "");
                    ed.commit();
                } else {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(this);

                    //Setting message manually and performing action on button click
                    builder.setMessage("Destination latlang not found.please try once again.")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Destination Not Found");
                    alert.show();
                }
                // Toast.makeText(this, a.getLatitude()+a.getLongitude()+"", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                // handle the exception
            }
            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

                    // Move map camera to the selected location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }
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

                            cdate.setText("0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                        }
                        if (monthOfYear < 10 && dayOfMonth >= 10) {
                            cdate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                        }
                        if (monthOfYear >= 10 && dayOfMonth < 10) {
                            cdate.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
