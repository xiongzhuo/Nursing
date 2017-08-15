package com.deya.hospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.vo.dbdata.planListDb;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

public class PersonAndJobAdapter extends BaseAdapter {
	// List<PicListVo> list;
	Context mcontext;
	private LayoutInflater inflater;
	LayoutParams para;
	List<planListDb> list;
	DisplayImageOptions optionsSquare;

	/**
	 * Creates a new instance of MyImageListAdapter.
	 */
	public PersonAndJobAdapter(Context context, List<planListDb> list) {
		inflater = LayoutInflater.from(context);
		mcontext = context;
		this.list = list;

	}
	public PersonAndJobAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mcontext = context;

	    int[] wh = AbViewUtil.getDeviceWH(mcontext);
	    para = new LayoutParams( (wh[0] - dp2Px(mcontext, 100)) /3,dp2Px(context, 35));
	}


	@Override
	public int getCount() {
	//	return list == null ? 0 : list.size();
         return 10;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder2 mviewHolder = null;
		if (convertView == null) {
			mviewHolder = new ViewHolder2();
			convertView = inflater.inflate(R.layout.adapter_item_button, null);
			mviewHolder.button = (TextView) convertView
					.findViewById(R.id.button);
			mviewHolder.num = (TextView) convertView.findViewById(R.id.num);
			mviewHolder.layout = (LinearLayout) convertView
					.findViewById(R.id.layout);
			mviewHolder.layout.setLayoutParams(para);
			convertView.setTag(mviewHolder);
		} else {
			mviewHolder = (ViewHolder2) convertView.getTag();
		}
		mviewHolder.layout.setBackgroundResource(R.drawable.round_grey_btn);
	
//
//			if (position != list.size()) {
//				mviewHolder.button.setText(list.get(position).getPname());
//				mviewHolder.num.setText("("
//						+ list.get(position).getSubTasks().size() + ")");
//		}
//		if (position == selection) {
//			mviewHolder.layout.setBackgroundResource(R.drawable.roundbg_btn);
//		} else {
//			mviewHolder.layout.setBackgroundResource(R.drawable.round_grey_btn);
//		}
		return convertView;
	}

	int selection = -1;

	public void serBgColor(int selection) {
		this.selection = selection;
		notifyDataSetChanged();
	}

	class ViewHolder2 {

		TextView button;
		TextView num;
		LinearLayout layout;

	}

	public int dp2Px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}
}
