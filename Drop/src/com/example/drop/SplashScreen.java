package com.example.drop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.parse.ParseAnalytics;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


public class SplashScreen extends Activity {
	boolean loggedIn;	//Used to determine if user is logged in
    private static int SPLASH_TIME_OUT = 500;	// Splash screen timer
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        
        ParseAnalytics.trackAppOpened(getIntent());
 
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		//Set default preferences (won't override user preferences)
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    
        loggedIn = prefs.getBoolean("loggedIn", false);          

        new Handler().postDelayed(new Runnable() {
 
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	ParseUser currentUser = ParseUser.getCurrentUser();
            	if(loggedIn && (currentUser != null) && ParseFacebookUtils.isLinked(currentUser))
            	{
	                Intent i = new Intent(SplashScreen.this, MainActivity.class);

	                startActivity(i);
	 
	                // close this activity
	                finish();
            	}
            	else
            	{
            		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
   	        	 	SharedPreferences.Editor editor = prefs.edit();
   	        	 	editor.putBoolean("loggedIn", false); // value to store
   	        	 	editor.commit();
   	        	 	
            		Intent i = new Intent(SplashScreen.this, LoginScreen.class);
	                startActivity(i);
	 
	                // close this activity
	                finish();
            	}
            }
        }, SPLASH_TIME_OUT);
    }
 
}
