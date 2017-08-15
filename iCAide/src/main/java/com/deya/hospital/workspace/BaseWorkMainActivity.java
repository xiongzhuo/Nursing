package com.deya.hospital.workspace;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseCommonTopActivity;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.descover.NeedKnowListActivity;
import com.deya.hospital.shop.ShopGoodsListActivity;
import com.deya.hospital.task.Tasker;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.HomePageBanner;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.vo.AdvEntity;
import com.deya.hospital.workspace.supervisorquestion.SupQuestionListActivity;
import com.deya.hospital.workspace.tasksearcjh.SearchHandTaskActivity;
import com.flyco.banner.widget.Banner.base.BaseBanner;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/6/22
 */
public abstract class BaseWorkMainActivity extends BaseCommonTopActivity implements View.OnClickListener,RequestInterface{
    private static final int GETCAROUSELFIGURE_SUCCESS = 0x001;
    public View headView;
    private PullToRefreshListView planLv;
    private LayoutInflater inflaterHead;
    private LinearLayout addLay;//添加任务点击区域
    private LinearLayout improveLay;//改进列表点击区域
    private LinearLayout searchTaskLay;//查看任务点击区域
    HomePageBanner viewPager;
    private List<AdvEntity.ListBean> pagerList = new ArrayList<AdvEntity.ListBean>();
    private ImageView red_point;
    ImageView message_red_point;
    private static final int QUESTION_COUNT = 0x121;
    @Override
    public String getTopTitle() {
        return getWorkTitle();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_wast_workspace;
    }

    @Override
    public void initBaseData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        red_point.setVisibility(Tasker.getAllLocalTaskByType(getTaskType()+"").size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initView() {
        topView.setRigtext("应知应会");
        topView.onRightClick(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it3= new Intent(mcontext,
                        NeedKnowListActivity.class);
                it3.putExtra("task_type",getTaskType());
                startActivity(it3);
            }
        });
        inflaterHead = LayoutInflater.from(mcontext);
        planLv = findView(R.id.planlist);
        headView = inflaterHead.inflate(R.layout.workspace_headview_item, null);
        planLv.getRefreshableView().addHeaderView(headView);
        planLv.setAdapter(getAdapter());
        addLay = (LinearLayout) headView
                .findViewById(R.id.handHhygieneLay);
        addLay.setOnClickListener(this);

        viewPager= (HomePageBanner) headView.findViewById(R.id.viewPager);
        improveLay = (LinearLayout) headView.findViewById(R.id.messageLay);
        improveLay.setOnClickListener(this);
        searchTaskLay = (LinearLayout) headView.findViewById(R.id.infectionLay);
        searchTaskLay.setOnClickListener(this);
        message_red_point=findView(R.id.message_red_point);
        headView.findViewById(R.id.radioGroup).setVisibility(View.GONE);
        red_point= (ImageView) headView.findViewById(R.id.red_point);
        headView.findViewById(R.id.reportLay).setOnClickListener(this);
        getCarouselFigure();
        getQuestionCount();
    }
    public void getQuestionCount() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext,
                QUESTION_COUNT, job, "supervisorQuestion/questionCount");

    }
    protected abstract int getTaskType();

    protected abstract String getWorkTitle();

    protected abstract DYSimpleAdapter getAdapter();

    /**
     * 添加任务
     */
    protected abstract void onAddTask();



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.messageLay:
                Intent intent3=new Intent(mcontext,SupQuestionListActivity.class);
                intent3.putExtra("origin",getTaskType());
                startActivity(intent3);
                break;
            case R.id.infectionLay:
                Intent intent2=new Intent(mcontext,SearchHandTaskActivity.class);
                intent2.putExtra("type",getTaskType());
                startActivity(intent2);
                break;
            case R.id.handHhygieneLay:
                onAddTask();
                break;
            case R.id.reportLay:
                showDialogToast("","参照手卫生,正在排队上线中!");
                break;
        }

    }


    /**
     * 获取广告栏
     */
    public void getCarouselFigure() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReqAllpath(this, this,
                GETCAROUSELFIGURE_SUCCESS, job, WebUrl.PUBLIC_SERVICE_URL + "/comm/getCarouselFigure");

    }

    /**
     * @param jsonObject 广告请求成功数据
     */
    private void setAdv(JSONObject jsonObject) {
        try {
            AdvEntity entity = TaskUtils.gson.fromJson(jsonObject.toString(), AdvEntity.class);
            if (entity != null) {
                if (entity.getResult_id().equals("0")) {
                    setRequestData(entity);
                } else {
                    ToastUtils.showToast(mcontext, entity.getResult_msg());
                }
            } else {
                ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        } finally {
            setAdvPic();
        }
    }

    private void setAdvPic() {
        if(null==viewPager){
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

        viewPager.setOnItemClickL(new BaseBanner.OnItemClickL() {

            @Override
            public void onItemClick(int position) {
                if (pagerList != null && pagerList.size() > 0) {
                    int type = pagerList.get(position).getType();
                    switch (type) {
                        case 0:
                            Intent intent = new Intent(mcontext, WebViewDemo.class);
                            intent.putExtra("url", pagerList.get(position).getHref_url());
                            startActivity(intent);
                            break;
                        case 1:
                            StartActivity(ShopGoodsListActivity.class);
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

    private void setMessageRedPoint(JSONObject jsonObject) {
        Log.i(getClass().getName(), jsonObject.toString());
        if (jsonObject.has("totalcnt") && jsonObject.optInt("totalcnt") > 0) {
            message_red_point.setVisibility(View.VISIBLE);
        } else {
            message_red_point.setVisibility(View.GONE);
        }
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

        switch (code){

            case QUESTION_COUNT:
                dismissdialog();
                setMessageRedPoint(jsonObject);
                break;
            default:
                setAdv(jsonObject);
                break;
        }


    }

    @Override
    public void onRequestErro(String message) {
        dismissdialog();
        ToastUtil.showMessage(message);
        Log.i("1111111111", message);
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
    protected void onDestroy() {
        if(null!=viewPager){
            viewPager.pauseScroll();
            viewPager.clearAnimation();
            viewPager=null;
        }
        super.onDestroy();
    }
}
