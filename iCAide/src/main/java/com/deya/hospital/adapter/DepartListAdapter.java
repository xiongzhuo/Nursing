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
import com.deya.hospital.vo.DepartLevelsVo;

import java.util.ArrayList;
import java.util.List;

public class DepartListAdapter extends BaseAdapter {
	List<DepartLevelsVo> list = new ArrayList<DepartLevelsVo>();
	private LayoutInflater inflater;
	Context context;
	int num=0;
	boolean showHistory=true;
	boolean showAll=false;
	public DepartListAdapter(Context context, List<DepartLevelsVo> list) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
	}
	public DepartListAdapter(Context context, List<DepartLevelsVo> list, boolean showHistory) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
		this.showHistory=showHistory;
	}
	@Override
	public int getCount() {
		Log.i("11111", list.size() + "");
		return showHistory?list.size()+1:list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private int checkPosition = 1;

	public void setIsCheck(int position) {
		checkPosition = position;
		notifyDataSetChanged();

	}
	public void showAll(boolean showAll){
		this.showAll=showAll;

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
			convertView = inflater.inflate(R.layout.fivelist_item, null);
			viewHolder.listtext = (TextView) convertView
					.findViewById(R.id.listtext);
			viewHolder.chekImg = (ImageView) convertView
					.findViewById(R.id.check_img);
			viewHolder.img=(ImageView) convertView.findViewById(R.id.img);
			viewHolder.numsTv=(TextView) convertView.findViewById(R.id.numsTv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (position==checkPosition) {
			viewHolder.img.setVisibility(View.VISIBLE);
			convertView.setBackgroundResource(R.color.white);
		}else{
			convertView.setBackgroundResource(R.color.main_bg);
			viewHolder.img.setVisibility(View.GONE);
		}
		if(showHistory){
			if(position==0){
			viewHolder.listtext.setText(showAll?"全部":"历史记录");
			}else{
			if (list.get(position-1).getChooseNum() > 0) {
				viewHolder.numsTv.setVisibility(View.VISIBLE);
			} else {
				viewHolder.numsTv.setVisibility(View.GONE);
			}
			viewHolder.numsTv.setText(list.get(position-1).getChooseNum() + "");
			Log.i("111111111111", list.get(position-1).getChooseNum() + "");
			viewHolder.listtext.setText(list.get(position-1).getRoot().getName());
			}
		}else {
			if (list.get(position).getChooseNum() > 0) {
				viewHolder.numsTv.setVisibility(View.VISIBLE);
			} else {
				viewHolder.numsTv.setVisibility(View.GONE);
			}
			viewHolder.numsTv.setText(list.get(position).getChooseNum() + "");
			Log.i("111111111111", list.get(position).getChooseNum() + "");
			viewHolder.listtext.setText(list.get(position).getRoot().getName());

		}
		return convertView;
	}

	public class ViewHolder {
		TextView listtext,numsTv;
		ImageView chekImg;
		ImageView img;

	}
}
