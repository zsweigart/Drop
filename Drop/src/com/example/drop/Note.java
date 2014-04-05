package com.example.drop;

import java.io.Serializable;

import android.graphics.Bitmap;
import java.util.Date;

public class Note implements Serializable{
	//TODO: get ids from database
	private static long nextID = 0;
	
	long id;
	//TODO: Change to parse user
	int creatorID;
	int receiverID;
	
	String message;
	Bitmap picture;
	//TODO: Change to parse GeoPoints or Fence
	String coordinates;
	
	long timeStamp;
	boolean pickedUp;
	
	Note(Bitmap p)
	{
		id = nextID;
		nextID++;
		creatorID = 0;
		receiverID = 0;
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
	public int getReceiverID()
	{
		return receiverID;
	}
		
	public void setID(int i)
	{
		receiverID = i;
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
		return picture;
	}
	
	public void setPicture(Bitmap p)
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
