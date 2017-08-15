package com.deya.hospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.HotVo;

import java.util.List;

public class TypeAdapter extends BaseAdapter{
	List<HotVo> list;
	private LayoutInflater layoutInflate;
	public TypeAdapter(Context context,List<HotVo> list){
		this.list=list;
		layoutInflate=LayoutInflater.from(context);
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(null==convertView){
			viewHolder=new ViewHolder();
			convertView=layoutInflate.inflate(R.layout.item_question_group, null);
			viewHolder.name=(TextView) convertView.findViewById(R.id.nameTv);
			viewHolder.numTv=(TextView) convertView.findViewById(R.id.numTv);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		HotVo hv=list.get(position);
		viewHolder.name.setText(hv.getName());
		return convertView;
	}
	public class ViewHolder {
		ImageView headImg,line;
		TextView name;
		TextView numTv,hospitalTv,moreTv;
		RelativeLayout hospitalLay;
		LinearLayout moreLay;

	}
}
