package com.deya.hospital.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.QualityReportVo;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/8
 */
public class QualityReportItemAdapter extends DYSimpleAdapter<QualityReportVo.ListBean.DataListBean> {
    public QualityReportItemAdapter(Context context, List<QualityReportVo.ListBean.DataListBean> list) {
        super(context, list);
    }

    @Override
    public int getCount() {
        return list.size()+1;
    }


    @Override
    protected void setView(int position, View convertView) {
        TextView txt1= findView(convertView, R.id.txt1);
        TextView txt2= findView(convertView,R.id.txt2);
        TextView txt3= findView(convertView,R.id.txt3);
        View line=findView(convertView,R.id.line);

        if(position==0){
            txt1.setTextColor(context.getResources().getColor(R.color.top_color));
            txt2.setTextColor(context.getResources().getColor(R.color.top_color));
            txt3.setTextColor(context.getResources().getColor(R.color.top_color));
            txt1.setText("来源");
            txt2.setText("次数");
            txt3.setText("平均分");
            line.setVisibility(View.VISIBLE);
        }else if(position>0&&position<=list.size()){
            QualityReportVo.ListBean.DataListBean bean=getItem(position-1);
            switch (bean.getCheck_type()){
            case 10:
                txt1.setText("抽查");
                break;
            case 20:
                txt1.setText("自查");
                break;

        }
            txt2.setText(null==bean?"0":AbStrUtil.isEmpty(bean.getCnt()+"")?"0":bean.getCnt()+"");
            txt3.setText(null==bean?"0":bean.getAverage_score()+"");
            if(!AbStrUtil.isEmpty(bean.getAverage_score())&&bean.getAverage_score().equals("100")){
                txt3.setTextColor(context.getResources().getColor(R.color.black));
            }else{
                txt3.setTextColor(context.getResources().getColor(R.color.orange));
            }
            txt1.setTextColor(context.getResources().getColor(R.color.black));
            txt2.setTextColor(context.getResources().getColor(R.color.black));

            line.setVisibility(View.GONE);
        }else{
            line.setVisibility(View.GONE);
            txt2.setText("0");
            txt3.setText("0");
        }




    }

    @Override
    public int getLayoutId() {
        return R.layout.quality_report_table;
    }
}
