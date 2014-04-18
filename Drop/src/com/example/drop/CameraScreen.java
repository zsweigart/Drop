package com.example.drop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

public class CameraScreen extends Activity {

	protected static final String TAG = "CAMERA";	//Tag for log outputs
	private File outputFile;	//File where the picture will be saved

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_screen);
		
		outputFile = getOutputMediaFile("/Android/data/com.example.drop/pictures");

		//Open the camera and store the picture to the file
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent retData) {
		super.onActivityResult(requestCode, resultCode, retData);
		
		if(resultCode == RESULT_OK)	//Picture was taken move to the Draw Screen
		{
			Intent i = new Intent(CameraScreen.this, DrawScreen.class);
			i.putExtra("image", outputFile);
			startActivity(i);
			CameraScreen.this.finish();
		}
		else if(resultCode == RESULT_CANCELED)	//Picture was not taken close the screen
		{
			CameraScreen.this.finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	// Create a File for saving an image 
	private  File getOutputMediaFile(String path){
		
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + path);

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            return null;
	        }
	    } 
	    
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
	    File mediaFile;
	        String mImageName="MI_"+ timeStamp +".jpg";
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);  
	    return mediaFile;
	} 

}
