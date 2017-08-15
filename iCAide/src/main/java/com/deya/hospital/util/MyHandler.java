
/**
 * 项目名称:Vicinity3.1 . <br/>
 * 文件名称:MyHandler.java . <br/>
 * 包名:com.trisun.vicinity.util . <br/>
 * 日期:2015年4月13日下午6:50:11 . <br/>
 * @author LIUXIN . <br/>
 *     Copyright (c) 2015,广东云上城科技有限公司 . <br/>
 *
 */

package com.deya.hospital.util;


import android.app.Activity;
import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * ClassName:. MyHandler【防止系统内存MyHandler内存泄露】 <br/>
 */
public class MyHandler extends Handler{
  
  public WeakReference<Activity> mactivity;
  
  public MyHandler(Activity leakActivity) {
    mactivity = new WeakReference<Activity>(leakActivity);

  }
}

