package com.deya.hospital.util;

import android.content.Context;
import android.widget.Toast;

import com.im.sdk.dy.common.utils.ToastUtil;

public class ToastUtils {
	private static String oldMsg;
	protected static Toast toast = null;
	private static long oneTime = 0;
	private static long twoTime = 0;

	public static void showToast(Context context, String s) {
		ToastUtil.showMessage(s);
	}

	public static void showToast(Context context, String s, int showTime) {
		if (toast == null) {
			toast = Toast.makeText(context, s, showTime);
			toast.show();
			oneTime = System.currentTimeMillis();
		} else {
			twoTime = System.currentTimeMillis();
			if (s.equals(oldMsg)) {
				if (twoTime - oneTime > showTime) {
					toast.show();
				}
			} else {
				oldMsg = s;
				toast.setText(s);
				toast.show();
			}
		}
		oneTime = twoTime;
	}

	public static void showToast(Context context, int resId) {
		showToast(context, context.getString(resId));
	}
}
