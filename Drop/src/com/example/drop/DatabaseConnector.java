package com.example.drop;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DatabaseConnector {

	public static ArrayList <Note> getNewNotes(String fbID)
	{
		final ArrayList <Note> notes = new ArrayList <Note> ();
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("getNewNotes"); 
		query.whereEqualTo("recipient", fbID);
		query.whereEqualTo("isPickedUp", false);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> loadedNotes, ParseException arg1) {
				for(int i = 0; i < loadedNotes.size(); i++)
				{
					Note n = new Note();
					n.setLat(loadedNotes.get(i).getDouble("lat"));
					n.setLon(loadedNotes.get(i).getDouble("lon"));
					n.setRadius((float) loadedNotes.get(i).getDouble("radius"));
					n.setId(loadedNotes.get(i).getString("objectId"));
					notes.add(n);
				}
				
			} });
		
		return notes;
	}
	
	public static Note getNoteById(String objID)
	{
		final Note n = new Note();
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("getNewNotes"); 
		query.whereEqualTo("objectId", objID);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> loadedNotes, ParseException arg1) {
				for(int i = 0; i < loadedNotes.size(); i++)
				{
					n.setLat(loadedNotes.get(i).getDouble("lat"));
					n.setLon(loadedNotes.get(i).getDouble("lon"));
					n.setRadius((float) loadedNotes.get(i).getDouble("radius"));
					n.setId(loadedNotes.get(i).getString("objectId"));
				}
				
			} });
		
		return n;
	}
}
