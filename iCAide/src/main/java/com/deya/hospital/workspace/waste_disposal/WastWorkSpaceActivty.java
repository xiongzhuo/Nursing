package com.deya.hospital.workspace.waste_disposal;

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
public class WastWorkSpaceActivty extends BaseWorkMainActivity{
    @Override
    protected int getTaskType() {
        return 13;
    }

    @Override
    protected String getWorkTitle() {
        return "医废管理";
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
        TaskUtils.addTask(this, 13, TaskUtils.getLoacalDate() + "");
    }
}
