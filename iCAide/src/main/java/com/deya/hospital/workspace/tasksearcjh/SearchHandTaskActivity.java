package com.deya.hospital.workspace.tasksearcjh;

import android.os.Bundle;
import android.view.View;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/6/20
 */
public class SearchHandTaskActivity extends CheckSupervisorActivity{
    int jumpType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        jumpType=getIntent().getIntExtra("type",0);
        if(getIntent().hasExtra("type")){
            task_type=getIntent().getIntExtra("type",0)+"";
        }
        super.onCreate(savedInstanceState);

        if(getTypeStr().length==1){
            supervisor_type.setClickable(false);

        }
        supervisor_type.setText(getTypeStr()[0]+"");
        supervisor_type.setChecked(true);
    }

    public String [] typeStr1={"手卫生","消毒消耗量"};
    public  int  types1[]={1,15};


    public String [] typeStr3={"临床质控"};
    public  int  types3[]={17};


    public String [] typeStr2={"督查反馈"};
    public  int  types2[]={2};

    public String [] typeStr4={"环境督导"};
    public  int  types4[]={10};

    public String [] typeStr5={"医废管理"};
    public  int  types5[]={13};
    public  int  types6[]={21};
    public String [] typeStr6={"手卫生操作考核"};

    @Override
    public String[] getTypeStr() {
        switch (jumpType){
            case 1:
               return typeStr1;
            case 7:
                return typeStr2;
            case 17:
                return typeStr3;
            case 10:
                return typeStr4;
            case 13:
                return typeStr5;
            case 21:
                return typeStr6;
        }
        return typeStr;
    }

    @Override
    public int getTypes(int pos) {
        switch (jumpType){
            case 1:
                return types1[pos];
            case 2:
                return types2[pos];
            case 17:
                return types3[pos];
            case 10:
                return types4[pos];
            case 13:
                return types5[pos];
            case 21:
                return types6[pos];
        }
        return types[pos];
    }

    @Override
    public void reSetSearchKey() {
        departments = "";
        mission_time_type = "";
        start_date = "";
        end_date = "";
        searchType = 0;
        typeGvChooIndex = -1;

    }
}