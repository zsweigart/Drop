package com.example.drop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class GeofenceRegistrationService extends IntentService {

	private ArrayList<Note> newNotes;	
	private GeofenceRequester gfRequester;
	private static String TAG = "GeofenceRegistrationService";
	
	public GeofenceRegistrationService() {
		super("GeofenceRegistrationService");
		// TODO Auto-generated constructor stub
		gfRequester = new GeofenceRequester();
		
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		new RegisterNewNotesAsyncTask().execute();
		
		
//		String fbId = intent.getStringExtra("facebookId");
//		
//		while(!playServicesAvailable()){
//			//wait, I guess.
//			
//		}
//		Log.d(TAG, "Google Play Services Available!");
//		
//		Log.d(TAG, fbId);
//		newNotes = DatabaseConnector.getNewNotes(fbId);
//		
//		if (newNotes.size() > 0)
//		{
//			Log.d(TAG, "New Notes Found!");
//			List<Geofence> newGeofences = new ArrayList<Geofence>();
//			//For each note we want to register with Google Play Services 
//			for (Note newNote : newNotes)
//			{
//				Log.d(TAG, "New Note Registered! "+newNote.getId());
//				//Create a Geofence object with the values from the new Note
//				newGeofences.add(new SimpleGeofence(newNote.getId(),
//													newNote.getLat(),
//													newNote.getLon(),
//													newNote.getRadius(),												
//													Geofence.NEVER_EXPIRE, // Wait until they're found
//													Geofence.GEOFENCE_TRANSITION_ENTER) // Only care about entry events
//													.toGeofence());
//			}
//			gfRequester.addGeofences(newGeofences);
//		} else {
//			Log.d(TAG, "No New Notes!");
//		}
		
	}
	
	private boolean playServicesAvailable(){
		return GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) == ConnectionResult.SUCCESS;
	}
	
	private class RegisterNewNotesAsyncTask extends
	AsyncTask<Intent, Void, List<Geofence>> {		
		
		List<Geofence> newGeofences = new ArrayList<Geofence>();
		@Override
		protected List<Geofence> doInBackground(Intent... intent) {		
			String fbId = intent[0].getStringExtra("facebookId");
			
			while(!playServicesAvailable()){			
				try {
					Thread.sleep(1000); //wait a second so we don't just spam 
				} catch (InterruptedException e) {						
					Log.e(TAG, "Interrupted Exception!");
				} 
			}
			Log.d(TAG, "Google Play Services Available!");
			
			Log.d(TAG, fbId);
			newNotes = DatabaseConnector.getNewNotes(fbId);
			
			if (newNotes.size() > 0)
			{
				Log.d(TAG, "New Notes Found!");
				List<Geofence> newGeofences = new ArrayList<Geofence>();
				
				//For each note we want to register with Google Play Services 
				for (Note newNote : newNotes)
				{
					Log.d(TAG, "New Note Registered! "+newNote.getId());
					//Create a Geofence object with the values from the new Note
					newGeofences.add(new SimpleGeofence(newNote.getId(),
														newNote.getLat(),
														newNote.getLon(),
														newNote.getRadius(),												
														Geofence.NEVER_EXPIRE, // Wait until they're found
														Geofence.GEOFENCE_TRANSITION_ENTER) // Only care about entry events
														.toGeofence());
				}
			}
			return newGeofences;
		}
		
		@Override
		protected void onPostExecute(List<Geofence> result)
		{
			gfRequester.addGeofences(newGeofences); 
		}
		
	}
	

}
