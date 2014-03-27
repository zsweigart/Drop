package com.example.drop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DrawScreen extends Activity {
    
	byte [] data;
	Bitmap b;
	SeekBar colorBar;
	DrawingWidget drawingWidget;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_screen);
        
        Intent i = getIntent();
        File pictureFile = (File)i.getSerializableExtra("image");
        data  = new byte[(int) pictureFile.length()];
        
        try 
        {
            //convert file into array of bytes
        	FileInputStream fileInputStream = new FileInputStream(pictureFile);
	    	fileInputStream.read(data);
	    	fileInputStream.close();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        final DrawingWidget background = (DrawingWidget)findViewById(R.id.background_picture);
        
        background.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout() {
                // Ensure you call it only once :
            	background.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            	//Get image taken
		        BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inMutable = true;
		        b = BitmapFactory.decodeByteArray(data, 0, data.length, options);
		        
		        //Rotate and scale image
		        if(b.getHeight() < b.getWidth())
		        {
			        Log.i("DRAW", background.getHeight() +" x "+ background.getWidth());
			        b = Bitmap.createScaledBitmap(b, background.getHeight(), background.getWidth(), true);
			        Matrix matrix = new Matrix();
			        matrix.postRotate(90);
			        b = Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), matrix, true);
		        }
		        else
		        {
		        	Log.i("DRAW", background.getHeight() +" x "+ background.getWidth());
			        b = Bitmap.createScaledBitmap(b, background.getWidth(), background.getHeight(), true);
		        }
		        
		        background.setBitmap(b);
            }
        });
        
        drawingWidget = (DrawingWidget)findViewById(R.id.background_picture);
        
        colorBar = (SeekBar)findViewById(R.id.color_slider);
        colorBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				float hue = progress/100.0f*360;

				Log.i("DRAW", "hue ="+hue);
				float [] hsv = {hue,(float) 1.0,(float) 1.0};
				
			    Color c = new Color();
			    drawingWidget.setColor(c.HSVToColor(hsv));
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        colorBar.setProgress(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_draw_screen, menu);
        return true;
    }
    
    @Override
    public void onStop() {
    	b.recycle();
    	b = null;
    	System.gc();
        
        super.onStop();
    }
    
    @Override
    public void onBackPressed() {
        // Open up the CameraScreen
    	Intent i = new Intent(this, CameraScreen.class);
        startActivity(i);
        this.finish();
        
        super.onBackPressed();
    }
}
