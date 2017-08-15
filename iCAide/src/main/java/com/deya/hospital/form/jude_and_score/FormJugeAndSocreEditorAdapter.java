package com.deya.hospital.form.jude_and_score;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.deya.hospital.vo.FormDetailVo;

import java.util.List;

/**
 * 对错打分模版
 * 
 * @author sunpeng
 *
 */
public class FormJugeAndSocreEditorAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<FormDetailVo> list;
	boolean isUpdate = false;
	SwipeMenuListView listview;
	boolean editorbal;
	FormJudeAndScoreSettingInter inter;
	int editorType;// 0解放状态、1只可加、2只可减、3不可编辑

	public FormJugeAndSocreEditorAdapter(Context context, boolean isUpdate,
			boolean editorbal, List<FormDetailVo> list,
			SwipeMenuListView listview, int editorType,
			FormJudeAndScoreSettingInter inter) {
		inflater = LayoutInflater.from(context);
		this.isUpdate = isUpdate;
		this.list = list;
		this.editorbal = editorbal;
		this.listview = listview;
		this.editorType = editorType;
		this.inter = inter;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	public void setEditorType(int editorType) {
		this.editorType = editorType;
		notifyDataSetChanged();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
//		if (null == convertView) {
//			viewHolder = new ViewHolder();
//			convertView = inflater.inflate(R.layout.layout_form_juge_editor,
//					null);
//			viewHolder.judeBtn = (Button) convertView
//					.findViewById(R.id.judeBtn);
//			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
//			viewHolder.scoreView = (LinearLayout) convertView
//					.findViewById(R.id.scoreView);
//			viewHolder.subtractionBtn = (Button) convertView
//					.findViewById(R.id.btn_subtraction);
//			viewHolder.plusBtn = (Button) convertView
//					.findViewById(R.id.btn_add);
//
//			viewHolder.numTv = (TextView) convertView.findViewById(R.id.numTv);
//			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
//		int result = list.get(position).getResult();
//		viewHolder.numTv.setText(AbStrUtil.reMoveUnUseNumber(list.get(position)
//				.getScores() + ""));
//		if (result == 2) {
//			viewHolder.judeBtn
//					.setBackgroundResource(R.drawable.review_wrong_img);
//			viewHolder.scoreView.setVisibility(View.VISIBLE);
//			// list.get(position).setCanOpen(true);
//		} else {
//			viewHolder.judeBtn
//					.setBackgroundResource(R.drawable.revirew_right_img);
//			viewHolder.scoreView.setVisibility(View.GONE);
//			// list.get(position).setCanOpen(false);
//
//		}
//		if (isUpdate&&!editorbal) {
//			viewHolder.judeBtn.setEnabled(false);
//			viewHolder.plusBtn.setVisibility(View.GONE);
//			viewHolder.subtractionBtn.setVisibility(View.GONE);
//			viewHolder.numTv.setBackgroundResource(R.color.white);
//
//		}else{
//			viewHolder.plusBtn.setVisibility(View.VISIBLE);
//			viewHolder.subtractionBtn.setVisibility(View.VISIBLE);
//			viewHolder.numTv.setBackgroundResource(R.drawable.num_text_bg);
//			viewHolder.judeBtn.setEnabled(true);
//		}
//		viewHolder.title.setText(list.get(position).getDescribes());
//		viewHolder.judeBtn.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				int result = list.get(position).getResult();
//				if (result == 1) {
//					viewHolder.judeBtn
//							.setBackgroundResource(R.drawable.review_wrong_img);
//					list.get(position).setResult(2);
//					list.get(position).setOpenState(1);
//				} else if (result == 2) {
//					viewHolder.judeBtn
//							.setBackgroundResource(R.drawable.revirew_right_img);
//					list.get(position).setResult(1);
//					list.get(position).setOpenState(2);
//				}
//
//				notifyDataSetChanged();
//			}
//		});
//		if (list.get(position).getOpenState() == 1) {
//
//			listview.smoothOpenMenu(position);
//		} else {
//			View view = parent.getChildAt(position);
//			if (view instanceof SwipeMenuLayout) {
//				SwipeMenuLayout mTouchView = (SwipeMenuLayout) view;
//				mTouchView.smoothCloseMenu();
//			}
//		}
//
//		viewHolder.plusBtn.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				double score = list.get(position).getScores();
//				double unitscore = list.get(position).getUnit_score();
//				score += unitscore;
//				viewHolder.numTv.setText(AbStrUtil
//						.reMoveUnUseNumber(score + ""));
//				list.get(position).setScores(score);
//				// if(list.get(position).getScores()>=list.get(position).getBase_score()){
//				// list.get(position).setOpenState(2);
//				// }else{
//				// list.get(position).setOpenState(1);
//				// }
//				inter.onAddScore(unitscore);
//				notifyDataSetChanged();
//
//			}
//		});
//
//		if (editorType == 2) {
//			viewHolder.plusBtn.setEnabled(false);
//			if (list.get(position).getScores() <= 0) {
//				viewHolder.subtractionBtn.setEnabled(false);
//			} else {
//				viewHolder.subtractionBtn.setEnabled(true);
//			}
//			viewHolder.subtractionBtn.setEnabled(true);
//		} else if (editorType == 1) {
//			if (list.get(position).getScores() >= list.get(position)
//					.getBase_score()) {
//				viewHolder.plusBtn.setEnabled(false);
//			} else if (list.get(position).getScores() <= 0) {
//				viewHolder.plusBtn.setEnabled(true);
//			}
//			viewHolder.subtractionBtn.setEnabled(false);
//		} else {
//			if (list.get(position).getScores() >= list.get(position)
//					.getBase_score()) {
//				viewHolder.plusBtn.setEnabled(false);
//				viewHolder.subtractionBtn.setEnabled(true);
//			} else if (list.get(position).getScores() <= 0) {
//				viewHolder.plusBtn.setEnabled(true);
//				viewHolder.subtractionBtn.setEnabled(false);
//			} else {
//				viewHolder.plusBtn.setEnabled(true);
//				viewHolder.subtractionBtn.setEnabled(true);
//			}
//		}
//		viewHolder.subtractionBtn
//				.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						double score = list.get(position).getScores();
//						if (score > 0) {
//							double unitscore = list.get(position)
//									.getUnit_score();
//							score -= unitscore;
//							viewHolder.numTv.setText(AbStrUtil
//									.reMoveUnUseNumber(score + ""));
//							list.get(position).setScores(score);
//							// list.get(position).setOpenState(1);
//							notifyDataSetChanged();
//							inter.onDecreaseScore(unitscore);
//
//						}
//
//					}
//				});
		return convertView;
	}

	public class ViewHolder {

		Button subtractionBtn, judeBtn, plusBtn;
		TextView numTv, title;
		LinearLayout scoreView;

	}

	public void setStatus(int positon, int status) {
		for (int i = 0; i < list.size(); i++) {
			if (positon == i) {
				list.get(i).setOpenState(status);
			}
		}
	}

	public interface FormJudeAndScoreSettingInter {
		public void onAddScore(double position);

		public void onDecreaseScore(double score);

		public void onDelResult(int score);

	}
}
