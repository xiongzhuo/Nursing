package com.deya.hospital.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.account.QualificationActivity;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.supervisor.PartTimeStaffDialog;


/**
 * ClassName:. CommonUtils【通用工具类】 <br/>
 */
public class CommonUtils {

  /**
   * getContext:(获取上下文). <br/>
   */
  public static Context getContext() {
    return MyAppliaction.getContext();
  }

  /**
   * dip2px:(dip转换px). <br/>
   */
  public static int dip2px(int dip) {
    final float scale = getContext().getResources().getDisplayMetrics().density;
    return (int) (dip * scale + 0.5f);
  }

  /**
   * px2dip:(pxz转换dip). <br/>
   */
  public static int px2dip(int px) {
    final float scale = getContext().getResources().getDisplayMetrics().density;
    return (int) (px / scale + 0.5f);
  }

  /**
   * inflate:(填充布局). <br/>
   */
  public static View inflate(int resId) {
    return LayoutInflater.from(getContext()).inflate(resId, null);
  }

  /**
   * getResources:(获取资源). <br/>
   */
  public static Resources getResources() {

    return getContext().getResources();
  }

  /**
   * getString:(获取文字). <br/>
   */
  public static String getString(int resId) {
    return getResources().getString(resId);
  }

  /**
   * getStringArray:(获取文字数组 ). <br/>
   */
  public static String[] getStringArray(int resId) {
    return getResources().getStringArray(resId);
  }

  /**
   * getDimens:(获取dimen). <br/>
   */
  public static int getDimens(int resId) {
    return getResources().getDimensionPixelSize(resId);
  }

  /**
   * getDrawable:(获取drawable). <br/>
   */
  public static Drawable getDrawable(int resId) {
    return getResources().getDrawable(resId);
  }

  /**
   * getColor:(获取颜色). <br/>
   */
  public static int getColor(int resId) {
    return getResources().getColor(resId);
  }

  /**
   * getColorStateList:(获取颜色选择器). <br/>
   */
  public static ColorStateList getColorStateList(int resId) {
    return getResources().getColorStateList(resId);
  }

  /**
   * isRunInMainThread:【判断当前的线程是不是在主线程】. <br/>
   * .@return 是否是主线程.<br/>
   */
  public static boolean isRunInMainThread() {
    return android.os.Process.myTid() == getMainThreadId();
  }

  /**
   * getMainThreadId:【获取主线程id】. <br/>
   * .@return 主线程id.<br/>
   */
  public static long getMainThreadId() {
    return MyAppliaction.getMainThreadid();
  }


  private static long lastClickTime;

  /**
   * isFastDoubleClick:【判断是否为双击】. <br/>
   * .@return.<br/>
   */
  public static boolean isFastDoubleClick() {
    long time = System.currentTimeMillis();
    long timeD = time - lastClickTime;
    if (0 < timeD && timeD < 500) {
      return true;
    }
    lastClickTime = time;
    return false;
  }
  

  /**
   * getSDCardPath:【获取SD卡路劲】. <br/>
   * .@return.<br/>
   */
  public static String getSDCardPath() {
    String sdDir = null;
    try {
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        // sd card 可用
        sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/vicityCache/";
      } else {
        // 当前不可用
        sdDir = null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      sdDir = null;
    }
    return sdDir;
  }

  public static PartTimeStaffDialog getTipsDialog(final Activity activity, int imgID) {
    PartTimeStaffDialog dialog = new PartTimeStaffDialog(activity, false,imgID, "下次再说", "去认证", "软件功能仅对实名认证用户开放哦，请尽快进行认证，如有疑问请致电客服电话：400-969-7756。", new PartTimeStaffDialog.PDialogInter() {

      @Override
      public void onEnter() {
        Intent it = new Intent(activity, QualificationActivity.class);
        if (activity.getClass().getName().equals("com.deya.hospital.login.MyRegisterActivity")) {
          it.putExtra("register", "LOGIN_MYREGISTERACTIVITY");
          activity.startActivity(it);
          activity.finish();
        } else {
          activity.startActivity(it);
        }
      }

      @Override
      public void onCancle() {
        Intent it = new Intent(activity, MainActivity.class);
        if (activity.getClass().getName().equals("com.deya.hospital.login.MyRegisterActivity")) {
          it.putExtra("register", "LOGIN_MYREGISTERACTIVITY");
          activity.startActivity(it);
          activity.finish();
        }

      }
    });
    dialog.setCancelable(false);
    return dialog;
  }
  public static PartTimeStaffDialog getTipsDialiog(final Activity activity,PartTimeStaffDialog.PDialogInter inter) {
    PartTimeStaffDialog dialog = new PartTimeStaffDialog(activity, false, R.drawable.ic_register_checktip, "下次再说", "去认证", "正在认证中", inter);
    dialog.setCancelable(false);
    return  dialog;
  }
  public static  int  isSignedSucUser(Tools tools){
    if(null==tools.getValue(Constants.STATE)){
      return 0;
    }
    int state=Integer.parseInt(AbStrUtil.isEmpty(tools.getValue(Constants.STATE))?"4":tools.getValue(Constants.STATE));
    return state;
  }


  public static void  callServiceTell(Context context){
    try {
      Intent intent = new Intent(Intent.ACTION_DIAL, Uri
              .parse("tel:"
                      +getResources().getString(R.string.credit_tel)));
      context.startActivity(intent);
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(context, "该设备不支持通话功能",
              Toast.LENGTH_SHORT).show();
    }

  }


  public static void  callServiceTell(Context context,String phone){
    try {
      Intent intent = new Intent(Intent.ACTION_DIAL, Uri
              .parse("tel:"
                      +phone));
      context.startActivity(intent);
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(context, "该设备不支持通话功能",
              Toast.LENGTH_SHORT).show();
    }

  }
}
