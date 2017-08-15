package com.deya.hospital.quality;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.deya.hospital.adapter.BaseMainReportAdapter;
import com.deya.hospital.base.BaseJumpToDepartActivty;
import com.deya.hospital.base.BaseListDialog;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.base.NetBroadCastReciver;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.consumption.ConsumptionFormActivity;
import com.deya.hospital.descover.NeedKnowListActivity;
import com.deya.hospital.handwash.ReportItemDetailActivity;
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
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.vo.AdvEntity;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.vo.QuaLityDepartReportVo;
import com.deya.hospital.vo.QualityReportVo;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.vo.ZformVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
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
import java.util.List;
import java.util.Map;


public class QualityMainActivity extends BaseJumpToDepartActivty implements
        View.OnClickListener, RadioGroup.OnCheckedChangeListener,  OnItemClickListener, PullToRefreshBase.OnRefreshListener2, WorkerFinishedNotify {

    public static final String UPDATA_ACTION = "updateList";

    public static final int ADD_SUPQUESTION = 0x118;// 是否关闭软文
    private static final int GETTASKREPORTINFO = 0x119;
    private static final int GETTASKHOSPITALREPORTINFO = 0x120;
    private static final int GETT_FORM_INFO = 0x121;
    private static final int SET_ADV = 0x122 ;
    private LayoutInflater inflaterHead;
    private Gson gson = new Gson();
    private Tools tools;
    private MyBrodcastReceiver brodcast;
    private PullToRefreshListView planLv;
    BaseMainReportAdapter adapter;
    BaseMainReportAdapter handMainHospitalAdapter;
    List<QualityReportVo.ListBean> hospitalList;
    String monthMinDate = "";
    String monthMaxDate = "";
    String monthMaxDate2 = "";
    HomePageBanner viewPager;
    View headView;
    LinearLayout handHhygieneLay, infectionLay;
    private Animation animation;
    private TextView qipaoTv;
    RadioGroup radioGroup;
    int pageIndex1 = 1;
    int pageIndex2 = 1;
    int pageCount;
    ImageView red_point;
    TextView otherJobTv;
    LinearLayout empertyView;
    List<QualityReportVo.ListBean> departReportList = new ArrayList<>();
    String[] popArr = {"WHO手卫生观察", "外科手消毒", "手卫生消耗量", "手卫生设施用品", "手卫生知识知晓",
            "供应室手卫生", "实验室手卫生"};
    private Worker worker;
    private NetworkReciever networkReciever;
    private TextView departmentTv, formTypeTv;
    private Map<String, ChildsVo> selectMap;
    private String departNames;
    private String departIds = "";
    LinearLayout departLay;
    RequestInterface requestInterface;
    List<RisistantVo.TaskInfoBean.TempListBean> zFormList;
    int tempId;
    FormChoosePop formChoosePop;
    private int pageCount2;
    boolean isHospital = true;
    private CommonTopView topView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflaterHead=LayoutInflater.from(mcontext);
        tools = new Tools(mcontext, Constants.AC);
        setContentView(R.layout.quality_main_fragment);
        requestInterface=new RequestInterface() {
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
                    case GETT_FORM_INFO:
                        setFormCache(jsonObject);
                        break;
                }
                dismissdialog();
            }

            @Override
            public void onRequestErro(String message) {
                dismissdialog();
                ToastUtil.showMessage(message);
                Log.i("1111111111", message);
            }

            @Override
            public void onRequestFail(int code) {
                dismissdialog();
                switch (code) {
                    case GETCAROUSELFIGURE_SUCCESS:
                        pagerList.clear();
                        setAdvPic();
                        break;
                }
            }
        };
        initView();
    }



    @Override
    public void onResume() {
        super.onResume();
        setRequstDate();

    }

    private void setRequstDate() {
        red_point.setVisibility(Tasker.getAllLocalTaskByType("17").size() > 0 ? View.VISIBLE : View.GONE);
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


    ImageView falshImg;
    private List<AdvEntity.ListBean> pagerList = new ArrayList<>();
    private AnimationDrawable falshdrawable;


    private void initView() {
        topView=findView(R.id.topView);
        topView.setTitle(getIntent().getIntExtra("task_type",17)==17?"临床质控":"手卫生操作考核");
        topView.init(this);
        topView.setRigtext("应知应会");
        topView.onRightClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it3= new Intent(mcontext,
                        NeedKnowListActivity.class);
                it3.putExtra("task_type",getIntent().getIntExtra("task_type",17));
                startActivity(it3);
            }
        });
        hospitalList = new ArrayList<>();
        zFormList = new ArrayList<>();
        adapter = new BaseMainReportAdapter(mcontext, 3, departReportList);
        adapter.setItemClickListener(this);
        adapter.setHospital(false);
        adapter.setTaskType(getIntent().getIntExtra("task_type",17));
        handMainHospitalAdapter = new BaseMainReportAdapter(mcontext, 3, hospitalList);
        handMainHospitalAdapter.setTaskType(getIntent().getIntExtra("task_type",17));
        handMainHospitalAdapter.setItemClickListener(this);
        handMainHospitalAdapter.setHospital(true);
        planLv = (PullToRefreshListView) findViewById(R.id.planlist);
        planLv.setOnItemClickListener(this);
        headView = inflaterHead.inflate(R.layout.quality_headview_item, null);
        planLv.getRefreshableView().addHeaderView(headView);
        planLv.setOnRefreshListener(this);
        radioGroup = (RadioGroup) headView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        qipaoTv = (TextView) headView.findViewById(R.id.qipaoTv);
        handHhygieneLay = (LinearLayout) headView
                .findViewById(R.id.handHhygieneLay);
        headView.findViewById(R.id.messageLay).setOnClickListener(this);
        departLay = (LinearLayout) headView.findViewById(R.id.departLay);
        departmentTv = (TextView) headView.findViewById(R.id.departmentTv);
        departmentTv.setOnClickListener(this);
        formTypeTv = (TextView) headView.findViewById(R.id.formTypeTv);
        formTypeTv.setOnClickListener(this);
        empertyView = (LinearLayout) headView.findViewById(R.id.empertyView);
        handHhygieneLay.setOnClickListener(this);
        infectionLay = (LinearLayout) headView.findViewById(R.id.infectionLay);
        infectionLay.setOnClickListener(this);
        red_point = (ImageView) findViewById(R.id.red_point);
        otherJobTv = (TextView) headView.findViewById(R.id.otherJobTv);
        headView.findViewById(R.id.reportLay).setOnClickListener(this);
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
                        addTask(5);
                        break;
                    case 2:
                        addTask(4);
                        break;

                }
                popDialog.dismiss();

            }
        });
        formChoosePop = new FormChoosePop(mcontext, zFormList, new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                formTypeTv.setText(zFormList.get(position).getTitle());
                tempId = zFormList.get(position).getId();
                pageIndex2 = 1;
                pageCount = 0;
                departReportList.clear();
                adapter.notifyDataSetChanged();
                getTaskReportInfo();
                formChoosePop.dismiss();

            }
        });
        networkReciever = new NetworkReciever();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(networkReciever, intentFilter);
        initListView();
        showprocessdialog();
        setAdvPic();
        getCarouselFigure();
        registerBroadcast();
        setRequstDate();
        getTaskReportInfo();
        getTaskHospitalReportInfo();
        getFormCache();
        getFormListInfo();

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
    ;
    private static final int GETCAROUSELFIGURE_FAIL = 0x11001;

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
        if (null != brodcast) {
            unregisterReceiver(brodcast);
        }

        if (null != networkReciever) {
            unregisterReceiver(networkReciever);
        }
        if (null != worker) {
            worker.cancel(true);
        }
        if(null!=viewPager){
            viewPager.pauseScroll();
            viewPager.clearAnimation();
            viewPager=null;
        }
        super.onDestroy();
    }



    @Override
    protected void onChooseSuc(DepartVos.DepartmentListBean bean) {

    }

    @Override
    protected void onChooseSuc(String names, String ids) {
        departmentTv.setText(names);
        departIds =ids+ "";
        pageIndex2 = 1;
        pageCount = 0;
        departReportList.clear();
        adapter.notifyDataSetChanged();
        getTaskReportInfo();
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
                Intent in=new Intent(mcontext,QualityFormChooseActivity.class);
                in.putExtra("task_type",getIntent().getIntExtra("task_type",17));
                startActivity(in);
                break;
            case R.id.infectionLay:
                Intent intent=new Intent(mcontext,SearchHandTaskActivity.class);
                intent.putExtra("type",getIntent().getIntExtra("task_type",17));
                startActivity(intent);
                break;
            case R.id.departmentTv:
                onMutiChoose();
                break;
            case R.id.messageLay:
                Intent intent3=new Intent(mcontext,SupQuestionListActivity.class);
                intent3.putExtra("origin",getIntent().getIntExtra("task_type",17));
                startActivity(intent3);
                break;
            case R.id.formTypeTv:
                formChoosePop.show();
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

                resetData();

                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.AUTHENT_LOSE);
        intentFilter.addAction(UPDATA_ACTION);
        intentFilter.addAction(SDKCoreHelper.ACTION_KICK_OFF);
        registerReceiver(brodcast, intentFilter);
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
        departLay.setVisibility(View.GONE);
        if (TaskUtils.isOtherJob(mcontext)) {
            departLay.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            otherJobTv.setVisibility(View.VISIBLE);
            otherJobTv.setText("本月" + tools.getValue(Constants.DEFULT_DEPART_NAME) + "数据");
            planLv.setAdapter(adapter);
        } else if (TaskUtils.mysticalJob(mcontext)) {
            departLay.setVisibility(View.GONE);
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
        MainBizImpl.getInstance().onComomReqAllpath(requestInterface, this,
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



    private void setFormCache(JSONObject jsonObject) {
        Log.i("1111111", jsonObject.toString());
        SharedPreferencesUtil.saveString(mcontext, "zlyb_formlist_cache"+getIntent().getIntExtra("task_type",17), jsonObject.toString());
    }

    private void getFormCache() {
        String str = SharedPreferencesUtil.getString(mcontext, "zlyb_formlist_cache"+getIntent().getIntExtra("task_type",17), "");
        if (!TextUtils.isEmpty(str)) {
            ZformVo vo = TaskUtils.gson.fromJson(str, ZformVo.class);
            if (null != vo.getList()) {
                zFormList.clear();
                zFormList.addAll(vo.getList());
                formChoosePop.refesh();
                getTaskReportInfo();
            }
        }


    }

    private void setHospitalReport(JSONObject jsonObject) {
        planLv.onRefreshComplete();
        Log.i("1111111111hos", jsonObject.toString());
        QualityReportVo reporVo = gson.fromJson(jsonObject.toString(), QualityReportVo.class);
        if (null != reporVo.getList()) {
            hospitalList.addAll(reporVo.getList());
        }
        if (pageIndex1 >= pageCount) {
            planLv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        } else {
            planLv.setMode(PullToRefreshBase.Mode.BOTH);
        }
        if(radioGroup.getCheckedRadioButtonId()== R.id.rb0){
            if(hospitalList.size()>0){
                empertyView.setVisibility(View.GONE);
            }

        }
        pageIndex1++;
        handMainHospitalAdapter.notifyDataSetChanged();

    }

    private void setReport(JSONObject jsonObject) {
        Log.i("1111111111depart", jsonObject.toString());
        planLv.onRefreshComplete();
        QuaLityDepartReportVo reporVo = gson.fromJson(jsonObject.toString(), QuaLityDepartReportVo.class);
        pageCount2 = reporVo.getPageTotal();
        if (pageIndex2 >= pageCount2) {
            planLv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        } else {
            planLv.setMode(PullToRefreshBase.Mode.BOTH);
        }
        pageIndex2++;

        for (QuaLityDepartReportVo.ListBean listBean : reporVo.getList()) {
            departReportList.addAll(listBean.getTempList());
            for (QualityReportVo.ListBean listBean1 : listBean.getTempList()) {
                listBean1.setDepartmentId(listBean.getDepartmentId());
                listBean1.setDepartmentName(listBean.getDepartmentName());
            }

        }

        if(radioGroup.getCheckedRadioButtonId()== R.id.rb1){
            if (departReportList.size() <= 0) {
                empertyView.setVisibility(View.VISIBLE);
            } else {
                empertyView.setVisibility(View.GONE);
            }

        }else{
            empertyView.setVisibility(View.GONE);
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb0:
                isHospital = true;
                planLv.setAdapter(handMainHospitalAdapter);
                planLv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
                departLay.setVisibility(View.GONE);
                getTaskHospitalReportInfo();
                if (pageCount < (pageIndex1 - 1) * 5) {
                    planLv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
                } else {
                    planLv.setMode(PullToRefreshBase.Mode.BOTH);
                }
                break;
            case R.id.rb1:
                isHospital = false;
                planLv.setAdapter(adapter);
                if (TaskUtils.isOtherJob(mcontext)) {
                    departLay.setVisibility(View.GONE);
                } else if (TaskUtils.mysticalJob(mcontext)) {
                    departLay.setVisibility(View.GONE);
                } else {
                    departLay.setVisibility(View.VISIBLE);
                }
                if (pageCount2 < (pageIndex2 - 1) * 5) {
                    planLv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
                } else {
                    planLv.setMode(PullToRefreshBase.Mode.BOTH);
                }
                getTaskReportInfo();

                //planLv.setRefreshing(true);
                //   planLv.startLayoutAnimation();


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
        if (isHospital) {
            pageIndex1 = 1;
            pageCount = 0;
            hospitalList.clear();
            handMainHospitalAdapter.notifyDataSetChanged();
            getTaskHospitalReportInfo();
        } else {
            pageIndex2 = 1;
            pageCount2 = 0;
            departReportList.clear();
            adapter.notifyDataSetChanged();
            getTaskReportInfo();
        }


    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (isHospital) {
            getTaskHospitalReportInfo();
        } else {
            getTaskReportInfo();
        }

    }

    @Override
    public void Finshed(TaskVo task) {
        resetData();

    }

    void resetData(){
        hospitalList.clear();
        handMainHospitalAdapter.notifyDataSetChanged();
        departReportList.clear();
        adapter.notifyDataSetChanged();
        pageIndex1 = 1;
        pageCount = 0;
        pageIndex2 = 1;
        pageCount2 = 0;
        getTaskHospitalReportInfo();
        getTaskReportInfo();
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
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("pageIndex", pageIndex2);
            job.put("task_type", getIntent().getIntExtra("task_type",17));
            if (tempId == 0 && zFormList.size() > 0) {
                tempId = zFormList.get(0).getId();
                formTypeTv.setText(zFormList.get(0).getTitle());
            }

            if (tempId == 0) {
                if (departReportList.size() <= 0) {
                    empertyView.setVisibility(View.VISIBLE);
                } else {
                    empertyView.setVisibility(View.GONE);
                }
                dismissdialog();
                return;
            }
            job.put("tempId", tempId);
            if (!departIds.equals("")) {
                job.put("departmentIds", departIds);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(requestInterface, mcontext,
                GETTASKREPORTINFO, job, "quality/countReportByDepartment");

    }


    public void getTaskHospitalReportInfo() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("pageIndex", pageIndex1);
            job.put("task_type", getIntent().getIntExtra("task_type",17));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(requestInterface, mcontext,
                GETTASKHOSPITALREPORTINFO, job, "quality/countReportByHospital");

    }

    public void getFormListInfo() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("task_type", getIntent().getIntExtra("task_type",17));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(requestInterface, mcontext,
                GETT_FORM_INFO, job, "quality/getTemp");

    }

    class NetworkReciever extends NetBroadCastReciver {

        @Override
        public void ontWorkChange() {

        }

        @Override
        public void onNetWorkConnect() {

            if (null == worker) {
                worker = new Worker(QualityMainActivity.this);
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

    private class FormChoosePop extends BaseListDialog<RisistantVo.TaskInfoBean.TempListBean> {


        public FormChoosePop(Context context, List<RisistantVo.TaskInfoBean.TempListBean> list, OnItemClickListener listener) {
            super(context, list, listener);
        }

        @Override
        protected void intUi() {
            titleTv.setText("请选择模板");
            right_txt.setVisibility(View.GONE);

        }

        @Override
        public void setListDta(ViewHolder viewHolder, int position) {
            RisistantVo.TaskInfoBean.TempListBean listBean = zFormList.get(position);
            viewHolder.listtext.setText(listBean.getTitle());

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
