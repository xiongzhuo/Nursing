package com.deya.hospital.workspace.workspacemain;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.adapter.TaskLisAdapter;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.base.NetBroadCastReciver;
import com.deya.hospital.base.TabBaseFragment;
import com.deya.hospital.base.img.TipsDialogRigister;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.consumption.ConsumptionFormActivity;
import com.deya.hospital.descover.GuidListActivity;
import com.deya.hospital.form.FormListActivity;
import com.deya.hospital.form.handantisepsis.HandDisinfectionPrivewActivity;
import com.deya.hospital.form.xy.XyFormListActivity;
import com.deya.hospital.login.LoginActivity;
import com.deya.hospital.task.Tasker;
import com.deya.hospital.task.Worker;
import com.deya.hospital.task.notify.WorkerFinishedNotify;
import com.deya.hospital.task.uploader.BaseUploader;
import com.deya.hospital.task.uploader.FailLogUpload;
import com.deya.hospital.util.ComomDialog;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.HorizontalListView;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.DynamicVo;
import com.deya.hospital.vo.TaskTypePopVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.widget.popu.TipsDialog;
import com.deya.hospital.workspace.BootomSelectDialog;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.WorkSpaceFragment;
import com.deya.hospital.workspace.environment.EnvironmentMainActivity;
import com.deya.hospital.workspace.handwash.HandWashTasksActivity;
import com.deya.hospital.workspace.priviewbase.FormCommitSucTipsActivity;
import com.deya.hospital.workspace.priviewbase.ResistantListActivity;
import com.deya.hospital.workspace.safeinjection.safeInjectionActivity;
import com.deya.hospital.workspace.site_infection.SiteInfectionPriviewAcitivty;
import com.deya.hospital.workspace.stastic.StasticTypeActivity;
import com.deya.hospital.workspace.supervisorquestion.SupQuestionListActivity;
import com.deya.hospital.workspace.supervisorquestion.SupervisorQueCreatActivity;
import com.deya.hospital.workspace.tasksearcjh.CheckSupervisorActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.dialog.ECAlertDialog;
import com.im.sdk.dy.common.utils.ECNotificationManager;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.im.sdk.dy.ui.TaskTipsActivity;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.yuntongxun.ecsdk.ECDevice;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodayDynamicFragment extends TabBaseFragment implements
        OnClickListener, WorkerFinishedNotify, TasktypeChooseListener<TaskTypePopVo> {

    public static final String UPDATA_ACTION = "updateList";
    public static final String SUMBMIT_ACCTION = "sumbmit_acction";//提交任务
    public static final String GETMODULE_SUC = "getmodule_suc";//提交任务
    public static final int DELETE_SUCESS = 0x20060;
    public static final int DELETE_FAIL = 0x20061;// 是否关闭软文
    public static final int ADD_SUPQUESTION = 0x118;// 是否关闭软文
    public static final int NEW_TASK_WORK = 0x2131;

    private LayoutInflater inflaterHead;
    private Gson gson = new Gson();
    private Tools tools;
    private MyBrodcastReceiver brodcast;
    private PullToRefreshListView planLv;
    public static LocalDate currentday;
    // 上次的月份
    TaskLisAdapter adapter;
    RelativeLayout empertyView;
    Context mcontext;
    private LocalDate localToday;
    String monthMinDate = "";
    String monthMaxDate = "";
    String monthMaxDate2 = "";
    View headView;
    Worker worker;
    LinearLayout moreLay, improveLay;
    ImageView hasQuestionImg;
    private HorizontalListView searchGv;
    private TodayDynamicAdapter hotAdapter;
    TaskPopWindow taskPopWindow;
    TaskPopSetting taskSettingWindow;
    NetworkReciever networkReciever;
    List<DynamicVo.ListBean> hotlist;
    private ArrayList<TaskTypePopVo> taskTypeList;
    int[] taskTypeImgs = {R.drawable.task_pop_handwash, R.drawable.task_pop_handantisepsis, R.drawable.task_pop_comsumption,
            R.drawable.task_pop_infection, R.drawable.task_pop_surpvisor, R.drawable.task_pop_surround, R.drawable.task_pop_risitance,
            R.drawable.task_pop_safe_injection, R.drawable.task_pop_threepips, R.drawable.task_pop_surgery_infection,R.drawable.wast,R.drawable.more_form,R.drawable.more_form};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_today_dynamic, container,
                    false);
            mcontext = getActivity();
            inflaterHead = LayoutInflater.from(getActivity());
            tools = new Tools(getActivity(), Constants.AC);
            hotlist = new ArrayList<>();
            taskTypeList = new ArrayList<>();

            initMyHandler();
            if (null == worker) {
                worker = new Worker(this);
            }

            initView();

            getAllLoacalTask();

        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }

        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setRequstDate();
        isSignedSucUser(tools);
    }

    private void setRequstDate() {
        localToday = LocalDate.now();


        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        monthMinDate=dateFormater.format(cal.getTime()) + "";
        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        monthMaxDate=dateFormater.format(cal.getTime())+  "  " + "23:59";
        monthMaxDate2 = dateFormater.format(cal.getTime()) + "-31";

    }

    public void getAllLoacalTask() {
        setCurrentData();
    }

    private void initView() {
        adapter = new TaskLisAdapter(getActivity(), monthTaskList);
        planLv = (PullToRefreshListView) findViewById(R.id.planlist);


        headView = inflaterHead.inflate(R.layout.today_dynamic_headview_item, null);

        searchGv = (HorizontalListView) headView.findViewById(R.id.searchGv);


        hotAdapter = new TodayDynamicAdapter(mcontext, hotlist, 0);
        searchGv.setAdapter(hotAdapter);
        searchGv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DynamicVo.ListBean bean = hotlist.get(position);
                Intent intent = new Intent(getActivity(), WorkStasticActivity.class);
                bean.setDate(localToday + "");
                switch (bean.getType()) {
                    // 1：检出多耐、2：三管监测、3：住院人数、4：疑似感染人数、5：任务数、6：工作提醒数
                    case 1:
                        bean.setCode("DN");
                    case 3:
                    case 4:
                        intent.putExtra("data", bean);
                        break;
                    case 2:
                        bean.setCode("SG");
                        intent.putExtra("data", bean);
                        break;
                    case 5:
                        intent.setClass(getActivity(), CheckSupervisorActivity.class);
                        intent.putExtra("data", bean);
                        break;
                    case 6:
                        intent.setClass(getActivity(), TaskTipsActivity.class);
                        intent.putExtra("data", bean);
                        break;

                }
                startActivity(intent);


            }
        });
        planLv.getRefreshableView().addHeaderView(headView);

        headView.findViewById(R.id.checkTaskLay).setOnClickListener(this);
        moreLay = (LinearLayout) headView.findViewById(R.id.moreLay);
        moreLay.setOnClickListener(this);
        headView.findViewById(R.id.reportLay).setOnClickListener(this);

        improveLay = (LinearLayout) headView.findViewById(R.id.improveLay);
        improveLay.setOnClickListener(this);
        headView.findViewById(R.id.addTaskLay).setOnClickListener(this);

        initListView();
        if (!NetWorkUtils.isConnect(getActivity())) {
            getAllLoacalTask();
        }
        initData();
        setRequstDate();
        getDynamicData();
        getTaskList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            worker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            worker.execute();
        }

        taskPopWindow = new TaskPopWindow(getActivity(), this);
        taskSettingWindow = new TaskPopSetting(getActivity(), this);
        setTaskTypes();
        networkReciever = new NetworkReciever();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        getActivity().registerReceiver(networkReciever, intentFilter);

    }

    private void setTaskTypes() {
        try {
            List<TaskTypePopVo> popVoList = DataBaseHelper
                    .getDbUtilsInstance(getActivity())
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
        TaskUtils.addTask(getActivity(), type, localToday + "");
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
                    .getDbUtilsInstance(getActivity())
                    .updateAll(list);
        } catch (DbException e) {
            e.printStackTrace();
        }
        taskPopWindow.setData(taskTypeList);
    }
    public void setTaskTypeList() {
        Map<String, TaskTypePopVo> map = new HashMap<>();
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
        if (map.containsKey("4")) {
            popVoList.add(map.get("4"));
        }
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
            TaskTypePopVo typePopVo=new TaskTypePopVo();
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

    public void OnStartActivity(Class<?> T) {
        Intent inten = new Intent(getActivity(), T);
        startActivity(inten);
    }

    private void initData() {

        registerBroadcast();

    }

    @Override
    public void onDestroy() {
        if (null != brodcast) {
            getActivity().unregisterReceiver(brodcast);
        }
        if (null != networkReciever) {
            getActivity().unregisterReceiver(networkReciever);
        }
        if (null != loacalWork) {
            loacalWork.cancel(true);
            loacalWork = null;
        }
        if (null != serviceTask) {
            serviceTask.cancel(true);
            serviceTask = null;
        }
        if (null != worker) {
            worker.cancel(true);
        }
        dismisAlldialog();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == CheckSupervisorActivity.SEARCH_TASK) {
            //    getTaskList();
        }


    }

    public boolean compareDate(Date date, Date date2, Date currentday) {
        Log.i("watchActivity", (currentday.compareTo(date) >= 0) + "");
        return currentday.compareTo(date) >= 0
                && currentday.compareTo(date2) <= 0;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.checkTaskLay:
                OnStartActivity(CheckSupervisorActivity.class);
                break;
            case R.id.handHhygieneLay:
                handDilog.show();
                break;
            case R.id.infectionLay:
                addTask(3, false);
                break;
            case R.id.moreLay:
                Intent it = new Intent(getActivity(),
                        GuidListActivity.class);
                it.putExtra("type", "3");
                startActivity(it);
                break;
            case R.id.improveLay:
                Intent it2 = new Intent(getActivity(),
                        SupQuestionListActivity.class);
                it2.putExtra("type", "3");
                startActivity(it2);
                break;
            case R.id.reportLay:
                OnStartActivity(StasticTypeActivity.class);
                break;
            case R.id.addTaskLay:
                taskPopWindow.show();
                break;
            default:
                break;
        }
    }

    BootomSelectDialog handDilog, superviousDialog;


    /**
     * 防止窗体泄漏
     */
    public void dismisAlldialog() {
        if (addScoredialog != null && addScoredialog.isShowing()) {
            addScoredialog.dismiss();
        }
        if (null != taskPopWindow && taskPopWindow.isShowing()) {
            taskPopWindow.dismiss();
        }
        if (null != taskSettingWindow && taskSettingWindow.isShowing()) {
            taskSettingWindow.dismiss();
        }
    }

    public void addTask(int type, boolean iswho) {
        Intent it = new Intent();
        it.putExtra("time", localToday + "");
        if (type != 4) {
            it.putExtra("type", type + "");
        }

//        if (iswho) {
//            it.putExtra("isWho", "1");
//        }
        it.putExtra("isAfter", false);
        switch (type) {
            case 1:
                it.setClass(getActivity(), HandWashTasksActivity.class);
                startActivity(it);
                break;
            case 2:
                it.setClass(getActivity(), SupervisorQueCreatActivity.class);
                startActivityForResult(it, TodayDynamicFragment.ADD_SUPQUESTION);
                break;
            case 4:
                it.setClass(getActivity(), ConsumptionFormActivity.class);
                TaskVo tv = new TaskVo();
                tv.setMission_time(localToday + "");
                tv.setStatus(2);
                it.putExtra("data", tv);
                startActivity(it);
                break;
            case 3:
                it.setClass(getActivity(), FormListActivity.class);
                it.putExtra("time", localToday + "");
                startActivity(it);
                break;
            case 5:
                it.setClass(getActivity(), HandDisinfectionPrivewActivity.class);
                startActivity(it);
                break;

            case 6://湘雅模版
                it.setClass(getActivity(), XyFormListActivity.class);

                it.putExtra("time", localToday + "");
                startActivity(it);
                break;
            case 8://多耐
                it.setClass(getActivity(), ResistantListActivity.class);
                it.putExtra("code", "DN");
                startActivity(it);
                break;

            case 9://三管
                it.setClass(getActivity(), ResistantListActivity.class);
                it.putExtra("code", "SG");
                startActivity(it);
                break;
            case 10:
                it.setClass(getActivity(), EnvironmentMainActivity.class);
                startActivity(it);
                break;
            case 11:
                it.setClass(getActivity(), safeInjectionActivity.class);
                startActivity(it);
                break;
            case 12:
                it.setClass(getActivity(), SiteInfectionPriviewAcitivty.class);
                startActivity(it);
                break;
            default:
                ToastUtils.showToast(getActivity(), "正在建设中！");
                break;
        }


    }


    private void registerBroadcast() {
        brodcast = new MyBrodcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("upload", "接收到了0");
                if (intent.getAction().equals(SUMBMIT_ACCTION)) {
                    Log.i("upload", "接收到了0");
                    setCurrentData();
                } else if (intent.getAction().equals(UPDATA_ACTION)) {
                    Log.i("upload", "接收到了0");
                    setCurrentData();
                }
                else if (intent.getAction().equals(SDKCoreHelper.ACTION_KICK_OFF)) {
                    if (intent.hasExtra("kickoffText")) {
                        handlerKickOff(intent.getStringExtra("kickoffText"));
                    }

                } else if (intent.getAction().equals(GETMODULE_SUC)) {
                    setTaskTypes();
                } else if (intent.getAction().equals(Constants.AUTHENT_LOSE)) {
                    ToastUtils.showToast(mcontext, "登录失效请重新登录");
                    resetCasche();
                    getActivity().finish();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATA_ACTION);
        intentFilter.addAction(Constants.AUTHENT_LOSE);
        intentFilter.addAction(GETMODULE_SUC);
        getActivity().registerReceiver(brodcast, intentFilter);
    }

    public void handlerKickOff(String kickoffText) {
        if (getActivity().isFinishing()) {
            return;
        }
        ECAlertDialog buildAlert = ECAlertDialog.buildAlert(getActivity(), kickoffText,
                getString(R.string.dialog_btn_confim),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ECNotificationManager.getInstance()
                                .forceCancelNotification();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        resetCasche();
                    }
                });
        buildAlert.setTitle("登录异常");
        buildAlert.setCanceledOnTouchOutside(false);
        buildAlert.setCancelable(false);
        buildAlert.show();
    }

    public void resetCasche() {
        try {
            tools.putValue(Constants.AUTHENT, "");
            tools.putValue(Constants.NAME, "");
            tools.putValue(Constants.HOSPITAL_NAME, "");
            tools.putValue(Constants.AGE, "");
            tools.putValue(Constants.SEX, "");
            tools.putValue(Constants.STATE, "");
            tools.putValue(Constants.MOBILE, "");
            tools.putValue(Constants.HEAD_PIC, "");
            tools.putValue(Constants.EMAIL, "");
            tools.putValue(Constants.JOB, "");
            tools.putValue(Constants.USER_ID, "");

            if (ECDevice.isInitialized())
                try {
                    ECDevice.unInitial();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            SDKCoreHelper.logout(false);

            CCPAppManager.setClientUser(null);

            MainActivity.mInit = false;

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void initListView() {
        planLv.setAdapter(adapter);
        planLv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        planLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getDynamicData();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        planLv.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position < 2) {
                    return;
                }
                final TaskVo tv = monthTaskList.get(position - 2);
                if (null == tv) {
                    return;
                }

                TipsDialogRigister dialog = new TipsDialogRigister(mcontext, new MyDialogInterface() {
                    @Override
                    public void onItemSelect(int postion) {
                    }

                    @Override
                    public void onEnter() {
                        if (tv.getStatus() == 1) {//上传中的任务 去编辑
                            tv.setStatus(2);
                            TaskUtils.onUpdateTaskById(tv);
                            TaskUtils.onStaractivity(getActivity(), tv, monthTaskList);
                            adapter.notifyDataSetChanged();
                        } else {//上传失败的任务 去上报日志
                            showprocessdialog();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (FailLogUpload.getUpload().upload(TaskUtils.gson.toJson(tv))) {
                                        dismissdialog();
                                    } else {
                                        dismissdialog();
                                        ToastUtil.showMessage("日志上传失败");

                                    }
                                }
                            }).start();


                        }
                    }

                    @Override
                    public void onCancle() {
                        if (tv.getStatus() == 1) {
                            if (NetWorkUtils.isConnect(mcontext)) {
                                if (!NetWorkUtils.isWifiState(MyAppliaction.getContext())) {

                                    Intent intent = new Intent(getActivity(), FormCommitSucTipsActivity.class);
                                    intent.putExtra("data", tv);
                                    intent.putExtra("commit_status", FormCommitSucTipsActivity.ONLY_WIFI);
                                    startActivity(intent);
                                } else {
                                    showprocessdialog();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int type = Integer.parseInt(tv.getType());
                                            Intent intent = new Intent(getActivity(), FormCommitSucTipsActivity.class);
                                            if (!BaseUploader.getUploader(tv).Upload(tv)) {
                                                tv.setFailNum(tv.getFailNum() + 1);
                                                if (tv.getFailNum() >= 5) {
                                                    tv.setStatus(4);
                                                }
                                                intent.putExtra("commit_status", FormCommitSucTipsActivity.UPLOAD_FIAL);
                                            } else {
                                                intent.putExtra("commit_status", FormCommitSucTipsActivity.UPLOAD_SUC);
                                            }
                                            dismissdialog();
                                            TaskUtils.onUpdateTaskById(tv);
                                            intent.putExtra("data", tv);
                                            startActivity(intent);
                                        }
                                    }).start();
                                }
                            } else {
                                Intent intent = new Intent(getActivity(), FormCommitSucTipsActivity.class);
                                intent.putExtra("data", tv);
                                intent.putExtra("commit_status", FormCommitSucTipsActivity.NET_WORK_DISCONECT);
                                startActivity(intent);
                                return;
                            }
                        } else {
                            monthTaskList.remove(tv);
                            Tasker.deleteTask(tv);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                if (tv.getStatus() == 1) {
                    dialog.show();
                    dialog.setContent("数据未提交,是否现在提交？");
                    dialog.setButton("去编辑");
                    dialog.setCancleButton("提交");
                    return;
                } else if (tv.getStatus() == 4) {
                    dialog.show();
                    dialog.setContent("非常抱歉，本条数据出现异常,请上报日志以便我们尽快解决这个问题！");
                    dialog.setButton("上报");
                    dialog.setCancleButton("删除");
                    return;
                }
                TaskUtils.onStaractivity(getActivity(), tv, monthTaskList);
            }

        });
        planLv.getRefreshableView().setOnItemLongClickListener(new OnItemLongClickListener() {
            Dialog deletDialog;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {

                deletDialog = new ComomDialog(getActivity(), "确认删除此任务？",
                        R.style.SelectDialog, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doDeleteTask(position - 2);
                        deletDialog.dismiss();
                    }
                });
                deletDialog.show();
                return true;
            }
        });
        empertyView = (RelativeLayout) rootView.findViewById(R.id.empertyView);

    }

    void doDeleteTask(int index) {
        if (index >= monthTaskList.size()||index<0) {
            return;
        }
        TaskVo tv = monthTaskList.get(index);
        if (tv.getStatus() == 1) {
            ToastUtils.showToast(getActivity(), "正在同步的任务暂时无法删除");
            return;
        } else if (tv.getStatus() != 0) {
            monthTaskList.remove(tv);
            Tasker.deleteTask(tv);
            adapter.notifyDataSetChanged();
        } else {
            for (TaskVo vo : taskList) {
                if (vo.getTask_id() == tv.getTask_id()) {
                    Log.i("taskId", tv.getTask_id() + "");
                    deletTasks(vo.getTask_id(), tv.getType());
                    break;
                }
            }

        }
    }

    public int dp2Px(int dp) {
        final float scale = getActivity().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + (int) 0.5f);
    }

    // 获取服务器任务数据
    // 获取同步任务列表

    public static List<TaskVo> taskList = new ArrayList<TaskVo>();

    public void getDynamicData() {
        String dynamicDataStr = SharedPreferencesUtil.getString(getActivity(), "DynamicData", "");
        try {
            JSONObject json = new JSONObject(dynamicDataStr);
            initDynamicData(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
                GET_DYNAMICDATA_SUC, GET_DYNAMICDATA_FAIL, job, "count/getDataByDay");

    }

    public void getTaskList() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("start_date", monthMinDate);
            job.put("end_date", monthMaxDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
                TASKLIST_SUCESS, TASKLIST_FAILE, job, "task/syncTaskInfos");

    }

    public static final int ADD_PRITRUE_CODE = 9009;
    // 压缩图片的msg的what
    private static final int TASKLIST_SUCESS = 0x20010;
    private static final int TASKLIST_FAILE = 0x20011;
    public static final int COMPRESS_IMAGE = 0x17;

    public static final int REFRESHVIEWPAGER_NOTIFY = 0x31;

    public static final int REFRESHVIEWPAGER_TASKADAPTER = 0X34;
    public static final int REFRESHVIEWPAGER_CURRENTDATA = 0X35;

    public static final int GET_DYNAMICDATA_SUC = 0X36;
    public static final int GET_DYNAMICDATA_FAIL = 0X37;

    private MyHandler myHandler;

    private void initMyHandler() {
        myHandler = new MyHandler(getActivity()) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = myHandler.mactivity.get();
                if (null != activity) {
                    switch (msg.what) {
                        case TASKLIST_SUCESS:
                            dismissdialog();
                            if (null != msg && null != msg.obj) {
                                Log.i("gettask", msg.obj + "");
                                try {
                                    setTaskList(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            break;
                        case TASKLIST_FAILE:
                            dismissdialog();
                            if (null != getActivity()) {
                                setCurrentData();
                                ToastUtils.showToast(getActivity(), "亲！您的网络不给力哦！");
                            }
                            break;

                        case DELETE_SUCESS:
                            if (null != msg && null != msg.obj) {
                                Log.i("1111", msg.obj + "");
                                try {

                                    setDeletRes(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            break;

                        case REFRESHVIEWPAGER_NOTIFY:
                            adapter.notifyDataSetChanged();
                            // refreshViewPager(false);
                            break;
                        case REFRESHVIEWPAGER_CURRENTDATA:
                            refreshCurrentData();
                            break;
                        case REFRESHVIEWPAGER_TASKADAPTER:
                            setCurrentData();
                            break;
                        case ADD_SUCESS:
                            if (null != msg && null != msg.obj) {
                                Log.i("1111msg", msg.obj + "");
                                try {
                                    setAddRes(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case ADD_FAILE:
                            Log.i("1111msg", msg.obj + "");
                            // ToastUtils.showToast(getActivity(), "");
                            break;

                        case GET_DYNAMICDATA_SUC:
                            planLv.onRefreshComplete();
                            if (null != msg.obj) {
                                Log.i("dynamic", msg.obj + "");
                                try {
                                    initDynamicData(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                        case GET_DYNAMICDATA_FAIL:
                            planLv.onRefreshComplete();
                            break;
                        case NEW_TASK_WORK:
                            if (null == worker) {
                                worker = new Worker(TodayDynamicFragment.this);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    worker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    worker.execute();
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        };
    }

    private void initDynamicData(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            SharedPreferencesUtil.saveString(getActivity(), "DynamicData", jsonObject.toString());
            DynamicVo dynamicVo = gson.fromJson(jsonObject.toString(), DynamicVo.class);
            hotlist.clear();
            hotlist.addAll(dynamicVo.getList());
            hotAdapter.notifyDataSetChanged();
        }
    }

    protected void setTaskList(JSONObject jsonObject) {
        Log.i("111111111tasklist", jsonObject + "");
        if (jsonObject.optString("result_id").equals("0")) {
            final JSONArray jarr = jsonObject.optJSONArray("syncTaskInfos");

            if (null == jarr) {
                return;
            }

            if (null != serviceTask) {
                serviceTask.cancel(true);
                serviceTask = null;
            }

            // serviceTask = new SysnServiceTask(jarr.toString());

            List<TaskVo> list = gson.fromJson(jarr.toString(),
                    new TypeToken<List<TaskVo>>() {
                    }.getType());
            if (list != null) {
                Tasker.syncNetworkTask(list);
            }
            setCurrentData();
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            serviceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else {
//            serviceTask.execute();
//        }
    }


    public boolean compareDate(LocalDate date, LocalDate date2) {
        return currentday.compareTo(date) < 0
                || currentday.compareTo(date2) > 0;
    }

    // 设置当前数据，服务器数据+本地数据
    List<TaskVo> monthTaskList = new ArrayList<TaskVo>();
    SysnLocalTask loacalWork;
    SysnServiceTask serviceTask;

    private void setCurrentData() {
        if (null != loacalWork) {
            loacalWork.cancel(true);
            loacalWork = null;
        }
        loacalWork = new SysnLocalTask();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            loacalWork.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            loacalWork.execute();
        }
    }


    /**
     * 刷新本地任务
     */
    private class SysnLocalTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onProgressUpdate(Integer... progresses) {
            int status = progresses[0];
            Log.i("upload", "onProgressUpdate(Progress... progresses) called");

        }

        // onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String result) {
            refreshCurrentData();
        }

        @Override
        protected String doInBackground(String... params) {

            List<TaskVo> list = Tasker.getAllLocalTask();
            taskList.clear();
            taskList.addAll(list);
            return "ok";
        }
    }

    /**
     * 更新服务器任务
     */
    private class SysnServiceTask extends AsyncTask<String, Integer, String> {
        String jarr;

        @Override
        protected void onProgressUpdate(Integer... progresses) {
            int status = progresses[0];

        }

        public SysnServiceTask(String jarr) {
            this.jarr = jarr;
        }

        // onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String result) {
            setCurrentData();
        }

        @Override
        protected String doInBackground(String... params) {

            List<TaskVo> list = gson.fromJson(jarr.toString(),
                    new TypeToken<List<TaskVo>>() {
                    }.getType());
            if (list != null) {
                Tasker.syncNetworkTask(list);
            }
            return "ok";
        }
    }

    public void refreshCurrentData() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        List<TaskVo> chcheList = new ArrayList<>();
        chcheList.addAll(taskList);
        monthTaskList.clear();
        for (TaskVo tv : chcheList) {
            String datatime = tv.getMission_time();
            Date time1;
            try {
                time1 = df.parse(datatime);
                boolean isdayOfMonth = compareDate(df.parse(monthMinDate),
                        df.parse(monthMaxDate2), time1);
                if (isdayOfMonth) {
                    monthTaskList.add(tv);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
        if (monthTaskList.size() <= 0) {
            empertyView.setVisibility(View.VISIBLE);
        } else {
            empertyView.setVisibility(View.GONE);
        }
        Log.i("currentdata", "size=" + monthTaskList.size());
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除已提交任务请求
     */
    int deletTaskId = 0;

    public void deletTasks(int taskId, String type) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            deletTaskId = taskId;
            job.put("task_id", taskId + "");
            job.put("type", type);// 手卫生的1，督导本2
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
                DELETE_SUCESS, DELETE_FAIL, job, "task/deleteTaskById");
    }

    // 删除任务处理
    protected void setDeletRes(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0") || jsonObject.optString("result_id").equals("8")) {
            for (TaskVo vo : taskList) {
                if (vo.getTask_id() == deletTaskId) {
                    Tasker.deleteTask(vo);
                    break;
                }
            }
            setCurrentData();
            ToastUtils.showToast(getActivity(), "任务删除成功");
        } else {
            ToastUtils.showToast(getActivity(),
                    jsonObject.optString("result_msg"));
        }

    }

    Dialog addScoredialog;

    protected void setAddRes(JSONObject jsonObject) {
        Log.i("share_umeng", "返回次数");
        Log.i("11111111", jsonObject.toString());
        if (jsonObject.optString("result_id").equals("0")) {
            int score = jsonObject.optInt("integral");
            String str = tools.getValue(Constants.INTEGRAL);
            if (null != str) {
                tools.putValue(Constants.INTEGRAL, Integer.parseInt(str)
                        + score + "");
            } else {
                tools.putValue(Constants.INTEGRAL, score + "");
            }
            if (score > 0) {
                addScoredialog = new TipsDialog(getActivity(), score + "");
                addScoredialog.show();
            }
        }
    }

    @Override
    public void Finshed(TaskVo task) {
        setCurrentData();
        // getTaskList();
        MyAppliaction.getContext().sendBroadcast(
                new Intent(WorkSpaceFragment.UPDATA_ACTION));// 其他任务显示页面提示刷新
        getAddScore(getActivity(), "2");
    }

    @Override
    public void RefreshNetwork() {
        getTaskList();
        MyAppliaction.getContext().sendBroadcast(
                new Intent(WorkSpaceFragment.UPDATA_ACTION));// 其他任务显示页面提示刷新
        getAddScore(getActivity(), "2");
    }

    protected static final int ADD_FAILE = 0x60089;
    protected static final int ADD_SUCESS = 0x60090;

    public void getAddScore(Activity activity, String id) {
        Log.i("share_umeng", "111111111111111");
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("aid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
                ADD_SUCESS, ADD_FAILE, job, "goods/actionGetIntegral");
    }


    @Override
    public void workFinish() {
        if(worker!=null){
        worker.cancel(true);
        worker = null;
        }
        /**
         * 无限循环提交
         */
//        worker = new Worker(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            worker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else {
//            worker.execute();
//        }

    }

    class NetworkReciever extends NetBroadCastReciver {

        @Override
        public void ontWorkChange() {

        }

        @Override
        public void onNetWorkConnect() {
            myHandler.sendEmptyMessageDelayed(NEW_TASK_WORK, 1000);
        }

        @Override
        public void onNetWorkDisConnect() {
            if (null != worker) {
                worker.cancel(true);
                worker = null;
            }

        }
    }
}
