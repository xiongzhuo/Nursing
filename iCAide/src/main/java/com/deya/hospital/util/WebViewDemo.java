package com.deya.hospital.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class WebViewDemo extends BaseActivity {

	private WebView webView;

	Activity activity;
	private ProgressDialog progDailog;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_demo);

		activity = this;

		showprocessdialog();

		webView = (WebView) findViewById(R.id.webview_compontent);
		url = getIntent().getStringExtra("url");
		InitTopView();
		// 设置可以访问文件


		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				showprocessdialog();
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
		webView.clearCache(true);
		webView.loadUrl(url);
		Log.i("1111111111url", url);
		webView.setDownloadListener(new MyWebViewDownLoadListener());

	}

	String title = "";
	String url = "";

	private Tools tools;

	private CommonTopView topView;

	private void InitTopView() {
		title=getIntent().getStringExtra("title");
		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);
		topView.setTitle(title);
		topView.onbackClick(this, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean bool = false;
				try {
					bool = webView.canGoBack();
					if(bool){
						webView.goBack();
					}else{
						finish();
					}
				} catch (Exception localException) {
				}
				
			}
		});
		if(getIntent().hasExtra("type")){
			topView.setRigtext("分享");
			topView.onRightClick(this, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showShare();
					
				}
			});
		}
	
	}

	private void showShare() {
		SHARE_MEDIA[] shareMedia = { SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.WEIXIN_CIRCLE };
		// initCustomPlatforms(shareMedia);
		// showShareContent(shareMedia, shareTitle, shareContent, targetUrl);
		showShareDialog(getString(R.string.app_name), title, url);
	}

	protected static final int ADD_FAILE = 0x60089;
	protected static final int ADD_SUCESS = 0x60090;

	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
									String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}

	@Override
	protected void onDestroy() {
		webView.removeAllViews();
		webView.destroy();
		super.onDestroy();
	}
}
