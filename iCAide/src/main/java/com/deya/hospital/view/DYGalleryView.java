package com.deya.hospital.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : yugq
 * @date 2016/7/18
 */
public class DYGalleryView  extends Gallery {
    public DYGalleryView(Context context) {
        super(context);
    }

    public DYGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DYGalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub
//  return super.onFling(e1, e2, velocityX/1.5f, velocityY);
        return false;
    }
}
