package com.deya.hospital.base;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;

public class BaseTipsDialog extends BaseDialog implements View.OnClickListener{

	Button cancleBtn;
	Button enterBtn;
	PDialogInter inter;
	TextView contentTv2,contentTv1;
	String title="";
	String cancleBtnStr="";
	String enterBtnStr="";
	boolean showContent2;
	boolean isTitleImg = false;
	private ImageView img;
	private TextView titleTv;
	int ImgId;

	public BaseTipsDialog(Context context, PDialogInter inter) {
		super(context);
		this.inter=inter;
		this.isTitleImg = false;
	}

	public void setCancleBtn(String string){
		enterBtn.setVisibility(View.GONE);
		cancleBtn.setText(string);
	}

	public void setImageResouce(int imgId){
		img.setVisibility(View.VISIBLE);
		img.setImageResource(ImgId);
	}

	public void setContentTv(String str,int corlor){
		contentTv1.setVisibility(View.VISIBLE);
		contentTv1.setText(str);
		contentTv1.setGravity(Gravity.CENTER);
		contentTv1.setTextColor(corlor);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_part_time_staff);
		cancleBtn=(Button) this.findViewById(R.id.cancleBtn);
		enterBtn=(Button) this.findViewById(R.id.enterBtn);
		contentTv1=(TextView) this.findViewById(R.id.contentTv1);
		contentTv2=(TextView) this.findViewById(R.id.contentTv2);
		titleTv=(TextView) this.findViewById(R.id.titleTv);
		img=(ImageView) this.findViewById(R.id.img);
		cancleBtn.setOnClickListener(this);
		enterBtn.setOnClickListener(this);
	}
	public void setButtons(String tx1,String tx2){
		cancleBtn.setText(tx1);
		enterBtn.setText(tx2);
	}
	public interface PDialogInter{
		public void onCancle();
		public void onEnter();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancleBtn:
			inter.onCancle();
			dismiss();
			break;
		case R.id.enterBtn:
			inter.onEnter();
			dismiss();
			break;
		default:
			break;
		}
	}
}
