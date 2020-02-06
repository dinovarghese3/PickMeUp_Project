package com.example.mapbox;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mapbox.Adapter.RiderAdapter;
import com.example.mapbox.model.Login_Model;
import com.example.mapbox.model.NearestModel;
import com.example.mapbox.webservice.Apiclient;
import com.example.mapbox.webservice.Apiinterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackChildActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String pchid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_child);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        SharedPreferences sharedPreferences = getSharedPreferences("logindata", Context.MODE_PRIVATE);
        pchid = sharedPreferences.getString("pchid", "");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        searchNearestRiders();
    }

    private void searchNearestRiders() {
        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<Login_Model> call = apiinterface.mychild("mychild", pchid);
        call.enqueue(new Callback<Login_Model>() {
            @Override
            public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                Login_Model l = response.body();
                LatLng latLng = new LatLng(Double.parseDouble(l.getClat()), Double.parseDouble(l.getClon()));
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("your child is here")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            @Override
            public void onFailure(Call<Login_Model> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);

        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to close this application ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                          moveTaskToBack(true);
                      finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Logout");
        alert.show();
    }
}
