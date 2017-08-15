package com.deya.hospital.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.handwash.DepartDataActivity;
import com.deya.hospital.vo.HandReportVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/10
 */
public class HandMainHospitalAdapter extends DYSimpleAdapter<HandReportVo.HospitalReportBean> {
    AdapterView.OnItemClickListener itemClickListener;
    public HandMainHospitalAdapter(Context context, List<HandReportVo.HospitalReportBean> list) {
        super(context, list);
    }

    @Override
    public int getCount() {
        return 1;
    }
    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener ){
        this.itemClickListener=itemClickListener;
    }

    @Override
    protected void setView(int position, View convertView) {
        TextView departmentTv=findView(convertView,R.id.departmentTv);
        departmentTv.setVisibility(View.GONE);
        View line=findView(convertView,R.id.line);
        line.setVisibility(View.GONE);
        ListView itemlist=findView(convertView,R.id.itemlist);
        ReportItemAdapter itemAdapter=new ReportItemAdapter(context,list);
        itemlist.setAdapter(itemAdapter);
            itemlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, DepartDataActivity.class);
                    intent.putExtra("name","本院");
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
