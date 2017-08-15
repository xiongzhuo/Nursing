package com.deya.hospital.supervisor;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.util.AbStrUtil;

public class PartTimeStaffDialog extends BaseDialog implements android.view.View.OnClickListener{

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

	public PartTimeStaffDialog(Context context,boolean showContent2,String title,PDialogInter inter) {
		super(context);
		this.inter=inter;
		this.title=title;
		this.showContent2=showContent2;
		this.isTitleImg = false;
	}

	/**
	 *
	 * @param context
	 * @param showContent2
	 * @param inter
	 * @param cancleBtnStr
     * @param enterBtnStr
     */
	public PartTimeStaffDialog(Context context, boolean showContent2, int imgid,String cancleBtnStr,String enterBtnStr,String title, PDialogInter inter) {
		super(context);
		this.inter=inter;
		this.showContent2=showContent2;
		this.title = title;
		this.isTitleImg = true;
		this.ImgId=imgid;
		this.cancleBtnStr = cancleBtnStr;
		this.enterBtnStr = enterBtnStr;
	}
	public PartTimeStaffDialog(Context context,String title, String cancleBtnStr,String enterBtnStr, PDialogInter inter) {
		super(context);
		this.inter=inter;
		this.title = title;
		this.isTitleImg = false;
		this.showContent2=false;
		this.cancleBtnStr = cancleBtnStr;
		this.enterBtnStr = enterBtnStr;
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

		if(!AbStrUtil.isEmpty(title)){
			contentTv1.setText(title);
		}

		if(!showContent2){
			contentTv2.setVisibility(View.GONE);
			if(!AbStrUtil.isEmpty(enterBtnStr)){
				enterBtn.setText(enterBtnStr);
			}else{
				enterBtn.setText("确认");
			}

		}
		if(!AbStrUtil.isEmpty(cancleBtnStr)){
			cancleBtn.setText(cancleBtnStr);
			enterBtn.setText(enterBtnStr);
		}
		if (isTitleImg) {
			titleTv.setVisibility(View.GONE);
			img.setVisibility(View.VISIBLE);
			img.setImageResource(ImgId);
			cancleBtn.setText(cancleBtnStr);
			enterBtn.setText(enterBtnStr);
		} else {
			titleTv.setVisibility(View.VISIBLE);
			img.setVisibility(View.GONE);
		}

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
