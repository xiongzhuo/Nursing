package com.deya.hospital.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.KindsVo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

public class KindsAdapter extends BaseAdapter {

	private List<KindsVo> list;
	private Context context;
	LayoutInflater inflate;
	private DisplayImageOptions optionsSquare;

	public KindsAdapter(Context context, List<KindsVo> list) {
		this.list = list;
		this.context = context;
		inflate = LayoutInflater.from(context);
		optionsSquare = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.defult_list_img)
		.showImageForEmptyUri(R.drawable.defult_list_img)
		.showImageOnFail(R.drawable.defult_list_img)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();
		this.context = context;
		this.list = list;

	}

	@Override
	public int getCount() {
		Log.i("11111111111count", list.size()+"");
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
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("11111111111count", "zhixing");
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = inflate.inflate(R.layout.kinds_list_item, null);
			viewHolder.img = (ImageView) convertView
					.findViewById(R.id.imgIcon);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Log.i("1111111111111", WebUrl.FILE_LOAD_URL
				+ list.get(position).getAttachment());
		String picType=list.get(position).getKind();
		ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
				+ list.get(position).getAttachment(), viewHolder.img, optionsSquare);
		viewHolder.title.setText(list.get(position).getKind_name());
		return convertView;
	}
	public class ViewHolder {
		public ImageView img;
		public TextView title;

	}
}
