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
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.setting.MyEditorActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;

public class CheckCodeActivity extends BaseActivity implements OnClickListener {
	private Button btnNext;
	private EditText codeEdt;
	private String codeX = "";
	private String code = "";
	private String pwd = "";
	private String mobile = "";
	private static final int CODE_SUCESS = 0x20000;
	private static final int CODE_FAILE = 0x200001;
	private static final int GET_CODE_SUCESS = 0x20002;
	private static final int GET_CODE_FAILE = 0x200003;
	Tools tools;
	TextView timerTv;
	TextView sendTv;
	// 发送验证码，并重新计时
	private TimeCount time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkcode);
		tools = new Tools(mcontext, Constants.AC);
		intView();
	}

	private void intView() {
		pwd = getIntent().getStringExtra("pwd");
		codeX = getIntent().getStringExtra("code");
		mobile = getIntent().getStringExtra("mobile");

		ImageView tv_top_location = (ImageView) this
				.findViewById(R.id.tv_top_location);
		tv_top_location.setOnClickListener(this);

		btnNext = (Button) this.findViewById(R.id.btn_next);
		btnNext.setOnClickListener(this);
		codeEdt = (EditText) this.findViewById(R.id.codeEdt);

		timerTv = (TextView) this.findViewById(R.id.timer);
		sendTv = (TextView) this.findViewById(R.id.reSendTv);

		sendTv.setOnClickListener(this);
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象

		String isOpen = getIntent().getStringExtra("isOpen");
		if (isOpen.equals("1")) {
			timerTv.setText("您的验证码为:" + code);
			sendTv.setVisibility(View.GONE);
		} else {
			time.start();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			checkRequset();
			break;
		case R.id.tv_top_location:
			CheckCodeActivity.this.finish();
			break;
		case R.id.reSendTv:
			sendverificationinit();
			break;
		default:
			break;
		}

	}

	private void checkRequset() {
		code = codeEdt.getText().toString();
		if (AbStrUtil.isEmpty(code)) {
			ToastUtils.showToast(mcontext, "验证码不能为空");
			return;
		}else if(code.equals(codeX)){
			JSONObject job = new JSONObject();
			try {
				job.put("password", pwd);
				job.put("mobile", mobile);
				job.put("verify_code", code);
				Log.i("1111", job.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}

			MainBizImpl.getInstance().onComomRequest(myHandler,CheckCodeActivity.this, CODE_SUCESS,
					CODE_FAILE, job,"member/newAddUser");

		}else {
			ToastUtils.showToast(mcontext, "验证码错误");
			return;
		}
		
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
							setRgistResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case CODE_FAILE:
					ToastUtils.showToast(mcontext, "连接服务器失败");
					break;
				case GET_CODE_SUCESS:
					ToastUtils.showToast(mcontext, "验证码发送成功");
					break;
				case GET_CODE_FAILE:
					// 计时完毕时触发
					sendTv.setText("重新发送验证码");
					sendTv.setClickable(true);
					sendTv.setTextColor(getResources().getColor(R.color.black));
					// btnMsgCode.setBackgroundResource(R.drawable.img_btn_lsft);
					timerTv.setText("");
					break;
				default:
					break;
				}
			}
		}
	};

	protected void setRgistResult(JSONObject json) {
		Log.i("111111", json.toString());
		if (json.has("result_id")) {
			if (json.optString("result_id").equals("0")) {
				tools.putValue(Constants.AUTHENT, json.optString("authent"));
				if (json.has("member")) {
					JSONObject job = json.optJSONObject("member");
				}
				Intent it = new Intent(mcontext, MyEditorActivity.class);
				it.putExtra("mobile", mobile);
				startActivity(it);
				CheckCodeActivity.this.finish();
			} else {
				ToastUtils.showToast(mcontext, json.optString("result_msg"));

			}
		}

	}

	private void sendverificationinit() {
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {

			job.put("mobile", mobile);
			String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
					.parseStrToMd5L32(job.toString()) + "member/newVerifyCode");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("1111", WebUrl.CODE_URL);
		MainBizImpl.getInstance().onComomRequest(myHandler,CheckCodeActivity.this, CODE_SUCESS,
				CODE_FAILE, job,"member/newVerifyCode");

		time.start();// 开始计时

	}

	private void setCodeResult(JSONObject jsonObject) {
		if (jsonObject.optString("result_id").equals("0")) {
			Log.i("111111", jsonObject.toString());
		}
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {
			// 计时完毕时触发
			sendTv.setText("重新发送验证码");
			sendTv.setClickable(true);
			sendTv.setTextColor(getResources().getColor(R.color.black));
			// btnMsgCode.setBackgroundResource(R.drawable.img_btn_lsft);
			timerTv.setText("未收到验证码？");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// 计时过程显示
			sendTv.setClickable(false);
			sendTv.setTextColor(getResources().getColor(R.color.gray));
			String secondNum = getString(R.string.second_num);
			secondNum = String.format(secondNum, millisUntilFinished / 1000);
			timerTv.setText("(" + secondNum + ")");
		}
	}
}
