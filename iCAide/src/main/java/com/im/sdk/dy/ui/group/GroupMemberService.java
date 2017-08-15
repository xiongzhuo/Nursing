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
package com.im.sdk.dy.ui.group;

import com.deya.acaide.R;
import com.deya.hospital.util.DebugUtil;
import com.google.gson.JsonObject;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.core.ClientUser;
import com.im.sdk.dy.core.DyMessage;
import com.im.sdk.dy.core.GroupCmdType;
import com.im.sdk.dy.core.MessageType;
import com.im.sdk.dy.storage.GroupMemberSqlManager;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.im.sdk.dy.ui.account.AccountLogic;
import com.im.sdk.dy.ui.chatting.IMChattingHelper;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.ECMessage.Type;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECGroupMember;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ESpeakStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 群组成员同步接口
 * @author Jorstin Chan@容联•云通讯
 * @date 2014-12-29
 * @version 4.0
 */
public class GroupMemberService {

    public static final String TAG = "GroupMemberService";
    private static GroupMemberService sInstence;
    public static GroupMemberService getInstance() {
        if(sInstence == null) {
            sInstence = new GroupMemberService();
        }
        return sInstence;
    }

    /**SDK 访问接口*/
    private ECGroupManager mGroupManager;
    /**群组成员同步完成回调*/
    private OnSynsGroupMemberListener mGroupMemberListener;
    private GroupMemberService() {
        mGroupManager = SDKCoreHelper.getECGroupManager();
    }

    public static void synsGroupMember(final String groupId) {
        getGroupManager();
        if(getInstance().mGroupManager == null) {
            return ;
        }
        ECGroupManager groupManager = getInstance().mGroupManager;
        groupManager.queryGroupMembers(groupId, new ECGroupManager.OnQueryGroupMembersListener() {

            @Override
            public void onQueryGroupMembersComplete(ECError error,
                                                    List<ECGroupMember> members) {
                if(getInstance().isSuccess(error)) {
                    if(members == null || members.isEmpty()) {
                        GroupMemberSqlManager.delAllMember(groupId);
                    } else {

                        LogUtil.d(TAG , "[synsGroupMember] members size :" +members.size());
                        ArrayList<String> accounts = GroupMemberSqlManager.getGroupMemberAccounts(groupId);
                        ArrayList<String> ids = new ArrayList<String>();
                        
                        for (int i = 0; i <members.size(); i++) {
                        	ECGroupMember member=members.get(i);
                        	ids.add(member.getVoipAccount());
                            if(member.getVoipAccount().equals(CCPAppManager.getClientUser().getUserId())&&member.getVoipAccount().equals(member.getDisplayName())){
                            	members.get(i).setDisplayName(CCPAppManager.getClientUser().getUserName());
                            	//更新群名片
                            	AccountLogic.setMemberInfo(groupId, CCPAppManager.getClientUser());
                            }
                             
						}
                       /*      
                        for(ECGroupMember member :members) {
                            ids.add(member.getVoipAccount());
                            if(member.getVoipAccount().equals(CCPAppManager.getClientUser().getUserId())&&member.getVoipAccount().equals(member.getDisplayName())){
                            	//更新群名片
                            	AccountLogic.setMemberInfo(groupId, CCPAppManager.getClientUser());
                            	
                            }
                        }*/

                        // 查找不是群组成员
                        if(accounts != null && !accounts.isEmpty()) {
                            for(String id : accounts) {
                                if(ids.contains(id)) {
                                    continue;
                                }
                                // 不是群组成员、从数据库删除
                                GroupMemberSqlManager.delMember(groupId, id);
                            }
                        }
                        GroupMemberSqlManager.insertGroupMembers(members);
                    }

                    getInstance().notify(groupId);
                }
            }

        });
    }

    /**
     * @param groupId
     */
    private void notify(final String groupId) {
        if(getInstance().mGroupMemberListener != null) {
            getInstance().mGroupMemberListener.onSynsGroupMember(groupId);
        }
    }

    /**
     * 邀请成员加入群组
     * @param groupId 群组ID
     * @param reason 邀请原因
     * @param confirm 是否需要对方确认
     * @param members 邀请的成员
     */
    public static void inviteMembers(String groupId ,String reason ,final ECGroupManager.InvitationMode confirm , String[] members,final ArrayList<ECContacts> contactlist,final GroupServiceInterface inter) {
        getGroupManager();
        inviteMembers(groupId, reason, confirm, members, new ECGroupManager.OnInviteJoinGroupListener() {

            @Override
            public void onInviteJoinGroupComplete(ECError error, String groupId,
                                                  String[] members) {
                if (getInstance().isSuccess(error)) {
                    if (confirm == ECGroupManager.InvitationMode.FORCE_PULL) {
                        GroupMemberSqlManager.insertGroupMembers(groupId, members);
                        
                        if(null!=contactlist&&contactlist.size()>0&&members!=null&&members.length>0){
	                        HashMap<String, String> map=new HashMap<String, String>();
	                        for (int i = 0; i < members.length; i++) {
	                        	map.put(members[i], members[i])	;
							}
	                        
	                        StringBuffer sb=new StringBuffer("");
	                        
	                        for (int j = 0; j < contactlist.size(); j++) {
								if(map.containsKey(contactlist.get(j).getContactid())){
									sb.append(","+ contactlist.get(j).getRname());
								}
							}
	                        
	                        if(sb.length()>1){
	                        	//邀请成功后，发送群消息
	                            SendTextMessage(groupId,sb.substring(1).toString()+" 加入群组");
	                            inter.onInvateComplet();
	                        }
	                        
                        }
                        
                    } else {
                        ToastUtil.showMessage(R.string.invite_wating_replay);
                    }
                } else {
                    ToastUtil.showMessage("邀请成员失败[" + error.errorCode + "]");
                }
                getInstance().notify(groupId);

            }
        });

   }
    
    
    private static void SendTextMessage(String mRecipients,String text) {
        // 组建一个待发送的ECMessage
        ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
        // 设置消息接收者
        msg.setTo(mRecipients);
        msg.setSessionId(mRecipients);
        DebugUtil.debug("iniIm", "to id>>"+mRecipients);
        
        
        // 创建一个文本消息体，并添加到消息对象中 
        ECTextMessageBody msgBody = new ECTextMessageBody(text.toString());
        
        msg.setBody(msgBody);
        try {
            // 发送消息，该函数见上
            long rowId = -1;
                rowId = IMChattingHelper.sendECMessage(msg);
            // 通知列表刷新
            msg.setId(rowId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static void inviteMembers(ECGroup mGroup, String[] members) {
    	for (int i = 0; i < members.length; i++) {
    		sendGroupInvMessage(mGroup, members[i]);
		}
   }

    private static void sendGroupInvMessage(ECGroup mGroup,String mRecipients) {
		ClientUser user = CCPAppManager.getClientUser();
		ECMessage msg = ECMessage.createECMessage(Type.TXT);

		ECTextMessageBody msgBody = new ECTextMessageBody(user.getUserName()+" 邀请您加入群组 "+mGroup.getName());
		
		msg.setBody(msgBody);
		// 设置消息接收者
		msg.setTo(mRecipients);
		msg.setSessionId(mRecipients);
		JsonObject j = new JsonObject();
		j.addProperty("type", GroupCmdType.INVITEJOIN.ordinal());
		j.addProperty("gid", mGroup.getGroupId());
		j.addProperty("gname", mGroup.getName());
		j.addProperty("name", user.getUserName());
		msg.setUserData(new DyMessage(MessageType.GROUP, j.toString())
				.toMessgeUserData());
		LogUtil.d("sendFriendMessage",
				new DyMessage(MessageType.FRIEND, j.toString())
						.toMessgeUserData());
		IMChattingHelper.sendFriendECMessage(msg);

	}

    public static void inviteMembers(String groupId ,String reason ,final ECGroupManager.InvitationMode confirm , String[] members , ECGroupManager.OnInviteJoinGroupListener l) {
        getGroupManager();
        getInstance().mGroupManager.inviteJoinGroup(groupId, reason, members, confirm, l);
    }

    /**
     * 将成员移除出群组
     * @param groupid 群组ID
     * @param member 移除出的群组成员
     */
    public static void removerMember(String groupid , String member) {
        getGroupManager();
        getInstance().mGroupManager.deleteGroupMember(groupid, member, new ECGroupManager.OnDeleteGroupMembersListener() {

            @Override
            public void onDeleteGroupMembersComplete(ECError error, String groupId, String members) {
                if (getInstance().isSuccess(error)) {
                    GroupMemberSqlManager.delMember(groupId, members);
                } else {
                    ToastUtil.showMessage("移除成员失败[" + error.errorCode + "]");
                }
                getInstance().notify(groupId);
            }
        });

    }

    /**
     * 设置群组成员禁言状态
     * @param groupId
     * @param member
     * @param enabled
     */
    public static void forbidMemberSpeakStatus(final String groupId , final String member ,final boolean enabled ) {
        getGroupManager();
        ESpeakStatus speakStatus = new ESpeakStatus();
        speakStatus.setOperation(enabled ? 2 : 1);
        getInstance().mGroupManager.forbidMemberSpeakStatus(groupId, member, speakStatus, new ECGroupManager.OnForbidMemberSpeakStatusListener() {
            @Override
            public void onForbidMemberSpeakStatusComplete(ECError error, String groupId, String member) {
                if (getInstance().isSuccess(error)) {
                    GroupMemberSqlManager.updateMemberSpeakState(groupId, member, enabled);
                } else {
                    ToastUtil.showMessage("设置失败[" + error.errorCode + "]");
                }
//                getInstance().notify(groupId);
            }

        });
    }


    public static void queryGroupMemberCard(final String groupId , final String member) {
        getGroupManager();
        getInstance().mGroupManager.queryMemberCard(member, groupId, new ECGroupManager.OnQueryMemberCardListener() {
            @Override
            public void onQueryMemberCardComplete(ECError error, ECGroupMember member) {
                if (getInstance().isSuccess(error)) {
                    if (member != null) {
                        LogUtil.d(TAG, "groupmember  " + member.toString());
                    }
                    return;
                }
                LogUtil.e(TAG, "query group member card fail " +
                        ", errorCode=" + error.errorCode);
            }
        });
    }

    public static void modifyGroupMemberCard(final String groupId , final String member) {
        getGroupManager();
        ECGroupMember groupMember = new ECGroupMember();
        groupMember.setBelong(groupId);
        groupMember.setVoipAccount(member);
        groupMember.setDisplayName("贾2");
        groupMember.setSex(2);
        groupMember.setRemark("詹季春改了贾政阳的名片");
        groupMember.setTel("18813192117");
        getInstance().mGroupManager.modifyMemberCard(groupMember, new ECGroupManager.OnModifyMemberCardListener() {
            @Override
            public void onModifyMemberCardComplete(ECError error, ECGroupMember member) {
                if (getInstance().isSuccess(error)) {
                    if (member != null) {
                        LogUtil.d(TAG, "groupmember  " + member.toString());
                    }
                    return;
                }
                LogUtil.e(TAG, "modify group member card fail " +
                        ", errorCode=" + error.errorCode);
            }

        });
    }


    public static void setGroupMessageOption(String groupid) {
        getGroupManager();

    }

    /**
     * 请求是否成功
     * @param error
     * @return
     */
    private boolean isSuccess(ECError error) {
        if(error.errorCode == SdkErrorCode.REQUEST_SUCCESS)  {
            return true;
        }
        return false;
    }

    public static void getGroupManager() {
        getInstance().mGroupManager = SDKCoreHelper.getECGroupManager();
    }


    /**
     * 注入SDK群组成员同步回调
     * @param l
     */
    public static void addListener(OnSynsGroupMemberListener l) {
        getInstance().mGroupMemberListener = l;
    }

    public interface OnSynsGroupMemberListener{
        void onSynsGroupMember(String groupId);
    }
    public interface GroupServiceInterface{
    	public void onInvateComplet();
    };
}
 