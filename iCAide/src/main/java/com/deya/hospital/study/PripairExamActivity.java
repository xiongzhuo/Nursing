package com.deya.hospital.study;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseCommonTopActivity;
import com.deya.hospital.base.DyQrCodeActivity;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.ExzanminVo;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/25
 */
public class PripairExamActivity extends BaseCommonTopActivity implements View.OnClickListener {
    ExzanminVo.ListBean bean;
    TextView titleTv;
    TextView numsTv;
    TextView totalTimeTv;
    Button startBtn, shareBtn;
    private int type;
    DyQrCodeActivity dyQrCodeDialog;

    @Override
    public String getTopTitle() {
        type = getIntent().getIntExtra("type", 1);
        return type == 2 ? "培训资料" : "知识评测";
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_exam_pripair;
    }

    @Override
    public void initBaseData() {
        bean = (ExzanminVo.ListBean) getIntent().getSerializableExtra("data");
        titleTv.setText(bean.getTitle());
        numsTv.setText("题目:" + bean.getSubject_count() + "题");
        totalTimeTv.setText("时间:" + bean.getMins() + "分钟");
    }

    @Override
    public void initView() {
        titleTv = findById(R.id.titleTv);
        numsTv = findById(R.id.numsTv);
        totalTimeTv = findById(R.id.totalTimeTv);
        startBtn = findById(R.id.startBtn);
        startBtn.setOnClickListener(this);
        shareBtn = findById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);
        findView(R.id.qrCodeBtn).setOnClickListener(this);

    }

    public <T extends View> T findById(int id) {
        return (T) this.findViewById(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startBtn:
                Intent intent = new Intent(mcontext, ExzanminPrivewActivity.class);
                intent.putExtra("article_id", bean.getId() + "");
                intent.putExtra("article_src", "3");
                intent.putExtra("data", bean);
                intent.putExtra("title", getTopTitle());
                startActivity(intent);
                finish();
                break;
            case R.id.shareBtn:
                 showShareDialog(getTopTitle(), bean.getTitle(), WebUrl.PUBLIC_SERVICE_URL + "/gkgzj/test_home.html?id=" + bean.getId()+"&article_src=3&channel_id=3");
                break;

            case R.id.qrCodeBtn:
                Intent intent1=new Intent(mcontext,DyQrCodeActivity.class);
                intent1.putExtra("content",WebUrl.PUBLIC_SERVICE_URL + "/gkgzj/test_home.html?id=" + bean.getId()+"&article_src=3&channel_id=3");
               startActivity(intent1);
                break;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
