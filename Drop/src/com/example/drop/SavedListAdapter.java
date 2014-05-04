package com.example.drop;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class SavedListAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<Note> data;
	public ImageLoader imageLoader;
	private final int imageHeight = 300;
	private final int imageWidth = 300;
	public static final String GRID_ITEM_POS = "GRID_ITEM_POS";

	public SavedListAdapter(Activity a, ArrayList<Note> d) {
		activity = a;
		data = d;
		imageLoader = new ImageLoader(activity.getApplicationContext());
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
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(activity);
            imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageHeight));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        //We should have a list of thumbails for this
        imageView.setImageBitmap(data.get(position).getPicture(imageHeight, imageWidth));
        
        imageView.setOnClickListener(new OnClickListener() {
				// When you click an item in the grid, grab the position and
				// send it on to the Gallery
				public void onClick(View v) {
				// TODO Auto-generated method stub
					Intent launchGallery = new Intent(activity,
							GalleryScreen.class);
					launchGallery.putExtra(GRID_ITEM_POS, position);
					activity.startActivity(launchGallery);
			}
		});
        
        return imageView;
    }
}
