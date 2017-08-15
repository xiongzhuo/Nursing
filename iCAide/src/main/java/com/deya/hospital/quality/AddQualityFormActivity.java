package com.deya.hospital.quality;

import android.view.View;

import com.deya.acaide.R;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/7/11
 */
public class AddQualityFormActivity extends QualityFormChooseActivity {
    @Override
    public void getFragment() {
        topView.setTitle("平台模板");
        findView(R.id.rb2).setVisibility(View.GONE);
            QualitiFormFragment itemFragment = QualitiFormFragment.newInstance(2,"");
            fragmentList.add(itemFragment);
    }
}
