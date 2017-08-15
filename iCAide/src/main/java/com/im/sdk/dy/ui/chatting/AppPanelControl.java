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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.deya.acaide.R;
import com.deya.hospital.application.MyAppliaction;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.ui.SDKCoreHelper;

/**
 * 聊天插件功能控制器
 * 
 * @author yung•云通讯
 * @date 2014-12-10
 * @version 4.0
 */
public class AppPanelControl {

	private Context mContext;

	private static boolean isShowVoipCall = true;

	/**
	 * 底部菜单文字
	 * 如  图片  拍照
	 * 如果需要扩展，需要在这里增加
	 */
	public int[] cap = new int[] { R.string.app_panel_pic,
			R.string.app_panel_tackpic};
	
	/**
	 * 底部菜单图片
	 * 如  图片  拍照
	 * 如果需要扩展，需要在这里增加
	 */
	public int[] capVoip = new int[] { R.string.app_panel_pic,
			R.string.app_panel_tackpic
			 };

	/**
     *
     */
	public AppPanelControl() {
		mContext = CCPAppManager.getContext();
	}
	
	
	

	public static void setShowVoipCall(boolean isShowVoipCall) {
		AppPanelControl.isShowVoipCall = isShowVoipCall;
	}

   


	/**
	 *
	 * @return
	 */
	public List<Capability> getCapability() {
		List<Capability> capabilities = new ArrayList<Capability>();

		if (isShowVoipCall&&SDKCoreHelper.getInstance().isSupportMedia()) {
			for (int i = 0; i < capVoip.length; i++) {
				Capability capability = getCapability(capVoip[i]);
				capabilities.add(capabilities.size(), capability);
			}

		} else {
			for (int i = 0; i < cap.length; i++) {
				Capability capability = getCapability(cap[i]);
				capabilities.add(capabilities.size(), capability);
			}
		}
		return capabilities;
	}

	/**
	 * 初始化底部菜单 的item
	 * 如  图片  拍照
	 * 如果需要扩展，需要在这里增加
	 * @param resid
	 * @return
	 */
	private Capability getCapability(int resid) {
		Capability capability = null;
		switch (resid) {
		case R.string.app_panel_pic:
			capability = new Capability(getContext().getString(
					R.string.app_panel_pic), R.drawable.image_icon);
			break;
		case R.string.app_panel_tackpic:

			capability = new Capability(getContext().getString(
					R.string.app_panel_tackpic), R.drawable.photograph_icon);
			break;
//		case R.string.app_panel_file:
//
//			capability = new Capability(getContext().getString(
//					R.string.app_panel_file), R.drawable.capability_file_icon);
//			break;
//		case R.string.app_panel_voice:
//			
//			capability = new Capability(getContext().getString(
//					R.string.app_panel_voice), R.drawable.voip_call);
//			break;
//		case R.string.app_panel_video:
//			
//			capability = new Capability(getContext().getString(
//					R.string.app_panel_video), R.drawable.video_call);
//			break;
//		case R.string.app_panel_read_after_fire:
//			
//			capability = new Capability(getContext().getString(
//					R.string.app_panel_read_after_fire), R.drawable.fire_msg);
//			break;

		default:
			break;
		}
		capability.setId(resid);
		return capability;
	}

	/**
	 * @return
	 */
	private Context getContext() {
		if (mContext == null) {
			mContext = MyAppliaction.getmInstance().getApplicationContext();
		}
		return mContext;
	}
}
