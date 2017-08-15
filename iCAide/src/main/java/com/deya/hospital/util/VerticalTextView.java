package com.deya.hospital.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class VerticalTextView extends TextView{

	private String text;

	public VerticalTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		  text = attrs.getAttributeValue(  
	                "http://schemas.android.com/apk/res/android", "text"); 
		  getVerticalText(text);
		  setText(getVerticalText(text));
		
	}

	
	public String getVerticalText(String str) {// 将文字竖向排列
		String title = "";
		for (int i = 0; i < str.length(); i++) {
			char item = str.charAt(i);
			if(i<str.length()-1&&48 <= item && item <= 57){
				char item2 = str.charAt(i+1);
				if(48 <= item2&& item2 <= 57){
					title += item;//将相连的数字组合到一起
					continue;
				}else{
					title += item+"\n";//将相连的数字组合到一起
					continue;	
				}
				
			}else if(i==str.length()-1&&48 <= item && item <= 57){
				title += item;
			}else{
				if (!TextUtils.isEmpty(item + "")) {
					title += item + "\n";
				}
			}
		}
		return title.trim();
	}
}
