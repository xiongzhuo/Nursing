package com.deya.hospital.workspace.priviewbase;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.vo.RisistantVo;
import com.im.sdk.dy.common.utils.ToastUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
@SuppressLint({"ValidFragment"})
public class BaseSupvisorFragment2 extends BaseFragment implements View.OnClickListener {
    ListView formListView;
    View rootView;
    List<RisistantVo.TempListBean.ItemListBean> list;
    ResitantPriviewAadapter2 adapter;


  public   static BaseSupvisorFragment2 newInstance(List<RisistantVo.TempListBean.ItemListBean> list) {
      BaseSupvisorFragment2 newFragment = new BaseSupvisorFragment2();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", (Serializable) list);
        newFragment.setArguments(bundle);
        //bundle还可以在每个标签里传送数
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
        list = (List<RisistantVo.TempListBean.ItemListBean>) args.getSerializable("data");
        formListView = (ListView) rootView.findViewById(R.id.formListView);
        adapter = new ResitantPriviewAadapter2(getActivity(), list);
        formListView.setAdapter(adapter);
        initData();

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
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }
}
