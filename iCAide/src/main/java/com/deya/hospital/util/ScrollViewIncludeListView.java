package com.deya.hospital.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ScrollViewIncludeListView extends ListView {

  public ScrollViewIncludeListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ScrollViewIncludeListView(Context context) {
    super(context);
  }

  public ScrollViewIncludeListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // 根据模式计算每个child的高度和宽度
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);
  }
}
