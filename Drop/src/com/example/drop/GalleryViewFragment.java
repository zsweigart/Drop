package com.example.drop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class GalleryViewFragment extends Fragment {

	public static final String IMAGE_URI_KEY="IMAGE_URI";
	public static final String MESSAGE_KEY="MESSAGE";
	
	private int imageURI;
	private String message;
	
	public GalleryViewFragment(){}
	
	@Override
	public void setArguments(Bundle b)
	{
		if (b.containsKey(IMAGE_URI_KEY)){
			imageURI = b.getInt(IMAGE_URI_KEY);
		}
		if (b.containsKey(MESSAGE_KEY)){
			message = b.getString(MESSAGE_KEY);
		}
	
	}
	
	public GalleryViewFragment withArguments(Bundle b){
		if (b.containsKey(IMAGE_URI_KEY)){
			imageURI = b.getInt(IMAGE_URI_KEY);
		}
		if (b.containsKey(MESSAGE_KEY)){
			message = b.getString(MESSAGE_KEY);
		}
	
		return this;
	}

	
	public static final GalleryViewFragment newInstance(int imageURI, String text)
	{
		Bundle newFragBundle = new Bundle();		
		newFragBundle.putInt(IMAGE_URI_KEY, imageURI);
		newFragBundle.putString(MESSAGE_KEY, text);
		
		GalleryViewFragment frag = new GalleryViewFragment().withArguments(newFragBundle);		
		
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