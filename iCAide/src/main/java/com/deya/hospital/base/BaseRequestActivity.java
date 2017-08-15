package com.deya.hospital.base;

import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.ToastUtils;

import org.json.JSONObject;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 * @author : sunp
 * @date ${date}
 */
public abstract class BaseRequestActivity extends BaseActivity implements RequestInterface {

    @Override
    public abstract void onRequestSucesss(int code, JSONObject jsonObject);

    @Override
    public   void onRequestErro(String message){
        dismissdialog();
        ToastUtils.showToast(mcontext,message);
    }

    @Override
    public abstract  void onRequestFail(int code);
}
