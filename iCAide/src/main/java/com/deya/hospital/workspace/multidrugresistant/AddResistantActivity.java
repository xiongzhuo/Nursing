package com.deya.hospital.workspace.multidrugresistant;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.ResistantMutiTextVo;
import com.deya.hospital.widget.view.MultipleTextViewGroup;
import com.deya.hospital.widget.view.TextViewGroup;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.priviewbase.CaseUtil;
import com.deya.hospital.workspace.priviewbase.ResistantListActivity;

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
public class AddResistantActivity extends RistantAndThreePipesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ristant_threepipes_layout;
    }

    @Override
    public void saveCache() {
        setCase();
        caseVo.setUploadStatus(2);
        caseVo.setTemp_type_id(1);
        CaseUtil.onAddCaseInDb(caseVo);
        Intent inten = new Intent();
        inten.setAction(ResistantListActivity.UPDATE_ADD);
        inten.putExtra("data", caseVo);
        sendBroadcast(inten);
        finish();
    }


    @Override
    public void initView() {
        topView.setTitle("添加多耐病例");
        getChache();
        if (getIntent().hasExtra("casedata")) {
            caseVo = (CaseListVo.CaseListBean) getIntent().getSerializableExtra("casedata");
            initBaseData();
            if (!AbStrUtil.isEmpty(caseVo.getCheck_ids())) {
                spiltCheckId(caseVo.getCheck_ids());

            }
            if (!AbStrUtil.isEmpty(caseVo.getDrug_ids())) {
                spiltDrugId(caseVo.getDrug_ids());

            }
        }
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
    public void setBaseJson(JSONObject job) {
        try {
            job.put("temp_type_id", "1");
            job.put("check_ids", caseVo.getCheck_ids());
            job.put("drug_ids", caseVo.getDrug_ids());
        } catch (JSONException e) {
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
    private void getChache() {
        String str = SharedPreferencesUtil.getString(mcontext, "resistant_mutitext", "");
        if (!AbStrUtil.isEmpty(str)) {
            try {
                setMutiTextResult(new JSONObject(str));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String str2 = SharedPreferencesUtil.getString(mcontext, "resistant_mutidrugtext", "");
        if (!AbStrUtil.isEmpty(str2)) {
            try {
                setDrugMutiTextResult(new JSONObject(str2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setMutiTextResult(JSONObject jsonObject) {

        if (jsonObject.optString("result_id").equals("0")) {
            rmtv = TaskUtils.gson.fromJson(jsonObject.toString(), ResistantMutiTextVo.class);
            SharedPreferencesUtil.saveString(mcontext, "resistant_mutitext", jsonObject.toString());
        } else {
            rmtv = new ResistantMutiTextVo();
        }

    }

    private void setDrugMutiTextResult(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            drugMtv = TaskUtils.gson.fromJson(jsonObject.toString(), ResistantMutiTextVo.class);
            SharedPreferencesUtil.saveString(mcontext, "resistant_mutidrugtext", jsonObject.toString());
        } else {
            drugMtv = new ResistantMutiTextVo();
        }
    }


    @Override
    public void setCase() {
        caseVo.setBed_no(bedNumberTv.getText().toString());
        //caseVo.setCheck_ids(che);
        caseVo.setPatient_id(patientIdEdt.getText().toString());
        caseVo.setTime(timeTv.getText().toString());
        caseVo.setPatient_name(nameTv.getText().toString());
        caseVo.setTime(timeTv.getText().toString());
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
    }



    // 送检标本
    List<TextViewGroup.TextViewGroupItem> reasonList = new ArrayList<TextViewGroup.TextViewGroupItem>();
    ArrayList<Integer> reasonIds = new ArrayList<Integer>();

    //耐药菌
    List<TextViewGroup.TextViewGroupItem> drugList = new ArrayList<TextViewGroup.TextViewGroupItem>();
    ArrayList<Integer> drugIds = new ArrayList<Integer>();

    String check_ids = "";
    String drug_ids = "";

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
