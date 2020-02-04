package com.example.mapbox.JoinRide;

import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapbox.Adapter.RiderAdapter;
import com.example.mapbox.CreateRide;
import com.example.mapbox.DecisionActivity;
import com.example.mapbox.LocationMonitoringService;
import com.example.mapbox.LoginActivity;
import com.example.mapbox.R;
import com.example.mapbox.RiderProfile;
import com.example.mapbox.model.NearestModel;
import com.example.mapbox.webservice.Apiclient;
import com.example.mapbox.webservice.Apiinterface;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinRideActivity extends FragmentActivity implements OnMapReadyCallback {
    String formattedDate;
    String latitude,longitude;
    private GoogleMap mMap;
    RecyclerView rc;
    private static final String TAG = JoinRideActivity.class.getSimpleName();
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    Marker now;
    private int mYear, mMonth, mDay, mHour, mMinute;
    double slatitude1, slongitude1, dlatitude, dlongitude;
    float km;
    /**
     * Code used in requesting runtime permissions.
     */
    List<NearestModel> rlist;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    EditText from, to;
    FloatingActionButton log, prof, noti;

    private boolean mAlreadyStartedService = false;
    private TextView mMsgView;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_ride);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        rlist = new ArrayList<>();
        rc=findViewById(R.id.riderrecycle);
        GridLayoutManager gg=new GridLayoutManager(this,2);
        rc.setLayoutManager(gg);
        initApp();
    }
    private void initApp() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        searchNearestRiders();
        SharedPreferences sp1 = getSharedPreferences("userlocation", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed1 = sp1.edit();
        ed1.putString("tripstatus", "started");
        ed1.commit();
//        from = findViewById(R.id.sorce);
//        to = findViewById(R.id.dest);
     //   search = findViewById(R.id.search);
        log = findViewById(R.id.logout);
        prof = findViewById(R.id.prof);
        noti = findViewById(R.id.Noti);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("logindata", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferences.edit();
                ed.putString("uid", "");
                ed.commit();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RiderProfile.class));
                finish();
            }
        });
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateRide.class));
                finish();
            }
        });
    }

    private void searchNearestRiders() {
        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<List<NearestModel>> call = apiinterface.searchnear("searchneNearRiders", latitude, longitude, formattedDate);
        call.enqueue(new Callback<List<NearestModel>>() {
            @Override
            public void onResponse(Call<List<NearestModel>> call, Response<List<NearestModel>> response) {
                int i;
                rlist = response.body();
                Log.d("@@@@@",rlist.get(0).getClat()+"");
                for( i=0;i<rlist.size();i++){
                    LatLng latLng = new LatLng(Double.parseDouble(rlist.get(i).getDlat().trim()), Double.parseDouble(rlist.get(i).getDlon().trim()));
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(rlist.get(i).getDestination())
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))).showInfoWindow();


                }
                RiderAdapter ra=new RiderAdapter(getApplicationContext(),rlist);
                rc.setAdapter(ra);

            }

            @Override
            public void onFailure(Call<List<NearestModel>> call, Throwable t) {

            }
        });
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
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                        //  Toast.makeText(userHome.this, latitude + longitude + "", Toast.LENGTH_SHORT).show();
                        if (latitude != null && longitude != null) {

                            LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(5).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                            Log.d("@@",latitude);
//                            Log.d("@@",longitude);
//                          //  mMap.clear();
//                            mMap.addMarker(new MarkerOptions()
//                                    .position(latLng)
//                                    .title("You are here")
//                                    .icon(BitmapDescriptorFactory
//                                            .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))).showInfoWindow();
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DecisionActivity.class));finish();
    }
}
