package com.deya.hospital.workspace.priviewbase;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.base.DepartChooseActivity;
import com.deya.hospital.util.DatePicDialog;
import com.deya.hospital.vo.CaseListVo2;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.workspace.TaskUtils;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public abstract class BasePriviewInfoFragment2 extends BaseFragment implements View.OnClickListener {
    public  TextView departTv;
    public TextView timeTv;
    public DatePicDialog datePicDialog;
    public CaseListVo2.CaseListBean caseVo;
    
   public  boolean isShowMore=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayout(), container,
                    false);
            initBuesinisData();
            initView();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }

    protected abstract void initBuesinisData();

    private void initView() {
        if(caseVo.getDepartment_id().equals("")&& TaskUtils.isPartTimeJob(getActivity())){
            caseVo.setDepartment_id(TaskUtils.getDefultDepartId());
            caseVo.setDepartmentName(TaskUtils.getDefultDepartName());
        }
        departTv = (TextView) rootView.findViewById(R.id.departTv);
        departTv.setOnClickListener(this);
        timeTv = (TextView) rootView.findViewById(R.id.timeTv);
        timeTv.setOnClickListener(this);
        timeTv.setText(TaskUtils.getLoacalDate());
        datePicDialog = new DatePicDialog(getActivity(),
                new DatePicDialog.OnDatePopuClick() {

                    @Override
                    public void enter(String text) {
                        timeTv.setText(text);
                        caseVo.setMission_time(text);
                        caseVo.setTime(TaskUtils.getTaskMissionTime(text));

                    }

                });
        initChildView();
    }

    public  abstract  void initChildView();
    public  abstract  int getLayout();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.departTv:
                Intent intent=new Intent(getActivity(), DepartChooseActivity.class);
                startActivityForResult(intent, DepartChooseActivity.CHOOSE_SUC);
                break;
            case R.id.timeTv:
                datePicDialog.show();
                break;
        }

    }

    public void initBaseData(){



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode == DepartChooseActivity.CHOOSE_SUC){
            DepartVos.DepartmentListBean bean= (DepartVos.DepartmentListBean) intent.getSerializableExtra("departData");
            departTv.setText(bean.getName());
            caseVo.setDepartmentName(bean.getName());
            caseVo.setDepartment_id(bean.getId()+"");
        }
    }

    @Override
    public void onDestroy() {
        if (null != datePicDialog && datePicDialog.isShowing()) {
            datePicDialog.dismiss();
        }
        super.onDestroy();
    }

    public abstract CaseListVo2.CaseListBean getData() ;



    public String getDepartment() {
        return caseVo.getDepartment_id();
    }
}
