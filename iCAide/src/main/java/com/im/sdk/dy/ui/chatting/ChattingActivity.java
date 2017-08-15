/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.im.sdk.dy.ui.chatting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;

import com.deya.acaide.R;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.ui.ECFragmentActivity;
import com.im.sdk.dy.ui.SDKCoreHelper;

/**
 * @author 容联•云通讯
 * @date 2014-12-9
 * @version 4.0
 */
public class ChattingActivity extends ECFragmentActivity implements
		ChattingFragment.OnChattingAttachListener {

	private static final String TAG = "IM_PATH.ChattingActivity";
	public ChattingFragment mChattingFragment;
	private MyReceiver myReceiver;
	private boolean isMyDepartGroup = false;
	String departGroupOwnerId = "";

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		LogUtil.d(TAG, "chatting ui dispatch key event :" + event);
		if (mChattingFragment != null
				&& mChattingFragment.onKeyDown(event.getKeyCode(), event)) {
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// CrashHandler.getInstance().setContext(this);

		IntentFilter filter1 = new IntentFilter();

		filter1.addAction("com.deya.acaide.Removemember");
		filter1.addAction(SDKCoreHelper.ACTION_KICK_OFF);

		registerReceiver(myReceiver, filter1);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(myReceiver);
	}

	public class MyReceiver extends BroadcastReceiver {
		// 可用Intent的getAction()区分接收到的不同广播
		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent == null) {
				return;
			}
			if (intent.getAction().equals(SDKCoreHelper.ACTION_KICK_OFF)
					|| intent.getAction()
							.equals("com.deya.acaide.Removemember"))
				finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.d(TAG, "onCreate");
		super.onCreate(null);
		getWindow().setFormat(PixelFormat.TRANSPARENT);

		myReceiver = new MyReceiver();
		String recipients = getIntent().getStringExtra(
				ChattingFragment.RECIPIENTS);
		if (getIntent().hasExtra(ChattingFragment.IS_MY_DEPART_GROU)) {
			isMyDepartGroup = true;
			departGroupOwnerId=getIntent().getStringExtra(ChattingFragment.DEPART_GROU_OWNER);
		}
		if (recipients == null) {
			finish();
			LogUtil.e(TAG, "recipients is null !!");
			return;
		}
		setContentView(R.layout.im_chattingui_activity_container);
		mChattingFragment = new ChattingFragment();
		Bundle bundle = getIntent().getExtras();
		bundle.putBoolean(ChattingFragment.FROM_CHATTING_ACTIVITY, true);
		bundle.putBoolean(ChattingFragment.IS_MY_DEPART_GROU, isMyDepartGroup);
		bundle.putString(ChattingFragment.DEPART_GROU_OWNER, departGroupOwnerId);
		if(getIntent().hasExtra("message")){
		bundle.putString(ChattingFragment.DEFULT_REPORT_MESSAGE,getIntent().getStringExtra("message"));
		}
		mChattingFragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
				.add(R.id.ccp_root_view, mChattingFragment).commit();
		onActivityCreate();

		AppPanelControl.setShowVoipCall(false);
		// if (isChatToSelf(recipients) || isPeerChat(recipients)) {
		// }

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.d(TAG, "chatting ui on key down, " + keyCode + ", " + event);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		LogUtil.d(TAG, "chatting ui on key up");
		return super.onKeyUp(keyCode, event);
	}

	public boolean isPeerChat() {
		if (mChattingFragment != null) {
			return mChattingFragment.isPeerChat();
		}
		return false;
	}

	public boolean isChatToSelf(String sessionId) {

		String userId = CCPAppManager.getUserId();
		return sessionId != null
				&& (sessionId.equalsIgnoreCase(userId) ? true : false);
	}

	public boolean isPeerChat(String sessionId) {

		return sessionId.toLowerCase().startsWith("g");
	}

	@Override
	public void onChattingAttach() {
		LogUtil.d(TAG, "onChattingAttach");
	}
}