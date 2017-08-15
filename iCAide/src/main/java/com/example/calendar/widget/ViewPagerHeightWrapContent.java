package com.example.calendar.widget;

import com.umeng.socialize.utils.Log;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class ViewPagerHeightWrapContent extends ViewPager {
    
    /**
     * Constructor
     *
     * @param context the context
     */
    public ViewPagerHeightWrapContent(Context context) {
        super(context);
    }
    
    /**
     * Constructor
     *
     * @param context the context
     * @param attrs the attribute set
     */
    public ViewPagerHeightWrapContent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        
        int height = 0;
        if (this.getChildCount() > 0) {
            View child = getCurrentChild();
            if (null == child) {
                child = this.getChildAt(0);
            }
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }
        
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);
                
        Log.d("Calendar Hubi",
                "ViewPagerHeightWrapContent onMeasure height = " + height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    public View getCurrentChild() {
    	Log.i("111111111111---thisItemt", this.getCurrentItem()+"");
        return findViewById(this.getCurrentItem());
    }
    public View getNextChild() {
    	 return findViewById(this.getCurrentItem()+1);
    	 
    }
    
    public int getCurrentId(){
    	  return this.getCurrentItem();
    }
    public View getLeftChild() {
   	 return findViewById(this.getCurrentItem()-1);
   }
}
