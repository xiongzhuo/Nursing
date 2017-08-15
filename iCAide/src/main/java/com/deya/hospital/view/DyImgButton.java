package com.deya.hospital.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.deya.acaide.R;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/18
 */
public class DyImgButton extends ImageButton {
    public DyImgButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        final ImageButton imageButton = new ImageButton(context);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.dyImageButton);
        final int src = a.getResourceId(R.styleable.dyImageButton_android_src, 0);
        final int press_src = a.getResourceId(R.styleable.dyImageButton_press_src, 0);
        imageButton.setImageResource(src);
        imageButton.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    imageButton.setImageResource(press_src); //按下的图片对应pressed
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    imageButton.setImageResource(src); //常态下的图片对应normal
                }
                return false;
            }
        });
        a.recycle();
    }
}
