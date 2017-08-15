package com.deya.hospital.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.services.BaseDataInitService;
import com.deya.hospital.setting.NewbieHelpActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.UpdateInfo;
import com.google.gson.Gson;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements
        OnClickListener, RequestInterface {
    private static final int LOGIN_SUCESS = 0;
    private static final int LOGIN_FAILE = 404;// 连接失败
    private static final int CHECK_UPDATE_SUCCESS = 0x1051;
    private static final int CHECK_UPDATE_FAIL = 0x1052;
    private EditText userNameEt;
    private EditText passwordEt;
    private String userName = "";
    private String password = "";
    private Button sumbmit;
    private Context mcontext;
    private TextView registerTv;
    private TextView refoundPwdTv;
    TextView intruduce;
    LinearLayout bgImg;
    UpdateInfo updateInfo;
    Tools tools;
    Gson gson;
    private RelativeLayout rl_back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = this;
        gson = new Gson();
        tools = new Tools(mcontext, Constants.AC);
        setContentView(R.layout.login_activity_login);
        initView();
        Intent intent = new Intent(Constants.AUTHENT_LOSE);
        intent.setAction(Constants.AUTHENT_LOSE);
        sendBroadcast(intent);
    }

    private void initView() {
        userNameEt = (EditText) this.findViewById(R.id.userName);
        String account = tools.getValue(Constants.USERACCOUNT);
        if (null != account && !AbStrUtil.isEmpty(account)) {
            userNameEt.setText(account);
            userNameEt.setSelection(account.length());//将光标移至文字末尾
        }

        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setVisibility(View.GONE);
        title = (TextView) findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        title.setText("登录");
        passwordEt = (EditText) this.findViewById(R.id.password);
        sumbmit = (Button) this.findViewById(R.id.sumbmit);
        sumbmit.setOnClickListener(this);
        registerTv = (TextView) this.findViewById(R.id.register);
        registerTv.setOnClickListener(this);
        refoundPwdTv = (TextView) this.findViewById(R.id.refound_pwd);
        registerTv.setOnClickListener(this);
        refoundPwdTv.setOnClickListener(this);
        intruduce = (TextView) this.findViewById(R.id.intruduce);
        intruduce.setOnClickListener(this);
        intruduce.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        // checkUpdate();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sumbmit:
                if (checkLoginInfo()) {
                    MobclickAgent.onEvent(this, "login");
                    LoginRequest();
                }
                break;
            case R.id.register:
                Intent Intent = new Intent(LoginActivity.this,
                        MyRegisterActivity.class);
                startActivity(Intent);
                break;
            case R.id.refound_pwd:
                Intent findpwd = new Intent(LoginActivity.this,
                        FindPasswordActivity.class);
                startActivity(findpwd);
                break;
            case R.id.intruduce:
                Intent helper = new Intent(LoginActivity.this,
                        NewbieHelpActivity.class);
                startActivity(helper);
                break;
            default:
                break;
        }

    }

    private boolean checkLoginInfo() {
        userName = userNameEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();

        if (userName.length() < 1) {
            ToastUtils.showToast(mcontext, "请输入账号");
            return false;
        } else if (AbStrUtil.isEmpty(password)) {
            ToastUtils.showToast(mcontext, "请输入密码");
            return false;
        } else if (password.length() < 6) {
            ToastUtils.showToast(mcontext, "密码长度6-16位");
            return false;
        } else if (password.length() > 16) {
            ToastUtils.showToast(mcontext, "密码长度6-16位");
            return false;
        } else {
            return true;
        }

    }

    // 请求服务器验证 账号密码
    private void LoginRequest() {
        JSONObject job = new JSONObject();
        try {
            job.put("userName", userName);
            job.put("passWord", password);
            showprocessdialog();
            MainBizImpl.getInstance().onComomReq(this, LoginActivity.this,
                    LOGIN_SUCESS, job, "account/checkAccount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case LOGIN_SUCESS:

                    case LOGIN_FAILE:
                        dismissdialog();
                        ToastUtils.showToast(LoginActivity.this, "亲，您的网络不顺畅哦！");
                        break;
                    default:
                        break;
                }
            }
        }

    };

    // 登录请求回调处理
    private void setLoginResult(JSONObject json) {
        tools.putValue(Constants.USERACCOUNT, userNameEt.getText()
                .toString());
        //  tools.putValue(Constants.AUTHENT, json.optString("authent"));
            JSONObject job = json;
            if (AbStrUtil.isEmpty(job.optString("mobile"))) {
                tools.putValue(Constants.MOBILE, "");
            } else {
                tools.putValue(Constants.MOBILE, job.optString("mobile"));
                MobclickAgent.onProfileSignIn(Constants.UMENG_ID);
            }
//				if (AbStrUtil.isEmpty(job.optString("name"))) {
//					Intent in = new Intent(mcontext, AccountSettingsActivity.class);
//					in.putExtra("register", "LOGIN_MYREGISTERACTIVITY");
//					startActivity(in);
//					return;
//				} else {
//					tools.putValue(Constants.NAME, job.optString("name"));
//				}
            if (AbStrUtil.isEmpty(job.optString("userId"))) {
                tools.putValue(Constants.USER_ID, "");
            } else {
                tools.putValue(Constants.USER_ID, job.optString("userId"));
            }
            if (AbStrUtil.isEmpty(job.optString("regis_job"))) {
                tools.putValue(Constants.JOB, "");
            } else {
                tools.putValue(Constants.JOB, job.optString("regis_job"));
            }
            if (AbStrUtil.isEmpty(job.optString("regis_job_name"))) {
                tools.putValue(Constants.JOB_NAME, "");
            } else {
                tools.putValue(Constants.JOB_NAME, job.optString("regis_job_name"));
            }
            if (!AbStrUtil.isEmpty(job.optString("name"))) {
                tools.putValue(Constants.USER_NAME,
                        job.optString("name"));
                tools.putValue(Constants.NAME,
                        job.optString("name"));
            } else {
                tools.putValue(Constants.NAME, job.optString("mobile"));
            }
            if (AbStrUtil.isEmpty(job.optString("email"))) {
                tools.putValue(Constants.EMAIL, job.optString(""));
            } else {
                tools.putValue(Constants.EMAIL, job.optString("email"));
            }
            if (AbStrUtil.isEmpty(job.optString("sex"))) {
                tools.putValue(Constants.SEX, "");
            } else {
                tools.putValue(Constants.SEX, job.optString("sex"));
            }
            if (AbStrUtil.isEmpty(job.optString("age"))) {
                tools.putValue(Constants.AGE, "");
            } else {
                tools.putValue(Constants.AGE, job.optString("age"));
            }
            tools.putValue(Constants.REMARK, job.optString("remark"));
            tools.putValue(Constants.STATE, job.optString("state"));
            if (!AbStrUtil.isEmpty(job.optString("is_sign_old"))) {
                tools.putValue(Constants.IS_SIGN_OLD, job.optString("is_sign_old") + "");
            }
            if (AbStrUtil.isEmpty(job.optString("auth_name"))) {
                tools.putValue(Constants.AUTH_NAME, "");
            } else {
                tools.putValue(Constants.HEAD_PIC, job.optString("auth_name"));
            }
            if (AbStrUtil.isEmpty(job.optString("comName"))) {
                tools.putValue(Constants.HOSPITAL_NAME, "");
            } else {
                tools.putValue(Constants.HOSPITAL_NAME,
                        job.optString("comName"));
            }
            if (AbStrUtil.isEmpty(job.optString("comId"))) {
                tools.putValue(Constants.HOSPITAL_ID, "");
            } else {
                tools.putValue(Constants.HOSPITAL_ID,
                        job.optString("comId"));
            }
            if (AbStrUtil.isEmpty(job.optString("avatar"))) {
                tools.putValue(Constants.HEAD_PIC, "");
            } else {
                tools.putValue(Constants.HEAD_PIC, job.optString("avatar"));
            }
            if (AbStrUtil.isEmpty(job.optString("is_admin"))) {
                tools.putValue(Constants.IS_ADMIN, "1");
            } else {
                tools.putValue(Constants.IS_ADMIN,
                        job.optString("is_admin"));
            }

            String departmentId = job.optString("departId");
            if (!AbStrUtil.isEmpty(departmentId)
                    && !departmentId.equals("0")
                    && !departmentId.equals("1")) {
                tools.putValue(Constants.DEFULT_DEPARTID,
                        job.optString("departId"));
            } else {
                tools.putValue(Constants.DEFULT_DEPARTID, "");
            }

            if (!AbStrUtil.isEmpty(job.optString("departmentName"))) {
                tools.putValue(Constants.DEFULT_DEPART_NAME,
                        job.optString("departmentName"));
                tools.putValue(Constants.DEPARTMENT_NAME,
                        job.optString("departmentName"));

            } else {
                tools.putValue(Constants.DEFULT_DEPART_NAME, "");
                tools.putValue(Constants.DEPARTMENT_NAME,
                        "");
            }
            if (AbStrUtil.isEmpty(job.optString("task_count"))) {
                tools.putValue(Constants.TASK_COUNT, "0");
            } else {
                tools.putValue(Constants.TASK_COUNT,
                        job.optString("task_count"));
            }
            if (!AbStrUtil.isEmpty(job.optString("mobile"))) {
                tools.putValue(Constants.MOBILE, job.optString("mobile"));
            } else {
                tools.putValue(Constants.MOBILE, "0");
            }
            if (!AbStrUtil.isEmpty(job.optString("integral"))) {
                tools.putValue(Constants.INTEGRAL,
                        job.optString("integral"));
            } else {
                tools.putValue(Constants.INTEGRAL, "0");
            }
            if (!AbStrUtil.isEmpty(job.optString("title"))) {
                tools.putValue(Constants.AJOB, job.optString("title"));
            } else {
                tools.putValue(Constants.AJOB, "");
            }

            if (!AbStrUtil.isEmpty(job.optString("in_job"))) {
                tools.putValue(Constants.HOSPITAL_JOB,
                        job.optString("in_job"));
            } else {
                tools.putValue(Constants.HOSPITAL_JOB, "");
            }
            if (!AbStrUtil.isEmpty(job.optString("post"))) {
                tools.putValue(Constants.PJOB, job.optString("post"));
            } else {
                tools.putValue(Constants.PJOB, "");
            }
            if (!AbStrUtil.isEmpty(job.optString("is_sign"))) {
                tools.putValue(Constants.IS_VIP_HOSPITAL,
                        job.optString("is_sign"));
            } else {
                tools.putValue(Constants.IS_VIP_HOSPITAL, "1");
            }

            tools.putValue(Constants.USER_ID, job.optInt("id") + "");

            String tipState = tools.getValue(Constants.IS_CLOSE_TIPS);
            if (AbStrUtil.isEmpty(tipState)) {
                tools.putValue(Constants.IS_CLOSE_TIPS, "0");
            }
            //   if (!AbStrUtil.isEmpty(tools.getValue(Constants.JOB))) {
            Intent intent = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(intent);
//            } else {
//                Intent intent = new Intent(LoginActivity.this,
//                        UserInfoActivity.class);
//                startActivity(intent);
//            }

            Intent intent2 = new Intent(mcontext, BaseDataInitService.class);
            intent2.putExtra(BaseDataInitService.INTENTCODE, BaseDataInitService.GETMODULE);
            startService(intent2);
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (passwordEt != null) {
            passwordEt.setFocusable(false);
            passwordEt = null;
        }
        if (userNameEt != null) {
            userNameEt.setFocusable(false);
            userNameEt = null;
        }

    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        dismissdialog();
        setLoginResult(jsonObject);
    }

    @Override
    public void onRequestErro(String message) {
        dismissdialog();
        ToastUtil.showMessage(message);

    }

    @Override
    public void onRequestFail(int code) {
        dismissdialog();

    }
}
