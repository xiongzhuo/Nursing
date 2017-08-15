package com.deya.hospital.widget.popu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.deya.acaide.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TipsDialog extends Dialog {
	View.OnClickListener listener;
	TextView numTv;
	private ScheduledExecutorService executor = Executors
			.newSingleThreadScheduledExecutor();
	String num="";
	private TipsInter inter;


	Context context;
	/**
	 * Creates a new instance of MyDialog.
	 */
	public TipsDialog(Context context, String num) {
		super(context, R.style.SelectDialog2);
		this.num = num;
		this.context=context;
	}
	public TipsDialog(Context context) {
		super(context, R.style.SelectDialog2);
		this.context=context;
	}
	public void setScoreText(String num) {
		this.num = num;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_tips);
		numTv = (TextView) this.findViewById(R.id.numTv);
		numTv.setText("+" + num);
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				dismiss();
				if(null!=inter){
					inter.onDismiss();
				}
				executor.shutdown();
			}
		};
		// 新建调度任务
		
		executor.schedule(runner, 2000, TimeUnit.MILLISECONDS);
	}

	
	public void setdismissListener(TipsInter inter){
		this.inter=inter;
	}
	 public interface TipsInter{
		 public void onDismiss();
	 }
	public void show(String num){
		super.show();
		numTv.setText("+" + num);

	}
	public  void showScore(String num){
		super.show();
		if(null!=context){
			numTv.setText("+" + num);
		}


	}


}
