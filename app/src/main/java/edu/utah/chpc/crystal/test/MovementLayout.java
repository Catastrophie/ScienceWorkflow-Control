package edu.utah.chpc.crystal.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * I don't really need this. Using it for an example.
 * Created by crystal on 10/25/2017.
 */

public class MovementLayout extends ViewGroup {
    public MovementLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int i, int i1, int i2, int i3) {
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View childView = getChildAt(childIndex);
            childView.layout(20, 20, 400, 400);
        }
    }

}
