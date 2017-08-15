package com.deya.hospital.workcircle;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.CommentVo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

public class CommentListAdapter extends BaseAdapter {
	List<CommentVo> list;
	Context context;
	LayoutInflater inflater;
	DisplayImageOptions optionsSquare_men;
	DisplayImageOptions optionsSquare_women;

	CommentListAdapter(List<CommentVo> list, Context context) {
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
		optionsSquare_men = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.men_defult)
				.showImageForEmptyUri(R.drawable.men_defult)
				.showImageOnFail(R.drawable.men_defult)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		optionsSquare_women = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.women_defult)
				.showImageForEmptyUri(R.drawable.women_defult)
				.showImageOnFail(R.drawable.women_defult)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == list ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.circle_comment_item, null);
			viewHolder.timeTv = (TextView) convertView
					.findViewById(R.id.update_time_tv);
			viewHolder.nameTv = (TextView) convertView
					.findViewById(R.id.nameTv);
			viewHolder.contentTv = (TextView) convertView
					.findViewById(R.id.contentTv);
			viewHolder.img = (ImageView) convertView
					.findViewById(R.id.avatar_iv);
			viewHolder.itemheadView= (LinearLayout) convertView
					.findViewById(R.id.itemheadView);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CommentVo cv = list.get(position);
		viewHolder.timeTv.setText(cv.getUpdate_time());
		ImageLoader.getInstance().displayImage(WebUrl.FILE_PDF_LOAD_URL+cv.getAvatar(), viewHolder.img, optionsSquare_men);;
		viewHolder.nameTv.setText(cv.getName());
		viewHolder.contentTv.setText(cv.getContent());
		viewHolder.itemheadView.setVisibility(position==0? View.VISIBLE: View.GONE);

		return convertView;
	}

	public class ViewHolder {
		TextView contentTv;
		TextView nameTv;
		ImageView img;
		TextView timeTv;
		LinearLayout itemheadView;

	}
}
