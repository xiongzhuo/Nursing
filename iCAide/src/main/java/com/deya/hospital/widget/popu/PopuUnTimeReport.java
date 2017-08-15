package com.deya.hospital.widget.popu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.deya.acaide.R;
import com.deya.hospital.util.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 填写邮箱弹框
* @author  yung
* @date 创建时间：2016年1月15日 上午11:52:33 
* @version 1.0
 */
public class PopuUnTimeReport implements OnClickListener {
	private MyPopu myPopu = null;

	private EditText edt_email_res;
	private TextView btn_cancel, btn_enter;
	private LinearLayout lay1;
	private OnPopuClick onPopuClick;
	private Context ctx;
	boolean needCheck=false;
	
	public PopuUnTimeReport(Context ctx,Activity _activity, boolean needCheck,View view,String text,OnPopuClick onPopuClick){
		myPopu = new MyPopu(ctx, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, R.layout.popwindow_email);
		this.ctx=ctx;
		this.onPopuClick=onPopuClick;
		this.needCheck=needCheck;
		btn_cancel=(TextView)myPopu.getTextView(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		
		btn_enter=(TextView)myPopu.getTextView(R.id.btn_enter);
		btn_enter.setOnClickListener(this);
		
		lay1=(LinearLayout)myPopu.getLinearLayout(R.id.lay1);
		lay1.setOnClickListener(this);
		
		edt_email_res=(EditText)myPopu.getEditText(R.id.edt_email_res);
		if(!needCheck){
			edt_email_res.setHint("用   , 分隔可添加多个邮箱!");
		}
		edt_email_res.setText(text);
		edt_email_res.setSelection(edt_email_res.getText().length());
		
		myPopu.setAlpha(200);
		myPopu.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
		myPopu.showSoftInput(_activity);
	}
	
	

	private void dismiss(){
			if(null!=myPopu){
				myPopu.dismiss();
				myPopu=null;
			}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
		case R.id.lay1:
			dismiss();
			this.onPopuClick.cancel();
			break;
		case R.id.btn_enter:
			String email=edt_email_res.getText().toString();
			if(email.equals("")){
				ToastUtils.showToast(this.ctx, "请输入邮箱！");
				return;
			}else{
				if(needCheck){
				String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
				Pattern regex = Pattern.compile(check);    
				Matcher matcher = regex.matcher(email);    
				boolean isMatched = matcher.matches();    
				if(isMatched){
					this.onPopuClick.enter(email);
					dismiss();
				}else{
					ToastUtils.showToast(this.ctx, "请正确输入邮箱！");
					return;
				}
				}else{
					this.onPopuClick.enter(email);
					dismiss();
					}
			}
			break;
		default:
			break;
		}

	}

	public interface OnPopuClick {
		public void enter(String text);
		public void cancel();
	}

}
