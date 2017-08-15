package com.deya.hospital.base.img;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

public class BitmapUtil {


  /**
   * reviewPicRotate:【获取图片文件的信息，是否旋转了90度，如果是则反转】. <br/>
   * .@param bitmap 需要旋转的图片 .@param path 图片的路径 .@return.<br/>
   */
  public static Bitmap reviewPicRotate(Bitmap bitmap, String path) {
    int degree = getPicRotate(path);
    if (degree != 0) {
      Matrix matrix = new Matrix();
      int width = bitmap.getWidth();
      int height = bitmap.getHeight();
      matrix.setRotate(degree); // 旋转angle度
      bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);// 从新生成图片
    }
    return bitmap;
  }

  /**
   * getPicRotate:【读取图片文件旋转的角度】. <br/>
   * .@param path 图片绝对路径 .@return 图片旋转的角度.<br/>
   */
  public static int getPicRotate(String path) {
    int degree = 0;
    try {
      ExifInterface exifInterface = new ExifInterface(path);
      int orientation =
          exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
              ExifInterface.ORIENTATION_NORMAL);
      switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_90:
          degree = 90;
          break;
        case ExifInterface.ORIENTATION_ROTATE_180:
          degree = 180;
          break;
        case ExifInterface.ORIENTATION_ROTATE_270:
          degree = 270;
          break;
        default:
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return degree;
  }
}
