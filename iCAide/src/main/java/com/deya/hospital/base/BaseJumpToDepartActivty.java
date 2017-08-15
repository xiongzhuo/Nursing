package com.deya.hospital.base;

import android.content.Intent;

import com.deya.hospital.vo.DepartVos;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/6/23
 */
public abstract class BaseJumpToDepartActivty extends BaseActivity{

    public void onChooseDepart(){
        Intent intent=new Intent(mcontext, DepartChooseActivity.class);
        startActivityForResult(intent,DepartChooseActivity.CHOOSE_SUC);
    }

    public void onMutiChoose(){
        Intent intent = new Intent(mcontext, DepartChooseActivity.class);
        intent.putExtra(DepartChooseActivity.MUTICHOOSE, 1);
        startActivityForResult(intent, DepartChooseActivity.CHOOSE_SUC);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(null!=data&&data.getAction()==DepartChooseActivity.MUTICHOOSE&&resultCode == DepartChooseActivity.CHOOSE_SUC){
            String   departIds="";

            List<DepartVos.DepartmentListBean> departs = (List<DepartVos.DepartmentListBean>) data.getSerializableExtra("departs");
            String names = "";
            for (int i = 0; i < departs.size(); i++) {
                DepartVos.DepartmentListBean bean = departs.get(i);

                if (i < departs.size() - 1) {
                    departIds += bean.getId() + ",";
                    names += bean.getName() + ",";
                } else {
                    departIds += bean.getId();
                    names += bean.getName();
                }
            }
            onChooseSuc(names,departIds);
        }else if(null!=data&&resultCode == DepartChooseActivity.CHOOSE_SUC){
            DepartVos.DepartmentListBean bean= (DepartVos.DepartmentListBean) data.getSerializableExtra("departData");
            onChooseSuc(bean);
        }
    }
    protected abstract  void onChooseSuc(DepartVos.DepartmentListBean bean);
    protected abstract  void onChooseSuc(String names,String ids);
}
