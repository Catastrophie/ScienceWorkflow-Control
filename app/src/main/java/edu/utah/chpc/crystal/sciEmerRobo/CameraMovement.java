package edu.utah.chpc.crystal.sciEmerRobo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;

public class CameraMovement extends AppCompatImageView{
    public float x, y, _theta;

    private RectF cameraValue;
    private PointF cameraAxis;

    OnAngleChangedListener listenerAngleChanged = null;

    public interface OnAngleChangedListener {
        void onAngleChanged(float theta);
    }

    public CameraMovement(Context context) {
        super(context);
        cameraAxis = new PointF(cameraValue.centerX(), cameraValue.centerY());
    }

    public float get_theta() {
        return _theta;
    }

    public void set_theta(float theta) {
        _theta = theta;
    }

    public void setListenerAngleChanged(OnAngleChangedListener listener) {
        listenerAngleChanged = listener;
    }

    public boolean onTouchEvent(MotionEvent e) {



        return false;
    }

    protected void onDraw(Canvas canvas) {

    }

    protected void onMeasure(){

    }

}
