package com.example.linustechtips;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.myApp.linustechtips.Comments;
import com.myApp.linustechtips.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Vector;

public class Thread extends Activity {
	private View mLoadView;
    private View mWebView;
    private int mShortAnimationDuration;
    private String comment;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		String link = intent.getStringExtra("values");
		comment = link;
		final String[] values = new String[2];
		values[0] = link;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thread);
        mWebView = findViewById(R.id.webview);
        mWebView.setVisibility(View.GONE);
        mLoadView = findViewById(R.id.loading_spinner);
        ActionBar bar = getActionBar();
        
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        if(isOnline()){
		    new GetThreadDownload().execute(values);
        }else{
            Toast.makeText(getApplicationContext(), "You Must Be Online To Use LinusTechTips", Toast.LENGTH_LONG).show();
        }
	}

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.thread, menu);
            return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.comments){
				Intent myIntent = new Intent(Thread.this, Comments.class);
				myIntent.putExtra("comments", comment);
				Thread.this.startActivity(myIntent);
				return true;
		}
		return true;
	}

    private void crossfade() {
        mWebView.setAlpha(0f);
        mWebView.setVisibility(View.VISIBLE);

        mWebView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        mLoadView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadView.setVisibility(View.GONE);
                    }
                });
    }    
    
	private class GetThreadDownload extends AsyncTask<String, Void, Vector<Elements>>{
		protected Vector<Elements> doInBackground(String...strings){
			try{
				Document doc = Jsoup.connect(strings[0]).get();
				Elements test = doc.select("span[itemprop*=creator]");
				Elements test2 = doc.select("div[itemprop*=commentText]");
				Elements test3 = doc.select("li[class*=avatar]");
				Vector<Elements> V = new Vector<Elements>();
				V.add(test);
				V.add(test2);
				V.add(test3);
				return V;
			}catch(IOException ex){
				Log.d("LinusTechTips",ex.toString());
     		Toast.makeText(getApplicationContext(),
	        			"No internet connection!",Toast.LENGTH_LONG)
	        			.show();
				return null;
			}
		}
		protected void onPostExecute(Vector<Elements> result){
	        final String name;
	        final String avatar;
	        Elements cool = result.get(1);
			int j = cool.size();
	        final String[] values2 = new String[j];
		    for(int i = 0; i < j; i++){
			   values2[i]=cool.eq(i).html();
		    }
		    name = "<p id=\"name\"> " + result.get(0).first().text() + "</p>";
		    avatar = result.get(2).first().html();
	       WebView myWebView = (WebView) findViewById(R.id.webview);
	       WebSettings webSettings = myWebView.getSettings();
	       webSettings.setJavaScriptEnabled(true);
	       webSettings.setDefaultTextEncodingName("UTF-8");

	        String test = "<style type=\"text/css\">    img { max-width: 100%} " +
	        		"										iframe{ max-width: 100%}" +
	        		"										a{ color: #DB4105}" +
	        		"										.name{ ;" +
	        		"												font: normal 15px HelveticaNeue-Light" +
	        		"												;}" +
	        		"										.ipsUserPhoto{" +
	        		"													  width:50px;" +
	        		"													  height:50px;" +
	        		"													  margin:auto;" +
	        		"													padding: 5px;} " +
	        		"										hr.fancy-line { " +
	        		"														height:1px;" +
	        		"														width: 100%;}" +
	        		"										.header{ background-color: rgb(255, 255 , 255);"+
	        		"												  border-bottom: 2px solid #d5d2d2;" +
	        		"											      box-shadow: 0px 3px 6px 2px rgba(121, 121, 121, 0.2);" +
	        		"												  width: 100%;" +
	        		"												  height: 70px;" +
	        		"												  padding-left: 10px;" +
	        		"												  padding-top: 10px;" +
	        		"												  position:fixed;" +
	        		"												  top:0px;" +
	        		"												  left:0px;" +
	        		"												  display: block;}" +
	        		"										.image{ width: 60px;" +
	        		"												height: 60px;" +
	        		"												padding: 0px;" +
	        		"												float:left;" +
	        		"												background-color: rgb(240, 240 , 240); }" +
	        		"										.name{height:30px;" +
	        		"											  width: 80px;" +
	        		"											  padding: 5px;" +
	        		"											  position:relative;" +
	        		"											  float:left;}" +
	        		"										.body{padding:10px;" +
	        		"											  display:block;" +
	        		"											  margin-top:100px;}</style>";

	        values2[0] = test + "<div class=\'header\'>" + "<span class=\'image\'>" + avatar + "</span>" + 
	        "<div class=\'name\'>" + name + "</div>" + 
	         "</div>" + "<div class=\"body\">" + values2[0] + "</div>";
	        
	       myWebView.setWebChromeClient(new WebChromeClient());
	       myWebView.loadDataWithBaseURL("fake://not/needed", values2[0], "text/html", "utf-8", "");

	       crossfade();
		}
}

}
