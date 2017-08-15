package com.deya.hospital.workspace.threepips;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.BaseTaskAadpter;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.CaseListVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class ThreePipsListAdapter extends BaseTaskAadpter<CaseListVo.CaseListBean> {
    private int drawables[] = {R.drawable.circle_1,
            R.drawable.circle_2, R.drawable.circle_3,
            R.drawable.circle_4, R.drawable.circle_3,
            R.drawable.circle_5, R.drawable.circle_2};
    public ThreePipsListAdapter(Context context, List<CaseListVo.CaseListBean> list) {
        super(context, list);
        this.list=list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskAdapterViewHolder mviewHolder = null;
        if (convertView == null) {
            mviewHolder = new TaskAdapterViewHolder();
            convertView = inflater.inflate(R.layout.list_threepips_item, null);
            mviewHolder.title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            mviewHolder.author = (TextView) convertView
                    .findViewById(R.id.tv_author);

            mviewHolder.finishtime = (TextView) convertView
                    .findViewById(R.id.finishtime);
            mviewHolder.stateTv = (TextView) convertView
                    .findViewById(R.id.stateTv);
            mviewHolder.typeTv = (TextView) convertView
                    .findViewById(R.id.typeTv1);
            mviewHolder.typeTv2 = (TextView) convertView
                    .findViewById(R.id.typeTv2);
            mviewHolder.typeTv3 = (TextView) convertView
                    .findViewById(R.id.typeTv3);
            convertView.setTag(mviewHolder);
        } else {
            mviewHolder = (TaskAdapterViewHolder) convertView.getTag();
        }

        CaseListVo.CaseListBean rv=list.get(position);
        if(null!=rv){
            String string = rv.getDepartmentName();
            string += AbStrUtil.isEmpty(rv.getBed_no()) ? "" : "   " + rv.getBed_no() + "床";
            string += AbStrUtil.isEmpty(rv.getPatient_name()) ? "" : "   " + rv.getPatient_name();
            mviewHolder.title.setText(string);
            mviewHolder.finishtime.setText(rv.getTime());
            mviewHolder.typeTv.setBackgroundResource(drawables[0]);
            mviewHolder.typeTv3.setBackgroundResource(drawables[1]);
            mviewHolder.typeTv2.setBackgroundResource(drawables[2]);
            mviewHolder.typeTv.setText("呼吸机");
            mviewHolder.typeTv2.setText("血管导管");
            mviewHolder.typeTv3.setText("导尿管");
            mviewHolder.stateTv.setText(list.get(position).getStatus()==1?"已完成":"未完成");
            if(rv.getTemp1()==1){
                mviewHolder.typeTv.setVisibility(View.VISIBLE);
            }else {
                mviewHolder.typeTv.setVisibility(View.GONE);
            }
            if(rv.getTemp2()==1){
                mviewHolder.typeTv2.setVisibility(View.VISIBLE);
            }else {
                mviewHolder.typeTv2.setVisibility(View.GONE);
            }
            if(rv.getTemp3()==1){
                mviewHolder.typeTv3.setVisibility(View.VISIBLE);
            }else {
                mviewHolder.typeTv3.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    @Override
    public void setItem(int position, TaskAdapterViewHolder mviewHolder) {

    }
}
