package com.deya.hospital.descover;

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
import com.deya.hospital.adapter.QuestionDetailImageListAdapter;
import com.deya.hospital.vo.AnswerVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.util.WebUrl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

public class AnswerAdapter extends BaseAdapter {

	List<AnswerVo> list;
	LayoutInflater inflater;
	Context context;
	AnswerInter inter;
	private DisplayImageOptions optionsSquare;
	public boolean showAdopt = true;
	public boolean isMine = false;

	public AnswerAdapter(Context context, List<AnswerVo> list, AnswerInter inter) {
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.inter = inter;
		optionsSquare = new DisplayImageOptions.Builder()
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHoler viewHoler;
		if (null == convertView) {
			viewHoler = new ViewHoler();
			convertView = inflater.inflate(
					R.layout.layout_question_detail_list, null);
			viewHoler.answerHeadImg = (ImageView) convertView
					.findViewById(R.id.answerHeadImg);
			viewHoler.zanLay = convertView.findViewById(R.id.zanLay);
			viewHoler.usernameTv = (TextView) convertView
					.findViewById(R.id.usernameTv);
			viewHoler.departNameTv = (TextView) convertView
					.findViewById(R.id.departNameTv);
			viewHoler.hospitalnameTv = (TextView) convertView
					.findViewById(R.id.hospitalnameTv);
			viewHoler.zanNumTv = (TextView) convertView
					.findViewById(R.id.zanNumTv);
			viewHoler.answerContenTv = (TextView) convertView
					.findViewById(R.id.answerContenTv);
			viewHoler.imgGv = (GridView) convertView.findViewById(R.id.imgGv);
			viewHoler.adoptImg = (ImageView) convertView
					.findViewById(R.id.adoptImg);
			viewHoler.recomendedTv = (TextView) convertView
					.findViewById(R.id.recomendedTv);
			viewHoler.zanImg = (ImageView) convertView
					.findViewById(R.id.zanImg);
			viewHoler.adaoptTv = (TextView) convertView
					.findViewById(R.id.adaoptTv);
			viewHoler.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);

			viewHoler.adoptLay = (LinearLayout) convertView
					.findViewById(R.id.adoptLay);
			viewHoler.adoptLine = convertView.findViewById(R.id.adoptLine);

			convertView.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) convertView.getTag();
		}
		final AnswerVo aVo = list.get(position);
		viewHoler.tv_time.setText(aVo.getUpdate_time());

		// 点击事件
		viewHoler.usernameTv.setText(aVo.getUser_name());
		viewHoler.answerContenTv.setText(aVo.getContent());
		viewHoler.hospitalnameTv.setText(aVo.getHospital());
		viewHoler.departNameTv.setText("(" + aVo.getRegis_job() + ")");
		viewHoler.zanNumTv.setText(aVo.getLike_count() + "");
		ImageLoader.getInstance().displayImage(
				WebUrl.FILE_PDF_LOAD_URL + aVo.getAvatar(),
				viewHoler.answerHeadImg, optionsSquare);
		if (aVo.getIs_adopt() == 1) {
			viewHoler.adoptLay.setVisibility(View.VISIBLE);
			viewHoler.adoptImg.setBackgroundResource(R.drawable.adopt_select);
			viewHoler.adoptLine.setVisibility(View.VISIBLE);
			viewHoler.adaoptTv.setText("已采纳");
			viewHoler.recomendedTv.setVisibility(View.VISIBLE);
		} else {
			viewHoler.adoptLay.setVisibility(View.GONE);
			viewHoler.adoptLine.setVisibility(View.GONE);
			viewHoler.recomendedTv.setVisibility(View.GONE);
		}
		// if (aVo.getAdoptState() == 0) {
		// viewHoler.adoptImg.setBackgroundResource(R.drawable.adopt_unselect);
		// } else {
		// viewHoler.adoptImg.setBackgroundResource(R.drawable.adopt_select);
		// }

		if (aVo.getIs_like() == 0) {
			viewHoler.zanImg.setBackgroundResource(R.drawable.zan_normal);
		} else {
			viewHoler.zanImg.setBackgroundResource(R.drawable.zan_select);
		}

		if (isMine) {

			for (int i = 0; i < list.size(); i++) {
				if (1==list.get(i).getIs_adopt()) {
					showAdopt = false;
				}
			}

			if (showAdopt) {
				viewHoler.adoptImg
						.setBackgroundResource(R.drawable.adopt_unselect);
				viewHoler.adoptImg.setVisibility(View.VISIBLE);
				viewHoler.adoptLay.setVisibility(View.VISIBLE);
			}

		}

		

		viewHoler.adoptImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				inter.onClickAdopt(position);
			}
		});
		viewHoler.zanLay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				inter.onClickZan(position);
			}
		});
		List<Attachments> imgList = aVo.getA_attachment();

		QuestionDetailImageListAdapter imgAdapter = new QuestionDetailImageListAdapter(
				context, imgList,
				new QuestionDetailImageListAdapter.ImageAdapterInter() {

					@Override
					public void ondeletImage(int position) {

					}
				});
		viewHoler.imgGv.setAdapter(imgAdapter);

		if ("匿名".equals(aVo.getUser_name())) {
			viewHoler.departNameTv.setText("");
			viewHoler.hospitalnameTv.setText("");
		}

		return convertView;
	}

	public class ViewHoler {
		public View zanLay;
		public LinearLayout adoptLay;
		ImageView answerHeadImg, adoptImg, zanImg;
		TextView usernameTv, departNameTv, hospitalnameTv, zanNumTv,
				answerContenTv, adaoptTv, recomendedTv,tv_time;
		GridView imgGv;
		View adoptLine;

	}

	public interface AnswerInter {
		public void onClickAdopt(int position);

		public void onClickZan(int position);
	}
}
