package com.deya.hospital.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/22
 */
public abstract class DYSimpleAdapter<T> extends BaseAdapter{
   public  List<T> list;
   public  LayoutInflater layoutInflater;
    public Context context;
    public DYSimpleAdapter(Context context, List<T> list){
        this.list=list;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(null==convertView){
            convertView =layoutInflater
                    .inflate(getLayoutId(), parent, false);
        }
        setView(position,convertView);
        return convertView;
    }

    protected abstract void setView(int position,View convertView);

    public abstract int  getLayoutId();
    public <T extends  View> T  findView(View convertView,int id){
        return BaseViewHolder.get(convertView,id);
    }

}
