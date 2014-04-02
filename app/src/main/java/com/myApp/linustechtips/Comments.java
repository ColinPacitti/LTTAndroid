package com.myApp.linustechtips;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.myApp.linustechtips.R;
import com.example.linustechtips.CommentName;
import com.example.linustechtips.commentAdapter;

public class Comments extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		Intent intent = getIntent();
		String link = intent.getStringExtra("comments");
        ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DB4105")));
		new GetThreadDownload().execute(link);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comments, menu);
		return true;
	}

	
	
	private class GetThreadDownload extends AsyncTask<String, Void, Elements[]>{
		protected Elements[] doInBackground(String...strings){
			try{
				Document doc = Jsoup.connect(strings[0]).get();
				Elements test = doc.select("div[class*=author_info]").select("span[itemprop=name]");
				Elements test2 = doc.select("div[itemprop*=commentText]");
				Elements test3 = doc.select("div[class*=author_info]").select("img[class*=ipsUserPhoto]");
				Elements test4 = doc.select("abbr[itemprop*=commentTime]");
				
				final Elements[] done = new Elements[4];
				done[0] = test;
				done[1] = test2;
				done[2] = test3;
				done[3] = test4;
				
				return done;
			}catch(IOException ex){
				Log.d("LinusTechTips",ex.toString());
        		Toast.makeText(getApplicationContext(),
	        			"No internet connection!",Toast.LENGTH_LONG)
	        			.show();
				return null;
			}
		}
		protected void onPostExecute(Elements[] result){
			final int j = result[1].size();
	        final String[] values = new String[j];
	        final String[] images = new String[j];
	        final String[] creator = new String[j];
	        final String[] time = new String[j];

	        final ListView listView = (ListView)findViewById(R.id.list2);
		    for(int i = 0; i < j; i++){
			   values[i]=result[1].eq(i).html();
			   creator[i]=result[0].eq(i).text();
			   images[i]=result[2].eq(i).attr("src");
			   time[i]=result[3].eq(i).text();
		    }

	        CommentName CommentData[] = new CommentName[j-1];
	        
        	for(int i = 1; i < j; i++){
        		CommentData[i-1] = new CommentName(creator[i], values[i], images[i], time[i]);
        	}		    
        	
        	commentAdapter adapter = new commentAdapter(Comments.this, R.layout.commentlayout, CommentData); 
	        listView.setAdapter(adapter);
		    
		}
	}

}
