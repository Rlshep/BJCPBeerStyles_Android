package io.github.rlshep.bjcp2015beerstyles.listeners;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String GESTURE_TAG = "GestureListener";
    public static final String SWIPE_LEFT = "left";
    public static final String SWIPE_RIGHT = "right";
    private static final int SWIPE_THRESHOLD = 200;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private static final int SWIPE_MIN_DISTANCE = 120;

    public static String currentGesture = "";

    @Override
    public boolean onSingleTapUp(MotionEvent ev) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent ev) {
    }

    @Override
    public void onLongPress(MotionEvent ev) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > SWIPE_MIN_DISTANCE) {
                        currentGesture = SWIPE_LEFT;
                    } else {
                        currentGesture = SWIPE_RIGHT;
                    }
                    result = true;
                }
            }
        } catch (Exception e) {
            Log.e(GESTURE_TAG, e.getMessage());
        }

        return result;
    }
}
