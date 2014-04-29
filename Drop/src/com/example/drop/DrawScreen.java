package com.example.drop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DrawScreen extends Activity {

	private static final String TAG = "DRAW"; // Used for logging
	Bitmap b; //Used to load the picture from the file
	SeekBar colorBar; //Sets the color to be drawn with
	Button nextButton; //Goes to the next screen
	DrawingWidget background; //Allows the picture to be drawn on
	File pictureFile; // Points to the file created with the picture by the
						// camera
	boolean isBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_screen);

		// Get the location of the picture
		Intent i = getIntent();
		pictureFile = (File) i.getSerializableExtra("image");
		isBack = i.getBooleanExtra("isBack", false);
		Log.i(TAG, pictureFile.toString());

		background = (DrawingWidget) findViewById(R.id.background_picture);

		background.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@SuppressWarnings("deprecation")
					public void onGlobalLayout() {
						// Ensure you call it only once :
						background.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);

						setPicture();
					}
				});

		colorBar = (SeekBar) findViewById(R.id.color_slider);
		colorBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				float hue = progress / 100.0f * 360;

				Log.i("DRAW", "hue =" + hue);
				float[] hsv = { hue, (float) 1.0, (float) 1.0 };

				background.setColor(Color.HSVToColor(hsv));
			}

			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			public void onStopTrackingTouch(SeekBar seekBar) {

			}

		});
		colorBar.setProgress(0);

		Button nextButton = (Button) findViewById(R.id.drawscreen_button_next);
		nextButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				try {
					FileOutputStream fos = new FileOutputStream(pictureFile);
					background.getBitmap().compress(Bitmap.CompressFormat.PNG,
							75, fos);
					fos.close();
				} catch (FileNotFoundException e) {
					Log.d(TAG, "File not found: " + e.getMessage());
				} catch (IOException e) {
					Log.d(TAG, "Error accessing file: " + e.getMessage());
				}

				Intent i = new Intent(DrawScreen.this, EditNoteScreen.class);
				Drop.current_note = new Note(pictureFile);
				startActivity(i);

				// close this activity
				finish();
			}

		});

		Button clearButton = (Button) findViewById(R.id.drawscreen_button_clear);
		clearButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				setPicture();
			}

		});
	}

	@Override
	public void onStop() {
		if (b != null) {
			b.recycle();
			b = null;
		}
		System.gc();

		super.onStop();
	}

	private void setPicture() {
		// Determine the sample size of the picture
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pictureFile.toString(), options);
		int sampleSize = calculateInSampleSize(options, background.getHeight(),
				background.getWidth());

		Log.i(TAG, "SAMPLE SIZE = " + sampleSize);

		// Get image taken
		options = new BitmapFactory.Options();
		options.inMutable = true;
		options.inScaled = false;
		options.inSampleSize = sampleSize;

		// Clear out the current bitmap if there is already one
		if (b != null) {
			b = null;
			System.gc();
		}


		b = BitmapFactory.decodeFile(pictureFile.toString());
		Log.i(TAG, b.getWidth() + " x " + b.getHeight());

		// Rotate and scale image
		if (b.getHeight() < b.getWidth()) {
			Log.i("DRAW",
					background.getHeight() + " x " + background.getWidth());
			b = Bitmap.createScaledBitmap(b, background.getHeight(),
					background.getWidth(), true);
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			if(!isBack)
			{
				float[] mirrorY = { -1, 0, 0, 0, 1, 0, 0, 0, -1};
	            Matrix matrixMirrorY = new Matrix();
	            matrixMirrorY.setValues(mirrorY);
	            matrix.postConcat(matrixMirrorY);

			}
			b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
					matrix, true);
		} else {
			Log.i("DRAW",
					background.getHeight() + " x " + background.getWidth());
			if(!isBack)
			{
	            Matrix matrix = new Matrix();
	            matrix.postScale(-1.0f, -1.0f);
	            b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
						matrix, true);
			}
			else
			{
				b = Bitmap.createScaledBitmap(b, background.getWidth(),
					background.getHeight(), true);
			}
		}

		Log.i(TAG, b.getWidth() + " x " + b.getHeight());
		background.setBitmap(b);
		b = null;
		System.gc();
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

}
