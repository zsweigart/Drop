package com.example.drop;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;


//This activity requires that a note is placed in its intent
public class ViewNoteScreen extends Activity {
	
	public static class ViewNoteFragment extends Fragment{
		View thisView = getView();
		public static ViewNoteFragment newInstance(Note note){
			ViewNoteFragment fragment = new ViewNoteFragment();
			Bundle bundle = new Bundle();
			bundle.putParcelable("Note", (Parcelable) note);
			fragment.setArguments(bundle);
			return fragment;
		}
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// The last two arguments ensure LayoutParams are inflated
			// properly.
			View rootView = inflater.inflate(
			R.layout.view_note_fragment, container, false);
			Bundle args = getArguments();
			Note note = args.getParcelable("Note");
	        if (note == null) 
	        	Toast.makeText(getActivity(),"Note not passed to ViewNote Activity", 
	        	Toast.LENGTH_LONG).show();
	        //else load_note(note);
	        return rootView;

	    }
	    void load_note(Note note){
	    	set_note_content(note.getMessage());
	    	set_sender(note.getCreator());
	    	set_reciever(note.getReceivers());
	    	set_picture(note.getPicture());
	    }
	    

	    
	    private void set_picture(Bitmap picture) {
			ImageView img = (ImageView) thisView.findViewById(R.id.imageView);
			img.setImageBitmap(picture);
		}

		private void set_reciever(ArrayList<JSONObject> arrayList) {
			String receiverString = "";
			for (JSONObject user : arrayList){
				if (receiverString != "") receiverString += ", ";
				try {
					receiverString += user.getString("id");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (receiverString.length() >= 2) 
				receiverString = receiverString.substring(0, receiverString.length()-1);
			TextView receiver_text_view = (TextView) thisView.findViewById(R.id.recievers);
			receiver_text_view.setText(receiverString);
		}

		private void set_sender(JSONObject user) {
			String sender_string = "";
			try {
				sender_string = (String) user.get("name");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			TextView to_text_view = (TextView) thisView.findViewById(R.id.to);
			to_text_view.setText(sender_string);
			
		}

		private void set_note_content(String content){
	    	TextView content_text_view = (TextView) thisView.findViewById(R.id.Note_content);
	    	content_text_view.setText(content);
	    }
	
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_view_note_screen);
        Note current_note = getIntent().getParcelableExtra("Note");
        if (current_note == null) 
        	Toast.makeText(getApplicationContext(),"Note not passed to ViewNote Activity", 
        	Toast.LENGTH_LONG).show();
        getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new ViewNoteFragment())
        .commit();
        //else load_note(current_note);

    }



}
