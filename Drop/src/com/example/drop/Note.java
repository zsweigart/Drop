package com.example.drop;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.util.Date;
import java.util.HashSet;

import com.parse.ParseUser;


public class Note implements Serializable{
	private static final long serialVersionUID = 1L;
	protected static final String TAG = "NOTE";
	//TODO: get ids from database
	private static long nextID = 0;
	
	long id;
	//TODO: Change to parse user
	ParseUser creator;
	HashSet<ParseUser> receivers;
	
	String message;
	File picture;
	//TODO: Change to parse GeoPoints or Fence
	String coordinates;
	
	long timeStamp;
	boolean pickedUp;
	
	Note(File p)
	{
		id = nextID;
		nextID++;
		creator = Drop.loggedInUser;
		receivers = new HashSet<ParseUser>();
		picture = p;
		coordinates = "";
		timeStamp = (new Date()).getTime();
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
	public ParseUser getCreator()
	{
		return creator;
	}
		
	public void setCreator(ParseUser user)
	{
		creator = user;
	}
		
	//Receiver ID get and set
	public HashSet<ParseUser> getReceivers()
	{
		return receivers;
	}
		
	public void addReciever(ParseUser i)
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
        byte[] data  = new byte[(int) picture.length()];
        
        try 
        {
            //convert file into array of bytes
        	FileInputStream fileInputStream = new FileInputStream(picture);
	    	fileInputStream.read(data);
	    	fileInputStream.close();
        }
        catch(Exception e){
        	Log.d(TAG, "Error reading picture: " + e.getMessage());
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length);
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
	
	//Time get and set
	public long getTime()
	{
		return timeStamp;
	}
	
	public void setTime(long time)
	{
		timeStamp = time;
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
