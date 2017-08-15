package com.deya.hospital.handwash;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseCommonTopFragmentActivity;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.workspace.TaskUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/12
 */
public class ItemTimesDetailActivity extends BaseCommonTopFragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    public RadioGroup radioGroup;
    ViewPager pager;
    TextView otherJobTv;
    public List<BaseFragment> fragmentList;
    MypagerFragmentAdapter mypagerFragmentAdapter;
    private String departmentId = "";

    @Override
    public String getTopTitle() {
        return getIntent().getStringExtra("name") + "报表";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_time_detail;
    }

    @Override
    public void initBaseData() {

    }

    @Override
    public void initView() {
        topView.setVisibility(View.GONE);
        departmentId = getIntent().getStringExtra("departmentId");
        fragmentList = new ArrayList<>();
        radioGroup = findView(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        otherJobTv = findView(R.id.otherJobTv);
        pager = findView(R.id.pager);
        pager.setOnPageChangeListener(this);
        findView(R.id.rback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        String time = getIntent().getStringExtra("time");
        if (TaskUtils.mysticalJob(mcontext)) {
            otherJobTv.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
            otherJobTv.setText("暗访");
            ReportTimeFragment itemFragment = ReportTimeFragment.newInstance(30, departmentId, time);
            fragmentList.add(itemFragment);
        } else {
            ReportTimeFragment itemFragment = ReportTimeFragment.newInstance(20, departmentId, time);
            fragmentList.add(itemFragment);
            ReportTimeFragment itemFragment2 = ReportTimeFragment.newInstance(10, departmentId, time);
            fragmentList.add(itemFragment2);
            ReportTimeFragment itemFragment3 = ReportTimeFragment.newInstance(30, departmentId, time);
            fragmentList.add(itemFragment3);
        }
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


    private class MypagerFragmentAdapter extends FragmentStatePagerAdapter {

        public MypagerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseFragment getItem(int position) {
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
