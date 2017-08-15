package com.deya.hospital.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.account.UserInfoActivity;
import com.deya.hospital.account.UserUtis;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseRequestActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class MyRegisterActivity extends BaseRequestActivity implements
        OnClickListener {
    private static final String SCENETYPE = "1";
    private static final int CODE_SUCESS = 0x100;
    private static final int REGSTER_SUCESS = 0x102;
    private EditText phoneHasRegister;
    private EditText edtMsgCode, text_code;
    private EditText edtNewPass;
    private Button btnMsgCode;
    private Button nextRetrievePassword;
    private String registerphone;
    private String vfcode = "";
    private String password;
    private TimeCount time;
    String code = "";
    boolean isAccept = true;
    Tools tools;
    TextView telTv;
    TextView provisionTv;
    CommonTopView topView;
    ImageView provisionImg;
    private CheckBox cb_seepsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_register);
        initView();
    }

    private void initView() {


        tools = new Tools(mcontext, Constants.AC);

        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象


        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);

        btnMsgCode = (Button) findViewById(R.id.btn_msg_code);
        btnMsgCode.setOnClickListener(this);

        phoneHasRegister = (EditText) findViewById(R.id.mobile);
        edtMsgCode = (EditText) findViewById(R.id.edt_msg_code);

        provisionTv = (TextView) this.findViewById(R.id.provisionTv);
        provisionTv.setOnClickListener(this);


        telTv = (TextView) this.findViewById(R.id.telTv);
        telTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        telTv.getPaint().setAntiAlias(true);//抗锯齿
        telTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                            .parse("tel:"
                                    + telTv.getText().toString()));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mcontext, "该设备不支持通话功能",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

        if (getIntent().hasExtra("type") || getIntent().hasExtra("NoPassWord")) {
            phoneHasRegister.setVisibility(View.GONE);
        }
        nextRetrievePassword = (Button) findViewById(R.id.sumbmit);
        nextRetrievePassword.setOnClickListener(this);

        edtNewPass = (EditText) findViewById(R.id.password);


        provisionImg = (ImageView) this.findViewById(R.id.provisionImg);
        provisionImg.setOnClickListener(this);

        text_code = (EditText) this.findViewById(R.id.text_code);

        cb_seepsw = (CheckBox) findViewById(R.id.cb_seepsw);
        cb_seepsw.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_msg_code:
                registerphone = phoneHasRegister.getText().toString().trim();
                if (AbStrUtil.isEmpty(registerphone)) {
                    ToastUtils.showToast(this, "请输入手机号码");
                    break;
                } else if (registerphone.length() != 11) {
                    ToastUtils.showToast(this, "手机号码不正确");
                    break;
                } else {
                    sendverificationinit();
                    btnMsgCode.setEnabled(false);
                }
                break;

            case R.id.sumbmit:
//                CommonUtils.showTips(this, R.drawable.ic_registe_success);
                registerphone = phoneHasRegister.getText().toString().trim();
                vfcode = edtMsgCode.getText().toString().trim();
                password = edtNewPass.getText().toString().trim();

                code = text_code.getText().toString().trim();
                if (checkRegisterInfo()) {
                    // 请求服务器修改密码
                    showprocessdialog();
                    Register();
//                    showTips();
                }

                break;
            case R.id.provisionTv:
                Intent agreementIntent = new Intent(MyRegisterActivity.this,
                        AgreementActivity.class);
                startActivity(agreementIntent);
                break;
            case R.id.provisionImg:
                setArgeenmentStatus();
            case R.id.cb_seepsw:
                String text = edtNewPass.getText().toString().trim();
                if (cb_seepsw.isChecked()) { // 当CheckBox被选中
                    edtNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); // 密码以明文显示
                    edtNewPass.setSelection(text.length());
                } else {
                    edtNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance()); // 以密文显示，以.代替
                    edtNewPass.setSelection(text.length());
                }
                break;
            default:
                break;
        }

    }

    void setArgeenmentStatus() {
        isAccept = !isAccept;

        int registerProvisionIcon = R.drawable.register_provision_icon;
        if (!isAccept) {
            registerProvisionIcon = R.drawable.register_provision_icon1;
        }
        nextRetrievePassword.setEnabled(isAccept);
        this.provisionImg.setImageResource(registerProvisionIcon);
    }


    private boolean checkRegisterInfo() {
        if (AbStrUtil.isEmpty(registerphone)) {
            ToastUtils.showToast(this, "请输入手机号码");
            return false;
        } else if (registerphone.length() != 11) {
            ToastUtils.showToast(this, "手机号码不正确");
            return false;
        } else if (AbStrUtil.isEmpty(password)) {
            ToastUtils.showToast(this, "请设置密码");
            return false;
        } else if ((password.length() < 6)) {
            ToastUtils.showToast(this, "密码长度6-16位");
            return false;
        } else if (AbStrUtil.isEmpty(vfcode)) {
            ToastUtils.showToast(this, "请输入验证码");
            return false;
        } else if (4 != vfcode.length()) {
            ToastUtils.showToast(this, "验证码不正确");
            return false;
        } else {
            return true;
        }

    }

    private void Register() {
        JSONObject job = new JSONObject();
        try {
            job.put("username", "");
            job.put("mobile", registerphone);
            job.put("password", password);
            job.put("verify_code", vfcode);
            job.put("auth_code", code);//授权码
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, MyRegisterActivity.this, REGSTER_SUCESS, job, "user/register");

    }

    /**
     * 请求服务器获取验证码
     */
    private void sendverificationinit() {
        JSONObject job = new JSONObject();
        try {
            job.put("mobile", registerphone);
            /* 1:注册验证码
            2:密码找回验证码*/
            job.put("sceneType", SCENETYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, MyAppliaction.getContext(), CODE_SUCESS, job, "common/sendValidCode");
        time.start();// 开始计时
    }

    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            dismissdialog();
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
            }
        }
    };

    private void setCodeResult(JSONObject jsonObject) {
        ToastUtil.showMessage(jsonObject.optString("msg"));
    }

    private void setRgisterResult(JSONObject json) {
        MobclickAgent.onEvent(this, "register");
        if (json.has("result_id")) {
            if (json.optString("result_id").equals("0")) {
                tools.putValue(Constants.AUTHENT, json.optString("authent"));
                JSONObject job = null;
                if (json.has("member")) {
                    job = json.optJSONObject("member");
                } else {
                    return;
                }
                if (null == job) {
                    return;
                }
                UserUtis.setEditorRes(tools, job);
                tools.putValue(Constants.USERACCOUNT, phoneHasRegister.getText().toString().trim());
                Intent intent = new Intent(mcontext, UserInfoActivity.class);
                startActivity(intent);
                finish();
            } else if (json.has("result_msg")) {
                ToastUtils.showToast(mcontext, json.optString("result_msg"));
            }
        }
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        switch (code) {
            case CODE_SUCESS:
                dismissdialog();
                setCodeResult(jsonObject);
                break;
            case REGSTER_SUCESS:
                dismissdialog();
                setRgisterResult(jsonObject);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestErro(String message) {
        super.onRequestErro(message);
        btnMsgCode.setText("获取验证码");
        btnMsgCode.setEnabled(true);
        time.cancel();
    }

    @Override
    public void onRequestFail(int code) {
        switch (code) {
            case CODE_SUCESS:
                btnMsgCode.setText("获取验证码");
                btnMsgCode.setEnabled(true);
                time.cancel();
                ToastUtils.showToast(MyRegisterActivity.this, "获取验证码失败");
                break;
            default:
                ToastUtils.showToast(mcontext, "网络不顺畅，请稍后再试");
                break;
        }
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
            // 计时完毕时触发
            btnMsgCode.setText("获取验证码");
            btnMsgCode.setEnabled(true);
        }

        @SuppressLint("StringFormatMatches")
        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程显示
            btnMsgCode.setEnabled(false);
            String secondNum = getString(R.string.second_num);
            secondNum = String.format(secondNum, millisUntilFinished / 1000);
            btnMsgCode.setText(secondNum);
        }
    }

}
