package com.deya.hospital.workspace.multidrugresistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.RisistantVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class RisitanCheckAdapter extends BaseAdapter {
    boolean isMutiChoose;
    List<RisistantVo.TempListBean.ItemListBean.ChildrenBean> list;
    LayoutInflater inflater;
    boolean isSingleCheck=false;

    public RisitanCheckAdapter(Context context, List<RisistantVo.TempListBean.ItemListBean.ChildrenBean> list, boolean isSingleCheck) {
        this.list = list;
        this.isSingleCheck=isSingleCheck;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_check_layout, null);
            viewHolder.item_check = (ImageView) convertView.findViewById(R.id.item_check);
            viewHolder.itemText = (TextView) convertView.findViewById(R.id.itemText);
            viewHolder.singleCheckLay = (LinearLayout) convertView.findViewById(R.id.singleCheckLay);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final RisistantVo.TempListBean.ItemListBean.ChildrenBean rItem = list.get(position);
        viewHolder.itemText.setText(rItem.getTitle());
        int isyes = 0;
        if (!AbStrUtil.isEmpty(rItem.getResult())) {
            isyes = Integer.parseInt(rItem.getResult());

        }
        viewHolder.item_check.setVisibility(View.GONE);
        switch (isyes) {
            case 0:
                viewHolder.item_check.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.item_check.setVisibility(View.VISIBLE);
                viewHolder.item_check.setImageResource(R.drawable.check_box_right);
                break;
            default:
                break;

        }


        viewHolder.singleCheckLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isyes = 0;
                if (!AbStrUtil.isEmpty(rItem.getResult())) {
                    isyes = Integer.parseInt(rItem.getResult());

                }
                switch (isyes) {
                    case 0:
                        viewHolder.item_check.setVisibility(View.VISIBLE);
                        viewHolder.item_check.setImageResource(R.drawable.check_box_right);
                        rItem.setResult("1");
                        if(isSingleCheck){
                            setSingleCheck(position);
                        }
                        break;
                    case 1:
                        viewHolder.item_check.setVisibility(View.GONE);
                        rItem.setResult("0");

                        break;
                    default:
                        break;


                }

            }
        });


        return convertView;
    }

    public void setSingleCheck(int pos){
        for (int i=0;i<list.size();i++){

            if(i==pos){
                list.get(pos).setResult("1");
            }else{
                list.get(i).setResult("0");
            }
        }
        notifyDataSetChanged();

    }
    private class ViewHolder {
        ImageView item_check;
        TextView itemText;
        LinearLayout singleCheckLay;
    }
}
