package com.deya.hospital.base;

import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.application.MyAppliaction;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/7/27
 */
public class AddScoreToast {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toastStart = null;
    private static TextView numTv;
    private static Object synObj = new Object();
    public static void showToast(final String message) {


        handler.post(new Runnable() {
            @Override
            public void run() {

                synchronized (synObj) { //加上同步是为了每个toast只要有机会显示出来
                    if(null==toastStart) {
                        toastStart = new Toast(MyAppliaction.getContext());
                        //加载Toast布局
                        View toastRoot = LayoutInflater.from(MyAppliaction.getContext()).inflate(R.layout.dialog_tips, null);
                        //初始化布局控件
                        numTv = (TextView) toastRoot.findViewById(R.id.numTv);
                        //为控件设置属性
                        //Toast的初始化

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(120), dp2px(120));

                        LinearLayout layout = (LinearLayout) toastRoot.findViewById(R.id.layout);
                        layout.setLayoutParams(params);
                        toastStart.setGravity(Gravity.CENTER, 0, 0);
                        toastStart.setDuration(Toast.LENGTH_LONG);
                        toastStart.setView(toastRoot);
                    }else{

                        numTv.setText(message);
                    }
                    toastStart.show();
                }
            }
        });
    }

    private static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                MyAppliaction.getContext().getResources().getDisplayMetrics());
    }


}
