package com.example.drop;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.util.Log;

import com.facebook.Request;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.facebook.Response;

public class Drop extends Application {

	static final String TAG = "DROP";
	static ParseUser loggedInUser;
	static JSONObject loggedInJSON;
	static Note current_note;

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
		makeMeRequest();

	}

	private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							// Create a JSON object to hold the profile info
							JSONObject userProfile = new JSONObject();
							try {
								// Populate the JSON object
								userProfile.put("facebookId", user.getId());
								userProfile.put("name", user.getName());
								if (user.getProperty("name") != null) {
									userProfile.put("name",
											(String) user.getProperty("name"));
								}
								if (user.getProperty("gender") != null) {
									userProfile.put("gender",
											(String) user.getProperty("gender"));
								}
								if (user.getBirthday() != null) {
									userProfile.put("birthday",
											user.getBirthday());
								}
								if (user.getProperty("relationship_status") != null) {
									userProfile
											.put("relationship_status",
													(String) user
															.getProperty("relationship_status"));
								}
								userProfile.put("checked", false);
								// Now add the data to the UI elements
								// ...

							} catch (JSONException e) {
								Log.d("RECIPIENTS",
										"Error parsing returned user data.");
							}
							
							loggedInJSON = userProfile;
						}
					}
				});
		request.executeAsync();

	}

}