package com.deya.acaide.wxapi;

import android.content.Context;
import android.graphics.Bitmap;

import com.deya.hospital.util.Constants;

import java.io.ByteArrayOutputStream;

/** 
 * @author  yung
 * @date 创建时间：2015年12月29日 下午2:59:23 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public class WeixiShareUtil {

	public static String getWeixinAppId(Context context) {
		// TODO Auto-generated method stub
		return Constants.APP_ID;
	}

	public static byte[] bmpToByteArray(Bitmap thumbBmp, boolean b) {
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 thumbBmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		   return baos.toByteArray();
	}

}
