package com.example.calendar;

import com.deya.acaide.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DepartmentHintFragment extends Fragment {
	private int height, listviewHeight, screnn_width,totalHeight;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_department_hint, null);

		height = getArguments().getInt("height", 650);
		listviewHeight = getArguments().getInt("listviewHeight", 121);
		
		Log.e("bbb", height+"+"+listviewHeight);

		/** 获取屏幕的宽度 */
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		screnn_width = wm.getDefaultDisplay().getWidth();
		Log.e("screnn width", screnn_width + "");

		totalHeight=height;
		
		
		TextView tv=(TextView) view.findViewById(R.id.tv);
		tv.setBackgroundColor(Color.WHITE);
		tv.setText(getArguments().getString("str"));
		LayoutParams para = tv.getLayoutParams();//获取按钮的布局
		para.width=screnn_width/2;//修改宽度
		para.height=listviewHeight;//修改高度
		tv.setLayoutParams(para); //设置修改后的布局。
		
		
		
		Log.e("aaaaaaaaaaaaaaaaaaa", screnn_width/2+"++++"+totalHeight);
		LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) tv.getLayoutParams();
        lp.setMargins(screnn_width/2, totalHeight, 0, 0);
        tv.setLayoutParams(lp);
		
		return view;
	}

}
