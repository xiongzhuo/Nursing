package com.deya.hospital.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.handwash.DepartDataActivity;
import com.deya.hospital.vo.HandReportVo;
import com.deya.hospital.workspace.TaskUtils;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/10
 */
public class HandMainAdapter extends DYSimpleAdapter<HandReportVo.DepartmentReportBean> {
    private AdapterView.OnItemClickListener itemClickListener;

    public HandMainAdapter(Context context, List<HandReportVo.DepartmentReportBean> list) {
        super(context, list);
    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    protected void setView(int position, View convertView) {
        TextView departmentTv = findView(convertView, R.id.departmentTv);
        View line = findView(convertView, R.id.line);
        LinearLayout leftLay=findView(convertView, R.id.leftLay);
        TextView typeTv=findView(convertView, R.id.typeTv);
        line.setVisibility(View.GONE);
        if (TaskUtils.isPartTimeJob(context)) {
            line.setVisibility(View.GONE);
            departmentTv.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.VISIBLE);
            departmentTv.setVisibility(View.VISIBLE);
        }
        final HandReportVo.DepartmentReportBean departmentReportBean = getItem(position);
        departmentTv.setText(departmentReportBean.getDepartmentName());
        ListView itemlist = findView(convertView, R.id.itemlist);
        if(departmentReportBean.getDataList().size()<=1){
            typeTv.setVisibility(View.VISIBLE);
            leftLay.setVisibility(View.GONE);
        }else{
            typeTv.setVisibility(View.GONE);
            leftLay.setVisibility(View.VISIBLE);
        }
        DepartReportItemAdapter departReportItemAdapter = new DepartReportItemAdapter(context, departmentReportBean.getDataList());
        itemlist.setAdapter(departReportItemAdapter);
        itemlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(context, DepartDataActivity.class);
                intent.putExtra("name",departmentReportBean.getDepartmentName());
                intent.putExtra("departmentId",departmentReportBean.getDepartmentId()+"");
                intent.putExtra("indext",position);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.handmain_report;
    }
}
