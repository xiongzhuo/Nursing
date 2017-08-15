package com.deya.hospital.workcircle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragmentActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.vo.HotVo;
import com.deya.hospital.workcircle.SearchPopWindow;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : yugq
 * @date 2016/8/19
 */
public class WorkCiecleChannelActivity extends BaseFragmentActivity {
    private Context mContext;
    private int id = -1;
    private CommonTopView topView;
    private String channelName="";
    private SearchPopWindow dWindow;
    private List<HotVo> hotList = new ArrayList<>();
    private Gson gson = new Gson();
    private EditText et_search;
    private LinearLayout searchLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_channel);
        bindView();
        setData();
        initFragment();
        getHotCache();
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    private void setData() {
        id = getIntent().getIntExtra("channelID", -1);
        channelName = getIntent().getStringExtra("channelName");
        if (!AbStrUtil.isEmpty(channelName)) {
            topView.setTitle(channelName);
            et_search.setHint("请输入"+channelName);
        } else {
            topView.setTitle("目录");
        }
    }

    private void bindView() {
        mContext = this;
        topView = (CommonTopView) findViewById(R.id.topView);
        topView.onbackClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_search = (EditText) findViewById(R.id.et_search);
        searchLay = (LinearLayout) findViewById(R.id.searchLay);
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchPop();
            }
        });
    }

    private Fragment createFragment() {
        switch (id) {
            case 0:
                //推荐
                return CircleRecommendFragment.newInstance(id);
            case 10086:
                //专家
                searchLay.setVisibility(View.GONE);
                return CircleExpertdFragment.newInstance(id);
            case 10087:
                //机构
                return CircleOrganizeFragment.newInstance(id);
            case 10088:
                //文献
                return CircleDocumentFragment3.newInstance(id);
            default:
                //其他频道
                return CircleTabFragment.newInstance(id);
        }
    }

    public void showSearchPop() {
        dWindow = new SearchPopWindow(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                dWindow.dismiss();

            }
        }, mContext, hotList,id+"",channelName);
        dWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void getHotCache() {
        hotList.clear();
        String str = SharedPreferencesUtil.getString(mContext, "hotkey","");
        List<HotVo> cachelist = null;
        try {
            cachelist = gson.fromJson(str,new TypeToken<List<HotVo>>() {}.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if(null!=cachelist){
            hotList.addAll(cachelist);
        }
    }

    @Override
    public void onDestroy() {
        if (null != dWindow && dWindow.isShowing()) {
            dWindow.dismiss();
        }
        super.onDestroy();
    }

}
