package com.example.mapbox.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapbox.FinishActivity;
import com.example.mapbox.MainActivity;
import com.example.mapbox.R;
import com.example.mapbox.RiderNotification;
import com.example.mapbox.ViewClientMap;
import com.example.mapbox.model.Login_Model;
import com.example.mapbox.model.viewRequest_model;
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

public class ViewrequestAdapter extends RecyclerView.Adapter<ViewrequestAdapter.MyviewHolder> {
    Context context;
    List<viewRequest_model> ride;
    AlertDialog.Builder builder;


    public ViewrequestAdapter(Context context, List<viewRequest_model> ride) {
        this.context = context;
        this.ride = ride;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_rider, parent, false);

        return new ViewrequestAdapter.MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewrequestAdapter.MyviewHolder holder, int position) {
        final viewRequest_model p = ride.get(position);
        holder.name.setText("Name\t:\t" + p.getName());
        holder.email.setText("Email\t:\t" + p.getEmail());
        holder.phone.setText("Phone\t:\t" + p.getPhone());
        holder.address.setText("Address\t:\t" + p.getAddress());

        holder.vno.setText("Source\t:\t" + p.getSource());
        holder.bname.setText("Destination\t:\t" + p.getDestination());
        if (p.getRstatus().equals("0")) {
            holder.age.setText("Status\t:\twaiting for Approval");
        } else if (p.getRstatus().equals("1")) {
            holder.age.setText("Status\t:\tRequest Approved");
        } else if (p.getRstatus().equals("2")) {
            holder.age.setText("Status\t:\tRequest Rejected");
        }
        else if (p.getRstatus().equals("3")) {
            holder.age.setText("Status\t:\tTrip Finished");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(p.getPhoto(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.img.setImageBitmap(decodedImage);
        holder.status.setVisibility(View.GONE);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences sharedPreferences = context.getSharedPreferences("logindata", Context.MODE_PRIVATE);
                final String uid = sharedPreferences.getString("uid", "");
                if (p.getRstatus().equals("0")) {
                    builder = new AlertDialog.Builder(v.getRootView().getContext());
                    builder.setMessage("What will you do this Request??")
                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int id) {
                                    Toast.makeText(context, p.getSeat() + "", Toast.LENGTH_SHORT).show();
                                    Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
                                    final Call<Login_Model> call = apiinterface.RequestDo("requestdo", p.getReqid(), "Accept", p.getToken(), uid);
                                    call.enqueue(new Callback<Login_Model>() {
                                        @Override
                                        public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                                            //Get the SmsManager instance and call the sendTextMessage method to send message

                                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(context, RiderNotification.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(i);

                                        }

                                        @Override
                                        public void onFailure(Call<Login_Model> call, Throwable t) {

                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
                                    final Call<Login_Model> call = apiinterface.RequestDo("requestdo", p.getReqid(), "Reject", p.getToken(), uid);
                                    call.enqueue(new Callback<Login_Model>() {
                                        @Override
                                        public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(context, RiderNotification.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(i);

                                        }

                                        @Override
                                        public void onFailure(Call<Login_Model> call, Throwable t) {

                                        }
                                    });

                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Request Conformation");
                    alert.show();
                } else if (p.getRstatus().equals("2")) {
                    builder = new AlertDialog.Builder(v.getRootView().getContext());
                    builder.setMessage("This Request is Rejected")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }

                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Request Conformation");
                    alert.show();
                } else if (p.getRstatus().equals("1")) {
                    builder = new AlertDialog.Builder(v.getRootView().getContext());
                    builder.setMessage("Locate this User")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int id) {
                                    Toast.makeText(context, p.getClat()+p.getClon()+"", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sp=context.getSharedPreferences("track",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor ed=sp.edit();
                                    ed.putString("clat",p.getClat());
                                    ed.putString("clon",p.getClon());
                                    ed.commit();
                                    Intent i = new Intent(context, ViewClientMap.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(i);
                                }

                            }).setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences sp=context.getSharedPreferences("finish",Context.MODE_PRIVATE);
                            SharedPreferences.Editor ed=sp.edit();
                            ed.putString("uid",uid);
                            ed.putString("rid",p.getRid());
                            ed.putString("reqid",p.getReqid());
                            ed.commit();
                            Intent i = new Intent(context, FinishActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }

                    }).setNeutralButton("start Trip", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences sp = context.getSharedPreferences("doc", Context.MODE_PRIVATE);
                            SharedPreferences.Editor ed = sp.edit();
                            ed.putString("lat", p.getDlat() + "");
                            ed.putString("lon", p.getDlon() + "");
                            ed.commit();
                            Intent i = new Intent(context, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Request Conformation");
                    alert.show();
                }
                else if (p.getRstatus().equals("3")){
                    builder = new AlertDialog.Builder(v.getRootView().getContext());
                    builder.setMessage("This Request is Finished")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }

                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Request Conformation");
                    alert.show();
                }
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
