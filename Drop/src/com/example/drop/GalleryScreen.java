package com.example.drop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryScreen extends FragmentActivity  {
	private static final String TAG = "GALLERY";
	GalleryPageAdapter pageAdapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_gallery);
	    
	    List<Fragment> fragments = getFragments();
        
        pageAdapter = new GalleryPageAdapter(getSupportFragmentManager(), fragments);
        
        ViewPager pager = (ViewPager)findViewById(R.id.galleryViewPager);
        pager.setAdapter(pageAdapter);
	}
	
		
	private List<Fragment> getFragments(){
    	List<Fragment> fList = new ArrayList<Fragment>();    	
    	
    	fList.add(GalleryViewFragment.newInstance(R.drawable.sample_1, "This is a picture for the first page. It's page #1.This is a picture for the first page. It's page #1.This is a picture for the first page. It's page #1.This is a picture for the first page. It's page #1.This is a picture for the first page. It's page #1."));
    	fList.add(GalleryViewFragment.newInstance(R.drawable.sample_2, "This is a picture for the second page. It's page #2.This is a picture for the second page. It's page #2.This is a picture for the second page. It's page #2.This is a picture for the second page. It's page #2.This is a picture for the second page. It's page #2."));
    	fList.add(GalleryViewFragment.newInstance(R.drawable.sample_3, "This is a picture for the third page. It's page #3.This is a picture for the third page. It's page #3.This is a picture for the third page. It's page #3.This is a picture for the third page. It's page #3.This is a picture for the third page. It's page #3."));
    	
    	return fList;
    }

}