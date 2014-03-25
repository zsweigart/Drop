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
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

public class DrawScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_screen);
        
        Intent i = getIntent();
        File pictureFile = (File)i.getSerializableExtra("image");
        byte [] data  = new byte[(int) pictureFile.length()];
        
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
        BitmapFactory factory = new BitmapFactory();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        b = Bitmap.createScaledBitmap(b, metrics.widthPixels, metrics.heightPixels, true);

        // Rotating Bitmap 
        /*int w = b.getWidth();
        int h = b.getHeight();
        Matrix mtx = new Matrix();
        mtx.preRotate(90);
        b = Bitmap.createBitmap(b, 0, 0, w, h, mtx, false);*/
        DrawingWidget background = (DrawingWidget)findViewById(R.id.background_picture);
        background.setBitmap(b);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_draw_screen, menu);
        return true;
    }
}
