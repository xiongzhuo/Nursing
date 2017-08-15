package com.deya.hospital.handwash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseViewHolder;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.base.TabBaseFragment;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.dudao.SupvisorMainActivity;
import com.deya.hospital.login.LoginActivity;
import com.deya.hospital.quality.QualityMainActivity;
import com.deya.hospital.shop.ShopGoodsListActivity;
import com.deya.hospital.shop.SignInActivity;
import com.deya.hospital.study.StudyMainActivity;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.HomePageBanner;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.vo.AdvEntity;
import com.deya.hospital.vo.TaskTypePopVo;
import com.deya.hospital.workspace.environment.EnviromentWorkActivty;
import com.deya.hospital.workspace.waste_disposal.WastWorkSpaceActivty;
import com.flyco.banner.widget.Banner.base.BaseBanner.OnItemClickL;
import com.google.gson.Gson;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.dialog.ECAlertDialog;
import com.im.sdk.dy.common.utils.ECNotificationManager;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.yuntongxun.ecsdk.ECDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomisedMainFragment extends TabBaseFragment implements
        View.OnClickListener, RequestInterface, AdapterView.OnItemClickListener {

    public static final String UPDATA_ACTION = "updateList";


    private Gson gson = new Gson();
    private Tools tools;
    private MyBrodcastReceiver brodcast;
    Context mcontext;
    private LinearLayout signInTv;
    HomePageBanner viewPager;
    ModuAdapter adapter;
    ListView projectList;
    GridView gridView;
    String modueStr[] = {"手卫生", "环境清洁","医废管理","职业防护","临床质控", "督查反馈", "学习考试", "感染会诊" };
    int modueImg[] = {R.drawable.me_tool_hw,   R.drawable.me_tool_envir,R.drawable.me_tool_wast,R.drawable.me_tool_zyfh,
            R.drawable.me_tool_quality,R.drawable.me_tool_ddb,R.drawable.me_tool_study,
             R.drawable.me_tool_huizhen
           };
    JSONObject toolState;
    List<TaskTypePopVo> toolList;

    String keys[] = {"hw", "envior","wast" ,"zyfh","quality","dudao", "kaohe",  "hz"  };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.customisedmain_fragment2, container,
                    false);
            mcontext = getActivity();
            tools = new Tools(getActivity(), Constants.AC);
            initView();
         //   isSignUser();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initTools();
        //isSignedSucUser(tools);
    }

    private void initTools() {
        toolState = MyAppliaction.getToolsState();
        toolList.clear();


        for (int i = 0; i < keys.length; i++) {
            boolean value = toolState.optBoolean(keys[i]);
            if (value) {
                TaskTypePopVo typeVo = new TaskTypePopVo();
                typeVo.setTypeName(keys[i]);
                typeVo.setImgid(modueImg[i]);
                typeVo.setName(modueStr[i]);
                toolList.add(typeVo);
            }
        }

        adapter.notifyDataSetChanged();
    }


    ImageView falshImg;
    private List<AdvEntity.ListBean> pagerList = new ArrayList<>();
    private AnimationDrawable falshdrawable;


    private void initView() {
        toolList = new ArrayList<>();
        signInTv = (LinearLayout) this.findViewById(R.id.signInTv);
        signInTv.setOnClickListener(this);
        falshImg = (ImageView) this.findViewById(R.id.falshImg);
        falshImg.setBackgroundResource(R.drawable.canlender_flash);
        falshdrawable = (AnimationDrawable) falshImg.getBackground();
        falshdrawable.start();
//        handHhygieneLay= (LinearLayout) findViewById(R.id.handHhygieneLay);
//        zlyblay =(LinearLayout) findViewById(R.id.zlybLay);
//        handHhygieneLay.setOnClickListener(this);
//        zlyblay.setOnClickListener(this);
//        kaoshiLay=(LinearLayout) findViewById(R.id.kaoshiLay);
//        kaoshiLay.setOnClickListener(this);
//        ddbLay =(LinearLayout) findViewById(R.id.ddbLay);
//        ddbLay.setOnClickListener(this);
        gridView = findView(R.id.gridview);
        viewPager = (HomePageBanner) this.findViewById(R.id.viewPager);
        adapter = new ModuAdapter(mcontext, toolList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        projectList = findView(R.id.projectList);

        setAdvPic();
        getCarouselFigure();

    }


    /**
     * setHomeBanner:【填充广告的数据】. <br/>
     */
    private static final int GETCAROUSELFIGURE_SUCCESS = 0x11000;

    private void setAdvPic() {
        if (null == viewPager) {
            return;
        }
        if (!(pagerList.size() > 0)) {
            AdvEntity.ListBean advEntity1 = new AdvEntity.ListBean();
            advEntity1.setDrawable(R.drawable.banner1);
            advEntity1.setType(1);
            advEntity1.setName("");
            pagerList.add(advEntity1);

            AdvEntity.ListBean advEntity2 = new AdvEntity.ListBean();
            advEntity2.setDrawable(R.drawable.banner2);
            advEntity2.setType(2);
            advEntity2.setName("");
            pagerList.add(advEntity2);
            viewPager.clearAnimation();
        }
        setHomeBanner();
    }

    /**
     * setHomeBanner:【填充广告的数据】. <br/>
     */
    private void setHomeBanner() {
        if (1 == pagerList.size()) {
            viewPager.setDelay(3).setPeriod(3).setAutoScrollEnable(false)
                    .setTouchAble(false).setSource(pagerList).startScroll(); // 只有一张广告设置不能滑动可以点击
        } else {// 否则执行自动滚动并设置为可以手动滑动也可以点击
            viewPager.setDelay(3).setPeriod(3).setAutoScrollEnable(true)
                    .setTouchAble(true).setSource(pagerList).startScroll();
        }

        viewPager.setOnItemClickL(new OnItemClickL() {

            @Override
            public void onItemClick(int position) {
                if (pagerList != null && pagerList.size() > 0) {
                    int type = pagerList.get(position).getType();
                    switch (type) {
                        case 0:
                            Intent intent = new Intent(getActivity(), WebViewDemo.class);
                            intent.putExtra("url", pagerList.get(position).getHref_url());
                            startActivity(intent);
                            break;
                        case 1:
                            OnStartActivity(ShopGoodsListActivity.class);
                            break;
//                        case 2:
//                            OnStartActivity(QuestionSortActivity.class);
//                            break;

                        default:
                            break;
                    }
                }
            }
        });
    }

    public void OnStartActivity(Class<?> T) {
        Intent inten = new Intent(getActivity(), T);
        startActivity(inten);
    }


    @Override
    public void onDestroy() {
        if (null != viewPager) {
            viewPager = null;
        }
        if (null != brodcast) {
            getActivity().unregisterReceiver(brodcast);
        }
        if(null!=handItemSelectDialog){
            handItemSelectDialog.dismiss();
        }

        super.onDestroy();
    }





    // 软文模块
    private void registerBroadcast() {
        brodcast = new MyBrodcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(SDKCoreHelper.ACTION_KICK_OFF)) {
                    if (intent.hasExtra("kickoffText")) {
                        handlerKickOff(intent.getStringExtra("kickoffText"));
                    }
                    getActivity().finish();

                } else if (intent.getAction().equals(Constants.AUTHENT_LOSE)) {
                    ToastUtils.showToast(mcontext, "登录失效请重新登录");
                    getActivity().finish();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.AUTHENT_LOSE);
        intentFilter.addAction(UPDATA_ACTION);
        intentFilter.addAction(SDKCoreHelper.ACTION_KICK_OFF);
        getActivity().registerReceiver(brodcast, intentFilter);
    }

    public void handlerKickOff(String kickoffText) {
        if (getActivity().isFinishing()) {
            return;
        }
        ECAlertDialog buildAlert = ECAlertDialog.buildAlert(getActivity(), kickoffText,
                getString(R.string.dialog_btn_confim),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ECNotificationManager.getInstance()
                                .forceCancelNotification();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        resetCasche();
                    }
                });
        buildAlert.setTitle("登录异常");
        buildAlert.setCanceledOnTouchOutside(false);
        buildAlert.setCancelable(false);
        buildAlert.show();
    }

    public void resetCasche() {
        try {
            tools.putValue(Constants.AUTHENT, "");
            tools.putValue(Constants.NAME, "");
            tools.putValue(Constants.HOSPITAL_NAME, "");
            tools.putValue(Constants.AGE, "");
            tools.putValue(Constants.SEX, "");
            tools.putValue(Constants.STATE, "");
            tools.putValue(Constants.MOBILE, "");
            tools.putValue(Constants.HEAD_PIC, "");
            tools.putValue(Constants.EMAIL, "");
            tools.putValue(Constants.JOB, "");
            tools.putValue(Constants.USER_ID, "");

            if (ECDevice.isInitialized())
                try {
                    ECDevice.unInitial();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            SDKCoreHelper.logout(false);

            CCPAppManager.setClientUser(null);

            MainActivity.mInit = false;

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    public void getCarouselFigure() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReqAllpath(this, getActivity(),
                GETCAROUSELFIGURE_SUCCESS, job, WebUrl.PUBLIC_SERVICE_URL + "/comm/getCarouselFigure");

    }

    private void setRequestData(AdvEntity entity) {
        if (entity.getList() != null && entity.getList().size() > 0) {
            viewPager.setDelay(0).setPeriod(0).setAutoScrollEnable(false)
                    .setTouchAble(false).setSource(pagerList).pauseScroll();
            pagerList.clear();
            pagerList.addAll(entity.getList());
        }
    }


    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        switch (code) {
            case GETCAROUSELFIGURE_SUCCESS:
                setAdv(jsonObject);
                break;
        }
        dismissdialog();

    }


    /**
     * @param jsonObject 广告请求成功数据
     */
    private void setAdv(JSONObject jsonObject) {
        try {
            AdvEntity entity = gson.fromJson(jsonObject.toString(), AdvEntity.class);
            if (entity != null) {
                if (entity.getResult_id().equals("0")) {
                    setRequestData(entity);
                } else {
                    ToastUtils.showToast(getActivity(), entity.getResult_msg());
                }
            } else {
                ToastUtils.showToast(getActivity(), "亲，您的网络不顺畅哦！");
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        } finally {
            setAdvPic();
        }
    }

    @Override
    public void onRequestErro(String message) {

    }

    @Override
    public void onRequestFail(int code) {
        switch (code) {
            case GETCAROUSELFIGURE_SUCCESS:
                pagerList.clear();
                setAdvPic();
                break;
        }

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.signInTv:
                OnStartActivity(SignInActivity.class);
                break;
            case R.id.zlybLay:
                OnStartActivity(QualityMainActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isSigned(toolList.get(position).getTypeName())) {
            return;
        }

        switch (toolList.get(position).getTypeName()) {
                case "hw":
                    handItemSelectDialog.show();
                    break;
                case "dudao":
                    onStartActivity(SupvisorMainActivity.class);
                    break;
                case "kaohe":
                    onStartActivity(StudyMainActivity.class);
                    break;
                case "hz":
                    showDialogToast("","正在排队上线中!");
                    break;
                case "zyfh":
                    showDialogToast("","正在排队上线中!");
                    break;
                case "quality":
                    Intent it2 = new Intent(getActivity(),
                            QualityMainActivity.class);
                    startActivity(it2);
                    break;
                case "envior":
                    onStartActivity(EnviromentWorkActivty.class);
                    break;
                case "wast":
                    onStartActivity(WastWorkSpaceActivty.class);
                    break;
                default:
                    break;

        }

    }

    private class ModuAdapter extends DYSimpleAdapter<TaskTypePopVo> {

        public ModuAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        protected void setView(int position, View convertView) {
            ImageView img = BaseViewHolder.get(convertView, R.id.img);
            TextView txt = BaseViewHolder.get(convertView, R.id.txt);
            TaskTypePopVo vo = list.get(position);
            txt.setText(vo.getName());
            img.setImageResource(vo.getImgid());
        }


        @Override
        public int getLayoutId() {
            return R.layout.img_top_txt_down;
        }
    }

    private static boolean hasTaskTypes(int type) {
        try {
            List<TaskTypePopVo> popVoList = DataBaseHelper
                    .getDbUtilsInstance(MyAppliaction.getContext())
                    .findAll(
                            Selector.from(TaskTypePopVo.class).orderBy("dbid"));
            if (null == popVoList) {
                return false;
            }
            for (TaskTypePopVo vo : popVoList) {
                if (type == vo.getId()) {
                    return true;
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }



}
