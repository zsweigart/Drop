package com.example.drop;

import java.io.File;
import java.io.FileInputStream;
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
    
	private static final String TAG = "DRAW";
	byte [] data;
	Bitmap b;
	SeekBar colorBar;
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
        /*data  = new byte[(int) pictureFile.length()];
        Log.i(TAG, "BYTES = " + pictureFile.length());
        
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
        }*/
        background = (DrawingWidget)findViewById(R.id.background_picture);
        
        background.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout() {
                // Ensure you call it only once :
            	background.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            	//Get image taken
		        BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inMutable = true;
		        options.inScaled = false;
		        options.inSampleSize = 2;
		        b = BitmapFactory.decodeFile(pictureFile.toString()); 
		        Log.i(TAG, b.getWidth() + " x " + b.getHeight());
		        		        
		        //Rotate and scale image
		        if(b.getHeight() < b.getWidth())
		        {
		        	//int height = (int)((((double)background.getWidth())/((double)b.getWidth()))*background.getHeight());
			        Log.i("DRAW", background.getHeight() +" x "+ background.getWidth());
			        b = Bitmap.createScaledBitmap(b, background.getHeight(), background.getWidth(), true);
			        Matrix matrix = new Matrix();
		        	matrix.postRotate(90);
			        b = Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), matrix, true);
		        }
		        else
		        {
		        	//int width = (int)((((double)background.getWidth())/((double)b.getWidth()))*background.getHeight());
		           	Log.i("DRAW", background.getHeight() +" x "+ background.getWidth());
			        b = Bitmap.createScaledBitmap(b, background.getWidth(), background.getHeight(), true);
		        }
		        
		        Log.i(TAG, b.getWidth()+" x " + b.getHeight());
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
				try {
		            FileOutputStream fos = new FileOutputStream(pictureFile);
		            background.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fos);
		            fos.close();
		        } catch (FileNotFoundException e) {
		            Log.d(TAG, "File not found: " + e.getMessage());
		        } catch (IOException e) {
		            Log.d(TAG, "Error accessing file: " + e.getMessage());
		        }
				
				Intent i = new Intent(DrawScreen.this, EditNoteScreen.class);
				i.putExtra("Note", new Note(pictureFile));
                startActivity(i);
 
                // close this activity
                finish();
			}
        	
        });
        
        Button clearButton = (Button)findViewById(R.id.drawscreen_button_clear);
        clearButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//Get image taken
		        BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inMutable = true;
		        options.inScaled = false;
		        options.inSampleSize = 2;
		        b = BitmapFactory.decodeFile(pictureFile.toString()); 
		        Log.i(TAG, b.getWidth() + " x " + b.getHeight());
		        		        
		        //Rotate and scale image
		        if(b.getHeight() < b.getWidth())
		        {
		        	//int height = (int)((((double)background.getWidth())/((double)b.getWidth()))*background.getHeight());
			        Log.i("DRAW", background.getHeight() +" x "+ background.getWidth());
			        b = Bitmap.createScaledBitmap(b, background.getHeight(), background.getWidth(), true);
			        Matrix matrix = new Matrix();
		        	matrix.postRotate(90);
			        b = Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), matrix, true);
		        }
		        else
		        {
		        	//int width = (int)((((double)background.getWidth())/((double)b.getWidth()))*background.getHeight());
		           	Log.i("DRAW", background.getHeight() +" x "+ background.getWidth());
			        b = Bitmap.createScaledBitmap(b, background.getWidth(), background.getHeight(), true);
		        }
		        
		        Log.i(TAG, b.getWidth()+" x " + b.getHeight());
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
