package com.deya.hospital.workspace.stastic;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.CommonUtils;
import com.deya.hospital.vo.StasticGroupVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/9/20
 */
public class StasticTypesAdapter extends BaseExpandableListAdapter {
    List<StasticGroupVo> list;
    LayoutInflater layoutInflater;
    onItemsClick onItemsClick;
    Context context;
    public StasticTypesAdapter(Context context, List<StasticGroupVo> list,onItemsClick onItemsClick) {
        this.context = context;
        this.list = list;
        layoutInflater = layoutInflater.from(context);
        this.onItemsClick=onItemsClick;

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return  1 ;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHoder viewHoder;
        if (null == convertView) {
            viewHoder = new ViewHoder();
            convertView = layoutInflater.inflate(R.layout.group_top, null);
            viewHoder.title = (TextView) convertView.findViewById(R.id.title);
            viewHoder.openStateImg = (ImageView) convertView.findViewById(R.id.openStateImg);
            viewHoder.img = (ImageView) convertView.findViewById(R.id.state_img);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();

        }
        viewHoder.img.setImageResource(list.get(groupPosition).getImgId());
        if (isExpanded) {
            viewHoder.openStateImg.setBackgroundResource(R.drawable.right_open_img);
        } else {
            viewHoder.openStateImg.setBackgroundResource(R.drawable.right_dowm_img);
        }
        viewHoder.title.setText(list.get(groupPosition).getTitle());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHoder childViewHoder;
        if (null == convertView) {
            childViewHoder = new ChildViewHoder();
            convertView = layoutInflater.inflate(R.layout.adapter_stastictype_child, null);
            childViewHoder.gridView = (GridView) convertView.findViewById(R.id.gv);
            childViewHoder.emperty_lay= (LinearLayout) convertView.findViewById(R.id.emperty_lay);
            convertView.setTag(childViewHoder);

        } else {
            childViewHoder = (ChildViewHoder) convertView.getTag();
        }
        if (list.get(groupPosition).getList().size() > 0) {
            childViewHoder.gridView.setVisibility(View.VISIBLE);
            childViewHoder.emperty_lay.setVisibility(View.GONE);
            StasticTypeChildAdapter stasticTypeChildAdapter = new StasticTypeChildAdapter(context, list.get(groupPosition).getList());
            childViewHoder.gridView.setAdapter(stasticTypeChildAdapter);
        }else{
            childViewHoder.gridView.setVisibility(View.GONE);
            childViewHoder.emperty_lay.setVisibility(View.VISIBLE);
        }
        childViewHoder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemsClick.onItemsClick(groupPosition,position);
            }
        });
        childViewHoder.emperty_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.callServiceTell(context);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    private class ChildViewHoder {
        GridView gridView;
        LinearLayout emperty_lay;

    }

    private class ViewHoder {
        TextView title;
        ImageView img, openStateImg;



    }
    interface onItemsClick{
     public void    onItemsClick(int groupPos,int childPos);

    }
}
