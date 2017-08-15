package com.deya.hospital.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ScrollViewIncludeGridView extends GridView {



  public ScrollViewIncludeGridView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ScrollViewIncludeGridView(Context context) {
    super(context);
  }

  public ScrollViewIncludeGridView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // 根据模式计算每个child的高度和宽度
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);

    /*
     * 解释一下MeasureSpec这个类: 一个MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求。
     * 一个MeasureSpec由大小和模式组成。它有三种模式：UNSPECIFIED(未指定),父元素部队自元素施加任何束缚，
     * 子元素可以得到任意想要的大小；EXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小； AT_MOST(至多)，子元素至多达到指定大小的值。
     * 它常用的三个函数： 1.static int getMode(int measureSpec):根据提供的测量值(格式)提取模式(上述三个模式之一) 2.static int
     * getSize(int measureSpec):根据提供的测量值(格式)提取大小值(这个大小也就是我们通常所说的大小) 3.static int makeMeasureSpec(int
     * size,int mode):根据提供的大小值和模式创建一个测量值(格式) makeMeasureSpec(int size, int mode)
     * 这个方法的主要作用就是根据你提供的大小和模式，返回你想要的大小值.
     */

  }
}
