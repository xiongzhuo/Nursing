package com.deya.hospital.workcircle.knowledge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.KnowledgeVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/9/28
 */
public class KnowLedgeDetailAdapter extends BaseAdapter {
    Context context;
    List<KnowledgeVo.ListBean> list;
    LayoutInflater layoutInflater;
   public  KnowLedgeDetailAdapter(Context context, List<KnowledgeVo.ListBean> list){
       this.context=context;
       this.list=list;
       layoutInflater= LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder viewHolder;
        if(null==convertView){
            convertView=layoutInflater.inflate(R.layout.adapter_knowledge_detail,null);
            viewHolder=new ViewHolder();
            viewHolder.itemlist= (ListView) convertView.findViewById(R.id.itemlist);
            viewHolder.titleTv= (TextView) convertView.findViewById(R.id.titleTv);
            viewHolder.analysisTv= (TextView) convertView.findViewById(R.id.analysisTv);
            viewHolder.typeTv= (TextView) convertView.findViewById(R.id.typeTv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        KnowledgeVo.ListBean bean=list.get(position);
        KnowledgeItemAdapter adapter=new KnowledgeItemAdapter(context,bean.getItems());
        viewHolder.itemlist.setAdapter(adapter);
        viewHolder.titleTv.setText("\u3000\u3000\u3000\u3000"+bean.getTitle());
        String note= AbStrUtil.isEmpty(bean.getNote())?"":bean.getNote();
        viewHolder.analysisTv.setText("正确答案:"+bean.getRightAswer() +"  "+note);
        viewHolder.typeTv.setText(bean.getSub_type()==2?"多选题":"单选题");
        return convertView;
    }

    public class ViewHolder{
        ListView itemlist;
        TextView titleTv,analysisTv,typeTv;

    }
}
