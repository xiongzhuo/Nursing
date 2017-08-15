package com.deya.hospital.setting;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeUsetAccountActivity extends BaseActivity {
	EditText edtEmail;
	RelativeLayout backBtn;
	Tools tools;
	private CommonTopView topView;
	private ImageView cancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_email);
		tools = new Tools(mcontext, Constants.AC);
		initView();

	}

	private void initView() {

		topView = (CommonTopView) this.findViewById(R.id.topView);
		topView.init(this);
		topView.setTitle("修改用户名");
		topView.setTitleHint("请输入用户名");
		topView.onRightClick(this, new OnClickListener() {

			@Override
			public void onClick(View v) {
				String uname = edtEmail.getText().toString().trim();

				if (uname.equals("")) {
					ToastUtils.showToast(mcontext, "请输入用户名！");
					return;
				} else {
					boolean result = uname.matches("[0-9]+");
					if (result && uname.length() == 11) {
						ToastUtils.showToast(mcontext, "用户名不能为11位纯数字！");
					} else {
						sendEditor();
					}
				}

			}
		});
		edtEmail = (EditText) this.findViewById(R.id.departEdt);
		edtEmail.setHint("请输入您的邮箱");
		cancle = (ImageView) this.findViewById(R.id.cancle);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edtEmail.setText("");

			}
		});

	}

	protected void changeEmail() {

	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {

				case SEND_SUCESS:
					if (null != msg && null != msg.obj) {
						Log.i("1111", msg.obj + "");
						try {
							setEditorRes(new JSONObject(msg.obj.toString()));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				case SEND_FAILE:
					if (null != msg && null != msg.obj) {
						Log.i("1111", msg.obj + "");
						Log.i("11111111", msg.obj.toString());
					}
					break;
				default:
					break;
				}
			}
		}
	};

	// 提交資料
	public void sendEditor() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("username", edtEmail.getText().toString().trim());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				ChangeUsetAccountActivity.this, SEND_SUCESS, SEND_FAILE, job,
				"member/modifyUser");

	}

	private static final int SEND_SUCESS = 0x20006;
	private static final int SEND_FAILE = 0x20007;

	protected void setEditorRes(JSONObject jsonObject) {
		Log.i("111111", jsonObject.toString());
		if (jsonObject.optString("result_id").equals("0")) {
			tools.putValue(Constants.USER_NAME, edtEmail.getText().toString()
					.trim());
			finish();
		} else {
			ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
		}
		ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));

	}
}
