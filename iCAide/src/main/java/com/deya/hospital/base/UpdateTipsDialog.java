package com.deya.hospital.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MyDialogInterface;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class UpdateTipsDialog extends BaseDialog implements View.OnClickListener{
        View.OnClickListener click;
        TextView contentTv,titleTv,contentTiltle;
        Button enterBtn;
        MyDialogInterface dialogInterface;
        Button cancleBtn;

        public UpdateTipsDialog(Context context, MyDialogInterface dialogInterface) {
            super(context);
            this.dialogInterface=dialogInterface;

        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_tips_update);
            cancleBtn=(Button) this.findViewById(R.id.cancleBtn);
            cancleBtn.setOnClickListener(this);
            titleTv= (TextView) this.findViewById(R.id.titleTv);
            contentTiltle= (TextView) this.findViewById(R.id.contentTiltle);
            enterBtn=(Button) this.findViewById(R.id.enterBtn);
            contentTv= (TextView) this.findViewById(R.id.contentTv);
            enterBtn.setOnClickListener(this);
        }

        public void setContentTv(String string){
            contentTv.setText(string);
        }
        public void setContent(String content){
            contentTv.setText(content);
        }
        public void setButton(String txt){
            enterBtn.setText(txt);
        }
        public void setCancleButton(String txt){
            cancleBtn.setText(txt);
        }
        public void setTitleTv(String text){
            titleTv.setText(text);
        }
        public void setContentTiltle(String txt){
            contentTiltle.setVisibility(View.VISIBLE);
            contentTiltle.setText(txt);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.enterBtn:
                    dismiss();
                    dialogInterface.onEnter();
                    break;
                case R.id.cancleBtn:
                    dialogInterface.onCancle();
                    dismiss();
                    break;
            }

        }


}
