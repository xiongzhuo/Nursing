package com.deya.hospital.workspace.multidrugresistant;

import android.annotation.SuppressLint;
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
import com.deya.hospital.adapter.BaseTaskAadpter;
import com.deya.hospital.adapter.ChildDepartListAdapter;
import com.deya.hospital.adapter.DepartListAdapter;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.ComomDialog;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DatePicDialog;
import com.deya.hospital.util.DatePicDialog.OnDatePopuClick;
import com.deya.hospital.util.LoadingView;
import com.deya.hospital.util.LoadingView.LoadingStateInter;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.DynamicStasticVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.widget.popu.PopuUnTimeReport;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.priviewbase.MutiResitantCreatAdapter;
import com.deya.hospital.workspace.site_infection.SiteInfectionPriviewAcitivty;
import com.deya.hospital.workspace.threepips.ThreePipsListAdapter;
import com.deya.hospital.workspace.threepips.ThreePipsPriviewActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.im.sdk.dy.common.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@SuppressLint({"ValidFragment"})
public class ResistantListSearchFragment extends BaseFragment implements
        OnClickListener, OnItemClickListener {
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
    List<CaseListVo.CaseListBean> list;
    BaseTaskAadpter adapter;
    boolean isFirst;
    private Context mcontext;
    private View view;
    Tools tools;
    Gson gson;
    private MyHandler myHandler;
    LinearLayout networkView;
    TextView empertyText;
    String type_id = "";
    boolean isRefreshData = true;
    String code = "";
    boolean isDynamic;
    DynamicStasticVo.ListBean bean;
    private ComomDialog deletDialog;

    public static ResistantListSearchFragment newInstance(String code) {
        ResistantListSearchFragment resistantListSearchFragment = new ResistantListSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        resistantListSearchFragment.setArguments(bundle);
        return resistantListSearchFragment;
    }

    public static ResistantListSearchFragment newInstance(DynamicStasticVo.ListBean bean) {
        ResistantListSearchFragment resistantListSearchFragment = new ResistantListSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", bean);
        resistantListSearchFragment.setArguments(bundle);
        return resistantListSearchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {

        Bundle args = getArguments();
        if (view == null) {
            view = inflater.inflate(R.layout.supvisior_question_report,
                    container, false);
            mcontext = getActivity();
            if (args.containsKey("data")) {
                bean = (DynamicStasticVo.ListBean) args.getSerializable("data");
                code = bean.getCode();
                isDynamic = true;
            } else {
                code = args.getString("code");
            }
            if(code.equals("DN")){
                type_id="1";
            }else if(code.equals("SG")){
                type_id="2";
            }else if(code.equals("SIP")){
                type_id="4";
            }

            tools = new Tools(getActivity(), Constants.AC);
            gson = new Gson();
            list = new ArrayList<CaseListVo.CaseListBean>();
            initMyHandler();
            initCheckView();
            isFirst = true;
            initListView();


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

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        adapter.notifyDataSetChanged();
        page = 0;
        onloadJsMethod();

    }

    private void registerBroadcast() {
        brodcast = new MyBrodcastReceiver() {


            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("upload", "接收到了0");
                if (intent.getAction().equals(UPDATA_ACTION)) {
                    if (intent.hasExtra("id")) {

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

        if (null != deletDialog && deletDialog.isShowing()) {
            deletDialog.dismiss();
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
        timeTxt.setHint("检出时间");
        deapartTxt = (TextView) view.findViewById(R.id.deapartTxt);
        identiTxt = (TextView) view.findViewById(R.id.identiTxt);
        identiTxt.setText("完成状态");
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
    }

    private void initListView() {
        networkView = (LinearLayout) view.findViewById(R.id.networkView);
        empertyText = (TextView) view.findViewById(R.id.empertyTextView);
        listView = (PullToRefreshListView) view.findViewById(R.id.questionList);
        if (code.equals("DN")) {
            adapter = new MutiResitantCreatAdapter(mcontext, list);
        } else if (code.equals("SIP")) {
            adapter = new MutiResitantCreatAdapter(mcontext, list);
        } else if (code.equals("SG")) {
            adapter = new ThreePipsListAdapter(mcontext, list);
        }
        listView.setAdapter(adapter);
        listView.getRefreshableView().setOnItemClickListener(this);
        if (isDynamic) {
            deapartTxt.setText(bean.getDep_name());
            departments = bean.getDep_no();
            starttime = bean.getDate() + " " + "00:00:00";
            endtime = bean.getDate() + " " + "23:59:59";
            start_time.setText(starttime);
            end_time.setText(endtime);
            isFirst = false;
        }

        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                deletDialog = new ComomDialog(getActivity(), "确认删除此病例？",
                        R.style.SelectDialog, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletDialog.dismiss();
                        deletPosiotion = position - 1;
                        CaseListVo.CaseListBean bean = list.get(position - 1);
                        if (bean.getTask_id() != 0) {
                            removeCase(bean.getTask_id() + "");
                        }
                    }
                });
                deletDialog.show();

                return true;
            }
        });
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

    int deletPosiotion = -1;

    public void removeCase(String caseId) {   //CHECK_SPECIMEN   ： 送检标本DRUG_RESISTANCE  ： 耐药菌
        showprocessdialog();

        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("task_id", caseId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(), RisitantRequestCode.REMOVE_CASE_SUCESS,
                RisitantRequestCode.REMOVE_CASE_FAIL, job, "comm/task/remove");

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

    RadioButton allPost, zhuanzhi, jianzhi, radioClosed, notFeedBack;

    private void initIdentifyPop(View view) {
        allPost = (RadioButton) view.findViewById(R.id.allPost);
        zhuanzhi = (RadioButton) view.findViewById(R.id.zhuanzhi);
        jianzhi = (RadioButton) view.findViewById(R.id.jianzhi);
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
                regis_job = "";
                popparent.setVisibility(View.GONE);
                identiTxt.setText("全部");
                onloadJsMethod();
            }
        });

        zhuanzhi.setText("未完成");
        zhuanzhi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetparams();

                setBottomPointView(0);
                regis_job = "0";
                identiTxt.setText("未完成");
                popparent.setVisibility(View.GONE);
                onloadJsMethod();
            }
        });
        jianzhi.setText("已完成");
        jianzhi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reSetparams();
                setBottomPointView(0);
                regis_job = "1";
                identiTxt.setText("已完成");
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
        start_time.setText(dateFormater.format(DateUtil
                .getFirstDayOfMonth(localTime)) + "");
        end_time.setText(dateFormater.format(DateUtil
                .getLastDayOfMonth(localTime)));
        starttime = start_time.getText().toString();
        endtime = end_time.getText().toString();
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
                // TODO Auto-generated method stubn
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

        view.findViewById(R.id.bottom_view4).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
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
                showPopLay(departView);
                setBottomPointView(1);
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
        isRefreshData = true;
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
    String regis_job = "";
    String department = "";
    String authent = "";

    int page = 0;
    int totalPage;

    public void onloadJsMethod() {

        JSONObject job = new JSONObject();
        try {

            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("temp_type_id", type_id);
            job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
            if (!isFirst) {
                job.put("startTime", starttime);
                job.put("endTime", endtime);
            }
            if (!AbStrUtil.isEmpty(departments)) {
                job.put("department_id", departments);
            }
            if (!AbStrUtil.isEmpty(regis_job)) {
                job.put("status", regis_job);
            }
            job.put("pageIndex", page + 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
                GET_DATA_SUCESS, GET_DATA_FAIL, job, "tempTask/caseList");


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
                            if (list.size() <= 0) {
                                listView.setVisibility(View.GONE);
                                loadingView.stopAnimition();
                                networkView.setVisibility(View.VISIBLE);
                                empertyText.setText("亲，您的网络不顺畅哦,\r\n请检查您的网络，再继续访问!");
                            } else {
                                listView.setVisibility(View.VISIBLE);
                                networkView.setVisibility(View.GONE);
                            }


                            break;
                        case RisitantRequestCode.REMOVE_CASE_SUCESS:
                            dismissdialog();
                            if (deletPosiotion >= 0 && deletPosiotion < list.size()) {
                                list.remove(deletPosiotion);
                                adapter.notifyDataSetChanged();
                                deletPosiotion = -1;
                            }

                            break;
                        case RisitantRequestCode.REMOVE_CASE_FAIL:
                            dismissdialog();
                            deletPosiotion = -1;
                            break;

                        default:
                            break;
                    }
                }
            }
        };
    }

    private void setDataResult(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            page++;
            CaseListVo rev = gson.fromJson(jsonObject.toString(), CaseListVo.class);
            if (isRefreshData) {
                list.clear();
                isRefreshData = false;
            }
            for (CaseListVo.CaseListBean listbean : rev.getCase_list()) {
                if (listbean!= null) {
                    list.add(listbean);
                }
            }
            adapter.notifyDataSetChanged();
            totalPage = jsonObject.optInt("pageTotal");
            if (page > totalPage) {
                listView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
            } else {
                listView.setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
            }
        }
        if (list.size() <= 0) {
            listView.setVisibility(View.GONE);
            empertyText.setText("抱歉，暂时没有可匹配的数据！");
            networkView.setVisibility(View.VISIBLE);

        } else {
            listView.setVisibility(View.VISIBLE);
            networkView.setVisibility(View.GONE);
        }

    }


    public void showPopLay(View view) {
        popparent.setVisibility(View.VISIBLE);
        popparent.removeAllViews();
        popparent.addView(view);
    }

    int jumPositon = -1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CaseListVo.CaseListBean taskListBean = list.get(position - 1);
        if (null == taskListBean) {
            return;
        }
        Intent in = new Intent();
        TaskVo taskVo = TaskUtils.onFindTaskByKeyAndValue("task_id", taskListBean.getTask_id());
        if (null == taskVo) {
            taskVo = new TaskVo();
            taskVo.setTaskListBean(TaskUtils.gson.toJson(taskListBean));
        }
        int business_id = taskListBean.getCase_id();
        taskVo.setId(business_id);
        taskVo.setSub_id(taskListBean.getSub_id() + "");
        taskVo.setTask_id(taskListBean.getTask_id());
        taskVo.setDepartment(taskListBean.getDepartment_id());
        taskVo.setFinished(1);
        taskVo.setDepartmentName(taskListBean.getDepartmentName());
        if (null == taskListBean.getTime()) {
            taskVo.setMission_time(TaskUtils.getLoacalDate());
        } else {
            taskVo.setMission_time(taskListBean.getTime());
        }
        in.putExtra("data", taskVo);
        in.putExtra("type_id", type_id);
        in.putExtra("code", code);
        if (code.equals("DN")) {
            in.setClass(mcontext, MutiRisitantPriviewActivity.class);
        } else  if (code.equals("SG")) {
            in.setClass(mcontext, ThreePipsPriviewActivity.class);
        }else if (code.equals("SIP")){
            in.setClass(mcontext, SiteInfectionPriviewAcitivty.class);
        }
        startActivity(in);

    }
}
