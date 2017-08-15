package com.deya.hospital.workspace.workspacemain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;

public class WorkClassifyAdapter extends BaseAdapter{
	Context context;
	String[] urls;
	String titles[];
	int [] imgs;
	LayoutInflater inflater;
	public WorkClassifyAdapter(Context context, String [] titles, int [] imgs ) {
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
			viewHolder.nameTv=(TextView) convertView.findViewById(R.id.itemTxHorital);
			viewHolder.img=(ImageView) convertView.findViewById(R.id.itemIcon);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.img.setImageResource(imgs[position]);
		viewHolder.nameTv.setVisibility(View.VISIBLE);
		viewHolder.nameTv.setText(titles[position]);
		viewHolder.titleTv.setVisibility(View.GONE);
		return convertView;
	}

	class ViewHolder{
		TextView titleTv,nameTv;
		ImageView img;
		
	}
}
