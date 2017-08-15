package com.deya.hospital.util;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebClient extends WebViewClient {
	WebViewActivity ov;

	public WebClient(WebViewActivity con) {
		this.ov = con;
	}

	public void onReceivedError(WebView paramWebView, int paramInt,
			String paramString1, String paramString2) {
		try {
			paramWebView.setScrollBarStyle(0);
			paramWebView
					.loadDataWithBaseURL(
							"",
							"<html><body bgcolor=\"#FFFFFF\" align=\"center\"><br/><font color=\"#000000\">抱歉！当前无法联网，请检查网络设置</font>"
									+ "<br/></body></html>", "text/html",
							"utf-8", "");

		} catch (Exception localException) {
		}
		super.onReceivedError(paramWebView, paramInt, paramString1,
				paramString2);
	}

	public void onPageStarted(WebView paramWebView, String paramString,
			Bitmap paramBitmap) {
		ov.probar.setVisibility(View.VISIBLE);
		// ov.titleText.setVisibility(View.VISIBLE);
		ov.probarLayout.setVisibility(View.VISIBLE);
		ov.webview.setVisibility(View.GONE);

		super.onPageStarted(paramWebView, paramString, paramBitmap);
	}

	public void onPageFinished(WebView paramWebView, String paramString) {
		ov.probar.setVisibility(View.GONE);
		// ov.titleText.setVisibility(View.GONE);
		ov.probarLayout.setVisibility(View.GONE);
		ov.webview.setVisibility(View.VISIBLE);
		super.onPageFinished(paramWebView, paramString);
	}

	public boolean shouldOverrideUrlLoading(WebView paramWebView,
			String paramString) {
		paramWebView.loadUrl(paramString);
		return true;
	}
}
