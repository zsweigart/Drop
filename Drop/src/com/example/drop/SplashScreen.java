package com.example.drop;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class SplashScreen extends OptionsMenuScreen {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //Set default preferences (won't override user preferences)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_splash_screen, menu);
//        return true;
//    }
    
//    @Override
//	 public boolean onOptionsItemSelected(MenuItem item) {
//	     // Handle item selection
//	     switch (item.getItemId()) {
//	         case R.id.menu_settings:
//	        	 startActivity(new Intent(getApplicationContext(), Settings.class));
//	             return true;       	         
//	         default:
//	             return super.onOptionsItemSelected(item);
//	     }
//	 }
}
