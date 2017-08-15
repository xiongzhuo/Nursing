package com.deya.hospital.handwash;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.DepartReportItemAdapter;
import com.deya.hospital.base.BaseCommonTopActivity;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.HandDepartReportVo;
import com.deya.hospital.workspace.TaskUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/4/6
 */public class DepartDataActivity extends BaseCommonTopActivity implements RequestInterface{
    private static final int GET_SUC = 0x001;
    ListView listView;
    private String departmentId;
    List<HandDepartReportVo.DataBean> dataListBeanList;
    MonthMainAdapter handMainAdapter;
    TextView departmentTxt;
    String departmentName="";
    @Override
    public String getTopTitle() {
        if(getIntent().hasExtra("departmentId")){
            return "科室数据";
        }else{
            return "全院数据";
        }

    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.hand_listview_activity);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initBaseData() {

    }

    private void getData(){
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            if(!AbStrUtil.isEmpty(departmentId)){
            job.put("departmentId", departmentId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomReq(this,this,GET_SUC,job,"task/handTimesReportByMonth");
    }
    @Override
    public void initView() {
        departmentId=getIntent().getStringExtra("departmentId");
        dataListBeanList=new ArrayList<>();
        topView.setRigtext("详情");
        topView.showRightView(View.GONE);

        listView = (ListView) this.findViewById(R.id.listView);
        departmentName=getIntent().getStringExtra("name");
        departmentTxt=findView(R.id.departmentTxt);
        departmentTxt.setText(departmentName);
        if(!AbStrUtil.isEmpty(departmentName)){
            departmentTxt.setVisibility(View.VISIBLE);
        }
        handMainAdapter=new MonthMainAdapter(mcontext,dataListBeanList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        listView.setAdapter(handMainAdapter);
        getData();
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        dismissdialog();
        Log.i(getClass().getName(),jsonObject.toString());
        HandDepartReportVo handReportVo= TaskUtils.gson.fromJson(jsonObject.toString(),HandDepartReportVo.class);
        dataListBeanList.addAll(handReportVo.getData());
        handMainAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRequestErro(String message) {
        dismissdialog();

    }

    @Override
    public void onRequestFail(int code) {

    }



    private class MonthMainAdapter extends DYSimpleAdapter<HandDepartReportVo.DataBean> {
        private AdapterView.OnItemClickListener itemClickListener;

        public MonthMainAdapter(Context context, List<HandDepartReportVo.DataBean> list) {
            super(context, list);
        }

        public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        @Override
        protected void setView(final int position, View convertView) {
            TextView departmentTv = findView(convertView, R.id.departmentTv);
            View line = findView(convertView, R.id.line);
            LinearLayout leftLay=findView(convertView, R.id.leftLay);
            TextView typeTv=findView(convertView, R.id.typeTv);


            final HandDepartReportVo.DataBean departmentReportBean = getItem(position);
            departmentTv.setText(departmentReportBean.getShowTime());
            if(position==0){
                line.setVisibility(View.GONE);
                departmentTv.setBackgroundColor(getResources().getColor(R.color.blue));
                departmentTv.setTextColor(getResources().getColor(R.color.white));
            }else{
                line.setVisibility(View.VISIBLE);
                departmentTv.setBackgroundColor(getResources().getColor(R.color.white));
                departmentTv.setTextColor(getResources().getColor(R.color.orange));
            }
            ListView itemlist = findView(convertView, R.id.itemlist);
            itemlist.setOnItemClickListener(null);
            if(departmentReportBean.getDataList().size()<=1){
                leftLay.setVisibility(View.GONE);
            }else{
                leftLay.setVisibility(View.VISIBLE);
            }
            DepartReportItemAdapter departReportItemAdapter = new DepartReportItemAdapter(context, departmentReportBean.getDataList());
            itemlist.setAdapter(departReportItemAdapter);
            itemlist.setItemsCanFocus(false);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, ItemDetailActivity.class);
                    intent.putExtra("name",departmentName);
                    intent.putExtra("departmentId",departmentId);
                    intent.putExtra("time",dataListBeanList.get(position).getTime());
                    startActivity(intent);
                }
            });
            itemlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                    Intent intent = new Intent(mcontext, ItemDetailActivity.class);
                    intent.putExtra("name",departmentName);
                    intent.putExtra("departmentId",departmentId);
                    intent.putExtra("time",dataListBeanList.get(position).getTime());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getLayoutId() {
            return R.layout.handmain_report;
        }
    }

}
