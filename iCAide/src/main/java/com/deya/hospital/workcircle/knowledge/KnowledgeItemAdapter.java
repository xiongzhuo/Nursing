package com.deya.hospital.workcircle.knowledge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.KnowledgeVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/9/27
 */
public class KnowledgeItemAdapter extends BaseAdapter {
    List<KnowledgeVo.ListBean.ItemsBean> list;
    Context context;
    LayoutInflater layoutInflater;
    boolean isMuti;

    public KnowledgeItemAdapter(Context context, List<KnowledgeVo.ListBean.ItemsBean> list) {
        this.context = context;
        this.list = list;
        this.isMuti = isMuti;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void setChooseIndex(int index) {
        if (isMuti) {
            if (list.get(index).getResult().equals("")) {
                list.get(index).setResult("1");
            } else {
                list.get(index).setResult("0");
            }
        } else {
            list.get(index).setResult("1");
        }
        notifyDataSetChanged();

    }

    public void setIsMuti(boolean isMuti) {
        this.isMuti = isMuti;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.adapter_knowledge_privew, null);
            viewHolder.indextTv = (TextView) convertView.findViewById(R.id.indexTv);
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
            viewHolder.chooseImg = (ImageView) convertView.findViewById(R.id.chooseImg);
            viewHolder.chooseLay = (LinearLayout) convertView.findViewById(R.id.chooseLay);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        KnowledgeVo.ListBean.ItemsBean itemsBean = list.get(position);
        viewHolder.indextTv.setText(list.get(position).getTitle());
        viewHolder.titleTv.setText(list.get(position).getNote());
        if (!list.get(position).getResult().equals("")) {
            if (itemsBean.getIs_yes().equals(itemsBean.getResult())) {
                if (itemsBean.getIs_yes().equals("1")) {
                    viewHolder.chooseImg.setVisibility(View.VISIBLE);
                    viewHolder.chooseImg.setImageResource(R.drawable.adopt_select);
                } else {
                    viewHolder.chooseImg.setVisibility(View.GONE);
                }
            } else {
                viewHolder.chooseImg.setVisibility(View.VISIBLE);
                viewHolder.chooseImg.setImageResource(R.drawable.img_no_p);
            }
            viewHolder.indextTv.setEnabled(true);
            viewHolder.indextTv.setTextColor(context.getResources().getColor(R.color.white));

        } else {
            if (!itemsBean.getIs_yes().equals("1")) {
                viewHolder.chooseImg.setVisibility(View.INVISIBLE);
                viewHolder.indextTv.setEnabled(false);
                viewHolder.indextTv.setTextColor(context.getResources().getColor(R.color.black));
            }else{
                viewHolder.chooseImg.setVisibility(View.VISIBLE);
                viewHolder.chooseImg.setImageResource(R.drawable.adopt_select);
                viewHolder.indextTv.setEnabled(false);
                viewHolder.indextTv.setTextColor(context.getResources().getColor(R.color.black));
            }
        }


        return convertView;
    }

    private class ViewHolder {
        TextView indextTv;
        TextView titleTv;
        ImageView chooseImg;
        LinearLayout chooseLay;

    }
}
