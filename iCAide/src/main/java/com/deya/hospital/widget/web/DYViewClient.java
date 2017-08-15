package com.deya.hospital.widget.web;


import com.deya.hospital.util.DebugUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class DYViewClient extends WebViewClient {
	public static String cid = "";
	private Context ctx;
	private OnClientListener back;

	public DYViewClient(Context paramContext) {
		this.ctx = paramContext;
	}
	

	@Override
	public void onPageFinished(WebView view, String url) {
		
		DebugUtil.debug("webClient", "onPageFinished url>>"+url);
		super.onPageFinished(view, url);

		view.setTag(BaseWebView.STATE_LOAD_SUCC);
		if (null != back) {
			back.Finish();
		}
		
		//ProgressUtils.dissProgress();
	}


	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		DebugUtil.debug("webClient", "onPageStarted");
		super.onPageStarted(view, url, favicon);
		view.setTag(BaseWebView.STATE_LOADING);
	
		if(null!=back){
		 
		  back.Loadding(); 
		  }
		 
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		DebugUtil.debug("webClient", "onReceivedError");
		super.onReceivedError(view, errorCode, description, failingUrl);
		try {
			Toast.makeText(this.ctx, "load page error��", 0).show();
			view.setTag(BaseWebView.STATE_LOAD_FAIL);
			if (null != back) {

				back.Error();
			}
			return;
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			// ProgressUtils.dissProgress();
		}
	}
	
	@Override
	public void onReceivedHttpAuthRequest(WebView view,
			HttpAuthHandler handler, String host, String realm) {
		DebugUtil.debug("webClient", "onReceivedHttpAuthRequest");
		super.onReceivedHttpAuthRequest(view, handler, host, realm);
	}
	
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
		DebugUtil.debug("webClient", "onReceivedSslError");
		super.onReceivedSslError(view, handler, error);
	}
	
	@Override
	public void onFormResubmission(WebView view, Message dontResend,
			Message resend) {
		DebugUtil.debug("webClient", "onFormResubmission");
		super.onFormResubmission(view, dontResend, resend);
	}
	
	
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		DebugUtil.debug("webClient", "shouldOverrideUrlLoading");
		//url.startsWith("http://") && 
		/*if(getRespStatus(url)==404) {   
            view.stopLoading();     
	    } else {   
	            view.loadUrl(url);   
	    }   */
	    return true;   
	}
	
	@Override
	public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
		DebugUtil.debug("webClient", "onUnhandledKeyEvent");
		super.onUnhandledKeyEvent(view, event);
	}




	public void setClientBack(OnClientListener back) {
		this.back = back;

	}

	public interface OnClientListener {
		public void Loadding();

		public void NoWifi();

		public void Error();

		public void Finish();
	}
}
