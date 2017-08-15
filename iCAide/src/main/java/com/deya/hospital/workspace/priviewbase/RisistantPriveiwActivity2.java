package com.deya.hospital.workspace.priviewbase;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.HorizontalListView;
import com.deya.hospital.vo.CaseListVo2;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.IdAndResultsVo;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.WorkSpaceFragment;
import com.deya.hospital.workspace.threepips.TabPagerAdapter2;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public abstract class RisistantPriveiwActivity2 extends BasePriviewActivityEvro {
    RadioButton rb1, rb2;
    List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
    public BasePriviewInfoFragment2 basePriviewInfoFragment;
    public TabPagerAdapter2 tabPagerAdapter;
    public String temp_type = "";//获取模版的参数


    public abstract String getTaskType();

    public abstract String getCacheForm();

    public abstract BasePriviewInfoFragment2 getInfoFragment(CaseListVo2.CaseListBean bean);

    @Override
    public void onFileNumChanged(int num) {
        if (null == tabPagerAdapter) {
            return;
        }
        tabPagerAdapter.setPagerFragmentTex(num == 0 ? "" : num + "");
    }


    @Override
    public void toAddCache() {
        SaveCache(2);
        Intent brodcastIntent = new Intent();
        brodcastIntent
                .setAction(WorkSpaceFragment.UPDATA_ACTION);
        sendBroadcast(brodcastIntent);
        finish();
    }

    @Override
    public void oncheckChange(int id) {
        switch (id) {
            case R.id.rb1:
                fragmentPager.setCurrentItem(0);
                break;
            case R.id.rb2:

                fragmentPager.setCurrentItem(1);

                break;
        }

    }

    @Override
    public void onCheckAll(boolean ischeck) {
//        tools.putValue(IS_ALL_RIGHT, ischeck ? "1" : "0");
//        setAllCheck(ischeck);
    }

    @Override
    public void setDataResult(JSONObject jsonObject) {
        Log.i("sendTask", jsonObject.toString());

    }

    @Override
    public void onGetSupDetailSucess(JSONObject jsonObject) {
//        if (jsonObject.optString("result_id").equals("0")) {
//            JSONObject job = jsonObject.optJSONObject("info");
//            if (null == job) {
//                return;
//            }
//            TaskVo sv = TaskUtils.gson.fromJson(job.toString(), TaskVo.class);
//            if (null == sv) {
//
//                return;
//            }
//            spvisorFragment = SupvisorFragment.newInstance(sv, rv);
//            listfragment.add(spvisorFragment);
//            myadapter.notifyDataSetChanged();
//
//
//        } else {
//            ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
//        }
    }


    @Override
    public void getDataSucess(JSONObject jsonObject) {

        if (!jsonObject.optString("result_id").equals("0")) {
            ToastUtil.showMessage(jsonObject.optString("result_msg"));
            return;
        }
        if (!AbStrUtil.isEmpty(jsonObject.toString())) {
            rv = TaskUtils.gson.fromJson(jsonObject.optJSONObject("task_info").toString(), RisistantVo.class);
            if (null == rv) {
                rv = new RisistantVo();
            }
            JSONObject jsonInfo = jsonObject.optJSONObject("task_info");
            this.caseVo = TaskUtils.gson.fromJson(jsonInfo.optJSONObject("case_info").toString(), CaseListVo2.CaseListBean.class);
            if (taskVo.getDbid() > 0 || taskVo.getTask_id() > 0) {
                resetData(false);
            } else {
                resetData(true);
            }
            if (jsonInfo.has("supervisor_question")) {
                questionVo = TaskUtils.gson.fromJson(jsonInfo.optJSONObject("supervisor_question").toString(), RisistantVo.TaskInfoBean.SupervisorQuestionBean.class);
            } else {
                questionVo=new RisistantVo.TaskInfoBean.SupervisorQuestionBean();

            }
            spvisorFragment = (SupvisorFragment2) SupvisorFragment2.newInstance(questionVo, rv);
            listfragment.add(spvisorFragment);
            caseVo.setTemp_type_id(rv.getTemp_type_id());

        }
        myadapter.notifyDataSetChanged();
        fragmentPager.setCurrentItem(1);


    }

    /**
     * @param jsonObject 非详情数据模版
     */
    public void setNewForm(String jsonObject) {
        listfragment.clear();
        myadapter.notifyDataSetChanged();
        if (!AbStrUtil.isEmpty(jsonObject.toString())) {
            rv = TaskUtils.gson.fromJson(jsonObject.toString(), RisistantVo.class);
            CaseListVo2.CaseListBean caseListBean = TaskUtils.gson.fromJson(jsonObject.toString(), CaseListVo2.CaseListBean.class);
            if (null == caseVo || caseVo.getStatus() == 1) {
                this.caseVo = caseListBean;
            }
        } else {
            rv = new RisistantVo();
            this.caseVo = new CaseListVo2.CaseListBean();
        }

        resetData(getdefultState());
        questionVo=new RisistantVo.TaskInfoBean.SupervisorQuestionBean();
        spvisorFragment = (SupvisorFragment2) SupvisorFragment2.newInstance(questionVo, rv);
        listfragment.add(spvisorFragment);
        myadapter.notifyDataSetChanged();
        fragmentPager.setCurrentItem(0);
    }

    public void setAllCheck(boolean allCheck) {
//        if (allCheck) {
//            setData();
//        } else {
//            for (RisistantVo.TempListBean rt : rv.getTemp_list()) {
//                if (rt == null) {
//                    continue;
//                }
//                IdAndResultsVo idAndResultsVo = new IdAndResultsVo();
//                idAndResultsVo.setResult(new ArrayList<IdAndResultsVo.ItemBean>());
//                idAndResultsVo.setTemp_id(rt.getId() + "");
//                for (RisistantVo.TempListBean.ItemListBean rti : rt.getItem_list()) {
//
//                    switch (rti.getType()) {
//                        case 2:
//                            rti.getChildren().get(0).setResult("0");
//                            break;
//                        case 4:
//                            rti.getChildren().get(0).setResult("0");
//                            break;
//                        default:
//                            break;
//
//                    }
//                }
//            }
        //}
     //   myadapter.notifyDataSetChanged();
    }

    private void setData() {
        for (RisistantVo.TempListBean rt : rv.getTemp_list()) {
            if (rt == null) {
                continue;
            }
            IdAndResultsVo idAndResultsVo = new IdAndResultsVo();
            idAndResultsVo.setResult(new ArrayList<IdAndResultsVo.ItemBean>());
            idAndResultsVo.setTemp_id(rt.getId() + "");
            for (RisistantVo.TempListBean.ItemListBean rti : rt.getItem_list()) {

                switch (rti.getType()) {
                    case 2:
                    case 4:
                    case 5:
                    case 6:
                        for(RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean:rti.getChildren()){
                            Log.e("1111111111",childrenBean.getIs_yes());
                            if(!AbStrUtil.isEmpty(childrenBean.getIs_yes())){
                                    childrenBean.setResult(childrenBean.getIs_yes());
                            }
                        }
                        break;
                    default:
                        break;

                }
            }
        }
    }

    private void resetData(boolean reset) {
        if (reset) {
            setData();
        }
        initChildForm(rv, this.caseVo);
    }

    public abstract void initChildForm(RisistantVo rv, CaseListVo2.CaseListBean businessBean);

    void intTab() {
        try {
            tabGv = (HorizontalListView) this.findViewById(R.id.tabGv);
            List<RisistantVo.TempListBean> tablist = new ArrayList<>();
            if (rv != null && rv.getTemp_list() != null && rv.getTemp_list().size() > 0) {
                for (RisistantVo.TempListBean rt : rv.getTemp_list()) {
                    if (null != rt && null != rv.getTemp_list() && rv.getTemp_list().size() > 0) {
                        tablist.add(rt);
                    }

                }
            }
            RisistantVo.TempListBean tempListBean = new RisistantVo.TempListBean();
            tempListBean.setTemp_type_id(0);
            tablist.add(0, tempListBean);
            initTab(tablist);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initTab(List<RisistantVo.TempListBean> tablist) {
        if (null == tabGv) {
            tabGv = (HorizontalListView) this.findViewById(R.id.tabGv);
        }
        tabPagerAdapter = new TabPagerAdapter2(mcontext, tablist);
        tabPagerAdapter.setCheckInter(new TabPagerAdapter2.onCheckInter() {
            @Override
            public void onCheck(int position) {
                fragmentPager.setCurrentItem(position);
                tabPagerAdapter.setCheckPOs(position);

            }
        });
        tabGv.setAdapter(tabPagerAdapter);
    }

    @Override
    public void initView() {
        if (getIntent().hasExtra("data")) {
            taskVo = (TaskVo) getIntent().getSerializableExtra("data");
            if (!AbStrUtil.isEmpty(taskVo.getTaskListBean())) {
                caseVo = TaskUtils.gson.fromJson(taskVo.getTaskListBean(), CaseListVo2.CaseListBean.class);
            }
        } else {
            taskVo = new TaskVo();
        }
        taskVo.setType(getTaskType());

        if (null == caseVo) {
            caseVo = new CaseListVo2.CaseListBean();
        }
        TaskUtils.getDepartList(mcontext, departlist);
        rb1 = (RadioButton) this.findViewById(R.id.rb1);
        fragmentPager.setCurrentItem(1);
        fragmentPager.setOffscreenPageLimit(10);
        fragmentPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                tabPagerAdapter.setCheckPOs(arg0);
                if (arg0 >= tabPagerAdapter.getCount() / 2) {
                    tabGv.scrollTo(10000);
                } else {
                    tabGv.scrollTo(0);
                }
                if (arg0 == tabPagerAdapter.getCount() - 1) {
                    spvisorFragment.setCheckContent();
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        if(!AbStrUtil.isEmpty(getTopTitle())){
        topView.setTitle(getTopTitle());
        }
        topView.setTileRightImgVisble(View.GONE);
        shareBtn.setVisibility(taskVo.getTask_id() > 0 ? View.VISIBLE : View.GONE);

        if (caseVo.getUploadStatus() == 3 && !AbStrUtil.isEmpty(taskVo.getTaskListBean())) {//病例缓存数据
            basePriviewInfoFragment = getInfoFragment(this.caseVo);
            listfragment.add(basePriviewInfoFragment);
            myadapter.notifyDataSetChanged();
            if (null != caseVo.getRisistantVo()) {
                rv = caseVo.getRisistantVo();
                if (rv.getTemp_list().size() > 0) {
                    for (RisistantVo.TempListBean rt : rv.getTemp_list()) {
                        if (null != rt && null != rv.getTemp_list() && rv.getTemp_list().size() > 0) {
                            BaseSupvisorFragment2 baseSupvisorFragment = BaseSupvisorFragment2.newInstance(rt.getItem_list());
                            listfragment.add(baseSupvisorFragment);
                        }
                    }
                }
            } else {
                rv = new RisistantVo();
            }

                questionVo=new RisistantVo.TaskInfoBean.SupervisorQuestionBean();
                spvisorFragment = SupvisorFragment2.newInstance(questionVo, rv);
                listfragment.add(spvisorFragment);

            intTab();
            myadapter.notifyDataSetChanged();
            fragmentPager.setCurrentItem(1);

        } else {
            if (caseVo.getStatus() == 0) {
                questionVo=new RisistantVo.TaskInfoBean.SupervisorQuestionBean();
                spvisorFragment = SupvisorFragment2.newInstance(questionVo, rv);
            }

            if (taskVo.getTask_id() <= 0) {
                setNewForm(getCacheForm());//取新模板
                myadapter.notifyDataSetChanged();
            } else {
                getDetailData(taskVo.getTask_id() + "");
            }
        }
        topView.onbackClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskVo.getTask_id() > 0||taskVo.getDbid()>0) {
                    finish();
                } else {
                    showBaseTipsDialog();
                }

            }
        });
    }

    protected abstract String getTopTitle();

    private void SaveCache(int state) {
        taskVo.setHospital(tools.getValue(Constants.HOSPITAL_ID));
        taskVo.setMobile(tools.getValue(Constants.MOBILE));
        taskVo.setType(getTaskType());
        if (caseVo == null) {
            finish();
            return;
        }
        if (null != spvisorFragment) {
            caseVo.setSupervisor_question(spvisorFragment.getMainRemark());
        }

        if (rv == null || rv.getTemp_list() == null || rv.getTemp_list().size() <= 0) {//无模版内容（加载失败）不缓存
            finish();
            return;
        }
        basePriviewInfoFragment.getData();
        String string = TaskUtils.gson.toJson(this.caseVo);
        // caseVo.setBusinessStr(string);
        caseVo.setRisistantVo(rv);
        caseVo.setUploadStatus(3);
        if (!AbStrUtil.isEmpty(this.caseVo.getDepartment_id())) {
            taskVo.setDepartmentName(this.caseVo.getDepartmentName());
            taskVo.setDepartment(this.caseVo.getDepartment_id());
        } else {
            finish();
            return;
        }
        CaseUtil.updateCaseInDb(caseVo);
        taskVo.setTaskListBean(TaskUtils.gson.toJson(caseVo));
        if (!AbStrUtil.isEmpty(caseVo.getTime())) {
            taskVo.setMission_time(caseVo.getTime());
        }

        taskVo.setCaseDbId(caseVo.getDbid());//在任务表里面挂的caseid //以便通过case表查询任务
        if (taskVo.getDbid() == 0) {
            taskVo.setStatus(state);
            TaskUtils.onAddTaskInDb(taskVo);
        } else {
            TaskUtils.onUpdateTaskById(taskVo);
        }
    }


    @Override
    public void doSumbmit() {
        basePriviewInfoFragment.getData();
        if (AbStrUtil.isEmpty(this.caseVo.getDepartment_id())) {
            ToastUtil.showMessage("请选择科室");
            fragmentPager.setCurrentItem(0);
            return;
        }
        starUpload();
    }

    @Override
    public void toAddFeedBack() {
        fragmentPager.setCurrentItem(listfragment.size() - 1);

    }

    @Override
    public void initFragmentList() {
        listfragment = new ArrayList<>();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sumbmitBtn:
                questionVo = spvisorFragment.getMainRemark();
                if( AbStrUtil.isEmpty(spvisorFragment.getMainRemark().getCheck_content())){
                    questionVo.setCheck_content(rv.getTask_info().getTemp_list().getTitle());
                }
                if (null == basePriviewInfoFragment) {
                    return;
                }
                basePriviewInfoFragment.getData();
                if (AbStrUtil.isEmpty(this.caseVo.getDepartment_id())) {
                    ToastUtil.showMessage("请选择科室");
                    fragmentPager.setCurrentItem(0);
                    return;
                }
                if (spvisorFragment.getMainRemark().getDeal_suggest().trim().equals("") && fragmentPager.getCurrentItem() != listfragment.size() - 1) {
                    tipsDialog.show();
                    return;
                }
                showprocessdialog();
                starUpload();
                break;

            case R.id.shareBtn:
                showShare(taskVo, spvisorFragment.getMainRemark().getCheck_content());
                break;


        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if (taskVo.getTask_id() > 0||taskVo.getDbid()>0) {
                return super.onKeyDown(keyCode, event);
            }else{
               showBaseTipsDialog();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void AddImgFile(String name) {
        Log.e("risitant", name);
        spvisorFragment.addImg(name);
    }

    @Override
    public void AddRecordFile(String name, double totalTime) {
        spvisorFragment.addRecord(name, totalTime + "");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }


}