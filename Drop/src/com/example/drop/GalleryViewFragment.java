package com.example.drop;

import java.io.File;
import java.io.FileInputStream;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//@SuppressLint("ValidFragment")
public class GalleryViewFragment extends Fragment {

	//private File pictureFile;
	private int imageURI;
	private String message;
	
	public GalleryViewFragment(){}
	
	public void setArguments(Bundle b)
	{
		if (b.containsKey("IMAGE_URI")){
			imageURI = b.getInt("IMAGE_URI");
		}
		if (b.containsKey("MESSAGE")){
			message = b.getString("MESSAGE");
		}
			
	}

	
	@SuppressLint("ValidFragment")
	public static final GalleryViewFragment newInstance(int imageURI, String text)
	{
		Bundle newFragBundle = new Bundle();		
		newFragBundle.putInt("IMAGE_URI", imageURI);
		newFragBundle.putString("MESSAGE", text);
		
		GalleryViewFragment frag = new GalleryViewFragment();
		frag.setArguments(newFragBundle);
		
	    return frag;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_gallery_layout, container, false);

        Log.i("FRAGMENT", ""+imageURI);
        
        ImageView picture = (ImageView)rootView.findViewById(R.id.gallery_fragment_imageView);
        TextView text = (TextView)rootView.findViewById(R.id.gallery_fragment_textView);
        picture.setImageDrawable(getResources().getDrawable(imageURI));
        text.setText(message);
        
        return rootView;
    }
}