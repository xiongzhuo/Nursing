package com.deya.hospital.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @description 显示一组网络图片(目前网页JS用到了)
 * @version 1.0
 * @author 邓浩然
 * @date 2014年9月16日 下午2:13:24
 */
public class ShowNetWorkImageActivity extends BaseActivity {
  private ViewPager viewpagerimage;
  private LinearLayout viewGroup;
  private ImageView imageView = null;
  private ImageView[] imageViews = null;
  private List<View> pics;
  private String[] urls;
  private long firstMillis = 0;// 用于保存时间戳
  private long timeLong = 600;// 时间戳
  private DisplayImageOptions squareoptions;
  int currpos;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_show_networkimage);
    squareoptions =
        new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loading_onepoint)
            .showImageForEmptyUri(R.drawable.loading_twopoint)
            .showImageOnFail(R.drawable.loading_threepoint).resetViewBeforeLoading(true)
            .cacheOnDisk(true).considerExifParams(true).cacheInMemory(true)
            .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new FadeInBitmapDisplayer(300)).build();
    // 第一次请求的时间
    long secondMillis = SystemClock.uptimeMillis();
    long value = secondMillis - firstMillis;
    if (value > timeLong) {
      firstMillis = secondMillis;
      initViewPager();
    }
  }

  private void initViewPager() {
    urls = getIntent().getStringArrayExtra("urls");
    String nowImage = getIntent().getStringExtra("nowImage");
    viewpagerimage = (ViewPager) findViewById(R.id.viewpager_image);
    viewGroup = (LinearLayout) findViewById(R.id.viewGroup);
    pics = new ArrayList<View>();
    int pos = getIntent().getIntExtra("nowImage", 0);
    for (int i = 0; i < urls.length; i++) {
      if (nowImage != null && nowImage.equals(urls[i])) {
        pos = i;
      }
      ImageView photoView = new ImageView(ShowNetWorkImageActivity.this);
      pics.add(photoView);
    }
    // 对imageviews进行填充
    imageViews = new ImageView[urls.length];
    // 小图标
    for (int i = 0; i < urls.length; i++) {
      imageView = new ImageView(this);
      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
      layoutParams.setMargins(5, 10, 5, 10);
      imageView.setLayoutParams(layoutParams);
      imageView.setPadding(5, 5, 5, 5);
      imageViews[i] = imageView;
      if (i == pos) {
        imageViews[i].setBackgroundResource(R.drawable.banner_focus);
      } else {
        imageViews[i].setBackgroundResource(R.drawable.banner_normal);
      }
      viewGroup.addView(imageViews[i]);
    }
    viewpagerimage.setAdapter(new AdvAdapter(pics));
    viewpagerimage.setCurrentItem(pos);
    viewpagerimage.setOnPageChangeListener(new GuidePageChangeListener());
  }

  private final class GuidePageChangeListener implements OnPageChangeListener {
    @Override
    public void onPageScrollStateChanged(int arg0) {}

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {}

    @Override
    public void onPageSelected(int arg0) {
      for (int i = 0; i < imageViews.length; i++) {
        imageViews[arg0].setBackgroundResource(R.drawable.banner_focus);
        if (arg0 != i) {
          imageViews[i].setBackgroundResource(R.drawable.banner_normal);
        }
      }
    }
  }

  private final class AdvAdapter extends PagerAdapter {
    private List<View> views = null;

    public AdvAdapter(List<View> views) {
      this.views = views;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
      ((ViewPager) arg0).removeView(views.get(arg1));
    }

    @Override
    public void finishUpdate(View arg0) {}

    @Override
    public int getCount() {
      return views == null ? 0 : views.size();
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
      ImageView itemview = (ImageView) views.get(arg1);
      itemview.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          // TODO Auto-generated method stub
          finish();
        }
      });
      itemview.setOnLongClickListener(new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
          // TODO Auto-generated method stub
          showDownloadDialog();
          return true;
        }
      });
      if("1".equals(getIntent().getStringExtra("type"))){
    	  Bitmap bitmap=BitmapFactory.decodeFile(urls[arg1].toString().replaceAll("\"", ""));
    	  itemview.setImageBitmap(bitmap);
      }else {
    	  ImageLoader.getInstance().displayImage(urls[arg1].toString().replaceAll("\"", ""), itemview,
    	          squareoptions);
	}
      
      ViewPager convertview = (ViewPager) arg0;
      convertview.addView(itemview, 0);
      return itemview;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
      return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {}

    @Override
    public Parcelable saveState() {
      return null;
    }

    @Override
    public void startUpdate(View arg0) {}
  }

  private void showDownloadDialog() {
    final String[] mItems =
        {CommonUtils.getString(R.string.common_image_save),
            CommonUtils.getString(R.string.photo_folder_choice_cancle)};
    AlertDialog.Builder builder = new AlertDialog.Builder(ShowNetWorkImageActivity.this);
    builder.setTitle(CommonUtils.getString(R.string.common_image_operator));
    builder.setItems(mItems, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        // 点击后弹出窗口选择了第几项
        switch (which) {
          case 0:
            dialog.dismiss();
            Toast.makeText(ShowNetWorkImageActivity.this,
                CommonUtils.getString(R.string.common_start_download) + "...", Toast.LENGTH_SHORT)
                .show();
            new ImageDownLoad(ShowNetWorkImageActivity.this, urls[viewpagerimage.getCurrentItem()]
                .toString().replaceAll("\"", ""), CommonUtils
                .getString(R.string.common_clound_city), CommonUtils
                .getString(R.string.common_save_local));
            break;
          case 1:
            dialog.dismiss();
            break;
          default:
            break;
        }
      }
    });
    builder.create().show();
  }

  @Override
  protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
    viewpagerimage = null;
    viewGroup = null;
    imageView = null;
    imageViews = null;
    pics = null;
    System.gc();
  }
}
