package com.deya.hospital.widget.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

import com.deya.acaide.R;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.widget.web.DYViewClient.OnClientListener;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class DYWebView extends RelativeLayout {
	public static final int HANDLER_NETERRO = -2;
	public static final int HANDLER_WEBERRO = -1;
	public static final int HANDLER_WEBOK = 0;
	public static final int HANDLER_TITLE = 888;
	public static final int HANDLER_FINISH = 999;

	/*
	 * public static final int HANDLER_VILNAME = 1; public static final int
	 * HANDLER_VIL = 2; public static final int HANDLER_MYVIL = 3;
	 * 
	 * public static final int HANDLER_NEWS = 4; public static final int
	 * HANDLER_VIL_FV = 5;
	 */

	private Context context;
	private BaseWebView currentWebView;
	private DYViewClient webViewClient;
	private OnWebListener webListener;

	public static final int STATUS_LOADING = 0;
	public static final int STATUS_LOADSUCCESS = 1;
	public static final int STATUS_LOADERRO = 2;
	public int loadstatus = -1;
	private int index = 0;

	private Handler myHandler;
	private static final int HANDLER_TEXTCHANG = 101;
	private Resources res;

	public void setResources(Resources res) {
		this.res = res;

	}

	public DYWebView(Context context) {
		super(context);
		init(context);
	}

	public DYWebView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		init(context);

	}

	private View view;
	private BaseWebView myWebView;

	private BaseWebView newWebView() {
		view = LayoutInflater.from(context).inflate(R.layout.base_web, null);

		myWebView = (BaseWebView) view.findViewById(R.id.web);
		myWebView.setWebViewClient(this.webViewClient);
		myWebView.setWebChromeClient(new MyWebChromeClient());

		myWebView.setClickable(true);
		// myWebView.setOnTouchListener(this);
		
		myWebView.addJavascriptInterface(this, "nxt");

		
		addView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		return myWebView;
	}

	// js >> nxt.WebMessage('message');
	public void WebMessage(String message) {
		DebugUtil.debug("message", message);
		/*JSONObject jsonObj = new JSONObject(message);
		Object ot = jsonObj.get("t");
		Object om = jsonObj.get("m");*/
		

	}

	class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url,
				final String message, JsResult result) {
			/*
			 * if(message.equals("123")){ Toast.makeText(context,
			 * "我是 测试  22222"+message, 1000).show(); }
			 */
			DebugUtil.debug("alert message", message);
			if (null != message && message.contains("'t'")) {
				WebMessage(message);
			}
			result.cancel();
			return true;
		}
		
		
		
		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {
			// result.cancel();
			return true;
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				currentWebView.progressbar.setVisibility(GONE);
			//	sendmessage();
			} else {
				if (currentWebView.progressbar.getVisibility() == GONE)
					currentWebView.progressbar.setVisibility(VISIBLE);
				currentWebView.progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			DebugUtil.debug("title", title);
			if(null!=title&&title!=""){
				String[] tt=title.split(",");
				int len=tt.length;
				if(len>0){
				}
				if(len>1){
					try {
					} catch (NumberFormatException e) {
						// TODO: handle exception
					}
				}
				if(len>2){
					try {
					} catch (NumberFormatException e) {
						// TODO: handle exception
					}
				}
				if(len>3){
					if (null != myHandler) {
						if(!"".equals(tt[3])){
						Message msg = new Message();
						msg.what = HANDLER_TITLE;
						msg.obj = tt[3];
						myHandler.sendMessage(msg);
						}
					}
				}
				if(len>4){
					try {
					} catch (NumberFormatException e) {
						// TODO: handle exception
					}
				}
				if(len>5){
					
				}
				
			}
			
			super.onReceivedTitle(view, title);
		}
		
		
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType) {
			String t=acceptType;
			DebugUtil.debug("jsmsg", "acceptType 3+>" + acceptType);
		/*	if (mUploadMessage == null)
			{
				
			}*/
			mUploadMessage = uploadMsg;
			if (null != webListener){
				
				webListener.openFile(mUploadMessage);
			}
		
			
			
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			DebugUtil.debug("jsmsg", "acceptType 3<" );
			openFileChooser(uploadMsg, "");
		}

		
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			DebugUtil.debug("jsmsg", "acceptType 4.1.1+>" + acceptType);
            openFileChooser(uploadMsg, acceptType);
		}

	}
	ValueCallback<Uri> mUploadMessage;
	
	
	
	
	public void loadJs(String method, Object[] obj) {
		String uri = "javascript:" + method + "(";
		if (null != obj && obj.length > 0) {
			String p = "";
			for (int i = 0; i < obj.length; i++) {
				p = p + ",'" + obj[i] + "'";
			}
			if (p.length() > 1) {
				p = p.substring(1);
				uri = uri + p;
			}

		}
		uri = uri + ")";
		DebugUtil.debug("jsurl", uri);
		loadUrlBase(uri);
	}

	private IWeb webback;

	public void setWebMessageBack(IWeb webback) {
		this.webback = webback;
	}

	public interface IWeb {
		public void execDateMessage(String msg);

		public void execTimeMessage(String msg);
	}

	public boolean canBack() {
		return ((getVisibility() == View.VISIBLE)
				&& (this.currentWebView != null) && (this.currentWebView
					.canGoBack()));

	}

	public String getUrl() {
		String str = "";
		if (this.currentWebView != null)
			str = this.currentWebView.getUrl();
		return str;
	}

	public void goBack() {
		
		
	}

	public void hide() {
		setVisibility(View.GONE);
	}

	public void SetHandler(Handler myHandler) {
		this.myHandler = myHandler;
	}

	public void init(Context context) {
		this.context = context;

		// setClickable(true);
		this.webViewClient = new DYViewClient(context);
		this.webViewClient.setClientBack(new OnClientListener() {

			@Override
			public void Loadding() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void NoWifi() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void Error() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void Finish() {
				// TODO Auto-generated method stub
				
			}

			
		});
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				if (null != myHandler)
					myHandler.sendEmptyMessage(HANDLER_WEBOK);
				load(url, refresh);
			} else {
				// 无效的
				loadstatus = STATUS_LOADERRO;
				if (null != myHandler)
					myHandler.sendEmptyMessage(HANDLER_WEBERRO);
			}
		}
	};
	
	

	public void loadUrl(String url) {
		loadstatus = STATUS_LOADING;
		loadUrl(url, false);
	}

	private void getRespStatus(final String url) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				int status = -1;
				try {
					HttpHead head = new HttpHead(url);
					HttpClient client = new DefaultHttpClient();
					HttpResponse resp = client.execute(head);
					status = resp.getStatusLine().getStatusCode();
					DebugUtil.debug("webClient", "status >>" + status);

				} catch (Exception e) {
					DebugUtil.debug("webClient", "e >>" + e.getMessage());
				}
				handler.sendEmptyMessage(status);
			}
		}).start();

	}

	private String url = "";
	private boolean refresh;

	public void loadUrl(String url, boolean refresh) {
		
		DebugUtil.debug("loadurl", "url>"+url);
		// progressBar.setVisibility(View.VISIBLE);
		url = url.replaceAll(" ", "");
		if (isNetworkConnected(this.context)) {
			if (url.startsWith("http://")) {
				this.url = url;
				this.refresh = refresh;
				getRespStatus(url);
			} else {
				load(url, refresh);
			}

			// currentWebView.reload();
		} else {

			if (null != myHandler) {
				myHandler.sendEmptyMessage(HANDLER_NETERRO);
			}
		}

		/*
		 * else { if (null != currentWebView)
		 * currentWebView.setVisibility(View.GONE); //
		 * adView.setVisibility(View.VISIBLE); // adView.setOffline(); }
		 */

	}

	private void load(String url, boolean refresh) {
		loadstatus = STATUS_LOADING;

		if (refresh) {
			DebugUtil.debug("webshow", "refresh");
			if (this.currentWebView != null) {
				DebugUtil.debug("webshow", "clearHistory");
				currentWebView.removeAllViews();
				currentWebView.clearHistory();
				currentWebView = null;
				this.removeAllViews();
			}

		}

		if (this.currentWebView == null) {

			DebugUtil.debug("webshow", "new");
			this.currentWebView = newWebView();

		} else {
			DebugUtil.debug("webshow", "stopLoading");
			this.currentWebView.stopLoading();
		}
		// this.currentWebView.setVisibility(View.VISIBLE);
		// this.currentWebView.setVisibility(View.GONE);
		loadUrlBase(url);
	}

	public void loadUrlAndRefresh(String url) {

		loadstatus = STATUS_LOADING;
		loadUrl(url, true);
	}

	public void loadUrlBase(String url) {
		// url="javascript:test('aa')"
		if (null != this.currentWebView) {
			this.currentWebView.loadUrl(url);
		}
	}

	public void refreshWebView() {

		if (this.currentWebView == null)
			return;
		this.currentWebView.loadUrl(this.currentWebView.getOriginalUrl());
	}

	public void setVisibility(int paramInt) {
		super.setVisibility(paramInt);

	}

	public boolean IsLoadSuccess() {
		return (null != this.currentWebView && this.currentWebView
				.getVisibility() == View.VISIBLE);
	}

	public void show() {
		setVisibility(View.VISIBLE);
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public void setOnWebListener(OnWebListener webListener) {
		this.webListener = webListener;
	}

	public interface OnWebListener {
		public void OnClikWebListener();

		public void OnTouchWebListener();

		public void OnIndexListener(int index);
		
		public void openFile(ValueCallback<Uri> mUploadMessage);
	}

}
