package com.example.drop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class GalleryScreen extends DrawerFragmentActivity {
	// private static final String TAG = "GALLERY";
	private GalleryPageAdapter pageAdapter;
	private ViewPager pager;
	private final int LAZY_NUM = 8;
	private int pos;
	private int position;
	private ArrayList <Fragment> fragList;
	private File[] fileList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View layout =  getLayoutInflater().inflate(R.layout.activity_gallery, null);
        FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
        frame.addView(layout);

		fragList = new ArrayList<Fragment>();
		getFragments();
		
		pageAdapter = new GalleryPageAdapter(getSupportFragmentManager(),
				fragList);

		// This is where we get the position passed from the SavedListScreen
		if (this.getIntent().getExtras() != null) {
			position = this.getIntent().getExtras()
					.getInt(SavedListAdapter.GRID_ITEM_POS);
		}
		
		pos = position - LAZY_NUM/2;
		if(pos < 0)
		{
			pos = 0;
		}

		pager = (ViewPager) findViewById(R.id.galleryViewPager);
		pager.setAdapter(pageAdapter);
		pager.setOnPageChangeListener(new OnPageChangeListener(){

			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			public void onPageSelected(int page) {
				//If we are near the ends of the loaded pages => load more pages
				if(page < position - LAZY_NUM/4)
				{
					position = page;
					updateFragmentList(true);
				}
				else if(page > position + LAZY_NUM/4)
				{
					position = page;
					updateFragmentList(false);
				}
				
			}
			
		});

	}

	/** Called when the activity resumes **/
	@Override
	public void onResume() {
		super.onResume();
		// And make the ViewPager item in the same position the current item
		pager.setCurrentItem(position);
	}
	
	private void updateFragmentList(boolean less)
	{
		if(less)
		{
			//Remove fragments from right
			for(int i = 0; i < LAZY_NUM/4; i++)
			{
				fragList.remove(fragList.get(fragList.size()-1));
			}
			
			//Update the position
			pos = position - LAZY_NUM/2;
			if(pos < 0)
			{
				pos = 0;
			}
			
			//Add new fragments to the beginning
			for(int i = pos; i < pos + LAZY_NUM/4; i++)
			{
				Note note = new Note();
				try {
					FileInputStream fin = new FileInputStream(
							fileList[i].getAbsolutePath());
					ObjectInputStream ois = new ObjectInputStream(fin);
					note = (Note) ois.readObject();
					ois.close();
				} catch (OptionalDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				fragList.add(i, ViewNoteFragment.newInstance(note, true, false));
			}
		}
		else
		{
			//Remove from the left
			for(int i = 0; i < LAZY_NUM/4; i++)
			{
				fragList.remove(0);
			}
			
			//Update the position
			pos = position + LAZY_NUM/2;
			if(pos > fileList.length-1)
			{
				pos = fileList.length-1;
			}
			
			//Add new fragments to the beginning
			for(int i = pos; i > pos - LAZY_NUM/4; i--)
			{
				Note note = new Note();
				try {
					FileInputStream fin = new FileInputStream(
							fileList[i].getAbsolutePath());
					ObjectInputStream ois = new ObjectInputStream(fin);
					note = (Note) ois.readObject();
					ois.close();
				} catch (OptionalDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				fragList.add(fileList.length-1-i, ViewNoteFragment.newInstance(note, true, false));
			}
		}
	}

	private void getFragments() {
		File directory = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + Drop.SAVED_NOTE_DIR);
		Note note = new Note();

		Log.i("DROPPED_LIST_ASYNC", directory.getAbsolutePath());
		fileList = directory.listFiles();
		
		for(int i = 0; i < fileList.length / 2; i++)
		{
			File temp = fileList[i];
		    fileList[i] = fileList[fileList.length - i - 1];
		    fileList[fileList.length - i - 1] = temp;
		}

		if (fileList != null) {

			for (int i = pos; i < pos+LAZY_NUM; i++) {
				if(i > fileList.length-1)
				{
					break;
				}
				try {
					FileInputStream fin = new FileInputStream(
							fileList[i].getAbsolutePath());
					ObjectInputStream ois = new ObjectInputStream(fin);
					note = (Note) ois.readObject();
					ois.close();
				} catch (OptionalDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				fragList.add(ViewNoteFragment.newInstance(note, true, false));
			}
		}
	}

}