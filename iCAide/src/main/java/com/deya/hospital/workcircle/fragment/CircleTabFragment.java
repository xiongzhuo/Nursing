package com.deya.hospital.workcircle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.hospital.dypdf.PdfPreviewActivity;
import com.deya.acaide.R;
import com.deya.hospital.base.BaseViewPagerFragment;
import com.deya.hospital.base.EasyBaseAdapter;
import com.deya.hospital.base.EasyViewHolder;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.LoadingView;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.AdvEntity;
import com.deya.hospital.vo.WorkCircleRecommentEntity;
import com.deya.hospital.workcircle.WebViewDtail;
import com.deya.hospital.workspace.TaskUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作圈-普通列表
 */
public class CircleTabFragment extends BaseViewPagerFragment implements View.OnClickListener {
    private static final String TAG = "com.deya.acaide.workcircle.fragment.CircleTabFragment";
    private static final int GET_DATA_SUCESS = 0x6050;
    private static final int GET_DATA_FAIL = 0x06051;
    private Tools tools;
    private com.handmark.pulltorefresh.library.PullToRefreshListView listview;
    private LoadingView loadingView;

    private int pageIndex = 1;
    private MyAdapter myAdapter;
    private boolean isRefresh = true;
    private List<WorkCircleRecommentEntity.ListBean> listBeen = new ArrayList<>();

    private MyHandler myHandler = null;
    private List<AdvEntity.ListBean> pagerList = new ArrayList<>();
    private int channel_id = 0;
    private JSONObject json_list = new JSONObject();;
    private DisplayImageOptions options;


    /**
     * 传入需要的参数，设置给arguments
     *
     * @param id
     * @return
     */
    public static CircleTabFragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, id);
        CircleTabFragment contentFragment = new CircleTabFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    protected int setBaseView() {
        options= AbViewUtil.getOptions(R.drawable.defult_img);
        tools = new Tools(getActivity(), Constants.AC);
        return R.layout.frgment_work_recommend;
    }

    @Override
    protected void initView() {
        bindViews();
        initHandler();
        initEvent();
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            channel_id = bundle.getInt(TAG);
        }

        if (listBeen == null || listBeen.size() <= 0) {
            getJsonChache();
            requestData(pageIndex);
        }

    }

    private void bindViews() {
        listview = getViewById(R.id.listview);
        loadingView = getViewById(R.id.loadingView);
    }

    private void initHandler() {
        myHandler = new MyHandler(getActivity()) {
            @Override
            public void handleMessage(Message msg) {
                if (loadingView != null) {
                    loadingView.stopAnimition();
                    loadingView.setVisibility(View.GONE);
                }
                switch (msg.what) {
                    case GET_DATA_SUCESS:
                        listview.onRefreshComplete();
                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                            try {
                                WorkCircleRecommentEntity entity = TaskUtils.gson.fromJson(msg.obj.toString(), WorkCircleRecommentEntity.class);
                                if (entity != null) {
                                    if (entity.getResult_id().equals("0")) {
                                        setRequestData(entity, msg.obj.toString());
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
                        listview.onRefreshComplete();
                        ToastUtils.showToast(getActivity(), "亲，您的网络不顺畅哦！");
                        break;
                    default:
                        break;
                }

            }
        };
    }

    private void initEvent() {
        loadingView.setLoadingListener(new LoadingView.LoadingStateInter() {

            @Override
            public void onloadingStart() {
                loadingView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onloadingFinish() {
                loadingView.setVisibility(View.GONE);

            }
        });
        loadingView.setVisibility(View.VISIBLE);
        loadingView.startAnimition();
        myAdapter = new MyAdapter(getActivity(), R.layout.adapter_item_recommend, listBeen);
        listview.setAdapter(myAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int mPosition = position - 1;
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

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                pageIndex = 1;
                requestData(pageIndex);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = false;
                pageIndex = pageIndex + 1;
                requestData(pageIndex);
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void requestData(int pageIndex) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("pageIndex", pageIndex);
            job.put("channel_id", channel_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onCirclModeRequest(myHandler, getActivity(), GET_DATA_SUCESS, GET_DATA_FAIL,
                job, "workCircle/articleList");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (loadingView != null) {
            loadingView.stopAnimition();
            loadingView = null;
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void setRequestData(WorkCircleRecommentEntity entity, String json) {

        if (entity.getList() != null && entity.getList().size() > 0) {
            if (isRefresh) {
                saveJsonChache(json);
                //下拉刷新
                listBeen.clear();
            }
            if (entity.getPageTotal() == pageIndex) {
                listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            } else {
                listview.setMode(PullToRefreshBase.Mode.BOTH);
            }
            listBeen.addAll(entity.getList());
            myAdapter.updateListView(listBeen);
        }

    }

    public void getJsonChache() {
        String str = SharedPreferencesUtil.getString(getActivity(), TAG, "");
        try {
            if (AbStrUtil.isEmpty(str)) {
                json_list = new JSONObject();
            } else {
                json_list = new JSONObject(str);
            }
            if (json_list.has(TAG + channel_id)) {
                String chacheStr = json_list.optString(TAG + channel_id);
                if (AbStrUtil.isEmpty(chacheStr)) {
                    return;
                }
                JSONObject job = new JSONObject(chacheStr);
                if (!AbStrUtil.isEmpty(job.optString("json_str"))) {
                    loadingView.stopAnimition();
                    loadingView.setVisibility(View.GONE);
                    WorkCircleRecommentEntity entity =TaskUtils.gson.fromJson(job.optString("json_str"), WorkCircleRecommentEntity.class);
                    setRequestData(entity, job.optString("json_str"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void saveJsonChache(String json) {

        JSONObject job = new JSONObject();
        try {
            job.put("json_str", json);
            json_list.put(TAG+channel_id, job.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferencesUtil.saveString(getActivity(), TAG, json_list.toString());
    }

    class MyAdapter extends EasyBaseAdapter<WorkCircleRecommentEntity.ListBean> {

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

        public MyAdapter(Context context, int layoutId, List<WorkCircleRecommentEntity.ListBean> list) {
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

            if (!AbStrUtil.isEmpty(list.getTitle())) {
                titleTv.setText(list.getTitle());
            }
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
                            + list.getAttachment().get(0).getFile_name(), imgView, options);
                    break;
                case 3:
                    imgLay.setVisibility(View.VISIBLE);
                    imgView.setVisibility(View.GONE);

                    ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
                            + list.getAttachment().get(0).getFile_name(), imgView1, options);

                    ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
                            + list.getAttachment().get(1).getFile_name(), imgView2, options);

                    ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
                            + list.getAttachment().get(2).getFile_name(), imgView3, options);
                    break;
                default:
                    break;

            }

        }
    }
}
