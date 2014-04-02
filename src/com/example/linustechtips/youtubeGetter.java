package com.example.linustechtips;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class youtubeGetter extends IntentService{
	
	public youtubeGetter() {
		super("youtubeGetter");
	}
	
	@Override
    protected void onHandleIntent(Intent workIntent) {
		 StringBuilder builder = new StringBuilder();
		 HttpClient client = new DefaultHttpClient();
		 HttpGet httpGet = new HttpGet("http://gdata.youtube.com/feeds/api/users/LinusTechTips/uploads?max-results=1&alt=json");
		try{
			HttpResponse response = client.execute(httpGet);
		    StatusLine statusLine = response.getStatusLine();
		    int statusCode = statusLine.getStatusCode();
		    if (statusCode == 200) {
		      HttpEntity entity = response.getEntity();
		      InputStream content = entity.getContent();
		      BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		      String line;
		      while ((line = reader.readLine()) != null) {
		          builder.append(line);
	          }
		    } else {
		       // Log.e(ParseJSON.class.toString(), "Failed to download file");
		    }
		    } catch (ClientProtocolException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
			
			 try {
			      //JSONArray jsonArray = new JSONArray(result);
			     // Log.i(ParseJSON.class.getName(),
			      //    "Number of entries " + jsonArray.length());
			     // for (int i = 0; i < jsonArray.length(); i++) {
			       // JSONObject jsonObject = jsonArray.getJSONObject(i);
				 String result = builder.toString();
				 JSONObject jObject = new JSONObject(result);
				 JSONObject video = jObject.getJSONObject("version");
			        String test = jObject.getString("$t");
				 System.out.println("test");

			     // }
			    } catch (Exception e) {
			      e.printStackTrace();
			    }

           /* NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
            												.setSmallIcon(R.drawable.logo_launcher)
            												.setContentTitle("New LinusTechTips upload")
            												.setContentText("Click to watch!");
            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent resultIntent = new Intent(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(link));
            PendingIntent pending = PendingIntent.getActivity(this, 0, resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
            notificationBuilder.setContentIntent(pending);
            mNotificationManager.notify(1, notificationBuilder.build());*/
		//}catch(IOException err){
		//	cool = null;
		//}
	}
}
