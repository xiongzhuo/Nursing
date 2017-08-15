package com.deya.hospital.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class DebugUtil {
    public static final String TAG = "DebugUtil";
     
    public static void toast(Context context,String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
     
    public static void debug(String tag,String msg){
        if (WebUrl.getInstance().isTest) {
            Log.d(tag, msg); 
        }
    }
     
    public static void debug(String msg){
        if (WebUrl.getInstance().isTest) { 
            Log.d(TAG, msg);
           
        }
    }
     
    public static void error(String tag,String error){
        Log.e(tag, error);
    }
     
    public static void error(String error){
        Log.e(TAG, error);
    }
    
    public static void error(String tag,String error,Throwable throwable){
    	 Log.e(tag, error, throwable);
    }
}
