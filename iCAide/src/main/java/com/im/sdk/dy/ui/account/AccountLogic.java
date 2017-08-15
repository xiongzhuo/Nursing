package com.im.sdk.dy.ui.account;

import android.text.TextUtils;

import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.core.ClientUser;
import com.im.sdk.dy.ui.chatting.IMChattingHelper;
import com.im.sdk.dy.ui.group.GroupService;
import com.im.sdk.dy.ui.group.GroupService.GroupCardCallBack;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.PersonInfo;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECGroupMember;


/**
 * 帐号逻辑处理
 * Created by yung on 2015/12/18.
 */
public class AccountLogic {

	
    private static AccountLogic sInstance;
    public static AccountLogic getInstance() {
        if (sInstance == null) {
            sInstance = new AccountLogic();
        }
        return sInstance;
    }
    
    /**
     * 设置个人信息
     * @param clientUser
     */
   public static void setAccount(ClientUser clientUser){ 
	   LogUtil.d("setAccount", "clientUser>>"+clientUser.getUserName());
	   PersonInfo personInfo =new PersonInfo();
       personInfo.setNickName(clientUser.getUserName());
       
       personInfo.setSex(clientUser.getSex()==1?PersonInfo.Sex.FEMALE:PersonInfo.Sex.MALE);
       personInfo.setSex(PersonInfo.Sex.FEMALE);
//       personInfo.setSign(clientUser.getAvatar());
       
       if(!TextUtils.isEmpty(clientUser.getUserName())){
       
      ECDevice.setPersonInfo(personInfo, new ECDevice.OnSetPersonInfoListener() {
           @Override
           public void onSetPersonInfoComplete(ECError e, int version) {
               IMChattingHelper.getInstance().mServicePersonVersion = version;
               if (SdkErrorCode.REQUEST_SUCCESS == e.errorCode) {
            	   LogUtil.d("setAccount", "set ok>>");
                   try {
                       ClientUser clientUser = CCPAppManager.getClientUser();
                   } catch (Exception e1) {
                       e1.printStackTrace();
                   }
                   return;
               }
               LogUtil.d("setAccount", "set erro>>"+e.errorMsg);
           }
       });
       }
   }
   
   /**
    * 修改群名片，加入群组时调用
    * @param groupId
    * @param clientUser
    */
   public static void setMemberInfo(String groupId,ClientUser clientUser){
	   setMemberInfo(groupId, clientUser.getUserId(), clientUser.getUserName(), clientUser.getSex(), clientUser.getAvatar());
   }
   
   public static void setMemberInfo(String groupId,String userId,String name,int sex,String avatar){
	   ECGroupMember groupMember =new ECGroupMember();
	   	groupMember.setBelong(groupId);
	   	groupMember.setDisplayName(name);
	   	groupMember.setSex(sex==1?2:1);
	   	groupMember.setRemark(avatar);
	   	groupMember.setVoipAccount(userId);
	   	groupMember.setEmail("");
	   	groupMember.setTel("");
	   	GroupService.modifyGroupCard(groupMember, new GroupCardCallBack() {
			
			@Override
			public void onQueryGroupCardSuccess(ECGroupMember member) {
				// TODO Auto-generated method stub
				LogUtil.d("modifyGroupCard","ok");
				
			}
			
			@Override
			public void onQueryGroupCardFailed(ECError error) {
				// TODO Auto-generated method stub
				LogUtil.d("modifyGroupCard","error");
			}
			
			@Override
			public void onModifyGroupCardSuccess() {
				// TODO Auto-generated method stub
				LogUtil.d("modifyGroupCard","ok2");
			}
			
			@Override
			public void onModifyGroupCardFailed(ECError error) {
				// TODO Auto-generated method stub
				LogUtil.d("modifyGroupCard","error2");
			}
		});
   }
}
