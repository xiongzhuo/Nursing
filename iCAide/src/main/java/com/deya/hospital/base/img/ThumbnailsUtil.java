package com.deya.hospital.base.img;


import android.annotation.SuppressLint;

import java.util.HashMap;

public class ThumbnailsUtil {

  @SuppressLint("UseSparseArrays")
  private static HashMap<Integer, String> hash = new HashMap<Integer, String>();

  /**
   * MapgetHashValue:【得到相册路径】. <br/>
   * .@param key 键 .@param defalt 默认值 .@return.<br/>
   */
  public static String mapgetHashValue(int key, String defalt) {
    if (hash == null || !hash.containsKey(key)) {
      return defalt;
    }
    return hash.get(key);
  }

  public static void put(Integer key, String value) {
    hash.put(key, value);
  }

  public static void clear() {
    hash.clear();
  }
}
