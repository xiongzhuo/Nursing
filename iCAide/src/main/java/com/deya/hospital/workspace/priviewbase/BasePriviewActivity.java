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
import android.view.KeyEvent;
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
import com.deya.hospital.task.uploader.CaseFormUpload;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.GsonUtils;
import com.deya.hospital.util.HorizontalListView;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.ClassifyVo;
import com.deya.hospital.vo.IdAndResultsVo;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.vo.ShareCodeVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.multidrugresistant.RisitantRequestCode;
import com.deya.hospital.workspace.threepips.TabPagerAdapter;
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
public abstract class BasePriviewActivity extends BaseAddFileActivity implements View.OnClickListener {
    private static final int ADD_HOS_FORM =0x1770 ;
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
    public TabPagerAdapter adapter;
    MyBrodcastReceiver receiver;
    public static String IS_ALL_RIGHT = "is_all_right";
    public CaseListVo.CaseListBean caseVo;
    public TaskVo taskVo;
    public RisistantVo.TaskInfoBean.SupervisorQuestionBean questionVo;
    public SupvisorFragment spvisorFragment;
    RisistantVo.TaskInfoBean taskInfoBean;
    public boolean isPriview;//判断是否通过平台模板预览
    public static boolean isDetail;//任务已提交后的预览

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
    protected abstract String getTopTitle();
    public void init() {
        isPriview=getIntent().getBooleanExtra("isPriview",false);
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.setTileRightImgVisble(View.VISIBLE);
        if (!AbStrUtil.isEmpty(getTopTitle())) {
            topView.setTitle(getTopTitle());
        }
        sumbmitBtn = (Button) this.findViewById(R.id.sumbmitBtn);
        sumbmitBtn.setOnClickListener(this);
        topView.init(this);
        topView.setRigtext("拍照");
        shareBtn = (Button) this.findViewById(R.id.shareBtn);
        if(isPriview){
            shareBtn.setVisibility(View.GONE);
            topView.setRigtext("");
            topView.setTitle("预览");
            sumbmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFormToHospital(taskInfoBean.getTempId()+"");

                }
            });

        }else {
            topView.onbackClick(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (taskVo.getTask_id() > 0||taskVo.getDbid()>0) {
                        toAddCache();
                        finish();
                    }else{
                        showBaseTipsDialog();
                    }

                }
            });
        }

        shareBtn.setOnClickListener(this);



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
        myadapter = new SurroundFragemtsAdapter(getSupportFragmentManager(),
                listfragment);
        fragmentPager.setAdapter(myadapter);
        tipsDialog = new PartTimeStaffDialog(mcontext, "您本次巡查任务是否完成？", "是,直接提交", "否,暂存",
                new PartTimeStaffDialog.PDialogInter() {
                    @Override
                    public void onEnter() {
                        toAddCache();

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
        baseTipsDialog.setContent("是否保存到\n未完成/已完成里面?");
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
                        case  ADD_HOS_FORM:
                            dismissdialog();
                            finish();
                            break;
                        default:
                            break;
                    }
                }
            }
        };
    }





    public abstract void onGetSupDetailSucess(JSONObject jsonObject);


    //提交任务成功回调
    public abstract void setChildJson(JSONObject job, CaseListVo.CaseListBean businessBean);

    private void setFromData(JSONObject jsonObject) {

        getDataSucess(jsonObject);
    }


    public void addFormToHospital(String id) {
        showprocessdialog();
        try {
            JSONObject job = GsonUtils.creatJsonObj("").put("authent", tools
                    .getValue(Constants.AUTHENT)).put("tempId", id);
            MainBizImpl.getInstance().onComomRequest(myHandler, this, ADD_HOS_FORM,
                    RisitantRequestCode.GET_CASES_FAIL, job, "quality/saveToHospital");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public abstract void getDataSucess(JSONObject jsonObject);

    public void getDetailData(String tempId) {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("task_id", tempId);
//           job.put("hospital_id","");
//            job.put("department_id","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, this, RisitantRequestCode.GET_CASES_SUC,
                RisitantRequestCode.GET_CASES_FAIL, job, "quality/tempDetail");
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
    public void comomsubmit(final TaskVo taskVo, CaseListVo.CaseListBean businessBean, RisistantVo.TaskInfoBean.SupervisorQuestionBean supervisor) {
        TaskUtils.onCommitAfterTask(taskVo);
        showprocessdialog();
        setTask();
        taskInfoBean.setBaseInfo(businessBean);
        taskInfoBean.setSupervisor_question(supervisor);
        taskVo.setMain_remark(TaskUtils.gson.toJson(supervisor));
        taskVo.setTaskListBean(TaskUtils.gson.toJson(taskInfoBean));
        taskVo.setIs_temp_task(1);
        if (NetWorkUtils.isConnect(mcontext)) {
            if (!NetWorkUtils.isWifiState(MyAppliaction.getContext())) {
                onsendTaskFail(FormCommitSucTipsActivity.ONLY_WIFI);
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CaseFormUpload.getUploader(17).Upload(taskVo)) {
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
     *   选项类型
     //    1）文字标签
     //    2）判断
     //    3）评分
     //    4）判断和评分
     //    5）单选
     //    6）多选
     */
    public List<IdAndResultsVo.ItemBean> resultlist = new ArrayList<>();



    ClassifyVo classifyVo=new ClassifyVo();
    public void setTask() {
        resultlist.clear();
        showprocessdialog();
        if (rv == null) {
            return;
        }

            int wrongNum=0;
            int rightNum = 0;
            int toalScore = 0;
            int undonum=0;
        RisistantVo.TaskInfoBean.TempListBean rt=taskInfoBean.getTemp_list();
            for (RisistantVo.TempListBean.ItemListBean rti : rt.getItem_list()) {
                double score=Double.parseDouble(rti.getScore());
                toalScore+=score;
                switch (rti.getType()) {
                    case 1:
                        break;
                    case 2:
                        IdAndResultsVo.ItemBean resultItems = new IdAndResultsVo.ItemBean();
                        resultItems.setResult(rti.getResult());
                        resultItems.setItem_id(rti.getId() + "");
                        resultItems.setItem_repo_id(rti.getTem_repo_id()+"");

                            resultItems.setItem_score(rti.getItem_score());
                        if(!AbStrUtil.isEmpty(rti.getSave_time())){
                            resultlist.add(resultItems);
                        }

                        break;
                    case 3:
                        IdAndResultsVo.ItemBean itemBean2 = new IdAndResultsVo.ItemBean();
                        itemBean2.setItem_score(rti.getItem_score());
                        itemBean2.setItem_id((rti.getId() + ""));
                        itemBean2.setItem_repo_id(rti.getTem_repo_id()+"");
                        if(!AbStrUtil.isEmpty(rti.getSave_time())){
                            resultlist.add(itemBean2);
                        }

                        break;
                    case 4:
                        IdAndResultsVo.ItemBean itemBean3 = new IdAndResultsVo.ItemBean();
                        itemBean3.setResult(rti.getResult());
                        itemBean3.setItem_id(rti.getId() + "");
                        itemBean3.setItem_repo_id(rti.getTem_repo_id()+"");
                        itemBean3.setItem_score(rti.getItem_score());
                        itemBean3.setItem_id(rti.getId() + "");
                        if(!AbStrUtil.isEmpty(rti.getSave_time())){
                            resultlist.add(itemBean3);
                        }
                        break;
                    case 5:
                    case 7:
                        IdAndResultsVo.ItemBean itemBean9 = new IdAndResultsVo.ItemBean();
                        for (RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean : rti.getChildren()) {

                            if (!AbStrUtil.isEmpty(childrenBean.getResult())&&childrenBean.getResult().equals("1")) {
                                itemBean9.setItem_id(childrenBean.getId()+"");

                            }

                        }
                        itemBean9.setItem_score(rti.getItem_score());
                        itemBean9.setItem_repo_id(rti.getTem_repo_id()+"");
                        itemBean9.setItem_id(rti.getId()+"");
                        if(!AbStrUtil.isEmpty(rti.getSave_time())){
                            resultlist.add(itemBean9);
                        }
                        break;
                    case 6:
                        IdAndResultsVo.ItemBean itemBean5 = new IdAndResultsVo.ItemBean();
                        for (RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean : rti.getChildren()) {

                            if (!AbStrUtil.isEmpty(childrenBean.getResult())&&childrenBean.getResult().equals("1")) {
                                itemBean5.setItem_id(childrenBean.getId()+",");

                            }
                        }
                        itemBean5.setItem_score(rti.getItem_score());
                        if(!itemBean5.getItem_score().equals("0")){

                            itemBean5.setItem_score((0-score)+"");
                        }
                        itemBean5.setItem_id(rti.getId()+"");
                        itemBean5.setItem_repo_id(rti.getTem_repo_id()+"");
                        if(!AbStrUtil.isEmpty(rti.getSave_time())){
                            resultlist.add(itemBean5);
                        }
                        break;
                    case 8:
                        if(rti.getChildren().size()==0||rti.getIsSaved()==0){
                            break;
                        }
                        double itemScore = 0;
                        for(RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean:rti.getChildren()){
                            if(childrenBean.getResult().equals("2")){
                                if(!AbStrUtil.isEmpty(childrenBean.getScore())){
                                    double childScore=Double.parseDouble(childrenBean.getScore());
                                    childrenBean.setGet_score((0-childScore)+"");
                                    itemScore+=0-childScore;
                                }
                            }
                        }
                        for(RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean:rti.getChildren()){
                            if(childrenBean.getResult().equals("2")){
                                if(!AbStrUtil.isEmpty(childrenBean.getScore())){
                                    double childScore=Double.parseDouble(childrenBean.getScore());
                                    childrenBean.setGet_score((0-childScore)+"");
                                }
                            }else{
                                childrenBean.setGet_score(0+"");
                            }
                            IdAndResultsVo.ItemBean itemBean6 = new IdAndResultsVo.ItemBean();
                            itemBean6.setResult(childrenBean.getResult());
                            itemBean6.setSelect_id(childrenBean.getId() + "");
                            itemBean6.setItem_id(rti.getId()+"");
                            itemBean6.setItem_score(itemScore+"");
                            itemBean6.setSave_time(childrenBean.getSave_time());
                            itemBean6.setGet_score(childrenBean.getGet_score());
                            resultlist.add(itemBean6);
                        }


//                        if(rti.getChildren().get(0).getResult().equals("1")){
//                            rightNum++;
//
//                        }else if(rti.getChildren().get(0).getResult().equals("2")){
//                            wrongNum++;
//                        }else{
//                            undonum++;
//                        }
                        break;


                }


            }
            caseVo.setResultlist(resultlist);

        for(IdAndResultsVo.ItemBean itemBean:resultlist){
            if(!AbStrUtil.isEmpty(itemBean.getItem_score())){
            Double itemS=Double.parseDouble(itemBean.getItem_score());
            classifyVo.setGetScore( classifyVo.getGetScore()+ itemS);
            }
        }
            classifyVo.setRightNum(rightNum);
            classifyVo.setWrongNum(wrongNum);
            classifyVo.setUndonum(undonum);
            classifyVo.setToalScore(toalScore);
            classifyVo.setFormName(rt.getTitle());
        int subScore= (int) (classifyVo.getToalScore()+classifyVo.getGetScore());

        classifyVo.setSumbScore(subScore);


            Log.i("1111111111",rightNum+"====="+wrongNum+"-----"+undonum+"-----"+toalScore);




    }

    /**
     * 缓存到本地
     */
    public void onSendSucess() {
        dismissdialog();
        caseVo.setTask_id(taskVo.getTask_id());
        caseVo.setStatus(1);
        taskVo.setStatus(0);
     //   setCache();//缓存到本地
        Intent it = new Intent(mcontext, FormCommitSucTipsActivity.class);
        it.putExtra("id", caseVo.getCase_id() + "");
        it.putExtra("data", taskVo);
        it.putExtra("type_id", "");
        it.putExtra("code", "");
        it.putExtra("commit_status", 0);
        it.putExtra("classifyVo",  classifyVo);

        startActivity(it);
        finish();


    }


    /**
     * 提交失败更新数据
     */
    private void onsendTaskFail(int state) {
//        if (caseVo.getDbid() != 0) {
//            CaseUtil.updateCaseInDb(caseVo);
//        }
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
        taskVo.setIs_temp_task(1);
        taskVo.setHospital(tools.getValue(Constants.HOSPITAL_ID));
        taskVo.setMobile(tools.getValue(Constants.MOBILE));
        taskVo.setTaskListBean(TaskUtils.gson.toJson(taskInfoBean));
        if (AbStrUtil.isEmpty(taskVo.getMission_time())) {
            if (!AbStrUtil.isEmpty(caseVo.getTime())) {
                taskVo.setMission_time(caseVo.getTime());
            } else {
                taskVo.setMission_time(TaskUtils.getLoacalDate());
            }
        }
        if (taskVo.getDbid() <= 0) {
            TaskUtils.onAddTaskInDb(taskVo);
            List<TaskVo> tasks = Tasker.getAllDbTask();
            taskVo = Tasker.getAllDbTask().get(tasks.size() - 1);
        } else {
            TaskUtils.onUpdateTaskById(taskVo);
        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isPriview&&keyCode == KeyEvent.KEYCODE_BACK) {
            if (taskVo.getTask_id() > 0||taskVo.getDbid()>0) {
                return super.onKeyDown(keyCode, event);
            }else{
                showBaseTipsDialog();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
