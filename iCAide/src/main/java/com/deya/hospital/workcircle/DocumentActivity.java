package com.deya.hospital.workcircle;//package com.deya.acaide.workcircle;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.artifex.mupdfdemo.PdfPreviewActivity;
//import com.deya.acaide.R;
//import com.deya.acaide.base.BaseActivity;
//import com.deya.acaide.base.EasyBaseAdapter;
//import com.deya.acaide.base.EasyViewHolder;
//import com.deya.acaide.base.GsonImpl;
//import com.deya.acaide.bizimp.MainBizImpl;
//import com.deya.acaide.util.AbStrUtil;
//import com.deya.acaide.util.Constants;
//import com.deya.acaide.util.LoadingView;
//import com.deya.acaide.util.MyHandler;
//import com.deya.acaide.util.SharedPreferencesUtil;
//import com.deya.acaide.util.ToastUtils;
//import com.deya.acaide.util.WebUrl;
//import com.deya.acaide.view.ScaleInTransformer;
//import com.deya.acaide.vo.DocumentCategoryEntity;
//import com.deya.acaide.vo.DocumentInfo;
//import com.deya.acaide.vo.HotVo;
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.im.sdk.dy.common.utils.LogUtil;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 文献栏目列表
// *
// * @author : yugq
// * @date 2016/7/6
// */
//public class DocumentActivity extends BaseActivity implements View.OnClickListener {
//    private static final int GET_TOPDATA_SUCESS = 0x6050;
//    private static final int GET_TOPDATA_FAIL = 0x06051;
//    private static final int GET_LISTDATA_SUCESS = 0x6052;
//    private static final int GET_LISTDATA_FAIL = 0x06053;
//
//    private com.deya.acaide.util.CommonTopView topView;
//    private LinearLayout searchLay;
//    private LinearLayout ic_knowing;
//    private EditText et_search;
//    //    private Gallery gallery;
//    private com.handmark.pulltorefresh.library.PullToRefreshListView listView;
//
//    private PictureAdapter adapter;
//    private List<DocumentCategoryEntity.CategoryListBean> list = new ArrayList<DocumentCategoryEntity.CategoryListBean>();
//    private List<HotVo> channelList = new ArrayList<HotVo>();
//    private List<HotVo> hotList = new ArrayList<HotVo>();
//    private List<DocumentInfo.CategoryListBean> documentInfos = new ArrayList<DocumentInfo.CategoryListBean>();
//    private com.deya.acaide.util.LoadingView loadingView;
//    private String channelId = "";
//    private String channelName = "文献";
//    private MyAdapter myAdapter = null;
//    private SearchPopWindow dWindow;
//    private int pageIndex = 1, pageSize = 10;
//    private String category_id = "";
//    private Gson gson = new Gson();
//    private boolean isRefresh = true;//是否刷新
//    private MyHandler myHandler = new MyHandler(this) {
//        @Override
//        public void handleMessage(Message msg) {
//            Activity activity = myHandler.mactivity.get();
//            if (null != activity) {
//                if (loadingView  != null) {
//                    loadingView.stopAnimition();
//                }
//                switch (msg.what) {
//                    case GET_TOPDATA_SUCESS:
//                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
//                            try {
//                                DocumentCategoryEntity entity = GsonImpl.get().toObject(msg.obj.toString(), DocumentCategoryEntity.class);
//                                if (entity != null) {
//                                    if (entity.getResult_id() == 0) {
//                                        loadingView.setVisibility(View.GONE);
//                                        setRequestData(entity);
//                                    } else {
//                                        ToastUtils.showToast(DocumentActivity.this, entity.getResult_msg());
//                                    }
//                                } else {
//                                    ToastUtils.showToast(DocumentActivity.this, "亲，您的网络不顺畅哦！");
//                                    loadingView.setVisibility(View.VISIBLE);
//                                    loadingView.setEmptyView("亲，您的网络不顺畅哦，\n 请检查您的网络，再继续访问！");
//                                }
//                            } catch (Exception e5) {
//                                e5.printStackTrace();
//                            }
//                        }
//                        break;
//                    case GET_TOPDATA_FAIL:
//                        loadingView.setVisibility(View.VISIBLE);
//                        ToastUtils.showToast(DocumentActivity.this, "亲，您的网络不顺畅哦！");
//                        loadingView.setEmptyView("亲，您的网络不顺畅哦，\n请检查您的网络，再继续访问！");
//                        break;
//                    case GET_LISTDATA_SUCESS:
//                        dismissdialog();
//                        listView.onRefreshComplete();
//                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
//                            try {
//                                DocumentInfo entity = GsonImpl.get().toObject(msg.obj.toString(), DocumentInfo.class);
//                                if (entity != null) {
//                                    if (entity.getResult_id() == 0) {
//                                        setListRequestData(entity);
//                                    } else {
//                                        ToastUtils.showToast(DocumentActivity.this, entity.getResult_msg());
//                                    }
//                                }
//                            } catch (Exception e5) {
//                                e5.printStackTrace();
//                            }
//                        }
//                        break;
//                    case GET_LISTDATA_FAIL:
//                        dismissdialog();
//                        listView.onRefreshComplete();
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//        }
//    };
//    private ViewPager mViewPager;
//    private MyPageAdapter mAdapter;
//    private FrameLayout gallery;
//    private ViewHold hold;
//    private View viewConter;
//    private View headView;
//    private LinearLayout ll_top;
//    private ListView listViewTem;
////    private boolean isFirst = true;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_document);
//        headView = LayoutInflater.from(this).inflate(R.layout.head_document, null);
//        getHotCache();
//        bindViews();
//        initEnvent();
//        requestTopData();
//    }
//
//    private void bindViews() {
//        topView = (com.deya.acaide.util.CommonTopView) findViewById(R.id.topView);
//        searchLay = (LinearLayout) findViewById(R.id.searchLay);
//        ic_knowing = (LinearLayout) findViewById(R.id.ic_knowing);
//        et_search = (EditText) findViewById(R.id.et_search);
////        gallery = (Gallery) findViewById(R.id.gallery);
//        listView = (com.handmark.pulltorefresh.library.PullToRefreshListView) findViewById(R.id.listview);
//        loadingView = (com.deya.acaide.util.LoadingView) findViewById(R.id.loadingView);
//        et_search.setOnClickListener(this);
//        setHeadView();
//
//        listViewTem = listView.getRefreshableView();
//        listViewTem.addHeaderView(headView);
//    }
//
//    private void initEnvent() {
//        topView.onbackClick(this, this);
//        listView.setMode(PullToRefreshBase.Mode.BOTH);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position < 2) {
//                    return;
//                }
//                Intent intent = new Intent();
//                if (!AbStrUtil.isEmpty(documentInfos.get(position - 2).getPdf_attach())) {
//                    intent.putExtra("pdf_attach", documentInfos.get(position - 2).getPdf_attach());
//                    intent.setClass(DocumentActivity.this, PdfPreviewActivity.class);
//                }
//                intent.putExtra("articleid", documentInfos.get(position - 2).getId() + "");
//
//                startActivity(intent);
//            }
//        });
//        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                isRefresh = true;
//                pageIndex = 1;
//                requestListData(pageIndex, pageSize, childlistId);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                isRefresh = false;
//                pageIndex = pageIndex + 1;
//                requestListData(pageIndex, pageSize, childlistId);
//            }
//        });
//        myAdapter = new MyAdapter(this, R.layout.item_document, documentInfos);
//        listView.setAdapter(myAdapter);
//
//        loadingView.setLoadingListener(new LoadingView.LoadingStateInter() {
//
//            @Override
//            public void onloadingStart() {
//                loadingView.setVisibility(View.VISIBLE);
//
//            }
//
//            @Override
//            public void onloadingFinish() {
//                loadingView.setVisibility(View.GONE);
//
//            }
//        });
//        loadingView.setVisibility(View.VISIBLE);
//        loadingView.startAnimition();
//
//    }
//
//    private void setHeadView() {
//        ll_top = (LinearLayout) headView.findViewById(R.id.ll_top);
//        gallery = (FrameLayout) headView.findViewById(R.id.gallery);
//        mViewPager = (ViewPager) headView.findViewById(R.id.id_viewpager);
//        mViewPager.setOffscreenPageLimit(3);
//        gallery.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return mViewPager.dispatchTouchEvent(event);
//            }
//        });
//        MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener();
//        mViewPager.setOnPageChangeListener(myOnPageChangeListener);
//        mViewPager.setCurrentItem(1);
//        mAdapter = new MyPageAdapter();
//        mViewPager.setAdapter(mAdapter);
//        mViewPager.setPageMargin(5);
//        // mViewPager.setPageTransformer(true, new AlphaPageTransformer());
//        mViewPager.setPageTransformer(true, new ScaleInTransformer());
//
////        adapter = new PictureAdapter(this, list,myhandler);
////        gallery.setAdapter(adapter);
////        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////
////            @Override
////            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                adapter.setSelectItem(position);
////            }
////            @Override
////            public void onNothingSelected(AdapterView<?> parent) {
////            }
////        });
//    }
//
//    private void setTopText() {
//        if (list != null && list.size() > 0 ) {
//            for (int i = 0; i < list.size(); i++) {
//                if (mViewPager.getChildAt(i) != null) {
//                    Message message = new Message();
//                    message.what = 2;
//                    message.arg1 = list.get(i).getId();
//                    message.arg2 = wh;
//                    message.obj = (TextView) mViewPager.getChildAt(i).findViewById(R.id.text);
//                    myhandler.sendMessage(message);
//                }
//
//            }
//        }
//    }
//
//    public class MyPageAdapter extends PagerAdapter {
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            LogUtil.i("position", "instantiateItem==" + position);
//            hold = new ViewHold();
//            viewConter = LayoutInflater.from(mcontext).inflate(R.layout.gallery_item, null);
//            hold.mImageView = (ImageView) viewConter.findViewById(R.id.imageview);
//            hold.mTextView = (TextView) viewConter.findViewById(R.id.text);
//
//            if (!AbStrUtil.isEmpty(list.get(position).getImg())) {
//                ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
//                        + list.get(position).getImg(), hold.mImageView, imgDisplayImageOptions(R.drawable.ic_document));
//            } else {
//                hold.mImageView.setImageResource(R.drawable.ic_document);
//            }
//
//            hold.mTextView.setFocusable(false);
//            hold.mTextView.setBackgroundResource(R.color.white);
//            hold.mTextView.setTextColor(mcontext.getResources().getColor(R.color.content_black));
//            hold.mTextView.setTextSize(14);
//            hold.mTextView.setText(list.get(position).getTitle());
////                setTopText();
//
//            container.addView(viewConter);
////                view.setAdjustViewBounds(true);
//            return viewConter;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object o) {
//            return view == o;
//        }
//    }
//
//    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//
//        @Override
//        public void onPageSelected(int position) {
//            LogUtil.i("position", "onPageSelected==" + position);
//            if (hold != null) {
//                setTopText();
//                if (mViewPager.getCurrentItem() == position && list.size() > 0) {
////                    int widthPix = mcontext.getResources().getDisplayMetrics().widthPixels;
////                    int wh = (int) Math.floor(widthPix * 0.36);
//                    Message message = new Message();
//                    message.what = 1;
//                    message.arg1 = list.get(position).getId();
////                    message.arg2 = wh;
//                    message.obj = (TextView) mViewPager.getChildAt(position).findViewById(R.id.text);
//                    myhandler.sendMessage(message);
//                }
//            }
//        }
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            // to refresh frameLayout
//            if (gallery != null) {
//                gallery.invalidate();
//            }
//
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//        }
//
//    }
//
//    static class ViewHold {
//        public TextView mTextView;
//        private ImageView mImageView;
//    }
//
//    private void requestTopData() {
//        JSONObject job = new JSONObject();
//        try {
//            job.put("authent", tools.getValue(Constants.AUTHENT));
//            job.put("category_id", category_id);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        MainBizImpl.getInstance().onComomRequest(myHandler,
//                this, GET_TOPDATA_SUCESS, GET_TOPDATA_FAIL, job,
//                "workCircle/category");
//
//    }
//
//    private void requestListData(int pageIndex, int pageSize, String id) {
////        if (!isFirst) {
//        showprocessdialog();
//        JSONObject job = new JSONObject();
//        try {
//            job.put("authent", tools.getValue(Constants.AUTHENT));
//            job.put("category_id", id);
//            job.put("pageIndex", pageIndex + "");
//            job.put("pageSize", pageSize + "");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        MainBizImpl.getInstance().onComomRequest(myHandler,
//                this, GET_LISTDATA_SUCESS, GET_LISTDATA_FAIL, job, "workCircle/category/article");
////        }
////        isFirst = false;
//    }
//
//    private void setRequestData(DocumentCategoryEntity entity) {
//        if (entity.getCategory_list().size() > 0) {
//            topView.setTitle(entity.getCategory_list().get(0).getParent_title());
//            list = entity.getCategory_list();
//            mAdapter.notifyDataSetChanged();
//            mViewPager.setCurrentItem(1);
//            mViewPager.setOffscreenPageLimit(list.size());
//            mAdapter.notifyDataSetChanged();
////            mAdapter = new MyPageAdapter();
////            mViewPager.setAdapter(mAdapter);
////            adapter.updataList(list,myhandler);
////            gallery.setSelection(1);
//        }
//    }
//
//    private void setListRequestData(DocumentInfo entity) {
//        if (entity.getCategory_list() != null && entity.getCategory_list().size() > 0) {
//            if (isRefresh) {
//                //下拉刷新
//                documentInfos.clear();
//            }
//            if (entity.getCategory_list().size() < pageSize) {
//                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//            } else {
//                listView.setMode(PullToRefreshBase.Mode.BOTH);
//            }
//            documentInfos.addAll(entity.getCategory_list());
//            myAdapter.updateListView(documentInfos);
//        }
//    }
//
//    CountDownTimer downTimer = null;
//    private String childlistId = "";
//    private String childlistIdNew = "";
//    private TextView textView;
//    private int wh;
//    private boolean isfirst = true;
//    private Handler myhandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    if (msg.obj != null) {
//                        childlistIdNew = msg.arg1 + "";
//                        if (downTimer != null) {
//                            downTimer.cancel();
//                        }
//                        textView = (TextView) msg.obj;
////                        wh = msg.arg2;
//                        if (isfirst && !AbStrUtil.isEmpty(childlistId)) {
//                            isfirst = false;
//                            textView.setBackgroundResource(R.drawable.btn_shape_fc7f1a);
////                                    textView.setWidth(wh);
//                            textView.setTextColor(mcontext.getResources().getColor(R.color.white));
//                            textView.setTextSize(16);
//                            textView.setFocusable(true);
//                            requestListData(pageIndex, pageSize, childlistId);
//                            mAdapter.notifyDataSetChanged();
//                            return;
//                        }
//
//
//                        downTimer = new CountDownTimer(1500, 500) {
//                            @Override
//                            public void onTick(long millisUntilFinished) {
//
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                if (!AbStrUtil.isEmpty(childlistIdNew) && !childlistId.equals(childlistIdNew)) {
//                                    textView.setBackgroundResource(R.drawable.btn_shape_fc7f1a);
////                                    textView.setWidth(wh);
//                                    textView.setTextColor(mcontext.getResources().getColor(R.color.white));
//                                    textView.setTextSize(16);
//                                    textView.setFocusable(true);
//                                    childlistId = childlistIdNew;
//                                    requestListData(pageIndex, pageSize, childlistId);
//                                    mAdapter.notifyDataSetChanged();
//                                }
//
//
//                            }
//                        };
//                        downTimer.start();
//
//                    }
//                case 2:
//                    textView = (TextView) msg.obj;
//                    textView.setFocusable(false);
//                    textView.setBackgroundResource(R.color.white);
//                    textView.setTextColor(mcontext.getResources().getColor(R.color.content_black));
//                    textView.setTextSize(14);
//                    break;
//
//            }
//        }
//    };
//
//    @Override
//    protected void onStop() {
//        if (null != loadingView) {
//            loadingView.stopAnimition();
//        }
//        super.onStop();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.rl_back:
//                finish();
//                break;
//            case R.id.et_search:
//                showSearchPop();
//                break;
//            default:
//                break;
//        }
//    }
//
//    public void showSearchPop() {
//        dWindow = new SearchPopWindow(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                dWindow.dismiss();
//            }
//        }, this, hotList, channelId != null ? channelId : "0", "文献");
//        dWindow.showAtLocation(DocumentActivity.this.findViewById(R.id.main), Gravity.BOTTOM
//                | Gravity.CENTER_HORIZONTAL, 0, 0);
//    }
//
//    public void getHotCache() {
//        category_id = getIntent().getStringExtra("category_id");
//        channelId = getIntent().getStringExtra("channelId");
//        channelName = getIntent().getStringExtra("channelName");
//        hotList.clear();
//        String str = SharedPreferencesUtil.getString(this, "hotkey", "");
//        List<HotVo> cachelist = null;
//        try {
//            cachelist = gson.fromJson(str, new TypeToken<List<HotVo>>() {
//            }.getType());
//        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
//        }
//        if (null != cachelist) {
//            hotList.addAll(cachelist);
//        }
//    }
//
//    class MyAdapter extends EasyBaseAdapter<DocumentInfo.CategoryListBean> {
//
//        private TextView tv_title;
//        private TextView tv_support;
//        private TextView tv_conment;
//        private TextView tv_reader;
//
//        public MyAdapter(Context context, int layoutId, List<DocumentInfo.CategoryListBean> list) {
//            super(context, layoutId, list);
//        }
//
//        @Override
//        public void convert(EasyViewHolder viewHolder, DocumentInfo.CategoryListBean info) {
//            tv_title = viewHolder.getTextView(R.id.tv_title);
//            tv_support = viewHolder.getTextView(R.id.tv_support);
//            tv_conment = viewHolder.getTextView(R.id.tv_conment);
//            tv_reader = viewHolder.getTextView(R.id.tv_reader);
//            tv_title.setText(info.getTitle());
//            tv_support.setText(info.getLike_count() + "赞");
//            tv_conment.setText(info.getComment_count() + "评论");
//            tv_reader.setText(info.getRead_count() + "");
//        }
//    }
//}
