package com.example.drop;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class EditNoteScreen extends DrawerActivity{//OptionsMenuScreen {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        View layout =  getLayoutInflater().inflate(R.layout.activity_edit_note_screen, null);
        FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
        frame.addView(layout);
        //setContentView(R.layout.activity_edit_note_screen);
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
		img.setVisibility(0);
		img.setImageBitmap(b);
	}
   
}
