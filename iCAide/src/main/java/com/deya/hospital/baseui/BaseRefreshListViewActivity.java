package com.deya.hospital.baseui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.MyHandler;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 * 含下拉刷新的基类
 */
public abstract class BaseRefreshListViewActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public PullToRefreshListView listview;
    public CommonTopView topView;
    public TextView empertyText;
    public LinearLayout networkView;
    public int page;
    public int totalPage;
    public boolean isFirst=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_refesh_listview);
        init();
        initView();
    }

    private void init() {

        listview = (PullToRefreshListView) this.findViewById(R.id.listview);
        topView = (CommonTopView) this.findViewById(R.id.topView);
        empertyText = (TextView) this.findViewById(R.id.empertyText);
        networkView = (LinearLayout) this.findViewById(R.id.networkView);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                onListViewPullDown();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                onListViewPullUp();
            }
        });
        listview.getRefreshableView().setOnItemClickListener(this);
    }

    public abstract void onListViewPullUp();

    public abstract void onListViewPullDown();

    public abstract void initView();

    protected static final int GET_DATA_SUCESS = 0x60501;
    protected static final int GET_DATA_FAIL = 060502;

    public MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case GET_DATA_SUCESS:
                        listview.onRefreshComplete();
                        if (null != msg && null != msg.obj) {
                            try {
                                setDataResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case GET_DATA_FAIL:
                        listview.onRefreshComplete();
                        break;

                    default:
                        break;
                }
            }
        }
    };

    protected abstract void setDataResult(JSONObject jsonObject);

    protected abstract void onItemclick(int position);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        onItemclick(position - 1);
    }
}
