/**
 * 项目名称:Vicinity3.1 . <br/>
 * 文件名称:BaseActivity.java . <br/>
 * 包名:com.trisun.vicinity.base . <br/>
 * 日期:2015年4月14日下午12:52:11 . <br/>
 *
 * @author LIUXIN . <br/>
 * Copyright (c) 2015,广东云上城科技有限公司 . <br/>
 */

package com.deya.hospital.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.deya.acaide.R;
import com.deya.hospital.analysis.AnalysisManager;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DyShareDialog;
import com.deya.hospital.util.LoadingAlertDialog;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.Tools;
import com.deya.hospital.widget.popu.TipsDialog;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * ClassName:. BaseActivity【第三方统计工具使用到的类】 <br/>
 */
public class BaseActivity extends Activity {

    private LoadingAlertDialog dialog;
    protected Context mcontext;
    protected Activity _activity;
    public Resources res;
    public Tools tools;
    private DialogToast dialogToast;
    MyHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = this;
        tools = new Tools(mcontext, Constants.AC);
        _activity = this;
        res = getResources();
        AnalysisManager.instance().onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalysisManager.instance().onResume(this);
        Log.i("yug", mcontext.getClass().getName() + "=====<");
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnalysisManager.instance().onPause(this);

    }

    public void addScore(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject job = new JSONObject();
                try {
                    job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
                    job.put("aid", id);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                JSONObject jsonObject = MainBizImpl.getInstance().sendSyncRequest("goods/actionGetIntegral", job);

                if (null != jsonObject) {
                    int score = jsonObject.optInt("integral");
                    String str = MyAppliaction.getTools().getValue(Constants.INTEGRAL);
                    if (null != str) {
                        MyAppliaction.getTools().putValue(Constants.INTEGRAL, Integer.parseInt(str) + score + "");
                    } else {
                        MyAppliaction.getTools().putValue(Constants.INTEGRAL, score + "");
                    }
                    Message msg = new Message();
                    msg.what = 10086;
                    msg.obj = score;
                    if (score > 0) {
                        AddScoreToast.showToast("+"+score);
                    }
                }
            }
        }).start();

    }



    @Override
    protected void onStop() {
        super.onStop();
        AnalysisManager.instance().onStop(this);
    }

    /**
     * showprocessdialog:【显示请求数据的processdialog】. <br/>
     * ..<br/>
     */
    protected void showprocessdialog() {
        try {
            if (null == dialog) {
                dialog = new LoadingAlertDialog(mcontext);
            }
            dialog.show();
            dialog.setCancelable(false);
        } catch (Exception e) {

        }

    }

    protected void showUncacleBleProcessdialog() {
        try {
            if (null == dialog) {
                dialog = new LoadingAlertDialog(mcontext);

            }
            dialog.show();
            dialog.setCancelable(false);
        } catch (Exception e) {

        }

    }

    protected void showCaclebleDialog() {
        if (null == dialog) {

            dialog = new LoadingAlertDialog(mcontext);

        }
        dialog.setCancelable(true);
        dialog.show();
    }


    /**
     * 公共弹出框吐司
     *
     * @param txt1
     * @param txt2
     */
    public void showDialogToast(String txt1, String txt2) {
        if (null == dialogToast) {
            dialogToast = new DialogToast(mcontext);
        }
        dialogToast.showTips(txt1, txt2);
    }

    /**
     * dismissdialog:【取消请求数据的processdialog】. <br/>
     * ..<br/>
     */
    protected void dismissdialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
        }


    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (scoreAdddialog != null) {
            scoreAdddialog.dismiss();
        }
        if (null != dyShareDialog) {
            dyShareDialog.dismiss();
        }

        if (null != dialogToast) {
            dialogToast.dismiss();
        }
        setContentView(R.layout.emperty_contentview);
        super.onDestroy();
    }

    public void StartActivity(Class<?> T) {
        Intent it = new Intent(mcontext, T);
        startActivity(it);
    }

    TipsDialog scoreAdddialog;

    public void showTipsDialog(String score) {
        if (scoreAdddialog == null) {
            scoreAdddialog = new TipsDialog(mcontext, score + "");
        }
        scoreAdddialog.showScore(score);
    }

    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    public void setLis(View view, View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }

    DyShareDialog dyShareDialog;

    public void showShareDialog(String title, String content,
                                String url) {
        if (null == dyShareDialog) {
            dyShareDialog = new DyShareDialog(this, title, content, url);
            dyShareDialog.show();
        } else {
            dyShareDialog.show();
        }

    }
//  @Override
//  public void onLayoutChange(View v, int left, int top, int right,  
//          int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {  
//        
//      //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值  
//        
////    System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);  
////    System.out.println(left + " " + top +" " + right + " " + bottom);  
//        
//        
//      //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起  
//      if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){  
//            
//          Toast.makeText(MainActivity.this, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();  
//        
//      }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){  
//            
//          Toast.makeText(MainActivity.this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();  
//        
//      }  
//        
//  }  
}
