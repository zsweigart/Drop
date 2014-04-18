package com.example.drop;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;

import org.json.JSONObject;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class DropNoteScreen extends DrawerActivity {
	private Note note;
	private ParseFile file;

    private static int DROP_TIMEOUT = 500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_note_screen);
        
        note = Drop.current_note;
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
					ParseObject obj = new ParseObject("Note");
		            obj.put("creator", note.getCreator().toString());
		            String recipients = "";
		            Iterator<JSONObject> i = note.getReceivers().iterator();
		            while(i.hasNext())
		            {
		            	recipients += i.next().toString();
		            }
		            obj.put("recipients", recipients);
		            obj.put("picture",file);
				}
				else
				{
					Log.i("DROP", arg0.toString());
				}
			}
        	
        });
        
        /*new Handler().postDelayed(new Runnable() {
        	 
            public void run() {        	 	
        		Intent i = new Intent(DropNoteScreen.this, DroppedListScreen.class);
                startActivity(i);
 
                // close this activity
		        finish();
            }
        }, DROP_TIMEOUT);*/
        
    }

   
}
