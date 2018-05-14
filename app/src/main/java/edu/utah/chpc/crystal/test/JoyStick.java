package edu.utah.chpc.crystal.test;

import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import android.view.MotionEvent;
import android.view.View;

import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.content.Context;

public class JoyStick extends AppCompatImageView{

    private int nobWidth, radius;

    private float angle, distance, deltaX, deltaY, deltaAngle, x, y;

    private PointF center; //center of the joystick

    private boolean touch = false; //whether your finger is on the screen

    private RectF bounds;//rectangular bounds for the joystick to be drawn in


    private RectF _knobRect = new RectF();
    private RectF _innerLines = new RectF();
    private PointF nibCenter = new PointF();
    private RectF nibRect = new RectF();

    //constructor, it has no height parameter because the joystick should be a square
    public JoyStick(float x, float y, int width, Context context) {
        super(context);

        radius = width / 2;
        bounds = new RectF(x - radius, y - radius, x + width - radius, y + width - radius);

        this.x = bounds.left + (width / 5);
        this.y = bounds.top + (width / 5);
        this.nobWidth = (int) (width * .6);

        center = new PointF(bounds.centerX(), bounds.centerY());
    }

    //handles touch events, call this in the current states onTouch method
    public  void onTouch(MotionEvent e, float scaledX, float scaledY) {
        //x1 - x2 is a value used in calculating angle and distance
        deltaX = (center.x - scaledX);
        deltaY = (center.y - scaledY);

        //angle between center point and touch point
        angle = (float)calculateAngle(deltaX, deltaY);
        //distance between center point and touch point
        distance = (float)Math.hypot(deltaX, deltaY);

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (distance <= radius) {
                x = scaledX - (nobWidth / 2);
                y = scaledY - (nobWidth / 2);
                touch = true;

                //this is so you dont need to touch exactly on the joystick to
                //activate it. this can be adjusted depending on you preferences
            } else if (distance <= bounds.width()) {
                x = scaledX - (nobWidth / 2);
                y = scaledY - (nobWidth / 2);
                //deltaAngle is here just to reduce the number of calculations that have
                //to be made each time onTouch is called. (calculated once and used twice)
                deltaAngle = (float)Math.toRadians(180 + calculateAngle(deltaX, deltaY));
                touch = true;
                x = (int) (Math.cos(deltaAngle) * radius + bounds.left - (nobWidth / 2) + radius);
                y = (int) (Math.sin(deltaAngle) * radius + bounds.top - (nobWidth / 2) + radius);
            }
        } else if (e.getAction() == MotionEvent.ACTION_MOVE && touch) {
            if (distance <= radius) {
                x = scaledX - (nobWidth / 2);
                y = scaledY - (nobWidth / 2);
            } else if (distance >= radius) {

                x = (int) (Math.cos(Math.toRadians(180 + calculateAngle(deltaX, deltaY))) * radius + bounds.left - (nobWidth / 2) + radius);
                y = (int) (Math.sin(Math.toRadians(180 + calculateAngle(deltaX, deltaY))) * radius + bounds.top - (nobWidth / 2) + radius);
            }
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            resetNob();
        }

    }



    //when you let go it resets the nob position and all of the values
    public void resetNob() {
        touch = false;
        angle = 0f;
        distance = 0f;
        deltaX = 0f;
        deltaY = 0f;
        x = bounds.left + (bounds.width() / 5);
        y = bounds.top + (bounds.width() / 5);
    }

    //for making the joystick return 4 different directions based
    //on the position. ie up, down, left, right
    public byte getFourDirection() {
        if (touch) {
            if (angle >= 45 && angle < 135) {
                return 1;
            } else if (angle >= 135 && angle < 225) {
                return 2;
            } else if (angle >= 225 && angle < 315) {
                return 3;
            } else if ((angle >= 315 && angle < 360) || (angle >= 0 && angle < 45)) {
                return 4;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    //for making the return the 4 normal direction(up, down, left, right) in
    //addition to the 4 angles (uppper left, upper right, lower left, lower right)
    public byte getEightDirection() {
        if (touch) {
            if (angle >= 67.5 && angle < 112.5) {
                return 1;
            } else if (angle >= 112.5 && angle < 157.5) {
                return 2;
            } else if (angle >= 157.5 && angle < 202.5) {
                return 3;
            } else if (angle >= 202.5 && angle < 247.5) {
                return 4;
            } else if (angle >= 247.5 && angle < 292.5) {
                return 5;
            } else if (angle >= 292.5 && angle < 337.5) {
                return 6;
            } else if ((angle >= 337.5 && angle < 360) || (angle >= 0 && angle < 22.5)) {
                return 7;
            } else if (angle >= 22.5 && angle < 67.5) {
                return 8;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    //used to calculate the angle between two points
    public double calculateAngle(float x, float y) {
        if(x >= 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x));
        else if(x < 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x < 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x > 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 360;
        return 0;
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



/*        nibCenter.x =  _knobRect.centerX() + Radius * (float)Math.cos(_theta);
        nibCenter.y =  _knobRect.centerY() + Radius * (float)Math.sin(_theta);*/


        float nibRadius = Radius * 0.2f;


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

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSpec = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSpec = View.MeasureSpec.getSize(heightMeasureSpec);


        int width = 265;
        int height = 265;
        width = Math.max(width, getSuggestedMinimumWidth());
        height = Math.max(height, getSuggestedMinimumHeight());

        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSpec;
            height = width;
        }
        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSpec;
            width = height;
        }

        setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, 0),
                resolveSizeAndState(height, heightMeasureSpec, 0));
    }

}
