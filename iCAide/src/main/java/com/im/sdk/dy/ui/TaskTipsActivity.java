package com.im.sdk.dy.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.account.QualificationActivity;
import com.deya.hospital.baseui.BaseRefreshListViewActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.IMTipsVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.supervisorquestion.SupervisorQesDetitalActivity;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

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
public class TaskTipsActivity extends BaseRefreshListViewActivity {
    private List<IMTipsVo> list = new ArrayList<IMTipsVo>();
    private TaskTipsAdapter adapter;
    private long threadId;
    String type = "0";


    @Override
    public void onListViewPullUp() {
        getListData();
    }

    @Override
    public void onListViewPullDown() {
        page = 1;
        isFirst = true;
        totalPage = 0;
        getListData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        isFirst = true;
        totalPage = 0;
        getListData();
    }

    @Override
    public void initView() {
        topView.init(this);
        topView.setTitle("全部");
        topView.getRigthtView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mcontext, WorkTipsSettingsActivity.class);
                startActivity(it);
            }
        }).setBackgroundResource(R.drawable.work_tips_img);
        topView.setTileRightImgVisble(View.VISIBLE);
        topView.setTitleViewClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopWindow(mcontext, topView);
            }
        });
        adapter = new TipsAdapter2(mcontext, list);
        listview.setAdapter(adapter);
        //   listview.getRefreshableView().setSelection(adapter.getCount()-1);
    }

    private void getListData() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("pageIndex", page + "");
            job.put("type", type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, this,
                GET_DATA_SUCESS, GET_DATA_FAIL, job, "msg/msgList");
    }

    @Override
    protected void setDataResult(JSONObject jsonObject) {

        if (!jsonObject.optString("result_id").equals("0")) {
            ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
            return;
        }
        List<IMTipsVo> cachelist = TaskUtils.gson.fromJson(jsonObject.optString("list"), new TypeToken<List<IMTipsVo>>() {
        }.getType());
        if (isFirst) {
            list.clear();
            isFirst = false;
        }
        totalPage = jsonObject.optInt("pageTotal", 0);
        if (totalPage > page) {
            listview.setMode(PullToRefreshBase.Mode.BOTH);
        } else {
            listview.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        }
        list.addAll(cachelist);
        adapter.notifyDataSetChanged();
        page++;
    }

    @Override
    protected void onItemclick(int position) {
        Intent it = new Intent();
        IMTipsVo tipsVo = list.get(position);
        switch (tipsVo.getType()) {

            case 1:
                it.setClass(mcontext, SupervisorQesDetitalActivity.class);
                it.putExtra("id", tipsVo.getId());
                startActivity(it);
                break;
            case 2:
                it.setClass(mcontext, QualificationActivity.class);
                it.putExtra("data", tipsVo.getData());
                startActivity(it);
                break;
        }


    }


//    private void getAllLocalMessage(){
//        try {
//            List<TipsMessage>    listCache = DataBaseHelper
//                    .getInstance()
//                    .getDbUtilsInstance(mcontext)
//                    .findAll(
//                            Selector.from(TipsMessage.class).where("mobile", "=", tools.getValue(Constants.MOBILE))
//                                    .orderBy("messageid"));
//            List<ECMessage> list2= IMessageSqlManager.queryIMessageList(threadId,10,null);
//            if(list2.size()>0){
//               for(ECMessage em:list2){
//                   Log.e("tipsMesaage",em.getUserData()+"----"+em.getBody());
//               }
//            }
//            if(null!=listCache){
//            list.addAll(list2);
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//    }


    public void showPopWindow(Context context, View parent) {
        int width = parent.getWidth();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopWindow = inflater.inflate(R.layout.sweetpopwindow, null,
                true);
        final PopupWindow popWindow = new PopupWindow(vPopWindow, width,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
        vPopWindow.findViewById(R.id.devider1).setVisibility(View.VISIBLE);
        TextView allTv = (TextView) vPopWindow.findViewById(R.id.allTv);
        allTv.setVisibility(View.VISIBLE);
        allTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topView.setTitle("全部");
                page = 1;
                isFirst = true;
                totalPage = 0;
                type = "0";
                getListData();
                popWindow.dismiss();
            }
        });

        TextView handView = (TextView) vPopWindow.findViewById(R.id.handView);
        handView.setText("工作提醒");
        handView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        topView.setTitle("工作提醒");
                        page = 1;
                        isFirst = true;
                        totalPage = 0;
                        type = "1";
                        getListData();
                        popWindow.dismiss();

                    }
                });
        TextView formview = (TextView) vPopWindow.findViewById(R.id.formview);
        formview.setText("注册提醒");
        vPopWindow.findViewById(R.id.formview).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        topView.setTitle("注册提醒");
                        page = 1;
                        isFirst = true;
                        totalPage = 0;
                        type = "2";
                        getListData();
                        popWindow.dismiss();

                    }
                });

        popWindow.setBackgroundDrawable(getResources().getDrawable(
                android.R.color.transparent));
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        int px = AbViewUtil.dip2px(mcontext, 10);
        popWindow.showAsDropDown(parent, AbViewUtil.dip2px(mcontext, 0), 0);
    }
}
