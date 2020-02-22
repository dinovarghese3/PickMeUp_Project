package com.example.mapbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapbox.JoinRide.JoinRideActivity;
import com.example.mapbox.JoinRide.Validations;
import com.example.mapbox.model.Login_Model;
import com.example.mapbox.webservice.Apiclient;
import com.example.mapbox.webservice.Apiinterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class DecisionActivity extends AppCompatActivity {
    Button create, join;
    SharedPreferences sp;
    String type[] = {"Select your Category", "Rider", "User"};
    EditText vname, vno, seat,pname,pmobile;
    Button uplic, update,parent,padd;

    SharedPreferences pref;
    Button card, lisence, signup, img;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Bitmap bitmapProfile = null;
    private int STORAGE_PERMISSION_CODE = 23;
    boolean somePermissionsForeverDenied = false;
    private String userChosenTask;
    String encodedImage = "", encodedImage1 = "", encodedImage2 = "";
    String uid;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        create = findViewById(R.id.create_ride);
        join = findViewById(R.id.search_Ride);
        parent = findViewById(R.id.parent1);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(v.getRootView().getContext());
                dialog.setContentView(R.layout.customparent);
                dialog.setTitle("Title...");


                pname = dialog.findViewById(R.id.pname);
                pmobile = dialog.findViewById(R.id.pmobile);
                padd = dialog.findViewById(R.id.padd);
                pname .addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {

                        if (pname.getText().toString().matches(Validations.email) && s.length() > 0)
                        {

                        }
                        else
                        {
                            //Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                            pname.setError("invalid mail");
                        }
                    }
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // other stuffs
                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // other stuffs
                    }
                });
                padd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(pname.getText().toString().isEmpty()){
                            pname.setError("please enter a valid email");
                        }
                        if (pmobile.getText().toString().isEmpty() || (!pmobile.getText().toString().matches(Validations.mobile))) {
                            pmobile.setError("Please enter a valid Mobile number");
                        }
                        else {
                            startAddParent();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();

            }
        });
        sp = getSharedPreferences("udata", Context.MODE_PRIVATE);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("logindata", Context.MODE_PRIVATE);
                uid = sharedPreferences.getString("uid", "");

                if (sharedPreferences.getString("utype", "").equals("Rider")) {


                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("ustatus", "create");
                    ed.commit();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    final Dialog dialog = new Dialog(v.getRootView().getContext());
                    dialog.setContentView(R.layout.customvechicle);
                    dialog.setTitle("Title...");


                    vname = dialog.findViewById(R.id.uvname);
                    vno = dialog.findViewById(R.id.uvno);
                    seat = dialog.findViewById(R.id.useat);
                    uplic = dialog.findViewById(R.id.uupload);
                    update = dialog.findViewById(R.id.uupdate);
                    uplic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isReadStorageAllowed()) {
                                selectImage();
                            } else
                                requestStoragePermission();
                        }
                    });
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(vname.getText().toString().isEmpty()){
                                vname.setError("Enter vechile name");
                            }
                            else if(vno.getText().toString().isEmpty()|| !vno.getText().toString().matches(Validations.vehicleno)){
                                vno.setError("Enter vechile number");
                            }
                            else if(seat.getText().toString().isEmpty()){
                                seat.setError("Enter seat capacity");
                            }
                            else if(uplic.getText().toString().equals("uploaded")){
                                Toast.makeText(DecisionActivity.this, "Please select your lisence", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                startRegistration();
                            }
                        }
                    });
                    dialog.show();
                    // Toast.makeText(DecisionActivity.this, "please add vehicle details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("ustatus", "join");
                ed.commit();
                startActivity(new Intent(getApplicationContext(), JoinRideActivity.class));
                finish();
            }
        });
    }

    private void startAddParent() {
        SharedPreferences sharedPreferences = getSharedPreferences("logindata", Context.MODE_PRIVATE);

        uid = sharedPreferences.getString("uid", "");

        final ProgressDialog progressDoalog = new ProgressDialog(DecisionActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Registering..");
        progressDoalog.setTitle("Please wait");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<Login_Model> call = apiinterface.addparent("Addparent", pname.getText().toString()
                , pmobile.getText().toString(), uid);
        call.enqueue(new Callback<Login_Model>() {
            @Override
            public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                if (response.body().getMessage().equals("success")) {
                    Toast.makeText(DecisionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DecisionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login_Model> call, Throwable t) {

            }
        });
        progressDoalog.dismiss();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
    }

    private void startRegistration() {
        Log.d("@@",vname.getText().toString()+"\n"+vno.getText().toString()+"\n"+seat.getText().toString()
        +"\n"+encodedImage+"\n"+uid);
        final ProgressDialog progressDoalog = new ProgressDialog(DecisionActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Registering..");
        progressDoalog.setTitle("Please wait");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<Login_Model> call = apiinterface.updatevehicledata("updatevehicle", vname.getText().toString()
                , vno.getText().toString(),
                seat.getText().toString(), encodedImage, uid);
        call.enqueue(new Callback<Login_Model>() {
            @Override
            public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                if (response.body().getMessage().equals("updated")) {
                    Toast.makeText(DecisionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(DecisionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login_Model> call, Throwable t) {

            }
        });
        progressDoalog.dismiss();
    }

    private void selectImage() {
        //final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        final CharSequence[] items = {"Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DecisionActivity.this);
        builder.setTitle("Upload your documents");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                // boolean result = Utility.checkPermission(RegistrationActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChosenTask = "Take Photo";
                    //  if (result)
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChosenTask = "Choose from Library";
                    //  if (result)
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    Log.d("dialog dismiss ", "true");
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        Toast.makeText(this, "" + destination, Toast.LENGTH_SHORT).show();
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bitmapProfile = thumbnail;
        if (bitmapProfile != null) {
            getStringImage(bitmapProfile);
        }


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Bitmap myImg = BitmapFactory.decodeFile(picturePath);
        // reference.setImageBitmap(myImg);
        // Bitmap bm=null;
        Bundle extras2 = data.getExtras();
        if (extras2 != null) {
            myImg = extras2.getParcelable("data");
        }
        //reference.setImageBitmap(myImg);

        bitmapProfile = myImg;
        if (bitmapProfile != null) {
            getStringImage(bitmapProfile);
        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (userChosenTask.equals("Take Photo"))
//                        cameraIntent();
//                    else if (userChosenTask.equals("Choose from Library"))
//                        galleryIntent();
//                } else {
//                    //code for deny
//                }
//                break;
//        }
//    }

    public void getStringImage(Bitmap bmp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        uplic.setText("uploaded");


        //return encodedImage;
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, CAMERA);
        int result1 = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{CAMERA, READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length == 0) {
            return;
        }
        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if (!allPermissionsGranted) {

            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    //denied
                    Log.e("denied", permission);
                    // requestStoragePermission();
                } else {
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", permission);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("You have forcefully denied some of the required permissions " +
                                "for this action. Please open settings, go to permissions and allow them.")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }

        } else {
            switch (requestCode) {
                //act according to the request code used while requesting the permission(s).
            }
        }
    }
}
