package com.deya.hospital.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.RulesVo;

import java.util.ArrayList;
import java.util.List;

public class MutichooseListAdapter extends BaseAdapter  {
	List<RulesVo> list = new ArrayList<RulesVo>();
	private LayoutInflater inflater;
	Context context;

	public MutichooseListAdapter(Context context, List<RulesVo> list) {
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
		if(list.get(position).isChoosed()){
			convertView.setBackgroundResource(R.color.light_yellow);
			listtext.setTextColor(context.getResources().getColor(R.color.white));
		}else{
			convertView.setBackgroundResource(R.color.white);
			listtext.setTextColor(context.getResources().getColor(R.color.list_title));
		}
		return convertView;
	}


}
