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
public class KnowledgeAdapter extends BaseAdapter {
    List<KnowledgeVo.ListBean.ItemsBean> list;
    Context context;
    LayoutInflater layoutInflater;
    boolean isMuti;

    public KnowledgeAdapter(Context context, List<KnowledgeVo.ListBean.ItemsBean> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    public void setChooseIndex(int index){
        if(isMuti){
            if(list.get(index).getResult().equals("")){
                list.get(index).setResult("1");
            }else{
                list.get(index).setResult("");
            }
        }else{
            for (int i=0;i<list.size();i++){
                 if(i==index){
                    list.get(i).setResult("1");
                }else{
                    list.get(i).setResult("");
                }
            }

        }
        notifyDataSetChanged();

    }

    public void setIsMuti(boolean isMuti){
        this.isMuti=isMuti;
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
            viewHolder.indextTv= (TextView) convertView.findViewById(R.id.indexTv);
            viewHolder.titleTv= (TextView) convertView.findViewById(R.id.titleTv);
            viewHolder.chooseImg= (ImageView) convertView.findViewById(R.id.chooseImg);
            viewHolder.chooseLay= (LinearLayout) convertView.findViewById(R.id.chooseLay);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.indextTv.setText(list.get(position).getTitle());
        viewHolder.titleTv.setText(list.get(position).getNote());
        setview(position, viewHolder);


        return convertView;
    }

    public void setview(int position, ViewHolder viewHolder) {
        if(list.get(position).getResult().equals("1")){
            viewHolder.chooseImg.setVisibility(View.VISIBLE);
            viewHolder.chooseImg.setImageResource(R.drawable.adopt_select);
            viewHolder.indextTv.setEnabled(true);
            viewHolder.indextTv.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            viewHolder.chooseImg.setVisibility(View.INVISIBLE);
            viewHolder.indextTv.setEnabled(false);
            viewHolder.indextTv.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    public class ViewHolder {
        public    TextView indextTv;
        public  TextView titleTv;
        public   ImageView chooseImg;
        public   LinearLayout chooseLay;

    }
}
