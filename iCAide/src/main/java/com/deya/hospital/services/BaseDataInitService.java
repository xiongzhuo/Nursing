package com.deya.hospital.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.deya.hospital.account.UserUtis;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseDataInitReciever;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.ClassifyExciseVo;
import com.deya.hospital.vo.DepartmentVo;
import com.deya.hospital.vo.FormCodeVo;
import com.deya.hospital.vo.FormVo;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.vo.ShareCodeVo;
import com.deya.hospital.vo.TaskTypePopVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.workspacemain.TodayDynamicFragment;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class BaseDataInitService extends Service implements RequestInterface {



    private String TAG = "BaseDataInitService";
    public static final String INTENTCODE = "intentCode";
    public static final int REFRESH_ALL = 0x7000;
    public static final int LINGCHUAN_FROM = 0x7001; //临床考核模版
    public static final int GET_FORM_CODE_SUC = 0x7002;//表格对应的CODE
    public static final int GET_SEARCH_LABELS = 0x7003;
    public static final int GET_CIRCLE_CHANEL = 0x7004;//工作圈的Tab
    public static final int GET_JOB = 0x7005;//获取职业
    public static final int GET_DEPARTMENT = 0x7006;//获取科室
    public static final int GET_HAND_WASH_RULE = 0x7007;//不规则选项
    public static final int GET_USER_INFO = 0x7008;//获取用户信息
    public static final int GETMODULE = 0x7009;//获取用户所有可选择任务菜单
    //填写表格相关
    public static final int GET_ENVIROMENT_FORM = 0x7010;//获取环境物表清洁表格
    public static final int GET_SITE_INFECTION_FORM = 0x7015;//获取手术部位感染表格
    public static final int GET_SAFE_INJECTION_FORM = 0x7016;//获取安全注射表格
    private static final int GET_SHARE_CODE = 0x7012;//获取表格分享链接
    private static final int GET_RISITANT_CODE = 0x7013;//获取多耐表格分享链接
    private static final int GET_SG_CODE = 0x7014;//获取三管表格分享链接
    private static final int GET_MUTITEXT_SUC = 0x7017;//多耐病例选择项
    private static final int GET_DRUG_MUTITEXT_SUC = 0x7018;
    private static final int GET_WAST_FORM_SUC = 0x7019;
    private static final int GET_OTHER_FORM_SUC = 0x7020;//其他模板
    private static final int GET_KEY_INFECTION_FORM_SUC = 0x7025;//临床考核模板

    //学习模块
    private static final int GET_KNOWLEDGE_CLASSIFY = 0x7021;//学习模块分类

    //环境物表模板新增的三个字段 数据获取
    private static final int GET_EV_QUARANTIN = 0x7022;//是否分离
    private static final int GET_EV_CLEARTIMES = 0x7023;//清洁时机
    private static final int GET_EV_MONITOR= 0x7024;//监测方法
    private static final int GET_FEEDBACK_CODE =0x7026 ;
    private static final int GET_NEW_DEPARTMENT =0x7027 ;
    private static final int GET_DUDAO_TYPES = 0x7028;
    Context mcontext;
    Tools tools;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        receiveIntent(intent);
        return super.onStartCommand(intent, flags, startId);

    }

    private void receiveIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        int code = intent.getIntExtra(INTENTCODE, 0);
        if (code == 0) {
            return;
        }
        switch (code) {
            case REFRESH_ALL:
                initData();
                break;
            case GET_DEPARTMENT:
                getDepartMentList();
                break;

            case GET_USER_INFO:
                getUserInfo();
                break;
            case GETMODULE:
                getModule();
                break;
            default:
                break;

        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mcontext = this;
        tools = new Tools(MyAppliaction.getContext(), Constants.AC);
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        Log.i(TAG + ">>", jsonObject.toString());
        switch (code) {
            case GET_DEPARTMENT:
                setDepartMentList(jsonObject);
                break;
            case GET_NEW_DEPARTMENT:
                setNewDepartMentList(jsonObject);
                break;
            case LINGCHUAN_FROM:
                Log.i(TAG + "LINGCHUAN_FROM>>", jsonObject.toString());
                setFormList(jsonObject);
                break;
            case GET_FORM_CODE_SUC:
                setFormCodeData(jsonObject);
                break;
            case GET_SEARCH_LABELS:
                setHotData(jsonObject);
                break;
            case GET_CIRCLE_CHANEL:
                setInfoRes(jsonObject);
                break;
            case GET_JOB:
                setJobList(jsonObject);
                break;
            case GET_HAND_WASH_RULE:
                saveWrongRuls(jsonObject);
                break;
            case GETMODULE:
                setUserMudule(jsonObject);
                break;
            case GET_USER_INFO:
                JSONObject job = jsonObject.optJSONObject("user");
                if (null == job) {
                    return;
                }
                UserUtis.setEditorRes(tools, job);
                sendSucessBrodCast(BaseDataInitReciever.GET_USER_INFO_SC, jsonObject.toString());
                break;
            case GET_KEY_INFECTION_FORM_SUC:
                saveCaseFormCache(jsonObject, "16");
                break;
            case GET_ENVIROMENT_FORM:
                saveCaseFormCache(jsonObject, "10");
                break;
            case GET_RISITANT_CODE:
                saveCaseFormCache(jsonObject, "8");
                break;
            case GET_SG_CODE:
                saveCaseFormCache(jsonObject, "9");
                break;
            case GET_SITE_INFECTION_FORM:
                saveCaseFormCache(jsonObject, "12");
                break;
            case GET_SAFE_INJECTION_FORM:
                saveCaseFormCache(jsonObject, "11");
                break;
            case GET_SHARE_CODE:
                setShareCode(jsonObject);
                break;
            case GET_MUTITEXT_SUC:
                setMutiTextResult(jsonObject);
                break;
            case GET_DRUG_MUTITEXT_SUC:
                setDrugMutiTextResult(jsonObject);
                break;
            case GET_WAST_FORM_SUC:
                saveCaseFormCache(jsonObject, "13");
                break;
            case GET_OTHER_FORM_SUC:
                saveCaseFormCache(jsonObject, "14");
                break;
            case GET_KNOWLEDGE_CLASSIFY:
                saveCaseFormCache(jsonObject);
                break;
            case GET_EV_QUARANTIN:
                SharedPreferencesUtil.saveString(mcontext, "get_ev_quarantin", jsonObject.toString());
                break;
            case GET_EV_CLEARTIMES:
                SharedPreferencesUtil.saveString(mcontext, "get_ev_cleartimes", jsonObject.toString());
                break;
            case GET_EV_MONITOR:
                SharedPreferencesUtil.saveString(mcontext, "get_ev_monitor", jsonObject.toString());
                break;
            case GET_FEEDBACK_CODE:
                tools.putValue("equipExamineParams", jsonObject.toString());
                break;
            case GET_DUDAO_TYPES:
                SharedPreferencesUtil.saveString(mcontext, "get_dudao_types", jsonObject.toString());
                break;
        }


    }

    private void setNewDepartMentList(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            SharedPreferencesUtil.saveString(mcontext, "new_depart_data", jsonObject.toString());
        }
    }
    private void saveCaseFormCache(JSONObject jsonObject) {
        if (jsonObject.has("result_id") && jsonObject.opt("result_id").equals("0")) {
            ClassifyExciseVo classifyExciseVo = TaskUtils.gson.fromJson(jsonObject.toString(), ClassifyExciseVo.class);

            if (null != classifyExciseVo && classifyExciseVo.getList().size() > 0) {

                try {
                    DataBaseHelper
                            .getDbUtilsInstance(mcontext)
                            .deleteAll(ClassifyExciseVo.ListBean.class);
                    DataBaseHelper
                            .getDbUtilsInstance(mcontext)
                            .saveAll(classifyExciseVo.getList());
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setDrugMutiTextResult(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            SharedPreferencesUtil.saveString(mcontext, "resistant_mutidrugtext", jsonObject.toString());
        }
    }

    private void setMutiTextResult(JSONObject jsonObject) {

        if (jsonObject.optString("result_id").equals("0")) {
            SharedPreferencesUtil.saveString(mcontext, "resistant_mutitext", jsonObject.toString());
        }

    }

    private void saveCaseFormCache(JSONObject jsonObject, String type) {
        Log.i("caseForm", type + "==========" + jsonObject);

        SharedPreferencesUtil.saveString(mcontext, "comonform" + type, jsonObject.toString());

    }

    private void setShareCode(JSONObject jsonObject) {
        try {
            if (!jsonObject.has("resultList")) {
                return;
            }
            List<ShareCodeVo> list = TaskUtils.gson.fromJson(jsonObject.optJSONArray("resultList").toString(),
                    new TypeToken<List<ShareCodeVo>>() {
                    }.getType());
            DataBaseHelper.getDbUtilsInstance(mcontext)
                    .deleteAll(ShareCodeVo.class);
            DataBaseHelper.getDbUtilsInstance(mcontext)
                    .saveAll(list);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void setUserMudule(JSONObject jsonObject) {
        try {
            List<TaskTypePopVo> list = TaskUtils.gson.fromJson(jsonObject.optJSONArray("list").toString(),
                    new TypeToken<List<TaskTypePopVo>>() {
                    }.getType());
            DataBaseHelper.getDbUtilsInstance(mcontext)
                    .deleteAll(TaskTypePopVo.class);
            DataBaseHelper.getDbUtilsInstance(mcontext)
                    .saveAll(list);
            sendSucessBrodCast(TodayDynamicFragment.GETMODULE_SUC, jsonObject.toString());
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    private void sendSucessBrodCast(String action, String json) {
        Intent intent = new Intent(action);
        intent.putExtra("data", json);
        sendBroadcast(intent);
    }

    @Override
    public void onRequestErro(String message) {
        Log.i("initData",message);
    }

    @Override
    public void onRequestFail(int code) {
        Log.i("initData", code+"");
    }

    private void initData() {
        Log.i("initData", "-----------------");
        getDepartMentList();
        getNewDepartMentList();
        getModule();
        getHandFeeddata();
      //  initFormData();
        initFormCode();
        getHotData();
        //  getChanelData();
        getJobList();
        getWrongRules();
        getMutiTextData("SUPERVISOR_QUESTION_ORIGIN_LIST", GET_DUDAO_TYPES);
        initShareCode();
        getCaseForm("1",GET_KEY_INFECTION_FORM_SUC);
        getCaseForm("2", GET_RISITANT_CODE);
        getCaseForm("3,4,5", GET_SG_CODE);
        getCaseForm("6", GET_ENVIROMENT_FORM);
        getCaseForm("7", GET_SITE_INFECTION_FORM);
        getCaseForm("8", GET_SAFE_INJECTION_FORM);
        getCaseForm("10", GET_WAST_FORM_SUC);
        getCaseForm("11", GET_OTHER_FORM_SUC);
        getMutiTextData("CHECK_SPECIMEN", GET_MUTITEXT_SUC);
        getMutiTextData("DRUG_RESISTANCE", GET_DRUG_MUTITEXT_SUC);
        getKnowledgeClassify();
        getMutiTextData("ENVIRONMENT_IS_QUARANTINE", GET_EV_QUARANTIN);
        getMutiTextData("ENVIRONMENT_CLEANING_TIME", GET_EV_CLEARTIMES);
        getMutiTextData("ENVIRONMENT_MONITORING_METHOD", GET_EV_MONITOR);
    }

    private void getKnowledgeClassify() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("hospitalId", tools.getValue(Constants.HOSPITAL_ID));
            job.put("subjectOwner","public");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext, GET_KNOWLEDGE_CLASSIFY, job, "subject/subjectCategory");
    }
     /*
    * 新获取科室
    */
    private void getNewDepartMentList() {
        JSONObject job = new JSONObject();
        try {
            job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance()
                .onComomReq(this, mcontext,
                        GET_NEW_DEPARTMENT, job,
                        "department/getDepartment");
    }
    public void getUserInfo() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            Log.i("1111", job.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext,
                GET_USER_INFO, job, "user/getUserInfo");
    }

    private void initFormData() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("type", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("111111111111", WebUrl.GET_FORM_LIST);
        MainBizImpl.getInstance().onComomReq(this, mcontext,
                LINGCHUAN_FROM, job, "grid/queryAllTemplateInfos");

    }

    /**
     *
     * @param
     */
    private void getHandFeeddata() {
        // TODO Auto-generated method stub
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
            MainBizImpl.getInstance().onComomReq(this, mcontext,
                GET_FEEDBACK_CODE, job,  WebUrl.URL_PATH_QUERYREMARKPARAMS);
    }
    /**
     * 获取用户功能模块
     */
    private void getModule() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("type", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext,
                GETMODULE, job, "user/getModule");

    }

    private void initFormCode() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("type", "TASK_TEMP_TYPE");
            job.put("timeLong", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("111111111111", WebUrl.GET_FORM_LIST);
        MainBizImpl.getInstance().onComomReq(this, mcontext,
                GET_FORM_CODE_SUC, job, "comm/queryStaticByCode");

    }

    private void initShareCode() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("type", "SHARE_URL_LIST");
            job.put("timeLong", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("111111111111", WebUrl.GET_FORM_LIST);
        MainBizImpl.getInstance().onComomReq(this, mcontext,
                GET_SHARE_CODE, job, "comm/queryStaticByCode");

    }

    //获取搜索缓存
    private void getHotData() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomCirclModReq(this, mcontext,
                GET_SEARCH_LABELS, job, "workCircle/getLabels");

    }

    // 获取职业
    private void getJobList() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
                    .parseStrToMd5L32(job.toString()) + "comm/jobInfosByType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext,
                GET_JOB, job, "comm/jobInfosByType");

    }

    /**
     * 获取科室
     */
    private void getDepartMentList() {
        JSONObject job = new JSONObject();
        try {
            job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance()
                .onComomReq(this, mcontext,
                        GET_DEPARTMENT, job,
                        "comm/departmentInfo");

    }

    // 获取手卫生，不规则操作选择项
    public void getWrongRules() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomReq(this, mcontext,
                GET_HAND_WASH_RULE, job, "task/unrulecauselist");

    }


    /**
     * ------------------------------------------------------------------------------------------------------------------------
     * 数据处理
     * -----------------------------------------------------------------------------------------------------------------------
     */
    protected void setFormList(JSONObject jsonObject) {
        Log.i("formlist", jsonObject.toString());
        JSONArray jarr = jsonObject.optJSONArray("grid_templates");
        if (null == jarr) {
            initFormData();
            return;
        }
        List<FormVo> formlist = null;
        try {
            formlist = TaskUtils.gson.fromJson(jarr.toString(),
                    new TypeToken<List<FormVo>>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();

        }

        if (null == formlist) {
            return;
        }
        for (FormVo fv : formlist) { // 可根据数据库语句查询
            String str = TaskUtils.gson.toJson(fv.getItem());
            fv.setCacheItems(str);
            Log.i("formlist", str.length() + "");
        }
        try {
            if (null != DataBaseHelper
                    .getDbUtilsInstance(mcontext).findAll(FormVo.class)
                    && DataBaseHelper
                    .getDbUtilsInstance(mcontext).findAll(FormVo.class)
                    .size() > 0) {
                DataBaseHelper.getDbUtilsInstance(mcontext)
                        .deleteAll(FormVo.class);
                DataBaseHelper.getDbUtilsInstance(mcontext)
                        .saveAll(formlist);
            } else {
                DataBaseHelper.getDbUtilsInstance(mcontext)
                        .saveAll(formlist);
            }

        } catch (DbException e) {
            e.printStackTrace();

        }
    }

    private void setFormCodeData(JSONObject jsonObject) {
        List<FormCodeVo> list = TaskUtils.gson.fromJson(jsonObject.optString("resultList"), new TypeToken<List<FormCodeVo>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            try {
                DataBaseHelper
                        .getDbUtilsInstance(mcontext)
                        .deleteAll(FormCodeVo.class);
                DataBaseHelper
                        .getDbUtilsInstance(mcontext)
                        .saveAll(list);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }


    protected void setHotData(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            JSONArray jarr = jsonObject.optJSONArray("list");
            if (null != jarr && jarr.length() > 0) {
                SharedPreferencesUtil.saveString(mcontext, "hotkey", jarr.toString());
            }
        }

    }

    /**
     * 多耐病例选择数据
     */


    public void getMutiTextData(String code, int reqCode) {   //CHECK_SPECIMEN   ： 送检标本DRUG_RESISTANCE  ： 耐药菌


        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("type", code);
            job.put("timeLong", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext,
                reqCode, job,
                "comm/queryStaticByCode");
    }

    // 获取工作圈首页TAB数据
    private void getChanelData() {


        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext,
                GET_CIRCLE_CHANEL, job,
                "workCircle/channelList");

    }

    protected void setInfoRes(JSONObject jsonObject) {
        if (jsonObject.has("list")) {
            JSONArray jarr = jsonObject.optJSONArray("list");
//		List<HotVo> list = gson.fromJson(jarr.toString(),
//				new TypeToken<List<HotVo>>() {
//				}.getType());
            if (null != jarr && jarr.length() > 0) {
                SharedPreferencesUtil.saveString(mcontext, "circle_channelList", jarr.toString());
            }


        }
    }


    protected void setJobList(JSONObject json) {
        JSONObject job = json.optJSONObject("jobInfos");
        SharedPreferencesUtil.saveString(mcontext, "jobinfolist",
                job.toString());
        JSONArray jarr = job.optJSONArray("jobType2");
        // 获取院内岗位职称
        List<JobListVo> list = TaskUtils.gson.fromJson(jarr.toString(),
                new TypeToken<List<JobListVo>>() {
                }.getType());
        if (null != list && list.size() > 0) {
            try {
                if (null != DataBaseHelper
                        .getDbUtilsInstance(mcontext)
                        .findAll(JobListVo.class)
                        && DataBaseHelper
                        .getDbUtilsInstance(mcontext)
                        .findAll(JobListVo.class).size() > 0) {
                    DataBaseHelper
                            .getDbUtilsInstance(mcontext)
                            .deleteAll(JobListVo.class);
                }
                DataBaseHelper.getDbUtilsInstance(mcontext)
                        .saveAll(list);
            } catch (DbException e) {
                e.printStackTrace();

            }
            // 获取职称

        }
    }

    protected void setDepartMentList(JSONObject json) {
        List<DepartmentVo> departlist = new ArrayList<DepartmentVo>();

        JSONArray jarr = json.optJSONArray("departments");
        if (null != json.optJSONArray("levels")) {
            JSONArray jarr2 = json.optJSONArray("levels");

            SharedPreferencesUtil.saveString(mcontext, "depart_levels",
                    jarr2.toString());
        } else {
            SharedPreferencesUtil.saveString(mcontext, "depart_levels", "");
        }
        SharedPreferencesUtil.saveString(mcontext, "departmentlist",
                jarr.toString());
        List<DepartmentVo> list = TaskUtils.gson.fromJson(jarr.toString(),
                new TypeToken<List<DepartmentVo>>() {
                }.getType());
        if (list.size() > 0) {
            departlist.clear();
            departlist.addAll(list);
            try {
                if (null != DataBaseHelper
                        .getDbUtilsInstance(mcontext)
                        .findAll(DepartmentVo.class)
                        && DataBaseHelper
                        .getDbUtilsInstance(mcontext)
                        .findAll(DepartmentVo.class).size() > 0) {
                    DataBaseHelper
                            .getDbUtilsInstance(mcontext)
                            .deleteAll(DepartmentVo.class);
                    DataBaseHelper
                            .getDbUtilsInstance(mcontext)
                            .saveAll(departlist);
                } else {
                    DataBaseHelper
                            .getDbUtilsInstance(mcontext)
                            .saveAll(departlist);

                }
            } catch (DbException e) {
                e.printStackTrace();

            }

        }
    }

    protected void saveWrongRuls(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            Log.i("111111111111rules", jsonObject.toString());
            SharedPreferencesUtil.saveString(mcontext, "rules_json",
                    jsonObject.toString());
        }

    }


    /**
     * @param id
     * @param code //回到参数
     */
    public void getCaseForm(String id, int code) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("temp_type", id + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, this, code, job, "comm/temp/list");
    }
}