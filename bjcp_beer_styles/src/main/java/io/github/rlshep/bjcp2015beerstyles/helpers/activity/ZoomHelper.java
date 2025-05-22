package io.github.rlshep.bjcp2015beerstyles.helpers.activity;

import android.graphics.Typeface;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

public class ZoomHelper {

    private final static float move = 200;
    private float ratio = 1.0f;
    private int bastDst;
    private float baseRatio;

    public boolean calculateZoom(@NonNull MotionEvent event, @NonNull TextView textview, boolean searchSections) {

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
                ratio = Math.min(20.0f, Math.max(0.1f, baseRatio * factor));

                Set<TextView> sectionViews = new HashSet<>();

                if (searchSections) {
                    sectionViews = findSectionViews(textview);
                } else {
                    sectionViews.add(textview);
                }

                for (TextView section : sectionViews) {
                    section.setTextSize(ratio + 15);
                }

            }

            eventConsumed = true;
        }

        return eventConsumed;
    }

    private Set<TextView> findSectionViews(@NonNull TextView textview) {
        Set<TextView> sectionViews = new HashSet<>();

        LinearLayout lp = (LinearLayout) textview.getParent();
        ListView lv = (ListView) lp.getParent();

        for (int j = 0; j < lv.getChildCount(); j++) {
            LinearLayout lp1 = (LinearLayout) lv.getChildAt(j);

            for (int k = 0; k < lp1.getChildCount(); k++) {
                TextView tv = (TextView) lp1.getChildAt(k);

                if (!tv.getText().toString().isEmpty() && !tv.getText().toString().contains(" - ") && tv.getTypeface().getStyle() != Typeface.BOLD) {
                    sectionViews.add(tv);
                }
            }
        }

        return sectionViews;
    }


    // get distance between the touch event
    private int getDistance(@NonNull MotionEvent event) {
        int distanceX = (int) (event.getX(0) - event.getX(1));
        int distanceY = (int) (event.getY(0) - event.getY(1));
        return (int) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }
}
