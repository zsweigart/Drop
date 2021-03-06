package com.example.drop;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class GeofenceRegistrationService extends IntentService {

	private GeofenceRequester gfRequester;
	private Handler handler;
	private static final int POLL_INTERVAL_MILLIS = 60*60*1000; // 1 hour
	private String fbId;
	private static String TAG = "GeofenceRegistrationService";
	
	public GeofenceRegistrationService() {
		super("GeofenceRegistrationService");
		
		gfRequester = new GeofenceRequester(this);		
		handler = new Handler();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		fbId = intent.getStringExtra("facebookId");
		
		
		final Runnable pollForNewNotes = new Runnable(){
			  
			public void run() {
				Log.d(TAG, "Calling pollForNewNotes("+fbId+")");
				pollForNewNotes();
				handler.postDelayed(this, POLL_INTERVAL_MILLIS);
			}
		};
		
		handler.post(pollForNewNotes);
		
	}
	
	private void pollForNewNotes()
	{
		while(!playServicesAvailable()){			
			try {
				Thread.sleep(1000); //wait a second so we don't just spam 
			} catch (InterruptedException e) {						
				Log.e(TAG, "Interrupted Exception!");
			} 
		}
		Log.d(TAG, "Google Play Services Available!");
		
		
		//** Ripped from DatabaseConnector **//
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Note"); 
		query.whereEqualTo("recipient", fbId);
		query.whereEqualTo("isPickedUp", false);
		
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> loadedNotes, ParseException arg1) {
				ArrayList <Geofence> newGeofences = new ArrayList <Geofence> ();
				if(arg1 != null){
					Log.e(TAG, arg1.toString());
				}

				Log.i("ParseQuery", "NUM NOTES: " + loadedNotes.size());

				for(int i = 0; i < loadedNotes.size(); i++)
				{
					Note n = new Note();
					n.setLat(loadedNotes.get(i).getDouble("lat"));
					n.setLon(loadedNotes.get(i).getDouble("lon"));
					n.setRadius((float) loadedNotes.get(i).getDouble("radius"));
					n.setId(loadedNotes.get(i).getObjectId());				
					Log.d(TAG, "New note: "+n.getId());					
					
					//Create a Geofence object with the values from the new Note
					newGeofences.add(new SimpleGeofence(n.getId(),
														n.getLat(),
														n.getLon(),
														n.getRadius(),												
														Geofence.NEVER_EXPIRE, // Wait until they're found
														Geofence.GEOFENCE_TRANSITION_ENTER) // Only care about entry events
														.toGeofence());
					Log.d(TAG, "New Note Registered! "+n.getId());
				}
				if(newGeofences.size() > 0)
				{					
					gfRequester.addGeofences(newGeofences);
					
				} else {
					Log.d(TAG, "No Geofences to Register");
				}
				
				
			} });
		//** RIPPED FROM DATABASE CONNECTOR **//	
	}
	
	private boolean playServicesAvailable(){
		return GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) == ConnectionResult.SUCCESS;
	}	

}
