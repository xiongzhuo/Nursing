package com.deya.hospital.workspace.other_forms;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.DatePicDialog;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.priviewbase.BasePriviewInfoFragment;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class OthertInfoFragment extends BasePriviewInfoFragment implements View.OnClickListener {
    private EditText bedNumberTv;
    private EditText nameTv;
    private EditText roomNumberTv;

    public static OthertInfoFragment newInstance(CaseListVo.CaseListBean caseVo) {
        OthertInfoFragment newFragment = new OthertInfoFragment();
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
        departTv = (TextView) this.findViewById(R.id.departTv);
        departTv.setOnClickListener(this);
        bedNumberTv = (EditText) this.findViewById(R.id.bedNumberTv);
        nameTv = (EditText) this.findViewById(R.id.nameTv);
        timeTv = (TextView) this.findViewById(R.id.timeTv);
        timeTv.setOnClickListener(this);
        timeTv.setText(TaskUtils.getLoacalDate());
        caseVo.setTime(TaskUtils.getTaskMissionTime(TaskUtils.getLoacalDate()));
        roomNumberTv = (EditText) this.findViewById(R.id.roomNumberTv);
        findViewById(R.id.bedNumberLay).setVisibility(View.GONE);
        findViewById(R.id.roomLay).setVisibility(View.GONE);
        datePicDialog = new DatePicDialog(getActivity(),
                new DatePicDialog.OnDatePopuClick() {

                    @Override
                    public void enter(String text) {
                        timeTv.setText(text);
                       caseVo.setTime(TaskUtils.getTaskMissionTime(text));
                    }

                });

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
