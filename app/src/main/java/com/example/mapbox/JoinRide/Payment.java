package com.example.mapbox.JoinRide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mapbox.ClientConformation;
import com.example.mapbox.CreateRide;
import com.example.mapbox.MainActivity;
import com.example.mapbox.R;
import com.example.mapbox.model.Login_Model;
import com.example.mapbox.model.getPayee;
import com.example.mapbox.webservice.Apiclient;
import com.example.mapbox.webservice.Apiinterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Payment extends AppCompatActivity {
EditText pamt,acno,cvv,pin,bal;
Button btnpay;
Double ded=0.0;
String pin1,cvv1;
     SharedPreferences sp;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        pamt=findViewById(R.id.payamt);
        acno=findViewById(R.id.cno);
        cvv=findViewById(R.id.cvv);
        pin=findViewById(R.id.pin);
        bal=findViewById(R.id.bal);
        btnpay=findViewById(R.id.ptnpay);
         sharedPreferences = getSharedPreferences("logindata", Context.MODE_PRIVATE);


        getPayee();
        sp=getSharedPreferences("payment", Context.MODE_PRIVATE);
     //   acno.setText(sp.getString("cno",""));
      //  bal.setText(sp.getString("balance",""));

        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pamt.getText().toString().isEmpty()){
                    pamt.setError("enter amount");
                }
                else if(cvv.getText().toString().isEmpty()||!cvv.getText().toString().equals(cvv1)||cvv.getText().toString().length()<3||cvv.getText().toString().length()>3){
                    cvv.setError("invalid cvv");
                }
                else if(pin.getText().toString().isEmpty()||!pin.getText().toString().equals(pin1)||pin.getText().toString().length()<4||cvv.getText().toString().length()>4){
                    pin.setError("invalid pin");
                }
                else if(Double.parseDouble(pamt.getText().toString())>Double.parseDouble(bal.getText().toString())){
                    Toast.makeText(Payment.this, "insuffficient balance", Toast.LENGTH_SHORT).show();
                }
                else{
                    ded=Double.parseDouble(bal.getText().toString())-Double.parseDouble(pamt.getText().toString());
                    startPayment();
                }
            }
        });
    }

    private void getPayee() {
        Toast.makeText(this, sharedPreferences.getString("uid","")+"", Toast.LENGTH_SHORT).show();
        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<getPayee> call = apiinterface.getpayee("getpayee", sharedPreferences.getString("uid",""));
        call.enqueue(new Callback<getPayee>() {
            @Override
            public void onResponse(Call<getPayee> call, Response<getPayee> response) {
                Toast.makeText(Payment.this, response.body().getCardnumber()+"", Toast.LENGTH_SHORT).show();
                acno.setText(response.body().getCardnumber());
                bal.setText(response.body().getBalance());
                cvv1=response.body().getCvv();
                pin1=response.body().getPin();
            }

            @Override
            public void onFailure(Call<getPayee> call, Throwable t) {

            }
        });
    }

    private void startPayment() {
        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<Login_Model> call = apiinterface.pay("payment", sp.getString("uid",""),
                sp.getString("rid",""), pamt.getText().toString(), ded+"");
        call.enqueue(new Callback<Login_Model>() {
            @Override
            public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                if (response.body().getMessage().equals("success")) {
                    Toast.makeText(Payment.this, "Payment has successfullly completed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ClientConformation.class));

                    finish();
                } else {
                    Toast.makeText(Payment.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login_Model> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ClientConformation.class));
        finish();
    }
}
