package com.example.calendar.widget;

import com.example.calendar.manager.TouchEventManager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CalendarLinearLayoutWithListView extends LinearLayout {
    
    private TouchEventManager mManager;
    private boolean isDown = false;
    
    public CalendarLinearLayoutWithListView(Context context) {
        super(context);
    }
    
    public CalendarLinearLayoutWithListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public CalendarLinearLayoutWithListView(Context context, AttributeSet attrs,
                                            int defStyle) {
        super(context, attrs, defStyle);
    }
    
    MotionEvent mDownEvent;
    int downY;
    
    ViewPagerHeightWrapContent vp;
    ListView lv;
    
    public void initChildViews(int viewpagerId, int listviewId) {
        vp = (ViewPagerHeightWrapContent) findViewById(
                viewpagerId);
        lv = (ListView) findViewById(listviewId);;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (null == mManager) {
            mManager = new TouchEventManager(vp);
        }
        int lvy = (int) lv.getY();
        
        final int action = MotionEventCompat.getActionMasked(ev);
        
        switch (action) {
        case MotionEvent.ACTION_DOWN: 
        {
            isDown = false;
            downY = (int) ev.getY();
            if (downY > lvy) {
                int lvScrollY = 0;
                View c = lv.getChildAt(0);
                if (null != c) {
                    int top = c.getTop();
                    int firstVisiblePosition = lv.getFirstVisiblePosition();
                    lvScrollY = Math.abs(top) + firstVisiblePosition;
                }
                if (lvScrollY == 0) {
                    isDown = true;
                    return mManager.onInterceptTouchEvent(ev);
                }
            } else /*if (downY > vpy || downY < vp.getY())*/ {
                isDown = true;
                return mManager.onInterceptTouchEvent(ev);
            }
        }
            break;
        case MotionEvent.ACTION_MOVE:
            if (isDown) { return mManager.onInterceptTouchEvent(ev); }
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
