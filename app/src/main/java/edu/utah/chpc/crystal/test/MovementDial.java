package edu.utah.chpc.crystal.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by crystal on 10/25/2017.
 */

public class MovementDial extends AppCompatImageView {

    public float x, y, Radius, _theta, yShift, offset;
    //private int origin = 90;
    private double xVal, yVal, distance;

    private RectF _knobRect = new RectF();
    private PointF nibCenter;
    private RectF nibRect = new RectF();

    OnAngleChangedListener _angleChangedListener = null;


    public interface OnAngleChangedListener {
        void onAngleChanged(float theta);
    }



    public MovementDial(Context context) {
        super(context);


       // this.nobWidth = (int) (_knobRect.width() * .3);

       // this.x=getX();
       // this.y=getY();
        nibCenter = new PointF(_knobRect.centerX(), _knobRect.centerY());
    }

    public float getTheta() {
        return _theta;

    }

    public void setTheta (float theta){
        _theta = theta;
        invalidate();
    }


    public void setOnAngleChangedListener(OnAngleChangedListener listener) {
        _angleChangedListener = listener;
    }

    private int map(double x, double in_min, double in_max, double out_min, double out_max)
    {
        int mapVal = (int) ((int)(x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
        return mapVal;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {



        PointF touchPoint = new PointF();

        touchPoint.x = event.getX() -  _knobRect.centerX();
        touchPoint.y = map((event.getY()),0,(_knobRect.height()-_knobRect.centerY()),(_knobRect.height()-_knobRect.centerY()), 0);

        x = touchPoint.x;
        y = touchPoint.y;
        yShift = event.getY();

        float theta = mapThetaCoords(x,y);

        Log.i("Touch", "Xvalue is :" +touchPoint.x);
        Log.i("Touch", "Yvalue is :" +touchPoint.y);
        Log.i ("Touch", "Touch point 3 changed to: " + theta);

        setTheta(theta);




        distance = (float)Math.hypot(x, y);


        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(distance <= Radius) {

                x = nibCenter.x - x;
                y = nibCenter.y - y;

            } else if(distance <= _knobRect.width())

            nibCenter.x =  _knobRect.centerX() + Radius * (float)Math.toRadians(Math.cos(_theta));
            nibCenter.y =  _knobRect.centerY() + Radius * (float)Math.toRadians(Math.sin(_theta));

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

        } else if (event.getAction() == MotionEvent.ACTION_UP){
            reset();
        }




        _angleChangedListener.onAngleChanged(theta);


        return true; // super.onTouchEvent(event); Makes onTouchEvent repeat forever

    }
    public float mapThetaCoords(float x, float y) {

        double atan2 = Math.atan2(y , x);
        float theta;

        if (x >= 0 && y >= 0) {             // Q 1
            theta = (float) Math.toDegrees(atan2);
            return theta;

        } else if (x < 0 && y >= 0) {       // Q 2
            theta = (float)  Math.toDegrees(atan2);
            return theta;
        } else if (x < 0 && y < 0) {        // Q 3
            theta = (float) Math.toDegrees(atan2) + 360;
            return theta;

        } else if (x > 0 && y < 0) {        // Q 4
            theta = (float) Math.toDegrees(atan2) + 360;
            return theta;
        }
        return 0;
    }

    public void reset(){

    }
    public void newView(){

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        _knobRect.left = getPaddingLeft();
        _knobRect.top = getPaddingTop();
        _knobRect.right = getWidth()- getPaddingRight();
        _knobRect.bottom = _knobRect.width();

        offset = (getHeight() - _knobRect.height()) * 0.5f;
        _knobRect.top += offset;
        _knobRect.bottom += offset;


        Radius = _knobRect.width() * 0.35f;

        nibCenter.x = x + _knobRect.centerX();
        nibCenter.y = yShift;


        float nibRadius = Radius * 0.2f;


        nibRect.left = nibCenter.x - nibRadius;
        nibRect.top = nibCenter.y - nibRadius;
        nibRect.right = nibCenter.x + nibRadius;
        nibRect.bottom = nibCenter.y + nibRadius;


        Paint knobPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        knobPaint.setColor(Color.BLACK);
        Paint nibPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nibPaint.setColor(Color.MAGENTA);

        canvas.drawOval(_knobRect, knobPaint);
        canvas.drawOval(nibRect, nibPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);


        int width = 300;
        int height = 300;
        width = Math.max(width, getSuggestedMinimumWidth());
        height = Math.max(height, getSuggestedMinimumHeight());

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSpec;
            height = width;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSpec;
            width = height;
        }

        setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, 0),
                resolveSizeAndState(height, heightMeasureSpec, 0));
    }
}