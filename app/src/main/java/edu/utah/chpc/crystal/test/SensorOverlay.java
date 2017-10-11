package edu.utah.chpc.crystal.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;


/**
 * Created by crystal on 10/11/2017.
 */

public class SensorOverlay extends ViewGroup {
    public SensorOverlay(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View childView = getChildAt(childIndex);
            childView.layout();// TODO: Location int
        }
    }
}
