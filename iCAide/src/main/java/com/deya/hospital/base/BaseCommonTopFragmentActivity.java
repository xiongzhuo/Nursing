package com.deya.hospital.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.deya.acaide.R;
import com.deya.hospital.util.CommonTopView;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/21
 */
public abstract class BaseCommonTopFragmentActivity extends BaseFragmentActivity {
    public CommonTopView topView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBaseContentView();
        initBaseView();
        initView();
        initBaseData();
    }
    //用于重写
   public void  getBaseContentView(){
       setContentView(R.layout.comon_activity_lay);
    }
    protected  void initBaseView(){
        topView= (CommonTopView) this.findViewById(R.id.topView);
        topView.setTitle(getTopTitle());
        topView.init(this);
        View childView= LayoutInflater.from(mcontext).inflate(getLayoutId(),null);
        LinearLayout layout= (LinearLayout) this.findViewById(R.id.layout);
        layout.addView(childView);
    }
    public abstract String getTopTitle();
    public abstract int getLayoutId();
    public abstract void initBaseData();
    public abstract void initView();


}
