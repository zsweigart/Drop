package com.example.drop;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;

public class Drop extends Application {

	static final String TAG = "DROP";

	@Override
	public void onCreate() {
		super.onCreate();

		// This allows read access to all objects
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
        Parse.initialize(this, Drop.this.getString(R.string.parse_id), 
        		Drop.this.getString(R.string.parse_client_key));
        ParseFacebookUtils.initialize(Drop.this.getString(R.string.app_id));

	}

}