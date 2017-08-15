package com.deya.hospital.view;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;


public class CustomButton extends AppCompatButton {
    private boolean touchEffect = true;
    public final float[] BG_PRESSED =
        new float[] {1, 0, 0, 0, -50, 0, 1, 0, 0, -50, 0, 0, 1, 0, -50, 0, 0, 0, 1, 0};
    public final float[] BG_NOT_PRESSED =
        new float[] {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setPressed(boolean pressed) {
        updateView(pressed);
        super.setPressed(pressed);
    }

    /**
     * 根据是否按下去来刷新bg和src created by minghao.zl at 2014-09-18
     * 
     * @param pressed
     */
    private void updateView(boolean pressed) {
        // 如果没有点击效果
        if (!touchEffect) {
            return;
        } // end if
        if (pressed) {// 点击
            /**
             * 通过设置滤镜来改变图片亮度@minghao
             */
            this.setDrawingCacheEnabled(true);
            this.getBackground().setColorFilter(new ColorMatrixColorFilter(BG_PRESSED));
        } else {// 未点击
            this.getBackground().setColorFilter(new ColorMatrixColorFilter(BG_NOT_PRESSED));
        }
    }
}
