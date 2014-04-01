package com.example.drop;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
>>>>>>> Development

public class LoginScreen extends Activity {

	private SharedPreferences prefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        Button nextButton = (Button)findViewById(R.id.loginscreen_button_login);
        nextButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent i = new Intent(LoginScreen.this, CameraScreen.class);
                startActivity(i);
                
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("loggedIn", true); // value to store
                editor.commit();
 
                // close this activity
                finish();
			}
        	
        });
    }

    
}
