package com.deya.hospital.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.deya.acaide.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ReviewTipsDialog extends Dialog {
	View.OnClickListener listener;
	TextView numTv;
	 private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(); 
	 String num;
	/**
	 * Creates a new instance of MyDialog.
	 */
	public ReviewTipsDialog(Context mcontext) {
		super(mcontext, R.style.SelectDialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_main_review);
		setCancelable(false);
		numTv=(TextView) this.findViewById(R.id.btn);
//		 Runnable runner = new Runnable() {  
//	            @Override  
//	            public void run() {  
//	                dismiss();  
//	                executor.shutdown();
//	            }  
//	        };  
//	        //新建调度任务  
//	        executor.schedule(runner, 2500, TimeUnit.MILLISECONDS);  
		numTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
