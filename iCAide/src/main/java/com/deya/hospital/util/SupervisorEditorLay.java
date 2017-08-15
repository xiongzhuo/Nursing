package com.deya.hospital.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.deya.acaide.R;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : yugq
 * @date ${date}
 */
public class SupervisorEditorLay extends LinearLayout {
    EditText checkContentEdt,problemEdt,sugesstEdt;
    LinearLayout layout;
    public SupervisorEditorLay(Context context, AttributeSet attrs) {
        super(context, attrs);
        layout= (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.layout_surpvisor_editor, this, true);
        checkContentEdt= (EditText) layout.findViewById(R.id.checkContentEdt);
        problemEdt= (EditText) layout.findViewById(R.id.problemEdt);
        sugesstEdt= (EditText) layout.findViewById(R.id.sugesstEdt);
    }
    public  void setEditorEnable(boolean enable){
        checkContentEdt.setEnabled(enable);
        problemEdt.setEnabled(enable);
        sugesstEdt.setEnabled(enable);
    }

    public EditText getCheckContentEdt(){
        return checkContentEdt;
    }
    public EditText getProblemEdt(){
        return problemEdt;
    }
    public EditText getSugesstEdt(){
        return sugesstEdt;
    }
}
