package com.example.drop;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class EditNoteScreen extends DrawerActivity{//OptionsMenuScreen {

	private EditText recipientsEdt;
	private final int REQUEST_CODE = 1;
	private ImageView thumbnail;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        View layout =  getLayoutInflater().inflate(R.layout.activity_edit_note_screen, null);
        FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
        frame.addView(layout);
        
        thumbnail = (ImageView)findViewById(R.id.edit_note_thumbnail);
        if (thumbnail == null) 
        	Log.i("EDIT", "NO THUMBNAIL");
        
        Note current_note = (Note)(getIntent().getSerializableExtra("Note"));
        load_note(current_note);
        
        recipientsEdt = (EditText)findViewById(R.id.recievers);
        recipientsEdt.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
				{
					Intent i = new Intent(EditNoteScreen.this, SelectRecipients.class);
					if(!recipientsEdt.getText().equals("Nobody"))
					{
						
					}
					startActivityForResult(i, REQUEST_CODE);
				}
				
			}
        	
        });

    }
	
	public void load_note(Note note){
		setImage(note.getPicture(thumbnail.getHeight(), thumbnail.getWidth()));
	}
	
	public void setImage(Bitmap b){
		thumbnail.setVisibility(0);
		thumbnail.setImageBitmap(b);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("numResults")) {
				Bundle b = data.getExtras();
				try
				{
					int numResults = b.getInt("numResults");
					String recipientList = "";
					for(int i = 0; i < numResults; i++)
					{
						Log.i("EDITNOTE", "recipient"+i+"  :  "+b.getString("recipient"+i));
						JSONObject jsonObj = new JSONObject(b.getString("recipient"+i));
						recipientList += jsonObj.get("name")+", ";
					}
					recipientList.substring(0, recipientList.length()-3);
					recipientsEdt.setText(recipientList);
				} catch (ClassCastException e) {
					Log.i("EDIT NOTE", "Error, value returned of wrong type");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("EDIT NOTE", "Error, JSON exception, no name");
					e.printStackTrace();
				}
			}
		}
	} 
   
}
