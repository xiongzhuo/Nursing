package com.deya.hospital.workspace.site_infection;

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
public class SiteInfectionPriviewAcitivty extends RisistantPriveiwActivity {

    @Override
    public BasePriviewInfoFragment getInfoFragment(CaseListVo.CaseListBean bean) {
        return SettingInfectionInfoFragment.newInstance(bean);
    }

    @Override
    public void setChildJson(JSONObject job, CaseListVo.CaseListBean businessBean) {
        try {
            job.put("temp_type_id", "4");
            job.put("operation_name", businessBean.getOperation_name());
            job.put("patient_id", businessBean.getPatient_id());
            job.put("patient_name", businessBean.getPatient_name());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        return "手术部位感染防控";
    }

    @Override
    public String getTaskType() {
       return  "12";
    }

    @Override
    public String getCacheForm() {
        return SharedPreferencesUtil.getString(mcontext,"comonform"+taskVo.getType(),"");
    }

    @Override
    public void reSetTemps(int temp, int state) {

    }
}