package com.deya.hospital.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.widget.popu.PopuUnTimeReport;
import com.deya.hospital.widget.popu.TipsDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

public class ReportWebViewDemo extends BaseActivity implements RequestInterface{

	protected static final int SEND_SUCESS = 0x6001;

	protected static final int SEN_FAILE = 0x6002;

	private WebView webView;

	private ProgressDialog progDailog;
	String jsonStr = "";
	private String task_url = "";
	Button shareBtn;
	LinearLayout bottomView;
	Tools tools;
	String feedback = "";
	TextView editorTask;
	TaskVo data;
	private FrameLayout fl_webview;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_web);
		getCacheData();
		registerBroadcast();
		baseUrl2 = MyAppliaction.getReportPath() + "web/report.html";
		baseUrl3 = "file://" + MyAppliaction.getReportPath() + "web/";

		if (getIntent().hasExtra("task_url")) {
			task_url = getIntent().getStringExtra("task_url");
			data=(TaskVo) getIntent().getSerializableExtra("data");
		} else {
			jsonStr = getIntent().getStringExtra("json");
		}

		showprocessdialog();

//		webView = (WebView) findViewById(R.id.webview_compontent);

		webView = new WebView(getApplicationContext());
		fl_webview = (FrameLayout) findViewById(R.id.fl_webview);
		fl_webview.addView(webView);


		if (getIntent().hasExtra("url")) {
			url = getIntent().getStringExtra("url");
			feedback = getIntent().getStringExtra("feedback");
		}
		InitTopView();
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBlockNetworkImage(false);
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
			showUncacleBleProcessdialog();
				view.loadUrl(url);

				return true;
			}
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
				view.loadUrl(url);
			}
			@Override
			public void onPageFinished(WebView view, final String url) {
			dismissdialog();
			}
		});
		if (!AbStrUtil.isEmpty(task_url)) {
			url = WebUrl.LEFT_URL + "/taskCount/fiveDetailCount?task_id="
					+ data.getTask_id();
			webView.clearCache(true);
			webView.loadUrl(url);
		} else {
			// getSdFile(baseUrl3,true);
			getSdFile("", false);
		}

		// webView.loadUrl("file:///android_asset/report.html");
		 Log.i("1111111111url", url);

	}

	String baseUrl2 = "";
	String baseUrl3 = "";

	public void getSdFile(String baseURL, boolean isSdk) {

		String encoding = "UTF-8";
		String historyUrl = "";

		webView.getSettings().setDefaultFixedFontSize(16);
		webView.getSettings().setDefaultFontSize(16);
		String text = "";
		try {
			// Return an AssetManager instance for your application's package
			InputStream is = null;

			if (isSdk) {
				File file = new File(baseUrl2);
				if (file.exists()) {
					is = new FileInputStream(baseUrl2);
				}
			} else {
				is = getAssets().open("report.html");
			}
			int size = 0;
			if (null != is)
				size = is.available();
			if (size < 100) {
				getSdFile(baseURL, false);
				return;
			}
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			text = new String(buffer, "utf-8");
			if (!AbStrUtil.isEmpty(text)) {

				if (!"".equals(task_url)) {

					// 此方法速度快但是无法显示语音跟图片…………
					// url = text.replace("__BODY__",
					// "<script type=\"text/javascript\" src=\"" + task_url
					// + "\"></script>");

					url = WebUrl.LEFT_URL
							+ "/taskCount/fiveDetailCount?task_id=" + task_id;
					webView.loadUrl(url);
				} else {
					url = text.replace("__BODY__",
							"<script type=\"text/javascript\">window.ReportData ="
									+ jsonStr + ";" + pposts
									+ "window.Feedbacks=" + feedback
									+ "</script>");
					if (isSdk) {
						webView.loadDataWithBaseURL(baseURL, url, "text/html",
								encoding, historyUrl);
					} else {
						webView.loadDataWithBaseURL("file:///android_asset/",
								url, "text/html", encoding, historyUrl);
					}

				}

			} else {

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setFile(String text) {

	}

	@Override
	protected void onDestroy() {
		webView.clearCache(true);
		if(null!=tipdialog){
			tipdialog.dismiss();
		}
		if(null!=brodcast){
			ReportWebViewDemo.this.unregisterReceiver(brodcast);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			dealJavascriptLeak();
		}
		if (webView != null) {
			((ViewGroup) webView.getParent()).removeView(webView);
			webView.destroy();
			webView = null;
		}
		super.onDestroy();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void dealJavascriptLeak() {
		webView.removeJavascriptInterface("searchBoxJavaBridge_");
		webView.removeJavascriptInterface("accessibility");
		webView.removeJavascriptInterface("accessibilityTraversal");
	}


	HashMap<String, String> map = new HashMap<String, String>();
	JSONObject popJson = new JSONObject();

	private String popstr = "";

	public void getCacheData() {
		// type1 位督导岗位 type4为职称 type3为职位
		String jsonStr = SharedPreferencesUtil.getString(mcontext,
				"jobinfolist", null);
		if (!TextUtils.isEmpty(jsonStr)) {
			try {
				JSONObject jsonObject = new JSONObject(jsonStr);
				JSONArray jarr2 = jsonObject.optJSONArray("jobType2");
				for (int i = 0; i < jarr2.length(); i++) {
					JSONObject job = (JSONObject) jarr2.get(i);
					map.put(job.optString("id"), job.optString("name"));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Set<String> keys = map.keySet();
		int i = 0;
		for (String key : keys) {
			// try {
			// popJson.put(key, map.get(key));
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			if (i == map.size() - 1) {

				popstr += key + ":'" + map.get(key) + "'";
			} else {
				popstr += key + ":'" + map.get(key) + "',";
			}
			i++;
		}

		popstr = "window.pposts ={" + popstr + ",0:'其他'}";
	}

	String title = "";
	String url = "";
	String task_id = "";

	String pposts = "window.pposts ={5:'护理',6:'医疗',7:'医技',8:'行政',9:'后勤',10:'科研',45:'工勤',46:'实习生',11:'其他',0:'其他'};";

	private CommonTopView topView;

	private void InitTopView() {
		title = getIntent().getStringExtra("title");
		tools = new Tools(mcontext, Constants.AC);
		bottomView = (LinearLayout) this.findViewById(R.id.bottomView);
		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);
		topView.setTitle(title);
		if(getIntent().hasExtra("task_url")){
			topView.setVisibility(View.VISIBLE);
		}

//		topView.setRigtext("编辑");
//		topView.onRightClick(this, new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent =new Intent(mcontext, HandWashTasksActivity.class);
//				intent.putExtra("data", data);
//				if (!AbStrUtil.isEmpty(data.getTask_type())) {
//					intent.putExtra("type", data.getTask_type());
//				} else {
//					intent.putExtra("type", "");
//				}
//				finish();
//				startActivity(intent);
//
//			}
//		});

		Button emailBtn = (Button) this.findViewById(R.id.emailBtn);
		shareBtn = (Button) this.findViewById(R.id.shareBtn);

		if (getIntent().hasExtra("showbottom")) {
			bottomView.setVisibility(View.VISIBLE);
			task_id = getIntent().getStringExtra("task_id");
			emailBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					sendEmails();

				}
			});

			shareBtn = (Button) this.findViewById(R.id.shareBtn);
			shareBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					showShare();

				}
			});
		} else {
			bottomView.setVisibility(View.VISIBLE);
			shareBtn = (Button) this.findViewById(R.id.shareBtn);
			shareBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(mcontext, "请到督导首页选择上传后的督导进行分享", Toast.LENGTH_LONG).show();
				}
			});
			emailBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(mcontext, "请到督导首页选择上传后的督导进行发送", Toast.LENGTH_LONG).show();

				}
			});
		}

		if (getIntent().hasExtra("type")) {
			RelativeLayout shareImg = (RelativeLayout) this.findViewById(R.id.share);
			shareImg.setVisibility(View.VISIBLE);
			shareImg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showShare();
				}
			});
		}

	}

	private void showShare() {
		showShareDialog("手卫生统计",data==null?"":data.getDepartmentName(),WebUrl.LEFT_URL + "/taskCount/fiveDetailCount?task_id="
				+ task_id);
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
						ToastUtils.showToast(ReportWebViewDemo.this, "亲，您的网络不顺畅哦！");
						break;
					case ADD_SUCESS:
						if (null != msg && null != msg.obj) {
							try {
								setAddRes(new JSONObject(msg.obj.toString()),
										activity);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						break;
					case ADD_FAILE:
						// ToastUtils.showToast(getActivity(), "");
						break;
					default:
						break;
				}
			}
		}
	};

	private void sendEail(String text) {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("task_id", task_id);
			job.put("email", text);
			job.put("inter_type", "1");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler, ReportWebViewDemo.this,
				SEND_SUCESS, SEN_FAILE, job,"mail/emailSend");
	}

	protected void setSendResult(JSONObject jsonObject) {
		// if(jsonObject.optString("resul_id").equals("0")){
		//
		// }
		tools.putValue(Constants.SEND_EMAILS, emailTx);
		ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
	}

	PopuUnTimeReport dialog;
	String emailTx = "";

	private MyBrodcastReceiver brodcast;

	public void sendEmails() {
		emailTx = tools.getValue(Constants.SEND_EMAILS);
		emailTx=(!AbStrUtil.isEmpty(emailTx))?emailTx:tools.getValue(Constants.EMAIL);
		dialog = new PopuUnTimeReport(mcontext, _activity, false, webView,
				emailTx, new PopuUnTimeReport.OnPopuClick() {
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

	protected static final int ADD_FAILE = 0x60089;
	protected static final int ADD_SUCESS = 0x60090;

	public void getAddScore(String id) {
		tools = new Tools(mcontext, Constants.AC);
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("aid", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				ReportWebViewDemo.this, ADD_SUCESS, ADD_FAILE, job, "goods/actionGetIntegral");
	}

	protected void setAddRes(JSONObject jsonObject, Activity activity) {
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
					tipdialog = new TipsDialog(mcontext, score + "");
					tipdialog.show();
				}
			}

		}
	}
	Dialog tipdialog;
	public static final String CLOSE_ACTIVITY = "close";
	private void registerBroadcast() {
		brodcast = new MyBrodcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(CLOSE_ACTIVITY)) {
					finish();
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CLOSE_ACTIVITY);
		registerReceiver(brodcast, intentFilter);
	}

	@Override
	public void onRequestSucesss(int code, JSONObject jsonObject) {

	}

	@Override
	public void onRequestErro(String message) {

	}

	@Override
	public void onRequestFail(int code) {

	}
}
