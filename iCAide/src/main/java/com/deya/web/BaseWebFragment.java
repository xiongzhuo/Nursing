package com.deya.web;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.deya.hospital.base.TabBaseFragment;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.EventManager;
import com.deya.hospital.util.Tools;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/8/15
 */
public abstract class BaseWebFragment extends TabBaseFragment{
    public WebView webView;
    EventManager.OnNotifyListener notifyLis;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {

        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container,
                    false);
            tools = new Tools(getActivity(), Constants.AC);
            initView();
            initWebView();
            initData();
            //   isSignUser();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }






    protected abstract void initData();


    protected abstract void initView();

    protected abstract  int getLayoutId();
    void initWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.addJavascriptInterface(new JsLoad(getActivity(),webView),"gk");
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
    public void onDestroy() {
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
    public void request(final String url, String data, final String suc, String fail, String comp) throws JSONException {
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
