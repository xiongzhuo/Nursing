package com.deya.hospital.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * ClassName:. Tools【获取本地shareprefrerence数据的工具类】 <br/>
 */
public class Tools {
  public SharedPreferences sp;
  public SharedPreferences.Editor editor;

  Context context;

  /**
   * Creates a new instance of Tools.
   */
  public Tools(Context context, String name) {
    this.context = context;
    Log.i("111111", context+"-----"+name);
    sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    editor = sp.edit();
  }

  /**
   * putValue:(设置值). <br/>
   * TODO(这里描述这个方法适用条件 – 可选).<br/>
   * TODO(这里描述这个方法的执行流程 – 可选).<br/>
   * TODO(这里描述这个方法的使用方法 – 可选).<br/>
   * TODO(这里描述这个方法的注意事项 – 可选).<br/>
   */
  public void putValue(String key, String value) {
    editor = sp.edit();
    editor.putString(key, value);
    editor.commit();
  }

  /**
   * putValue:(设置值). <br/>
   * TODO(这里描述这个方法适用条件 – 可选).<br/>
   * TODO(这里描述这个方法的执行流程 – 可选).<br/>
   * TODO(这里描述这个方法的使用方法 – 可选).<br/>
   * TODO(这里描述这个方法的注意事项 – 可选).<br/>
   */
  public void putValue(String key, int value) {
    editor = sp.edit();
    editor.putInt(key, value);
    editor.commit();
  }

  /**
   * putValue:(设置值). <br/>
   * TODO(这里描述这个方法适用条件 – 可选).<br/>
   * TODO(这里描述这个方法的执行流程 – 可选).<br/>
   * TODO(这里描述这个方法的使用方法 – 可选).<br/>
   * TODO(这里描述这个方法的注意事项 – 可选).<br/>
   */
  public void putValue(String key, boolean value) {
    editor = sp.edit();
    editor.putBoolean(key, value);
    editor.commit();
  }

  /**
   * getValue:(获取值). <br/>
   * TODO(这里描述这个方法适用条件 – 可选).<br/>
   * TODO(这里描述这个方法的执行流程 – 可选).<br/>
   * TODO(这里描述这个方法的使用方法 – 可选).<br/>
   * TODO(这里描述这个方法的注意事项 – 可选).<br/>
   */
  public String getValue(String key) {
    return sp.getString(key, null);
  }

  /**
   * getValue:(获取值). <br/>
   * TODO(这里描述这个方法适用条件 – 可选).<br/>
   * TODO(这里描述这个方法的执行流程 – 可选).<br/>
   * TODO(这里描述这个方法的使用方法 – 可选).<br/>
   * TODO(这里描述这个方法的注意事项 – 可选).<br/>
   */
  public int getValue_int(String key) {
    return sp.getInt(key, 0);
  }
  public int getValue_int(String key,int defult) {
    return sp.getInt(key, defult);
  }
  /**
   * removeValue:(移除值). <br/>
   * TODO(这里描述这个方法适用条件 – 可选).<br/>
   * TODO(这里描述这个方法的执行流程 – 可选).<br/>
   * TODO(这里描述这个方法的使用方法 – 可选).<br/>
   * TODO(这里描述这个方法的注意事项 – 可选).<br/>
   */
  public void removeValue(String key) {
    editor = sp.edit();
    editor.remove(key);
    editor.commit();
  }
}
