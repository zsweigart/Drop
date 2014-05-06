package com.example.drop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraSurfaceFragment extends Fragment {

	protected static final String TAG = "CAMERA_SURFACE"; // Tag for log outputs
	private Camera mCamera;
	private boolean isBack;
	private CameraPreview mPreview;
	private View view;

	static Fragment init() {
		CameraSurfaceFragment cameraFrag = new CameraSurfaceFragment();
		Bundle args = new Bundle();
		cameraFrag.setArguments(args);
		return cameraFrag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.fragment_camera_surface, container,
				false);
		
		view.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent arg1) {

				mCamera.autoFocus(new AutoFocusCallback(){

					public void onAutoFocus(boolean success, Camera mCamera) {
				        Log.i(TAG, "Inside autofocus callback. autofocused="+success);
				        //play the autofocus sound
					}
					
				});
				return true;
			}
			
		});

		isBack = true;

		setCamera();

		// Add a listener to the Capture button Button captureButton =
		Button captureButton = (Button) view.findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { // get an image from the camera
				Drop.current_note = new Note();
				Drop.current_note.setPicture(Drop.getOutputMediaFile(Drop.PICTURE_DIR));
				mCamera.takePicture(null, null, mPicture);
			}
		});

		// Add a listener to the swap button Button swapButton = (Button)
		Button swapButton = (Button) view
				.findViewById(R.id.button_switch_camera);
		swapButton.setOnClickListener(new View.OnClickListener() {
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
		});

		return view;
	}

	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable

	}

	private PictureCallback mPicture = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			File pictureFile = Drop.current_note.getPictureFile();

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}
			Intent i = new Intent(getActivity(), DrawScreen.class);
			i.putExtra("image", pictureFile);
			i.putExtra("isBack", isBack);
			data = null;
			System.gc();
			startActivity(i);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		System.gc();
		if (mCamera == null) {
			Log.i(TAG, "OPENING");
			setCamera();
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	private void setCamera() {
		// Create an instance of Camera
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		int cameraCount = Camera.getNumberOfCameras();
		Log.i(TAG, "count = " + cameraCount);
		Log.i(TAG, "isBack = " + isBack);
		int id = 0;

		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			Log.i(TAG, "cameraInfoFacing = " + cameraInfo.facing);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK
					&& isBack) {
				try {
					Log.i(TAG, "SETTING mCamera");
					mCamera = Camera.open(camIdx);
					id = camIdx;
				} catch (RuntimeException e) {
					Log.e(TAG,
							"Camera failed to open: " + e.getLocalizedMessage());
				}
				break;
			} else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT
					&& !isBack){
				try {
					Log.i(TAG, "SETTING mCamera");
					mCamera = Camera.open(camIdx);
					id = camIdx;
				} catch (RuntimeException e) {
					Log.e(TAG,
							"Camera failed to open: " + e.getLocalizedMessage());
				}
				break;
			}
		}

		if (mCamera != null) {
			Camera.Parameters cameraParameters = mCamera.getParameters();
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int screenWidth = size.x;
			int width = Integer.MAX_VALUE;
			int height = 0;
			Log.i(TAG, "SCREENWIDTH: " + screenWidth);
			List<Camera.Size> sizes = cameraParameters.getSupportedPreviewSizes();
			for (Camera.Size cSize : sizes) {
				if (cSize.width >= screenWidth && cSize.width <= width) {
					width = cSize.width;
					height = cSize.height;
				}
			}
			
			Log.i(TAG, "Width: " + width + "    HEIGHT: " + height);

			if (mPreview != null) {
				mPreview.switchCamera(mCamera, id, width, height);
			} else {
				mPreview = new CameraPreview(getActivity(), mCamera, id, width, height);
				FrameLayout preview = (FrameLayout) view
						.findViewById(R.id.camera_preview);
				preview.addView(mPreview);
			}
			mCamera.startPreview();
		}
	}
}
