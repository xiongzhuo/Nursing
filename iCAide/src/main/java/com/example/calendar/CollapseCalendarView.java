package com.example.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.example.calendar.manager.CalendarManager;
import com.example.calendar.manager.CalendarUnit;
import com.example.calendar.manager.Day;
import com.example.calendar.manager.Month;
import com.example.calendar.manager.ResizeManager;
import com.example.calendar.manager.Week;
import com.example.calendar.widget.DayView;
import com.example.calendar.widget.WeekView;

import org.joda.time.LocalDate;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Blaz Solar on 28/02/14.
 */
public class CollapseCalendarView extends LinearLayout implements
		View.OnClickListener {

	private static final String TAG = "CalendarView";

	@Nullable
	private CalendarManager mManager;

	@NonNull
	private LinearLayout mWeeksView;

	@NonNull
	private final LayoutInflater mInflater;
	@NonNull
	private final RecycleBin mRecycleBin = new RecycleBin();

	@Nullable
	private OnDateSelect mListener;

	@NonNull
	private ResizeManager mResizeManager;

	private boolean initialized;

	// 当前日历月份所属索引
	private int mPosition;
	// 当前日历所属月份数据
	private Month mCurrentMonth;

	private CalendarUnit mUnit;
	private Context mcontext;

	public CollapseCalendarView(Context context) {
		this(context, null);
	}

	public CollapseCalendarView(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.calendarViewStyle);
	}

	@SuppressLint("NewApi")
	public CollapseCalendarView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		mInflater = LayoutInflater.from(context);
		mcontext = context;
		mResizeManager = new ResizeManager(this);

		inflate(context, R.layout.calendar_layout, this);

		setOrientation(VERTICAL);
	}

	public void init(@NonNull CalendarManager manager, int position) {
		if (manager != null) {
			mManager = manager;
			mPosition = position;
			populateLayout();
			this.setListener(manager);
		}
	}

	public void reset() {
		// mCurrentMonth = (Month) mManager.getUnits(State.MONTH, mPosition);
		mUnit = null;
		populateLayout();
	}

	@Nullable
	public CalendarManager getManager() {
		return mManager;
	}

	@Override
	public void onClick(View v) {
		Log.d(TAG, "On click");
		if (mManager != null) {
			int id = v.getId();
			// if (id == R.id.prev) {
			// if (mManager.prev()) {
			// populateLayout();
			// }
			// } else if (id == R.id.next) {
			// Log.d(TAG, "next");
			// if (mManager.next()) {
			// Log.d(TAG, "populate");
			// populateLayout();
			// }
			// }

		}
	}

	@SuppressLint("WrongCall")
	@Override
	protected void dispatchDraw(@NonNull Canvas canvas) {
		mResizeManager.onDraw();

		super.dispatchDraw(canvas);
	}

	@Nullable
	public CalendarManager.State getState() {
		if (mManager != null) {
			return mManager.getState();
		} else {
			return null;
		}
	}

	public void setListener(@Nullable OnDateSelect listener) {
		mListener = listener;
	}

	public boolean onMyInterceptTouchEvent(MotionEvent ev) {
		return mResizeManager.onInterceptTouchEvent(ev);
	}

	public boolean onMyTouchEvent(@NonNull MotionEvent event) {
		super.onTouchEvent(event);
		return mResizeManager.onTouchEvent(event);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mWeeksView = (LinearLayout) findViewById(R.id.weeks);
		populateLayout();
	}

	private void populateDays() {

		if (!initialized) {
			CalendarManager manager = getManager();

			if (manager != null) {
				// Formatter formatter = manager.getFormatter();
				//
				// LinearLayout layout = (LinearLayout) findViewById(R.id.days);
				//
				// LocalDate date = LocalDate.now()
				// .withDayOfWeek(DateTimeConstants.MONDAY);
				// for (int i = 0; i < 7; i++) {
				// TextView textView = (TextView) layout.getChildAt(i);
				// textView.setText(formatter.getDayName(date));
				//
				// date = date.plusDays(1);
				// }
				initialized = true;
			}
		}

	}

	public void populateLayout() {

		if (mManager != null) {

			populateDays();
			if (mManager.getState() == CalendarManager.State.MONTH) {
				if (null == mUnit) {
					mUnit = mManager.getUnits(mManager.getState(), mPosition);
					mCurrentMonth = (Month) mUnit;
				} else if (mUnit instanceof Week) {
					mCurrentMonth = mManager.getMonth((Week) mUnit);
					mUnit = mCurrentMonth;
				}
				populateMonthLayout(mCurrentMonth);
			} else {
				if (null == mUnit) {
					mUnit = mManager.getUnits(mManager.getState(), mPosition);
					mCurrentMonth = mManager.getMonth((Week) mUnit);
				} else if (mUnit instanceof Month) {
					mCurrentMonth = (Month) mUnit;
					mUnit = mManager.getWeek((Month) mUnit);
				}
				populateWeekLayout((Week) mUnit);
			}
		}

	}

	private void populateMonthLayout(Month month) {

		List<Week> weeks = month.getWeeks();
		int cnt = weeks.size();
		for (int i = 0; i < cnt; i++) {
			WeekView weekView = getWeekView(i);
			populateWeekLayout(weeks.get(i), weekView);
			weekView.setVisibility(View.VISIBLE);
		}
		int childCnt = mWeeksView.getChildCount();
		if (cnt < childCnt) {
			for (int i = cnt; i < childCnt; i++) {
				cacheView(i);
			}
		}
	}

	private void populateMonthLayout(Month month, String num) {
		Log.i("1111111111", num + "");
		List<Week> weeks = month.getWeeks();
		int cnt = weeks.size();
		for (int i = 0; i < cnt; i++) {
			WeekView weekView = getWeekView(i);
			populateWeekLayout(weeks.get(i), weekView);
			weekView.setVisibility(View.VISIBLE);
		}
		int childCnt = mWeeksView.getChildCount();
		if (cnt < childCnt) {
			for (int i = cnt; i < childCnt; i++) {
				cacheView(i);
			}
		}
	}

	private void populateWeekLayout(Week week) {
		WeekView weekView = getWeekView(0);
		populateWeekLayout(week, weekView);

		int cnt = mWeeksView.getChildCount();
		if (cnt > 1) {
			for (int i = cnt - 1; i > 0; i--) {
				cacheView(i);
			}
		}
	}


	// 数据绑定，将week的数据绑定到weekview
	private void populateWeekLayout(@NonNull Week week,
			@NonNull WeekView weekView) {
		List<Day> days = week.getDays();
		for (int i = 0; i < 7; i++) {
			final Day day = days.get(i);
			// DayView dayView = (DayView) weekView.getChildAt(i);
			RelativeLayout layout = (RelativeLayout) weekView.getChildAt(i);
			DayView dayView = (DayView) layout.findViewById(R.id.tvDayView);
			DayView tvChinaDay = (DayView) layout.findViewById(R.id.tvChinaDay);
			boolean enables = day.isEnabled();
			TextView taskNumTV = (TextView) layout.findViewById(R.id.num);
			int undoNum=day.getUnDoNum();
			if (CalendarMainActivity.isShowALLTask) {
				if (day.getTaskNum() > 0 && enables) {
					taskNumTV.setText(day.getTaskNum() + "");
					taskNumTV.setVisibility(View.VISIBLE);
					taskNumTV
					.setBackgroundResource(undoNum > 0?R.drawable.round_orange:R.drawable.task_num_img);
				} else {
					taskNumTV.setVisibility(View.GONE);
				}
			} else {
				if (undoNum > 0 && enables) {
					taskNumTV.setText(day.getTaskNum() + "");
					taskNumTV.setVisibility(View.VISIBLE);
					taskNumTV
					.setBackgroundResource(undoNum > 0?R.drawable.round_orange:R.drawable.task_num_img);
				} else {
					taskNumTV.setVisibility(View.GONE);
				}
			}

			dayView.setText(day.getText());
			layout.setSelected(day.isSelected());
			dayView.setCurrent(day.isCurrent());
			tvChinaDay.setText(day.getChinaDate());
			tvChinaDay.setCurrent(day.isCurrent());

			dayView.setEnabled(enables);
			tvChinaDay.setEnabled(enables);
			if (enables) { // 解除点击限制，所有的都可以点击
				layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						LocalDate date = day.getDate();
						if (mListener != null) {
							mListener.onDateSelected(date);
						}
					}
				});
			} else {
				layout.setOnClickListener(null);
			}
		}

	}

	@NonNull
	public LinearLayout getWeeksView() {
		return mWeeksView;
	}

	@NonNull
	private WeekView getWeekView(int index) {
		int cnt = mWeeksView.getChildCount();

		if (cnt < index + 1) {
			for (int i = cnt; i < index + 1; i++) {
				View view = getView();
				mWeeksView.addView(view);
			}
		}

		return (WeekView) mWeeksView.getChildAt(index);
	}

	private View getView() {
		View view = mRecycleBin.recycleView();
		if (view == null) {
			view = mInflater.inflate(R.layout.week_layout, this, false);
		} else {
			view.setVisibility(View.VISIBLE);
		}
		return view;
	}

	private void cacheView(int index) {
		View view = mWeeksView.getChildAt(index);
		if (view != null) {
			mWeeksView.removeViewAt(index);
			mRecycleBin.addView(view);
		}
	}

	public LocalDate getSelectedDate() {
		return mManager.getSelectedDate(mUnit);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		mResizeManager.recycle();
	}

	private class RecycleBin {

		private final Queue<View> mViews = new LinkedList<View>();

		@Nullable
		public View recycleView() {
			return mViews.poll();
		}

		public void addView(@NonNull View view) {
			mViews.add(view);
		}

	}

	public Month getCurrentMonth() {
		return mCurrentMonth;
	}

	public LocalDate getMonthMaxDay() {
		return mCurrentMonth.getMaxDate();
	}

	public LocalDate getMonthMinDay() {
		return mCurrentMonth.getMinDate();
	}

	public interface OnDateSelect {

		public void onDateSelected(LocalDate date);
	}

}
