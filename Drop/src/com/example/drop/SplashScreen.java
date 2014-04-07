package com.example.drop;

import com.parse.Parse;
import com.parse.ParseAnalytics;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;


public class SplashScreen extends Activity {
	boolean loggedIn;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;
 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        
        Parse.initialize(this, "zdWe11jTmmRmb5wCR9dtQdF3GohrPyHeuLAzihnP", "VB2WIVxrbuzlY555vJ8Dt4LUp5s2y1vG7NCB71vU");
        ParseAnalytics.trackAppOpened(getIntent());
 
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		//Set default preferences (won't override user preferences)
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    
        loggedIn = prefs.getBoolean("loggedIn", false);
     
        new Handler().postDelayed(new Runnable() {
 
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	
            	if(loggedIn)
            	{
	                Intent i = new Intent(SplashScreen.this, CameraScreen.class);
	                startActivity(i);
	 
	                // close this activity
	                finish();
            	}
            	else
            	{
            		Intent i = new Intent(SplashScreen.this, LoginScreen.class);
	                startActivity(i);
	 
	                // close this activity
	                finish();
            	}
            }
        }, SPLASH_TIME_OUT);
    }
 
}
