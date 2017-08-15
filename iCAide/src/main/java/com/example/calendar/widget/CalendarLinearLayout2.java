package com.example.calendar.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.example.calendar.manager.TouchEventManager;

public class CalendarLinearLayout2 extends LinearLayout {

    private TouchEventManager mManager;
    private boolean isDown = false;

    public CalendarLinearLayout2(Context context) {
        super(context);
    }

    public CalendarLinearLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarLinearLayout2(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }

    MotionEvent mDownEvent;
    int downY;

    ViewPagerHeightWrapContent vp;

    public void initChildViews(int viewpagerId,int childId) {
        vp = (ViewPagerHeightWrapContent) findViewById(
                viewpagerId);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (null == mManager) {
            mManager = new TouchEventManager(vp);
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isDown = false;
                downY = (int) ev.getY();
                isDown = true;
                return mManager.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_MOVE:
                if (isDown) {
                    return mManager.onInterceptTouchEvent(ev);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isDown) {
                    isDown = false;
                    return mManager.onInterceptTouchEvent(ev);
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (null == mManager) {
            mManager = new TouchEventManager(vp);
        }
        if (isDown && mManager.onTouchEvent(event)) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

}
