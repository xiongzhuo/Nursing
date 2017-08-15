package com.deya.hospital.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.deya.acaide.R;
import com.deya.hospital.descover.SurroundFragemtsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/9/20
 */
public abstract class BasePagerFragmentActivity extends BaseFragmentActivity {
    private ViewPager shoppager;
    private SurroundFragemtsAdapter myadapter;
    private List<Fragment> listfragment;
    private RadioGroup mGroup;
    BaseFragment fragment1;
    BaseFragment fragment2;
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        } else {
            setContentView(R.layout.layout_pagerfragment_base);
        }

        layoutInflater = LayoutInflater.from(mcontext);
        initView();
        initChildView();
    }

    protected abstract void initChildView();

    private void initView() {

        mGroup = (RadioGroup) this.findViewById(R.id.group);
        mGroup.setOnCheckedChangeListener(new CheckedChangeListener());
        shoppager = (ViewPager) this.findViewById(R.id.order_pager);
        shoppager.setSaveEnabled(false);
        listfragment = new ArrayList<Fragment>();
        myadapter = new SurroundFragemtsAdapter(getSupportFragmentManager(),
                listfragment);
        fragment1 = getFragment1();
        fragment2 = getFragment2();
        listfragment.add(fragment1);
        listfragment.add(fragment2);


        mGroup = (RadioGroup) this.findViewById(R.id.group);
        mGroup.setOnCheckedChangeListener(new CheckedChangeListener());
        shoppager.setOnPageChangeListener(new PageChangeListener());
        shoppager.setOffscreenPageLimit(2);
        shoppager.setAdapter(myadapter);
        RadioButton radio_frist = (RadioButton) this.findViewById(R.id.radio_frist);
        mGroup.check(R.id.radio_frist);
        RadioButton radio_second = (RadioButton) this.findViewById(R.id.radio_second);
        mGroup.check(R.id.radio_frist);

        radio_frist.setText(getRadioText1());
        radio_second.setText(getRadioText2());
        if (getLayoutId() > 0) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.main);
            View view = layoutInflater.inflate(getLayoutId(), null);

        }

        findViewById(R.id.signInTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    protected abstract int getLayoutId();

    protected abstract String getRadioText2();

    protected abstract String getRadioText1();

    public abstract BaseFragment getFragment1();

    public abstract BaseFragment getFragment2();


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class CheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radio_frist:
                    shoppager.setCurrentItem(0);
                    // searchGv.setVisibility(View.VISIBLE);

                    break;
                case R.id.radio_second:
                    //   searchGv.setVisibility(View.GONE);
                    shoppager.setCurrentItem(1);
                    break;

            }


        }

    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mGroup.check(R.id.radio_frist);

                    break;
                case 1:
                    mGroup.check(R.id.radio_second);


                    break;

            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }


}
