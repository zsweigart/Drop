package com.example.drop;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Note implements Serializable{
	Bitmap picture;
	
	Note(Bitmap p)
	{
		picture = p;
	}
	
	public Bitmap getPicture()
	{
		return picture;
	}
	
	public void setPicture(Bitmap p)
	{
		picture = p;
	}
}
