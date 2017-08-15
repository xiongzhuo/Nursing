package com.deya.hospital.quality;

import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.workspace.multidrugresistant.RisitantRequestCode;
import com.deya.hospital.workspace.priviewbase.BasePriviewInfoFragment;
import com.deya.hospital.workspace.priviewbase.BaseSupvisorFragment;
import com.deya.hospital.workspace.priviewbase.QualityInfoFragment;
import com.deya.hospital.workspace.priviewbase.RisistantPriveiwActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/17
 */
public class QualityPriviewActivity extends RisistantPriveiwActivity {

    @Override
    public String getTaskType() {
        return getIntent().getIntExtra("task_type",17)+"";
    }

    @Override
    public String getCacheForm() {
        return getIntent().getStringExtra("formdata");
    }

    @Override
    public BasePriviewInfoFragment getInfoFragment(CaseListVo.CaseListBean bean) {
        return QualityInfoFragment.newInstance(bean);
    }

    @Override
    public void setChildJson(JSONObject job, CaseListVo.CaseListBean businessBean) {
        try {
            job.put("room_no", businessBean.getRoom_no());
            job.put("bed_no", businessBean.getBed_no());
            job.put("person_liable", businessBean.getPerson_liable());
            job.put("temp_type_id", getTaskType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initChildForm(RisistantVo rv, CaseListVo.CaseListBean businessBean) {

        List<RisistantVo.TaskInfoBean.TempListBean> tablist = new ArrayList<>();

        RisistantVo.TaskInfoBean.TempListBean tempListBean = rv.getTask_info().getTemp_list();

        RisistantVo.TaskInfoBean.TempListBean tempListBean1 = new RisistantVo.TaskInfoBean.TempListBean();
        if (!isPriview) {
            basePriviewInfoFragment = getInfoFragment(businessBean);
            listfragment.add(basePriviewInfoFragment);

            if (null != rv.getTask_info().getTemp_list().getItem_list()) {
                BaseSupvisorFragment baseSupvisorFragment = BaseSupvisorFragment.newInstance(rv.getTask_info());
                listfragment.add(baseSupvisorFragment);
                if (!isPriview&&!isDetail) {
                    topView.setTitle(tempListBean.getTitle());
                }//动态设置标题
                businessBean.setTemp_type_id(tempListBean.getTemp_type_id());//只有环境
            }
            RisistantVo.TaskInfoBean.TempListBean tempListBean2 = new RisistantVo.TaskInfoBean.TempListBean();
            tempListBean2.setTemp_type_id(0);
            tablist.add(0, tempListBean2);
            tempListBean1.setTemp_type_id(15);
            tablist.add(1, tempListBean1);
        } else {
            if (null != rv.getTask_info().getTemp_list().getItem_list()) {
                BaseSupvisorFragment baseSupvisorFragment = BaseSupvisorFragment.newInstance(rv.getTask_info());
                listfragment.add(baseSupvisorFragment);
                if (!isPriview&&!isDetail) {
                    topView.setTitle(tempListBean.getTitle());
                }//动态设置标题
                businessBean.setTemp_type_id(tempListBean.getTemp_type_id());//只有环境
            }
            tempListBean1.setTemp_type_id(15);
            tablist.add(0, tempListBean1);
        }


        initTab(tablist);

    }

    @Override
    public void getDetailData(String tempId) {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("task_id", taskVo.getTask_id());
//           job.put("hospital_id","");
//            job.put("department_id","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, this, RisitantRequestCode.GET_CASES_SUC,
                RisitantRequestCode.GET_CASES_FAIL, job, "quality/taskDetail");
    }

    @Override
    protected String getTopTitle() {
        return isPriview ? "预览" : getIntent().getStringExtra("title");
    }

    @Override
    public void reSetTemps(int temp, int state) {

    }

}
