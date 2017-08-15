package com.deya.hospital.form.xy;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.Subrules;

public class SubRulesAdapter extends BaseAdapter{
	List<Subrules> list;
	LayoutInflater inflate;
	RulesAdapterInter inter;
	public SubRulesAdapter(Context context,List<Subrules> list,RulesAdapterInter inter) {
		inflate=LayoutInflater.from(context);
		this.list=list;
		this.inter=inter;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
			convertView=inflate.inflate(R.layout.list_xiangya_form_item, null);
			viewHolder.itemTitleTv=(TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				inter.onPutdata();
				
			}
		});
		viewHolder.itemTitleTv.setText(list.get(position).getName());
		return convertView;
	}
private class ViewHolder{
	TextView itemTitleTv;
	
}
interface RulesAdapterInter{
	public  void  onPutdata();
}	
}
