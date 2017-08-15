package com.deya.hospital.form;

import android.content.Context;

import com.deya.hospital.util.SharedPreferencesUtil;

public class FormDataCache {
	public static void saveFormListItemData(Context context,String formId,String str){
		if(str.length()>0){
		SharedPreferencesUtil.saveString(context, "formList_data"+formId, str);
		}
	}
	public static String getFormItemCacheData(Context context,String formId){
		
		return SharedPreferencesUtil.getString(context, "formList_data"+formId, "");
	}

	public static void saveFormList(Context context,String str){
		if(str.length()>0){
			SharedPreferencesUtil.saveString(context, "formList", str);
			}
	}
	public static String getFormList(Context context){
		return SharedPreferencesUtil.getString(context, "formList", "");
	}
	
	public static void saveXYFormList(Context context,String str){
		if(str.length()>0){
			SharedPreferencesUtil.saveString(context, "xyformList", str);
			}
	}
	public static String getXYFormList(Context context){
		return SharedPreferencesUtil.getString(context, "xyformList", "");
	}
}
