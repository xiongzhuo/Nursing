package com.deya.hospital.workspace.threepips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.vo.RisistantVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class TabPagerAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    List<RisistantVo.TaskInfoBean.TempListBean> list;
    int checkPOs;
    onCheckInter inter;
    String text = "临床反馈";
    Context context;
    LinearLayout.LayoutParams para;

    public TabPagerAdapter(Context context, List<RisistantVo.TaskInfoBean.TempListBean> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setPagerFragmentTex(String text) {
        this.text = text;
        notifyDataSetChanged();
    }

    public void setCheckInter(onCheckInter inter) {
        this.inter = inter;
    }

    @Override
    public int getCount() {
        int size = list.size() + 1;
        int[] wh = AbViewUtil.getDeviceWH(context);
        if (size < 4) {
            para = new LinearLayout.LayoutParams(wh[0] / size, AbViewUtil.dip2px(context, 50));
        } else {
            para = new LinearLayout.LayoutParams(wh[0] / 4, AbViewUtil.dip2px(context, 50));
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setCheckPOs(int pos) {
        this.checkPOs = pos;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.tabpager_item_lay, null);
            viewHolder.radioButton = (RadioButton) convertView.findViewById(R.id.rb);
            viewHolder.numsTv= (TextView) convertView.findViewById(R.id.numsTv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.radioButton.setLayoutParams(para);
        if (position < list.size()) {
            RisistantVo.TaskInfoBean.TempListBean bean = list.get(position);
            switch (bean.getTemp_type_id()) {
                case 0:
                    viewHolder.radioButton.setText("基本信息");
                    break;
                case 3:
                    viewHolder.radioButton.setText("呼吸机");
                    break;
                case 4:
                    viewHolder.radioButton.setText("血管导管");
                    break;
                case 5:
                    viewHolder.radioButton.setText("导尿管");
                    break;
                default:
                    viewHolder.radioButton.setText("现场督导");
                    break;
            }
            viewHolder.numsTv.setVisibility(View.GONE);
            //viewHolder.radioButton.setText(list.get(position).getTitle());
        } else {
            viewHolder.radioButton.setText(text);
            if(AbStrUtil.isEmpty(text)){
                viewHolder.numsTv.setVisibility(View.GONE);
            }else{
                viewHolder.numsTv.setVisibility(View.VISIBLE);
                viewHolder.numsTv.setText(text);
            }
            viewHolder.radioButton.setText("存在问题");
        }
        if (checkPOs == position) {
            viewHolder.radioButton.setChecked(true);
        } else {
            viewHolder.radioButton.setChecked(false);
        }

        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inter.onCheck(position);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        RadioButton radioButton;
        TextView numsTv;
    }

    public interface onCheckInter {
        public void onCheck(int position);
    }
}
