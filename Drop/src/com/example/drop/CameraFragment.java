package com.example.drop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CameraFragment extends Fragment {

	protected static final String TAG = "CAMERA";	//Tag for log outputs
	private Button newNoteBtn;
	
	static Fragment init() {
		CameraFragment cameraFrag = new CameraFragment();
        Bundle args = new Bundle();
        cameraFrag.setArguments(args);
        return cameraFrag;
    }

	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_camera_screen,
		        container, false);
		
		Drop.current_note = new Note();
		Drop.current_note.setPicture(Drop.getOutputMediaFile("/Android/data/com.example.drop/pictures"));

		newNoteBtn = (Button) view.findViewById(R.id.camera_fragment_new_note_btn);
		newNoteBtn.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				//Open the camera and store the picture to the file
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Drop.current_note.getPictureFile()));
				getActivity().startActivityForResult(intent, Drop.CAMERA_INTENT_REQUEST);
			}
			
		});

		return view;
	}
}
