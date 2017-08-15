package com.deya.hospital.study;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseViewHolder;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.baseui.BaseRefreshListViewActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.KnowledgeVo;
import com.deya.hospital.workspace.TaskUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/25
 */
public class HospitalKnowledgeActivity extends BaseRefreshListViewActivity implements RequestInterface {
    int GET_SUBJECT = 0x802;
    /**
     * @param job  获取本院题库 分类ID 传hospital
     */
    List<KnowledgeVo.ListBean> list;
    private KnowledgeVo knowledgeVo;
  CollectionAdapter  adapter;

    private void getAllHospitalData() {

        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("hospitalId", tools.getValue(Constants.HOSPITAL_ID));
            job.put("subjectOwner","hospital");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this,mcontext,GET_SUBJECT,job,"subject/subjectCategory");
    }

    @Override
    public void onListViewPullUp() {
        page++;
        getAllHospitalData();

    }

    @Override
    public void onListViewPullDown() {
        page = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        getAllHospitalData();

    }

    @Override
    public void initView() {

        list=new ArrayList<>();
        adapter=new CollectionAdapter(mcontext,list);
        listview.setAdapter(adapter);
        page = 1;
        showprocessdialog();
        getAllHospitalData();
        topView.setTitle("本院题库");
        topView.init(this);
    }

    @Override
    protected void setDataResult(JSONObject jsonObject) {
    }

    @Override
    protected void onItemclick(int position) {

    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        page++;
        knowledgeVo = TaskUtils.gson.fromJson(jsonObject.toString(), KnowledgeVo.class);
        list.addAll(knowledgeVo.getList());
        adapter.notifyDataSetChanged();

        dismissdialog();

    }

    @Override
    public void onRequestErro(String message) {

    }

    @Override
    public void onRequestFail(int code) {

    }

    public class CollectionAdapter extends DYSimpleAdapter<KnowledgeVo.ListBean> {

        public CollectionAdapter(Context context, List<KnowledgeVo.ListBean> list) {
            super(context, list);
        }

        @Override
        protected void setView(int position, View convertView) {
            KnowledgeVo.ListBean vo = list.get(position);
            TextView typeTv = BaseViewHolder.get(convertView, R.id.typeTv);
            TextView titleTv = BaseViewHolder.get(convertView, R.id.titleTv);
            titleTv.setText("\u3000\u3000\u3000\u3000" + vo.getTitle());
            typeTv.setText(vo.getSub_type() == 1 ? "单选题" : "多选题");

        }

        @Override
        public int getLayoutId() {
            return R.layout.layout_adpter_knoledgecollection;
        }
    }
}
