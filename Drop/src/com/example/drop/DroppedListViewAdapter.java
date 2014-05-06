package com.example.drop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DroppedListViewAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<Note> data;
	private static LayoutInflater inflater = null;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	private class ViewHolder {
		public ImageView picture;
		public TextView noteText;
		public TextView recipients;
		public RelativeLayout rowView;
	}

	public DroppedListViewAdapter(Activity a, ArrayList<Note> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.drop_icon)
		.showImageForEmptyUri(R.drawable.drop_icon)
		.showImageOnFail(R.drawable.drop_icon)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
	}

	public int getCount() {
		return data.size();
	}

	public Note getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final Note note = data.get(position);
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.dropped_listview_item, null);
			holder = new ViewHolder();
			holder.picture = (ImageView) view.findViewById(R.id.dropped_row_imageView);
			holder.picture.setScaleType(ScaleType.CENTER_CROP);
			holder.noteText = (TextView) view.findViewById(R.id.dropped_row_note_content);
			holder.recipients = (TextView) view.findViewById(R.id.dropped_row_recievers);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.noteText.setText(note.getMessage());
		
		StringBuilder sb = new StringBuilder();
		ArrayList<String> r = note.getReceivers();
		for (int i = 0; i < r.size(); i++) {
			try {
				JSONObject obj = new JSONObject(r.get(i));
				//Append first and last initial to receiver list				
				String fullName = obj.getString("name");							
				String[] firstAndLast = fullName.split("\\s+");				
				sb.append(firstAndLast[0]);//Add first name
				if(firstAndLast.length > 1) // If there's a last name, add the initial and a period
				{
					sb.append(" "+firstAndLast[1].substring(0, 1)+".");					
				}
				if(i < r.size()-1) //unless it's the last name in the list, add comma
				{
					sb.append(", ");					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		holder.recipients.setText(sb.toString());
		
		holder.rowView = (RelativeLayout) view
				.findViewById(R.id.dropped_row);

		holder.rowView.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Log.d("DroppedListViewAdapter", "Clicked on position "
						+ position);
				Intent launchGallery = new Intent(activity,
						GalleryScreen.class);
				launchGallery.putExtra("type", "dropped");
				launchGallery.putExtra("GRID_ITEM_POS", position);
				activity.startActivity(launchGallery);
			}

		});
		
		imageLoader.displayImage(Uri.fromFile(note.getPictureFile()).toString(), holder.picture, options, animateFirstListener);

		return view;

	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
