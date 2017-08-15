package com.deya.hospital.workspace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.hospital.adapter.TaskLisAdapter;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.task.Worker;
import com.deya.hospital.util.HomePageBanner;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.Tools;
import com.example.calendar.CalendarPagerAdapter;
import com.example.calendar.widget.ViewPagerHeightWrapContent;
import com.google.gson.Gson;

import org.joda.time.LocalDate;

public class WorkSpaceFragment extends BaseFragment{

    public static final String UPDATA_ACTION = "updateList";

    public static final int DELETE_SUCESS = 0x20060;
    public static final int DELETE_FAIL = 0x20061;// 是否关闭软文
    public static final int ADD_SUPQUESTION = 0x118;// 是否关闭软文

    private LayoutInflater inflater, inflaterHead;
    TextView workCalanderImg;
    private ViewPagerHeightWrapContent content_viewpager;
    private CalendarPagerAdapter mPagerAdapter;
    private Gson gson = new Gson();
    private Tools tools;
    private MyBrodcastReceiver brodcast;
    private ListView planLv;
    public static LocalDate currentday;
    // 上次的月份
    private LinearLayout creatBtn;
    TaskLisAdapter adapter;
    ImageView switchBtn;
    RelativeLayout empertyView;
    Context mcontext;
    private boolean isAfter;// 判断是否为预设任务
    private LocalDate localToday;
    private LinearLayout signInTv;
    public static boolean isShowALLTask = true;
    String monthMinDate = "";
    String monthMaxDate = "";
    String monthMaxDate2 = "";
    HomePageBanner viewPager;
    View headView;
    Worker worker;
    LinearLayout handHhygieneLay, superviousLay, infectionLay, moreLay, improveLay;
    private Animation animation;
    private TextView qipaoTv;
    ImageView hasQuestionImg;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstance) {
//
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.fragment_workspace, container,
//                    false);
//            this.inflater = inflater;
//            mcontext = getActivity();
//            inflaterHead = LayoutInflater.from(getActivity());
//            tools = new Tools(getActivity(), Constants.AC);
//            initMyHandler();
//            if (null == worker) {
//                worker = new Worker(this);
//            }
//            initView();
//            getAllLoacalTask();
//
//        } else {
//            ViewGroup parent = (ViewGroup) rootView.getParent();
//            if (parent != null) {
//                parent.removeView(rootView);
//            }
//        }
//        return rootView;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        setRequstDate();
//    }
//
//    private void setRequstDate() {
//        localToday = LocalDate.now();
//        monthMinDate = localToday.getYear() + "-" + localToday.getMonthOfYear()
//                + "-1";
//        monthMaxDate = localToday.getYear() + "-"
//                + (localToday.getMonthOfYear() + 1) + "-1" + "  " + "00:00";
//        monthMaxDate2 = localToday.getYear() + "-"
//                + (localToday.getMonthOfYear() + 1) + "-31";
//        // getCurrentMonthData(monthMinDate, monthMaxDate);
//
//    }
//
//    public void getAllLoacalTask() {
//        setCurrentData();
//    }
//
//    ImageView falshImg;
//    private TextView allTaskTv;
//    private List<AdvEntity.ListBean> pagerList = new ArrayList<AdvEntity.ListBean>();
//    private AnimationDrawable falshdrawable;
//
//
//    private void initView() {
//        adapter = new TaskLisAdapter(getActivity(), monthTaskList);
//        planLv = (ListView) findViewById(R.id.planlist);
//        signInTv = (LinearLayout) this.findViewById(R.id.signInTv);
//        signInTv.setOnClickListener(this);
//        falshImg = (ImageView) this.findViewById(R.id.falshImg);
//        falshImg.setBackgroundResource(R.drawable.canlender_flash);
//        falshdrawable = (AnimationDrawable) falshImg.getBackground();
//        falshdrawable.start();
//        animation = AnimationUtils
//                .loadAnimation(getActivity(), R.anim.applaud_animation);
//        headView = inflaterHead.inflate(R.layout.workspace_headview_item, null);
//        planLv.addHeaderView(headView);
//        hasQuestionImg = (ImageView) headView.findViewById(R.id.hasQuestionImg);
//        qipaoTv = (TextView) headView.findViewById(R.id.qipaoTv);
//        workCalanderImg = (TextView) rootView.findViewById(R.id.staticsImg);
//        allTaskTv = (TextView) headView.findViewById(R.id.allTaskTv);
//        allTaskTv.setOnClickListener(this);
//        headView.findViewById(R.id.checkTask).setOnClickListener(this);
//        moreLay = (LinearLayout) headView.findViewById(R.id.moreLay);
//        headView.findViewById(R.id.moreLay2).setOnClickListener(this);
//        moreLay.setOnClickListener(this);
//
//        handHhygieneLay = (LinearLayout) headView
//                .findViewById(R.id.handHhygieneLay);
//        handHhygieneLay.setOnClickListener(this);
//        superviousLay = (LinearLayout) headView
//                .findViewById(R.id.superviousLay);
//        superviousLay.setOnClickListener(this);
//        infectionLay = (LinearLayout) headView.findViewById(R.id.infectionLay);
//        infectionLay.setOnClickListener(this);
//
//        improveLay = (LinearLayout) headView.findViewById(R.id.improveLay);
//        improveLay.setOnClickListener(this);
//        workCalanderImg.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),
//                        CalendarMainActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        viewPager = (HomePageBanner) headView.findViewById(R.id.viewPager);
//
//        initListView();
//        if (!NetWorkUtils.isConnect(getActivity())) {
//            getAllLoacalTask();
//        }
//        setAdvPic();
//        getCarouselFigure();
//        initDialog();
//        getChangedSuprousList();
//        initData();
//        setRequstDate();
//        getTaskList();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            worker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else {
//            worker.execute();
//        }
//
//    }
//
//
//
//    /**
//     * setHomeBanner:【填充广告的数据】. <br/>
//     */
//    private static final int GETCAROUSELFIGURE_SUCCESS = 0x11000;
//    ;
//    private static final int GETCAROUSELFIGURE_FAIL = 0x11001;
//
//    private void setAdvPic() {
//        if (!(pagerList.size() > 0)) {
//            AdvEntity.ListBean advEntity1 = new AdvEntity.ListBean();
//            advEntity1.setDrawable(R.drawable.banner1);
//            advEntity1.setType(1);
//            advEntity1.setName("");
//            pagerList.add(advEntity1);
//
//            AdvEntity.ListBean advEntity2 = new AdvEntity.ListBean();
//            advEntity2.setDrawable(R.drawable.banner2);
//            advEntity2.setType(2);
//            advEntity2.setName("");
//            pagerList.add(advEntity2);
//            viewPager.clearAnimation();
//        }
//        setHomeBanner();
//    }
//
//    /**
//     * setHomeBanner:【填充广告的数据】. <br/>
//     */
//    private void setHomeBanner() {
//        if (1 == pagerList.size()) {
//            viewPager.setDelay(3).setPeriod(3).setAutoScrollEnable(false)
//                    .setTouchAble(false).setSource(pagerList).startScroll(); // 只有一张广告设置不能滑动可以点击
//        } else {// 否则执行自动滚动并设置为可以手动滑动也可以点击
//            viewPager.setDelay(3).setPeriod(3).setAutoScrollEnable(true)
//                    .setTouchAble(true).setSource(pagerList).startScroll();
//        }
//
//        viewPager.setOnItemClickL(new OnItemClickL() {
//
//            @Override
//            public void onItemClick(int position) {
//                if (pagerList != null && pagerList.size() > 0) {
//                    int type = pagerList.get(position).getType();
//                    switch (type) {
//                        case 0:
//                            Intent intent = new Intent(getActivity(), WebViewDemo.class);
//                            intent.putExtra("url", pagerList.get(position).getHref_url());
//                            startActivity(intent);
//                            break;
//                        case 1:
//                            OnStartActivity(ShopGoodsListActivity.class);
//                            break;
//                        case 2:
//                            OnStartActivity(QuestionSortActivity.class);
//                            break;
//
//                        default:
//                            break;
//                    }
//                }
//            }
//        });
//    }
//
//    public void OnStartActivity(Class<?> T) {
//        Intent inten = new Intent(getActivity(), T);
//        startActivity(inten);
//    }
//
//    private void initData() {
//
//        registerBroadcast();
//
//    }
//
//    @Override
//    public void onDestroy() {
//        if (null != brodcast) {
//            getActivity().unregisterReceiver(brodcast);
//        }
//        if (null != loacalWork) {
//            loacalWork.cancel(true);
//            loacalWork = null;
//        }
//        if (null != serviceTask) {
//            serviceTask.cancel(true);
//            serviceTask = null;
//        }
//        if(null!=worker){
//        worker.cancel(true);
//        }
//        dismisAlldialog();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == WorkSpaceFragment.ADD_SUPQUESTION) {
//            hasNewQuestion();
//        }
//
//
//    }
//
//
//    public void hasNewQuestion() {
//        // hasQuestionImg.setVisibility(View.VISIBLE);
//        qipaoTv.setVisibility(View.VISIBLE);
//        qipaoTv.startAnimation(animation);
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                qipaoTv.setVisibility(View.GONE);
//            }
//        }, 5000);
//    }
//
//    // 更新列表
//    List<TaskVo> needUpdateSupList = new ArrayList<TaskVo>();
//
//    public void getChangedSuprousList() {
//
//        String str = SharedPreferencesUtil.getString(mcontext, "save_sup_json",
//                "");
//        needUpdateSupList = gson.fromJson(str, new TypeToken<List<TaskVo>>() {
//        }.getType());
//
//    }
//
//    public void saveChangedSuprousList(TaskVo vo) {
//        needUpdateSupList.add(vo);
//        SharedPreferencesUtil.saveString(mcontext, "save_sup_json",
//                gson.toJson(needUpdateSupList));
//    }
//
//    private void getCurrentMonthData(String start, String last) {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        List<TaskVo> dateTaskList = new ArrayList<TaskVo>();
//        List<TaskVo> list = Tasker.getAllLocalTask();
//        for (int i = list.size() - 1; i >= 0; i--) {
//            TaskVo tv = list.get(i);
//            String datatime = tv.getMission_time();
//            Log.i("watchActivity", start + "---" + last + "----" + datatime);
//            Date time1;
//            try {
//                time1 = df.parse(datatime);
//                Date time2 = df.parse(start + "");
//                Date time3 = df.parse(last + "");
//                if (compareDate(time2, time3, time1)) {
//                    dateTaskList.add(tv);
//                }
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        taskList.addAll(dateTaskList);
//        adapter.setData(taskList);
//    }
//
//    public boolean compareDate(Date date, Date date2, Date currentday) {
//        Log.i("watchActivity", (currentday.compareTo(date) >= 0) + "");
//        return currentday.compareTo(date) >= 0
//                && currentday.compareTo(date2) <= 0;
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id) {
//            case R.id.signInTv:
//                OnStartActivity(SignInActivity.class);
//                break;
//            case R.id.allTaskTv:
//                OnStartActivity(StasticActivity.class);
//                break;
//            case R.id.checkTask:
//                OnStartActivity(CheckSupervisorActivity.class);
//                break;
//            case R.id.handHhygieneLay:
//                handDilog.show();
//                break;
//            case R.id.superviousLay:
//                superviousDialog.show();
//             //   addTask(2, false);
//                break;
//            case R.id.infectionLay:
//                addTask(3, false);
//                break;
//            case R.id.moreLay:
//                Intent it = new Intent(getActivity(),
//                  GuidListActivity.class);
//                it.putExtra("type", "3");
//                startActivity(it);
//                break;
//            case R.id.improveLay:
//                Intent it2 = new Intent(getActivity(),
//                        SupQuestionListActivity.class);
//                it2.putExtra("type", "3");
//                startActivity(it2);
//                break;
//            case R.id.moreLay2:
//                Intent it3 = new Intent(getActivity(),
//                        CompletlyWHOTaskActivity.class);
//                it3.putExtra("type", "3");
//                startActivity(it3);
//                break;
//            default:
//                break;
//        }
//    }
//
//    BootomSelectDialog handDilog, superviousDialog, infectionDialog;
//
//    public void initDialog() {
//        String titles1[] = {"多耐", "WHO手卫生观察(详细版)", "外科手卫生操作考核",
//                "手卫生消毒消耗量"};
//        String titles2[] = {"督导本", "三管"};
//        String titles3[] = {"医院感染管理临床质控", "湖南省医院感染管理临床质控"};
//        handDilog = new BootomSelectDialog(getActivity(), titles1,
//                new BottomDialogInter() {
//
//                    @Override
//                    public void onClick3() {
//                        addTask(5, false);
//                    }
//
//                    @Override
//                    public void onClick2() {
//                        addTask(1, false);
//                    }
//
//                    @Override
//                    public void onClick1() {
//                        addTask(8, true);
//
//                    }
//
//                    @Override
//                    public void onClick4() {
//                        addTask(4, false);
//
//                    }
//                });
//        infectionDialog = new BootomSelectDialog(getActivity(), titles3,
//                new BottomDialogInter() {
//
//                    @Override
//                    public void onClick3() {
//
//                    }
//
//                    @Override
//                    public void onClick2() {
//
//                        addTask(6, false);
//
//                    }
//
//                    @Override
//                    public void onClick1() {
//                        addTask(3, false);
//
//                    }
//
//                    @Override
//                    public void onClick4() {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//        superviousDialog = new BootomSelectDialog(getActivity(), titles2,
//                new BottomDialogInter() {
//
//                    @Override
//                    public void onClick3() {
//                        // TODO Auto-generated method stub
//
//                    }
//
//                    @Override
//                    public void onClick2() {
//                        // TODO Auto-generated method stub
//                        addTask(9, false);
//                    }
//
//                    @Override
//                    public void onClick1() {
//                        addTask(2, false);
//
//                    }
//
//                    @Override
//                    public void onClick4() {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//
//    }
//
//    /**
//     * 防止窗体泄漏
//     */
//    public void dismisAlldialog() {
//        if (null != handDilog && handDilog.isShowing()) {
//            handDilog.dismiss();
//        }
//        if (null != infectionDialog && infectionDialog.isShowing()) {
//            infectionDialog.dismiss();
//        }
//        if (null != superviousDialog && superviousDialog.isShowing()) {
//            superviousDialog.dismiss();
//        }
//        if (addScoredialog != null && addScoredialog.isShowing()) {
//            addScoredialog.dismiss();
//        }
//    }
//
//    public void addTask(int type, boolean iswho) {
//        Intent it = new Intent();
//        it.putExtra("time", localToday + "");
//        if (type != 4) {
//            it.putExtra("type", type + "");
//        }
//
////        if (iswho) {
////            it.putExtra("isWho", "1");
////        }
//        it.putExtra("isAfter", false);
//        switch (type) {
//            case 1:
//                it.setClass(getActivity(), HandWashTasksActivity.class);
//                startActivity(it);
//                break;
//            case 2:
//                it.setClass(getActivity(), SupervisorQueCreatActivity.class);
//                startActivityForResult(it, WorkSpaceFragment.ADD_SUPQUESTION);
//                break;
//            case 4:
//                it.setClass(getActivity(), ConsumptionFormActivity.class);
//                TaskVo tv = new TaskVo();
//                tv.setMission_time(localToday + "");
//                tv.setStatus(2);
//                it.putExtra("data", tv);
//                startActivity(it);
//                break;
//            case 3:
//                it.setClass(getActivity(), FormListActivity.class);
//                it.putExtra("time", localToday + "");
//                startActivity(it);
//                break;
//            case 5:
//                it.setClass(getActivity(), HandDisinfectionPrivewActivity.class);
//                startActivity(it);
//                break;
//
//            case 6://湘雅模版
//                it.setClass(getActivity(), XyFormListActivity.class);
//
//                it.putExtra("time", localToday + "");
//                startActivity(it);
//                break;
//            case 8://多耐
//                it.setClass(getActivity(), ResistantListActivity.class);
//                it.putExtra("code", "DN");
//                startActivity(it);
//                break;
//
//            case 9://三管
//                it.setClass(getActivity(), ResistantListActivity.class);
//                it.putExtra("code", "SG");
//                startActivity(it);
//                break;
//            default:
//                break;
//        }
//
//
//    }
//
//    private String getCurrentSelectedDate() {
//        String time = "";
//        return time;
//    }
//
//    // 软文模块
//    private void registerBroadcast() {
//        brodcast = new MyBrodcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.i("upload", "接收到了0");
//                if (intent.getAction().equals(UPDATA_ACTION)) {
//                    Log.i("upload", "接收到了0");
//                    setCurrentData();
//                } else if (intent.getAction().equals(
//                        CalendarCaStatisticsActivity.DELET_TASK_SUCESS)) {
//                    if (intent.hasExtra("dbid")) {
//                        int dbid = intent.getIntExtra("dbid", -1);
//                        if (dbid != -1) {
//                            setCurrentData();
//
//                        }
//                    }
//
//                }
//            }
//        };
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(UPDATA_ACTION);
//        intentFilter.addAction(CalendarCaStatisticsActivity.DELET_TASK_SUCESS);
//        getActivity().registerReceiver(brodcast, intentFilter);
//    }
//
//    private void initListView() {
//        planLv.setAdapter(adapter);
//        planLv.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                TaskVo tv = new TaskVo();
//                tv = monthTaskList.get(position - 1);
//                TaskUtils.onStaractivity(getActivity(), tv, monthTaskList);
//            }
//
//        });
//        planLv.setOnItemLongClickListener(new OnItemLongClickListener() {
//            Dialog deletDialog;
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view,
//                                           final int position, long id) {
//
//                deletDialog = new ComomDialog(getActivity(), "确认删除此任务？",
//                        R.style.SelectDialog, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        doDeleteTask(position - 1);
//                        deletDialog.dismiss();
//                    }
//                });
//                deletDialog.show();
//                return true;
//            }
//        });
//        empertyView = (RelativeLayout) rootView.findViewById(R.id.empertyView);
//
//    }
//
//    public void onStarTaskActivity(int status, TaskVo tv, Class<?> class1,
//                                   Class<?> class2) {
//        if (status == 2 || status == 3) {
//            Intent it = new Intent(getActivity(), class1);
//            it.putExtra("type", tv.getTask_type());
//            it.putExtra("time", tv.getMission_time());
//            it.putExtra(CreatPlansAcitivity.departName, tv.getDepartmentName());
//            it.putExtra(CreatPlansAcitivity.departId, tv.getDepartment());
//            it.putExtra("data", tv);
//            getActivity().startActivity(it);
//
//        } else if (status == 0) {
//            Intent toWeb = new Intent(getActivity(), ReportWebViewDemo.class);
//            toWeb.putExtra("task_url", WebUrl.TASKDETAILURL + tv.getTask_id());
//            toWeb.putExtra("title", "督导统计");
//            toWeb.putExtra("showbottom", "show");
//            toWeb.putExtra("task_id", tv.getTask_id() + "");
//            toWeb.putExtra("data", tv);
//            getActivity().startActivity(toWeb);
//        }
//    }
//
//    public String getFeedback(TaskVo tv) {
//        JSONObject job = new JSONObject();
//        try {
//            job.put("remark", tv.getRemark());
//            job.put("is_training", tv.getIs_training());
//            job.put("training_recycle", tv.getTraining_recycle());
//            job.put("equip_examine", tv.getEquip_examine());
//            job.put("feedback_obj", tv.getFeedback_obj());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return job.toString();
//    }
//
//    public String getHtml(TaskVo tv) {
//        List<planListDb> pdblist = gson.fromJson(tv.getFiveTasks().toString(),
//                new TypeToken<List<planListDb>>() {
//                }.getType());
//
//        JSONObject job = new JSONObject();
//        JSONObject json = new JSONObject();
//        String str = gson.toJson(pdblist);
//        JSONArray jarr2 = null;
//
//        try {
//            jarr2 = new JSONArray(str);
//        } catch (JSONException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//
//        try {
//            job.put("authent", tools.getValue(Constants.AUTHENT));
//            job.put("fiveTasks", jarr2);
//            job.put("department", tv.getDepartmentName());
//            String jobStr = AbStrUtil
//                    .parseStrToMd5L32(AbStrUtil.parseStrToMd5L32(job.toString())
//                            + "task/saveFiveTaskInfo");
//            json.put("token", jobStr);
//            json.put("msg_content", job.toString());
//            Log.i("11111111sendTask", job.toString());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return job.toString();
//    }
//
//    void doDeleteTask(int index) {
//        if (index >= monthTaskList.size()) {
//            return;
//        }
//        TaskVo tv = monthTaskList.get(index);
//        if (tv.getStatus() == 1) {
//            ToastUtils.showToast(getActivity(), "正在同步的任务暂时无法删除");
//            return;
//        } else if (tv.getStatus() != 0) {
//                Tasker.deleteTask(tv);
//                ToastUtils.showToast(getActivity(), "删除成功！");
//            setCurrentData();
//        } else {
//            for (TaskVo vo : taskList) {
//                if (vo.getTask_id() == tv.getTask_id()) {
//                    Log.i("taskId", tv.getTask_id() + "");
//                    deletTasks(vo.getTask_id(), tv.getType());
//                    break;
//                }
//            }
//
//        }
//    }
//
//    public int dp2Px(int dp) {
//        final float scale = getActivity().getResources().getDisplayMetrics().density;
//        return (int) (dp * scale + (int) 0.5f);
//    }
//
//    // 获取服务器任务数据
//    // 获取同步任务列表
//
//    public static List<TaskVo> taskList = new ArrayList<TaskVo>();
//
//    public void getTaskList() {
//        JSONObject job = new JSONObject();
//        try {
//            job.put("authent", tools.getValue(Constants.AUTHENT));
//            job.put("start_date", monthMinDate);
//            job.put("end_date", monthMaxDate);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
//                TASKLIST_SUCESS, TASKLIST_FAILE, job, "task/syncTaskInfos");
//
//    }
//
//    public int uploadStatus = 0;
//    public static final int ADD_PRITRUE_CODE = 9009;
//    // 压缩图片的msg的what
//    private static final int TASKLIST_SUCESS = 0x20010;
//    private static final int TASKLIST_FAILE = 0x20011;
//    private static final int SEND_TASK_SUCESS = 0x20012;
//    private static final int SEND_TASK_FIAL = 0x20013;
//    protected static final int UPLOADFILE_SUCESS = 0;
//    public static final int COMPRESS_IMAGE = 0x17;
//
//    public static final int REFRESHVIEWPAGER = 0x30;
//    public static final int REFRESHVIEWPAGER_NOTIFY = 0x31;
//    public static final int REFRESHVIEWPAGER_NOTIFY2 = 0x32;
//
//    public static final int REFRESHVIEWPAGER_LISTONLY = 0x33;
//    public static final int REFRESHVIEWPAGER_TASKADAPTER = 0X34;
//    public static final int REFRESHVIEWPAGER_CURRENTDATA = 0X35;
//
//    int picIndex = 0;
//    private MyHandler myHandler;
//
//    private void initMyHandler() {
//        myHandler = new MyHandler(activity) {
//            @Override
//            public void handleMessage(Message msg) {
//                Activity activity = myHandler.mactivity.get();
//                if (null != activity) {
//                    switch (msg.what) {
//                        case TASKLIST_SUCESS:
//                            if (null != msg && null != msg.obj) {
//                                Log.i("gettask", msg.obj + "");
//                                try {
//                                    setTaskList(new JSONObject(msg.obj.toString()));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                            break;
//                        case TASKLIST_FAILE:
//                            if (null != getActivity()) {
//                                setCurrentData();
//                                ToastUtils.showToast(getActivity(), "亲！您的网络不给力哦！");
//                            }
//                            break;
//
//                        case DELETE_SUCESS:
//                            if (null != msg && null != msg.obj) {
//                                Log.i("1111", msg.obj + "");
//                                try {
//
//                                    setDeletRes(new JSONObject(msg.obj.toString()));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                            break;
//
//                        case REFRESHVIEWPAGER_NOTIFY:
//                            adapter.notifyDataSetChanged();
//                            mPagerAdapter.notifyDataSetChanged();
//                            // refreshViewPager(false);
//                            break;
//                        case REFRESHVIEWPAGER_CURRENTDATA:
//                            refreshCurrentData();
//                            break;
//                        case REFRESHVIEWPAGER_TASKADAPTER:
//                            setCurrentData();
//                            break;
//                        case ADD_SUCESS:
//                            if (null != msg && null != msg.obj) {
//                                Log.i("1111msg", msg.obj + "");
//                                try {
//                                    setAddRes(new JSONObject(msg.obj.toString()));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            break;
//                        case ADD_FAILE:
//                            Log.i("1111msg", msg.obj + "");
//                            // ToastUtils.showToast(getActivity(), "");
//                            break;
//
//                        case GETCAROUSELFIGURE_SUCCESS:
//                            if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
//                                try {
//                                    AdvEntity entity = gson.fromJson(msg.obj.toString(), AdvEntity.class);
//                                    if (entity != null) {
//                                        if (entity.getResult_id().equals("0")) {
//                                            setRequestData(entity);
//                                        } else {
//                                            ToastUtils.showToast(getActivity(), entity.getResult_msg());
//                                        }
//                                    } else {
//                                        ToastUtils.showToast(getActivity(), "亲，您的网络不顺畅哦！");
//                                    }
//                                } catch (Exception e5) {
//                                    e5.printStackTrace();
//                                } finally {
//                                    setAdvPic();
//                                }
//                            }
//                            break;
//                        case GETCAROUSELFIGURE_FAIL:
//                            pagerList.clear();
//                            setAdvPic();
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            }
//        };
//    }
//
//    protected void setTaskList(JSONObject jsonObject) {
//        Log.i("111111111tasklist", jsonObject + "");
//        if (jsonObject.optString("result_id").equals("0")) {
//            final JSONArray jarr = jsonObject.optJSONArray("syncTaskInfos");
//
////            new Thread(new Runnable() {
////
////                @Override
////                public void run() {
////                    List<TaskVo> list = gson.fromJson(jarr.toString(),
////                            new TypeToken<List<TaskVo>>() {
////                            }.getType());
////                    if (list != null) {
////
////                    }
////                    myHandler.sendEmptyMessage(REFRESHVIEWPAGER_TASKADAPTER);
////                }
////            }).start();
//
//            if (null == jarr) {
//                return;
//            }
//
//            if (null != serviceTask) {
//                serviceTask.cancel(true);
//                serviceTask = null;
//            }
//
//            // serviceTask = new SysnServiceTask(jarr.toString());
//
//            List<TaskVo> list = gson.fromJson(jarr.toString(),
//                    new TypeToken<List<TaskVo>>() {
//                    }.getType());
//            if (list != null) {
//                Tasker.syncNetworkTask(list);
//            }
//            setCurrentData();
//        }
//
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
////            serviceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
////        } else {
////            serviceTask.execute();
////        }
//    }
//
//
//    public boolean compareDate(LocalDate date, LocalDate date2) {
//        return currentday.compareTo(date) < 0
//                || currentday.compareTo(date2) > 0;
//    }
//
//    // 设置当前数据，服务器数据+本地数据
//    List<TaskVo> monthTaskList = new ArrayList<TaskVo>();
//    SysnLocalTask loacalWork;
//    SysnServiceTask serviceTask;
//
//    private void setCurrentData() {
//        if (null != loacalWork) {
//            loacalWork.cancel(true);
//            loacalWork = null;
//        }
//        loacalWork = new SysnLocalTask();
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            loacalWork.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else {
//            loacalWork.execute();
//        }
//    }
//
//    /**
//     * 刷新本地任务
//     */
//    private class SysnLocalTask extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected void onProgressUpdate(Integer... progresses) {
//            int status = progresses[0];
//            Log.i("upload", "onProgressUpdate(Progress... progresses) called");
//
//        }
//
//        // onPostExecute方法用于在执行完后台任务后更新UI,显示结果
//        @Override
//        protected void onPostExecute(String result) {
//            refreshCurrentData();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            List<TaskVo> list = Tasker.getAllLocalTask();
//            taskList.clear();
//            taskList.addAll(list);
//            return "ok";
//        }
//    }
//
//    /**
//     * 更新服务器任务
//     */
//    private class SysnServiceTask extends AsyncTask<String, Integer, String> {
//        String jarr;
//
//        @Override
//        protected void onProgressUpdate(Integer... progresses) {
//            int status = progresses[0];
//
//        }
//
//        public SysnServiceTask(String jarr) {
//            this.jarr = jarr;
//        }
//
//        // onPostExecute方法用于在执行完后台任务后更新UI,显示结果
//        @Override
//        protected void onPostExecute(String result) {
//            setCurrentData();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            List<TaskVo> list = gson.fromJson(jarr.toString(),
//                    new TypeToken<List<TaskVo>>() {
//                    }.getType());
//            if (list != null) {
//                Tasker.syncNetworkTask(list);
//            }
//            return "ok";
//        }
//    }
//
//    public void refreshCurrentData() {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        monthTaskList.clear();
//        adapter.notifyDataSetChanged();
//        for (TaskVo tv : taskList) {
//            String datatime = tv.getMission_time();
//            Date time1;
//            try {
//                time1 = df.parse(datatime);
//                boolean isdayOfMonth = compareDate(df.parse(monthMinDate),
//                        df.parse(monthMaxDate2), time1);
//                if (isdayOfMonth) {
//                    monthTaskList.add(tv);
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        Log.i("task", gson.toJson(monthTaskList));
//        if (monthTaskList.size() <= 0) {
//            empertyView.setVisibility(View.VISIBLE);
//        } else {
//            empertyView.setVisibility(View.GONE);
//        }
//        Log.i("currentdata", "size=" + monthTaskList.size());
//        adapter.notifyDataSetChanged();
//    }
//
//    List<Attachments> localfiles = new ArrayList<Attachments>();// 本地任务中的图片
//    TaskVo pripartavo = new TaskVo();
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    // 数据加载模块
//    String imgurl = "";
//    int id;
//
//    /**
//     * 删除已提交任务请求
//     */
//    int deletTaskId = 0;
//
//    public void deletTasks(int taskId, String type) {
//        JSONObject job = new JSONObject();
//        try {
//            job.put("authent", tools.getValue(Constants.AUTHENT));
//            deletTaskId = taskId;
//            job.put("task_id", taskId + "");
//            job.put("type", type);// 手卫生的1，督导本2
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
//                DELETE_SUCESS, DELETE_FAIL, job, "task/deleteTaskById");
//    }
//
//    // 删除任务处理
//    protected void setDeletRes(JSONObject jsonObject) {
//        if (jsonObject.optString("result_id").equals("0")) {
//            for (TaskVo vo : taskList) {
//                if (vo.getTask_id() == deletTaskId) {
//                        Tasker.deleteTask(vo);
//                    break;
//                }
//            }
//            setCurrentData();
//            ToastUtils.showToast(getActivity(), "任务删除成功");
//        } else {
//            ToastUtils.showToast(getActivity(),
//                    jsonObject.optString("result_msg"));
//        }
//
//    }
//
//    Dialog addScoredialog;
//
//    protected void setAddRes(JSONObject jsonObject) {
//        Log.i("share_umeng", "返回次数");
//        Log.i("11111111", jsonObject.toString());
//        if (jsonObject.optString("result_id").equals("0")) {
//            int score = jsonObject.optInt("integral");
//            String str = tools.getValue(Constants.INTEGRAL);
//            if (null != str) {
//                tools.putValue(Constants.INTEGRAL, Integer.parseInt(str)
//                        + score + "");
//            } else {
//                tools.putValue(Constants.INTEGRAL, score + "");
//            }
//            if (score > 0) {
//                addScoredialog = new TipsDialog(activity, score + "");
//                addScoredialog.show();
//            }
//        }
//    }
//
//    @Override
//    public void Finshed(TaskVo task) {
//        setCurrentData();
//        // getTaskList();
//        getAddScore(getActivity(), "2");
//    }
//
//    @Override
//    public void RefreshNetwork() {
//        setCurrentData();
//        getTaskList();
//        getAddScore(getActivity(), "2");
//    }
//
//    protected static final int ADD_FAILE = 0x60089;
//    protected static final int ADD_SUCESS = 0x60090;
//
//    public void getAddScore(Activity activity, String id) {
//        Log.i("share_umeng", "111111111111111");
//        JSONObject job = new JSONObject();
//        try {
//            job.put("authent", tools.getValue(Constants.AUTHENT));
//            job.put("aid", id);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
//                ADD_SUCESS, ADD_FAILE, job, "goods/actionGetIntegral");
//    }
//
//    public void getCarouselFigure() {
//        JSONObject job = new JSONObject();
//        try {
//            job.put("authent", tools.getValue(Constants.AUTHENT));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
//                GETCAROUSELFIGURE_SUCCESS, GETCAROUSELFIGURE_FAIL, job, "comm/getCarouselFigure");
//
//    }
//
//    private void setRequestData(AdvEntity entity) {
//        if (entity.getList() != null && entity.getList().size() > 0) {
//            viewPager.setDelay(0).setPeriod(0).setAutoScrollEnable(false)
//                    .setTouchAble(false).setSource(pagerList).pauseScroll();
//            pagerList.clear();
//            pagerList.addAll(entity.getList());
//        }
//    }
//
//    @Override
//    public void workFinish() {
//        worker.cancel(true);
//        worker = null;
//        worker = new Worker(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            worker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else {
//            worker.execute();
//        }
//
//    }
}
