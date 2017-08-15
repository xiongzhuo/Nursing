package com.example.calendar.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import com.deya.hospital.util.DebugUtil;
import com.example.calendar.CollapseCalendarView;
import com.example.calendar.CollapseCalendarView.OnDateSelect;

/**
 * Created by Blaz Solar on 27/02/14.
 */
public class CalendarManager implements OnDateSelect {
    
    @NonNull
    private State mState;
    // @NonNull
    // private RangeUnit mUnit;
    @NonNull
    private LocalDate mSelected;
    private int mSelectedIndexInMonth;// 所选的日期月序号（多少号）
    private int mSelectedIndexInWeek;// 所选的日期周序号（星期几）
    @NonNull
//    private final LocalDate mToday;
//    private int mTodayPosition;// 今天在ViewPagerAdapter数据中的索引
    
    private LocalDate mRelativeDate;//参考日期
    private int mRelativePosition;//参考日期的position
    
    public LocalDate getRelativeDate() {
        return mRelativeDate;
    }
    
    public void setRelativeDate(LocalDate mReletiveDate) {
        this.mRelativeDate = mReletiveDate;
    }
    
    public int getRelativePosition() {
        return mRelativePosition;
    }
    
    public void setRelativePosition(int mReletivePosition) {
        this.mRelativePosition = mReletivePosition;
    }

//    public LocalDate getToday() {
//        return mToday;
//    }
//    
//    public int getTodayPosition() {
//        return mTodayPosition;
//    }
//    
//    public void setTodayPosition(int mTodayPosition) {
//        this.mTodayPosition = mTodayPosition;
//    }
    
    @Nullable
    private LocalDate mMinDate;
    @Nullable
    private LocalDate mMaxDate;
    @NonNull
    private Formatter formatter;
    
    // private LocalDate mActiveMonth;
    
    private OnStateChange mStateChangedListener;
    private OnDateSelect mOnDateSelect;
    
    public void setOnDateSelect(OnDateSelect mOnDateSelect) {
        this.mOnDateSelect = mOnDateSelect;
    }
    
    public void setStateChangedListener(OnStateChange mStateChangedListener) {
        this.mStateChangedListener = mStateChangedListener;
        setState(mState);
    }
    
    public CalendarManager(@NonNull LocalDate selected, @NonNull State state,
            @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        this(selected, state, minDate, maxDate, null);
    }
    
    public CalendarManager(@NonNull LocalDate selected, @NonNull State state,
            @Nullable LocalDate minDate, @Nullable LocalDate maxDate,
            @Nullable Formatter formatter) {
//        mToday = LocalDate.now();
        mRelativeDate = LocalDate.now();
        setState(state);
        if (formatter == null) {
            this.formatter = new DefaultFormatter();
        } else {
            this.formatter = formatter;
        }
        
        init(selected, minDate, maxDate);
    }
    
    public boolean selectDay(@NonNull LocalDate date) {
        if (!mSelected.isEqual(date)) {
            // mUnit.deselect(mSelected);
            mSelected = date;
            // mUnit.select(mSelected);
            mSelectedIndexInMonth = date.getDayOfMonth();
            mSelectedIndexInWeek = date.getDayOfWeek();
            
            // if (mState == State.WEEK) {
            // setActiveMonth(date);
            // }
            return true;
        } else {
            return false;
        }
    }
    public void  setDayTaskNum(@NonNull LocalDate date) {
      
    	
    	
    }
    @NonNull
    public LocalDate getSelectedDay() {
        return mSelected;
    }
    
    // @NonNull
    // public String getHeaderText() {
    // return formatter.getHeaderText(mUnit.getType(), mUnit.getFrom(),
    // mUnit.getTo());
    // }
    
    @NonNull
    public String getHeaderText(int position) {
        CalendarUnit unit = this.getUnits(mState, position);
        return formatter.getHeaderText(unit.getType(), unit.getFrom(),
                unit.getTo());
    }
    
    public boolean hasNext() {
        return false;
        // return mUnit.hasNext();
    }
    
    public boolean hasPrev() {
        return false;
        // return mUnit.hasPrev();
    }
    
    // private void changeUnitState() {
    // if (null != mStateChangedListener) {
    // mStateChangedListener.OnPageChanged(mState, 1);
    // }
    // }
    //
    // public boolean next() {
    //
    // boolean next = mUnit.next();
    // mUnit.select(mSelected);
    //
    // setActiveMonth(mUnit.getFrom());
    // changeUnitState();
    // return next;
    // }
    //
    // public boolean prev() {
    //
    // boolean prev = mUnit.prev();
    // mUnit.select(mSelected);
    //
    // setActiveMonth(mUnit.getTo());
    // changeUnitState();
    // return prev;
    // }
    
    /**
     *
     * @return index of month to focus to
     */
    public void toggleView(CollapseCalendarView view) {
        if (mState == State.MONTH) {
            toggleFromMonth(view);
        } else {
            toggleFromWeek(view);
        }
    }
    
    @NonNull
    public State getState() {
        return mState;
    }
    
    private void setState(State state) {
        mState = state;
        // if (null != this.mStateChangedListener) {
        // mStateChangedListener.OnStateChanged(mState);
        // }
    }
    
    // public CalendarUnit getUnits() {
    // return mUnit;
    // }
    
    public CalendarUnit getUnits(State state, int position) {
        int offset = position - mRelativePosition;
        CalendarUnit unit = null;
        if (state == State.MONTH) {
            LocalDate date = mRelativeDate.plusMonths(offset);
            int index = mSelectedIndexInMonth;
            if (index > date.dayOfMonth().getMaximumValue()) {
                index = date.dayOfMonth().getMaximumValue();
            }
            LocalDate selected = date.withDayOfMonth(index);
            
            unit = new Month(selected, mRelativeDate, date.withDayOfMonth(1),
                    date.withDayOfMonth(date.dayOfMonth().getMaximumValue()));
            unit.select(selected);
        } else {
            LocalDate date = mRelativeDate.plusWeeks(offset);
            int index = mSelectedIndexInWeek;
            if (index > date.dayOfWeek().getMaximumValue()) {
                index = date.dayOfWeek().getMaximumValue();
            }
            LocalDate selected = date.withDayOfWeek(index);
            unit = new Week(selected, mRelativeDate, mMinDate, mMaxDate);
            unit.select(selected);
        }
        return unit;
    }
    
    public Month getMonth(Week week) {
    	LocalDate from = week.getFrom();
    	int index = mSelectedIndexInWeek;
        if (index > from.dayOfWeek().getMaximumValue()) {
            index = from.dayOfWeek().getMaximumValue();
        }
    	LocalDate selected = from.withDayOfWeek(mSelectedIndexInWeek);
    	Month month = new Month(selected, mRelativeDate, selected.withDayOfMonth(1),
    			selected.withDayOfMonth(selected.dayOfMonth().getMaximumValue()));
    	month.select(selected);
    	return month;
    }
    
    public Week getWeek(Month month) {
    	LocalDate date = month.getFrom();
        int index = mSelectedIndexInMonth;
        if (index > date.dayOfMonth().getMaximumValue()) {
            index = date.dayOfMonth().getMaximumValue();
        }
        LocalDate selected = date.withDayOfMonth(index);
        
        Week week = new Week(selected, mRelativeDate, selected.withDayOfMonth(1),
    			selected.withDayOfMonth(selected.dayOfMonth().getMaximumValue()));
        week.select(selected);
    	return week;
    }
    
    private void toggleFromMonth(CollapseCalendarView view) {
        Month month = view.getCurrentMonth();
        
        LocalDate date = month.getFrom();
        int index = mSelectedIndexInMonth;
        if (index > date.dayOfMonth().getMaximumValue()) {
            index = date.dayOfMonth().getMaximumValue();
        }
        LocalDate selected = date.withDayOfMonth(index);
        toggleFromMonth(month, selected);
    }
    
    void toggleToWeek(CollapseCalendarView view, int weekInMonth) {
    	
        Month month = view.getCurrentMonth();
        LocalDate selected = month.getFrom().plusDays(weekInMonth * 7);
        toggleFromMonth(month, selected);
    }
    
    private void toggleFromMonth(Month month, LocalDate selected) {
        if (month.isInView(selected)) {
            month.select(selected);
        } else {
            month.select(month.getFrom());
        }
        
        DebugUtil.debug("pull","toggleFromMonth>> "+selected.toString());
        
        setState(State.WEEK);
    }
    
    private void toggleFromWeek(CollapseCalendarView view) {
        Month month = view.getCurrentMonth();
        LocalDate date = month.getFrom();
        int index = mSelectedIndexInMonth;
        if (index > date.dayOfMonth().getMaximumValue()) {
            index = date.dayOfMonth().getMaximumValue();
        }
        LocalDate selected = date.withDayOfMonth(index);
        
        if (month.isInView(selected)) {
            month.select(selected);
        } else {
            month.select(month.getFrom());
        }
        setState(State.MONTH);
    }
    
    public void callStateChanged() {
        if (null != this.mStateChangedListener) {
            mStateChangedListener.OnStateChanged(mState);
        }
    }
    
    /**
     * 获取选中日在月份中的周数
     * 
     * @param month 月份数据
     * @return 第几周
     */
    public int getSelectedWeekOfMonth(Month month) {
        LocalDate date = month.getFrom();
        int index = mSelectedIndexInMonth;
        if (index > date.dayOfMonth().getMaximumValue()) {
            index = date.dayOfMonth().getMaximumValue();
        }
        LocalDate selected = date.withDayOfMonth(index);
        if (month.isInView(selected)) {
            if (month.isIn(selected)) {
                return month.getWeekInMonth(selected);
            } else if (month.getFrom().isAfter(selected)) {
                return month.getWeekInMonth(month.getFrom());
            } else {
                return month.getWeekInMonth(month.getTo());
            }
        } else {
            return month.getFirstWeek(month.getFrom());
        }
    }
    
    public LocalDate getSelectedDateOfMonth(Month month) {
        LocalDate date = month.getFrom();
        int index = mSelectedIndexInMonth;
        if (index > date.dayOfMonth().getMaximumValue()) {
            index = date.dayOfMonth().getMaximumValue();
        }
        return date.withDayOfMonth(index);
    }
    
    public LocalDate getSelectedDateOfWeek(Week week) {
        LocalDate date = week.getFrom();
        int index = mSelectedIndexInWeek;
        if (index > date.dayOfWeek().getMaximumValue()) {
            index = date.dayOfWeek().getMaximumValue();
        }
        return date.withDayOfWeek(index);
    }
    
    public LocalDate getSelectedDate(CalendarUnit unit) {
    	if(unit instanceof Month) {
    		return getSelectedDateOfMonth((Month) unit);
    	} else {
    		return getSelectedDateOfWeek((Week) unit);
    	}
    }
    
    public void init(@NonNull LocalDate date, @Nullable LocalDate minDate,
            @Nullable LocalDate maxDate) {
        mSelected = date;
        // setActiveMonth(date);
        mMinDate = minDate;
        mMaxDate = maxDate;
        mSelectedIndexInMonth = mSelected.getDayOfMonth();
        mSelectedIndexInWeek = mSelected.getDayOfWeek();
        // init();
    }
    
    @Nullable
    public LocalDate getMinDate() {
        return mMinDate;
    }
    
    public void setMinDate(@Nullable LocalDate minDate) {
        mMinDate = minDate;
    }
    
    @Nullable
    public LocalDate getMaxDate() {
        return mMaxDate;
    }
    
    public void setMaxDate(@Nullable LocalDate maxDate) {
        mMaxDate = maxDate;
    }
    
    public int getMonthsNum() {
        return (mMaxDate.getYear() - mMinDate.getYear() - 1) * 12;
    }
    
    public int getWeeksNum() {
        return (mMaxDate.getYear() - mMinDate.getYear() - 1) * 52;
    }
    
    @NonNull
    public Formatter getFormatter() {
        return formatter;
    }
    
    public enum State {
        MONTH, WEEK
    }
    
    public interface OnStateChange {
        
        public void OnStateChanged(State state);
    }
    
    @Override
    public void onDateSelected(LocalDate date) {
        
        if (selectDay(date)&&null != mOnDateSelect) {
            mOnDateSelect.onDateSelected(date);
        }
    }
}
