package com.deya.hospital.workspace.workspacemain;

import com.deya.acaide.R;
import com.deya.hospital.util.CommonTopView;
import com.deya.web.BaseWebFragment;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/8/15
 */
public class WorkWebFragment extends BaseWebFragment{
    CommonTopView topView;
    @Override
    protected void initData() {
        showprocessdialog();
        webView.loadUrl("http://172.16.30.47:8080/nursecompete?id=1");

    }

    @Override
    protected void initView() {
        topView=findView(R.id.topView);
        topView.init(getActivity());
        topView.setTitle("工作间");
        webView=findView(R.id.webview_compontent);
    }

    @Override
   public int getLayoutId() {
        return R.layout.web_demo;
    }
}
