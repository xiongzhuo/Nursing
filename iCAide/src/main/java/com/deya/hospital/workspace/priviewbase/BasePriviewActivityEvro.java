package com.deya.hospital.workspace.priviewbase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseAddFileActivity;
import com.deya.hospital.base.img.TipsDialogRigister;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.descover.SurroundFragemtsAdapter;
import com.deya.hospital.supervisor.PartTimeStaffDialog;
import com.deya.hospital.task.Tasker;
import com.deya.hospital.task.uploader.BaseUploader;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.HorizontalListView;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.CaseListVo2;
import com.deya.hospital.vo.IdAndResultsVo2;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.vo.ShareCodeVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.multidrugresistant.RisitantRequestCode;
import com.deya.hospital.workspace.threepips.TabPagerAdapter2;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public abstract class BasePriviewActivityEvro extends BaseAddFileActivity implements View.OnClickListener {
    public TabPagerAdapter2 adapter;
    public CaseListVo2.CaseListBean caseVo;
    public static String IMG_CHANGED = "img_changed";
    public static String TEMPS_CHANGED = "temps_changed";
    public CommonTopView topView;
    public RadioGroup radioGroup;
    public PartTimeStaffDialog tipsDialog;
    public Button sumbmitBtn;
    public ViewPager fragmentPager;
    public SurroundFragemtsAdapter myadapter;
    public List<Fragment> listfragment;
    public RisistantVo rv;
    public HorizontalListView tabGv;
    public Tools tools;
    public Button shareBtn;
    public TipsDialogRigister baseTipsDialog;
    MyBrodcastReceiver receiver;

    public static String IS_ALL_RIGHT = "is_all_right";
    public TaskVo taskVo;
    public RisistantVo.TaskInfoBean.SupervisorQuestionBean questionVo;
    public SupvisorFragment spvisorFragment;
    RisistantVo.TaskInfoBean taskInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risistant_base);
        tools = new Tools(mcontext, Constants.AC);
        initMyHandler();
        initFragmentList();
        init();
        initView();
    }

    public void init() {
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.setTileRightImgVisble(View.VISIBLE);
        sumbmitBtn = (Button) this.findViewById(R.id.sumbmitBtn);
        sumbmitBtn.setOnClickListener(this);
        shareBtn = (Button) this.findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);


        topView.init(this);
        topView.setRigtext("拍照");
        topView.onRightClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecordPopWindow(R.id.mainLay, true, false);

            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                oncheckChange(checkedId);
            }
        });

        fragmentPager = (ViewPager) this.findViewById(R.id.fragmentPager);
        fragmentPager.setSaveEnabled(false);
        fragmentPager.setOffscreenPageLimit(3);
        myadapter = new SurroundFragemtsAdapter(getSupportFragmentManager(),
                listfragment);
        fragmentPager.setAdapter(myadapter);
        tipsDialog = new PartTimeStaffDialog(mcontext, "您还没有填写改进建议，填写完成之后可以直接反馈到临床科室，是否填写？", "直接提交", "去填写",
                new PartTimeStaffDialog.PDialogInter() {
                    @Override
                    public void onEnter() {
                        toAddFeedBack();
                    }

                    @Override
                    public void onCancle() {
                        doSumbmit();
                    }
                });

        baseTipsDialog = new TipsDialogRigister(mcontext, new MyDialogInterface() {
            @Override
            public void onItemSelect(int postion) {

            }

            @Override
            public void onEnter() {
                toAddCache();
            }

            @Override
            public void onCancle() {
                finish();
            }
        });

        receiver = new MyBrodcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                if (intent.getAction().equals(IMG_CHANGED)) {
                    if (intent.hasExtra("fileSize")) {
                        onFileNumChanged(intent.getIntExtra("fileSize", 0));
                    }
                } else if (intent.getAction().equals(TEMPS_CHANGED)) {
                    int temp = intent.getIntExtra("temptype", 0);
                    int state = intent.getIntExtra("state", 0);
                    reSetTemps(temp, state);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(IMG_CHANGED);
        filter.addAction(TEMPS_CHANGED);
        registerReceiver(receiver, filter);
    }

    @Override
    public boolean getdefultState() {
        boolean isCheckAll = true;
        if (null != tools.getValue(IS_ALL_RIGHT)) {
            isCheckAll = tools.getValue(IS_ALL_RIGHT).equals("1");
        }

        return isCheckAll;
    }

    public abstract void reSetTemps(int temp, int state);

    public abstract void onFileNumChanged(int num);

    public abstract void toAddCache();

    public void showBaseTipsDialog() {
        baseTipsDialog.show();
        baseTipsDialog.setContent("是否保存");
        baseTipsDialog.setButton("保存");

    }

    public abstract void oncheckChange(int id);//选项卡切换

    public abstract void initView();

    public abstract void doSumbmit();//提交按钮点击

    public abstract void toAddFeedBack();//去反馈

    public abstract void initFragmentList();//初始化所有Fragment

    /**
     * @param jsonObject 模版详情json
     */
    public abstract void setDataResult(JSONObject jsonObject);

    public void initMyHandler() {
        myHandler = new MyHandler(this) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = myHandler.mactivity.get();
                if (null != activity) {
                    switch (msg.what) {
                        case RisitantRequestCode.GET_DUCHA_SUC:
                            dismissdialog();
                            if (null != msg && null != msg.obj) {
                                try {
                                    setDataResult(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e5) {
                                    e5.printStackTrace();
                                }
                            }
                            break;
                        case RisitantRequestCode.GET_DUCHA_FAIL:
                            dismissdialog();
                            break;

                        case COMPRESS_IMAGE:
                            if (null != msg && null != msg.obj) {
                                File file = new File(msg.obj + "");
                                Log.i("1111", file.exists() + "");
                                AddImgFile(file.toString());
                            }
                            break;
                        case RisitantRequestCode.GET_CASES_SUC:
                            dismissdialog();
                            if (null != msg && null != msg.obj) {
                                try {
                                    setFromData(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e5) {
                                    e5.printStackTrace();
                                }
                            }

                            break;
                        case RisitantRequestCode.SEND_TASK_FAIL:
                            dismissdialog();
                            onsendTaskFail(FormCommitSucTipsActivity.UPLOAD_FIAL);
                            break;
                        case RisitantRequestCode.SEND_FEEDBACK_SUCESS:
                            break;
                        case RisitantRequestCode.SEND_FEEDBACK_FAIL:
                            dismissdialog();
                            break;
                        case RisitantRequestCode.REMOVE_SUPDETAIL_SUCESS:
                            if (null != msg.obj) {
                                try {
                                    onGetSupDetailSucess(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e5) {
                                    e5.printStackTrace();
                                }
                            }

                            break;
                        case RisitantRequestCode.SEB_CASE_SUCESS:

                            break;
                        case RisitantRequestCode.SEB_CASE_FAIL:
                            dismissdialog();
                            break;
                        default:
                            break;
                    }
                }
            }
        };
    }


    public void getQuestionDetail(String questhionId) {
        JSONObject job = new JSONObject();
        try {

            job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
            job.put("id", questhionId);
        } catch (Exception e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler,
                this, RisitantRequestCode.REMOVE_SUPDETAIL_SUCESS, RisitantRequestCode.REMOVE_SUPDETAIL_FAIL, job,
                "supervisorQuestion/questionDetail");
    }


    public abstract void onGetSupDetailSucess(JSONObject jsonObject);


    //提交任务成功回调
    public abstract void setChildJson(JSONObject job, CaseListVo.CaseListBean businessBean);

    private void setFromData(JSONObject jsonObject) {

        getDataSucess(jsonObject);
    }


    public abstract void getDataSucess(JSONObject jsonObject);

    public void getDetailData(String taskId) {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("task_id", taskId);
//           job.put("hospital_id","");
//            job.put("department_id","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, this, RisitantRequestCode.GET_CASES_SUC,
                RisitantRequestCode.GET_CASES_FAIL, job, "comm/temp/findTask");
    }

    @Override
    protected void onDestroy() {
        if (tipsDialog != null && tipsDialog.isShowing()) {
            tipsDialog.dismiss();
        }
        if (null != baseTipsDialog && baseTipsDialog.isShowing()) {
            baseTipsDialog.dismiss();
        }
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void showShare(TaskVo taskVo, String checkContent) {
        String type = taskVo.getType();
        try {
            ShareCodeVo shareCodeVo = DataBaseHelper
                    .getDbUtilsInstance(mcontext)
                    .findFirst(
                            Selector.from(ShareCodeVo.class).where("data_id", "=", type));

            if (shareCodeVo.getPdata_id() == 1) {

                SHARE_MEDIA[] shareMedia = {SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                        SHARE_MEDIA.WEIXIN_CIRCLE};
                showShareDialog(getShareTitle(type), taskVo.getDepartmentName() + ":" + checkContent,getShareUrl(taskVo.getTask_id() + "", shareCodeVo.getData_name()));
            } else {
                ToastUtils.showToast(mcontext, "抱歉！分享页面正在加紧开发中，请稍等几天！");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    //180810877
    public String getShareTitle(String type) {
        int i = Integer.parseInt(type);
        switch (i) {
            case 8:
                return "多耐";
            case 9:
                return "三管";
            case 10:
                return "环境物表清洁";
            case 12:
                return "手术部位感染";
            case 11:
                return "安全注射";
            default:
                return "";

        }
    }

    public String getShareUrl(String id, String methodUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return WebUrl.LEFT_URL + "/" + methodUrl + "?u=" + AbStrUtil.getBase64(jsonObject.toString());

    }

    /**
     * @param taskVo       任务对象
     * @param businessBean 病例对象
     * @param supervisor   督查对象
     */
    public void comomsubmit(final TaskVo taskVo, CaseListVo2.CaseListBean businessBean, RisistantVo.TaskInfoBean.SupervisorQuestionBean supervisor) {
        showprocessdialog();
        setTask();
        taskVo.setMain_remark(TaskUtils.gson.toJson(supervisor));
        taskVo.setTaskListBean(TaskUtils.gson.toJson(caseVo));
        if (NetWorkUtils.isConnect(mcontext)) {
            if (!NetWorkUtils.isWifiState(MyAppliaction.getContext())) {
                onsendTaskFail(FormCommitSucTipsActivity.ONLY_WIFI);
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (BaseUploader.getUploader(8).Upload(taskVo)) {
                            onSendSucess();

                        } else {
                            onsendTaskFail(FormCommitSucTipsActivity.UPLOAD_FIAL);
                        }

                    }
                }).start();

            }

        } else {
            onsendTaskFail(FormCommitSucTipsActivity.NET_WORK_DISCONECT);
            return;
        }

    }

    /**
     * 上传附件
     */


    public void starUpload() {
        if (AbStrUtil.isEmpty(taskVo.getDepartment())) {
            taskVo.setDepartment(caseVo.getDepartment_id());
            taskVo.setDepartmentName(caseVo.getDepartmentName());
            taskVo.setMobile(tools.getValue(Constants.MOBILE));
        }
        comomsubmit(taskVo, caseVo, questionVo);
    }

    /**
     * 组装模板数据以便上传
     */
    public List<IdAndResultsVo2> resultlist = new ArrayList<>();

    public void setTask() {
        resultlist.clear();
        showprocessdialog();
        if (rv == null) {
            return;
        }
        for (RisistantVo.TempListBean rt : rv.getTemp_list()) {
            if (rt == null) {
                continue;
            }
            IdAndResultsVo2 idAndResultsVo = new IdAndResultsVo2();
            idAndResultsVo.setResult(new ArrayList<IdAndResultsVo2.ItemBean>());
            idAndResultsVo.setTemp_id(rt.getId() + "");
            for (RisistantVo.TempListBean.ItemListBean rti : rt.getItem_list()) {

                switch (rti.getType()) {
                    case 1:
                        break;
                    case 2:
                        IdAndResultsVo2.ItemBean resultItems = new IdAndResultsVo2.ItemBean();
                        resultItems.setResult(rti.getChildren().get(0).getResult());
                        resultItems.setSelect_id(rti.getChildren().get(0).getId() + "");
                        idAndResultsVo.getResult().add(resultItems);
                        break;
                    case 3:
                        IdAndResultsVo2.ItemBean itemBean2 = new IdAndResultsVo2.ItemBean();
                        itemBean2.setResult(rti.getChildren().get(0).getResult());
                        itemBean2.setSelect_id((rti.getChildren().get(0).getId() + ""));
                        idAndResultsVo.getResult().add(itemBean2);
                        break;
                    case 4:
                        IdAndResultsVo2.ItemBean itemBean3 = new IdAndResultsVo2.ItemBean();
                        itemBean3.setResult(rti.getChildren().get(0).getResult());
                        itemBean3.setSelect_id(rti.getChildren().get(0).getId() + "");
                        IdAndResultsVo2.ItemBean itemBean4 = new IdAndResultsVo2.ItemBean();
                        itemBean4.setResult(rti.getChildren().get(1).getResult());//积分
                        itemBean4.setSelect_id(rti.getChildren().get(1).getId() + "");
                        idAndResultsVo.getResult().add(itemBean3);
                        idAndResultsVo.getResult().add(itemBean4);
                        break;
                    case 5:
                    case 6:
                        for (RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean : rti.getChildren()) {
                            IdAndResultsVo2.ItemBean itemBean5 = new IdAndResultsVo2.ItemBean();
                            itemBean5.setSelect_id(childrenBean.getId() + "");
                            if (AbStrUtil.isEmpty(childrenBean.getResult())) {
                                itemBean5.setResult("0");
                            } else {
                                itemBean5.setResult(childrenBean.getResult());
                            }
                            idAndResultsVo.getResult().add(itemBean5);
                        }
                        break;


                }


            }
            resultlist.add(idAndResultsVo);
            caseVo.setResultlist(resultlist);
            if(!taskVo.getTask_type().equals("9")){
                break;
            }

        }


    }

    /**
     * 缓存到本地
     */
    public void onSendSucess() {
        dismissdialog();
        caseVo.setTask_id(taskVo.getTask_id());
        caseVo.setStatus(1);
        taskVo.setStatus(0);
        setCache();//缓存到本地
        Intent it = new Intent(mcontext, FormCommitSucTipsActivity.class);
        it.putExtra("id", caseVo.getCase_id() + "");
        it.putExtra("data", taskVo);
        it.putExtra("type_id", "");
        it.putExtra("code", "");
        it.putExtra("commit_status", 0);

        startActivity(it);
        finish();


    }


    /**
     * 提交失败更新数据
     */
    private void onsendTaskFail(int state) {
        if (caseVo.getDbid() != 0) {
            CaseUtil.updateCaseInDb(caseVo);
        }
        taskVo.setStatus(1);
        setCache();
        Intent it = new Intent(mcontext, FormCommitSucTipsActivity.class);
        it.putExtra("id", caseVo.getCase_id() + "");
        it.putExtra("data", taskVo);
        it.putExtra("type_id", "");
        it.putExtra("code", "");
        it.putExtra("commit_status", state);

        startActivity(it);
        finish();
    }


    private void setCache() {
        taskVo.setDepartment(caseVo.getDepartment_id());
        taskVo.setDepartmentName(caseVo.getDepartmentName());
        taskVo.setHospital(tools.getValue(Constants.HOSPITAL_ID));
        taskVo.setMobile(tools.getValue(Constants.MOBILE));
        caseVo.setSupervisor_question(spvisorFragment.getMainRemark());
        caseVo.setRisistantVo(rv);
        caseVo.setUploadStatus(3);
        taskVo.setTaskListBean(TaskUtils.gson.toJson(caseVo));
        if (AbStrUtil.isEmpty(taskVo.getMission_time())) {
            if (!AbStrUtil.isEmpty(caseVo.getTime())) {
                taskVo.setMission_time(caseVo.getTime());
            } else {
                taskVo.setMission_time(TaskUtils.getLoacalDate());
            }
        }
        taskVo.setCaseDbId(caseVo.getDbid());//在任务表里面挂的caseid //以便通过case表查询任务
        if (taskVo.getDbid() <= 0) {
            TaskUtils.onAddTaskInDb(taskVo);
        } else {
            TaskUtils.onUpdateTaskById(taskVo);
        }
        List<TaskVo> tasks = Tasker.getAllDbTask();
        taskVo = Tasker.getAllDbTask().get(tasks.size() - 1);
    }
}
