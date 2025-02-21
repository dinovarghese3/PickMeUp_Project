package com.example.mapbox;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mapbox.model.Login_Model;
import com.example.mapbox.webservice.Apiclient;
import com.example.mapbox.webservice.Apiinterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationMonitoringService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    static GoogleApiClient mLocationClient = null;
    LocationRequest mLocationRequest = new LocationRequest();
    String lat1, lon1, type;

    long oldTime = 0;
    private Timer timer, timer1;
    private TimerTask timerTask, timerTask1;
    String driver_Id, oldlat = null, oldlon = null;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {

        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        mLocationRequest.setInterval(Constants.LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.FASTEST_LOCATION_INTERVAL);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.setPriority(priority);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient.connect();

        startTimer();
        startTimer1();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
            Notification notification = new NotificationCompat.Builder(LocationMonitoringService.this, CHANNEL_ID)
                    .setContentTitle("MyRide App is tracking you")
                    .setContentText("keep signed in")
                    .setSmallIcon(R.drawable.ic_sound_on)
                    .setSound(null)
                    .build();
            startForeground(1, notification);
        }
        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

        Log.d(TAG, "Connected to Google API");
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");


        if (location != null) {
            Log.d(TAG, "== location != null");
            //Send result to activities
            sendMessageToUI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

        }

    }

    private void sendMessageToUI(String lat, String lng) {
        lat1 = lat;
        lon1 = lng;
        Log.d(TAG, "Sending info...");
        //Toast.makeText(this, lat + lng + "", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);


    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 500, 500); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + lat1 + lon1);

//                   Toast.makeText(LocationMonitoringService.this,lat1+""+lon1, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void startTimer1() {
        //set a new Timer
        timer1 = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask1();
        //Date currentTime = Calendar.getInstance().getTime();
        //schedule the timer, to wake up every 5 minute

        timer1.schedule(timerTask1, 1000, 1000); //
    }

    public void initializeTimerTask1() {
        timerTask1 = new TimerTask() {
            public void run() {

                        SharedPreferences sp1 = getSharedPreferences("logindata", Context.MODE_PRIVATE);

                        if (sp1.getString("uid", "")!="") {
                            driverUpdation();

                        } else {
                            Log.d("bgloc", "id not given");
                        }



            }
        };
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");

    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        //calling broadcastreceiver to restart service

        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);
        sendBroadcast(broadcastIntent);

        //stoptimertask();
    }

    private void driverUpdation() {
        SharedPreferences sp1 = getSharedPreferences("logindata", Context.MODE_PRIVATE);

        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<Login_Model> call = apiinterface.locupdate("LocUpdateCommon", sp1.getString("uid",""),lat1,lon1);
        call.enqueue(new Callback<Login_Model>() {
            @Override
            public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                //Toast.makeText(LocationMonitoringService.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Login_Model> call, Throwable t) {

            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.setSound(null, null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}