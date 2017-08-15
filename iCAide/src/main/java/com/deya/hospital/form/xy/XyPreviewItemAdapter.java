package com.deya.hospital.form.xy;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.FormDetailVo;
import com.deya.hospital.vo.Subrules;

public class XyPreviewItemAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<FormDetailVo> list;
	ItemInter inter;
	int type;
	Context context;
	int parentPosition;
	boolean isUploded;

	public XyPreviewItemAdapter(Context context, boolean isUploded,
			List<FormDetailVo> list, int type, int parentPosition,
			ItemInter itemInter) {
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.type = type;
		this.context = context;
		this.inter = itemInter;
		this.isUploded = isUploded;
		this.parentPosition = parentPosition;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewholder;

		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_item_prviewlist, null);
			viewholder.titleTv = (TextView) convertView
					.findViewById(R.id.titleTv);
			viewholder.remarkTv = (TextView) convertView
					.findViewById(R.id.normalScoreTv);
			viewholder.subRulesTv = (TextView) convertView
					.findViewById(R.id.subRulesTv);
			viewholder.itemRulesLay = (RelativeLayout) convertView
					.findViewById(R.id.itemRulesLay);
			viewholder.moreTv = (TextView) convertView
					.findViewById(R.id.moreTv);

			convertView.setTag(viewholder);

		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}

		// viewholder.ruleAdapter = new SubRulesAdapter(context,
		// fv.getSub_rule(), new RulesAdapterInter() {
		//
		// @Override
		// public void onPutdata() {
		//
		// inter.onputdata(position);
		// }
		// });
		final FormDetailVo fv = list.get(position);

		viewholder.subRulesTv.setText(getsubRulesTv(fv.getSub_rule()));

		viewholder.titleTv.setText(position + 1 + "、" + fv.getDescribes());
		viewholder.itemRulesLay.setVisibility(View.VISIBLE);

		if (fv.getBase_score() != fv.getScores()) {
			String text1 = fv.getDescribes();
			viewholder.titleTv.setText(text1
					+ AbStrUtil.reMoveUnUseNumber("(标准分：" + fv.getBase_score()
							+ "")
					+ ",得分："
					+ AbStrUtil.reMoveUnUseNumber(list.get(position)
							.getScores() + "") + ")");

			if (isUploded) {
				viewholder.remarkTv.setVisibility(View.GONE);
			} else {
				viewholder.remarkTv.setVisibility(View.VISIBLE);
			}
			if (fv.isRemark() || fv.getIs_remark() == 1) {
				viewholder.remarkTv.setText("已备注");
				viewholder.remarkTv
						.setBackgroundResource(R.drawable.big_green_biankuang02);
				viewholder.remarkTv.setTextColor(context.getResources()
						.getColor(R.color.light_green));
				AbStrUtil.setPiceTextCorlor(
						context.getResources().getColor(R.color.light_green),
						viewholder.titleTv, viewholder.titleTv.getText()
								.toString(), viewholder.titleTv.getText()
								.toString().length(), text1.length());
			} else {
				viewholder.remarkTv
						.setBackgroundResource(R.drawable.big_yellow_biankuang);
				viewholder.remarkTv.setTextColor(context.getResources()
						.getColor(R.color.light_yellow));
				viewholder.remarkTv.setText("加备注");
				AbStrUtil.setPiceTextCorlor(
						context.getResources().getColor(R.color.light_yellow),
						viewholder.titleTv, viewholder.titleTv.getText()
								.toString(), viewholder.titleTv.getText()
								.toString().length(), text1.length());
			}

		} else {
			viewholder.titleTv.setTextColor(context.getResources().getColor(
					R.color.black));
			viewholder.titleTv.setText(position + 1 + "、" + fv.getDescribes()
					+ "(" + fv.getBase_score() + ")");
			if (!AbStrUtil.isEmpty(list.get(position).getRemark())||fv.getIs_remark() == 1) {
				viewholder.remarkTv.setText("已备注");
				viewholder.remarkTv
						.setBackgroundResource(R.drawable.big_green_biankuang02);
				viewholder.remarkTv.setTextColor(context.getResources()
						.getColor(R.color.light_green));
				if (isUploded) {
					viewholder.remarkTv.setVisibility(View.GONE);
				} else {
					viewholder.remarkTv.setVisibility(View.VISIBLE);
				}
			} else {
				viewholder.remarkTv.setVisibility(View.GONE);
			}
		}

		if (fv.isReadMore()) {
			viewholder.subRulesTv.setMaxLines(100);
			viewholder.moreTv.setVisibility(View.GONE);
		} else {
			viewholder.subRulesTv.setMaxLines(4);
			viewholder.moreTv.setVisibility(View.VISIBLE);
		}
		// viewholder.subRulesTv.post(new Runnable() {
		// @SuppressLint("NewApi")
		// @Override
		// public void run() {
		// if (viewholder.subRulesTv.getLineCount() <= 4
		// || fv.isReadMore()) {
		// viewholder.moreTv.setVisibility(View.GONE);
		// } else {
		// viewholder.moreTv.setVisibility(View.VISIBLE);
		// }
		//
		// }
		//
		// });

		viewholder.moreTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fv.setReadMore(true);
				viewholder.moreTv.setMaxLines(100);
				viewholder.moreTv.setVisibility(View.GONE);
				inter.onrefresh();

			}
		});
		// if(list.get(position).isRemark()){
		// viewholder.remarkTv.setText("已备注");
		// }else{
		// viewholder.remarkTv.setText("加备注");
		// }
		String text1 = position + 1 + "、" + list.get(position).getDescribes();
		if (this.type == 3) { // ABCD类型
			text1 = list.get(position).getDescribes();
		}
		Log.i("debug", this.type + "");

		if (fv.getChooseItem() != 0 && fv.getChooseItem() != -1) {

			if (AbStrUtil.isEmpty(list.get(position).getRemark())&&fv.getIs_remark() == 0) {
				viewholder.titleTv.setText(text1
						+ "(评定结果:"
						+ list.get(position).getSub_rule()
								.get(list.get(position).getChooseItem())
								.getChoose() + ")");

				AbStrUtil.setPiceTextCorlor(
						context.getResources().getColor(R.color.light_yellow),
						viewholder.titleTv, viewholder.titleTv.getText()
								.toString(), viewholder.titleTv.getText()
								.toString().length(), text1.length());
			} else {
				viewholder.titleTv.setText(text1
						+ "(评定结果:"
						+ list.get(position).getSub_rule()
								.get(list.get(position).getChooseItem())
								.getChoose() + ")");

				AbStrUtil.setPiceTextCorlor(
						context.getResources().getColor(R.color.light_green),
						viewholder.titleTv, viewholder.titleTv.getText()
								.toString(), viewholder.titleTv.getText()
								.toString().length(), text1.length());
			}
		} else {
			viewholder.titleTv.setText(text1 + "("
					+ AbStrUtil.reMoveUnUseNumber(fv.getBase_score() + "")
					+ "分)");
		}

		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				inter.onputdata(position);

			}
		});
		viewholder.remarkTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inter.onRemark(position);
			}
		});
		return convertView;
	}

	private String getsubRulesTv(List<Subrules> list2) {
		String str = "";
		for (int i = 0; i < list2.size(); i++) {
			if (i == list2.size() - 1) {
				str += list2.get(i).getChoose() + "、" + list2.get(i).getName();
			} else {
				str +=  list2.get(i).getChoose()+ "、" + list2.get(i).getName() + "\n";
			}
		}
		return str;
	}

	public class ViewHolder {
		TextView titleTv;
		TextView remarkTv;
		TextView nowScoreTv;
		TextView subRulesTv, moreTv;
		RelativeLayout itemRulesLay;

	}

	public interface ItemInter {
		public void onRemark(int position);

		public void onputdata(int position);

		public void onrefresh();
	}

	int morePosition = -1;

	public void setTextView(final TextView tx1, final TextView tx2) {
		tx1.post(new Runnable() {
			@SuppressLint("NewApi")
			@Override
			public void run() {
				if (tx1.getLineCount() > 4) {
					tx2.setVisibility(View.VISIBLE);
				}

			}

		});

	}
}
