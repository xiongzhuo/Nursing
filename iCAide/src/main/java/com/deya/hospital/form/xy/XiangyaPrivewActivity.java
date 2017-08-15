package com.deya.hospital.form.xy;

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
import com.deya.hospital.base.DepartChoosePop;
import com.deya.hospital.base.DepartChoosePop.OnDepartPopuClick;
import com.deya.hospital.form.BaseFromPriViewActivity;
import com.deya.hospital.form.FormSettingActivity;
import com.deya.hospital.form.FormUtils;
import com.deya.hospital.form.xy.XyPreviewAdapter.previewAdapterInter;
import com.deya.hospital.supervisor.AddDepartmentActivity;
import com.deya.hospital.supervisor.SupervisionActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormDetailVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.WorkSpaceFragment;
import com.example.calendar.CalendarMainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class XiangyaPrivewActivity extends BaseFromPriViewActivity implements
        OnClickListener {
    private XyPreviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSaveLevels();
        if (getIntent().hasExtra("data")) {
            data = (TaskVo) getIntent().getSerializableExtra("data");
            title = data.getName();
            formtype = data.getFormType();
            totalScore = data.getTotalscore();
            id = data.getFormId();
        } else {
            data = new TaskVo();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initBaseData() {
        String time = getIntent().getStringExtra("time");
        time = TaskUtils.getTaskMissionTime(time);

        if (data.getDbid() <= 0||data.getStatus()==5) {
            data.setMission_time(time);
            getCacheDetail();
            initLookData();//非本院模板是否已添加
        } else {
            List<FormDetailListVo> lis1t = gson.fromJson(data.getItems()
                    .toString(), new TypeToken<List<FormDetailListVo>>() {
            }.getType());
            list.addAll(lis1t);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_prviewlist;
    }

    @Override
    public void initChildView() {
        intTopView();
        initView();
    }

    @Override
    public void saveCache(int status) {

    }

    @Override
    protected void onChooseDepartList(String name, String id) {
        departTv.setText(name);
        data.setDepartmentName(name);
        data.setDepartment(id);
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
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        sumbmitBtn = (Button) this.findViewById(R.id.sumbmitBtn);
        sumbmitBtn.setOnClickListener(this);
        // sumbmitBtn.setText("保\u3000存");
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
        sumbmitlay = (LinearLayout) this.findViewById(R.id.sumbmitlay);
        sumbmitlay.setVisibility(View.VISIBLE);
        formTitleTv = (TextView) this.findViewById(R.id.formTitleTv);
        formTitleTv.setText(title);

        totalScoreTv = (TextView) this.findViewById(R.id.totalScoreTv);
        totalScoreTv2 = (TextView) this.findViewById(R.id.totalScoreTv2);
        totalScoreTv2.setVisibility(View.VISIBLE);
        totalScoreTv2.setText("评分标准及内容" + "(总分："
                + AbStrUtil.reMoveUnUseNumber(totalScore + "") + ")");
        listView = (ListView) this.findViewById(R.id.ListView);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                toFormSetting(position);
            }
        });
        adapter = new XyPreviewAdapter(mcontext, false, list,
                new previewAdapterInter() {

                    @Override
                    public void onputData(int parentposition, int position) {
                        putdata(parentposition, position);

                    }

                    @Override
                    public void onRemark(int parentPosition, int position) {

                        toRemark(parentPosition, position);
                    }
                });
        listView.setAdapter(adapter);

        departDialog = new DepartChoosePop(mcontext, departlist,
                new OnDepartPopuClick() {

                    @Override
                    public void onChooseDepart(String name, String id) {

                        departTv.setText(name);
                        data.setDepartmentName(name);
                        data.setDepartment(id);
                    }

                    @Override
                    public void onAddDepart() {
                        Intent it = new Intent(mcontext,
                                AddDepartmentActivity.class);
                        it.putExtra("data", (Serializable) departlist);
                        startActivityForResult(it, 0x22);

                    }
                });

    }

    List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();

    private void getSaveLevels() {
        String str = SharedPreferencesUtil.getString(mcontext, "depart_levels",
                "");
        String childsStr = SharedPreferencesUtil.getString(mcontext,
                "departmentlist", "");
        List<ChildsVo> list2 = gson.fromJson(childsStr,
                new TypeToken<List<ChildsVo>>() {
                }.getType());
        List<ChildsVo> otherList = new ArrayList<ChildsVo>();
        if (!AbStrUtil.isEmpty(str)) {
            for (ChildsVo cv : list2) {
                if (cv.getParent().equals("0") || cv.getParent().equals("1")) {
                    otherList.add(cv);
                }
            }
            List<DepartLevelsVo> list = gson.fromJson(str,
                    new TypeToken<List<DepartLevelsVo>>() {
                    }.getType());
            departlist.addAll(list);
            for (DepartLevelsVo dlv : departlist) {
                if (dlv.getRoot().getId().equals("0")) {
                    if (dlv.getChilds().size() == 0) {
                        dlv.getChilds().addAll(otherList);
                        break;
                    }
                }
            }
            // TODO Auto-generated catch block
        } else {

            DepartLevelsVo dlv = new DepartLevelsVo();
            dlv.getRoot().setId("0");
            dlv.getRoot().setName("全部");
            dlv.getChilds().addAll(list2);
            departlist.add(dlv);

        }
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


    private void intTopView() {
        titleTv = (TextView) this.findViewById(R.id.title);
        rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
        rlBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                savaData();
                break;
            case R.id.sumbmitBtn:
                if (AbStrUtil.isEmpty(data.getDepartment())) {
                    ToastUtils.showToast(mcontext, "请先完善资料再保存");
                    departDialog.show();
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

    private void savaData() {
        if (!needAdd) {
            finish();
            return;
        }
        if (data.getDbid()>0) {
            data.setItems(gson.toJson(list));
           TaskUtils.onUpdateTaskById(data);
        } else {
            addTask(false, 2);
        }
        Intent brodcastIntent = new Intent();
        brodcastIntent.setAction(CalendarMainActivity.UPDATA_ACTION);
        this.sendBroadcast(brodcastIntent);
        setResult(0x116);
        finish();
    }

    @Override
    public void doSubmit() {
            addTask(true, 1);

    }


    private Gson gson = new Gson();

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent redata) {
        super.onActivityResult(requestCode, resultCode, redata);
        if (resultCode == 0x171 && null != redata) {
            sumbmitlay.setVisibility(View.VISIBLE);
            list.clear();
            List<FormDetailListVo> cachelist = (List<FormDetailListVo>) redata.getSerializableExtra("data");
            double score = 0;
            for (FormDetailListVo fdlv : cachelist) {
                for (FormDetailVo fdv : fdlv.getSub_items()) {
                    score += fdv.getScores();
                }
            }
            totalScoreTv2.setText("质量标准" + "(总分："
                    + AbStrUtil.reMoveUnUseNumber(score + "") + ")");
            totalScore = score;
            list.clear();
            needAdd = true;
            list.addAll(cachelist);
            adapter.notifyDataSetChanged();
        } else if (resultCode == 0x172 && null != redata) {
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
            adapter.notifyDataSetChanged();
        } else if (resultCode == 0x55 && null != redata) {
            TaskVo resut = (TaskVo) redata.getSerializableExtra("data");
            data.setDepartmentName(resut.getDepartmentName());
            data.setDepartment(resut.getDepartment());
            data.setWork_type(resut.getWork_type());
            data.setAddress(resut.getAddress());
            data.setUname(resut.getUname());
            data.setPpost(resut.getPpost());
            data.setUteam(resut.getUteam());
            data.setMission_time(resut.getMission_time());
            departTv.setText(resut.getDepartmentName() + "  "
                    + resut.getUname() + "  " + data.getAddress());
            setDepartLay();
            needAdd = true;
        } else if (resultCode == 0x22 && null != redata) {
            ChildsVo dv = (ChildsVo) redata.getSerializableExtra("data");
            String rooId = dv.getParent();
            for (DepartLevelsVo dlv : departlist) {
                if (rooId.equals("1") && dlv.getRoot().getId().equals("0")) {
                    // dlv.setChooseNum(dlv.getChooseNum() + 1);
                    dlv.getChilds().add(0, dv);
                } else if (dlv.getRoot().getId().equals(rooId)) {
                    // dlv.setChooseNum(dlv.getChooseNum() + 1);
                    dlv.getChilds().add(0, dv);
                }
            }
            departDialog.setdata(departlist);

        } else if (resultCode == 0x23 && null != redata) {
            ChildsVo dv2 = (ChildsVo) redata.getSerializableExtra("data");
            departTv.setText(dv2.getName());
            data.setDepartment(dv2.getId());
            data.setDepartmentName(dv2.getName());
        }

    }

    int settingitemPosition = -1;
    int settingparentPosition = -1;

    public void putdata(int parentposition, int position) {

        if (isOnlyPriview) {
            return;
        }
        this.settingitemPosition = position;
        this.settingparentPosition = parentposition;
        Intent it = new Intent(mcontext, XiangYaFormSettingActivity.class);
        it.putExtra("list", (Serializable) list);
        it.putExtra("position", position);
        it.putExtra("parentposition", parentposition);
        it.putExtra("data", data);
        it.putExtra("updated", data.getTask_id() > 0);
        it.putExtra("title", title);
        it.putExtra("vo", list.get(parentposition));
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
            remarkVo = new TaskVo();
            remarkVo.setCheck_content(list.get(position).getName()
                    + "\n"
                    + list.get(position).getSub_items().get(itemPosition)
                    .getDescribes());
        }

        it.putExtra("data", remarkVo);
        it.putExtra("isRemark", "1");
        startActivityForResult(it, 0x117);
    }

    private List<TaskVo> newTaskList = new ArrayList<TaskVo>();


    public void addTask(boolean upload, int state) {
        data.setTypes_info(title);
        data.setTotalscore(totalScore);
        data.setFormId(id);
        data.setName(title);
        data.setHospital(tools.getValue(Constants.HOSPITAL_ID));
        data.setType("6");
        data.setTmp_id(id);
        data.setMobile(tools.getValue(Constants.MOBILE));
        data.setStatus(state);
        data.setScore(totalScore + "");
        data.setGrid_type(formtype + "");
        data.setFormType(formtype);
        data.setDepartmentName(this.data.getDepartmentName());
        data.setItems(gson.toJson(list));
        if (upload) {
            commsubmit(data);
        } else {
            TaskUtils.onAddTaskInDb(data);
            Intent brodcastIntent = new Intent();
            brodcastIntent.setAction(WorkSpaceFragment.UPDATA_ACTION);
            sendBroadcast(brodcastIntent);
            setResult(0x116);
            finish();

        }

    }

    public void setDepartLay() {
        if (departTv.getText().length() > 0) {
            tipsTv.setVisibility(View.GONE);
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

}
