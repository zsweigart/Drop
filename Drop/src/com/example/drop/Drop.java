package com.example.drop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class Drop extends Application {

	static final String TAG = "DROP";	//Used for Logging
	static final String PICTURE_DIR = "/Android/data/com.example.drop/pictures";
	static final String DROPPED_NOTE_DIR = "/Android/data/com.example.drop/dropped";
	static final String SAVED_NOTE_DIR = "/Android/data/com.example.drop/saved";
	static ParseUser loggedInUser;		//Hold the parse information for the logged in user
	static JSONObject loggedInJSON;		//Holds the facebook information for the logged in user
	static Note current_note;			//Holds the note about to be dropped
	static final int CAMERA_INTENT_REQUEST = 1;
	static int currentPage = 1;

	@Override
	public void onCreate() {
		super.onCreate();

		// This allows read access to all objects
		ParseACL defaultACL = new ParseACL();

		defaultACL.setPublicReadAccess(true);
		defaultACL.setPublicWriteAccess(true);

		//Connect the app to the Parse backend
		ParseACL.setDefaultACL(defaultACL, true);
		Parse.initialize(this, Drop.this.getString(R.string.parse_id),
				Drop.this.getString(R.string.parse_client_key));
		ParseFacebookUtils.initialize(Drop.this.getString(R.string.app_id));
		makeMeRequest();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs() // Remove for release app
		.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	//Get the Facebook information for the logged in parse user
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
							} catch (JSONException e) {
								Log.d(TAG,
										"Error parsing returned user data.");
							}
							
							loggedInJSON = userProfile;
							
							//Hopefully, this will grab your new notes and register geofences for them
							Intent regServiceIntent = new Intent(getApplicationContext(), GeofenceRegistrationService.class);
							try {
								regServiceIntent.putExtra("facebookId", loggedInJSON.getString("facebookId"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Log.d("Drop", "GeofenceRegistrationService starting");
							startService(regServiceIntent);
						}
					}
				});
		request.executeAsync();

	}
	
	// Create a File for saving an image 
		public static File getOutputMediaFile(String path){
			
			File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + path);

		    // Create the storage directory if it does not exist
		    if (! mediaStorageDir.exists()){
		        if (! mediaStorageDir.mkdirs()){
		            return null;
		        }
		    } 
		    
		    // Create a media file name
		    String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm_ssSSS").format(new Date());
		    File mediaFile;
		        String mImageName="MI_"+ timeStamp +".jpg";
		        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);  
		    return mediaFile;
		} 
}