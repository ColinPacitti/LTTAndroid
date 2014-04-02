package com.example.linustechtips;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.myApp.linustechtips.R;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis()); 
    	
        Intent intentAlarm = new Intent(context, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0 ,intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //AlarmManager.INTERVAL_FIFTEEN_MINUTES
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP , System.currentTimeMillis() + 30000 , 10000 , pendingIntent);
        //Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show();
       /* NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.placeholder).setContentTitle("Test Notification")
                .setContentText("It WorK!!!!");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notificationBuilder.build());*/
    }
}
