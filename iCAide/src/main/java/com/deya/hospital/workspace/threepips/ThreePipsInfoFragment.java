package com.deya.hospital.workspace.threepips;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.SimpleSwitchButton;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.workspace.BootomSelectDialog;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.priviewbase.BasePriviewActivity;
import com.deya.hospital.workspace.priviewbase.BasePriviewInfoFragment;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class ThreePipsInfoFragment extends BasePriviewInfoFragment {
    LinearLayout sanguanLay;
    SimpleSwitchButton sanguan1, sanguan2, sanguan3;
    private TextView sexTv;
    private EditText scoreEdt;
    private EditText bedNumberTv;
    private EditText nameTv;
    private EditText patientIdEdt;
    private BootomSelectDialog sexDilog;
    private TextView moreView;
    private LinearLayout moreLay;

    public static ThreePipsInfoFragment newInstance(CaseListVo.CaseListBean caseVo) {
        ThreePipsInfoFragment newFragment = new ThreePipsInfoFragment();
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
        sanguanLay = (LinearLayout) rootView.findViewById(R.id.sanguanLay);
        sanguanLay.setVisibility(View.VISIBLE);
        rootView. findViewById(R.id.mutitextLay1).setVisibility(View.GONE);
        rootView. findViewById(R.id.sumbmitlay).setVisibility(View.GONE);
        sexTv = (TextView) rootView.findViewById(R.id.sexTv);
        sexTv.setOnClickListener(this);
        moreView= (TextView) rootView.findViewById(R.id.moreView);
        moreLay=(LinearLayout) rootView.findViewById(R.id.moreLay);
        moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowMore=!isShowMore;
                moreView.setText(isShowMore?"点击收起":"查看全部");
                moreLay.setVisibility(isShowMore?View.VISIBLE:View.GONE);
            }
        });
        bedNumberTv = (EditText) rootView.findViewById(R.id.bedNumberTv);
        nameTv = (EditText) rootView.findViewById(R.id.nameTv);
        patientIdEdt = (EditText) rootView.findViewById(R.id.patientIdEdt);
        scoreEdt = (EditText) rootView.findViewById(R.id.scoreEdt);
        sanguan1 = (SimpleSwitchButton) rootView.findViewById(R.id.sanguan1);
        sanguan1.setText("呼吸机");
        sanguan2 = (SimpleSwitchButton) rootView.findViewById(R.id.sanguan2);
        sanguan2.setText("血管导管");
        sanguan3 = (SimpleSwitchButton) rootView.findViewById(R.id.sanguan3);
        sanguan3.setText("导尿管");
        rootView.findViewById(R.id.topView).setVisibility(View.GONE);
        sanguan1.setCheck(caseVo.getTemp1() == 1 ? true : false);
        sanguan2.setCheck(caseVo.getTemp2() == 1 ? true : false);
        sanguan3.setCheck(caseVo.getTemp3() == 1 ? true : false);
        if(caseVo.getIsInspector()==1){
            sanguan1.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
                @Override
                public void onCheckChange(boolean ischeck) {
                    caseVo.setTemp1(ischeck ? 1 : 0);
                    sendTempSchangedBro(caseVo.getTemp1(),1);
                }
            });
            sanguan2.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
                @Override
                public void onCheckChange(boolean ischeck) {
                    caseVo.setTemp2(ischeck ? 1 : 0);
                    sendTempSchangedBro(caseVo.getTemp2(),2);
                }
            });
            sanguan3.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
                @Override
                public void onCheckChange(boolean ischeck) {
                    caseVo.setTemp3(ischeck ? 1 : 0);
                    sendTempSchangedBro(caseVo.getTemp3(),3);
                }
            });

        }else{
            sanguan1.setEnabled(false);
            sanguan2.setEnabled(false);
            sanguan3.setEnabled(false);
        }

        patientIdEdt.setText(caseVo.getPatient_id());
        bedNumberTv.setText(caseVo.getBed_no());
        scoreEdt.setText(caseVo.getApache());
        sexTv.setText(caseVo.getSex() == 1 ? "女" : "男");
        departTv.setText(caseVo.getDepartmentName());
        if(AbStrUtil.isEmpty(caseVo.getTime())){
            caseVo.setTime(TaskUtils.getLoacalDate());
        }
        timeTv.setText(caseVo.getTime());
        nameTv.setText(caseVo.getPatient_name());
        initSexDialog();
        initBaseData();
    }


    public void  sendTempSchangedBro(int state,int temptype){
        Intent intent=new Intent(BasePriviewActivity.TEMPS_CHANGED);
        intent.putExtra("temptype", temptype);
       intent.putExtra("state", state);
        getActivity().sendBroadcast(intent);

    }
    @Override
    public int getLayout() {
        return R.layout.ristant_threepipes_layout;
    }

    @Override
    public CaseListVo.CaseListBean getData() {
        caseVo.setBed_no(bedNumberTv.getText().toString());
        caseVo.setPatient_id(patientIdEdt.getText().toString());
        caseVo.setTime(timeTv.getText().toString());
        caseVo.setPatient_name(nameTv.getText().toString());
        caseVo.setTime(timeTv.getText().toString());
        caseVo.setApache(scoreEdt.getText().toString());
        return caseVo;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==R.id.sexTv){
            sexDilog.show();
        }

    }


    public void initSexDialog() {
        String titles[] = {"男", "女"};
        sexDilog = new BootomSelectDialog(getActivity(), titles,
                new BootomSelectDialog.BottomDialogInter() {

                    @Override
                    public void onClick3() {

                    }

                    @Override
                    public void onClick2() {
                        sexTv.setText("女");
                        caseVo.setSex(1);
                    }

                    @Override
                    public void onClick1() {
                        sexTv.setText("男");
                        caseVo.setSex(0);
                    }

                    @Override
                    public void onClick4() {
                        // TODO Auto-generated method stub

                    }
                });
    }


    @Override
    public void onDestroy() {
        if(null!=sexDilog){
            sexDilog.dismiss();
        }
        super.onDestroy();
    }
}
