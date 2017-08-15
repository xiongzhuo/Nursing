package com.deya.hospital.form.xy;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.form.xy.XyPreviewItemAdapter.ItemInter;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.FormDetailListVo;

public class XyPreviewAdapter extends BaseAdapter {
	LayoutInflater inflate;
	List<FormDetailListVo> list;
	int type;
	Context context;
	ListView mListView;
	previewAdapterInter inter;
	boolean isUploded;
	public XyPreviewAdapter(Context context,boolean isUploded, List<FormDetailListVo> list,
			 previewAdapterInter inter) {
		inflate = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		this.type = type;
		this.inter = inter;
		this.isUploded=isUploded;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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

	public void setdata(List<FormDetailListVo> list) {
		this.list = list;
		notifyDataSetChanged();

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = inflate.inflate(R.layout.item_prviewlist, null);
			viewHolder.itemListView = (ListView) convertView
					.findViewById(R.id.itemList);
			viewHolder.titleTv = (TextView) convertView
					.findViewById(R.id.title);
			viewHolder.layout = (LinearLayout) convertView
					.findViewById(R.id.layout);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (position % 2 == 0) {
			viewHolder.layout.setBackgroundResource(R.color.white);
		} else {
			viewHolder.layout.setBackgroundResource(R.color.list_item_corlor);
		}
		FormDetailListVo fdv=list.get(position);
		String text = fdv.getName().trim();
		viewHolder.titleTv.setText(text);
		XyPreviewItemAdapter itemAdapter = new XyPreviewItemAdapter(context, isUploded,fdv.getSub_items(), type, position, new ItemInter() {

			@Override
			public void onRemark(int itemposition) {
				Log.i("xypriv", position+"==="+itemposition);
				inter.onRemark(position, itemposition);
			}

			@Override
			public void onputdata(int itemposition) {
				inter.onputData(position,itemposition);
				
			}

			@Override
			public void onrefresh() {
				notifyDataSetChanged();
				
			}
		});
		viewHolder.itemListView.setAdapter(itemAdapter);



		return convertView;
	}

	public class ViewHolder {
		ListView itemListView;
		TextView titleTv;
		LinearLayout layout;

	}

	public String getVerticalText(String str) {// 将文字竖向排列
		String title = "";
		for (int i = 0; i < str.length(); i++) {
			char item = str.charAt(i);
			if (48 <= item && item <= 57) {
				title += item;//将相连的数字组合到一起
			} else {
				if (!AbStrUtil.isEmpty(item + "")) {
					title += item + "\n";
				}
			}

		}
		return title.trim();
	}

	public interface previewAdapterInter {
		public void onputData(int parentposition,int position);// 去操作页面

		public void onRemark(int parentPosition, int position);// 备注
	}
//	public void updateView(int itemIndex) {
//		//得到第一个可显示控件的位置，
//		int visiblePosition = mListView.getFirstVisiblePosition();
//		//只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
//		if (itemIndex - visiblePosition >= 0) {
//			//得到要更新的item的view
//			View convertView = mListView.getChildAt(itemIndex - visiblePosition);
//			//从view中取得holder
//			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
//			viewHolder.itemListView = (ListView) convertView
//					.findViewById(R.id.itemList);
//			viewHolder.titleTv = (TextView) convertView
//					.findViewById(R.id.title);
//			viewHolder.layout = (LinearLayout) convertView
//					.findViewById(R.id.layout);
//			updateData(viewHolder, list.get(itemIndex),itemIndex);
//		}		
//	}


}
