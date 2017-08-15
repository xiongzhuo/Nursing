package com.deya.hospital.workspace.priviewbase;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.CommonUtils;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.im.sdk.dy.ui.GKHelperActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public abstract class BaseFormMainActivity extends BaseActivity implements View.OnClickListener {
    public CommonTopView topView;
    LayoutInflater inflater;
    private RisistantVo rv;
    List<RisistantVo.TempListBean> list = new ArrayList<>();
    EnvironmentTypesAdapter adapter;
    ListView listView;
    TaskVo taskVo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.environment_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public  void initView() {
        if(getIntent().hasExtra("data")){
            taskVo= (TaskVo) getIntent().getSerializableExtra("data");
        }
        listView = (ListView) this.findViewById(R.id.listView);
        adapter = new EnvironmentTypesAdapter();
        listView.setAdapter(adapter);
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);
        inflater = LayoutInflater.from(mcontext);
        findViewById(R.id.sendMessageBtn).setOnClickListener(this);
        findViewById(R.id.tellTv).setOnClickListener(this);
        setFormData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RisistantVo risistantVo=new RisistantVo();

                RisistantVo.TempListBean bean = list.get(position);
                risistantVo.getTemp_list().add(bean);
                Intent it = new Intent(mcontext, getActivityClass());
                it.putExtra("formdata", TaskUtils.gson.toJson(risistantVo));
                it.putExtra("title", bean.getTitle());
                it.putExtra("data",taskVo);
                startActivity(it);
            }
        });

    }

    public abstract   Class<?> getActivityClass();
    public abstract   String  getTaskType();
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageBtn:
                StartActivity(GKHelperActivity.class);
                break;
            case R.id.tellTv:
                CommonUtils.callServiceTell(mcontext);
                break;

        }

    }



    private void setFormData() {
        String string=getChache();
        if(AbStrUtil.isEmpty(string)){
            return;
        }
        rv = TaskUtils.gson.fromJson(string, RisistantVo.class);
        list.clear();
        list.addAll(rv.getTemp_list());
        adapter.notifyDataSetChanged();
    }
    private String getChache(){
        return SharedPreferencesUtil.getString(mcontext, "comonform"+getTaskType() , "");
    }

    private int corlors[]={R.color.line2_corlor, R.color.blue, R.color.line4_corlor, R.color.line1_corlor, R.color.line3_corlor, R.color.line5_corlor};
    class EnvironmentTypesAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewholder;
            if (null == convertView) {
                viewholder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listitem_environment_types, null);
                viewholder.typesName = (TextView) convertView.findViewById(R.id.typesName);
                convertView.setTag(viewholder);

            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            int corlorId=position%6;
            viewholder.typesName.setBackgroundResource(corlors[corlorId]);
            viewholder.typesName.setText(list.get(position).getTitle());
            return convertView;
        }
    }

    private class ViewHolder {
        TextView typesName;
    }
}
