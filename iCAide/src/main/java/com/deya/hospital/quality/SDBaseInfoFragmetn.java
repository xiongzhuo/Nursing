package com.deya.hospital.quality;

import android.os.Bundle;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.workspace.priviewbase.BasePriviewInfoFragment;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/2/20
 */
public class SDBaseInfoFragmetn extends BasePriviewInfoFragment {
    TextView hospitalTv;

    public static SDBaseInfoFragmetn newInstance(CaseListVo.CaseListBean caseVo) {
        SDBaseInfoFragmetn newFragment = new SDBaseInfoFragmetn();
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
        hospitalTv= (TextView) findViewById(R.id.hospitalTv);
        hospitalTv.setOnClickListener(this);
        caseVo.setDepartment_id("0");

    }

    @Override
    public int getLayout() {
        return R.layout.environment_info_fragment;
    }

    @Override
    public CaseListVo.CaseListBean getData() {
        return caseVo;
    }

}
