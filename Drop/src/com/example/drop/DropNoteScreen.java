package com.example.drop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class DropNoteScreen extends DrawerFragmentActivity {


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
		
		Drop.current_note.setIsDropped(true);
		
		Intent dropIntent = new Intent(getApplicationContext(), DropNoteService.class);
		dropIntent.putExtra("note", Drop.current_note);
		startService(dropIntent);
		
		Note note =  Drop.current_note;
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
		
        endActivity();
    }
    
    private void endActivity()
    {
		 Drop.currentPage = 2;
		 // close this activity
		 finish();
    }

   
}
