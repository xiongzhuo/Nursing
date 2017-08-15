package com.deya.hospital.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 * 跑马灯效果
 * @author : yugq
 * @date 2016/8/22
 */
public class MarqueeTextView extends TextView {

    public MarqueeTextView(Context con) {
        super(con);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
