package com.deya.hospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseViewHolder;
import com.deya.hospital.vo.ReportDetailVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/10
 */
public class HandReportDetailAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    ReportDetailVo reportDetailVo;
    Context context;

    public HandReportDetailAdapter(Context context, ReportDetailVo reportDetailVo) {
        layoutInflater = LayoutInflater.from(context);
        this.reportDetailVo = reportDetailVo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return null==reportDetailVo?0:5;
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
        if (null == convertView) {
            convertView = layoutInflater
                    .inflate(R.layout.repor_detail_lay, parent, false);
        }
        ListView listView = BaseViewHolder.get(convertView, R.id.listView);
        List<ReportDetailVo.HandFunReportDataBean> handFunReportDataBeenList = null;
        TextView txt1= BaseViewHolder.get(convertView,R.id.txt1);
        TextView txt2= BaseViewHolder.get(convertView,R.id.txt2);
        TextView txt3= BaseViewHolder.get(convertView,R.id.txt3);
        TextView txt4= BaseViewHolder.get(convertView,R.id.txt4);
        TextView txt5= BaseViewHolder.get(convertView,R.id.txt5);
        TextView txt6= BaseViewHolder.get(convertView,R.id.txt6);
        switch (position) {
            case 0:
                txt1.setText("来源");
                handFunReportDataBeenList
                        = reportDetailVo.getTimesReportData();
                break;
            case 1:
                txt1.setText("按岗位");
               handFunReportDataBeenList
                        = reportDetailVo.getPostReportData();
                break;
            case 2:
                txt1.setText("按指征");
                handFunReportDataBeenList
                        = reportDetailVo.getColTypeReportData();
                break;
            case 3:
                txt2.setVisibility(View.INVISIBLE);
                txt4.setText("正确数");
                txt5.setText("正确率");
                txt6.setText("不规范");
                txt1.setText("洗手方式");
                txt3.setText("次数");
                handFunReportDataBeenList
                        = reportDetailVo.getHandFunReportData();
                break;
            case 4:
                txt1.setText("按人员");
                handFunReportDataBeenList
                        = reportDetailVo.getPnameReportData();
                break;

        }
        ReportItemItemAdapter reportItemItemAdapter = new ReportItemItemAdapter(context, handFunReportDataBeenList);
        reportItemItemAdapter.setPosition(position);
        listView.setAdapter(reportItemItemAdapter);
        return convertView;
    }

}
