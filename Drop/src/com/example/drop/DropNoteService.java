package com.example.drop;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

public class DropNoteService extends IntentService {
	private Note note;
	private ParseFile file;
	private Iterator<String> i;
	
	public DropNoteService() {
		super("DropNoteService");
	}

	protected void onHandleIntent(Intent intent) {
		note =  (Note) intent.getSerializableExtra("note");

        Bitmap bitmap = note.getPicture();
        ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); 
        byte[] data = stream.toByteArray(); 
        file = new ParseFile("picture", data);
        file.saveInBackground(new SaveCallback(){

			@Override
			public void done(ParseException arg0) {
				Log.i("DROP_NOTE_SERVICE", "DONE");
				if(arg0 == null)
				{
					Log.i("DROP_NOTE_SERVICE", "NO ERROR");
					i = note.getReceivers().iterator();
					while(i.hasNext())
		            {
						ParseObject obj = new ParseObject("Note");
		            	obj.put("creator", note.getCreator().toString());
		            	String recipient = "";
						try {
							JSONObject jsonObj = new JSONObject(i.next());
							Log.i("DROP", jsonObj.toString());
							recipient = jsonObj.get("facebookId").toString();
						} catch (JSONException e) {
							e.printStackTrace();
						}
		            	obj.put("recipient", recipient);
		            	obj.put("message", note.getMessage());
			            obj.put("picture",file);
			            obj.put("isPickedUp", note.isPickedUp());
			            obj.put("radius", note.getRadius());
			            obj.put("lat", note.getLat());
			            obj.put("lon", note.getLon());
			            obj.saveInBackground(new SaveCallback(){

			    			@Override
			    			public void done(ParseException arg0)
			    			{
			    				
			    			}
			            });
		            }
		            
				}
				else
				{
					Log.i("DROP_NOTE_SERVICE", arg0.toString());
				}
			}
        	
        });
	}
}
