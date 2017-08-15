package com.deya.acaide.wxapi;

import android.os.Bundle;

import com.deya.hospital.util.Constants;
import com.deya.hospital.util.ToastUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {

	public static final String WEIXIN_LOGIN_SUCCESS = "winxinLoginsuccess";
	private static IWXAPI api;
	private static String code;

	private void regToWx() {
		if (api == null) {
			api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
			api.registerApp(Constants.APP_ID);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		regToWx();
		Bundle bundle = getIntent().getExtras();
		api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq req) {
		ToastUtils.showToast(WXEntryActivity.this, "=============");
	}

	//寰俊鍥炶皟
    @Override
    public void onResp(BaseResp resp) {   //鍒嗕韩涔嬪悗鐨勫洖璋�
    	  super.onResp(resp);
    	  if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
    		  //Toast.makeText(this, "鍒嗕韩鎴愬姛", Toast.LENGTH_LONG).show();
    		  }
      
    }
}
