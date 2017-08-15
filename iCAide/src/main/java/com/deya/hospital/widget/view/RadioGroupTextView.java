package com.deya.hospital.widget.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.im.sdk.dy.common.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多行，相同大小，自定义选择控件
* @author  yung
* @date 创建时间：2016年1月14日 上午11:14:04 
* @version 1.0
 */
public class RadioGroupTextView extends TextViewGroup implements
		OnClickListener {

	public RadioGroupTextView(Context context) {
		super(context);
	}

	public RadioGroupTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public void setTextViews(List<TextViewGroupItem> dataList) {
		this._dataList = dataList;
		if (layout_width > 0) {
			setTextViewsTrue();
		} else {
			ViewTreeObserver viewTreeObserver = getViewTreeObserver();
			if (viewTreeObserver.isAlive()) {
				viewTreeObserver
						.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
							@Override
							public void onGlobalLayout() {

								layout_width = getWidth();
								LogUtil.d("addOnGlobalLayoutListener",
										"layout_width>>" + layout_width);
								if (layout_width > 0) {
									getViewTreeObserver()
											.removeGlobalOnLayoutListener(this);

									setTextViewsTrue();
								}
							}
						});
			}
		}
	}

	public void setTextViewsTrue() {

		if (viewgroup_columnNum <= 0) {
			viewgroup_columnNum = _dataList.size();
		}

		item_width = (layout_width - viewgroup_itemmargin * (viewgroup_columnNum - 1)) / viewgroup_columnNum;

		ViewGroup parentView = (ViewGroup) getParent();
		parentView.getWidth();
		if (_dataList == null || _dataList.size() == 0) {
			return;
		}

		int line = 0;
		Map<Integer, List<TextView>> lineMap = new HashMap<Integer, List<TextView>>();
		List<TextView> lineList = new ArrayList<TextView>();
		lineMap.put(0, lineList);

		int x = 0;
		int y = 0;

		for (int i = 0; i < _dataList.size(); i++) {
			_dataList.get(i).setPosition(i);
			TextView tv = new TextView(context);
			tv.setTag(_dataList.get(i).getTag());
			tv.setText(_dataList.get(i).getText());
			tv.setTag(R.id.radio_item_tag1, _dataList.get(i));
			if (_dataList.get(i).isStatus()) {
				tv.setBackgroundResource(viewgroup_textBackground_s);
				tv.setTextColor(viewgroup_textColor_s);
			} else {
				tv.setBackgroundResource(viewgroup_textBackground);
				tv.setTextColor(viewgroup_textColor);
			}

			tv.setTextSize(viewgroup_textSize);

			tv.setGravity(Gravity.CENTER_HORIZONTAL);

			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});

			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			tv.measure(w, h);
			int tvh = tv.getMeasuredHeight();
			item_height=tvh;
			if (viewgroup_shape == ShowType.square.ordinal()) {
				item_height = item_width;
			} else if (viewgroup_shape == ShowType.rectangle_half.ordinal()) {
				item_height = item_width / 2;
			} else if (viewgroup_shape == ShowType.rectangle_third.ordinal()) {
				item_height = item_width *2/ 3;
			} 

			tvh = item_height;
			int tvw = item_width;

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					item_width, item_height);
			if (lineMap.get(line).size() >= viewgroup_columnNum) {
				x = 0;
				y = y + tvh + viewgroup_linemargin;

				line++;
				lineMap.put(line, new ArrayList<TextView>());
			}
			lp.leftMargin = x;
			lp.topMargin = y;
			x = x + tvw + viewgroup_itemmargin;
			tv.setLayoutParams(lp);
			lineMap.get(line).add(tv);
		}

		for (int i = 0; i <= line; i++) {
			for (int j = 0; j < lineMap.get(i).size(); j++) {
				TextView tView2 = lineMap.get(i).get(j);
				tView2.setEllipsize(TruncateAt.END);

				if (null != lineMap.get(i).get(j).getTag(R.id.radio_item_tag1)
						&& lineMap.get(i).get(j).getTag(R.id.radio_item_tag1) instanceof TextViewGroupItem) {
					TextViewGroupItem item = (TextViewGroupItem) lineMap.get(i).get(j)
							.getTag(R.id.radio_item_tag1);
					tView2.setOnClickListener(new ItemClick(item));
				}
				tView2.setGravity(Gravity.CENTER);

				tView2.setPadding(tView2.getPaddingLeft(),
						tView2.getPaddingTop(), tView2.getPaddingRight(),
						tView2.getPaddingBottom());
				addView(tView2);
			}
		}
	}
	
}
