package com.deya.hospital.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.FormDetailVo;

import java.util.List;

public class FormEditorAdapter2 extends BaseAdapter {
	LayoutInflater inflater;
	List<FormDetailVo> list;
	TextContral contral;
	SwipeMenuListView listview;
	boolean isUpdated = false;
	Context mcContext;
	boolean editorbal;

	public FormEditorAdapter2(Context context, boolean isUpdated,boolean editorbal,
			List<FormDetailVo> list, TextContral contral,
			SwipeMenuListView listview) {
		inflater = LayoutInflater.from(context);
		this.contral = contral;
		this.isUpdated = isUpdated;
		this.mcContext = context;
		this.list = list;
		this.editorbal=editorbal;
		this.listview = listview;
	}

	public void setStatus(int positon, int status) {
		for (int i = 0; i < list.size(); i++) {
			if (positon == i) {
				list.get(i).setOpenState(status);
			}
		}
	}

	public List<FormDetailVo> getList() {
		return this.list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_form_editor, null);
			viewHolder.subtractionBtn = (Button) convertView
					.findViewById(R.id.btn_subtraction);
			viewHolder.plusBtn = (Button) convertView
					.findViewById(R.id.btn_add);
			viewHolder.numTv = (TextView) convertView.findViewById(R.id.numTv);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(list.get(position).getDescribes());
		if (isUpdated) {
			viewHolder.numTv.setText(AbStrUtil.reMoveUnUseNumber(list.get(
					position).getScore()
					+ ""));
			if(!editorbal){
			viewHolder.plusBtn.setVisibility(View.GONE);
			viewHolder.subtractionBtn.setVisibility(View.GONE);
			viewHolder.numTv.setBackgroundResource(R.color.white);
			}else{
				viewHolder.plusBtn.setVisibility(View.VISIBLE);
				viewHolder.subtractionBtn.setVisibility(View.VISIBLE);
				viewHolder.numTv.setBackgroundResource(R.drawable.num_text_bg);
			}
			if (list.get(position).getScore() >= list.get(position)
					.getBase_score()) {
				list.get(position).setCanOpen(false);
				viewHolder.plusBtn.setEnabled(false);
				viewHolder.subtractionBtn.setEnabled(true);
			} else if (list.get(position).getScore() <= 0) {
				list.get(position).setCanOpen(true);
				viewHolder.plusBtn.setEnabled(true);
				viewHolder.subtractionBtn.setEnabled(false);
			} else {
				list.get(position).setCanOpen(true);
				viewHolder.plusBtn.setEnabled(true);
				viewHolder.subtractionBtn.setEnabled(true);
			}
		} else {
			viewHolder.numTv.setText(AbStrUtil.reMoveUnUseNumber(list.get(
					position).getScores()
					+ ""));
			viewHolder.plusBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					double score = list.get(position).getScores();
					double unitscore = list.get(position).getUnit_score();
					score += unitscore;
					viewHolder.numTv.setText(AbStrUtil.reMoveUnUseNumber(score
							+ ""));
					list.get(position).setScores(score);
					if (list.get(position).getScores() >= list.get(position)
							.getBase_score()) {
						list.get(position).setOpenState(2);

					} else {
						list.get(position).setOpenState(1);
					}
					contral.setText(list);
					notifyDataSetChanged();

				}
			});
			if (list.get(position).getScores() >= list.get(position)
					.getBase_score()) {
				viewHolder.plusBtn.setEnabled(false);
				viewHolder.subtractionBtn.setEnabled(true);
			} else if (list.get(position).getScores() <= 0) {
				viewHolder.plusBtn.setEnabled(true);
				viewHolder.subtractionBtn.setEnabled(false);
			} else {
				viewHolder.plusBtn.setEnabled(true);
				viewHolder.subtractionBtn.setEnabled(true);
			}
			viewHolder.subtractionBtn
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							double score = list.get(position).getScores();
							if (score > 0) {
								double unitscore = list.get(position)
										.getUnit_score();
								score -= unitscore;
								viewHolder.numTv.setText(AbStrUtil
										.reMoveUnUseNumber(score + ""));
								list.get(position).setScores(score);
								contral.setText(list);
								list.get(position).setOpenState(1);
								notifyDataSetChanged();

							}

						}
					});

			listview.getItemAtPosition(position);

		}
		if (list.get(position).getOpenState() == 1) {

			listview.smoothOpenMenu(position);
		} else {
			View view = listview.getChildAt(position);
			if (view instanceof SwipeMenuLayout) {
				SwipeMenuLayout mTouchView = (SwipeMenuLayout) view;
				mTouchView.smoothCloseMenu();
			}
		}
		return convertView;
	}

	public class ViewHolder {
		Button subtractionBtn, plusBtn;
		TextView numTv, title;
	}

	public interface TextContral {
		public void setText(List<FormDetailVo> list);
	}
}
