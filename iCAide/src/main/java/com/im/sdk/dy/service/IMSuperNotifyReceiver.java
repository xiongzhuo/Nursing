package com.im.sdk.dy.service;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.deya.hospital.util.DebugUtil;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

//处理广播类
public abstract class IMSuperNotifyReceiver extends BroadcastReceiver {
	public static  String EXTRA_MESSAGE = "extra_message";
	public static  String NF_EXTRA_FROM_USER_ID = "nf_extra_from_user_id";
	public static  String NF_EXTRA_FROM_USERNAME = "nf_extra_from_username";
	public static  String NF_EXTRA_LAST_MSG_TYPE = "nf_extra_last_msg_type";
	public static  String NF_EXTRA_SESSION = "nf_extra_session";
	public static  String NF_TYPE = "nf_type";
	public static  String RECEIVER_OPTION = "option";
	
	/**
	 * 基础数据标识
	 */
	public final static int BASE_DATA=0x99;

	public final static int NF_TYPE_MESSAGE = 0x00;
	public final static int OPTION_SUB_CUSTOM = 0x01;
	public final static int OPTION_SUB_GROUP = 0x02;
	public final static int OPTION_SUB_MESSAGE_NOTIFY = 0x03;
	public final static int OPTION_SUB_NORMAL = 0x04;

	/**
	 * 请求联系人数据标识
	 */
	public final static int OPTION_SYNC_CONTACT = 0x10;
	/**
	 * 请求联系人列表数据标识
	 */
	public final static int OPTION_SYNC_CONTACTS = 0x11;
	/**
	 * 请求群组数据标识
	 */
	public final static int OPTION_SYNC_GROUP = 0x12;
	/**
	 * 请求群组列表数据标识
	 */
	public final static int OPTION_SYNC_GROUPS = 0x13;
	/**
	 * 请求群组成员标识
	 */
	public final static int OPTION_SYNC_GROUP_MEMBERS = 0x14;
	/**
	 * 手机联系人与平台联系人比较数据标识
	 * 用于同步手机联系人
	 */
	public final static int OPTION_SYNC_PHONES = 0x15;

	/**
	 * 处理联系人数据标识
	 */
	public final static int OPTION_CMD_CONTACT = 0x20;
	/**
	 * 处理联系人列表数据标识
	 */
	public final static int OPTION_CMD_CONTACTS = 0x21;
	/**
	 * 处理群组数据标识
	 */
	public final static int OPTION_CMD_GROUP = 0x22;
	/**
	 * 处理群组列表数据标识
	 */
	public final static int OPTION_CMD_GROUPS = 0x23;
	/**
	 * 处理群组成员标识
	 */
	public final static int OPTION_CMD_GROUP_MEMBERS = 0x24;
	/**
	 * 处理联系人与平台联系人比较数据标识
	 * 用于同步手机联系人
	 */
	public final static int OPTION_CMD_PHONES = 0x25;

	private Context context;
	protected Context getContext() {
		//return this.getContext();
		return context;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		//处理接收到的广播做相应的处理
		this.context=context;
		int option = intent.getIntExtra(RECEIVER_OPTION, 0);
		  LogUtil.d("IMSuperNotifyReceiver", "onReceive option " + option );
		switch (option) {
		case OPTION_SYNC_CONTACTS:
			onSyncContacts(context);
			break;
		case OPTION_SYNC_CONTACT:
			String id=intent.getStringExtra(IMNotifyReceiver.MESSAGE_SUB_CONTACTS_ID);
			onSyncContact(context, id);
			break;
		case BASE_DATA:
			//获取基础数据
			DebugUtil.debug("IMNotifyReceiver_base","get broadcast");
			onSyncBaseData(context);
			break;

		default:
			break;
		}

	}
	
	/**
	 * 获取基础数据
	 */
	public abstract void onSyncBaseData(Context context);

	/**
	 * 获取联系人信息
	 */
	public abstract void onSyncContact(Context context, String id);

	/**
	 * 获取联系人列表
	 */
	public abstract void onSyncContacts(Context context);

	/**
	 * 获取群组信息
	 */
	public abstract void onSyncGroup(Context context, int id);

	/**
	 * 获取群组列表
	 */
	public abstract void onSyncGroups(Context context);

	/**
	 * 获取群组成员
	 */
	public abstract void onSyncGroupMembers(Context context, int id);

	/**
	 * 处理联系人信息
	 */
	public abstract void onCmdContact(Context context, ECContacts contact);

	/** 处理联系人列表
	 * 
	 */
	public abstract void onCmdContacts(Context contex,ArrayList<ECContacts> contacts);

	/** 处理群组信息
	 * 
	 */
	public abstract void onCmdGroup(Context context, ECGroup group);

	/** 处理群组列表
	 * 
	 */
	public abstract void onCmdGroups(Context context,ArrayList<ECGroup> groups);

	/** 处理群组成员
	 * 
	 */
	public abstract void onCmdGroupMembers(Context context, ArrayList<ECContacts> contacts);

	/** 文字及文件消息
	 * 
	 */
	public abstract void onReceivedMessage(Context context, ECMessage msg);

	/** 群组消息
	 * 
	 */
	public abstract void onReceiveGroupNoticeMessage(Context context,
			ECGroupNoticeMessage notice);

	/** 
	 * 点击状态栏消息
	 */
	public abstract void onNotificationClicked(Context context, int notifyType,
			String sender);

	/** 状态栏消息
	 * 
	 */
	public abstract void onReceiveMessageNotify(Context context, ECMessageNotify notify);

	public abstract void onCallArrived(Context context, Intent intent);

	public void onSoftVersion(Context context, String version, String softDesc,
			boolean force) {

	}

}
