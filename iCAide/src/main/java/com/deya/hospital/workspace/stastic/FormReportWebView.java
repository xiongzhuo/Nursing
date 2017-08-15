package com.deya.hospital.workspace.stastic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.ChildDepartListAdapter;
import com.deya.hospital.adapter.DepartListAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.BaseTypeAdapter;
import com.deya.hospital.base.DepartChooseActivity;
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
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.vo.HotVo;
import com.deya.hospital.widget.popu.PopuUnTimeReport;
import com.deya.hospital.widget.popu.PopuUnTimeReport.OnPopuClick;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.reflect.TypeToken;
import com.im.sdk.dy.common.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FormReportWebView extends BaseActivity implements
        OnClickListener {
    WebView webView;
    LinearLayout topcheckLay, popparent;
    CommonTopView topView;
    String title = "";
    String url = "";
    String shareMethod = "";
    int repotType;
    boolean isFirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_form_report);
        authent = getBase64Authent();
        onResetParams();
        initCheckView();
        initWebView();
        initBottomPointView();
    }

    TextView timeTxt, deapartTxt, identiTxt, searchText, keyText;
    private Button shareBtn;
    private ProgressDialog progDailog;
    private LoadingView loadingView;

    private void initCheckView() {
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);
        title = getIntent().getStringExtra("webtitle");
        keyList = (List<HotVo>) getIntent().getSerializableExtra("keyList");
        if (getIntent().hasExtra("psoition")) {
            type = getIntent().getIntExtra("psoition", -2) + 1;
        }
        topView.setTitle(title);

        shareBtn = (Button) this.findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);
        findViewById(R.id.emailBtn).setOnClickListener(this);

        topcheckLay = (LinearLayout) this.findViewById(R.id.topcheckLay);
        popparent = (LinearLayout) this.findViewById(R.id.popparent);
        timeTxt = (TextView) this.findViewById(R.id.timeTxt);
        deapartTxt = (TextView) this.findViewById(R.id.deapartTxt);
        if(TaskUtils.isPartTimeJob(mcontext)){
            deapartTxt.setText(tools.getValue(Constants.DEFULT_DEPART_NAME));
            departments=tools.getValue(Constants.DEFULT_DEPARTID);
        }
        identiTxt = (TextView) this.findViewById(R.id.identiTxt);
        if(TaskUtils.mysticalJob(mcontext)){
            identiTxt.setText("暗访");
            return;
        }
        searchText = (TextView) this.findViewById(R.id.searchText);
        keyText = (TextView) this.findViewById(R.id.keyText);

        loadingView = (LoadingView) this.findViewById(R.id.loadingView);
        // progDailog = ProgressDialog.show(activity, "正在加载", "努力加载中。。。", true);
        // progDailog.setCancelable(true);
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
        keyText.setOnClickListener(this);
        initPopView();
    }

    @SuppressLint("NewApi")
    private void initWebView() {
        webView = (WebView) this.findViewById(R.id.webview_compontent);
        WebSettings settings = webView.getSettings();
//		if (repotType == 1) {
//			progDailog = ProgressDialog.show(this, "加载中", "...", true);
//			progDailog.setCancelable(true);
//		}
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setGeolocationEnabled(true);
        settings.setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setVerticalScrollbarOverlay(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setHorizontalScrollbarOverlay(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                 view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                // topView.setTitle(view.getTitle());]
                if (null != loadingView) {
                    loadingView.stopAnimition();
                }
                if (isFirstLoad) {
                    onloadJsMethod();
                    isFirstLoad = false;
                }

            }

        });

        webView.clearCache(true);

        webView.loadUrl(url);

    }


    LayoutInflater layoutInflater;
    View departView;
    View timeView;
    View identyView;
    View keyView;

    public void initPopView() {
        layoutInflater = LayoutInflater.from(mcontext);
        timeView = layoutInflater
                .inflate(R.layout.supervisor_time_layout, null);
        departView = layoutInflater.inflate(R.layout.depatment_layout, null);
        identyView = layoutInflater.inflate(R.layout.identy_layout, null);
        keyView = layoutInflater.inflate(R.layout.supervisor_type_layout, null);
        keyView.findViewById(R.id.pop2).setVisibility(View.VISIBLE);
        initTimePop(timeView);
        initIdentifyPop(identyView);
        initKeyPop(keyView);
    }

    GridView keyGv;
    KeyAdapter keyAdapter;
    List<HotVo> keyList;

    private void initKeyPop(View keyView) {
        keyGv = (GridView) keyView.findViewById(R.id.gv);
        keyAdapter = new KeyAdapter(mcontext, keyList);
        keyGv.setAdapter(keyAdapter);
        keyGv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (repotType == 1) {
                    keyAdapter.setChooseItem(position);
                    keyText.setText(keyList.get(position).getName());
                    popparent.setVisibility(View.GONE);
                    type = position + 1;
                    onloadJsMethod();
                    setBottomPointView(0);
                } else if (repotType == 3) {
                    url = (position == 0 ? WebUrl.WEB_FORM_COUNT
                            : WebUrl.WEB_QUESTION_COUNT);
                    shareMethod = (position == 0 ? "/gkgzj/gridCountShare.html?u="
                            : "/gkgzj/gridSupervisorShare.html?u=");
                }
            }
        });
    }

    public class KeyAdapter extends BaseTypeAdapter<HotVo> {
        int chooseIndex;
        Context context;
        List<HotVo> list;

        public KeyAdapter(Context context, List<HotVo> list) {
            super(context, list);
            this.context = context;
            this.list = list;
        }

        public void setChooseItem(int position) {
            chooseIndex = position;
            notifyDataSetChanged();
        }

        @Override
        public void setViewData(BaseTypeAdapter<HotVo>.ViewHolder viewhoder, int position) {
            if (chooseIndex != position) {
                viewhoder.textView.setBackgroundResource(R.drawable.circle_sharp_blue);
                viewhoder.textView.setTextColor(context.getResources().getColor(
                        R.color.top_color));
            } else {
                viewhoder.textView.setBackgroundResource(R.drawable.round_orange);
                viewhoder.textView.setTextColor(context.getResources().getColor(R.color.white));
            }
            viewhoder.textView.setText(list.get(position).getName());
        }

    }

    RadioButton allPost, zhuanzhi, jianzhi;

    private void initIdentifyPop(View view) {
        allPost = (RadioButton) view.findViewById(R.id.allPost);
        zhuanzhi = (RadioButton) view.findViewById(R.id.zhuanzhi);
        zhuanzhi.setText("抽查");
        jianzhi = (RadioButton) view.findViewById(R.id.jianzhi);
        jianzhi.setText("自查");
        view.findViewById(R.id.txt2).setVisibility(View.VISIBLE);
        RadioButton radiobtn4= (RadioButton) view.findViewById(R.id.radiobtn4);
        radiobtn4.setVisibility(View.VISIBLE);
        radiobtn4.setText("暗访");
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
                check_type = "0";
                popparent.setVisibility(View.GONE);
                identiTxt.setText("全部");
                onloadJsMethod();
            }
        });
        zhuanzhi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomPointView(0);
                check_type = "10";
                identiTxt.setText("抽查");
                popparent.setVisibility(View.GONE);
                onloadJsMethod();
            }
        });
        jianzhi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setBottomPointView(0);
                check_type = "20";
                identiTxt.setText("自查");
                popparent.setVisibility(View.GONE);
                onloadJsMethod();
            }
        });
        radiobtn4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomPointView(0);
                check_type = "30";
                identiTxt.setText("暗访");
                popparent.setVisibility(View.GONE);
                onloadJsMethod();
            }
        });

    }

    @Override
    protected void onDestroy() {
        topView = null;
        dateFormater = null;
        departlist = null;
        cacheList = null;
        childList = null;
        webView.clearCache(true);
        super.onDestroy();
    }

    /**
     * 督导时间
     */
    TextView start_time, end_time;
    RadioButton rb_week, rb_month, rb_year, rb_lastMonth;
    TextView confirm;
    SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");

    private void initTimePop(View view) {
        timeTxt.setText("本月");
        Calendar cal = Calendar.getInstance();
        final Date localTime = cal.getTime();

        view.findViewById(R.id.whiteView).setVisibility(View.VISIBLE);
        view.findViewById(R.id.lastMonth).setVisibility(View.VISIBLE);
        rb_lastMonth = (RadioButton) view.findViewById(R.id.lastMonth);
        rb_lastMonth.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
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
                WindowManager windowManager = getWindowManager();
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
                WindowManager windowManager = getWindowManager();
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
        rb_week.setChecked(true);
        rb_month = (RadioButton) view.findViewById(R.id.rb_month);
        rb_month.setText("本季度");
        rb_year = (RadioButton) view.findViewById(R.id.rb_year);

        rb_week.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

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
                popparent.setVisibility(View.GONE);
                timeTxt.setText("今年");


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
                setBottomPointView(1);
                break;
            case R.id.deapartTxt:
                if(TaskUtils.isPartTimeJob(mcontext)){
                    return;
                }
//                showPopLay(departView);
//                setBottomPointView(2);
                Intent intent = new Intent(mcontext, DepartChooseActivity.class);
                intent.putExtra(DepartChooseActivity.MUTICHOOSE, 1);
                startActivityForResult(intent, DepartChooseActivity.CHOOSE_SUC);
                break;
            case R.id.identiTxt:
                if(TaskUtils.mysticalJob(mcontext)){
                    return;
                }
                showPopLay(identyView);
                setBottomPointView(3);
                break;
            case R.id.searchText:
                onloadJsMethod();
                break;
            case R.id.keyText:
                showPopLay(keyView);
                setBottomPointView(4);
                break;
            case R.id.shareBtn:
                showShareDialog(title, "点击查看", shareUrl);
                break;
            case R.id.emailBtn:
                sendEmails();
                break;
            default:
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (resultCode == DepartChooseActivity.CHOOSE_SUC) {
            List<DepartVos.DepartmentListBean> departs = (List<DepartVos.DepartmentListBean>) data.getSerializableExtra("departs");
            String names = "";
            departments="";
            for (int i = 0; i < departs.size(); i++) {
                DepartVos.DepartmentListBean bean = departs.get(i);

                if (i < departs.size() - 1) {
                    departments += bean.getId() + ",";
                    names += bean.getName() + ",";
                } else {
                    departments += bean.getId();
                    names += bean.getName();
                }
            }
            deapartTxt.setText(names);
           onloadJsMethod();
        }


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

    protected static final int SEND_SUCESS = 0x6001;

    protected static final int SEN_FAILE = 0x6002;
    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case SEND_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setSendResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case SEN_FAILE:
                        ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void sendEail(String text) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("startTime", starttime);
            job.put("endTime", endtime);
            job.put("check_type", check_type);
            job.put("department", departments);
            job.put("email", text);
            job.put("flag", "1");
            int report_type = getIntent().getIntExtra("psoition", -1) + 1;
            job.put("report_type", report_type + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String method = getIntent().getIntExtra("reportType", -1) == 1 ? "mail/emailSendReport"
                : "mail/emailSendGridReport";
        MainBizImpl.getInstance().onComomRequest(myHandler, this, SEND_SUCESS,
                SEN_FAILE, job, method);
    }

    protected void setSendResult(JSONObject jsonObject) {
        // if(jsonObject.optString("resul_id").equals("0")){
        //
        // }
        Log.i("1111111111", jsonObject.toString());
        tools.putValue(Constants.SEND_EMAILS, emailTx);
        ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
    }

    PopuUnTimeReport dialog;
    String emailTx = "";

    private MyBrodcastReceiver brodcast;

    public void sendEmails() {
        emailTx = tools.getValue(Constants.SEND_EMAILS);
        emailTx = (!AbStrUtil.isEmpty(emailTx)) ? emailTx : tools
                .getValue(Constants.EMAIL);
        dialog = new PopuUnTimeReport(mcontext, this, false, webView, emailTx,
                new OnPopuClick() {
                    @Override
                    public void enter(String text) {
                        emailTx = text;
                        sendEail(emailTx);
                    }

                    @Override
                    public void cancel() {

                    }
                });

    }

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


    ImageView iv1, iv2, iv3, iv4;
    LinearLayout checkPostionLay;

    public void initBottomPointView() {
        checkPostionLay = (LinearLayout) this.findViewById(R.id.checkPostion);
        iv1 = (ImageView) this.findViewById(R.id.iv1);
        iv2 = (ImageView) this.findViewById(R.id.iv2);
        iv3 = (ImageView) this.findViewById(R.id.iv3);
        iv4 = (ImageView) this.findViewById(R.id.iv4);
    }

    public void setBottomPointView(int position) {
        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.GONE);
        switch (position) {

            case 0:

                break;
            case 1:
                iv1.setVisibility(View.VISIBLE);

                break;
            case 2:
                iv2.setVisibility(View.VISIBLE);
                break;
            case 3:
                iv3.setVisibility(View.VISIBLE);
                break;
            case 4:
                iv4.setVisibility(View.VISIBLE);
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

    String starttime = "";
    String endtime = "";
    String timetype = "1";
    String check_type = "";
    String department = "";
    String authent = "";

    String cacheStarttime = "";
    String cacheEndtime = "";
    String cacheTimetype = "1";
    String cacheRgis_job = "";
    String cacheDepartment = "";
    JSONObject job = new JSONObject();
    int type;

    public void onloadJsMethod() {

        try {
            if (repotType == 1) {
                job.put("type", type + "");
            }
            job.put("authent", authent);
            job.put("startTime", starttime);
            job.put("endTime", endtime);
            job.put("check_type", check_type);
            job.put("department", departments);
            job.put("timetype", timetype);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cacheStarttime = starttime;
        cacheEndtime = endtime;
        cacheTimetype = "0";
        cacheRgis_job = check_type;
        cacheDepartment = departments;
        if (repotType == 1) {
            shareUrl = WebUrl.WEB_HAND_REPORT
                    + AbStrUtil.getBase64(job.toString());
            webView.clearCache(true);
//			if (repotType == 1) {
//				progDailog.show();
//			}
            if(null!=loadingView){
            loadingView.startAnimition();
            }
            webView.loadUrl(shareUrl);
        } else {
            shareUrl = WebUrl.LEFT_URL + shareMethod
                    + AbStrUtil.getBase64(job.toString());
            webView.loadUrl("javascript:setInitData(" + job.toString() + ")");
        }

    }

    String shareUrl = "";

    public void onResetParams() {
        Calendar cal = Calendar.getInstance();
        shareMethod = getIntent().getStringExtra("shareMethod");
        final Date localTime = cal.getTime();
        repotType = getIntent().getIntExtra("reportType", -1);
        starttime = dateFormater.format(DateUtil
                .getFirstDayOfMonth(localTime)) + "";
        endtime = dateFormater
                .format(DateUtil.getLastDayOfMonth(localTime)) + "";
        timetype = "1";
     String   regis_job = tools.getValue(Constants.JOB);
        if (regis_job.equals("1")) {

            department = "0";
        } else  {
            department = "";
        }
        if(TaskUtils.isPartTimeJob(mcontext)){
            departments=tools.getValue(Constants.DEFULT_DEPARTID);
        }
        if(TaskUtils.mysticalJob(mcontext)){
            check_type="30";
        }else{
            check_type="0";
        }
        cacheRgis_job = check_type;
        cacheDepartment = department;
        try {
            if (repotType == 1) {
                int type = getIntent().getIntExtra("psoition", -2) + 1;
                job.put("type", type + "");
            }
            job.put("authent", authent);
            job.put("startTime", starttime);
            job.put("endTime", endtime);
            job.put("check_type", check_type);
            job.put("department", departments);
            job.put("timetype", timetype);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cacheStarttime = starttime;
        cacheEndtime = endtime;
        cacheTimetype = "0";
        cacheRgis_job = check_type;
        cacheDepartment = departments;
        if (repotType == 1) {
            url = shareUrl = WebUrl.WEB_HAND_REPORT
                    + AbStrUtil.getBase64(job.toString());
        } else {
            url = getIntent().getStringExtra("url");
            shareUrl = WebUrl.LEFT_URL + shareMethod
                    + AbStrUtil.getBase64(job.toString());
        }

    }

    public void showPopLay(View view) {
        popparent.setVisibility(View.VISIBLE);
        popparent.removeAllViews();
        popparent.addView(view);
    }

}
