package com.example.drop;

import android.os.Bundle;
import android.preference.PreferenceManager;

public class SplashScreen extends OptionsMenuScreen {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //Set default preferences (won't override user preferences)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

}
