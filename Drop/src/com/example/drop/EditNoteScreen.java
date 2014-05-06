package com.example.drop;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


public class EditNoteScreen extends DrawerFragmentActivity  {

	private String TAG = "EditNoteScreen";
	private EditText recipientsEdt;
	private EditText messageEdt;
	private final int REQUEST_CODE = 1;
	private ImageView thumbnail;
	private Button dropButton;
	private ArrayList<String> recipients;
	private LocationService client;
	private Bitmap bit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View layout = getLayoutInflater().inflate(
				R.layout.activity_edit_note_screen, null);
		FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
		frame.addView(layout);
		//setContentView(R.layout.activity_edit_note_screen);
		
		client = new LocationService(this); //** Using the new LocationService
		recipients = new ArrayList<String>();

		messageEdt = (EditText) findViewById(R.id.edit_note_content);
		thumbnail = (ImageView) findViewById(R.id.edit_note_thumbnail);

		dropButton = (Button) findViewById(R.id.editScreen_drop_button);
		dropButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (recipients.size() > 0) {
					if ((messageEdt.getText().toString().equals("Nobody"))
							|| (messageEdt.getText().toString().equals(""))
							|| (messageEdt.getText().toString() == null)) {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								EditNoteScreen.this);

						// set title
						alertDialogBuilder.setTitle("Missing Message");

						// set dialog message
						alertDialogBuilder
								.setMessage(
										"Are you sure you want to drop a note without a message?")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// if this button is clicked,
												// drop the note
												createAndDropNote();
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// if this button is clicked,
												// just close
												// the dialog box and do nothing
												dialog.cancel();
											}
										});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();
					} else {
						createAndDropNote();
					}
				} else {
					Toast t = Toast.makeText(EditNoteScreen.this,
							"You have to select friends!", Toast.LENGTH_LONG);
					t.show();
				}

			}

		});

		load_note(Drop.current_note);

		recipientsEdt = (EditText) findViewById(R.id.recievers);
		recipientsEdt.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					Intent i = new Intent(EditNoteScreen.this,
							SelectRecipients.class);
					if (!recipientsEdt.getText().equals("Nobody")) {

					}
					startActivityForResult(i, REQUEST_CODE);
				}
			}
		});
	}

	public void load_note(Note note) {
		setImage(note.getPicture(thumbnail.getHeight(), thumbnail.getWidth()));
	}

	public void setImage(Bitmap b) {
		bit = b;
		thumbnail.setVisibility(0);
		thumbnail.setImageBitmap(b);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("numResults")) {
				Bundle b = data.getExtras();
				try {
					int numResults = b.getInt("numResults");
					if (numResults > 0) {
						String recipientList = "";
						for (int i = 0; i < numResults; i++) {
							Log.i("EDITNOTE",
									"recipient" + i + "  :  "
											+ b.getString("recipient" + i));
							JSONObject jsonObj = new JSONObject(
									b.getString("recipient" + i));
							recipients.add(jsonObj.toString());
							recipientList += jsonObj.get("name") + ", ";
						}
						recipientList.substring(0, recipientList.length() - 3);
						recipientsEdt.setText(recipientList);
					}
				} catch (ClassCastException e) {
					Log.i("EDIT NOTE", "Error, value returned of wrong type");
				} catch (JSONException e) {
					Log.i("EDIT NOTE", "Error, JSON exception, no name");
					e.printStackTrace();
				}
			}
		}
	}

	private void createAndDropNote() {
		Drop.current_note.setCreator(Drop.loggedInJSON.toString());
		Drop.current_note.setRecievers(recipients);
		Drop.current_note.setMessage(messageEdt.getText().toString());
		Drop.current_note.setPickedUp(false);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
		
		Location loc = client.getLastLocation(); //** Using the new LocationService
		
		Drop.current_note.setLon(loc.getLongitude());
		Drop.current_note.setLat(loc.getLatitude());		
		
		Log.d(TAG, "the radius pref key is"+getString(R.string.pref_note_radius_key));
		Log.d(TAG, "the radius default is"+getString(R.string.pref_note_radius_default));
		//Grab the radius from the sharedPreferences
		float radiusFromPrefs = Float.parseFloat(prefs.getString(getString(R.string.pref_note_radius_key), //The saved radius 
														getString(R.string.pref_note_radius_default)));   //Default radius
		
		Log.d(TAG, "radius loaded from preferences is:"+ radiusFromPrefs);
		Drop.current_note.setRadius(radiusFromPrefs);		
		
		try {
			bit.recycle();
			bit = null;
			System.gc();
		} catch(Exception e){
		}
		Intent i = new Intent(EditNoteScreen.this, DropNoteScreen.class);
		startActivity(i);

		// close this activity
		finish();
	}
	
	
	/**
	 * Define a DialogFragment to display the error dialog generated in
	 * showErrorDialog.
	 */
	public static class ErrorDialogFragment extends DialogFragment {

		// Global field to contain the error dialog
		private Dialog mDialog;

		/**
		 * Default constructor. Sets the dialog field to null
		 */
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		/**
		 * Set the dialog to display
		 * 
		 * @param dialog
		 *            An error dialog
		 */
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		/*
		 * This method must return a Dialog to the DialogFragment.
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}
	

}
