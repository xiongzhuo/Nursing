package com.deya.hospital.setting;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.WebUrl;

public class NewbieHelpActivity extends BaseActivity {
	private CommonTopView topView;
	private WebView webView; 
	private LinearLayout networkView;
	
	class MyWebViewClient extends WebViewClient{
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) { 
			super.onPageStarted(view, url, favicon);
			checkNetWork();
			showprocessdialog();
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			NewbieHelpActivity.this.setTitle(view.getTitle());
			dismissdialog();
			
			if(view.canGoBack()){
				topView.showRightView(View.VISIBLE);
			} else {
				topView.showRightView(View.GONE);
			}
	    }
		
		@Override 
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		
		@Override
		public void onReceivedError(WebView view, int errorCode,
	            String description, String failingUrl) {
			Log.e("web", errorCode + " "+description);
			if(errorCode==ERROR_HOST_LOOKUP){
				networkView.setVisibility(View.VISIBLE);
			}
			dismissdialog();
	    }
	}	
	
	void checkNetWork(){
		if(NetWorkUtils.isConnect(mcontext)){
			networkView.setVisibility(View.GONE);
		}else{
			networkView.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newbie_help);
		initView();
	}
	
	void setTitle(String title){
		this.topView.setTitle(title);
	}
	
	@SuppressLint("SetJavaScriptEnabled") private void initView(){
		topView = (CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);
		topView.setRigtext("分享");
		
		topView.onbackClick(this, new OnClickListener(){
			@Override
			public void onClick(View v) {
				NewbieHelpActivity.this.goBack();
			}
		});
		topView.showRightView(View.GONE);
		
		networkView=(LinearLayout) this.findViewById(R.id.networkView);
		
		webView = (WebView) this.findViewById(R.id.webviewHelp);
		webView.setWebViewClient(new MyWebViewClient());
		// JavaScript使能(如果要加载的页面中有JS代码，则必须使能JS)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        
        
		webView.loadUrl(WebUrl.PUBLIC_SERVICE_URL+"/gkgzj-help/index.html");

		topView.onRightClick(this, new OnClickListener() {

			@Override
			public void onClick(View v) {
				showShareDialog("新手帮助",webView.getTitle(),webView.getUrl());
			}
		});
	}
	
	void goBack(){
		if(webView!=null){
			if(webView.canGoBack()){
				webView.goBack();
				return;
			}
		}
		this.finish();
	}

	@Override
	protected void onDestroy() {
		if(null!=webView){
			webView.clearCache(true);
			webView.removeAllViews();
			webView.destroy();
		}
		super.onDestroy();
	}
}
