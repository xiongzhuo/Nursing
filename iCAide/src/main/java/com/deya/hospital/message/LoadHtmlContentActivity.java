package com.deya.hospital.message;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.AbStrUtil;

public class LoadHtmlContentActivity extends BaseActivity {

	private WebView webView;

	Activity activity;
	private ProgressDialog progDailog;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_demo);

		activity = this;


		webView = (WebView) findViewById(R.id.webview_compontent);
		String url = getIntent().getStringExtra("html");
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
		
		String baseUrl    = "http://admin.gkgzj.com/";
		String encoding   = "UTF-8";
		String historyUrl = "";

	
		webView.getSettings().setDefaultFixedFontSize(16);
		webView.getSettings().setDefaultFontSize(16);
		String text="" ;
        try {  
//Return an AssetManager instance for your application's package  
            InputStream is = getAssets().open("template.html");  
            int size = is.available();  
  
            // Read the entire asset into a local byte buffer.  
            byte[] buffer = new byte[size];  
            is.read(buffer);  
            is.close();  
            text= new String(buffer, "utf-8");  
        } catch (IOException e) {  
            e.printStackTrace();
        }  
        
	if(!AbStrUtil.isEmpty(text)){
		 url=text.replace("__BODY__", url);
		
	}
	   
		
		webView.loadDataWithBaseURL(baseUrl, url, "file:///android_asset/", encoding, historyUrl);
		Log.i("1111111111url", url);

	}

	private void InitTopView() {
		String title=getIntent().getStringExtra("title");
		ImageView btn_back = (ImageView) findViewById(R.id.img_back);
		TextView titletTv = (TextView) this.findViewById(R.id.tv_title);
		titletTv.setText(title);
		btn_back.setOnClickListener(new View.OnClickListener() {

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
	}
}
