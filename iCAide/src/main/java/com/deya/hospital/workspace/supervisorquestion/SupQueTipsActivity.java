package com.deya.hospital.workspace.supervisorquestion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.workspacemain.TodayDynamicFragment;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class SupQueTipsActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout shareLay;
    LinearLayout listLay;
    LinearLayout spaceLay;
    TextView tipTxt;
    int tipIndex = -1;
    TextView continueTxt;
    TaskVo taskVo;
    String type_id = "";
    String code = "";
    String id;
    String[] tips = {"提交成功！发现的问题会统一\n归档到\"工作间-改进提醒\"中，\n方便您持续跟踪与改进！", "提交成功！系统会自动提醒相关\n人员查看，您也可以持续关注问题\n的改进效果。", "提交成功！问题已关闭，如果您需要查看，\n可以进入\"工作间-改进提醒-全部\"，\n按条件查询。"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supvisior_question_tips);
        if (getIntent().hasExtra("tipIndex")) {
            tipIndex = getIntent().getIntExtra("tipIndex", -1);
            id = getIntent().getStringExtra("id");
        }

        Intent brodcastIntent = new Intent();
        brodcastIntent.setAction(TodayDynamicFragment.SUMBMIT_ACCTION);
        this.sendBroadcast(brodcastIntent);
        initView();
        addScore("2");
    }


    private void initView() {
        shareLay = (LinearLayout) this.findViewById(R.id.shareLay);
        listLay = (LinearLayout) this.findViewById(R.id.listLay);
        spaceLay = (LinearLayout) this.findViewById(R.id.spaceLay);
        tipTxt = (TextView) this.findViewById(R.id.tipTxt);
        continueTxt = (TextView) this.findViewById(R.id.continueTxt);
        if (tipIndex >= 0) {
            tipTxt.setText(tips[tipIndex]);
        } else {
            tipTxt.setText("提交成功！");
            continueTxt.setText("继续督导");

        }
        shareLay.setOnClickListener(this);
        listLay.setOnClickListener(this);
        spaceLay.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent();

        switch (v.getId()) {
            case R.id.shareLay:
                    it.setClass(mcontext, SupervisorQesDetitalActivity.class);
                    it.putExtra("id", id);

                break;
            case R.id.listLay:
                if (tipIndex >= 0) {
                    it.setClass(mcontext, SupQuestionListActivity.class);
                } else {

                    finish();
                    return;
                }

                break;
            case R.id.spaceLay:
                it.setClass(mcontext, MainActivity.class);
                break;
            default:
                break;

        }
        startActivity(it);
        finish();
    }


}
