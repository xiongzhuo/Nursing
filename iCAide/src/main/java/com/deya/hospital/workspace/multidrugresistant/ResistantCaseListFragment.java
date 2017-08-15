package com.deya.hospital.workspace.multidrugresistant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.BaseTaskAadpter;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.supervisor.PartTimeStaffDialog;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.ComomDialog;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.ResistantMutiTextVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.priviewbase.CaseUtil;
import com.deya.hospital.workspace.priviewbase.MutiResitantCreatAdapter;
import com.deya.hospital.workspace.site_infection.SiteInfectionPriviewAcitivty;
import com.deya.hospital.workspace.threepips.ThreePipsListAdapter;
import com.deya.hospital.workspace.threepips.ThreePipsPriviewActivity;
import com.google.gson.Gson;

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
@SuppressLint({"ValidFragment"})
public class ResistantCaseListFragment extends BaseFragment implements View.OnClickListener {
    BaseTaskAadpter adapter;
    List<CaseListVo.CaseListBean> list = new ArrayList<CaseListVo.CaseListBean>();
    public ListView listView;
    public LinearLayout sumbmitlay;
    Button button, leftButton;
    private CommonTopView topView;
    View view;
    Context mcontext;
    Gson gson;
    Tools tools;
    String type_id;
    private MyHandler myHandler;
    private ResistantMutiTextVo rmtv, drugMtv;
    private PartTimeStaffDialog tipdialog;
    private ComomDialog deletDialog;
    String code = "";
    private LinearLayout newWorkView;
    TextView empertyText;


    public static ResistantCaseListFragment newInstance(String code) {
        ResistantCaseListFragment newFragment = new ResistantCaseListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        newFragment.setArguments(bundle);

        //bundle还可以在每个标签里传送数据


        return newFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        Bundle args = getArguments();
        if (view == null) {
            view = inflater.inflate(R.layout.lisview_bottom_button,
                    container, false);
            mcontext = getActivity();
            code = args.getString("code");
            if (code.equals("DN")) {
                type_id = "1";
            } else if (code.equals("SG")) {
                type_id = "2";
            } else if (code.equals("SIP")) {
                type_id = "4";
            }
            tools = new Tools(getActivity(), Constants.AC);
            gson = new Gson();
            initView();
            initMyHandler();
            onCreateData();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }

        //  onloadJsMethod();
        return view;
    }

    void initView() {
        listView = (ListView) view.findViewById(R.id.listView);
        sumbmitlay = (LinearLayout) view.findViewById(R.id.sumbmitlay);
        button = (Button) view.findViewById(R.id.sumbmitBtn);
        leftButton = (Button) view.findViewById(R.id.shareBtn);
        leftButton.setText("现场督察");
        newWorkView = (LinearLayout) view.findViewById(R.id.networkView);
        newWorkView.setVisibility(View.GONE);
        empertyText = (TextView) view.findViewById(R.id.empertyText);
    }


    public ListView getListView() {
        return listView;
    }

    public LinearLayout getSumbmitLay() {
        return sumbmitlay;
    }

    public Button getButton() {
        return button;
    }

    public Button getLeftButton() {
        return leftButton;
    }

    void onCreateData() {
        button = getButton();
        button.setOnClickListener(this);
        button.setText("添加病例");
        getLeftButton().setText("现场督察");
        getLeftButton().setOnClickListener(this);

        if (code.equals("DN")) {
            adapter = new MutiResitantCreatAdapter(mcontext, list);
        } else if (code.equals("SIP")) {
            adapter = new MutiResitantCreatAdapter(mcontext, list);
        } else if (code.equals("SG")) {
            adapter = new ThreePipsListAdapter(mcontext, list);
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CaseListVo.CaseListBean taskListBean = list.get(position);
                Intent in = new Intent();
                TaskVo taskVo = TaskUtils.onFindTaskByCaseDbId(taskListBean.getDbid());
                if (null == taskVo) {
                    taskVo = new TaskVo();
                    taskVo.setTaskListBean(TaskUtils.gson.toJson(taskListBean));
                }
                taskVo.setFinished(0);
                taskVo.setTask_id(taskListBean.getTask_id());
                int business_id = 0;
                if (taskListBean.getCase_id()>0) {
                    business_id =taskListBean.getCase_id();
                }
                taskVo.setId(business_id);
                taskVo.setDepartment(taskListBean.getDepartment_id());
                taskVo.setMission_time(taskListBean.getTime());
                taskVo.setDepartmentName(taskListBean.getDepartmentName());
                in.putExtra("data", taskVo);
                if (code.equals("DN")) {
                    in.setClass(mcontext, MutiRisitantPriviewActivity.class);
                } else if (code.equals("SG")) {
                    in.setClass(mcontext, ThreePipsPriviewActivity.class);
                } else if (code.equals("SIP")) {
                    in.setClass(mcontext, SiteInfectionPriviewAcitivty.class);
                }
                startActivity(in);

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                deletDialog = new ComomDialog(getActivity(), "确认删除此病例？",
                        R.style.SelectDialog, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletDialog.dismiss();
                        CaseListVo.CaseListBean bean = list.get(position);
                        if (bean.getTask_id() != 0) {
                            removeCase(bean.getTask_id() + "");
                            deletId = bean.getDbid();
                        } else {
                            CaseUtil.removeLoacalCase(bean.getDbid());
                            list.remove(position);
                            adapter.notifyDataSetChanged();

                        }
                    }
                });
                deletDialog.show();

                return true;
            }
        });
        getChache();

    }

    @Override
    public void onResume() {
        super.onResume();
        getCaseList();
    }

    @Override
    public void onDestroy() {
        if (null != deletDialog) {
            deletDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {


    }


    public void onAddData(CaseListVo.CaseListBean bean) {
        list.add(0, bean);
        adapter.notifyDataSetChanged();
    }

    private void getChache() {
        String str = SharedPreferencesUtil.getString(mcontext, "resistant_mutitext", "");
        if (AbStrUtil.isEmpty(str) && NetWorkUtils.isConnect(mcontext)) {
            getMutiTextData("CHECK_SPECIMEN");
            return;
        } else {
            try {
                setMutiTextResult(new JSONObject(str));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String str2 = SharedPreferencesUtil.getString(mcontext, "resistant_mutidrugtext", "");
        if (NetWorkUtils.isConnect(mcontext)) {
            getMutiTextData2("DRUG_RESISTANCE");
            return;
        } else {
            try {
                setDrugMutiTextResult(new JSONObject(str2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置不规则排序控件数据
     */
    public void getMutiTextData(String code) {   //CHECK_SPECIMEN   ： 送检标本DRUG_RESISTANCE  ： 耐药菌


        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("type", code);
            job.put("timeLong", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(), RisitantRequestCode.GRT_MUTITEXT_SUC,
                RisitantRequestCode.GRT_MUTITEXT_FAIL, job, "comm/queryStaticByCode");

    }

    public void getMutiTextData2(String code) {   //CHECK_SPECIMEN   ： 送检标本DRUG_RESISTANCE  ： 耐药菌


        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("type", code);
            job.put("timeLong", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(), RisitantRequestCode.GRT_DRUG_MUTITEXT_SUC,
                RisitantRequestCode.GRT_DRUG_MUTITEXT_FAIL, job, "comm/queryStaticByCode");

    }


    int deletId;

    public void removeCase(String caseId) {   //CHECK_SPECIMEN   ： 送检标本DRUG_RESISTANCE  ： 耐药菌


        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("task_id", caseId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(), RisitantRequestCode.REMOVE_CASE_SUCESS,
                RisitantRequestCode.REMOVE_CASE_FAIL, job, "comm/task/remove");

    }

    public void getCaseList() {   //CHECK_SPECIMEN   ： 送检标本DRUG_RESISTANCE  ： 耐药菌
        list.clear();
        List<CaseListVo.CaseListBean> loacalCase = CaseUtil.getlocalCaseById(mcontext, type_id);
        list.addAll(loacalCase);
        adapter.notifyDataSetChanged();

        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
            job.put("temp_type_id", type_id);
            job.put("status", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(), RisitantRequestCode.GET_CASES_SUC,
                RisitantRequestCode.GET_CASES_FAIL, job, "tempTask/caseList");

    }

    public void initMyHandler() {
        myHandler = new MyHandler(getActivity()) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = myHandler.mactivity.get();
                if (null != activity) {
                    switch (msg.what) {


                        case RisitantRequestCode.GRT_MUTITEXT_SUC:
                            if (null != msg && null != msg.obj) {
                                try {
                                    setMutiTextResult(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e5) {
                                    e5.printStackTrace();
                                }
                            }
                            break;
                        case RisitantRequestCode.GRT_DRUG_MUTITEXT_SUC:
                            if (null != msg && null != msg.obj) {
                                try {
                                    setDrugMutiTextResult(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e5) {
                                    e5.printStackTrace();
                                }
                            }
                            break;
                        case RisitantRequestCode.GET_CASES_SUC:
                            dismissdialog();
                            if (null != msg && null != msg.obj) {
                                try {
                                    setListData(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e5) {
                                    e5.printStackTrace();
                                }
                            }
                            break;
                        case RisitantRequestCode.GET_CASES_FAIL:
                            dismissdialog();
                            if (list.size() <= 0) {
                                newWorkView.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                                empertyText.setText("亲，您的网络不顺畅哦,\r\n请检查您的网络，再继续访问！");
                            } else {
                                newWorkView.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                            }
                            break;
                        case RisitantRequestCode.REMOVE_CASE_SUCESS:
                            CaseUtil.removeLoacalCase(deletId);
                            getCaseList();
                            break;
                        default:
                            break;
                    }
                }
            }
        };
    }

    private void setDrugMutiTextResult(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            drugMtv = TaskUtils.gson.fromJson(jsonObject.toString(), ResistantMutiTextVo.class);
            SharedPreferencesUtil.saveString(mcontext, "resistant_mutidrugtext", jsonObject.toString());
        } else {
            getMutiTextData("DRUG_RESISTANCE");
            ToastUtils.showToast(mcontext, rmtv.getResult_msg());
        }
    }

    private void setListData(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            CaseListVo rev = gson.fromJson(jsonObject.toString(), CaseListVo.class);
            CaseUtil.syscServiceCase(mcontext, rev.getCase_list());
            list.clear();
            List<CaseListVo.CaseListBean> loacalCase = CaseUtil.getlocalCaseById(mcontext, type_id);
//            for (CaseListVo.CaseListBean bean : loacalCase) {
//                if (!AbStrUtil.isEmpty(bean.getBusinessStr())) {
//                    bean.setBusiness(gson.fromJson(bean.getBusinessStr(), CaseListVo.CaseListBean.BusinessBean.class));
//                }
//            }
            list.addAll(loacalCase);

            if (list.size() <= 0) {
                newWorkView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                empertyText.setText("亲，没有未督导的病例,\r\n点全部查看所有已督导的记录！");
            } else {
                newWorkView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void setMutiTextResult(JSONObject jsonObject) {

        if (jsonObject.optString("result_id").equals("0")) {
            rmtv = TaskUtils.gson.fromJson(jsonObject.toString(), ResistantMutiTextVo.class);
            SharedPreferencesUtil.saveString(mcontext, "resistant_mutitext", jsonObject.toString());
        } else {
            getMutiTextData("CHECK_SPECIMEN");
            ToastUtils.showToast(mcontext, rmtv.getResult_msg());
        }

    }
}
