package com.deya.hospital.base;

import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BaseWebViewClient extends WebViewClient {
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		JSONObject map = new JSONObject();
		try {
			map.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("json", e.getMessage());
		}
		//设置变量，供js使用
		view.loadUrl("javascript:setInitData(" + map.toString() +")");
    }
	
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		Log.i("url", url);
		//"getArticles.json?callback=setData"
//		Map<String, String> map = parseUrl(url);
//		if(map!=null){
//			Route(map);
//		}
	    return null;
	}
	
	private Map<String, String> parseUrl(String url){
		if(url.indexOf("?")>0){
			String[] arrUrl = url.split("?");
			String param = arrUrl[arrUrl.length-1];
			String[] arrParam = param.split("&");
			Map<String, String> map = new HashMap<String, String>();
			for(int i =0; i < arrParam.length; ++i){
				String[] arrArg = arrParam[i].split("=");
				if(arrArg.length==2){
					map.put(arrArg[0], arrArg[1]);
				}
			}
			return map;
		}
		return null;
	}
	
	//根据参数进行路由
	private void Route(Map<String, String> map){
		
	}
}
