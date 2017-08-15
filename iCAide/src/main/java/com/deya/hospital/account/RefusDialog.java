package com.deya.hospital.account;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.base.BaseDialog;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class RefusDialog extends BaseDialog implements View.OnClickListener {
    Button cancleBtn;
    Button enterBtn;
    RefuseInter dialogInterface;
    int selectPos;
    ImageView check_img1, check_img2;
    EditText moreEditor;

    public RefusDialog(Context context, RefuseInter dialogInterface) {
        super(context);
        this.dialogInterface = dialogInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tips_refus);
        initView();
    }

    private void initView() {
        cancleBtn = (Button) this.findViewById(R.id.cancleBtn);
        enterBtn = (Button) this.findViewById(R.id.enterBtn);
        check_img1= (ImageView) this.findViewById(R.id.check_img1);
        check_img2= (ImageView) this.findViewById(R.id.check_img2);

        moreEditor= (EditText) this.findViewById(R.id.moreEditor);
        cancleBtn.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        check_img1.setOnClickListener(this);
        check_img2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancleBtn:
                dialogInterface.onItemSelect(selectPos,moreEditor.getText().toString());
                dismiss();
                break;
            case R.id.enterBtn:
                dismiss();
                dialogInterface.onCancle();
                break;
            case R.id.check_img1:
                selectPos=1;
                check_img2.setImageResource(R.drawable.choose_box_normal);
                check_img1.setImageResource(R.drawable.choose_box_select);
                break;
            case R.id.check_img2:
                selectPos=2;
                check_img1.setImageResource(R.drawable.choose_box_normal);
                check_img2.setImageResource(R.drawable.choose_box_select);
                break;
        }

    }

    interface RefuseInter extends MyDialogInterface{
       abstract void  onItemSelect(int position,String text);

    }

}
