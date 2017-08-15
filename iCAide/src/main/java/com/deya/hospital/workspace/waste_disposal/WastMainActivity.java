package com.deya.hospital.workspace.waste_disposal;

import android.view.View;

import com.deya.hospital.workspace.priviewbase.BaseFormMainActivity;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class WastMainActivity extends BaseFormMainActivity implements View.OnClickListener {

    @Override
    public Class<?> getActivityClass() {
        return WastFormActivity.class;
    }

    @Override
    public String getTaskType() {
        return "13";
    }
}
