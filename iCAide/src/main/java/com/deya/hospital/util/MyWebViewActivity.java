package com.deya.hospital.util;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.deya.acaide.R;

/**
 * Demo描述: 利用WebView加载网络PDF资源,并且实现下载
 * 步骤:
 * 1 利用谷歌服务得到解析后的pdf,且在Webview中显示
 * 2 实现Webview的下载监听.
 *  即mWebView.setDownloadListener()实现下载
 *  
 * 备注:
 * 测试时最好链接VPN
 */
public class MyWebViewActivity extends Activity {
	private WebView mWebView;
	private Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_webview);
		init();
	}

	private void init() {
		mWebView = (WebView) findViewById(R.id.webview_content);
		loadPDF();
	}

	private void loadPDF() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);
		//mWebView.getSettings().setPluginsEnabled(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.requestFocus();
		mWebView.getSettings().setLoadWithOverviewMode(true);
		String pdfUrl = getIntent().getStringExtra("url");
		Log.i("11111", pdfUrl);
		mWebView.loadUrl(pdfUrl);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);

			}

		});

		mWebView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,long contentLength) {
				 System.out.println("=========>开始下载 url =" + url);
				 Uri uri = Uri.parse(url);   
                 Intent intent = new Intent(Intent.ACTION_VIEW, uri);   
                 startActivity(intent);
			}
		});

	}

}