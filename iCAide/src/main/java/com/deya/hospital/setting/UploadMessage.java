/**
 * Project Name:Vicinity.<br/>
 * File Name:UploadMessage.java.<br/>
 * Package Name:com.trisun.vicinity.common.message.<br/>
 * Date:2015年4月22日下午6:10:47.<br/>
 * Copyright (c) 2015, 广东云上城网络科技有限公司.
 *
 */

package com.deya.hospital.setting;


/**
 * .ClassName: UploadMessage(用一句话描述这个类的作用是做什么) <br/>
 * date: 2015年4月22日 下午6:10:47 <br/>
 * 
 * @author BMM
 */
public class UploadMessage {
  public static final int DELETE_UPLOAD_FILE =  0x500200;
  
  // 启动商城图片上传
  public static final int START_SHOP_UPLOAD_PICTURE = 0x500201;
  // 取笑商城图片上传
  public static final int CANCEL_SHOP_UPLOAD_PICTURE = 0x500202;

  // 启动商城图片上传
  public static final int START_SHOP_UPLOAD_RECORD = 0x500203;
  // 取笑商城图片上传
  public static final int CANCEL_SHOP_UPLOAD_RECORD = 0x500204;

  // 启动物业图片上传
  public static final int START_PROPERTY_UPLOAD_PICTURE = 0x500205;
  // 取消物业图片上传
  public static final int CANCEL_PROPERTY_UPLOAD_PICTURE = 0x500206;

  // 启动物业录音上传
  public static final int START_PROPERTY_UPLOAD_RECORD = 0x500207;
  // 取消物业录音上传
  public static final int CANCEL_PROPERTY_UPLOAD_RECORD = 0x500208;

  // 商城上传图片 请求成功
  public static final int SHOP_UPLOAD_PICTURE_SUCCESS = 0x500209;
  // 商城上传图片 请求失败
  public static final int SHOP_UPLOAD_PICTURE_FAIL = 0x500210;

  // 商城上传录音 请求成功
  public static final int SHOP_UPLOAD_RECORD_SUCCESS = 0x500211;
  // 商城上传录音 请求失败
  public static final int SHOP_UPLOAD_RECORD_FAIL = 0x500212;

  // 物业上传图片 请求成功
  public static final int PROPERTY_UPLOAD_PICTURE_SUCCESS = 0x500213;
  // 物业上传图片 请求失败
  public static final int PROPERTY_UPLOAD_PICTURE_FAIL = 0x500214;

  // 物业上传录音 请求成功
  public static final int PROPERTY_UPLOAD_RECORD_SUCCESS = 0x500215;
  // 物业上传录音 请求失败
  public static final int PROPERTY_UPLOAD_RECORD_FAIL = 0x500216;

public static final int SUP_PICTURE_SUCCESS = 0x5000217;

public static final int SUP_PICTURE_FAIL = 0x5000218;

public static final int FORM_UPLOAD_PICTURE_SUCCESS = 0x5000219;

public static final int FORM_UPLOAD_PICTURE_FAIL = 0x5000220;

public static final int UPLOAD_PICTURE_SUCCESS = 0x5000221;

public static final int UPLOAD_PICTURE_FAIL = 0x5000222;

public static final int UPLOAD_LOG_SUCCESS = 0x5000223;

public static final int UPLOAD_LOG_FAIL = 0x5000224;

}
