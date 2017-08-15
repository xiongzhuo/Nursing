package com.deya.hospital.base.img;

import android.content.Context;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class CheckImageLoaderConfiguration {
  private static final long discCacheLimitTime = 3600 * 24 * 15L;

  /**
   * checkImageLoaderConfiguration:【检查图片配置信息】. <br/>
   * .@param context.<br/>
   */
  public static void checkImageLoaderConfiguration(Context context) {
    if (!UniversalImageLoadTool.checkImageLoader()) {
      // This configuration tuning is custom. You can tune every option, you may tune some of them,
      // or you can create default configuration by
      // ImageLoaderConfiguration.createDefault(this);
      // method.
      ImageLoaderConfiguration config =
          new ImageLoaderConfiguration.Builder(context)
              .threadPriority(Thread.NORM_PRIORITY)
              .denyCacheImageMultipleSizesInMemory()
              .diskCacheFileNameGenerator(new Md5FileNameGenerator())
              .diskCache(
                  new LimitedAgeDiscCache(StorageUtils.getCacheDirectory(context),
                      discCacheLimitTime)).tasksProcessingOrder(QueueProcessingType.LIFO).build();
      // Initialize ImageLoader with configuration.
      ImageLoader.getInstance().init(config);
    }
  }
}
