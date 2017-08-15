package com.deya.hospital.base;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/7/4
 */
public class DialogToast extends BaseDialog{
    TextView title;
    TextView  content;
    /**
     * Creates a new instance of MyDialog.
     *
     * @param context
     */
    public DialogToast(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tost);
        title=findView(R.id.title);
        content=findView(R.id.content);
    }

    public void showTips(String  txt1,String txt2) {
              show();
        if(!AbStrUtil.isEmpty(txt1)){
        title.setText(txt1);
        }
        if(!AbStrUtil.isEmpty(txt2)){
            content.setText(txt2);
        }

    }
}
