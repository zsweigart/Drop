package com.example.drop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DatabaseConnector {

	private static String TAG = "DatabaseConnector";
	private static File pictureFile = null;

	public static ArrayList<Note> getNewNotes(String fbID) {
		Log.d(TAG, "getNewNotes() starting...");
		final ArrayList<Note> notes = new ArrayList<Note>();

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Note");
		query.whereEqualTo("recipient", fbID);
		query.whereEqualTo("isPickedUp", false);
		Log.i("DBC", "FBID: " + fbID);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> loadedNotes, ParseException arg1) {

				if (arg1 != null) {
					Log.e(TAG, arg1.toString());
				}

				Log.i("DBC", "NUM NOTES: " + loadedNotes.size());

				for (int i = 0; i < loadedNotes.size(); i++) {
					Note n = new Note();
					n.setLat(loadedNotes.get(i).getDouble("lat"));
					n.setLon(loadedNotes.get(i).getDouble("lon"));
					n.setRadius((float) loadedNotes.get(i).getDouble("radius"));
					n.setId(loadedNotes.get(i).getObjectId());
					notes.add(n);
					Log.d(TAG, "New note: " + n.getId());
				}

			}
		});

		return notes;
	}

	public static Note getNoteById(String objID) {
		final Note n = new Note();

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Note");
		query.whereEqualTo("objectId", objID);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> loadedNotes, ParseException arg1) {
				for (int i = 0; i < loadedNotes.size(); i++) {
					n.setLat(loadedNotes.get(i).getDouble("lat"));
					n.setLon(loadedNotes.get(i).getDouble("lon"));
					n.setRadius((float) loadedNotes.get(i).getDouble("radius"));
					n.setId(loadedNotes.get(i).getString("objectId"));
					n.setMessage(loadedNotes.get(i).getString("message"));
					n.setCreator(loadedNotes.get(i).getString("creator"));
				}

			}
		});

		return n;
	}

	public static File getPictureById(String objID) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				"getPictureById");

		query.getInBackground(objID, new GetCallback<ParseObject>() {

			public void done(ParseObject object, ParseException e) {
				if (object == null) {
					Log.d("test", "The object was not found...");
				} else {
					Log.d("test", "Retrieved the object.");
					ParseFile fileObject = (ParseFile) object.get("picture");

					fileObject.getDataInBackground(new GetDataCallback() {
						public void done(byte[] data, ParseException e) {
							if (e == null) {
								Log.d("test", "We've got data in data.");
								// Write the picture byte[] to the picture File
								pictureFile = Drop
										.getOutputMediaFile(Drop.PICTURE_DIR);
								try {
									FileOutputStream fos = new FileOutputStream(
											pictureFile);
									fos.write(data);
									fos.close();
								} catch (FileNotFoundException z) {
									Log.d(TAG,
											"File not found: " + z.getMessage());
								} catch (IOException x) {
									Log.d(TAG,
											"Error accessing file: "
													+ x.getMessage());
								}
							} else {
								Log.d("test",
										"There was a problem downloading the data.");
							}
						}
					});
				}
			}
		});

		return pictureFile;
	}

	public static void markPickedUp(String objId) {
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Note");
		query.whereEqualTo("objectId", objId);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> loadedNotes, ParseException arg1) {
				//loadedNotes.get(0).put("picture", null);
				loadedNotes.get(0).put("isPickedUp", true);
				loadedNotes.get(0).saveInBackground();
			}
		});
	}
}
