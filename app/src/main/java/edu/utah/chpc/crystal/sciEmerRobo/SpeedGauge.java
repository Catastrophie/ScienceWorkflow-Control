package edu.utah.chpc.crystal.sciEmerRobo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;

public class SpeedGauge extends AppCompatImageView {

    public float x, y;
    public int  _speed;

    private PointF axis;
    private RectF value;



    OnSpeedChangedListener listenerAmountChanged = null;

    public interface OnSpeedChangedListener {
        void onSpeedChanged();
    }

    public SpeedGauge(Context context) {
        super(context);
        axis = new PointF(value.centerX(), value.centerY());
    }

    public float getSpeed() {
        return _speed;

    }

    public void setSpeed (int speed){
        _speed = speed;
        invalidate();
    }

    public void setOnSpeedChangedListener(OnSpeedChangedListener listener) {
        listenerAmountChanged = listener;
    }

    public boolean onTouchEvent(MotionEvent e) {
        float accelerate = getY();
        int time = 0;
        int initSpeed = 0;
        int speed = speedOnActionDown(accelerate, time, initSpeed);



        if (e.getAction() == MotionEvent.ACTION_DOWN){
            setSpeed(speed);
        }
        else if (e.getAction() == MotionEvent.ACTION_UP) {
            resetAction();
        }


       return true;
    }

    private int speedOnActionDown(float accel, int time, int initialSpeed){
        int speed = initialSpeed + 1;

        // If I create a button, time will impact speed levels until capped at full speed.
        // But if I create a level dial speed is capped at the level the current dial is placed at.

        while (speed > 0 && speed < 100){
            time = time + 1;

            while (time > 0) {
                speed = speed + (int) accel * time;

            }

        }if (speed >= 100){
            speed = 100;
        }

        // when speed hits 100% if e.getAction continues,
        //speed retains value, time continues count, but accelerate evens out.
        return speed;

    }
    private void resetAction() {
        _speed = 0;

    }


    protected void onDraw(Canvas canvas) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 200;
        int height = 800;

        setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, 0),
                resolveSizeAndState(height, heightMeasureSpec, 0));

    }
}
