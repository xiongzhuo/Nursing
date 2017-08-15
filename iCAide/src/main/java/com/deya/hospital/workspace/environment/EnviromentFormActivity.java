package com.deya.hospital.workspace.environment;

import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.CaseListVo2;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.workspace.priviewbase.BasePriviewInfoFragment2;
import com.deya.hospital.workspace.priviewbase.BaseSupvisorFragment2;
import com.deya.hospital.workspace.priviewbase.RisistantPriveiwActivity2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/10/8
 */
public class EnviromentFormActivity extends RisistantPriveiwActivity2 {
    @Override
    public String getTaskType() {
        return "10";
    }

    @Override
    public String getCacheForm() {
        return getIntent().getStringExtra("formdata");
    }



    @Override
    public BasePriviewInfoFragment2 getInfoFragment(CaseListVo2.CaseListBean bean) {
        return EnvironmentInfoFragment.newInstance(bean);
    }

    @Override
    public void setChildJson(JSONObject job, CaseListVo.CaseListBean businessBean) {
        try {
            job.put("room_no", businessBean.getRoom_no());
            job.put("bed_no", businessBean.getBed_no());
            job.put("person_liable", businessBean.getPerson_liable());
            job.put("temp_type_id", "3");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initChildForm(RisistantVo rv, CaseListVo2.CaseListBean businessBean) {
        List<RisistantVo.TempListBean> tablist = new ArrayList<>();
        basePriviewInfoFragment=getInfoFragment(businessBean);
        listfragment.add(basePriviewInfoFragment);
        if (null != rv.getTemp_list() && rv.getTemp_list().size() > 0) {
            for (RisistantVo.TempListBean rt : rv.getTemp_list()) {
                BaseSupvisorFragment2 baseSupvisorFragment = BaseSupvisorFragment2.newInstance(rt.getItem_list());
                listfragment.add(baseSupvisorFragment);
                 topView.setTitle(rt.getTitle());//动态设置标题
                tablist.add(rt);
                businessBean.setTemp_type_id(rt.getTemp_type_id());//只有环境
                break;
            }
        }
        RisistantVo.TempListBean tempListBean = new RisistantVo.TempListBean();
        tempListBean.setTemp_type_id(0);
        tablist.add(0, tempListBean);
        initTab(tablist);

    }


    @Override
    protected String getTopTitle() {
        return getIntent().getStringExtra("title");
    }

    @Override
    public void reSetTemps(int temp, int state) {

    }

}
