package com.deya.hospital.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class ViewpagerIncludScorllview extends ViewPager{

        public ViewpagerIncludScorllview(Context context) {
            super(context);
        }

        public ViewpagerIncludScorllview(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override

        protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {

            if ((v instanceof HorizontalScrollView)||v instanceof HorizontalListView) {
                return true;
            }
            return super.canScroll(v, checkV, dx, x, y);
        }
}
