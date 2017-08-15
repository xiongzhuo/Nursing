package com.example.calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseListDialog;
import com.deya.hospital.base.BaseMutiDepartChooseActivity;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.DatePicDialog;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.TaskTypePopVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.workspacemain.TaskPopSetting;
import com.deya.hospital.workspace.workspacemain.TaskPopWindow;
import com.deya.hospital.workspace.workspacemain.TasktypeChooseListener;
import com.deya.hospital.workspace.workspacemain.TodayDynamicFragment;
import com.deya.notification.NotificationReceiver;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/12/14
 */
public class AddAfterTaskActivity extends BaseMutiDepartChooseActivity implements View.OnClickListener, TasktypeChooseListener<TaskTypePopVo> {
    TextView taskTypeTv, departTv, timeTv, remaindTv, recycleTv;
    DatePicDialog datePicDialog;
    private Map<String, ChildsVo> selectMap = new HashMap<>();
    int taskType;
    Button submitBtn;
    private String departNames = "";
    TaskPopWindow taskPopWindow;
    TaskPopSetting taskSettingWindow;
    int recycleState;
    CommonTopView topView;
    List<String> listRecyle, listremind;
    int remindState;

    ChooseDialog recyleDialog, remindDialog;

    private ArrayList<TaskTypePopVo> taskTypeList;
    int[] taskTypeImgs = {R.drawable.task_pop_handwash, R.drawable.task_pop_handantisepsis, R.drawable.task_pop_comsumption,
            R.drawable.task_pop_infection, R.drawable.task_pop_surpvisor, R.drawable.task_pop_surround, R.drawable.task_pop_risitance,
            R.drawable.task_pop_safe_injection, R.drawable.task_pop_threepips, R.drawable.task_pop_surgery_infection, R.drawable.wast, R.drawable.more_form, R.drawable.more_form};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_after_task);
        initView();
    }

    @Override
    protected void onChooseDepartList(Map<String, ChildsVo> map) {
        selectMap = map;
        Iterator<String> iter = selectMap.keySet().iterator();

        int i = 0;
        while (iter.hasNext()) {
            ChildsVo value = selectMap.get(iter.next());
            if (i < selectMap.size() - 1) {
                departNames += value.getName() + ",";
            } else {
                departNames += value.getName();
            }

            i++;

        }
        departTv.setText(departNames);
    }


    private void initView() {
        listRecyle = new ArrayList<>();
        listremind = new ArrayList<>();
        taskTypeList = new ArrayList<>();
        taskTypeTv = findView(R.id.taskTypeTv);
        departTv = findView(R.id.departTv);
        timeTv = findView(R.id.timeTv);
        if(getIntent().hasExtra("time")){
            String time=getIntent().getStringExtra("time");
            timeTv.setText(time);
        }
        remaindTv = findView(R.id.remaindTv);
        remaindTv.setText("不提醒");
        recycleTv = findView(R.id.recycleTv);
        recycleTv.setText("不重复");
        topView=findView(R.id.topView);
        topView.init(this);
        setLis(taskTypeTv, this);
        setLis(departTv, this);
        setLis(timeTv, this);
        setLis(remaindTv, this);
        setLis(recycleTv, this);
        initData();
        datePicDialog = new DatePicDialog(mcontext,
                new DatePicDialog.OnDatePopuClick() {

                    @Override
                    public void enter(String text) {
                        timeTv.setText(text);

                    }

                });

        recyleDialog = new ChooseDialog(mcontext, listRecyle, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recycleState = position;
                recycleTv.setText(listRecyle.get(position));
                recyleDialog.dismiss();

            }
        });
        remindDialog = new ChooseDialog(mcontext, listremind, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                remindState = position;
                remaindTv.setText(listremind.get(position));
                remindDialog.dismiss();

            }
        });
        submitBtn = (Button) this.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);
        taskPopWindow = new TaskPopWindow(mcontext, this);
        taskSettingWindow = new TaskPopSetting(mcontext, this);
        setTaskTypes();

    }

    private void initData() {
        listRecyle.add("不重复");
        listRecyle.add("每天重复");
        listRecyle.add("每周重复");
        listRecyle.add("每月重复");


        listremind.add("不提醒");
        listremind.add("当天提醒");
    }

    private void setTaskTypes() {
        try {
            List<TaskTypePopVo> popVoList = DataBaseHelper
                    .getDbUtilsInstance(mcontext)
                    .findAll(
                            Selector.from(TaskTypePopVo.class).orderBy("dbid"));
            if (popVoList == null || popVoList.size() <= 0) {
                // initTaskTypeList();
            } else {
                taskTypeList.clear();
                taskTypeList.addAll(popVoList);
                setTaskTypeList();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChooseType(int type) {
        this.taskType = type;
        TaskTypePopVo vo = map.get(type + "");
        taskTypeTv.setText(vo.getName().replaceAll("\n", ""));
    }

    @Override
    public void onChooseSetting() {
        taskSettingWindow.show();

    }

    @Override
    public void onSelectType(List list) {
        this.taskTypeList.clear();
        this.taskTypeList.addAll(list);
        try {
            DataBaseHelper
                    .getDbUtilsInstance(mcontext)
                    .updateAll(list);
        } catch (DbException e) {
            e.printStackTrace();
        }
        taskPopWindow.setData(taskTypeList);
    }

    Map<String, TaskTypePopVo> map;

    public void setTaskTypeList() {
        map = new HashMap<>();
        for (TaskTypePopVo vo : taskTypeList) {

            switch (vo.getId()) {
                case 1:
                    vo.setImgid(taskTypeImgs[0]);
                    vo.setName("WHO\n手卫生观察");
                    break;
                case 5:
                    vo.setImgid(taskTypeImgs[1]);
                    vo.setName("外科手消毒\n操作考核");
                    break;
                case 4:
                    vo.setImgid(taskTypeImgs[2]);
                    vo.setName("手消毒\n消耗量");
                    break;
                case 3:
                    vo.setImgid(taskTypeImgs[3]);
                    vo.setName("质量控制");
                    break;
                case 2:
                    vo.setImgid(taskTypeImgs[4]);
                    vo.setType(2);
                    vo.setName("通用督导本");
                    break;
                case 10:
                    vo.setImgid(taskTypeImgs[5]);
                    vo.setName("环境物表清洁");
                    break;
                case 8:
                    vo.setImgid(taskTypeImgs[6]);
                    vo.setName("多重耐药\n感染防控");
                    break;
                case 11:
                    vo.setImgid(taskTypeImgs[7]);
                    vo.setName("安全注射");
                    break;
                case 9:
                    vo.setImgid(taskTypeImgs[8]);
                    vo.setName("三管感染防控");
                    break;
                case 12:
                    vo.setImgid(taskTypeImgs[9]);
                    vo.setName("手术部位\n感染防控");
                    break;
                case 13:
                    vo.setImgid(taskTypeImgs[10]);
                    vo.setName("医疗废物");
                    break;
                case 14:
                    vo.setImgid(taskTypeImgs[11]);
                    vo.setName("更多");
                    break;

            }
            map.put(vo.getId() + "", vo);

        }

        List<TaskTypePopVo> popVoList = new ArrayList<>();
//        for(Map.Entry<String, TaskTypePopVo> entry:map.entrySet()){
//            popVoList.add(entry.getValue());
//        }
        if (map.containsKey("1")) {
            popVoList.add(map.get("1"));
        }
        if (map.containsKey("5")) {
            popVoList.add(map.get("5"));
        }
//        if (map.containsKey("4")) {
//            popVoList.add(map.get("4"));
//        }
        if (map.containsKey("8")) {
            popVoList.add(map.get("8"));
        }
        if (map.containsKey("9")) {
            popVoList.add(map.get("9"));
        }
        if (map.containsKey("12")) {
            popVoList.add(map.get("12"));
        }
        if (map.containsKey("3")) {
            popVoList.add(map.get("3"));
        }
        if (map.containsKey("10")) {
            popVoList.add(map.get("10"));
        }
        if (map.containsKey("11")) {
            popVoList.add(map.get("11"));
        }
        if (map.containsKey("2")) {
            popVoList.add(map.get("2"));
        }
        if (map.containsKey("13")) {
            popVoList.add(map.get("13"));
        }

        if (map.containsKey("3")) {
            TaskTypePopVo typePopVo = new TaskTypePopVo();
            typePopVo.setImgid(R.drawable.risk_assess);
            typePopVo.setId(3);
            typePopVo.setName("风险评估");
            popVoList.add(typePopVo);
        }
        if (map.containsKey("14")) {
            popVoList.add(map.get("14"));
        }

        taskPopWindow.setData(popVoList);
        taskSettingWindow.setData(popVoList);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.taskTypeTv:
                taskPopWindow.show();
                break;
            case R.id.departTv:
                departDialog.show();
                break;
            case R.id.timeTv:
                datePicDialog.show();
                break;
            case R.id.remaindTv:
                remindDialog.show();
                break;
            case R.id.recycleTv:
                recyleDialog.refesh();
                recyleDialog.show();

                break;
            case R.id.submitBtn:
                if(taskType<=0){
                    ToastUtil.showMessage("请选择任务类型");
                    return;
                }
                if(selectMap.size()<=0){
                    ToastUtil.showMessage("请选择科室");
                    return;
                }
                addTask();
                break;
        }
    }

    private void addTask() {
        List<TaskVo> taskList = new ArrayList<>();
        Iterator<String> iter = selectMap.keySet().iterator();

        int i = 0;
        while (iter.hasNext()) {
            ChildsVo value = selectMap.get(iter.next());
            TaskVo tv = new TaskVo();
            tv.setStatus(5);
            tv.setDepartmentName(value.getName());
            tv.setDepartment(value.getId());
            tv.setMission_time(timeTv.getText().toString());
            tv.setRemind_time(remaindTv.getText().toString());
            tv.setMobile(tools.getValue(Constants.MOBILE));
            tv.setType(taskType + "");
            tv.setRecycleState(recycleState);
            tv.setRemindState(remindState);
            tv.setIsAfterTask(1);
            taskList.add(tv);
        }
        setReminder(timeTv.getText().toString(),remindState);
        try {
            DataBaseHelper.getDbUtilsInstance(mcontext).saveAll(taskList);
            Intent intent = new Intent(TodayDynamicFragment.UPDATA_ACTION);
            sendBroadcast(intent);
            finish();

        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    private void setReminder(String time, int isSend) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date newDay=null;
        if(isSend==1){
          newDay = new DateTime(time).plusDays(1).toDate();
        }
        time=newDay+"";
        // get the AlarmManager instance
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        // create a PendingIntent that will perform a broadcast
        PendingIntent pi = PendingIntent.getBroadcast(mcontext, 0, new Intent(
                this, NotificationReceiver.class), 0);

        if (isSend==1) {
            // just use current time + 10s as the Alarm time.
            // schedule an alarm
            Date date = null;
            if (time == null) {
                return;
            }
            time=sdf2.format(newDay);
            try {
                date = sdf.parse(TaskUtils.getTaskMissionTime(time));
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            am.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
        } else {
            // cancel current alarm
            am.cancel(pi);
        }

    }
    class ChooseDialog extends BaseListDialog<String> {


        public ChooseDialog(Context context, List list, AdapterView.OnItemClickListener listener) {
            super(context, list, listener);
        }

        @Override
        protected void intUi() {
            right_txt.setVisibility(View.GONE);
            titleTv.setText("请选择");

        }

        @Override
        public void setListDta(ViewHolder viewHolder, int position) {
            viewHolder.listtext.setText(list.get(position));
        }
    }
}
