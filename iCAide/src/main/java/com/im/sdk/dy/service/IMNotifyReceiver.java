package com.im.sdk.dy.service;

import java.util.ArrayList;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.deya.hospital.util.DebugUtil;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

/**
 * 消息处理，数据处理  广播类
 * 用于后台服务操作数据
* @author  yung
* @date 创建时间：2016年1月15日 下午2:16:14 
* @version 1.0
 */
public class IMNotifyReceiver extends IMSuperNotifyReceiver {
	 public static final String ACTION_NOTIRECEIVER_IM = "com.im.sdk.ACTION_NOTIRECEIVER_IM";
    public static final String SERVICE_OPT_CODE = "service_opt_code";//貌似是uid
    public static final String MESSAGE_SUB_TYPE  = "message_type";
    public static final String MESSAGE_SUB_CONTACTS_ID  = "contacts_id";

    /** 来电 */
    public static final int EVENT_TYPE_CALL = 1;
    /** 消息推送 */
    public static final int EVENT_TYPE_MESSAGE = 2;
    /** 请求数据 */
    public static final int EVENT_TYPE_SYNC = 3;
    /** 处理请求数据 */
    public static final int EVENT_TYPE_CMD = 4;

    /**
     * 创建一个服务Intent
     * @return Intent
     */
    public Intent buildServiceAction(int optCode) {
        Intent notifyIntent = new Intent(getContext(), DYNotifyService.class);
        notifyIntent.putExtra("service_opt_code" , optCode);
        return notifyIntent;
    }

	/**
     * 创建一个服务消息Intent
     * @return Intent
     */
    public Intent buildMessageServiceAction(int subOptCode) {
        Intent intent = buildServiceAction(EVENT_TYPE_MESSAGE);
        intent.putExtra(MESSAGE_SUB_TYPE , subOptCode);
        return intent;
    }
    
    /**
     * 创建一个服务请求数据Intent
     * @param subOptCode
     * @return
     */
    public Intent buildSyncServiceAction(int subOptCode) {
        Intent intent = buildServiceAction(EVENT_TYPE_SYNC);
        intent.putExtra(MESSAGE_SUB_TYPE , subOptCode);
        LogUtil.d("IMNotifyReceiver", "buildSyncServiceAction subOptCode>>"+subOptCode );
        return intent;
    }
    
    /**
     * 创建一个服务处理请求数据Intent
     * @param subOptCode
     * @return
     */
    public Intent buildCmdServiceAction(int subOptCode) {
        Intent intent = buildServiceAction(EVENT_TYPE_CMD);
        intent.putExtra(MESSAGE_SUB_TYPE , subOptCode);
        return intent;
    }

    @Override
    public void onReceivedMessage(Context context, ECMessage msg) {
    	 LogUtil.i("IMNotifyReceiver", "onReceivedMessage" );
        Intent intent = buildMessageServiceAction(OPTION_SUB_NORMAL);
        intent.putExtra(EXTRA_MESSAGE , msg);
        context.startService(intent);
    }

    @Override
    public void onCallArrived(Context context, Intent intent) {
        Intent serviceAction = buildServiceAction(EVENT_TYPE_CALL);
        serviceAction.putExtras(intent);
        context.startService(serviceAction);
    }

    @Override
    public void onReceiveGroupNoticeMessage(Context context, ECGroupNoticeMessage notice) {
        Intent intent = buildMessageServiceAction(OPTION_SUB_GROUP);
        intent.putExtra(EXTRA_MESSAGE , notice);
        context.startService(intent);
    }

    @Override
    public void onNotificationClicked(Context context, int notifyType, String sender) {
      
        Intent intent = new Intent(context, LauncherActivity.class);
        intent.putExtra("Main_Session", sender);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onReceiveMessageNotify(Context context, ECMessageNotify notify) {
    	 LogUtil.i("IMNotifyReceiver", "onReceiveMessageNotify" );
        Intent intent = buildMessageServiceAction(OPTION_SUB_MESSAGE_NOTIFY);
        intent.putExtra(EXTRA_MESSAGE , notify);
        context.startService(intent);
    }
    
    @Override
    public void onSoftVersion(Context context, String version , String softDesc, boolean force) {
    	// TODO Auto-generated method stub
    	super.onSoftVersion(context, version, softDesc, force);
    	SDKCoreHelper.setSoftUpdate(version, softDesc, force);
    }


	@Override
	public void onSyncContact(Context context, String id) {
		// TODO Auto-generated method stub
		Intent intent = buildSyncServiceAction(OPTION_SYNC_CONTACT);
		intent.putExtra(MESSAGE_SUB_CONTACTS_ID, id);
        context.startService(intent);
	}

	@Override
	public void onSyncContacts(Context context) {
		// TODO Auto-generated method stub
		 LogUtil.i("IMNotifyReceiver", "onSyncContacts" );
		Intent intent = buildSyncServiceAction(OPTION_SYNC_CONTACTS);
        context.startService(intent);
	}

	@Override
	public void onSyncGroup(Context context, int id) {
		// TODO Auto-generated method stub
		Intent intent = buildSyncServiceAction(OPTION_SYNC_GROUP);
		intent.putExtra("id", id);
        context.startService(intent);
	}

	@Override
	public void onSyncGroups(Context context) {
		// TODO Auto-generated method stub
		Intent intent = buildSyncServiceAction(OPTION_SYNC_GROUPS);
        context.startService(intent);
	}

	@Override
	public void onSyncGroupMembers(Context context, int id) {
		// TODO Auto-generated method stub
		Intent intent = buildSyncServiceAction(OPTION_SYNC_GROUP_MEMBERS);
		intent.putExtra("id", id);
        context.startService(intent);
	}
	
	@Override
	public void onSyncBaseData(Context context) {
		// TODO Auto-generated method stub
		Intent intent = buildSyncServiceAction(BASE_DATA);
        context.startService(intent);
        DebugUtil.debug("IMNotifyReceiver_base","get broadcast and start services");
	}

	@Override
	public void onCmdContact(Context context, ECContacts contact) {
		// TODO Auto-generated method stub
		Intent intent = buildCmdServiceAction(OPTION_CMD_CONTACT);
		intent.putExtra("contact", contact);
        context.startService(intent);
	}

	@Override
	public void onCmdContacts(Context context, ArrayList<ECContacts> contacts) {
		// TODO Auto-generated method stub
		Intent intent = buildCmdServiceAction(OPTION_CMD_CONTACTS);
		Bundle bl=new Bundle();
		bl.putParcelableArrayList("contacts", contacts);
		intent.putExtra("data", bl);
        context.startService(intent);
	}

	@Override
	public void onCmdGroup(Context context, ECGroup group) {
		// TODO Auto-generated method stub
		Intent intent = buildCmdServiceAction(OPTION_CMD_GROUP);
		intent.putExtra("group", group);
        context.startService(intent);
	}

	@Override
	public void onCmdGroups(Context context, ArrayList<ECGroup> groups) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCmdGroupMembers(Context context, ArrayList<ECContacts> contacts) {
		// TODO Auto-generated method stub
		
	}


}
