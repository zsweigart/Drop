package com.example.drop;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewNoteFragment extends Fragment {
	// View thisView = getView();
	private Note note;
	private boolean wasFound;
	private ImageView img;
	private TextView receiver_text_view;
	private TextView to_text_view;
	private TextView content_text_view;

	public static ViewNoteFragment newInstance(Note note, boolean found) {
		ViewNoteFragment fragment = new ViewNoteFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Note", note);
		bundle.putBoolean("wasFound", found);
		fragment.setArguments(bundle);
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = inflater.inflate(R.layout.view_note_fragment,
				container, false);
		Bundle args = getArguments();
		note = (Note) args.getSerializable("Note");
		wasFound = args.getBoolean("wasFound", true);
		img = (ImageView) rootView.findViewById(R.id.imageView);
		receiver_text_view = (TextView) rootView.findViewById(R.id.recievers);
		to_text_view = (TextView) rootView.findViewById(R.id.view_note_to);
		content_text_view = (TextView) rootView.findViewById(R.id.Note_content);
		
		if(wasFound)
		{
			to_text_view.setVisibility(View.GONE);
			receiver_text_view.setVisibility(View.VISIBLE);
		}
		else
		{
			to_text_view.setVisibility(View.VISIBLE);
			receiver_text_view.setVisibility(View.GONE);
		}

		if (note == null) {
			Toast.makeText(getActivity(),
					"Note not passed to ViewNote Activity", Toast.LENGTH_LONG)
					.show();
		} else {
			// If you need the picture from the database,
			// Grab the picture for the note with an Async Task (or not)
			if (note.getPictureFile() == null) {
				Log.d("ViewNoteScreen", "note " + note.getId()
						+ " doesn't have a picture");
				note.setPicture(DatabaseConnector.getPictureById(note.getId()));
				// new setPictureAsyncTask().execute(note);

			}

		}
		return rootView;

	}

	public void onResume() {
		super.onResume();
		Log.d("ViewNoteScreen", "note " + note.getId() + " is loading");
		load_note(note);
	}

	void load_note(Note note) {
		// Set the values for fields appropriate when note was found
		if (wasFound) {
			set_sender(note.getCreator().toString());
		}

		// Set values for fields appropriate when note was not found
		if (!wasFound) {
			set_reciever(note.getReceivers());
		}

		// Set other fields that are always appropriate
		set_note_content(note.getMessage());
		set_picture(note.getPicture());
	}

	private void set_picture(Bitmap picture) {
		img.setImageBitmap(picture);
	}

	private void set_reciever(ArrayList<String> arrayList) {
		String receiverString = "";
		for (String user : arrayList) {
			if (receiverString != "")
				receiverString += ", ";
			try {
				JSONObject obj = new JSONObject(user);
				receiverString += obj.getString("id");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (receiverString.length() >= 2)
			receiverString = receiverString.substring(0,
					receiverString.length() - 1);
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
		to_text_view.setText(sender_string);

	}

	private void set_note_content(String content) {
		content_text_view.setText(content);
	}

}