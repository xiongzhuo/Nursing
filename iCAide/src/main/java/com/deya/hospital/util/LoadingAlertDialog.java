package com.deya.hospital.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.deya.acaide.R;

/**
 * ClassName:. LoadingAlertDialog【加载的对话框】 <br/>
 */
public class LoadingAlertDialog extends Dialog {
  private AnimationDrawable animationDrawable;

  /**
   * Creates a new instance of LoadingAlertDialog.
   *
   * @param context 上下文
   * @param theme 主题
   */
  public LoadingAlertDialog(Context context, int theme) {
    super(context, theme);
    setContentView(R.layout.layout_webview_loading);
    this.setOnShowListener(new OnShowListener() {
      @Override
      public void onShow(DialogInterface dialog) {
        ImageView imgloading = (ImageView) findViewById(R.id.img_loading);
        animationDrawable = (AnimationDrawable) imgloading.getDrawable();
        animationDrawable.start();
      }
    });
    this.setOnDismissListener(new OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialog) {
        animationDrawable.stop();
      }
    });
  }
  public LoadingAlertDialog(Context context,boolean cancleBle) {
	    super(context,  R.style.SelectDialog2);
	    setContentView(R.layout.layout_webview_loading);
	    setCancelable(cancleBle);
	    this.setOnShowListener(new OnShowListener() {
	      @Override
	      public void onShow(DialogInterface dialog) {
	        ImageView imgloading = (ImageView) findViewById(R.id.img_loading);
	        animationDrawable = (AnimationDrawable) imgloading.getDrawable();
	        animationDrawable.start();
	      }
	    });
	    this.setOnDismissListener(new OnDismissListener() {
	      @Override
	      public void onDismiss(DialogInterface dialog) {
	        animationDrawable.stop();
	      }
	    });
	  }
  /**
   * Creates a new instance of LoadingAlertDialog.
   *
   * @param context 上下文
   */
  public LoadingAlertDialog(Context context) {
    super(context, R.style.SelectDialog2);
    setContentView(R.layout.loading_dialog);
//    this.setOnShowListener(new OnShowListener() {
//      @Override
//      public void onShow(DialogInterface dialog) {
//        ImageView imgloading = (ImageView) findViewById(R.id.img_loading);
//        animationDrawable = (AnimationDrawable) imgloading.getDrawable();
//        animationDrawable.start();
//      }
//    });
    this.setOnDismissListener(new OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialog) {

      }
    });
  }

}
