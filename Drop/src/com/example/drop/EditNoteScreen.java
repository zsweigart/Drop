package com.example.drop;

import com.example.drop.ViewNoteScreen.ViewNoteFragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class EditNoteScreen extends OptionsMenuScreen {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_edit_note_screen);
        Note current_note = (Note)(getIntent().getSerializableExtra("Note"));
        if (current_note == null) 
        	Toast.makeText(getApplicationContext(),"Note not passed to EditNote Activity", 
        	Toast.LENGTH_LONG).show();
        else load_note(current_note);

    }
	
	public void load_note(Note note){
		setImage(note.getPicture());
	}
	public void setImage(Bitmap b){
		ImageView img = (ImageView) findViewById(R.id.imageView);
		img.setImageBitmap(b);
	}
   
}
