package com.deya.hospital.workspace.handwash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseAddFileActivity;
import com.deya.hospital.base.DepartChoosePop;
import com.deya.hospital.setting.NewbieHelpActivity;
import com.deya.hospital.supervisor.AddDepartmentActivity;
import com.deya.hospital.supervisor.SupervisorFeedBackActivity;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.vo.dbdata.planListDb;
import com.deya.hospital.vo.dbdata.subTaskVo;
import com.deya.hospital.workspace.BootomSelectDialog;
import com.deya.hospital.workspace.TaskUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/10/11
 */
public abstract class HandBaseActivity extends BaseAddFileActivity {
    private PopupWindow popWindow;
    String titles2[] = {"拍照", "本地图片"};
    protected BootomSelectDialog bootomSelectDialog;
    protected  List<planListDb> list = new ArrayList<>();
    protected  TaskVo tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDepartPop();
        bootomSelectDialog=new BootomSelectDialog(mcontext, titles2, new BootomSelectDialog.BottomDialogInter() {
            @Override
            public void onClick1() {
                takePhoto();
            }

            @Override
            public void onClick2() {
                getloacalimg();
            }

            @Override
            public void onClick3() {

            }

            @Override
            public void onClick4() {

            }
        });
        initFeedUsageData();//用品设施
    }

    protected  void initFeedUsageData(){

        String json = tools.getValue("equipExamineParams");
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            String r = jsonObject.getString("result_id");
            if (null != r && r.equals("0")) {

                JSONArray jsonArray1 = jsonObject
                        .getJSONArray("equipExamineParams");
                HashMap<String, Object> map = null;
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject itemJson = jsonArray1.getJSONObject(i);
                    map = new HashMap<String, Object>();
                    map.put("id", itemJson.getInt("id"));
                    map.put("name", itemJson.getString("name"));
                    equipList.add(map);
                }

                JSONArray jsonArray2 = jsonObject
                        .getJSONArray("trainingParams");
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject itemJson = jsonArray2.getJSONObject(i);
                    map = new HashMap<String, Object>();
                    map.put("id", itemJson.getInt("id"));
                    map.put("name", itemJson.getString("name"));
                    trainingList.add(map);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showsettingPopWindow(Context context, View parent) {
        if (null == popWindow) {
            int width = parent.getWidth();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View vPopWindow = inflater.inflate(R.layout.handwash_tasksetting_pop, null,
                    true);
            vPopWindow.findViewById(R.id.layout).setAlpha(0.80f);
            popWindow = new PopupWindow(vPopWindow, width,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
            vPopWindow.findViewById(R.id.tackphotoTv).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popWindow.dismiss();
                            bootomSelectDialog.show();
                        }
                    });
            vPopWindow.findViewById(R.id.localImgTv).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popWindow.dismiss();
                            startFeedBack();

                        }
                    });
            vPopWindow.findViewById(R.id.recoderTv).setVisibility(View.GONE);
            vPopWindow.findViewById(R.id.recoderTv).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popWindow.dismiss();
                            showRecordPopWindow(R.id.main,false,false);
                        }
                    });

            vPopWindow.findViewById(R.id.setttings).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popWindow.dismiss();
                            Intent intent=new Intent(mcontext,HandWashSettingActivity.class);
                            startActivityForResult(intent,HandWashSettingActivity.RESULTCODE);
                        }
                    });
            vPopWindow.findViewById(R.id.newsHelper).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mcontext,NewbieHelpActivity.class);
                    intent.putExtra("url", WebUrl.LEFT_URL+"/gkgzj-help/app_sws.html");
                    startActivity(intent);
                }
            });
        }


        popWindow.setBackgroundDrawable(getResources().getDrawable(
                android.R.color.transparent));
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        int px = AbViewUtil.dip2px(mcontext, 10);
        popWindow.showAsDropDown(parent, AbViewUtil.dip2px(mcontext, 0), -20);
    }
    List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
    public DepartChoosePop departDialog;

    public void initDepartPop(){
        TaskUtils.getDepartList(mcontext, departlist);
        departDialog = new DepartChoosePop(mcontext, departlist,
                new DepartChoosePop.OnDepartPopuClick() {
                    @Override
                    public void onChooseDepart(String name, String id) {
                        onChooseDepartList(name,id);
                    }

                    @Override
                    public void onAddDepart() {
                        Intent it = new Intent(mcontext,
                                AddDepartmentActivity.class);
                        it.putExtra("data", (Serializable) departlist);
                        startActivityForResult(it, 0x22);
                    }
                });
    }
    protected abstract void onChooseDepartList(String name, String id);

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == 0x22 && null != intent) {
            ChildsVo dv = (ChildsVo) intent.getSerializableExtra("data");
            String rooId = dv.getParent();
            for (DepartLevelsVo dlv : departlist) {
                if (rooId.equals("1") && dlv.getRoot().getId().equals("0")) {
                    // dlv.setChooseNum(dlv.getChooseNum() + 1);
                    dlv.getChilds().add(0, dv);
                } else if (dlv.getRoot().getId().equals(rooId)) {
                    // dlv.setChooseNum(dlv.getChooseNum() + 1);
                    dlv.getChilds().add(0, dv);
                }
            }
            departDialog.setdata(departlist);
            onChooseDepartList(dv.getName(),dv.getId());

        } else if (resultCode == 0x23 && null != intent) {
            ChildsVo dv2 = (ChildsVo) intent.getSerializableExtra("data");
            onChooseDepartList(dv2.getName(),dv2.getId());
        }

    }




    /**
     * 卫生消毒操作的不规范记录
     */
    ArrayList<HashMap<String, String>> recordDisinfectionList = new ArrayList<HashMap<String, String>>();
    /**
     * 洗手操作的不规范记录
     */
    ArrayList<HashMap<String, String>> recordWashHandsList = new ArrayList<HashMap<String, String>>();
    /**
     * 戴手套操作的不规范记录
     */
    ArrayList<HashMap<String, String>> recordGloveList = new ArrayList<HashMap<String, String>>();
    /**
     * 未采取措施的不规范记录
     */
    ArrayList<HashMap<String, String>> recordNothingList = new ArrayList<HashMap<String, String>>();
    /**
     * 初始化反馈数据
     */
    /**
     * 用品设施
     */
    private ArrayList<HashMap<String, Object>> equipList = new ArrayList<HashMap<String, Object>>();
    /**
     * 时间周期
     */
    private ArrayList<HashMap<String, Object>> trainingList = new ArrayList<HashMap<String, Object>>();
    public void initFeedData() {
        if (null != recordNothingList)
            recordNothingList.clear();
        if (null != recordDisinfectionList)
            recordDisinfectionList.clear();
        if (null != recordWashHandsList)
            recordWashHandsList.clear();
        if (null != recordGloveList)
            recordGloveList.clear();
        for (int k = 0; k < list.size(); k++) {

            planListDb pdb = list.get(k);
            // { "4卫生手消毒", "5洗手", "6戴手套", "0未采取措施" };

            for (subTaskVo sv : pdb.getSubTasks()) {
                if (null == sv)
                    continue;
                HashMap<String, String> map = new HashMap<String, String>();
                // sv.setPname(pdb.getPname());//名称
                // sv.setPname(pdb.getPpostName());//岗位
                map.put("id", "" + k);
                map.put("job", pdb.getPpostName());
                map.put("job_type", pdb.getPpost());
                map.put("hos", pdb.getWorkName());
                map.put("hos_type", pdb.getWork_type());

                map.put("name", pdb.getPname());

                pdb.get_id();

                if (null != sv.getUnrules() && sv.getUnrules().size() > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < sv.getUnrules().size(); i++) {

                        if (!TextUtils
                                .isEmpty(sv.getUnrules().get(i).getName())) {
                            sb.append("\u2000"
                                    + sv.getUnrules().get(i).getName());
                        }
                    }
                    if (sb.length() > 0) {
                        map.put("reason", sb.toString());
                    }
                }
                map.put("type", sv.getResults());
                if (sv.getResults().equals("0")) {
                    recordNothingList.add(map);
                } else if (sv.getResults().equals("4")) {
                    recordWashHandsList.add(map);
                } else if (sv.getResults().equals("5")) {
                    recordDisinfectionList.add(map);
                } else if (sv.getResults().equals("6")) {
                    recordGloveList.add(map);
                }
            }
        }

    }


    /**
     * 跳转设置 intent 设置反馈数据
     */
    public void startFeedBack() {
        initFeedData();
        Intent intent = new Intent(mcontext, SupervisorFeedBackActivity.class);
        // 设置培训时间序号
        intent.putExtra(SupervisorFeedBackActivity.INTENT_TIMEINDEX, timeIndex);

        // 设置设施调查序号
        intent.putExtra(SupervisorFeedBackActivity.INTENT_REASONS, reasonIds);

        intent.putExtra(SupervisorFeedBackActivity.INTENT_REMARK, str_remark);
        intent.putExtra(SupervisorFeedBackActivity.INTENT_NANE, str_name);
        intent.putExtra(
                SupervisorFeedBackActivity.INTENT_RECORDALLLIST_DISINFECTION,
                recordDisinfectionList);
        intent.putExtra(
                SupervisorFeedBackActivity.INTENT_RECORDALLLIST_WASHHANDS,
                recordWashHandsList);
        intent.putExtra(SupervisorFeedBackActivity.INTENT_RECORDALLLIST_GLOVE,
                recordGloveList);
        intent.putExtra(
                SupervisorFeedBackActivity.INTENT_RECORDALLLIST_NOTHING,
                recordNothingList);

        intent.putExtra(SupervisorFeedBackActivity.INTENT_PARAMS_EQUIP,
                equipList);
        intent.putExtra(SupervisorFeedBackActivity.INTENT_PARAMS_TRAIN,
                trainingList);
        intent.putExtra(SupervisorFeedBackActivity.INTENT_ISWHO, tv.isWho());
        intent.putExtra(SupervisorFeedBackActivity.FEEDBACK_DEPARTMENT, tv.getIs_feedback_department());
        intent.putExtra(SupervisorFeedBackActivity.AGAIN_SUP, tv.getIs_again_supervisor());
        startActivityForResult(intent, 0x11);
    }


    // 反馈记录
    private String str_remark;
    // 被反馈对象
    private String str_name;
    // 培训时间序号
    int timeIndex = -1;
    // 已选择的设施调查ID
    ArrayList<Integer> reasonIds = new ArrayList<Integer>();
    /**
     * 设置数据
     */
    public void setFeedData(Intent data) {
        str_remark = data
                .getStringExtra(SupervisorFeedBackActivity.INTENT_REMARK);
        str_name = data.getStringExtra(SupervisorFeedBackActivity.INTENT_NANE);
        timeIndex = data.getIntExtra(
                SupervisorFeedBackActivity.INTENT_TIMEINDEX, -1);
        reasonIds = data
                .getIntegerArrayListExtra(SupervisorFeedBackActivity.INTENT_REASONS);
        ArrayList<String> reasons=data.getStringArrayListExtra(SupervisorFeedBackActivity.INTENT_REASONS_NAME);
        String reasonStr="";
        tv.setIs_training(timeIndex > 0 ? "1" : "0");
        tv.setTraining_recycle(timeIndex + "");
        tv.setIs_again_supervisor(data.getIntExtra(SupervisorFeedBackActivity.AGAIN_SUP,0));
        tv.setIs_feedback_department(data.getIntExtra(SupervisorFeedBackActivity.FEEDBACK_DEPARTMENT,0));
        if (null != reasonIds && reasonIds.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < reasonIds.size(); i++) {
                sb.append(reasonIds.get(i));
                reasonStr+=reasons.get(i);
                if (i < reasonIds.size() - 1) {
                    sb.append(",");
                    reasonStr+=",";
                }
            }
            tv.setEquip_examine(sb.toString());

        }



        String unrulesStr="洗手不规范记录:";
        tv.setCheck_content("手卫生调查");
        if(recordDisinfectionList.size()>0){
            for(Map map:recordDisinfectionList){
                try {
                    unrulesStr+=map.get("name").toString()+map.get("reason").toString();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }else{
            unrulesStr="";
        }
        String	xiaoduUnRules="卫生手消毒不规范记录:";
        if(recordWashHandsList.size()>0){
            for(Map map:recordWashHandsList){
                xiaoduUnRules+=map.get("reason").toString()+";";
            }
        }else{
            xiaoduUnRules="";
        }
        String isTraned=timeIndex>0?"是":"否";
        tv.setFeedback_obj(str_name);
        tv.setExist_problem("科室被反馈人:"+str_name+"\n"+unrulesStr+"\n"+xiaoduUnRules+"\n"+
                "是否培训过:"+isTraned+"\n"+
                "手卫生用品设施调查:"+reasonStr
        );
        tv.setDeal_suggest(str_remark);
        tv.setRemark(str_remark);
    }
    @Override
    public void onDestroy() {
        if(null!=departDialog&&departDialog.isShowing()){
            departDialog.dismiss();
        }
        super.onDestroy();
    }
}
