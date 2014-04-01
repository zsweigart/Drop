package com.example.drop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


public class CameraScreen extends Activity {

	protected static final String TAG = null;
	private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_screen);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
    	Camera.Parameters cameraParameters = mCamera.getParameters();
        //cameraParameters.setRotation(90); 
    	cameraParameters.setPictureFormat(ImageFormat.JPEG); 
    	cameraParameters.set("orientation", "portrait");
    	cameraParameters.setRotation(90);
        mCamera.setParameters(cameraParameters);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        // Add a listener to the Capture button
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    // get an image from the camera
                    mCamera.takePicture(null, null, mPicture);
                }
            }
        );
    }
    
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable

    }
    
    private PictureCallback mPicture = new PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
        	File pictureFile = new File(CameraScreen.this.getCacheDir(), "picture");

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        	Intent i = new Intent(CameraScreen.this, DrawScreen.class);
        	i.putExtra("image", pictureFile);
        	startActivity(i);
        	CameraScreen.this.finish();
        }
    };
    
    @Override
	protected void onResume() {
		super.onResume();
		if(mCamera == null) {
			Log.i("CAMERA", "OPENING");
			
			// Create an instance of Camera
	        mCamera = getCameraInstance();
	        mCamera.setDisplayOrientation(90);
	    	Camera.Parameters cameraParameters = mCamera.getParameters();
	    	cameraParameters.setPictureFormat(ImageFormat.JPEG); 
	    	cameraParameters.set("orientation", "portrait");
	    	cameraParameters.setRotation(90);
	        mCamera.setParameters(cameraParameters);

	        // Create our Preview view and set it as the content of our activity.
	        mPreview = new CameraPreview(this, mCamera);
	        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
	        preview.addView(mPreview);
		}
	}

	@Override
	protected void onPause() {
		if(mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
		super.onPause();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_camera_sceen, menu);
        return true;
    }
}
