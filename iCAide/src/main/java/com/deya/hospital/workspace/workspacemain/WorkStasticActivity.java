package com.deya.hospital.workspace.workspacemain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.BaseNextImgAdapter;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DatePicDialog;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.vo.DynamicStasticVo;
import com.deya.hospital.vo.DynamicVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.priviewbase.ResistantListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class WorkStasticActivity extends BaseActivity {
    private ListView listView;
    private WorkStasticAdapter adapter;
    DynamicVo.ListBean dv = new DynamicVo.ListBean();
    private CommonTopView topView;
    private MyHandler myHandler;
    protected static final int SEARCH_SUCCESS = 0x40;
    protected static final int SEARCH_FAIL = 0x41;
    List<DynamicStasticVo.ListBean> list = new ArrayList<>();
    private DatePicDialog datePicDialog;
    LinearLayout newWorkView;
    TextView empertyText;
    Button emperty_button;
    TextView tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gkexpert2);
        initMyHandler();
        initView();
    }

    private void initView() {
        if (getIntent().hasExtra("data")) {
            dv = (DynamicVo.ListBean) getIntent().getSerializableExtra("data");
        }
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);
        tips = (TextView) this.findViewById(R.id.tips);
        tips.setText(dv.getName());
        topView.setTitle(dv.getDate());
        topView.setTileRightImgVisble(View.VISIBLE);
        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicDialog.show();
            }
        });
        newWorkView= (LinearLayout) this.findViewById(R.id.networkView);
        newWorkView.setVisibility(View.GONE);
        empertyText= (TextView) this.findViewById(R.id.empertyText);
        listView = (ListView) findViewById(R.id.lv_expert);
        emperty_button= (Button) this.findViewById(R.id.emperty_button);
        emperty_button.setText("去了解如何对接数据吧！");
        emperty_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(mcontext, WebViewDemo.class);
                it.putExtra("url", WebUrl.LEFT_URL+"/gkgzj-help/question_detail.html");
                it.putExtra("title", "如何对接");
                startActivity(it);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DynamicStasticVo.ListBean bean = list.get(position);
                Intent intent = new Intent(mcontext, ResistantListActivity.class);
                if (AbStrUtil.isEmpty(dv.getCode())) {
                    return;
                }
                bean.setCode(dv.getCode());
                bean.setDate(dv.getDate());
                intent.putExtra("daynamic", bean);
                intent.putExtra("type", dv.getType());
                startActivity(intent);

            }
        });
        adapter = new WorkStasticAdapter(mcontext, list);
        listView.setFooterDividersEnabled(true);
        listView.setAdapter(adapter);
        datePicDialog = new DatePicDialog(mcontext,
                new DatePicDialog.OnDatePopuClick() {

                    @Override
                    public void enter(String text) {
                        topView.setTitle(text);
                        dv.setDate(text);
                        getClassfyData();

                    }

                });
        if(dv.getNum()<0){
            newWorkView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            emperty_button.setVisibility(View.VISIBLE);
            tips.setVisibility(View.GONE);
            empertyText.setText("亲，您还没有接入医院内数据哦，赶快去对接吧！");
         return;
        }
        getClassfyData();
    }

    public void getClassfyData() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("type", dv.getType());
            job.put("getDate", dv.getDate());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("watchActivity", job.toString());
        MainBizImpl.getInstance().onComomRequest(myHandler, this,
                SEARCH_SUCCESS, SEARCH_FAIL, job, "count/dataDetailByDay");

    }

    private void initMyHandler() {
        myHandler = new MyHandler(this) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = myHandler.mactivity.get();
                if (null != activity) {
                    switch (msg.what) {
                        case SEARCH_SUCCESS:
                            if (null != msg.obj) {
                                Log.i("1111", msg.obj + "");
                                try {
                                    setListData(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            break;
                        case SEARCH_FAIL:
                            newWorkView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                            tips.setVisibility(View.GONE);
                            empertyText.setText("亲，您的网络不顺畅，请检查您的网络！");
                            break;

                        default:
                            break;
                    }
                }
            }
        };
    }

    private void setListData(JSONObject jsonObject) {
        if (AbStrUtil.iSJsonSuc(jsonObject)) {
            DynamicStasticVo dv = TaskUtils.gson.fromJson(jsonObject.toString(), DynamicStasticVo.class);
            list.clear();
            if (null != dv.getList()) {
                list.addAll(dv.getList());
            }
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(mcontext, AbStrUtil.FailJsonMsg(jsonObject));
        }
        if(list.size()<=0){
            newWorkView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            tips.setVisibility(View.GONE);
            empertyText.setText("亲，今天没有任何数据哦！");
        }else{
            newWorkView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    private class WorkStasticAdapter extends BaseNextImgAdapter<DynamicStasticVo.ListBean> {

        public WorkStasticAdapter(Context context, List<DynamicStasticVo.ListBean> list) {
            super(context, list);
        }

        @Override
        public void getView(BaseNextImgAdapter<DynamicStasticVo.ListBean>.ViewHolder viewHolder, int position) {
            DynamicStasticVo.ListBean bean = list.get(position);
            viewHolder.numTv.setText(bean.getPatient_count() + "人");
            viewHolder.name.setText(bean.getDep_name());

        }
    }

}
