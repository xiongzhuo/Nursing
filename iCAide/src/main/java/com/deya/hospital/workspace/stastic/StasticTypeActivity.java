package com.deya.hospital.workspace.stastic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragmentActivity;
import com.deya.hospital.descover.SurroundFragemtsAdapter;
import com.deya.hospital.util.CommonTopView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/9/20
 */
public class StasticTypeActivity extends BaseFragmentActivity {
    private ViewPager shoppager;
    private SurroundFragemtsAdapter myadapter;
    private List<Fragment> listfragment;
    StasticTypeListragment fragment1;
    CommonTopView topView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stastic_main);
        initView();
    }

    private void initView() {

        shoppager = (ViewPager) this.findViewById(R.id.order_pager);
        shoppager.setSaveEnabled(false);
        topView= (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);
        listfragment = new ArrayList<>();
        myadapter = new SurroundFragemtsAdapter(getSupportFragmentManager(),
                listfragment);
        fragment1 = new StasticTypeListragment();
        listfragment.add(fragment1);


        shoppager.setOnPageChangeListener(new PageChangeListener());
        shoppager.setOffscreenPageLimit(2);
        shoppager.setAdapter(myadapter);




    }



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

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }


}
