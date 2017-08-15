package com.deya.hospital.dudao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.TaskLisAdapter;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.base.BaseMutiDepartChooseActivity;
import com.deya.hospital.base.DeletDialog2;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.base.NetBroadCastReciver;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.consumption.ConsumptionFormActivity;
import com.deya.hospital.descover.NeedKnowListActivity;
import com.deya.hospital.login.LoginActivity;
import com.deya.hospital.shop.ShopGoodsListActivity;
import com.deya.hospital.shop.SignInActivity;
import com.deya.hospital.task.Tasker;
import com.deya.hospital.task.Worker;
import com.deya.hospital.task.notify.WorkerFinishedNotify;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DatePicDialog;
import com.deya.hospital.util.HomePageBanner;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.vo.AdvEntity;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.supervisorquestion.SupQuestionListActivity;
import com.deya.hospital.workspace.supervisorquestion.SupervisorQesDetitalActivity;
import com.deya.hospital.workspace.supervisorquestion.SupervisorQueCreatActivity;
import com.deya.hospital.workspace.tasksearcjh.SearchHandTaskActivity;
import com.flyco.banner.widget.Banner.base.BaseBanner;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.dialog.ECAlertDialog;
import com.im.sdk.dy.common.utils.DateUtil;
import com.im.sdk.dy.common.utils.ECNotificationManager;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.yuntongxun.ecsdk.ECDevice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/29
 */
public class SupvisorMainActivity extends BaseMutiDepartChooseActivity implements
        View.OnClickListener, RadioGroup.OnCheckedChangeListener, RequestInterface, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2, WorkerFinishedNotify {

    public static final String UPDATA_ACTION = "updateList";

    private static final int GETTASKREPORTINFO = 0x119;
    private static final int GETT_FORM_INFO = 0x121;
    private static final int TASKLIST_SUCESS = 0x122;
    private static final int DELETE_SUCESS =0x123 ;
    List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
    private LayoutInflater inflaterHead;
    private Gson gson = new Gson();
    private Tools tools;
    private MyBrodcastReceiver brodcast;
    private PullToRefreshListView planLv;
    private LinearLayout signInTv;
    String monthMinDate = "";
    String monthMaxDate = "";
    String monthMaxDate2 = "";
    HomePageBanner viewPager;
    View headView;
    LinearLayout handHhygieneLay, infectionLay;
    int pageIndex1 = 1;
    int pageIndex2 = 1;
    int pageCount;
    ImageView red_point;
    TextView otherJobTv;
    LinearLayout empertyView;
    private Worker worker;
    private NetworkReciever networkReciever;
    private String departIds = "";
    int tempId;
    TaskLisAdapter adapter;
    List<TaskVo> taskVoList;
    private String start_date = "";
    private String end_date = "";
    TimeSearchDialog timeSearchDialog;
    private TextView timeTv;
    DeletDialog2 deletDialog;
    private CommonTopView topView;
    private static final int QUESTION_COUNT = 0x121;
    ImageView message_red_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflaterHead = LayoutInflater.from(mcontext);
        tools = new Tools(mcontext, Constants.AC);
        setContentView(R.layout.sup_main_fragment);
        initView();
    }

    @Override
    protected void onChooseDepartList(Map<String, ChildsVo> map) {
    }

    @Override
    public void onResume() {
        super.onResume();
        setRequstDate();

    }

    private void setRequstDate() {
        red_point.setVisibility(Tasker.getAllLocalTaskByType("7").size() > 0 ? View.VISIBLE : View.GONE);
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        monthMinDate = dateFormater.format(cal.getTime()) + "";
        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        monthMaxDate = dateFormater.format(cal.getTime()) + "  " + "23:59";
        monthMaxDate2 = dateFormater.format(cal.getTime()) + "-31";
    }


    ImageView falshImg;
    SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
    private List<AdvEntity.ListBean> pagerList = new ArrayList<>();


    private void initView() {
        topView=findView(R.id.topView);
        topView.setTitle("督查反馈");
        topView.init(this);
        topView.setRigtext("应知应会");
        topView.onRightClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it3= new Intent(mcontext,
                        NeedKnowListActivity.class);
                it3.putExtra("task_type","7");
                startActivity(it3);
            }
        });
        Calendar cal = Calendar.getInstance();
        final Date localTime = cal.getTime();
        start_date=dateFormater.format(DateUtil
                .getFirstDayOfMonth(localTime)) + "";
        end_date=dateFormater.format(DateUtil
                .getLastDayOfMonth(localTime));
        taskVoList = new ArrayList<>();
        planLv = (PullToRefreshListView) findViewById(R.id.planlist);
        planLv.setOnItemClickListener(this);
        adapter = new TaskLisAdapter(mcontext, taskVoList);
        planLv.setAdapter(adapter);


        headView = inflaterHead.inflate(R.layout.sup_headview_item, null);
        planLv.getRefreshableView().addHeaderView(headView);
        planLv.setOnRefreshListener(this);
        handHhygieneLay = (LinearLayout) headView
                .findViewById(R.id.handHhygieneLay);
        headView.findViewById(R.id.messageLay).setOnClickListener(this);
       timeTv= (TextView) headView.findViewById(R.id.timeTv);
        timeTv.setText("本月数据");
        timeTv.setOnClickListener(this);
        empertyView = (LinearLayout) headView.findViewById(R.id.empertyView);
        handHhygieneLay.setOnClickListener(this);
        infectionLay = (LinearLayout) headView.findViewById(R.id.infectionLay);
        infectionLay.setOnClickListener(this);
        red_point = (ImageView) findViewById(R.id.red_point);
        otherJobTv = (TextView) headView.findViewById(R.id.otherJobTv);

        headView.findViewById(R.id.reportLay).setOnClickListener(this);
        viewPager = (HomePageBanner) headView.findViewById(R.id.viewPager);
        message_red_point=findView(R.id.message_red_point);
        timeSearchDialog=new TimeSearchDialog(mcontext);
        networkReciever = new NetworkReciever();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mcontext.registerReceiver(networkReciever, intentFilter);
        initListView();
        setAdvPic();
        getCarouselFigure();
        registerBroadcast();
        setRequstDate();
        getTaskList();
        deletDialog=new DeletDialog2(mcontext, "确认删除此任务？", new DeletDialog2.DeletInter() {
            @Override
            public void onDelet(int position) {

                TaskVo tv=taskVoList.get(position);
                deletTasks(tv.getTask_id());
                deletDialog.dismiss();
            }
        });
        planLv.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                if(position<2){
                    return false;
                }else{
                deletDialog.setDeletPosition(position-2);
                deletDialog.show();
                return true;
                }
            }
        });
        getQuestionCount();
        if (null == worker) {
            worker = new Worker(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            worker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            worker.execute();
        }
    }
    public void getQuestionCount() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext,
                QUESTION_COUNT, job, "supervisorQuestion/questionCount");

    }
    public void deletTasks(int taskId) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("task_id", taskId + "");
            job.put("type", "7");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext, DELETE_SUCESS, job, "task/deleteTaskById");
    }
    /**
     * setHomeBanner:【填充广告的数据】. <br/>
     */
    private static final int GETCAROUSELFIGURE_SUCCESS = 0x11000;

    private void setAdvPic() {
        if(null==viewPager){
            return;
        }
        if (!(pagerList.size() > 0)) {
            AdvEntity.ListBean advEntity1 = new AdvEntity.ListBean();
            advEntity1.setDrawable(R.drawable.banner1);
            advEntity1.setType(1);
            advEntity1.setName("");
            pagerList.add(advEntity1);

            AdvEntity.ListBean advEntity2 = new AdvEntity.ListBean();
            advEntity2.setDrawable(R.drawable.banner2);
            advEntity2.setType(2);
            advEntity2.setName("");
            pagerList.add(advEntity2);
            viewPager.clearAnimation();
        }
        setHomeBanner();
    }

    /**
     * setHomeBanner:【填充广告的数据】. <br/>
     */
    private void setHomeBanner() {
        if (1 == pagerList.size()) {
            viewPager.setDelay(3).setPeriod(3).setAutoScrollEnable(false)
                    .setTouchAble(false).setSource(pagerList).startScroll(); // 只有一张广告设置不能滑动可以点击
        } else {// 否则执行自动滚动并设置为可以手动滑动也可以点击
            viewPager.setDelay(3).setPeriod(3).setAutoScrollEnable(true)
                    .setTouchAble(true).setSource(pagerList).startScroll();
        }

        viewPager.setOnItemClickL(new BaseBanner.OnItemClickL() {

            @Override
            public void onItemClick(int position) {
                if (pagerList != null && pagerList.size() > 0) {
                    int type = pagerList.get(position).getType();
                    switch (type) {
                        case 0:
                            Intent intent = new Intent(mcontext, WebViewDemo.class);
                            intent.putExtra("url", pagerList.get(position).getHref_url());
                            startActivity(intent);
                            break;
                        case 1:
                            OnStartActivity(ShopGoodsListActivity.class);
                            break;
//                        case 2:
//                            OnStartActivity(QuestionSortActivity.class);
//                            break;

                        default:
                            break;
                    }
                }
            }
        });
    }

    public void OnStartActivity(Class<?> T) {
        Intent inten = new Intent(mcontext, T);
        startActivity(inten);
    }


    @Override
    public void onDestroy() {
        if(null!=viewPager){
            viewPager.pauseScroll();
            viewPager.clearAnimation();
            viewPager=null;
        }
        if (null != brodcast) {
            mcontext.unregisterReceiver(brodcast);
        }

        if (null != networkReciever) {
            mcontext.unregisterReceiver(networkReciever);
        }
        if (null != worker) {
            worker.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.signInTv:
                OnStartActivity(SignInActivity.class);
                break;
            case R.id.handHhygieneLay:
               OnStartActivity(SupervisorQueCreatActivity.class);
                break;
            case R.id.infectionLay:
                Intent intent=new Intent(mcontext,SearchHandTaskActivity.class);
                intent.putExtra("type",7);
                startActivity(intent);
                break;
            case R.id.departmentTv:
                if (null == departlist || departlist.size() < 1) {
                    initDepartPop();
                }
                departDialog.show();
                break;
            case R.id.messageLay:
                Intent intent3=new Intent(mcontext,SupQuestionListActivity.class);
                intent3.putExtra("origin",7);
                startActivity(intent3);
                break;
            case R.id.timeTv:
                timeSearchDialog.show();
                break;
            case R.id.reportLay:
                showDialogToast("","参照手卫生,正在排队上线中!");
                break;
            default:
                break;
        }
    }


    public void addTask(int type) {
        Intent it = new Intent();
        if (type != 4) {
            it.putExtra("type", type + "");
        }
        switch (type) {

            case 4:
                it.setClass(mcontext, ConsumptionFormActivity.class);
                TaskVo tv = new TaskVo();
                tv.setMission_time(TaskUtils.getLoacalDate() + "");
                tv.setStatus(2);
                it.putExtra("data", tv);
                startActivity(it);
                break;
            default:
                break;
        }


    }


    // 软文模块
    private void registerBroadcast() {
        brodcast = new MyBrodcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(SDKCoreHelper.ACTION_KICK_OFF)) {
                    if (intent.hasExtra("kickoffText")) {
                        handlerKickOff(intent.getStringExtra("kickoffText"));
                    }
                   finish();

                } else if (intent.getAction().equals(Constants.AUTHENT_LOSE)) {
                    ToastUtils.showToast(mcontext, "登录失效请重新登录");
                  finish();
                } else if (intent.getAction().equals(UPDATA_ACTION)) {


                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.AUTHENT_LOSE);
        intentFilter.addAction(UPDATA_ACTION);
        intentFilter.addAction(SDKCoreHelper.ACTION_KICK_OFF);
        mcontext.registerReceiver(brodcast, intentFilter);
    }

    public void handlerKickOff(String kickoffText) {
        if (isFinishing()) {
            return;
        }
        ECAlertDialog buildAlert = ECAlertDialog.buildAlert(mcontext, kickoffText,
                getString(R.string.dialog_btn_confim),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ECNotificationManager.getInstance()
                                .forceCancelNotification();
                        Intent intent = new Intent(mcontext, LoginActivity.class);
                        startActivity(intent);
                        finish();
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


    }


    public void getCarouselFigure() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReqAllpath(this, this,
                GETCAROUSELFIGURE_SUCCESS, job, WebUrl.PUBLIC_SERVICE_URL + "/comm/getCarouselFigure");

    }

    private void setRequestData(AdvEntity entity) {
        if (entity.getList() != null && entity.getList().size() > 0) {
            viewPager.setDelay(0).setPeriod(0).setAutoScrollEnable(false)
                    .setTouchAble(false).setSource(pagerList).pauseScroll();
            pagerList.clear();
            pagerList.addAll(entity.getList());
        }

    }


    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        switch (code) {
            case GETCAROUSELFIGURE_SUCCESS:
                setAdv(jsonObject);
                break;
            case TASKLIST_SUCESS:
                setTaskData(jsonObject);
                break;
            case DELETE_SUCESS:
                taskVoList.clear();
                adapter.notifyDataSetChanged();
                refreshData();
                break;
            case QUESTION_COUNT:
                setMessageRedPoint(jsonObject);
                break;
        }
        dismissdialog();

    }

    private void setMessageRedPoint(JSONObject jsonObject) {
        Log.i(getClass().getName(), jsonObject.toString());
        if (jsonObject.has("totalcnt") && jsonObject.optInt("totalcnt") > 0) {
            message_red_point.setVisibility(View.VISIBLE);
        } else {
            message_red_point.setVisibility(View.GONE);
        }
    }
    private void setTaskData(JSONObject jsonObject) {
        Log.i("11111111",jsonObject.toString());
        planLv.onRefreshComplete();
        if (jsonObject.optString("result_id").equals("0")) {

            JSONArray jarr = jsonObject.optJSONArray("syncTaskInfos");
            List<TaskVo> list = null;
            try {
                list = gson.fromJson(jarr.toString(),
                        new TypeToken<List<TaskVo>>() {
                        }.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            taskVoList.addAll(list);
             pageCount = jsonObject.optInt("pageTotal");
            if (pageCount > pageIndex1) {
                planLv.setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
            } else {
                planLv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
            }
            if(taskVoList.size()>0){
                empertyView.setVisibility(View.GONE);
            }else{
                empertyView.setVisibility(View.VISIBLE);
            }
            pageIndex1++;
            adapter.notifyDataSetChanged();
        }

    }



    /**
     * @param jsonObject 广告请求成功数据
     */
    private void setAdv(JSONObject jsonObject) {

        try {
            AdvEntity entity = gson.fromJson(jsonObject.toString(), AdvEntity.class);
            if (entity != null) {
                if (entity.getResult_id().equals("0")) {
                    setRequestData(entity);
                } else {
                    ToastUtils.showToast(mcontext, entity.getResult_msg());
                }
            } else {
                ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        } finally {
            setAdvPic();
        }
    }
    @Override
    public void onRequestErro(String message) {
        dismissdialog();
        ToastUtil.showMessage(message);
        Log.i("1111111111", message);
    }

    @Override
    public void onRequestFail(int code) {
        switch (code) {
            case GETCAROUSELFIGURE_SUCCESS:
                pagerList.clear();
                setAdvPic();
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb0:

                break;
            case R.id.rb1:

                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position<2){
            return;
        }
        Intent toWeb = new Intent(mcontext,
                SupervisorQesDetitalActivity.class);
        toWeb.putExtra("id", taskVoList.get(position-2).getTask_id() + "");
        startActivityForResult(toWeb, 0x136);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        refreshData();


    }

    private void refreshData(){
        pageIndex1=1;
        pageCount=0;
        taskVoList.clear();
        adapter.notifyDataSetChanged();
        getTaskList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        getTaskList();

    }

    @Override
    public void Finshed(TaskVo task) {
        pageIndex1 = 1;
        pageCount = 0;
        pageIndex2 = 1;
        planLv.setRefreshing();

    }

    @Override
    public void RefreshNetwork() {

    }

    @Override
    public void workFinish() {

    }

    public void getTaskList() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {

            job.put("authent", tools.getValue(Constants.AUTHENT));
            if (!AbStrUtil.isEmpty(start_date)) {
                job.put("start_time", start_date);
            }
                job.put("task_type", "7");
            if (!AbStrUtil.isEmpty(end_date)) {
                job.put("end_time", end_date);
            }
            job.put("pageIndex", pageIndex1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext, TASKLIST_SUCESS, job, "task/taskList");

    }


    class NetworkReciever extends NetBroadCastReciver {

        @Override
        public void ontWorkChange() {

        }

        @Override
        public void onNetWorkConnect() {

            if (null == worker) {
                worker = new Worker(SupvisorMainActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    worker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    worker.execute();
                }
            }
        }

        @Override
        public void onNetWorkDisConnect() {
            if (null != worker) {
                worker.cancel(true);
                worker = null;
            }

        }
    }

    public class TimeSearchDialog extends BaseDialog {
        private TextView confirm;
        private TextView start_time;
        private TextView end_time;
        private RadioButton lastMonth;
        RadioGroup radioGroup;
        String timeTxt;
        Context mcontext;

        /**
         * Creates a new instance of MyDialog.
         *
         * @param context
         */
        public TimeSearchDialog(Context context) {
            super(context);
            mcontext=context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.supervisor_time_layout2);
            initView();
        }

        private void initView() {
            confirm = (TextView) this.findViewById(R.id.confirm2);
            confirm.setEnabled(true);
            radioGroup= (RadioGroup) this.findViewById(R.id.group);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.rb_week:
                            timeTxt="本周数据";
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.DAY_OF_WEEK, 1);
                            cal.getTime();
                            start_time.setText(dateFormater.format(cal.getTime()) + "");
                            cal.set(Calendar.DAY_OF_WEEK,
                                    cal.getActualMaximum(Calendar.DAY_OF_WEEK));
                            end_time.setText(dateFormater.format(cal.getTime()));
                            start_date = start_time.getText().toString();
                            end_date = end_time.getText().toString();
                            timeTv.setText(timeTxt);
                            dismiss();
                            refreshData();
                            break;
                        case R.id.rb_month:
                            timeTxt=("本月数据");
                            timeTv.setText(timeTxt);
                            Calendar cal1 = Calendar.getInstance();
                            cal1.set(Calendar.DAY_OF_MONTH, 1);
                            cal1.getTime();
                            start_time.setText(dateFormater.format(cal1.getTime()) + "");

                            cal1.set(Calendar.DAY_OF_MONTH,
                                    cal1.getActualMaximum(Calendar.DAY_OF_MONTH));
                            end_time.setText(dateFormater.format(cal1.getTime()));
                            dismiss();
                            start_date = start_time.getText().toString();
                            end_date = end_time.getText().toString();
                            dismiss();
                            refreshData();
                            break;
                        case R.id.rb_year:
                            timeTxt=("今年数据");
                            timeTv.setText(timeTxt);
                            Calendar cal2 = Calendar.getInstance();
                            cal2.set(Calendar.DAY_OF_YEAR, 1);
                            cal2.getTime();
                            start_time.setText(dateFormater.format(cal2.getTime()) + "");

                            cal2.set(Calendar.DAY_OF_YEAR,
                                    cal2.getActualMaximum(Calendar.DAY_OF_YEAR));
                            end_time.setText(dateFormater.format(cal2.getTime()));
                            start_date = start_time.getText().toString();
                            end_date = end_time.getText().toString();
                            dismiss();
                            refreshData();
                            break;
                    }

                }
            });
            start_time = (TextView) this.findViewById(R.id.start_time);
            start_time.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DatePicDialog dialog = new DatePicDialog(mcontext,
                            new DatePicDialog.OnDatePopuClick() {

                                @Override
                                public void enter(String text) {
                                    start_time.setText(text);
                                }

                            });
                    dialog.show();
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams lp = dialog.getWindow()
                            .getAttributes();
                    lp.width = (int) (display.getWidth()); // 设置宽度
                    lp.height = (int) (display.getHeight()); // 设置高度
                    dialog.getWindow().setAttributes(lp);
                }
            });

            end_time = (TextView) this.findViewById(R.id.end_time);
            end_time.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DatePicDialog dialog = new DatePicDialog(mcontext,
                            new DatePicDialog.OnDatePopuClick() {

                                @Override
                                public void enter(String text) {
                                    end_time.setText(text);
                                }

                            });
                    dialog.show();
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams lp = dialog.getWindow()
                            .getAttributes();
                    lp.width = (int) (display.getWidth()); // 设置宽度
                    lp.height = (int) (display.getHeight()); // 设置高度
                    dialog.getWindow().setAttributes(lp);
                }
            });

            lastMonth = (RadioButton) this.findViewById(R.id.lastMonth);
            lastMonth.setVisibility(View.GONE);
            this.findViewById(R.id.whiteView).setVisibility(View.GONE);
            this.findViewById(R.id.confirm2).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            start_date = start_time.getText().toString();
                            end_date = end_time.getText().toString();
                            dismiss();
                            refreshData();
                            timeTv.setText(start_date+"至"+end_date);

                        }
                    });
            this.findViewById(R.id.canser2).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dismiss();
                            timeTxt=("督导时间");
                            start_time.setText("");
                            end_time.setText("");
                            dismiss();
                        }
                    });



        }


    }
}