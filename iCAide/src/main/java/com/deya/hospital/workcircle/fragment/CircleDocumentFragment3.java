package com.deya.hospital.workcircle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.deya.hospital.view.EcoGallery;
import com.deya.hospital.view.EcoGalleryAdapterView;
import com.deya.hospital.vo.DocumentCategoryEntity;
import com.deya.hospital.vo.DocumentInfo;
import com.deya.hospital.vo.HotVo;
import com.deya.hospital.workcircle.PictureAdapter;
import com.deya.hospital.workcircle.SearchPopWindow;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作圈-文献首页
 */
public class CircleDocumentFragment3 extends BaseViewPagerFragment implements View.OnClickListener {
    private static final int GET_TOPDATA_SUCESS = 0x6050;
    private static final int GET_TOPDATA_FAIL = 0x06051;
    private static final int GET_LISTDATA_SUCESS = 0x6052;
    private static final int GET_LISTDATA_FAIL = 0x06053;
    private static final String TAG = "com.deya.acaide.workcircle.fragment.CircleDocumentFragment";

    private EditText et_search;
    private com.handmark.pulltorefresh.library.PullToRefreshListView listView;

    private PictureAdapter adapter;
    private List<DocumentCategoryEntity.CategoryListBean> list = new ArrayList<DocumentCategoryEntity.CategoryListBean>();
    private List<HotVo> hotList = new ArrayList<HotVo>();
    private List<DocumentInfo.CategoryListBean> documentInfos = new ArrayList<DocumentInfo.CategoryListBean>();
    private LoadingView loadingView;
    private String channelId = "";
    private String channelName = "文献";
    private MyAdapter myAdapter = null;
    private SearchPopWindow dWindow;
    private int pageIndex = 1, pageSize = 10;
    private String category_id = "";
    private Gson gson = new Gson();
    private boolean isRefresh = true;//是否刷新
    private MyHandler myHandler;
    private EcoGallery gallery;
    private ViewHold hold;
    //    private View viewConter;
    private View headView;
    private LinearLayout ll_top;
    private ListView listViewTem;
    private Tools tools;
    private GalleryAdapter mAdapter;
    private int IDPosition = -1;

    /**
     * 传入需要的参数，设置给arguments
     *
     * @param id
     * @return
     */
    public static CircleDocumentFragment3 newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, id);
        CircleDocumentFragment3 contentFragment = new CircleDocumentFragment3();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    protected int setBaseView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_document2, null);
        return R.layout.activity_document2;
    }

    @Override
    protected void initView() {
        tools = new Tools(getActivity(), Constants.AC);
        getHotCache();
        bindViews();
        initHandler();
        initEnvent();
        requestTopData();

    }

    private void initHandler() {
        myHandler = new MyHandler(getActivity()) {
            @Override
            public void handleMessage(Message msg) {
                if (loadingView != null) {
                    loadingView.stopAnimition();
                }
                switch (msg.what) {
                    case GET_TOPDATA_SUCESS:
                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                            try {
                                DocumentCategoryEntity entity = TaskUtils.gson.fromJson(msg.obj.toString(), DocumentCategoryEntity.class);
                                if (entity != null) {
                                    if (entity.getResult_id() == 0) {
                                        loadingView.setVisibility(View.GONE);
                                        setRequestData(entity);
                                    } else {
                                        ToastUtils.showToast(getActivity(), entity.getResult_msg());
                                    }
                                } else {
                                    ToastUtil.showMessage( "亲，您的网络不顺畅哦！");
                                    loadingView.setVisibility(View.VISIBLE);
                                    loadingView.setEmptyView("亲，您的网络不顺畅哦，\n 请检查您的网络，再继续访问！");
                                }
                            } catch (Exception e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case GET_TOPDATA_FAIL:
                        loadingView.setVisibility(View.VISIBLE);
                        ToastUtil.showMessage("亲，您的网络不顺畅哦！");
                        loadingView.setEmptyView("亲，您的网络不顺畅哦，\n请检查您的网络，再继续访问！");
                        break;
                    case GET_LISTDATA_SUCESS:
                        dismissdialog();
                        listView.onRefreshComplete();
                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                            try {
                                DocumentInfo entity = TaskUtils.gson.fromJson(msg.obj.toString(), DocumentInfo.class);
                                if (entity != null) {
                                    if (entity.getResult_id() == 0) {
                                        setListRequestData(entity);
                                    } else {
                                        ToastUtils.showToast(getActivity(), entity.getResult_msg());
                                    }
                                }
                            } catch (Exception e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case GET_LISTDATA_FAIL:
                        dismissdialog();
                        listView.onRefreshComplete();
                        break;

                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void initData() {

    }

    private void bindViews() {
        et_search = getViewById(R.id.et_search);
        listView = getViewById(R.id.listview);
        loadingView = getViewById(R.id.loadingView);
        et_search.setOnClickListener(this);
        setHeadView();

        listViewTem = listView.getRefreshableView();
        listViewTem.addHeaderView(headView);

    }

    private void setHeadView() {
        ll_top = (LinearLayout) headView.findViewById(R.id.ll_top);
        gallery = (com.deya.hospital.view.EcoGallery) headView.findViewById(R.id.gallery);
        mAdapter = new GalleryAdapter();
        gallery.setAdapter(mAdapter);
        gallery.setOnItemSelectedListener(new EcoGalleryAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(EcoGalleryAdapterView<?> parent, View view, int position, long id) {
//                setCleanTopText();
                if (list != null && list.size() > 0) {
                    Message message = new Message();
                    message.what = 1;
                    message.arg1 = list.get(position).getId();
                    message.arg2 = position;
                    galleryHandler.sendMessage(message);
                }

            }

            @Override
            public void onNothingSelected(EcoGalleryAdapterView<?> parent) {
                Toast.makeText(mContext, "Selected" + parent, Toast.LENGTH_SHORT).show();
            }
        });
        gallery.setOnItemClickListener(new EcoGalleryAdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(EcoGalleryAdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    private void initEnvent() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 2) {
                    return;
                }
                Intent intent = new Intent();
                if (!AbStrUtil.isEmpty(documentInfos.get(position - 2).getPdf_attach())) {
                    intent.putExtra("pdf_attach", documentInfos.get(position - 2).getPdf_attach());
                    intent.setClass(getActivity(), PdfPreviewActivity.class);
                }
                intent.putExtra("articleid", documentInfos.get(position - 2).getId() + "");

                startActivity(intent);
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                pageIndex = 1;
                requestListData(pageIndex, pageSize, childlistId);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = false;
                pageIndex = pageIndex + 1;
                requestListData(pageIndex, pageSize, childlistId);
            }
        });
        myAdapter = new MyAdapter(getActivity(), R.layout.item_document, documentInfos);
        listView.setAdapter(myAdapter);

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

    }


    static class ViewHold {
        public TextView mTextView;
        private ImageView mImageView;
    }

    private void requestTopData() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("category_id", category_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onCirclModeRequest(myHandler,
                getActivity(), GET_TOPDATA_SUCESS, GET_TOPDATA_FAIL, job,
                "workCircle/category");

    }

    private void requestListData(int pageIndex, int pageSize, String id) {
//        if (!isFirst) {
        if (isfirst) {
            showprocessdialog();
        }
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("category_id", id);
            job.put("pageIndex", pageIndex + "");
            job.put("pageSize", pageSize + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onCirclModeRequest(myHandler,
                getActivity(), GET_LISTDATA_SUCESS, GET_LISTDATA_FAIL, job, "workCircle/category/article");
//        }
//        isFirst = false;
    }


    private void setRequestData(DocumentCategoryEntity entity) {
        if (entity.getCategory_list().size() > 0) {
            list = entity.getCategory_list();
            mAdapter.notifyDataSetChanged();
            gallery.setSelection(1);
        }
    }

    private void setListRequestData(DocumentInfo entity) {
        if (entity.getCategory_list() != null && entity.getCategory_list().size() > 0) {
            if (isRefresh) {
                //下拉刷新
                documentInfos.clear();
            }
            if (entity.getCategory_list().size() < pageSize) {
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            } else {
                listView.setMode(PullToRefreshBase.Mode.BOTH);
            }
            documentInfos.addAll(entity.getCategory_list());
            myAdapter.updateListView(documentInfos);
        }
    }

    CountDownTimer downTimer = null;
    private String childlistId = "";
    private String childlistIdNew = "";
    private int wh;
    private boolean isfirst = true;
    private Handler galleryHandler = new Handler() {
        public int idPosition;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                        childlistIdNew = msg.arg1 + "";
                        idPosition = msg.arg2;
                        if (downTimer != null) {
                            downTimer.cancel();
                        }
                        if (isfirst && !AbStrUtil.isEmpty(childlistIdNew)) {
                            isfirst = false;
                            IDPosition = idPosition;
                            mAdapter.notifyDataSetChanged();
                            childlistId = childlistIdNew;
                            requestListData(pageIndex, pageSize, childlistId);
                            return;
                        }


                        downTimer = new CountDownTimer(1500, 500) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                if (!AbStrUtil.isEmpty(childlistIdNew) && !childlistId.equals(childlistIdNew)) {
                                    IDPosition = idPosition;
                                    mAdapter.notifyDataSetChanged();
                                    childlistId = childlistIdNew;
                                    requestListData(pageIndex, pageSize, childlistId);
                                }


                            }
                        };
                        downTimer.start();

                case 2:
                    idPosition = msg.arg2;
                    mAdapter.setSelectText(idPosition,14,R.color.white,R.color.content_black);
                    mAdapter.notifyDataSetChanged();
                    break;

            }
        }
    };

    @Override
    public void onStop() {
        if (null != loadingView) {
            loadingView.stopAnimition();
        }
        super.onStop();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search:
                showSearchPop();
                break;
            default:
                break;
        }
    }

    public void showSearchPop() {
        dWindow = new SearchPopWindow(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dWindow.dismiss();
            }
        }, getActivity(), hotList, channelId != null ? channelId : "0", "文献");
        dWindow.showAtLocation(getActivity().findViewById(R.id.main), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void getHotCache() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            channelId = bundle.getInt(TAG) + "";
        }
        category_id = "1";
        channelName = "文献";
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

    class MyAdapter extends EasyBaseAdapter<DocumentInfo.CategoryListBean> {

        private TextView tv_title;
        private TextView tv_support;
        private TextView tv_conment;
        private TextView tv_reader;

        public MyAdapter(Context context, int layoutId, List<DocumentInfo.CategoryListBean> list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(EasyViewHolder viewHolder, DocumentInfo.CategoryListBean info) {
            tv_title = viewHolder.getTextView(R.id.tv_title);
            tv_support = viewHolder.getTextView(R.id.tv_support);
            tv_conment = viewHolder.getTextView(R.id.tv_conment);
            tv_reader = viewHolder.getTextView(R.id.tv_reader);
            tv_title.setText(info.getTitle());
            tv_support.setText(info.getLike_count() + "赞");
            tv_conment.setText(info.getComment_count() + "评论");
            tv_reader.setText(info.getRead_count() + "");
        }
    }

    public class GalleryAdapter extends BaseAdapter {


        public GalleryAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            hold = new ViewHold();
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
            }

            hold.mImageView = (ImageView) convertView.findViewById(R.id.imageview);
            hold.mTextView = (TextView) convertView.findViewById(R.id.text);

            if (list != null && list.size() > 0) {
                if (!AbStrUtil.isEmpty(list.get(position).getImg())) {
                    ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
                            + list.get(position).getImg(), hold.mImageView, AbViewUtil.getOptions(R.drawable.ic_document));
                } else {
                    hold.mImageView.setImageResource(R.drawable.ic_document);
                }

                if (IDPosition == position) {
                    mAdapter.setSelectText(position,14,R.drawable.btn_shape_fc7f1a,R.color.white);
                } else {
                    setSelectText(position,14,R.color.white,R.color.content_black);
                }


            }


            return convertView;
        }

        private void setSelectText(int position,int textSize,int txtBG,int txtColor) {
            hold.mTextView.setFocusable(false);
            hold.mTextView.setBackgroundResource(txtBG);
            hold.mTextView.setTextColor(getActivity().getResources().getColor(txtColor));
            hold.mTextView.setTextSize(12);
            hold.mTextView.setText(list.get(position).getTitle());
        }
    }
}