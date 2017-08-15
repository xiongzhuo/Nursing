package com.im.sdk.dy.ui;

import java.util.List;
import java.util.Map;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.WebUrl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class TopHoListAdapter extends BaseAdapter {
	private List< Map<String, Object>> data;
	private Context context;
	DisplayImageOptions optionsSquare_men;
	DisplayImageOptions optionsSquare_women;
	

	public TopHoListAdapter(List<Map<String, Object>> data, Context context) {
		super();
		this.data = data;
		this.context = context;
		
		optionsSquare_men = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.im_men_defalut)
		.showImageForEmptyUri(R.drawable.im_men_defalut)
		.showImageOnFail(R.drawable.im_men_defalut)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();

optionsSquare_women = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.im_women_defalut)
		.showImageForEmptyUri(R.drawable.im_women_defalut)
		.showImageOnFail(R.drawable.im_women_defalut)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();
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


	
	private class  ViewHold{
		ImageView iv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHold viewHold=null;
		if (convertView==null) {
			viewHold=new ViewHold();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_top, null);
			viewHold.iv=(ImageView) convertView.findViewById(R.id.iv);
			convertView.setTag(viewHold);
		}
		
		viewHold=(ViewHold) convertView.getTag();
		
		
		int sex = (Integer)data.get(position).get("hSex");
		if (!AbStrUtil.isEmpty(data.get(position).get("hIv").toString())) {
			ImageLoader.getInstance().displayImage(
					WebUrl.FILE_LOAD_URL + data.get(position).get("hIv").toString(),
					viewHold.iv,
					sex == 1 ? optionsSquare_women
							: optionsSquare_men);

		} else {
			ImageLoader.getInstance().displayImage(
					"",
					viewHold.iv,
					sex == 1 ? optionsSquare_women
							: optionsSquare_men);
		}
		
		
		return convertView;
	}

}
