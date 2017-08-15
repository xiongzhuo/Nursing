package com.deya.hospital.workspace.workspacemain;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.vo.TaskTypePopVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class TaskPopWindow extends BaseDialog implements View.OnClickListener {

    GridView taskTypeGv;
    TaskTypeAdapter adapter;
    Context context;
    List<TaskTypePopVo> list = new ArrayList<>();
    TasktypeChooseListener inter;

    /**
     * Creates a new instance of MyDialog.
     *
     * @param context
     */
    public TaskPopWindow(Context context, TasktypeChooseListener inter) {
        super(context);
        this.context = context;
        this.inter = inter;
    }

    public void setData(List<TaskTypePopVo> list) {
        this.list.clear();
        for (TaskTypePopVo vo : list) {
            if (vo.getOpenState() == 1) {
                this.list.add(vo);
            }

        }

        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow_tasktype_choose);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        initView();
    }

    private void initView() {
        taskTypeGv = (GridView) this.findViewById(R.id.taskTypeGv);
        adapter = new TaskTypeAdapter(context, list);
        findViewById(R.id.cancle).setOnClickListener(this);
        TextView confirm = (TextView) findViewById(R.id.confirm);
        confirm.setText("设置");
        confirm.setOnClickListener(this);
        taskTypeGv.setAdapter(adapter);
        taskTypeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inter.onChooseType(list.get(position).getId());
                dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle:
                dismiss();
                break;
            case R.id.confirm:
                inter.onChooseSetting();
                dismiss();
                break;
        }

    }
}
