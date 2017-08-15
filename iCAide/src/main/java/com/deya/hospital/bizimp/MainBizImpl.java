/**
 * Project Name:Vicinity3.1.<br/>
 * File Name:MainBizImpl.java.<br/>
 * Package Name:com.trisun.vicinity.init.bizimpl.<br/>
 * Date:2015年4月22日上午9:45:37.<br/>
 * Copyright (c) 2015, 广东云上城网络科技有限公司.
 *
 */

package com.deya.hospital.bizimp;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.HttpDate;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ParamsUtil;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.common.utils.LogUtil;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.umeng.socialize.utils.Log;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * .ClassName: MainBizImpl(用一句话描述这个类的作用是做什么) <br/>
 * date: 2015年4月22日 上午9:45:37 <br/>
 * 
 * @author 孙鹏
 */
public class MainBizImpl {
	private static MainBizImpl single;
	private HttpDate httpObject = HttpDate.getHttpRequestInstance();

	/**
	 * getInstance:(获取IntegralBizImpl的单例). <br/>
	 */
	public static MainBizImpl getInstance() {
		if (null == single) {
			single = new MainBizImpl();
		}
		return single;
	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see com.trisun.vicinity.init.biz.MainBiz#getLogin(com.trisun.vicinity.util.MyHandler,
	 *      int, int, String)
	 */

	/**
	 * 获取
	 * 
	 * @param handler
	 * @param successMessage
	 * @param failMessage
	 * @param path
	 */
	public void getData(Handler handler, int successMessage, int failMessage,
			JSONObject json, String path, String authent) {
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(
					syncParam(json, path, authent), ParamsUtil.CHASET));
			httpObject.startRequestDate(WebUrl.getInstance().web_url(path),
					handler, params, successMessage, failMessage);
		} catch (UnsupportedEncodingException e) {
			LogUtil.d("MainBizImpl",
					"UnsupportedEncodingException>>" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 获取
	 * 
	 * @param handler
	 * @param successMessage
	 * @param failMessage
	 * @param path
	 */
	public void getData(MyHandler handler, Activity activity,
						int successMessage, int failMessage, JSONObject json, String path,
						String authent) {
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(
					syncParam(json, path, authent), ParamsUtil.CHASET));
			httpObject.startRequestDateGet(activity, WebUrl.getInstance()
					.web_url(path), handler, params, successMessage,
					failMessage);
		} catch (UnsupportedEncodingException e) {
			LogUtil.d("MainBizImpl",
					"UnsupportedEncodingException>>" + e.getMessage());
		}
	}

	public void getAgreemtnText(MyHandler handler, int successMessage,
								int failMessage) {
		RequestParams params = new RequestParams();
		httpObject.startRequestDateGet2(WebUrl.GETAGREETEXT, handler, params,
				successMessage, failMessage);
	}

	public void getWebFiles(MyHandler handler, int successMessage,
							int failMessage) {
		RequestParams params = new RequestParams();
		httpObject.startRequestDateGet2(WebUrl.GETWEBFILES, handler, params,
				successMessage, failMessage);
	}

	public void sendFormTaskList(MyHandler handler, Activity activity,
								 int successMessage, int failMessage, String param) {
		param = addVersion(param, "grid/submitGridInfos");
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(param, ParamsUtil.CHASET));
			httpObject.startRequestDateGet(activity, WebUrl.SENDFORM, handler,
					params, successMessage, failMessage);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private String syncParam(JSONObject msg_json, String path, String authent) {
		JSONObject json = new JSONObject();
		if (null == msg_json) {
			msg_json = new JSONObject();
		}
		try {
			msg_json.put("authent", authent);
			msg_json.put("device_type", "2");
			String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
					.parseStrToMd5L32(msg_json.toString()) + path);
			String pkName = MyAppliaction.getContext().getPackageName();
			String versionName = MyAppliaction.getContext().getPackageManager()
					.getPackageInfo(pkName, 0).versionName;
			json.put("token", jobStr);
			JSONObject clientInfoJson = new JSONObject();
			clientInfoJson.put("soft_ver", versionName);
			clientInfoJson.put("device_model", android.os.Build.MODEL + "");
			clientInfoJson.put("os_ver", android.os.Build.VERSION.RELEASE
					+ "");
			clientInfoJson.put("device_type", "2");
			json.put("client_info", clientInfoJson);
			json.put("soft_ver", versionName);
			json.put("msg_content", msg_json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}



	// 工作圈请求
	public void onCirclModeRequest(MyHandler handler, Activity activity,
							   int successMessage, int failMessage, JSONObject param, String url) {
		param = addCommonParam(param, url);
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(param.toString(), ParamsUtil.CHASET));
			httpObject.startRequestDateGet(activity, WebUrl.PUBLIC_SERVICE_URL+"/"+url, handler,
					params, successMessage, failMessage);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LogUtil.i("startRequestDateGet", "params>>"+param.toString());
	}
	public void onComomCirclModReq(RequestInterface requestInterface, Context context,
						   int requestCode, JSONObject param, String url) {
		param = addCommonParam(param, url);
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(param.toString(), ParamsUtil.CHASET));
			httpObject.startRequestDateGet(context,requestCode,requestInterface, params,  WebUrl.PUBLIC_SERVICE_URL+"/"+url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LogUtil.i("startRequestDateGet", "params>>"+param.toString());
	}
	// 公共请求方法
	public void onComomRequest(MyHandler handler, Activity activity,
							   int successMessage, int failMessage, JSONObject param, String url) {
		param = addCommonParam(param, url);
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(param.toString(), ParamsUtil.CHASET));
			httpObject.startRequestDateGet(activity, WebUrl.LEFT_URL+"/"+url, handler,
					params, successMessage, failMessage);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LogUtil.i("startRequestDateGet", "params>>"+param.toString());
	}
	// 公共请求方法
	public void onComomReq(RequestInterface requestInterface, Activity activity,
						   int requestCode, JSONObject param, String url) {
		param = addCommonParam(param, url);
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(param.toString(), ParamsUtil.CHASET));
			httpObject.startRequestDateGet(activity,requestCode,requestInterface, params, WebUrl.LEFT_URL+"/"+url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LogUtil.i("startRequestDateGet", "params>>"+param.toString());
	}
	public void onComomReqAllpath(RequestInterface requestInterface, Activity activity,
						   int requestCode, JSONObject param, String url) {
		param = addCommonParam(param, url);
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(param.toString(), ParamsUtil.CHASET));
			httpObject.startRequestDateGet(activity,requestCode,requestInterface, params,url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LogUtil.i("startRequestDateGet", "params>>"+param.toString());
	}

	// 公共请求方法
	public void onComomReq(RequestInterface requestInterface, Context context,
						   int requestCode, JSONObject param, String url) {
		param = addCommonParam(param, url);
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(param.toString(), ParamsUtil.CHASET));
			httpObject.startRequestDateGet(context,requestCode,requestInterface, params, WebUrl.LEFT_URL+"/"+url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LogUtil.i("startRequestDateGet", "params>>"+param.toString());
	}
	public JSONObject sendSyncRequest(String url,JSONObject param) {
		param = addCommonParam(param, url);
		RequestParams params = new RequestParams();
		JSONObject obj = null;
		try {
			params.setBodyEntity(new StringEntity(param.toString(), ParamsUtil.CHASET));
			
			Log.i("action", param.toString());
			ResponseStream response = httpObject.startRequestDate(WebUrl.LEFT_URL+"/"+url, params);
		    String respondStr = response.readString();
		    Log.i("action", respondStr);
		    
		    obj = new JSONObject(respondStr);
		} catch (Exception e) {
			Log.e("action", e.getMessage()+"");
			e.printStackTrace();
		}
		return obj;
	}
	
	public void onComomRequest(MyHandler handler, Context context,
							   int successMessage, int failMessage, JSONObject param, String url) {
		param = addCommonParam(param, url);
		RequestParams params = new RequestParams();
		Log.i("yug", WebUrl.LEFT_URL+"/"+url);
		Log.i("yug", param.toString());
		try {
			params.setBodyEntity(new StringEntity(param.toString(), ParamsUtil.CHASET));
			httpObject.startRequestDate(WebUrl.LEFT_URL+"/"+url, handler,
					params, successMessage, failMessage);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @deprecated 将版本号插入进去
	 * @param param
	 * @param dataUrl
	 *            数据请求方法名
	 * @return
	 */
	public String addVersion(String param, String dataUrl) {// 添加版本名、设备类型、设备名称、手机系统
		try {
			String pkName = MyAppliaction.getContext().getPackageName();
			String versionName = MyAppliaction.getContext().getPackageManager()
					.getPackageInfo(pkName, 0).versionName;
			JSONObject job = new JSONObject(param);
			if (job.has("msg_content")) {
				String content = job.optString("msg_content");
				JSONObject jobContent = new JSONObject(content);
				JSONObject clientInfoJson = new JSONObject();
				clientInfoJson.put("soft_ver", versionName);
				clientInfoJson.put("device_model", android.os.Build.MODEL + "");
				clientInfoJson.put("os_ver", android.os.Build.VERSION.RELEASE
						+ "");
				clientInfoJson.put("device_type", "2");
				jobContent.put("client_info", clientInfoJson);
				String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
						.parseStrToMd5L32(jobContent.toString()) + dataUrl);
				job.put("token", jobStr);
				job.put("msg_content", jobContent);
			}
			return job.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return param;
		}
	}

	public JSONObject addCommonParam(JSONObject param, String dataUrl) {// 添加版本名、设备类型、设备名称、手机系统
		JSONObject job = new JSONObject();

		try {
			String pkName = MyAppliaction.getContext().getPackageName();
			String versionName = MyAppliaction.getContext().getPackageManager()
					.getPackageInfo(pkName, 0).versionName;
			JSONObject jobContent = param;

			JSONObject clientInfoJson = new JSONObject();
			clientInfoJson.put("soft_ver", versionName);
			clientInfoJson.put("device_model", android.os.Build.MODEL + "");
			clientInfoJson.put("os_ver", android.os.Build.VERSION.RELEASE + "");
			clientInfoJson.put("device_type", "2");
			clientInfoJson.put("app_code",WebUrl.APP_CODE);
			jobContent.put("client_info", clientInfoJson);
			String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
					.parseStrToMd5L32(jobContent.toString()) + dataUrl);
			if(!AbStrUtil.isEmpty(MyAppliaction.getTools().getValue(Constants.USER_ID))){
				job.put("userId", (MyAppliaction.getTools().getValue(Constants.USER_ID)));
			}
			job.put("token", jobStr);
			job.put("sub_id", "");
			job.put("data", jobContent.toString());

		} catch (Exception e) {
			e.printStackTrace();

		}
		return job;
	}
}
