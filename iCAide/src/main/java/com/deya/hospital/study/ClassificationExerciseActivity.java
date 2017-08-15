package com.deya.hospital.study;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseClassifyAdapter;
import com.deya.hospital.base.BaseCommonTopActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.ClassifyExciseVo;
import com.deya.hospital.workspace.TaskUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/21
 */
public class ClassificationExerciseActivity extends BaseCommonTopActivity implements RequestInterface {
    ListView listView;
    ClassificationExerciseAdapter adapter;
    List<ClassifyExciseVo.ListBean> list;
    int GET_CLASSIFY_DATA = 0x801;
    ClassifyExciseVo classifyExciseVo;
    int type;//1、分类练习 2、本院题库；
    private LinearLayout empertyView;
    private TextView eempertyTxt;

    @Override
    public String getTopTitle() {
        return "分类练习";
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
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("hospitalId", tools.getValue(Constants.HOSPITAL_ID));
            job.put("subjectOwner", type == 1 ? "public" : "hospital");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext, GET_CLASSIFY_DATA, job, "subject/subjectCategory");

    }

    @Override
    public void initView() {
        type = getIntent().getIntExtra("type", 1);
        topView.setTitle(type == 1 ? "分类练习" : "本院题库");
        listView = (ListView) this.findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new ClassificationExerciseAdapter(mcontext);
        listView.setAdapter(adapter);
        listView.setDividerHeight(2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mcontext, StudyKnowledgeActivity.class);
                intent.putExtra("data", list.get(position));
                intent.putExtra("type", type);
                startActivity(intent);

            }
        });
        empertyView = (LinearLayout) findViewById(R.id.empertyView);
        eempertyTxt = (TextView) this.findViewById(R.id.tv);
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        dismissdialog();
        if (jsonObject.has("result_id") && jsonObject.opt("result_id").equals("0")) {
            classifyExciseVo = TaskUtils.gson.fromJson(jsonObject.toString(), ClassifyExciseVo.class);
            list.clear();
            list.addAll(classifyExciseVo.getList());
            adapter.notifyDataSetChanged();
        }
        eempertyTxt.setText("请到医院后台题库管理\n--题目列表新增上传吧！");
        if (list.size() <= 0) {
            empertyView.setVisibility(View.VISIBLE);
        } else {
            empertyView.setVisibility(View.GONE);
        }

        int total = 0;
        for (ClassifyExciseVo.ListBean cf : list) {
            total += cf.getCount();
        }
        adapter.notifyDataSetChanged();

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

    class ClassificationExerciseAdapter extends BaseClassifyAdapter<ClassifyExciseVo.ListBean> {

        public ClassificationExerciseAdapter(Context context) {
            super(context, list);
        }

        @Override
        public void setItem(ViewHolder viewHolder, int position) {
            ClassifyExciseVo.ListBean item = list.get(position);
            viewHolder.name.setText(item.getCateName());
            viewHolder.numTv.setText(item.getCount() + "");

        }
    }
}
