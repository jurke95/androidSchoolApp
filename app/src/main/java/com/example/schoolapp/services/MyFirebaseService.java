package com.example.schoolapp.services;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.schoolapp.R;
import com.example.schoolapp.activities.MainActivity;
import com.example.schoolapp.activities.SplashScreenActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;
public class MyFirebaseService extends FirebaseMessagingService {
    String type = "";


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        System.out.println(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        SharedPreferences getPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String notify = getPreferences.getString("notifications", null);
        System.out.println("Are notifications allowed?");
        System.out.println(notify);
        if (notify.equals("true")) {
            if (remoteMessage.getData().size() > 0) {
                type = "json";
                sendNotification(remoteMessage.getData().toString());
            }
            if (remoteMessage.getNotification() != null) {
                type = "message";
                sendNotification(remoteMessage.getNotification().getBody());
            }
        }
    }

    private void sendNotification(String messageBody){
        String id="",message="",title="";

        if(type.equals("json")) {
            try {
                JSONObject jsonObject = new JSONObject(messageBody);
                id = jsonObject.getString("id");
                message = jsonObject.getString("message");
                title = jsonObject.getString("title");

            } catch (JSONException e) {
                //            }
            }
        }
        else if(type.equals("message"))
        {
            message = messageBody;
        }

        Intent intent=new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        //NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "M_CH_ID");


        notificationBuilder.setContentTitle(getString(R.string.app_name));
        notificationBuilder.setContentText(message);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(soundUri);
        notificationBuilder.setSmallIcon(R.drawable.school);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.drawable.school));
        notificationBuilder.setAutoCancel(true);
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notificationBuilder.build());
    }
}
