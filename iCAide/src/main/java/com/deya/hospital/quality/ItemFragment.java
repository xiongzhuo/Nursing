package com.deya.hospital.quality;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.BaseTaskAadpter;
import com.deya.hospital.base.BaseTableFragment;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.QualityReportTaskVo;
import com.deya.hospital.vo.QualityReportVo;
import com.deya.hospital.vo.dbdata.TaskVo;
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
    QualityTaskAdapter adapter;
    List<QualityReportTaskVo.ListBean> list;
    ListView listView;
    QualityReportTaskVo reportDetailVo;
    QualityReportVo.ListBean listBean;
    LinearLayout empertyView;
    OnGetNumLis onGetNumLis;

    public static ItemFragment newInstance(int type, QualityReportVo.ListBean listBean) {
       ItemFragment newFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        bundle.putSerializable("listBean", listBean);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public void setOnGetNumLis(OnGetNumLis onGetNumLis) {
        this.onGetNumLis = onGetNumLis;
    }

    @Override
    public void initView() {
        list = new ArrayList<>();
        listView = findById(R.id.listView);
        adapter = new QualityTaskAdapter(getActivity(),list );
        listView.setAdapter(adapter);
        empertyView = (LinearLayout) findViewById(R.id.empertyView);
        Bundle args = getArguments();
        int type = args.getInt("type", 0);
        listBean= (QualityReportVo.ListBean) args.getSerializable("listBean");

        getdetailData(type, listBean);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), QualityPriviewActivity.class);
                TaskVo taskVo=new TaskVo();
                taskVo.setTask_id(list.get(position).getTask_id());
                taskVo.setDepartmentName(list.get(position).getDepartmentName());
                taskVo.setMission_time(list.get(position).getCreate_time());
                taskVo.setWho(true);//没有病例的现场督察
                intent.putExtra("tempId",listBean.getTemp_id());
                intent.putExtra("data",taskVo);
                startActivity(intent);
            }
        });

    }

    private void getdetailData(int type, QualityReportVo.ListBean listBean) {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("checkType", type + "");
            job.put("tempId",listBean.getTemp_id());
            if (!AbStrUtil.isEmpty(listBean.getDepartmentId()+"")) {
                job.put("departmentId", listBean.getDepartmentId() + "");
            }
            job.put("task_type",getActivity().getIntent().getIntExtra("task_type",17));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, getActivity(),
                GET_DATA_DETAIL, job, "quality/taskListByDepartment");
    }

    @Override
    public int getLayoutId() {
        return R.layout.dy_listview_lay_whitout_magintop;
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        Log.i("GET_DATA_DETAIL", jsonObject.toString());
        list.clear();
        dismissdialog();
        reportDetailVo = TaskUtils.gson.fromJson(jsonObject.toString(), QualityReportTaskVo.class);
        if (reportDetailVo.getList().size() <= 0) {
            empertyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        if(null!=getActivity()){
        onGetNumLis.OnSuc(reportDetailVo.getTotalcnt());
        TextView tv = (TextView) empertyView.findViewById(R.id.tv);
        tv.setText("亲，没有查询到报表数据哟！");
        list.addAll(reportDetailVo.getList());
        adapter.notifyDataSetChanged();
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

    private  class QualityTaskAdapter extends BaseTaskAadpter<QualityReportTaskVo.ListBean> {

        public QualityTaskAdapter(Context context, List<QualityReportTaskVo.ListBean> list) {
            super(context, list);
        }

        @Override
        public void setItem(int position, TaskAdapterViewHolder mviewHolder) {
            mviewHolder.timeTypes.setText("");
            mviewHolder.typeTv.setText("临床质控");

            QualityReportTaskVo.ListBean vo=list.get(position);
            // mviewHolder.timeTypes.setText(tv.getDepartmentName()+"的"+tv.getName());
            mviewHolder.title
                        .setText(vo.getDepartmentName()+";"+listBean.getTempName()+"("+vo.getScore()+"分)");
            mviewHolder.typeTv.setBackgroundResource(R.drawable.circle_1);
            mviewHolder.finishtime.setText(vo.getUserName() + "  " + vo.getMission_time());
            mviewHolder.stateTv.setText("已上传");
            mviewHolder.stateImg
                    .setBackgroundResource(R.drawable.finish_img);
        }
    }


    public  interface OnGetNumLis{
        abstract void OnSuc(int num);

    }
}
