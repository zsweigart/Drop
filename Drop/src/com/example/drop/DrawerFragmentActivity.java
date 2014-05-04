package com.example.drop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseUser;

public class DrawerFragmentActivity extends FragmentActivity {
	 private DrawerLayout mDrawerLayout;
	    private ListView mDrawerList;
	    private ActionBarDrawerToggle mDrawerToggle;

	    private CharSequence mTitle;
	    private String[] mPlanetTitles;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_drawer);

	        mTitle = getTitle();
	        mPlanetTitles = getResources().getStringArray(R.array.nav_drawer_items);
	        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	        mDrawerList = (ListView) findViewById(R.id.left_drawer);

	        // set a custom shadow that overlays the main content when the drawer opens
	        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	        // set up the drawer's list view with items and click listener
	        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
	                R.layout.drawer_list_item, R.id.tv, mPlanetTitles));
	        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

	        // enable ActionBar app icon to behave as action to toggle nav drawer
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        getActionBar().setHomeButtonEnabled(true);

	        // ActionBarDrawerToggle ties together the the proper interactions
	        // between the sliding drawer and the action bar app icon
	        mDrawerToggle = new ActionBarDrawerToggle(
	                this,                  /* host Activity */
	                mDrawerLayout,         /* DrawerLayout object */
	                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
	                R.string.drawer_open,  /* "open drawer" description for accessibility */
	                R.string.drawer_closed  /* "close drawer" description for accessibility */
	                ) {
	            public void onDrawerClosed(View view) {
	                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	            }

	            public void onDrawerOpened(View drawerView) {
	                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	            }
	        };
	        mDrawerLayout.setDrawerListener(mDrawerToggle);
	    }

	   
	    /* Called whenever we call invalidateOptionsMenu() */
	    @Override
	    public boolean onPrepareOptionsMenu(Menu menu) {
	        return super.onPrepareOptionsMenu(menu);
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	         // The action bar home/up action should open or close the drawer.
	         // ActionBarDrawerToggle will take care of this.
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	        // Handle action buttons
	        switch(item.getItemId()) {
	        	
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }

	    /* The click listner for ListView in the navigation drawer */
	    private class DrawerItemClickListener implements ListView.OnItemClickListener {
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            selectItem(position);
	        }
	    }

	    private void selectItem(int position) {
	        mDrawerList.setItemChecked(position, true);
	        Intent i;
	        switch (position)
	        {
	        case 0: 	//leave note
	        	Drop.currentPage = 1;
	        	i = new Intent(getApplicationContext(), MainActivity.class);
	        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(i);
	        	break;
	        case 1:		//dropped
	        	Drop.currentPage = 2;
	        	i = new Intent(getApplicationContext(), MainActivity.class);
	        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	DroppedListFragment.updateDropped = true;
	        	startActivity(i);
	        	break;
	        case 2:		//saved
	        	Drop.currentPage = 0;
	        	i = new Intent(getApplicationContext(), MainActivity.class);
	        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	DroppedListFragment.updateDropped = true;
	        	startActivity(i);
	        	break;
	        case 3:		//map
	        	Drop.currentPage = 0;
	        	i = new Intent(getApplicationContext(), MainActivity.class);
	        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(i);;
	        	break;
	        case 4:		//settings
	        	i = new Intent(getApplicationContext(), Settings.class);
	        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(i);
	        	break;
        	case 5:		//log out
    			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	        	SharedPreferences.Editor editor = prefs.edit();
	        	editor.putBoolean("loggedIn", false); // value to store
	        	editor.commit();
	        	// Log the user out
	        	ParseUser.logOut();
	        	Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
	        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	        	startActivity(intent);
	        	break;
	        }
	        mDrawerLayout.closeDrawer(mDrawerList);
	    }

	    @Override
	    public void setTitle(CharSequence title) {
	        mTitle = title;
	        getActionBar().setTitle(mTitle);
	    }

	    /**
	     * When using the ActionBarDrawerToggle, you must call it during
	     * onPostCreate() and onConfigurationChanged()...
	     */

	    @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	        mDrawerToggle.syncState();
	    }

	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        // Pass any configuration change to the drawer toggls
	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }
}
