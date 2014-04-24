package com.example.drop;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;


import android.app.Activity;
import android.content.IntentSender.SendIntentException;
import android.location.Location;
import android.os.Bundle;

public class LocationService implements
ConnectionCallbacks, OnConnectionFailedListener, LocationListener 
{

	Activity mActivity;
	LocationClient mLocationClient;
	boolean connected;

	
	public LocationService(Activity callingActivity){
		mActivity = callingActivity;
		mLocationClient = null;	
		connected = false;
		requestConnection();		
	}
	
	public Location getLastLocation(){
		if(!connected){		
			requestConnection();			
		}
		return mLocationClient.getLastLocation();
	}

	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
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
		} else {
			// If no resolution is available, display a dialog to the user with
			// the error.
			//showErrorDialog(connectionResult.getErrorCode());
		}
		
	}

	public void onConnected(Bundle arg0) {
		connected = true;
		
	}

	public void onDisconnected() {
		// TODO Auto-generated method stub
		connected = false;
		
	}	
	
	/**
     * Request a connection to Location Services. This call returns immediately,
     * but the request is not complete until onConnected() or onConnectionFailure() is called.
     */
    private void requestConnection() {
        getLocationClient().connect();
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
	
}
