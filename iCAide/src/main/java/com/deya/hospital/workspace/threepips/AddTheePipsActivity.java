package com.deya.hospital.workspace.threepips;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.SimpleSwitchButton;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.multidrugresistant.RistantAndThreePipesActivity;
import com.deya.hospital.workspace.priviewbase.CaseUtil;
import com.deya.hospital.workspace.priviewbase.ResistantListActivity;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONObject;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class AddTheePipsActivity extends RistantAndThreePipesActivity implements SimpleSwitchButton.SimpleSwitchInter {
    LinearLayout sanguanLay;
    SimpleSwitchButton sanguan1, sanguan2, sanguan3;


    @Override
    public int getLayoutId() {
        return R.layout.ristant_threepipes_layout;
    }

    @Override
    public void saveCache() {
        setCase();
        if(AbStrUtil.isEmpty(getTempStrs())){
            finish();
            return;
        }
        String string = TaskUtils.gson.toJson(caseVo);
        caseVo.setUploadStatus(2);
        caseVo.setTemp_type_id(2);
        CaseUtil.onAddCaseInDb(caseVo);
        Intent inten = new Intent();
        inten.setAction(ResistantListActivity.UPDATE_ADD);
        inten.putExtra("data", caseVo);
        sendBroadcast(inten);
        finish();
    }

    @Override
    public void initView() {
        topView.setTitle("添加三管病例");

        sanguanLay = (LinearLayout) this.findViewById(R.id.sanguanLay);
        sanguanLay.setVisibility(View.VISIBLE);
        findViewById(R.id.mutitextLay1).setVisibility(View.GONE);
        sexTv = (TextView) this.findViewById(R.id.sexTv);
        sexTv.setOnClickListener(this);
        scoreEdt = (EditText) this.findViewById(R.id.scoreEdt);
        sanguan1 = (SimpleSwitchButton) this.findViewById(R.id.sanguan1);
        sanguan1.setText("呼吸机");
        sanguan2 = (SimpleSwitchButton) this.findViewById(R.id.sanguan2);
        sanguan2.setText("血管导管");
        sanguan3 = (SimpleSwitchButton) this.findViewById(R.id.sanguan3);
        sanguan3.setText("导尿管");
        sanguan1.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
            @Override
            public void onCheckChange(boolean ischeck) {
                caseVo.setTemp1(ischeck ? 1 : 0);
            }
        });
        sanguan1.setCheck(false);
        sanguan2.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
            @Override
            public void onCheckChange(boolean ischeck) {
                caseVo.setTemp2(ischeck ? 1 : 0);
            }
        });
        sanguan2.setCheck(false);
        sanguan3.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
            @Override
            public void onCheckChange(boolean ischeck) {
                caseVo.setTemp3(ischeck ? 1 : 0);
            }
        });
        sanguan3.setCheck(false);
        if (getIntent().hasExtra("casedata")) {//详情里面的病例
            caseVo = (CaseListVo.CaseListBean) getIntent().getSerializableExtra("casedata");
            initBaseData();
            sanguan1.setCheck(caseVo.getTemp1() == 1 ? true : false);
            sanguan1.setEnabled(false);
            sanguan2.setEnabled(false);
            sanguan3.setEnabled(false);
            sanguan2.setCheck(caseVo.getTemp2() == 1 ? true : false);
            sanguan3.setCheck(caseVo.getTemp3() == 1 ? true : false);
        }
    }

    @Override
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
        if (caseVo.getTemp1() == 0 && caseVo.getTemp2() == 0 && caseVo.getTemp3() == 0) {
            ToastUtil.showMessage("请至少选择一项三管内容");
            dismissdialog();
            return;
        }
        super.sendCase();
    }

    @Override
    public void setBaseJson(JSONObject job) {
        try {
            job.put("sex", caseVo.getSex());
            job.put("apache", caseVo.getApache());
            job.put("temp_type_id", "2");
            job.put("temp1", caseVo.getTemp1());
            job.put("temp2", caseVo.getTemp2());
            job.put("temp3", caseVo.getTemp3());
            if(caseVo.getCase_id()!=0){
                job.put("case_id", caseVo.getCase_id());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTempStrs() {
        String string = "";
        if (caseVo.getTemp1() == 1) {
            string += "3";
        } else if (caseVo.getTemp2() == 1) {
            if (!AbStrUtil.isEmpty(string)) {
                string += ",4";
            } else {
                string += "4";
            }
        } else if (caseVo.getTemp3() == 1) {
            if (!AbStrUtil.isEmpty(string)) {
                string += ",5";
            } else {
                string += "5";
            }
        }
        return string;
    }

    @Override
    public void setCase() {
        caseVo.setBed_no(bedNumberTv.getText().toString());
        caseVo.setPatient_id(patientIdEdt.getText().toString());
        caseVo.setTime(timeTv.getText().toString());
        caseVo.setPatient_name(nameTv.getText().toString());
        caseVo.setTime(timeTv.getText().toString());
        caseVo.setApache(scoreEdt.getText().toString());

    }


    @Override
    public void onCheckChange(boolean ischeck) {

    }


}
