package com.deya.hospital.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.dbdata.planListDb;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.util.List;

public class ButtonAdapter extends BaseAdapter {
	// List<PicListVo> list;
	Context mcontext;
	private LayoutInflater inflater;
	LayoutParams para;
	List<planListDb> list;
	DisplayImageOptions optionsSquare;

	/**
	 * Creates a new instance of MyImageListAdapter.
	 */
	public ButtonAdapter(Context context, List<planListDb> list) {
		inflater = LayoutInflater.from(context);
		mcontext = context;
		this.list = list;

	}

	/**
	 * getSDPath:【获取sd卡】. <br/>
	 * .@return.<br/>
	 */
	public static File getSdPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/deyaCache/");
		}
		return sdDir;
	}

	@Override
	public int getCount() {
		return list == null ? 1 : list.size() + 1;

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
			mviewHolder.addlayout = (LinearLayout) convertView
					.findViewById(R.id.addlayout);
			convertView.setTag(mviewHolder);
		} else {
			mviewHolder = (ViewHolder2) convertView.getTag();
		}
		// mviewHolder.title.setText(list.get(position).getState());
		if (list.size() == 0) {
			mviewHolder.layout.setVisibility(View.GONE);
			mviewHolder.addlayout.setVisibility(View.VISIBLE);
		} else {

			if (position != list.size()) {
				mviewHolder.layout.setVisibility(View.VISIBLE);
				mviewHolder.addlayout.setVisibility(View.GONE);
				mviewHolder.button.setText(list.get(position).getPname());
				mviewHolder.num.setText("("
						+ list.get(position).getSubTasks().size() + ")");
			} else if (position == list.size()) {
				mviewHolder.layout.setVisibility(View.GONE);
				mviewHolder.addlayout.setVisibility(View.VISIBLE);
			}
		}
		if (position == selection) {
			mviewHolder.layout.setBackgroundResource(R.drawable.roundbg_btn);
		} else {
			mviewHolder.layout.setBackgroundResource(R.drawable.round_grey_btn);
		}
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
		LinearLayout addlayout;

	}

	public int dp2Px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}
}
