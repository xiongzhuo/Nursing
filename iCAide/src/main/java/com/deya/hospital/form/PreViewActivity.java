package com.deya.hospital.form;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.form.PreviewAdapter.previewAdapterInter;
import com.deya.hospital.supervisor.SupervisionActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormDetailVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.example.calendar.CalendarMainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class PreViewActivity extends BaseFromPriViewActivity implements OnClickListener {
    PreviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        data = (TaskVo) getIntent().getSerializableExtra("data");
        title = data.getName();
        formtype = data.getFormType();
        totalScore = data.getTotalscore();
        id = data.getFormId();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initBaseData() {
        String time = getIntent().getStringExtra("time");
        if (data.getTask_id() > 0) {
            List<FormDetailListVo> lis1t = (List<FormDetailListVo>) getIntent()
                    .getSerializableExtra("list");
            list.addAll(lis1t);
        } else if (data.getDbid() <= 0||data.getStatus()==5) {
            data.setMission_time(TaskUtils.getTaskMissionTime(time));
            getCacheDetail();
        } else {
            List<FormDetailListVo> lis1t = gson.fromJson(data.getItems()
                    .toString(), new TypeToken<List<FormDetailListVo>>() {
            }.getType());
            list.addAll(lis1t);
            adapter.setdata(list);
            if (formtype == 4) {
                int total = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getItemTotalScore() != -1) {
                        total += list.get(i).getItemTotalScore();
                    } else {
                        total += list.get(i).getScore();
                    }
                }
                totalScoreTv2.setText("质量标准" + "(总分："
                        + AbStrUtil.reMoveUnUseNumber(total + "") + ")");
            }
        }
    }

    @Override
    protected void onChooseDepartList(String name, String id) {
        departTv.setText(name);
        data.setDepartmentName(name);
        data.setDepartment(id);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_prviewlist;
    }

    @Override
    public void initChildView() {
        intTopView();
        titleLay = (LinearLayout) this.findViewById(R.id.titleLay);
        titleLay.setVisibility(View.GONE);
        findViewById(R.id.departRelay).setBackgroundResource(
                R.drawable.big_round_blue_type_style);
        findViewById(R.id.mainRemarkTv).setVisibility(View.GONE);
        findViewById(R.id.departLine).setVisibility(View.GONE);
        departLay = (LinearLayout) this.findViewById(R.id.departLay);
        departLay.setVisibility(View.VISIBLE);
        departLay.setOnClickListener(this);
        departTv = (TextView) this.findViewById(R.id.departTv);
        departTv.setHint("请选择科室");
        departTv.setGravity(Gravity.CENTER);
        titleTv.setText(title);

        tipsTv = (TextView) this.findViewById(R.id.tiptv);
        tipsTv.setVisibility(View.GONE);
        String text = "";
        if (!AbStrUtil.isEmpty(data.getDepartmentName())) {
            text = data.getDepartmentName();

        }
        departTv.setText(text);
        sumbmitBtn = (Button) this.findViewById(R.id.sumbmitBtn);
        sumbmitBtn.setOnClickListener(this);
        sumbmitlay = (LinearLayout) this.findViewById(R.id.sumbmitlay);
        if (data.getStatus() != 2) {
            sumbmitlay.setVisibility(View.GONE);
        }
        formTitleTv = (TextView) this.findViewById(R.id.formTitleTv);
        formTitleTv.setText(title);
        totalScoreTv2 = (TextView) this.findViewById(R.id.totalScoreTv2);
        if (formtype == 1) {

            totalScoreTv2.setVisibility(View.VISIBLE);
            totalScoreTv2.setText("质量标准" + "(总分："
                    + AbStrUtil.reMoveUnUseNumber(totalScore + "") + ")");
        } else if (formtype == 2) {
            totalScoreTv2.setText("质量标准");
        }
        if (data.getType().equals("5")) {
            titleLay = (LinearLayout) this.findViewById(R.id.titleLay);
            titleLay.setVisibility(View.GONE);
            departLay = (LinearLayout) this.findViewById(R.id.departLay);
            departLay.setVisibility(View.VISIBLE);
            departLay.setOnClickListener(this);
            departTv = (TextView) this.findViewById(R.id.departTv);
        }

        listView = (ListView) this.findViewById(R.id.ListView);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                toFormSetting(position);
            }
        });
        adapter = new PreviewAdapter(mcontext, list, formtype,
                new previewAdapterInter() {

                    @Override
                    public void onputData(int position) {
                        putdata(position);

                    }

                    @Override
                    public void onRemark(int parentPosition, int position) {
                        toRemark(parentPosition, position);

                    }
                });
        listView.setAdapter(adapter);
    }


    @Override
    public void saveCache(int status) {

    }


    private void getCacheDetail() {
        List<FormDetailListVo> lis1t = (List<FormDetailListVo>) getIntent()
                .getSerializableExtra("itemList");
        list.addAll(lis1t);
        for (FormDetailListVo fdlv : list) {
            for (FormDetailVo fdv : fdlv.getSub_items()) {
                fdv.setScores(fdv.getBase_score());
            }
        }
        adapter.setdata(list);
    }

    public void toFormSetting(int position) {
        if (isOnlyPriview) {
            return;
        }
        Intent it = new Intent(mcontext, FormSettingActivity.class);
        it.putExtra("data", (Serializable) list);
        it.putExtra("position", position);
        it.putExtra("vo", data);

        startActivity(it);

    }

    TextView titleTv;

    private void intTopView() {
        titleTv = (TextView) this.findViewById(R.id.title);

        rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
        rlBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                onBack();
                break;
            case R.id.sumbmitBtn:
                if (AbStrUtil.isEmpty(data.getDepartment())) {
                    ToastUtils.showToast(mcontext, "请选择科室");
                    departDialog.show();
                    return;
                } else {
                    showTips();
                }

                break;

            case R.id.departLay:
                departDialog.show();
                break;
            default:
                break;
        }

    }

    @Override
    public void addMinRemark() {
        TaskVo remarkData = FormUtils
                .getMainRemark(list, data, title, mcontext);
        if (null != remarkData) {
            String mainRemarkStr = gson.toJson(remarkData);
            data.setMain_remark(mainRemarkStr);
        }
    }

    @Override
    public void doSubmit() {
        if (data.getStatus() != 2) {
            addTask(true, 1);
            data.setStatus(3);//未开始过任务 ——未在数据库保存过数据
        }
        data.setItems(gson.toJson(list));
        sumbmitBtn.setEnabled(false);
        commsubmit(data);
//        Intent intent = new Intent(this, SupervisorQueCreatActivity.class);
// ent.putExtra("formdata", data);
//        startActivity(intent);
      //  finish();
    }

    private void onBack() {
        if (!needAdd) {
            finish();
            return;
        }
        if (data.getDbid()>0) {
            data.setItems(gson.toJson(list));
            TaskUtils.onUpdateTaskById(data);
            Intent brodcastIntent = new Intent();
            brodcastIntent.setAction(CalendarMainActivity.UPDATA_ACTION);
            this.sendBroadcast(brodcastIntent);
            setResult(0x116);
            finish();
            return;
        } else {
            addTask(false, 2);

        }
        Intent brodcastIntent = new Intent();
        brodcastIntent.setAction(CalendarMainActivity.UPDATA_ACTION);
        this.sendBroadcast(brodcastIntent);
        setResult(0x116);
        finish();
    }


    private Gson gson = new Gson();

    protected void setResult(JSONObject jsonObject) {
        JSONArray jarr = jsonObject.optJSONArray("item");
        if (null != jarr) {
            List<FormDetailListVo> lis1t = gson.fromJson(jarr.toString(),
                    new TypeToken<List<FormDetailListVo>>() {
                    }.getType());
            list.addAll(lis1t);
            for (FormDetailListVo fdlv : list) {
                for (FormDetailVo fdv : fdlv.getSub_items()) {
                    fdv.setScores(fdv.getBase_score());
                }
            }
            adapter.setdata(list);
        }

        FormDataCache.saveFormListItemData(mcontext, id, jsonObject.toString());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent redata) {
        super.onActivityResult(requestCode, resultCode, redata);
        if (resultCode == 0x171 && null != redata) {
            sumbmitlay.setVisibility(View.VISIBLE);
            list = (List<FormDetailListVo>) redata.getSerializableExtra("data");
            double score = 0;
            if (formtype == 4) {//累加差量
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getItemTotalScore() != -1) {
                        score += list.get(i).getItemTotalScore();
                    } else {
                        score += list.get(i).getScore();
                    }
                }

            } else {
                for (FormDetailListVo fdlv : list) {
                    for (FormDetailVo fdv : fdlv.getSub_items()) {
                        score += fdv.getScores();
                    }
                }
            }
            totalScoreTv2.setText("总分："
                    + AbStrUtil.reMoveUnUseNumber(score + "") + "分");
            totalScore = score;
            totalScoreTv2.setText("质量标准" + "(总分："
                    + AbStrUtil.reMoveUnUseNumber(totalScore + "") + ")");
            needAdd = true;
            adapter.setdata(list);
        } else if (resultCode == 0x172 && null != data) {
            TaskVo remarkVo = (TaskVo) redata
                    .getSerializableExtra("remarkdata");
            String remarkStr = gson.toJson(remarkVo);
            list.get(markParentPosition).getSub_items().get(markitemPosition)
                    .setRemark(remarkStr);
            list.get(markParentPosition).getSub_items().get(markitemPosition)
                    .setRemark(true);
            list.get(markParentPosition).getSub_items().get(markitemPosition)
                    .setRemarkVo(remarkVo);
            needAdd = true;
            adapter.setdata(list);
        }

    }

    public void putdata(int position) {
        if (isOnlyPriview) {
            return;
        }
        // 第二次跳转方式

        Intent it = new Intent(mcontext, FormSettingActivity.class);
        it.putExtra("list", (Serializable) list);
        it.putExtra("position", position);
        it.putExtra("data", data);
        it.putExtra("updated", data.getTask_id() > 0);
        it.putExtra("title", title);
        it.putExtra("vo", list.get(position));
        it.putExtra("type", formtype);
        startActivityForResult(it, 0x117);
    }

    private int markParentPosition;
    private int markitemPosition;

    public void toRemark(int position, int itemPosition) {
        this.markParentPosition = position;
        this.markitemPosition = itemPosition;
        Intent it = new Intent(mcontext, SupervisionActivity.class);
        TaskVo remarkVo = list.get(position).getSub_items().get(itemPosition)
                .getRemarkVo();
        if (null != remarkVo) {
        }
        if (null == remarkVo || AbStrUtil.isEmpty(remarkVo.getCheck_content())) {
            remarkVo = data;
            remarkVo.setCheck_content(list.get(position).getSub_items()
                    .get(itemPosition).getDescribes());
        }

        it.putExtra("data", remarkVo);
        it.putExtra("isRemark", "1");
        startActivityForResult(it, 0x117);

    }

    public void  addTask(boolean upload, int state) {
        data.setTypes_info(title);
        data.setTotalscore(totalScore);
        data.setFormId(id);
        data.setName(title);
        data.setFormType(formtype);
        data.setHospital(tools.getValue(Constants.HOSPITAL_ID));
        data.setDepartment(data.getDepartment());
        data.setMain_remark(data.getMain_remark());
        data.setType("3");
        data.setTmp_id(id);
        data.setMobile(tools.getValue(Constants.MOBILE));
        data.setStatus(state);
        data.setScore(totalScore + "");
        data.setGrid_type(formtype + "");
        data.setFormType(formtype);
        // tv.setName(title);
        data.setDepartmentName(data.getDepartmentName());
        data.setItems(TaskUtils.gson.toJson(list));
        if (!upload) {//返回保存
            TaskUtils.onAddTaskInDb(data);
        }
    }
}
