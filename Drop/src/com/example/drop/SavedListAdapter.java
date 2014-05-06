package com.example.drop;

import java.util.ArrayList;

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
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class SavedListAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<Note> data;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private static LayoutInflater inflater = null;
	public static final String GRID_ITEM_POS = "GRID_ITEM_POS";
	DisplayImageOptions options;

	class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
	}

	public SavedListAdapter(Activity a, ArrayList<Note> d) {
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
		.bitmapConfig(Bitmap.Config.RGB_565)
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

	// create a new ImageView for each item referenced by the Adapter
	public View getView(final int position, View convertView, ViewGroup parent) {
		Note note = data.get(position);
		final ViewHolder holder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.item_grid_image,
					parent, false);
			holder = new ViewHolder();
			assert view != null;
			holder.imageView = (ImageView) view.findViewById(R.id.image);
			holder.imageView.setOnClickListener(new OnClickListener(){

				public void onClick(View arg0) {
					Log.d("SavedListViewAdapter", "Clicked on position "
							+ position);
					Intent launchGallery = new Intent(activity,
							GalleryScreen.class);
					launchGallery.putExtra("type", "saved");
					launchGallery.putExtra("GRID_ITEM_POS", position);
					activity.startActivity(launchGallery);					
				}
				
			});
			holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		imageLoader.displayImage(Uri.fromFile(note.getPictureFile()).toString(), holder.imageView,
				options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						holder.progressBar.setProgress(0);
						holder.progressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						holder.progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						holder.progressBar.setVisibility(View.GONE);
					}
				}, new ImageLoadingProgressListener() {
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {
						holder.progressBar.setProgress(Math.round(100.0f
								* current / total));
					}
				});
		
		holder.imageView.setScaleType(ScaleType.CENTER_CROP);

		return view;
	}
}
