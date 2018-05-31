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

    public float x, y, r, Radius, _theta, xShift, yShift, offset;
    //private int origin = 90;
    private double xVal, yVal, distance, distanceAdj;

    private RectF _knobRect = new RectF();
    private PointF nibCenter, touchPoint;
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
    public boolean onTouchEvent(MotionEvent e) {

        yShift = e.getY();
        xShift = e.getX();

        touchPoint = new PointF();

        touchPoint.x = e.getX() -  _knobRect.centerX();
        touchPoint.y = map(yShift,0,(_knobRect.height()-_knobRect.centerY()),(_knobRect.height()-_knobRect.centerY()), 0);

        x = touchPoint.x;
        y = touchPoint.y;

        float theta = mapThetaCoords(x,y);  //maps out theta values between 0 to 360 using a traditional unit circle layout.
/*
        Log.i("Touch", "Xvalue is :" +touchPoint.x);
        Log.i("Touch", "Yvalue is :" +touchPoint.y);
        Log.i ("Touch", "Touch point 3 changed to: " + theta);*/

        setTheta(theta);

/*      x, y = radialToCartesianCoords(r, theta);*/

        distance = (float) Math.hypot(x, y); //polar coordinate radius (r = sqrt x^2 + y^2)


        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            Log.i("Touch", "Xvalue touch is :" +touchPoint.x);
            Log.i("Touch", "Yvalue touch is :" +touchPoint.y);
            Log.i ("Touch", "Touch point 6 changed to: " + theta);

        if(distance <= Radius) {
            x = touchPoint.x;
            y = touchPoint.y;

            double Radian = Math.toRadians(_theta);
            final double PIValue = Math.PI/180;

            float xNibValue = (float)(distance * Math.cos(PIValue*Radian));
            float yNibValue = (float)(distance * Math.sin(PIValue*Radian));

            nibCenter.x = xNibValue; // _knobRect.centerX() + Radius *
            nibCenter.y = map(yNibValue,0,(_knobRect.height()-_knobRect.centerY()),(_knobRect.height()-_knobRect.centerY()), 0);
            // _knobRect.centerY() + Radius *


            Log.i("Touch", "Xvalue touch2 is :" +touchPoint.x);
            Log.i("Touch", "Yvalue touch2 is :" +touchPoint.y);
            Log.i("Touch", "Xvalue nib2 touch is :" +nibCenter.x);
            Log.i("Touch", "Yvalue nib2 touch is :" +nibCenter.y);

        } else if(distance <= _knobRect.width()) {
            x = touchPoint.x;
            y = touchPoint.y;

            Log.i("Touch", "Xvalue touch3 is :" +touchPoint.x);
            Log.i("Touch", "Yvalue touch3 is :" +touchPoint.y);

            nibCenter.x = (float)(distance * Math.cos(Math.toRadians(_theta))); // _knobRect.centerX() + Radius *
            nibCenter.y = (float)(distance * Math.sin(Math.toRadians(_theta))); // _knobRect.centerY() + Radius *

            Log.i("Touch", "Xvalue nib1 touch is :" +nibCenter.x);
            Log.i("Touch", "Yvalue nib1 touch is :" +nibCenter.y);
           /* x = nibCenter.x - x;
            y = nibCenter.y - yShift;*/



            }
            return true;


        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {

            Log.i("Touch", "Xvalue2 move is :" + touchPoint.x);
            Log.i("Touch", "Yvalue2 move is :" + touchPoint.y);
            Log.i ("Touch", "Move value point 7 changed to: " + theta);

            distanceAdj = distance;
            distance = Math.min(distance, distanceAdj); //comparing changing polar radius values as coordinates change place.

            if(distance <= Radius) {    //check to see if polar radius coordinate is less or equal to the radius value
                x = touchPoint.x;
                y = touchPoint.y;
                nibCenter.y = y;
                Log.i("Touch", "Xvalue move1 is :" + x);


            } else if (distance > Radius){
                x = touchPoint.x;
                y = touchPoint.y;
                Log.i("Touch", "Yvalue (greater than) move is :" + y);
                nibCenter.y = y; // + _knobRect.centerY();

                Log.i("Touch", "Xvalue move2 is :" + x);
                Log.i("Touch", "Yvalue move2 is :" + y);

            }
            return true;

        } else if (e.getAction() == MotionEvent.ACTION_UP){
            reset();
        }

        return false; // super.onTouchEvent(event); Makes onTouchEvent repeat forever

    }
    public float mapThetaCoords(float x, float y) {

        double atan2 = Math.atan2(y , x);
        float theta;

        if (x >= 0 && y >= 0) {             // Q 1
            theta = (float) Math.toDegrees(atan2);
            return theta;

        } else if (x < 0 && y > 0) {       // Q 2
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
       x = _knobRect.centerX()-(_knobRect.width()/2);
       y = _knobRect.centerY()-(_knobRect.height()/2);
       nibCenter.x = x;
       nibCenter.y = y + _knobRect.centerY();
       _theta = 0f;
       distance = 0f;

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
        nibCenter.y = y + _knobRect.centerY();


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


        int width = 800;
        int height = 800;
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