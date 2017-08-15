package com.deya.web;

import com.deya.acaide.R;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/8/8
 */
public class StudymainWeb extends BaseWebActivity{
    @Override
    protected void initData() {
        webView.loadUrl("file:///android_asset/test2.html");

    }

    @Override
    protected void initView() {
        webView=findView(R.id.webview_compontent);
    }

    @Override
    int getLayoutId() {
        return R.layout.web_demo;
    }
}
