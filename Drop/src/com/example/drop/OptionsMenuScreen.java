package com.example.drop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

/*
 * This allows every activity to have the same options menu in the action bar
 * Define all menu settings here
 */


public class OptionsMenuScreen extends Activity {
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.options_menu_screen, menu);
	        return true;
	    }
	
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     // Handle item selection
	     switch (item.getItemId()) {
	         case R.id.menu_settings:
	        	 startActivity(new Intent(getApplicationContext(), Settings.class));
	             return true;
	         case R.id.gallery:
	        	 startActivity(new Intent(getApplicationContext(), GalleryScreen.class));
	        	 return true;
	         case R.id.saved:
	        	 startActivity(new Intent(getApplicationContext(), SavedListFragment.class));
	        	 return true;
	         case R.id.map:
	        	 startActivity(new Intent(getApplicationContext(), MapScreen.class));
	        	 return true;
	         case R.id.drawer:
	        	 Log.i("OPTION", "DRAWER");
	        	 startActivity(new Intent(getApplicationContext(), DrawerFragmentActivity.class));
	        	 return true;
	         case R.id.logout:
	        	 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	        	 SharedPreferences.Editor editor = prefs.edit();
	        	 editor.putBoolean("loggedIn", false); // value to store
	        	 editor.commit();
	        	 // Log the user out
	        	 ParseUser.logOut();
	        	 Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
	        	 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	        	 startActivity(intent);
	        	 return true;
	         default:
	             return super.onOptionsItemSelected(item);
	     }
	 }

}
