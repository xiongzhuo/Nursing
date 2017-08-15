package com.deya.hospital.handwash;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.HandMainAdapter;
import com.deya.hospital.adapter.HandMainHospitalAdapter;
import com.deya.hospital.base.BaseListDialog;
import com.deya.hospital.base.BaseMutiDepartChooseActivity;
import com.deya.hospital.base.DepartChooseActivity;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.base.NetBroadCastReciver;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.consumption.ConsumptionFormActivity;
import com.deya.hospital.descover.NeedKnowListActivity;
import com.deya.hospital.form.FormListActivity;
import com.deya.hospital.form.handantisepsis.HandDisinfectionPrivewActivity;
import com.deya.hospital.login.LoginActivity;
import com.deya.hospital.shop.ShopGoodsListActivity;
import com.deya.hospital.shop.SignInActivity;
import com.deya.hospital.task.Tasker;
import com.deya.hospital.task.Worker;
import com.deya.hospital.task.notify.WorkerFinishedNotify;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.HomePageBanner;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.vo.AdvEntity;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.vo.HandReportVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.handwash.HandWashTasksActivity;
import com.deya.hospital.workspace.stastic.StasticTypeActivity;
import com.deya.hospital.workspace.supervisorquestion.SupQuestionListActivity;
import com.deya.hospital.workspace.tasksearcjh.SearchHandTaskActivity;
import com.flyco.banner.widget.Banner.base.BaseBanner.OnItemClickL;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.dialog.ECAlertDialog;
import com.im.sdk.dy.common.utils.ECNotificationManager;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.yuntongxun.ecsdk.ECDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HandMainActivity extends BaseMutiDepartChooseActivity implements
        View.OnClickListener, RadioGroup.OnCheckedChangeListener, RequestInterface, OnItemClickListener, PullToRefreshBase.OnRefreshListener2, WorkerFinishedNotify {

    public static final String UPDATA_ACTION = "updateList";

    public static final int ADD_SUPQUESTION = 0x118;// 是否关闭软文
    private static final int GETTASKREPORTINFO = 0x119;
    private static final int GETTASKHOSPITALREPORTINFO = 0x120;
    private static final int QUESTION_COUNT = 0x121;
    List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
    private LayoutInflater inflaterHead;
    private Gson gson = new Gson();
    private Tools tools;
    private MyBrodcastReceiver brodcast;
    private PullToRefreshListView planLv;
    HandMainAdapter adapter;
    HandMainHospitalAdapter handMainHospitalAdapter;
    List<HandReportVo.HospitalReportBean> hospitalList;
    private LinearLayout signInTv;
    String monthMinDate = "";
    String monthMaxDate = "";
    String monthMaxDate2 = "";
    HomePageBanner viewPager;
    View headView;
    LinearLayout handHhygieneLay, infectionLay;
    private Animation animation;
    private TextView qipaoTv;
    RadioGroup radioGroup;
    int pageIndex = 1;
    int pageCount;
    ImageView red_point;
    TextView otherJobTv;
    LinearLayout empertyView;
    TextView typeTv;
    ImageView switchView;
    int state = 1;
    List<HandReportVo.DepartmentReportBean> departReportList = new ArrayList<>();
    //    String[] popArr = {"WHO手卫生观察", "外科手消毒", "手卫生消耗量", "手卫生设施用品", "手卫生知识知晓",
//            "供应室手卫生", "实验室手卫生"};
   String[] popArr = {"WHO手卫生观察", "外科手消毒", "手卫生消耗量"};
    private Worker worker;
    private NetworkReciever networkReciever;
    private TextView departmentTv;
    private Map<String, ChildsVo> selectMap;
    private String departNames;
    private String departIds = "";
    LinearLayout departLay;
    private boolean isCheck;
    LinearLayout reportLay;
    LinearLayout messageLay;
    ImageView message_red_point;
    private CommonTopView topView;
    private static final int SET_ADV = 0x122 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_workspace);
        inflaterHead = LayoutInflater.from(mcontext);
        tools = new Tools(mcontext, Constants.AC);
        
        initView();
    }

    @Override
    protected void onChooseDepartList(Map<String, ChildsVo> map) {
        departNames = "";
        departIds = "";
        selectMap = map;
        Iterator<String> iter = selectMap.keySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            ChildsVo value = selectMap.get(iter.next());
            if (i < selectMap.size() - 1) {
                departNames += value.getName() + ",";
                departIds += value.getId() + ",";
            } else {
                departNames += value.getName();
                departIds += value.getId();
            }

            i++;

        }
        departmentTv.setText(departNames.equals("") ? "全部" : departNames);
        pageIndex = 1;
        pageCount = 0;
        departReportList.clear();
        adapter.notifyDataSetChanged();
        getTaskReportInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        setRequstDate();
        getQuestionCount();
        setRed_point();
    }

    public void setRed_point(){
        if(null!=Tasker.findListByKeyValue("type","1")&&Tasker.findListByKeyValue("type","1").size()>0){
            red_point.setVisibility(View.VISIBLE);
        }else{
            red_point.setVisibility(View.GONE);
        }
    }
    private void setRequstDate() {
       // red_point.setVisibility(Tasker.getAllLocalTask().size() > 0 ? View.VISIBLE : View.GONE);
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        monthMinDate = dateFormater.format(cal.getTime()) + "";
        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        monthMaxDate = dateFormater.format(cal.getTime()) + "  " + "23:59";
        monthMaxDate2 = dateFormater.format(cal.getTime()) + "-31";

    }


    private List<AdvEntity.ListBean> pagerList = new ArrayList<AdvEntity.ListBean>();


    private void initView() {
        topView=findView(R.id.topView);
        topView.setTitle("WHO手卫生观察");
        topView.init(this);
        topView.setRigtext("应知应会");
        topView.onRightClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it3= new Intent(mcontext,
                        NeedKnowListActivity.class);
                it3.putExtra("task_type","1");
                startActivity(it3);
            }
        });
        hospitalList = new ArrayList<>();
        adapter = new HandMainAdapter(mcontext, departReportList);
        adapter.setItemClickListener(this);
        handMainHospitalAdapter = new HandMainHospitalAdapter(mcontext, hospitalList);
        handMainHospitalAdapter.setItemClickListener(this);
        planLv = (PullToRefreshListView) findViewById(R.id.planlist);
        planLv.setOnItemClickListener(this);
        headView = inflaterHead.inflate(R.layout.workspace_headview_item, null);
        planLv.getRefreshableView().addHeaderView(headView);
        planLv.setOnRefreshListener(this);
        radioGroup = (RadioGroup) headView.findViewById(R.id.radioGroup);
        message_red_point = (ImageView) headView.findViewById(R.id.message_red_point);
        radioGroup.setOnCheckedChangeListener(this);
        qipaoTv = (TextView) headView.findViewById(R.id.qipaoTv);

        departLay = (LinearLayout) headView.findViewById(R.id.departLay);
        departmentTv = (TextView) headView.findViewById(R.id.departmentTv);
        departmentTv.setOnClickListener(this);
        empertyView = (LinearLayout) headView.findViewById(R.id.empertyView);
        handHhygieneLay = (LinearLayout) headView
                .findViewById(R.id.handHhygieneLay);
        handHhygieneLay.setOnClickListener(this);
        messageLay = (LinearLayout) headView.findViewById(R.id.messageLay);
        messageLay.setOnClickListener(this);
        infectionLay = (LinearLayout) headView.findViewById(R.id.infectionLay);
        infectionLay.setOnClickListener(this);
        red_point = (ImageView) findViewById(R.id.red_point);
        otherJobTv = (TextView) headView.findViewById(R.id.otherJobTv);
        typeTv = (TextView) headView.findViewById(R.id.typeTv);
        typeTv.setOnClickListener(this);
        switchView = (ImageView) headView.findViewById(R.id.switchView);
        switchView.setOnClickListener(this);
        reportLay = (LinearLayout) headView.findViewById(R.id.reportLay);
        reportLay.setOnClickListener(this);
        viewPager = (HomePageBanner) headView.findViewById(R.id.viewPager);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(popArr));
        popDialog = new ChoosePop(mcontext, arrayList, new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        addTask(1);
                        break;
                    case 1:
                        StartActivity(FormListActivity.class);
                       // showDialogToast("","正在排队上线中!");
                        break;
                    case 2:
                        addTask(4);
                        break;

                }
                popDialog.dismiss();

            }
        });
//        popDialog2 = new ChoosePop(mcontext, arrayList, new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        break;
//                    case 1:
//                        break;
//                    case 2:
//                        break;
//
//                }
//                popDialog2.dismiss();
//            }
//        });
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
        getTaskReportInfo();
        getTaskHospitalReportInfo();
        if (null == worker) {
            worker = new Worker(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            worker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            worker.execute();
        }
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

        viewPager.setOnItemClickL(new OnItemClickL() {

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

    private void refreshDepartData() {
        departReportList.clear();
        adapter.notifyDataSetChanged();
        pageCount = 0;
        pageIndex = 1;
        getTaskReportInfo();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == HandMainActivity.ADD_SUPQUESTION) {
            hasNewQuestion();
        } else if (resultCode == DepartChooseActivity.CHOOSE_SUC) {
            departIds="";

            List<DepartVos.DepartmentListBean> departs = (List<DepartVos.DepartmentListBean>) data.getSerializableExtra("departs");
            String names = "";
            for (int i = 0; i < departs.size(); i++) {
                DepartVos.DepartmentListBean bean = departs.get(i);

                if (i < departs.size() - 1) {
                    departIds += bean.getId() + ",";
                    names += bean.getName() + ",";
                } else {
                    departIds += bean.getId();
                    names += bean.getName();
                }
            }
            departmentTv.setText(names);
            refreshDepartData();
        }


    }


    public void hasNewQuestion() {
        qipaoTv.setVisibility(View.VISIBLE);
        qipaoTv.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                qipaoTv.setVisibility(View.GONE);
            }
        }, 5000);
    }


    ChoosePop popDialog;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.signInTv:
                OnStartActivity(SignInActivity.class);
                break;
            case R.id.handHhygieneLay:
                addTask(1);
                break;
            case R.id.infectionLay:
                Intent intent2=new Intent(mcontext,SearchHandTaskActivity.class);
                intent2.putExtra("type",1);
                startActivity(intent2);
                break;
            case R.id.departmentTv:
//                if(null==departlist||departlist.size()<1){
//                    initDepartPop();
//                }
//                departDialog.show();
                Intent intent = new Intent(mcontext, DepartChooseActivity.class);
                intent.putExtra(DepartChooseActivity.MUTICHOOSE, 1);
                startActivityForResult(intent, DepartChooseActivity.CHOOSE_SUC);
                break;
            case R.id.typeTv:
                //popDialog2.showBottomHightAjust();
                break;
            case R.id.switchView:
                isCheck = !isCheck;
                switchView.setImageResource(isCheck ? R.drawable.dynamic_but2
                        : R.drawable.dynamic_but1);
                state = isCheck ? 0 : 1;
                refreshDepartData();
                break;
            case R.id.reportLay:
                OnStartActivity(StasticTypeActivity.class);
                break;
            case R.id.messageLay:
                Intent intent3=new Intent(mcontext,SupQuestionListActivity.class);
                intent3.putExtra("origin",1);
                startActivity(intent3);
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
            case 1:
                it.setClass(mcontext, HandWashTasksActivity.class);
                startActivity(it);
                break;
            case 5:
                it.setClass(mcontext, HandDisinfectionPrivewActivity.class);
                startActivity(it);
                break;
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

                    pageIndex = 1;
                    pageCount = 0;
                    departReportList.clear();
                    hospitalList.clear();
                    handMainHospitalAdapter.notifyDataSetChanged();
                    planLv.setRefreshing();
                    adapter.notifyDataSetChanged();
                    getTaskHospitalReportInfo();

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

        if (TaskUtils.isOtherJob(mcontext)) {
            radioGroup.setVisibility(View.GONE);
            otherJobTv.setVisibility(View.VISIBLE);
            otherJobTv.setText("本月" + tools.getValue(Constants.DEFULT_DEPART_NAME) + "数据");
            planLv.setAdapter(adapter);
        } else if (TaskUtils.mysticalJob(mcontext)) {
            otherJobTv.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
            planLv.setAdapter(adapter);
            otherJobTv.setText("本月督导数据");
        } else {
            planLv.setAdapter(handMainHospitalAdapter);
        }

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
                if(null==viewPager){
                    return;
                }
                Message msg=new Message();
                msg.obj=jsonObject.toString();
                msg.what=SET_ADV;
                myHandler.sendMessage(msg);
                break;
            case GETTASKREPORTINFO:
                setReport(jsonObject);
                break;
            case GETTASKHOSPITALREPORTINFO:
                setHospitalReport(jsonObject);
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

    private void setHospitalReport(JSONObject jsonObject) {
        HandReportVo reporVo = gson.fromJson(jsonObject.toString(), HandReportVo.class);
        if (null != reporVo.getHospitalReport()) {
            hospitalList.addAll(reporVo.getHospitalReport());
        }
        handMainHospitalAdapter.notifyDataSetChanged();

    }

    private void setReport(JSONObject jsonObject) {
        planLv.onRefreshComplete();
        HandReportVo reporVo = gson.fromJson(jsonObject.toString(), HandReportVo.class);
        pageCount = reporVo.getPageTotal();
        if (pageCount <= pageIndex) {
            planLv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        } else {
            planLv.setMode(PullToRefreshBase.Mode.BOTH);
        }
        pageIndex++;

        if (null != reporVo.getDepartmentReport()) {
            departReportList.addAll(reporVo.getDepartmentReport());
        }

        adapter.notifyDataSetChanged();

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
                planLv.setAdapter(handMainHospitalAdapter);
                planLv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
                departLay.setVisibility(View.GONE);

                break;
            case R.id.rb1:
                if (TaskUtils.isOtherJob(mcontext)) {
                    departLay.setVisibility(View.GONE);
                } else if (TaskUtils.mysticalJob(mcontext)) {
                    departLay.setVisibility(View.GONE);
                } else {
                    departLay.setVisibility(View.VISIBLE);
                }
                //planLv.setRefreshing(true);
                //   planLv.startLayoutAnimation();
                if (pageCount < (pageIndex - 1) * 5) {
                    planLv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
                } else {
                    planLv.setMode(PullToRefreshBase.Mode.BOTH);
                }
                planLv.setAdapter(adapter);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mcontext, ReportItemDetailActivity.class);

        startActivity(intent);

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pageIndex = 1;
        pageCount = 0;
        departReportList.clear();
        hospitalList.clear();
        handMainHospitalAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        getTaskReportInfo();
        getTaskHospitalReportInfo();

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        getTaskReportInfo();

    }

    @Override
    public void Finshed(TaskVo task) {
        hospitalList.clear();
        departReportList.clear();
        pageIndex = 1;
        pageCount = 0;
        getTaskHospitalReportInfo();
        planLv.setRefreshing();

    }

    @Override
    public void RefreshNetwork() {

    }

    @Override
    public void workFinish() {

    }


    class ChoosePop extends BaseListDialog<String> {

        public ChoosePop(Context context, List<String> list, OnItemClickListener listener) {
            super(context, list, listener);
        }

        @Override
        protected void intUi() {
            titleTv.setText("选择模板");
            right_txt.setVisibility(View.GONE);
            left_txt.setVisibility(View.VISIBLE);
            left_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

        @Override
        public void setListDta(ViewHolder viewHolder, int position) {
            if (position == 0 || position == 1 || position == 2) {
                viewHolder.tiptxt.setVisibility(View.GONE);
                viewHolder.listtext.setText(list.get(position));
            } else {
                viewHolder.tiptxt.setVisibility(View.VISIBLE);
                viewHolder.listtext.setText(list.get(position));
                viewHolder.tiptxt.setText("(升级后使用)");
            }
            viewHolder.contentLay.setGravity(Gravity.CENTER);
        }
    }

    public void getTaskReportInfo() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("pageIndex", pageIndex);
            if (!departIds.equals("")) {
                job.put("departmentIds", departIds);
            }
            job.put("state", state);
            job.put("pageCount", 5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext,
                GETTASKREPORTINFO, job, "task/handTimesReport");

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


    public void getTaskHospitalReportInfo() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext,
                GETTASKHOSPITALREPORTINFO, job, "task/handTimesReportByHospital");

    }


    class NetworkReciever extends NetBroadCastReciver {

        @Override
        public void ontWorkChange() {

        }

        @Override
        public void onNetWorkConnect() {

            if (null == worker) {
                worker = new Worker(HandMainActivity.this);
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

    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case SET_ADV:
                        try {
                            setAdv(new JSONObject(msg.obj.toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        }

    };
}
