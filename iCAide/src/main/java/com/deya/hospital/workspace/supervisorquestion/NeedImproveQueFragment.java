package com.deya.hospital.workspace.supervisorquestion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.SupQuestionLisAdapter;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.base.DeletDialog2;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.LoadingView;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.SupervisorQestionVo;
import com.deya.hospital.widget.popu.PopuUnTimeReport;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONArray;
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
public class NeedImproveQueFragment extends BaseFragment implements
        View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    LinearLayout topcheckLay, popparent;
    CommonTopView topView;
    String title = "";
    String url = "";
    String shareMethod = "";
    int repotType;
    boolean isFirstLoad = true;
    PullToRefreshListView listView;
    List<SupervisorQestionVo> list;
    SupQuestionLisAdapter adapter;
    View empertyView;
    TextView empertyText;
    View view;
    Tools tools;
    Gson gson;
    Context mcontext;
    private MyHandler myHandler;
    DeletDialog2 deletDialog2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {

        if (view == null) {
            view = inflater.inflate(R.layout.circle_collection_activity,
                    container, false);
            mcontext = getActivity();
            tools = new Tools(getActivity(), Constants.AC);
            gson = new Gson();
            list = new ArrayList<SupervisorQestionVo>();
            initMyHandler();
            initListView();
            initCheckView();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }

        onloadJsMethod();

        return view;
    }


    TextView timeTxt, deapartTxt, identiTxt, searchText;
    private Button shareBtn;
    private ProgressDialog progDailog;
    private LoadingView loadingView;

    private void initCheckView() {
        topView = (CommonTopView) view.findViewById(R.id.topView);
//        topView.init(getActivity());
//        topView.onRightClick(getActivity(), new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(mcontext, SupQuestionSearchListActivity.class);
//                startActivity(it);
//
//            }
//        });
//        topView.setRigtext("查看全部");
//        topView.setTitle("待改进列表");
//        topView.setVisibility(View.GONE);


    }

    private void initListView() {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        empertyView = view.findViewById(R.id.empertyLay);
        empertyText = (TextView) view.findViewById(R.id.enpertytext);

        adapter = new SupQuestionLisAdapter(mcontext, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.getRefreshableView().setOnItemLongClickListener(this);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        deletDialog2 = new DeletDialog2(mcontext, "是否删除", new DeletDialog2.DeletInter() {
            @Override
            public void onDelet(int position) {
                deletQue(position);
                deletDialog2.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        onloadJsMethod();
    }

    private void initMyHandler() {
        myHandler = new MyHandler(getActivity()) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = myHandler.mactivity.get();
                if (null != activity) {
                    switch (msg.what) {
                        case GET_DATA_SUCESS:
                            dismissdialog();
                            if (null != msg && null != msg.obj) {
                                try {
                                    setDataResult(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e5) {
                                    e5.printStackTrace();
                                }
                            }
                            break;
                        case GET_DATA_FAIL:
                            dismissdialog();
                            listView.setVisibility(View.GONE);
                            empertyView.setVisibility(View.VISIBLE);
                            empertyText.setText("亲,您的网络不顺畅哦！");
                            break;

                        case DELET_DATA_SUCESS:
                            dismissdialog();
                            try {
                                setDelet(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }

                            break;
                        case DELET_DATA_FAIL:
                            dismissdialog();
                            ToastUtil.showMessage("亲,您的网络不顺畅哦!");
                            break;

                        default:
                            break;
                    }
                }
            }


        };
    }


    private void setDelet(JSONObject jsonObject) {
        ToastUtil.showMessage(jsonObject.optString("result_msg"));
        if (jsonObject.optInt("result_id") == 0) {
            onloadJsMethod();
        }


    }

    protected static final int GET_DATA_SUCESS = 0x7052;

    protected static final int GET_DATA_FAIL = 07053;
    protected static final int DELET_DATA_SUCESS = 0x7054;
    protected static final int DELET_DATA_FAIL = 07055;

    private void setDataResult(JSONObject jsonObject) {

        JSONArray jarr = jsonObject.optJSONArray("list");
        List<SupervisorQestionVo> cachelist = null;
        try {
            cachelist = TaskUtils.gson.fromJson(jarr.toString(),
                    new TypeToken<List<SupervisorQestionVo>>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != list && null != cachelist && cachelist.size() > 0) {
            list.clear();
            list.addAll(cachelist);
            adapter.notifyDataSetChanged();
        }

        if (list.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            empertyView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            empertyView.setVisibility(View.VISIBLE);
            empertyText.setText("请您还没有问题哦！");
        }


    }

    private void onloadJsMethod() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            if (getActivity().getIntent().hasExtra("origin")) {
                job.put("origin", getActivity().getIntent().getIntExtra("origin", 0));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("supvisor", job.toString());
        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
                GET_DATA_SUCESS, GET_DATA_FAIL, job, "supervisorQuestion/questionList");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            default:
                break;
        }
    }


    PopuUnTimeReport dialog;
    String emailTx = "";

    private MyBrodcastReceiver brodcast;


    String shareUrl = "";


    public void showPopLay(View view) {
        popparent.setVisibility(View.VISIBLE);
        popparent.removeAllViews();
        popparent.addView(view);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent it = new Intent(mcontext, SupervisorQesDetitalActivity.class);
        it.putExtra("id", list.get(position - 1).getId() + "");
        it.putExtra("state", list.get(position - 1).getState());
        it.putExtra("creatorId", list.get(position - 1).getUid());
        startActivity(it);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        deletDialog2.show();
        deletDialog2.setDeletPosition(position-1);
        return true;
    }

    private void deletQue(int position) {

        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("id", list.get(position).getId());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("supvisor", job.toString());
        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
                DELET_DATA_SUCESS, DELET_DATA_FAIL, job, "supervisorQuestion/deleteQuestion");
    }
}
