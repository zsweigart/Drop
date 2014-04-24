package com.example.drop;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


public class LoginScreen extends Activity {

	protected static final String TAG = "LOGIN";
	private SharedPreferences prefs;
	ProgressDialog progressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        
        PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo("com.example.drop",  PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        Button loginButton = (Button)findViewById(R.id.loginscreen_button_login);
        loginButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				onLoginButtonClicked();
			}
        });
        
    }
    
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
                    
                    Drop.loggedInUser = user;

    				Intent i = new Intent(LoginScreen.this, CameraScreen.class);
                    startActivity(i);
                    
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("loggedIn", true); // value to store
                    editor.commit();
     
                    // close this activity
                    finish();
                } else {
                    Log.d(TAG,
                            "User logged in through Facebook!");
                    
                    Drop.loggedInUser = user;

    				Intent i = new Intent(LoginScreen.this, CameraScreen.class);
                    startActivity(i);
                    
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("loggedIn", true); // value to store
                    editor.commit();
     
                    // close this activity
                    finish();
                }
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
    
}
