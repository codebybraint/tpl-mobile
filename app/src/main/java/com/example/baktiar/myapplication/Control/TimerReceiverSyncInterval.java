package com.example.baktiar.myapplication.Control;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Baktiar Krisna on 29/06/2018.
 */

public class TimerReceiverSyncInterval extends BroadcastReceiver {
    int time []= {8,9,10,11,12,13,14,15,16};
    String Calender[] = { "January", "February", "March", "April", "May", "June",
            "July", "August"};
    @Override
    public void onReceive(Context context, Intent intent) {
        scheduleAlarms(context);
        context.startService(new Intent(context, NotificationServiceSyncInterval.class));
        Log.d("TAG", "Sync OnReceive");
    }


    public static void scheduleAlarms(Context paramContext) {
//    for(int a=0;a<8;a++){
//        //Calendar  Calender[a] = Calendar.getInstance();
//    }
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();
        Calendar calendar4 = Calendar.getInstance();
        Calendar calendar5 = Calendar.getInstance();
        Calendar calendar6 = Calendar.getInstance();
        Calendar calendar7 = Calendar.getInstance();
        Calendar calendar8 = Calendar.getInstance();
        Calendar calendar9 = Calendar.getInstance();
        Calendar calendar10 = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,13);
        calendar.set(Calendar.MINUTE,40);
        calendar2.set(Calendar.HOUR_OF_DAY,13);
        calendar2.set(Calendar.MINUTE,42);
        calendar3.set(Calendar.HOUR_OF_DAY,13);
        calendar3.set(Calendar.MINUTE,44);
        calendar4.set(Calendar.HOUR_OF_DAY,13);
        calendar4.set(Calendar.MINUTE,46);
        calendar5.set(Calendar.HOUR_OF_DAY,13);
        calendar5.set(Calendar.MINUTE,48);
        calendar6.set(Calendar.HOUR_OF_DAY,13);
        calendar6.set(Calendar.MINUTE,52);
        calendar7.set(Calendar.HOUR_OF_DAY,13);
        calendar7.set(Calendar.MINUTE,54);
        calendar8.set(Calendar.HOUR_OF_DAY,13);
        calendar8.set(Calendar.MINUTE,56);
        calendar9.set(Calendar.HOUR_OF_DAY,13);
        calendar9.set(Calendar.MINUTE,58);
        calendar10.set(Calendar.HOUR_OF_DAY,14);
        calendar10.set(Calendar.MINUTE,0);
        AlarmManager localAlarmManager = (AlarmManager) paramContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent localPendingIntent = PendingIntent.getService(paramContext, 0,
                new Intent(paramContext, NotificationServiceSyncInterval.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent localPendingIntent1 = PendingIntent.getService(paramContext, 1,
                new Intent(paramContext, NotificationServiceSyncInterval.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent localPendingIntent2 = PendingIntent.getService(paramContext, 2,
                new Intent(paramContext, NotificationServiceSyncInterval.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent localPendingIntent3 = PendingIntent.getService(paramContext, 3,
                new Intent(paramContext, NotificationServiceSyncInterval.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent localPendingIntent4 = PendingIntent.getService(paramContext, 4,
                new Intent(paramContext, NotificationServiceSyncInterval.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent localPendingIntent5 = PendingIntent.getService(paramContext, 5,
                new Intent(paramContext, NotificationServiceSyncInterval.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent localPendingIntent6 = PendingIntent.getService(paramContext, 6,
                new Intent(paramContext, NotificationServiceSyncInterval.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent localPendingIntent7 = PendingIntent.getService(paramContext, 7,
                new Intent(paramContext, NotificationServiceSyncInterval.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent localPendingIntent8 = PendingIntent.getService(paramContext, 8,
                new Intent(paramContext, NotificationServiceSyncInterval.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent localPendingIntent9 = PendingIntent.getService(paramContext, 9,
                new Intent(paramContext, NotificationServiceSyncInterval.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        localAlarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
//                (1 * 6000), localPendingIntent);


        localAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), localPendingIntent);
        localAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), localPendingIntent1);
        localAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), localPendingIntent2);
        localAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar4.getTimeInMillis(), localPendingIntent3);
        localAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar5.getTimeInMillis(), localPendingIntent4);
        localAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar6.getTimeInMillis(), localPendingIntent5);
        localAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar7.getTimeInMillis(), localPendingIntent6);
        localAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar8.getTimeInMillis(), localPendingIntent7);
        localAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar9.getTimeInMillis(), localPendingIntent8);
        localAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar10.getTimeInMillis(), localPendingIntent9);

//        System.out.print("local alarm and manager set alarm, calender get timemilis local penmding");


    }
}