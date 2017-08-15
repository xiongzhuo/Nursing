package com.deya.hospital.workspace.safeinjection;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.workspace.priviewbase.BasePriviewInfoFragment;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class SafeInjectionInfoFragment extends BasePriviewInfoFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    RadioGroup radioGroup1, radioGroup2;

    public static SafeInjectionInfoFragment newInstance(CaseListVo.CaseListBean caseVo) {
        SafeInjectionInfoFragment newFragment = new SafeInjectionInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", caseVo);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.safeinjection_info_fragment;
    }

    @Override
    protected void initBuesinisData() {
        Bundle args = getArguments();
        caseVo = (CaseListVo.CaseListBean) args.getSerializable("data");
    }

    @Override
    public void initChildView() {
        radioGroup1 = (RadioGroup) rootView.findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(this);
        radioGroup2 = (RadioGroup) rootView.findViewById(R.id.radioGroup2);

        radioGroup2.setOnCheckedChangeListener(this);
        if (null != caseVo) {
            departTv.setText(caseVo.getDepartmentName());
            if (!AbStrUtil.isEmpty(caseVo.getTime())) {
                timeTv.setText(caseVo.getTime());
            }
            if (AbStrUtil.isEmpty(caseVo.getOperation_type())) {
                caseVo.setOperation_name("注射");
                caseVo.setOperation_type("1");
            }
            int type = Integer.parseInt(caseVo.getOperation_type());
            switch (type) {
                case 1:
                    radioGroup2.check(R.id.radio3);
                    break;
                case 2:
                    radioGroup2.check(R.id.radio4);
                    break;
                case 3:
                    radioGroup2.check(R.id.radio5);
                    break;
            }
            if (AbStrUtil.isEmpty(caseVo.getWork_type())) {
                caseVo.setWork_type("2");
            }

            int type2 = Integer.parseInt(caseVo.getWork_type());
            switch (type2) {
                case 1:
                    radioGroup1.check(R.id.radio1);
                    break;
                case 2:
                    radioGroup1.check(R.id.radio2);
                    break;
            }
        }

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
        caseVo.setTemp_type_id(5);
        return caseVo;

    }

    public String getDepartment() {
        return caseVo.getDepartment_id();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == radioGroup1) {
            switch (checkedId) {
                case R.id.radio1:
                    caseVo.setWork_type("1");
                    break;
                case R.id.radio2:
                    caseVo.setWork_type("2");
                    break;
            }

        } else if (group == radioGroup2) {
            switch (checkedId) {
                case R.id.radio3:
                    caseVo.setOperation_type("1");
                    break;
                case R.id.radio4:
                    caseVo.setOperation_type("2");
                    break;
                case R.id.radio5:
                    caseVo.setOperation_type("3");
                    break;
            }

        }

    }


}
