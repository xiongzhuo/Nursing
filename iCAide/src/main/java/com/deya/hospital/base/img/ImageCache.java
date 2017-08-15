package com.deya.hospital.base.img;

import android.graphics.Bitmap;

import java.util.WeakHashMap;


public class ImageCache extends WeakHashMap<String, Bitmap> {

  public boolean isCached(String url) {
    return containsKey(url) && get(url) != null;
  }

}
