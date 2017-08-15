package com.deya.hospital.workspace.KeySectorsFrom;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.priviewbase.BasePriviewInfoFragment;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class KeySecotorstInfoFragment extends BasePriviewInfoFragment implements View.OnClickListener {
    private EditText bedNumberTv;
    private EditText nameTv;
    private EditText roomNumberTv;

    public static KeySecotorstInfoFragment newInstance(CaseListVo.CaseListBean caseVo) {
        KeySecotorstInfoFragment newFragment = new KeySecotorstInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", caseVo);
        newFragment.setArguments(bundle);
        return newFragment;
    }



    @Override
    protected void initBuesinisData() {
        Bundle args = getArguments();
        caseVo = (CaseListVo.CaseListBean) args.getSerializable("data");
    }
    @Override
    public void initChildView() {
        bedNumberTv = (EditText) this.findViewById(R.id.bedNumberTv);
        nameTv = (EditText) this.findViewById(R.id.nameTv);
        caseVo.setTime(TaskUtils.getTaskMissionTime(TaskUtils.getLoacalDate()));
        roomNumberTv = (EditText) this.findViewById(R.id.roomNumberTv);
        findViewById(R.id.bedNumberLay).setVisibility(View.GONE);
        findViewById(R.id.roomLay).setVisibility(View.GONE);

        if(null!=caseVo){
            departTv.setText(caseVo.getDepartmentName());
            nameTv.setText(caseVo.getPerson_liable());
            if(AbStrUtil.isEmpty(caseVo.getTime())){
                caseVo.setTime(TaskUtils.getLoacalDate());
            }
            timeTv.setText(caseVo.getTime());
            roomNumberTv.setText(caseVo.getRoom_no());
            bedNumberTv.setText(caseVo.getBed_no());
        }
    }

    @Override
    public int getLayout() {
        return R.layout.environment_info_fragment;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.timeTv:
                datePicDialog.show();
                break;
        }

    }



    public CaseListVo.CaseListBean getData() {
        caseVo.setMission_time(timeTv.getText().toString());
        caseVo.setDepartmentName(departTv.getText().toString());
        caseVo.setRoom_no(roomNumberTv.getText().toString());
        caseVo.setBed_no(bedNumberTv.getText().toString());
        caseVo.setPerson_liable(nameTv.getText().toString());
        caseVo.setTime(timeTv.getText().toString());
        caseVo.setTemp_type_id(3);
        return caseVo;

    }

    public String getDepartment() {
        return caseVo.getDepartment_id();
    }
}
