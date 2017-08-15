package com.deya.hospital.workspace.priviewbase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.consumption.ConsumptionFormActivity;
import com.deya.hospital.form.PreViewActivityDetial;
import com.deya.hospital.form.handantisepsis.HandDisinfectionPrivewActivity;
import com.deya.hospital.form.handantisepsis.HandPreViewActivityDetial;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.environment.EnviromentFormActivity;
import com.deya.hospital.workspace.handwash.HandWashTasksActivity;
import com.deya.hospital.workspace.multidrugresistant.MutiRisitantPriviewActivity;
import com.deya.hospital.workspace.other_forms.OtherFormActivity;
import com.deya.hospital.workspace.safeinjection.safeInjectionActivity;
import com.deya.hospital.workspace.site_infection.SiteInfectionPriviewAcitivty;
import com.deya.hospital.workspace.supervisorquestion.SupervisorQueCreatActivity;
import com.deya.hospital.workspace.threepips.ThreePipsPriviewActivity;
import com.deya.hospital.workspace.waste_disposal.WastFormActivity;
import com.deya.hospital.workspace.workspacemain.TodayDynamicFragment;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class FormCommitSucTipsActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 上传成功
     */
    public static final int UPLOAD_SUC = 0;
    /**
     * 上传失败
     */
    public static final int UPLOAD_FIAL = 1;
    /**
     * 网络未连接
     */
    public static final int NET_WORK_DISCONECT = 2;
    /**
     * 仅wifi状态下上传，当前处于流量状态
     */
    public static final int ONLY_WIFI = 3;
    LinearLayout shareLay;
    LinearLayout listLay;
    LinearLayout spaceLay;
    TextView tipTxt;
    TextView continueTxt;
    TaskVo taskVo;
    String type_id = "";
    String code = "";
    String id;
    ImageView ivStatus;
    boolean isolderForm;
    TextView tips;
    TextView caculateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_form_tips);
        if (getIntent().hasExtra("type_id")) {
            type_id = getIntent().getStringExtra("type_id");
            code = getIntent().getStringExtra("code");

        }
        taskVo = (TaskVo) getIntent().getSerializableExtra("data");
        Intent brodcastIntent = new Intent();
        brodcastIntent.setAction(TodayDynamicFragment.SUMBMIT_ACCTION);
        sendBroadcast(brodcastIntent);
        brodcastIntent.setAction(TodayDynamicFragment.UPDATA_ACTION);
        sendBroadcast(brodcastIntent);
        initView();
        addScore("2");
    }


    private void initView() {

        CommonTopView topView = (CommonTopView) this.findViewById(R.id.topView);

        listLay = (LinearLayout) this.findViewById(R.id.listLay);
        spaceLay = (LinearLayout) this.findViewById(R.id.spaceLay);
        tipTxt = (TextView) this.findViewById(R.id.tipTxt);
        ivStatus = (ImageView) this.findViewById(R.id.ivStatus);
        tips = (TextView) this.findViewById(R.id.tips);
        caculateTv = (TextView) this.findViewById(R.id.caculateTv);
        final int commit_status = getIntent().getIntExtra("commit_status", 1);
        String stateTxt = "";
        String title = "";
//        if (getIntent().hasExtra("classifyVo")&&taskVo.getType().equals("17")) {
//            ClassifyVo classifyVo = (ClassifyVo) getIntent().getSerializableExtra("classifyVo");
//                    caculateTv.setText(caculateTv.getText().toString() + "\n" + "得分:" + classifyVo.getSumbScore()+",总分:" + classifyVo.getToalScore() + "," + "扣分:"+(classifyVo.getToalScore()-classifyVo.getSumbScore()));
//
//
//
//        }
        int imgId = R.drawable.submit_fail;
        switch (commit_status) {
            case UPLOAD_SUC:
                stateTxt = "数据提交成功";
                title = "";
                imgId = R.drawable.submit_suc;
                break;
            case UPLOAD_FIAL:
                title = "上传失败";
                imgId = R.drawable.submit_fail;
                stateTxt = "数据提交失败，系统 会暂存您的数据，并尝试连上wifi后再次提交";
                break;
            case NET_WORK_DISCONECT:
                stateTxt = "当前网络不给力，系统会暂存您的数据，在连上网络后自动提交";
                imgId = R.drawable.network_diconnect;
                title = "待上传";
                break;
            case ONLY_WIFI:
                title = "待上传";
                imgId = R.drawable.network_diconnect;
                stateTxt = "您设置了仅在wifi状态下提交数据，系统会暂存您的数据，在连上wifi后自动提交";
                break;
            default:
                break;
        }
        tips.setText(stateTxt);
        tipTxt.setText(title);
        ivStatus.setImageResource(imgId);
        isolderForm = taskVo.getType() == "3" || taskVo.getType().equals("4") || taskVo.getType().equals("5");
//        if (isolderForm) {
//            TextView typesName = (TextView) this.findViewById(R.id.typesName);
//            typesName.setText("添加督导本");
//            if (commit_status != 0) {
//                listLay.setVisibility(View.GONE);
//            }
//        }
        topView.onbackClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.putExtra("data", taskVo);
                it.putExtra("type_id", type_id);
                it.putExtra("code", code);
                String taskType = taskVo.getType();
                int type = Integer.parseInt(taskType);
                switch (type) {
                    case 1:

                    case 2:
                        break;
                    case 3:
                        if (commit_status != 0) {
                            return;
                        }
                        it.setClass(mcontext, PreViewActivityDetial.class);
                        startActivity(it);
                        finish();
                        break;
                    case 5:
                        if (commit_status != 0) {
                            return;
                        }
                        it.setClass(mcontext, HandPreViewActivityDetial.class);
                        it.putExtra("data", taskVo);
                        startActivity(it);
                        break;
                    case 6:
                        if (commit_status != 0) {
                            return;
                        }
                        it.setClass(mcontext, PreViewActivityDetial.class);
                        it.putExtra("data", taskVo);
                        startActivity(it);
                        break;
                    case 7:
                        break;
                    case 8:
                        it.setClass(mcontext, MutiRisitantPriviewActivity.class);
                        startActivity(it);
                        break;
                    case 9:
                        it.setClass(mcontext, ThreePipsPriviewActivity.class);
                        startActivity(it);
                        break;
                    case 10:
                        it.setClass(mcontext, EnviromentFormActivity.class);
                        startActivity(it);
                        break;
                    case 11:
                        it.setClass(mcontext, safeInjectionActivity.class);
                        startActivity(it);
                        break;
                    case 12:
                        it.setClass(mcontext, SiteInfectionPriviewAcitivty.class);
                        startActivity(it);
                        break;
                    case 13:
                        it.setClass(mcontext, WastFormActivity.class);
                        startActivity(it);
                        break;
                    case 14:
                        it.setClass(mcontext, OtherFormActivity.class);
                        startActivity(it);
                        break;
                    default:
                        break;
                }
                finish();

            }
        });
        listLay.setOnClickListener(this);
        spaceLay.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.listLay:
                Intent it = new Intent();
                it.putExtra("time", TaskUtils.getLoacalDate());
                String taskType = taskVo.getType();
                int type = Integer.parseInt(taskType);
                switch (type) {
                    case 1:
                        it.setClass(mcontext, HandWashTasksActivity.class);
                        startActivity(it);
                        break;
                    case 2:
                        it.setClass(mcontext, SupervisorQueCreatActivity.class);
                        startActivityForResult(it, TodayDynamicFragment.ADD_SUPQUESTION);
                        break;
                    case 4:
                        it.setClass(mcontext, ConsumptionFormActivity.class);
                        TaskVo tv = new TaskVo();
                        tv.setMission_time(TaskUtils.getLoacalDate());
                        tv.setStatus(2);
                        it.putExtra("data", tv);
                        startActivity(it);
                        break;
                    case 5:
                        it.setClass(mcontext, HandDisinfectionPrivewActivity.class);
                        startActivity(it);
                        break;
                    case 7:
                        it.setClass(mcontext, SupervisorQueCreatActivity.class);
                        startActivityForResult(it, TodayDynamicFragment.ADD_SUPQUESTION);
                        break;
                    case 11:
                        it.setClass(mcontext, safeInjectionActivity.class);
                        it.putExtra("code", "SIP");
                        startActivity(it);
                        break;
                    case 13:
                        it.setClass(mcontext, WastFormActivity.class);
                        startActivity(it);
                        break;
                    default:
                        finish();
                        break;
                }
                finish();
                break;
            case R.id.spaceLay:
                Intent it2 = new Intent();
                it2.setClass(mcontext, MainActivity.class);
                startActivity(it2);
                finish();
                break;
            default:
                break;

        }

    }


}
