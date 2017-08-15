package com.deya.hospital.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.deya.acaide.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/12/5
 */
public class YearMonthDialog extends Dialog{
    private DatePicker datePicker;
    Context context;
    protected String date;
    TextView tv;
    private OnDatePopuClick OnPopuClick;
    public YearMonthDialog(Context context,OnDatePopuClick click) {
        super(context, R.style.SelectDialog3);
        this.context=context;
        OnPopuClick=click;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_diaog);
        this.findViewById(R.id.parentView).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        datePicker = (DatePicker) findViewById(R.id.dpPicker);
        Time curTime = new Time();
        curTime.setToNow();
        int year = curTime.year;
        int month = curTime.month;
        int day = curTime.monthDay;
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                // 获取一个日历对象，并初始化为当前选中的时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd");
//                Toast.makeText(context,
//                        format.format(calendar.getTime()), Toast.LENGTH_SHORT)
//                        .show();
                date=calendar.getTime()+"";
            }
        });
        Button yes=(Button) this.findViewById(R.id.yesBtn);
        yes.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM");

                date=format.format(calendar.getTime());
                OnPopuClick.enter(date);
                dismiss();

            }
        });
    }

    public interface OnDatePopuClick {
        public void enter(String text);
    }


    @Override
    public void show() {
        super.show();
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        lp.height = (int) (display.getHeight()); // 设置高度
        getWindow().setAttributes(lp);
        DatePicker dp=  datePicker;
        if(null!=dp){

          //  ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        }else{
            dp=  (DatePicker) findViewById(R.id.dpPicker);
        }

        ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
    }
}
