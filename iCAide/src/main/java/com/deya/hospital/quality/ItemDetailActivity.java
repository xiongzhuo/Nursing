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
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.vo.QualityReportVo;
import com.deya.hospital.workspace.TaskUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/12
 */
public class ItemDetailActivity extends BaseCommonTopFragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    RadioGroup radioGroup;
    ViewPager pager;
    TextView otherJobTv;
    public List<BaseFragment> fragmentList;
    MypagerFragmentAdapter mypagerFragmentAdapter;
    String departmentId;
    QualityReportVo.ListBean listBean;
    int indext, item_repo_id;
    RadioButton rb0,rb1;

    @Override
    public String getTopTitle() {
        return getIntent().getStringExtra("departmentName");
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
//        topView.setRigtext("详情");
//        topView.showRightView(View.VISIBLE);
//        topView.onRightClick(this, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mcontext, ItemTimesDetailActivity.class);
//                intent.putExtra("name", getIntent().getStringExtra("departmentName"));
//                intent.putExtra("departmentId", departmentId);
//                startActivity(intent);
//
//            }
//        });
        if (getIntent().hasExtra("departmentId")) {
            departmentId = getIntent().getStringExtra("departmentId");

            listBean = (QualityReportVo.ListBean) getIntent().getSerializableExtra("listBean");

        }
        rb0= (RadioButton) this.findViewById(R.id.rb0);
        rb1= (RadioButton) this.findViewById(R.id.rb1);
        findView(R.id.rb2).setVisibility(View.GONE);
        indext = getIntent().getIntExtra("indext", 0);
        item_repo_id = getIntent().getIntExtra("item_repo_id", 0);
        fragmentList = new ArrayList<>();
        radioGroup = findView(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        otherJobTv = findView(R.id.otherJobTv);
        pager = findView(R.id.pager);
        pager.setOnPageChangeListener(this);
        mypagerFragmentAdapter = new MypagerFragmentAdapter(getSupportFragmentManager());
        getFragment();
        pager.setAdapter(mypagerFragmentAdapter);
        pager.setCurrentItem(indext);


    }

    public void getFragment() {
        if (TaskUtils.mysticalJob(mcontext)) {
            otherJobTv.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
            otherJobTv.setText("暗访");
            ItemFragment itemFragment = ItemFragment.newInstance(30, listBean);
            fragmentList.add(itemFragment);
        } else {
                ItemFragment itemFragment = ItemFragment.newInstance( 20, listBean);
                itemFragment.setOnGetNumLis(new ItemFragment.OnGetNumLis() {
                    @Override
                    public void OnSuc(int num) {
                          rb0.setText("自查("+num+")");

                    }
                });
                fragmentList.add(itemFragment);

            ItemFragment itemFragment2 = ItemFragment.newInstance( 10, listBean);
            itemFragment2.setOnGetNumLis(new ItemFragment.OnGetNumLis() {
                @Override
                public void OnSuc(int num) {
                    rb1.setText("抽查("+num+")");

                }
            });
            fragmentList.add(itemFragment2);

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
