package com.example.drop;


import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.*;
import com.facebook.model.*;
import com.parse.LogInCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.ParseException;


public class LoginScreen extends Activity {

	protected static final String TAG = null;
	private SharedPreferences prefs;
	ProgressDialog progressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        // start Facebook Login
        /*Session.openActiveSession(this, true, new Session.StatusCallback() {

            // callback when session changes state
			public void call(Session session, SessionState state, Exception exception) {
              if (session.isOpened()) {

                // make request to the /me API
                Request.newMeRequest(session, new Request.GraphUserCallback() {

                  // callback after Graph API response with user object
                  public void onCompleted(GraphUser user, Response response) {
                	Log.i("LOGIN", "ONCOMPLETED");
                    if (user != null) {
                    	Log.i("LOGIN", "USER NOT NULL");
                    	TextView welcome = (TextView) findViewById(R.id.loginscreen_welcome);
                    	welcome.setText("Hello " + user.getName() + "!");
                    }
                  }
                });
              }
            }
          });*/
        
        Button loginButton = (Button)findViewById(R.id.loginscreen_button_login);
        loginButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				onLoginButtonClicked();
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
    
    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }*/
    
    private void onLoginButtonClicked() {
        LoginScreen.this.progressDialog = ProgressDialog.show(
        		LoginScreen.this, "", "Logging in...", true);
        List<String> permissions = Arrays.asList("basic_info", "user_about_me",
                "user_relationships", "user_birthday", "user_location");
        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
            	LoginScreen.this.progressDialog.dismiss();
                if (user == null) {
                    Log.d(TAG,
                            "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d(TAG,
                            "User signed up and logged in through Facebook!");
                } else {
                    Log.d(TAG,
                            "User logged in through Facebook!");
                }
            }
        });
    }
    
}
