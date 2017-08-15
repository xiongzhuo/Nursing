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
 * @date 2016/11/21
 */
public abstract class BaseClassifyAdapter<T> extends BaseAdapter{
    private  List<T> list;
    LayoutInflater layoutInflate;
    public BaseClassifyAdapter(Context context,List<T> list){
        this.list=list;
        layoutInflate=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
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
            convertView=layoutInflate.inflate(R.layout.item_question_group, null);
            viewHolder.name=(TextView) convertView.findViewById(R.id.nameTv);
            viewHolder.numTv=(TextView) convertView.findViewById(R.id.numTv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        setItem(viewHolder,position);
        return convertView;
    }
    public abstract void  setItem(ViewHolder viewHolder,int position);
    public class ViewHolder {
        ImageView headImg,line;
        public TextView name;
        public  TextView numTv,hospitalTv,moreTv;
        RelativeLayout hospitalLay;
        LinearLayout moreLay;

    }
}
