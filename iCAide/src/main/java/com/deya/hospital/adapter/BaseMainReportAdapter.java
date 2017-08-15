package com.deya.hospital.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.quality.HospitalDepartRepActivity;
import com.deya.hospital.quality.ItemDetailActivity;
import com.deya.hospital.vo.QualityReportVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/10
 */
public class BaseMainReportAdapter extends DYSimpleAdapter<QualityReportVo.ListBean> {
    private AdapterView.OnItemClickListener itemClickListener;
    int type;
    boolean isHospital;
    int  task_type=17;

    public BaseMainReportAdapter(Context context, int type, List<QualityReportVo.ListBean> list) {
        super(context, list);
        this.type=type;
    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setHospital(boolean isHospital){
        this.isHospital=isHospital;
    }
    public void setTaskType(int  task_type){
        this.task_type=task_type;
    }
    @Override
    protected void setView(int position, View convertView) {
        TextView departmentTv = findView(convertView, R.id.departmentTv);
        View line = findView(convertView, R.id.line);
        LinearLayout leftLay=findView(convertView, R.id.leftLay);
        TextView typeTv=findView(convertView, R.id.typeTv);
        TextView title=findView(convertView, R.id.title);

        final QualityReportVo.ListBean departmentReportBean = getItem(position);

        ListView itemlist = findView(convertView, R.id.itemlist);

        if (isHospital) {
            leftLay.setVisibility(View.GONE);
            departmentTv.setText(departmentReportBean.getItem_title()+"");
            departmentTv.setTextColor(context.getResources().getColor(R.color.black));
            line.setBackgroundColor(context.getResources().getColor(R.color.main_bg));
        } else {
            leftLay.setVisibility(View.VISIBLE);
            title.setText(departmentReportBean.getTempName());
            departmentTv.setText(departmentReportBean.getDepartmentName());
            departmentTv.setTextColor(context.getResources().getColor(R.color.orange));
            line.setBackgroundColor(context.getResources().getColor(R.color.orange));
        }
        BaseAdapter departReportItemAdapter =getItemAdapter(type,position);
        itemlist.setAdapter(departReportItemAdapter);
        itemlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("item_repo_id",departmentReportBean.getItem_repo_id()+"");
                intent.putExtra("listBean",departmentReportBean);
                intent.putExtra("task_type",task_type);
                if(isHospital){
                  intent.setClass(context, HospitalDepartRepActivity.class);
                    intent.putExtra("describe",departmentReportBean.getItem_title());
                    context.startActivity(intent);
                    return;
                }
                intent.setClass(context, ItemDetailActivity.class);
                intent.putExtra("indext",position);
                intent.putExtra("departmentName",departmentReportBean.getDepartmentName());
                intent.putExtra("departmentId",departmentReportBean.getDepartmentId());
                context.startActivity(intent);

            }
        });
    }

    private BaseAdapter getItemAdapter(int type,int position) {
        switch (type){
            case 3:
                return new QualityReportItemAdapter(context,list.get(position).getDataList());
            default:
                return null;
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.quality_main_report;
    }
}
