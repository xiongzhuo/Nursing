package com.deya.hospital.workcircle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.deya.hospital.dypdf.PdfPreviewActivity;
import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.LoadingView;


public class WebViewExpertDtail extends BaseActivity implements View.OnClickListener {

	private WebView webView;

	private ProgressDialog progDailog;
	CommentListAdapter adapter;
	String articalId = "";
	private LoadingView loadingView;
	private FrameLayout fl_webview;


	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.circle_tab_web);

		
//		progDailog = ProgressDialog.show(this, "加载中", "...", true);
//		progDailog.setCancelable(true);

		webView = (WebView) this.findViewById(R.id.webview);

		webView = new WebView(getApplicationContext());
		fl_webview = (FrameLayout) findViewById(R.id.fl_webview);
		fl_webview.addView(webView);
		
		loadingView = (LoadingView) this.findViewById(R.id.loadingView);
		url=getIntent().getStringExtra("url");
		articalId = url.substring(url.indexOf("=") + 1, url.length());
		InitTopView();
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Intent it = new Intent(WebViewExpertDtail.this, WebViewDtail.class);
				it.putExtra("url", url);
				if (url.contains("pdfid")) {
					it.putExtra("articleid", url.substring(url.indexOf("id=") + 3, url.indexOf("&pdfid=")));
					it.setClass(mcontext, PdfPreviewActivity.class);
				} else {
					it.setClass(mcontext, WebViewDtail.class);
				}
				startActivity(it);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, final String url) {
				
//				progDailog.dismiss();
				if (loadingView != null) {
					loadingView.stopAnimition();
					loadingView.setVisibility(View.GONE);
				}
			}
		});
	    WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                topView.setTitle(view.getTitle());
            }  
  
        };  
        // 设置setWebChromeClient对象  
        webView.setWebChromeClient(wvcc); 
		webView.clearCache(true);
		webView.loadUrl(url);
		loadingView.setLoadingListener(new LoadingView.LoadingStateInter() {

			@Override
			public void onloadingStart() {
				loadingView.setVisibility(View.VISIBLE);

			}

			@Override
			public void onloadingFinish() {
				loadingView.setVisibility(View.GONE);

			}
		});
		loadingView.setVisibility(View.VISIBLE);
		loadingView.startAnimition();
	}

	String title = "";
	String url = "";

	private CommonTopView topView;


	@Override
	protected void onStop() {
		if ( null != loadingView) {
			loadingView.stopAnimition();
		}
		super.onStop();
	}
	private void InitTopView() {
		title = getIntent().getStringExtra("title");
		topView = (CommonTopView) this.findViewById(R.id.topView);
		topView.setVisibility(View.VISIBLE);
		topView.init(this);
		topView.setTitle(title);
		topView.onbackClick(this, new OnClickListener() {

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


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}

	}

}
