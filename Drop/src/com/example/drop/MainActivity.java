package com.example.drop;

import java.io.File;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends DrawerFragmentActivity {
    static final int ITEMS = 3;
    MyAdapter mAdapter;
    ViewPager mPager;
    
    static MainActivity init()
    {
    	MainActivity activity = new MainActivity();
    	return activity;
    }
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        
        View layout = getLayoutInflater().inflate(
				R.layout.activity_main, null);
		FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
		frame.addView(layout);
				
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(new OnPageChangeListener(){

			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			public void onPageSelected(int position) {
				Log.i("MAIN", "PAGE SELECTED: " +position);
				Drop.currentPage = position;
				if(position == 0)
				{
					SavedListFragment fragment = (SavedListFragment)findFragmentByPosition(position);
					if(fragment != null)
					{
						fragment.updateList();
					}
				}
				
				if(position == 2)
				{
					DroppedListFragment fragment = (DroppedListFragment)findFragmentByPosition(position);
					if(fragment != null)
					{
						fragment.updateList();
					}
				}
				
			}
        	
        });
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	mPager.setCurrentItem(Drop.currentPage);
    	if(Drop.current_note != null)
    	{
    		if(!Drop.current_note.isDropped())
    		{
    			File file = Drop.current_note.getPictureFile();
    			file.delete();
    		}
    	}
    }

    public static class MyAdapter extends FragmentPagerAdapter  {
        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
 
        @Override
        public int getCount() {
            return ITEMS;
        }
 
        @Override
        public Fragment getItem(int position) {
        	System.gc();
        	switch(position)
        	{
        	case 0:
        		return SavedListFragment.init();
        	case 1:
        		//return CameraFragment.init();
        		return CameraSurfaceFragment.init();
        	default:
        		return DroppedListFragment.init();
        	}
        }
    }
    
    public Fragment findFragmentByPosition(int position) {
        return getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + mPager.getId() + ":"
                        + mAdapter.getItemId(position));
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent retData) {
		super.onActivityResult(requestCode, resultCode, retData);
		Log.i("MAIN", "ACTIVITY RESULT REQUEST CODE: " + requestCode);
		switch(requestCode)
		{
		case Drop.CAMERA_INTENT_REQUEST:
			if(resultCode == RESULT_OK)	//Picture was taken move to the Draw Screen
			{
				Log.i("MAIN", Drop.current_note.getPictureFile().toString());
				Intent i = new Intent(MainActivity.this, DrawScreen.class);
				i.putExtra("image", Drop.current_note.getPictureFile());
				startActivity(i);
			}
			break;
		}		
	}
}