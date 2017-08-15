/**
 * Project Name:Vicinity.<br/>
 * File Name:UploadBizImpl.java.<br/>
 * Package Name:com.trisun.vicinity.common.bizimpl.<br/>
 * Date:2015年4月22日下午5:35:44.<br/>
 * Copyright (c) 2015, 广东云上城网络科技有限公司.
 *
 */

package com.deya.hospital.bizimp;

import android.content.Context;
import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.biz.UploadBiz;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.HttpDate;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * .ClassName: UploadBizImpl(用一句话描述这个类的作用是做什么) <br/>
 * date: 2015年4月22日 下午5:35:44 <br/>
 * 
 * @author BMM
 */
public class UploadBizImpl implements UploadBiz {
  HttpDate httpObject = HttpDate.getHttpRequestInstance();
  private static UploadBizImpl commonBiz;
  Tools tools;
  /**
   * .getHttpRequestInstance:单列模式
   */
  public static UploadBizImpl getInstance() {
    if (null == commonBiz) {
      commonBiz = new UploadBizImpl();
    }
    return commonBiz;
  }

  @Override
  public void shopUploadPicture(MyHandler handler, List<String> fileList, int successMessage,
                                int failMessage) {
    try {
      RequestParams params = new RequestParams();
      MultipartEntity mpEntity = new MultipartEntity(); // 文件传输
      for (int i = 0; i < fileList.size(); i++) {
        ContentBody cbFile = new FileBody(new File(fileList.get(i)));
        mpEntity.addPart("uploadify" + (i + 1), cbFile);
      }
      StringBody sb = new StringBody("3.0");
      mpEntity.addPart("versionTag", sb);
      params.setBodyEntity(mpEntity);
      httpObject.startRequestDate("", handler, params, successMessage,
          failMessage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void shopUploadRecord(MyHandler handler, List<String> fileList, int successMessage,
                               int failMessage) {
    try {
      RequestParams params = new RequestParams();
      MultipartEntity mpEntity = new MultipartEntity(); // 文件传输
      for (int i = 0; i < fileList.size(); i++) {
        params.addBodyParameter("file", new File(fileList.get(i)));
        FileBody body = new FileBody(new File(fileList.get(i)), "audio/mp3");
        mpEntity.addPart("file", body);
        mpEntity.addPart("HAS_UPLOADED_NUM", new StringBody(fileList.get(i)));
        StringBody sb = new StringBody("3.0");
        mpEntity.addPart("versionTag", sb);
      }
      params.setBodyEntity(mpEntity);
      httpObject.startRequestDate("", handler, params, successMessage,
          failMessage);
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

    @Override
    public void propertyUploadPicture(MyHandler handler, final String file, int successMessage,
                                      int failMessage) {
        try {
            RequestParams params = new RequestParams();
            MultipartEntity mpEntity = new MultipartEntity(); // 文件传输
            File uploadFile = new File(file);
            FileBody cbFile = new FileBody(uploadFile);
//      StringBody sb=new StringBody("myFile");
            mpEntity.setMultipartSubtype("form-data");
            mpEntity.addPart("file", cbFile);
            params.setBodyEntity(mpEntity);
            httpObject.startRequestDate(WebUrl.FILE_UPLOAD_URL, handler, params, successMessage,
                    failMessage);

            Log.i("UploadPicture", "上传"+params.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
  
  @Override
  public JSONObject syncUploadPicture(final String file) {
	  JSONObject map = null;
	  try {
      RequestParams params = new RequestParams();
      MultipartEntity mpEntity = new MultipartEntity(); // 文件传输
      File uploadFile = new File(file);
      FileBody cbFile = new FileBody(uploadFile);
      StringBody sb=new StringBody("myFile");
      
      Tools tools = new Tools(MyAppliaction.getContext(), Constants.AC);
      String authent = tools.getValue(Constants.AUTHENT);
      if(authent!=null){
    	  mpEntity.addPart("authent", new StringBody(authent));
      }

      mpEntity.setMultipartSubtype("form-data");
      mpEntity.addPart("fileParamName", sb);
      mpEntity.addPart("myFile", cbFile);
      params.setBodyEntity(mpEntity);
      
      Log.i("UploadPicture", "上传"+params.getEntity());
      
      ResponseStream response = httpObject.startRequestDate(WebUrl.FILE_UPLOAD_URL, params);
      String respondStr = response.readString();
      Log.i("upload", respondStr);
      
      map = new JSONObject(respondStr);
    } catch (Exception e) {
    	Log.e("upload", e.getMessage()+"");
    	e.printStackTrace();
    }
	  return map;
  }
    public JSONObject syncUploadPicture(Context context,final String file) {
        JSONObject map = null;
        try {
            RequestParams params = new RequestParams();
            MultipartEntity mpEntity = new MultipartEntity(); // 文件传输
            File uploadFile = new File(file);
            FileBody cbFile = new FileBody(uploadFile);
            StringBody sb=new StringBody("myFile");

            Tools tools = new Tools(MyAppliaction.getContext(), Constants.AC);
            String authent = tools.getValue(Constants.AUTHENT);
            if(authent!=null){
                mpEntity.addPart("authent", new StringBody(authent));
            }

            mpEntity.setMultipartSubtype("form-data");
            mpEntity.addPart("fileParamName", sb);
            mpEntity.addPart("myFile", cbFile);
            params.setBodyEntity(mpEntity);

            Log.i("UploadPicture", "上传"+params.getEntity());

            ResponseStream response = httpObject.startRequestDate(WebUrl.FILE_UPLOAD_URL, params);
            String respondStr = response.readString();
            Log.i("upload", respondStr);

            map = new JSONObject(respondStr);
        } catch (Exception e) {
            Log.e("upload", e.getMessage()+"");
            e.printStackTrace();
        }
        return map;
    }
  @Override
  public void propertyUploadRecord(MyHandler handler, List<String> fileList, int successMessage,
                                   int failMessage) {
    // TODO Auto-generated method stub
    try {
      RequestParams params = new RequestParams();
      MultipartEntity mpEntity = new MultipartEntity(); // 文件传输
      ContentBody cbFile = new FileBody(new File(fileList.get(0)));
      mpEntity.addPart("uploadify", cbFile); // <input type="file"// name="userfile" /> 对应的
      StringBody sb = new StringBody("3.0");
      mpEntity.addPart("versionTag", sb);     
      params.setBodyEntity(mpEntity);
      httpObject.startRequestDate(WebUrl.FILE_UPLOAD_URL, handler, params, successMessage,
          failMessage);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
}