package com.deya.hospital.widget.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;

/**
 * 单行 ，自动宽，选择  自定义控件
* @author  yung
* @date 创建时间：2016年1月14日 上午11:14:36 
* @version 1.0
 */
public class SingleLineTextViewGroup extends TextViewGroup implements
		OnClickListener {
	

	public SingleLineTextViewGroup(Context context) {
		super(context);
	}

	public SingleLineTextViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setTextViewsTrue() {
		ViewGroup parentView = (ViewGroup) getParent();
		parentView.getWidth();
		if (_dataList == null || _dataList.size() == 0) {
			return;
		}

		int line = 0;
		List<TextView> lineList = new ArrayList<TextView>();

		int x = 0;
		int y = 0;
		int margin_left=0;
		int itemsize= _dataList.size();

		for (int i = 0; i < itemsize; i++) {
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

			tv.setGravity(Gravity.CENTER);

		//	tv.setPadding(0, item_padding, 0, item_padding);
			
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
		
			int tvw = tv.getMeasuredWidth();//+2*item_padding ;
			
			
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					tvw,tv.getMeasuredHeight());
		
			lp.leftMargin = x;
			if(i < itemsize-1){
				x = x + tvw + viewgroup_itemmargin;
			}else{
				x = x + tvw ;
			}
			 tv.setLayoutParams(lp);
			 
			 
			lineList.add(tv);
		}
		
		int a=layout_width-x;
		int _width=a/itemsize;
		
		int _x=0;
		for (int i = 0; i <lineList.size(); i++) {
				TextView tView2 = lineList.get(i);
				tView2.setEllipsize(TruncateAt.END);
				
		        tView2.setOnClickListener(new ItemClick(_dataList.get(i)));
				
				tView2.setGravity(Gravity.CENTER);
				
				RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)tView2.getLayoutParams();
//				
				lp.width=lp.width+_width;
				lp.leftMargin = _x;
				_x=_x+lp.width+viewgroup_itemmargin;
				
				tView2.setLayoutParams(lp);
				addView(tView2);
		}
	}
}
