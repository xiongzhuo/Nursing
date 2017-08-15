package com.deya.hospital.widget.popu;

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

public class PopuAddHospital implements OnClickListener {
	private MyPopu myPopu = null;

	private EditText hospitalEdt;
	private TextView btn_cancel, btn_enter;
	private LinearLayout lay1;
	private OnPopuClick onPopuClick;
	private Context ctx;
	
	public PopuAddHospital(Context ctx,Activity _activity, View view,String text,OnPopuClick onPopuClick){
		myPopu = new MyPopu(ctx, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, R.layout.popuwindow_addhospital);
		this.ctx=ctx;
		this.onPopuClick=onPopuClick;
		btn_cancel=(TextView)myPopu.getTextView(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		
		btn_enter=(TextView)myPopu.getTextView(R.id.btn_enter);
		btn_enter.setOnClickListener(this);
		
		lay1=(LinearLayout)myPopu.getLinearLayout(R.id.lay1);
		lay1.setOnClickListener(this);
		
		hospitalEdt=(EditText)myPopu.getEditText(R.id.hospitalEdt);
		hospitalEdt.setText(text);
		hospitalEdt.setSelection(hospitalEdt.getText().length());
		myPopu.setAlpha(200);
		myPopu.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
		myPopu.showSoftInput(_activity);
	}
	
	public PopuAddHospital(Context ctx,Activity _activity,String hint, View view,String text,OnPopuClick onPopuClick){
		myPopu = new MyPopu(ctx, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, R.layout.popuwindow_addhospital);
		this.ctx=ctx;
		this.onPopuClick=onPopuClick;
		btn_cancel=(TextView)myPopu.getTextView(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		
		btn_enter=(TextView)myPopu.getTextView(R.id.btn_enter);
		btn_enter.setOnClickListener(this);
		
		lay1=(LinearLayout)myPopu.getLinearLayout(R.id.lay1);
		lay1.setOnClickListener(this);
		
		hospitalEdt=(EditText)myPopu.getEditText(R.id.hospitalEdt);
		hospitalEdt.setHint(hint);
		hospitalEdt.setText(text);
		hospitalEdt.setSelection(hospitalEdt.getText().length());
		myPopu.setAlpha(200);
		myPopu.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
		myPopu.showSoftInput(_activity);
	}

	private void dismiss(){
			if(null!=myPopu){
				myPopu.dismiss();
				myPopu=null;
			}
			this.onPopuClick.dismiss();
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
			String name=hospitalEdt.getText().toString().trim();
			if("".equals(name)){
				ToastUtils.showToast(ctx, "名字长度必须大于0");
			}else{
				this.onPopuClick.enter(name);
				dismiss();
			}
			break;
		default:
			break;
		}

	}

	public interface OnPopuClick {
		public void enter(String text);
		public void cancel();
		public void dismiss();
	}

}
