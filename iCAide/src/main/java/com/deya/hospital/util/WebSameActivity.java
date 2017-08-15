package com.deya.hospital.util;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.deya.acaide.R;

public class WebSameActivity extends WebViewActivity {
	WebSameActivity mContext;
	String title;
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings_webview);
		
		mContext = this;

		String url = getIntent().getStringExtra("url");
		//title=getIntent().getStringExtra("title");
		InitTopView();
		this.probar = (ProgressBar) this
				.findViewById(R.id.webview_progressBar1);
		this.probarLayout = (LinearLayout) this
				.findViewById(R.id.webview_progressbar_layout);
		this.webview = (WebView) findViewById(R.id.webview_content);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setDefaultTextEncodingName("utf-8");
		webview.getSettings().setDomStorageEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.setInitialScale(100);
		webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		this.webview.setWebViewClient(new WebClient(this));
		this.currentUrl = url;
		this.webview.loadUrl(url);
	}
	

	private void InitTopView() {
		ImageView btn_back = (ImageView) findViewById(R.id.img_back);
		TextView titletTv=(TextView) this.findViewById(R.id.tv_title);
		titletTv.setText(title);
		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean bool = false;
				try {
					bool = webview.canGoBack();
					if(bool){
						webview.goBack();
					}else{
						finish();
					}
				} catch (Exception localException) {
				}
				
			}
		});
	}

}
