package com.deya.hospital.workspace.multidrugresistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public abstract class BasePriviewAdapter<T> extends BaseAdapter {
    public Context context;
    public List<T> list;
    LayoutInflater inflater;

    public BasePriviewAdapter(Context context, List<T> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;

    }

    public int[] CheckImg = {R.drawable.check_box_right, R.drawable.check_box_wrong};

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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_resistanlist, null);
            viewHolder.groupTileImg=(ImageView) convertView.findViewById(R.id.groupTileImg);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.itemList = (GridView) convertView.findViewById(R.id.itemList);
            viewHolder.itemList2= (ListView) convertView.findViewById(R.id.itemList2);
            viewHolder.itemRulesLay = (LinearLayout) convertView.findViewById(R.id.itemRulesLay);
            viewHolder.itemRulesLayVertical= (LinearLayout) convertView.findViewById(R.id.itemRulesLayVertical);
            viewHolder.singleCheckLay = (LinearLayout) convertView.findViewById(R.id.singleCheckLay);
            viewHolder.scoreEdtLay = (LinearLayout) convertView.findViewById(R.id.scoreEdtLay);
            viewHolder.title_check = (ImageView) convertView.findViewById(R.id.title_check);
            viewHolder.numTv= (TextView) convertView.findViewById(R.id.numTv);
            viewHolder.subtractionBtn = (Button) convertView
                    .findViewById(R.id.btn_subtraction);
            viewHolder.plusBtn = (Button) convertView
                    .findViewById(R.id.btn_add);
            viewHolder.indexTv= (TextView) convertView.findViewById(R.id.indexTv);
            viewHolder.detailLay= (LinearLayout) convertView.findViewById(R.id.detailLay);
            viewHolder.scoreTv= (TextView) convertView.findViewById(R.id.scoreTv);
            viewHolder.nameTv= (TextView) convertView.findViewById(R.id.nameTv);
            viewHolder.timeTv= (TextView) convertView.findViewById(R.id.timeTv);
            viewHolder.titleLay= (LinearLayout) convertView.findViewById(R.id.titleLay);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setItem(position, viewHolder);
        return convertView;
    }

    public abstract void setItem(int position, ViewHolder viewHolder);

    public class ViewHolder {
        public  Button subtractionBtn, plusBtn;
        public GridView itemList;
        public TextView title,numTv;
        public LinearLayout itemRulesLay;//子项
        public ImageView title_check;
        public LinearLayout singleCheckLay;//标题判断题类型
        public LinearLayout scoreEdtLay;//积分操作类型布局
        public ImageView groupTileImg;
        public LinearLayout itemRulesLayVertical;
        public ListView itemList2;
        public TextView indexTv;
        public LinearLayout detailLay;
        public TextView scoreTv;
        public TextView nameTv;
        public TextView timeTv;
        public LinearLayout titleLay;
    }
}
