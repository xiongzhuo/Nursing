package com.deya.hospital.workspace.handwash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.FileLisAdapter;
import com.deya.hospital.adapter.FinishTimesAdapter;
import com.deya.hospital.adapter.FiveMethodsListAdapter;
import com.deya.hospital.adapter.JobListAdapter;
import com.deya.hospital.adapter.JobTimesListAdapter;
import com.deya.hospital.adapter.MethodsListAdapter;
import com.deya.hospital.adapter.MutichooseListAdapter;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.base.DeletDialog;
import com.deya.hospital.base.DepartChooseActivity;
import com.deya.hospital.base.img.TipsDialogRigister;
import com.deya.hospital.supervisor.UnTime2Activity;
import com.deya.hospital.task.Tasker;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DYHorizontalScrollView;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ThreadPoolUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.vo.HospitalInfo;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.vo.RulesVo;
import com.deya.hospital.vo.WrongRuleVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.vo.dbdata.planListDb;
import com.deya.hospital.vo.dbdata.subTaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.WorkSpaceFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 手卫生-新建任务
 */
public class HandWashTasksActivity extends HandBaseActivity implements
        OnClickListener {
    LinearLayout personlistView;
    DYHorizontalScrollView dyscroller;
    LinearLayout jobTimesListView;
    ListView typelistView;
    ListView methodListView;
    ListView finishListView, fileListView;
    TextView moreTv;
    ScrollView scroll;
    MethodsListAdapter mAdapter;
    FiveMethodsListAdapter metodsAdapter;
    int personIndex = -1;
    Button continueBtn, finishBtn;
    TextView titleTv, departTv, personTv, qipaoTv, typeTv;
    View unchooseline, choosline, methodline;// 间隔线
    List<subTaskVo> finishList;
    FinishTimesAdapter finishAdapter;
    private TextView totalTv;
    ImageView imgBack;
    LinearLayout groupLayout;
    FileLisAdapter fileAdapter;
    TextView totalfileTv, totalfileTvQipao;
    View taskLine, fileLine;
    int subtaskId = 0;
    MutichooseListAdapter chooseAdapter;
    List<JobListVo> jobTimesList;
    JobTimesListAdapter jobTimeAdapter;
    View addPerView;
    String str;
    Button addpersonBtn;
    LinearLayout personIndecatorlay;
    List<RulesVo> chooseRules;
    EndDialog endDilog;


    public static String[] personItem = {"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z","AA","AB","AC","AD","AF","AG","AH","AJ","AK" };

    boolean isCheck[] = {false, false, false, false, false};
    private LayoutParams  para2;
    private Animation animation;
    Timer timer;
    TimerTask timerTask;
    static int minute = 20;
    static int second = 0;
    private TipsDialogRigister baseTipsDialog;

    void startTimer() {
        if (null == tv.getTotalNum()) {
            tv.setTotalNum(0 + "");
        }
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                handler2.sendMessage(msg);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);


    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask = null;
        }
        super.onDestroy();
    }

    TextView backText;

    int pageCount = 4;
    int itemPersonWith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dotasks_activity2);

        inflater = LayoutInflater.from(mcontext);
        int[] wh = AbViewUtil.getDeviceWH(mcontext);
        initMyhandler();
        item_width = (wh[0] - dp2Px(mcontext, 80)) / pageCount;
        itemPersonWith = wh[0] / pageCount;
        para2 = new LinearLayout.LayoutParams(item_width, item_width);
        tools = new Tools(mcontext, Constants.AC);

        finishList=new ArrayList<>();
        jobTimesList=new ArrayList<>();
        chooseRules = new ArrayList<>();
        jobList = new ArrayList<>();
        findDbJobList();
        getJobCacheData();

        if (savedInstanceState != null) {
            tv = (TaskVo) savedInstanceState.getSerializable("tv");
            minute = savedInstanceState.getInt("minute");
            second = savedInstanceState.getInt("second");
        }

        initViews();
        ThreadPoolUtil.getInstant().execute(new Runnable() {

            @Override
            public void run() {
                getWrongRules();

            }
        });

        popAdapter = new JobListAdapter(mcontext, jobList);
        qipaoTv = (TextView) this.findViewById(R.id.qipaoTv);

    }

    @Override
    public boolean getdefultState() {
        return false;
    }

    @Override
    public void onCheckAll(boolean ischeck) {

    }

    @Override
    protected void onChooseDepartList(String name, String id) {
        departTv.setText(name);
        tv.setDepartment(id);
        tv.setDepartmentName(name);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        outState.putSerializable("tv", tv);
        outState.putInt("minute", minute);
        outState.putInt("second", second);
        super.onSaveInstanceState(outState);
    }

    private Tools tools;

    private void initViews() {
        intBasedata();
        initFinishDialog();
        departTv = (TextView) this.findViewById(R.id.departTv);
        departTv.setOnClickListener(this);
        animation = AnimationUtils
                .loadAnimation(this, R.anim.applaud_animation);
        totalTv = (TextView) this.findViewById(R.id.totalTv);
        titleTv = (TextView) this.findViewById(R.id.titleTv);
        totalTv.setOnClickListener(this);
        totalfileTv = (TextView) this.findViewById(R.id.totalfileTv);
        totalfileTv.setOnClickListener(this);
        imgBack = (ImageView) this.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        typeTv = (TextView) this.findViewById(R.id.typeTv);
        backText = (TextView) this.findViewById(R.id.backText);
        taskLine = this.findViewById(R.id.taskline);
        fileLine = this.findViewById(R.id.fileline);
        backText.setOnClickListener(this);
        metodsAdapter = new FiveMethodsListAdapter(mcontext);
        mAdapter = new MethodsListAdapter(mcontext);

        addpersonBtn = (Button) this.findViewById(R.id.addpersonBtn);
        addpersonBtn.setOnClickListener(this);
        personIndecatorlay = (LinearLayout) this.findViewById(R.id.personIndecatorlay);
        dyscroller = (DYHorizontalScrollView) this.findViewById(R.id.dyscroller);
        dyscroller.setHandler(myHandler);
        dyscroller.setOnScrollStateChangedListener(new DYHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(DYHorizontalScrollView.ScrollType scrollType) {

            }

            @Override
            public void onScrollStop(DYHorizontalScrollView.ScrollType scrollType, int x) {
                int endx;
                if (x % itemPersonWith > 0) {
                    if (x % itemPersonWith >= (itemPersonWith / 2)) {
                        endx = x - (x % itemPersonWith) + itemPersonWith;
                        dyscroller.smoothScrollTo(endx, 0);
                    } else {
                        endx = x - (x % itemPersonWith);
                        dyscroller.smoothScrollTo(endx, 0);
                    }
                } else {
                    endx = x;
                }
                Log.i("111111111", endx + "=====" + itemPersonWith * (personIndex));

                /**
                 * 如果被选中项，在滑动停止后还可见，则不需要切换选中项
                 * 逻辑为选中项的末端减去scrollview的初始位置大于一个item宽度小于屏幕总项数宽度
                 */
                if (itemPersonWith * (personIndex + 1) - endx >= itemPersonWith && itemPersonWith * (personIndex + 1) - endx <= 3 * itemPersonWith) {
                    return;
                }
                setPersonChoosed(endx / itemPersonWith + 2);
            }
        });
        addPerView = inflater.inflate(R.layout.adapter_handtask_person, null);
        LinearLayout child = (LinearLayout) this.findViewById(R.id.addpersonlayout);
        child.setLayoutParams(para2);
        child.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showJobDialog(-1, "", "", "");
                //addperson();
            }
        });
        if (tools.getValue_int(HandWashSettingActivity.SHOW_FRAM_MORE, 0) == 0) {
            findViewById(R.id.frambg).setVisibility(View.VISIBLE);
            findViewById(R.id.frambg).setOnClickListener(this);
            findViewById(R.id.moreTipsView).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.frambg).setVisibility(View.GONE);
                    tools.putValue(HandWashSettingActivity.SHOW_FRAM_MORE, 1);
                }
            });
        }

        personlistView = (LinearLayout) this
                .findViewById(R.id.personlistView);
        addPerView.findViewById(R.id.layout).setLayoutParams(para2);
        personlistView.addView(addPerView);
        jobTimesListView = (LinearLayout) this
                .findViewById(R.id.jobTimesListView);
        ResetJobTimesListView();
        fileAdapter = new FileLisAdapter(mcontext, fileList);
        jobTimeAdapter = new JobTimesListAdapter(mcontext, jobTimesList);
        moreTv = (TextView) this.findViewById(R.id.moreTv);
        moreTv.setOnClickListener(this);
        typelistView = (ListView) this.findViewById(R.id.typelistView);
        methodListView = (ListView) this.findViewById(R.id.listView2);
        methodListView.setAdapter(mAdapter);
        typelistView.setAdapter(metodsAdapter);
        personTv = (TextView) this.findViewById(R.id.person);
        unchooseline = this.findViewById(R.id.unchooseline);
        choosline = this.findViewById(R.id.chooseline);
        methodline = this.findViewById(R.id.methodline);
        groupLayout = (LinearLayout) this.findViewById(R.id.personGroup);
        finishAdapter = new FinishTimesAdapter(mcontext, finishList, jobList);
        typelistView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (personIndex != -1) {
                    if (position == 3) {
                        if (isCheck[4]) {
                            // ToastUtils.showToast(mcontext,
                            // "接触患者后和接触患者周边环境后不能同时选择");
                            // return;
                            isCheck[4] = false;
                        }
                    }
                    if (position == 4) {
                        if (isCheck[3]) {
                            // ToastUtils.showToast(mcontext,
                            // "接触患者后和接触患者周边环境后不能同时选择");
                            // return;
                            isCheck[3] = false;
                        }
                    }

                    if(position==2&&mAdapter.getMethodType()==2){
                        mAdapter.setIsCheck(-1);
                        setContinueBtn(false);
                        mAdapter.notifyDataSetChanged();
                    }
                    isCheck[position]=!isCheck[position];
                    metodsAdapter.setIsCheck(position, isCheck[position]);
                    boolean cancheck = false;
                    for(boolean check:isCheck){
                        if(check){
                            cancheck = true;
                            break;
                        }
                    }
                    if (cancheck) {
                        unchooseline.setVisibility(View.GONE);
                        choosline.setVisibility(View.VISIBLE);
                        methodline.setBackgroundResource(R.color.check_corlor);
                    } else {
                        setContinueBtn(false);
                        methodline.setBackgroundResource(R.color.devider);
                    }
                    mAdapter.canCheck(cancheck);
                } else {
                    ToastUtils.showToast(mcontext, "请先添加被观察对象！");
                    return;

                }

            }

        });
        methodListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }

        });

        finishListView = (ListView) this.findViewById(R.id.finishTimesList);
        fileListView = (ListView) this.findViewById(R.id.fileListView);
        finishListView.setAdapter(finishAdapter);
        fileListView.setAdapter(fileAdapter);
        finishListView
                .setOnItemLongClickListener(new OnItemLongClickListener() {
                    Dialog dialog;

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view, final int position, long id) {

                        dialog = new DeletDialog(mcontext,
                                R.style.SelectDialog,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        A:for (int i = 0; i < list.size(); i++) {
                                            for (int j = 0; j < list.get(i)
                                                    .getSubTasks().size(); j++) {
                                                if (list.get(i).getSubTasks()
                                                        .get(j).getSubtaskId() == finishList
                                                        .get(position)
                                                        .getSubtaskId()) {
                                                    if (null != list.get(i)
                                                            .getSubTasks().get(j)) {
                                                        list.get(i).getSubTasks()
                                                                .remove(j);
                                                        setPersonByIndex(i);
                                                    }
                                                    break A;
                                                }
                                            }

                                        }

                                        finishList.remove(position);
                                        tv.setTotalNum(finishList.size() + "");

                                        totalTv.setText("已完成("
                                                + finishList.size() + ")");
                                        finishAdapter.notifyDataSetChanged();
                                        if (!colType.equals("1")) {
                                            titleTv.setText("完成:"
                                                    + finishList.size() + "/"
                                                    + colType);
                                        } else {
                                            if (tv.isUpdatedTask()) {
                                                titleTv.setText("完成:"
                                                        + finishList.size());
                                            }
                                        }
                                        String str = gson.toJson(list);
                                        tv.setFiveTasks(str);
                                        updataDb(personIndex);
                                        setPersonByIndex(personIndex);
                                        //pAdapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                    }

                                });
                        // finishList.remove(position);
                        // finishAdapter.notifyDataSetChanged();

                        dialog.show();
                        return false;
                    }
                });
        typelistView.setFocusable(false);
        scroll = (ScrollView) this.findViewById(R.id.scroll);
        scroll.smoothScrollTo(0, 20);

        continueBtn = (Button) this.findViewById(R.id.continueBtn);
        continueBtn.setEnabled(false);
        finishBtn = (Button) this.findViewById(R.id.finishBtn);

        totalfileTvQipao = (TextView) this.findViewById(R.id.totalfileTvpop);
        continueBtn.setOnClickListener(this);
        finishBtn.setOnClickListener(this);
        setEndButton(false);
        departs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HospitalInfo dv = new HospitalInfo();
            dv.setName("戴手套");
            departs.add(dv);
        }
        baseTipsDialog = new TipsDialogRigister(mcontext, new MyDialogInterface() {
            @Override
            public void onItemSelect(int postion) {

            }

            @Override
            public void onEnter() {
                if (tv.getTask_id()>0|| list.size() <= 0) {
                    finish();
                    return;
                }
                setJob();
                updateDb(tv);
                finish();
            }

            @Override
            public void onCancle() {
                finish();
            }
        });
        setViewData();
        initFeedData();//初始化现场反馈数据
    }


    private void initFinishDialog() {
        endDilog = new EndDialog(mcontext, R.style.SelectDialog);
        endDilog.setClickInter(new EndDialog.FinishInter() {
            @Override
            public void onEnter() {
                finishTask();
            }

            @Override
            public void onCancle() {
                iscontinuTask = true;

            }
        });
    }

    List<HospitalInfo> departs;

    public void showChooseDialog(int position) {
        int showMuti = tools.getValue_int(HandWashSettingActivity.IS_WHO, 1);
        if (showMuti == 1) {
            MultiChooseDialog dialog = new MultiChooseDialog(mcontext, ruleList
                    .get(position).getItems());
            dialog.show();
        }
    }




    private void selectItem(int position) {
        setContinudata();
        setContinueBtn(false);
        personIndex = position;
        personTv.setText("(选中被调查人：" + list.get(personIndex).getPname() + ")");
        resetFiveMethodAdapter();

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelect_status(false);
        }
        list.get(position).setSelect_status(true);
    }

    // 重置指针选中状态
    public void resetFiveMethodAdapter() {
        for (int i = 0; i < isCheck.length; i++) {
            isCheck[i] = false;
        }
        metodsAdapter.reset();// 重置指针选中状态
        mAdapter.ResetAdapter();

    }

    public int dp2Px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + (int) 0.5f);
    }

    // 设置继续按钮的状态
    public void setContinueBtn(boolean enable) {
        continueBtn.setEnabled(enable);
        if (!enable) {
            unchooseline.setVisibility(View.VISIBLE);
            choosline.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moreTv:

//                if (!isOldUserOrChecked(mcontext)) {
//                    CommonUtils.showTips(HandWashTasksActivity.this, R.drawable.ic_register_checktip);
//                    return;
//                }

                fileAdapter.stoPplayMedia();
                showsettingPopWindow(mcontext, findViewById(R.id.top));
                break;
            case R.id.continueBtn:
                setContinueBtn(false);
                setContinudata();

                break;
            case R.id.finishBtn:
                finishTask();
                break;
            case R.id.img_back:
            case R.id.backText:
                if(tv.getDbid()<=0){
                    baseTipsDialog.show();
                    baseTipsDialog.setContent("是否保存?");
                    baseTipsDialog.setButton("保存");
                    baseTipsDialog.setCancleButton("不保存");
                }else{
                    finish();
                }
                break;
            case R.id.totalfileTv:
                dialToatalFileTv();
                break;
            case R.id.totalTv:
                dialToaltalTaskTV();
                break;
            case R.id.departTv:
                Intent intent=new Intent(mcontext, DepartChooseActivity.class);
                startActivityForResult(intent,DepartChooseActivity.CHOOSE_SUC);
                break;
            case R.id.addpersonBtn:
                //  showJobDialog(-1, "", "", "");
                break;
            default:
                break;
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if(tv.getDbid()<=0){
                baseTipsDialog.show();
                baseTipsDialog.setContent("是否保存");
                baseTipsDialog.setButton("保存");
                baseTipsDialog.setCancleButton("不保存");
            }else{
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setLastTimes() {
        tv.setMinute(minute);
        tv.setSeconds(second);
    }

    private void dialToaltalTaskTV() {
        if (null == finishListView) {
            return;
        }
        if (finishListView.getVisibility() != View.VISIBLE) {
            scroll.smoothScrollTo(0, 150);
            finishListView.setVisibility(View.VISIBLE);
            fileListView.setVisibility(View.GONE);
            totalTv.setBackgroundResource(R.color.white);
            totalfileTv.setBackgroundResource(R.color.color_f5f5f5);
            fileLine.setVisibility(View.VISIBLE);
            taskLine.setVisibility(View.GONE);
        }
    }

    private void dialToatalFileTv() {
        if (fileListView.getVisibility() != View.VISIBLE) {
            scroll.scrollTo(100, 100);
            finishListView.setVisibility(View.GONE);
            fileListView.setVisibility(View.VISIBLE);
            totalfileTv.setBackgroundResource(R.color.white);
            totalTv.setBackgroundResource(R.color.color_f5f5f5);
            taskLine.setVisibility(View.VISIBLE);
            fileLine.setVisibility(View.GONE);
        }
    }

    private void setJob() {
        if (list.size() > 0) {
            str = gson.toJson(list);
        }

        tv.setStatus(2);
        tv.setFiveTasks(str);

    }

    public void finishTask() {
        if (tv.getDepartment().equals("")) {
            ToastUtil.showMessage("请选择科室");
            Intent intent=new Intent(mcontext, DepartChooseActivity.class);
            startActivityForResult(intent,DepartChooseActivity.CHOOSE_SUC);
            return;
        }
        setContinudata();
        setContinueBtn(false);
        Intent it = new Intent(mcontext, UnTime2Activity.class);
        String str = gson.toJson(list);
        tv.setFiveTasks(str);
        it.putExtra("data", tv);
        it.putExtra("colType", colType);
        if (colType.equals("1")) {
            it.putExtra("timetype", "不限时机");
        } else {
            it.putExtra("timetype", typeTv.getText().toString());
        }
        tv.setMinute(minute);
        tv.setSeconds(second);
        updataDb(personIndex);
        updateDb(tv);
        startActivityForResult(it, REQUEST_SAVE_REMARK);
        // startActivity(it);
    }

    // 执行切换后保存数据
    public void setContinudata() {
        // 重置页面，将已选数据添加到list
        dialToaltalTaskTV();
        List<subTaskVo> sblist = new ArrayList<>();
        String strType = "";
        for (int i = 0; i < isCheck.length; i++) {
            if (isCheck[i]) {
                if (null != list.get(personIndex).getSubTasks()) {
                    sblist = list.get(personIndex).getSubTasks();
                    switch (i) {
                        case 0:
                            strType += i;
                            break;
                        case 1:
                            strType += 2;
                            break;
                        case 2:
                            strType += 3;
                            break;
                        case 3:
                            strType += 1;
                            break;
                        case 4:
                            strType += i;
                            break;
                        default:
                            break;
                    }

                }

            }
        }
        subTaskVo sb = new subTaskVo();

        if (strType.length() < 1 || mAdapter.getMethodType() == -1) {
            return;
        }
        sb.setResults(mAdapter.getMethodType() + "");
        sb.setCol_type(strType + "");

        int result = Integer.parseInt(sb.getResults());

        // 存放不规则选项
        if (result == 4 || result == 5 || result == 6) {
            if (chooseRules.size() > 0) {
                List<RulesVo> wrongRules = new ArrayList<>();
                wrongRules.addAll(chooseRules);
                sb.setUnrules(wrongRules);

            }
        }

        sb.setSubtaskId(subtaskId);
        subtaskId++;
        if (saveAttachmentsList.size() > 0) {
            List<Attachments> attList = new ArrayList<>();
            attList.addAll(saveAttachmentsList);
            sb.setAttachments(attList);
            saveAttachmentsList.clear();// 清除临时保存时机的文件
        }

        sblist.add(sb);
        finishList.add(0, sb);

        // qipaoTv.setText("+" + strType.length());
        qipaoTv.setVisibility(View.VISIBLE);
        qipaoTv.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                qipaoTv.setVisibility(View.GONE);
            }
        }, 1000);
        list.get(personIndex).setSubTasks(sblist);
        resetFiveMethodAdapter();
        updataDb(personIndex);


        tv.setSaveAttachmentsList("");
        chooseRules.clear();
    }

    // 设置结束观察按钮
    private void setEndButton(boolean showDialog) {
        int num1;
        if (AbStrUtil.isEmpty(tv.getTotalNum())) {
            num1 = 0;
        } else {
            num1 = Integer.parseInt(tv.getTotalNum());
        }

        int num2 = Integer.parseInt(colType);
        if (num1 >= num2 && num2 >= 0) {
            finishBtn.setEnabled(true);
            if (showDialog&&!iscontinuTask && !colType.equals("1") && !tv.isUpdatedTask()) {
                endDilog.show();
                endDilog.setTitleTxt("您的督导已达设定时机数，是否提交？");
            }

        }
        if(num1>0) {
            finishBtn.setEnabled(true);
        }else{
            finishBtn.setEnabled(false);
        }

    }

    double rate;
    double rt_rate;
    private int state = 2;
    String progress = "0";
    public static final int SETENDBUTTION = 0x500102;
    public static final int SETETOATAL = 0x500103;
    public static final int SETETITLE = 0x500109;
    public static final int FRESHADAPTER = 0x500105;

    private void updataDb(int index) {

        int num = 0;

        int unDoNum = 0;// 没洗次数
        int errorNum = 0;
        jobTimesList.clear();
        for (JobListVo jCache : jobList) {
            JobListVo jv = new JobListVo();
            jv.setId(jCache.getId());
            jv.setName(jCache.getName());
            jobTimesList.add(jv);
        }
        for (planListDb pdb : list) {
            for (JobListVo jTL : jobTimesList) {
                if (pdb.getPpost().equals(jTL.getId())) {
                    jTL.setTaskNum(jTL.getTaskNum() + pdb.getSubTasks().size());
                } else if (pdb.getPpost().equals("0")) {
                    if (jTL.getId().equals("11")) {
                        jTL.setTaskNum(jTL.getTaskNum()
                                + pdb.getSubTasks().size());
                    }

                }
            }
            refreshJobTimesAdapter();
            for (subTaskVo sv : pdb.getSubTasks()) {
                sv.setPname(pdb.getPname());
                sv.setPpoName(pdb.getPpostName());
                sv.setWorkName(pdb.getWorkName());
                num = num + 1;
                if (sv.getResults().equals("0") || sv.getResults().equals("3")
                        || sv.getResults().equals("6")) {
                    unDoNum = unDoNum + 1;
                }
                if (sv.getResults().equals("4") || sv.getResults().equals("5")) {
                    errorNum = errorNum + 1;
                }

            }

        }
        tv.setTotalNum(finishList.size() + "");
        int size = finishList.size();
        Message msg = new Message();
        msg.what = SETETOATAL;
        msg.obj = size;
        myHandler.handleMessage(msg);
        myHandler.sendEmptyMessage(FRESHADAPTER);
        double a = Double.parseDouble((num - unDoNum) + "")
                / Double.parseDouble(num + "");
        // String b = new DecimalFormat("###,###,###.####").format(a);
        double b = Double.parseDouble((num - unDoNum - errorNum) + "")
                / Double.parseDouble((num - unDoNum) + "");
        rate = Math.round(a * 10000);
        rt_rate = Math.round(b * 10000);
        if (colType.equals("1")) {

            // titleTv.setText(num + "");

            if (num >= 1) {
                progress = "100";
            }
            if (Integer.parseInt(progress) > 100) {
                progress = "100";
            }
            if (tv.isUpdatedTask()) {
                titleTv.setText("完成：" + num);
            }
        } else {
            progress = ((num * 100 / Integer.parseInt(colType)) + "");
            if (Integer.parseInt(progress) >= 100) {
                progress = "100";
            }
            if (num / Integer.parseInt(colType) >= 1) {
            }
            titleTv.setText("完成：" + num + "/" + colType);
        }
        // if (progress.equals("100")) {
        // save.setTextColor(getResources().getColor(R.color.white));
        // }
        String str = gson.toJson(list);
        tv.setStatus(state);
        tv.setFiveTasks(str);
        int rb = (int) Math.floor(rate / 100);
        tv.setYc_rate(rb + "");
        tv.setYc((num - errorNum - unDoNum) + "");
        tv.setValid_rate((int) Math.floor(rt_rate / 100) + "");
        tv.setProgress(progress);
        myHandler.sendEmptyMessage(SETENDBUTTION);
        setPersonByIndex(index);
    }

    public void setTitleTv(int num) {
        titleTv.setText("完成：" + num + "/" + colType);
    }

    public void setToatleTv(int num) {
        totalTv.setText("已完成(" + num + ")");
    }

    public void refreshAdapter() {
        finishAdapter.setdata(finishList);
    }

    // 更新数据库
    public void updateDb(TaskVo tv) {
        setLastTimes();
        if (tv.getTask_id() <= 0) {
            if (tv.getDbid() <= 0) {
                tv.setMission_time(TaskUtils.getTaskMissionTime(date));
                tv.setStatus(2);
                tv.setMobile(tools.getValue(Constants.MOBILE));
                tv.setType("1");
                String types = tools.getValue(HandWashSettingActivity.COLLTYPE);
                tv.setTask_type(types);
                tv.setHospital(tools.getValue(Constants.HOSPITAL_ID));

                TaskUtils.onAddTaskInDb(tv);

                List<TaskVo> list = Tasker.getAllDbTask();
                if (list.size() > 0) {
                    tv.setDbid(list.get(list.size() - 1).getDbid());
                }
            }else{
                if(tv.getIsAfterTask()==1&&AbStrUtil.isEmpty(tv.getTask_type())){
                    String types = tools.getValue(HandWashSettingActivity.COLLTYPE);
                    tv.setTask_type(types);
                }
                TaskUtils.onUpdateTaskById(tv);
            }
        }

        Intent brodcastIntent = new Intent();
        brodcastIntent.setAction(WorkSpaceFragment.UPDATA_ACTION);
        sendBroadcast(brodcastIntent);

    }

    List<JobListVo> jobList;
    String jobs[];

    private void findDbJobList() {
        try {
            if (null != DataBaseHelper
                    .getDbUtilsInstance(mcontext).findAll(JobListVo.class)) {
                jobList = DataBaseHelper
                        .getDbUtilsInstance(mcontext).findAll(JobListVo.class);

                jobs = new String[jobList.size()];

                List<JobListVo> temp = new ArrayList<>();
                JobListVo other = new JobListVo();
                int k = 0;
                for (int i = 0, j = jobList.size(); i < j; i++) {
                    JobListVo jv = jobList.get(i);
                    if (jv.getName().equals("其他")) {
                        other = jv;
                    } else {
                        temp.add(jv);
                        jobs[k] = jv.getName();
                        k++;
                    }

                }
                jobList.clear();
                jobList.addAll(temp);
                jobList.add(other);
                jobs[jobList.size() - 1] = other.getName();

            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    JobListAdapter popAdapter;

    private String date;
    private String hosId = "";
    private String colType = "";
    private Gson gson = new Gson();

    void intBasedata() {
        tv = (TaskVo) getIntent().getSerializableExtra("data");
        if (tv == null) {
            tv = new TaskVo();
        }

        if(tv.getDepartment().equals("")&&TaskUtils.isPartTimeJob(mcontext)){
            tv.setDepartment(TaskUtils.getDefultDepartId());
            tv.setDepartmentName(TaskUtils.getDefultDepartName());
        }
        date = getIntent().getStringExtra("time");
        hosId = tools.getValue(Constants.HOSPITAL_ID);
        setTaskType_times();

        if (null == hosId) {
            hosId = "";
        }
    }

    private void setViewData() {
        departTv.setText(tv.getDepartmentName());
//        if (colType.equals("1")) {
//            typeTv.setText("不限时机");
//            typeTv.setVisibility(View.GONE);
//        } else {
//            typeTv.setText(colType + "个时机");
//        }
        String str = tv.getFiveTasks();
        if (null == str) {
            str = "";
        }
        JSONArray jarr = null;
        try {
            jarr = new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (null != tv.getSaveAttachmentsList()
                && tv.getSaveAttachmentsList().length() > 0) {
            saveAttachmentsList = gson.fromJson(tv.getSaveAttachmentsList()
                    .toString(), new TypeToken<List<Attachments>>() {
            }.getType());
            fileList.addAll(saveAttachmentsList);
        }
        if (tv.getFiveTasksInfo().size() <= 0
                && (jarr == null || str.equals("[]"))) {
            addpersonBtn.setVisibility(View.GONE);
            personIndecatorlay.setVisibility(View.VISIBLE);
            titleTv.setText("完成:0" + "/" + colType);
            list=new ArrayList<>();
            for (int  i=0;i<3;i++){
                planListDb planListDb=new planListDb();
                planListDb.setPname(personItem[i]);
                if(jobList.size()>0){
                    switch (i){
                        case 0:
                            planListDb.setPpost(jobList.get(0).getId());
                            planListDb.setPpostName(jobList.get(0).getName());
                            break;
                        case 1:
                            planListDb.setPpost(jobList.get(2).getId());
                            planListDb.setPpostName(jobList.get(2).getName());
                            break;
                        case 2:
                            planListDb.setPpost(jobList.get(6).getId());
                            planListDb.setPpostName(jobList.get(6).getName());
                            break;
                    }
                }

                list.add(planListDb);

            }

            // pdb.setHospital(tools.getValue(Constants.HOSPITAL_ID));

        } else {
            addpersonBtn.setVisibility(View.GONE);
            personIndecatorlay.setVisibility(View.VISIBLE);
            if (tv.getTask_id() != 0) {
                tv.setUpdatedTask(true);
                backText.setText("取消编辑");
                tv.setStatus(2);
                list = tv.getFiveTasksInfo();// 从服务器获取的
                if (null == list || list.size() == 0) {
                    List<planListDb> cachelist = gson.fromJson(
                            tv.getFiveTasks(),
                            new TypeToken<List<planListDb>>() {
                            }.getType());
                    list.addAll(cachelist);
                }
                if (tv.getFlag() == 1) {
                    tv.setWho(true);
                }
                List<planListDb> catheList = new ArrayList<planListDb>();
                catheList.addAll(list);
                titleTv.setText("完成:0" + "/" + colType);
            } else {
                list = gson.fromJson(jarr.toString(),
                        new TypeToken<List<planListDb>>() {
                        }.getType());
            }
        }

        if (list.size() > 0) {
            personIndex = 0;
            jobTimesList.clear();

            for (JobListVo jCache : jobList) {
                JobListVo jv = new JobListVo();
                jv.setId(jCache.getId());
                jv.setName(jCache.getName());
                jobTimesList.add(jv);

            }

            for (planListDb pdb : list) {
                if (tv.getTask_id()>0) {
                    for (JobListVo jv : jobList) {
                        if (pdb.getPpost().equals(jv.getId())
                                && !pdb.getPpost().equals("11")) {
                            pdb.setPpostName(jv.getName());
                        }

                    }
//                    for (JobListVo jv : jobTypelist) {
//                        if (pdb.getWork_type().equals(jv.getId())) {
//                            pdb.setWorkName(jv.getName());
//                        }
//
//                    }
                }

                for (JobListVo jTL : jobTimesList) {
                    if (null == jTL) {
                        continue;
                    }


                    if (null != pdb.getPpost() && pdb.getPpost().equals(jTL.getId())) {
                        jTL.setTaskNum(jTL.getTaskNum()
                                + pdb.getSubTasks().size());
                    } else if (null != pdb.getPpost() && pdb.getPpost().equals("0")) {
                        if (jTL.getId().equals("11")) {
                            jTL.setTaskNum(jTL.getTaskNum()
                                    + pdb.getSubTasks().size());
                        }

                    }

                }

                for (subTaskVo sv : pdb.getSubTasks()) {

                    sv.setPname(pdb.getPname());
                    sv.setPpoName(pdb.getPpostName());
                    sv.setSubtaskId(subtaskId);
                    subtaskId++;
                    finishList.add(sv);

                    if (null == sv.getAttachments()) {
                        for (Attachments att : sv.getAttachments()) {
                            if (AbStrUtil.isEmpty(att.getState())) {
                                att.setState("2");
                            }
                            fileList.add(att);

                        }
                    }
                }
                fileAdapter.notifyDataSetChanged();
                addpersonView(pdb);
            }
            totalTv.setText("已完成(" + finishList.size() + ")");
            totalfileTv.setText("照片与录音(" + fileList.size() + ")");
            tv.setTotalNum(finishList.size() + "");
            personTv.setText("(选中被调查人：" + list.get(personIndex).getPname() + ")");
        }

        if (!colType.equals("1")) {
            titleTv.setText("完成:" + finishList.size() + "/" + colType);
        } else {
            if (tv.getTask_id() > 0) {
                titleTv.setText("完成:" + finishList.size());
            } else {
                if (list.size() >= 1) {
                    if(tv.getMinute()>0){
                        minute=tv.getMinute();
                        second=tv.getSeconds();
                    }else{
                        second = 0;
                        minute = tools.getValue_int(HandWashSettingActivity.LIMT_TIME,20);
                    }
                    startTimer();
                }

            }
        }
        adddDefultPersonView();
    }

    public void ResetJobTimesListView() {
        jobTimesListView.removeAllViews();
        for (JobListVo jTL2 : jobTimesList) {
            if (jTL2.getTaskNum() > 0) {
                TextView textView = new TextView(mcontext);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setText(jTL2.getName() + ":" + jTL2.getTaskNum());
                textView.setPadding(10, 0, 10, 0);
                jobTimesListView.addView(textView);

            }
        }
        TextView textView2 = new TextView(mcontext);
        textView2.setPadding(dp2Px(mcontext, 23), 0, dp2Px(mcontext, 10), 0);
        jobTimesListView.addView(textView2);
    }

    public void addperson() {
        planListDb pdb = new planListDb();
        int x = 0;
        for (int j = 0; j < personItem.length; j++) {
            boolean needContinue = true;
            for (planListDb pb : list) {
                if (pb.getPname().equals(personItem[j])) {
                    needContinue = false;
                    break;
                }
            }
            if (needContinue) {
                x = j;
                break;
            }

        }
        pdb.setPname(personItem[x]);
        List<subTaskVo> list1 = new ArrayList<>();
        pdb.setSubTasks(list1);
//        pdb.setDepartment(departId);
//        pdb.setHospital(hosId);
//        pdb.setTask_id(task_id);
//        pdb.setMission_time(date);
//        pdb.setTask_type(taskType);
//        if (AbStrUtil.isEmpty(tv.getDefaltJobId())) {
//            pdb.setPpost("0");
//        } else {
//            pdb.setPpost(tv.getDefaltJobId());
//            pdb.setPpostName(tv.getDefaltJobName());
//        }
        if (AbStrUtil.isEmpty(tv.getDefaltWorkType())) {
            pdb.setWork_type("");
            pdb.setWorkName("");
        } else {
            pdb.setWork_type(tv.getDefaltWorkType());
            pdb.setWorkName(tv.getDefaltWorkTypeName());
        }
        list.add(pdb);
        addpersonView(pdb);

    }


    private View getPesonView() {
        return inflater.inflate(R.layout.adapter_handtask_person, null);
    }

    private void addpersonView(planListDb pdb) {
        View view = getPesonView();
        final LinearLayout personlay = (LinearLayout) view.findViewById(R.id.layout);
        TextView button = (TextView) view.findViewById(R.id.button);
        button.setText(pdb.getPname());
        TextView numsTv = (TextView) view.findViewById(R.id.numsTv);
        numsTv.setText("(" + pdb.getSubTasks().size() + ")");
        TextView tv_job = (TextView) view.findViewById(R.id.tv_job);
        tv_job.setText(pdb.getPpostName());
        view.findViewById(R.id.line).setBackgroundResource(R.drawable.dashe_line_blue);

        personlay.setLayoutParams(para2);
        final int pos = personlistView.getChildCount() - 1;
        personlistView.addView(view, pos);
        setPersonChoosed(personlistView.getChildCount() - 2);
        personlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos == personIndex) {
                    planListDb pdb = list.get(pos);
                    showJobDialog(pos, pdb.getPname(), pdb.getWork_type(), pdb.getPpost());
                } else {
                    setPersonChoosed(pos);
                }
            }
        });
        dyscroller.post(new Runnable() {
            @Override
            public void run() {
                dyscroller.smoothScrollTo((personlistView.getChildCount() + 1) * itemPersonWith, 0);
            }
        });
    }

    private void setFrampersonView(planListDb pdb) {
        final LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.frambgpersonInfo);
        linearLayout.setVisibility(View.VISIBLE);
        final LinearLayout personlay = (LinearLayout) linearLayout.findViewById(R.id.frampersonlayout);
        TextView button = (TextView) linearLayout.findViewById(R.id.button);
        button.setText(pdb.getPname());
        TextView numsTv = (TextView) linearLayout.findViewById(R.id.numsTv);
        numsTv.setText("(" + pdb.getSubTasks().size() + ")");
        TextView tv_job = (TextView) linearLayout.findViewById(R.id.fram_tv_job);
        tv_job.setText(pdb.getPpostName());
        linearLayout.findViewById(R.id.line).setBackgroundResource(R.drawable.dashe_line_white);

        personlay.setLayoutParams(para2);
        if (tools.getValue_int(HandWashSettingActivity.SHOW_FRAM_PERSON_INFO, 0) == 0) {

            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            linearLayout.findViewById(R.id.hand_wasdh_know2).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTaskType_times();
                    tools.putValue(HandWashSettingActivity.SHOW_FRAM_PERSON_INFO, 1);
                    linearLayout.setVisibility(View.GONE);
                }
            });

        } else {
            linearLayout.setVisibility(View.GONE);
        }


    }

    /**
     * 设置选中观察对象
     * @param position
     */
    private void setPersonChoosed(int position) {


        if (position == personIndex && personIndex != 0) {
            return;
        }
        if (personIndex >= 0) {
            View view2 = personlistView.getChildAt(personIndex);//前一个被选中项
            LinearLayout personlayBefore = (LinearLayout) view2.findViewById(R.id.layout);
            TextView nameTv2 = (TextView) view2.findViewById(R.id.button);
            TextView numsTv2 = (TextView) view2.findViewById(R.id.numsTv);
            TextView tv_job2 = (TextView) view2.findViewById(R.id.tv_job);

            nameTv2.setTextColor(getResources().getColor(R.color.top_color));
            numsTv2.setTextColor(getResources().getColor(R.color.top_color));
            tv_job2.setTextColor(getResources().getColor(R.color.top_color));
            view2.findViewById(R.id.line).setBackgroundResource(R.drawable.dashe_line_blue);
            personlayBefore.setBackgroundResource(R.drawable.circle_sharp_blue);
            view2.findViewById(R.id.pointTv).setVisibility(View.GONE);
        }

        View view = personlistView.getChildAt(position);
        LinearLayout personlay = (LinearLayout) view.findViewById(R.id.layout);
        TextView nameTv = (TextView) view.findViewById(R.id.button);
        TextView numsTv = (TextView) view.findViewById(R.id.numsTv);
        TextView tv_job = (TextView) view.findViewById(R.id.tv_job);

        view.findViewById(R.id.line).setBackgroundResource(R.drawable.dashe_line_white);
        nameTv.setTextColor(getResources().getColor(R.color.white));
        numsTv.setTextColor(getResources().getColor(R.color.white));
        tv_job.setTextColor(getResources().getColor(R.color.white));

        personlay.setBackgroundResource(R.drawable.round_sharp_blue);
        view.findViewById(R.id.pointTv).setVisibility(View.VISIBLE);

        selectItem(position);
    }

    /**
     * 重设单个观察对象
     * @param i
     */
    public void setPersonByIndex(int i) {
        planListDb pdb = list.get(i);
        View view = personlistView.getChildAt(i);
        TextView nameTv = (TextView) view.findViewById(R.id.button);
        TextView numsTv = (TextView) view.findViewById(R.id.numsTv);
        TextView tv_job = (TextView) view.findViewById(R.id.tv_job);
        numsTv.setText("(" + pdb.getSubTasks().size() + ")");
        view.findViewById(R.id.line).setVisibility(View.VISIBLE);
        tv_job.setText(pdb.getPpostName());
        nameTv.setText(pdb.getPname());
    }

    // 添加文件
    List<Attachments> fileList = new ArrayList<Attachments>();// 总的文件

    public List<Attachments> saveAttachmentsList = new ArrayList<Attachments>();// 待保存的数据//
    // 时机对应的文件

    public void addFile(String file, int type, String time) {
        dialToatalFileTv();
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        Attachments att = new Attachments();
        att.setDate(date);
        att.setState("1");
        att.setFile_name(file);
        att.setFile_type(type + "");
        if (!AbStrUtil.isEmpty(time) && type == 2) {
            att.setTime(time);
        } else {
            att.setTime("");
        }
        totalfileTvQipao.setVisibility(View.VISIBLE);
        totalfileTvQipao.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                totalfileTvQipao.setVisibility(View.GONE);
            }
        }, 1000);
        fileList.add(0, att);
        saveAttachmentsList.add(att);
        totalfileTv.setText("照片与录音(" + fileList.size() + ")");
        tv.setSaveAttachmentsList(gson.toJson(saveAttachmentsList));
        fileAdapter.notifyDataSetChanged();

    }

    // 更多弹出框
    LayoutInflater inflater;
    public static final int REQUEST_SAVE_REMARK = 0X10; // 保存小结


    public static final int CODE_RESULT_CANCEL = -0x2; // 取消

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == CODE_RESULT_CANCEL) {
            // 返回结果异常
            finish();
        } else if (resultCode == HandWashSettingActivity.RESULTCODE) {
            setTaskType_times();
        } else if (requestCode == 0x11 && resultCode == RESULT_OK) {
            setFeedData(intent);
            // setViewData();
        } else if(resultCode == DepartChooseActivity.CHOOSE_SUC){
            DepartVos.DepartmentListBean bean= (DepartVos.DepartmentListBean) intent.getSerializableExtra("departData");
            departTv.setText(bean.getName());
            tv.setDepartment(bean.getId()+"");
            tv.setDepartmentName(bean.getName());
        }else {
            switch (requestCode) {
                case REQUEST_SAVE_REMARK:
                    if (intent != null) {
                        tv = (TaskVo) intent.getSerializableExtra("data");

                        JSONArray jarr = null;
                        try {
                            jarr = new JSONArray(tv.getFiveTasks());
                            DebugUtil.debug("untime_list", "jarr.toString()>>"
                                    + jarr.toString());
                            List<planListDb> list2 = gson.fromJson(jarr.toString(),
                                    new TypeToken<List<planListDb>>() {
                                    }.getType());
                            list.clear();
                            list.addAll(list2);
                            personTv.setText("(选中被调查人："
                                    + list.get(personIndex).getPname() + ")");
                            setPersonByIndex(personIndex);
                            resetFinishAdapter();

                            departTv.setText(tv.getDepartmentName());

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void setTaskType_times() {
        String types = tools.getValue(HandWashSettingActivity.COLLTYPE);
        if (null == types) {
            types = "0";
            minute = 20;
            tools.putValue(HandWashSettingActivity.COLLTYPE, "0");
            tools.putValue(HandWashSettingActivity.LIMT_TIME, 20);
        }
        int type = Integer.parseInt(types);
        switch (type) {
            case 0:
                colType = "1";

                if(tools.getValue_int(HandWashSettingActivity.LIMT_TIME,20)>0){
                    second = 0;
                    minute = tools.getValue_int(HandWashSettingActivity.LIMT_TIME,20);
                }
                break;
            case 1:
                colType = "10";

                break;
            case 2:
                colType = "15";

                break;
            case 3:
                colType = "20";

                break;
            case 4:
                colType = "40";

                break;

            default:
                colType = type - 10 + "";
                break;
        }

        if (colType.equals("1") && null != titleTv) {
            if (null != timer) {
                timer.cancel();
                timer = null;
            }
            if (tv.getMinute() > 0) {
                second = 0;
                minute = tv.getMinute();
            }else if(tools.getValue_int(HandWashSettingActivity.LIMT_TIME,20)>0){
                second = 0;
                minute = tools.getValue_int(HandWashSettingActivity.LIMT_TIME,20);
            }

            if (list.size() > 0) {
                startTimer();
            }
        } else if (null != titleTv) {
            if (null != timer) {
                timer.cancel();
                timer = null;
            }
            titleTv.setText("完成:" + finishList.size() + "/" + colType);
        }
    }

    @Override
    public void AddImgFile(String name) {
        addFile(name, 1, "");

    }

    @Override
    public void AddRecordFile(String name, double totalTime) {
        addFile(name, 2, totalTime + "");
    }

    public void refreshJobTimesAdapter() {
        jobTimesList.clear();
        for (JobListVo jCache : jobList) {
            JobListVo jv = new JobListVo();
            jv.setId(jCache.getId());
            jv.setName(jCache.getName());
            jobTimesList.add(jv);
        }
        for (planListDb pdb : list) {
            for (JobListVo jTL : jobTimesList) {
                if (pdb.getPpost().equals(jTL.getId())) {
                    jTL.setTaskNum(jTL.getTaskNum() + pdb.getSubTasks().size());
                } else if (pdb.getPpost().equals("0")) {
                    if (jTL.getId().equals("11")) {
                        jTL.setTaskNum(jTL.getTaskNum()
                                + pdb.getSubTasks().size());
                    }

                }
            }

        }
        ResetJobTimesListView();
    }

    public void resetFinishAdapter() {
        finishList.clear();
        for (planListDb pdb : list) {
            for (subTaskVo sv : pdb.getSubTasks()) {
                sv.setPname(pdb.getPname());
                sv.setPpoName(pdb.getPpostName());
                sv.setWorkName(pdb.getWorkName());
                finishList.add(sv);
            }
        }

        finishAdapter.notifyDataSetChanged();
    }

    /**
     * startUploadActivity:【开启上传图片activity】. <br/>
     * ..<br/>
     */
    public static final int COMPRESS_IMAGE = 0x17;
    public static final String CLOSE_ACTIVITY = "close";
    int item_width = 0;

    void initMyhandler() {
        myHandler = new MyHandler(this) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = myHandler.mactivity.get();
                if (null != activity) {
                    switch (msg.what) {
                        case COMPRESS_IMAGE:
                            if (null != msg && null != msg.obj) {
                                File file = new File(msg.obj + "");
                                Log.i("1111", file.exists() + "");
                                AddImgFile(file.toString());
                            }
                            break;
                        case SETENDBUTTION:
                            setEndButton(true);
                            break;
                        case SETETOATAL:
                            // ToastUtils.showToast(mcontext, msg.obj.toString());
                            setToatleTv(Integer.parseInt(msg.obj.toString()));
                            break;
                        case FRESHADAPTER:
                            refreshAdapter();
                            break;
                        case SETETITLE:
                            if (null != msg && null != msg.obj) {
                                int num = Integer.parseInt(msg.obj.toString());
                                titleTv.setText("完成：" + num + "/" + colType);
                                setTitleTv(Integer.parseInt(msg.obj.toString()));
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public boolean iscontinuTask = false;



    DeletDialog deletDialog;

    public void showDeletFile(final String file, final int position) {
        deletDialog = new DeletDialog(mcontext, R.style.SelectDialog,
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        deletFile(file, position);
                        deletDialog.dismiss();
                    }
                });
        deletDialog.show();

    }

    public void deletFile(String file, int position) {
        for (planListDb pdb : list) {
            for (subTaskVo sb : pdb.getSubTasks()) {
                if (null != sb.getAttachments()) {
                    A:for (Attachments att : sb.getAttachments()) {
                        try {
                            if (null != att.getFile_name()
                                    && att.getFile_name().equals(file)) {
                                sb.getAttachments().remove(att);
                                break A;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

        }
        fileList.remove(position);
        totalfileTv.setText("照片与录音(" + fileList.size() + ")");

        fileAdapter.notifyDataSetChanged();

    }

    private void adddDefultPersonView() {
        if (list.size() >= 3) {
            return;
        }
        for (int i = list.size(); i < 3; i++) {
            View view = getPesonView();
            LinearLayout personlay = (LinearLayout) view.findViewById(R.id.layout);
            personlay.setBackgroundResource(R.drawable.line_dashgap);
            view.findViewById(R.id.line).setVisibility(View.GONE);
            personlay.setLayoutParams(para2);
            final int pos = i;
            personlay.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pos == personIndex) {
                        planListDb pdb = list.get(pos);
                        showJobDialog(pos, pdb.getPname(), pdb.getWork_type(), pdb.getPpost());
                    } else {
                        if (pos >= list.size()) {
                            ToastUtil.showMessage("请点击右侧按钮添加观察对象！");
                        } else {
                            setPersonChoosed(pos);
                        }
                    }
                }
            });
            personlistView.addView(view, personlistView.getChildCount() - 1);
        }

    }

    public void showJobDialog(final int position, String name, String workId,
                              String jobId) {
        Dialog dialog = new JobDialogHis(mcontext, name, workId, jobId,
                jobTypelist, jobList, new JobDialogHis.ChooseItem() {
            @Override
            public void getJobChoosePosition(int positon1, int position2) {

            }

            @Override
            public void getJobChoosePosition(String name, int positon1,
                                             int position2) {
                addpersonBtn.setVisibility(View.GONE);
                personIndecatorlay.setVisibility(View.VISIBLE);
                if (position < 0) {
                    if(list.size()>=26){
                        ToastUtil.showMessage("已达人数上限");
                        return;
                    }
                    planListDb pdb = new planListDb();
                    pdb.setPname(name.equals("") ? personItem[list.size()] : name);
                    pdb.setPpostName(jobList.get(position2).getName());
                    pdb.setPpost(jobList.get(position2).getId());
                    // pdb.setWork_type(jobTypelist.get(positon1).getId());
                    // pdb.setWorkName(jobTypelist.get(positon1).getName());
                    list.add(pdb);
                    if (list.size() <= 3) {
                        setPersonByIndex(list.size() - 1);
                        setPersonChoosed(list.size() - 1);
                    } else {
                        addpersonView(pdb);
                    }
                    if (list.size() == 1) {
                        if (colType.equals("1") && null != titleTv) {
                            if (null != timer) {
                                timer.cancel();
                                timer = null;
                            }
                            if (tv.getMinute() > 0) {
                                second = tv.getSeconds();
                                minute = tv.getMinute();
                            }

                            if (list.size() > 0) {
                                startTimer();
                            }
                        }
                        setFrampersonView(pdb);

                    }
                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.scrollTo(0,0);
                        }
                    });
                    return;
                }
                if (position2 >= 0) {
                    list.get(position).setPpostName(
                            jobList.get(position2).getName());
                    list.get(position).setPpost(
                            jobList.get(position2).getId());
                } else {
                    list.get(position).setPpostName("");
                    list.get(position).setPpost("11");
                }
                if (positon1 >= 0) {
                    list.get(position).setWork_type(
                            jobTypelist.get(positon1).getId());
                    list.get(position).setWorkName(
                            jobTypelist.get(positon1).getName());
                } else {
                    list.get(position).setWork_type("");
                    list.get(position).setWorkName("");
                }
                if (!AbStrUtil.isEmpty(name)) {
                    list.get(position).setPname(name.trim());
                    if (position == personIndex) {
                        personTv.setText("(选中被调查人："
                                + list.get(personIndex).getPname()
                                + ")");
                    }
                }
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.scrollTo(0,0);
                    }
                });
                setPersonByIndex(position);
                refreshJobTimesAdapter();
                resetFinishAdapter();

            }
        });

        dialog.show();
    }

    private List<JobListVo> jobTypelist;
    List<WrongRuleVo> ruleList;

    public void getJobCacheData() {
        // type1 位督导岗位 type4为职称 type3为职位
        jobTypelist = HandWashUtils.getJobCacheData(mcontext);
        if (null == jobTypelist) {
            jobTypelist = new ArrayList<>();
        }
    }

    public void getWrongRules() {
        ruleList = HandWashUtils.getWrongRules(mcontext);
        if (null == ruleList) {
            ruleList = new ArrayList<>();
        }
    }

    Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            System.out.println("handle!");
            if (minute == 0) {
                if (second == 0) {
                    titleTv.setText("时间结束!  完成:" + tv.getTotalNum());

                    endDilog.show();
                    endDilog.setTitleTxt("您的督导已达设定时间，是否提交？");
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (timerTask != null) {
                        timerTask = null;
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        titleTv.setText("0" + minute + ":" + second + "完成:"
                                + tv.getTotalNum());
                    } else {
                        titleTv.setText("0" + minute + ":0" + second + "完成:"
                                + tv.getTotalNum());
                    }
                }
            } else {
                if (second == 0) {
                    second = 59;
                    minute--;
                    if (minute >= 10) {
                        titleTv.setText(minute + ":" + second + "完成:"
                                + tv.getTotalNum());
                    } else {
                        titleTv.setText("0" + minute + ":" + second + "完成:"
                                + tv.getTotalNum());
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        if (minute >= 10) {
                            titleTv.setText(minute + ":" + second + "完成:"
                                    + tv.getTotalNum());
                        } else {
                            titleTv.setText("0" + minute + ":" + second + "完成:"
                                    + tv.getTotalNum());
                        }
                    } else {
                        if (minute >= 10) {
                            titleTv.setText(minute + ":0" + second + "完成:"
                                    + tv.getTotalNum());
                        } else {
                            titleTv.setText("0" + minute + ":0" + second
                                    + "完成:" + tv.getTotalNum());
                        }
                    }
                }
            }
        }

        ;
    };



    // 不规范弹出框
    public class MultiChooseDialog extends BaseDialog {
        ListView listView;
        List<RulesVo> ruleItems2;

        public MultiChooseDialog(Context context, List<RulesVo> ruleItems) {
            super(context);
            this.ruleItems2 = ruleItems;
            chooseAdapter = new MutichooseListAdapter(mcontext, ruleItems2);

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_mutichoose);
            listView = (ListView) this.findViewById(R.id.dialog_list);

            Button choose = (Button) this.findViewById(R.id.chooseBtn);
            choose.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();

                }
            });
            listView.setAdapter(chooseAdapter);

            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (ruleItems2.get(position).isChoosed()) {
                        ruleItems2.get(position).setChoosed(false);
                    } else {
                        ruleItems2.get(position).setChoosed(true);
                    }
                    chooseAdapter.notifyDataSetChanged();
                }
            });

            choose.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    chooseRules.clear();
                    for (RulesVo rv : ruleItems2) {
                        if (rv.isChoosed()) {
                            chooseRules.add(rv);
                            rv.setChoosed(false);
                        }
                    }
                    setContinudata();
                    setContinueBtn(false);
                    dismiss();
                }
            });
        }

    }

    public boolean getFiveCheckPos(int pos){
        return isCheck[pos];
    }
}
