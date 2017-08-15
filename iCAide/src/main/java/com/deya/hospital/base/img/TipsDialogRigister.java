package com.deya.hospital.base.img;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.base.BaseDialog;

public class TipsDialogRigister extends BaseDialog implements View.OnClickListener{
	android.view.View.OnClickListener click;
	TextView contentTv,titleTv,contentTiltle;
	Button enterBtn;
	MyDialogInterface dialogInterface;
	Button cancleBtn;

	public TipsDialogRigister(Context context,MyDialogInterface dialogInterface) {
		super(context);
		this.dialogInterface=dialogInterface;
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_tips_rgister);
		cancleBtn=(Button) this.findViewById(R.id.cancleBtn);
		cancleBtn.setOnClickListener(this);
		titleTv= (TextView) this.findViewById(R.id.titleTv);
		contentTiltle= (TextView) this.findViewById(R.id.contentTiltle);
		enterBtn=(Button) this.findViewById(R.id.enterBtn);
		contentTv= (TextView) this.findViewById(R.id.contentTv);
		enterBtn.setOnClickListener(this);
	}

	/**
	 * 设置文本是否居中
	 * @param gravity
     */
	public  void setContentTxtGravity(int  gravity,int corlor){
		if(gravity!=0){
		contentTv.setGravity(gravity);
		}
		if(gravity!=0){
			contentTv.setTextColor(corlor);
		}
	}
	public  void setContentTiltleTxtGravity(int  gravity,int corlor){
		if(gravity!=0){
			contentTiltle.setGravity(gravity);
		}
		if(gravity!=0){
			contentTiltle.setTextColor(corlor);
		}
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
