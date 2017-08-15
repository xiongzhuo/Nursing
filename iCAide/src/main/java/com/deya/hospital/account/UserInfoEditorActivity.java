package com.deya.hospital.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseListDialog;
import com.deya.hospital.base.DepartChooseActivity;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.setting.SettingNameActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.vo.JobEntity;
import com.deya.hospital.vo.UserInfoVo;
import com.deya.hospital.workspace.TaskUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class UserInfoEditorActivity extends GaveAuthority implements View.OnClickListener {
    private static final int REQUSET = 0x103;
    private static final int GET_USER_INFO = 0x104;
    List<DepartLevelsVo> departlist;
    JobDialog jobDialog;
    public String jobId;
    LinearLayout stateLay;
    private ImageView iv_waring;
    private TextView tv_state;
    private TextView tv_state_tip;
    boolean isReAuthority;
    RelativeLayout avatarLay;
    private TextView hospitalTv;


    @Override
    public int getLayoutId() {
        return R.layout.gave_authority_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refuseBtn.setVisibility(View.GONE);
        isEditor = true;
        initOwnViews();
        userInfo = new UserInfoVo();
        accept_btn.setText("提 交");
        frambg = (LinearLayout) this.findViewById(R.id.frambg);
        departlist = new ArrayList<>();
        getUserInfo();
    }

    private void initOwnViews() {
        stateLay = (LinearLayout) this.findViewById(R.id.stateLay);
        iv_waring = (ImageView) this.findViewById(R.id.iv_waring);
        tv_state = (TextView) this.findViewById(R.id.tv_state);
        tv_state_tip = (TextView) this.findViewById(R.id.tv_state_tip);
        name.setOnClickListener(this);
        avatarLay= (RelativeLayout) findViewById(R.id.avatarLay);
        hospitalTv = (TextView) this.findViewById(R.id.hospitalTv);
        hospitalTv.setText(tools.getValue(Constants.HOSPITAL_NAME));
    }

    public void getUserInfo() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            Log.i("1111", job.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, this,
                GET_USER_INFO, job, "member/getUserInfo");
    }

    private void initData() {
        stateLay.setVisibility(View.VISIBLE);
        String state = tools.getValue(Constants.STATE);
        rel_supervision.setOnClickListener(null);
        if (!AbStrUtil.isEmpty(state)) {
            if (state.equals("5")) {//认证通过
                avatar.setOnClickListener(null);
                iv_waring.setImageResource(R.drawable.ic_hasschecked);
                tv_state.setText(getString(R.string.register_checked_success_title));
                accept_btn.setVisibility(View.GONE);
                tv_state_tip.setClickable(true);
                avatarLay.setVisibility(View.GONE);

            } else if (state.equals("4")) {//认证中
                iv_waring.setImageResource(R.drawable.ic_witing_check);
                tv_state.setText(getString(R.string.register_checking_title));
                tv_state_tip.setText("若想加快认证速度，请联系院感科专职人员进行认证");
                accept_btn.setVisibility(View.GONE);
                tv_state_tip.setClickable(true);
                tv_email.setOnClickListener(null);
                sex.setOnClickListener(null);
            } else if (state.equals("6")) {//认证失败
                isReAuthority = true;
                tv_state_tip.setVisibility(View.VISIBLE);
                iv_waring.setImageResource(R.drawable.ic_no_pass);
                tv_state.setText(getString(R.string.register_checked_fail_title));
                accept_btn.setVisibility(View.VISIBLE);
                tv_state_tip.setText(tools.getValue(Constants.A_NOTE));
                rel_supervision.setOnClickListener(this);

            } else if (state.equals("0")) {
                avatar.setOnClickListener(null);
                iv_waring.setImageResource(R.drawable.ic_hasschecked);
                tv_state.setText(getString(R.string.register_checked_success_title));
                // accept_btn.setVisibility(View.GONE);
                tv_state_tip.setClickable(true);
                avatarLay.setVisibility(View.GONE);


            }


        }
        String department = tools.getValue(Constants.DEFULT_DEPARTID);
        boolean canChooseDepart = AbStrUtil.isEmpty(department);

        defultdeparTv.setText(tools.getValue(Constants.DEFULT_DEPART_NAME));
        supervision.setText(tools.getValue(Constants.JOB_NAME));

        userInfo.setRegis_job(tools.getValue(Constants.JOB));
        userInfo.setDepartment(department);
        userInfo.setSex(tools.getValue(Constants.SEX));
        sex.setText(userInfo.getSex().equals("0") ? "男" : "女");
        if (!AbStrUtil.isEmpty(userInfo.getRegis_job())) {
            if (userInfo.getRegis_job().equals("1")) {
                supervision.setText("感控科主任");
                defultdeparTv.setText("院感科");
                userInfo.setDepartmentName("1");
            } else if (userInfo.getRegis_job().equals("2")) {
                defultdeparTv.setText("院感科");
                supervision.setText("专职感控人员");
                userInfo.setDepartment("1");
            } else if (userInfo.getRegis_job().equals("3")) {
                supervision.setText("兼职感控人员");
            } else if (userInfo.getRegis_job().equals("4")) {
                supervision.setText("其他");
            }
        }
        userInfo.setEmail(tools.getValue(Constants.EMAIL));
        tv_email.setText(userInfo.getEmail());
        userInfo.setUsername(tools.getValue(Constants.NAME));
        name.setText(userInfo.getUsername());

        String aVata = tools.getValue(Constants.CERTIFY_PHOTO);
        mobileTv.setText(tools.getValue(Constants.MOBILE));

        if (!AbStrUtil.isEmpty(aVata)) {
            ImageLoader.getInstance().displayImage(
                    WebUrl.FILE_LOAD_URL
                            + aVata,
                    avatar, optionsSquare);

        }


            getJobLists();

    }


    /**
     * 认证
     */
    public void toSign() {
      showUncacleBleProcessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("sex", userInfo.getSex());
            job.put("department", userInfo.getDepartment());
            job.put("certify_photo", tools.getValue(Constants.CERTIFY_PHOTO));
            job.put("regis_job", userInfo.getRegis_job());
            job.put("name", name.getText().toString());
            job.put("email", tv_email.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, this,
                UPDATE_USERINFO, job, "user/updateUser");
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        super.onRequestSucesss(code, jsonObject);
        switch (code) {
            case GET_USER_DEPARTLIST:
                setDepartMent(jsonObject);
                break;
            case JOB_SUCESS:
                setJoblLisRes(jsonObject.toString());
                break;
            case UPDATE_USERINFO://认证申请提交成功
                Intent in = new Intent(mcontext, MainActivity.class);
                startActivity(in);
                break;
            case GET_USER_INFO:
                dismissdialog();
                JSONObject job = jsonObject.optJSONObject("user");
                UserUtis.setEditorRes(tools, job);
                initData();
                break;
        }

    }

    List<String> namelist = new ArrayList<String>();

    public List<String> getDepartList() {
        if (namelist.size() > 0) {
            return namelist;
        } else {
            namelist.size();
            for (JobEntity.ResultListBean dv : joblist) {
                namelist.add(dv.getData_name());
            }
            return namelist;
        }
    }

    protected void setJoblLisRes(String json) {
        joblist.clear();
        JobEntity info = TaskUtils.gson.fromJson(json, JobEntity.class);
        if (info.getResultList() != null && info.getResultList().size() > 0) {
            joblist.addAll(info.getResultList());
        }

        if (joblist.size() < 0) {
            return;
        }
        setJobDialog();
    }


    void setJobDialog() {

        jobDialog = new JobDialog(mcontext, joblist, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                supervision.setText(joblist.get(position).getData_name());
                jobId = joblist.get(position).getData_id();
                userInfo.setRegis_job(jobId);
                if (jobId.equals("1") || jobId.equals("2")) {//不需要选科室的岗位
                    defultdepartlay.setVisibility(View.GONE);
                    defultdeparTv.setText("院感科");
                    userInfo.setDepartment("1");
                } else if (jobId.equals("5")) {
                    defultdepartlay.setVisibility(View.GONE);
                    defultdeparTv.setText("暗访");
                    userInfo.setDepartment("");
                } else {
                    defultdepartlay.setVisibility(View.VISIBLE);
                    defultdeparTv.setText("");
                    userInfo.setDepartment("");

                }
                jobDialog.dismiss();
            }
        });

    }

    private void setDepartMent(JSONObject jsonObject) {
        dismissdialog();
        if (jsonObject.optString("result_id").equals("0")) {
            SharedPreferencesUtil.saveString(mcontext, "new_depart_data", jsonObject.toString());
        }
    }



    @Override
    public void onRequestFail(int code) {
        super.onRequestFail(code);
        switch (code) {
            case GET_USER_DEPARTLIST:
                dismissdialog();
                getDepartMentList();
                break;
            case GET_USER_INFO:
                dismissdialog();
                initData();
                break;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_btn:
                if (checkInfo()) {
                    toSign();
                }
                break;
            case R.id.defultdepartlay:
                Intent intent2 = new Intent(mcontext, DepartChooseActivity.class);
                startActivityForResult(intent2, DepartChooseActivity.CHOOSE_SUC);
                break;
            case R.id.rel_sex:
                sexDilog.show();
                break;
            case R.id.avatar:
                tokephote();
                break;
            case R.id.rel_supervision:
                jobDialog.show();
                break;
            case R.id.rl_email:
                Intent it2 = new Intent(this,
                        SettingNameActivity.class);
                it2.putExtra("typename", "email");
                startActivityForResult(it2, REQUSET);
                break;
            case R.id.name:
                if (!isReAuthority && isEditor) {//认证失败才能修改
                    return;
                }
                Intent it3 = new Intent(this,
                        SettingNameActivity.class);
                it3.putExtra("typename", "name");
                startActivityForResult(it3, REQUSET);
                break;
        }

    }

    // 获取岗位列表
    public void getJobLists() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("type", "ROLE_JOB_LIST");
            job.put("timeLong", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomReq(this, this, JOB_SUCESS,
                job, "comm/queryStaticByCode");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET
                && resultCode == RESULT_OK) {
            if (data.hasExtra(SettingNameActivity.KEY_USER_EMAIL)) {
                if (!AbStrUtil.isEmpty(data.getStringExtra(SettingNameActivity.KEY_USER_EMAIL))) {
                    String emailStr = data.getStringExtra(SettingNameActivity.KEY_USER_EMAIL);
                    tv_email.setText(emailStr);
                    updateInfo(UPDATE_USERINFO_EMAIL, "email", emailStr);
                }
            } else {
                if (!AbStrUtil.isEmpty(data.getStringExtra(SettingNameActivity.KEY_USER_ID))) {
                    name.setText(data.getStringExtra(SettingNameActivity.KEY_USER_ID));
                    String nameStr = data.getStringExtra(SettingNameActivity.KEY_USER_ID);
                    updateInfo(UPDATE_USERINFO_EMAIL, "email", nameStr);

                }
            }
        }else if (resultCode == DepartChooseActivity.CHOOSE_SUC) {
            DepartVos.DepartmentListBean bean = (DepartVos.DepartmentListBean) data.getSerializableExtra("departData");
            defultdeparTv.setText(bean.getName());
            userInfo.setDepartment(bean.getId() + "");
            if (isEditor) {
                updateInfo(UPDATE_USERINFO_DEPARTMENT, "department", bean.getId()+"");
            }
        }
    }


    public class JobDialog extends BaseListDialog<JobEntity.ResultListBean> {

        public JobDialog(Context context, List<JobEntity.ResultListBean> list, AdapterView.OnItemClickListener listener) {
            super(context, list, listener);
        }

        @Override
        protected void intUi() {
            right_txt.setText("取消");
            right_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jobDialog.dismiss();
                }
            });
            titleTv.setText("选择身份");

        }

        @Override
        public void setListDta(ViewHolder viewHolder, int position) {
            viewHolder.listtext.setText(joblist.get(position).getData_name());

        }
    }


    public boolean checkInfo() {
        String errorString = "";
        if (AbStrUtil.isEmpty(name.getText().toString()) || name.getText().toString().equals("请输入姓名")) {
            errorString = "请输入姓名";
        } else if (AbStrUtil.isEmpty(sex.getText().toString()) || sex.getText().toString().equals("请选择性别")) {
            errorString = "请选择性别";
        } else if (AbStrUtil.isEmpty(supervision.getText().toString()) || supervision.getText().toString().equals("请选择岗位")) {
            errorString = "请选择岗位";
        } else if (AbStrUtil.isEmpty(defultdeparTv.getText().toString()) || defultdeparTv.getText().toString().equals("请选择科室")) {
            errorString = "请选择科室";
        }
        if (!AbStrUtil.isEmpty(errorString)) {
            ToastUtils.showToast(this, errorString);
            return false;
        }
        return true;
    }

}
