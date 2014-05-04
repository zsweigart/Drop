package com.example.drop;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.Toast;

//This activity requires that a note is placed in its intent
public class ViewNoteScreen extends DrawerFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		Note current_note = (Note) getIntent().getSerializableExtra(
				"com.example.drop.Note");
		boolean wasFound = getIntent().getBooleanExtra("wasFound", true);
		boolean justPickedUp = getIntent().getBooleanExtra("justPickedUp",
				false);
		
		//Mark the note as picked up and unregister the geofence
		if(justPickedUp)
		{
			DatabaseConnector.markPickedUp(current_note.getId());
			GeofenceRemover remover = new GeofenceRemover(this);
			ArrayList <String> geofenceList = new ArrayList<String>();
			geofenceList.add(current_note.getId());
			remover.removeGeofencesById(geofenceList);
		}

		if (current_note == null)
			Toast.makeText(getApplicationContext(),
					"Note not passed to ViewNote Activity", Toast.LENGTH_LONG)
					.show();

		getSupportFragmentManager()
				.beginTransaction()	
				.replace(
						android.R.id.content,
						ViewNoteFragment.newInstance(current_note, wasFound,
								justPickedUp)).commit();
	}

}
