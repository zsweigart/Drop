package com.example.drop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Date;
import java.util.HashSet;


public class Note implements Serializable{
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	protected static final String TAG = null;
	//TODO: get ids from database
	private static long nextID = 0;
	
	long id;
	//TODO: Change to parse user
	int creatorID;
	HashSet<Integer> receiverIDs;
	
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
		creatorID = 0;
		receiverIDs = new HashSet<Integer>();
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
	public int getCreatorID()
	{
		return creatorID;
	}
		
	public void setCreatorID(int i)
	{
		creatorID = i;
	}
		
	//Receiver ID get and set
	public HashSet<Integer> getReceiverIDs()
	{
		return receiverIDs;
	}
		
	public void addRecieverID(int i)
	{
		receiverIDs.add(i);
	}
	public boolean removeRecieverIDs(int i){
		boolean inSet = receiverIDs.contains(i);
		if (inSet) receiverIDs.remove(i);
		return inSet;
	}
	public void removeAllRecieverIDs(){
		receiverIDs.clear();
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
