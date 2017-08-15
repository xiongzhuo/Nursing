package com.deya.hospital.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.ChildsVo;

import java.util.ArrayList;
import java.util.List;

public class ChildDepartListAdapter extends BaseAdapter {
	List<ChildsVo> list = new ArrayList<ChildsVo>();
	private LayoutInflater inflater;
	Context context;
	int type;

	public ChildDepartListAdapter(Context context, List<ChildsVo> list) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
	}
	public ChildDepartListAdapter(Context context, List<ChildsVo> list, int type) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
		this.type=type;
	}
	@Override
	public int getCount() {
		Log.i("11111", list.size() + "");
		return type==1?list.size():list.size();
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

	}

	public void setData(List<ChildsVo> list) {
		this.list = list;
		notifyDataSetInvalidated();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.fivelist_item, null);
			viewHolder.listtext = (TextView) convertView
					.findViewById(R.id.listtext);
			viewHolder.chekImg = (ImageView) convertView
					.findViewById(R.id.check_img);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.img.setImageResource(R.drawable.depart);

		if (position <= list.size() - 1) {
			viewHolder.listtext.setTextColor(context.getResources().getColor(
					R.color.black));
			if (list.get(position).isChoosed()) {
				viewHolder.chekImg.setVisibility(View.VISIBLE);
			} else {
				viewHolder.chekImg.setVisibility(View.GONE);
			}
			viewHolder.listtext.setText(list.get(position).getName());
			viewHolder.listtext.setVisibility(View.VISIBLE);;
		} else {
				viewHolder.chekImg.setVisibility(View.GONE);
				viewHolder.listtext.setVisibility(View.VISIBLE);;
				viewHolder.listtext.setText("添加科室");
				viewHolder.listtext.setTextColor(context.getResources().getColor(
						R.color.top_color));
			
		}
		return convertView;
	}

	public class ViewHolder {
		TextView listtext;
		ImageView chekImg;
		ImageView img;

	}
}
