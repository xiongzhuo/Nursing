package com.deya.hospital.workcircle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.hospital.dypdf.PdfPreviewActivity;
import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.EasyBaseAdapter;
import com.deya.hospital.base.EasyViewHolder;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.LoadingView;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.OranizeEntity;
import com.deya.hospital.workspace.TaskUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 机构-详情
 * @author : yugq
 * @date 2016/7/6
 */
public class OrganizeActivity extends BaseActivity implements View.OnClickListener{
    private static final int GET_DATA_SUCESS = 0x6050;
    private static final int GET_DATA_FAIL = 0x06051;

    private RelativeLayout rl_back;
    private ImageView img;
    private TextView tv_title;
    private TextView tv_digest;
    private TextView tv_com;
    private ListView listview;
    private LoadingView loadingView;

    private MyAdapter myAdapter = null;
    private List<OranizeEntity.ArticlelistBean> articlelistBeanList = new ArrayList<>();

    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                loadingView.stopAnimition();
                switch (msg.what) {
                    case GET_DATA_SUCESS:
                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                            try {
                                OranizeEntity entity = TaskUtils.gson.fromJson(msg.obj.toString(), OranizeEntity.class);
                                if (entity != null) {
                                    if (entity.getResult_id().equals("0")) {
                                        loadingView.setVisibility(View.GONE);
                                        setRequestData(entity);
                                    } else {
                                        ToastUtils.showToast(OrganizeActivity.this,entity.getResult_msg());
                                    }
                                } else {
                                    ToastUtils.showToast(OrganizeActivity.this,"亲，您的网络不顺畅哦！");
                                    loadingView.setVisibility(View.VISIBLE);
                                    loadingView.setEmptyView("亲，您的网络不顺畅哦，\n 请检查您的网络，再继续访问！");
                                }
                            } catch (Exception e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case GET_DATA_FAIL:
                        loadingView.setVisibility(View.VISIBLE);
                        ToastUtils.showToast(OrganizeActivity.this,"亲，您的网络不顺畅哦！");
                        loadingView.setEmptyView("亲，您的网络不顺畅哦，\n请检查您的网络，再继续访问！");
                        break;

                    default:
                        break;
                }
            }
        }
    };
    private String organization_id = "";
    private DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organize);
        organization_id = getIntent().getStringExtra("organization_id");
        options=AbViewUtil.getOptions(R.drawable.defult_img);
        bindViews();
        initEnvent();
        requestData();
    }

    private void bindViews() {
        View headView = LayoutInflater.from(mcontext).inflate(
                R.layout.headview_organize, null);
        img = (ImageView) headView.findViewById(R.id.img);
        tv_title = (TextView) headView.findViewById(R.id.tv_title);
        tv_digest = (TextView) headView.findViewById(R.id.tv_digest);
        tv_com = (TextView) headView.findViewById(R.id.tv_com);

        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        listview = (ListView) findViewById(R.id.listview);
        loadingView = (com.deya.hospital.util.LoadingView) findViewById(R.id.loadingView);
        listview.addHeaderView(headView);
    }

    private void initEnvent() {
        rl_back.setOnClickListener(this);
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
        myAdapter = new MyAdapter(this,R.layout.adapter_item_organize,articlelistBeanList);
        listview.setAdapter(myAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int mPosition = position - 1;
                if (articlelistBeanList.size()>0 && position >0) {
                    articlelistBeanList.get(mPosition).getId();
                    String url = "";
                    Intent it = new Intent();
                    if (articlelistBeanList.get(mPosition).getIs_pdf() == 1) {
                        if (!AbStrUtil.isEmpty(articlelistBeanList.get(mPosition).getPdf_attach())) {
                            url = WebUrl.WEB_PDF+"?id="+articlelistBeanList.get(mPosition).getId()+"&&pdfid="+articlelistBeanList.get(mPosition).getPdf_attach();
                        }
                        it.putExtra("articleid",articlelistBeanList.get(mPosition).getId() + "");
                        it.setClass(OrganizeActivity.this, PdfPreviewActivity.class);
                    } else {
                        url = WebUrl.WEB_ARTICAL_DETAIL + "?id="+articlelistBeanList.get(mPosition).getId();
                        it.setClass(OrganizeActivity.this, WebViewDtail.class);
                    }
                    it.putExtra("url", url);
                    startActivity(it);
                }
            }
        });
    }

    private void requestData() {
            JSONObject job = new JSONObject();
            try {
                job.put("authent", tools.getValue(Constants.AUTHENT));
                job.put("organization_id", organization_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MainBizImpl.getInstance().onCirclModeRequest(myHandler,
                    this, GET_DATA_SUCESS, GET_DATA_FAIL, job,
                    "workCircle/organizationDetail");

    }

    private void setRequestData(OranizeEntity entity) {

        if (entity.getInfo()!= null){
            if (!AbStrUtil.isEmpty(entity.getInfo().getName())) {
                tv_title.setText(entity.getInfo().getName());
            }
            if (!AbStrUtil.isEmpty(entity.getInfo().getCompany())) {
                tv_com.setText(entity.getInfo().getCompany());
            }
            String digest = "简介：";
            if (!AbStrUtil.isEmpty(entity.getInfo().getIntroduce())) {
                digest = "简介：" + entity.getInfo().getIntroduce();
            }
            tv_digest.setText(digest);
            ImageLoader.getInstance().displayImage(WebUrl.FILE_PDF_LOAD_URL
                    + entity.getInfo().getAvatar(), img, AbViewUtil.getOptions(R.drawable.ic_launcher));
        }
        if (entity.getArticlelist().size() > 0) {
            articlelistBeanList = entity.getArticlelist();
            myAdapter.updateListView(entity.getArticlelist());

        }

    }

    @Override
    protected void onStop() {
        if ( null != loadingView) {
            loadingView.stopAnimition();
        }
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            default:
                break;
        }
    }

    class MyAdapter extends EasyBaseAdapter<OranizeEntity.ArticlelistBean> {

        private LinearLayout main;
        private TextView titleTv;
        private LinearLayout imgLay;
        private ImageView imgView1;
        private ImageView imgView2;
        private ImageView imgView3;
        private TextView tv_tag;
        private TextView tv_date;
        private TextView tv_like;
        private ImageView imgView;

        public MyAdapter(Context context, int layoutId, List<OranizeEntity.ArticlelistBean> list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(EasyViewHolder viewHolder, OranizeEntity.ArticlelistBean articlelistBean) {
            main = viewHolder.getLinearLayout(R.id.main);
            titleTv = viewHolder.getTextView(R.id.titleTv);
            imgLay = viewHolder.getLinearLayout(R.id.imgLay);
            imgView1 = viewHolder.getImageView(R.id.imgView1);
            imgView2 = viewHolder.getImageView(R.id.imgView2);
            imgView3 = viewHolder.getImageView(R.id.imgView3);
            tv_tag = viewHolder.getTextView(R.id.tv_tag);
            tv_date = viewHolder.getTextView(R.id.tv_date);
            tv_like = viewHolder.getTextView(R.id.tv_like);
            imgView = viewHolder.getImageView(R.id.imgView);


            tv_tag.setText(articlelistBean.getChannel_name());
            titleTv.setText(articlelistBean.getTitle());
            tv_date.setText(articlelistBean.getCreate_time());
            tv_like.setText(articlelistBean.getLike_count()+"赞    " + articlelistBean.getComment_count()+"评论");
//            list_type	列表类型  0：纯文本 1：单图  3：三张图
            switch (articlelistBean.getList_type()) {
                case 0:
                    imgLay.setVisibility(View.GONE);
                    imgView.setVisibility(View.GONE);
                    break;
                case 1:
                    if(articlelistBean.getAttachment().size()<=0){
                        return;
                    }
                    imgLay.setVisibility(View.GONE);
                    imgView.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(WebUrl.FILE_PDF_LOAD_URL
                            + articlelistBean.getAttachment().get(0).getFile_name(), imgView, options);
                    break;
                case 3:
                    if(articlelistBean.getAttachment().size()<=3){
                        return;
                    }
                    imgLay.setVisibility(View.VISIBLE);
                    imgView.setVisibility(View.GONE);

                    ImageLoader.getInstance().displayImage(WebUrl.FILE_PDF_LOAD_URL
                            + articlelistBean.getAttachment().get(0).getFile_name(), imgView1, options);

                    ImageLoader.getInstance().displayImage(WebUrl.FILE_PDF_LOAD_URL
                            + articlelistBean.getAttachment().get(1).getFile_name(), imgView2, options);

                    ImageLoader.getInstance().displayImage(WebUrl.FILE_PDF_LOAD_URL
                            + articlelistBean.getAttachment().get(2).getFile_name(), imgView3, options);
                    break;
                default:
                    break;

            }

        }
    }
}
