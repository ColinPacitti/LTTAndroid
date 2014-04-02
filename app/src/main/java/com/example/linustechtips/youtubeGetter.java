package com.example.linustechtips;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.myApp.linustechtips.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class youtubeGetter extends IntentService{
	
	public youtubeGetter() {
		super("youtubeGetter");
	}
	
	@Override
    protected void onHandleIntent(Intent workIntent) {
         String result = null;
         String link = null;
         String title = null;
         String checker = null;

        int push = 0;
		HttpClient client = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse response = null;
		HttpGet httpGet = new HttpGet("http://gdata.youtube.com/feeds/api/users/UCXuqSBlHAE6Xw-yeJA0Tunw/uploads?max-results=5&alt=json");

        if(isOnline()){
		try {
			response = client.execute(httpGet);
            httpEntity = response.getEntity();

            if(httpEntity != null){
                result = EntityUtils.toString(httpEntity);
            }

		} catch (ClientProtocolException e) {
		      e.printStackTrace();
	    } catch (IOException e) {
		      e.printStackTrace();
	    }
			
        try {
		    JSONObject jObject = new JSONObject(result);
	  	    JSONObject video = jObject.getJSONObject("feed");
            JSONObject tester = video.getJSONArray("entry").getJSONObject(0);
            JSONObject links = tester.getJSONArray("link").getJSONObject(0);
            JSONObject titles = video.getJSONArray("entry").getJSONObject(0).getJSONObject("title");
			link = links.getString("href");
            title = titles.getString("$t");

            String init = "null";
            File file = getFileStreamPath("config.txt");
            if(!file.exists()){
                try{
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(init);
                    outputStreamWriter.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            InputStream inputStream = openFileInput("config.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            checker = bufferedReader.readLine();

            if(!checker.equals(title)){
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(title);
                outputStreamWriter.close();
                push = 1;
            }

	    } catch (Exception e) {
			e.printStackTrace();
	    }

           if(push == 1){
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                                                                .setSmallIcon(R.drawable.logo_launcher)
                                                                .setContentTitle(title)
                                                                .setContentText("LinusTechTips has uploaded a new video")
                                                                .setAutoCancel(true);

                NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                Intent resultIntent = new Intent(Intent.ACTION_VIEW);
                resultIntent.setData(Uri.parse(link));
                PendingIntent pending = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBuilder.setContentIntent(pending);
                mNotificationManager.notify(1, notificationBuilder.build());
                Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
           }
	    }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
