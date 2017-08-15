package com.deya.hospital.analysis;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.Tools;
import com.umeng.analytics.MobclickAgent;

public class AnalysisManager {
	static AnalysisManager self;
	private boolean lastClosed = true;

	public static AnalysisManager instance() {
		if (self == null) {
			self = new AnalysisManager();
		}
		return self;
	}

	public void onCreate() {
		MobclickAgent.setDebugMode(true);
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		// MobclickAgent.setSessionContinueMillis(50000);
	}

	public void onResume(Context o) {
		if (o == null)
			return;
		MobclickAgent.onResume(o);
		MobclickAgent.onPageStart(o.getClass().getName());

		Log.i("Analysis", "onResume " + isAppOnForeground(o));
		if (lastClosed && isAppOnForeground(o)) { // 切入程序
			if(actionRequest(1)){
				lastClosed = false;
			}
		}
	}

	public void onPause(Context o) {
		if (o == null)
			return;
		MobclickAgent.onPageEnd(o.getClass().getName());
		MobclickAgent.onPause(o);
	}

	public void onStop(Context o) {
		Log.i("Analysis", "onStop " + isAppOnForeground(o));

		if (!isAppOnForeground(o)) { // 切出程序
			lastClosed = true;
			actionRequest(2);
		}
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground(Context o) {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) o
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		String packageName = o.getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	private final static int ACTION_POST_SUCCESS = 1;
	private final static int ACTION_POST_FAILTURE = 2;

	/**
	 * 用户行为上传
	 * 
	 * @param action_type
	 *            1 打开， 2 关闭
	 */
	private boolean actionRequest(int action_type) {

		Tools tools = new Tools(MyAppliaction.getContext(), Constants.AC);
		String authent = tools.getValue(Constants.AUTHENT);
		if (authent == null) {
			return false;
		}

		JSONObject job = new JSONObject();
		try {

			job.put("authent", authent);
			job.put("action_type", action_type);
			job.put("device_type", 2);
			MainBizImpl.getInstance().onComomRequest(myHandler, null,
					ACTION_POST_SUCCESS, ACTION_POST_FAILTURE, job,
					"action/actionCount");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}

	private MyHandler myHandler = new MyHandler(null) {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case ACTION_POST_SUCCESS:
				Log.i("action", "上传成功");
				break;
			case ACTION_POST_FAILTURE:
				Log.e("action", "上传失败");
				break;
			default:
				break;
			}

		}
	};

}
