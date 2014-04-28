package com.example.drop;

import java.io.IOException;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private static final String TAG = "CameraPreview";
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private Context mContext;
	private int CAM_ID;

	@SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera, int id) {
		super(context);
		mCamera = camera;
		mContext = context;
		CAM_ID = id;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		setCameraParams(mHolder);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();

		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}
	
	private void setCameraParams(SurfaceHolder holder)
	{
		if (mCamera == null) {
			Log.d(TAG, "setCameraDisplayOrientation - camera null");
			return;
		}

		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(CAM_ID, info);

		WindowManager winManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		int rotation = winManager.getDefaultDisplay().getRotation();

		int degrees = 0;

		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		
		Log.i(TAG, "DEGREES: " + degrees);
		Log.i(TAG, "INFO ORIENTATION: " + info.orientation);
		Log.i(TAG, "CAMERA FACING: " + info.facing);

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			Log.i(TAG, "FACING FRONT");
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}

		Log.i(TAG, "RESULT: " + result);
		
		mCamera.setDisplayOrientation(result);
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Camera.Parameters cameraParameters = mCamera.getParameters();
		cameraParameters.setPictureFormat(ImageFormat.JPEG);
		cameraParameters.set("orientation", "portrait");
		cameraParameters.setRotation(result);
		mCamera.setParameters(cameraParameters);
		mCamera.startPreview();
	}

	public void setCamera(Camera camera) {
		mCamera = camera;

		setCameraParams(mHolder);
	}

	public void setCamera(Camera camera, int id) {
		mCamera = camera;
		CAM_ID = id;

		setCameraParams(mHolder);
	}

	public void switchCamera(Camera camera) {
		setCamera(camera);
		try {
			camera.setPreviewDisplay(mHolder);
		} catch (IOException exception) {
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
	}

	public void switchCamera(Camera camera, int id) {
		setCamera(camera, id);
		try {
			camera.setPreviewDisplay(mHolder);
		} catch (IOException exception) {
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
	}
}