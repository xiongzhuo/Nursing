package com.deya.hospital.workspace.safeinjection;

import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.workspace.priviewbase.BasePriviewInfoFragment;
import com.deya.hospital.workspace.priviewbase.BaseSupvisorFragment;
import com.deya.hospital.workspace.priviewbase.RisistantPriveiwActivity;

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
public class safeInjectionActivity extends RisistantPriveiwActivity {
    @Override
    public String getTaskType() {
        return "11";
    }

    @Override
    public String getCacheForm() {
        return SharedPreferencesUtil.getString(mcontext, "comonform" + taskVo.getType(), "");
    }

    @Override
    public BasePriviewInfoFragment getInfoFragment(CaseListVo.CaseListBean bean) {
        return SafeInjectionInfoFragment.newInstance(bean);
    }

    @Override
    public void initChildForm(RisistantVo rv, CaseListVo.CaseListBean businessBean) {
        List<RisistantVo.TempListBean> tablist = new ArrayList<>();
        basePriviewInfoFragment=getInfoFragment(businessBean);
        listfragment.add(basePriviewInfoFragment);
        if (null != rv.getTemp_list() && rv.getTemp_list().size() > 0) {
            for (RisistantVo.TempListBean rt : rv.getTemp_list()) {
                BaseSupvisorFragment baseSupvisorFragment = BaseSupvisorFragment.newInstance(rv.getTask_info());
                listfragment.add(baseSupvisorFragment);
                tablist.add(rt);
                break;
            }
        }
        RisistantVo.TempListBean tempListBean = new RisistantVo.TempListBean();
        tempListBean.setTemp_type_id(0);
        tablist.add(0, tempListBean);
        //initTab(tablist);
    }

    @Override
    protected String getTopTitle() {
        return "安全注射";
    }

    @Override
    public void reSetTemps(int temp, int state) {

    }

    @Override
    public void setChildJson(JSONObject job, CaseListVo.CaseListBean businessBean) {
        try {
            job.put("work_type", businessBean.getWork_type()+"");
            job.put("operation_type", businessBean.getOperation_type()+"");
            job.put("temp_type_id", "5");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
//    @Override
//    public BasePriviewInfoFragment getInfoFragment(CaseListVo.CaseListBean bean) {
//        return SafeInjectionInfoFragment.newInstance(bean);
//    }
//
//    @Override
//    public void initTaskVo() {
//        taskVo.setType("11");
//    }
//    @OverrideF
//    public String getCacheForm() {
//        return SharedPreferencesUtil.getString(mcontext,"comonform"+taskVo.getType(),"");
//    }
//
//    @Override
//    protected String getFormType() {
//        return "8";
//    }
//
//    @Override
//    protected void settoTipsActivityIntent() {
//        Intent it = new Intent(mcontext, FormCommitSucTipsActivity.class);
//        taskVo.setTaskListBean(TaskUtils.gson.toJson(caseVo));
//        it.putExtra("data", taskVo);
//        it.putExtra("type_id", getFormType());
//        it.putExtra("code", "");
//        startActivity(it);
//    }
//
//    @Override
//    public CaseListVo.CaseListBean getCaseBussiniBean() {
//        return caseVo;
//    }
//
//    @Override
//    public String getChildTitle() {
//        return "安全注射";
//    }
//
//    @Override
//    public void initTaskJson(JSONObject job,CaseListVo.CaseListBean businessBean) {
//
//        try {
//            job.put("work_type", businessBean.getWork_type()+"");
//            job.put("operation_type", businessBean.getOperation_type()+"");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void reSetTemps(int temp, int state) {
//
//    }
//
//    @Override
//    public void setChildJson(JSONObject job, CaseListVo.CaseListBean businessBean) {
//
//    }
}
