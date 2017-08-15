package com.deya.hospital.workcircle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.deya.hospital.dypdf.PdfPreviewActivity;
import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.descover.QuestionSortActivity;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.widget.view.TextViewGroup;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CircleSearchActivity extends BaseActivity implements
		OnClickListener {
	private ImageView imgBack, imgClear;
	private TextView imgSearch;
	private EditText edtsearch;
	Gson gson = new Gson();
	// 分页
	Tools tools;
	String key = "";
	private LinearLayout networkView;
	List<TextViewGroup.TextViewGroupItem> reasonList = new ArrayList<TextViewGroup.TextViewGroupItem>();
	private WebView webView;
	String chanelId;
	String chanelName="";
	boolean isFirst=true;
	private FrameLayout fl_webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.circle_search_list_activity);
		tools = new Tools(mcontext, Constants.AC);
		key=getIntent().getStringExtra("key");
		chanelId=getIntent().getStringExtra("chanelId");
		chanelName=getIntent().getStringExtra("chanelName");
		initView();
	}

	public void checkNetWork() {
		networkView = (LinearLayout) this.findViewById(R.id.networkView);
		if (NetWorkUtils.isConnect(mcontext)) {
			networkView.setVisibility(View.GONE);
		} else {
			networkView.setVisibility(View.VISIBLE);
		}

	}

	private void initView() {
		imgSearch = (TextView) this.findViewById(R.id.searchImg);
		edtsearch = (EditText) this.findViewById(R.id.et_search);
		edtsearch.setHint("搜索"+(chanelId.equals("0")?"":chanelName));
		edtsearch.setText(key);
		imgClear = (ImageView) this.findViewById(R.id.clear);
		imgClear.setOnClickListener(this);
		imgSearch.setOnClickListener(this);

		edtsearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView view, int actionId,
										  KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					AbViewUtil.colseVirtualKeyboard(CircleSearchActivity.this);
					String text = edtsearch.getText().toString().trim();
					startSearch(text);
					return true;
				}
				return false;
			}
		});

		webView = new WebView(getApplicationContext());
		fl_webview = (FrameLayout)findViewById(R.id.fl_webview);
		fl_webview.addView(webView);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Intent it = new Intent();
				it.putExtra("url", url);
				if (url.contains("pdfid")) {
					it.putExtra("articleid", url.substring(url.indexOf("id=")+3,url.indexOf("&pdfid=")));
					it.setClass(CircleSearchActivity.this, PdfPreviewActivity.class);
				} else if(url.contains("question")){
					it.setClass(CircleSearchActivity.this, QuestionSortActivity.class);
				} else {
					it.setClass(CircleSearchActivity.this, WebViewDtail.class);
				}

				startActivity(it);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, final String url) {
				if(isFirst){
					isFirst = false;
					if (key.equals("10086")) {
						webView.loadUrl("javascript:setInitData("+ ")");
					} else {
						startSearch(key);
					}
				}
			}
		});
		webView.clearCache(true);
		webView.loadUrl(WebUrl.WEB_SEARCH);

	}

	public void startSearch(String text) {
		JSONObject job=new JSONObject();
		try {
			job.put("keyword", text);
			job.put("channel_id", chanelId);
			webView.clearCache(true);
			webView.loadUrl("javascript:setInitData("+ job.toString()  + ")");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.searchImg:
				finish();
				break;
			case R.id.clear:
				edtsearch.setText("");
				break;
			default:
				break;
		}

	}

	@Override
	protected void onDestroy() {
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




	/**
	 * colseVirtualKeyboard:【隐藏软键盘】. <br/>
	 * .@param activity.<br/>
	 */
	public static void closeVirtualKeyboard(Activity activity) {
		View view = activity.getWindow()
				.findViewById(Window.ID_ANDROID_CONTENT);
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

}
