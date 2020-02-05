package com.example.mapbox;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    public static String TAG="MyFirebaseMessagingService";

    String channelID="app.eexposeller";
    String channelNAME="eexpo_seller";
    Context mContext;
  //  SharedPreferenceHelper sharedPreferenceHelper;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

       // sharedPreferenceHelper=new SharedPreferenceHelper(MyFirebaseMessagingService.this);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage);
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification1(remoteMessage);
        }
    }
    private void sendNotification1(RemoteMessage messageBody) {

        Log.d("@@", messageBody.getNotification().getBody() + "");
        try {
            String key = "", title = "", message = "";
//            List<String> notidata=new ArrayList<String>(messageBody.getData().values());;

            // key = messageBody.getData().get("key");
            title = messageBody.getNotification().getTitle();
            message = messageBody.getNotification().getBody();
            //    new SharedPreferenceHelper(getApplicationContext()).putString(Constants.noti_key, "");
            Intent intent = null;
            PendingIntent pendingIntent = null;
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channelID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(title)
                    //.setStyle(bigText)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntent(intent);

            notificationManager.notify(0   , notificationBuilder.build());


        }
        catch (Exception e){
            e.printStackTrace();
//            Log.e(TAG, "sendNotification: ");
        }
    }

    private void sendNotification(RemoteMessage messageBody) {

        Log.d("@@", messageBody.getData() + "");
        try {
            String key = "", title = "", message = "";
//            List<String> notidata=new ArrayList<String>(messageBody.getData().values());;

            // key = messageBody.getData().get("key");
            title = messageBody.getData().get("title");
            message = messageBody.getData().get("body");
            //    new SharedPreferenceHelper(getApplicationContext()).putString(Constants.noti_key, "");
            Intent intent = null;
            PendingIntent pendingIntent = null;
//            Map<String,String> my_firebase_map=messageBody.getData();
//            if(my_firebase_map.containsKey("key"))
//            {
//                if(my_firebase_map.get("key")!=null) {
//                    key = messageBody.getData().get("key");
//
//                }
//            }

            //   if (key.equals("3")) {

//                intent = new Intent(this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//              //  if (new SharedPreferenceHelper(this).getBoolean(Constants.isLoggedIn,false)) {
//                    if (!isAppIsInBackground(this)) {
//                        pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                                PendingIntent.FLAG_UPDATE_CURRENT);
//                    }
//
//                    else {
//                        Intent in=new Intent(this,MainActivity.class);
//                      //  in.putExtra(Constants.key3,key);
//                        pendingIntent = PendingIntent.getActivity(this, 0,in,
//                                PendingIntent.FLAG_UPDATE_CURRENT);
//                    }
            // }

//                else {
//                    Intent intent1=new Intent(this,Login.class);
//                    intent1.putExtra(Constants.key3,key);
//                    pendingIntent = PendingIntent.getActivity(this, 0,intent1,
//                            PendingIntent.FLAG_UPDATE_CURRENT);
//
//                }
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    NotificationChannel mChannel = new NotificationChannel(
                            channelID, channelNAME, importance);
                    notificationManager.createNotificationChannel(mChannel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                makeNotificationChannel(channelID, channelNAME, NotificationManager.IMPORTANCE_HIGH);
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(title)
                    //                .setStyle(bigText)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher));
                notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background);
                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
            } else {
                notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background);
            }
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntent(intent);

            notificationManager.notify(0, notificationBuilder.build());

        //    }
            //else if (key.equals("2")) {
//                intent = new Intent(this, OrderHistoryActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                if (new SharedPreferenceHelper(this).getBoolean(Constants.isLoggedIn,false)) {
//                    if (!isAppIsInBackground(this)) {
//                        pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                                PendingIntent.FLAG_UPDATE_CURRENT);
//                    }
//
//                    else {
//                        Intent in=new Intent(this,Splash.class);
//                        in.putExtra(Constants.key2,key);
//                        pendingIntent = PendingIntent.getActivity(this, 0,in,
//                                PendingIntent.FLAG_UPDATE_CURRENT);
//                    }
//                }
//
//                else {
//                    Intent intent1=new Intent(this,Login.class);
//                    intent1.putExtra(Constants.key2,key);
//                    pendingIntent = PendingIntent.getActivity(this, 0,intent1,
//                            PendingIntent.FLAG_UPDATE_CURRENT);
//
//                }
//
//                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                NotificationManager notificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//             /*   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    try {
//                        NotificationChannel mChannel = new NotificationChannel(
//                                channelId, channelName, importance);
//                        notificationManager.createNotificationChannel(mChannel);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }*/
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                    makeNotificationChannel(channelID, channelNAME, NotificationManager.IMPORTANCE_HIGH);
//                }
//                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channelID)
//                        .setSmallIcon(R.drawable.push_img1)
//                        .setContentTitle(title)
//                        //                .setStyle(bigText)
//                        .setContentText(message)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                     /*   notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
//                                R.mipmap.ic_launcher));*/
//                    notificationBuilder.setSmallIcon(R.drawable.push_img1);
//                    notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
//                } else {
//                    notificationBuilder.setSmallIcon(R.drawable.push_img1);
//                }
//                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//                stackBuilder.addNextIntent(intent);
//
//                notificationManager.notify(0   , notificationBuilder.build());
//            } else if (key.equals("5")) {
//                intent = new Intent(this, OrderHistoryActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                if (new SharedPreferenceHelper(this).getBoolean(Constants.isLoggedIn,false)) {
//                    if (!isAppIsInBackground(this)) {
//                        pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                                PendingIntent.FLAG_UPDATE_CURRENT);
//                    }
//
//                    else {
//                        Intent in=new Intent(this,Splash.class);
//                        in.putExtra(Constants.key5,key);
//                        pendingIntent = PendingIntent.getActivity(this, 0,in,
//                                PendingIntent.FLAG_UPDATE_CURRENT);
//                    }
//                }
//
//                else {
//                    Intent intent1=new Intent(this,Login.class);
//                    intent1.putExtra(Constants.key5,key);
//                    pendingIntent = PendingIntent.getActivity(this, 0,intent1,
//                            PendingIntent.FLAG_UPDATE_CURRENT);
//
//                }
//                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                NotificationManager notificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//             /*   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    try {
//                        NotificationChannel mChannel = new NotificationChannel(
//                                channelId, channelName, importance);
//                        notificationManager.createNotificationChannel(mChannel);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }*/
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                    makeNotificationChannel(channelID, channelNAME, NotificationManager.IMPORTANCE_HIGH);
//                }
//                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channelID)
//                        .setSmallIcon(R.drawable.push_img1)
//                        .setContentTitle(title)
//                        //                .setStyle(bigText)
//                        .setContentText(message)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                     /*   notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
//                                R.mipmap.ic_launcher));*/
//                    notificationBuilder.setSmallIcon(R.drawable.push_img1);
//                    notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
//                } else {
//                    notificationBuilder.setSmallIcon(R.drawable.push_img1);
//                }
//                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//                stackBuilder.addNextIntent(intent);
//
//                notificationManager.notify(0   , notificationBuilder.build());
//            } else if (key.equals("7")) {
//                intent = new Intent(this, Login.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                NotificationManager notificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//             /*   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    try {
//                        NotificationChannel mChannel = new NotificationChannel(
//                                channelId, channelName, importance);
//                        notificationManager.createNotificationChannel(mChannel);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }*/
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                    makeNotificationChannel(channelID, channelNAME, NotificationManager.IMPORTANCE_HIGH);
//                }
//                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channelID)
//                        .setSmallIcon(R.drawable.push_img1)
//                        .setContentTitle(title)
//                        //                .setStyle(bigText)
//                        .setContentText(message)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                     /*   notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
//                                R.mipmap.ic_launcher));*/
//                    notificationBuilder.setSmallIcon(R.drawable.push_img1);
//                    notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
//                } else {
//                    notificationBuilder.setSmallIcon(R.drawable.push_img1);
//                }
//                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//                stackBuilder.addNextIntent(intent);
//
//                notificationManager.notify(0   , notificationBuilder.build());
////                setnotification();
//            }
        }
        catch (Exception e){
            e.printStackTrace();
//            Log.e(TAG, "sendNotification: ");
        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, int importance)
    {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
//        channel.setShowBadge(true); // set false to disable badges, Oreo exclusive

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }
}