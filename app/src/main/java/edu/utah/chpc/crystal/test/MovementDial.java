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


    public interface OnAngleChangedListener {
        void onAngleChanged(int theta);
    }

    int _theta = 0;
    RectF _knobRect = new RectF();
    RectF _innerLines = new RectF();
    OnAngleChangedListener _angleChangedListener = null;

    public MovementDial(Context context) {
        super(context);
    }

    public int getTheta() {
        return _theta;

    }

    public void setTheta (int theta){
        _theta = theta;
        invalidate();
    }


    public void setOnAngleChangedListener(OnAngleChangedListener listener) {
        _angleChangedListener = listener;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF touchPoint = new PointF();
        touchPoint.x = event.getX();
        touchPoint.y = event.getY();

        // TODO: touchPoint -> theta

        int theta = (int)Math.atan2(
                (double)touchPoint.y - _knobRect.centerY(),
                (double)touchPoint.x - _knobRect.centerX());
        setTheta(theta);



        _angleChangedListener.onAngleChanged(theta);

      //  Log.i ("Touch", "Touch point changed to: " + theta);
        return true; // super.onTouchEvent(event); Makes onTouchEvent repeat forever

    }

    /*
    Maps a value from one number range onto another @author : Aaron Pabst
    */
    private int map(double x, double in_min, double in_max, double out_min, double out_max)
    {
        int mapVal = (int) ((int)(x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
        return mapVal;
    }

    /*
    Mapping theta values
     */
    public int mapTheta(int _theta) {

        int theta;

         if (_theta > 0){
             theta = map(_theta,0,3.14,0,180);

            return theta;
        } else {
             theta = map(_theta,0,-3.14,0,-180);
             theta = ((180 + theta) +180);
             Log.i ("Touch", "Touch point changed to: " + theta);
             return theta;

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        _knobRect.left = getPaddingLeft();
        _knobRect.top = getPaddingTop();
        _knobRect.right = getWidth()- getPaddingRight();
        _knobRect.bottom = _knobRect.width();

        float offset = (getHeight() - _knobRect.height()) * 0.5f;
        _knobRect.top += offset;
        _knobRect.bottom += offset;

        float Radius = _knobRect.width() * 0.35f;


        PointF nibCenter = new PointF();
        nibCenter.x =  _knobRect.centerX() + Radius * (float)Math.cos((double)_theta);
        nibCenter.y =  _knobRect.centerY() + Radius * (float)Math.sin((double)_theta);

        float nibRadius = Radius * 0.2f;

        RectF nibRect = new RectF();
        nibRect.left = nibCenter.x - nibRadius;
        nibRect.top = nibCenter.y - nibRadius;
        nibRect.right = nibCenter.x + nibRadius;
        nibRect.bottom = nibCenter.y + nibRadius;


        Paint knobPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        knobPaint.setColor(Color.WHITE);
        Paint knobInner = new Paint(Paint.ANTI_ALIAS_FLAG);
        knobPaint.setColor(Color.BLACK);
        Paint nibPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nibPaint.setColor(Color.YELLOW);
        canvas.drawOval(_knobRect, knobPaint);
        canvas.drawOval(nibRect, nibPaint);
        canvas.drawOval (_innerLines, knobInner);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);


        int width = 265;
        int height = 265;
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