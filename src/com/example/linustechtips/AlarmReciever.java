package com.example.linustechtips;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.myApp.linustechtips.R;

public class AlarmReciever extends BroadcastReceiver{
	
	@Override
    public void onReceive(final Context context, Intent intent)
    {
			
		context.startService(new Intent(context, youtubeGetter.class));
		/*NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.placeholder).setContentTitle("Test Notification")
                .setContentText("It WorK!!!!");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notificationBuilder.build());*/
       
     }
}
