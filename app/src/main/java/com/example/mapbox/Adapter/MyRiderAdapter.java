package com.example.mapbox.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapbox.ClientConformation;
import com.example.mapbox.JoinRide.Payment;
import com.example.mapbox.R;
import com.example.mapbox.model.Login_Model;
import com.example.mapbox.model.Request_model;
import com.example.mapbox.webservice.Apiclient;
import com.example.mapbox.webservice.Apiinterface;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyRiderAdapter extends RecyclerView.Adapter<MyRiderAdapter.MyviewHolder> {
    Context context;
    List<Request_model> ride;
    AlertDialog.Builder builder;

    public MyRiderAdapter(Context context, List<Request_model> ride) {
        this.context = context;
        this.ride = ride;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_rider, parent, false);

        return new MyRiderAdapter.MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRiderAdapter.MyviewHolder holder, int position) {
        final Request_model p = ride.get(position);
        holder.name.setText("Name\t:\t" + p.getName());
        holder.email.setText("Email\t:\t" + p.getEmail());
        holder.phone.setText("Phone\t:\t" + p.getPhone());
        holder.address.setText("Address\t:\t" + p.getAddress());
        // holder.age.setText("Age\t:\t" + p.getAge());
        holder.vno.setText("Vechicle Number\t:\t" + p.getVno());
        holder.bname.setText("Vehicle Name\t:\t" + p.getVname());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(p.getPhoto(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.img.setImageBitmap(decodedImage);
        if (p.getRstatus().equals("0")) {
            holder.status.setText("Status\t:\t" + "Waiting for conformation");
        } else if (p.getRstatus().equals("1")) {
            holder.status.setText("Status\t:\t" + "Request has been Conformed");
        } else if (p.getRstatus().equals("3")) {
            holder.status.setText("Status\t:\t" + "Ride Completed");
        }


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences sharedPreferences = context.getSharedPreferences("logindata", Context.MODE_PRIVATE);
                final String uid = sharedPreferences.getString("uid", "");
                final String rid = p.getUid();
               // Toast.makeText(context, "rid=" + rid, Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(v.getRootView().getContext());
                dialog.setContentView(R.layout.customupdate);
                dialog.setTitle("Title...");
                final RatingBar rat1;
                Button rat2,pay;
                rat1 = dialog.findViewById(R.id.rat1);
                rat2 = dialog.findViewById(R.id.rat2);
                pay = dialog.findViewById(R.id.pay);
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sp=context.getSharedPreferences("payment",Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed=sp.edit();
                        ed.putString("uid",uid);
                        ed.putString("rid",rid);
                        ed.putString("cno",p.getCno());
                        ed.putString("cvv",p.getCvv());
                        ed.putString("pin",p.getPin());
                        ed.putString("balance",p.getBalance());
                        ed.commit();
                        Intent i=new Intent(context, Payment.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                });
                rat2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Float rating = rat1.getRating();
                        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
                        Call<Login_Model> call = apiinterface.sendRating("sendRating", uid,rid,String.valueOf(rating));
                        call.enqueue(new Callback<Login_Model>() {
                            @Override
                            public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                                Log.d("@@", response.body().getMessage());
                                Toast.makeText(context, response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<Login_Model> call, Throwable t) {

                            }
                        });
                    }
                });

                dialog.show();


            }
        });
    }


    @Override
    public int getItemCount() {
        return ride.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView name, age, address, email, phone, vno, bname, status;
        ImageView img;
        CardView card;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.r1name);
            age = itemView.findViewById(R.id.r1age);
            address = itemView.findViewById(R.id.r1address);
            email = itemView.findViewById(R.id.r1email);
            phone = itemView.findViewById(R.id.r1phone);
            vno = itemView.findViewById(R.id.r1vno);
            bname = itemView.findViewById(R.id.r1bname);
            img = itemView.findViewById(R.id.r1img);
            status = itemView.findViewById(R.id.r1status);
            card = itemView.findViewById(R.id.r1card);

        }
    }
}
