package com.deya.hospital.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public abstract class BaseNextImgAdapter<T> extends BaseAdapter {
    List<T> list;
    LayoutInflater layoutInflater;

    public BaseNextImgAdapter(Context context, List<T> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);

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
        if(null==convertView){
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.item_question_group, null);
            viewHolder.name=(TextView) convertView.findViewById(R.id.nameTv);
            viewHolder.numTv=(TextView) convertView.findViewById(R.id.numTv);
            viewHolder.hospitalTv=(TextView) convertView.findViewById(R.id.hospitalTv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        getView(viewHolder,position);
        return convertView;
    }
    public abstract void getView(ViewHolder viewHolder,int position);
    public class ViewHolder {
        public ImageView headImg,line;
        public TextView name;
        public TextView numTv,hospitalTv,moreTv;
        public  RelativeLayout hospitalLay;
        public  LinearLayout moreLay;

    }
}
