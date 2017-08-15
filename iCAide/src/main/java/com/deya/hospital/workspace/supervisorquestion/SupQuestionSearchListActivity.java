package com.deya.hospital.workspace.supervisorquestion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.ChildDepartListAdapter;
import com.deya.hospital.adapter.DepartListAdapter;
import com.deya.hospital.adapter.SupQuestionLisAdapter;
import com.deya.hospital.base.BaseJumpToDepartFragment;
import com.deya.hospital.base.DeletDialog2;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DatePicDialog;
import com.deya.hospital.util.DatePicDialog.OnDatePopuClick;
import com.deya.hospital.util.LoadingView;
import com.deya.hospital.util.LoadingView.LoadingStateInter;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.vo.SupervisorQestionVo;
import com.deya.hospital.widget.popu.PopuUnTimeReport;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.im.sdk.dy.common.utils.DateUtil;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SupQuestionSearchListActivity extends BaseJumpToDepartFragment implements
        OnClickListener, OnItemClickListener,AdapterView.OnItemLongClickListener {
    private static final int QUE_DETAIL_RESULT_CODE = 0x115;
    public static final String UPDATA_ACTION = "que_list_update";
    public static final String FINISH_QUES_LIST = "finish_que_list";
    LinearLayout topcheckLay, popparent;
    CommonTopView topView;
    String title = "";
    String url = "";
    String shareMethod = "";
    int repotType;
    boolean isFirstLoad = true;
    PullToRefreshListView listView;
    List<SupervisorQestionVo> list;
    SupQuestionLisAdapter adapter;
    boolean isFirst;
    View empertyView;
    private Context mcontext;
    private View view;
    Tools tools;
    Gson gson;
    private MyHandler myHandler;
    LinearLayout networkView;
    TextView empertyText;
    private DeletDialog2 deletDialog2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {


        if (view == null) {
            view = inflater.inflate(R.layout.supvisior_question_report,
                    container, false);
            mcontext = getActivity();
            tools = new Tools(getActivity(), Constants.AC);
            gson = new Gson();
            list = new ArrayList<SupervisorQestionVo>();
            initMyHandler();
            initCheckView();

            initListView();

            isFirst = true;
            initBottomPointView();
            registerBroadcast();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }


        return view;
    }

    private void registerBroadcast() {
        brodcast = new MyBrodcastReceiver() {


            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("upload", "接收到了0");
                if (intent.getAction().equals(UPDATA_ACTION)) {
                    if(intent.hasExtra("id")){
                        for(SupervisorQestionVo tv:list){
                            if(tv.getId()==intent.getIntExtra("id",0)){
                                if(intent.hasExtra("state")){
                                tv.setState(intent.getIntExtra("state",0));
                                }
                            }

                        }
                    }

                } else if (intent.getAction().equals(
                        FINISH_QUES_LIST)) {
                   getActivity().finish();

                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FINISH_QUES_LIST);
        intentFilter.addAction(UPDATA_ACTION);
        getActivity().registerReceiver(brodcast, intentFilter);
    }

    @Override
    public void onDestroy() {
        if (null != brodcast) {
            getActivity().unregisterReceiver(brodcast);
        }

        super.onDestroy();
    }

    TextView timeTxt, deapartTxt, identiTxt, searchText;
    private Button shareBtn;
    private ProgressDialog progDailog;
    private LoadingView loadingView;

    private void initCheckView() {
        topView = (CommonTopView) view.findViewById(R.id.topView);
        topView.setVisibility(View.GONE);


        topcheckLay = (LinearLayout) view.findViewById(R.id.topcheckLay);
        popparent = (LinearLayout) view.findViewById(R.id.popparent);
        timeTxt = (TextView) view.findViewById(R.id.timeTxt);
        timeTxt.setHint("督导时间");
        deapartTxt = (TextView) view.findViewById(R.id.deapartTxt);
        identiTxt = (TextView) view.findViewById(R.id.identiTxt);
        searchText = (TextView) view.findViewById(R.id.searchText);

        loadingView = (LoadingView) view.findViewById(R.id.loadingView);
        loadingView.setLoadingListener(new LoadingStateInter() {

            @Override
            public void onloadingStart() {
                loadingView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onloadingFinish() {
                loadingView.setVisibility(View.GONE);

            }
        });
        loadingView.setVisibility(View.VISIBLE);
        loadingView.startAnimition();
        timeTxt.setOnClickListener(this);
        deapartTxt.setOnClickListener(this);

        identiTxt.setOnClickListener(this);
        searchText.setOnClickListener(this);
        initPopView();
        deletDialog2 = new DeletDialog2(mcontext, "是否删除", new DeletDialog2.DeletInter() {
            @Override
            public void onDelet(int position) {
                deletQue(position);
                deletDialog2.dismiss();
            }
        });
    }
    protected static final int DELET_DATA_SUCESS = 0x7054;
    protected static final int DELET_DATA_FAIL = 07055;
    private void deletQue(int position) {

        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("id", list.get(position).getId());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("supvisor", job.toString());
        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
                DELET_DATA_SUCESS, DELET_DATA_FAIL, job, "supervisorQuestion/deleteQuestion");
    }
    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        adapter.notifyDataSetChanged();
        page = 0;
        onloadJsMethod();
    }

    private void initListView() {
        networkView= (LinearLayout) view.findViewById(R.id.networkView);
        empertyText= (TextView) view.findViewById(R.id.empertyTextView);
        listView = (PullToRefreshListView) view.findViewById(R.id.questionList);
        adapter = new SupQuestionLisAdapter(mcontext, list);
        listView.setAdapter(adapter);
        listView.getRefreshableView().setOnItemClickListener(this);
        listView.getRefreshableView().setOnItemLongClickListener(this);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                list.clear();
                adapter.notifyDataSetChanged();
                page = 0;
                onloadJsMethod();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                onloadJsMethod();
            }
        });
    }


    LayoutInflater layoutInflater;
    View departView;
    View timeView;
    View identyView;

    public void initPopView() {
        layoutInflater = LayoutInflater.from(mcontext);
        timeView = layoutInflater
                .inflate(R.layout.supervisor_time_layout, null);
        departView = layoutInflater.inflate(R.layout.depatment_layout, null);
        identyView = layoutInflater.inflate(R.layout.identy_layout, null);
        initDepartPop(departView);
        initTimePop(timeView);
        initIdentifyPop(identyView);
    }

    RadioButton allPost, zhuanzhi, jianzhi, radioClosed,notFeedBack;

    private void initIdentifyPop(View view) {
        allPost = (RadioButton) view.findViewById(R.id.allPost);
        zhuanzhi = (RadioButton) view.findViewById(R.id.zhuanzhi);
        jianzhi = (RadioButton) view.findViewById(R.id.jianzhi);
        radioClosed = (RadioButton) view.findViewById(R.id.radiobtn4);
        radioClosed.setVisibility(View.VISIBLE);
        view.findViewById(R.id.txt2).setVisibility(View.VISIBLE);
        view.findViewById(R.id.txt1).setVisibility(View.VISIBLE);
        notFeedBack=(RadioButton) view.findViewById(R.id.notFeedBack);
        notFeedBack.setVisibility(View.VISIBLE);
        view.findViewById(R.id.bottom_view5).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        setBottomPointView(0);
                        popparent.setVisibility(View.GONE);

                    }
                });

        allPost.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setBottomPointView(0);
                reSetparams();
                regis_job = "0";
                popparent.setVisibility(View.GONE);
                identiTxt.setText("全部");
                onloadJsMethod();
            }
        });
        notFeedBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomPointView(0);
                reSetparams();
                regis_job = "1";
                popparent.setVisibility(View.GONE);
                identiTxt.setText("未反馈");
                onloadJsMethod();
            }
        });
        zhuanzhi.setText("待确认");
        zhuanzhi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetparams();

                setBottomPointView(0);
                regis_job = "2";
                identiTxt.setText("待确认");
                popparent.setVisibility(View.GONE);
                onloadJsMethod();
            }
        });
        jianzhi.setText("已确认");
        jianzhi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reSetparams();
                setBottomPointView(0);
                regis_job = "3";
                identiTxt.setText("已确认");
                popparent.setVisibility(View.GONE);
                onloadJsMethod();
            }
        });
        radioClosed.setText("已关闭");
        radioClosed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reSetparams();
                setBottomPointView(0);
                regis_job = "4";
                identiTxt.setText("已关闭");
                popparent.setVisibility(View.GONE);
                onloadJsMethod();
            }
        });

    }

    /**
     * 督导时间
     */
    TextView start_time, end_time;
    RadioButton rb_week, rb_month, rb_year, rb_lastMonth;
    TextView confirm;
    SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");

    private void initTimePop(View view) {
        Calendar cal = Calendar.getInstance();
        final Date localTime = cal.getTime();

        view.findViewById(R.id.whiteView).setVisibility(View.VISIBLE);
        view.findViewById(R.id.lastMonth).setVisibility(View.VISIBLE);
        rb_lastMonth = (RadioButton) view.findViewById(R.id.lastMonth);
        rb_lastMonth.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reSetparams();
                timeTxt.setText("上月");
                start_time.setText(dateFormater.format(DateUtil
                        .getFirstDayLastMonth(localTime)) + "");
                end_time.setText(dateFormater.format(DateUtil
                        .getLastDayOfLastMonth(localTime)) + "");
                starttime = start_time.getText().toString();
                endtime = end_time.getText().toString();
                setBottomPointView(0);
                popparent.setVisibility(View.GONE);
                onloadJsMethod();

            }
        });
        confirm = (TextView) view.findViewById(R.id.confirm2);
        start_time = (TextView) view.findViewById(R.id.start_time);
        start_time.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePicDialog dialog = new DatePicDialog(mcontext,
                        new OnDatePopuClick() {

                            @Override
                            public void enter(String text) {
                                start_time.setText(text);
                                starttime = text;
                                if ("".equals(start_time.getText() + "")
                                        || "".equals(end_time.getText() + "")) {
                                    confirm.setTextColor(getResources()
                                            .getColor(R.color.gray));
                                    confirm.setClickable(false);
                                } else {

                                    confirm.setTextColor(getResources()
                                            .getColor(R.color.blue));
                                    confirm.setClickable(true);
                                }
                            }

                        });
                dialog.show();
                WindowManager windowManager = getActivity().getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = dialog.getWindow()
                        .getAttributes();
                lp.width = (int) (display.getWidth()); // 设置宽度
                lp.height = (int) (display.getHeight()); // 设置高度
                dialog.getWindow().setAttributes(lp);
            }
        });

        end_time = (TextView) view.findViewById(R.id.end_time);
        end_time.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePicDialog dialog = new DatePicDialog(mcontext,
                        new OnDatePopuClick() {

                            @Override
                            public void enter(String text) {
                                end_time.setText(text);
                                endtime = text;
                                if ("".equals(start_time.getText() + "")
                                        || "".equals(end_time.getText() + "")) {
                                    confirm.setTextColor(getResources()
                                            .getColor(R.color.gray));
                                    confirm.setClickable(false);
                                } else {

                                    confirm.setTextColor(getResources()
                                            .getColor(R.color.blue));
                                    confirm.setClickable(true);
                                }
                            }

                        });
                dialog.show();
                WindowManager windowManager = getActivity().getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = dialog.getWindow()
                        .getAttributes();
                lp.width = (int) (display.getWidth()); // 设置宽度
                lp.height = (int) (display.getHeight()); // 设置高度
                dialog.getWindow().setAttributes(lp);
            }
        });

        rb_week = (RadioButton) view.findViewById(R.id.rb_week);
        rb_week.setText("本月");
        rb_month = (RadioButton) view.findViewById(R.id.rb_month);
        rb_month.setText("本季度");
        rb_year = (RadioButton) view.findViewById(R.id.rb_year);

        rb_week.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reSetparams();
                popparent.setVisibility(View.GONE);
                timeTxt.setText("本月");
                start_time.setText(dateFormater.format(DateUtil
                        .getFirstDayOfMonth(localTime)) + "");
                end_time.setText(dateFormater.format(DateUtil
                        .getLastDayOfMonth(localTime)));
                starttime = start_time.getText().toString();
                endtime = end_time.getText().toString();
                setBottomPointView(0);
                onloadJsMethod();
            }
        });


        rb_month.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reSetparams();
                popparent.setVisibility(View.GONE);
                timeTxt.setText("本季度");
                start_time.setText(dateFormater.format(DateUtil
                        .getFirstDayOfQuarter(localTime)) + "");
                end_time.setText(dateFormater.format(DateUtil
                        .getLastDayOfQuarter(localTime)));
                starttime = start_time.getText().toString();
                endtime = end_time.getText().toString();
                setBottomPointView(0);
                onloadJsMethod();
            }
        });
        rb_year.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                reSetparams();
                popparent.setVisibility(View.GONE);
                timeTxt.setText("今年");

                SimpleDateFormat dateFormater = new SimpleDateFormat(
                        "yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_YEAR, 1);
                cal.getTime();
                start_time.setText(dateFormater.format(cal.getTime()) + "");

                cal.set(Calendar.DAY_OF_YEAR,
                        cal.getActualMaximum(Calendar.DAY_OF_YEAR));
                end_time.setText(dateFormater.format(cal.getTime()));
                starttime = start_time.getText().toString();
                endtime = end_time.getText().toString();
                setBottomPointView(0);
                onloadJsMethod();
            }
        });

        // if ("".equals(start_time.getText() + "")
        // || "".equals(end_time.getText() + "")) {
        // confirm.setTextColor(getResources().getColor(R.color.gray));
        // confirm.setClickable(false);
        // } else {
        // confirm.setClickable(true);
        // confirm.setTextColor(getResources().getColor(R.color.blue));
        //
        // }
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reSetparams();
                rb_lastMonth.setChecked(false);
                rb_month.setChecked(false);
                rb_week.setChecked(false);
                rb_year.setChecked(false);
                popparent.setVisibility(View.GONE);
                setBottomPointView(0);
                onloadJsMethod();
            }
        });
        view.findViewById(R.id.canser2).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        reSetparams();
                        rb_lastMonth.setChecked(false);
                        rb_month.setChecked(false);
                        rb_week.setChecked(false);
                        rb_year.setChecked(false);
                        start_time.setText("");
                        end_time.setText("");
                        starttime = "";
                        endtime = "";
                        timeTxt.setText("");
                        onloadJsMethod();
                        popparent.setVisibility(View.GONE);
                        setBottomPointView(0);

                    }
                });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.timeTxt:
                showPopLay(timeView);
                setBottomPointView(3);
                break;
            case R.id.deapartTxt:
              onMutiChoose();
                break;
            case R.id.identiTxt:
                showPopLay(identyView);
                setBottomPointView(2);
                break;
            case R.id.searchText:
                onloadJsMethod();
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onChooseSuc(DepartVos.DepartmentListBean bean) {

    }

    @Override
    protected void onChooseSuc(String names, String ids) {
        deapartTxt.setText(names);
        departments = ids;
        page =0;
        reSetparams();
        popparent.setVisibility(View.GONE);
        setBottomPointView(0);
        onloadJsMethod();
    }

    public void resetDepartData() {
        for (DepartLevelsVo dv : departlist) {
            dv.setChooseNum(0);
            for (ChildsVo cv : dv.getChilds()) {
                cv.setChoosed(false);
            }
        }

        selectMap.clear();
        departAdapter.setData(departlist);
        childAdapter.setData(childList);
        departments = "";
        deapartTxt.setText("");
    }


    PopuUnTimeReport dialog;
    String emailTx = "";

    private MyBrodcastReceiver brodcast;


    /**
     * 按科室选择
     */
    ListView popListView, childListView;
    DepartListAdapter departAdapter;
    List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
    List<DepartLevelsVo> cacheList = new ArrayList<DepartLevelsVo>();
    List<ChildsVo> childList = new ArrayList<ChildsVo>();
    private HashMap<String, ChildsVo> selectMap = new HashMap<String, ChildsVo>();
    ChildDepartListAdapter childAdapter;
    TextView allDepartTv;

    int depatCheckPosition = 0;
    String departments = "";

    public void initDepartPop(View view) {
        getSaveLevels();
        view.findViewById(R.id.pop1).setVisibility(View.VISIBLE);
        popListView = (ListView) view.findViewById(R.id.poplist);
        allDepartTv = (TextView) view.findViewById(R.id.allDepartTv);
        allDepartTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reSetparams();
                deapartTxt.setText("全部");
                departments = "0";
                popparent.setVisibility(View.GONE);
                setBottomPointView(0);
                onloadJsMethod();

            }
        });
        view.findViewById(R.id.titleView).setVisibility(View.VISIBLE);
        view.findViewById(R.id.confirm).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        reSetparams();
                        String departNames = "";
                        departments = "";
                        popparent.setVisibility(View.GONE);
                        setBottomPointView(0);
                        Iterator<String> iter = selectMap.keySet().iterator();

                        int i = 0;
                        while (iter.hasNext()) {
                            ChildsVo value = selectMap.get(iter.next());
                            if (i < selectMap.size() - 1) {
                                departments += value.getId() + ",";
                                departNames += value.getName() + ",";
                            } else {
                                departments += value.getId();
                                departNames += value.getName();
                            }

                            i++;

                        }
                        deapartTxt.setText(departNames);
                        onloadJsMethod();
                    }
                });
        view.findViewById(R.id.bottom_view1).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popparent.setVisibility(View.GONE);
                        setBottomPointView(0);

                    }
                });
        view.findViewById(R.id.editorlay).setVisibility(View.VISIBLE);
        view.findViewById(R.id.canser).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        reSetparams();
                        resetDepartData();
                        onloadJsMethod();
                        popparent.setVisibility(View.GONE);
                        setBottomPointView(0);
                    }
                });
        departAdapter = new DepartListAdapter(mcontext, departlist);

        childAdapter = new ChildDepartListAdapter(mcontext, childList, 1);
        childListView = (ListView) view.findViewById(R.id.levelList);

        childListView.setAdapter(childAdapter);
        popListView.setAdapter(departAdapter);
        popListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                childList.clear();
                DepartLevelsVo dv = departlist.get(arg2);
                depatCheckPosition = arg2;
                childList.addAll(dv.getChilds());
                childAdapter.setData(childList);
                departAdapter.setIsCheck(arg2);
                allDepartTv.setBackgroundResource(R.color.listselect);

            }

        });
        childListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int arg2,
                                    long id) {
                boolean isSelect = !childList.get(arg2).isChoosed();
                for (DepartLevelsVo dlv : departlist) {
                    for (ChildsVo pcv : dlv.getChilds()) {
                        if (pcv.getId().equals(childList.get(arg2).getId())) {
                            pcv.setChoosed(isSelect);
                            if (isSelect) {
                                dlv.setChooseNum(dlv.getChooseNum() + 1);
                            } else {
                                dlv.setChooseNum(dlv.getChooseNum() - 1);
                            }
                        }

                    }

                }
                childList.get(arg2).setChoosed(isSelect);
                Log.i("departchoose", isSelect + "");
                childAdapter.setData(childList);
                departAdapter.notifyDataSetChanged();

                if (isSelect) {
                    selectMap.put(childList.get(arg2).getId() + "",
                            childList.get(arg2));

                } else {
                    selectMap.remove(childList.get(arg2).getId() + "");
                }

            }
        });

    }

    ImageView iv1, iv2, iv3;
    LinearLayout checkPostionLay;

    public void initBottomPointView() {
        checkPostionLay = (LinearLayout) view.findViewById(R.id.checkPostion);
        iv1 = (ImageView) view.findViewById(R.id.iv1);
        iv2 = (ImageView) view.findViewById(R.id.iv2);
        iv3 = (ImageView) view.findViewById(R.id.iv3);
    }

    public void setBottomPointView(int position) {
        switch (position) {
            case 0:
                iv1.setVisibility(View.GONE);
                iv2.setVisibility(View.GONE);
                iv3.setVisibility(View.GONE);
                break;
            case 1:
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.GONE);
                iv3.setVisibility(View.GONE);
                break;
            case 2:
                iv1.setVisibility(View.GONE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.GONE);
                break;
            case 3:
                iv1.setVisibility(View.GONE);
                iv2.setVisibility(View.GONE);
                iv3.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }

    }

    private void getSaveLevels() {
        cacheList.clear();
        String str = SharedPreferencesUtil.getString(mcontext, "depart_levels",
                "");
        String childsStr = SharedPreferencesUtil.getString(mcontext,
                "departmentlist", "");
        List<ChildsVo> list2 = TaskUtils.gson.fromJson(childsStr,
                new TypeToken<List<ChildsVo>>() {
                }.getType());
        List<ChildsVo> otherList = new ArrayList<ChildsVo>();
        if (!AbStrUtil.isEmpty(str)) {
            for (ChildsVo cv : list2) {
                if (cv.getParent().equals("0") || cv.getParent().equals("1")) {
                    otherList.add(cv);
                }
            }
            List<DepartLevelsVo> list = TaskUtils.gson.fromJson(str,
                    new TypeToken<List<DepartLevelsVo>>() {
                    }.getType());
            cacheList.addAll(list);
            for (DepartLevelsVo dlv : cacheList) {
                if (dlv.getRoot().getId().equals("0")) {
                    if (dlv.getChilds().size() == 0) {
                        dlv.getChilds().addAll(otherList);
                        break;
                    }
                }
            }

        } else {

            DepartLevelsVo dlv = new DepartLevelsVo();
            dlv.getRoot().setId("0");
            dlv.getRoot().setName("其他");
            if (list2 != null) {
                dlv.getChilds().addAll(list2);
            }
            cacheList.add(dlv);

        }

        departlist.addAll(cacheList);
        if (departlist.size() > 0) {
            childList.addAll(departlist.get(0).getChilds());
        }

    }

    public String getBase64Authent() {
        return AbStrUtil.getBase64(tools.getValue(Constants.AUTHENT));
    }


    public void reSetparams() {
        page = 0;
        totalPage = 0;
        isFirst = false;
//        starttime = "";
//        endtime = "";
//        timetype = "";
//        regis_job = "0";
//        department = "";
//        authent = "";
        list.clear();
        adapter.notifyDataSetChanged();

    }

    String starttime = "";
    String endtime = "";
    String timetype = "1";
    String regis_job = "0";
    String department = "";
    String authent = "";

    int page = 0;
    int totalPage;

    public void onloadJsMethod() {

        JSONObject job = new JSONObject();
        try {

            job.put("authent", tools.getValue(Constants.AUTHENT));
            if (!isFirst) {
                job.put("startTime", starttime);
                job.put("endTime", endtime);
                job.put("department", departments);
            }
            if(getActivity().getIntent().hasExtra("origin")){
                job.put("origin", getActivity().getIntent().getIntExtra("origin",0));
            }
            job.put("state", regis_job);
            job.put("pageIndex", page + 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
                GET_DATA_SUCESS, GET_DATA_FAIL, job, "supervisorQuestion/questionList");


    }

    protected static final int GET_DATA_SUCESS = 0x7050;
    protected static final int GET_DATA_FAIL = 07051;


    private void initMyHandler() {
        myHandler = new MyHandler(getActivity()) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = myHandler.mactivity.get();
                if (null != activity) {
                    switch (msg.what) {
                        case GET_DATA_SUCESS:
                            loadingView.stopAnimition();
                            if (null != msg && null != msg.obj) {
                                listView.onRefreshComplete();
                                try {
                                    setDataResult(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e5) {
                                    e5.printStackTrace();
                                }
                            }
                            break;
                        case GET_DATA_FAIL:
                            listView.onRefreshComplete();
                            loadingView.stopAnimition();
                            networkView.setVisibility(View.VISIBLE);
                            empertyText.setText("抱歉，暂时没有找到相关的数据！");

                            break;
                        case DELET_DATA_SUCESS:
                            dismissdialog();
                            try {
                                setDelet(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }

                            break;
                        case DELET_DATA_FAIL:
                            dismissdialog();
                            ToastUtil.showMessage("亲,您的网络不顺畅哦!");
                            break;
                        default:
                            break;
                    }
                }
            }
        };
    }
    private void setDelet(JSONObject jsonObject) {
        ToastUtil.showMessage(jsonObject.optString("result_msg"));
        if (jsonObject.optInt("result_id") == 0) {
            list.clear();
            adapter.notifyDataSetChanged();
            page = 0;
            onloadJsMethod();
            onloadJsMethod();
        }


    }
    private void setDataResult(JSONObject jsonObject) {
        page++;
        JSONArray jarr = jsonObject.optJSONArray("list");
        if(null==jarr){
            return;
        }
        List<SupervisorQestionVo> cachelist = TaskUtils.gson.fromJson(jarr.toString(),
                new TypeToken<List<SupervisorQestionVo>>() {
                }.getType());

        totalPage = jsonObject.optInt("pageTotal");
        if (null != list && null != cachelist && cachelist.size() > 0) {
            list.addAll(cachelist);
            adapter.notifyDataSetChanged();
        }
        if (page >= totalPage) {
            listView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);

        } else {
            listView.setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
        }

        if(list.size()<=0){
           listView.setVisibility(View.GONE);
            networkView.setVisibility(View.VISIBLE);
            empertyText.setText("抱歉，暂时没有找到相关的数据！");
        }else{
            listView.setVisibility(View.VISIBLE);
            networkView.setVisibility(View.GONE);
        }

    }

    String shareUrl = "";


    public void showPopLay(View view) {
        popparent.setVisibility(View.VISIBLE);
        popparent.removeAllViews();
        popparent.addView(view);
    }

    int jumPositon = -1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent it = new Intent(mcontext, SupervisorQesDetitalActivity.class);
        it.putExtra("id", list.get(position - 1).getId() + "");
        it.putExtra("state", list.get(position - 1).getState());
        it.putExtra("creatorId", list.get(position - 1).getUid());
        startActivityForResult(it, QUE_DETAIL_RESULT_CODE);
        jumPositon = position - 1;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        deletDialog2.show();
        deletDialog2.setDeletPosition(position-1);
        return true;
    }
}
