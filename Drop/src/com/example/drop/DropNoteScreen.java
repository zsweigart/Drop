package com.example.drop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class DropNoteScreen extends DrawerActivity {
	private Note note;
	private ParseFile file;
	private Iterator<String> i;
	private int numNotes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View layout = getLayoutInflater().inflate(
				R.layout.activity_drop_note_screen, null);
		FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
		frame.addView(layout);
		
		BounceView icon = (BounceView) findViewById(R.id.anim_view);
		icon.setVisibility(View.VISIBLE);
		icon.bringToFront();
		
        
        note = Drop.current_note;
        File saveNote = Drop.getOutputMediaFile("/Android/data/com.example.drop/notes");
        try {
			FileOutputStream fos = new FileOutputStream(saveNote);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(note);
			oos.close();
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Bitmap bitmap = note.getPicture();
        ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); 
        byte[] data = stream.toByteArray(); 
        file = new ParseFile("picture", data);
        file.saveInBackground(new SaveCallback(){

			@Override
			public void done(ParseException arg0) {
				Log.i("DROP", "DONE");
				if(arg0 == null)
				{
					Log.i("DROP", "NO ERROR");
					i = note.getReceivers().iterator();
					numNotes=note.getReceivers().size();
					while(i.hasNext())
		            {
						ParseObject obj = new ParseObject("Note");
		            	obj.put("creator", note.getCreator().toString());
		            	String recipient = i.next().toString();
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
			    				numNotes++;
			    				if(numNotes >= note.getReceivers().size()-1)
			    				{
				    				endActivity();
			    				}
			    			}
			            });
		            }
		            
				}
				else
				{
					Log.i("DROP", arg0.toString());
				}
			}
        	
        });
        
    }
    
    private void endActivity()
    {
    	 new Handler().postDelayed(new Runnable() {
    		 
             public void run() {
            	 Intent i = new Intent(DropNoteScreen.this, DroppedListScreen.class);
                 startActivity(i);

                 // close this activity
                 finish();
             }
         }, 500);
    }

   
}
