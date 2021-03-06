package com.example.linustechtips;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.myApp.linustechtips.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends Activity {
	private View mLoadView;
    private View mListView;
    private int mShortAnimationDuration;
 
    private String[] Sections = {"General Discussion", "Forum News& Info", "Folding@home, Boinc and Coin Mining", 
    		"Troubleshooting", "Off Topic" , "Hot Deals" , "Tech News and Reviews" , "Linus' Videos, News and Ramblings" ,
    		"Member Reviews", "Guides and Tutorials" , "CPU's, Motherboards, and Memory" , "Graphics Cards" ,
    		"Cases and Power Supplies" , "Air Cooling" , "Water Cooling" , "Storage Solutions" , 
    		"Audio" , "Displays" , "Peripherals" , "Networking" , "Operating Systems and Software" ,
    		"Programming & Software Desing" , "Build Logs", "Case Modding and Other Mods" , "New Builds and Planning",
    		"PC Gaming" , "Looking for Group" , "Esports", "Console Gaming","Mobile Gaming" , "Mobile Devices" , 
    		"Home Theatre", "Photo/Video"};
    
    private String[] sectionLink = {"http://linustechtips.com/main/forum/6-general-discussion/", "http://linustechtips.com/main/forum/23-forum-news-info/" ,
    								"http://linustechtips.com/main/forum/37-foldinghome-boinc-and-coin-mining/" , "http://linustechtips.com/main/forum/46-troubleshooting/" , 
    								"http://linustechtips.com/main/forum/50-off-topic/" , "http://linustechtips.com/main/forum/69-hot-deals/" , "http://linustechtips.com/main/forum/13-tech-news-and-reviews/" ,
    								"http://linustechtips.com/main/forum/17-linus-videos-news-and-ramblings/" , "http://linustechtips.com/main/forum/25-member-reviews/", 
    								"http://linustechtips.com/main/forum/30-guides-and-tutorials/" , "http://linustechtips.com/main/forum/5-cpus-motherboards-and-memory/",
    								"http://linustechtips.com/main/forum/16-graphics-cards/" , "http://linustechtips.com/main/forum/21-cases-and-power-supplies/" , "http://linustechtips.com/main/forum/28-air-cooling/",
    								"http://linustechtips.com/main/forum/36-water-cooling/" , "http://linustechtips.com/main/forum/38-storage-solutions/" , "http://linustechtips.com/main/forum/40-audio/",
    								"http://linustechtips.com/main/forum/42-displays/" , "http://linustechtips.com/main/forum/44-peripherals/" , "http://linustechtips.com/main/forum/45-networking/",
    								"http://linustechtips.com/main/forum/10-operating-systems-and-software/" , "http://linustechtips.com/main/forum/20-programming-software-design/",
    								"http://linustechtips.com/main/forum/3-build-logs/", "http://linustechtips.com/main/forum/14-case-modding-and-other-mods/", "http://linustechtips.com/main/forum/18-new-builds-and-planning/",
    								"http://linustechtips.com/main/forum/11-pc-gaming/" , "http://linustechtips.com/main/forum/58-looking-for-group/" , "http://linustechtips.com/main/forum/61-esports/" , "http://linustechtips.com/main/forum/15-console-gaming/",
    								"http://linustechtips.com/main/forum/26-mobile-gaming/" , "http://linustechtips.com/main/forum/8-mobile-devices/" , "http://linustechtips.com/main/forum/24-home-theater/",
    								"http://linustechtips.com/main/forum/31-photovideo/"};
    
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        mDrawerToggle = new ActionBarDrawerToggle(
        		this, mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open, 
                R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        
        mDrawerList.setAdapter(new ArrayAdapter<String>(this , R.layout.drawer_list_item, Sections));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

   }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Highlight the selected item, update the title, and close the drawer
        if(isOnline()){
            new GetXmlDownload().execute(sectionLink[position]);
        }else{
            Toast.makeText(getApplicationContext(), "You Must Be Online To Use LinusTechTips", Toast.LENGTH_LONG).show();
        }
        mDrawerList.setItemChecked(position, true);
        setTitle(Sections[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
            return super.onOptionsItemSelected(item);
        
    }
    
 private class GetXmlDownload extends AsyncTask<String, Void, Elements[]>{
			protected Elements[] doInBackground(String...strings){
				try{
					Document doc = Jsoup.connect(strings[0]).get();
					Elements test = doc.select("a[class*=topic_title]");
					Elements test2 = doc.select("meta[itemprop*=interactionCount]");
					Elements test3 = doc.select("img[class*=ipsUserPhoto");
                    Elements test4 = doc.select("span[itemprop*=dateCreated");
					
					final Elements[] info = new Elements[4];
					info[0] = test;
					info[1] = test2;
					info[2] = test3;
                    info[3] = test4;



					return info;

				}catch(IOException ex){
					return null;
				}
			}

			protected void onPostExecute(Elements[] result){
				final int j = result[1].size();
		        final String[] values = new String[j];
		        final String[] links = new String[j];
		        final String[] images = new String[j];
		        final String[] commentCount = new String[j];
                final String[] date = new String[j];





		        final ListView listView = (ListView)findViewById(R.id.list);
			    for(int i = 0; i < j; i++){
				   values[i]=result[0].eq(i).text();
				   links[i]=result[0].eq(i).attr("href");
				   commentCount[i]=result[1].eq(i).attr("content");
				   images[i]=result[2].eq(i).attr("src");
                   date[i]=result[3].eq(i).text();
			    }

		        ThreadName threadData[] = new ThreadName[j];
		        	for(int i = 0; i < j; i++){
		        		threadData[i] = new ThreadName(values[i], commentCount[i], images[i], date[i]);
		        	}

		        MyAdapter adapter = new MyAdapter(MainActivity.this, R.layout.rowlayout, threadData); 

		        listView.setAdapter(adapter);

		        listView.setOnItemClickListener(new OnItemClickListener(){
		        	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		        		int itemPosition = position;
		        		Intent myIntent = new Intent(MainActivity.this, Thread.class);
		        		myIntent.putExtra("values", links[itemPosition]);
		        		myIntent.putExtra("postition", itemPosition);
		        		MainActivity.this.startActivity(myIntent);
		        	}
		        });
			}
	}
}
