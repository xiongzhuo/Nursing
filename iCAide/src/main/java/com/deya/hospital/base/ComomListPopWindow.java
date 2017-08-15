package com.deya.hospital.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.deya.acaide.R;

import java.util.List;

public class ComomListPopWindow extends PopupWindow {

		private View mMenuView;
		private ListView popLv;
		LayoutInflater inflater;
		private TextView yesTv, cancelTv,titleTv;
		Context mcontext;
		LinearLayout frambg;
		

		public ComomListPopWindow(OnItemClickListener listener, List<String> list,String title,Context contex,final LinearLayout frambg) {
			super(contex);
			mcontext=contex;
			inflater =LayoutInflater.from(mcontext);
			this.frambg=frambg;
			mMenuView = inflater.inflate(R.layout.selector_popwindow, null);
			popLv = (ListView) mMenuView.findViewById(R.id.poplist);
			popLv.setAdapter(new popAdapter(list));
			yesTv = (TextView) mMenuView.findViewById(R.id.yes);
			titleTv=(TextView) mMenuView.findViewById(R.id.title);
			titleTv.setText(title);
			popLv.setOnItemClickListener(listener);
			cancelTv = (TextView) mMenuView.findViewById(R.id.cancle);
			cancelTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});

			// 设置按钮监听
			// 设置SelectPicPopupWindow的View
			this.setContentView(mMenuView);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(LayoutParams.FILL_PARENT);
			// 设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.WRAP_CONTENT);
			// 设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			// 设置SelectPicPopupWindow弹出窗体动画效果
			frambg.setVisibility(View.VISIBLE);
			this.setAnimationStyle(R.style.popupAnimation);
			// 实例化一个ColorDrawable颜色为半透明
			// 设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(mcontext.getResources().getDrawable(
					android.R.color.transparent));
			setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					frambg.setVisibility(View.GONE);

				}
			});

		}


	public class popAdapter extends BaseAdapter {
		LayoutInflater inflater;
		List<String> list;

		public popAdapter(List<String> list) {
			inflater = LayoutInflater.from(mcontext);
			this.list = list;
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
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.poplist_item, null);
			TextView listtext = (TextView) convertView
					.findViewById(R.id.listtext);
			listtext.setText(list.get(position));
			return convertView;
		}

	}
	}