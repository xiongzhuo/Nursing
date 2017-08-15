package com.deya.hospital.workspace.workspacemain;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.workspace.BootomSelectDialog;
import com.deya.hospital.workspace.TaskUtils;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.ChattingFragment;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class WorkClassifyFragment extends BaseFragment {
    GridView gridView;
    WorkClassifyAdapter adapter;
    String localToday;
    int[] imgs1 = {R.drawable.work_class1, R.drawable.work_class2,
            R.drawable.work_class3, R.drawable.work_class4,
            R.drawable.work_class5, R.drawable.work_class6,
            R.drawable.work_class7, R.drawable.work_class8,R.drawable.classify_wast,R.drawable.work_risk_assessment,R.drawable.classify_more};
    String titles1[] = {"通用\n督导本", "质量\n控制", "手卫生", "环境\n物表\n清洁", "多重\n耐药",
            "安全\n注射", "三管\n监测", "手术\n部位\n感染","医疗\n废物","风险\n评估","更多"};
    private BootomSelectDialog handDilog;
    int type;

    public static WorkClassifyFragment newInstance(int type) {
        WorkClassifyFragment workClassifyFragment = new WorkClassifyFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        workClassifyFragment.setArguments(bundle);
        return workClassifyFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_work_classify, container,
                    false);
            Bundle bundle = getArguments();
            if (null != bundle) {
                type = bundle.getInt("type", 0);
            }
            initView();
            initHandDialog();

        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }
    private void initView() {

        localToday = TaskUtils.getLoacalDate();
        gridView = (GridView) this.findViewById(R.id.gv);
        adapter = new WorkClassifyAdapter(getActivity(), titles1, imgs1);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 2) {
                    Intent intent = new Intent(getActivity(), ChattingActivity.class);
                    intent.putExtra(ChattingFragment.RECIPIENTS, "29");
                    intent.putExtra(ChattingFragment.CONTACT_USER, "感控小助手");
                    intent.putExtra(ChattingFragment.CUSTOMER_SERVICE, false);
                    intent.putExtra(ChattingFragment.CONTACTS_TYPE, 1);
                    String str=titles1[position].replaceAll("\n", "");
                    intent.putExtra(ChattingFragment.DEFULT_REPORT_MESSAGE,"您查看了"+str+"报告");
                    startActivity(intent);
                } else {
                    switch (position) {
                        case 0:
                            addTask(2);
                            break;
                        case 1:
                            addTask(3);
                            break;
                        case 2:
                            handDilog.show();
                            break;
                        case 3:
                            addTask(10);
                            break;
                        case 4:
                            addTask(8);
                            break;
                        case 5:
                            addTask(11);
                            break;
                        case 6:
                            addTask(9);
                            break;
                        case 7:
                            addTask(12);
                            break;
                        case 8:
                            addTask(13);
                            break;
                        case 10:
                            addTask(14);
                            break;
                        case 9:
                            addTask(3);
                            break;
                    }
                }
            }
        });

    }

    public void addTask(int type) {
        TaskUtils.addTask(getActivity(), type, localToday);
    }

    public void initHandDialog() {
        String titles1[] = {"WHO手卫生观察", "外科手卫生操作考核",
                "手消毒消耗量"};
        handDilog = new BootomSelectDialog(getActivity(), titles1,
                new BootomSelectDialog.BottomDialogInter() {

                    @Override
                    public void onClick3() {
                        addTask(4);
                    }

                    @Override
                    public void onClick2() {
                        addTask(5);
                    }

                    @Override
                    public void onClick1() {
                        addTask(1);
                    }

                    @Override
                    public void onClick4() {


                    }
                });
    }

    @Override
    public void onDestroy() {
        if (handDilog != null && handDilog.isShowing()) {
            handDilog.dismiss();
        }
        super.onDestroy();
    }
}
