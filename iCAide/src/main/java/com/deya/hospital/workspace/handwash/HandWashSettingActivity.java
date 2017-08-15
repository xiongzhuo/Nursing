package com.deya.hospital.workspace.handwash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.SimpleSwitchButton;
/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/10/11
 */
public class HandWashSettingActivity extends BaseActivity {
    public static final String SHOW_FRAM_MORE = "show_fram_more";
    public static final String SHOW_FRAM_PERSON_INFO= "show_fram_person_info";
    CommonTopView topView;
    SimpleSwitchButton limitSwitch, timesSwitch, showResonsSwitch;
    LinearLayout limitlay, timelay;
    public static final String COLLTYPE="colltype";
    public static final String LIMT_TIME="limt_time";
    public static final String IS_WHO="is_who";
    public static final int RESULTCODE=0x019;
    EditText limitEdt,timeEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_wash_setting);
        initView();
    }

    private void initView() {
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.onbackClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,HandWashTasksActivity.class);
                if(limitSwitch.getCheckState()){
                    int count=Integer.parseInt(limitEdt.getText().toString())+10;
                    tools.putValue(COLLTYPE,count+"");
                }else{
                    tools.putValue(COLLTYPE,"0");
                    int time=Integer.parseInt(timeEdt.getText().toString());
                    tools.putValue(LIMT_TIME,time);
                }
                setResult(RESULTCODE,intent);
                finish();
            }
        });
        limitSwitch = (SimpleSwitchButton) this.findViewById(R.id.limitSwitch);
        timesSwitch = (SimpleSwitchButton) this.findViewById(R.id.timesSwitch);
        showResonsSwitch = (SimpleSwitchButton) this.findViewById(R.id.showResonsSwitch);
        limitEdt= (EditText) this.findViewById(R.id.limitEdt);
        timeEdt= (EditText) this.findViewById(R.id.timeEdt);
        String types = tools.getValue(HandWashSettingActivity.COLLTYPE);
        int count = 0;
        if(null!=types){
            count=Integer.parseInt(types)-10;
        }
        limitEdt.setText(count+"");
        limitlay = (LinearLayout) this.findViewById(R.id.limitlay);
        timelay = (LinearLayout) this.findViewById(R.id.timelay);
        setwitchViews(tools.getValue(COLLTYPE).equals("0")?1:0);
        limitSwitch.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
            @Override
            public void onCheckChange(boolean ischeck) {
                setwitchViews(ischeck ? 0 : 1);
            }
        });
        timesSwitch.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
            @Override
            public void onCheckChange(boolean ischeck) {
                setwitchViews(ischeck ? 1 : 0);


            }
        });
        showResonsSwitch.setCheck(tools.getValue_int(IS_WHO,1)==1?true:false);
        showResonsSwitch.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
            @Override
            public void onCheckChange(boolean ischeck) {
                tools.putValue(IS_WHO,ischeck?1:0);

            }
        });
        int  minute = tools.getValue_int(HandWashSettingActivity.LIMT_TIME);
        timeEdt.setText(minute+"");
        limitEdt.setSelection(limitEdt.getText().length());
        timeEdt.setSelection(timeEdt.getText().length());
    }

    public void setwitchViews(int state) {
        if (state == 0) {
            limitSwitch.setCheck(true);
            timesSwitch.setCheck(false);
            limitlay.setVisibility(View.VISIBLE);
            timelay.setVisibility(View.GONE);
            if(tools.getValue(COLLTYPE).equals("0")){
                limitEdt.setText("5");
            }

        } else {
            limitSwitch.setCheck(false);
            timesSwitch.setCheck(true);
            limitlay.setVisibility(View.GONE);
            timelay.setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
