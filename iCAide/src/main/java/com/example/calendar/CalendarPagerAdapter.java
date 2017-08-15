/**
 * Copyright (c) hb-cloud 2015
 * 
 * @author hubin
 * @date 2015-9-2
 */
package com.example.calendar;

import com.deya.acaide.R;
import com.example.calendar.manager.CalendarManager;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author hubin
 *         
 */
public class CalendarPagerAdapter extends PagerAdapter {
    
    private LayoutInflater mInflater;
    private CalendarManager mMgr;
    private CollapseCalendarView mTodayView;
    private CollapseCalendarView mYesterdayView;
    private CollapseCalendarView mTomorrowView;
    private int viewId;
    
    public CalendarPagerAdapter(LayoutInflater inflater,
            CalendarManager manager/* , List<View> list */) {
        mInflater = inflater;
        mMgr = manager;
        Log.i("newview","创建视图了 init");
    }
    
    public void setManager(CalendarManager manager){
    	 mMgr = manager;
    }
    
    /*
     * (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
    	// Log.i("newview","创建视图了 getcount");
        return Integer.MAX_VALUE;
    }
    
    /*
     * (non-Javadoc)
     * @see
     * android.support.v4.view.PagerAdapter#isViewFromObject(android.view
     * .View, java.lang.Object)
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    
    CollapseCalendarView view = null;
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	view=(CollapseCalendarView) container.getChildAt(position);
        if (null == view) {
            view = (CollapseCalendarView) mInflater
                    .inflate(R.layout.view_calendar, null);
            view.init(mMgr, position);
            Log.i("newview","创建视图了"+position);
            viewId=position;
       
	        view.setId(position);
	        container.addView(view);
        }
        
        return view;
    }

    public int getViewPosition(){
    	return viewId;
    }
    
}
