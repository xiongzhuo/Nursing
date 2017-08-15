package com.deya.hospital.study;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseCommonTopActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.SimpleSwitchButton;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.ExzanminVo;
import com.deya.hospital.vo.KnowledgeVo;
import com.deya.hospital.workcircle.knowledge.KnowLedgeDetailAdapter;
import com.deya.hospital.workspace.TaskUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/28
 */
public class ExzanmingShareActivity extends BaseCommonTopActivity {
    ImageView exzamingState;
    int state;
    ExzanminVo.ListBean data;
    TextView tips;
    String [] strs={"没错你就是美貌与智慧并重,英雄与侠义的化身","谁与我争锋,还差那么一丢丢...","俺是追梦的路人,不要打扰俺","嘿 该醒醒,起来看书啦..."};
    List<KnowledgeVo.ListBean> wrongList = new ArrayList<>();


    String stateName="";
    TextView useTimeTv;
    int seconds;
    Button moveBtn;

    @Override
    public void getBaseContentView() {
        setContentView(R.layout.comon_activity_white);
    }

    @Override
    public String getTopTitle() {
        return getIntent().hasExtra("title")?getIntent().getStringExtra("title"):"";
    }

    @Override
    public int getLayoutId() {
        return R.layout.dy_listview_lay2;
    }

    @Override
    public void initBaseData() {

    }

    @Override
    public void initView() {
        layoutInflater=LayoutInflater.from(mcontext);
        data = (ExzanminVo.ListBean) getIntent().getSerializableExtra("data");
        seconds=getIntent().getIntExtra("second",0);
        list= (List<KnowledgeVo.ListBean>) getIntent().getSerializableExtra("list");
        if(null==list){
            list=new ArrayList<>();
        }
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);
        View view = layoutInflater.inflate(R.layout.layout_exzanming_share, null);
        moveBtn= (Button) view.findViewById(R.id.moveBtn);
        tips= (TextView) view.findViewById(R.id.tips);
        exzamingState = (ImageView) view.findViewById(R.id.exzamingState);
        if (data.getScore() > 91) {
            tips.setText(tools.getValue(Constants.NAME)+strs[0]);
            AbStrUtil.setPiceTextCorlor(
                    getResources().getColor(R.color.black),
                    tips, tips.getText()
                            .toString(), tips.getText()
                            .toString().length(), strs[0].length());
            exzamingState.setImageResource(R.drawable.exzanming_state1 );
            stateName="感控达人称号";
        } else if (data.getScore() < 91 && data.getScore() >= 81) {
            exzamingState.setImageResource(R.drawable.exzanming_state2 );
            tips.setText(strs[1]);
            stateName="感控学霸称号";
        } else if (data.getScore() < 81 && data.getScore() >= 61) {
            exzamingState.setImageResource(R.drawable.exzanming_state3 );
            tips.setText(strs[2]);
            stateName="感控奋青称号";
        } else {
            tips.setText(strs[3]);
            exzamingState.setImageResource(R.drawable.exzanming_state4 );
            stateName="梦游侠称号";
        }



        nameTv = (TextView) view.findViewById(R.id.nameTv);
        totalScoreTv = (TextView) view.findViewById(R.id.totalScoreTv);
        totalScoreTv.setText(data.getScore() + "分");
        rightNum = (TextView) view.findViewById(R.id.rightNum);
        rightNum.setText("正确:" + data.getRightNum() + "");
        wrongNumTv = (TextView) view.findViewById(R.id.wrongNum);
        wrongNumTv.setText("错误:" + data.getWrongNum() + "");
        moveBtn.setText(data.getWrongNum()>0?"查看错题":"查看答题");

        nameTv.setText(tools.getValue(Constants.NAME));
        int min = seconds / 60;
        int s = seconds % 60;
        useTimeTv= (TextView) view.findViewById(R.id.useTimeTv);
        useTimeTv.setText("用时:"+"00:"+formatData(min + "") + ":" + formatData(s + ""));
        view.findViewById(R.id.shareBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog(data.getTitle(),getShareContent(),getUrl());
            }


        });

        TextView submitTime= (TextView) view.findViewById(R.id.submitTime);
        submitTime.setText("提交时间:"+ TaskUtils.getLoacalTextDate());
        showAllBtn= (SimpleSwitchButton) view.findViewById(R.id.showAll);
        showAllBtn.setCheck(false);
        showAllBtn.setText("显示全部");
        showAllBtn.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
            @Override
            public void onCheckChange(boolean ischeck) {
                if(ischeck){
                    showList.clear();
                    showList.addAll(list);
                }else{
                    showList.clear();
                    showList.addAll(wrongList);
                }
                adapter.notifyDataSetChanged();
            }
        });
        listView = (ListView) this.findViewById(R.id.listView);
        listView.addHeaderView(view);
        adapter = new KnowLedgeDetailAdapter(mcontext, showList);
        listView.setAdapter(adapter);
        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.getWrongNum()>0){
                    if(listView.getCount()>=2){
                        listView.smoothScrollToPosition(2);
                    }
                }else{
                    showList.clear();
                    showAllBtn.setCheck(true);
                    showList.addAll(list);
                    adapter.notifyDataSetChanged();
                    if(listView.getCount()>=2){
                        listView.smoothScrollToPosition(2);
                    }
                }
            }
        });
        initData();
    }
    private String formatData(String text) {


        return text.length() < 2 ? "0" + text : text;
    }
    private String getUrl() {
        JSONObject job=new JSONObject();
        try {
            job.put("id",data.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return WebUrl.LEFT_URL+"/subject/testShare?u="+ AbStrUtil.getBase64(job.toString());
    }

    String getShareContent(){

        return nameTv.getText().toString()+"得到"+data.getScore()+"分,"+"获得"+stateName+",我来挑战他";
    }
    private TextView nameTv;
    private TextView totalScoreTv;
    TextView rightNum;
    TextView wrongNumTv;
    ListView listView;
    CommonTopView topView;
    KnowLedgeDetailAdapter adapter;
    LayoutInflater layoutInflater;
    List<KnowledgeVo.ListBean> list = new ArrayList<>();
    List<KnowledgeVo.ListBean> showList = new ArrayList<>();
    SimpleSwitchButton showAllBtn;

    private void initData() {
        showList.clear();
        for (KnowledgeVo.ListBean bean : list) {
            if (!bean.getChooseAswer().equals(bean.getRightAswer())) {
                bean.setRight(false);
                wrongList.add(bean);
            }

        }
        showList.addAll(wrongList);
        adapter.notifyDataSetChanged();
    }
}
