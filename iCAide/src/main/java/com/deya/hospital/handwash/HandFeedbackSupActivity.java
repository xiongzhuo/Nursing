package com.deya.hospital.handwash;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseCommonTopActivity;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/4/10
 */
public class HandFeedbackSupActivity extends BaseCommonTopActivity {
    private TextView discover;
    TextView feedNameTex;
    TextView isTrainedTex;
    TextView facilitiesTex;
    TextView unRulesTex;
    Button shareBtn;

    @Override
    public String getTopTitle() {
        return "手卫生(现场反馈)";
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.hand_feedback_sup);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initBaseData() {

    }

    @Override
    public void initView() {
        discover = findView(R.id.discover);
        feedNameTex=findView(R.id.feedNameTex);
        isTrainedTex=findView(R.id.isTrainedTex);
        facilitiesTex=findView(R.id.facilitiesTex);
        unRulesTex=findView(R.id.unRulesTex);
        shareBtn=findView(R.id.shareBtn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
