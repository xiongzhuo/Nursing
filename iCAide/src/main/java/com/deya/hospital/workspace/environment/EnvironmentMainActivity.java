package com.deya.hospital.workspace.environment;

import android.view.View;

import com.deya.hospital.workspace.priviewbase.BaseFormMainActivity;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class EnvironmentMainActivity extends BaseFormMainActivity2 implements View.OnClickListener {

    @Override
    public Class<?> getActivityClass() {
        return EnviromentFormActivity.class;
    }

    @Override
    public String getTaskType() {
        return "10";
    }
}
