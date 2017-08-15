package com.deya.hospital.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.deya.hospital.vo.FormDetailVo;

import java.util.List;

public class FormJugeEditorAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<FormDetailVo> list;

	public FormJugeEditorAdapter(Context context, List<FormDetailVo> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
//		final ViewHolder viewHolder;
//		if (null == convertView) {
//			viewHolder = new ViewHolder();
//			convertView = inflater.inflate(R.layout.layout_form_juge_editor, null);
//			viewHolder.judeBtn = (Button) convertView
//					.findViewById(R.id.judeBtn);
//			viewHolder.title=(TextView) convertView.findViewById(R.id.title);
//			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
//		int result =list.get(position).getResult();
//		if(result==2){
//			viewHolder.judeBtn.setBackgroundResource(R.drawable.review_wrong_img);
//		}else{
//			viewHolder.judeBtn.setBackgroundResource(R.drawable.revirew_right_img);
//		}
//		viewHolder.title.setText(list.get(position).getDescribes());
//		viewHolder.judeBtn.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				int result =list.get(position).getResult();
//				if(result==1){
//					viewHolder.judeBtn.setBackgroundResource(R.drawable.review_wrong_img);
//					list.get(position).setResult(2);
//					list.get(position).setOpenState(1);
//				}else{
//					viewHolder.judeBtn.setBackgroundResource(R.drawable.revirew_right_img);
//					list.get(position).setResult(1);
//					list.get(position).setOpenState(2);
//				}
//
//				notifyDataSetChanged();
//			}
//		});

		return convertView;
	}

	public class ViewHolder {
		Button subtractionBtn, judeBtn;
		TextView numTv,title;

	}

	public void setStatus(int positon,int status){
		for (int i = 0; i < list.size(); i++) {
			if(positon==i){
				list.get(i).setOpenState(status);
			}
		}
	}
}
