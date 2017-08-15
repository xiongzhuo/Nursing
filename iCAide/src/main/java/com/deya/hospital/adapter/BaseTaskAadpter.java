package com.deya.hospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;
import com.google.gson.Gson;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public abstract class BaseTaskAadpter<T> extends BaseAdapter {
    public Context context;
    public LayoutInflater inflater;
    public List<T> list;
    Tools tools;
    Gson gson = new Gson();

    public BaseTaskAadpter(Context context, List<T> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        tools = new Tools(context, Constants.AC);

    }

    public void setData(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TaskAdapterViewHolder mviewHolder = null;
        if (convertView == null) {
            mviewHolder = new TaskAdapterViewHolder();
            convertView = inflater.inflate(R.layout.list_tasklist_item_2, null);
            mviewHolder.title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            mviewHolder.author = (TextView) convertView
                    .findViewById(R.id.tv_author);
            // mviewHolder.ycRate = (TextView) convertView
            // .findViewById(R.id.yc_rate);
            mviewHolder.stateImg = (ImageView) convertView
                    .findViewById(R.id.stateImg);
            mviewHolder.timeTypes = (TextView) convertView
                    .findViewById(R.id.timeTypes);
            mviewHolder.finishtime = (TextView) convertView
                    .findViewById(R.id.finishtime);
            mviewHolder.stateTv = (TextView) convertView
                    .findViewById(R.id.stateTv);
            mviewHolder.typeTv = (TextView) convertView
                    .findViewById(R.id.typeTv);
            convertView.setTag(mviewHolder);
        } else {
            mviewHolder = (TaskAdapterViewHolder) convertView.getTag();
        }

        setItem(position, mviewHolder);

        return convertView;
    }

    public abstract void setItem(int position, TaskAdapterViewHolder mviewHolder);

    public class TaskAdapterViewHolder {
        public TextView title;
        public TextView author;
        public TextView ycRate;
        public ImageView stateImg;
        // public TextView rtRate;// 正确率
        public TextView timeTypes, stateTv;
        public TextView finishtime;
        public TextView typeTv,typeTv2,typeTv3;
    }

    public int dp2Px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + (int) 0.5f);
    }
}
