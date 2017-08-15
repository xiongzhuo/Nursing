package com.deya.hospital.account;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseCommonTopActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.CommonUtils;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.GsonUtils;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/7/3
 */
public class SignAcitivity extends BaseCommonTopActivity implements View.OnClickListener{
    EditText phoneTxt;
    Button contectUs,confirm;
    RequestInterface requestInterface;
    @Override
    public String getTopTitle() {
        return "申请预约";
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_sign;
    }

    @Override
    public void initBaseData() {

    }

    @Override
    public void initView() {
        phoneTxt=findView(R.id.phoneTxt);
        phoneTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    confirm.setEnabled(true);
                }else{
                    confirm.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        contectUs=findView(R.id.contectUs);
        contectUs.setOnClickListener(this);
        confirm=findView(R.id.confirm);
        confirm.setOnClickListener(this);
        initRequestInter();

    }

    private void initRequestInter() {
        requestInterface=new RequestInterface() {
            @Override
            public void onRequestSucesss(int code, JSONObject jsonObject) {
                dismissdialog();
                ToastUtil.showMessage( jsonObject.optString("result_msg"));
                finish();
            }

            @Override
            public void onRequestErro(String message) {
                ToastUtil.showMessage(message);

            }

            @Override
            public void onRequestFail(int code) {

            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.contectUs:
                CommonUtils.callServiceTell(mcontext,"15377495785");
                break;
            case R.id.confirm:
                confirm();
                break;
        }

    }

    private void confirm() {
        if(phoneTxt.getText().toString().trim().length()<1){
            ToastUtil.showMessage("请填写手机号码或者邮箱");
            return;
        }
        showprocessdialog();
        try {
            JSONObject job=GsonUtils.creatJsonObj("").put(Constants.AUTHENT,tools.getValue(Constants.AUTHENT))
                    .put("note",phoneTxt.getText().toString());
            MainBizImpl.getInstance().onComomReq(requestInterface,this,0,job,"apply/sign");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        requestInterface=null;
        super.onDestroy();
    }
}
