package com.deya.hospital.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deya.acaide.R;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/9/23
 */
public abstract class BaseTypeAdapter<T> extends BaseAdapter {
    private Context context;
    List<T> list;

    public BaseTypeAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewhoder;
        if (convertView == null) {
            viewhoder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_job_type, null);
            viewhoder.textView= (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewhoder);
        }else{
            viewhoder= (ViewHolder) convertView.getTag();
        }
        setViewData(viewhoder,position);
        return convertView;
    }

   public abstract  void setViewData(ViewHolder viewhoder, int position);
    public class ViewHolder {
        public TextView textView;
    }
}
