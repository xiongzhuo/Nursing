package com.deya.web;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/8/7
 */
public  class CommonJs {
    Activity context;
    WebView Webview;
    CommonJs(Activity context, WebView Webview){
        this.context=context;
        this.Webview=Webview;
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
