/**
 * 项目名称:Vicinity3.1 . <br/>
 * 文件名称:BaseFragmentActivity.java . <br/>
 * 包名:com.trisun.vicinity.base . <br/>
 * 日期:2015年4月14日下午4:45:43 . <br/>
 * 
 * @author Lingwk . <br/>
 *         Copyright (c) 2015,广东聚光电子科技有限公司-深圳分公司. . <br/>
 *
 */

package com.deya.hospital.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.deya.acaide.R;
import com.deya.hospital.analysis.AnalysisManager;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DyShareDialog;
import com.deya.hospital.util.LoadingAlertDialog;
import com.deya.hospital.util.Tools;

/**
 * ClassName:. BaseFragmentActivity【第三方统计工具使用到的类】 <br/>
 */
public class BaseFragmentActivity extends FragmentActivity {
  protected Context mcontext;
  private LoadingAlertDialog dialog;
  private static final String tag = "BaseFragmentActivity";
  public Tools tools;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
	  Log.i(tag, "onCreate");
    super.onCreate(savedInstanceState);
    mcontext = this;
    tools=new Tools(mcontext, Constants.AC);
    AnalysisManager.instance().onCreate();
  }

  @Override
  protected void onResume() {
    super.onResume();
    AnalysisManager.instance().onResume(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    AnalysisManager.instance().onPause(this);
  }
  

  @Override
protected void onStop() {
	super.onStop();
	AnalysisManager.instance().onStop(this);
}
  public void StartActivity(Class<?> T) {
    Intent it = new Intent(mcontext, T);
    startActivity(it);
  }
/**
   * showprocessdialog:【显示请求数据的processdialog】. <br/>
   * ..<br/>
   */
  protected void showprocessdialog() {
    if (null == dialog) {
      dialog = new LoadingAlertDialog(mcontext);
    }
    dialog.show();
  }
  public    <T extends View> T findView(int id){
    return (T) findViewById(id);
  }
  public void setLis(View view, View.OnClickListener listener){
    view.setOnClickListener(listener);
  }
  @Override
  protected void onDestroy() {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }
    if(null!=dyShareDialog){
      dyShareDialog.cancel();
    }
   setContentView(R.layout.emperty_contentview);
    super.onDestroy();
  }

  /**
   * dismissdialog:【取消请求数据的processdialog】. <br/>
   * ..<br/>
   */
  protected void dismissdialog() {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }
  }
  DyShareDialog dyShareDialog;
  public void showShareDialog(String title, String content,
                              String url){
    if(null==dyShareDialog){
      dyShareDialog = new DyShareDialog(this, title, content, url);
      dyShareDialog.show();
    }else{
      dyShareDialog.show();
    }

  }
  protected void showprocessdialog(boolean cancleble) {
	    if (null == dialog) {
	      dialog = new LoadingAlertDialog(mcontext,cancleble);
	    }
	    dialog.show();
	  }
}
