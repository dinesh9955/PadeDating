package com.padedatingapp.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;


import com.google.gson.Gson;
import com.padedatingapp.R;
import com.padedatingapp.ui.main.HomeActivity;

import org.json.JSONObject;

public class MyReciever extends WakefulBroadcastReceiver
{

//    SavePref pref = new SavePref();
    private static final String TAG = "MyReciever";

    Context context = null;

    private static final String ACTION_REGISTRATION
            = "com.google.android.c2dm.intent.REGISTRATION";
    private static final String ACTION_RECEIVE
            = "com.google.android.c2dm.intent.RECEIVE";


   // SavePref pref = new SavePref();


    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    String NOTIFICATION_CHANNEL_ID = "1000";
    String NOTIFICATION_CHANNEL_ID2 = "2000";

    String NOTIFICATION_CHANNEL_NAME2 = "FullBatteryAlarm2";

    int badgeCount = 0;

    @Override
    public void onReceive(Context context11, Intent intent)
    {
        context = context11;

//        pref.SavePref(context11);

        Log.e(TAG, "onReceive: " + intent.getAction());

        Gson gson = new Gson();

        String ff = gson.toJson(intent);

        Log.e(TAG, "onReceiveff: " + ff);
      // pref.SavePref(context11);




        String action = intent.getAction();
        for (String key : intent.getExtras().keySet()) {
            Log.d(
                    "TAG",
                    "{UniversalFCM}->onReceive: key->"
                            + key + ", value->" + intent.getExtras().get(key)
            );
        }


        String data = intent.getStringExtra("data");
                Log.e(
                "TAGGGString ",
                "{UniversalFCMQQQ}->"+data
        );


                try{
                    JSONObject jsonObject = new JSONObject(data);
                    if(jsonObject.has("notificationtype")){
                        String notificationtype = jsonObject.getString("notificationtype");
//                        if(notificationtype.equalsIgnoreCase("invoice seen")){
//                            if(!pref.getCustomerName().equalsIgnoreCase("")){
//                                sendNotification(context11);
//                            }
//                        }
                    }
                }catch (Exception e){

                }




//

        abortBroadcast();




    }

    private void sendNotification(Context context11) {

        NotificationManager notificationManager = (NotificationManager) context11.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID2, NOTIFICATION_CHANNEL_NAME2, importance);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(context11, notificationChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(context11);
        }



        Intent notificationIntent = new Intent(context11, HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context11, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        builder = builder
                .setSmallIcon(R.mipmap.ic_launcher)
                // .setColor(ContextCompat.getColor(context, R.color.color))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(context11.getString(R.string.app_name))
              //  .setContentText(context11.getString(R.string.noti_invoice_seen)+" "+  pref.getCustomerName())
                // .setContentText("Level: "+level+"% | Remaining Time: "+(hour)+"h "+(min)+"min")
                .setPriority(Notification.PRIORITY_MAX)
//                .setCategory(Notification.CATEGORY_SERVICE)
//                .setTicker("VINlocity")
//                .setContentText("VINlocity Driver is tracking.")
//                .setDefaults(Notification.DEFAULT_ALL)
                .setOnlyAlertOnce(true)
                .setSound(null)
                .setAutoCancel(true)
                .setOngoing(true);
        notificationManager.notify(11, builder.build());
       // startForeground(11, builder.build());



    }




}