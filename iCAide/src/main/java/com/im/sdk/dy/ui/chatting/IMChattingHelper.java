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

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.TipsMessage;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.DemoUtils;
import com.im.sdk.dy.common.utils.ECNotificationManager;
import com.im.sdk.dy.common.utils.ECPreferenceSettings;
import com.im.sdk.dy.common.utils.ECPreferences;
import com.im.sdk.dy.common.utils.FileAccessor;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.core.ClientUser;
import com.im.sdk.dy.core.DyMessage;
import com.im.sdk.dy.core.FriendCmdType;
import com.im.sdk.dy.core.MessageType;
import com.im.sdk.dy.core.SysMessgeType;
import com.im.sdk.dy.service.IMNotifyReceiver;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.storage.GroupNoticeSqlManager;
import com.im.sdk.dy.storage.GroupSqlManager;
import com.im.sdk.dy.storage.IMessageSqlManager;
import com.im.sdk.dy.storage.ImgInfoSqlManager;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.im.sdk.dy.ui.WorkTipsSettingsActivity;
import com.im.sdk.dy.ui.chatting.model.ImgInfo;
import com.im.sdk.dy.ui.contact.ContactLogic;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.im.sdk.dy.ui.group.DemoGroupNotice;
import com.im.sdk.dy.ui.group.GroupNoticeHelper;
import com.im.sdk.dy.ui.group.GroupService;
import com.lidroid.xutils.exception.DbException;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECMessage.Direction;
import com.yuntongxun.ecsdk.ECMessage.Type;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.PersonInfo;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
import com.yuntongxun.ecsdk.im.ECMessageDeleteNotify;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.ECMessageNotify.NotifyType;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 聊天帮助类
 * @author Jorstin Chan@容联•云通讯
 * @date 2014-12-12
 * @version 4.0
 */
public class IMChattingHelper implements OnChatReceiveListener,
		ECChatManager.OnDownloadMessageListener {

	private static final String TAG = "IM_PATH.IMChattingHelper";
	public static final String INTENT_ACTION_SYNC_MESSAGE = "com.im.sdk.dy_sync_message";
	public static final String GROUP_PRIVATE_TAG = "@priategroup.com";
	private static HashMap<String, SyncMsgEntry> syncMessage = new HashMap<String, SyncMsgEntry>();
	private static IMChattingHelper sInstance;
	private boolean isSyncOffline = false;
	private static Context context;
	Tools tools;

	public static IMChattingHelper getInstance() {
		if (sInstance == null) {
			sInstance = new IMChattingHelper();
		}
		
		return sInstance;
	}
	
	public static void  setContext(Context ct){
		context=ct;
	}

	/** 云通讯SDK聊天功能接口 */
	private ECChatManager mChatManager;
	/** 全局处理所有的IM消息发送回调 */
	private ChatManagerListener mListener;
	/** 是否是同步消息 */
	private boolean isFirstSync = false;

	private IMChattingHelper() {
		mChatManager = SDKCoreHelper.getECChatManager();
		mListener = new ChatManagerListener();
	}

	private void checkChatManager() {
		if (null == mChatManager)
			mChatManager = SDKCoreHelper.getECChatManager();
	}

	/**
	 * 消息发送报告
	 */
	private OnMessageReportCallback mOnMessageReportCallback;

	/**
	 * 发送ECMessage 消息
	 * 
	 * @param msg
	 */
	public static long sendECMessage(ECMessage msg) {
		getInstance().checkChatManager();
		// 获取一个聊天管理器
		ECChatManager manager = getInstance().mChatManager;
		if (manager != null) {
			// 调用接口发送IM消息
			boolean isShowChatName = ECPreferences
					.getSharedPreferences()
					.getBoolean(
							ECPreferenceSettings.SETTINGS_SHOW_CHATTING_NAME
									.getId(),
							false);
			msg.setMsgTime(System.currentTimeMillis());
			manager.sendMessage(msg, getInstance().mListener);
			// 保存发送的消息到数据库
			if (msg.getType() == Type.FILE
					&& msg.getBody() instanceof ECFileMessageBody) {
				ECFileMessageBody fileMessageBody = (ECFileMessageBody) msg
						.getBody();
				msg.setUserData("fileName=" + fileMessageBody.getFileName());
			}
		} else {
			msg.setMsgStatus(ECMessage.MessageStatus.FAILED);
		}
		return IMessageSqlManager.insertIMessage(msg,
				ECMessage.Direction.SEND.ordinal());
	}
	public static long reciewECMessage(ECMessage msg) {
		getInstance().checkChatManager();
		// 获取一个聊天管理器
		ECChatManager manager = getInstance().mChatManager;
		if (manager != null) {
			// 调用接口发送IM消息
			boolean isShowChatName = ECPreferences
					.getSharedPreferences()
					.getBoolean(
							ECPreferenceSettings.SETTINGS_SHOW_CHATTING_NAME
									.getId(),
							false);
			msg.setMsgTime(System.currentTimeMillis());
			manager.sendMessage(msg, getInstance().mListener);
			// 保存发送的消息到数据库
			if (msg.getType() == Type.FILE
					&& msg.getBody() instanceof ECFileMessageBody) {
				ECFileMessageBody fileMessageBody = (ECFileMessageBody) msg
						.getBody();
				msg.setUserData("fileName=" + fileMessageBody.getFileName());
			}
		} else {
			msg.setMsgStatus(ECMessage.MessageStatus.FAILED);
		}
		return IMessageSqlManager.insertIMessage(msg,
				Direction.RECEIVE.ordinal());
	}
	public static void sendFriendECMessage(ECMessage msg) {
		getInstance().checkChatManager();
		// 获取一个聊天管理器
		ECChatManager manager = getInstance().mChatManager;
		if (manager != null) {
			// 调用接口发送IM消息
			msg.setMsgTime(System.currentTimeMillis());
			String  res=manager.sendMessage(msg, getInstance().mListener);
		} else {
			msg.setMsgStatus(ECMessage.MessageStatus.FAILED);
		}
	}
	
	/**
	 * 群组邀请
	 * @param msg
	 */
	public static void sendGroupInvECMessage(ECMessage msg) {
		getInstance().checkChatManager();
		// 获取一个聊天管理器
		ECChatManager manager = getInstance().mChatManager;
		if (manager != null) {
			// 调用接口发送IM消息
			msg.setMsgTime(System.currentTimeMillis());
			String  res=manager.sendMessage(msg, getInstance().mListener);
		} else {
			msg.setMsgStatus(ECMessage.MessageStatus.FAILED);
		}
	}

	public void destroy() {
		if (syncMessage != null) {
			syncMessage.clear();
		}
		mListener = null;
		mChatManager = null;
		isFirstSync = false;
		sInstance = null;
	}

	/**
	 * 消息重发
	 * 
	 * @param msg
	 * @return
	 */
	public static long reSendECMessage(ECMessage msg) {
		getInstance().checkChatManager();
		ECChatManager manager = getInstance().mChatManager;
		if (manager != null) {
			// 调用接口发送IM消息
			String oldMsgId = msg.getMsgId();

			if (msg.getType() == Type.IMAGE
					&& IMessageSqlManager.isFireMsg(oldMsgId)) {
				msg.setUserData("fireMessage");
			}

			manager.sendMessage(msg, getInstance().mListener);
			if (msg.getType() == ECMessage.Type.IMAGE) {
				ImgInfo imgInfo = ImgInfoSqlManager.getInstance().getImgInfo(
						oldMsgId);
				if (imgInfo == null
						|| TextUtils.isEmpty(imgInfo.getBigImgPath())) {
					return -1;
				}
				String bigImagePath = new File(FileAccessor.getImagePathName(),
						imgInfo.getBigImgPath()).getAbsolutePath();
				imgInfo.setMsglocalid(msg.getMsgId());
				ECFileMessageBody body = (ECFileMessageBody) msg.getBody();
				body.setLocalUrl(bigImagePath);
				BitmapFactory.Options options = DemoUtils
						.getBitmapOptions(new File(FileAccessor.IMESSAGE_IMAGE,
								imgInfo.getThumbImgPath()).getAbsolutePath());
				msg.setUserData("outWidth://" + options.outWidth
						+ ",outHeight://" + options.outHeight + ",THUMBNAIL://"
						+ msg.getMsgId());
				ImgInfoSqlManager.getInstance().updateImageInfo(imgInfo);
			}
			// 保存发送的消息到数据库
			return IMessageSqlManager.changeResendMsg(msg.getId(), msg);
		}
		return -1;
	}

	public static long sendImageMessage(ImgInfo imgInfo, ECMessage message) {
		getInstance().checkChatManager();
		ECChatManager manager = getInstance().mChatManager;
		if (manager != null) {
			// 调用接口发送IM消息
			if (ChattingFragment.isFireMsg
					|| IMessageSqlManager.isFireMsg(message.getMsgId())) {
				message.setUserData("fireMessage");
			}
			manager.sendMessage(message, getInstance().mListener);

			if (TextUtils.isEmpty(message.getMsgId())) {
				return -1;
			}
			imgInfo.setMsglocalid(message.getMsgId());
			BitmapFactory.Options options = DemoUtils
					.getBitmapOptions(new File(FileAccessor.IMESSAGE_IMAGE,
							imgInfo.getThumbImgPath()).getAbsolutePath());
			message.setUserData("outWidth://" + options.outWidth
					+ ",outHeight://" + options.outHeight + ",THUMBNAIL://"
					+ message.getMsgId() + ",PICGIF://" + imgInfo.isGif);
			long row = IMessageSqlManager.insertIMessage(message,
					ECMessage.Direction.SEND.ordinal());

			if (row != -1) {
				return ImgInfoSqlManager.getInstance().insertImageInfo(imgInfo);
			}
		}
		return -1;

	}

	public void getPersonInfo() {
		LogUtil.d(TAG, "[getPersonInfo] currentVersion :");
		final ClientUser clientUser = CCPAppManager.getClientUser();
		if (clientUser == null) {
			return;
		}
		LogUtil.d("getPersonInfo", "[getPersonInfo] clientUser.getUserId() :"
				+ clientUser.getUserId());
		if (clientUser.getpVersion() < mServicePersonVersion) {
			ECDevice.getPersonInfo(clientUser.getUserId(),
					new ECDevice.OnGetPersonInfoListener() {
						@Override
						public void onGetPersonInfoComplete(ECError e,
								PersonInfo p) {
							// if (e.errorCode == SdkErrorCode.REQUEST_SUCCESS
							// && p != null) {
							// clientUser.setpVersion(p.getVersion());
							// clientUser.setSex(p.getSex().ordinal() + 1);
							// //clientUser.setUserName(p.getNickName());
							// clientUser.setSignature(p.getSign());
							// if (!TextUtils.isEmpty(p.getBirth())) {
							// clientUser.setBirth(DateUtil
							// .getActiveTimelong(p.getBirth()));
							// }
							// String newVersion = clientUser.toString();
							// LogUtil.d(TAG,
							// "[getPersonInfo -result] ClientUser :"
							// + newVersion);
							// try {
							// ECPreferences
							// .savePreference(
							// ECPreferenceSettings.SETTINGS_REGIST_AUTO,
							// newVersion, true);
							// } catch (InvalidClassException e1) {
							// e1.printStackTrace();
							// }
							// }
							//
						}
					});
		}
	}

	private class ChatManagerListener implements
			ECChatManager.OnSendMessageListener {

		@Override
		public void onSendMessageComplete(ECError error, ECMessage message) {
			if (message == null) {
				return;
			}
			// 处理ECMessage的发送状态
			if (message != null) {
				if (message.getType() == ECMessage.Type.VOICE) {
					try {
						DemoUtils.playNotifycationMusic(
								CCPAppManager.getContext(),
								"sound/voice_message_sent.mp3");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				IMessageSqlManager.setIMessageSendStatus(message.getMsgId(),
						message.getMsgStatus().ordinal());
				IMessageSqlManager.notifyMsgChanged(message.getSessionId());
				if (mOnMessageReportCallback != null) {
					mOnMessageReportCallback.onMessageReport(error, message);
				}
				return;
			}
		}

		@Override
		public void onProgress(String msgId, int total, int progress) {
			// 处理发送文件IM消息的时候进度回调
			LogUtil.d(TAG, "[IMChattingHelper - onProgress] msgId：" + msgId
					+ " ,total：" + total + " ,progress:" + progress);
		}

	}

	public static void setOnMessageReportCallback(
			OnMessageReportCallback callback) {
		getInstance().mOnMessageReportCallback = callback;
	}

	public interface OnMessageReportCallback {
		void onMessageReport(ECError error, ECMessage message);

		void onPushMessage(String sessionId, List<ECMessage> msgs);
	}

	private int getMaxVersion() {
		int maxVersion = IMessageSqlManager.getMaxVersion();
		int maxVersion1 = GroupNoticeSqlManager.getMaxVersion();
		return maxVersion > maxVersion1 ? maxVersion : maxVersion1;
	}

	/**
	 * 收到新的IM文本和附件消息
	 */
	@Override
	public void OnReceivedMessage(ECMessage msg) {
		
		LogUtil.d(TAG, "[OnReceivedMessage] show notice true"+msg);
		if (msg == null) {
			return;
		}
		
		Intent intent=new Intent();
		intent.setAction("com.deyamanda.gkgj");
		String sessionId=msg.getSessionId();
		context.sendBroadcast(intent);
		postReceiveMessage(msg, true);
	}

	/**
	 * 处理接收消息
	 * 
	 * @param msg
	 * @param showNotice
	 */
	private synchronized void postReceiveMessage(ECMessage msg,

			boolean showNotice) {
		
		LogUtil.e("reuserdata_init", "msg.getUserData()》》" + msg.getUserData()
				+ "   msg.getType()>>" + msg.getType().name()
				+ "   msg.getDirection()>>" + msg.getDirection().name()+" msg.name>>"+msg.getNickName());

		// 接收到的IM消息，根据IM消息类型做不同的处理
		// IM消息类型：ECMessage.Type
			if (msg.getType() != ECMessage.Type.TXT) {
			}else{
				
				String userData=msg.getUserData();
				if(null!=userData&&!userData.equals("")&&userData.contains(DyMessage.IM_MYDATA)){
					DyMessage dyMessage=new DyMessage(userData);
					if(null!=dyMessage){
						if(dyMessage.getMessageType()==MessageType.FRIEND.ordinal()){
						}else if(dyMessage.getMessageType()==MessageType.SYSTEM.ordinal()){

							//处理系统
							cmdSysNotice(msg,dyMessage.getValue());
						}else if(dyMessage.getMessageType()==MessageType.GROUP.ordinal()){
							LogUtil.i("im_message",MessageType.GROUP.ordinal()+"");
							//群组操作
							//cmdGroup(dyMessage.getValue());
						}
					}
					
				}
			}
		

		if (IMessageSqlManager
				.insertIMessage(msg, msg.getDirection().ordinal()) <= 0) {
			return;
		}

		if (mOnMessageReportCallback != null) {
			ArrayList<ECMessage> msgs = new ArrayList<ECMessage>();
			msgs.add(msg);
			mOnMessageReportCallback.onPushMessage(msg.getSessionId(), msgs);
		}

		// 是否状态栏提示
		if (showNotice) {
			showNotification(msg,1);
		}
	}

	/*
	 * 通知栏处理 
	 */
	private static void showNotification(ECMessage msg,int Type) {
		if (checkNeedNotification(msg.getSessionId())) {
			ECNotificationManager.getInstance().forceCancelNotification();
			String lastMsg = "";
			if (msg.getType() == ECMessage.Type.TXT) {
				lastMsg = ((ECTextMessageBody) msg.getBody()).getMessage();
			}
			ECContacts contact = ContactSqlManager.getContact(msg.getForm());
			if (contact == null) {
				return;
			}

			if(Type==2){
			ECNotificationManager.getInstance()
					.showCustomNewMessageNotification2(
							CCPAppManager.getContext(), lastMsg,
							contact,msg);
			}else{
				ECNotificationManager.getInstance()
						.showCustomNewMessageNotification(
								CCPAppManager.getContext(), lastMsg,
								contact,msg);
			}
		}
	}

	public static void checkDownFailMsg() {
		getInstance().postCheckDownFailMsg();
	}

	private void postCheckDownFailMsg() {
		List<ECMessage> downdFailMsg = IMessageSqlManager.getDowndFailMsg();
		if (downdFailMsg == null || downdFailMsg.isEmpty()) {
			return;
		}
		for (ECMessage msg : downdFailMsg) {
			ECImageMessageBody body = (ECImageMessageBody) msg.getBody();
			body.setThumbnailFileUrl(body.getRemoteUrl() + "_thum");
			if (syncMessage != null) {
				syncMessage.put(msg.getMsgId(), new SyncMsgEntry(false, true,
						msg));
			}
			if (mChatManager != null) {
				mChatManager.downloadThumbnailMessage(msg, this);
			}
		}

	}

	/**
	 * 是否需要状态栏通知
	 * 
	 * @param contactId
	 */
	public static boolean checkNeedNotification(String contactId) {
		String currentChattingContactId = ECPreferences
				.getSharedPreferences()
				.getString(
						ECPreferenceSettings.SETTING_CHATTING_CONTACTID.getId(),
						(String) ECPreferenceSettings.SETTING_CHATTING_CONTACTID
								.getDefaultValue());
		if (contactId == null) {
			return true;
		}
		// 当前聊天
		if (contactId.equals(currentChattingContactId)) {
			return false;
		}
		// 群组免打扰
		if (contactId.toUpperCase().startsWith("G")) {
			return GroupSqlManager.isGroupNotify(contactId);
		}
		return true;
	}

	@Override
	public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage notice) {
		if (notice == null) {
			return;
		}

		// 接收到的群组消息，根据群组消息类型做不同处理
		// 群组消息类型：ECGroupMessageType
		GroupNoticeHelper.insertNoticeMessage(notice,
				new GroupNoticeHelper.OnPushGroupNoticeMessageListener() {

					@Override
					public void onPushGroupNoticeMessage(DemoGroupNotice system) {
						IMessageSqlManager
								.notifyMsgChanged(GroupNoticeSqlManager.CONTACT_ID);

						ECMessage msg = ECMessage
								.createECMessage(ECMessage.Type.TXT);
						msg.setSessionId(system.getSender());
						msg.setForm(system.getSender());
						ECTextMessageBody tx = new ECTextMessageBody(system
								.getContent());
						msg.setBody(tx);
						// 是否状态栏提示
						//showNotification(msg);
					}
				});

	}

	private int mHistoryMsgCount = 0;

	@Override
	public void onOfflineMessageCount(int count) {
		mHistoryMsgCount = count;
	}

	@Override
	public int onGetOfflineMessage() {
		// 获取全部的离线历史消息
		return ECDevice.SYNC_OFFLINE_MSG_ALL;
	}

	private ECMessage mOfflineMsg = null;

	@Override
	public void onReceiveOfflineMessage(List<ECMessage> msgs) {
		// 离线消息的处理可以参考 void OnReceivedMessage(ECMessage msg)方法
		// 处理逻辑完全一样
		// 参考 IMChattingHelper.java
		LogUtil.d(TAG, "[onReceiveOfflineMessage] show notice false");
		if (msgs != null && !msgs.isEmpty() && !isFirstSync)
			isFirstSync = true;
		for (ECMessage msg : msgs) {
			mOfflineMsg = msg;
			postReceiveMessage(msg, false);
		}
	}

	@Override
	public void onReceiveOfflineMessageCompletion() {
		if (mOfflineMsg == null) {
			return;
		}
		// SDK离线消息拉取完成之后会通过该接口通知应用
		// 应用可以在此做类似于Loading框的关闭，Notification通知等等
		// 如果已经没有需要同步消息的请求时候，则状态栏开始提醒
		ECMessage lastECMessage = mOfflineMsg;
		try {
			if (lastECMessage != null && mHistoryMsgCount > 0 && isFirstSync) {
				showNotification(lastECMessage,1);
				// lastECMessage.setSessionId(lastECMessage.getTo().startsWith("G")?lastECMessage.getTo():lastECMessage.getForm());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		isFirstSync = isSyncOffline = false;
		// 无需要同步的消息
		CCPAppManager.getContext().sendBroadcast(
				new Intent(INTENT_ACTION_SYNC_MESSAGE));
		mOfflineMsg = null;
	}

	public int mServicePersonVersion = 0;

	@Override
	public void onServicePersonVersion(int version) {
		mServicePersonVersion = version;
	}

	/**
	 * 客服消息
	 * 
	 * @param msg
	 */
	@Override
	public void onReceiveDeskMessage(ECMessage msg) {
		LogUtil.d(TAG, "[onReceiveDeskMessage] show notice true");
		OnReceivedMessage(msg);
	}

	@Override
	public void onSoftVersion(String version, int sUpdateMode) {
		// Deprecated
	}

	public static boolean isSyncOffline() {
		return getInstance().isSyncOffline;
	}

	/**
	 * 下载
	 */
	@Override
	public void onDownloadMessageComplete(ECError e, ECMessage message) {
		LogUtil.e("reuserdata_Complete",
				"msg.getUserData()》》" + message.getUserData());
		if (e.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
			if (message == null)
				return;
			// 处理发送文件IM消息的时候进度回调
			LogUtil.d(TAG,
					"[onDownloadMessageComplete] msgId：" + message.getMsgId());
			postDowloadMessageResult(message);

			if (message.getType() == Type.VIDEO
					&& mOnMessageReportCallback != null
					&& message.getDirection() == Direction.RECEIVE
					&& mOnMessageReportCallback instanceof ChattingFragment) {

				((ChattingFragment) mOnMessageReportCallback)
						.dismissPostingDialog();
			}

		} else {
			// 重试下载3次
			SyncMsgEntry remove = syncMessage.remove(message.getMsgId());
			if (remove == null) {
				return;
			}
			LogUtil.d(TAG,
					"[onDownloadMessageComplete] download fail , retry ："
							+ remove.retryCount);
			retryDownload(remove);
		}
	}

	@Override
	public void onProgress(String msgId, int totalByte, int progressByte) {
		// 处理发送文件IM消息的时候进度回调 //download
		LogUtil.d(TAG, "[IMChattingHelper - onProgress] msgId: " + msgId
				+ " , totalByte: " + totalByte + " , progressByte:"
				+ progressByte);

	}

	/**
	 * 重试下载3次
	 * 
	 * @param entry
	 */
	private void retryDownload(SyncMsgEntry entry) {
		if (entry == null || entry.msg == null || entry.isRetryLimit()) {
			return;
		}
		entry.increase();
		// download ..
		if (mChatManager != null) {
			if (entry.thumbnail) {
				mChatManager.downloadThumbnailMessage(entry.msg, this);
			} else {
				mChatManager.downloadMediaMessage(entry.msg, this);
			}
		}
		syncMessage.put(entry.msg.getMsgId(), entry);
	}

	private synchronized void postDowloadMessageResult(ECMessage message) {
		if (message == null) {
			return;
		}
		LogUtil.e("reuserdata_postdown",
				"msg.getUserData()》》" + message.getUserData());
		if (message.getType() == ECMessage.Type.VOICE) {
			ECVoiceMessageBody voiceBody = (ECVoiceMessageBody) message
					.getBody();
			voiceBody.setDuration(DemoUtils.calculateVoiceTime(voiceBody
					.getLocalUrl()));
		} else if (message.getType() == ECMessage.Type.IMAGE) {
			ImgInfo thumbImgInfo = ImgInfoSqlManager.getInstance()
					.getThumbImgInfo(message);
			if (thumbImgInfo == null) {
				return;
			}
			ImgInfoSqlManager.getInstance().insertImageInfo(thumbImgInfo);
			BitmapFactory.Options options = DemoUtils
					.getBitmapOptions(new File(FileAccessor.getImagePathName(),
							thumbImgInfo.getThumbImgPath()).getAbsolutePath());
			message.setUserData("outWidth://" + options.outWidth
					+ ",outHeight://" + options.outHeight + ",THUMBNAIL://"
					+ message.getMsgId() + ",PICGIF://" + thumbImgInfo.isGif);
		}
		LogUtil.e("reuserdata_postdown", "ready dwon  msg.getUserData()》》"
				+ message.getUserData());
		if (IMessageSqlManager.updateIMessageDownload(message) <= 0) {
			return;
		}
		if (mOnMessageReportCallback != null) {
			mOnMessageReportCallback.onMessageReport(null, message);
		}
		boolean showNotice = true;
		SyncMsgEntry remove = syncMessage.remove(message.getMsgId());
		if (remove != null) {
			showNotice = remove.showNotice;
			if (mOnMessageReportCallback != null && remove.msg != null) {
				ArrayList<ECMessage> msgs = new ArrayList<ECMessage>();
				msgs.add(remove.msg);
				mOnMessageReportCallback.onPushMessage(
						remove.msg.getSessionId(), msgs);
			}
		}
		if (showNotice)
			showNotification(message,1);
	}

	public class SyncMsgEntry {
		// 是否是第一次初始化同步消息
		boolean showNotice = false;
		boolean thumbnail = false;

		// 重试下载次数
		private int retryCount = 1;
		ECMessage msg;

		public SyncMsgEntry(boolean showNotice, boolean thumbnail,
				ECMessage message) {
			this.showNotice = showNotice;
			this.msg = message;
			this.thumbnail = thumbnail;
		}

		public void increase() {
			retryCount++;
		}

		public boolean isRetryLimit() {
			return retryCount >= 3;
		}
	}

	@Override
	public void onReceiveMessageNotify(ECMessageNotify msg) {
		if (msg.getNotifyType() == NotifyType.DELETE) {
			ECMessageDeleteNotify deleteMsg = (ECMessageDeleteNotify) msg;
			IMessageSqlManager.updateMsgReadStatus(msg.getMsgId(), true);
			IMessageSqlManager.deleteLocalFileAfterFire(msg.getMsgId());
			if (mOnMessageReportCallback != null) {
				mOnMessageReportCallback.onMessageReport(null, null);
			}
		}

	}
	
	/**
	 * 处理好友消息
	 * @param msg
	 * @param code
	 * @return
	 */
	private boolean cmdFriend(ECMessage msg,int code){
		if(code==FriendCmdType.REQUEST.ordinal()){
			//好友请求，插入聊天数据
			//addContacts(msg,-1);
			
			ContactLogic.addContacts(msg,0);
			msg.setType(Type.TXT);
			ECTextMessageBody body=new ECTextMessageBody("");
			body.setMessage("成为好友");
			msg.setBody(body);
			
			//发送查询广播
			Intent intent=new Intent();
	    	intent.setAction(IMNotifyReceiver.ACTION_NOTIRECEIVER_IM);
	    	intent.putExtra(IMNotifyReceiver.RECEIVER_OPTION, IMNotifyReceiver.OPTION_SYNC_CONTACT);
	    	intent.putExtra("id", msg.getForm());
	    	MyAppliaction.getContext().sendBroadcast(intent);
	    	
		}else if(code==FriendCmdType.ACCEPT.ordinal()){
			//被接受，发送广播，添加好友到数据库
			ContactLogic.addContacts(msg,0);
			msg.setType(Type.TXT);
			ECTextMessageBody body=new ECTextMessageBody("");
			body.setMessage("成为好友");
			msg.setBody(body);
			
			//发送查询广播
			Intent intent=new Intent();
	    	intent.setAction(IMNotifyReceiver.ACTION_NOTIRECEIVER_IM);
	    	intent.putExtra(IMNotifyReceiver.RECEIVER_OPTION, IMNotifyReceiver.OPTION_SYNC_CONTACT);
	    	intent.putExtra("id", msg.getForm());
	    	MyAppliaction.getContext().sendBroadcast(intent);
			
		}else if(code==FriendCmdType.REFUSE.ordinal()){
			//被拒绝， 不做操作,不插入聊天数据
			return true;
		}else if(code==FriendCmdType.DEL.ordinal()){
			//被删除，操作本地不插入聊天数据
			return true;
		}
		return false;
	}
	
	/**
	 * 处理系统消息
	 * @param msg
	 * @param json
	 */
	private void cmdSysNotice(ECMessage msg,String json) {
		// TODO Auto-generated method stub
		JSONObject j;

		tools=new Tools(context, Constants.AC);
		try {
			j = new JSONObject(json);
			if(j.getInt("type")==SysMessgeType.SYSMESSAGE.ordinal()){
				msg.setNickName("护理工作间");
				ContactLogic.addAdminContacts(msg,999);


			}else if(j.getInt("type")==2){
				msg.setNickName("工作提醒");
				TipsMessage tipsMessage=new TipsMessage();
				tipsMessage.setId(j.optInt("id",-1));
				tipsMessage.setP(j.optInt("p",0));
				if(tipsMessage.getP()==1){
					msg.setNickName("工作提醒");
				}else{
					msg.setNickName("注册提醒");
				}

				if(j.has("data")){
					JSONObject dataObj=j.optJSONObject("data");
					tipsMessage.setDepartmentName(dataObj.optString("departmentName"));
					tipsMessage.setUserName(dataObj.optString("userName"));
					tipsMessage.setTime(dataObj.optString("time"));
					tipsMessage.setContent(dataObj.optString("content"));

					if(null!=tools.getValue(Constants.MOBILE)){
					tipsMessage.setMobile(tools.getValue(Constants.MOBILE));
					}
					try {
						DataBaseHelper.getDbUtilsInstance(context)
                                .save(tipsMessage);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				ContactLogic.addAdminContacts(msg,1000);
				if(tipsMessage.getP()==1){
					boolean showTaskTip=tools.getValue_int(WorkTipsSettingsActivity.OPQUESTIONLIST)==0;//任务提醒
					if(showTaskTip){
						showNotification(msg,2);
					}
				}else if(tipsMessage.getP()==2){
					boolean showTaskTip=tools.getValue_int(WorkTipsSettingsActivity.REGISTERTIPS)==0;//注册提醒
					if(showTaskTip){
						showNotification(msg,2);
					}
				}

			}
			
			 //GroupService.applyGroup(j.getString("gid"), "", null);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 处理群组消息
	 * @param json
	 */
	private void cmdGroup(String json){
		//{"gname":"扣扣","gid":"gg80070424541","type":1,"name":"yung"}
		JSONObject j;
		try {
			j = new JSONObject(json);
			TextUtils.isEmpty(j.getString("gid"));
			 GroupService.applyGroup(j.getString("gid"), "", null);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
