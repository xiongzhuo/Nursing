package com.deya.hospital.workspace.multidrugresistant;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.ResistantMutiTextVo;
import com.deya.hospital.widget.view.MultipleTextViewGroup;
import com.deya.hospital.widget.view.TextViewGroup;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.priviewbase.BasePriviewInfoFragment;

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
public class MutiInfoFragment extends BasePriviewInfoFragment{
    private EditText bedNumberTv;
    private EditText nameTv;
    private EditText patientIdEdt;
    private ResistantMutiTextVo rmtv;
    private ResistantMutiTextVo drugMtv;
    private MultipleTextViewGroup rl;
    private MultipleTextViewGroup drugMutiTv;
    LinearLayout mutiInfolay;
    TextView moreView;

    public static MutiInfoFragment newInstance(CaseListVo.CaseListBean caseVo) {
        MutiInfoFragment newFragment = new MutiInfoFragment();
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
        getChache();
        if (!AbStrUtil.isEmpty(caseVo.getCheck_ids())) {
            spiltCheckId(caseVo.getCheck_ids());

        }
        if (!AbStrUtil.isEmpty(caseVo.getDrug_ids())) {
            spiltDrugId(caseVo.getDrug_ids());

        }
        rootView. findViewById(R.id.sumbmitlay).setVisibility(View.GONE);
        bedNumberTv = (EditText) rootView.findViewById(R.id.bedNumberTv);
        nameTv = (EditText) rootView.findViewById(R.id.nameTv);
        patientIdEdt = (EditText) rootView.findViewById(R.id.patientIdEdt);
        rootView.findViewById(R.id.topView).setVisibility(View.GONE);
        patientIdEdt.setText(caseVo.getPatient_id());
        bedNumberTv.setText(caseVo.getBed_no());
        departTv.setText(caseVo.getDepartmentName());
        moreView= (TextView) rootView.findViewById(R.id.moreView);
        mutiInfolay= (LinearLayout) rootView.findViewById(R.id.mutiInfolay);
        moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             isShowMore=!isShowMore;
                moreView.setText(isShowMore?"点击收起":"查看全部");
                mutiInfolay.setVisibility(isShowMore?View.VISIBLE:View.GONE);
            }
        });
        if(AbStrUtil.isEmpty(caseVo.getTime())){
            caseVo.setTime(TaskUtils.getLoacalDate());
        }
        timeTv.setText(caseVo.getTime());
        nameTv.setText(caseVo.getPatient_name());
        timeTv.setText(caseVo.getTime());
        nameTv.setText(caseVo.getPatient_name());
        initBaseData();
        rl = (MultipleTextViewGroup) findViewById(R.id.main_rl);
        drugMutiTv = (MultipleTextViewGroup) findViewById(R.id.drugMutiTv);

        rl.setOnTextViewGroupItemClickListener(new TextViewGroup.OnTextViewGroupItemClickListener() {

            @Override
            public void OnTextViewGroupClick(View view,
                                             List<TextViewGroup.TextViewGroupItem> _dataList,
                                             TextViewGroup.TextViewGroupItem item) {


            }
        });
        iniMultipleTextView();


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
        caseVo.setTemp_type_id(1);

        reasonIds.clear();
        if (null != reasonList && reasonList.size() > 0) {
            for (int i = 0; i < reasonList.size(); i++) {
                if (reasonList.get(i).isStatus()) {
                    // reasons.add(reasonList.get(i).getName());
                    if (i < reasonList.size() - 1) {
                        check_ids += reasonList.get(i).getPosition() + ",";
                    } else {
                        check_ids += reasonList.get(i).getPosition();
                    }
                }
            }
        }
        caseVo.setCheck_ids(check_ids);
        if (null != drugList && drugList.size() > 0) {
            for (int i = 0; i < drugList.size(); i++) {
                if (drugList.get(i).isStatus()) {
                    // reasons.add(reasonList.get(i).getName());
                    if (i < drugList.size() - 1) {
                        drug_ids += drugList.get(i).getPosition() + ",";
                    } else {
                        drug_ids += drugList.get(i).getPosition();
                    }
                }
            }
        }
        caseVo.setDrug_ids(drug_ids);
        return caseVo;
    }


    private void getChache() {
        String str = SharedPreferencesUtil.getString(getActivity(), "resistant_mutitext", "");
        if (!AbStrUtil.isEmpty(str)) {
            try {
                setMutiTextResult(new JSONObject(str));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String str2 = SharedPreferencesUtil.getString(getActivity(), "resistant_mutidrugtext", "");
        if (!AbStrUtil.isEmpty(str2)) {
            try {
                setDrugMutiTextResult(new JSONObject(str2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // 送检标本
    List<TextViewGroup.TextViewGroupItem> reasonList = new ArrayList<TextViewGroup.TextViewGroupItem>();
    ArrayList<Integer> reasonIds = new ArrayList<Integer>();

    //耐药菌
    List<TextViewGroup.TextViewGroupItem> drugList = new ArrayList<TextViewGroup.TextViewGroupItem>();
    ArrayList<Integer> drugIds = new ArrayList<Integer>();

    String check_ids = "";
    String drug_ids = "";
    private void setMutiTextResult(JSONObject jsonObject) {

        if (jsonObject.optString("result_id").equals("0")) {
            rmtv = TaskUtils.gson.fromJson(jsonObject.toString(), ResistantMutiTextVo.class);
            SharedPreferencesUtil.saveString(getActivity(), "resistant_mutitext", jsonObject.toString());
        } else {
            rmtv = new ResistantMutiTextVo();
        }

    }

    private void setDrugMutiTextResult(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            drugMtv = TaskUtils.gson.fromJson(jsonObject.toString(), ResistantMutiTextVo.class);
            SharedPreferencesUtil.saveString(getActivity(), "resistant_mutidrugtext", jsonObject.toString());
        } else {
            drugMtv = new ResistantMutiTextVo();
        }
    }

    public  void spiltDrugId(String sss) {
        try {
            String[] arr = sss.split(",");//根据“ ”和“,”区分
            for(String string :arr){
                drugIds.add(Integer.parseInt(string));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void spiltCheckId(String sss) {
        try {
            String[] arr = sss.split(",");//根据“ ”和“,”区分
            for(String string :arr){
                reasonIds.add(Integer.parseInt(string));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void iniMultipleTextView() {
        if (null == rmtv) {
            rmtv = new ResistantMutiTextVo();
        }

        List<ResistantMutiTextVo.ResultListBean> mutiTxtList = rmtv.getResultList();
        if (mutiTxtList == null) {
            mutiTxtList = new ArrayList<>();
        }
        if (null != mutiTxtList && mutiTxtList.size() > 0) {
            TextViewGroup.TextViewGroupItem item = null;
            for (int i = 0; i < mutiTxtList.size(); i++) {
                item = rl.NewTextViewGroupItem();
                item.setText(mutiTxtList.get(i).getData_name());
                String id_str = mutiTxtList.get(i).getData_id()+"";
                if (TextUtils.isDigitsOnly(id_str)) {
                    item.setStatus(reasonIds.contains(Integer.parseInt(id_str)));
                    item.setPosition(Integer.parseInt(id_str));
                }
                reasonList.add(item);
            }
            rl.setTextViews(reasonList);
        }

        if (null == drugMtv) {
            drugMtv = new ResistantMutiTextVo();
        }
        List<ResistantMutiTextVo.ResultListBean> mutiTxtList2 = drugMtv.getResultList();
        if (null != mutiTxtList2 && mutiTxtList2.size() > 0) {
            TextViewGroup.TextViewGroupItem item = null;
            for (int i = 0; i < mutiTxtList2.size(); i++) {
                item = drugMutiTv.NewTextViewGroupItem();
                item.setText(mutiTxtList2.get(i).getData_name());
                String id_str = mutiTxtList2.get(i).getData_id()+"";
                if (TextUtils.isDigitsOnly(id_str)) {
                    item.setStatus(drugIds.contains(Integer.parseInt(id_str)));
                    item.setPosition(Integer.parseInt(id_str));
                }
                drugList.add(item);
            }
            drugMutiTv.setTextViews(drugList);
        }
    }
}
