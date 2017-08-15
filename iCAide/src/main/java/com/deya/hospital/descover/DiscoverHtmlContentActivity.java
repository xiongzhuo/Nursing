package com.deya.hospital.descover;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class DiscoverHtmlContentActivity extends BaseActivity {

	private WebView webView;

	Activity activity;
	private ProgressDialog progDailog;
	RelativeLayout share;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_demo);

		activity = this;


		webView = (WebView) findViewById(R.id.webview_compontent);
		String url = DoucmentListActivity.html;
		InitTopView();
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setDefaultFixedFontSize(16);
		webView.getSettings().setDefaultFontSize(16);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				showCaclebleDialog();
				view.loadUrl(url);

				return true;
			}

			@Override
			public void onPageFinished(WebView view, final String url) {
				dismissdialog();
			}
		});

		String baseUrl = "http://admin.gkgzj.com/";
		String encoding = "UTF-8";
		String historyUrl = "";

		webView.getSettings().setDefaultFixedFontSize(16);
		webView.getSettings().setDefaultFontSize(16);
		String text = "";
		try {
			// Return an AssetManager instance for your application's package
			InputStream is = getAssets().open("template.html");
			int size = is.available();

			// Read the entire asset into a local byte buffer.
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			text = new String(buffer, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!AbStrUtil.isEmpty(text)) {
			url = text.replace("__BODY__", url);

		}

		webView.loadDataWithBaseURL(baseUrl, url, "file:///android_asset/",
				encoding, historyUrl);

	}

	private void InitTopView() {
		String title = getIntent().getStringExtra("title");
		ImageView btn_back = (ImageView) findViewById(R.id.img_back);
		TextView titletTv = (TextView) this.findViewById(R.id.tv_title);
		titletTv.setText(title);
		share = (RelativeLayout) this.findViewById(R.id.share);
		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean bool = false;
				try {
					bool = webView.canGoBack();
					if (bool) {
						webView.goBack();
					} else {
						finish();
					}
				} catch (Exception localException) {
				}

			}
		});
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showShareDialog(getString(R.string.app_name),
						"点击下载并安装,感控医务工作者免费使用的移动应用工具",
						"http://studio.gkgzj.com");
			}
		});
	}


	protected static final int ADD_FAILE = 0x60089;
	protected static final int ADD_SUCESS = 0x60090;
	public void getAddScore(String id){
		tools=new Tools(mcontext, Constants.AC);
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("aid", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler,DiscoverHtmlContentActivity.this, ADD_SUCESS,
				ADD_FAILE, job,"goods/actionGetIntegral");
	}


	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
					case ADD_SUCESS:
						if (null != msg && null != msg.obj) {
							try {
								setAddRes(new JSONObject(msg.obj.toString()),activity);
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
	protected void setAddRes(JSONObject jsonObject,Activity activity) {
		if(jsonObject.optString("result_id").equals("0")){
			int score=jsonObject.optInt("integral");
			String str=tools.getValue(Constants.INTEGRAL);
			if(null!=str){
				tools.putValue(Constants.INTEGRAL, Integer.parseInt(str)+score+"");
			}else{
				tools.putValue(Constants.INTEGRAL,score+"");
			}
			if(score>0){
				if(null!=activity){
					showTipsDialog(score+"");
				}
			}

		}
	}

}
