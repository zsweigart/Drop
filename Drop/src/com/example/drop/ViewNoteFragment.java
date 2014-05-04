package com.example.drop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewNoteFragment extends Fragment {
	// View thisView = getView();
	private Note note;
	private boolean wasFound;
	private boolean justPickedUp;
	private ImageView img;
	private TextView from_label;
	private TextView to_label;
	private TextView from_text_view;
	private TextView to_text_view;
	private TextView content_text_view;
	private Button saveBtn;
	private Button tossBtn;

	public static Fragment newInstance(Note note, boolean found) {
		ViewNoteFragment fragment = new ViewNoteFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Note", note);
		bundle.putBoolean("wasFound", found);
		bundle.putBoolean("justPickedUp", false);
		fragment.setArguments(bundle);
		return fragment;
	}

	public static Fragment newInstance(Note note, boolean found,
			boolean pickedUp) {
		ViewNoteFragment fragment = new ViewNoteFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Note", note);
		bundle.putBoolean("wasFound", found);
		bundle.putBoolean("justPickedUp", pickedUp);
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
		wasFound = args.getBoolean("wasFound", false);
		justPickedUp = args.getBoolean("justPickedUp", false);

		Log.d("ViewNoteFragment", "wasFound? " + wasFound);
		img = (ImageView) rootView.findViewById(R.id.view_note_thumbnail);
		from_text_view = (TextView) rootView
				.findViewById(R.id.view_note_sender);
		from_label = (TextView) rootView.findViewById(R.id.view_note_from);
		to_text_view = (TextView) rootView
				.findViewById(R.id.view_note_receiver);
		to_label = (TextView) rootView.findViewById(R.id.view_note_to);
		content_text_view = (TextView) rootView
				.findViewById(R.id.view_note_content);

		if (wasFound) {
			to_label.setVisibility(View.GONE);
			to_text_view.setVisibility(View.GONE);
			from_label.setVisibility(View.VISIBLE);
			from_text_view.setVisibility(View.VISIBLE);
		} else {
			to_label.setVisibility(View.VISIBLE);
			to_text_view.setVisibility(View.VISIBLE);
			from_text_view.setVisibility(View.GONE);
			from_label.setVisibility(View.GONE);
		}

		if (note == null) {
			Toast.makeText(getActivity(),
					"Note not passed to ViewNote Activity", Toast.LENGTH_LONG)
					.show();
		}

		if (justPickedUp) {
			saveBtn = (Button) rootView.findViewById(R.id.view_note_saveBtn);
			saveBtn.setVisibility(View.VISIBLE);
			saveBtn.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					File saveNote = Drop
							.getOutputMediaFile(Drop.SAVED_NOTE_DIR);
					try {
						FileOutputStream fos = new FileOutputStream(saveNote);
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						oos.writeObject(note);
						oos.close();
						fos.flush();
						fos.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					ViewNoteFragment.this.getActivity().finish();
				}
			});

			tossBtn = (Button) rootView.findViewById(R.id.view_note_tossBtn);
			tossBtn.setVisibility(View.VISIBLE);
			tossBtn.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					ViewNoteFragment.this.getActivity().finish();
				}

			});
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
			set_receiver(note.getReceivers());
		}

		// Set values for fields appropriate when note was not found
		if (!wasFound) {
			set_sender(note.getCreator().toString());
		}

		// Set other fields that are always appropriate
		set_note_content(note.getMessage());
		set_picture(note.getPicture());
	}

	private void set_picture(Bitmap picture) {
		img.setImageBitmap(picture);
	}

	private void set_receiver(ArrayList<String> arrayList) {
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
		to_text_view.setText(receiverString);
	}

	private void set_sender(String string) {
		String sender_string = "";
		try {
			JSONObject obj = new JSONObject(string);
			sender_string = (String) obj.get("name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		from_text_view.setText(sender_string);

	}

	private void set_note_content(String content) {
		content_text_view.setText(content);
	}

}