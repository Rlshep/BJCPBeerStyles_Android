package io.github.rlshep.bjcp2015beerstyles.helpers.activity;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ZoomHelper {

    private final static float move = 200;
    private float ratio = 1.0f;
    private int bastDst;
    private float baseRatio;

    public boolean calculateZoom(@NonNull MotionEvent event, @NonNull TextView textview) {

        boolean eventConsumed = false;

        if (event.getPointerCount() == 2) {

            int action = event.getAction();
            int mainAction = action & MotionEvent.ACTION_MASK;
            if (mainAction == MotionEvent.ACTION_POINTER_DOWN) {
                bastDst = getDistance(event);
                baseRatio = ratio;
            } else {
                float scale = (getDistance(event) - bastDst) / move;
                float factor = (float) Math.pow(2, scale);
                ratio = Math.min(30.0f, Math.max(0.1f, baseRatio * factor));

                textview.setTextSize(ratio + 15);
            }

            eventConsumed = true;
        }

        return eventConsumed;
    }

    // get distance between the touch event
    private int getDistance(@NonNull MotionEvent event) {
        int distanceX = (int) (event.getX(0) - event.getX(1));
        int distanceY = (int) (event.getY(0) - event.getY(1));
        return (int) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }
}
