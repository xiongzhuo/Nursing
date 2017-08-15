package com.deya.hospital.workspace;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.descover.SurroundFragemtsAdapter;
import com.deya.hospital.shop.SignInActivity;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.Tools;
import com.deya.hospital.workspace.workspacemain.TodayDynamicFragment;
import com.deya.hospital.workspace.workspacemain.WorkClassifyFragment;
import com.example.calendar.CalendarMainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class WorkSpaceMainFragment extends BaseFragment implements View.OnClickListener {
    private ViewPager shoppager;
    private SurroundFragemtsAdapter myadapter;
    private List<Fragment> listfragment;
    private RadioGroup mGroup;
    TodayDynamicFragment fragment1;
    WorkClassifyFragment fragment2;
    private LayoutInflater inflater;
    private Tools tools;
    ImageView falshImg;
    private AnimationDrawable falshdrawable;
    private LinearLayout workCalanderLay;
    private MyBrodcastReceiver brodcast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_workspace_main, container,
                    false);
            this.inflater = inflater;
            tools = new Tools(getActivity(), Constants.AC);
            initView();

        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }

    private void initView() {

        mGroup = (RadioGroup) rootView.findViewById(R.id.group);
        mGroup.setOnCheckedChangeListener(new CheckedChangeListener());
        shoppager = (ViewPager) rootView.findViewById(R.id.order_pager);
        shoppager.setSaveEnabled(false);
        listfragment = new ArrayList<Fragment>();
        myadapter = new SurroundFragemtsAdapter(getChildFragmentManager(),
                listfragment);
        fragment1 = new TodayDynamicFragment();
        fragment2 = new WorkClassifyFragment();
        listfragment.add(fragment1);
        listfragment.add(fragment2);


        mGroup = (RadioGroup) rootView.findViewById(R.id.group);
        mGroup.setOnCheckedChangeListener(new CheckedChangeListener());
        shoppager.setOnPageChangeListener(new PageChangeListener());
        shoppager.setOffscreenPageLimit(2);
        shoppager.setAdapter(myadapter);
        RadioButton radio_frist = (RadioButton) rootView.findViewById(R.id.radio_frist);
        radio_frist.setText("今日动态");
        mGroup.check(R.id.radio_frist);


        //初始化签到按钮动画
        this.findViewById(R.id.signInTv).setOnClickListener(this);
        falshImg = (ImageView) this.findViewById(R.id.falshImg);
        falshImg = (ImageView) this.findViewById(R.id.falshImg);
        falshImg.setBackgroundResource(R.drawable.canlender_flash);
        falshdrawable = (AnimationDrawable) falshImg.getBackground();
        falshdrawable.start();
        rootView.findViewById(R.id.calandarLay).setOnClickListener(this);
        registerBroadcast();

    }

    private void registerBroadcast() {
        brodcast = new MyBrodcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(TodayDynamicFragment.SUMBMIT_ACCTION)) {
                    shoppager.setCurrentItem(0);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TodayDynamicFragment.SUMBMIT_ACCTION);
        getActivity().registerReceiver(brodcast, intentFilter);
    }

    @Override
    public void onDestroy() {
        if (null != brodcast) {
            getActivity().unregisterReceiver(brodcast);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInTv:
                OnStartActivity(SignInActivity.class);
                break;
            case R.id.calandarLay:
                Intent intent = new Intent(getActivity(),
                        CalendarMainActivity.class);
                startActivity(intent);
                break;
        }

    }

    public void OnStartActivity(Class<?> T) {
        Intent inten = new Intent(getActivity(), T);
        startActivity(inten);
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
