package com.deya.hospital.base.img;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UniversalImageLoadTool {

  private static ImageLoader imageLoader = ImageLoader.getInstance();

  public static ImageLoader getImageLoader() {
    return imageLoader;
  }

  public static boolean checkImageLoader() {
    return imageLoader.isInited();
  }

  /**
   * disPlay:【显示图片】. <br/>
   * .@param uri 地址
   * .@param imageAware 图片信息
   * .@param defaultPic 默认图片.<br/>
   */
  public static void disPlay(String uri, ImageAware imageAware, int defaultPic) {
    DisplayImageOptions options =
        new DisplayImageOptions.Builder().showImageOnLoading(defaultPic)
            .showImageForEmptyUri(defaultPic).showImageOnFail(defaultPic).cacheInMemory(true)
            .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new SimpleBitmapDisplayer()).build();

    imageLoader.displayImage(uri, imageAware, options);
  }

  public static void clear() {
    imageLoader.clearMemoryCache();
    imageLoader.clearDiscCache();
  }

  public static void resume() {
    imageLoader.resume();
  }


  /**
   * pause:【暂停加载】. <br/>
   * ..<br/>
   */
  public static void pause() {
    imageLoader.pause();
  }

  /**
   * stop:【停止加载】. <br/>
   * ..<br/>
   */
  public static void stop() {
    imageLoader.stop();
  }

  /**
   * destroy:【销毁加载】. <br/>
   * ..<br/>
   */
  public static void destroy() {
    imageLoader.destroy();
  }
}
