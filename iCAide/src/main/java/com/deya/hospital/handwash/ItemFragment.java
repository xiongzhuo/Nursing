package com.deya.hospital.handwash;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.HandReportDetailAdapter;
import com.deya.hospital.base.BaseTableFragment;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.ReportDetailVo;
import com.deya.hospital.workspace.TaskUtils;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/16
 */
public class ItemFragment extends BaseTableFragment implements RequestInterface {
    private static final int GET_DATA_DETAIL = 0x9010;
    HandReportDetailAdapter adapter;
    List<ReportDetailVo.HandFunReportDataBean> list;
    ListView listView;
    ReportDetailVo reportDetailVo;
    LinearLayout empertyView;

    public static ItemFragment newInstance(int type, String departmentId,String time) {
        ItemFragment newFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        bundle.putSerializable("departmentId", departmentId);
        bundle.putSerializable("time", time);
        newFragment.setArguments(bundle);
        return newFragment;
    }



    @Override
    public void initView() {
        list = new ArrayList<>();
        listView = findById(R.id.listView);
        empertyView= (LinearLayout) findViewById(R.id.empertyView);
        Bundle args = getArguments();
        int type = args.getInt("type", 0);
        String departmentId = args.getString("departmentId");
        String time = args.getString("time");
        getdetailData(type, departmentId,time);

    }

    private void getdetailData(int type, String departmentId,String time) {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("checkType", type + "");
            job.put("time",time);
            if(!AbStrUtil.isEmpty(departmentId)){
            job.put("departmentId", departmentId + "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, getActivity(),
                GET_DATA_DETAIL, job, "task/monthDepartmentReport");
    }

    @Override
    public int getLayoutId() {
        return R.layout.dy_listview_lay;
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        Log.i("GET_DATA_DETAIL", jsonObject.toString());
        dismissdialog();
        reportDetailVo = TaskUtils.gson.fromJson(jsonObject.toString(), ReportDetailVo.class);
        if(reportDetailVo.getTimesReportData().size()<=0){
            empertyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            TextView tv= (TextView) empertyView.findViewById(R.id.tv);
            tv.setText("亲，没有查询到报表数据哟！");
        }else{
        adapter=new HandReportDetailAdapter(getContext(),reportDetailVo);
        listView.setAdapter(adapter);
        }

    }

    @Override
    public void onRequestErro(String message) {
        dismissdialog();
        ToastUtil.showMessage(message);
    }

    @Override
    public void onRequestFail(int code) {
        dismissdialog();
        ToastUtil.showMessage("您的网络不顺畅！");
    }
}
