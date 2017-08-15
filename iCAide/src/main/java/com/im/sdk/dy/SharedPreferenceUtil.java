package com.im.sdk.dy;

/**
 * Created by Administrator on 2015/12/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

	public static void putInt(Context context, String fileName, String key,
			int a) {
		SharedPreferences mySharedPreferences = context.getSharedPreferences(
				fileName, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putInt(key, a);
		editor.commit();
	}

	public static int getInt(Context context, String fileName, String key) {
		SharedPreferences mySharedPreferences = context.getSharedPreferences(
				fileName, Activity.MODE_PRIVATE);
		return mySharedPreferences.getInt(key, 0);
	}
}
