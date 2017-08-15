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
package com.im.sdk.dy.ui.settings;

import java.io.InvalidClassException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.dialog.ECAlertDialog;
import com.im.sdk.dy.common.dialog.ECProgressDialog;
import com.im.sdk.dy.common.utils.DemoUtils;
import com.im.sdk.dy.common.utils.ECPreferenceSettings;
import com.im.sdk.dy.common.utils.ECPreferences;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.view.SettingItem;
import com.im.sdk.dy.core.ClientUser;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.ui.ECSuperActivity;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.im.sdk.dy.ui.chatting.IMChattingHelper;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.im.sdk.dy.ui.contact.ContactLogic;
import com.im.sdk.dy.ui.contact.ECContacts;


/**
 * 设置界面/设置新消息提醒（声音或者振动）
 * @author Jorstin Chan@容联•云通讯
 * @date 2014-12-27
 * @version 4.0
 */
public class SettingsActivity extends ECSuperActivity implements View.OnClickListener{

    private static final String TAG = "ECDemo.SettingsActivity";

    public static final int CONFIG_TYPE_SERVERIP = 1;
    public static final int CONFIG_TYPE_APPKEY = 2;
    public static final int CONFIG_TYPE_TOKEN = 3;
    public static final int CONFIG_TYPE_GROUP_NAME = 4;
    public static final int CONFIG_TYPE_GROUP_NOTICE = 5;
    /**头像*/
    private ImageView mPhotoView;
    /**号码*/
    private EmojiconTextView mUsername;
    /**昵称*/
    private TextView mNumber;
    private SettingItem mSettingSound;
    private SettingItem mSettingShake;
    private SettingItem mSettingServerIp;
    private SettingItem mSettingAppkey;
    private SettingItem mSettingToken;
    private SettingItem mSettingExit;
    private SettingItem mSettingSwitch;
    private SettingItem mSettingUpdater;
    private ECProgressDialog mPostingdialog;

    private int mExitType = 0;

    private final View.OnClickListener mSettingExitClickListener
                = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	View contentView = View.inflate(SettingsActivity.this,R.layout.im_exit_dialog_view , null);
            	final CheckBox cb = (CheckBox) contentView.findViewById(R.id.open_dialog_cb);
            	cb.setChecked(true);
            	ECAlertDialog alertDialog = new ECAlertDialog(SettingsActivity.this);
            	alertDialog.setContentView(contentView);
            	alertDialog.setButton(ECAlertDialog.BUTTON_NEGATIVE, R.string.app_cancel, null);
            	alertDialog.setButton(ECAlertDialog.BUTTON_POSITIVE, R.string.dialog_alert_close, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mExitType = 1;
			            handleLogout(cb.isChecked());
					}
				});
            	alertDialog.show();
        }
    };

    private final View.OnClickListener mSettingSwitchClickListener
            = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            ECAlertDialog buildAlert = ECAlertDialog.buildAlert(SettingsActivity.this, R.string.settings_logout_warning_tip, null, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mExitType = 0;
                    handleLogout(false);
                }

            });
            buildAlert.setTitle(R.string.settings_logout);
            buildAlert.show();
        }
    };

	private TextView mSignureTv;

    private final class OnConfigClickListener implements View.OnClickListener {

        private int type;
        public OnConfigClickListener(int type) {
            this.type = type;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SettingsActivity.this , EditConfigureActivity.class);
            intent.putExtra("setting_type" , type);
            startActivityForResult(intent , 0xa);
        }
    }

    /**
     * 处理退出操作
     */
    private void handleLogout(boolean isNotice) {
        mPostingdialog = new ECProgressDialog(this, R.string.posting_logout);
        mPostingdialog.show();
        SDKCoreHelper.logout(isNotice);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.im_settings_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        getTopBarView().setTopBarToStatus(1, R.drawable.btn_back_style,
                R.color.white, null,
                getString(R.string.app_server_config),
                getString(R.string.app_set), null, this);

        registerReceiver(new String[]{SDKCoreHelper.ACTION_LOGOUT});
    }

    /**
     * 加载页面布局
     */
    private void initView() {
        mPhotoView = (ImageView) findViewById(R.id.desc);
        mUsername = (EmojiconTextView) findViewById(R.id.contact_nameTv);
        mNumber = (TextView) findViewById(R.id.contact_numer);
       mSignureTv = (TextView) findViewById(R.id.contact_signure);
        
        mSignureTv.setText(CCPAppManager.getClientUser().getSignature());

        mSettingSound = (SettingItem) findViewById(R.id.settings_new_msg_sound);
        mSettingSound.getCheckedTextView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateNewMsgNotification(0);
            }
        });
        mSettingShake = (SettingItem) findViewById(R.id.settings_new_msg_shake);
        mSettingShake.getCheckedTextView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateNewMsgNotification(1);
            }
        });
        mSettingExit = (SettingItem) findViewById(R.id.setting_exit);
        mSettingExit.setOnClickListener(mSettingExitClickListener);
        mSettingSwitch = (SettingItem) findViewById(R.id.setting_switch);
        mSettingSwitch.setOnClickListener(mSettingSwitchClickListener);

        mSettingServerIp = (SettingItem) findViewById(R.id.settings_serverIP);
        mSettingAppkey = (SettingItem) findViewById(R.id.settings_appkey);
        mSettingToken = (SettingItem) findViewById(R.id.settings_token);
        mSettingUpdater = (SettingItem) findViewById(R.id.settings_update);
        mSettingUpdater.setTitleText(getString(R.string.demo_current_version ,CCPAppManager.getVersion()));
        mSettingServerIp.setOnClickListener(new OnConfigClickListener(CONFIG_TYPE_SERVERIP));
        mSettingAppkey.setOnClickListener(new OnConfigClickListener(CONFIG_TYPE_APPKEY));
        mSettingToken.setOnClickListener(new OnConfigClickListener(CONFIG_TYPE_TOKEN));

        if( IMChattingHelper.getInstance() != null
                && SDKCoreHelper.mSoftUpdate != null
                && DemoUtils.checkUpdater(SDKCoreHelper.mSoftUpdate.version)) {
            mSettingUpdater.setNewUpdateVisibility(true);
            mSettingUpdater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CCPAppManager.startUpdater(SettingsActivity.this);
                }
            });
        } else {
            mSettingUpdater.setNewUpdateVisibility(false);
        }
        initConfigValue();
    }

    private void initConfigValue() {
        // mSettingServerIp.setDetailText(getConfig(ECPreferenceSettings.SETTINGS_SERVERIP));
        mSettingAppkey.setDetailText(getConfig(ECPreferenceSettings.SETTINGS_APPKEY));
        mSettingToken.setDetailText(getConfig(ECPreferenceSettings.SETTINGS_TOKEN));
    }



    private String getConfig(ECPreferenceSettings settings) {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        String value = sharedPreferences.getString(settings.getId() , (String)settings.getDefaultValue());
        return value;
    }


    @Override
    protected void handleReceiver(Context context, Intent intent) {
        super.handleReceiver(context, intent);
        if(SDKCoreHelper.ACTION_LOGOUT.equals(intent.getAction())) {

//            try {
//                Intent outIntent = new Intent(SettingsActivity.this, LauncherActivity.class);
//                outIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                if(mExitType == 1) {
//                    ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_FULLY_EXIT, true, true);
//                    startActivity(outIntent);
//                    finish();
//                    return ;
//                }
//                dismissPostingDialog();
//                ECDevice.unInitial();
//                ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO, "", true);
//                startActivity(outIntent);
//                finish();
//            } catch (InvalidClassException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSettings();
        initActivityState();
        if(mSignureTv!=null){
          mSignureTv.setText(CCPAppManager.getClientUser().getSignature());
        }
    }

    /**
     * 初始化
     */
    private void initSettings() {
        initNewMsgNotificationSound();
        initNewMsgNotificationShake();
    }

    /**
     * 初始化新消息声音设置参数
     */
    private void initNewMsgNotificationSound() {
        if(mSettingSound == null) {
            return ;
        }
        mSettingSound.setVisibility(View.VISIBLE);
        boolean shakeSetting = ECPreferences.getSharedPreferences().getBoolean(ECPreferenceSettings.SETTINGS_NEW_MSG_SOUND.getId(),
                (Boolean) ECPreferenceSettings.SETTINGS_NEW_MSG_SOUND.getDefaultValue());
        mSettingSound.setChecked(shakeSetting);
    }

    /**
     * 初始化新消息震动设置参数
     */
    private void initNewMsgNotificationShake() {
        if(mSettingShake == null) {
            return ;
        }
        mSettingShake.setVisibility(View.VISIBLE);
        boolean shakeSetting = ECPreferences.getSharedPreferences().getBoolean(ECPreferenceSettings.SETTINGS_NEW_MSG_SHAKE.getId(),
                (Boolean) ECPreferenceSettings.SETTINGS_NEW_MSG_SHAKE.getDefaultValue());
        mSettingShake.setChecked(shakeSetting);
    }

    /**
     * 更新状态设置
     * @param type
     */
    protected void updateNewMsgNotification(int type) {
        try {
            if(type == 0) {
                if(mSettingSound == null) {
                    return ;
                }
                mSettingSound.toggle();
                ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_NEW_MSG_SOUND, mSettingSound.isChecked(), true);
                LogUtil.d(TAG, "com.im.sdk.dy_new_msg_sound " + mSettingSound.isChecked());
                return ;
            }
            if(type == 1) {
                if(mSettingShake == null) {
                    return ;
                }
                mSettingShake.toggle();
                ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_NEW_MSG_SHAKE, mSettingShake.isChecked(), true);
                LogUtil.d(TAG, "com.im.sdk.dy_new_msg_sound " + mSettingSound.isChecked());
            }
        } catch (InvalidClassException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置页面参数
     */
    private void initActivityState() {
        ClientUser clientUser = CCPAppManager.getClientUser();
        if(clientUser == null) {
            return ;
        }
        ECContacts contact = ContactSqlManager.getContact(clientUser.getUserId());
        if(contact == null) {
            return ;
        }

        mPhotoView.setImageBitmap(ContactLogic.getPhoto(contact.getRemark()));
        mUsername.setText(clientUser.getUserName());
        mNumber.setText(contact.getContactid());

    }

    /**
     * 关闭对话框
     */
    private void dismissPostingDialog() {
        if(mPostingdialog == null || !mPostingdialog.isShowing()) {
            return ;
        }
        mPostingdialog.dismiss();
        mPostingdialog = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
    		case R.id.btn_text_left:
                hideSoftKeyboard();
                finish();
                break;
            case R.id.text_right:
                startActivity(new Intent(this , SettingPersionInfoActivity.class));
                break;
            default:
                break;
        }
    }

}
