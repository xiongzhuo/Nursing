package com.deya.hospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deya.acaide.R;

import java.util.List;
import java.util.Map;

public class SortByMineAdapter extends BaseAdapter {
	private List<Map<String, Object>> data;
	private Context context;
	

	public SortByMineAdapter(List<Map<String, Object>> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
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

	class ViewHolder{
		private TextView question_title,answer_number,question_type,question_ask_or_answer;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if (convertView==null) {
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_sort_by_mine, null);
			holder.question_title=(TextView) convertView.findViewById(R.id.question_title);
			holder.answer_number=(TextView) convertView.findViewById(R.id.answer_number);
			holder.question_type=(TextView) convertView.findViewById(R.id.question_type);
			holder.question_ask_or_answer=(TextView) convertView.findViewById(R.id.question_ask_or_answer);
			convertView.setTag(holder);
		}
		holder=(ViewHolder) convertView.getTag();
		holder.question_title.setText(data.get(position).get("title")+"");
		holder.answer_number.setText("("+data.get(position).get("answer_count")+"个回答)");
		holder.question_type.setText(data.get(position).get("type_name")+"");
		holder.question_ask_or_answer.setText(data.get(position).get("my_type")+"");
		
		if ("1".equals(data.get(position).get("q_type")+"")) {
			holder.question_type.setBackgroundResource(R.drawable.big_yellow_bg);
		}else if ("2".equals(data.get(position).get("q_type")+"")) {
			holder.question_type.setBackgroundResource(R.drawable.big_yellow_bg);
		}else if ("3".equals(data.get(position).get("q_type")+"")) {
			holder.question_type.setBackgroundResource(R.drawable.big_yellow_bg);
		}else if ("4".equals(data.get(position).get("q_type")+"")) {
			holder.question_type.setBackgroundResource(R.drawable.big_yellow_bg);
		}else if ("5".equals(data.get(position).get("q_type")+"")) {
			holder.question_type.setBackgroundResource(R.drawable.big_yellow_bg);
		}
		
		return convertView;
	}

}
