package com.example.drop;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
=======
import android.app.Activity;
import android.os.Bundle;

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
	    	set_note_content(note.get_text());
	    	set_to_from_textview(note.sent_by_me());
	    	if (note.sent_by_me())
	    		set_people_textview(note.recipients());
	    	else set_people_textview(note.senders());
	    }
	    
	    void set_to_from_textview(boolean sent){
	        TextView sent_or_recieved = (TextView) thisView.findViewById(R.id.sent_or_recieved);
	        if (sent) sent_or_recieved.setText("To:");
	        else sent_or_recieved.setText("From:");
	    }
	    
	    void set_people_textview(String people){
	    	TextView people_textview = (TextView) thisView.findViewById(R.id.Recipient_Content);
	    	people_textview.setText(people);
	    }
	    
	    void set_note_content(String content){
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
