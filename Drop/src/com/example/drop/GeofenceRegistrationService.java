package com.example.drop;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class GeofenceRegistrationService extends IntentService {

	private ArrayList<Note> newNotes;	
	private GeofenceRequester gfRequester;
	
	public GeofenceRegistrationService() {
		super("GeofenceRegistrationService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub		
		
		try {
			newNotes = DatabaseConnector.getNewNotes(Drop.loggedInJSON.getString("facebookId"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) == 1)
		{
			List<Geofence> newGeofences = new ArrayList<Geofence>();
			//For each note we want to register with Google Play Services 
			for (Note newNote : newNotes)
			{
				Log.d("GeofenceRegistrationService", "New Note Registerd! "+newNote.getId());
				//Create a Geofence object with the values from the new Note
				newGeofences.add(new SimpleGeofence(newNote.getId(),
													newNote.getLat(),
													newNote.getLon(),
													newNote.getRadius(),												
													Geofence.NEVER_EXPIRE, // Wait until they're found
													Geofence.GEOFENCE_TRANSITION_ENTER) // Only care about entry events
													.toGeofence());
			}
			gfRequester.addGeofences(newGeofences);
		}
		
	}
	
	
	

}
