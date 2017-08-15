package com.deya.hospital.workcircle.knowledge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.SimpleSwitchButton;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.KnowledgeVo;
import com.deya.hospital.workspace.TaskUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/9/27
 */
public class KnowledgeDetailActivity extends BaseActivity {
    private TextView nameTv;
    private TextView totalScoreTv;
    TextView rightNum;
    TextView wrongNumTv;
    TextView submitTime;
    ListView listView;
    CommonTopView topView;
    KnowLedgeDetailAdapter adapter;
    KnowledgeVo knowledgeVo = new KnowledgeVo();
    LayoutInflater layoutInflater;
    List<KnowledgeVo.ListBean> list = new ArrayList<>();
    List<KnowledgeVo.ListBean> showList = new ArrayList<>();
    int wrongNum;
    SimpleSwitchButton showAllBtn;
    int score;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_detail);
        initData();
        initView();

    }

    private void initData() {
        showList.clear();
        knowledgeVo = (KnowledgeVo) getIntent().getSerializableExtra("vo");
        for (KnowledgeVo.ListBean bean : knowledgeVo.getList()) {
            if (!bean.getChooseAswer().equals(bean.getRightAswer())) {
                bean.setRight(false);
                list.add(bean);
                wrongNum++;
            } else {
                if (!AbStrUtil.isEmpty(bean.getS_score())) {
                    score += Integer.parseInt(bean.getS_score());
                }

            }

        }
        showList.addAll(list);
    }

    private void initView() {
        layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.knowledge_detaillist_headview, null);
        showAllBtn= (SimpleSwitchButton) view.findViewById(R.id.showAll);
        showAllBtn.setCheck(false);
        showAllBtn.setText("显示全部");
        showAllBtn.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
            @Override
            public void onCheckChange(boolean ischeck) {

                if(ischeck){
                    showList.clear();
                    showList.addAll(knowledgeVo.getList());
                }else{
                    showList.clear();
                    showList.addAll(list);
                }
                adapter.notifyDataSetChanged();
            }
        });
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);
        nameTv = (TextView) view.findViewById(R.id.nameTv);
        totalScoreTv = (TextView) view.findViewById(R.id.totalScoreTv);
        totalScoreTv.setText(score + "分");
        rightNum = (TextView) view.findViewById(R.id.rightNum);
        int num = knowledgeVo.getList().size() - list.size();
        rightNum.setText("正确" + num + "");
        wrongNumTv = (TextView) view.findViewById(R.id.wrongNum);
        wrongNumTv.setText("错误" + list.size() + "");
        nameTv.setText(tools.getValue(Constants.NAME));
        submitTime = (TextView) view.findViewById(R.id.submitTime);
        submitTime.setText("提交时间" + TaskUtils.getLoacalDate());
        listView = (ListView) this.findViewById(R.id.listView);
        listView.addHeaderView(view);
        adapter = new KnowLedgeDetailAdapter(mcontext, showList);
        listView.setAdapter(adapter);



        if(knowledgeVo.getArticle_src().equals("2")){
            url =  DoucmentUitl.getShareUrl(knowledgeVo.getTest_id());
        }else{
        url =  WebUrl.WEB_ARTICAL
                +knowledgeVo.getTest_id();
        }
        topView.onRightClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(url);
            }
        });
    }

    private void showShare(String url) {
        showShareDialog( "文章分享",
                knowledgeVo.getTitle(),
                url);
    }
}
