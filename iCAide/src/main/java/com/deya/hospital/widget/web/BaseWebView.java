package com.deya.hospital.widget.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class BaseWebView extends WebView {
	public static final Integer STATE_LOADING = Integer.valueOf(0);
	public static final Integer STATE_LOAD_FAIL;
	public static final Integer STATE_LOAD_SUCC = Integer.valueOf(1);
	private Context context;
	private Bitmap currentBitmap;
	 public ProgressBar progressbar;
	// private Handler threadHandler;

	static {
		STATE_LOAD_FAIL = Integer.valueOf(2);
	}

	public BaseWebView(Context paramContext) {
		super(paramContext);
		init(paramContext);
	}

	public BaseWebView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		init(paramContext);
	}

	public BaseWebView(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		init(paramContext);
	}

	private void init(Context paramContext) {
		this.context = paramContext;
	//	setVerticalScrollBarEnabled(false);
		setHorizontalScrollBarEnabled(false);
		setHorizontalFadingEdgeEnabled(false);
		setVerticalFadingEdgeEnabled(false);
		setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
		setPadding(0, 0, 0, 0);
		 progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
	        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 3, 0, 0));
	        addView(progressbar);
		WebSettings webSetting = getSettings();
		webSetting.setJavaScriptEnabled(true);
	
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
		webSetting.setSupportZoom(false);
		webSetting.setBuiltInZoomControls(true);
		
	}
	

	public Bitmap getCurrentBitmap() {
		return this.currentBitmap;
	}

	public void reload() {
		if ((Integer) getTag() != STATE_LOADING)
			return;
		super.reload();
	}

	public void setCurrentBitmap(Bitmap paramBitmap) {
		this.currentBitmap = paramBitmap;
	}

	public void stopLoading() {
		if ((Integer) getTag() != STATE_LOADING)
			return;
		super.stopLoading();
	}

}