package com.deya.hospital.widget.popu;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 填写邮箱弹框
 *
 * @author yung
 * @date 创建时间：2016年1月15日 上午11:52:33
 * @version 1.0
 */
public class PopCircleCommement implements OnClickListener {
	private MyPopu myPopu = null;

	private EditText edt_email_res;
	private TextView  btn_enter;
	private LinearLayout lay1;
	private OnPopuClick onPopuClick;
	private Context ctx;
	private String  questionId;
	boolean needCheck = false;

	public PopCircleCommement(Context ctx, Activity _activity,String questionId,
							  View view, OnPopuClick onPopuClick) {
		myPopu = new MyPopu(ctx, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, R.layout.popwindow_comment);
		this.ctx = ctx;
		this.onPopuClick = onPopuClick;
		this.questionId = questionId;
		json = new JSONObject();
		btn_enter = (TextView) myPopu.getTextView(R.id.submiText);
		btn_enter.setOnClickListener(this);

		lay1 = (LinearLayout) myPopu.getLinearLayout(R.id.lay1);
		lay1.setOnClickListener(this);

		edt_email_res = (EditText) myPopu.getEditText(R.id.commentEdt);

		myPopu.setAlpha(200);
		myPopu.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
		myPopu.showSoftInput(_activity);

		try {
			getCache();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void dismiss() {
		try {
			if (!AbStrUtil.isEmpty(edt_email_res.getText().toString())) {
				saveCache(edt_email_res.getText().toString());
			} else {
				saveCache("");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (null != myPopu) {
			myPopu.dismiss();
			myPopu = null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.lay1:
				dismiss();
				this.onPopuClick.cancel();
				break;
			case R.id.submiText:
				String email = edt_email_res.getText().toString();
				if (email.equals("")) {
					ToastUtils.showToast(this.ctx, "评论不能为空！");
					return;
				} else {

					this.onPopuClick.enter(email);
					dismiss();
				}
				break;
			default:
				break;
		}

	}
	JSONObject json = null;
	private void saveCache(String str) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("comment",str);

		json.put(questionId,jsonObject.toString());

		SharedPreferencesUtil.saveString(ctx, "commetChache", json.toString());
	}

	private void getCache() throws JSONException {
		String str = SharedPreferencesUtil.getString(ctx, "commetChache", "");
		if(AbStrUtil.isEmpty(str)){
			json=new JSONObject();
		}else{
			json = new JSONObject(str);
		}
		if (json.has(questionId)) {
			String chacheStr = json.optString(questionId);
			if (AbStrUtil.isEmpty(chacheStr)) {
				return;
			}
			JSONObject job = new JSONObject(chacheStr);
			if (!AbStrUtil.isEmpty(job.optString("comment"))) {
				edt_email_res.setText(job.optString("comment"));
				edt_email_res.setSelection(job.optString("comment").length());
			}
		}

	}

	public interface OnPopuClick {
		public void enter(String text);

		public void cancel();
	}

}