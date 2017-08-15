/**
 * 项目名称:Vicinity3.1 . <br/>
 * 文件名称:HttpRequestDate.java . <br/>
 * 包名:com.trisun.vicinity.util . <br/>
 * 日期:2015年4月13日上午11:35:30 . <br/>
 *
 * @author LIUXIN . <br/>
 * Copyright (c) 2015,广东云上城科技有限公司 . <br/>
 */

package com.deya.hospital.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.login.LoginActivity;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yuntongxun.ecsdk.ECDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * ClassName:. HttpRequestDate【用于返回服务器端的数据】 <br/>
 */
public class HttpDate {
    /**
     * .startRequestDate:开始请求数据<br/>
     * 该方法适用于请求服务器后台返回的相关数据 String URL用于传递服务器请求地址 Handler 用于请求和更新 message
     * 用于返回的消息标示
     */

    // 私有的默认构造子
    private HttpDate() {
    }

    // 注意，这里没有final
    private static HttpDate single;

    /**
     * .getHttpRequestInstance:单列模式
     */
    public static HttpDate getHttpRequestInstance() {
        if (null == single) {
            single = new HttpDate();
        }
        return single;
    }

    /**
     * .startRequestDate:公用的调用方法
     */
    public void startRequestDate(String url, final Handler handler,
                                 RequestParams params, final int successMessage,
                                 final int failMessage) {
        Log.i("UploadPicture", "startRequestDate");
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);// 超时时间 Long配置当前Http缓存到期
        // 设置超时时间
        http.configTimeout(10 * 1000);// 连接超时  //指的是连接一个url的连接等待时间。
        http.configSoTimeout(10 * 1000);// 获取数据超时  //指的是连接上一个url，获取response的返回等待时间
        http.send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                        Log.i("UploadPicture", "onStart");
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.i("UploadPicture", "onSuccess");
                        String stringResult = responseInfo.result;
                        Message message = new Message();
                        message.what = successMessage;
                        if (!TextUtils.isEmpty(stringResult)) {
                            message.obj = stringResult;
                        }
                        handler.removeMessages(successMessage);
                        JSONObject job = null;
                        try {
                            job = new JSONObject(stringResult);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.i("UploadPicture", "onFailure " + msg);
                        Log.e("UploadPictrue", error.getMessage());
                        Message message = new Message();
                        message.what = failMessage;
                        if (!TextUtils.isEmpty(msg)) {
                            message.obj = msg;
                        }
                        handler.removeMessages(failMessage);
                        handler.sendMessage(message);
                    }
                });
    }


    public ResponseStream startRequestDate(String url,
                                           RequestParams params) throws HttpException {
        Log.i("UploadPicture", "startRequestDate");
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);// 超时时间 Long配置当前Http缓存到期
        // 设置超时时间
        http.configRequestRetryCount(0);
        http.configTimeout(10 * 1000);// 连接超时  //指的是连接一个url的连接等待时间。
        http.configSoTimeout(10 * 1000);// 获取数据超时  //指的是连接上一个url，获取response的返回等待时间
        return http.sendSync(HttpRequest.HttpMethod.POST, url, params);
    }

    /**
     * .startRequestDate:公用的调用方法
     */
    public void startRequestDateGet(final Activity activity, String url,
                                    final MyHandler handler, RequestParams params,
                                    final int successMessage, final int failMessage) {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);// 超时时间 Long配置当前Http缓存到期
        // 设置超时时间
        http.configTimeout(10 * 1000);// 连接超时  //指的是连接一个url的连接等待时间。
        http.configSoTimeout(10 * 1000);// 获取数据超时  //指的是连接上一个url，获取response的返回等待时间
        LogUtil.i("startRequestDateGet", "url>>" + url);

        http.send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String stringResult = responseInfo.result;
                        LogUtil.i("startRequestDateGet", "stringResult>>"
                                + stringResult);
                        Message message = new Message();
                        message.what = successMessage;
                        if (!TextUtils.isEmpty(stringResult)) {
                            message.obj = stringResult;
                        }
                        handler.removeMessages(successMessage);
                        JSONObject job = null;
                        try {
                            job = new JSONObject(stringResult);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (null == job || AbStrUtil.isEmpty(job.toString())) {
                            Message message3 = new Message();
                            message3.what = failMessage;
                            handler.removeMessages(failMessage);
                            handler.sendMessage(message3);
                            return;
                        }

                        if ((job.has("code"))
                                && !AbStrUtil.isEmpty(job
                                .optString("code"))
                                && job.optString("code").equals("9")) {
                            onAuthentLoseEfficacy(job, activity);
                        } else {
                            handler.sendMessage(message);
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Message message = new Message();
                        message.what = failMessage;
                        if (!TextUtils.isEmpty(msg)) {
                            message.obj = msg;
                        }
                        LogUtil.d("startRequestDateGet", "stringResult>>"
                                + msg.toString());
                        handler.removeMessages(failMessage);
                        handler.sendMessage(message);
                    }
                });
    }


    /**
     * @param activity
     * @param code             请求识别的code
     * @param requestInterface 回调接口
     * @param params
     * @param url
     */
    public void startRequestDateGet(final Activity activity, final int code, final RequestInterface requestInterface,
                                    RequestParams params, String url
    ) {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);// 超时时间 Long配置当前Http缓存到期
        // 设置超时时间
        http.configRequestRetryCount(0);
        http.configTimeout(10 * 1000);// 连接超时  //指的是连接一个url的连接等待时间。
        http.configSoTimeout(10 * 1000);// 获取数据超时  //指的是连接上一个url，获取response的返回等待时间
        http.send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String stringResult = responseInfo.result;
                        if (AbStrUtil.isEmpty(stringResult)) {
                            requestInterface.onRequestFail(code);
                            return;
                        }
                        JSONObject job = null;
                        try {
                            job = new JSONObject(stringResult);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (null == job || AbStrUtil.isEmpty(job.toString())) {
                            requestInterface.onRequestFail(code);
                            return;
                        }
                        if (!job.has("code")) {
                            ToastUtils.showToast(activity, "服务器数据异常：" + job);
                            return;
                        }

                        if (job.optString("code").equals("9")) {
                            onAuthentLoseEfficacy(job, activity);//凭证失效
                        } else if (job.optString("code").equals("0")) {
                            requestInterface.onRequestSucesss(code, job);
                        } else {
                            String optString = job.optString("msg");
                            ToastUtil.showMessage(optString);
                            if (!AbStrUtil.isEmpty(optString)) {
                                requestInterface.onRequestErro(optString);
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        requestInterface.onRequestFail(code);
                    }
                });
    }

    public void startRequestDateGet(final Context context, final int code, final RequestInterface requestInterface,
                                    RequestParams params, String url
    ) {
        HttpUtils http = new HttpUtils(100 * 1000);
        http.configCurrentHttpCacheExpiry(1000 * 10);// 超时时间 Long配置当前Http缓存到期
        // 设置超时时间
        http.configRequestRetryCount(0);
        http.configTimeout(10 * 1000);// 连接超时  //指的是连接一个url的连接等待时间。
        http.configSoTimeout(10 * 1000);// 获取数据超时  //指的是连接上一个url，获取response的返回等待时间
        http.send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        Log.i(total + "----", current + "");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String stringResult = responseInfo.result;
                        if (AbStrUtil.isEmpty(stringResult)) {
                            requestInterface.onRequestFail(code);
                            return;
                        }
                        JSONObject job = null;
                        try {
                            job = new JSONObject(stringResult);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (null == job || AbStrUtil.isEmpty(job.toString())) {
                            requestInterface.onRequestFail(code);
                            return;
                        }
                        if (!job.has("code")) {
                            ToastUtils.showToast(context, "服务器数据异常：" + job);
                            return;
                        }

                        if (job.optString("code").equals("9")) {
                            onAuthentLoseEfficacy(job, context);
                            return;
                        } else if (job.optString("code").equals("0")) {
                            requestInterface.onRequestSucesss(code, job);
                        } else {
                            String optString = job.optString("msg");
                            if (!AbStrUtil.isEmpty(optString)) {
                                ToastUtil.showMessage(optString);
                                requestInterface.onRequestErro(optString);
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        requestInterface.onRequestFail(code);
                    }
                });
    }

    private void onAuthentLoseEfficacy(JSONObject job, Context context) {
        Tools tools;
        ToastUtil.showMessage(
                job.optString("result_msg"));
        tools = MyAppliaction.getTools();
        tools.putValue(Constants.AUTHENT, "");
        tools.putValue(Constants.NAME, "");
        tools.putValue(Constants.HOSPITAL_NAME, "");
        tools.putValue(Constants.AGE, "");
        tools.putValue(Constants.SEX, "");
        tools.putValue(Constants.MOBILE, "");
        tools.putValue(Constants.HEAD_PIC, "");
        tools.putValue(Constants.EMAIL, "");
        tools.putValue(Constants.JOB, "");
        tools.putValue(Constants.USER_ID, "");

        if (ECDevice.isInitialized())
            try {
                ECDevice.unInitial();
            } catch (Exception e) {
                e.printStackTrace();
            }
        SDKCoreHelper.logout(false);

        CCPAppManager.setClientUser(null);

        MainActivity.mInit = false;

        try {
            tools.putValue(Constants.AUTHENT, "");
            if (context == null) {
                return;
            }
            Intent intent = new Intent(context,
                    LoginActivity.class);
            if (Build.VERSION.SDK_INT >= 14) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            intent.setAction(Constants.AUTHENT_LOSE);

            context.sendBroadcast(intent);
            context.startActivity(intent);
            Activity activity = (Activity) context;
            activity.finish();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }


    }

    public void startRequestDateGet2(String url, final MyHandler handler,
                                     RequestParams params, final int successMessage,
                                     final int failMessage) {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);// 超时时间 Long配置当前Http缓存到期
        // 设置超时时间
        http.configTimeout(10 * 1000);// 连接超时  //指的是连接一个url的连接等待时间。
        http.configSoTimeout(10 * 1000);// 获取数据超时  //指的是连接上一个url，获取response的返回等待时间\
        http.configRequestRetryCount(0);

        http.send(HttpRequest.HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String stringResult = responseInfo.result;
                        Message message = new Message();
                        message.what = successMessage;
                        if (!TextUtils.isEmpty(stringResult)) {
                            message.obj = stringResult;
                        }
                        handler.removeMessages(successMessage);
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Message message = new Message();
                        message.what = failMessage;
                        if (!TextUtils.isEmpty(msg)) {
                            message.obj = msg;
                        }
                        handler.removeMessages(failMessage);
                        handler.sendMessage(message);
                    }
                });
    }

    public String multipartRequest(String urlTo, Map<String, String> parmas,
                                   String filepath, String filefield, String fileMimeType)
            throws Exception {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "----" + Long.toString(System.currentTimeMillis())
                + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        String[] q = filepath.split("/");
        int idx = q.length - 1;

        try {
            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent",
                    "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\""
                    + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary"
                    + lineEnd);

            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            Iterator<String> keys = parmas.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = parmas.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream
                        .writeBytes("Content-Disposition: form-data; name=\""
                                + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                    + lineEnd);

            if (200 != connection.getResponseCode()) {
            } else {

            }

            inputStream = connection.getInputStream();

            result = this.convertStreamToString(inputStream);

            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return result;
        } catch (Exception e) {
            Log.i("1111", e + "");
        }
        return result;

    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
