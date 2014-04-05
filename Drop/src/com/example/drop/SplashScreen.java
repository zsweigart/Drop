package com.example.drop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;


public class SplashScreen extends Activity {
	boolean loggedIn;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
 
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
