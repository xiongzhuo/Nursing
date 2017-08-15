package com.deya.hospital.workcircle.fragment;//package com.deya.acaide.workcircle.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.deya.acaide.R;
//import com.deya.acaide.base.BaseViewPagerFragment;
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
//import com.deya.acaide.util.Tools;
//import com.deya.acaide.util.WebUrl;
//import com.deya.acaide.vo.WorkCircleDocumentEntity;
//import com.deya.acaide.workcircle.DocumentActivity;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 工作圈-文献首页
// */
//public class CircleDocumentFragment2 extends BaseViewPagerFragment implements View.OnClickListener {
//    private static final String TAG = "com.deya.acaide.workcircle.fragment.CircleDocumentFragment";
//    private static final int GET_DATA_SUCESS = 0x6050;
//    private static final int GET_DATA_FAIL = 0x06051;
//    private Tools tools;
//    private com.handmark.pulltorefresh.library.PullToRefreshListView listview;
//    private LoadingView loadingView;
//
//    private MyAdapter myAdapter;
//    private boolean isRefresh = true;
//    private List<WorkCircleDocumentEntity.CategoryListBean> listBeen = new ArrayList<>();
//
//    private MyHandler myHandler = null;
//
//    /**
//     * 传入需要的参数，设置给arguments
//     *
//     * @param id
//     * @return
//     */
//    public static CircleDocumentFragment2 newInstance(int id) {
//        Bundle bundle = new Bundle();
//        bundle.putInt(TAG, id);
//        CircleDocumentFragment2 contentFragment = new CircleDocumentFragment2();
//        contentFragment.setArguments(bundle);
//        return contentFragment;
//    }
//
//    @Override
//    protected int setBaseView() {
//        tools = new Tools(getActivity(), Constants.AC);
//        return R.layout.frgment_work_recommend;
//    }
//
//    @Override
//    protected void initView() {
//        bindViews();
//        initHandler();
//        initEvent();
//    }
//
//    @Override
//    protected void initData() {
//        if (listBeen == null || listBeen.size() <= 0) {
//            getCache();
//            requestData();
//        }
//    }
//
//    private void bindViews() {
//        listview = getViewById(R.id.listview);
//        loadingView = getViewById(R.id.loadingView);
//    }
//
//    private void initHandler() {
//        myHandler = new MyHandler(getActivity()) {
//            @Override
//            public void handleMessage(Message msg) {
//                loadingView.stopAnimition();
//                loadingView.setVisibility(View.GONE);
//                switch (msg.what) {
//                    case GET_DATA_SUCESS:
//                        listview.onRefreshComplete();
//                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
//                            try {
//                                WorkCircleDocumentEntity entity = GsonImpl.get().toObject(msg.obj.toString(), WorkCircleDocumentEntity.class);
//                                if (entity != null) {
//                                    if (entity.getResult_id() == 0) {
//                                        if (isRefresh) {
//                                            SharedPreferencesUtil.saveString(getActivity(), TAG, msg.obj.toString());
//                                        }
//                                        setRequestData(entity);
//                                    } else {
//                                        ToastUtils.showToast(getActivity(), entity.getResult_msg());
//                                    }
//                                } else {
////                                    if (isRefresh) {
////                                        loadingView.setVisibility(View.VISIBLE);
////                                        ToastUtils.showToast(getActivity(),"亲，您的网络不顺畅哦！");
////                                        loadingView.setEmptyView("亲，您的网络不顺畅哦，\n请检查您的网络，再继续访问！");
////                                    }
//                                }
//                            } catch (Exception e5) {
//                                e5.printStackTrace();
//                            }
//                        }
//                        break;
//                    case GET_DATA_FAIL:
//                        listview.onRefreshComplete();
////                        if (isRefresh) {
////                            loadingView.setVisibility(View.VISIBLE);
////                            ToastUtils.showToast(getActivity(),"亲，您的网络不顺畅哦！");
////                            loadingView.setEmptyView("亲，您的网络不顺畅哦，\n请检查您的网络，再继续访问！");
////                        }
//                        break;
//
//                    default:
//                        break;
//                }
//
//            }
//        };
//    }
//
//    private void initEvent() {
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
//        myAdapter = new MyAdapter(getActivity(), R.layout.adapter_item_circledocument, listBeen);
//        listview.setAdapter(myAdapter);
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                int mPosition = position - 1;
//                if (mPosition >= listBeen.size()) {
//                    mPosition = listBeen.size() - 1;
//                }
//                if (listBeen.size() > 0 && position > 0) {
//
//                    Intent it = new Intent();
//                    it.putExtra("category_id",listBeen.get(mPosition).getId()+"");
//                    it.putExtra("channelId","10088");
//                    it.putExtra("channelName","文献");
//                    it.setClass(getActivity(), DocumentActivity.class);
//                    startActivity(it);
//                }
//            }
//        });
//
//        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                isRefresh = true;
//                requestData();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//            }
//        });
//    }
//
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }
//
//    private void requestData() {
//        JSONObject job = new JSONObject();
//        try {
//            job.put("authent", tools.getValue(Constants.AUTHENT));
//            job.put("category_id", "");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
//                GET_DATA_SUCESS, GET_DATA_FAIL, job, "workCircle/category");
//
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        if (loadingView != null) {
//            loadingView.stopAnimition();
//            loadingView = null;
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//    private void setRequestData(WorkCircleDocumentEntity entity) {
//
//        if (entity.getCategory_list() != null && entity.getCategory_list().size() > 0) {
//            if (isRefresh) {
//                //下拉刷新
//                listBeen.clear();
//            }
////			if (entity.getPageTotal() == pageIndex) {
//            listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
////			} else {
////				listview.setMode(PullToRefreshBase.Mode.BOTH);
////			}
//            listBeen.addAll(entity.getCategory_list());
//            myAdapter.updateListView(listBeen);
//        } else {
//            if (isRefresh) {
//                loadingView.setVisibility(View.VISIBLE);
//                loadingView.setEmptyView("抱歉，没有找到相关的内容，\n请稍后再试");
//            }
//        }
//
//    }
//
//    public void getCache() {
//        String cacheStr = SharedPreferencesUtil.getString(getActivity(), TAG, "");
//        if (!AbStrUtil.isEmpty(cacheStr)) {
//            WorkCircleDocumentEntity entity = GsonImpl.get().toObject(cacheStr, WorkCircleDocumentEntity.class);
//            setRequestData(entity);
//        }
//    }
//
//    class MyAdapter extends EasyBaseAdapter<WorkCircleDocumentEntity.CategoryListBean> {
//
//        private TextView tv_title;
//        private TextView tv_content;
//        private ImageView imgView;
//
//        public MyAdapter(Context context, int layoutId, List<WorkCircleDocumentEntity.CategoryListBean> list) {
//            super(context, layoutId, list);
//        }
//
//        @Override
//        public void convert(EasyViewHolder viewHolder, WorkCircleDocumentEntity.CategoryListBean list) {
//            tv_title = viewHolder.getTextView(R.id.tv_title);
//            imgView = viewHolder.getImageView(R.id.imgView);
//            tv_content = viewHolder.getTextView(R.id.tv_content);
//
//            if (!AbStrUtil.isEmpty(list.getTitle())) {
//                tv_title.setText(list.getTitle());
//            }
//            if (!AbStrUtil.isEmpty(list.getIntroduce())) {
//                tv_content.setText(list.getIntroduce());
//            }
//            if (!AbStrUtil.isEmpty(list.getImg())) {
//                ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
//                        + list.getImg(), imgView, imgDisplayImageOptions(R.drawable.ic_document));
//            } else {
//                imgView.setImageResource(R.drawable.ic_document);
//            }
//
//
//        }
//    }
//}
