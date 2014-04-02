package com.example.linustechtips;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myApp.linustechtips.R;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class commentAdapter extends ArrayAdapter<CommentName>{
		 Context context;
		 int position1;
		 CommentName[] values = null;
		 boolean pauseOnScroll = false; // or true
		 boolean pauseOnFling = true;
		 private ImageLoader ImageLoader;
		
		public commentAdapter(Context context, int position1, CommentName[] values){
			super(context, position1, values);
			this.position1 = position1;
			this.context = context;
			this.values = values;
			
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
    		.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
    		.discCacheExtraOptions(60, 60, CompressFormat.JPEG, 75, null)
    		.build();
			this.ImageLoader = ImageLoader.getInstance();
			ImageLoader.init(config);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){

			View row = convertView;
			ThreadHolder holder = null;
			
			if(row == null){
				LayoutInflater inflater = (LayoutInflater) context
				        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.commentlayout, parent, false);
			 
				holder = new ThreadHolder();
	        
				holder.image = (ImageView) row.findViewById(R.id.icon);
				holder.image.getLayoutParams().height = 180;
				holder.image.getLayoutParams().width = 180;
				holder.title = (TextView) row.findViewById(R.id.title);
				holder.commentCount = (TextView) row.findViewById(R.id.comments);
				holder.time = (TextView) row.findViewById(R.id.time);
			
				row.setTag(holder);
			
			}else{
				holder = (ThreadHolder)row.getTag();
			}
			
			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
	    		.cacheInMemory(true)
	    		.cacheOnDisc(true)
	    		.displayer(new FadeInBitmapDisplayer(740))
	    	.build();
			
			//PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader, pauseOnScroll, pauseOnFling);
			//listView.setOnScrollListener(listener);
			CommentName commentname = values[position];
			URLImageParser p = new URLImageParser(holder.commentCount, this.context);
			Spanned htmlSpan = Html.fromHtml(commentname.comments, p, null);
			holder.commentCount.setText(htmlSpan);
			holder.commentCount.setMovementMethod(LinkMovementMethod.getInstance());
			holder.title.setText(commentname.title);
			holder.time.setText("Posted: " + commentname.time);
			ImageLoader.displayImage(commentname.image,holder.image, defaultOptions);
			//Picasso.with(context).load(commentname.image).resize(50,50).into(holder.image);
			
			return row;
		}

		static class ThreadHolder{
			ImageView image;
			TextView title;
			TextView commentCount;
			TextView time;
		}
		
        public class URLDrawable extends BitmapDrawable {
        	// the drawable that you need to set, you could set the initial drawing
        	// with the loading image if you need to
        	protected Drawable drawable;

@Override
public void draw(Canvas canvas) {
   // override the draw to facilitate refresh function later
   if(drawable != null) {
       drawable.draw(canvas);

   }
}
}
public class URLImageParser implements ImageGetter {
Context c;
View container;

/***
* Construct the URLImageParser which will execute AsyncTask and refresh the                              container
* @param t
* @param c
*/
public URLImageParser(View t, Context c) {
   this.c = c;
   this.container = t;
}

public Drawable getDrawable(String source) {
   URLDrawable urlDrawable = new URLDrawable();

   // get the actual source
   ImageGetterAsyncTask asyncTask = 
       new ImageGetterAsyncTask( urlDrawable);

   asyncTask.execute(source);

   // return reference to URLDrawable where I will change with actual image from
   // the src tag
   return urlDrawable;
}

public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable>  {
   URLDrawable urlDrawable;

   public ImageGetterAsyncTask(URLDrawable d) {
       this.urlDrawable = d;
   }

   @Override
   protected Drawable doInBackground(String... params) {
       String source = params[0];
       //System.out.println("source==="+source);
       return fetchDrawable(source);
   }

   @Override
   protected void onPostExecute(Drawable result) {
       // set the correct bound according to the result from HTTP call

       // System.out.println("result.getIntrinsicWidth()==="+result.getIntrinsicWidth());
       // System.out.println("result.getIntrinsicHeight()==="+result.getIntrinsicHeight());
        if(result!=null)
        {
       urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0 
               + result.getIntrinsicHeight()); 
        }

       // change the reference of the current drawable to the result
       // from the HTTP call
       urlDrawable.drawable = result;

       // redraw the image by invalidating the container
       URLImageParser.this.container.invalidate();
       ((TextView) URLImageParser.this.container).setText(((TextView) URLImageParser.this.container).getText());

   }

   /***
    * Get the Drawable from URL
    * @param urlString
    * @return
    */
   public Drawable fetchDrawable(String urlString) {
       try {
           InputStream is = fetch(urlString);
           Drawable drawable = Drawable.createFromStream(is, "src");
           if(drawable!=null){
           drawable.setBounds(0,0, 0 + drawable.getIntrinsicWidth(), 0 
                   + drawable.getIntrinsicHeight()); 
           }
           else
           {
               drawable=null;
           }
           return drawable;
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       } 
   }

	        private InputStream fetch(String urlString) throws MalformedURLException, IOException {
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpGet request = new HttpGet(urlString);
	            HttpResponse response = httpClient.execute(request);
	            return response.getEntity().getContent();
	        }
	    }
	}
	

}

