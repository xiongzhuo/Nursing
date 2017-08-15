package com.deya.hospital.workspace.site_infection;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.deya.acaide.R;
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
public class SiteInfectionCaseActivity extends RistantAndThreePipesActivity {
    EditText roomNumberTv;

    @Override
    public int getLayoutId() {
        return R.layout.setinfection_info_fragment;
    }

    @Override
    public void saveCache() {
        setCase();
        caseVo.setUploadStatus(2);
        caseVo.setMission_time(caseVo.getTime());
        caseVo.setTemp_type_id(3);
        CaseUtil.onAddCaseInDb(caseVo);
        Intent inten = new Intent();
        inten.setAction(ResistantListActivity.UPDATE_ADD);
        inten.putExtra("data", caseVo);
        sendBroadcast(inten);
        finish();
    }

    @Override
    public void initView() {
        findViewById(R.id.topView).setVisibility(View.VISIBLE);
        findViewById(R.id.sumbmitlay).setVisibility(View.VISIBLE);
        roomNumberTv = (EditText) this.findViewById(R.id.roomNumberTv);
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

    @Override
    public void setCase() {
        caseVo.setDepartmentName(departTv.getText().toString());
        caseVo.setOperation_name(roomNumberTv.getText().toString());
        caseVo.setPatient_id(bedNumberTv.getText().toString());
        caseVo.setPatient_name(nameTv.getText().toString());
        caseVo.setTime(timeTv.getText().toString());
    }

    @Override
    public void setBaseJson(JSONObject job) {
        try {
            job.put("create_time", caseVo.getTime());
            job.put("operation_name", caseVo.getOperation_name());
            job.put("patient_id", caseVo.getPatient_id());
            job.put("patient_name", caseVo.getPatient_name());
            job.put("temp_type_id", "4");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
