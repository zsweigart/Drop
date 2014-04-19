package com.example.drop;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.drop.LocationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationListener;

public class LocationService extends IntentService
implements LocationListener,
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{

	public LocationService() {
		super("LocationService");
		// TODO Auto-generated constructor stub
	}
	
	@Override
    protected void onHandleIntent(Intent intent) {
		
	}

	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	

	
}
