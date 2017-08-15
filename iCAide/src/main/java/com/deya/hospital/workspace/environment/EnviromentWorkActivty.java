package com.deya.hospital.workspace.environment;

import android.view.View;

import com.deya.acaide.R;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.workspace.BaseWorkMainActivity;
import com.deya.hospital.workspace.TaskUtils;

import java.util.ArrayList;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/6/22
 */
public class EnviromentWorkActivty extends BaseWorkMainActivity{
    @Override
    protected int getTaskType() {
        return 10;
    }

    @Override
    protected String getWorkTitle() {
        return "环境清洁";
    }

    @Override
    protected DYSimpleAdapter getAdapter() {
        return new DYSimpleAdapter(mcontext,new ArrayList()) {
            @Override
            protected void setView(int position, View convertView) {

            }

            @Override
            public int getLayoutId() {
                return R.layout.item_document;
            }
        };
    }

    @Override
    protected void onAddTask() {
        TaskUtils.addTask(this, 10, TaskUtils.getLoacalDate() + "");
    }
}
