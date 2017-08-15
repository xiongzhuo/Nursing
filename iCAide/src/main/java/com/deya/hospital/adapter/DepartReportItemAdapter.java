package com.deya.hospital.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.vo.HandReportVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/11
 */
public class DepartReportItemAdapter extends DYSimpleAdapter<HandReportVo.DepartmentReportBean.DataListBean> {
    public DepartReportItemAdapter(Context context, List<HandReportVo.DepartmentReportBean.DataListBean> list) {
        super(context, list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    protected void setView(int position, View convertView) {
        TextView txt1= findView(convertView,R.id.txt1);
        TextView txt2= findView(convertView,R.id.txt2);
        TextView txt3= findView(convertView,R.id.txt3);
        TextView txt4= findView(convertView,R.id.txt4);
        TextView txt5= findView(convertView,R.id.txt5);
        TextView txt6= findView(convertView,R.id.txt6);
        HandReportVo.DepartmentReportBean.DataListBean bean;
        if(position<list.size()){
            bean=list.get(position);
        }else{
            bean=null;
        }

        switch (bean.getCheck_type()){
            case 10:
                txt1.setText("抽查");
                    break;
            case 20:
                txt1.setText("自查");
                break;
            case 30:
                txt1.setText("暗访");
                break;

        }

        txt2.setText(null==bean?"0":bean.getTimers()+"");
        txt3.setText(null==bean?"0":bean.getYc_cnt()+"");
        txt4.setText(null==bean?"0":bean.getYc_rate()+"");
        txt5.setText(null==bean?"0":bean.getValid_cnt()+"");
        txt6.setText(null==bean?"0":bean.getValid_rate()+"");

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_report_table_item;
    }
}
