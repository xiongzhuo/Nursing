package com.deya.hospital.quality;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseAddFileActivity;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.base.img.TipsDialogRigister;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.descover.SurroundFragemtsAdapter;
import com.deya.hospital.supervisor.PartTimeStaffDialog;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.ZformVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.priviewbase.BasePriviewInfoFragment;
import com.deya.hospital.workspace.priviewbase.BaseSupvisorFragment;

import org.json.JSONObject;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 *
 * @author : sunp
 * @date 2017/3/17
 */
public abstract class QualityBaseFormActivity extends BaseAddFileActivity implements RequestInterface, View.OnClickListener {
    private static final int REMOVE_SUPDETAIL_SUCESS =0x003 ;
    public BasePriviewInfoFragment basePriviewInfoFragment;
    public QualitySupvisorFragment supvisorFragment;
    public BaseSupvisorFragment baseSupvisorFragment;
    private CommonTopView topView;
    private Button sumbmitBtn;
    private Button shareBtn;
    private RadioGroup radioGroup;
    private ViewPager fragmentPager;
    private SurroundFragemtsAdapter myadapter;
    List<Fragment> listfragment;
    private PartTimeStaffDialog tipsDialog;
    public TipsDialogRigister baseTipsDialog;
    ZformVo.ListBean dataBean;
    TaskVo supVo;
    public List<ZformVo.ListBean.ItemListBean> dataList;
    CaseListVo.CaseListBean caseVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_priview_base);
        initView();
    }

    @Override
    public boolean getdefultState() {
        return false;
    }

    @Override
    public void onCheckAll(boolean ischeck) {

    }

    @Override
    public void AddImgFile(String name) {

    }

    @Override
    public void AddRecordFile(String name, double totalTime) {

    }

    private void initView() {
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.setTileRightImgVisble(View.VISIBLE);
        sumbmitBtn = (Button) this.findViewById(R.id.sumbmitBtn);
        sumbmitBtn.setOnClickListener(this);
        shareBtn = (Button) this.findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);


        topView.init(this);
        topView.setRigtext("拍照");
        topView.onRightClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecordPopWindow(R.id.mainLay, true, false);

            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb0:
                        fragmentPager.setCurrentItem(0);
                        break;
                    case R.id.rb1:
                        fragmentPager.setCurrentItem(1);
                        break;
                    case R.id.rb2:
                        fragmentPager.setCurrentItem(2);
                        break;
                }
            }
        });

        fragmentPager = (ViewPager) this.findViewById(R.id.fragmentPager);
        fragmentPager.setSaveEnabled(false);
        myadapter = new SurroundFragemtsAdapter(getSupportFragmentManager(),
                listfragment);
        fragmentPager.setAdapter(myadapter);
        tipsDialog = new PartTimeStaffDialog(mcontext, "您本次巡查任务是否完成？", "是,直接提交", "否,暂存",
                new PartTimeStaffDialog.PDialogInter() {
                    @Override
                    public void onEnter() {
                        //toAddCache();

                    }

                    @Override
                    public void onCancle() {
                      //  doSumbmit();
                    }
                });

        baseTipsDialog = new TipsDialogRigister(mcontext, new MyDialogInterface() {
            @Override
            public void onItemSelect(int postion) {

            }

            @Override
            public void onEnter() {
                //toAddCache();
            }

            @Override
            public void onCancle() {
                finish();
            }
        });

        if(getIntent().hasExtra("data")){
            dataBean= (ZformVo.ListBean) getIntent().getSerializableExtra("data");
            dataList=dataBean.getItem_list();
            initFragment();
        }


       // getFormDetailData();
    }

    private void initFragment() {
        caseVo=new CaseListVo.CaseListBean();
        basePriviewInfoFragment= SDBaseInfoFragmetn.newInstance(caseVo);
        listfragment.add(basePriviewInfoFragment);
        baseSupvisorFragment= QualityFormPriviewFragment.newInstance(dataList);
        listfragment.add(baseSupvisorFragment);
        supvisorFragment= QualitySupvisorFragment.newInstance(supVo,dataBean);
        listfragment.add(supvisorFragment);
        myadapter.notifyDataSetChanged();

    }

    private void getFormDetailData() {
        JSONObject job = new JSONObject();
        try {

            job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
           // job.put("tempId", tempId);
        } catch (Exception e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this,
                this,REMOVE_SUPDETAIL_SUCESS, job,
                "quality/tempDetail");
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        Log.i("1111111",jsonObject.toString());

    }

    @Override
    public void onRequestErro(String message) {

    }

    @Override
    public void onRequestFail(int code) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        if (tipsDialog != null && tipsDialog.isShowing()) {
            tipsDialog.dismiss();
        }
        if (null != baseTipsDialog && baseTipsDialog.isShowing()) {
            baseTipsDialog.dismiss();
        }
        super.onDestroy();
    }

    /**
     * @description 用于
     * @author sunp
     * @data ${date}
     * @modified 修改者，修改日期，修改内容
     * ${tags}
     */
    public abstract BaseFragment getBaseInfoFragment();
    public abstract BaseFragment getBaseFormFragment();
    public abstract BaseFragment getBaseSupervisorFragment();
}
