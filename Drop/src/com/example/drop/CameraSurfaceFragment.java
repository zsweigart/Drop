package com.example.drop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
		
		isBack = false;

		setCamera();

		// Add a listener to the Capture button Button captureButton =
		Button captureButton = (Button) view.findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { // get an image from the camera
				Drop.current_note = new Note();
				Drop.current_note.setPicture(Drop.getOutputMediaFile("/Android/data/com.example.drop/pictures"));
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
		swapButton.setVisibility(View.GONE);

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
			if(!isBack)
			{
				/*Matrix matrix = new Matrix();
				matrix.preScale(1.0f, 1.0f);
				Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
				Bitmap bm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
				bmp = null;
				System.gc();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
				data = stream.toByteArray();
				bm = null;
				System.gc();*/
			}

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
			i.putExtra("image", Drop.current_note.getPictureFile());
			i.putExtra("isBack", isBack);
			startActivity(i);
		}
	};

	@Override
	public void onResume() {
		super.onResume();

		if (mCamera == null) {
			Log.i("CAMERA", "OPENING");
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
		Log.i("CAMERA", "count = " + cameraCount);
		Log.i("CAMERA", "isBack = " + isBack);
		int id = 0;

		if (isBack) {
			for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
				Camera.getCameraInfo(camIdx, cameraInfo);
				Log.i("CAMERA", "cameraInfoFacing = " + cameraInfo.facing);
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
					try {
						Log.i("CAMERA", "SETTING mCamera");
						mCamera = Camera.open(camIdx);
						id = camIdx;
					} catch (RuntimeException e) {
						Log.e(TAG,
								"Camera failed to open: "
										+ e.getLocalizedMessage());
					}
					break;
				}
			}

			if (mCamera != null) {

				if (mPreview != null) {
					mPreview.switchCamera(mCamera, id);
				} else {
					mPreview = new CameraPreview(getActivity(), mCamera, id);
					FrameLayout preview = (FrameLayout) view
							.findViewById(R.id.camera_preview);
					preview.addView(mPreview);
				}
				mCamera.startPreview();
			}
		} else {
			for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
				Camera.getCameraInfo(camIdx, cameraInfo);
				Log.i("CAMERA", "cameraInfoFacing = " + cameraInfo.facing);
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
					try {
						Log.i("CAMERA", "SETTING mCamera");
						mCamera = Camera.open(camIdx);
						id = camIdx;
					} catch (RuntimeException e) {
						Log.e(TAG,
								"Camera failed to open: "
										+ e.getLocalizedMessage());
					}
					break;
				}
			}

			if (mCamera != null) {
				if (mPreview != null) {
					mPreview.switchCamera(mCamera, id);
				} else {
					mPreview = new CameraPreview(getActivity(), mCamera, id);
					FrameLayout preview = (FrameLayout) view
							.findViewById(R.id.camera_preview);
					preview.addView(mPreview);
				}
				mCamera.startPreview();
			}
		}
	}
}
