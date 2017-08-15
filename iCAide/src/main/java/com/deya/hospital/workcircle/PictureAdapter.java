package com.deya.hospital.workcircle;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.DocumentCategoryEntity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

public class PictureAdapter extends BaseAdapter {

	private Context context;
	private int selectItem;
	private List<DocumentCategoryEntity.CategoryListBean> list;
	private Handler myhandler;


	public PictureAdapter(Context context, List<DocumentCategoryEntity.CategoryListBean> list, Handler myhandler) {
		super();
		this.context = context;
		this.list = list;
		this.myhandler = myhandler;
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (list != null) {
			return list.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setSelectItem(int selectItem) {

		if (this.selectItem != selectItem) {
			this.selectItem = selectItem;
			notifyDataSetChanged();
		}
	}

	public void updataList(List<DocumentCategoryEntity.CategoryListBean> list, Handler myhandler){
		this.list = list;
		this.myhandler = myhandler;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHold hold;

		if (convertView == null) {
			hold = new ViewHold();
			convertView = LayoutInflater.from(context).inflate(R.layout.gallery_item, null);
			hold.mImageView = (ImageView) convertView.findViewById(R.id.imageview);
			hold.mTextView = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(hold);
		} else {
			hold = (ViewHold) convertView.getTag();
		}


		if (!AbStrUtil.isEmpty(list.get(position).getImg())) {
			ImageLoader.getInstance().displayImage(WebUrl.FILE_PDF_LOAD_URL
					+ list.get(position).getImg(), hold.mImageView, imgDisplayImageOptions(R.drawable.ic_document));
		} else {
			hold.mImageView.setImageResource(R.drawable.ic_document);
		}
		hold.mTextView.setText(list.get(position).getTitle());
		int widthPix = context.getResources().getDisplayMetrics().widthPixels;
		if (selectItem == position) {
//			hold.ll_document_item.setLayoutParams(new LinearLayout.LayoutParams(widthPix/3, 180));
			int wh = (int) Math.floor(widthPix * 0.36);
			int hh = (int) Math.floor(wh / 0.75);
			hold.mImageView.setLayoutParams(new LinearLayout.LayoutParams(wh, hh));
			hold.mTextView.setBackgroundResource(R.drawable.btn_shape_fc7f1a);
			hold.mTextView.setWidth(wh);
			hold.mTextView.setTextColor(context.getResources().getColor(R.color.white));
			hold.mTextView.setTextSize(16);
			hold.mTextView.setFocusable(true);
			Message message = new Message();
			message.what = 1;
			message.arg1 = list.get(position).getId();
			message.arg2 = wh;
			message.obj = hold.mTextView;
			myhandler.sendMessage(message);
		} else {
//			hold.ll_document_item.setLayoutParams(new LinearLayout.LayoutParams(widthPix/3, 180));
			int wh = (int) Math.floor(widthPix * 0.28);
			int hh = (int) Math.floor(wh / 0.75);
			hold.mImageView.setLayoutParams(new LinearLayout.LayoutParams(wh, hh));
			hold.mTextView.setFocusable(false);
			hold.mTextView.setTextColor(context.getResources().getColor(R.color.content_black));
			hold.mTextView.setTextSize(14);
		}
		return convertView;
	}

	static class ViewHold {
		public TextView mTextView;
		private ImageView mImageView;
	}

	public DisplayImageOptions imgDisplayImageOptions(int imgId) {
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(imgId)
				.showImageForEmptyUri(imgId)
				.showImageOnFail(imgId)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		return displayImageOptions;
	}
}
