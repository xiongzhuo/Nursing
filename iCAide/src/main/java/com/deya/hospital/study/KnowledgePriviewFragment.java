package com.deya.hospital.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.vo.KnowledgeVo;
import com.deya.hospital.workcircle.knowledge.KnowledgeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/21
 */
public class KnowledgePriviewFragment extends BaseFragment {
    ListView pagerList;
    TextView typeTv, titleTv;
    KnowledgeAdapter adapter;
    boolean isMuti = false;
    int index;
    KnowledgeVo knowlegeVo;
    List<KnowledgeVo.ListBean.ItemsBean> list;
    KnowledgePriviewBaseActivity activity;

    public static KnowledgePriviewFragment newInstance(KnowledgeVo.ListBean vo, int type) {
        KnowledgePriviewFragment newFragment = new KnowledgePriviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", vo);
        newFragment.setArguments(bundle);
        return newFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_knowledge_priview, container,
                    false);
            initBuesinisData();
            initView();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }

    private void initView() {
        activity= (StudyKnowledgeActivity) getActivity();
        if(null==list){
            list=new ArrayList<>();
        }
        pagerList = (ListView) this.findViewById(R.id.pagerList);
        pagerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


            }
        });
        adapter = new KnowledgeAdapter(getActivity(), list);
        pagerList.setAdapter(adapter);
        typeTv = (TextView) this.findViewById(R.id.typeTv);
        titleTv = (TextView) this.findViewById(R.id.titleTv);
        findViewById(R.id.submitBtn).setVisibility(View.GONE);
    }

    private void initBuesinisData() {
        Bundle args = getArguments();
        knowlegeVo = (KnowledgeVo) args.getSerializable("data");
    }

    public void refresh(KnowledgeVo.ListBean vo){
        titleTv.setText("\u3000\u3000\u3000\u3000"+vo.getTitle());
        typeTv.setText(vo.getSub_type()==1?"单选题":"多选题");
        isMuti = vo.getSub_type() == 2;
        adapter.setIsMuti(isMuti);
        list.clear();
        list.addAll(vo.getItems());
        adapter.notifyDataSetChanged();

    }

}
