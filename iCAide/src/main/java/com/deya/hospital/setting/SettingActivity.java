package com.deya.hospital.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.account.UserInfoEditorActivity;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.login.LoginActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.vo.DepartmentVo;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.lidroid.xutils.exception.DbException;
import com.yuntongxun.ecsdk.ECDevice;

public class SettingActivity extends BaseActivity implements
        OnClickListener {
    public int uploadStatus = 0;

    private RelativeLayout mybillsRl;
    private LinearLayout shareRl;

    protected static final int GET_SUCESS = 0x3007;
    protected static final int GET_FAILE = 0x3008;
    private RelativeLayout helpingTv;
    CommonTopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        initView();
    }


    private void initView() {
        topView = findView(R.id.topView);
        topView.init(this);
        mybillsRl = findView(R.id.mybills);
        helpingTv = findView(R.id.helping);
        helpingTv.setOnClickListener(this);
        dialog = new MyDialog(mcontext, R.style.SelectDialog);
        pushstting = findView(R.id.pushstting);
        pushstting.setOnClickListener(this);
        findViewById(R.id.login_out).setOnClickListener(this);
        shareRl = findView(R.id.share);
        shareRl.setOnClickListener(this);
        String hospitalJob = tools.getValue(Constants.JOB);
        if (!AbStrUtil.isEmpty(hospitalJob)) {
            if (hospitalJob.equals("1")) {
                shareRl.setVisibility(View.VISIBLE);
            } else if (hospitalJob.equals("2")) {
                shareRl.setVisibility(View.VISIBLE);
            } else if (hospitalJob.equals("3")) {
                shareRl.setVisibility(View.GONE);
            } else if (hospitalJob.equals("4")) {
                shareRl.setVisibility(View.GONE);
            }else if (hospitalJob.equals("5")) {
                shareRl.setVisibility(View.GONE);
            }
        }

        above = findView(R.id.above);
        above.setOnClickListener(this);

        mybillsRl.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mybills:
                Intent it = new Intent(mcontext,
                        UserInfoEditorActivity.class);
                it.putExtra("isEditor", true);
                startActivity(it);

                break;
            case R.id.pushstting:
                Intent System_setting = new Intent(mcontext,
                        SystemSettingsActivity.class);
                startActivity(System_setting);
                break;

            case R.id.above:
                Intent above = new Intent(mcontext, ContectUsActivity.class);
                startActivity(above);
                break;
            case R.id.share:
                Intent shareIn = new Intent(mcontext, HospitalShareActivity.class);
                startActivity(shareIn);
                break;
            case R.id.helping:
                Intent it3 = new Intent(mcontext, NewbieHelpActivity.class);
                startActivity(it3);

                break;
            case R.id.login_out:
                dialog.show();
                break;
            default:
                break;
        }

    }

    MyDialog dialog;
    private RelativeLayout pushstting;
    private RelativeLayout above;

    public class MyDialog extends Dialog {

        private TextView showBtn;
        private TextView deletBtn;
        private TextView cancleBtn;

        /**
         * Creates a new instance of MyDialog.
         */
        public MyDialog(Context context, int theme) {
            super(context, theme);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO 自动生成的方法存根
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.dialog_delet);

            showBtn = (TextView) this.findViewById(R.id.show);
            showBtn.setText("退出登录？");
            deletBtn = (TextView) this.findViewById(R.id.yes);
            deletBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    tools.putValue(Constants.AUTHENT, "");
                    tools.putValue(Constants.NAME, "");
                    tools.putValue(Constants.HOSPITAL_NAME, "");
                    tools.putValue(Constants.AGE, "");
                    tools.putValue(Constants.SEX, "");
                    tools.putValue(Constants.STATE, "");
                    tools.putValue(Constants.MOBILE, "");
                    tools.putValue(Constants.HEAD_PIC, "");
                    tools.putValue(Constants.EMAIL, "");
                    tools.putValue(Constants.JOB, "");
                    tools.putValue(Constants.USER_ID, "");
                    tools.putValue(Constants.IS_VIP_HOSPITAL, "");
                    SharedPreferencesUtil.saveString(mcontext, "discoverData",
                            "");
                    SharedPreferencesUtil.saveString(mcontext, "chat_editor_unsend_text1", "");
                    try {
                        if (null != DataBaseHelper
                                .getDbUtilsInstance(mcontext)
                                .findAll(DepartmentVo.class)) {
                            DataBaseHelper
                                    .getDbUtilsInstance(mcontext)
                                    .deleteAll(DepartmentVo.class);
                        }
                    } catch (DbException e1) {
                        e1.printStackTrace();
                    }
                    if (ECDevice.isInitialized())
                        try {
                            ECDevice.unInitial();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    SDKCoreHelper.logout(true);

                    CCPAppManager.setClientUser(null);

                    MainActivity.mInit = false;
                    Intent intent = new Intent(mcontext,
                            LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    intent.setAction(Constants.AUTHENT_LOSE);
//					sendBroadcast(intent);
                    finish();

                }
            });
            cancleBtn = (TextView) this.findViewById(R.id.cacle);
            cancleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                }
            });
        }
    }
    @Override
    public void onDestroy() {
        if(null!=dialog&&dialog.isShowing()){
            dialog.dismiss();
        }
        super.onDestroy();
    }
}
