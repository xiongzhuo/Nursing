package com.deya.hospital.workspace.stastic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.HotVo;

import java.util.List;

public class StasticTypeChildAdapter extends BaseAdapter{
	Context context;
	String[] urls;
	LayoutInflater inflater;
	List<HotVo> list;
	public StasticTypeChildAdapter(Context context,List<HotVo> list) {
		this.context=context;
		inflater=LayoutInflater.from(context);
		this.list=list;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(null==convertView){
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_stastic_adapter, null);
			viewHolder.titleTv=(TextView) convertView.findViewById(R.id.itemTxt);
			viewHolder.img=(ImageView) convertView.findViewById(R.id.itemIcon);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		HotVo hotVo=list.get(position);
		viewHolder.img.setImageResource(hotVo.getImgid());
		viewHolder.titleTv.setText(hotVo.getName());
		return convertView;
	}

	class ViewHolder{
		TextView titleTv;
		ImageView img;
		
	}
}
