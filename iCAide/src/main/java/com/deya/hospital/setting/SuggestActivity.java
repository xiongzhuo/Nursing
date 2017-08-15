package com.deya.hospital.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.SoftarticleViewFlipper;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.TipsVo;
import com.example.calendar.CalendarMainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author sunp
 *
 */
public class SuggestActivity extends BaseActivity {
	private EditText sugesstEdt;
	private EditText tellEdt;
	Button btnSend;
	private RelativeLayout rlBack;
	private MyBrodcastReceiver brodcast;
	private SoftarticleViewFlipper articleviewFlipper;
	private LinearLayout llsoftarticle;
	private ImageView imgclosearticle;
	private List<TipsVo> tipsList = new ArrayList<TipsVo>();
	private Gson gson=new Gson();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggest);
		getTipsData();
		intTopView();
		initView();
		registerBroadcast();
	}

	private void intTopView() {
		TextView titleTv = (TextView) this.findViewById(R.id.title);
		titleTv.setText("意见反馈");
		rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
		rlBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void registerBroadcast() {
		brodcast = new MyBrodcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String tipState = tools.getValue(Constants.IS_CLOSE_TIPS);
				if (!AbStrUtil.isEmpty(tipState) && tipState.equals("0")) {
					disposeAdvertorial();
				} else {
					llsoftarticle.setVisibility(View.GONE);
				}

			}
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CalendarMainActivity.ISCLOSE_TIPS);
		registerReceiver(brodcast, intentFilter);
	}

	private void disposeAdvertorial() {
		if (tipsList.size() > 0) {
			llsoftarticle.setVisibility(View.VISIBLE);
			articleviewFlipper.setData(tipsList);
			imgclosearticle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					hideArticleAnimation();
				}
			});
		} else {
			llsoftarticle.setVisibility(View.GONE);
		}
	}

	// 关闭软文
	private void hideArticleAnimation() {
		AlphaAnimation alphaaniin = new AlphaAnimation(1.0f, 0.0f);
		alphaaniin.setDuration(600);
		llsoftarticle.startAnimation(alphaaniin);
		llsoftarticle.getAnimation().setAnimationListener(
				new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						llsoftarticle.setVisibility(View.GONE);
					}
				});
	}

	
	public void getTipsData() {
		String jsonStr = SharedPreferencesUtil.getString(mcontext,
				"tips_json", null);
		JSONObject job;
		if (!AbStrUtil.isEmpty(jsonStr)) {
			try {
				job = new JSONObject(jsonStr);
				JSONObject json = job.optJSONObject("tipsMsgHints");
				JSONArray jarr = json.optJSONArray("feedback");
				tipsList = gson.fromJson(jarr.toString(),
						new TypeToken<List<TipsVo>>() {
						}.getType());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void initView() {
		// 初始化软文消息
		llsoftarticle = (LinearLayout) this.findViewById(R.id.ll_softarticle);
		articleviewFlipper = (SoftarticleViewFlipper) this
				.findViewById(R.id.article_viewFlipper);
		imgclosearticle = (ImageView) this.findViewById(R.id.img_closearticle);
		String tipState = tools.getValue(Constants.IS_CLOSE_TIPS);
		if (!AbStrUtil.isEmpty(tipState) && tipState.equals("0")) {
			disposeAdvertorial();
		} else {
			llsoftarticle.setVisibility(View.GONE);
		}

		sugesstEdt = (EditText) this.findViewById(R.id.sugesstEdt);
		tellEdt = (EditText) this.findViewById(R.id.contectPhone);
		btnSend = (Button) this.findViewById(R.id.sumbmitBtn);
		btnSend.setEnabled(false);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendSuggest();
			}
		});
		sugesstEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0 && tellEdt.length() > 0) {
					btnSend.setEnabled(true);
				} else {
					btnSend.setEnabled(false);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		tellEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0 && sugesstEdt.length() > 0) {
					btnSend.setEnabled(true);
				} else {
					btnSend.setEnabled(false);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
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
					ToastUtils.showToast(mcontext, "亲您的网络不顺畅，请稍后重试！");
					break;
				default:
					break;
				}
			}
		}
	};

	// 提交資料
	public void sendSuggest() {
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("mobile", tellEdt.getText().toString());
			job.put("feedback", sugesstEdt.getText().toString());
			String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
					.parseStrToMd5L32(job.toString()) + "comm/submitFeedback");
			json.put("token", jobStr);
			json.put("msg_content", job.toString());
			Log.i("11111111", json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler, SuggestActivity.this,
				SEND_SUCESS, SEND_FAILE, job,"comm/submitFeedback");

	}

	private static final int SEND_SUCESS = 0x20011;
	private static final int SEND_FAILE = 0x20012;

	protected void setEditorRes(JSONObject jsonObject) {
		Log.i("111111", jsonObject.toString());
		if (jsonObject.optString("result_id").equals("0")) {
			finish();
		} else {

		}
		ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));

	}
}
