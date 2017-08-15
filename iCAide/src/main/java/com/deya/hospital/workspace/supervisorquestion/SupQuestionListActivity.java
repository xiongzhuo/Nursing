/**
 * 日期:2015年6月20日下午4:53:13 . <br/>
 */

package com.deya.hospital.workspace.supervisorquestion;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragmentActivity;
import com.deya.hospital.descover.SurroundFragemtsAdapter;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:. SurrounFragment【周边主界面】 <br/>
 * 工作圈-问答
 */
public class SupQuestionListActivity extends BaseFragmentActivity implements
        OnClickListener {
    private static final int SEARCH_TYPE_SUCCESS = 0x70007;
    private static final int SEARCH_TYPE_FAIL = 0x70008;
    private RadioButton recommendRadio;
    private RadioButton newsRadio;

    Gson gson = new Gson();
    private MyHandler myHandler;
    private Tools tools;
    LayoutInflater LayoutInflater;
    private ViewPager shoppager;
    private SurroundFragemtsAdapter myadapter;
    private List<Fragment> listfragment;
    private RadioGroup mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supquestionlist_main);
        tools = new Tools(mcontext, Constants.AC);
        LayoutInflater = LayoutInflater.from(mcontext);
        initViews();
    }


    public void initViews() {
        shoppager = (ViewPager) this.findViewById(R.id.order_pager);
        shoppager.setSaveEnabled(false);
        listfragment = new ArrayList<Fragment>();
        myadapter = new SurroundFragemtsAdapter(getSupportFragmentManager(),
                listfragment);
        listfragment.add(new NeedImproveQueFragment());
        listfragment.add(new SupQuestionSearchListActivity());
        mGroup = (RadioGroup) this.findViewById(R.id.group);
        mGroup.setOnCheckedChangeListener(new CheckedChangeListener());
        shoppager.setOnPageChangeListener(new PageChangeListener());
        shoppager.setOffscreenPageLimit(0);
        shoppager.setAdapter(myadapter);
        mGroup.check(R.id.radio_frist);
        findViewById(R.id.rl_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.sumbmitlay).setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {

    }

    private class CheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radio_frist:
                    shoppager.setCurrentItem(0);

                    break;
                case R.id.radio_second:

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

        /**
         * .注册相关广播
         */
        @Override
        public void onResume() {
            super.onResume();

        }

        /**
         * .注销广播或者注销线程等操作
         */
        @Override
        public void onDestroy() {
            super.onDestroy();
        }


        private static final int SEARCH_SUCCESS = 0x009912;
        private static final int SEARCH_FAIL = 0x009913;


        private void initMyHandler() {
            myHandler = new MyHandler(this) {
                @Override
                public void handleMessage(Message msg) {
                    Activity activity = myHandler.mactivity.get();
                    if (null != activity) {
                        switch (msg.what) {
                            case SEARCH_SUCCESS:
                                if (null != msg && null != msg.obj) {
                                }
                                break;
                            case SEARCH_FAIL:
                                ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
                                break;
                            case SEARCH_TYPE_SUCCESS:
                                if (null != msg && null != msg.obj) {
                                }
                                break;

                            default:
                                break;
                        }
                    }
                }
            };
        }


    }
