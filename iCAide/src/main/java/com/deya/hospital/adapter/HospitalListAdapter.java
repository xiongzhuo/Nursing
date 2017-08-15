package com.deya.hospital.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.HospitalInfo;

import java.util.ArrayList;
import java.util.List;

public class HospitalListAdapter extends BaseAdapter implements Filterable {
	List<HospitalInfo> list = new ArrayList<HospitalInfo>();
	private LayoutInflater inflater;
	Context context;

	public HospitalListAdapter(Context context, List<HospitalInfo> list) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.list_item, null);
		TextView listtext = (TextView) convertView.findViewById(R.id.listtext);
		listtext.setText(list.get(position).getName());
		return convertView;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

}
