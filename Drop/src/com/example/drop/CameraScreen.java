package com.example.drop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


public class CameraScreen extends Activity {

	protected static final String TAG = null;
	private Camera mCamera;
    private CameraPreview mPreview;
    private boolean isBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_screen);
        
        Intent i = getIntent();
        if(i.hasExtra("isBack"))
        {
        	isBack = i.getBooleanExtra("isBack",true);
        }
        else
        {
        	isBack = true;
        }
        
        setCamera();
        
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
        
     // Add a listener to the swap button
        Button swapButton = (Button) findViewById(R.id.button_switch_camera);
        swapButton.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                	if (mCamera != null) {
                		mCamera.stopPreview();
                		mCamera.setPreviewCallback(null);
                		mCamera.release();
                		mCamera = null;
                		System.gc();
                	} 
                	isBack = !isBack;
                	
                	setCamera();
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
        	i.putExtra("isBack", isBack);
        	startActivity(i);
        	CameraScreen.this.finish();
        }
    };
    
    @Override
	protected void onResume() {
		super.onResume();
		if(mCamera == null) {
			Log.i("CAMERA", "OPENING");
			
			setCamera();
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
    
    private void setCamera()
    {
    	// Create an instance of Camera
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		int cameraCount = Camera.getNumberOfCameras();
		Log.i("CAMERA", "count = " + cameraCount);
		Log.i("CAMERA", "isBack = " + isBack);
		
    	if(isBack)
    	{
    		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
    			Camera.getCameraInfo( camIdx, cameraInfo );
        		Log.i("CAMERA", "cameraInfoFacing = " + cameraInfo.facing);
    			if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK  ) 
    			{
    				try {
                		Log.i("CAMERA", "SETTING mCamera");
    					mCamera = Camera.open( camIdx );
    				} catch (RuntimeException e) {
    					Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
    				}
        			break;	
    			}
    		}
    		
    		
    		if(mCamera != null)
    		{
    			mCamera.setDisplayOrientation(90);
             	Camera.Parameters cameraParameters = mCamera.getParameters();
             	cameraParameters.setPictureFormat(ImageFormat.JPEG); 
             	cameraParameters.set("orientation", "portrait");
             	cameraParameters.setRotation(90);
                 mCamera.setParameters(cameraParameters);
                 
                if(mPreview != null)
                {
                	mPreview.switchCamera(mCamera);
                }
                else
                {
                	mPreview = new CameraPreview(this, mCamera);
                	FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        	        preview.addView(mPreview);
                }
                mCamera.startPreview();
    		}
    	}
    	else
    	{
    		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
    			Camera.getCameraInfo( camIdx, cameraInfo );
        		Log.i("CAMERA", "cameraInfoFacing = " + cameraInfo.facing);
    			if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  ) 
    			{
    				try {
                		Log.i("CAMERA", "SETTING mCamera");
    					mCamera = Camera.open( camIdx );
    				} catch (RuntimeException e) {
    					Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
    				}	
        			break;
    			}
    		}
    		
    		if(mCamera != null)
    		{
                mCamera.setDisplayOrientation(90);
            	Camera.Parameters cameraParameters = mCamera.getParameters();
            	cameraParameters.setPictureFormat(ImageFormat.JPEG); 
            	cameraParameters.set("orientation", "portrait");
            	cameraParameters.setRotation(270);
                mCamera.setParameters(cameraParameters);

                if(mPreview != null)
                {
                	mPreview.switchCamera(mCamera);
                }
                else
                {
                	mPreview = new CameraPreview(this, mCamera);
                	FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        	        preview.addView(mPreview);
                }
                mCamera.startPreview();
    		}
    	}
    }

}
