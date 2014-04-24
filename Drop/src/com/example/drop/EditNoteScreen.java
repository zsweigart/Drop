package com.example.drop;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
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

public class EditNoteScreen extends DrawerActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener {// OptionsMenuScreen {

	private EditText recipientsEdt;
	private EditText messageEdt;
	private final int REQUEST_CODE = 1;
	private ImageView thumbnail;
	private Button dropButton;
	private ArrayList<String> recipients;
	private Note note;
	private LocationClient client;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View layout = getLayoutInflater().inflate(
				R.layout.activity_edit_note_screen, null);
		FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
		frame.addView(layout);

		client = new LocationClient(this, this, this);
		recipients = new ArrayList<String>();

		messageEdt = (EditText) findViewById(R.id.edit_note_content);

		thumbnail = (ImageView) findViewById(R.id.edit_note_thumbnail);
		if (thumbnail == null)
			Log.i("EDIT", "NO THUMBNAIL");

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

		note = Drop.current_note;
		load_note(note);

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
	
	@Override
	public void onStart()
	{
		super.onStart();
		client.connect();
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		client.disconnect();
	}

	public void load_note(Note note) {
		setImage(note.getPicture(thumbnail.getHeight(), thumbnail.getWidth()));
	}

	public void setImage(Bitmap b) {
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
		note.setCreator(Drop.loggedInJSON.toString());
		note.setRecievers(recipients);
		note.setMessage(messageEdt.getText().toString());
		note.setPickedUp(false);
		Location loc = client.getLastLocation();
		note.setLon(loc.getLongitude());
		note.setLat(loc.getLatitude());
		Intent i = new Intent(EditNoteScreen.this, DropNoteScreen.class);
		Drop.current_note = note;
		startActivity(i);

		// close this activity
		finish();
	}

	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			// If no resolution is available, display a dialog to the user with
			// the error.
			showErrorDialog(connectionResult.getErrorCode());
		}
	}

	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	/**
	 * Show a dialog returned by Google Play services for the connection error
	 * code
	 * 
	 * @param errorCode
	 *            An error code returned from onConnectionFailed
	 */
	private void showErrorDialog(int errorCode) {

		// Get the error dialog from Google Play services
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
				this, LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

		errorDialog.show();
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

	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

}
