package com.deya.hospital.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * ClassName:. MyAdvImageAdapter【图片相关处理类】 <br/>
 */
public class MyAdvImageAdapter extends PagerAdapter {

  private List<View> mimageList;

  public MyAdvImageAdapter(List<View> mimageList) {
    this.mimageList = mimageList;
  }

  public void updateAdvList(List<View> mimageList) {
    this.mimageList = mimageList;
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    if (mimageList.size() == 1) {
      return mimageList.size();
    }
    return Integer.MAX_VALUE;
  }

  /**
   * TODO 复用对象 true 复用view false 复用的是Object（可选）.
   * 
   * @see android.support.v4.view.PagerAdapter#isViewFromObject(View, Object)
   */
  @Override
  public boolean isViewFromObject(View arg0, Object arg1) {
    return arg0 == arg1;
  }

  /**
   * TODO 更新list（可选）.
   *
   * @see android.support.v4.view.PagerAdapter#getItemPosition(Object)
   */
  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

  /**
   * TODO 销毁对象（可选）.
   *
   * @see android.support.v4.view.PagerAdapter#destroyItem(ViewGroup, int,
   *      Object)
   */
  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    /*
     * if (mImageList.size() > 0) { container.removeView(mImageList.get(position %
     * mImageList.size())); }
     */
  }

  /**
   * TODO 初始化一个对象（可选）.
   *
   * @see android.support.v4.view.PagerAdapter#instantiateItem(ViewGroup, int)
   */
  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    // 对ViewPager页号求模取出View列表中要显示的项
    if (mimageList.size() > 0) {
      position %= mimageList.size();
      if (position < 0) {
        position = mimageList.size() + position;
      }
      View view = mimageList.get(position);
      // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
      ViewParent vp = view.getParent();
      if (vp != null) {
        ViewGroup parent = (ViewGroup) vp;
        parent.removeView(view);
      }
      container.addView(view);
      // add listeners here if necessary
      return view;
    }
    return null;
  }
}
