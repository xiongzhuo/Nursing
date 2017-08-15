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

public class JobTimesListAdapter extends BaseAdapter implements Filterable {
	List<JobListVo> list = new ArrayList<JobListVo>();
	private LayoutInflater inflater;
	Context context;
	private LayoutParams para2;
	int index = -1;

	public JobTimesListAdapter(Context context, List<JobListVo> list,
			LayoutParams para2) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
		this.para2 = para2;
	}

	public JobTimesListAdapter(Context context, List<JobListVo> list) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		Log.i("11111", list.size() + "");
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

	public void setdata(List<JobListVo> list) {
		this.list = list;
		notifyDataSetChanged();

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(null==convertView){
			viewHolder=new ViewHolder();
		convertView = inflater.inflate(R.layout.list_item, null);
		viewHolder.listtext = (TextView) convertView.findViewById(R.id.listtext);
		convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		
		if(list.get(position).getTaskNum()>0){
			viewHolder.listtext.setText(list.get(position).getName()+":"+list.get(position).getTaskNum());
		}else{
			viewHolder.listtext.setText("");	
		}
		return convertView;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	public class ViewHolder{
		TextView listtext;
	} 
}
