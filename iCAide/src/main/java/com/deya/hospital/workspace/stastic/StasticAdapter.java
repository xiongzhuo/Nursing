package com.deya.hospital.workspace.stastic;

import com.deya.acaide.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StasticAdapter extends BaseAdapter{
	Context context;
	String[] urls;
	String titles[];
	int [] imgs;
	LayoutInflater inflater;
	public StasticAdapter(Context context,String [] titles,int [] imgs ) {
		inflater=LayoutInflater.from(context);
		this.titles=titles;
		this.imgs=imgs;
	}


	@Override
	public int getCount() {
		return titles.length;
	}

	public void setTitles(String titles[],int [] imgs ){
		this.titles=titles;
		notifyDataSetChanged();
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
		viewHolder.img.setImageResource(imgs[position]);
		viewHolder.titleTv.setText(titles[position]);
		return convertView;
	}

	class ViewHolder{
		TextView titleTv;
		ImageView img;
		
	}
}
