package com.deya.hospital.workspace.other_forms;

import android.view.View;

import com.deya.hospital.workspace.priviewbase.BaseFormMainActivity;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class OtherFormtMainActivity extends BaseFormMainActivity implements View.OnClickListener {


    @Override
    public Class<?> getActivityClass() {
        return OtherFormActivity.class;
    }

    @Override
    public String getTaskType() {
        return "14";
    }

    @Override
    public void initView() {
        super.initView();
        topView.setTitle("更多");
    }
}
