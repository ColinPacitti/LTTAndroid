package com.example.linustechtips;

import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
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
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


public class MyAdapter extends ArrayAdapter<ThreadName>{
	 Context context;
	 int position1;
	 ThreadName[] values = null;
	 private ImageLoader ImageLoader;
	 
	public MyAdapter(Context context, int position1, ThreadName[] values){
		super(context, position1, values);
		this.position1 = position1;
		this.context = context;
		this.values = values;
		
		this.ImageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
		.discCacheExtraOptions(60, 60, CompressFormat.JPEG, 75, null)
		.build();
		ImageLoader.init(config);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){

		View row = convertView;
		ThreadHolder holder = null;
		
		if(row == null){
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    row = inflater.inflate(R.layout.rowlayout, parent, false);
		
        
			holder = new ThreadHolder();
        
			holder.image = (ImageView) row.findViewById(R.id.icon);
			holder.title = (TextView) row.findViewById(R.id.title);
			holder.commentCount = (TextView) row.findViewById(R.id.comments);
            //holder.date = (TextView) row.findViewById(R.id.date);
		
			row.setTag(holder);
		
		}else{
			holder = (ThreadHolder)row.getTag();
		}
		
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
        .showImageOnLoading(R.drawable.grey)
        .resetViewBeforeLoading(true)
		.cacheOnDisc(true)
		.displayer(new FadeInBitmapDisplayer(740))
		.build();
		
		ThreadName threadname = values[position];
		holder.title.setText(threadname.title);
		holder.commentCount.setText("Replies " + threadname.comments.substring(13));
        //holder.date.setText(threadname.date);
		ImageLoader.displayImage(threadname.image,holder.image, defaultOptions);

		return row;
	}

	static class ThreadHolder{
		ImageView image;
		TextView title;
		TextView commentCount;
        TextView date;
	}
	
}
