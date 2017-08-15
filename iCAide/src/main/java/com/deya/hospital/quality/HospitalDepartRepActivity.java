package com.deya.hospital.quality;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.deya.acaide.R;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/23
 */
public class HospitalDepartRepActivity extends QualityItemTimesDetailActivity {
    TextView descriptionTv;

    @Override
    public void getFragment() {
        descriptionTv=findView(R.id.descriptionTv);
        final RadioButton rb0= (RadioButton) this.findViewById(R.id.rb0);
        final RadioButton rb2= (RadioButton) this.findViewById(R.id.rb2);
        rb0.setText("自查(0)");

        rb2.setText("抽查(0)");
            MutiTimeFragment itemFragment =   MutiTimeFragment.newInstance(20,getIntent().getStringExtra("item_repo_id"));
            itemFragment.setNumLis(new MutiTimeFragment.DataRefesh() {
                @Override
                public void onNumRefesh(int num) {
                        rb0.setText("自查("+num+")");
                }
            }) ;


            fragmentList.add(itemFragment);
                MutiTimeFragment itemFragment2 =   MutiTimeFragment.newInstance(10,getIntent().getStringExtra("item_repo_id"));
                itemFragment2.setNumLis(new MutiTimeFragment.DataRefesh() {
                    @Override
                    public void onNumRefesh(int num) {
                            rb2.setText("抽查("+num+")");
                    }
                }) ;


                fragmentList.add(itemFragment2);
        findView(R.id.rb1).setVisibility(View.GONE);


        descriptionTv.setText(getIntent().getStringExtra("describe"));
        descriptionTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                radioGroup.check(R.id.rb0);
                break;
            case 1:
                radioGroup.check(R.id.rb2);
                break;
        }
    }
}
