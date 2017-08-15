package com.deya.hospital.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

public class SharedPreferencesUtil {

  private static String SP_NAME = "my_cache_file";
  private static SharedPreferences sp;

  /**
   * saveString:【存储字符串数据】. <br/>
   * .@param context 上下文 
   * .@param key 键 
   * .@param value 值 .<br/>
   */
  public static void saveString(Context context, String key, String value) {
    if (sp == null) {
      sp = context.getSharedPreferences(SP_NAME, 0);
    }
    sp.edit().putString(key, value).commit();
  }

  /**
   * getString:【读取字符串数据】. <br/>
   * .@param context 上下文 
   * .@param key 键 
   * .@param defValue 值 .<br/>
   */
  public static String getString(Context context, String key, String defValue) {
    if (sp == null) {
      sp = context.getSharedPreferences(SP_NAME, 0);
    }
    return sp.getString(key, defValue);
  }

  /**
   * saveInt:【存储整型数据】. <br/>
   * .@param context 上下文 
   * .@param key 键 
   * .@param value 值 .<br/>
   */
  public static void saveInt(Context context, String key, int value) {
    if (sp == null) {
      sp = context.getSharedPreferences(SP_NAME, 0);
    }
    sp.edit().putInt(key, value).commit();
  }
  
  /**
   * getInt:【读取整型数据】. <br/>
   * .@param context 上下文 
   * .@param key 键 
   * .@param defValue 值 .<br/>
   */
  public static int getInt(Context context, String key, int defValue) {
    if (sp == null) {
      sp = context.getSharedPreferences(SP_NAME, 0);
    }
    return sp.getInt(key, defValue);
  }

  /**
   * saveBoolean:【存储布尔型数据】. <br/>
   * .@param context 上下文 
   * .@param key 键 
   * .@param value 值 .<br/>
   */
  public static void saveBoolean(Context context, String key, boolean value) {
    if (sp == null) {
      sp = context.getSharedPreferences(SP_NAME, 0);
    }
    sp.edit().putBoolean(key, value).commit();
  }

  /**
   * getBoolean:【读取布尔型数据】. <br/>
   * .@param context 上下文 
   * .@param key 键 
   * .@param defValue 值 .<br/>
   */
  public static boolean getBoolean(Context context, String key, boolean defValue) {
    if (sp == null) {
      sp = context.getSharedPreferences(SP_NAME, 0);
    }
    return sp.getBoolean(key, defValue);
  }

  /**
   * cleanData:【清除数据】. <br/>
   * .@param context 上下文
   * .@param key 键.<br/>
   */
  public static void cleanData(Context context, String key) {
    if (sp == null) {
      sp = context.getSharedPreferences(SP_NAME, 0);
    }
    sp.edit().putString(key, "").commit();
  }
  public static void clearCacheById(Context context,String id,String parentKey) {
    try {
      JSONObject json = new JSONObject();
      json.remove(id);
      SharedPreferencesUtil.saveString(context, parentKey, json.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
