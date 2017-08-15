package com.deya.hospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.DepartLevelsVo;

import java.util.ArrayList;
import java.util.List;

public class DepartDialogListAdapter extends BaseAdapter {
	List<DepartLevelsVo> list = new ArrayList<DepartLevelsVo>();
	private LayoutInflater inflater;
	Context context;

	public DepartDialogListAdapter(Context context, List<DepartLevelsVo> list) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
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

	private int checkPosition = -1;

	public void setIsCheck(int position) {
		checkPosition = position;
		notifyDataSetChanged();

	}
	public void setData(List<DepartLevelsVo> list){
		this.list=list;
		notifyDataSetInvalidated();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder=new ViewHolder();
			convertView = inflater.inflate(R.layout.list_item, null);
			viewHolder.listtext = (TextView) convertView
					.findViewById(R.id.listtext);
			viewHolder.bottm_view=convertView.findViewById(R.id.bottm_view);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		

		viewHolder.listtext.setText(list.get(position).getRoot().getName());
		if (position==list.size()-1) {
			viewHolder.bottm_view.setVisibility(View.GONE);
		}else {
			viewHolder.bottm_view.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	public class ViewHolder {
		TextView listtext,numsTv;
		View bottm_view;
	}
}
