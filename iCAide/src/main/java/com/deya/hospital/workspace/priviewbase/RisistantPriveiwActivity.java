package com.deya.hospital.workspace.priviewbase;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.deya.acaide.R;
import com.deya.hospital.quality.QualityPriviewActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.EventManager;
import com.deya.hospital.util.HorizontalListView;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.IdAndResultsVo;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.threepips.TabPagerAdapter;
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
public abstract class RisistantPriveiwActivity extends BasePriviewActivity {
    RadioButton rb1, rb2;
    List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
    public BasePriviewInfoFragment basePriviewInfoFragment;
    public TabPagerAdapter tabPagerAdapter;
    public String temp_type = "";//获取模版的参数


    public abstract String getTaskType();

    public abstract String getCacheForm();

    public abstract BasePriviewInfoFragment getInfoFragment(CaseListVo.CaseListBean bean);

    @Override
    public void onFileNumChanged(int num) {
        if (null == tabPagerAdapter) {
            return;
        }
        tabPagerAdapter.setPagerFragmentTex(num == 0 ? "" : num + "");
    }


    @Override
    public void toAddCache() {
        taskInfoBean.setSupervisor_question(questionVo);
        taskInfoBean.setBaseInfo(caseVo);
        taskVo.setType(getTaskType());
        taskVo.setDepartment(caseVo.getDepartment_id());
        taskVo.setDepartmentName(caseVo.getDepartmentName());
        taskVo.setMission_time(caseVo.getTime());
        taskVo.setMobile(tools.getValue(Constants.MOBILE));
        taskVo.setTaskListBean(TaskUtils.gson.toJson(taskInfoBean));
        taskVo.setStatus(2);
        taskVo.setTitle(taskVo.getDepartmentName() + ":" + taskInfoBean.getTemp_list().getTitle());
        if (taskVo.getDbid() <= 0) {
            TaskUtils.onAddTaskInDb(taskVo);
        } else {
            TaskUtils.onUpdateTaskById(taskVo);
        }
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
            try {
                rv = TaskUtils.gson.fromJson(jsonObject.toString(), RisistantVo.class);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(getLocalClassName(), e.toString());
            }
            if (getIntent().hasExtra("tempId")) {
                rv.getTask_info().setTempId(getIntent().getIntExtra("tempId", 0));
            }
            taskInfoBean = rv.getTask_info();
            this.caseVo = rv.getTask_info().getBaseInfo();
            caseVo.setDepartmentName(taskInfoBean.getDepartmentName());
            if (taskVo.getDbid() > 0 || taskVo.getTask_id() > 0) {
                resetData(false);
            } else {
                resetData(true);
            }
            if (!isPriview) {
                questionVo = rv.getTask_info().getSupervisor_question();

                spvisorFragment = SupvisorFragment.newInstance(questionVo, rv);
                listfragment.add(spvisorFragment);
            }
            caseVo.setTemp_type_id(rv.getTemp_type_id());
            EventManager.getInstance().notify(taskInfoBean.getDepartmentName(), BasePriviewInfoFragment.DEPART_CHANGE);

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
            CaseListVo.CaseListBean caseListBean = TaskUtils.gson.fromJson(jsonObject.toString(), CaseListVo.CaseListBean.class);
            if (null == caseVo || caseVo.getStatus() == 1) {
                this.caseVo = caseListBean;
            }
        } else {
            rv = new RisistantVo();
            this.caseVo = new CaseListVo.CaseListBean();
        }

        resetData(getdefultState());
        if (!isPriview) {
            questionVo = new RisistantVo.TaskInfoBean.SupervisorQuestionBean();

            spvisorFragment = SupvisorFragment.newInstance(questionVo, rv);
            listfragment.add(spvisorFragment);
            myadapter.notifyDataSetChanged();
            fragmentPager.setCurrentItem(0);
        }
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
                        for (RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean : rti.getChildren()) {
                            Log.e("1111111111", childrenBean.getIs_yes());
                            if (!AbStrUtil.isEmpty(childrenBean.getIs_yes())) {
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

    public abstract void initChildForm(RisistantVo rv, CaseListVo.CaseListBean businessBean);

    void intTab() {
        try {
            tabGv = (HorizontalListView) this.findViewById(R.id.tabGv);
            if (isPriview) {
                tabGv.setVisibility(View.GONE);
            }
            List<RisistantVo.TaskInfoBean.TempListBean> tablist = new ArrayList<>();
            if (null != rv.getTask_info().getTemp_list()) {
                tablist.add(rv.getTask_info().getTemp_list());
            }

            RisistantVo.TaskInfoBean.TempListBean tempListBean = new RisistantVo.TaskInfoBean.TempListBean();
            tempListBean.setTemp_type_id(0);
            tablist.add(0, tempListBean);
            initTab(tablist);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initTab(List<RisistantVo.TaskInfoBean.TempListBean> tablist) {
        if (null == tabGv) {
            tabGv = (HorizontalListView) this.findViewById(R.id.tabGv);
        }
        tabPagerAdapter = new TabPagerAdapter(mcontext, tablist);
        tabPagerAdapter.setCheckInter(new TabPagerAdapter.onCheckInter() {
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
        rv = new RisistantVo();
        if (getIntent().hasExtra("data")) {
            taskVo = (TaskVo) getIntent().getSerializableExtra("data");
            if (null == taskVo) {
                taskVo = new TaskVo();
            }
            if (!AbStrUtil.isEmpty(taskVo.getTaskListBean())) {
                taskInfoBean = TaskUtils.gson.fromJson(taskVo.getTaskListBean(), RisistantVo.TaskInfoBean.class);
            } else {
                taskInfoBean = new RisistantVo.TaskInfoBean();
            }

            if (isPriview) {
                if (taskInfoBean.getIs_save() == 1) {
                    sumbmitBtn.setText("已保存到本院");
                    sumbmitBtn.setEnabled(false);
                } else {
                    sumbmitBtn.setText("保存到本院");
                }
            }
            rv.setTask_info(taskInfoBean);
        } else {
            taskVo = new TaskVo();
            taskInfoBean = new RisistantVo.TaskInfoBean();
            rv.setTask_info(taskInfoBean);
        }
        taskVo.setType(getTaskType());

        isDetail = taskVo.getTask_id() > 0&&taskVo.getStatus()==0&&!getIntent().hasExtra("isEditor");
        if (isDetail) {
            shareBtn.setVisibility(View.VISIBLE);
            topView.setRigtext("");
            topView.setTitle("预览");
            sumbmitBtn.setText("修改");
            sumbmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent commonIntent = new Intent(mcontext, QualityPriviewActivity.class);
                    commonIntent.putExtra("data", taskVo);
                    commonIntent.putExtra("isEditor",true);
                    commonIntent.putExtra("title",getIntent().getStringExtra("title"));
                    startActivity(commonIntent);
                    finish();
                }
            });
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


        topView.setTileRightImgVisble(View.GONE);
        caseVo = taskInfoBean.getBaseInfo();

        if (taskInfoBean.getTemp_list().getItem_list().size() > 0) {
            if (null != taskInfoBean.getSupervisor_question()) {
                resetData(false);

            }
            if (!isPriview) {
                questionVo = rv.getTask_info().getSupervisor_question();
                spvisorFragment = SupvisorFragment.newInstance(questionVo, rv);
                listfragment.add(spvisorFragment);
            }
            intTab();
            myadapter.notifyDataSetChanged();
            fragmentPager.setCurrentItem(1);

        } else {
            if (caseVo.getStatus() == 0) {
                questionVo = new RisistantVo.TaskInfoBean.SupervisorQuestionBean();
                spvisorFragment = SupvisorFragment.newInstance(questionVo, rv);
            }

            if (taskVo.getTask_id() <= 0) {
                setNewForm(getCacheForm());//取新模板
                myadapter.notifyDataSetChanged();
            } else {
                getDetailData(taskVo.getTask_id() + "");
            }
        }

    }


    private void SaveCache(int state) {
        taskVo.setHospital(tools.getValue(Constants.HOSPITAL_ID));
        taskVo.setMobile(tools.getValue(Constants.MOBILE));
        taskVo.setType(getTaskType());
        if (caseVo == null) {
            finish();
            return;
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
        taskVo.setTaskListBean(TaskUtils.gson.toJson(taskInfoBean));
        if (AbStrUtil.isEmpty(taskVo.getMission_time())) {
            taskVo.setMission_time(TaskUtils.getLoacalDate());
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
                sumbmitBtn.setEnabled(false);
                starUpload();
                break;

            case R.id.shareBtn:
                 ToastUtil.showMessage("抱歉！分享页面正在加紧开发中，请稍等几天！");
                break;


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