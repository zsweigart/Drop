package com.example.drop;

import java.io.File;
import java.io.Serializable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;

import com.parse.ParseUser;


public class Note implements Serializable{
	private static final long serialVersionUID = 1L;
	protected static final String TAG = "NOTE";
	
	String creator;
	ArrayList<String> receivers;
	
	String message;
	File picture;
	
	float radius;
	double lat;
	double lon;
	
	boolean pickedUp;
	
	Note()
	{
		radius = 50;
	}
	
	Note(File p)
	{
		creator = Drop.loggedInJSON.toString();
		receivers = new ArrayList<String>();
		picture = p;
		radius = 50;
	}
	
	//Creator ID get and set
	public String getCreator()
	{
		return creator;
	}
		
	public void setCreator(String user)
	{
		creator = user;
	}
		
	//Receiver ID get and set
	public ArrayList<String> getReceivers()
	{
		return receivers;
	}
	
	public void setRecievers(ArrayList<String> r)
	{
		receivers = r;
	}
		
	public void addReciever(String i)
	{
		receivers.add(i);
	}
	
	public boolean removeReciever(ParseUser i){
		boolean inSet = receivers.contains(i);
		if (inSet) receivers.remove(i);
		return inSet;
	}
	public void removeAllRecievers(){
		receivers.clear();
	}
	//Note message get and set
	public String getMessage()
	{
		return message;
	}
		
	public void setMessage(String msg)
	{
		message = msg;
	}
	
	//Bitmap Picture get and set
	public Bitmap getPicture()
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
        
    	//Get image taken
        options.inMutable = true;
        options.inScaled = false;
        
        return BitmapFactory.decodeFile(picture.toString()); 
	}
	
	public Bitmap getPicture(int height, int width)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picture.toString(), options); 
    	int sampleSize = DrawScreen.calculateInSampleSize(options, height, width);
        
    	Log.i(TAG, "SAMPLE SIZE = " + sampleSize);
    	
    	//Get image taken
        options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inScaled = false;
        options.inSampleSize = sampleSize;
        options.inPurgeable=true;
    	
        System.gc();
        
        return BitmapFactory.decodeFile(picture.toString());
	}
	
	public void setPicture(File p)
	{
		picture = p;        
	}
	
	//Coordinates get and set
	public float getRadius()
	{
		return radius;
	}
	
	public void setRadius(float r)
	{
		radius = r;
	}
	
	public double getLat()
	{
		return lat;
	}
	
	public void setLat(double l)
	{
		lat = l;
	}
	
	public double getLon()
	{
		return lon;
	}
	
	public void setLon(double l)
	{
		lon = l;
	}
	
	//Picked up get and set
	public boolean isPickedUp()
	{
		return pickedUp;
	}
	
	public void setPickedUp(boolean up)
	{
		pickedUp = up;
	}
}
