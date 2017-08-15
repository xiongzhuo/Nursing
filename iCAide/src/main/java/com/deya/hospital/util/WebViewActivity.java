package com.deya.hospital.util;

import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.deya.hospital.base.BaseActivity;

public class WebViewActivity extends BaseActivity {
	public ProgressBar probar = null;
	public WebView webview = null;
	public LinearLayout probarLayout = null;

	public String currentUrl = null;

	protected void onResume() {
		super.onResume();
		this.webview.loadUrl(currentUrl);
	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		boolean bool = false;
		try {
			bool = this.webview.canGoBack();
		} catch (Exception localException) {
		}
		if ((paramInt == 4) && (bool)) {
			this.webview.goBack();
			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

}
