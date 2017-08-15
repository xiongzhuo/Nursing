package com.deya.hospital.base;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.ChildDepartListAdapter;
import com.deya.hospital.adapter.DepartListAdapter;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.widget.popu.MyPopu;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MutiDepartChoosePop extends BaseDialog {
    Context mcontext;
    List<ChildsVo> childList = new ArrayList<ChildsVo>();
    protected int firstPosition = 1;
    private ChildDepartListAdapter childAdapter;
    private ListView popListView;
    private ListView childListView;
    private DepartListAdapter departAdapter;
    TextView addDepart;
    View calcleView;
    List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
    private TextView cancle_seach;
    private EditText et_search;
    private LinearLayout top2;
    private String keyWords;
    private ImageView ic_cancel;
    private Tools tools = null;
    private List<ChildsVo> historyList = new ArrayList<>();
    private HashMap<String, ChildsVo> selectMap = new HashMap<String, ChildsVo>();
    Button submitBtn;

    public MutiDepartChoosePop(Context context, List<DepartLevelsVo> departlist,
                               OnDepartPopuClick onPopuClick) {
        super(context);
        this.mcontext = context;
        this.onPopuClick = onPopuClick;
        this.departlist.clear();
        this.departlist.addAll(departlist);
        this.childList.clear();
        if (departlist != null && departlist.size() > 0) {
            this.childList.addAll(departlist.get(0).getChilds());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_depart_pop);
        tools = new Tools(mcontext, Constants.AC);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = LayoutParams.MATCH_PARENT;
        int wh[] = AbViewUtil.getDeviceWH(mcontext);
        lp.height = wh[1] / 100 * 80;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        popListView = (ListView) this.findViewById(R.id.poplist);
        findViewById(R.id.submitLay).setVisibility(View.VISIBLE);
        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopuClick.onChooseDepart(selectMap);
                dismiss();
            }
        });
        top2 = (LinearLayout) this.findViewById(R.id.top2);
        et_search = (EditText) this.findViewById(R.id.et_search);
        cancle_seach = (TextView) this.findViewById(R.id.cancle_seach);
        ic_cancel = (ImageView) this.findViewById(R.id.ic_cancel);
        ic_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });
        cancle_seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
                // 先隐藏键盘
                ((InputMethodManager) et_search.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                cancle_seach.setVisibility(View.GONE);
                et_search.setCursorVisible(false);
                top2.setVisibility(View.VISIBLE);
                et_search.setHint("");
                popListView.setVisibility(View.VISIBLE);
            }
        });

        top2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                et_search.setCursorVisible(true);
                cancle_seach.setVisibility(View.VISIBLE);
                et_search.requestFocus();
                top2.setVisibility(View.GONE);
                et_search.setHint("搜索科室");
            }
        });

        departAdapter = new DepartListAdapter(mcontext, departlist);
        departAdapter.showAll(true);
        popListView.setAdapter(departAdapter);
        childAdapter = new ChildDepartListAdapter(mcontext, childList);
        childListView = (ListView) this.findViewById(R.id.levelList);
        childListView.setAdapter(childAdapter);

        addDepart = (TextView) this.findViewById(R.id.addDepart);
        if (tools.getValue(Constants.JOB) != null) {
            if (tools.getValue(Constants.JOB).equals("0") || tools.getValue(Constants.JOB).equals("1") || tools.getValue(Constants.JOB).equals("2")) {
                addDepart.setVisibility(View.VISIBLE);
            } else {
                addDepart.setVisibility(View.GONE);
            }
        }
        addDepart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onPopuClick.onAddDepart();
            }
        });
        popListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                firstPosition = arg2;
                for (DepartLevelsVo dlv : departlist) {
                    for (ChildsVo pcv : dlv.getChilds()) {
                        if (pcv.getId().equals(departlist.get(arg2).getRoot())) {
                            pcv.setChoosed(false);
                            dlv.setChooseNum(0);
                        }

                    }

                }
                if (arg2 == 0) {
                    selectMap.clear();
                    childList.clear();
                    departAdapter.setIsCheck(arg2);
                    childAdapter.notifyDataSetChanged();
                    onPopuClick.onChooseDepart(selectMap);
                    dismiss();
                } else {
                    DepartLevelsVo dv = departlist.get(arg2 - 1);

                    childList.addAll(dv.getChilds());
                    departAdapter.setIsCheck(arg2);
                }
                childAdapter.setData(childList);

            }

        });
        childListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int arg2,
                                    long id) {
                boolean isSelect = !childList.get(arg2).isChoosed();
                for (DepartLevelsVo dlv : departlist) {
                    for (ChildsVo pcv : dlv.getChilds()) {
                        if (pcv.getId().equals(childList.get(arg2).getId())) {
                            pcv.setChoosed(isSelect);
                            if (isSelect) {
                                dlv.setChooseNum(dlv.getChooseNum() + 1);
                            } else {
                                dlv.setChooseNum(dlv.getChooseNum() - 1);
                            }
                        }

                    }

                }
                childList.get(arg2).setChoosed(isSelect);
                Log.i("departchoose", isSelect + "");
                childAdapter.setData(childList);
                departAdapter.notifyDataSetChanged();

                if (isSelect) {
                    selectMap.put(childList.get(arg2).getId() + "",
                            childList.get(arg2));

                } else {
                    selectMap.remove(childList.get(arg2).getId() + "");
                }

            }
        });

        et_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                et_search.setCursorVisible(true);
                cancle_seach.setVisibility(View.VISIBLE);
                top2.setVisibility(View.GONE);
                et_search.requestFocus();
                et_search.setHint("搜索科室");
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (firstPosition == 0) {
                    return;
                }
                if (s.toString().trim().length() > 0) {
                    popListView.setVisibility(View.GONE);
                    childList.clear();
                    for (DepartLevelsVo dv : departlist) {
                        for (ChildsVo cv : dv.getChilds()) {
                            if (cv.getName().contains(s.toString())) {
                                childList.add(cv);
                            }
                        }

                    }
                    childAdapter.setData(childList);
                } else {
                    childList.clear();
                    popListView.setVisibility(View.VISIBLE);
                    if (departlist.size() > 1) {
                        childList.addAll(departlist.get(firstPosition).getChilds());
                    } else {
                        childList.addAll(departlist.get(0).getChilds());
                    }
                    childAdapter.setData(childList);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        getHistoryDepart();
    }

    private MyPopu myPopu = null;

    private LinearLayout lay1;
    private OnDepartPopuClick onPopuClick;

    public interface OnDepartPopuClick {
        public void onChooseDepart(Map<String, ChildsVo> map);

        public void onAddDepart();
    }

    public void setdata(List<DepartLevelsVo> departlist) {
        this.departlist.clear();
        this.departlist.addAll(departlist);
        notifyChange();

    }

    void notifyChange() {
        if (null != departAdapter) {
            departAdapter.notifyDataSetChanged();
            childAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void show() {
        notifyChange();
        super.show();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//隐藏键盘
    }

    public void setHistoryList(ChildsVo dv) {
        boolean needToAdd = true;
        for (ChildsVo dpv : historyList) {
            if (dpv.getId().equals(dv.getId())) {
                needToAdd = false;
            }
        }
        if (needToAdd) {
            historyList.add(0, dv);
            if (historyList.size() > 10) {
                historyList.remove(10);
            }
            SharedPreferencesUtil.saveString(mcontext,
                    tools.getValue(Constants.MOBILE)
                            + "history_used_departlist1",
                    TaskUtils.gson.toJson(historyList));
        }
    }


    public void getHistoryDepart() {
        historyList.clear();
        String str = SharedPreferencesUtil.getString(mcontext,
                tools.getValue(Constants.MOBILE) + "history_used_departlist1",
                "");
        if (null != str && str.length() > 0) {
            List<ChildsVo> hyList = TaskUtils.gson.fromJson(str,
                    new TypeToken<List<ChildsVo>>() {
                    }.getType());
            historyList.addAll(hyList);

        }
    }
}
