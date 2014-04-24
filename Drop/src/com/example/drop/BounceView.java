package com.example.drop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class BounceView extends ImageView {
	private Context mContext;
	private int bounces = 0;
	int x = -1;
	int y = -1;
	private int yVelocity = 40;
	private Handler h;
	private final int FRAME_RATE = 30;

	public BounceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		h = new Handler();

	}

	private Runnable r = new Runnable() {
		public void run() {
			invalidate();
		}
	};

	protected void onDraw(Canvas c) {
		BitmapDrawable ball = (BitmapDrawable) mContext.getResources()
				.getDrawable(R.drawable.drop_icon);
		if (x < 0 && y < 0) {
			x = this.getWidth() / 2;
			y = this.getHeight() / 2 - ball.getBounds().height();
		} else {
			y += yVelocity;
			Log.i("BOUNCE", "y = " + y + "   height = " +this.getHeight() + "   ball = " +2*ball.getBounds().height());
			if (y < ((this.getHeight()/2)*Math.pow(.8, bounces)))
			{
				y = (int) ((this.getHeight()/2)*Math.pow(.8, bounces));
				yVelocity = (int)(yVelocity * -1);
				bounces++;
			}
			if (y >= (this.getHeight()*.75-2*ball.getBounds().height())){
				y = this.getHeight()-2*ball.getBounds().height();
				yVelocity = (int)(yVelocity * -.8);
				bounces++;
			}
		}
		c.drawBitmap(ball.getBitmap(), x, y, null);
		h.postDelayed(r, FRAME_RATE);
	}
}
