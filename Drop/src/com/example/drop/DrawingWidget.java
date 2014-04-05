package com.example.drop;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingWidget extends View 
{

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Paint mBitmapPaint;
	private Paint mPaint;
	private ArrayList <Integer> strokes;

	public DrawingWidget(Context c) 
	{
		super(c);

		mBitmap = Bitmap.createBitmap(900, 1100, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
		mPaint.setStrokeWidth(6);
		mBitmap.eraseColor(Color.WHITE);
		strokes = new ArrayList <Integer> ();
		invalidate();
	}
	

	public DrawingWidget(Context context, AttributeSet attr)
	{
		super(context,attr);
		mBitmap = Bitmap.createBitmap(900, 1100, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
		mPaint.setStrokeWidth(6);
		mPaint.setColor(Color.BLACK);
		mBitmap.eraseColor(Color.WHITE);
		strokes = new ArrayList <Integer> ();
		invalidate();
	}


	public DrawingWidget(Context context, AttributeSet attr, int defaultStyles)
	{
		super(context, attr, defaultStyles);
		
		mBitmap = Bitmap.createBitmap(900, 1100, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
		mPaint.setStrokeWidth(6);
		mPaint.setColor(Color.BLACK);
		mBitmap.eraseColor(Color.WHITE);
		strokes = new ArrayList <Integer> ();
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) 
	{
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) 
	{
		canvas.drawColor(0xFFAAAAAA);
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
	}


	private void touch_start(float x, float y) 
	{
		mX = x;
		mY = y;
		strokes.add((int)(x/4));
		strokes.add((int)(y/4));
	}

	private void touch_move(float x, float y) 
	{
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) 
		{
			mCanvas.drawLine(mX, mY, x, y, mPaint);
			mX = x;
			mY = y;
			if(Math.sqrt(Math.pow((strokes.get(strokes.size()-2)-x/4), 2)+Math.pow((strokes.get(strokes.size()-1)-y/4), 2)) > 4)
			{
				strokes.add((int)(x/4));
				strokes.add((int)(y/4));
			}
		}
	}
	
	private void touch_end(float x, float y) 
	{
		strokes.add((int)(x/4));
		strokes.add((int)(y/4));
		strokes.add(255);
		strokes.add(0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
				touch_end(x, y);
				break;
		}
		return true;
	}
	
	public void clearContents()
	{
		mBitmap = Bitmap.createBitmap(900, 1100, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mBitmap.eraseColor(Color.WHITE);
		invalidate();
	}
	
	public void setColor(int c)
	{
		mPaint.setColor(c);
	}
	
	public void setStrokeWidth(float w)
	{
		mPaint.setStrokeWidth(w);
	}
	
	public void setBitmap(Bitmap b)
	{
		mBitmap = b;
		mCanvas = new Canvas(mBitmap);
		invalidate();
	}
	
	public int getColor()
	{
		return mPaint.getColor();
	}
	
	public float getStrokeWidth()
	{
		return mPaint.getStrokeWidth();
	}
	
	public Bitmap getBitmap()
	{
		return mBitmap;
	}
	
	public void resetStrokes()
	{
		strokes = new ArrayList <Integer> ();
	}
	public ArrayList <Integer> getStrokes()
	{
		return strokes;
	}
	
	public void setStrokes(ArrayList <Integer> s)
	{
		strokes = s;
	}
}