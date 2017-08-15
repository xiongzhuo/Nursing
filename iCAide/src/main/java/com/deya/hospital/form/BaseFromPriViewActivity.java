package com.deya.hospital.form;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseDepartChooseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.supervisor.PartTimeStaffDialog;
import com.deya.hospital.task.uploader.BaseUploader;
import com.deya.hospital.task.uploader.FormUploader;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.WorkSpaceFragment;
import com.deya.hospital.workspace.priviewbase.FormCommitSucTipsActivity;
import com.deya.hospital.workspace.supervisorquestion.SupervisorQesDetitalActivity;
import com.deya.hospital.workspace.supervisorquestion.SupervisorQueCreatActivity;
import com.deya.hospital.workspace.workspacemain.TodayDynamicFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/10/26
 */
public abstract class BaseFromPriViewActivity extends BaseDepartChooseActivity {
    private static final int SAVE_SUCESS = 0x7010;
    private static final int SAVE_FAILE = 0x7011;
    public ListView listView;
    public List<FormDetailListVo> list = new ArrayList<FormDetailListVo>();
    public RelativeLayout rlBack;
    public String id;
    public int formtype;
    public Button sumbmitBtn;
    public TextView titleTv;
    public LinearLayout sumbmitlay, titleLay;

    public TextView totalScoreTv, formTitleTv, departTv, totalScoreTv2, tipsTv;
    public double totalScore = 0;
    public String title = "";
    public TaskVo data;
    public boolean needAdd = false;
    public LinearLayout departLay;
    public boolean isOnlyPriview;
    public PartTimeStaffDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initBaseView();
        initChildView();
        initBaseData();
    }

    protected abstract void initBaseData();

    public void initLookData() {

        /**
         * 设置默认科室
         */
        String hospitalJob = tools.getValue(Constants.JOB);
        String defultDepart = tools.getValue(Constants.DEFULT_DEPARTID);
        if (null != hospitalJob && hospitalJob.equals("3") && !AbStrUtil.isEmpty(defultDepart)) {//
            // 兼职感控人员在设置了默认科室后可以直接跳过选择部分
            if (!AbStrUtil.isEmpty("defultDepart")) {

                data.setDepartmentName(tools.getValue(Constants.DEFULT_DEPART_NAME));
                data.setDepartment(defultDepart);
                departTv.setText(tools.getValue(Constants.DEFULT_DEPART_NAME));
            }
        }
        /**
         * 非本院模板 只能看是否已添加
         */
        if (getIntent().hasExtra("jastLook")) {
            titleTv.setText("预览");
            int isSaved = getIntent().getIntExtra("is_save", 0);
            isOnlyPriview = true;
            departLay.setVisibility(View.GONE);
            sumbmitlay.setVisibility(View.VISIBLE);
            sumbmitBtn.setText(isSaved == 0 ? "添加到本院" : "已添加到本院");
            sumbmitBtn.setEnabled(isSaved == 0 ? true : false);
            sumbmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addFormList(getIntent().getStringExtra("item_id"));
                }
            });
        }
    }

    protected void initBaseView() {

    }

    public abstract int getLayoutId();

    public abstract void initChildView();

    public abstract void saveCache(int status);

    /**
     * 保存其他医院模板到本医院
     */
    public void addFormList(String id) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("tmp_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler,
                this, SAVE_SUCESS, SAVE_FAILE, job,
                "grid/addTemplateToHospital");
    }

    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case SAVE_SUCESS:
                        //普通版需修改
                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                            try {
                                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                                ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
                                if (jsonObject.optString("result_id").equals("0")) {
                                    FormVo fv = (FormVo) getIntent().getExtras().get("FormVo");
                                    fv.setIs_save(1);
                                    DataBaseHelper.getDbUtilsInstance(mcontext).update(fv);
                                    if (jsonObject.has("id")) {
                                        fv.setId(jsonObject.optString("id"));
                                    }
                                    fv.setIs_open(0);
                                    fv.setUse_cnt("0");
                                    fv.setCreater(tools.getValue(Constants.USER_NAME));
                                    fv.setHospital(tools.getValue(Constants.HOSPITAL_NAME));
                                    DataBaseHelper.getDbUtilsInstance(mcontext).save(fv);
                                    sumbmitBtn.setEnabled(false);
                                    sumbmitBtn.setText("已添加到本院");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case SAVE_FAILE:
                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                            try {
                                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                                ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }

    };

    public void showTips() {
        dialog = new PartTimeStaffDialog(mcontext, false, "是否提交,提交之后就不能修改了哟！",
                new PartTimeStaffDialog.PDialogInter() {

                    @Override
                    public void onEnter() {
                        addMinRemark();
                        doSubmit();
                    }

                    @Override
                    public void onCancle() {
                        // TODO Auto-generated method stub

                    }
                });
        dialog.show();
    }

    /**
     * 添加备注
     */
    public abstract void addMinRemark();

    /**
     * 提交
     */
    public abstract void doSubmit();

    public void commsubmit(final TaskVo data) {

        TaskUtils.onCommitAfterTask(data);
        /**
         * 执行上传
         */
        showprocessdialog();

        if (NetWorkUtils.isConnect(mcontext)) {
            if (!NetWorkUtils.isWifiState(MyAppliaction.getContext())) {
                data.setStatus(1);
                onsendTaskFail(FormCommitSucTipsActivity.ONLY_WIFI);
                finish();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FormUploader uplodaer = (FormUploader) BaseUploader.getUploader(3);
                        if (uplodaer.Upload(data)) {
                            dismissdialog();
                            if (data.getDbid() > 0) {
                                TaskUtils.onUpdateTaskById(data);
                            } else {
                                TaskUtils.onAddTaskInDb(data);
                            }
                            Intent brodcastIntent = new Intent(mcontext,SupervisorQueCreatActivity.class);
                            if(AbStrUtil.isEmpty(data.getMain_remark_id())||data.getMain_remark_id().equals("0")){


                                brodcastIntent.setAction(TodayDynamicFragment.UPDATA_ACTION);
                                BaseFromPriViewActivity.this.sendBroadcast(brodcastIntent);
                                brodcastIntent.setClass(mcontext,FormCommitSucTipsActivity.class);
                                brodcastIntent.putExtra("data", data);
                            }else{
                                brodcastIntent.setClass(mcontext,SupervisorQesDetitalActivity.class);
                                TaskVo remark=TaskUtils.gson.fromJson(data.getMain_remark(), TaskVo.class);
                                remark.setIs_main_remark("1");
                                remark.setTask_id(Integer.parseInt(data.getMain_remark_id()));
                                remark.setDbid(1000000000);
                                brodcastIntent.putExtra("id", data.getMain_remark_id());
                            }
                            brodcastIntent.putExtra("commit_status", FormCommitSucTipsActivity.UPLOAD_SUC);
                            startActivity(brodcastIntent);
                            finish();
                        } else {
                            onsendTaskFail(FormCommitSucTipsActivity.UPLOAD_FIAL);
                        }

                    }
                }).start();

            }

        } else {
            onsendTaskFail(FormCommitSucTipsActivity.NET_WORK_DISCONECT);
            return;
        }


    }

    private void onsendTaskFail(int state) {
        dismissdialog();
        data.setStatus(1);
        if (data.getDbid() > 0) {
            TaskUtils.onUpdateTaskById(data);
        } else {
            TaskUtils.onAddTaskInDb(data);
        }
        Intent brodcastIntent = new Intent(mcontext, FormCommitSucTipsActivity.class);
        brodcastIntent.setAction(WorkSpaceFragment.UPDATA_ACTION);
        sendBroadcast(brodcastIntent);
        brodcastIntent.putExtra("data", data);
        brodcastIntent.putExtra("commit_status", state);
        startActivity(brodcastIntent);
        finish();
    }


}
