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

import com.example.mapbox.R;
import com.example.mapbox.model.Login_Model;
import com.example.mapbox.model.NearestModel;
import com.example.mapbox.model.Profile_pojo;
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

public class RiderAdapter extends RecyclerView.Adapter<RiderAdapter.MyviewHolder> {
    Context context;
    List<NearestModel> ride;
    AlertDialog.Builder builder;
    SharedPreferences sharedPreferences;

    public RiderAdapter(Context context, List<NearestModel> ride) {
        this.context = context;
        this.ride = ride;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom, parent, false);

        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyviewHolder holder, int position) {
        final NearestModel m = ride.get(position);
        Log.d("@@",m.getPhoto()+"");
        holder.name.setText("Name\t:\t"+m.getName());
        holder.dest.setText("Destination\t:\t"+m.getDestination());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(m.getPhoto(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.img.setImageBitmap(decodedImage);
        sharedPreferences = context.getSharedPreferences("logindata", Context.MODE_PRIVATE);


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// custom dialog
                final Dialog dialog = new Dialog(v.getRootView().getContext());
                dialog.setContentView(R.layout.viewrider);
                dialog.setTitle("Title...");
                ImageView image;
                TextView name, email, phone, address, common;
                RatingBar rat;
                Button req, route;
                String uid = null;
                image = dialog.findViewById(R.id.image);
                name = dialog.findViewById(R.id.Name);
                email = dialog.findViewById(R.id.email);
                phone = dialog.findViewById(R.id.phone);
                address = dialog.findViewById(R.id.addressView);
                common = dialog.findViewById(R.id.common);
                rat = dialog.findViewById(R.id.ratingBar);
                req = dialog.findViewById(R.id.Send);
                route = dialog.findViewById(R.id.nav);
                rat.setRating(Float.parseFloat(m.getAvgrating()));
                name.setText("Name\t:\t" + m.getName());
                email.setText("Email\t:\t" + m.getEmail());
                phone.setText("Phone\t:\t" + m.getPhone());
                address.setText("Address\t:\t" + m.getAddress());

                if (m.getUtype().equals("Rider")) {
                    common.setVisibility(View.VISIBLE);
                    rat.setVisibility(View.VISIBLE);
                    common.setText("Vehicle Name\t:\t" + m.getVname() + "\n\n" +
                            "Vehicle Number\t:\t" + m.getVno() + "\n\nAvailable Seats\t:\t" + m.getSeat() + "\nSource\t:\t" + m.getSource()
                            + "\n\nDestination\t:\t" + m.getDestination());
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                imageBytes = Base64.decode(m.getPhoto(), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                image.setImageBitmap(decodedImage);
                final String finalUid = uid;
                req.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Integer.parseInt(m.getSeat()) > 0) {
                            Date c = Calendar.getInstance().getTime();
                            System.out.println("Current time => " + c);
                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                       //     Toast.makeText(context, sharedPreferences.getString("uid", "") + "\n" + m.getRid() + "\n" + "" + df.format(c) + "\n" + "" + m.getToken(), Toast.LENGTH_SHORT).show();

                            Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
                            Call<Login_Model> call = apiinterface.sendreq("sendreq", sharedPreferences.getString("uid", ""), m.getRid(), df.format(c), m.getToken());
                            call.enqueue(new Callback<Login_Model>() {
                                @Override
                                public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                                    Log.d("@@", response.body().getMessage());
                                    Toast.makeText(context, response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Login_Model> call, Throwable t) {

                                }
                            });
                        } else {
                            Toast.makeText(context, "you cannot send request to this rider,due to insufficient num of seats", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                route.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent1 = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?"
                                        + "saddr=" + m.getSlat() + "," + m.getSlon() + "&daddr=" + m.getDlat() + "," + m.getDlon()));
                        intent1.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
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
        TextView name,dest;
        CardView card;
        ImageView img;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text);
            img = itemView.findViewById(R.id.image);
            dest = itemView.findViewById(R.id.dest);
            card = itemView.findViewById(R.id.card);
        }
    }
}