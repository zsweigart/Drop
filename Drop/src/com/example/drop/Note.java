package com.example.drop;

import java.io.File;
import java.io.Serializable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;

import org.json.JSONObject;

import com.parse.ParseUser;


public class Note implements Serializable{
	private static final long serialVersionUID = 1L;
	protected static final String TAG = "NOTE";
	//TODO: get ids from database
	private static long nextID = 0;
	
	long id;
	JSONObject creator;
	ArrayList<JSONObject> receivers;
	
	String message;
	File picture;
	//TODO: Change to parse GeoPoints or Fence
	String coordinates;
	
	boolean pickedUp;
	
	Note()
	{
	}
	
	Note(File p)
	{
		id = nextID;
		nextID++;
		creator = Drop.loggedInJSON;
		receivers = new ArrayList<JSONObject>();
		picture = p;
		coordinates = "";
	}
	
	//Note ID get and set
	public long getID()
	{
		return id;
	}
		
	public void setID(long i)
	{
		id = i;
	}
	
	//Creator ID get and set
	public JSONObject getCreator()
	{
		return creator;
	}
		
	public void setCreator(JSONObject user)
	{
		creator = user;
	}
		
	//Receiver ID get and set
	public ArrayList<JSONObject> getReceivers()
	{
		return receivers;
	}
	
	public void setRecievers(ArrayList<JSONObject> r)
	{
		receivers = r;
	}
		
	public void addReciever(JSONObject i)
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
    	
        return BitmapFactory.decodeFile(picture.toString());
	}
	
	public void setPicture(File p)
	{
		picture = p;        
	}
	
	//Coordinates get and set
	public String getCoordinates()
	{
		return coordinates;
	}
	
	public void setCoordinates(String c)
	{
		coordinates = c;
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
