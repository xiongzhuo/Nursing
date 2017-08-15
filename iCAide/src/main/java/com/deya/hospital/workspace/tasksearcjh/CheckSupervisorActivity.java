package com.deya.hospital.workspace.tasksearcjh;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.timepicker.ScreenInfo;
import com.baoyz.timepicker.WheelMain;
import com.deya.acaide.R;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.adapter.RcyCompletionAdapter;
import com.deya.hospital.adapter.TaskLisAdapter;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseJumpToDepartActivty;
import com.deya.hospital.base.img.TipsDialogRigister;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.task.Tasker;
import com.deya.hospital.task.uploader.BaseUploader;
import com.deya.hospital.task.uploader.FailLogUpload;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.ComomDialog;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.DatePicDialog;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.WorkSpaceFragment;
import com.deya.hospital.workspace.priviewbase.FormCommitSucTipsActivity;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 督导反馈
 */
public class CheckSupervisorActivity extends BaseJumpToDepartActivty implements
        OnClickListener {
    private RelativeLayout rl_back;
    public RadioButton department, supervisor_type, performance,
            supervisor_time;
    public List<String> jobTypeList;
    private Context context;
    private LayoutParams para;
    public static final int SEARCH_TASK = 0x190;
    int[] wh;
    private LinearLayout pop1, pop2, pop3, pop4;
    private ImageView iv1, iv2, iv3, iv4;
    LinearLayout view_screen;
    private CommonTopView topView;
    RcyCompletionAdapter rcyAdapter;
    private int defItem = -1;


    public String[] typeStr = {"手卫生", "通用督\n导本", "临床质控", "外科\n手消毒", "多耐", "三管"
            , "环境物表清洁", "手术部位感染", "安全注射", "其他", "医疗废物", "全部"};
    public int types[] = {1, 2, 17, 5, 8, 9, 10, 12, 11, 14, 13, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stubf
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_supervisor);
        context = this;

        initListView();
        initMyHandler();
        initData();
        initView();
//        performance.setText("已上传");

        getTaskByType(0);
        registerBroadcast();
        view_screen = (LinearLayout) findViewById(R.id.layout_pop);
        pop1 = (LinearLayout) view_screen.findViewById(R.id.pop1);
        pop2 = (LinearLayout) view_screen.findViewById(R.id.pop2);
        pop3 = (LinearLayout) view_screen.findViewById(R.id.pop3);
        pop4 = (LinearLayout) view_screen.findViewById(R.id.pop4);

    }

    private void hintPop(LinearLayout pop) {
        pop1.setVisibility(View.GONE);
        pop2.setVisibility(View.GONE);
        pop3.setVisibility(View.GONE);
        pop4.setVisibility(View.GONE);
        pop.setVisibility(View.VISIBLE);
        pop1.setVisibility(View.GONE);
    }

    public void initData() {
        jobTypeList = Arrays.asList(getTypeStr());

    }

    public int getTypes(int pos) {
        return types[pos];
    }

    public String[] getTypeStr() {
        return typeStr;
    }

    private void initView() {
        // TODO Auto-generated method stub

        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.onbackClick(this, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        department = (RadioButton) findViewById(R.id.department);
        department.setHint("全部");
        department.setOnClickListener(this);
        supervisor_type = (RadioButton) findViewById(R.id.supervisor_type);
        supervisor_type.setOnClickListener(this);
        performance = (RadioButton) findViewById(R.id.performance);
        performance.setOnClickListener(this);
        supervisor_time = (RadioButton) findViewById(R.id.supervisor_time);
        supervisor_time.setOnClickListener(this);

    }

    private PullToRefreshListView planLv;
    TaskLisAdapter adapter;
    RelativeLayout empertyView;
    private MyBrodcastReceiver brodcast;

    // 注册广播
    private void registerBroadcast() {
        brodcast = new MyBrodcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                refreshData();
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WorkSpaceFragment.UPDATA_ACTION);
        registerReceiver(brodcast, intentFilter);
    }

    @Override
    protected void onDestroy() {

        if (null != brodcast) {
            unregisterReceiver(brodcast);
        }
        myHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    protected void refreshData() {// 接收到广播后更新数据
        // Tasker.deletAllFinishedTask();
        page = 1;
        performance.setText("已上传");
        getTaskByType(searchType);
    }

    private void initListView() {

        planLv = (PullToRefreshListView) findViewById(R.id.planlist);
        empertyView = (RelativeLayout) findViewById(R.id.empertyView);
        adapter = new TaskLisAdapter(mcontext, taskList);
        planLv.setAdapter(adapter);
        planLv.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                planLv.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            }
        });
        planLv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TaskVo tv = taskList.get(position - 1);
                if (null != TaskUtils.onFindTaskById(tv.getTask_id())) {
                    tv = TaskUtils.onFindTaskById(tv.getTask_id());//读本地缓存
                }
                final TaskVo finalTv = tv;
                onTaskClick(tv, finalTv);

            }
        });

        planLv.getRefreshableView().setOnItemLongClickListener(
                new OnItemLongClickListener() {
                    Dialog deletDialog;

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view, final int position, long id) {

                        deletDialog = new ComomDialog(
                                CheckSupervisorActivity.this, "确认删除此任务？",
                                R.style.SelectDialog,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        doDeleteTask(position - 1);
                                        deletDialog.dismiss();
                                    }
                                });
                        deletDialog.show();
                        return true;
                    }
                });
        planLv.setEmptyView(empertyView);

    }

    private void onTaskClick(TaskVo tv, final TaskVo finalTv) {
        TipsDialogRigister dialog = new TipsDialogRigister(mcontext, new MyDialogInterface() {
            @Override
            public void onItemSelect(int postion) {
            }

            @Override
            public void onEnter() {
                if (finalTv.getStatus() == 1) {//上传中的任务 去编辑
                    finalTv.setStatus(2);
                    TaskUtils.onUpdateTaskById(finalTv);
                    TaskUtils.onStaractivity(CheckSupervisorActivity.this, finalTv, taskList);
                    adapter.notifyDataSetChanged();
                } else {//上传失败的任务 去上报日志
                    showprocessdialog();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (FailLogUpload.getUpload().upload(TaskUtils.gson.toJson(finalTv))) {
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
                if (finalTv.getStatus() == 1) {
                    if (NetWorkUtils.isConnect(mcontext)) {
                        if (!NetWorkUtils.isWifiState(MyAppliaction.getContext())) {

                            Intent intent = new Intent(mcontext, FormCommitSucTipsActivity.class);
                            intent.putExtra("data", finalTv);
                            intent.putExtra("commit_status", FormCommitSucTipsActivity.ONLY_WIFI);
                            startActivity(intent);
                        } else {
                            showprocessdialog();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int type = Integer.parseInt(finalTv.getType());
                                    Intent intent = new Intent(mcontext, FormCommitSucTipsActivity.class);
                                    if (!BaseUploader.getUploader(finalTv).Upload(finalTv)) {
                                        finalTv.setFailNum(finalTv.getFailNum() + 1);
                                        if (finalTv.getFailNum() >= 5) {
                                            finalTv.setStatus(4);
                                        }
                                        intent.putExtra("commit_status", FormCommitSucTipsActivity.UPLOAD_FIAL);
                                    } else {
                                        intent.putExtra("commit_status", FormCommitSucTipsActivity.UPLOAD_SUC);
                                    }
                                    dismissdialog();
                                    TaskUtils.onUpdateTaskById(finalTv);
                                    intent.putExtra("data", finalTv);
                                    startActivity(intent);
                                }
                            }).start();
                        }
                    } else {
                        Intent intent = new Intent(mcontext, FormCommitSucTipsActivity.class);
                        intent.putExtra("data", finalTv);
                        intent.putExtra("commit_status", FormCommitSucTipsActivity.NET_WORK_DISCONECT);
                        startActivity(intent);
                        return;
                    }
                } else {
                    taskList.remove(finalTv);
                    Tasker.deleteTask(finalTv);
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
        TaskUtils.onStaractivity(CheckSupervisorActivity.this, tv,
                taskList);
    }

    void doDeleteTask(int index) {
        if (index >= taskList.size()) {
            index = taskList.size() - 1;
        }
        TaskVo tv = taskList.get(index);
        if (tv.getStatus() == 1) {
            ToastUtils.showToast(mcontext, "正在同步的任务暂时无法删除");
            return;
        } else if (tv.getStatus() == 2 || tv.getStatus() == 3
                || tv.getStatus() == 4) {
            try {
                DataBaseHelper
                        .getDbUtilsInstance(mcontext)
                        .delete(TaskVo.class,
                                WhereBuilder.b("dbid", "=", tv.getDbid()));
                ToastUtils.showToast(mcontext, "删除成功！");
                taskList.remove(index);
                adapter.notifyDataSetChanged();
            } catch (DbException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            for (TaskVo vo : taskList) {
                if (vo.getTask_id() == tv.getTask_id()) {
                    deletTasks(vo.getTask_id(), tv.getType());
                    break;
                }

            }

        }
    }

    // 删除任务处理
    protected void setDeletRes(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            for (TaskVo vo : taskList) {
                if (vo.getTask_id() == deletTaskId) {
                    taskList.remove(vo);
                    adapter.notifyDataSetChanged();
                    break;
                }

            }
//            if (taskList.size() <= 0) {
//                empertyView.setVisibility(View.VISIBLE);
//            } else {
//                empertyView.setVisibility(View.GONE);
//            }
            ToastUtils.showToast(this, "任务删除成功");
        } else {
            ToastUtils.showToast(this, jsonObject.optString("result_msg"));
        }

    }

    public static final int REFRESHVIEWPAGER_TASKADAPTER = 0X34;

    /**
     * 删除已提交任务请求
     */
    public static final int DELETE_SUCESS = 0x20060;
    public static final int DELETE_FAIL = 0x20061;// 是否关闭软文
    int deletTaskId = 0;

    public void deletTasks(int taskId, String type) {
        JSONObject job = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            deletTaskId = taskId;
            job.put("task_id", taskId + "");
            job.put("type", type);// 手卫生的1，督导本2
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomRequest(myHandler, this,
                DELETE_SUCESS, DELETE_FAIL, job, "task/deleteTaskById");
    }

    /**
     * 显示黄色小箭头
     */
    private void showIv(ImageView iv) {
        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.GONE);
        iv.setVisibility(View.VISIBLE);
    }

    /**
     * radiobutton的选中事件
     */
    private void showCheckRadio(RadioButton rb) {
        department.setChecked(false);
        supervisor_type.setChecked(false);
        performance.setChecked(false);
        supervisor_time.setChecked(false);
        rb.setChecked(true);
    }

    String departments = "";
    private HashMap<String, ChildsVo> selectMap = new HashMap<String, ChildsVo>();


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        switch (id) {
            case R.id.department:
                if (department.isChecked()) {
//                    showIv(iv1);
                    hideIv();
                    showCheckRadio(department);
                }
//			initDepartPop(view_screen);
//			setDepartmentPop();

                view_screen.setVisibility(View.GONE);
                hintPop(pop1);
                onMutiChoose();
                break;
            case R.id.supervisor_type:
                if (supervisor_type.isChecked()) {
                    showIv(iv2);
                    showCheckRadio(supervisor_type);
                }
                setSupervisorTypePop();
                hintPop(pop2);
                break;
            case R.id.performance:
                if (performance.isChecked()) {
                    showIv(iv3);
                    showCheckRadio(performance);
                }
                setPerformancePop();
                hintPop(pop3);
                break;
            case R.id.supervisor_time:
                if (supervisor_time.isChecked()) {
                    showIv(iv4);
                    showCheckRadio(supervisor_time);
                }
                setSupervisortime();
                hintPop(pop4);
                break;
            default:
                break;
        }
    }


    private void hideIv() {
        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.GONE);
    }

    /**
     * 督导类型popWindow
     */

    int typeGvChooIndex = -1;

    private void setSupervisorTypePop() {
        if (View.GONE == view_screen.getVisibility()) {
            view_screen.setVisibility(View.VISIBLE);
        }

        GridView gv = (GridView) view_screen.findViewById(R.id.gv);
        final GvAdapter gvAdapter = new GvAdapter();
        gv.setAdapter(gvAdapter);
        gvAdapter.setChooseItem(typeGvChooIndex);
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                gvAdapter.setChooseItem(position);
                reSetSearchKey();
                page = 1;
                searchType = 2;
                supervisor_type.setText(jobTypeList.get(position));
                task_type = getTypes(position) + "";
                getTaskByType(2);
                typeGvChooIndex = position;
                view_screen.setVisibility(View.GONE);
                iv2.setVisibility(View.GONE);
            }
        });
        // if (!AbStrUtil.isEmpty(workId)) {
        // for (int j = 0; j < jobTypeList.size(); j++) {
        // if (workId.equals(jobTypeList.get(j).getId())) {
        // chooseIndex = j;
        // gvAdapter.notifyDataSetChanged();
        // }
        // }
        // }

        view_screen.findViewById(R.id.bottom_view2).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        view_screen.setVisibility(View.GONE);

                        hideIv();
                    }
                });
    }

    int chooseIndex = -1;

    @Override
    protected void onChooseSuc(DepartVos.DepartmentListBean bean) {

    }

    @Override
    protected void onChooseSuc(String names, String ids) {
        department.setText(AbStrUtil.isEmpty(names) ? "全部" : names);
        departments = ids;
        page = 1;
        searchType = 1;
        defItem = 3;
        performance.setText("已上传");
        getTaskByType(1);
    }

    public class GvAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return jobTypeList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public void setChooseItem(int position) {
            chooseIndex = position;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_job_type, null);
            TextView tx = (TextView) convertView.findViewById(R.id.textView);
            if (chooseIndex != position) {
                tx.setBackgroundResource(R.drawable.circle_sharp_blue);
                tx.setTextColor(context.getResources().getColor(
                        R.color.top_color));
            } else {
                tx.setBackgroundResource(R.drawable.round_orange);
                tx.setTextColor(context.getResources().getColor(R.color.white));
            }
            tx.setText(jobTypeList.get(position));
            // tx.setLayoutParams(para);
            return convertView;
        }
    }

    public int dp2Px(int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + (int) 0.5f);
    }

    /**
     * 完成情况
     */
    private void setPerformancePop() {
        if (View.GONE == view_screen.getVisibility()) {
            view_screen.setVisibility(View.VISIBLE);
        }
        view_screen.findViewById(R.id.bottom_view3).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        view_screen.setVisibility(View.GONE);
                        hideIv();
                    }
                });
        RecyclerView rg_state = (RecyclerView) view_screen.findViewById(R.id.recycleview);
        if (rcyAdapter != null) {
            //设置初始化选中项
            rcyAdapter.setDefSelect(defItem);
            rg_state.setAdapter(rcyAdapter);
            return;
        }
//        view_screen.findViewById(R.id.all_performance).setVisibility(View.GONE);
        List<String> list = new ArrayList<>();
        list.add("未完成");
        list.add("待上传");
        list.add("提交失败");
        list.add("已上传");
        list.add("全部");
        int width = getWindowManager().getDefaultDisplay().getWidth();
        rcyAdapter = new RcyCompletionAdapter(CheckSupervisorActivity.this, list, width);
        rg_state.setLayoutManager(new GridLayoutManager(this, 5));
        rcyAdapter.setDefSelect(defItem);
        rg_state.setAdapter(rcyAdapter);
        rcyAdapter.setOnItemClickLitener(new RcyCompletionAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //如果点击了相同的条目不触发点击事件
                if (position == defItem)
                    return;
                defItem = position;
                rcyAdapter.setDefSelect(defItem);
                rcyAdapter.notifyDataSetChanged();
                //状态选中刷新数据
                if (position == 0) {
                    //   Tasker.deletAllFinishedTask();
                    searchType = 6;
//                        getAllLoacalTask();
                    setCompletionData("status", "2", "未完成");
                } else if (position == 1) {
                    setCompletionData("status", "1", "待上传");
                } else if (position == 2) {
                    setCompletionData("status", "4", "提交失败");
                } else if (position == 3) {
                    // Tasker.deletAllFinishedTask();
//						reSetSearchKey();
                    page = 1;
                    searchType = 1;
                    getTaskByType(4);
                    iv3.setVisibility(View.GONE);
                    view_screen.setVisibility(View.GONE);
                    performance.setText("已上传");
                    hideIv();
                } else if (position == 4) {
                    Tasker.deletAllFinishedTask();
                    reSetSearchKey();
                    page = 1;
                    searchType = 0;
                    getTaskByType(0);
//                        getAllLoacalTask();
                    performance.setText("全部");
                    view_screen.setVisibility(View.GONE);
                    hideIv();
                }
            }
        });

    }

    public void setCompletionData(String k, String v, String title) {
        reSetSearchKey();
        getStateLoacalData(k, v);
        planLv.setMode(Mode.DISABLED);
        view_screen.setVisibility(View.GONE);
        performance.setText(title);
        hideIv();
    }

    /**
     * 督导时间
     */
    TextView start_time, end_time;
    RadioButton lastMonth, rb_week, rb_month, rb_year;
    private boolean isStart = true;
    TextView confirm;

    private void setSupervisortime() {
        view_screen.setVisibility(View.VISIBLE);
        confirm = (TextView) view_screen.findViewById(R.id.confirm2);
        confirm.setEnabled(true);
        start_time = (TextView) view_screen.findViewById(R.id.start_time);
        start_time.setOnClickListener(new OnClickListener() {

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
                LayoutParams lp = dialog.getWindow()
                        .getAttributes();
                lp.width = (int) (display.getWidth()); // 设置宽度
                lp.height = (int) (display.getHeight()); // 设置高度
                dialog.getWindow().setAttributes(lp);
            }
        });

        end_time = (TextView) view_screen.findViewById(R.id.end_time);
        end_time.setOnClickListener(new OnClickListener() {

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
                LayoutParams lp = dialog.getWindow()
                        .getAttributes();
                lp.width = (int) (display.getWidth()); // 设置宽度
                lp.height = (int) (display.getHeight()); // 设置高度
                dialog.getWindow().setAttributes(lp);
            }
        });

        lastMonth = (RadioButton) view_screen.findViewById(R.id.lastMonth);
        lastMonth.setVisibility(View.GONE);
        view_screen.findViewById(R.id.whiteView).setVisibility(View.GONE);
        rb_week = (RadioButton) view_screen.findViewById(R.id.rb_week);
        rb_month = (RadioButton) view_screen.findViewById(R.id.rb_month);
        rb_year = (RadioButton) view_screen.findViewById(R.id.rb_year);

        rb_week.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                iv4.setVisibility(View.GONE);
                supervisor_time.setText("本周");
                defItem = -1;
                SimpleDateFormat dateFormater = new SimpleDateFormat(
                        "yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_WEEK, 1);
                cal.getTime();
                start_time.setText(dateFormater.format(cal.getTime()) + "");

                cal.set(Calendar.DAY_OF_WEEK,
                        cal.getActualMaximum(Calendar.DAY_OF_WEEK));
                end_time.setText(dateFormater.format(cal.getTime()));
//				reSetSearchKey();
                start_date = start_time.getText().toString();
                end_date = end_time.getText().toString();
                view_screen.setVisibility(View.GONE);
                page = 1;
                mission_time_type = "1";
                searchType = 3;
                defItem = 3;
                performance.setText("已上传");
                getTaskByType(3);
            }
        });
        rb_month.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                defItem = -1;
                iv4.setVisibility(View.GONE);
                supervisor_time.setText("本月");

                SimpleDateFormat dateFormater = new SimpleDateFormat(
                        "yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.getTime();
                start_time.setText(dateFormater.format(cal.getTime()) + "");

                cal.set(Calendar.DAY_OF_MONTH,
                        cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                end_time.setText(dateFormater.format(cal.getTime()));
                view_screen.setVisibility(View.GONE);
//				reSetSearchKey();
                start_date = start_time.getText().toString();
                end_date = end_time.getText().toString();
                page = 1;
                mission_time_type = "2";
                searchType = 3;
                defItem = 3;
                performance.setText("已上传");
                getTaskByType(3);
            }
        });
        rb_year.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                iv4.setVisibility(View.GONE);
                supervisor_time.setText("今年");

                SimpleDateFormat dateFormater = new SimpleDateFormat(
                        "yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_YEAR, 1);
                cal.getTime();
                start_time.setText(dateFormater.format(cal.getTime()) + "");

                cal.set(Calendar.DAY_OF_YEAR,
                        cal.getActualMaximum(Calendar.DAY_OF_YEAR));
                end_time.setText(dateFormater.format(cal.getTime()));
//				reSetSearchKey();
                start_date = start_time.getText().toString();
                end_date = end_time.getText().toString();
                view_screen.setVisibility(View.GONE);
                page = 1;
                defItem = -1;
                mission_time_type = "3";
                searchType = 3;
                defItem = 3;
                performance.setText("已上传");
                getTaskByType(3);
            }
        });
        view_screen.findViewById(R.id.confirm2).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //   Tasker.deletAllFinishedTask();
//						reSetSearchKey();
                        defItem = -1;
                        String aString = start_time.getText() + "";
                        start_date = start_time.getText().toString();
                        end_date = end_time.getText().toString();
                        page = 1;
                        searchType = 5;
                        performance.setText("完成情况");
                        getTaskByType(5);
                        iv4.setVisibility(View.VISIBLE);
                        view_screen.setVisibility(View.GONE);

                        supervisor_time.setText("督导时间");
                        hideIv();
                    }
                });
        view_screen.findViewById(R.id.canser2).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        iv4.setVisibility(View.VISIBLE);
                        view_screen.setVisibility(View.GONE);

                        supervisor_time.setText("督导时间");
                        rb_week.setChecked(false);
                        rb_month.setChecked(false);
                        rb_year.setChecked(false);
                        start_time.setText("");
                        end_time.setText("");
                        hideIv();
                    }
                });

        view_screen.findViewById(R.id.bottom_view4).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        view_screen.setVisibility(View.GONE);
                        ;
                        hideIv();
                    }
                });
    }


    public class MyDialog extends Dialog {

        private Button showBtn;

        /**
         * Creates a new instance of MyDialog.
         */
        public MyDialog(Context context, int theme) {
            super(context, theme);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO 自动生成的方法存根
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.timepicker);
            LinearLayout timepickerview = (LinearLayout) this
                    .findViewById(R.id.timePicker1);
            ScreenInfo screenInfo = new ScreenInfo(CheckSupervisorActivity.this);
            final WheelMain wheelMain = new WheelMain(timepickerview, false);
            wheelMain.screenheight = screenInfo.getHeight();
            Time curTime = new Time(); // or Time t=new Time("GMT+8"); 加上Time
            // Zone资料
            curTime.setToNow(); // 取得系统时间。
            int year = curTime.year - 10;
            int month = curTime.month;
            int day = curTime.monthDay;
            int hour = curTime.hour; // 0-23
            int minute = curTime.minute;
            wheelMain.initDateTimePicker(year, month, day, hour, minute);
            showBtn = (Button) this.findViewById(R.id.btn_ok);
            showBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // orderTime = wheelMain.getTime().toString();
                    // timeTv.setText(orderTime);
                    if (isStart) {
                        start_time.setText(wheelMain.getTime().toString());
                    } else {
                        end_time.setText(wheelMain.getTime().toString());
                    }

                    dismiss();

                }
            });

        }
    }

    List<TaskVo> taskList = new ArrayList<TaskVo>();
    private String nextMonthMaxDay;
    private String currentMothMinDay;
    private static final int TASKLIST_SUCESS = 0x20010;
    private static final int TASKLIST_FAILE = 0x20011;

    /**
     * 获取数据
     */
    public void getTaskList(int method) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
//			switch (method) {
//			case 0:
//			case 4:
//				job.put("pageIndex", page);
//				break;
//			case 1:
//				job.put("department", departments);
//				job.put("pageIndex", page);
//				break;
//			case 2:
//				job.put("pageIndex", page);
//				job.put("task_type", task_type);
//				break;
//			case 3:
//				job.put("pageIndex", page);
//				job.put("mission_time_type", mission_time_type);
//				break;
//			case 5:
//				job.put("start_time", start_date);
//				job.put("end_time", end_date);
//				job.put("pageIndex", page);
//				break;
//
//			default:
//
//				break;
//			}

            if (!AbStrUtil.isEmpty(departments)) {
                job.put("department", departments);
            }
            if (!AbStrUtil.isEmpty(task_type)) {
                job.put("task_type", task_type);
            }
            if (!AbStrUtil.isEmpty(mission_time_type)) {
                job.put("mission_time_type", mission_time_type);
            }
            if (!AbStrUtil.isEmpty(start_date)) {
                job.put("start_time", start_date);
            }
            if (!AbStrUtil.isEmpty(end_date)) {
                job.put("end_time", end_date);
            }
            job.put("pageIndex", page);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, this,
                TASKLIST_SUCESS, TASKLIST_FAILE, job, "task/taskList");

    }

    private MyHandler myHandler;

    private void initMyHandler() {
        myHandler = new MyHandler(this) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = myHandler.mactivity.get();
                if (null != activity) {
                    switch (msg.what) {
                        case TASKLIST_SUCESS:
                            dismissdialog();
                            if (null != msg && null != msg.obj) {
                                try {
                                    setTaskList(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            break;
                        case TASKLIST_FAILE:
                            dismissdialog();
                            taskList.clear();
//                            empertyView.setVisibility(View.VISIBLE);
//                            planLv.onRefreshComplete();
                            adapter.notifyDataSetChanged();
                            ToastUtil.showMessage("亲！您的网络不给力哦！");
                            break;

                        case DELETE_SUCESS:
                            if (null != msg && null != msg.obj) {
                                try {
                                    setDeletRes(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            break;
                        case REFRESHVIEWPAGER_TASKADAPTER:
                            adapter.notifyDataSetChanged();
                            break;

                        default:
                            break;
                    }
                }
            }
        };
    }

    int totalPage = 0;
    int page = 1;
    int searchType = 0;
    String departmentId = "";
    String task_type = "";
    String mission_time_type = "";
    String start_date = "";
    String end_date = "";

    public void reSetSearchKey() {
        departments = "";
        mission_time_type = "";
        start_date = "";
        end_date = "";
        searchType = 0;
        typeGvChooIndex = -1;
        department.setText("科室");
        supervisor_type.setText("督导类型");
        supervisor_time.setText("督导时间");

    }

    protected void setTaskList(JSONObject jsonObject) {

        planLv.onRefreshComplete();
        if (jsonObject.optString("result_id").equals("0")) {

            JSONArray jarr = jsonObject.optJSONArray("syncTaskInfos");
            List<TaskVo> list = null;
            try {
                list = TaskUtils.gson.fromJson(jarr.toString(),
                        new TypeToken<List<TaskVo>>() {
                        }.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            if (page == 1) {
                taskList.clear();
                if (searchType != 1) {
                    getAllLoacalTask();
                }
            }
            if (list != null) {
                taskList.addAll(list);
            }
            String num = jsonObject.optString("pageTotal");
            totalPage = Integer.parseInt(num);
            if (totalPage > page) {
                planLv.setMode(Mode.PULL_UP_TO_REFRESH);
            } else {
                planLv.setMode(Mode.PULL_DOWN_TO_REFRESH);
            }
            planLv.setOnRefreshListener(new OnRefreshListener2() {

                @Override
                public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                    page = 1;
//                    performance.setText("已上传");
                    getTaskByType(searchType);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                    if (totalPage > page) {
                        page++;
//                        performance.setText("已上传");
                        getTaskByType(searchType);
                    } else {
                        planLv.setMode(Mode.PULL_DOWN_TO_REFRESH);
                    }
                }
            });
//            if (taskList.size() <= 0) {
//                empertyView.setVisibility(View.VISIBLE);
//            } else {
//                empertyView.setVisibility(View.GONE);
//            }
            adapter.notifyDataSetChanged();
        }

    }


    /**
     * @param type
     * @author sunp
     * @deprecated 根据任务类型查询数据
     */
    public void getTaskByType(int type) {
        // Tasker.deletAllFinishedTask();
        showprocessdialog();
        switch (type) {

            case 0:// 全部任务
                // getAllLoacalTask(-1);
                getTaskList(0);

                break;
            case 1:// 根据科室Id查询
                // getLocakTaskListByDepart(departmentId);
                getTaskList(1);
                break;
            case 2:// 根据任务类型查询
                getTaskList(2);
                break;
            case 3:// 根据周、月、年查询
                getTaskList(3);

                break;
            case 4:
                getTaskList(4);
                break;
            case 5:// 根据时间段查询
                getTaskList(5);// 根据开始和结束时间查询
                break;
            case 6:// 查询所有未完成任务
                getAllLoacalTask();
                planLv.onRefreshComplete();
            case 7:// 查询所有已完成任务
                getTaskList(0);
                break;

            default:
                break;

        }

    }

    public void getStateLoacalData(String k, String v) {
        taskList.clear();
        if (Tasker.findListByKeyValue(k, v) != null) {
            taskList.addAll(Tasker.findListByStatusType(v, task_type));
        }
        adapter.setData(taskList);
    }


    public void getAllLoacalTask() {
        taskList.clear();
        if (page == 1) {
            taskList.clear();
            if (AbStrUtil.isEmpty(start_date) && AbStrUtil.isEmpty(end_date)) {
                taskList.addAll(Tasker.getLocalTaskByDate("2014-12-12", "2020-12-12", task_type));
            } else if (AbStrUtil.isEmpty(start_date)) {
                taskList.addAll(Tasker.getLocalTaskByDate("2014-12-12", end_date, task_type));
            } else {
                taskList.addAll(Tasker.getLocalTaskByDate(start_date, "2020-12-12", task_type));
            }
        }
        adapter.setData(taskList);
    }

    public boolean compareDate(Date date, Date date2, Date currentday) {

        return currentday.compareTo(date) >= 0
                && currentday.compareTo(date2) <= 0;
    }


}
