package com.deya.hospital.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;

/**
 * 
 * @author sunpeng
 *
 */
public class CommonTopView2 extends RelativeLayout {
	RelativeLayout layout;
	TextView submitTxt;
	RelativeLayout rl_back;
	TextView mTv;

	public CommonTopView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		layout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.commomtop2, this, true);

		rl_back = (RelativeLayout) layout.findViewById(R.id.rl_back);
		submitTxt = (TextView) layout.findViewById(R.id.submit);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.commonTopView);
		mTv = (TextView) layout.findViewById(R.id.title);
		CharSequence text = a.getText(R.styleable.commonTopView_android_text);
		CharSequence rightText = a.getText(R.styleable.commonTopView_righttext);
		if (text != null)
			mTv.setText(text);
		boolean showRight = a.getBoolean(R.styleable.commonTopView_showRight,
				false);
		
		if (showRight) {
			submitTxt.setVisibility(View.VISIBLE);
			if(!AbStrUtil.isEmpty(rightText.toString())){
				submitTxt.setText(rightText.toString());
			}
		} else {
			submitTxt.setVisibility(View.GONE);
		}
		a.recycle();
	}

	/**
	 * @deprecated 在布局设置了
	 * @param context
	 * @param title
	 */

	public void  showRightView(){
		
	}
	public void setBackAndRightClick(final Activity context,
			OnClickListener listener, OnClickListener listener2) {
		onbackClick(context, listener);
		onRightClick(context, listener2);

	}

	public void init(final Activity context) {
		rl_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				context.finish();
			}
		});
	}

	public void setOnormalClick(final Activity context) {
		rl_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				context.finish();
			}
		});
	}

	public void onbackClick(final Activity context, OnClickListener listener) {
		rl_back.setOnClickListener(listener);
	}

	public void setRigtext(String text){
		submitTxt.setText(text);
	}
	/**
	 * 右边按钮点击事件
	 * 
	 * @param context
	 * @param listener
	 */
	public void onRightClick(final Activity context, OnClickListener listener) {
		submitTxt.setVisibility(View.VISIBLE);
		submitTxt.setOnClickListener(listener);
	}

	/**
	 * @deprecated 只有标题的titlebaar
	 * @param title
	 */
	public void setHasOnlyTitle() {
		rl_back.setVisibility(View.GONE);
		submitTxt.setVisibility(View.GONE);
	}

	public void setTitle(String text) {
		mTv.setText(text);
	}
	public void setTitleHint(String text) {
		mTv.setHint(text);;
	}
}
