package com.baoyz.timepicker;

import android.text.format.Time;
import android.view.View;

import com.deya.acaide.R;

import java.util.Arrays;
import java.util.List;

public class WheelMain {

	private View view;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;
	public int screenheight;
	private boolean hasSelectTime;
	private static int START_YEAR = 1990, END_YEAR = 2100, START_MONTH = 1,
			START_DAY = 1, START_HOUR = 0, START_MINUTE = 0;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public WheelMain(View view) {
		super();
		this.view = view;
		hasSelectTime = false;
		setView(view);
	}

	public WheelMain(View view, boolean hasSelectTime) {
		super();
		this.view = view;
		this.hasSelectTime = hasSelectTime;
		setView(view);
	}

	public void initDateTimePicker(int year, int month, int day) {
		this.initDateTimePicker(year, month, day, 0, 0);
	}

	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void initDateTimePicker(int year, int month, int day, int h, int m) {
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		START_YEAR = year;
		END_YEAR = year + 50;
		START_MONTH = month;
		START_DAY = day;
		START_HOUR = h;
		START_MINUTE = m;
		// 年
		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(false);// 可循环滚动

		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(0);// 初始化时显示的数据
		// 月
		wv_month = (WheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(month + 1, 12));
		wv_month.setCyclic(false);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(0);

		// 日
		wv_day = (WheelView) view.findViewById(R.id.day);
		wv_day.setCyclic(false);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(day, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(day, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(day, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(day, 28));
		}
		wv_day.setLabel("日");
		wv_day.setCurrentItem(0);

		wv_hours = (WheelView) view.findViewById(R.id.hour);
		wv_mins = (WheelView) view.findViewById(R.id.min);
		if (hasSelectTime) {
			wv_hours.setVisibility(View.VISIBLE);
			wv_mins.setVisibility(View.VISIBLE);

			wv_hours.setAdapter(new NumericWheelAdapter(h, 23));
			wv_hours.setCyclic(false);// 可循环滚动
			wv_hours.setLabel("时");// 添加文字
			wv_hours.setCurrentItem(0);

			wv_mins.setAdapter(new NumericWheelAdapter(m, 59));
			wv_mins.setCyclic(false);// 可循环滚动
			wv_mins.setLabel("分");// 添加文字
			wv_mins.setCurrentItem(0);
		} else {
			wv_hours.setVisibility(View.GONE);
			wv_mins.setVisibility(View.GONE);
		}

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time
				t.setToNow(); // 取得系统时间。
				int year = t.year;
				int month = t.month;
				int day = t.monthDay;
				int hour = t.hour; // 0-23
				int minute = t.minute;
				START_YEAR = year;
				START_MONTH = month;
				START_DAY = day;
				START_HOUR = hour;
				START_MINUTE = minute;
				int year_num = newValue + START_YEAR;
				if (newValue + START_YEAR == START_YEAR) {
					// 月
					wv_month = (WheelView) view.findViewById(R.id.month);
					wv_month.setAdapter(new NumericWheelAdapter(
							START_MONTH + 1, 12));
					wv_month.setCyclic(false);
					wv_month.setLabel("月");
					wv_month.setCurrentItem(0);

					// 判断大小月及是否闰年,用来确定"日"的数据
					if (list_big.contains(String.valueOf(START_MONTH + 1))) {
						wv_day.setAdapter(new NumericWheelAdapter(START_DAY, 31));
					} else if (list_little.contains(String
							.valueOf(START_MONTH + 1))) {
						wv_day.setAdapter(new NumericWheelAdapter(START_DAY, 30));
					} else {
						// 闰年
						if ((year_num % 4 == 0 && year_num % 100 != 0)
								|| year_num % 400 == 0)
							wv_day.setAdapter(new NumericWheelAdapter(
									START_DAY, 29));
						else
							wv_day.setAdapter(new NumericWheelAdapter(
									START_DAY, 28));
					}
					wv_day.setLabel("日");
					wv_day.setCurrentItem(0);
					if (hasSelectTime) {
						wv_hours.setVisibility(View.VISIBLE);
						wv_mins.setVisibility(View.VISIBLE);

						wv_hours.setAdapter(new NumericWheelAdapter(START_HOUR,
								23));
						wv_hours.setCyclic(false);// 可循环滚动
						wv_hours.setLabel("时");// 添加文字
						wv_hours.setCurrentItem(0);

						wv_mins.setAdapter(new NumericWheelAdapter(
								START_MINUTE, 59));
						wv_mins.setCyclic(false);// 可循环滚动
						wv_mins.setLabel("分");// 添加文字
						wv_mins.setCurrentItem(0);
					} else {
						wv_hours.setVisibility(View.GONE);
						wv_mins.setVisibility(View.GONE);
					}

				}
				// 判断大小月及是否闰年,用来确定"日"的数据
				else {
					wv_month = (WheelView) view.findViewById(R.id.month);
					wv_month.setAdapter(new NumericWheelAdapter(1, 12));
					wv_month.setCyclic(false);
					wv_month.setLabel("月");
					wv_month.setCurrentItem(0);

					if (list_big.contains(String.valueOf(wv_month
							.getCurrentItem() + 1))) {
						wv_day.setAdapter(new NumericWheelAdapter(1, 31));
					} else if (list_little.contains(String.valueOf(wv_month
							.getCurrentItem() + 1))) {
						wv_day.setAdapter(new NumericWheelAdapter(1, 30));
					} else {
						if ((year_num % 4 == 0 && year_num % 100 != 0)
								|| year_num % 400 == 0)
							wv_day.setAdapter(new NumericWheelAdapter(1, 29));
						else
							wv_day.setAdapter(new NumericWheelAdapter(1, 28));
					}
					wv_day.setLabel("日");
					wv_day.setCurrentItem(0);
					if (hasSelectTime) {
						wv_hours.setVisibility(View.VISIBLE);
						wv_mins.setVisibility(View.VISIBLE);

						wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
						wv_hours.setCyclic(false);// 可循环滚动
						wv_hours.setLabel("时");// 添加文字
						wv_hours.setCurrentItem(0);

						wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
						wv_mins.setCyclic(false);// 可循环滚动
						wv_mins.setLabel("分");// 添加文字
						wv_mins.setCurrentItem(0);
					} else {
						wv_hours.setVisibility(View.GONE);
						wv_mins.setVisibility(View.GONE);
					}
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time
				t.setToNow(); // 取得系统时间。
				int year = t.year;
				int month = t.month;
				int day = t.monthDay;
				int hour = t.hour; // 0-23
				int minute = t.minute;
				START_YEAR = year;
				START_MONTH = month;
				START_DAY = day;
				START_HOUR = hour;
				START_MINUTE = minute;
				if (wv_year.getCurrentItem() == 0 && newValue == 0) {
					if (list_big.contains(String.valueOf(month_num))) {
						wv_day.setAdapter(new NumericWheelAdapter(START_DAY, 31));
					} else if (list_little.contains(String.valueOf(month_num))) {
						wv_day.setAdapter(new NumericWheelAdapter(START_DAY, 30));
					} else {
						if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
								.getCurrentItem() + START_YEAR) % 100 != 0)
								|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
							wv_day.setAdapter(new NumericWheelAdapter(
									START_DAY, 29));
						else
							wv_day.setAdapter(new NumericWheelAdapter(
									START_DAY, 28));
					}
					wv_day.setLabel("日");
					wv_day.setCurrentItem(0);
					if (hasSelectTime) {
						wv_hours.setVisibility(View.VISIBLE);
						wv_mins.setVisibility(View.VISIBLE);

						wv_hours.setAdapter(new NumericWheelAdapter(START_HOUR,
								23));
						wv_hours.setCyclic(false);// 可循环滚动
						wv_hours.setLabel("时");// 添加文字
						wv_hours.setCurrentItem(0);

						wv_mins.setAdapter(new NumericWheelAdapter(
								START_MINUTE, 59));
						wv_mins.setCyclic(false);// 可循环滚动
						wv_mins.setLabel("分");// 添加文字
						wv_mins.setCurrentItem(0);
					} else {
						wv_hours.setVisibility(View.GONE);
						wv_mins.setVisibility(View.GONE);
					}
				} else {
					// 判断大小月及是否闰年,用来确定"日"的数据
					if (list_big.contains(String.valueOf(month_num))) {
						wv_day.setAdapter(new NumericWheelAdapter(1, 31));
					} else if (list_little.contains(String.valueOf(month_num))) {
						wv_day.setAdapter(new NumericWheelAdapter(1, 30));
					} else {
						if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
								.getCurrentItem() + START_YEAR) % 100 != 0)
								|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
							wv_day.setAdapter(new NumericWheelAdapter(1, 29));
						else
							wv_day.setAdapter(new NumericWheelAdapter(1, 28));
					}
					wv_day.setLabel("日");
					wv_day.setCurrentItem(0);
					if (hasSelectTime) {
						wv_hours.setVisibility(View.VISIBLE);
						wv_mins.setVisibility(View.VISIBLE);

						wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
						wv_hours.setCyclic(false);// 可循环滚动
						wv_hours.setLabel("时");// 添加文字
						wv_hours.setCurrentItem(0);

						wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
						wv_mins.setCyclic(false);// 可循环滚动
						wv_mins.setLabel("分");// 添加文字
						wv_mins.setCurrentItem(0);
					} else {
						wv_hours.setVisibility(View.GONE);
						wv_mins.setVisibility(View.GONE);
					}
				}
			}
		};

		// 添加"日"监听
		OnWheelChangedListener wheelListener_day = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time
				t.setToNow(); // 取得系统时间。
				int year = t.year;
				int month = t.month;
				int day = t.monthDay;
				int hour = t.hour; // 0-23
				int minute = t.minute;
				START_YEAR = year;
				START_MONTH = month;
				START_DAY = day;
				START_HOUR = hour;
				START_MINUTE = minute;
				if (wv_year.getCurrentItem() == 0
						&& wv_month.getCurrentItem() == 0 && newValue == 0) {
					if (hasSelectTime) {
						wv_hours.setVisibility(View.VISIBLE);
						wv_mins.setVisibility(View.VISIBLE);

						wv_hours.setAdapter(new NumericWheelAdapter(START_HOUR,
								23));
						wv_hours.setCyclic(false);// 可循环滚动
						wv_hours.setLabel("时");// 添加文字
						wv_hours.setCurrentItem(0);

						wv_mins.setAdapter(new NumericWheelAdapter(
								START_MINUTE, 59));
						wv_mins.setCyclic(false);// 可循环滚动
						wv_mins.setLabel("分");// 添加文字
						wv_mins.setCurrentItem(0);
					} else {
						wv_hours.setVisibility(View.GONE);
						wv_mins.setVisibility(View.GONE);
					}
				} else {
					// 判断大小月及是否闰年,用来确定"日"的数据
					if (hasSelectTime) {
						wv_hours.setVisibility(View.VISIBLE);
						wv_mins.setVisibility(View.VISIBLE);

						wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
						wv_hours.setCyclic(false);// 可循环滚动
						wv_hours.setLabel("时");// 添加文字
						wv_hours.setCurrentItem(0);

						wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
						wv_mins.setCyclic(false);// 可循环滚动
						wv_mins.setLabel("分");// 添加文字
						wv_mins.setCurrentItem(0);
					} else {
						wv_hours.setVisibility(View.GONE);
						wv_mins.setVisibility(View.GONE);
					}
				}
			}
		};

		// 添加"时"监听
		OnWheelChangedListener wheelListener_hour = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time
				t.setToNow(); // 取得系统时间。
				int year = t.year;
				int month = t.month;
				int day = t.monthDay;
				int hour = t.hour; // 0-23
				int minute = t.minute;
				START_YEAR = year;
				START_MONTH = month;
				START_DAY = day;
				START_HOUR = hour;
				START_MINUTE = minute;
				if (wv_year.getCurrentItem() == 0
						&& wv_month.getCurrentItem() == 0
						&& wv_day.getCurrentItem() == 0 && newValue == 0) {
					if (hasSelectTime) {
						wv_hours.setVisibility(View.VISIBLE);
						wv_mins.setVisibility(View.VISIBLE);

						wv_mins.setAdapter(new NumericWheelAdapter(
								START_MINUTE, 59));
						wv_mins.setCyclic(false);// 可循环滚动
						wv_mins.setLabel("分");// 添加文字
						wv_mins.setCurrentItem(0);
					} else {
						wv_hours.setVisibility(View.GONE);
						wv_mins.setVisibility(View.GONE);
					}
				} else {
					// 判断大小月及是否闰年,用来确定"日"的数据
					if (hasSelectTime) {
						wv_hours.setVisibility(View.VISIBLE);
						wv_mins.setVisibility(View.VISIBLE);

						wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
						wv_mins.setCyclic(false);// 可循环滚动
						wv_mins.setLabel("分");// 添加文字
						wv_mins.setCurrentItem(0);
					} else {
						wv_hours.setVisibility(View.GONE);
						wv_mins.setVisibility(View.GONE);
					}
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
		wv_day.addChangingListener(wheelListener_day);
		wv_hours.addChangingListener(wheelListener_hour);
		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		if (hasSelectTime)
			textSize = (screenheight / 100) * 3;
		else
			textSize = (screenheight / 100) * 4;
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;

	}

	public String getTime() {
		StringBuffer sb = new StringBuffer();
		if (!hasSelectTime)
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
					.append((wv_month.getCurrentItem() + 1)).append("-")
					.append((wv_day.getCurrentItem() + 1));
		else {
			int year = 0;
			year = wv_year.getCurrentItem() + START_YEAR;
			int month = 0;
			if (wv_year.getCurrentItem() == 0) {
				month = wv_month.getCurrentItem() + 1 + START_MONTH;
			} else {
				month = wv_month.getCurrentItem() + 1;
			}
			int day = 0;
			if (wv_year.getCurrentItem() == 0 && wv_month.getCurrentItem() == 0) {
				day = wv_day.getCurrentItem() + START_DAY;
			} else {
				day = wv_day.getCurrentItem() + 1;
			}
			int hour = 0;
			if (wv_year.getCurrentItem() == 0 && wv_month.getCurrentItem() == 0
					&& wv_day.getCurrentItem() == 0) {
				hour = wv_hours.getCurrentItem() + START_HOUR;
			} else {
				hour = wv_hours.getCurrentItem();
			}
			int minute = 0;
			if (wv_year.getCurrentItem() == 0 && wv_month.getCurrentItem() == 0
					&& wv_day.getCurrentItem() == 0
					&& wv_hours.getCurrentItem() == 0) {
				minute = wv_mins.getCurrentItem() + START_MINUTE;
			} else {
				minute = wv_mins.getCurrentItem();
			}
			String cache_month="",cache_day="",cache_hour = "", cache_minute = "";
			if(month<10){
				cache_month="0"+month;
			}else{
				cache_month=String.valueOf(month);
			}
			if(day<10){
				cache_day="0"+day;
			}else{
				cache_day=String.valueOf(day);
			}
			if (hour < 10) {
				cache_hour = "0" + hour;
			} else {
				cache_hour = String.valueOf(hour);
			}
			if (minute < 10) {
				cache_minute = "0" + minute;
			} else {
				cache_minute = String.valueOf(minute);
			}
			sb.append(year).append("-").append(cache_month).append("-").append(cache_day)
					.append(" ").append(cache_hour).append(":")
					.append(cache_minute).append(":").append("00");
		}
		return sb.toString();
	}
}
