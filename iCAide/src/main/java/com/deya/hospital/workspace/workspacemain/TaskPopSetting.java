package com.deya.hospital.workspace.workspacemain;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.vo.TaskTypePopVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class TaskPopSetting extends BaseDialog implements View.OnClickListener {

    GridView taskTypeGv;
    TaskTypeSelectAdapter adapter;
    Context context;
    List<TaskTypePopVo> list = new ArrayList<>();
    TasktypeChooseListener inter;
    LayoutInflater layoutInflater;
    Map<Integer,Integer> map=new HashMap<>();
    /**
     * Creates a new instance of MyDialog.
     *
     * @param context
     */
    public TaskPopSetting(Context context, TasktypeChooseListener inter) {
        super(context);
        this.context = context;
        this.inter = inter;
    }

    public void setData(List<TaskTypePopVo> list) {
        this.list.clear();
        this.list.addAll(list);
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow_tasktype_choose);
        layoutInflater = LayoutInflater.from(context);
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
        adapter = new TaskTypeSelectAdapter();
        findViewById(R.id.cancle).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);

        taskTypeGv.setAdapter(adapter);
        taskTypeGv.setNumColumns(1);
        taskTypeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskTypePopVo taskTypePopVo=list.get(position);
                list.get(position).setOpenState(taskTypePopVo.getOpenState()==1?0:1);
                adapter.notifyDataSetChanged();

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
                inter.onSelectType(list);
                dismiss();
                break;

        }

    }

    private class TaskTypeSelectAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.tasktype_setingadapter_item, null);
                viewHolder.title = (TextView) convertView.findViewById(R.id.listtext);
                viewHolder.chooseBox = (ImageView) convertView.findViewById(R.id.check_img);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            TaskTypePopVo vo = list.get(position);
            viewHolder.title.setText(vo.getName().replace("\n","").trim());
            viewHolder.chooseBox.setImageResource(vo.getOpenState() == 1 ? R.drawable.choose_box_select : R.drawable.choose_box_normal);
            return convertView;
        }
    }

    private class ViewHolder {
        TextView title;
        ImageView chooseBox;

    }

}
