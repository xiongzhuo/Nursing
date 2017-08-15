/**
 * Project Name:Vicinity.<br/>
 * File Name:ShopUploadBiz.java.<br/>
 * Package Name:com.trisun.vicinity.common.biz.<br/>
 * Date:2015年4月22日下午5:23:28.<br/>
 * Copyright (c) 2015, 广东云上城网络科技有限公司.
 *
 */

package com.deya.hospital.biz;

import java.util.List;

import org.json.JSONObject;

import com.deya.hospital.util.MyHandler;

/**
 * .ClassName: ShopUploadBiz(用一句话描述这个类的作用是做什么) <br/>
 * date: 2015年4月22日 下午5:23:28 <br/>
 * 
 * @author BMM
 */
public interface UploadBiz {
  /**
   * shopUploadPicture:【商城上传图片】. <br/>
   * .@param handler .@param fileList .@param successMessage .@param failMessage.<br/>
   */
  public void shopUploadPicture(MyHandler handler, List<String> fileList, int successMessage,
      int failMessage);

  /**
   * shopUploadPicture:【商城上传录音】. <br/>
   * .@param handler .@param fileList .@param successMessage .@param failMessage.<br/>
   */
  public void shopUploadRecord(MyHandler handler, List<String> fileList, int successMessage,
      int failMessage);

  /**
   * shopUploadPicture:【物业上传图片】. <br/>
   * .@param handler .@param fileList .@param successMessage .@param failMessage.<br/>
   */
  public void propertyUploadPicture(MyHandler handler, String file, int successMessage,
      int failMessage);

  /**
   * shopUploadPicture:【物业上传录音】. <br/>
   * .@param handler .@param fileList .@param successMessage .@param failMessage.<br/>
   */
  public void propertyUploadRecord(MyHandler handler, List<String> fileList, int successMessage,
		  
      int failMessage);
  
	/**
	 * 同步上传图片
	 */
	JSONObject syncUploadPicture(String file);
  
  
}
