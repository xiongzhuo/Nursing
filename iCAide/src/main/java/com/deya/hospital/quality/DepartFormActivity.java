package com.deya.hospital.quality;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseCommonTopFragmentActivity;
import com.deya.hospital.base.BaseTableFragment;
import com.deya.hospital.handwash.ItemTimesDetailActivity;
import com.deya.hospital.workspace.TaskUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/8
 */
public class DepartFormActivity extends BaseCommonTopFragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    RadioGroup radioGroup;
    ViewPager pager;
    List<View> imgviewList;
    RadioGroup group;
    TextView otherJobTv;
    public RadioButton rb0, rb1;
    public List<BaseTableFragment> fragmentList;
    MypagerFragmentAdapter mypagerFragmentAdapter;
    private String departmentId = "";

    @Override
    public String getTopTitle() {
        return getIntent().getStringExtra("name") + "报表";
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
        topView.showRightView(View.VISIBLE);
        topView.onRightClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartActivity(ItemTimesDetailActivity.class);

            }
        });
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
        rb0.setText("自查("+0+")");
        rb1.setText("抽查("+0+")");
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
        for (int i = 0; i < 2; i++) {
            QualitiFormFragment itemFragment = QualitiFormFragment.newInstance((i + 1) * 10, departmentId);
            fragmentList.add(itemFragment);
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
