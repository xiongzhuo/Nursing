package com.deya.hospital.quality;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseCommonTopFragmentActivity;
import com.deya.hospital.base.BaseTableFragment;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.workspace.TaskUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/6
 */
public class QualityFormChooseActivity extends BaseCommonTopFragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, RequestInterface,View.OnClickListener {
    private static final int GET_DEPART_FORM =0x001 ;
    RadioGroup radioGroup;
    ViewPager pager;
    List<View> imgviewList;
    RadioGroup group;
    TextView otherJobTv;
    public RadioButton rb0, rb1;
    public List<BaseTableFragment> fragmentList;
    MypagerFragmentAdapter mypagerFragmentAdapter;
    private String departmentId = "";
    ImageView add_form;
    int type;
    private DisplayMetrics dm;

    @Override
    public String getTopTitle() {
        return getIntent().getIntExtra("type",0)==0?"选择考核模板":"getString(R.string.app_name)+精选模板";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_report_item_detail;
    }

    @Override
    public void initBaseData() {

    }

    @Override
    public void initView() {
        imgviewList = new ArrayList<>();
        radioGroup = findView(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        otherJobTv = findView(R.id.otherJobTv);
        pager = findView(R.id.pager);
        pager.setOnPageChangeListener(this);
        departmentId = getIntent().getStringExtra("departmentId");
        fragmentList = new ArrayList<>();
        radioGroup = findView(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        rb0 = findView(R.id.rb0);
        rb1 = findView(R.id.rb1);
        rb0.setText("按模板");
        rb1.setText("按科室");
        add_form= findView(R.id.add_form);
        add_form.setOnClickListener(this);
        dm = getResources().getDisplayMetrics();
        int itemWidth = dm.widthPixels;
        int itemHeight = (int) (itemWidth / 23 * 7);
        add_form.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));
        type=getIntent().getIntExtra("type",0);
        add_form.setVisibility(type==0?View.VISIBLE:View.GONE);
        add_form.setScaleType(ImageView.ScaleType.FIT_XY);
        radioGroup.setVisibility(View.GONE);
        otherJobTv = findView(R.id.otherJobTv);
        pager = findView(R.id.pager);
        pager.setOnPageChangeListener(this);
        mypagerFragmentAdapter = new MypagerFragmentAdapter(getSupportFragmentManager());
        getFragment();
        pager.setAdapter(mypagerFragmentAdapter);
        if (TaskUtils.mysticalJob(mcontext)) {
            otherJobTv.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
            otherJobTv.setText("暗访");
        }
    }

    public void getFragment() {
        findView(R.id.rb2).setVisibility(View.GONE);


        QualitiFormFragment itemFragment = QualitiFormFragment.newInstance(type, departmentId);
        fragmentList.add(itemFragment);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb0:
                pager.setCurrentItem(0);
                break;
            case R.id.rb1:
                pager.setCurrentItem(1);
                break;
            case R.id.rb2:
                pager.setCurrentItem(2);
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                radioGroup.check(R.id.rb0);
                break;
            case 1:
                radioGroup.check(R.id.rb1);
                break;
            case 2:
                radioGroup.check(R.id.rb2);
                break;

        }


    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {

    }

    @Override
    public void onRequestErro(String message) {

    }

    @Override
    public void onRequestFail(int code) {

    }

    @Override
    public void onClick(View v) {
        if(v==add_form){
            Intent intent=new Intent(mcontext,QualityFormChooseActivity.class);
            intent.putExtra("type",1);
            intent.putExtra("task_type",getIntent().getIntExtra("task_type",17));
            startActivity(intent);
        }
    }


    private class MypagerFragmentAdapter extends FragmentStatePagerAdapter {

        public MypagerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseTableFragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;//不保存tag
        }
    }
}