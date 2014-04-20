package com.example.drop;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.drop.LocationUtils;
import com.google.android.gms.common.*;
import com.google.android.gms.location.*;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;

public class LocationService extends IntentService
implements LocationListener,
OnAddGeofencesResultListener,
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{
	/*
	 * 1) Listen for Note Geofence entry transitions, and post a notification (that opens the Note in question) when those transitions occur
	 * 2) Add a Geofence for new Notes (so that the user can see their dropped Notes on the Map) 
	 */

	 // Storage for a reference to the calling client
    private final Activity mActivity;
	
    // Stores the PendingIntent used to send geofence transitions back to the app
    private PendingIntent mGeofencePendingIntent;

    // Stores the current list of geofences
    private ArrayList<Geofence> mCurrentGeofences;

    // Stores the current instantiation of the location client
    private LocationClient mLocationClient;

    /*
     * Flag that indicates whether an add or remove request is underway. Check this
     * flag before attempting to start a new request.
     */
    private boolean mInProgress;
    
	public LocationService(Activity activityContext) {
		super("LocationService");		
		mActivity = activityContext;
		
		// Initialize the globals to null
        mGeofencePendingIntent = null;
        mLocationClient = null;
        mInProgress = false;
	}
	
	/**
     * Set the "in progress" flag from a caller. This allows callers to re-set a
     * request that failed but was later fixed.
     *
     * @param flag Turn the in progress flag on or off.
     */
    public void setInProgressFlag(boolean flag) {
        // Set the "In Progress" flag.
        mInProgress = flag;
    }

    /**
     * Get the current in progress status.
     *
     * @return The current value of the in progress flag.
     */
    public boolean getInProgressFlag() {
        return mInProgress;
    }    
   
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
            String errorMessage = LocationServiceErrorMessages.getErrorString(this, errorCode);

            // Log the error
            Log.e(LocationUtils.APPTAG,
                    getString(R.string.geofence_transition_error_detail, errorMessage)
            );

            // Set the action and error message for the broadcast intent
            broadcastIntent.setAction(LocationUtils.ACTION_GEOFENCE_ERROR)
                           .putExtra(LocationUtils.EXTRA_GEOFENCE_STATUS, errorMessage);

            // Broadcast the error *locally* to other components in this app
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        }
        
        

        
	}

	public void onConnectionFailed(ConnectionResult connectionResult) {		
		 /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {

            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(mActivity,
                    LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

            /*
             * Thrown if Google Play services canceled the original
             * PendingIntent
             */
            } catch (SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }

        /*
         * If no resolution is available, put the error code in
         * an error Intent and broadcast it back to the main Activity.
         * The Activity then displays an error dialog.
         * is out of date.
         */
        } else {

            Intent errorBroadcastIntent = new Intent(LocationUtils.ACTION_CONNECTION_ERROR);
            errorBroadcastIntent.addCategory(LocationUtils.CATEGORY_LOCATION_SERVICES)
                                .putExtra(LocationUtils.EXTRA_CONNECTION_ERROR_CODE,
                                        connectionResult.getErrorCode());
            LocalBroadcastManager.getInstance(mActivity).sendBroadcast(errorBroadcastIntent);
        }
		
	}

	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	public void onLocationChanged(Location arg0) {
		// 
		
	}
	
	/**
     * Get the current location client, or create a new one if necessary.
     *
     * @return A LocationClient object
     */
    private GooglePlayServicesClient getLocationClient() {
        if (mLocationClient == null) {

            mLocationClient = new LocationClient(mActivity, this, this);
        }
        return mLocationClient;

    }

	public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
		// Create a broadcast Intent that notifies other components of success or failure
        Intent broadcastIntent = new Intent();

        // Temp storage for messages
        String msg;

        // If adding the geocodes was successful
        if (LocationStatusCodes.SUCCESS == statusCode) {

            // Create a message containing all the geofence IDs added.
            msg = mActivity.getString(R.string.add_geofences_result_success,
                    Arrays.toString(geofenceRequestIds));

            // In debug mode, log the result
            Log.d(LocationUtils.APPTAG, msg);

            // Create an Intent to broadcast to the app
            broadcastIntent.setAction(LocationUtils.ACTION_GEOFENCES_ADDED)
                           .addCategory(LocationUtils.CATEGORY_LOCATION_SERVICES)
                           .putExtra(LocationUtils.EXTRA_GEOFENCE_STATUS, msg);
        // If adding the geofences failed
        } else {

            /*
             * Create a message containing the error code and the list
             * of geofence IDs you tried to add
             */
            msg = mActivity.getString(
                    R.string.add_geofences_result_failure,
                    statusCode,
                    Arrays.toString(geofenceRequestIds)
            );

            // Log an error
            Log.e(LocationUtils.APPTAG, msg);

            // Create an Intent to broadcast to the app
            broadcastIntent.setAction(LocationUtils.ACTION_GEOFENCE_ERROR)
                           .addCategory(LocationUtils.CATEGORY_LOCATION_SERVICES)
                           .putExtra(LocationUtils.EXTRA_GEOFENCE_STATUS, msg);
        }

        // Broadcast whichever result occurred
        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(broadcastIntent);

        // Disconnect the location client
        requestDisconnection();
    }    

	/**
     * Get a location client and disconnect from Location Services
     */
    private void requestDisconnection() {

        // A request is no longer in progress
        mInProgress = false;

        getLocationClient().disconnect();
    }	
}
	
	

	

	

