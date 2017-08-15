package com.deya.hospital.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.login.ChangePasswordActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountSecurityActivity extends BaseActivity implements
		OnClickListener {

	protected static final int CODE_SUCESS = 0x200013;
	protected static final int CODE_FAIL = 0200014;
	private RelativeLayout img_back;
	private RelativeLayout rel_qq;
	private RelativeLayout rel_email;
	TextView emailTv, usernameTv;
	private RelativeLayout rel_change_password;
	Tools tools;
	private TextView mobile_phone_number;
	EditText text_vsitor;
	Button btn_set_visitor;
	private CommonTopView topView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_security_activity);
		tools = new Tools(mcontext, Constants.AC);
		initView();
	}

	private void initView() {
		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);

		mobile_phone_number = (TextView) findViewById(R.id.mobile_phone_number);
		mobile_phone_number.setText(tools.getValue(Constants.MOBILE));
		rel_email = (RelativeLayout) this.findViewById(R.id.rel_email);
		rel_email.setOnClickListener(this);
		emailTv = (TextView) this.findViewById(R.id.email);
		rel_change_password = (RelativeLayout) this
				.findViewById(R.id.rel_change_password);
		usernameTv = (TextView) this.findViewById(R.id.usernameTv);
		rel_change_password.setOnClickListener(this);
		String email = tools.getValue(Constants.EMAIL);
		if (!AbStrUtil.isEmpty(email)) {
			emailTv.setText(tools.getValue(Constants.EMAIL));
		} else {
			emailTv.setText("设置邮箱");
		}

		text_vsitor = (EditText) this.findViewById(R.id.text_vsitor);
		btn_set_visitor = (Button) this.findViewById(R.id.btn_set_visitor);
		btn_set_visitor.setOnClickListener(this);
		String visitorCode = tools.getValue(Constants.VSITIRCODE);
		if (!AbStrUtil.isEmpty(visitorCode)) {
			text_vsitor.setText("已绑定邀请码    "+visitorCode);
			text_vsitor.setEnabled(false);
			btn_set_visitor.setVisibility(View.GONE);

		}
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		String userAccount=tools.getValue(Constants.USER_NAME);
		usernameTv.setText(tools.getValue(Constants.USER_NAME));
		if(AbStrUtil.isEmpty(userAccount)){
			usernameTv.setOnClickListener(this);
		}else{
			usernameTv.setOnClickListener(null);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rel_email:
			Intent it2 = new Intent(AccountSecurityActivity.this,
					ChangeEmailActivity.class);
			startActivityForResult(it2, 0x15);
			break;
		case R.id.rel_change_password:
			Intent it = new Intent(AccountSecurityActivity.this,
					ChangePasswordActivity.class);
			startActivity(it);
			break;
		case R.id.btn_set_visitor:
			sendVisitor();
			break;
		case R.id.usernameTv:
			Intent it3 = new Intent(AccountSecurityActivity.this,
					ChangeUsetAccountActivity.class);
			startActivityForResult(it3, 0x15);
			break;
		default:
			break;
		}

	}

	private void sendVisitor() {
		if (text_vsitor.getText().toString().length() <= 0) {
			ToastUtils.showToast(mcontext, "邀请码不能为空");
			return;
		}else if(text_vsitor.getText().toString().trim().equals(tools.getValue(Constants.MOBILE))){
			ToastUtils.showToast(mcontext, "邀请码不能是自己的手机号码哦！");
			return;
		}
		sendverificationinit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String email = tools.getValue(Constants.EMAIL);
		if (!AbStrUtil.isEmpty(email)) {
			emailTv.setText(tools.getValue(Constants.EMAIL));
		}

	}

	private void sendverificationinit() {
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {
			job.put("mobile", text_vsitor.getText().toString());
			job.put("authent", tools.getValue(Constants.AUTHENT));
			String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
					.parseStrToMd5L32(job.toString())
					+ "goods/recommendAddIntegral");
			json.put("token", jobStr);
			json.put("msg_content", job.toString());
			Log.i("11111111", json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				AccountSecurityActivity.this, CODE_SUCESS, CODE_FAIL,
				job,"goods/recommendAddIntegral");
	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case CODE_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							setCodeResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case CODE_FAIL:

					ToastUtils.showToast(mcontext, "");
					break;
				default:
					break;
				}
			}
		}
	};

	private void setCodeResult(JSONObject jsonObject) {
		Log.i("11111", jsonObject + "");
		if (jsonObject.optString("result_id").equals("0")) {
			ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
			text_vsitor.setEnabled(false);
			btn_set_visitor.setVisibility(View.GONE);
			tools.putValue(Constants.VSITIRCODE, text_vsitor.getText()
					.toString());
		} else {
			ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
		}
	}

}
