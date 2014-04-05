package com.example.drop;

import java.io.File;
import java.io.FileInputStream;

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
    
	byte [] data;
	Bitmap b;
	SeekBar colorBar;
	DrawingWidget drawingWidget;
	Button nextButton;
	boolean isBack;
	DrawingWidget background;
	File pictureFile;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_screen);
        
        Intent i = getIntent();
        pictureFile = (File)i.getSerializableExtra("image");
        isBack = i.getBooleanExtra("isBack", false);
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
        background = (DrawingWidget)findViewById(R.id.background_picture);
        
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
			        if(isBack)
			        {
			        	matrix.postRotate(90);
				        b = Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), matrix, true);
			        }
			        else
			        {
			        	matrix.postRotate(270);
				        b = Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), matrix, true);
			        	matrix.setScale(-1,1);
				        b = Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), matrix, true);
			        }
		        }
		        else
		        {
		        	Log.i("DRAW", background.getHeight() +" x "+ background.getWidth());
			        b = Bitmap.createScaledBitmap(b, background.getWidth(), background.getHeight(), true);
		        }
		        
		        background.setBitmap(b);
            }
        });
                
        colorBar = (SeekBar)findViewById(R.id.color_slider);
        colorBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				float hue = progress/100.0f*360;

				Log.i("DRAW", "hue ="+hue);
				float [] hsv = {hue,(float) 1.0,(float) 1.0};
				
			    Color c = new Color();
			    background.setColor(c.HSVToColor(hsv));
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        colorBar.setProgress(0);
        
        Button nextButton = (Button)findViewById(R.id.drawscreen_button_next);
        nextButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent i = new Intent(DrawScreen.this, EditNoteScreen.class);
                startActivity(i);
 
                // close this activity
                finish();
			}
        	
        });
        
        Button clearButton = (Button)findViewById(R.id.drawscreen_button_clear);
        clearButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
	        	data = null;
	        	background.clearContents();
		        System.gc();
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
				
				//Get image taken
		        BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inMutable = true;
		        b = null;
		        System.gc();
		        b = BitmapFactory.decodeByteArray(data, 0, data.length, options);
			
				//Rotate and scale image
		        if(b.getHeight() < b.getWidth())
		        {
			        Log.i("DRAW", background.getHeight() +" x "+ background.getWidth());
			        b = Bitmap.createScaledBitmap(b, background.getHeight(), background.getWidth(), true);
			        Matrix matrix = new Matrix();
			        if(isBack)
			        {
			        	matrix.postRotate(90);
				        b = Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), matrix, true);
			        }
			        else
			        {
			        	matrix.postRotate(270);
				        b = Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), matrix, true);
			        	matrix.setScale(-1,1);
				        b = Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), matrix, true);
			        }
		        }
		        else
		        {
		        	Log.i("DRAW", background.getHeight() +" x "+ background.getWidth());
			        b = Bitmap.createScaledBitmap(b, background.getWidth(), background.getHeight(), true);
		        }
		        
		        background.setBitmap(b);
			}
        	
        });
    }

    
    @Override
    public void onStop() {
    	if(b != null)
    	{
    		b.recycle();
    		b = null;
    	}
    	System.gc();
        
        super.onStop();
    }
    
    @Override
    public void onBackPressed() {
        // Open up the CameraScreen
    	Intent i = new Intent(this, CameraScreen.class);
    	i.putExtra("isBack", isBack);
        startActivity(i);
        this.finish();
        
        super.onBackPressed();
    }

}
