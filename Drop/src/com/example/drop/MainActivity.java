/***
  Copyright (c) 2013 CommonsWare, LLC
  
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.example.drop;

import java.io.File;

import android.app.ActionBar;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

public class MainActivity extends DrawerFragmentActivity implements
		ActionBar.OnNavigationListener, MyCameraFragment.Contract {
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private static final String STATE_SINGLE_SHOT = "single_shot";
	private static final String STATE_LOCK_TO_LANDSCAPE = "lock_to_landscape";
	private static final int CONTENT_REQUEST = 1337;
	private MyCameraFragment std = null;
	private MyCameraFragment ffc = null;
	private MyCameraFragment current = null;
	private boolean hasTwoCameras = (Camera.getNumberOfCameras() > 1);
	private boolean singleShot = false;
	private boolean isLockedToLandscape = false;
	private SavedListFragment savedFrag;
	private DroppedListFragment droppedFrag;

	/**
	 * The number of pages (wizard steps) to show in this demo.
	 */
	private static final int NUM_PAGES = 3;

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;
	
	public static MainActivity init()
	{
		MainActivity activity = new MainActivity();
		return activity;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View layout = getLayoutInflater().inflate(
				R.layout.activity_main, null);
		FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
		frame.addView(layout);

		if (hasTwoCameras) {
			final ActionBar actionBar = getActionBar();

			//actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(actionBar.getThemedContext(),
							R.array.nav, android.R.layout.simple_list_item_1);

			actionBar.setListNavigationCallbacks(adapter, this);
		} else {
			current = MyCameraFragment.newInstance(false);

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, current).commit();
		}

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (hasTwoCameras) {
			if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
				getActionBar().setSelectedNavigationItem(
						savedInstanceState
								.getInt(STATE_SELECTED_NAVIGATION_ITEM));
			}
		}

		setSingleShotMode(savedInstanceState.getBoolean(STATE_SINGLE_SHOT));
		isLockedToLandscape = savedInstanceState
				.getBoolean(STATE_LOCK_TO_LANDSCAPE);

		if (current != null) {
			current.lockToLandscape(isLockedToLandscape);
		}
	}

	public void onSaveInstanceState(Bundle outState) {
		if (hasTwoCameras) {
			outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
					.getSelectedNavigationIndex());
		}

		outState.putBoolean(STATE_SINGLE_SHOT, isSingleShotMode());
		outState.putBoolean(STATE_LOCK_TO_LANDSCAPE, isLockedToLandscape);
	}

	public boolean onNavigationItemSelected(int position, long id) {
		if (position == 0) {
			if (std == null) {
				std = MyCameraFragment.newInstance(false);
			}

			current = std;
			current.setIsFFC(false);
		} else {
			if (ffc == null) {
				ffc = MyCameraFragment.newInstance(true);
			}

			current = ffc;
			current.setIsFFC(true);
		}

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, current).commit();

		findViewById(android.R.id.content).post(new Runnable() {

			public void run() {
				current.lockToLandscape(isLockedToLandscape);
			}
		});

		return (true);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.main, menu);

		menu.findItem(R.id.landscape).setChecked(isLockedToLandscape);

		return (super.onCreateOptionsMenu(menu));
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.content) {
			Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File dir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			File output = new File(dir, "CameraContentDemo.jpeg");

			i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));

			startActivityForResult(i, CONTENT_REQUEST);
		} else if (item.getItemId() == R.id.landscape) {
			item.setChecked(!item.isChecked());
			current.lockToLandscape(item.isChecked());
			isLockedToLandscape = item.isChecked();
		} else if (item.getItemId() == R.id.fullscreen) {
			//startActivity(new Intent(this, FullScreenActivity.class));
		}

		return (super.onOptionsItemSelected(item));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CONTENT_REQUEST) {
			if (resultCode == RESULT_OK) {
				// do nothing
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_CAMERA && current != null
				&& !current.isSingleShotProcessing()) {
			current.takePicture();

			return (true);
		}

		return (super.onKeyDown(keyCode, event));
	}

	public boolean isSingleShotMode() {
		return (singleShot);
	}

	public void setSingleShotMode(boolean mode) {
		singleShot = mode;
	}

	/**
	 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects,
	 * in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public Fragment getItem(int position) {
			switch(position)
        	{
        	case 0:
        		savedFrag = (SavedListFragment) SavedListFragment.init();
        		return savedFrag;
        	case 1:
        		//return CameraFragment.init();
        		return new CameraContainerFragment();
        	default:
        		droppedFrag =  (DroppedListFragment) DroppedListFragment.init();
        		return droppedFrag;
        	}
		}

		public int getCount() {
			return NUM_PAGES;
		}
	}
}
