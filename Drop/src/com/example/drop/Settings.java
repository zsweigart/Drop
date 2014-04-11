package com.example.drop;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.FrameLayout;


public class Settings extends DrawerActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);
        
        View layout =  getLayoutInflater().inflate(R.layout.activity_dropped_list_screen, null);
        FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
        frame.addView(layout);
        
     // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();       
    }  
	
	public static class SettingsFragment extends PreferenceFragment{
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.preferences);
	    }		
		
	}
	

}
