package com.deya.hospital.workspace.priviewbase;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseListDialog;
import com.deya.hospital.quality.QualityPriviewActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.GsonUtils;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.ResistantMutiTextVo;
import com.deya.hospital.workspace.TaskUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class QualityInfoFragment extends BasePriviewInfoFragment implements View.OnClickListener {
    private EditText bedNumberTv;
    private EditText nameTv;
    private EditText roomNumberTv;
    ResistantMutiTextVo textVo1, textVo2, textVo3;
    List<ResistantMutiTextVo.ResultListBean> dialogList;
    TextView quarantineTv, cleanTimesTv, monitorMethodTv;
    ListDialog listDialog;
    int dialogIndex;
    TextView personview;

    public static QualityInfoFragment newInstance(CaseListVo.CaseListBean caseVo) {
        QualityInfoFragment newFragment = new QualityInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", caseVo);
        newFragment.setArguments(bundle);
        return newFragment;
    }


    @Override
    protected void initBuesinisData() {
        Bundle args = getArguments();
        caseVo = (CaseListVo.CaseListBean) args.getSerializable("data");
    }

    @Override
    public void initChildView() {
        dialogList=new ArrayList<>();
        bedNumberTv = (EditText) this.findViewById(R.id.bedNumberTv);
        nameTv = (EditText) this.findViewById(R.id.nameTv);
        caseVo.setTime(TaskUtils.getTaskMissionTime(TaskUtils.getLoacalDate()));
        findView(R.id.nameLay).setVisibility(View.GONE);
        roomNumberTv = (EditText) this.findViewById(R.id.roomNumberTv);

        if (null != caseVo) {
            departTv.setText(caseVo.getDepartmentName());
            nameTv.setText(caseVo.getPerson_liable());
            if (AbStrUtil.isEmpty(caseVo.getTime())) {
                caseVo.setTime(TaskUtils.getLoacalDate());
            }
            timeTv.setText(caseVo.getTime());
            roomNumberTv.setText(caseVo.getRoom_no());
            bedNumberTv.setText(caseVo.getBed_no());
        }
        quarantineTv = (TextView) this.findViewById(R.id.quarantineTv);
        quarantineTv.setOnClickListener(this);
        cleanTimesTv = (TextView) this.findViewById(R.id.cleanTimesTv);
        cleanTimesTv.setOnClickListener(this);
        monitorMethodTv = (TextView) this.findViewById(R.id.monitorMethodTv);
        monitorMethodTv.setOnClickListener(this);
        String str1 = SharedPreferencesUtil.getString(getActivity(), "get_ev_quarantin", "");
        textVo1 =  GsonUtils.JsonToObject(str1,ResistantMutiTextVo.class);
        String str2 = SharedPreferencesUtil.getString(getActivity(), "get_ev_cleartimes", "");
        textVo2=  GsonUtils.JsonToObject(str2,ResistantMutiTextVo.class);
        String str3 = SharedPreferencesUtil.getString(getActivity(), "get_ev_monitor", "");
        textVo3 =  GsonUtils.JsonToObject(str3,ResistantMutiTextVo.class);
        listDialog=new ListDialog(getActivity(), dialogList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dataname=dialogList.get(position).getData_name();
                switch (dialogIndex){
                    case 0:
                        caseVo.setIs_quarantine(dialogList.get(position).getData_id()+"");
                        quarantineTv.setText(dataname);
                        break;
                    case  1:
                        cleanTimesTv.setText(dataname);
                        caseVo.setCleaning_time(dialogList.get(position).getData_id()+"");
                        break;
                    case 2:
                        caseVo.setMonitoring_method(dialogList.get(position).getData_id()+"");
                        monitorMethodTv.setText(dataname);
                        break;
                }
                listDialog.dismiss();

            }
        });
//        if(!AbStrUtil.isEmpty(caseVo.getIs_quarantine())){
//            for(ResistantMutiTextVo.ResultListBean bean:textVo1.getResultList()){
//                if(bean.getData_id().equals(caseVo.getIs_quarantine())){
//                    quarantineTv.setText(bean.getData_name());
//                }
//            }
//        }
//        if(!AbStrUtil.isEmpty(caseVo.getCleaning_time())){
//            for(ResistantMutiTextVo.ResultListBean bean:textVo2.getResultList()){
//                if(bean.getData_id().equals(caseVo.getCleaning_time())){
//                    cleanTimesTv.setText(bean.getData_name());
//                }
//            }
//        }
//        if(!AbStrUtil.isEmpty(caseVo.getMonitoring_method())){
//            for(ResistantMutiTextVo.ResultListBean bean:textVo1.getResultList()){
//                if(bean.getData_id().equals(caseVo.getMonitoring_method())){
//                    monitorMethodTv.setText(bean.getData_name());
//                }
//            }
//        }

        personview= (TextView) this.findViewById(R.id.personview);
        personview.setText("清洁人员 ");
        // findViewById(R.id.ev_view).setVisibility(View.VISIBLE);
        if(QualityPriviewActivity.isDetail){
            timeTv.setOnClickListener(null);
            departTv.setOnClickListener(null);
        }

    }

    @Override
    public int getLayout() {
        return R.layout.environment_info_fragment;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.quarantineTv:
                dialogIndex=0;
                dialogList.clear();
                dialogList.addAll(textVo1.getResultList());
                listDialog.showCenter();
                break;
            case R.id.cleanTimesTv:
                dialogIndex=1;
                dialogList.clear();
                dialogList.addAll(textVo2.getResultList());
                listDialog.showCenter();
                break;
            case R.id.monitorMethodTv:
                dialogIndex=2;
                dialogList.clear();
                dialogList.addAll(textVo3.getResultList());
                listDialog.showCenter();
                break;
        }

    }


    public CaseListVo.CaseListBean getData() {
        caseVo.setMission_time(timeTv.getText().toString());
        caseVo.setDepartmentName(departTv.getText().toString());
        caseVo.setRoom_no(roomNumberTv.getText().toString());
        caseVo.setBed_no(bedNumberTv.getText().toString());
        caseVo.setPerson_liable(nameTv.getText().toString());
        caseVo.setTime(timeTv.getText().toString());
        caseVo.setTemp_type_id(3);
        return caseVo;

    }

    public String getDepartment() {
        return caseVo.getDepartment_id();
    }
    class  ListDialog extends BaseListDialog<ResistantMutiTextVo.ResultListBean> {

        public ListDialog(Context context, List<ResistantMutiTextVo.ResultListBean> list, AdapterView.OnItemClickListener listener) {
            super(context, list, listener);
        }

        @Override
        protected void intUi() {
            titleTv.setText("请选择");
            right_txt.setVisibility(View.GONE);

        }

        @Override
        public void setListDta(ViewHolder viewHolder, int position) {
            viewHolder.listtext.setText(dialogList.get(position).getData_name());

        }
    }
}
