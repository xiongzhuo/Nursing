package com.deya.hospital.workspace.stastic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.HotVo;
import com.deya.hospital.vo.StasticGroupVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/9/20
 */
public class StasticTypeListragment extends BaseFragment{
    List<StasticGroupVo> list = new ArrayList<>();
    ExpandableListView listView;
    StasticTypesAdapter adapter;
    int[] taskTypeImgs = {R.drawable.task_pop_handwash};
    String  []taskTypeTitles={"WHO手卫生观察"};
    int[] imgs1 = {R.drawable.job_type_time, R.drawable.years_stastic,
            R.drawable.zhizheng_supervious, R.drawable.depart_supvious,
            R.drawable.menber_supervios, R.drawable.job_supvious,
            R.drawable.depart_yc, R.drawable.menber_yc, R.drawable.job_yc};
    int[] imgs2 = {R.drawable.job_type_time, R.drawable.years_stastic};
    String titles1[] = {"岗位-任务统计", "时机-任务统计", "指征-任务统计", "时机—科室依从", "时机-职员依从",
            "时机-岗位依从", "指征-科室依从", "指征-职员依从", "指征-岗位依从"};
    String titles2[] = {"考核统计", "问题汇总"};
    Tools tools;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_stastic_types, container,
                    false);
            tools=new Tools(getActivity(),Constants.AC);
            initData();
            initView();

        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }


    private void initData() {
        for (int j=0;j<taskTypeTitles.length;j++){
            StasticGroupVo groupVo = new StasticGroupVo();
            groupVo.setTitle(taskTypeTitles[j]);
            groupVo.setImgId(taskTypeImgs[j]);
            switch (j){
                case 0:
                    for (int i = 0; i < titles1.length; i++) {
                        HotVo hv = new HotVo();
                        hv.setImgid(imgs1[i]);
                        hv.setName(titles1[i]);
                        groupVo.getList().add(hv);
                    }
                    break;
                case 1:
                    for (int i = 0; i < titles2.length; i++) {
                        HotVo hv = new HotVo();
                        hv.setImgid(imgs2[i]);
                        hv.setName(titles2[i]);
                        groupVo.getList().add(hv);
                    }
                    break;
                default:
                    break;

            }
            list.add(groupVo);
        }
    }

    private void initView() {
        listView = (ExpandableListView) rootView.findViewById(R.id.listView);
        listView.setGroupIndicator(null);
        adapter = new StasticTypesAdapter(getActivity(), list, new StasticTypesAdapter.onItemsClick() {
            @Override
            public void onItemsClick(int groupPos, int childPos) {
                Intent intent = new Intent(getActivity(),
                        FormReportWebView.class);
                intent.putExtra("keyList",(Serializable) list.get(groupPos).getList());
                switch (groupPos){
                    case 0:

                        intent.putExtra("psoition", childPos);
                            intent.putExtra(
                                    "url",
                                    WebUrl.LEFT_URL+"/reportCounts/reportNew?type="+(childPos+1)+"&authent="+getBase64Authent());
                        intent.putExtra("webtitle", titles1[childPos]);
                        intent.putExtra("reportType", 1);

                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("webtitle", titles2[childPos]);
                        intent.putExtra("url",
                                childPos == 0 ? WebUrl.WEB_FORM_COUNT
                                        : WebUrl.WEB_QUESTION_COUNT);
                        intent.putExtra("shareMethod",
                                childPos == 0 ? "/gkgzj/gridCountShare.html?u="
                                        : "/gkgzj/gridSupervisorShare.html?u=");
                        startActivity(intent);
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }

            }
        });
        listView.setAdapter(adapter);
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        listView.collapseGroup(i);
                    }
                }
            }
        });
        listView.expandGroup(0);
    }

    public String getBase64Authent() {
        return AbStrUtil.getBase64(tools.getValue(Constants.AUTHENT));
    }
}