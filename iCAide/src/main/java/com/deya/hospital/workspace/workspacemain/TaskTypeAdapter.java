package com.deya.hospital.workspace.workspacemain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.TaskTypePopVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class TaskTypeAdapter extends BaseAdapter {
    List<TaskTypePopVo> list;
    LayoutInflater layoutInflater;

    public TaskTypeAdapter(Context context, List<TaskTypePopVo> list) {
        this.list = list;
        layoutInflater = LayoutInflater.from(context);

    }

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_tasktype_adapter, null);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.itemIcon);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.itemTxt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TaskTypePopVo hotVo = list.get(position);
        viewHolder.icon.setImageResource(hotVo.getImgid());
        viewHolder.textView.setText(hotVo.getName());
        return convertView;
    }

    private class ViewHolder {
        ImageView icon;
        TextView textView;

    }
}
