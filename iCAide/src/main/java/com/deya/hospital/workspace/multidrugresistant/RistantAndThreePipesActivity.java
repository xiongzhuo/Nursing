package com.deya.hospital.workspace.multidrugresistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.DepartChoosePop;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.supervisor.AddDepartmentActivity;
import com.deya.hospital.supervisor.PartTimeStaffDialog;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DatePicDialog;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.ResistantMutiTextVo;
import com.deya.hospital.widget.view.MultipleTextViewGroup;
import com.deya.hospital.workspace.BootomSelectDialog;
import com.deya.hospital.workspace.TaskUtils;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public abstract class RistantAndThreePipesActivity extends BaseActivity implements View.OnClickListener {
    public TextView departTv;
    public String departmentId = "";
    public EditText bedNumberTv;
    public EditText nameTv;
    public EditText patientIdEdt;
    public TextView sexTv;
    public  EditText scoreEdt;
    public CommonTopView topView;
    public LinearLayout sumbmitlay;
    public  Button button;
    public TextView timeTv;
    List<DepartLevelsVo> departlist = new ArrayList<>();

    public DepartChoosePop departDialog;
    public MultipleTextViewGroup rl, drugMutiTv;
    public MyHandler myHandler;
    public CaseListVo.CaseListBean caseVo;
    ResistantMutiTextVo rmtv, drugMtv;
    private PartTimeStaffDialog tipdialog, tipdialog2;
    public String type_id;
    private BootomSelectDialog sexDilog;
    DatePicDialog datePicDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        caseVo = new CaseListVo.CaseListBean();
        caseVo.setUploadStatus(2);
        TaskUtils.getDepartList(mcontext, departlist);
        initMyHandler();
        init();
    }

    public abstract int getLayoutId();

    private void init() {
        type_id = getIntent().getStringExtra("type_id");
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);
        topView.onbackClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AbStrUtil.isEmpty(departTv.getText().toString()) && !AbStrUtil.isEmpty(bedNumberTv.getText().toString())) {
                    tipdialog.show();
                } else {
                    tipdialog2.show();
                }
            }
        });
        departTv = (TextView) this.findViewById(R.id.departTv);
        departTv.setOnClickListener(this);
        bedNumberTv = (EditText) this.findViewById(R.id.bedNumberTv);
        nameTv = (EditText) this.findViewById(R.id.nameTv);
        patientIdEdt = (EditText) this.findViewById(R.id.patientIdEdt);
        timeTv = (TextView) this.findViewById(R.id.timeTv);
        timeTv.setOnClickListener(this);
        timeTv.setText(TaskUtils.getLoacalDate());
        caseVo.setTime(TaskUtils.getTaskMissionTime(TaskUtils.getLoacalDate()));
        sumbmitlay = (LinearLayout) this.findViewById(R.id.sumbmitlay);
        button = (Button) this.findViewById(R.id.sumbmitBtn);
        button.setOnClickListener(this);
        departDialog = new DepartChoosePop(mcontext, departlist,
                new DepartChoosePop.OnDepartPopuClick() {


                    @Override
                    public void onChooseDepart(String name, String id) {

                        departTv.setText(name);
                        caseVo.setDepartment_id(id);
                        caseVo.setDepartmentName(name);
                        caseVo.setDepartmentName(name);
                        caseVo.setDepartment_id(id);
                    }

                    @Override
                    public void onAddDepart() {
                        Intent it = new Intent(mcontext,
                                AddDepartmentActivity.class);
                        it.putExtra("data", (Serializable) departlist);
                        startActivityForResult(it, 0x22);

                    }
                });
        tipdialog = new PartTimeStaffDialog(mcontext, false, "是否保存本次编辑？",
                new PartTimeStaffDialog.PDialogInter() {
                    @Override
                    public void onEnter() {
                        saveCache();
                    }

                    @Override
                    public void onCancle() {
                        finish();
                    }
                });

        tipdialog2 = new PartTimeStaffDialog(mcontext, false, "是否放弃本次编辑？",
                new PartTimeStaffDialog.PDialogInter() {
                    @Override
                    public void onEnter() {
                        finish();
                    }

                    @Override
                    public void onCancle() {
                        tipdialog2.cancel();
                        finish();
                    }
                });

        datePicDialog = new DatePicDialog(mcontext,
                new DatePicDialog.OnDatePopuClick() {

                    @Override
                    public void enter(String text) {
                        timeTv.setText(text);
                        caseVo.setTime(TaskUtils.getTaskMissionTime(text));

                    }

                });


        initSexDialog();
        initView();
    }


    public void initBaseData() {
        topView.setTitle("修改病例");
        button.setText("提交");

        departTv.setText(caseVo.getDepartmentName());
        bedNumberTv.setText(caseVo.getBed_no());
        nameTv.setText(caseVo.getPatient_name());
        patientIdEdt.setText(caseVo.getPatient_id());
        sexTv.setText(caseVo.getSex() == 1 ? "女" : "男");
        sexTv.setOnClickListener(this);
        scoreEdt.setText(caseVo.getApache());
        timeTv.setText(caseVo.getTime());
    }


    public abstract void saveCache();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent redata) {
        super.onActivityResult(requestCode, resultCode, redata);
        if (resultCode == 0x22 && null != redata) {
            ChildsVo dv = (ChildsVo) redata.getSerializableExtra("data");
            String rooId = dv.getParent();
            for (DepartLevelsVo dlv : departlist) {
                if (rooId.equals("1") && dlv.getRoot().getId().equals("0")) {
                    // dlv.setChooseNum(dlv.getChooseNum() + 1);
                    dlv.getChilds().add(0, dv);
                } else if (dlv.getRoot().getId().equals(rooId)) {
                    // dlv.setChooseNum(dlv.getChooseNum() + 1);
                    dlv.getChilds().add(0, dv);
                }
            }
            departDialog.setdata(departlist);

        } else if (resultCode == 0x23 && null != redata) {
            ChildsVo dv2 = (ChildsVo) redata.getSerializableExtra("data");
            departTv.setText(dv2.getName());
            departmentId = dv2.getId();
        }

    }


    @Override
    protected void onDestroy() {
        if (null != departDialog && departDialog.isShowing()) {
            departDialog.dismiss();
        }
        if (null != sexDilog && sexDilog.isShowing()) {
            sexDilog.dismiss();
        }

        if (null != tipdialog && tipdialog.isShowing()) {
            tipdialog.dismiss();
        }
        if (null != tipdialog2 && tipdialog2.isShowing()) {
            tipdialog2.dismiss();
        }
        if (null != datePicDialog && datePicDialog.isShowing()) {
            datePicDialog.dismiss();
        }
        super.onDestroy();
    }

    public abstract void initView();


    public void initMyHandler() {
        myHandler = new MyHandler(this) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = myHandler.mactivity.get();
                if (null != activity) {
                    switch (msg.what) {
                        case RisitantRequestCode.SEB_CASE_SUCESS:
                            dismissdialog();
                            if (null != msg.obj) {
                                try {
                                    setDataResult(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e5) {
                                    e5.printStackTrace();
                                }
                            }
                            break;
                        case RisitantRequestCode.SEB_CASE_FAIL:
                            dismissdialog();
                            ToastUtil.showMessage("亲，您的网络不顺畅哦！");
                            break;
                        default:
                            break;
                    }
                }
            }
        };
    }


    private void setDataResult(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            Intent intent = new Intent();
            intent.putExtra("caseVo", caseVo);
            setResult(0x119, intent);
            finish();
        }
        //  ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sumbmitBtn:
                doSumbmit();
                break;
            case R.id.departTv:
                departDialog.show();
                break;
            case R.id.sexTv:
                sexDilog.show();
                break;
            case R.id.timeTv:
                datePicDialog.show();
                break;
        }

    }
    public void doSumbmit() {
        if (caseVo.getDepartment_id().equals("")) {
            departDialog.show();
            ToastUtil.showMessage("请先选择科室");
            return;
        }


        showprocessdialog();
        setCase();
        sendCase();

    }


    public void sendCase() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
            job.put("department_id", caseVo.getDepartment_id());
            job.put("bed_no", caseVo.getBed_no());
            job.put("patient_name", caseVo.getPatient_name());
            job.put("patient_id", caseVo.getPatient_id());
            job.put("time", caseVo.getTime());
            job.put("create_time", caseVo.getTime());
            job.put("mission_time", caseVo.getTime());

            setBaseJson(job);
            if(caseVo.getCase_id()!=0){
                job.put("case_id", caseVo.getCase_id());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, this, RisitantRequestCode.SEB_CASE_SUCESS,
                RisitantRequestCode.SEB_CASE_FAIL, job, "tempTask/submitCase");
    }
    public abstract void setBaseJson(JSONObject job );

    public abstract void setCase();


    public void showTips() {
        tipdialog.show();
    }


    public void initSexDialog() {
        String titles[] = {"男", "女"};
        sexDilog = new BootomSelectDialog(mcontext, titles,
                new BootomSelectDialog.BottomDialogInter() {

                    @Override
                    public void onClick3() {

                    }

                    @Override
                    public void onClick2() {
                        sexTv.setText("女");
                        caseVo.setSex(1);
                    }

                    @Override
                    public void onClick1() {
                        sexTv.setText("男");
                        caseVo.setSex(0);
                    }

                    @Override
                    public void onClick4() {
                        // TODO Auto-generated method stub

                    }
                });
    }
}
