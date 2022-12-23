package com.example.baktiar.myapplication.Control;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.example.baktiar.myapplication.Control.LaporanHarianActivity;
import com.example.baktiar.myapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by TRIPILAR on 05/07/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        if(remoteMessage.getData().size() > 0){
            //handle the data message here
        }
        if(remoteMessage.getNotification() != null){
            sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
//            sendNotification();
        }
        //getting the title and the body
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        //then here we can use the title and body to build a notification
    }
     private  void sendNotification(String body,String title){
        //Disini direct lokasi notifikasi... merubahnya dibagian sini broh
         Intent intent = new Intent(this,LaporanHarianActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_ONE_SHOT);

         Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

         Notification.Builder notifBuilder =  new Notification.Builder(this)
                 .setSmallIcon(R.mipmap.ic_launcher)
                 .setContentTitle(title)
                 .setContentText(body)
                 .setAutoCancel(true)
                 .setSound(notificationSound)
                 .setContentIntent(pendingIntent);

         NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
         assert notificationManager != null;
         notificationManager.notify(0,notifBuilder.build());


//         NotificationManager notificationManager = (NotificationManager)
//                 getSystemService(Context.NOTIFICATION_SERVICE);
//         Notification notification = new Notification( );

//         Intent notificationIntent = new Intent(this, LaporanHarianActivity.class);
//
//         notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                 | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//         PendingIntent intent = PendingIntent.getActivity(this, 0,
//                 notificationIntent, 0);
//
//    //     notification.setLatestEventInfo(this, title, body, intent);
//         notification.flags |= Notification.FLAG_AUTO_CANCEL;
//         notificationManager.notify(0, notification);
     }
}