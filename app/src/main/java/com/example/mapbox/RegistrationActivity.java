package com.example.mapbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegistrationActivity extends AppCompatActivity {
    EditText name, email, phone, address, vname, vno, seat;
    RadioButton m, f, cary, carn;
    TextView lbl_lic;
    Button btn_pic, btn_lisence, btn_reg;
    String Gender, utype;
    String type[] = {"Select your Category", "Rider", "User"};


    Button card, lisence, signup, img;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Bitmap bitmapProfile = null;
    private int STORAGE_PERMISSION_CODE = 23;
    boolean somePermissionsForeverDenied = false;
    private String userChosenTask;
    String encodedImage = "", encodedImage1 = "", encodedImage2 = "";
    int t1 = 0;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name = findViewById(R.id.userName);
        email = findViewById(R.id.email_id);
        phone = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        vname = findViewById(R.id.vehiclename);
        vno = findViewById(R.id.vehiclenumber);
        seat = findViewById(R.id.seat);

        m = findViewById(R.id.radio_male);
        f = findViewById(R.id.radio_female);
        cary = findViewById(R.id.yes);
        carn = findViewById(R.id.No);

        lbl_lic = findViewById(R.id.lbl);

        btn_pic = findViewById(R.id.choose);
        btn_lisence = findViewById(R.id.licence);
        btn_reg = findViewById(R.id.register);
        cary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cary.isChecked()) {
                    vname.setVisibility(View.VISIBLE);
                    vno.setVisibility(View.VISIBLE);
                    seat.setVisibility(View.VISIBLE);
                    lbl_lic.setVisibility(View.VISIBLE);
                    btn_lisence.setVisibility(View.VISIBLE);


                }
            }
        });
        btn_lisence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1 = 1;
                if (isReadStorageAllowed()) {
                    selectImage();
                } else
                    requestStoragePermission();
            }
        });
        btn_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1 = 2;
                if (isReadStorageAllowed()) {
                    selectImage();
                } else
                    requestStoragePermission();
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m.isChecked()) {
                    Gender = m.getText().toString();
                } else if (f.isChecked()) {
                    Gender = f.getText().toString();
                }
                if (cary.isChecked()) {
                    utype = "Rider";
                } else if (carn.isChecked()) {
                    utype = "User";
                }
                if(name.getText().toString().isEmpty()){
                    name.setError("Please enter your name");
                }
                if(email.getText().toString().isEmpty()){
                    email.setError("Please enter your name");
                }
                else {
                    startRegistration();
                }
            }
        });
        carn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (carn.isChecked()) {
                    vname.setVisibility(View.GONE);
                    vno.setVisibility(View.GONE);
                    seat.setVisibility(View.GONE);
                    lbl_lic.setVisibility(View.GONE);
                    btn_lisence.setVisibility(View.GONE);

                }
            }
        });

    }

    private void startRegistration() {
        final ProgressDialog progressDoalog = new ProgressDialog(RegistrationActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Registering..");
        progressDoalog.setTitle("Please wait");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        Apiinterface apiinterface = Apiclient.getClient().create(Apiinterface.class);
        Call<Login_Model> call = apiinterface.getRegisterData("register", name.getText().toString(), email.getText().toString(),
                phone.getText().toString(), address.getText().toString(), Gender, utype,
                vname.getText().toString(), vno.getText().toString(), seat.getText().toString(), encodedImage, encodedImage1);
        call.enqueue(new Callback<Login_Model>() {
            @Override
            public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                if (response.body().getMessage().equals("Registration Successfully completed")) {
                    Toast.makeText(RegistrationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegistrationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
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
        if (t1 == 1) {
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            btn_lisence.setText("uploaded");

        } else if (t1 == 2) {
            encodedImage1 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            btn_pic.setText("uploaded");
        } else if (t1 == 3) {
            encodedImage2 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            img.setText("uploaded");
        }
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
