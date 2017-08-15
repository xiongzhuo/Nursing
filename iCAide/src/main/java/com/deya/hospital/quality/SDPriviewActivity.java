package com.deya.hospital.quality;

import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.workspace.multidrugresistant.RisitantRequestCode;
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
 * @date 2017/2/20
 */
public class SDPriviewActivity extends RisistantPriveiwActivity {
    String hospitalId="";
    String temp_id="";
    String hosptalName="";
    @Override
    public String getTaskType() {
        return "101";
    }

    @Override
    public String getCacheForm() {

        return SharedPreferencesUtil.getString(mcontext, "comonform" + taskVo.getType(), "");
    }

    @Override
    public BasePriviewInfoFragment getInfoFragment(CaseListVo.CaseListBean bean) {
        if (getIntent().hasExtra("isInspector")) {
            bean.setIsInspector(1);
            bean.setTemp_type_id(101);
        }
        return SDBaseInfoFragmetn.newInstance(bean);
    }
    /**
     *   重写获取详情方法
     */
    public void getDetailData(String taskId){
        if(getIntent().hasExtra("note")){
            taskVo.setNote(getIntent().getStringExtra("note"));
        }
        if(AbStrUtil.isEmpty(hospitalId)){
            return;
        }
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("temp_id", "17");
            job.put("hospital_id",hospitalId);
            job.put("note",taskVo.getNote());
//            job.put("department_id","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, this, RisitantRequestCode.GET_CASES_SUC,
                RisitantRequestCode.GET_CASES_FAIL, job, "tempTask/temp/findTempResult");
    }
    @Override
    public void setChildJson(JSONObject job, CaseListVo.CaseListBean businessBean) {
        try {
            job.put("check_ids", businessBean.getCheck_ids());
            job.put("drug_ids", businessBean.getDrug_ids());
            job.put("temp_type_id", "1");
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

        return "质量巡查";

    }


    @Override
    public void reSetTemps(int temp, int state) {

    }
}
