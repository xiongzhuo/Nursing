package com.deya.hospital.workspace.KeySectorsFrom;

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
 * @date 2016/12/13
 */
public class KeySectorsFormActivity extends RisistantPriveiwActivity{
    @Override
    public String getTaskType() {
        return "16";
    }

    @Override
    public String getCacheForm() {
        return getIntent().getStringExtra("formdata");
    }

    @Override
    public BasePriviewInfoFragment getInfoFragment(CaseListVo.CaseListBean bean) {
        return KeySecotorstInfoFragment.newInstance(bean);
    }

    @Override
    public void setChildJson(JSONObject job, CaseListVo.CaseListBean businessBean) {
        try {
            job.put("room_no", businessBean.getRoom_no());
            job.put("bed_no", businessBean.getBed_no());
            job.put("person_liable", businessBean.getPerson_liable());
            job.put("temp_type_id", "8");
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
                topView.setTitle(rt.getTitle());//动态设置标题
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
        return getIntent().getStringExtra("title");
    }

    @Override
    public void reSetTemps(int temp, int state) {

    }

}
