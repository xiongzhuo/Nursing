package com.deya.hospital.shop;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.deya.acaide.R;

public class SinTipsDialog extends Dialog {
	View.OnClickListener listener;
	TextView tv,toptv;
	 private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();  
	/**
	 * Creates a new instance of MyDialog.
	 */
	 String text;
	public SinTipsDialog(Activity context,String text) {
		super(context, R.style.SelectDialog);
		this.text=text;
	}

	public  void setScoreText(String text){
		this.text=text;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sign_dialog_tips);
		tv=(TextView) this.findViewById(R.id.totalTv);
		toptv=(TextView) this.findViewById(R.id.toptv);
		toptv.setText(text+"橄榄");
		tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
		tv.setText(text);
		 Runnable runner = new Runnable() {  
	            @Override  
	            public void run() {  
	                dismiss();  
	                executor.shutdown();
	            }  
	        };  
	        //新建调度任务  
	        executor.schedule(runner, 2500, TimeUnit.MILLISECONDS); 
	        
	}
}
