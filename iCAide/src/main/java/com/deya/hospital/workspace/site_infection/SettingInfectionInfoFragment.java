package com.deya.hospital.workspace.site_infection;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class SettingInfectionInfoFragment extends BasePriviewInfoFragment implements View.OnClickListener {
    private EditText bedNumberTv;
    private EditText nameTv;
    private EditText roomNumberTv;
    private TextView moreView;
    private LinearLayout mutiInfolay;

    public static SettingInfectionInfoFragment newInstance(CaseListVo.CaseListBean caseVo) {
        SettingInfectionInfoFragment newFragment = new SettingInfectionInfoFragment();
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
        moreView= (TextView) rootView.findViewById(R.id.moreView);
        mutiInfolay= (LinearLayout) rootView.findViewById(R.id.moreLay);
        moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowMore=!isShowMore;
                moreView.setText(isShowMore?"点击收起":"查看全部");
                mutiInfolay.setVisibility(isShowMore?View.VISIBLE:View.GONE);
            }
        });
        bedNumberTv = (EditText) rootView.findViewById(R.id.bedNumberTv);
        nameTv = (EditText) rootView.findViewById(R.id.nameTv);
        roomNumberTv = (EditText) rootView.findViewById(R.id.roomNumberTv);
        if(null!=caseVo){
            departTv.setText(caseVo.getDepartmentName());
            nameTv.setText(caseVo.getPatient_name());
            if (!AbStrUtil.isEmpty(caseVo.getTime())) {
                timeTv.setText(caseVo.getTime());
            }
            roomNumberTv.setText(caseVo.getOperation_name());
            bedNumberTv.setText(caseVo.getPatient_id());
        }
        if(AbStrUtil.isEmpty(caseVo.getTime())){
            caseVo.setTime(TaskUtils.getLoacalDate());
        }
        timeTv.setText(caseVo.getTime());
        nameTv.setText(caseVo.getPatient_name());
    }

    @Override
    public int getLayout() {
        return R.layout.setinfection_info_fragment;
    }

    public void getInfo() {

    }


    @Override
    public void onDestroy() {
        if (null != datePicDialog && datePicDialog.isShowing()) {
            datePicDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public CaseListVo.CaseListBean getData() {
        caseVo.setTime(timeTv.getText().toString());
        caseVo.setDepartmentName(departTv.getText().toString());
        caseVo.setOperation_name(roomNumberTv.getText().toString());
        caseVo.setPatient_id(bedNumberTv.getText().toString());
        caseVo.setPatient_name(nameTv.getText().toString());
        return caseVo;
    }

    public String getDepartment() {
        return caseVo.getDepartment_id();
    }
}
