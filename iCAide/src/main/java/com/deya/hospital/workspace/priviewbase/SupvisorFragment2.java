package com.deya.hospital.workspace.priviewbase;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.deya.hospital.vo.RisistantVo;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */

@SuppressLint({"ValidFragment"})
public class SupvisorFragment2 extends SupvisorFragment implements View.OnClickListener {

    public static SupvisorFragment newInstance(RisistantVo.TaskInfoBean.SupervisorQuestionBean tv, RisistantVo rv) {
        SupvisorFragment2 newFragment = new SupvisorFragment2();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", tv);
        bundle.putSerializable("RisistantVo", rv);
        newFragment.setArguments(bundle);

        //bundle还可以在每个标签里传送数据


        return newFragment;

    }
    @Override
    public void setCheckContent() {
        if(null==problemEdt){
            return;
        }
        listQueStr="";

        if(null==rv){
            rv=new RisistantVo();
        }
        for (RisistantVo.TempListBean rt : rv.getTemp_list()) {
            if (rt == null) {
                continue;
            }
            String text = checkContentEdt.getText().toString();
            if (tv.getId()<=0&&rt.getContentState() == 0) {
                checkContentEdt.setText(getHeadText(text) + rt.getTitle());
                rt.setContentState(1);
            }

            for (RisistantVo.TempListBean.ItemListBean rti : rt.getItem_list()) {
                String text2 ="";
                switch (rti.getType()) {
                    case 4:
                    case 2:
                        if (null != rti.getChildren().get(0).getResult() && rti.getChildren().get(0).getResult().equals("2")) {
                            listQueStr = getHeadText(listQueStr) + rti.getTitle();
                        }
                        break;
                    default:
                        break;

                }
            }
        }

        if(null!=tv&&null!=rv&&tv.getId()<=0&&rv.getNeedChangeState()==0){
            problemEdt.setText(listQueStr);
        }
        firstQustion = problemEdt.getText().toString();
    }
}
