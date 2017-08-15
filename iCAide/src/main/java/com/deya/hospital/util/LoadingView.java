package com.deya.hospital.util;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;

public class LoadingView extends LinearLayout {

	private LinearLayout layout;
	private ImageView falshImg;
	LinearLayout networkView;
	TextView tips;
	private AnimationDrawable falshdrawable;
	LoadingStateInter inter;

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		layout = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.common_network_unconnect, this, true);
		init();
	}

	private void init() {
		falshImg = (ImageView) layout.findViewById(R.id.falshImg);
		falshImg.setBackgroundResource(R.drawable.loading_flash);
		falshdrawable = (AnimationDrawable) falshImg.getBackground();
		networkView = (LinearLayout) layout.findViewById(R.id.networkView);
		tips = (TextView) layout.findViewById(R.id.empertyText);
		tips.setText("正在努力加载...");

	}

	public void setTipsText(String text) {
		tips.setText(text);
	}
	public void setEmptyView(String text) {
		falshImg.setBackgroundResource(R.drawable.no_network);
		tips.setText(text);
	}
	public void startAnimition() {
		if (null != inter) {
			inter.onloadingStart();
		}
		falshdrawable.start();
	
	}

	public void setLoadingListener(LoadingStateInter listener) {
		this.inter = listener;
	}

	public void stopAnimition() {
		if (null != inter) {
			inter.onloadingFinish();
		}
		falshdrawable.stop();
		falshdrawable.selectDrawable(0);
	

	}

	public interface LoadingStateInter {
		public void onloadingFinish();

		public void onloadingStart();

	}
}
