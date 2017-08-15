package com.deya.hospital.workspace.workspacemain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.DynamicVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class TodayDynamicAdapter extends BaseAdapter {
    List<DynamicVo.ListBean> list;
    LayoutInflater layoutInflater;
    int type;
    Context context;

    public TodayDynamicAdapter(Context context, List<DynamicVo.ListBean> list,int type) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.type=type;
        this.context=context;
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
            convertView = layoutInflater.inflate(R.layout.item_today_dynamic_adapter, null);
            viewHolder.itemTxt = (TextView) convertView.findViewById(R.id.itemTxt);
            viewHolder.itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);
            viewHolder.itemNum = (TextView) convertView.findViewById(R.id.itemNum);
            viewHolder.lauout = (LinearLayout) convertView.findViewById(R.id.lauout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DynamicVo.ListBean bean = list.get(position);
        viewHolder.itemTxt.setText(bean.getName());
       // viewHolder.itemNum.setText(bean.getNum() < 0 ? "未接入\n数据" : bean.getNum() + "");
        viewHolder.itemNum.setText(bean.getNum() < 0 ? context.getResources().getString(R.string.cantnot_get_hospital_data) : bean.getNum() + "");
        if(type==1){
            viewHolder.itemIcon.setImageResource(R.drawable.daynamic_circle_blue);
            viewHolder.itemTxt.setTextColor(context.getResources().getColor(R.color.list_title));
            viewHolder.itemNum.setTextColor((context.getResources().getColor(R.color.top_color)));
            viewHolder.lauout.setBackgroundResource(R.color.white);
        }else{
            viewHolder.itemIcon.setImageResource(R.drawable.daynamic_circle);
            viewHolder.itemTxt.setTextColor(context.getResources().getColor(R.color.white));
        }
        return convertView;
    }

    private class ViewHolder {
        TextView itemTxt, itemNum;
        ImageView itemIcon;
        LinearLayout lauout;

    }
}
