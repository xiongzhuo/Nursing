package com.example.calendar.manager;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.deya.hospital.util.DebugUtil;
import com.example.calendar.CollapseCalendarView;
import com.example.calendar.models.AbstractViewHolder;
import com.example.calendar.models.SizeViewHolder;
import com.example.calendar.models.StubViewHolder;

/**
 * Created by Blaz Solar on 17/04/14.
 */
public class ProgressManagerImpl extends ProgressManager {
    
    public ProgressManagerImpl(@NonNull CollapseCalendarView calendarView,
            int activeWeek, boolean fromMonth) {
        super(calendarView, activeWeek, fromMonth);
        mPreDrawed = false;
        if (!fromMonth) {
            initMonthView();
        } else {
            initWeekView();
        }
        
    }
    
    @Override
    public void finish(final boolean expanded) {
        mCalendarView.postDelayed(new Runnable() { // to prevent flickering
            
            @Override
            public void run() {
				DebugUtil.debug("pull_f", "expanded>>"+expanded);
                mCalendarView
                        .getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mWeeksView
                        .getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        
                for (AbstractViewHolder view : mViews) {
                    view.onFinish(true);
                }
                CalendarManager manager = mCalendarView.getManager();
                if (!expanded) {
                    if (mFromMonth) {
                        manager.toggleView(mCalendarView);
                    } else {
                        manager.toggleToWeek(mCalendarView, mActiveIndex);
                    }
                    mCalendarView.populateLayout();
                }
                manager.callStateChanged();
            }
        }, 1);
    }
    
    private void initMonthView() {
        mCalendarHolder = new SizeViewHolder(mCalendarView.getHeight(), 0);
        mCalendarHolder.setView(mCalendarView);
        mCalendarHolder.setDelay(0);
        mCalendarHolder.setDuration(1);
        
        mWeeksHolder = new SizeViewHolder(mWeeksView.getHeight(), 0);
        mWeeksHolder.setView(mWeeksView);
        mWeeksHolder.setDelay(0);
        mWeeksHolder.setDuration(1);
        
        mCalendarView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    
                    @Override
                    public boolean onPreDraw() {
                        mCalendarView.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                                
                        mCalendarHolder.setMaxHeight(mCalendarView.getHeight());
                        mWeeksHolder.setMaxHeight(mWeeksView.getHeight());
                        
                        mCalendarView.getLayoutParams().height = mCalendarHolder
                                .getMinHeight();
                        mWeeksView.getLayoutParams().height = mCalendarHolder
                                .getMinHeight();
                                
                        initializeChildren();
                        
                        setInitialized(true);
                        Log.d("Calendar Hubi",
                                "ProgressManagerImpl onPreDraw mCalendarHolder maxH = "
                                        + mCalendarHolder.getMaxHeight()
                                        + " minH = "
                                        + mCalendarHolder.getMinHeight()
                                        + " parentH = "
                                        + ((View)mCalendarView.getParent()).getHeight());
                        mPreDrawed = true;
                        return false;
                    }
                });
    }
    
    private void initWeekView() {
        mCalendarHolder = new SizeViewHolder(0, mCalendarView.getHeight());
        mCalendarHolder.setView(mCalendarView);
        mCalendarHolder.setDelay(0);
        mCalendarHolder.setDuration(1);
        
        mWeeksHolder = new SizeViewHolder(0, mWeeksView.getHeight());
        mWeeksHolder.setView(mWeeksView);
        mWeeksHolder.setDelay(0);
        mWeeksHolder.setDuration(1);
        
        initializeChildren();
        
        mCalendarView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    
                    @Override
                    public boolean onPreDraw() {
                        mCalendarView.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                                
                        mCalendarHolder.setMinHeight(mCalendarView.getHeight());
                        mWeeksHolder.setMinHeight(mWeeksView.getHeight());
                        
                        mCalendarView.getLayoutParams().height = mCalendarHolder
                                .getMaxHeight();
                        mWeeksView.getLayoutParams().height = mCalendarHolder
                                .getMaxHeight();
                                
                        setInitialized(true);
                        Log.d("Calendar Hubi",
                                "ProgressManagerImpl onPreDraw mCalendarHolder maxH = "
                                        + mCalendarHolder.getMaxHeight()
                                        + " minH = "
                                        + mCalendarHolder.getMinHeight()
                                        + " parentH = "
                                        + ((View)mCalendarView.getParent()).getHeight());
                        mPreDrawed = true;
                        return false;
                    }
                });
    }
    
    private void initializeChildren() {
        
        int childCount = mWeeksView.getChildCount();
        
        // FIXME do not assume that all views are the same height
        mViews = new AbstractViewHolder[childCount];
        for (int i = 0; i < childCount; i++) {
            
            View view = mWeeksView.getChildAt(i);
            
            int activeIndex = getActiveIndex();
            
            AbstractViewHolder holder;
            if (i == activeIndex) {
                holder = new StubViewHolder();
            } else {
                SizeViewHolder tmpHolder = new SizeViewHolder(0,
                        view.getHeight());
                        
                final int duration = mWeeksHolder.getMaxHeight()
                        - view.getHeight();
                        
                if (i < activeIndex) {
                    tmpHolder.setDelay(view.getTop() * 1.0f / duration);
                } else {
                    tmpHolder.setDelay((view.getTop() - view.getHeight()) * 1.0f
                            / duration);
                }
                tmpHolder.setDuration(view.getHeight() * 1.0f / duration);
                
                holder = tmpHolder;
                
                view.setVisibility(View.GONE);
            }
            
            holder.setView(view);
            
            mViews[i] = holder;
        }
        
    }
    
}
