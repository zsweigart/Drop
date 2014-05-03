package com.example.drop;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


//This activity requires that a note is placed in its intent
public class ViewNoteScreen extends Activity {
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
    }
	
	@Override
	public void onResume(){
		super.onResume();
		 Note current_note = (Note) getIntent().getSerializableExtra("com.example.drop.Note");
		 boolean wasFound = getIntent().getBooleanExtra("wasFound", true);
	        if (current_note == null) 
	        	Toast.makeText(getApplicationContext(),"Note not passed to ViewNote Activity", 
	        	Toast.LENGTH_LONG).show();
	        
	        getFragmentManager().beginTransaction()
	        .replace(android.R.id.content, ViewNoteFragment.newInstance(current_note, wasFound))
	        .commit();
	        //else load_note(current_note);
	}
	
}
