package com.deya.hospital.workspace.stastic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.deya.hospital.widget.popu.PopuUnTimeReport;
import com.deya.hospital.widget.popu.PopuUnTimeReport.OnPopuClick;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

public class StasticWebView extends BaseActivity implements OnClickListener {
	// String actions[] = { "chart/fiveTimerReportCount",
	// "chart/fiveTimerReportByTimer",
	// "chart/fiveTimerReportByDepartment",
	// "chart/fiveTimerReportByPname", "chart/fiveTimerReportByPpost",
	// "chart/complyByDepartment", "chart/complyByPname",
	// "chart/complyByPpost" };
	String actions[] = { "taskCount/pageJobTypeCount",
			"taskCount/pageFiveYearCount", "taskCount/pageFiveTimerCount",
			"taskCount/pageFiveDepartmentCount",
			"taskCount/pageFiveMemberCount", "taskCount/pageFivePpostCount",
			"taskCount/pageFiveComplyByDepartment",
			"taskCount/pageFiveComplyByPname",
			"taskCount/pageFivePpostComplyCount" };

	String actions2[] = { "taskCount/pageJobTypeCount",
			"taskPerson/pageFiveYearByUid", "taskPerson/pageFiveTimerByUid",
			"taskPerson/pageFiveDepartmentByUid",
			"taskPerson/pageFiveMemberByUid", "taskPerson/pageFivePpostByUid",
			"taskPerson/pageFiveComplyDepartmentByUid",
			"taskPerson/pageFiveComplyPnameByUid",
			"taskPerson/pageFivePpostComplyByUid" };

	String menus[] ={"岗位-任务统计", "时机-任务统计", "指征-任务统计", "时机—科室依从", "时机-职员依从", "时机-岗位依从", "指征-科室依从",
			"指征-职员依从", "指征-岗位依从" };
	String types[] = { "20", "2", "3", "4", "5", "6", "7", "8", "9" };
	String types2[] = { "20", "10", "11", "12", "13", "14", "15", "16", "17" };
	private WebView webView;
	String url = "";
	Activity activity;
	private ProgressDialog progDailog;
	LinearLayout layout;
	Tools tools;
	boolean isShowing = false;
	TextView stastic;
	Button shareBtn, emailBtn;
    int position=0;
    CommonTopView topView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stastic_web_demo);

		activity = this;
		tools = new Tools(mcontext, Constants.AC);
		progDailog = ProgressDialog.show(activity, "正在加载", "努力加载中。。。", true);
		progDailog.setCancelable(true);

		webView = (WebView) findViewById(R.id.webview_compontent);
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.setWebChromeClient(new WebChromeClient());
		shareBtn = (Button) this.findViewById(R.id.shareBtn);
		shareBtn.setOnClickListener(this);
		emailBtn = (Button) this.findViewById(R.id.emailBtn);
		emailBtn.setOnClickListener(this);
		job = tools.getValue(Constants.JOB);
		position=getIntent().getIntExtra("psoition", 0);
		String isAdmin = tools.getValue(Constants.IS_ADMIN);
		if (AbStrUtil.isEmpty(isAdmin)) {
			isAdmin = "";
		}
		if(isAdmin.equals("1")){
			job="1";
		}
		
		url = getUrl(position+1);
		InitTopView(position);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				progDailog.show();
				view.loadUrl(url);

				return true;
			}

			@Override
			public void onPageFinished(WebView view, final String url) {
				progDailog.dismiss();
			}
		});
		Log.i("11111url", url);
		webView.loadUrl(url);

	}

	TextView titletTv;

	protected String job;

	private void InitTopView(int position) {
		topView=(CommonTopView) this.findViewById(R.id.topView);
		topView.setTitle(menus[position]);
		topView.init(this);
	}

	public int selectPosintion = 0;

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shareBtn:
			showShare();
			break;
		case R.id.emailBtn:
			sendEmails();
			break;
		default:
			break;
		}

	}

	PopuUnTimeReport dialog;
	String emailTx = "";

	public void sendEmails() {
		emailTx = tools.getValue(Constants.SEND_EMAILS);
		emailTx=(!AbStrUtil.isEmpty(emailTx))?emailTx:tools.getValue(Constants.EMAIL);
		dialog = new PopuUnTimeReport(mcontext, _activity, false, webView,
				emailTx, new OnPopuClick() {
					@Override
					public void enter(String text) {
						emailTx = text;
						sendEail(emailTx);
					}

					@Override
					public void cancel() {

					}
				});

	}

	protected static final int SEND_SUCESS = 0x6001;

	protected static final int SEN_FAILE = 0x6002;

	private void sendEail(String text) {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("email", text);
		job.put("report_type", (position+1)+"");
			Log.i("1111", job.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				StasticWebView.this, SEND_SUCESS, SEN_FAILE, job,
				"mail/emailSendReport");
	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case SEND_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							setSendResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case SEN_FAILE:
					ToastUtils.showToast(StasticWebView.this, "亲，您的网络不顺畅哦！");
					break;
				case ADD_SUCESS:
					if (null != msg && null != msg.obj) {
						Log.i("1111msg", msg.obj + "");
						try {
							setAddRes(new JSONObject(msg.obj.toString()),
									activity);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				case ADD_FAILE:
					Log.i("1111msg", msg.obj + "");
					// ToastUtils.showToast(getActivity(), "");
					break;
				default:
					break;
				}
			}
		}
	};

	protected void setSendResult(JSONObject jsonObject) {
		// if(jsonObject.optString("resul_id").equals("0")){
		//
		// }
		Log.i("1111111111", jsonObject.toString());
		tools.putValue(Constants.SEND_EMAILS, emailTx);
		ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
	}

	private void showShare() {
		SHARE_MEDIA[] shareMedia = { SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.WEIXIN_CIRCLE };
		// initCustomPlatforms(shareMedia);
		// showShareContent(shareMedia, shareTitle, shareContent, targetUrl);
		showShareDialog(menus[position], "点击查看统计数据", url);
	}

	public String getUrl(int index) {
		String s= Base64.encodeToString(tools.getValue(Constants.AUTHENT).getBytes(), Base64.NO_WRAP);
				
		s = s.split("=")[0]; // Remove any trailing '='s
		s = s.replace('+', '-'); // 62nd char of encoding
		s = s.replace('/', '_'); // 63rd char of encoding
		url = WebUrl.LEFT_URL + "/reportCounts/report?type=" + index + "&" + "authent="
				+s;
		return url;
	}

//	public String getUrl2(int index) {
//		url = WebUrl.LEFT_URL + "reportCounts/report?type=" + index + "?" + "authent="
//				+ Base64.decode(tools.getValue(Constants.AUTHENT), Base64.DEFAULT);
//		
//		http://192.168.199.125:8080/gkgzjsys/reportCounts/report?type=9&authent=1
//		return url;
//	}

	public void showMenu() {
		if (isShowing) {
			layout.setVisibility(View.GONE);
			isShowing = false;
		} else {
			layout.setVisibility(View.VISIBLE);
			isShowing = true;
		}
	}

	protected static final int ADD_FAILE = 0x60089;
	protected static final int ADD_SUCESS = 0x60090;

	public void getAddScore(String id) {
		Log.i("share_umeng", "111111111111111");
		tools = new Tools(mcontext, Constants.AC);
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("aid", id);
			Log.i("1111", job.toString());
			String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
					.parseStrToMd5L32(job.toString())
					+ "goods/actionGetIntegral");
			json.put("token", jobStr);
			json.put("msg_content", job.toString());
			Log.i("11111111", json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				StasticWebView.this, ADD_SUCESS, ADD_FAILE, job,
				"goods/actionGetIntegral");
	}

	protected void setAddRes(JSONObject jsonObject, Activity activity) {
		Log.i("share_umeng", "返回次数");
		Log.i("11111111", jsonObject.toString());
		if (jsonObject.optString("result_id").equals("0")) {
			int score = jsonObject.optInt("integral");
			String str = tools.getValue(Constants.INTEGRAL);
			if (null != str) {
				tools.putValue(Constants.INTEGRAL, Integer.parseInt(str)
						+ score + "");
			} else {
				tools.putValue(Constants.INTEGRAL, score + "");
			}
			if (score > 0) {
				if (null != activity) {
					showTipsDialog(score+"");
				}
			}

		}
	}

	@Override
	protected void onDestroy() {
		webView.clearCache(true);
		super.onDestroy();
	}
}
