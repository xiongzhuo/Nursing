/**
 * 日期:2015年6月20日下午4:53:13 . <br/>
 */

package com.deya.hospital.workspace.priviewbase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragmentActivity;
import com.deya.hospital.descover.SurroundFragemtsAdapter;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.DynamicStasticVo;
import com.deya.hospital.vo.FormCodeVo;
import com.deya.hospital.vo.ResistantMutiTextVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.multidrugresistant.AddResistantActivity;
import com.deya.hospital.workspace.multidrugresistant.MutiRisitantPriviewActivity;
import com.deya.hospital.workspace.multidrugresistant.ResistantCaseListFragment;
import com.deya.hospital.workspace.multidrugresistant.ResistantListSearchFragment;
import com.deya.hospital.workspace.site_infection.SiteInfectionCaseActivity;
import com.deya.hospital.workspace.site_infection.SiteInfectionPriviewAcitivty;
import com.deya.hospital.workspace.threepips.AddTheePipsActivity;
import com.deya.hospital.workspace.threepips.ThreePipsPriviewActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:. SurrounFragment【周边主界面】 <br/>
 * 工作圈-问答
 */
public class ResistantListActivity extends BaseFragmentActivity implements
        OnClickListener {
    private static final int SEARCH_TYPE_SUCCESS = 0x70007;
    private static final int SEARCH_TYPE_FAIL = 0x70008;
    private RadioButton recommendRadio;
    private RadioButton newsRadio;
    Gson gson = new Gson();
    private MyHandler myHandler;
    private Tools tools;
    LayoutInflater layoutInflater;
    private ViewPager shoppager;
    private SurroundFragemtsAdapter myadapter;
    private List<Fragment> listfragment;
    private RadioGroup mGroup;
    private MyBrodcastReceiver brodcast;
    private ResistantMutiTextVo rmtv;
    private String data_id = "DN";
    public static final String UPDATE_ADD = "update_add";
    public static final String UPDATE_UPDATE = "update_update";
    ResistantCaseListFragment fragment1;
    ResistantListSearchFragment fragment2;
    String code="";
    boolean isDynamic;
    DynamicStasticVo.ListBean stasticVo=new  DynamicStasticVo.ListBean();
    private Button button,shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supquestionlist_main);
        tools = new Tools(mcontext, Constants.AC);
        if(getIntent().hasExtra("code")){
            code=getIntent().getStringExtra("code");
        }

        if(getIntent().hasExtra("daynamic")){
            isDynamic=true;
            stasticVo= (DynamicStasticVo.ListBean) getIntent().getSerializableExtra("daynamic");
            code=stasticVo.getCode();
        }
        layoutInflater = LayoutInflater.from(mcontext);
        initViews();
    }


    List<FormCodeVo> formCodeVoList;

    public void initViews() {
//        try {
//            formCodeVoList = DataBaseHelper
//                    .getInstance()
//                    .getDbUtilsInstance(mcontext)
//                    .findAll(
//                            Selector.from(FormCodeVo.class).where("data_id", "=", code)
//                                    .orderBy("dbid"));
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//
//        if(null==formCodeVoList){
//            formCodeVoList=new ArrayList<>();
//        }
        shoppager = (ViewPager) this.findViewById(R.id.order_pager);
        shoppager.setSaveEnabled(false);
        listfragment = new ArrayList<Fragment>();
        myadapter = new SurroundFragemtsAdapter(getSupportFragmentManager(),
                listfragment);
        fragment1= ResistantCaseListFragment.newInstance(code);
        if(isDynamic){
            fragment2= ResistantListSearchFragment.newInstance(stasticVo);
        }else{
        fragment2= ResistantListSearchFragment.newInstance(code);
        }
        listfragment.add(fragment1);
        listfragment.add(fragment2);

        mGroup = (RadioGroup) this.findViewById(R.id.group);
        mGroup.setOnCheckedChangeListener(new CheckedChangeListener());
        shoppager.setOnPageChangeListener(new PageChangeListener());
        shoppager.setOffscreenPageLimit(2);
        shoppager.setAdapter(myadapter);
        RadioButton radio_frist = (RadioButton) findViewById(R.id.radio_frist);
        radio_frist.setText("未完成");
        mGroup.check(R.id.radio_frist);
        findViewById(R.id.rl_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        registerBroadcast();
        if(isDynamic){
           shoppager.setCurrentItem(1);
        }

        button = (Button) this.findViewById(R.id.sumbmitBtn);
        button.setOnClickListener(this);
        button.setText("添加病例");
        shareBtn= (Button) this.findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);
    }

    private void registerBroadcast() {
        brodcast = new MyBrodcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("upload", "接收到了0");
                if (intent.getAction().equals(UPDATE_ADD)) {
                    if (intent.hasExtra("data")){

                        fragment1.onAddData((CaseListVo.CaseListBean) intent.getSerializableExtra("data"));

                    }

                } else if (intent.getAction().equals(
                        UPDATE_UPDATE)) {
                    if (intent.hasExtra("dbid")) {
                        int dbid = intent.getIntExtra("dbid", -1);
                        if (dbid != -1) {

                        }
                    }

                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_ADD);
        intentFilter.addAction(UPDATE_UPDATE);
       registerReceiver(brodcast, intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sumbmitBtn:
                if (code.equals("DN")) {
                    Intent intent = new Intent(mcontext, AddResistantActivity.class);
                    startActivity(intent);
                } else if (code.equals("SIP")) {
                    Intent intent = new Intent(mcontext, SiteInfectionCaseActivity.class);
                    startActivity(intent);
                } else if (code.equals("SG")) {
                    Intent intent = new Intent(mcontext, AddTheePipsActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.shareBtn://按科室批量督察
                Intent in = new Intent();
                TaskVo taskVo = new TaskVo();
                taskVo.setTaskListBean("");
                taskVo.setFinished(0);
                in.putExtra("data", taskVo);
                if (code.equals("DN")) {
                    in.setClass(mcontext, MutiRisitantPriviewActivity.class);
                } else if (code.equals("SG")) {
                    in.putExtra("isInspector",1);
                    in.setClass(mcontext, ThreePipsPriviewActivity.class);
                } else if (code.equals("SIP")) {
                    in.setClass(mcontext, SiteInfectionPriviewAcitivty.class);
                }
                startActivity(in);

                break;

            default:
                break;
        }
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
        if(null!=brodcast){
        unregisterReceiver(brodcast);
        }
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

    private void setMutiTextResult(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            rmtv = TaskUtils.gson.fromJson(jsonObject.toString(), ResistantMutiTextVo.class);
        } else {
            rmtv = new ResistantMutiTextVo();
            ToastUtils.showToast(mcontext, rmtv.getResult_msg());
        }

    }

}
