package org.codeforhealth.hip.android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TransparentPanel extends RelativeLayout 
{ 
	private Paint	innerPaint, borderPaint ;
    
	public TransparentPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TransparentPanel(Context context) {
		super(context);
		init();
	}

	private void init() {
		innerPaint = new Paint();
		//innerPaint.setARGB(255, 255, 255, 255); //gray
		innerPaint.setARGB(225, 75, 75, 75);
		innerPaint.setAntiAlias(true);

		/*
		borderPaint = new Paint();
		borderPaint.setARGB(0, 0, 0, 0);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(2);
		*/
		
		borderPaint = new Paint();
		borderPaint.setARGB(255, 255, 255, 255);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(2);
	}
	
	public void setInnerPaint(Paint innerPaint) {
		this.innerPaint = innerPaint;
	}

	public void setBorderPaint(Paint borderPaint) {
		this.borderPaint = borderPaint;
	}

    @Override
    protected void dispatchDraw(Canvas canvas) {
    	
    	RectF drawRect = new RectF();
    	drawRect.set(0,10, getMeasuredWidth(), getMeasuredHeight()- 10);
    	
    	canvas.drawRect(drawRect, innerPaint);
		canvas.drawRect(drawRect, borderPaint);
		
		super.dispatchDraw(canvas);
    }
}