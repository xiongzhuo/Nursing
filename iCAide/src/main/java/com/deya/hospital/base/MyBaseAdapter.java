package com.deya.hospital.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	List<T> list = new ArrayList<T>();
	DataInter inter;

	public MyBaseAdapter(Context mContext, DataInter inter) {
		this.inter = inter;
	}

	@Override
	public int getCount() {
		return null == list ? 0 : list.size();
	}

	@Override
	public T getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder;
		if(null==convertView){
			viewholder=new ViewHolder();
		}
		//DepartmentVo item=(DepartmentVo) getItem(position);
		//String text = inter.getItem(position);
		return convertView;
	}

	public interface DataInter {
		public String getItem(int position);
	}
	
	public  class ViewHolder{
		
	}
}
