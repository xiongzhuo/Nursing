package com.deya.hospital.workspace.priviewbase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.quality.QualityPriviewActivity;
import com.deya.hospital.quality.SDPriviewPop;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.EventManager;
import com.deya.hospital.vo.ClassifyVo;
import com.deya.hospital.vo.IdAndResultsVo;
import com.deya.hospital.vo.RisistantVo;
import com.im.sdk.dy.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
@SuppressLint({"ValidFragment"})
public class BaseSupvisorFragment extends BaseFragment implements View.OnClickListener {
    ListView formListView;
    View rootView;
    List<RisistantVo.TempListBean.ItemListBean> list;
    ResitantPriviewAadapter adapter;
    private SDPriviewPop sdPriviewPop;
   TextView formName ,totalScoreTv ,departName;
    RisistantVo.TaskInfoBean taskInfoBean;
    public   static BaseSupvisorFragment newInstance(RisistantVo.TaskInfoBean taskInfoBean) {
        BaseSupvisorFragment newFragment = new BaseSupvisorFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", taskInfoBean);
        newFragment.setArguments(bundle);

        //bundle还可以在每个标签里传送数据


        return newFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_risitant_ducha, container,
                    false);
            init(args);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }


    public void init(Bundle args) {
        taskInfoBean= (RisistantVo.TaskInfoBean) args.getSerializable("data");
        list = taskInfoBean.getTemp_list().getItem_list();
        formListView = (ListView) rootView.findViewById(R.id.formListView);
        adapter = new ResitantPriviewAadapter(getActivity(), list);
        formName= (TextView) rootView.findViewById(R.id.formName);
        formName.setText(taskInfoBean.getTemp_list().getTitle());
        departName= (TextView) rootView.findViewById(R.id.departName);
        departName.setText(taskInfoBean.getDepartmentName());
        totalScoreTv= (TextView) rootView.findViewById(R.id.totalScoreTv);
        adapter.setChildPopLis(new ResitantPriviewAadapter.OnPopChildInter() {
            @Override
            public void OnPop(RisistantVo.TempListBean.ItemListBean chi) {
                if(QualityPriviewActivity.isDetail){
                    return;
                }
                sdPriviewPop = new SDPriviewPop(getActivity());
                sdPriviewPop.showPOp(chi);
                sdPriviewPop.setDialogInterface(new SDPriviewPop.SumbmitInter() {
                    @Override
                    public void onsumbmit() {
                        adapter.notifyDataSetChanged();
                        sendBordCast();
                        setTask();
                    }
                });
            }
        });
        setTask();
        formListView.setAdapter(adapter);
        initData();
        if(!QualityPriviewActivity.isDetail){
            rootView.findViewById(R.id.tongjiLay).setVisibility(View.GONE);
        }
    }

    EventManager.OnNotifyListener onNotifyListener;
    ClassifyVo classifyVo=new ClassifyVo();
    public List<IdAndResultsVo.ItemBean> resultlist = new ArrayList<>();
    public void setTask() {
        resultlist.clear();
        int wrongNum=0;
        int rightNum = 0;
        int toalScore = 0;
        int undonum=0;
        for (RisistantVo.TempListBean.ItemListBean rti :list) {
            double score=Double.parseDouble(rti.getScore());

            double itemScore;
            if (AbStrUtil.isEmpty(rti.getItem_score())) {
                itemScore = 0;
            } else {
                itemScore = Double.parseDouble(rti.getItem_score());
            }
            toalScore+=score + itemScore;

        }

        onNotifyListener =new EventManager.OnNotifyListener() {
            @Override
            public void onNotify(Object object, String eventTag) {
                switch (eventTag){
                    case BasePriviewInfoFragment.DEPART_CHANGE:
                        if(null!=departName){
                        departName.setText(object.toString());
                        }
                        break;
                }

            }
        };
        EventManager.getInstance().registerListener(onNotifyListener);


        totalScoreTv.setText(toalScore+"");

        Log.i("1111111111",rightNum+"====="+wrongNum+"-----"+undonum+"-----"+toalScore);




    }
    private void sendBordCast() {
        Intent intent = new Intent(SupvisorFragment.CHECKCONTENTNEEDCHANGE);
        getActivity().sendBroadcast(intent);

    }
    private void initData() {
        adapter.notifyDataSetChanged();
    }

    public void setData(List<RisistantVo.TempListBean.ItemListBean> list) {
        ToastUtil.showMessage("ahdkjashdjkash", 2000);
        this.list = list;
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        EventManager.getInstance().unRegisterListener(onNotifyListener);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }
}
