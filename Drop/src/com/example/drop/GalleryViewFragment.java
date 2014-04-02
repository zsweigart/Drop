package com.example.drop;

import java.io.File;
import java.io.FileInputStream;

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

public class GalleryViewFragment extends Fragment {

	private File pictureFile;
	private String message;
	
	public GalleryViewFragment(File f, String text)
	{
		pictureFile = f;
		message = text;
	}
	
	public static final GalleryViewFragment newInstance(File f, String text)
	{
		GalleryViewFragment frag = new GalleryViewFragment(f, text);
	    return frag;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_gallery_layout, container, false);

        Log.i("FRAGMENT", ""+pictureFile.getName()+"    " + pictureFile.length());
        byte [] data  = new byte[(int) pictureFile.length()];
        
        try 
        {
            //convert file into array of bytes
        	FileInputStream fileInputStream = new FileInputStream(pictureFile);
	    	fileInputStream.read(data);
	    	fileInputStream.close();
        }
        catch(Exception e)
        {
        	Log.i("FRAGMENT", "ERROR OPENING FILE");
        	e.printStackTrace();
        }
        
        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
        if(b == null)
        {
        	Log.i("FRAGMENT", "B is NULL");
        }
        
        ImageView picture = (ImageView)rootView.findViewById(R.id.gallery_fragment_imageView);
        TextView text = (TextView)rootView.findViewById(R.id.gallery_fragment_textView);
        picture.setImageBitmap(b);
        Log.i("FRAGMENT", ""+b.getHeight());
        picture.getLayoutParams().height = b.getHeight();
        picture.getLayoutParams().width = b.getWidth();
        text.setText(message);
        
        
        return rootView;
    }
}