package com.deya.hospital.quality;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseTableFragment;
import com.deya.hospital.base.BaseViewHolder;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.QualityHospitalDepartVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.multidrugresistant.RisitantRequestCode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/17
 */
public class MutiTimeFragment extends BaseTableFragment implements RequestInterface,PullToRefreshBase.OnRefreshListener2{
    private static final int GET_DATA_DETAIL = 0x9011;
    private int pageIndex = 1;
    RadioGroup radioGroup;
    int type;
    List<Attachments> attachmentsList;
    LinearLayout empertyView;

    RadioButton rb0, rb1;
    private int pageCount;
    private String departmentId = "";
    private ArrayList<QualityHospitalDepartVo.ListBean> timesList;
    TextView titleTv;
    PullToRefreshListView listView;
    int pagIndex=1;
    private String item_repo_id;
    HospitalDepartsDataAdapter adapter;
    DataRefesh dataRefesh;

    public static MutiTimeFragment newInstance(int type, String item_repo_id) {
        MutiTimeFragment newFragment = new MutiTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        bundle.putSerializable("item_repo_id", item_repo_id);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void initView() {
        timesList = new ArrayList<>();
        adapter=new HospitalDepartsDataAdapter(getActivity(),timesList);
        attachmentsList = new ArrayList<>();
        Bundle args = getArguments();
        type = args.getInt("type", 0);
        item_repo_id = args.getString("item_repo_id");
        titleTv = findView(R.id.titleTv);
        listView = findView(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
        showprocessdialog();
        getData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.quality_hospital_depart;
    }


    public void getData() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("pageIndex", pagIndex);
            job.put("item_repo_id", item_repo_id);
            job.put("checkType", type+"");
            job.put("task_type",getActivity().getIntent().getIntExtra("task_type",17));
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, getActivity(), RisitantRequestCode.GET_CASES_SUC, job, "quality/countReportByIndex");
    }

    //刷新Activity的数字状态
    public void setNumLis(DataRefesh dataRefesh){
        this.dataRefesh=dataRefesh;

    }
    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        listView.onRefreshComplete();
        dismissdialog();
        Log.i(getClass().getName(),jsonObject.toString());
        QualityHospitalDepartVo vo= TaskUtils.gson.fromJson(jsonObject.toString(),QualityHospitalDepartVo.class);
        timesList.addAll(vo.getList());
        adapter.notifyDataSetChanged();
        Intent intent=new Intent("hospital_data");
        intent.putExtra("checkType",type);
        intent.putExtra("num",vo.getTotalcnt());
        if(null!=dataRefesh) {
            dataRefesh.onNumRefesh(vo.getTotalcnt());

        }
        if(pagIndex>=vo.getPageTotal()){
            listView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        }else{
            listView.setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
        }
        pagIndex++;
    }

    @Override
    public void onRequestErro(String message) {
        Log.i(getClass().getName(),message);

    }

    @Override
    public void onRequestFail(int code) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        timesList.clear();
        adapter.notifyDataSetChanged();
        pagIndex=1;
        pageCount=0;
        getData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        getData();
    }

    private class HospitalDepartsDataAdapter extends DYSimpleAdapter<QualityHospitalDepartVo.ListBean> {

        public HospitalDepartsDataAdapter(Context context, List<QualityHospitalDepartVo.ListBean> list) {
            super(context, list);
        }

        @Override
        protected void setView(int position, View convertView) {

            TextView departmentTv = BaseViewHolder.get(convertView, R.id.departmentTv);
            TextView timeTv = BaseViewHolder.get(convertView, R.id.timeTv);
            TextView nameTv = BaseViewHolder.get(convertView, R.id.nameTv);
            TextView scoreTv = BaseViewHolder.get(convertView, R.id.scoreTv);
            QualityHospitalDepartVo.ListBean vo=getItem(position);
            departmentTv.setText(vo.getDepartmentName());
            timeTv.setText(vo.getCreate_time());
            nameTv.setText(vo.getUserName());
            scoreTv.setText(vo.getScore()+"分");
        }

        @Override
        public int getLayoutId() {
            return R.layout.quality_hospital_depart_adapter;
        }
    }

    public  interface DataRefesh{
        abstract void onNumRefesh(int num);
    }
}
