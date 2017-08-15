package com.deya.hospital.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.ShowNetWorkImageActivity;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.UserInfoVo;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 实名认证
 */
public class QualificationActivity extends GaveAuthority implements
		OnClickListener {
	private static final int ACCEPTUSER =0x101;
	private String a_note="";
	private LinearLayout sumbmitlay;
	TextView authorName;

	@Override
	public int getLayoutId() {
		return R.layout.gave_authority_activity;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initOwerView();
		initData();
	}
	private void initOwerView() {
		refuseBtn.setVisibility(View.VISIBLE);
		sumbmitlay=(LinearLayout)this.findViewById(R.id.sumbmitlay);
		authorName= (TextView) this.findViewById(R.id.authorName);
		name.setEnabled(false);
		refusDialog = new RefusDialog(mcontext, new RefusDialog.RefuseInter() {
			@Override
			public void onItemSelect(int position, String text) {
				//a_note=(position==0?"非本院用户,直接删除":"资料不完善，或填写错误。");
				a_note=text;
				aCceptUser(6);
			}

			@Override
			public void onItemSelect(int postion) {

			}

			@Override
			public void onEnter() {

			}

			@Override
			public void onCancle() {

			}
		});

	}



	private void initData() {
		if(getIntent().hasExtra("data")){
			userInfo= (UserInfoVo) getIntent().getSerializableExtra("data");

		}
		if(null==userInfo){
			finish();
			return;
		}
		defultdeparTv.setText(userInfo.getDepartmentName());
		sex.setText(userInfo.getSex().equals("0")?"男":"女");

		if (!AbStrUtil.isEmpty(userInfo.getRegis_job())) {
			if (userInfo.getRegis_job().equals("1")) {
				supervision.setText("感控科主任");
			} else if (userInfo.getRegis_job().equals("2")) {
				supervision.setText("专职感控人员");
			} else if (userInfo.getRegis_job().equals("3")) {
				supervision.setText("兼职感控人员");
			} else if (userInfo.getRegis_job().equals("4")) {
				supervision.setText("其他");
			}
		}
		setHeadImg();
		name.setText(userInfo.getName());
		mobileTv.setText(userInfo.getMobile());
		tv_email.setText(userInfo.getEmail());

		if(userInfo.getState()==5){
			sumbmitlay.setVisibility(View.GONE);
			authorName.setVisibility(View.VISIBLE);
			authorName.setText("认证通过（认证人:"+userInfo.getAuth_name()+")");
		}else if(userInfo.getState()==6){
			sumbmitlay.setVisibility(View.GONE);
			authorName.setVisibility(View.VISIBLE);
			authorName.setText("认证失败（认证人:"+userInfo.getAuth_name()+")"+"\n"+
					userInfo.getA_note());
		}else {
			sumbmitlay.setVisibility(View.VISIBLE);
			authorName.setVisibility(View.GONE);
		}

	}


	private void setHeadImg() {
		if(AbStrUtil.isEmpty(userInfo.getCertify_photo())){
			avatar.setVisibility(View.GONE);
		}
		ImageLoader.getInstance().displayImage(
				WebUrl.FILE_LOAD_URL
						+ userInfo.getCertify_photo(),
				avatar, optionsSquare);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.refuseBtn:
				refusDialog.show();
				break;
			case R.id.accept_btn:
				aCceptUser(5);
				break;
			case R.id.avatar:
				Intent it = new Intent(mcontext, ShowNetWorkImageActivity.class);
				String [] strings ={WebUrl.FILE_LOAD_URL
						+ userInfo.getCertify_photo()};
				it.putExtra("urls", strings);
				it.putExtra("nowImage", 0);
				mcontext.startActivity(it);
				break;
		}

	}

	@Override
	public void onRequestSucesss(int code, JSONObject jsonObject) {
		super.onRequestSucesss(code, jsonObject);
		ToastUtils.showToast(mcontext,AbStrUtil.FailJsonMsg(jsonObject));
		finish();
	}

	@Override
	public void onRequestFail(int code) {
		super.onRequestFail(code);
		ToastUtil.showMessage("网络不顺畅，请稍后再试！");

	}


	/**
	 * 接受用户认证通过接口
	 */
	public void aCceptUser(int state) {
		showprocessdialog();
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("user_id", userInfo.getId());
			job.put("state",state);
			job.put("a_note",a_note);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomReq(this, this,
				ACCEPTUSER, job, "user/authUser");
	}
}
