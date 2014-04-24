package com.example.drop;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapScreen extends DrawerActivity {
	
	// Google Map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            View layout =  getLayoutInflater().inflate(R.layout.activity_map_screen, null);
            FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
            frame.addView(layout);
            
            
            try {
                // Loading map
                initilizeMap();
     
            } catch (Exception e) {
                e.printStackTrace();
            }
         
    }
    
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            
            googleMap.setMyLocationEnabled(true);
            
            // latitude and longitude
            double latitude = 40.442492;
            double longitude = -79.942553;
             
            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");
             
            // adding marker
            googleMap.addMarker(marker);
            
            
            // Acquire a reference to the system Location Manager
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            // Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                  // Called when a new location is found by the network location provider.                	
                	double latitude= location.getLatitude();
                	double longtitude = location.getLongitude();
                	LatLng ll = new LatLng(latitude, longtitude);

                	googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 14));
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}

              };

            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);        

 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

}
