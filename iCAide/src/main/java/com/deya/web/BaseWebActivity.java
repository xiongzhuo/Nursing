package com.deya.web;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.EventManager;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/8/7
 */
public abstract class BaseWebActivity extends BaseActivity {
    public WebView webView;
    EventManager.OnNotifyListener notifyLis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initWebView();
        initData();

    }

    protected abstract void initData();


    protected abstract void initView();

    abstract  int getLayoutId();
    void initWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.addJavascriptInterface(new JsLoad(this,webView),"gk");

        webView.setWebChromeClient(new WebChromeClient());
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
                view.reload();
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                dismissdialog();
            }
        });
        webView.clearCache(true);


    }

    void  loadJS(String data){

    }
    @Override
    protected void onDestroy() {
        EventManager.getInstance().unRegisterListener(notifyLis);
        super.onDestroy();
    }

    private class JsLoad {
        Activity context;
        WebView Webview;
        JsLoad(Activity context, WebView Webview){
            this.context=context;
            this.Webview=webView;
        }
        @JavascriptInterface
        public void request(final String url, String data, final String suc, String fail, String comp) throws JSONException{
            ToastUtil.showMessage(data);
            Webview.post(new Runnable() {
                @Override
                public void run() {
                    String str="javascript:success('"+url+"')";
                    Webview.loadUrl(str);
                    ToastUtil.showMessage("返回过去了");
                }
            });




//            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "15307318537"));
//            startActivity(intent);


        }
    }
}
