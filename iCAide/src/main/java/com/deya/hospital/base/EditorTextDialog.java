package com.deya.hospital.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.deya.acaide.R;
import com.im.sdk.dy.common.utils.ToastUtil;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/4/14
 */
public class EditorTextDialog extends BaseDialog implements View.OnClickListener{
    TextView title;
    EditText edt_email_res;
    String titleStr,hintStr;
    OnTextSumbmit onTextSumbmit;
    /**
     * Creates a new instance of MyDialog.
     *
     * @param context
     */
    public EditorTextDialog(Context context,String titleStr,String hint) {
        super(context);
        this.titleStr=titleStr;
        this.hintStr=hint;
    }

   public void  setOnTextSumbmitLis(OnTextSumbmit onTextSumbmit){
       this.onTextSumbmit=onTextSumbmit;

    }
    public void setInputType (int inputType){
        if(null!=edt_email_res){
            edt_email_res.setInputType(inputType);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_dialog);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_enter).setOnClickListener(this);
        title= (TextView) this.findViewById(R.id.title);
        title.setText(titleStr);
        edt_email_res= (EditText) this.findViewById(R.id.edt_email_res);
        edt_email_res.setHint(hintStr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_enter:
                if(edt_email_res.getText().length()>0){
                    onTextSumbmit.onTextSumbmit(edt_email_res.getText().toString());
                    dismiss();
                }else{
                    ToastUtil.showMessage("输入内容不能为空!");
                    dismiss();
                }

                break;
        }

    }
    public interface OnTextSumbmit{
         void onTextSumbmit(String txt);
    }
}
