package com.deya.hospital.base;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.baoyz.timepicker.ScreenInfo;
import com.baoyz.timepicker.WheelMain;
import com.deya.acaide.R;

public  class TimerDialog extends Dialog {

		private Button showBtn;
		Activity  context;
		TimerInter inter;
		/**
		 * Creates a new instance of MyDialog.
		 */
		public TimerDialog(Activity contex,TimerInter inter) {
			super(contex, R.style.SelectDialog);
			this.context=contex;
			this.inter=inter;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO 自动生成的方法存根
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);

			setContentView(R.layout.timepicker);

			LinearLayout timepickerview = (LinearLayout) this
					.findViewById(R.id.timePicker1);
			ScreenInfo screenInfo = new ScreenInfo(context);
			final WheelMain wheelMain = new WheelMain(timepickerview, true);
			wheelMain.screenheight = screenInfo.getHeight();
			Time curTime = new Time(); // or Time t=new Time("GMT+8"); 加上Time
										// Zone资料
			curTime.setToNow(); // 取得系统时间。
			int year = curTime.year;
			int month = curTime.month;
			int day = curTime.monthDay;
			int hour = curTime.hour; // 0-23
			int minute = curTime.minute;
			wheelMain.initDateTimePicker(year, month, day, hour, minute);
			showBtn = (Button) this.findViewById(R.id.btn_ok);

			showBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					inter.onChooseTime(wheelMain.getTime().toString());
					dismiss();

				}
			});

		}
		
	public	interface TimerInter{
			public void onChooseTime(String time);
		}
		
	}