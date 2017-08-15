package com.deya.hospital.workcircle.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.base.EasyBaseAdapter;
import com.deya.hospital.base.EasyViewHolder;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.descover.QuestionSortActivity;
import com.deya.hospital.descover.QuetionDetailActivity;
import com.deya.hospital.dypdf.PdfPreviewActivity;
import com.deya.hospital.shop.ShopGoodsListActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.HomePageBanner;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ScrollViewIncludeGridView;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.vo.AdvEntity;
import com.deya.hospital.vo.HotVo;
import com.deya.hospital.vo.WorkCircleMainEntity;
import com.deya.hospital.vo.WorkCircleRecommentEntity;
import com.deya.hospital.workcircle.SearchPopWindow;
import com.deya.hospital.workcircle.WebViewDtail;
import com.deya.hospital.workspace.TaskUtils;
import com.flyco.banner.widget.Banner.base.BaseBanner;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 新工作圈
 */
public class WorkCircleMainFragment extends BaseFragment implements View.OnClickListener, AbsListView.OnScrollListener {
    private static final String TAG = "com.deya.hospital.workcircle.fragment.WorkCircleMainFragment";
    private static final int GET_DATA_SUCESS = 0x6050;
    private static final int GET_DATA_FAIL = 0x06051;
    private static final int GET_MAIN_SUCESS = 0x6052;
    private static final int GET_MAIN_FAIL = 0x06053;
    private int pageIndex = 1;
    private View view;
    private LinearLayout main;
    private HomePageBanner hpb_title;
    private ScrollViewIncludeGridView gridview;
    private TextView line;
    private TextView tv_answer;
    private TextView tv_more_ancwer;
    private com.deya.hospital.view.MarqueeTextView tv_move;
    private ImageView img_more_ancwer;
    private com.deya.hospital.util.ScrollViewIncludeListView lv_answer;
    private com.deya.hospital.util.HomePageBanner hpb_adv;
    private TextView line2;
    private TextView tv_recommend;
    private RelativeLayout rl_que;
    private LinearLayout ll_ask;
    private LinearLayout ll_search;
    private LinearLayout ll_search_sharp;
    private View lineno;
    private com.handmark.pulltorefresh.library.PullToRefreshListView lv_recommend;


    private List<HotVo> list = new ArrayList<>();
    private List<WorkCircleMainEntity.QuestionListBean> qusList = new ArrayList<>();
    private List<WorkCircleRecommentEntity.ListBean> listBeen = new ArrayList<>();
    private AnimationDrawable falshdrawable;
    private MyHandler myHandler;
    private Tools tools;
    private ChannelAdapter channelAdapter;
    private RecommenAdapter recommenAdapter;
    private boolean isRefresh = true;
    private View headView;
    private Gson gson = new Gson();
    private AskAdapter askAdapter;
    private List<WorkCircleMainEntity.ConvertListBean> converList = new ArrayList<>();
    private DisplayImageOptions options;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        if (view == null) {
            tools = new Tools(getActivity(), Constants.AC);
            view = inflater.inflate(R.layout.frgment_workcircle_mian, container, false);
            headView = LayoutInflater.from(getActivity()).inflate(R.layout.headview_circlemain, null);
            bindViews();
            initHandler();
//            adjustPadding();
            initChannel();
            initAsk();
            initRecomment();
            initData();
        } else if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViews();
        }
        getHotCache();
        return view;
    }

//    private void adjustPadding() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            ll_search.setPadding(0, AbViewUtil.dp2Px(getActivity(), 25), 0, 0);
//        } else {
//            ll_search.setPadding(0, 0, 0, 0);
//        }
//    }

    private void bindViews() {
         options = AbViewUtil.getOptions(R.drawable.defult_img);
        hpb_title = getHeadViewById(R.id.hpb_title);
        gridview = getHeadViewById(R.id.gridview);
        line = getHeadViewById(R.id.line);
        tv_answer = getHeadViewById(R.id.tv_answer);
        tv_more_ancwer = getHeadViewById(R.id.tv_more_ancwer);
        img_more_ancwer = getHeadViewById(R.id.img_more_ancwer);
        lv_answer = getHeadViewById(R.id.lv_answer);
        hpb_adv = getHeadViewById(R.id.hpb_adv);
        line2 = getHeadViewById(R.id.line2);
        rl_que = getHeadViewById(R.id.rl_que);
        ll_ask = getHeadViewById(R.id.ll_ask);
        tv_move = getHeadViewById(R.id.tv_move);
        tv_recommend = getHeadViewById(R.id.tv_recommend);
        lv_recommend = getViewById(R.id.lv_recommend);
        ll_search = getViewById(R.id.ll_search);
        ll_search_sharp = getViewById(R.id.ll_search_sharp);
        lineno = getViewById(R.id.lineno);
        main = getViewById(R.id.main);

        listViewADD = lv_recommend.getRefreshableView();
        listViewADD.addHeaderView(headView);
        rl_que.setOnClickListener(this);
        ll_search_sharp.setOnClickListener(this);
        tv_move.setOnClickListener(this);

        lv_recommend.setMode(PullToRefreshBase.Mode.DISABLED);
        //当布局的状态或者控件的可见性发生改变回调的接口
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //这一步很重要，使得上面的布局和下面的布局重合
            }
        });
        lv_recommend.setOnScrollListener(this);
    }


    private ListView listViewADD;
//起始位置从屏幕搞算起
//    public int getScrollY() {
//        View c = listViewADD.getChildAt(0);
//        if (c == null) {
//            return 0;
//        }
//        int firstVisiblePosition = listViewADD.getFirstVisiblePosition();
//        int top = c.getTop();
//        return -top + firstVisiblePosition * c.getHeight();
//    }


    private void initData() {
        setCacheAdv();
        //网络
        questionsRecommend();
        workCircleRecommendList(pageIndex);
    }

    private void setCacheAdv() {
        //缓存
        getMainCache();
        getRecommentCache();
    }

    private void initAsk() {
        askAdapter = new AskAdapter(getActivity(), R.layout.adapter_item_text, qusList);
        lv_answer.setAdapter(askAdapter);
        lv_answer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toGuide = new Intent(getActivity(), QuetionDetailActivity.class);
                toGuide.putExtra("q_id", qusList.get(position).getQ_id() + "");
                getActivity().startActivity(toGuide);
            }
        });
    }

    private void initRecomment() {
        recommenAdapter = new RecommenAdapter(getActivity(), R.layout.adapter_item_recommend, listBeen);
        lv_recommend.setAdapter(recommenAdapter);
        lv_recommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int mPosition = position - 2;
                if (mPosition >= listBeen.size()) {
                    mPosition = listBeen.size() - 1;
                }
                if (listBeen.size() > 0 && position > 0) {
                    listBeen.get(mPosition).getId();
                    String url = "";
                    Intent it = new Intent();
                    if (listBeen.get(mPosition).getIs_pdf() == 1) {
                        if (!AbStrUtil.isEmpty(listBeen.get(mPosition).getPdf_attach())) {
                            url = WebUrl.WEB_PDF + "?id=" + listBeen.get(mPosition).getId() + "&&pdfid=" + listBeen.get(mPosition).getPdf_attach();
                        }
                        it.putExtra("articleid", listBeen.get(mPosition).getId() + "");
                        it.setClass(getActivity(), PdfPreviewActivity.class);
                    } else {
                        url = WebUrl.WEB_ARTICAL_DETAIL + "?id=" + listBeen.get(mPosition).getId();
                        it.setClass(getActivity(), WebViewDtail.class);
                    }
                    it.putExtra("url", url);
                    startActivity(it);
                }
            }
        });

        lv_recommend.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                pageIndex = 1;
                workCircleRecommendList(pageIndex);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = false;
                pageIndex = pageIndex + 1;
                workCircleRecommendList(pageIndex);
            }
        });
    }

    private void initChannel() {
        falshdrawable = (AnimationDrawable) img_more_ancwer.getDrawable();
        falshdrawable.start();

        getDefChannel();
        channelAdapter = new ChannelAdapter(getActivity(), R.layout.adapter_item_channel, list);
        gridview.setAdapter(channelAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (list.get(position).getId().equals("-1")) {
                    Intent in = new Intent(getActivity(), QuestionSortActivity.class);
                    startActivity(in);
                } else {
                    Intent intent = new Intent(getActivity(), WorkCiecleChannelActivity.class);
                    intent.putExtra("channelID", Integer.valueOf(list.get(position).getId()));
                    intent.putExtra("channelName", list.get(position).getName());
                    startActivity(intent);
                }
            }
        });
    }

    private void getDefChannel() {
        HotVo hv1 = new HotVo();
        hv1.setId("2");
        hv1.setSrcId(R.drawable.bg_circle_guide_selector);
        hv1.setName("指南");

        HotVo hv2 = new HotVo();
        hv2.setId("10088");
        hv2.setSrcId(R.drawable.bg_circle_document_selector);
        hv2.setName("文献");

        HotVo hv3 = new HotVo();
        hv3.setId("6");
        hv3.setSrcId(R.drawable.bg_circle_openclass_selector);
        hv3.setName("公开课");

        HotVo hv4 = new HotVo();
        hv4.setId("10089");
        hv4.setSrcId(R.drawable.bg_circle_courseware_selector);
        hv4.setName("课件");

        HotVo hv5 = new HotVo();
        hv5.setId("4");
        hv5.setSrcId(R.drawable.bg_circle_video_selector);
        hv5.setName("视频");

        HotVo hv6 = new HotVo();
        hv6.setId("10087");
        hv6.setSrcId(R.drawable.bg_circle_organization_selector);
        hv6.setName("机构");

        HotVo hv7 = new HotVo();
        hv7.setId("1");
        hv7.setSrcId(R.drawable.bg_circle_news_selector);
        hv7.setName("资讯");

        HotVo hv8 = new HotVo();
        hv8.setId("-1");
        hv8.setSrcId(R.drawable.bg_circle_answer_selector);
        hv8.setName("问答");
        list.clear();

        list.add(hv1);
        list.add(hv2);
        list.add(hv3);
        list.add(hv4);
        list.add(hv5);
        list.add(hv6);
        list.add(hv7);
        list.add(hv8);
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) view.findViewById(id);
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getHeadViewById(@IdRes int id) {
        return (VT) headView.findViewById(id);
    }


    private void initHandler() {
        myHandler = new MyHandler(getActivity()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GET_DATA_SUCESS:
                        lv_recommend.onRefreshComplete();
                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                            try {
                                WorkCircleRecommentEntity entity = TaskUtils.gson.fromJson(msg.obj.toString(), WorkCircleRecommentEntity.class);
                                if (entity != null) {
                                    if (entity.getResult_id().equals("0")) {
                                        if (isRefresh) {
                                            SharedPreferencesUtil.saveString(getActivity(), TAG + "recomment", msg.obj.toString());
                                        }
                                        setRequestData(entity);
                                    } else {
                                        ToastUtils.showToast(getActivity(), entity.getResult_msg());
                                    }
                                } else {
                                    ToastUtils.showToast(getActivity(), "亲，您的网络不顺畅哦！");
                                }
                            } catch (Exception e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case GET_DATA_FAIL:
                        lv_recommend.onRefreshComplete();
                        ToastUtils.showToast(getActivity(), "亲，您的网络不顺畅哦！");
                        break;
                    case GET_MAIN_SUCESS:
                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                            try {
                                WorkCircleMainEntity entity =  TaskUtils.gson.fromJson(msg.obj.toString(), WorkCircleMainEntity.class);
                                if (entity != null) {
                                    if (entity.getResult_id().equals("0")) {
                                        SharedPreferencesUtil.saveString(getActivity(), TAG + "main", msg.obj.toString());
                                        setMainRequestData(entity, false);
                                    } else {
                                        ToastUtils.showToast(getActivity(), entity.getResult_msg());
                                    }
                                } else {
                                    ToastUtils.showToast(getActivity(), "亲，您的网络不顺畅哦！");
                                }
                            } catch (Exception e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case GET_MAIN_FAIL:
                        pagerList.clear();
                        pagerList2.clear();
                        setCacheAdv();
                        break;
                    default:
                        break;
                }

            }
        };
    }

    /**
     * @param pageIndex
     */
    private void workCircleRecommendList(int pageIndex) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("pageIndex", pageIndex);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onCirclModeRequest(myHandler, getActivity(), GET_DATA_SUCESS, GET_DATA_FAIL, job, "workCircle/recommendList");

    }

    public void getRecommentCache() {
        String cacheStr = SharedPreferencesUtil.getString(getActivity(), TAG + "recomment", "");
        if (!AbStrUtil.isEmpty(cacheStr)) {
            WorkCircleRecommentEntity entity =  TaskUtils.gson.fromJson(cacheStr, WorkCircleRecommentEntity.class);
            setRequestData(entity);
        }
    }

    private void getMainCache() {
        String cacheStr = SharedPreferencesUtil.getString(getActivity(), TAG + "main", "");
        if (!AbStrUtil.isEmpty(cacheStr)) {
            WorkCircleMainEntity entity =  TaskUtils.gson.fromJson(cacheStr, WorkCircleMainEntity.class);
            setMainRequestData(entity, true);
        }
    }

    private void setRequestData(WorkCircleRecommentEntity entity) {

        if (entity.getList() != null && entity.getList().size() > 0) {
            if (isRefresh) {
                //下拉刷新
                listBeen.clear();
            }
            if (entity.getPageTotal() == pageIndex) {
                lv_recommend.setMode(PullToRefreshBase.Mode.DISABLED);
            } else {
                lv_recommend.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            }
            listBeen.addAll(entity.getList());
            recommenAdapter.updateListView(listBeen);
        }
    }

    /**
     * 工作圈首页
     */
    private void questionsRecommend() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onCirclModeRequest(myHandler, getActivity(),
                GET_MAIN_SUCESS, GET_MAIN_FAIL, job, "workCircle/getMainInfo");

    }

    private void setMainRequestData(WorkCircleMainEntity entity, boolean isCache) {
        //顶部广告图
        if (entity.getTopImageList() != null && entity.getTopImageList().size() > 0) {
            List<AdvEntity.ListBean> pagerTemps = new ArrayList<>();
            for (int i = 0; i < entity.getTopImageList().size(); i++) {
                AdvEntity.ListBean listBean = new AdvEntity.ListBean();
                listBean.setId(entity.getTopImageList().get(i).getId());
                listBean.setType(entity.getTopImageList().get(i).getType());
                listBean.setName(entity.getTopImageList().get(i).getName());
                listBean.setHref_url(entity.getTopImageList().get(i).getHref_url());
                AdvEntity.ListBean.DataBean dataBean = new AdvEntity.ListBean.DataBean();
                dataBean.setId(entity.getTopImageList().get(i).getData().getId());
                dataBean.setName(entity.getTopImageList().get(i).getData().getName());
                listBean.setData(dataBean);
                pagerTemps.add(listBean);
            }
            pagerList.clear();
            pagerList.addAll(pagerTemps);
            try {
                hpb_title.setDelay(0).setPeriod(0).setAutoScrollEnable(false).setTouchAble(false).setSource(pagerList).pauseScroll();
                hpb_title.clearAnimation();
            }catch (Exception e){

            }

        }
        //中间广告图
        if (entity.getCenterImageList() != null && entity.getCenterImageList().size() > 0) {
            List<AdvEntity.ListBean> pagerTemps = new ArrayList<>();
            for (int i = 0; i < entity.getCenterImageList().size(); i++) {
                AdvEntity.ListBean listBean = new AdvEntity.ListBean();
                listBean.setId(entity.getCenterImageList().get(i).getId());
                listBean.setType(entity.getCenterImageList().get(i).getType());
                listBean.setName(entity.getCenterImageList().get(i).getName());
                listBean.setHref_url(entity.getCenterImageList().get(i).getHref_url());
                AdvEntity.ListBean.DataBean dataBean = new AdvEntity.ListBean.DataBean();
                dataBean.setId(entity.getCenterImageList().get(i).getData().getId());
                dataBean.setName(entity.getCenterImageList().get(i).getData().getName());
                listBean.setData(dataBean);
                pagerTemps.add(listBean);
            }
            pagerList2.clear();
            pagerList2.addAll(pagerTemps);
            hpb_adv.setDelay(0).setPeriod(0).setAutoScrollEnable(false).setTouchAble(false).setSource(pagerList2).pauseScroll();
            hpb_adv.clearAnimation();
        }

        //推荐问答
        if (entity.getQuestionList() != null && entity.getQuestionList().size() > 0) {
            qusList = entity.getQuestionList();
            askAdapter.updateListView(qusList);
        }
        //滚动条
        if (entity.getConvertList() != null && entity.getConvertList().size() > 0) {
            converList = entity.getConvertList();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < converList.size(); i++) {
                builder.append(converList.get(i).getStr());
                builder.append("\u3000\u3000\u3000\u3000");
            }
            tv_move.setText(builder.toString());
        } else {
            tv_move.setText("");
        }

        setAdvPic(hpb_title, pagerList, 1);
        setAdvPic(hpb_adv, pagerList2, 2);
    }


    /**
     * setHomeBanner:【填充广告的数据】. <br/>
     */
    private List<AdvEntity.ListBean> pagerList = new ArrayList<>();
    private List<AdvEntity.ListBean> pagerList2 = new ArrayList<>();

    private void setAdvPic(HomePageBanner hpb, List<AdvEntity.ListBean> pagerlist, int flag) {
        if (hpb == null) {
            return;
        }
        if (!(pagerlist.size() > 0)) {
            if (flag == 1) {
                AdvEntity.ListBean advEntity1 = new AdvEntity.ListBean();
                advEntity1.setDrawable(R.drawable.ic_adv_default3);
                advEntity1.setType(-1);
                advEntity1.setName("");
                pagerlist.add(advEntity1);
            } else {
                AdvEntity.ListBean advEntity1 = new AdvEntity.ListBean();
                advEntity1.setDrawable(R.drawable.ic_adv_default1);
                advEntity1.setType(-1);
                advEntity1.setName("");
                pagerlist.add(advEntity1);
            }
            hpb.pauseScroll();
            hpb.clearAnimation();
        }
        setHomeBanner(hpb, pagerlist);
    }

    /**
     * setHomeBanner:【填充广告的数据】. <br/>
     */
    private void setHomeBanner(HomePageBanner hpb, final List<AdvEntity.ListBean> pagerlist) {
        if (1 == pagerlist.size()) {
            hpb.setDelay(3).setPeriod(3).setAutoScrollEnable(false)
                    .setTouchAble(false).setSource(pagerlist).startScroll(); // 只有一张广告设置不能滑动可以点击
        } else {// 否则执行自动滚动并设置为可以手动滑动也可以点击
            hpb.setDelay(3).setPeriod(3).setAutoScrollEnable(true)
                    .setTouchAble(true).setSource(pagerlist).startScroll();
        }

        hpb.setOnItemClickL(new BaseBanner.OnItemClickL() {

            @Override
            public void onItemClick(int position) {
                if (pagerlist != null && pagerlist.size() > 0) {
                    int type = pagerlist.get(position).getType();

                    switch (type) {
                        case 0:
                            Intent intent = new Intent();
                            String url = pagerlist.get(position).getHref_url();
                            intent.putExtra("url", pagerlist.get(position).getHref_url());
                            if (url.contains("pdfid")) {
                                intent.putExtra("articleid", url.substring(url.indexOf("id=") + 3, url.indexOf("&pdfid=")));
                                intent.setClass(getActivity(), PdfPreviewActivity.class);
                            } else {
                                intent.setClass(getActivity(), WebViewDemo.class);
                            }
                            startActivity(intent);
                            break;
                        case 1:
                            startActivity(new Intent(getActivity(), ShopGoodsListActivity.class));
                            break;
                        case 2:
                            startActivity(new Intent(getActivity(), QuestionSortActivity.class));
                            break;
                        case 3:
                            Intent intentChannel = new Intent(getActivity(), WorkCiecleChannelActivity.class);
                            intentChannel.putExtra("channelID", Integer.valueOf(pagerlist.get(position).getData().getId()));
                            intentChannel.putExtra("channelName", pagerlist.get(position).getData().getName());
                            startActivity(intentChannel);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
        if (hpb_title != null) {
            hpb_title.pauseScroll();
            hpb_title = null;
        }
        if (hpb_adv != null) {
            hpb_adv.pauseScroll();
            hpb_adv = null;
        }

        if (falshdrawable != null) {
            falshdrawable.stop();
            falshdrawable = null;
        }

        if (null != dWindow && dWindow.isShowing()) {
            dWindow.dismiss();
        }

        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_que:
                Intent in = new Intent(getActivity(), QuestionSortActivity.class);
                startActivity(in);
                break;
            case R.id.ll_search_sharp:
                showSearchPop();
                break;
            case R.id.tv_move:
                startActivity(new Intent(getActivity(), ShopGoodsListActivity.class));
                break;
        }
    }

    private SearchPopWindow dWindow;
    List<HotVo> hotList = new ArrayList<>();

    public void getHotCache() {
        hotList.clear();
        String str = SharedPreferencesUtil.getString(getActivity(), "hotkey", "");
        List<HotVo> cachelist = null;
        try {
            cachelist = gson.fromJson(str, new TypeToken<List<HotVo>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if (null != cachelist) {
            hotList.addAll(cachelist);
        }
    }

    public void showSearchPop() {
        dWindow = new SearchPopWindow(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                dWindow.dismiss();

            }
        }, getActivity(), hotList, "0", "");
        dWindow.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    private SparseArray recordSp = new SparseArray(0);
    private int mCurrentfirstVisibleItem = 0;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mCurrentfirstVisibleItem = firstVisibleItem;
        View firstView = view.getChildAt(0);
        if (null != firstView) {
            ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
            if (null == itemRecord) {
                itemRecord = new ItemRecod();
            }
            itemRecord.height = firstView.getHeight();
            itemRecord.top = firstView.getTop();
            recordSp.append(firstVisibleItem, itemRecord);

            int h = getScrollY();//滚动距离
//                    ToastUtil.showMessage("滑动距离"+h);
            if (h > 5) {
                ll_search.setBackgroundColor(getResources().getColor(R.color.white));
                ll_search_sharp.setBackgroundResource(R.drawable.btn_shape_gray_all_s);
                if (h > 100) {
                    ll_search.getBackground().setAlpha(255);//0~255透明度值 ，0为完全透明，255为不透明
                    ll_search_sharp.getBackground().setAlpha(255);//0~255透明度值 ，0为完全透明，255
                    lineno.setVisibility(View.VISIBLE);
                } else {
                    ll_search.getBackground().setAlpha(h * 2);//0~255透明度值 ，0为完全透明，255为不透明
                    ll_search_sharp.getBackground().setAlpha(h * 2);//0~255透明度值 ，0为完全透明，255为不透明
                    lineno.setVisibility(View.GONE);
                }
            } else {
                ll_search.setBackgroundColor(getResources().getColor(R.color.color_00ffffff));
                ll_search_sharp.setBackgroundResource(R.drawable.btn_shape_gray_half);
                lineno.setVisibility(View.GONE);
            }

        }
    }

    private int getScrollY() {
        int height = 0;
        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
            ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
            if (itemRecod != null) {
                height += itemRecod.height;
            }
        }
        ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;
    }

    class ItemRecod {
        int height = 0;
        int top = 0;
    }

    class ChannelAdapter extends EasyBaseAdapter<HotVo> {
        private TextView tv_channel;
        private ImageView iv_channel;

        public ChannelAdapter(Context context, int layoutId, List<HotVo> list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(EasyViewHolder viewHolder, HotVo list) {
            tv_channel = viewHolder.getTextView(R.id.tv_channel);
            iv_channel = viewHolder.getImageView(R.id.iv_channel);
            tv_channel.setText(list.getName());
            iv_channel.setImageResource(list.getSrcId());
        }


    }

    class AskAdapter extends EasyBaseAdapter<WorkCircleMainEntity.QuestionListBean> {
        private TextView tv_content;
        private ImageView iv_tag;

        public AskAdapter(Context context, int layoutId, List<WorkCircleMainEntity.QuestionListBean> list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(EasyViewHolder viewHolder, WorkCircleMainEntity.QuestionListBean list) {
            tv_content = viewHolder.getTextView(R.id.tv_content);
            iv_tag = viewHolder.getImageView(R.id.iv_tag);
            tv_content.setText(list.getTitle());


            if (list.getIntegral() > 0) {
                String frontText = "【悬赏" + list.getIntegral() + "橄榄】";
                tv_content.setText(frontText + list.getTitle());
                AbStrUtil.setPiceTextCorlor(getResources().getColor(R.color.top_color), tv_content, tv_content.getText().toString(), frontText.length(), 0);
            } else {
                tv_content.setText(list.getTitle());
            }

            if (list.getA_id() == 0) {
                iv_tag.setImageResource(R.drawable.ic_circle_yes);
            } else {
                iv_tag.setImageResource(R.drawable.ic_circle_no);
            }

        }


    }


    class RecommenAdapter extends EasyBaseAdapter<WorkCircleRecommentEntity.ListBean> {

        private TextView titleTv;
        private LinearLayout imgLay;
        private ImageView imgView1;
        private ImageView imgView2;
        private ImageView imgView3;
        private TextView tv_tag;
        private TextView tv_see;
        private TextView tv_like;
        private TextView tv_commont;
        private ImageView imgView;

        public RecommenAdapter(Context context, int layoutId, List<WorkCircleRecommentEntity.ListBean> list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(EasyViewHolder viewHolder, WorkCircleRecommentEntity.ListBean list) {
            titleTv = viewHolder.getTextView(R.id.titleTv);
            imgLay = viewHolder.getLinearLayout(R.id.imgLay);
            imgView1 = viewHolder.getImageView(R.id.imgView1);
            imgView2 = viewHolder.getImageView(R.id.imgView2);
            imgView3 = viewHolder.getImageView(R.id.imgView3);
            tv_tag = viewHolder.getTextView(R.id.tv_tag);
            tv_see = viewHolder.getTextView(R.id.tv_see);
            tv_like = viewHolder.getTextView(R.id.tv_like);
            tv_commont = viewHolder.getTextView(R.id.tv_commont);
            imgView = viewHolder.getImageView(R.id.imgView);


            tv_tag.setText(list.getChannel_name());
            tv_tag.setVisibility(View.VISIBLE);
            int tagLength = list.getChannel_name().length();
            String tagStr = "   ";
            for (int i = 0; i < tagLength; i++) {
                tagStr = tagStr + ("    ");
            }
            titleTv.setText(tagStr + list.getTitle());
            tv_see.setText(list.getRead_count() + "");
            tv_like.setText(list.getLike_count() + "赞");
            tv_commont.setText(list.getComment_count() + "评论");
//            list_type	列表类型  0：纯文本 1：单图  3：三张图
            switch (list.getList_type()) {
                case 0:
                    imgLay.setVisibility(View.GONE);
                    imgView.setVisibility(View.GONE);
                    break;
                case 1:
                    imgLay.setVisibility(View.GONE);
                    imgView.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
                            + list.getAttachment().get(0).getFile_name(), imgView,options);
                    break;
                case 3:
                    imgLay.setVisibility(View.VISIBLE);
                    imgView.setVisibility(View.GONE);
                    if(list.getAttachment().size()>=3){

                    ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
                            + list.getAttachment().get(0).getFile_name(), imgView1, options);

                    ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
                            + list.getAttachment().get(1).getFile_name(), imgView2, options);

                    ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
                            + list.getAttachment().get(2).getFile_name(), imgView3,  options);
                    }
                    break;
                default:
                    break;

            }

        }
    }


}
