package com.deya.hospital.study;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseCommonTopActivity;
import com.deya.hospital.base.BaseViewHolder;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.ExzanminVo;
import com.deya.hospital.workspace.TaskUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 * 试卷类型
 *
 * @author : sunp
 * @date 2016/11/25
 */
public class ExaminationPaperActivity extends BaseCommonTopActivity implements RequestInterface {
    private static final int GET_SUBJECT = 0x804;
    ListView listView;
    List<ExzanminVo.ListBean> list;
    ExzanminClassAdapter adapter;
    int type;//1、培训资料 公开  2、本院
    private CountDownTimer timer;
    private LinearLayout empertyView;
    TextView eempertyTxt;

    @Override
    public String getTopTitle() {
        return "院内考核";
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.paper_list_lay);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initBaseData() {
        type=getIntent().getIntExtra("type",1);
        topView.setTitle(type==2?"院内考核":"知识评测");
        getData();
    }

    @Override
    public void initView() {
        list = new ArrayList<>();
        listView = (ListView) this.findViewById(R.id.listView);
        empertyView = (LinearLayout) findViewById(R.id.empertyView);
        eempertyTxt= (TextView) this.findViewById(R.id.tv);
        adapter = new ExzanminClassAdapter(mcontext, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mcontext, PripairExamActivity.class);
                it.putExtra("data", list.get(position));
                it.putExtra("type",type);
                startActivity(it);
            }
        });


    }

    private void getData() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("hospitalId", tools.getValue(Constants.HOSPITAL_ID));
            job.put("testType", type==1?"public":"hospital");
            job.put("userId", tools.getValue(Constants.USER_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext, GET_SUBJECT, job, "subject/test/testList");
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        dismissdialog();
        Log.i("111111", jsonObject.toString());
        ExzanminVo vo = TaskUtils.gson.fromJson(jsonObject.toString(), ExzanminVo.class);
        list.addAll(vo.getList());
        eempertyTxt.setText("请到医院后台培训考核--试卷列表\n--本院试卷新增上传吧！");
        if (list.size() <= 0) {
            listView.setVisibility(View.GONE);
            empertyView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            empertyView.setVisibility(View.GONE);
        }
            adapter.notifyDataSetChanged();
        int total=0;
        for (ExzanminVo.ListBean cf : list) {
            total += cf.getSubject_count();
        }
    }

    @Override
    public void onRequestErro(String message) {
        eempertyTxt.setText(message);
        dismissdialog();

    }

    @Override
    public void onRequestFail(int code) {
        eempertyTxt.setText("亲您的网络不顺畅，请稍后再试");
        dismissdialog();

    }

    private class ExzanminClassAdapter extends DYSimpleAdapter<ExzanminVo.ListBean> {

        public ExzanminClassAdapter(Context context, List<ExzanminVo.ListBean> list) {
            super(context, list);
        }

        @Override
        protected void setView(int position, View convertView) {
            ExzanminVo.ListBean vo = list.get(position);
            TextView titleTv = BaseViewHolder.get(convertView, R.id.titleTv);
            TextView numsTv = BaseViewHolder.get(convertView, R.id.numsTv);
            TextView totalScoreTv = BaseViewHolder.get(convertView, R.id.totalScoreTv);
            TextView totalTimeTv = BaseViewHolder.get(convertView, R.id.totalTimeTv);
            titleTv.setText(vo.getTitle());
            numsTv.setText(vo.getSubject_count() + "题");
            totalScoreTv.setText("总分:" + vo.getMax_score() + "分");
            totalTimeTv.setText("限时：" + vo.getMins() + "分");
            // titleTv.setText(vo.getTitle());

        }

        @Override
        public int getLayoutId() {
            return R.layout.layout_adpter_exzamin;
        }
    }
}
