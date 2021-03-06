package com.example.drop;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class GeofenceTransitionService extends IntentService {
	/*
	 * 1) Listen for Note Geofence entry transitions, and post a notification
	 * (that opens the Note in question) when those transitions occur
	 */

	private static String TAG = "GeofenceTransitionService";
	
	private GeofenceRemover gfRemover;

	/**
	 * Sets an identifier for this class' background thread
	 */
	public GeofenceTransitionService() {
		super("GeofenceTransitionService");
		gfRemover = new GeofenceRemover(this);
	}

	/**
	 * Handles incoming intents
	 * 
	 * @param intent
	 *            The Intent sent by Location Services. This Intent is provided
	 *            to Location Services (inside a PendingIntent) when you call
	 *            addGeofences()
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		// Create a local broadcast Intent
		Intent broadcastIntent = new Intent();

		// Give it the category for all intents sent by the Intent Service
		broadcastIntent.addCategory(LocationUtils.CATEGORY_LOCATION_SERVICES);

		// First check for errors
		if (LocationClient.hasError(intent)) {

			// Get the error code
			int errorCode = LocationClient.getErrorCode(intent);

			// Get the error message
			String errorMessage = LocationServiceErrorMessages.getErrorString(
					this, errorCode);

			// Log the error
			Log.e(LocationUtils.APPTAG,
					getString(R.string.geofence_transition_error_detail,
							errorMessage));

			// Set the action and error message for the broadcast intent
			broadcastIntent
					.setAction(LocationUtils.ACTION_GEOFENCE_ERROR)
					.putExtra(LocationUtils.EXTRA_GEOFENCE_STATUS, errorMessage);

			// Broadcast the error *locally* to other components in this app
			LocalBroadcastManager.getInstance(this).sendBroadcast(
					broadcastIntent);

			// If there's no error, get the transition type and create a
			// notification
		} else {

			// Get the type of transition (entry or exit)
			int transition = LocationClient.getGeofenceTransition(intent);

			// Test that a valid transition was reported
			if ((transition == Geofence.GEOFENCE_TRANSITION_ENTER)
					|| (transition == Geofence.GEOFENCE_TRANSITION_EXIT)) {

				// Post a notification
				List<Geofence> geofences = LocationClient
						.getTriggeringGeofences(intent);

				// Each triggering geofence creates a new notification
				for (int index = 0; index < geofences.size(); index++) {
					String id = geofences.get(index).getRequestId();
					String transitionType = getTransitionString(transition);
					Log.d(TAG, transitionType + " geofence " + id);
					sendNotification(transitionType, id);

					// Log the transition type and a message
					Log.d(LocationUtils.APPTAG,
							getString(
									R.string.geofence_transition_notification_title,
									transitionType, id));
					Log.d(LocationUtils.APPTAG,
							getString(R.string.geofence_transition_notification_text));
				}

				// An invalid transition was reported
			} else {
				// Always log as an error
				Log.e(LocationUtils.APPTAG,
						getString(R.string.geofence_transition_invalid_type,
								transition));
			}
		}
	}

	/**
	 * Posts a notification in the notification bar when a transition is
	 * detected. If the user clicks the notification, control goes to the main
	 * Activity.
	 * 
	 * @param transitionType
	 *            The type of transition that occurred.
	 * 
	 */
	private void sendNotification(final String transitionType, final String id) {

		// Create an explicit content Intent that starts the ViewNoteScreen
		// Activity
		final Intent notificationIntent = new Intent(getApplicationContext(),
				ViewNoteScreen.class);

		final Note n = new Note();

		Log.i("GEOFENCE_TRANSITION", id);

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Note");
		query.whereEqualTo("objectId", id);
		query.findInBackground(new FindCallback<ParseObject>() 
		{

			@Override
			public void done(List<ParseObject> loadedNotes, ParseException arg1) {

				if (arg1 != null) {
					Log.e(TAG, arg1.toString());
				}
				
				ParseObject loadedNote;
				if(loadedNotes.isEmpty())
				{
					//loadedNotes is empty! GHOST NOTE! REMOVE IT!
					List<String> fencesToRemove = new ArrayList<String>();
					fencesToRemove.add(id);
					gfRemover.removeGeofencesById(fencesToRemove);					
				}
				else
				{									
					loadedNote = loadedNotes.get(0);

					n.setLat(loadedNote.getDouble("lat"));
					n.setLon(loadedNote.getDouble("lon"));
					n.setRadius((float) loadedNote.getDouble("radius"));
					n.setId(loadedNote.getObjectId());
					n.setMessage(loadedNote.getString("message"));
					n.setCreator(loadedNote.getString("creator"));
	
					ParseFile fileObject = (ParseFile) loadedNote.get("picture");
				
					fileObject.getDataInBackground(new GetDataCallback() {
						public void done(byte[] data, ParseException e) {
							if (e == null) {
								Log.d("test", "We've got data in data.");
								// Write the picture byte[] to the picture File
								n.setPicture(Drop
										.getOutputMediaFile(Drop.PICTURE_DIR));
								try {
									FileOutputStream fos = new FileOutputStream(
											n.getPictureFile());
									fos.write(data);
									fos.close();
								} catch (FileNotFoundException z) {
									Log.d(TAG, "File not found: " + z.getMessage());
								} catch (IOException x) {
									Log.d(TAG,
											"Error accessing file: "
													+ x.getMessage());
								}
							} else {
								Log.d("test",
										"There was a problem downloading the data.");
							}
							Log.i("GEOFENCE_TRANSITION", "NOTE ID: " + n.getId());
	
							// Grab the note that we just found from the database and put it
							// in the notification intent
							// notificationIntent.putExtra(getString(R.string.note_id), id);
							// //note_id => requestId of the triggering geofence
							notificationIntent.putExtra("com.example.drop.Note", n);
							notificationIntent.putExtra("justPickedUp", true);
							
							// Construct a task stack
							TaskStackBuilder stackBuilder = TaskStackBuilder
									.create(GeofenceTransitionService.this);
	
							// Adds the View Note Activity to the task stack as the parent
							//stackBuilder.addParentStack(ViewNoteScreen.class);
							
							//Adds the MainActivity as the parent of the task stack
							stackBuilder.addParentStack(MainActivity.class);					
	
							// Push the content Intent onto the stack
							stackBuilder.addNextIntent(notificationIntent);
	
							// Get a PendingIntent containing the entire back stack
							PendingIntent notificationPendingIntent = stackBuilder
									.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
	
							// Get a notification builder that's compatible with platform
							// versions >= 4
							NotificationCompat.Builder builder = new NotificationCompat.Builder(
									GeofenceTransitionService.this);
	
							String creatorName = "";
							try {
								creatorName = (String) (new JSONObject(n.getCreator()))
										.get("name");
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
	
							// Set the notification contents
							builder.setSmallIcon(R.drawable.drop_icon)
									.setContentTitle("You found a note!")
									.setContentText(creatorName + " has left you a note")
									.setContentIntent(notificationPendingIntent);
	
							// Get an instance of the Notification manager
							NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	
							// Issue the notification
							mNotificationManager.notify(0, builder.build());
						}
					});
				}
			}				
		});	
	}
	

	/**
	 * Maps geofence transition types to their human-readable equivalents.
	 * 
	 * @param transitionType
	 *            A transition type constant defined in Geofence
	 * @return A String indicating the type of transition
	 */
	private String getTransitionString(int transitionType) {
		switch (transitionType) {

		case Geofence.GEOFENCE_TRANSITION_ENTER:
			return getString(R.string.geofence_transition_entered);

		case Geofence.GEOFENCE_TRANSITION_EXIT:
			return getString(R.string.geofence_transition_exited);

		default:
			return getString(R.string.geofence_transition_unknown);
		}
	}
}
