package com.example.drop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


//This activity requires that a note is placed in its intent
public class ViewNoteScreen extends Activity {
	
	private ProgressDialog progress;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_view_note_screen);
//        Note current_note = getIntent().getParcelableExtra("Note");
//        if (current_note == null) 
//        	Toast.makeText(getApplicationContext(),"Note not passed to ViewNote Activity", 
//        	Toast.LENGTH_LONG).show();
//        getFragmentManager().beginTransaction()
//        .replace(android.R.id.content, new ViewNoteFragment())
//        .commit();
//        //else load_note(current_note);
    }
	
	@Override
	public void onResume(){
		super.onResume();
		 Note current_note = (Note) getIntent().getSerializableExtra("com.example.drop.Note");
	        if (current_note == null) 
	        	Toast.makeText(getApplicationContext(),"Note not passed to ViewNote Activity", 
	        	Toast.LENGTH_LONG).show();
	        
	        getFragmentManager().beginTransaction()
	        .replace(android.R.id.content, ViewNoteFragment.newInstance(current_note))
	        .commit();
	        //else load_note(current_note);
	}
	
	public static class ViewNoteFragment extends Fragment{
		//View thisView = getView();
		private Note note;
		public static ViewNoteFragment newInstance(Note note){
			ViewNoteFragment fragment = new ViewNoteFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("Note", note);
			fragment.setArguments(bundle);
			return fragment;
		}
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// The last two arguments ensure LayoutParams are inflated
			// properly.
			View rootView = inflater.inflate(
					R.layout.view_note_fragment, container, false);
			Bundle args = getArguments();
			note = (Note) args.getSerializable("Note");
	        if (note == null) 
	        	Toast.makeText(getActivity(),"Note not passed to ViewNote Activity", 
	        	Toast.LENGTH_LONG).show();
	        else {
	        	// If you need the picture from the database, 
	        	// Grab the picture for the note with an Async Task (or not)
		        if(note.getPictureFile() == null){
		        	Log.d("ViewNoteScreen", "note "+note.getId()+" doesn't have a picture");
		        	note.setPicture(DatabaseConnector.getPictureById(note.getId()));
		        	//new setPictureAsyncTask().execute(note);
		        	
		        }        
		       
	        }
	        return rootView;

	    }
		
		public void onResume(){
			super.onResume();
			Log.d("ViewNoteScreen", "note "+note.getId()+" is loading");
		    load_note(note);
		}
	    void load_note(Note note){
	    	set_note_content(note.getMessage());
	    	set_sender(note.getCreator().toString());
	    	set_reciever(note.getReceivers());
	    	set_picture(note.getPicture());
	    }
	    
	    private void set_picture(Bitmap picture) {
			ImageView img = (ImageView) getView().findViewById(R.id.imageView);
			img.setImageBitmap(picture);
		}

		private void set_reciever(ArrayList<String> arrayList) {
			String receiverString = "";
			for (String user : arrayList){
				if (receiverString != "") receiverString += ", ";
				try {
					JSONObject obj = new JSONObject(user);
					receiverString += obj.getString("id");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (receiverString.length() >= 2) 
				receiverString = receiverString.substring(0, receiverString.length()-1);
			TextView receiver_text_view = (TextView) getView().findViewById(R.id.recievers);
			receiver_text_view.setText(receiverString);
		}

		private void set_sender(String string) {
			String sender_string = "";
			try {
				JSONObject obj = new JSONObject(string);
				sender_string = (String) obj.get("name");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			TextView to_text_view = (TextView) getView().findViewById(R.id.to);
			to_text_view.setText(sender_string);
			
		}

		private void set_note_content(String content){
	    	TextView content_text_view = (TextView) getView().findViewById(R.id.Note_content);
	    	content_text_view.setText(content);
	    }	
	}
	
	private class setPictureAsyncTask extends
	AsyncTask<Note, Void, File> {
		Note n; // temporary note object
		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(ViewNoteScreen.this);
			progress.setTitle("Fetching Image");
			progress.setMessage("Wait while loading...");
			progress.show();
		}
		
		@Override
		protected File doInBackground(Note... params) {		
			n = params[0];
			return DatabaseConnector.getPictureById(n.getId());
		}
		
		@Override
		protected void onPostExecute(File result)
		{
		    super.onPostExecute(result);
		    n.setPicture(result);		    
		}
		
	}
	
	
}
