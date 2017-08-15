package com.example.calendar.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.example.calendar.CollapseCalendarView;
import com.example.calendar.widget.ViewPagerHeightWrapContent;

/**
 * Created by Blaz Solar on 17/04/14.
 */
public class TouchEventManager {

    private static final String TAG = "TouchEventManager";

    private ViewPagerHeightWrapContent mView;
//    @Nullable private ProgressManager mProgressManager;

    public TouchEventManager(@NonNull ViewPagerHeightWrapContent view) {
        mView = view;
    }

    public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
        CollapseCalendarView view = (CollapseCalendarView) mView.getCurrentChild();
        if(null != view ){
            return view.onMyInterceptTouchEvent(ev);
        }
        return false;
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        CollapseCalendarView view = (CollapseCalendarView) mView.getCurrentChild();
        if(null != view ){
            return view.onMyTouchEvent(event);
        }
        return true;
    }

}
