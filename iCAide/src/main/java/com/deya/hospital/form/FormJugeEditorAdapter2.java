package com.deya.hospital.form;

import java.util.List;

import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.deya.acaide.R;
import com.deya.hospital.vo.FormDetailVo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class FormJugeEditorAdapter2 extends BaseAdapter {
	LayoutInflater inflater;
	List<FormDetailVo> list;
	boolean isUpdate = false;
	SwipeMenuListView listview;
	boolean editorbal;

	public FormJugeEditorAdapter2(Context context, boolean isUpdate,boolean editorbal,
			List<FormDetailVo> list, SwipeMenuListView listview) {
		inflater = LayoutInflater.from(context);
		this.isUpdate = isUpdate;
		this.list = list;
		this.editorbal=editorbal;
		this.listview = listview;
	}

	@Override
	public int getCount() {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
//		final ViewHolder viewHolder;
//		if (null == convertView) {
//			viewHolder = new ViewHolder();
//			convertView = inflater.inflate(R.layout.layout_form_juge_editor,
//					null);
//			viewHolder.judeBtn = (Button) convertView
//					.findViewById(R.id.judeBtn);
//			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
//			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
//		int result = list.get(position).getResult();
//
//		if (result == 2) {
//			viewHolder.judeBtn
//					.setBackgroundResource(R.drawable.review_wrong_img);
//			// list.get(position).setCanOpen(true);
//		} else {
//			viewHolder.judeBtn
//					.setBackgroundResource(R.drawable.revirew_right_img);
//			// list.get(position).setCanOpen(false);
//
//		}
//		if (isUpdate&&!editorbal) {
//			viewHolder.judeBtn.setEnabled(false);
//		}else{
//			viewHolder.judeBtn.setEnabled(true);
//		}
//		viewHolder.title.setText(list.get(position).getDescribes());
//		viewHolder.judeBtn.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				int result = list.get(position).getResult();
//				if (result == 1) {
//					viewHolder.judeBtn
//							.setBackgroundResource(R.drawable.review_wrong_img);
//					list.get(position).setResult(2);
//					list.get(position).setOpenState(1);
//				} else if (result == 2) {
//					viewHolder.judeBtn
//							.setBackgroundResource(R.drawable.revirew_right_img);
//					list.get(position).setResult(1);
//					list.get(position).setOpenState(2);
//				}
//
//				notifyDataSetChanged();
//			}
//		});
//		if (list.get(position).getOpenState() == 1) {
//
//			listview.smoothOpenMenu(position);
//		} else {
//			View view = parent.getChildAt(position);
//			if (view instanceof SwipeMenuLayout) {
//				SwipeMenuLayout mTouchView = (SwipeMenuLayout) view;
//				mTouchView.smoothCloseMenu();
//			}
//		}
		return convertView;
	}

	public class ViewHolder {
		Button subtractionBtn, judeBtn;
		TextView numTv, title;

	}

	public void setStatus(int positon, int status) {
		for (int i = 0; i < list.size(); i++) {
			if (positon == i) {
				list.get(i).setOpenState(status);
			}
		}
	}
}
