package com.example.mapbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapbox.model.Login_Model;
import com.example.mapbox.webservice.Apiclient;
import com.example.mapbox.webservice.Apiinterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView signup;
    EditText uname, pass;
    Button btnlog;
String fcm_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent serviceIntent = new Intent(getApplicationContext(), LocationMonitoringService.class);
            ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);

        } else {
            Intent intent1 = new Intent(getApplicationContext(), LocationMonitoringService.class);
            startService(intent1);

        }
        getFirebasetoken();
        SharedPreferences sharedPreferences = getSharedPreferences("logindata", Context.MODE_PRIVATE);
       if(sharedPreferences.getString("uid","")!="") {
           startActivity(new Intent(getApplicationContext(), DecisionActivity.class));
           finish();
       }
        signup = findViewById(R.id.signUp_text);
        uname = findViewById(R.id.emailID);
        pass = findViewById(R.id.pass);
        btnlog = findViewById(R.id.signin);
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uname.getText().toString().isEmpty()){
                    uname.setError("please enter a valid email");
                }
                if(pass.getText().toString().isEmpty()){
                    pass.setError("please enter a valid Password");
                }
                else {
                    startLogin();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }

    private void startLogin() {
        final ProgressDialog progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Checking login..");
        progressDoalog.setTitle("Please wait");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();

        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<Login_Model> call = apiinterface.getlogindata("login", uname.getText().toString(), pass.getText().toString());
       call.enqueue(new Callback<Login_Model>() {
           @Override
           public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
               SharedPreferences sharedPreferences = getSharedPreferences("logindata", Context.MODE_PRIVATE);
               SharedPreferences.Editor ed = sharedPreferences.edit();
               ed.putString("uid", response.body().getUid());
               ed.putString("utype", response.body().getUtype());
               ed.commit();
               if (response.body().getMessage().equals("Login success")) {
                   startActivity(new Intent(getApplicationContext(), DecisionActivity.class));
                   finish();
                   //

               }
               else{
                   Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(Call<Login_Model> call, Throwable t) {

           }
       });
       progressDoalog.dismiss();
    }

    private void getFirebasetoken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {

                            fcm_id=null;
                            return;
                        }
                        String token = task.getResult().getToken();
                        fcm_id=token;
                        storeRegIdInPrefs(fcm_id);
                        Log.d("fcmid", ""+fcm_id);
                    }
                });
    }
    public void storeRegIdInPrefs(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("token", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
}
