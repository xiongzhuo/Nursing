package com.deya.hospital.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.GsonUtils;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.widget.view.MultipleTextViewGroup;
import com.deya.hospital.widget.view.TextViewGroup;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/2
 */
public class DepartChooseActivity extends BaseActivity implements View.OnClickListener, TextWatcher, TextViewGroup.OnTextViewGroupItemClickListener, DepartTypesPop.TypeChooseInter {
    public static final int CHOOSE_SUC = 0x90;
    public static final int MUTI_CHOOSE_SUC = 0x91;
    public static final String MUTICHOOSE = "mutichoose";
    private int itemSum = 0;
    private LinearLayout historyLay;
    private ListView popListView;
    /*private LinearLayout lay_next;
    private Button btn_next;*/
    private MultipleTextViewGroup departgv;
    private EditText departmentTv;
    LinearLayout typesLay;
    TextView typesTv;
    DepartAdapter departAdapter;
    List<DepartVos.DepartmentListBean> deparList;
    List<DepartVos.DepartmentListBean> outoList;
    List<DepartVos.DepartmentTypeListBean> departTypeList;
    DepartTypesPop departTypePop;
    ArrayList<DepartVos.DepartmentListBean> choosedList = new ArrayList<>();
    List<DepartVos.DepartmentListBean> historyList = new ArrayList<>();
    List<TextViewGroup.TextViewGroupItem> reasonList = new ArrayList<>();
    boolean isMutiChoose;
    CommonTopView topView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhe2_depart_choose_lay);
        initView();
    }

    private void initView() {
        deparList = new ArrayList<>();
        outoList = new ArrayList<>();
        departTypeList = new ArrayList<>();
       /* lay_next = (LinearLayout) findViewById(R.id.lay_next);
        btn_next = (Button) findViewById(R.id.btn_next);*/
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);
        if (getIntent().getIntExtra(MUTICHOOSE, 0) == 1) {
            isMutiChoose = true;
//            lay_next.setVisibility(View.VISIBLE);
            topView.setRigtext("确认");
            topView.onRightClick(this, this);
            topView.setLeftImagvisibility(CommonTopView.GONE);
            topView.setLefttext("取消");
        }
        typesTv = findView(R.id.typesTv);
        historyLay = (LinearLayout) this.findViewById(R.id.historyLay);
        popListView = (ListView) this.findViewById(R.id.poplist);

//        btn_next.setOnClickListener(this);
        departgv = (MultipleTextViewGroup) this.findViewById(R.id.main_rl);
        departgv.setOnTextViewGroupItemClickListener(this);
        departmentTv = (EditText) this.findViewById(R.id.departmentTv);
        departmentTv.addTextChangedListener(this);
        typesLay = findView(R.id.typesLay);
        typesLay.setOnClickListener(this);
        departAdapter = new DepartAdapter(mcontext, outoList);
        popListView.setAdapter(departAdapter);
        departTypePop = new DepartTypesPop(mcontext, departTypeList);
        departTypePop.setChooseListener(this);
        getHistoryDepart();
        getDeparts();
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isMutiChoose) {
                    DepartVos.DepartmentListBean bean = outoList.get(position);
                    if(bean.getIsChoosed()){
                        itemSum--;
                    }else {
                        itemSum++;
                    }
                    topView.setTitle(jointTitle(itemSum));
                    bean.setIsChoosed(!bean.getIsChoosed());
                    departAdapter.notifyDataSetChanged();
//                    btn_next.setEnabled(true);
                } else {
                    addHistoryDepart(outoList.get(position));
                    setChooseResult(outoList.get(position));
                }
            }
        });
    }

    public String jointTitle(int num) {
        StringBuffer sb = new StringBuffer();
        sb.append("选择科室");
        if (num>0){
            sb.append("(");
            sb.append(num);
            sb.append(")");
        }
        return sb.toString();
    }

    public void getDeparts() {
        String strs = SharedPreferencesUtil.getString(mcontext, "new_depart_data", "");
        DepartVos vos = GsonUtils.JsonToObject(strs, DepartVos.class);
        initListData(vos);
    }

    public void initListData(DepartVos vos) {
        deparList.addAll(vos.getDepartmentList());
        outoList.addAll(deparList);
        departTypeList.addAll(vos.getDepartmentTypeList());
        DepartVos.DepartmentTypeListBean typeBean = new DepartVos.DepartmentTypeListBean();
        typeBean.setId(0);
        typeBean.setName("未分类");
        departTypeList.add(typeBean);
        departAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                for (DepartVos.DepartmentListBean bean : outoList) {
                    for (DepartVos.DepartmentListBean choosedBean : choosedList) {
                        if (choosedBean.getId() == bean.getId()) {
                            bean.setIsChoosed(false);
                            break;
                        }
                    }
                    if (bean.getIsChoosed()) {
                        choosedList.add(bean);
                    }
                }
                Intent intent = new Intent(MUTICHOOSE);
                intent.putExtra("departs", choosedList);
                setResult(CHOOSE_SUC, intent);
                finish();
                break;
            case R.id.typesLay:
                departTypePop.show();
                break;
        }

    }

    public void getHistoryDepart() {
        String str = SharedPreferencesUtil.getString(mcontext,
                tools.getValue(Constants.MOBILE) + "history_departs",
                "");
        if (null != str && str.length() > 0) {
            List<DepartVos.DepartmentListBean> hyList = TaskUtils.gson.fromJson(str,
                    new TypeToken<List<DepartVos.DepartmentListBean>>() {
                    }.getType());
            TextViewGroup.TextViewGroupItem item = null;
            for (DepartVos.DepartmentListBean hdv : hyList) {
                historyList.add(hdv);
                item = departgv.NewTextViewGroupItem();
                item.setText(hdv.getName());
                item.setDepartment(hdv.getId() + "");
                if (reasonList.size() < 6) {
                    reasonList.add(item);
                }
            }
            departgv.setTextViews(reasonList);
        }
        if (historyList.size() <= 0) {
            historyLay.setVisibility(View.GONE);
        } else {
            historyLay.setVisibility(View.VISIBLE);
        }
    }


    public void addHistoryDepart(DepartVos.DepartmentListBean bean) {
        boolean add = true;
        for (DepartVos.DepartmentListBean hBean : historyList) {
            if (bean.getId() == hBean.getId()) {
                add = false;
                historyList.remove(hBean);
                historyList.add(0, hBean);//如果已经存在，将此对象移动到第一个位置
                break;
            }
        }
        if (add) {
            historyList.add(0, bean);
        }
        SharedPreferencesUtil.saveString(mcontext,
                tools.getValue(Constants.MOBILE)
                        + "history_departs",
                TaskUtils.gson.toJson(historyList));

    }

    private void setChooseResult(DepartVos.DepartmentListBean bean) {
        Intent intent = new Intent();
        intent.putExtra("departData", bean);
        setResult(CHOOSE_SUC, intent);
        finish();
    }

    @Override
    public void OnTextViewGroupClick(View view, List<TextViewGroup.TextViewGroupItem> _dataList, TextViewGroup.TextViewGroupItem item) {
        DepartVos.DepartmentListBean bean = new DepartVos.DepartmentListBean();
        try {
            bean.setId(Integer.parseInt(item.getDepartment()));
            bean.setName(item.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isMutiChoose) {
            boolean add = true;
            for (DepartVos.DepartmentListBean listbean : choosedList) {
                if (listbean.getId() == bean.getId()) {
                    add = false;
                    choosedList.remove(listbean);//取消选择
                    break;
                }
            }
            if (add) {
                choosedList.add(bean);
            }

        } else {
            setChooseResult(bean);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        outoList.clear();
        if (s.length() > 0) {
            outoList.clear();
            typesTv.setText("");
            for (DepartVos.DepartmentListBean bean : deparList) {
                if (bean.getName().contains(s.toString()) || s.toString().contains(bean.getName())) {
                    outoList.add(bean);
                }
            }


        } else {
            outoList.addAll(deparList);
        }
        departAdapter.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onChoose(String name, int id) {
        typesTv.setText(name);
        outoList.clear();
        for (DepartVos.DepartmentListBean bean : deparList) {
            if (bean.getParent() == id) {
                outoList.add(bean);
            }
        }
        departAdapter.notifyDataSetChanged();

    }

    class DepartAdapter extends DYSimpleAdapter<DepartVos.DepartmentListBean> {
        public DepartAdapter(Context context, List<DepartVos.DepartmentListBean> list) {
            super(context, list);
        }

        @Override
        protected void setView(int position, View convertView) {
            TextView listtext = BaseViewHolder.get(convertView, R.id.listtext);
            ImageView check_img = BaseViewHolder.get(convertView, R.id.check_img);
            DepartVos.DepartmentListBean bean = getItem(position);
            listtext.setText(bean.getName());
            check_img.setVisibility(bean.getIsChoosed() ? View.VISIBLE : View.GONE);

        }

        @Override
        public int getLayoutId() {
            return R.layout.fivelist_item;
        }
    }

}
