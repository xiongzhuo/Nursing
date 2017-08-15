package com.deya.hospital.descover;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.vo.HotVo;

public class HotSearchAdapter extends BaseAdapter {
	int chooseIndex = -1;
	Context context;
	List<HotVo> list;
	private int[] wh;
	private LayoutParams para2;

	public HotSearchAdapter(Context context, List<HotVo> list) {
		this.context = context;
		this.list = list;
		wh = AbViewUtil.getDeviceWH(context);
		para2 = new LayoutParams((wh[0] - dp2Px(70, context)) / 5,
				(wh[0] - dp2Px(70, context)) / 5);
	}

	public int dp2Px(int dp, Context context) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	public void setData(List<HotVo> list){
		this.list=list;
		notifyDataSetChanged();
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

	public void setChooseItem(int position) {
		chooseIndex = position;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(
				R.layout.item_job_type, null);
		TextView tx = (TextView) convertView.findViewById(R.id.textView);
		if (chooseIndex != position) {
			tx.setBackgroundResource(R.drawable.circle_sharp_blue);
			tx.setTextColor(context.getResources().getColor(R.color.top_color));
		} else {
			tx.setBackgroundResource(R.drawable.round_orange);
			tx.setTextColor(context.getResources().getColor(R.color.white));
		}
		tx.setText(list.get(position).getName());
		return convertView;
	}

}