package com.deya.hospital.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseListDialog;
import com.deya.hospital.base.DepartChooseActivity;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.services.BaseDataInitService;
import com.deya.hospital.setting.HospitalListActivity;
import com.deya.hospital.setting.SettingNameActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.vo.JobEntity;
import com.deya.hospital.vo.UserInfoVo;
import com.deya.hospital.workspace.TaskUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 * 认证页面
 */
public class UserInfoActivity extends GaveAuthority {
    private static final int REQUSET = 0x103;
    List<DepartLevelsVo> departlist;
    JobDialog jobDialog;
    public String jobId="";
    LinearLayout stateLay;
    boolean isReAuthority;
    TextView hospitalTv;


    @Override
    public int getLayoutId() {
        return R.layout.user_renzheng_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refuseBtn.setVisibility(View.GONE);
        initOwnViews();
        userInfo = new UserInfoVo();
        accept_btn.setText("提交");
        topView.setTitle("完善资料");
        topView.onbackClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.putValue(Constants.AUTHENT, "");
                finish();
            }
        });
        departlist = new ArrayList<>();
        initData();

    }

    private void initOwnViews() {
        stateLay = (LinearLayout) this.findViewById(R.id.stateLay);
        hospitalTv = (TextView) this.findViewById(R.id.hospitalTv);
        hospitalTv.setText(tools.getValue(Constants.HOSPITAL_NAME));
        hospitalTv.setOnClickListener(this);

    }

    private void initData() {
        showprocessdialog();
        getJobLists();
        getDepartMentList();

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
            job.put("hospital",tools.getValue(Constants.HOSPITAL_ID));

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
                dismissdialog();
                setDepartMent(jsonObject);
                break;
            case JOB_SUCESS:
                setJoblLisRes(jsonObject.toString());
                break;
            case UPDATE_USERINFO://认证申请提交成功
                Intent intent2 = new Intent(mcontext, BaseDataInitService.class);
                intent2.putExtra(BaseDataInitService.INTENTCODE, BaseDataInitService.GETMODULE);
                startService(intent2);
                Intent in = new Intent(mcontext, MainActivity.class);
                startActivity(in);
                finish();
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
                getDepartMentList();
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
                if (AbStrUtil.isEmpty(tools.getValue(Constants.HOSPITAL_ID))) {
                    ToastUtils.showToast(mcontext, "请先选择医院！");

                    return;
                }
                if (userInfo.getRegis_job().equals("")) {
                    ToastUtils.showToast(mcontext, "请先选择身份！");

                    return;
                }
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
            case R.id.hospitalTv:
                Intent intent=new Intent(mcontext, HospitalListActivity.class);
                startActivityForResult(intent,0x111);
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
                }
            }
        } else if (resultCode == DepartChooseActivity.CHOOSE_SUC) {
            DepartVos.DepartmentListBean bean = (DepartVos.DepartmentListBean) data.getSerializableExtra("departData");
            defultdeparTv.setText(bean.getName());
            userInfo.setDepartment(bean.getId() + "");
        } else if (resultCode == 0x18 && data != null) {
            SharedPreferencesUtil.saveString(mcontext, "new_depart_data", "");
            SharedPreferencesUtil.saveString(mcontext,
                    tools.getValue(Constants.MOBILE)
                            + "history_departs",
                    "");

           tools.putValue(Constants.HOSPITAL_ID,data.getStringExtra("hospital_id"));
            tools.putValue(Constants.HOSPITAL_NAME,data.getStringExtra("hospital_name"));
            getDepartMentList();
            hospitalTv.setText(data.getStringExtra("hospital_name"));
            if (jobId.equals("1") || jobId.equals("2")) {//不需要选科室的岗位
                defultdepartlay.setVisibility(View.GONE);
                defultdeparTv.setText("院感科");
                userInfo.setDepartment("1");
            } else if (jobId.equals("5")) {
                defultdepartlay.setVisibility(View.GONE);
                defultdeparTv.setText("暗访");
                userInfo.setDepartment("");
            } else {

                defultdeparTv.setText("");
                userInfo.setDepartment("");
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

        if (AbStrUtil.isEmpty(hospitalTv.getText().toString().trim())) {
            errorString = "请选择医院";
        } else if (AbStrUtil.isEmpty(name.getText().toString()) || name.getText().toString().equals("请输入姓名")) {
            errorString = "请输入姓名";
        } else if (AbStrUtil.isEmpty(sex.getText().toString()) || sex.getText().toString().equals("请选择性别")) {
            errorString = "请选择性别";
        } else if (AbStrUtil.isEmpty(supervision.getText().toString()) || supervision.getText().toString().equals("请选择岗位")) {
            errorString = "请选择身份";
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
