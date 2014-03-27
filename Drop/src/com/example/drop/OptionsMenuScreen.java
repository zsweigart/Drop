package com.example.drop;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

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
	        	 startActivity(new Intent(getApplicationContext(), Gallery.class));
	        	 return true;
	         default:
	             return super.onOptionsItemSelected(item);
	     }
	 }

}
