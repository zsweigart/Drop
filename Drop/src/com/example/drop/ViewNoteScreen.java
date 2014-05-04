package com.example.drop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


//This activity requires that a note is placed in its intent
public class ViewNoteScreen extends DrawerFragmentActivity {
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        //These three lines might make the drawer menu work. That's the hope, anyway.
        View layout =  getLayoutInflater().inflate(R.layout.activity_dropped_list_screen, null);
        FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
        frame.addView(layout);
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
