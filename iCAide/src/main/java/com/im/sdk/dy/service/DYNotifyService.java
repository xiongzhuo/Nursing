package com.im.sdk.dy.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.im.sdk.dy.ui.chatting.IMChattingHelper;
import com.im.sdk.dy.ui.contact.ContactLogic;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yung
 * @version 1.0
 * @date 创建时间：2015年12月10日 下午2:17:04
 * @parameter
 * @return
 */
public class DYNotifyService extends Service {
    public static final String TAG = "DYNotifyService";
    public final int FAIL = -1;
    private Tools tools;
    private Context ctx;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void receiveImp(Intent intent) {
        tools = new Tools(this, Constants.AC);
        if (intent == null) {
            LogUtil.e(TAG, "receiveImp receiveIntent == null");
            return;
        }
        int optCode = intent.getIntExtra(IMNotifyReceiver.SERVICE_OPT_CODE, 0);
        LogUtil.d(TAG, "buildSyncServiceAction optCode>>" + optCode);
        if (optCode == 0) {
            LogUtil.e(TAG, "receiveImp invalid opcode.");
            return;
        }
        // if(!SDKCoreHelper.isUIShowing()) {
        // SDKCoreHelper.init(this);
        // }
        switch (optCode) {
            case IMNotifyReceiver.EVENT_TYPE_CALL:
                LogUtil.d(TAG, "receive call event ");
                break;
            case IMNotifyReceiver.EVENT_TYPE_MESSAGE:
                dispatchOnReceiveMessage(intent);
                break;
            case IMNotifyReceiver.EVENT_TYPE_SYNC:
                dispatchOnSync(intent);
                break;
            case IMNotifyReceiver.EVENT_TYPE_CMD:
                dispatchOnCmd(intent);
                break;
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        ctx = this;
        receiveImp(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.v(TAG, String.format(
                "onStartCommand flags :%d startId :%d intent %s", flags,
                startId, intent));
        receiveImp(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 处理消息
     *
     * @param intent
     */
    private void dispatchOnReceiveMessage(Intent intent) {
        if (intent == null) {
            LogUtil.e(TAG, "dispatch receive message fail.");
            return;
        }
        int subOptCode = intent.getIntExtra(IMNotifyReceiver.MESSAGE_SUB_TYPE,
                -1);
        if (subOptCode == -1) {
            return;
        }
        switch (subOptCode) {
            case IMNotifyReceiver.OPTION_SUB_NORMAL:
                ECMessage message = intent
                        .getParcelableExtra(IMNotifyReceiver.EXTRA_MESSAGE);
                IMChattingHelper.getInstance().OnReceivedMessage(message);
                break;
            case IMNotifyReceiver.OPTION_SUB_GROUP:
                ECGroupNoticeMessage notice = intent
                        .getParcelableExtra(IMNotifyReceiver.EXTRA_MESSAGE);
                IMChattingHelper.getInstance().OnReceiveGroupNoticeMessage(notice);
                break;
            case IMNotifyReceiver.OPTION_SUB_MESSAGE_NOTIFY:
                ECMessageNotify notify = intent
                        .getParcelableExtra(IMNotifyReceiver.EXTRA_MESSAGE);
                IMChattingHelper.getInstance().onReceiveMessageNotify(notify);
                break;
            case IMNotifyReceiver.BASE_DATA:

                break;
        }
    }

    /**
     * 处理请求
     *
     * @param intent
     */
    private void dispatchOnSync(Intent intent) {
        if (intent == null) {
            LogUtil.e(TAG, "dispatch receive message fail.");
            return;
        }
        LogUtil.d(TAG, "dispatchOnSync>>");
        int subOptCode = intent.getIntExtra(IMNotifyReceiver.MESSAGE_SUB_TYPE,
                -1);
        if (subOptCode == -1) {
            return;
        }

        LogUtil.d("ACTION_CONTACTS_CHANGE", "dispatchOnSync uid>>" + intent.getStringExtra(IMNotifyReceiver.MESSAGE_SUB_CONTACTS_ID));
        switch (subOptCode) {
            case IMNotifyReceiver.OPTION_SYNC_CONTACT:
                syncContact(subOptCode, intent);
                break;
            case IMNotifyReceiver.OPTION_SYNC_CONTACTS:
                syncContacts(subOptCode);
                //GroupService.syncGroup(null);
                break;
            case IMNotifyReceiver.BASE_DATA:
                DebugUtil.debug("IMNotifyReceiver_base", " start services sync basedara");
                sysBaseData(subOptCode);
                break;
        }
    }

    private void sysBaseData(int subOptCode) {
        // TODO Auto-generated method stub
        LogUtil.d("cmdContacts", "sysBaseData  subOptCode>>" + subOptCode);
        JSONObject json = new JSONObject();
        request(subOptCode, json, WebUrl.URL_PATH_QUERYREMARKPARAMS);
    }

    private void syncPhones(int subOptCode, String phones) {
        LogUtil.d("cmdContacts", "syncPhones  subOptCode>>" + subOptCode + "  phones>>" + phones);
        JSONObject json = new JSONObject();
        try {
            json.put("mobiles", phones);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        request(subOptCode, json, WebUrl.CONTACT_PHONES);
    }

    private void syncContacts(int subOptCode) {
        LogUtil.d("cmdContacts", "syncContacts  subOptCode>>" + subOptCode);
        JSONObject json = new JSONObject();
        request(subOptCode, json, WebUrl.CONTACTS_PATH);
    }

    private void syncContact(int subOptCode, Intent intent) {

        JSONObject json = new JSONObject();
        try {
            json.put("uid", intent.getStringExtra(IMNotifyReceiver.MESSAGE_SUB_CONTACTS_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request(subOptCode, json, WebUrl.CONTACT_FIND_ID_PATH);
    }

    private void request(int subOptCode, JSONObject json, String path) {
        MainBizImpl.getInstance().getData(myHandler, subOptCode, FAIL, json,
                path, tools.getValue(Constants.AUTHENT));
    }

    /**
     * 处理请求返回数据
     *
     * @param intent
     */
    private void dispatchOnCmd(Intent intent) {

    }

    private void cmdResponse(int option, String json) {
        // LogUtil.d(TAG, "option>>"+option+"  cmdResponse json>>"+json );
        //
        // Intent intent=new Intent();
        // intent.setAction(ECManager.CMD_RECEIVER_ACTION);
        // intent.putExtra("option",option);
        // intent.putExtra("json", json);
        // sendBroadcast(intent);
    }

    private void cmdResponseContacts(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String r = jsonObject.getString("result_id");
            if (null != r && r.equals("0")) {
                // String goods=jsonObject.getString("goods");

                LogUtil.d("cmdContacts", "cmdResponseContacts json>>" + json);

                JSONArray jsonArray = jsonObject.getJSONArray("friendsList");
                if (jsonObject.has("groupList")) {
                    JSONArray jarrGroup = jsonObject.getJSONArray("groupList");
                    SharedPreferencesUtil.saveString(ctx, "jarrGroup", jarrGroup.toString());
                }
                JSONArray jarrhospotal = jsonObject.getJSONArray("hospitalFriendList");
                JSONArray jarrMyGroup = jsonObject.optJSONArray("myGroup");


                SharedPreferencesUtil.saveString(ctx, "myGroup", jarrMyGroup.toString());
                if (!AbStrUtil.isEmpty(jsonObject.optString("haveMemberSize"))) {
                    tools.putValue("haveMemberSize", jsonObject.optInt("haveMemberSize"));
                } else {
                    tools.putValue("haveMemberSize", 0);
                }
                SharedPreferencesUtil.saveString(ctx, "hospitalFriendList", jarrhospotal.toString());
                List<ECContacts> contactsList = new ArrayList<ECContacts>();
                ContactLogic.contactPhones.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject itemJson = jsonArray.getJSONObject(i);
                    ECContacts contacts = new ECContacts();
                    if (itemJson.has("friend_id") && !AbStrUtil.isEmpty(itemJson.optString("friend_id"))) {
                        contacts.setContactid(itemJson.optInt("friend_id") + "");
                    } else {
                        continue;
                    }

                    if (itemJson.has("name") && !AbStrUtil.isEmpty(itemJson.optString("name"))) {
                        contacts.setNickname(itemJson.optString("name"));
                    } else {
                        contacts.setNickname("");
                    }
                    if (itemJson.has("regis_job") && !AbStrUtil.isEmpty(itemJson.optString("regis_job"))) {
                        contacts.setRegis_job(itemJson.optString("regis_job"));
                    } else {
                        contacts.setRegis_job("其他");
                    }
                    if (itemJson.has("friend_type") && !AbStrUtil.isEmpty(itemJson.optString("friend_type"))) {
                        contacts.setType(itemJson.optInt("friend_type"));
                    } else {
                        contacts.setType(0);
                        ;
                    }

                    if (itemJson.has("mobile") && !AbStrUtil.isEmpty(itemJson.optString("mobile"))) {
                        contacts.setMobile(itemJson.optString("mobile"));
                    } else {
                        continue;
                    }

                    if (itemJson.has("avatar") && !AbStrUtil.isEmpty(itemJson.optString("avatar"))) {
                        contacts.setAvatar(itemJson.optString("avatar"));
                    } else {
                        contacts.setAvatar("");
                    }

                    if (itemJson.has("city") && !AbStrUtil.isEmpty(itemJson.optString("city"))) {
                        contacts.setCity(itemJson.optString("city"));
                    } else {
                        contacts.setCity("");
                    }

                    if (itemJson.has("department") && !AbStrUtil.isEmpty(itemJson.optString("department"))) {
                        contacts.setDepartment(itemJson.optString("department"));
                    } else {
                        contacts.setDepartment("");
                        ;
                    }

                    if (itemJson.has("hospital") && !AbStrUtil.isEmpty(itemJson.optString("hospital"))) {
                        contacts.setHospital(itemJson.optString("hospital"));
                    } else {
                        contacts.setHospital("");
                    }

                    if (itemJson.has("sex") && !AbStrUtil.isEmpty(itemJson.optString("sex"))) {
                        contacts.setSex(itemJson.optInt("sex"));
                    } else {
                        contacts.setSex(0);
                    }

                    if (itemJson.has("province") && !AbStrUtil.isEmpty(itemJson.optString("province"))) {
                        contacts.setProvince(itemJson.optString("province"));
                    } else {
                        contacts.setProvince("");
                    }


                    if (itemJson.has("remark") && !AbStrUtil.isEmpty(itemJson.optString("remark"))) {
                        contacts.setRemark(itemJson.optString("remark"));
                    } else {
                        contacts.setRemark("");
                    }
                    String rname = null;
                    if (itemJson.has("rname") && !AbStrUtil.isEmpty(itemJson.optString("rname"))) {
                        rname = itemJson.optString("rname");

                    } else {
                        rname = "";
                    }

                    rname = rname == null || rname.equals("") ? contacts
                            .getNickname() : rname;

                    contacts.setRname(rname);
                    //LogUtil.d("cmdContactsss", "sex>>" + itemJson.getInt("sex"));

                    contactsList.add(contacts);

                    ContactLogic.contactPhones.put(itemJson.optString("mobile"), itemJson.optString("mobile"));
                }
                if (contactsList.size() > 0) {
                    ContactLogic.contactList.clear();
                    ContactLogic.contactList.addAll(contactsList);
                    ContactSqlManager.insertContacts(contactsList);
                }

                //发送广播，更改联系人页面
//				Intent intent=new Intent();
//				intent.setAction("contact_isupdated");
//				MyAppliaction.getInstance().sendBroadcast(intent);

            }
        } catch (JSONException e5) {
            e5.printStackTrace();
        }
    }

    private void cmdContact(String json) {
        try {

            LogUtil.d("ACTION_CONTACTS_CHANGE", "json>>" + json);

            JSONObject jsonObject = new JSONObject(json);
            String r = jsonObject.getString("result_id");
            if (null != r && r.equals("1")) {
            } else if (null != r && r.equals("2")) {
            } else {
                JSONObject itemJson = jsonObject.getJSONObject("friend");
                ECContacts contacts = ContactLogic
                        .GetContacts(itemJson);
                ContactSqlManager.insertContact(contacts);
                //通知更改界面
                Intent intent = new Intent(SDKCoreHelper.ACTION_CONTACTS_CHANGE);
                intent.putExtra("contacts", contacts);
                this.sendBroadcast(intent);
            }

            return;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private void cmdContacts(String json) {
        cmdResponseContacts(json);

        new Thread(new Runnable() {

            @Override
            public void run() {
                //对比手机联系人数据
                int page = 0;
                int oldPage = -1;
                while (page > oldPage) {
                    oldPage = page;
                    page = PhoesSync(page);
                    LogUtil.d("sysn_add", "oldPage now>>" + oldPage);
                    LogUtil.d("sysn_add", "page next>>" + page);
                }
            }
        }).start();

    }

    private void cmdBaseData(String json) {
        // TODO Auto-generated method stub
        DebugUtil.debug("IMNotifyReceiver_base", " services cmdBaseData json>>" + json);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            String r = jsonObject.getString("result_id");
            if (null != r && r.equals("0")) {
                tools.putValue("equipExamineParams", json);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private int PhoesSync(int page) {
        int p = ContactLogic.getPhoneContacts(this, 100, page);

        //发送广播到页面,查询页面
        //通知更改界面
        Intent intent = new Intent(SDKCoreHelper.ACTION_MOBILE_CONTACTS_CHANGE);
        this.sendBroadcast(intent);

        if (null != ContactLogic.phoneString && ContactLogic.phoneString.length() > 0) {
            //请求服务器对比
            syncPhones(IMSuperNotifyReceiver.OPTION_SYNC_PHONES, ContactLogic.phoneString);
        }
        return p;
    }

    private Handler myHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case IMNotifyReceiver.OPTION_SYNC_CONTACT:
                    if (CCPAppManager.getClientUser() == null)
                        return;
                    if (null != msg.obj) {
                        cmdContact(msg.obj.toString());

                    }
                    break;
                case IMNotifyReceiver.OPTION_SYNC_CONTACTS:
                    if (CCPAppManager.getClientUser() == null)
                        return;
                    if (null != msg.obj) {
                        cmdContacts(msg.obj.toString());
                        // Intent intent=new Intent();
                        // intent.setAction(action);
                        // intent.putExtra(IMNotifyReceiver.RECEIVER_OPTION,
                        // msg.what);
                    }
                    break;
                case IMSuperNotifyReceiver.OPTION_SYNC_PHONES:
                    if (CCPAppManager.getClientUser() == null)
                        return;
                    if (null != msg.obj) {
                        cmdResponseContacts(msg.obj.toString());
                    }
                    break;
                case IMSuperNotifyReceiver.BASE_DATA:
                    if (null != msg.obj) {
                        cmdBaseData(msg.obj.toString());
                    }
                    break;
                case FAIL:
                    LogUtil.d(TAG, "fail>>" + msg.obj);
                    break;
            }

        }
    };

}
