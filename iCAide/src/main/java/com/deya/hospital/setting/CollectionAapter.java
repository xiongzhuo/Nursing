package com.deya.hospital.setting;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.ArticalVo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

public class CollectionAapter extends BaseAdapter {
	Context context;
	List<ArticalVo> list;
	LayoutInflater inflater;
	private DisplayImageOptions optionsSquare;

	CollectionAapter(Context context, List<ArticalVo> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		optionsSquare = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.defult_list_img)
		.showImageForEmptyUri(R.drawable.defult_list_img)
		.showImageOnFail(R.drawable.defult_list_img)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.circle_artical_item, null);
			viewHolder.titleTv = (TextView) convertView
					.findViewById(R.id.titleTv);
			viewHolder.timeTv = (TextView) convertView
					.findViewById(R.id.update_time_tv);
			viewHolder.nameTv = (TextView) convertView
					.findViewById(R.id.nameTv);
			viewHolder.zanNumTv = (TextView) convertView
					.findViewById(R.id.zanNumTv);
			viewHolder.typeTv = (TextView) convertView
					.findViewById(R.id.typeTv);
			viewHolder.commentNumTv = (TextView) convertView
					.findViewById(R.id.commentNumTv);
			viewHolder.contentTv = (TextView) convertView
					.findViewById(R.id.contentTv);
			viewHolder.img = (ImageView) convertView
					.findViewById(R.id.avatar_iv);
			viewHolder.rightImgView = (ImageView) convertView
					.findViewById(R.id.rightImgView);
			viewHolder.imgView1 = (ImageView) convertView
					.findViewById(R.id.imgView1);
			viewHolder.imgView2 = (ImageView) convertView
					.findViewById(R.id.imgView2);
			viewHolder.imgView3 = (ImageView) convertView
					.findViewById(R.id.imgView3);
			viewHolder.imgLay = (LinearLayout) convertView
					.findViewById(R.id.imgLay);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ArticalVo av = (ArticalVo) getItem(position);
		if (!AbStrUtil.isEmpty(av.getChannel_name())) {
			viewHolder.nameTv.setText(av.getExpert_name());
		} else {
			viewHolder.nameTv.setText("");
		}
	//	viewHolder.timeTv.setText(av.getUpdate_time());
		viewHolder.titleTv.setText(av.getTitle());
		viewHolder.typeTv.setText(av.getChannel_name());

		viewHolder.imgLay.setVisibility(av.getList_type() == 3 ? View.VISIBLE
				: View.GONE);
		viewHolder.rightImgView
				.setVisibility(av.getList_type() == 1 ? View.VISIBLE
						: View.GONE);
		viewHolder.zanNumTv.setText(av.getLike_count()+"赞");
		viewHolder.commentNumTv.setText(av.getComment_count()+"评论");
		if (av.getList_type() == 1) {
			ImageLoader.getInstance().displayImage(
					WebUrl.FILE_LOAD_URL + "/"
							+ av.getAttachment().get(0).getFile_name(),
					viewHolder.rightImgView,optionsSquare);
		} else if (av.getList_type() == 3) {
			ImageLoader.getInstance().displayImage(
					WebUrl.FILE_LOAD_URL + "/"
							+ av.getAttachment().get(0).getFile_name(),
					viewHolder.imgView1,optionsSquare);
			ImageLoader.getInstance().displayImage(
					WebUrl.FILE_LOAD_URL + "/"
							+ av.getAttachment().get(1).getFile_name(),
					viewHolder.imgView2,optionsSquare);
			ImageLoader.getInstance().displayImage(
					WebUrl.FILE_LOAD_URL + "/"
							+ av.getAttachment().get(2).getFile_name(),
					viewHolder.imgView3,optionsSquare);
		}
		return convertView;
	}

	public class ViewHolder {
		TextView contentTv, typeTv;
		TextView nameTv;
		ImageView img, rightImgView, imgView1, imgView2, imgView3;
		TextView timeTv, zanNumTv, commentNumTv, titleTv;
		GridView imgGv;
		LinearLayout imgLay;
	}
}
