/**
 * 项目名称:Vicinity3.1 . <br/>
 * 文件名称:SplashActivity.java . <br/>
 * 包名:com.trisun.vicinity.init.activity . <br/>
 * 日期:2015年4月10日下午4:53:13 . <br/>
 * 
 * @author LIUXIN . <br/>
 *         Copyright (c) 2015,广东云上城科技有限公司 . <br/>
 *
 */

package com.deya.hospital.base;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.deya.acaide.R;
import com.deya.hospital.login.LoginActivity;
import com.deya.hospital.util.ParamsUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ClassName:. SplashActivity【启动主界面】 <br/>
 */
public class SplashActivity extends BaseActivity {

  private ImageView imgFirst;
  private ImageView imgSecond;
  public  static final int GOTO_HOMEFRAGMENT=0;
  @SuppressLint({"NewApi", "InlinedApi"})
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    if (Build.VERSION.SDK_INT >= ParamsUtil.SDK_VERSION) {
      WindowManager.LayoutParams params = getWindow().getAttributes();
      params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
      getWindow().setAttributes(params);
    }
    setContentView(R.layout.init_activity_splash);
    init();
  }

  /**
   * .INIT:初始化相关数据
   */
  private void init() {
	  startTimer();
  }
  private Timer timerReciprocal;
  private int secounds = 0;
  private void startTimer() {
	    TimerTask task = new TimerTask() {
	      @Override
	      public void run() {
	        secounds++;
	        if (secounds == 3) {
	        	Intent it=new Intent(SplashActivity.this,LoginActivity.class);
				startActivity(it);
				finish();
	        }
	      }
	    };
	    timerReciprocal = new Timer(true);
	    timerReciprocal.schedule(task, 800, 800);
	  }
  @Override
	protected void onDestroy() {
	  timerReciprocal.cancel();
		super.onDestroy();
	}
}
