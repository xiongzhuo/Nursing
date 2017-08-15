package com.deya.hospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deya.acaide.R;

public class ScorGvAdapter extends BaseAdapter{
	LayoutInflater inflater;
	Context mcontext;
	int choosePosition;
	boolean isVip=false;
	public ScorGvAdapter(Context context,int choosePosition,boolean isVip) {
		mcontext=context;
		this.isVip=isVip;
		inflater=LayoutInflater.from(context);
		this.choosePosition=choosePosition;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setVipState(boolean isVip){
		this.isVip=isVip;
		notifyDataSetChanged();

	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setNum(int num){
		this.choosePosition=num;
		notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView=inflater.inflate(R.layout.score_gv_buttons, null);
		TextView tv=(TextView) convertView.findViewById(R.id.text);
//		if(isVip){
		tv.setText(10*(position+1)+"");
//		}else{
//		tv.setText(5*(position+1)+"");
//		}
		if(choosePosition==position&&choosePosition<=7){
			tv.setBackgroundResource(R.drawable.circle_choose);
			tv.setTextColor(mcontext.getResources().getColor(R.color.top_color));
		}else if(choosePosition>=7){
			if(position==7){
				tv.setBackgroundResource(R.drawable.circle_choose);
				tv.setTextColor(mcontext.getResources().getColor(R.color.top_color));
			}

		}
		return convertView;
	}

}
