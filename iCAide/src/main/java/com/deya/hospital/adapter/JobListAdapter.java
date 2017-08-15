package com.deya.hospital.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.JobListVo;

import java.util.ArrayList;
import java.util.List;

public class JobListAdapter extends BaseAdapter implements Filterable {
	List<JobListVo> list = new ArrayList<JobListVo>();
	private LayoutInflater inflater;
	Context context;
	private LayoutParams para2;
	int index=-1;
	

	public JobListAdapter(Context context, List<JobListVo> list, LayoutParams para2) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list=list;
		this.para2=para2;
	}
	public JobListAdapter(Context context, List<JobListVo> list) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list=list;
	}
	@Override
	public int getCount() {
		Log.i("11111", list.size()+"");
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

	public void chooseItem(int indext){
		if(this.index==indext){
			this.index=-1;
		}else{
		this.index=indext;
		}
		notifyDataSetChanged();
	}
	public int getChooseIndex(){
		return index;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_job_list, null);
		TextView listtext = (TextView) convertView.findViewById(R.id.textView);
		listtext.setText(list.get(position).getName());
		if(index!=position){
			listtext.setBackgroundResource(R.drawable.big_round_blue_type_style);
			listtext.setTextColor(context.getResources().getColor(R.color.top_color));
			}else{
				listtext.setBackgroundResource(R.drawable.addperson_btn);
				listtext.setTextColor(context.getResources().getColor(R.color.white));
			}
		if(null!=para2){
			//listtext.setLayoutParams(para2);
		}
		return convertView;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

}
