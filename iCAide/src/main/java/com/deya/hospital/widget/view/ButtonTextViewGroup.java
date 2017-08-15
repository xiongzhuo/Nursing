package com.deya.hospital.widget.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义自动换行
* @author  yung
* @date 创建时间：2016年1月6日 下午4:02:28 
* @version 1.0
 */
public class ButtonTextViewGroup extends TextViewGroup {

    public ButtonTextViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
   
    public void setTextViewsTrue() {
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
            TextView tv = new TextView(context);
            tv.setText(_dataList.get(i).getText());
//            if(_dataList.get(i).isStatus()){
//	            if (viewgroup_textBackground != -1)
//	                tv.setBackgroundResource(viewgroup_textBackground_s);
//	            tv.setTextColor(viewgroup_textColor_s);
//            }else{
//            	if (viewgroup_textBackground != -1)
//	                tv.setBackgroundResource(viewgroup_textBackground);
//	            tv.setTextColor(viewgroup_textColor);
//            }
            tv.setBackgroundResource(viewgroup_textBackground);
            tv.setTextSize(viewgroup_textSize);
            tv.setSingleLine(true);
            tv.setEllipsize(TruncateAt.END);
            
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            
            tv.setPadding(viewgroup_itempadding,viewgroup_itempadding,viewgroup_itempadding,viewgroup_itempadding);
            tv.setTag(_dataList.get(i));// 标记position
            tv.setOnClickListener(new ItemClick(_dataList.get(i)));

            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            tv.measure(w, h);
            int tvh = tv.getMeasuredHeight();
            int tvw = getMeasuredWidth(tv);

            
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (x + tvw > layout_width ) {//|| lineMap.get(line).size() >= viewgroup_columnNum
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
            int padding = 0;
            if (viewgroup_overspread) {
                // 该行最后一个位置
                int len = lineMap.get(i).size();
                TextView tView = lineMap.get(i).get(len - 1);

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tView.getLayoutParams();
                
             //   int right = lp.leftMargin + getMeasuredWidth(tView)+textPaddingRight+textPaddingLeft;
                
                int right = lp.leftMargin+ getMeasuredWidth(tView);
                int emptyWidth = layout_width - right;
                padding = emptyWidth / (len * 2);
                
                if(i == line&&lineMap.get(i).size()==1){
                	 if(emptyWidth>=layout_width/2-viewgroup_itemmargin){
                		 lp.width=layout_width/2+ tView.getPaddingLeft()-viewgroup_itemmargin;
                		 lineMap.get(i).get(0).setLayoutParams( lp);
                	 }
					padding=0;
                }
            }


            int leftOffset = 0;
            for (int j = 0; j < lineMap.get(i).size(); j++) {
                TextView tView2 = lineMap.get(i).get(j);

                if (viewgroup_overspread) {
                    RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) tView2.getLayoutParams();
                    lp2.leftMargin = lp2.leftMargin + leftOffset;
                    leftOffset = (j + 1) * 2 * padding;
                }
                if(padding<0){
                	padding=0;
                }
                
//                if(j== lineMap.get(i).size()-1){
//                	padding=padding+(lineMap.get(i).size()-1)*wordMargin;
//                }
                
                  tView2.setSingleLine(true);
                
                tView2.setEllipsize(TruncateAt.END);
                
                tView2.setPadding(
                        tView2.getPaddingLeft() + padding,
                        tView2.getPaddingTop(),
                        tView2.getPaddingRight() + padding,
                        tView2.getPaddingBottom());
                addView(tView2); 
            }
        }
    }
 
}
