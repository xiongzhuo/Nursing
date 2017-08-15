package com.deya.hospital.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;

public class ChangePasswordActivity extends BaseActivity implements
		OnClickListener {
	private static final int CODE_SUCESS = 0x100;
	private static final int CODE_FAIL = 0x101;
	private static final int REGSTER_SUCESS = 0x102;
	private static final int REGSTER_FAIL = 0x103;
	private ImageView backImg;
	private EditText phoneHasRegister;
	private EditText edtMsgCode;
	private EditText edtNewPass;
	private EditText confirm_password;
	private Button btnMsgCode;
	private Button nextRetrievePassword;
	private String registerphone;
	private String vfcode = "";
	private String password;
	private String confirmPwd;
	private RelativeLayout bgImg;
	private TimeCount time;
	Tools tools;
	private CommonTopView topView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_findpassword);
		tools=new Tools(mcontext, Constants.AC);
		initView();
	}

	private void initView() {
		// bgImg = (RelativeLayout) this.findViewById(R.id.topImg);
		// int[] w 
		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
		btnMsgCode = (Button) findViewById(R.id.btn_msg_code);
		btnMsgCode.setOnClickListener(this);

		phoneHasRegister = (EditText) findViewById(R.id.userName);
		phoneHasRegister.setText(tools.getValue(Constants.MOBILE));
		phoneHasRegister.setEnabled(false);
		edtMsgCode = (EditText) findViewById(R.id.edt_msg_code);

		btnMsgCode = (Button) findViewById(R.id.btn_msg_code);
		btnMsgCode.setOnClickListener(this);

		if (getIntent().hasExtra("type") || getIntent().hasExtra("NoPassWord")) {
			phoneHasRegister.setVisibility(View.GONE);
		}
		nextRetrievePassword = (Button) findViewById(R.id.sumbmit);
		nextRetrievePassword.setOnClickListener(this);

		edtNewPass = (EditText) findViewById(R.id.password);
		confirm_password = (EditText) findViewById(R.id.confirm_password);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_msg_code:
			// 获得验证码
			// if (getIntent().hasExtra("type") ||
			// getIntent().hasExtra("NoPassWord")) {
			// //registerphone = tools.getValue(Constants.REGISTERMOBILE2);
			// } else {

			// }
			registerphone = phoneHasRegister.getText().toString().trim();
			if (registerphone.length() != 11) {
				ToastUtils.showToast(this, "手机号码只能为11位");
				break;
			} else {
				sendverificationinit();
				btnMsgCode.setClickable(false);
				btnMsgCode.setBackgroundResource(R.drawable.next_selector);
			}
			break;

		case R.id.sumbmit:
			registerphone = phoneHasRegister.getText().toString().trim();
			vfcode = edtMsgCode.getText().toString().trim();
			password = edtNewPass.getText().toString().trim();
			confirmPwd = confirm_password.getText().toString().trim();
			if (checkRegisterInfo()) {
				// nextRetrievePassword.setClickable(false);
				// nextRetrievePassword.setBackgroundResource(R.drawable.next_selector);
				// 请求服务器修改密码
				Register();
			}

			break;
		case R.id.agreement:
			Intent agreementIntent = new Intent(ChangePasswordActivity.this,
					AgreementActivity.class);
			startActivity(agreementIntent);
			break;
		default:
			break;
		}

	}

	private boolean checkRegisterInfo() {
		if (AbStrUtil.isEmpty(registerphone)) {
			ToastUtils.showToast(this, "请输入账号");
			return false;
		} else if (registerphone.length() != 11) {
			ToastUtils.showToast(this, "手机号码只能为11位");
			return false;
		} else if (AbStrUtil.isEmpty(vfcode)) {
			ToastUtils.showToast(this, "验证码不能为空");
			return false;
		} else if (4 != vfcode.length()) {
			ToastUtils.showToast(this, "验证码只能为4位");
			return false;
		} else if ((password.length() < 5) && confirmPwd.length() < 5) {
			ToastUtils.showToast(this, "密码长度5-20位");
			return false;
		} else {
			return true;
		}

		// else if (password.equals(confirmPwd)) {
		// return true;
		// } else {
		// ToastUtils.showToast(this, "两次密码输入不相同");
		// return false;
		// }

	}

	/**
	 * 请求服务器修改密码
	 * ***/
	private void Register() {
		JSONObject job = new JSONObject();
		try {
			job.put("mobile", registerphone);
			job.put("password", password);
			job.put("verify_code", vfcode);
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("1111", WebUrl.RESETPASSWORD);
		MainBizImpl.getInstance().onComomRequest(myHandler,ChangePasswordActivity.this, REGSTER_SUCESS,
				REGSTER_FAIL, job,"member/pwdmodify");

	}

	/**
	 * 请求服务器发送验证码
	 * */
	private void sendverificationinit() {
		JSONObject job = new JSONObject();
		try {
			job.put("mobile", registerphone);
			job.put("is_addUser","0");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("1111", WebUrl.CODE_URL);
		MainBizImpl.getInstance().onComomRequest(myHandler,ChangePasswordActivity.this, CODE_SUCESS,
				CODE_FAIL, job,"member/newVerifyCode");
		time.start();// 开始计时
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
					btnMsgCode.setText("发送验证码");
					btnMsgCode.setClickable(true);
					btnMsgCode.setBackgroundResource(R.drawable.round_blue_style);
					time.cancel();
					ToastUtils
							.showToast(ChangePasswordActivity.this, "发送验证码失败");
					break;
				case REGSTER_SUCESS:
					if (null != msg && null != msg.obj) {
						Log.i("QAQ", "" + msg.obj.toString());
						try {
							setRgisterResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case REGSTER_FAIL:
					ToastUtils.showToast(mcontext, "修改密码失败");
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
			ToastUtils.showToast(mcontext, "验证码发送成功");
		} else {
			time.cancel();
			btnMsgCode.setClickable(true);
			btnMsgCode.setText("获取验证码");
			btnMsgCode.setBackgroundResource(R.drawable.sharp_but);
			ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
		}
	}

	private void setRgisterResult(JSONObject jsonObject) {
		Log.i("11111", jsonObject + "");
		if (jsonObject.optString("result_id").equals("0")) {
			Intent toLoginIt = new Intent(ChangePasswordActivity.this,
					LoginActivity.class);
			startActivity(toLoginIt);
			finish();

		}
		ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {
			// 计时完毕时触发
			btnMsgCode.setText("发送验证码");
			btnMsgCode.setClickable(true);
			btnMsgCode.setBackgroundResource(R.drawable.sharp_but);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// 计时过程显示
			btnMsgCode.setClickable(false);
			btnMsgCode.setBackgroundResource(R.drawable.next_selector);
			String secondNum = getString(R.string.second_num);
			secondNum = String.format(secondNum, millisUntilFinished / 1000);
			btnMsgCode.setText(secondNum);
		}
	}

}
