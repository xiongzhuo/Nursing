package com.deya.hospital.supervisor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.JobListAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.DepartChooseActivity;
import com.deya.hospital.base.DepartChoosePop;
import com.deya.hospital.base.TimerDialog;
import com.deya.hospital.base.TimerDialog.TimerInter;
import com.deya.hospital.supervisor.JobDialog.ChooseItem;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddtaskInfoActivity extends BaseActivity implements
        OnClickListener {

    GridView listView;
    List<JobListVo> jobList = new ArrayList<JobListVo>();
    List<JobListVo> jobTypelist = new ArrayList<JobListVo>();// 工作性质
    JobListAdapter adapter;
    GridView gv;
    ChooseItem chooseInter;
    Context context;
    GvAdapter gvAdapter;
    String workId = "";
    String jobId = "";
    private String name;
    LinearLayout nameLay;
    int[] wh;
    boolean showNameEditor;
    private EditText nameEditor, adressEdt, comporator;
    private LayoutParams para;
    private LayoutParams para2;
    TextView titleTv, timeTv;
    DepartChoosePop departDialog;
    private TextView departTv;
    TaskVo data;
    private Button sumbmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_compeletinfo);
        context = mcontext;
        data = (TaskVo) getIntent().getSerializableExtra("data");
        workId = data.getWork_type();
        jobId = data.getPpost();
        findDbJobList();
        getCacheData();
        intTopView();
        intiView();

    }

    private void intTopView() {
        TextView titleTv = (TextView) this.findViewById(R.id.title);
        titleTv.setText("填写信息");
        RelativeLayout rlBack = (RelativeLayout) this
                .findViewById(R.id.rl_back);
        rlBack.setOnClickListener(this);
    }

    private void intiView() {
        nameLay = (LinearLayout) this.findViewById(R.id.nameLay);
        titleTv = (TextView) this.findViewById(R.id.titleTv);
        nameEditor = (EditText) this.findViewById(R.id.nameEditor);
        nameEditor.setText(data.getUname());
        comporator = (EditText) this.findViewById(R.id.comporator);
        adressEdt = (EditText) this.findViewById(R.id.adressEdt);
        adressEdt.setText(data.getAddress());

        timeTv = (TextView) this.findViewById(R.id.timeTv);
        timeTv.setOnClickListener(this);
        if (!AbStrUtil.isEmpty(data.getMission_time())) {
            timeTv.setText(data.getMission_time());
        }
        sumbmitBtn = (Button) this.findViewById(R.id.sumbmitBtn);
        sumbmitBtn.setOnClickListener(this);

        departTv = (TextView) this.findViewById(R.id.departTv);
        departTv.setText(data.getDepartmentName());

        departTv.setOnClickListener(this);
        // if (!showNameEditor) {
        // nameLay.setVisibility(View.GONE);
        // } else {
        // titleTv.setText("修改信息");
        // }
        wh = AbViewUtil.getDeviceWH(mcontext);
        para2 = new LayoutParams((wh[0] - dp2Px(95)) / 3,
                (wh[0] - dp2Px(100)) / 5);
        adapter = new JobListAdapter(context, jobList, para2);
        para = new LayoutParams((wh[0] - dp2Px(100)) / 4,
                (wh[0] - dp2Px(100)) / 4);
        listView = (GridView) this.findViewById(R.id.dialog_list);
        gv = (GridView) this.findViewById(R.id.gv);
        gvAdapter = new GvAdapter();
        gv.setAdapter(gvAdapter);
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                gvAdapter.setChooseItem(position);
                data.setWork_type(jobTypelist.get(position).getId());
            }
        });
        if (!AbStrUtil.isEmpty(workId)) {
            for (int j = 0; j < jobTypelist.size(); j++) {
                if (workId.equals(jobTypelist.get(j).getId())) {
                    chooseIndex = j;
                    gvAdapter.notifyDataSetChanged();
                }
            }
        }
        if (!AbStrUtil.isEmpty(jobId)) {
            for (int i = 0; i < jobList.size(); i++) {
                if (jobId.equals(jobList.get(i).getId())) {
                    adapter.chooseItem(i);
                }
            }
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                adapter.chooseItem(position);
                data.setPpost(jobList.get(position).getId());

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (null == intent) {
            return;
        }

        switch (resultCode) {
            case DepartChooseActivity.CHOOSE_SUC:
                DepartVos.DepartmentListBean bean = (DepartVos.DepartmentListBean) intent.getSerializableExtra("departData");
                departTv.setText(bean.getName());
                data.setDepartment(bean.getId() + "");
                data.setDepartmentName(bean.getName());
                break;
            default:
                break;
        }

    }

    public int dp2Px(int dp) {
        final float scale = mcontext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + (int) 0.5f);
    }

    int chooseIndex = -1;

    public class GvAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return jobTypelist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public void setChooseItem(int position) {
            if (chooseIndex == position) {
                chooseIndex = -1;
            } else {
                chooseIndex = position;
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_job_type, null);
            TextView tx = (TextView) convertView.findViewById(R.id.textView);
            if (chooseIndex != position) {
                tx.setBackgroundResource(R.drawable.circle_sharp_blue);
                tx.setTextColor(context.getResources().getColor(
                        R.color.top_color));
            } else {
                tx.setBackgroundResource(R.drawable.round_orange);
                tx.setTextColor(context.getResources().getColor(R.color.white));
            }
            tx.setText(jobTypelist.get(position).getName());
            tx.setLayoutParams(para);
            return convertView;
        }

    }

    String jobs[];

    private void findDbJobList() {
        try {
            if (null != DataBaseHelper
                    .getDbUtilsInstance(mcontext).findAll(JobListVo.class)) {
                List<JobListVo> list = DataBaseHelper
                        .getDbUtilsInstance(mcontext).findAll(JobListVo.class);
                if (list.size() > 3) {
                    jobList.add(list.get(0));
                    jobList.add(list.get(1));
                    jobList.add(list.get(list.size() - 1));
                }
                Log.i("11111111111joblist", jobList.size() + "");

                jobs = new String[jobList.size()];

                List<JobListVo> temp = new ArrayList<JobListVo>();
                JobListVo other = new JobListVo();
                int k = 0;
                for (int i = 0, j = jobList.size(); i < j; i++) {
                    JobListVo jv = jobList.get(i);
                    if (jv.getName().equals("其他")) {
                        other = jv;
                    } else {
                        temp.add(jv);
                        jobs[k] = jv.getName();
                        k++;
                    }

                }
                jobList.clear();
                jobList.addAll(temp);
                jobList.add(other);
                jobs[jobList.size() - 1] = other.getName();

            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    Gson gson = new Gson();

    public void getCacheData() {
        // type1 位督导岗位 type4为职称 type3为职位
        String jsonStr = SharedPreferencesUtil.getString(mcontext,
                "jobinfolist", null);
        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray jarr4 = jsonObject.optJSONArray("jobType5");
                jobTypelist = gson.fromJson(jarr4.toString(),
                        new TypeToken<List<JobListVo>>() {
                        }.getType());

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timeTv:
                showTimeDialog();
                break;
            case R.id.departTv:
                Intent intent2 = new Intent(mcontext, DepartChooseActivity.class);
                startActivityForResult(intent2, DepartChooseActivity.CHOOSE_SUC);
                break;
            case R.id.sumbmitBtn:
                if (AbStrUtil.isEmpty(adressEdt.getText().toString())) {
                    ToastUtils.showToast(mcontext, "手术室为必填！");
                    return;
                }
                if (AbStrUtil.isEmpty(data.getDepartmentName())) {
                    ToastUtils.showToast(mcontext, "科室为必填！");
                    return;
                }
                data.setAddress(adressEdt.getText().toString());
                data.setUname(nameEditor.getText().toString());
                data.setUteam(comporator.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("data", data);
                setResult(0x55, intent);
                finish();
                break;
            case R.id.rl_back:
                finish();
                break;
            default:
                break;
        }

    }

    TimerDialog dialog;

    void showTimeDialog() {
        dialog = new TimerDialog(AddtaskInfoActivity.this, new TimerInter() {

            @Override
            public void onChooseTime(String time) {
                timeTv.setText(time);
                data.setMission_time(time + ":" + "00");
            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (null != dialog) {
            dialog.dismiss();
        }
        if (null != departDialog) {
            departDialog.dismiss();
        }
        super.onDestroy();
    }

}
