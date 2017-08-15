package com.deya.hospital.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;

/**
 * 
 * @author sunpeng
 *
 */
public class CommonTopView extends RelativeLayout {
	private final ImageView rightButton;
	RelativeLayout layout;
	TextView submitTxt,backText;
	RelativeLayout rl_back;
	TextView mTv;
	LinearLayout titleView;
	ImageView titleRightImg,img_back;
	public static final int VISIBLE = 0;
	public static final int INVISIBLE = 1;
	public static final int GONE = 2;

	public CommonTopView(Context context, AttributeSet attrs) {
		super(context, attrs);
		layout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.commomtop, this, true);

		rl_back = (RelativeLayout) layout.findViewById(R.id.rl_back);
		//左边文字设置
		backText = (TextView) layout.findViewById(R.id.backText);
		//设置左边文字图片是否隐藏或显示
		img_back = (ImageView) layout.findViewById(R.id.img_back);
		submitTxt = (TextView) layout.findViewById(R.id.submit);
		titleView=(LinearLayout) layout.findViewById(R.id.titleView);
		titleRightImg= (ImageView) layout.findViewById(R.id.titleRightImg);
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

		rightButton=(ImageView) layout.findViewById(R.id.rightButton);
 		a.recycle();
	}

	public void setLeftImagvisibility(int state){
		switch (state){
			case VISIBLE:
				img_back.setVisibility(View.VISIBLE);
				break;
			case INVISIBLE:
				img_back.setVisibility(View.INVISIBLE);
				break;
			case GONE:
				img_back.setVisibility(View.GONE);
				break;
		}


	}

	/**
	 * @param context
	 * @param title
	 */

	public void  showRightView(int visibility){
		submitTxt.setVisibility(visibility);
	}
	public void setBackAndRightClick(final Activity context,
			OnClickListener listener, OnClickListener listener2) {
		onbackClick(context, listener);
		onRightClick(context, listener2);

	}

	public  TextView getRightTextView(){
		return submitTxt;
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

	
	public void setTileRightImgVisble(int visible){
		titleRightImg.setVisibility(visible);
	}
	public void setTitleViewClick(OnClickListener click){
		titleView.setOnClickListener(click);
	}
	public void onbackClick(final Activity context, OnClickListener listener) {
		rl_back.setOnClickListener(listener);
	}

	public void setRigtext(String text){
		submitTxt.setText(text);
	}
	public void setLefttext(String text){
		backText.setText(text);
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

	public LinearLayout getTitleView(){
		return  titleView;
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
	public  ImageView getRigthtView(OnClickListener lis){
		rightButton.setOnClickListener(lis);
		rightButton.setVisibility(VISIBLE);
		return rightButton;
	}
}
