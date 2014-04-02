package com.example.linustechtips;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentAlarm = new Intent(context, youtubeGetter.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0 ,intentAlarm, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP , System.currentTimeMillis() , AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }
}
