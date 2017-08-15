package com.example.calendar.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.google.gson.Gson;

import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blaz Solar on 24/02/14.
 */
public class Week extends RangeUnit {

    @NonNull private final List<Day> mDays = new ArrayList<Day>(7);

    public Week(@NonNull LocalDate date, @NonNull LocalDate today, @Nullable LocalDate minDate,
                @Nullable LocalDate maxDate) {
        super(
                date.withDayOfWeek(1),
                date.withDayOfWeek(7),
                today,
                minDate,
                maxDate
        );

        build();
    }

    @Override
    public boolean hasNext() {
        LocalDate maxDate = getMaxDate();
        if (maxDate == null) {
            return true;
        } else {
            return maxDate.isAfter(mDays.get(6).getDate());
        }
    }

    @Override
    public boolean hasPrev() {
        LocalDate minDate = getMinDate();
        if (minDate == null) {
            return true;
        } else {
            return minDate.isBefore(mDays.get(0).getDate());
        }
    }

    @Override
    public boolean next() {
        if (hasNext()) {
            setFrom(getFrom().plusWeeks(1));
            setTo(getTo().plusWeeks(1));
            build();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean prev() {
        if (hasPrev()) {
            setFrom(getFrom().minusWeeks(1));
            setTo(getTo().minusWeeks(1));
            build();
            return true;
        } else {
            return false;
        }
    }

    @Override public int getType() {
        return TYPE_WEEK;
    }

    @Override
    public void deselect(@NonNull LocalDate date) {
        if (date != null && getFrom().compareTo(date) <= 0 &&
                getTo().compareTo(date) >= 0) {
            setSelected(false);

            for (Day day : mDays) {
                day.setSelected(false);
            }
        }
    }

    @Override
    public boolean select(@NonNull LocalDate date) {
        if (date != null && getFrom().compareTo(date) <= 0 &&
                getTo().compareTo(date) >= 0) {
            setSelected(true);

            for (Day day : mDays) {
                day.setSelected(day.getDate().isEqual(date));
            }
            return true;
        } else {
            return false;
        }
    }

    @NonNull
    public List<Day> getDays() {
        return mDays;
    }
    private List<TaskVo> monthDataList=new ArrayList<TaskVo>();
    private List<TaskVo> tasknumDataList=new ArrayList<TaskVo>();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Gson gson=new Gson();
    int i=0;
    @Override
    public void build() {
        LocalDate date = getFrom();
      
        for(; date.compareTo(getTo()) <= 0; date = date.plusDays(1)) {
        	i++;
        	
            Day day = new Day(date, date.equals(getToday()));
           
           DebugUtil.debug("compdate",i+"  date>>"+date+"  getToday()>>"+getToday()+"  isafter>>"+date.isAfter(getToday()));
            
            day.setAfter( date.isAfter(getToday()));
            
            day.setEnabled(isDayEnabled(date));
            try {
				String dateStr = df.parse("" + date).toString();
                if(null== MyAppliaction.mMonthTaskNums){
                    return;
                }
				Integer num = MyAppliaction.mMonthTaskNums.get(dateStr);
				Integer unDoNum = MyAppliaction.unDoTastNums.get(dateStr);
				Log.i("333333", num+"-----"+unDoNum);
				
				if(null != num) {
					day.setTaskNum(num);	
				} else {
					day.setTaskNum(-1);
				}
				if(null!= unDoNum) {
					day.setUnDoNum(unDoNum);	
				} else {
					day.setUnDoNum(0);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            mDays.add(day);
        }

    }

    private boolean isDayEnabled(@NonNull LocalDate date) {

        LocalDate minDate = getMinDate();
        if (minDate != null && date.isBefore(minDate)) {
            return false;
        }

        LocalDate maxDate = getMaxDate();
        if (maxDate != null && date.isAfter(maxDate)) {
            return false;
        }

        return true;
    }

    @Nullable
    @Override
    LocalDate getFirstDateOfCurrentMonth(@NonNull LocalDate currentMonth) {

        if (currentMonth != null) {
            int year = currentMonth.getYear();
            int month = currentMonth.getMonthOfYear();

            LocalDate date = getFrom();
            for (; date.compareTo(getTo()) <= 0; date = date.plusDays(1)) {
                int fromYear = date.getYear();
                int fromMonth = date.getMonthOfYear();

                if (year == fromYear && month == fromMonth) {
                    return date;
                }
            }
        }

        return null;
    }
}
