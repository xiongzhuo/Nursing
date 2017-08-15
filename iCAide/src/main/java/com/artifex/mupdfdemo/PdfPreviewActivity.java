package com.artifex.mupdfdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.dypdf.DyPdfActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.FileDownloadThread;
import com.deya.hospital.util.LoadingView;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.AritcleEntity;
import com.deya.hospital.vo.CommentVo;
import com.deya.hospital.widget.popu.PopCircleCommement;
import com.deya.hospital.workcircle.WebViewPDComentl2;
import com.deya.hospital.workcircle.knowledge.KnowLegePrivewActivity;
import com.deya.hospital.workcircle.knowledge.KnowledgeInfoSearchActivity;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.Gson;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Pdf预览页
 * @author : yugq
 * @date 2016/7/6
 */
public class PdfPreviewActivity extends KnowledgeInfoSearchActivity implements View.OnClickListener{
    private static final int GET_DATA_SUCESS = 0x6050;
    private static final int GET_DATA_FAIL = 0x06051;
    private RelativeLayout rl_back;
    private View line;
    private TextView img_loading;
    private TextView tv_title;
    private TextView tv_author;
    private TextView tv_digest;
    private TextView tv_pdfprogress;
    private TextView tv_pdfsize;
    private TextView tv_like;
    private TextView tv_comment;
    private TextView tv_reader;
    private String articleid="";
    private String urlweb = "";
    private LoadingView loadingView;
    Button knowledge_btn;
    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                if (loadingView != null) {
                    loadingView.stopAnimition();
                    loadingView.setVisibility(View.GONE);
                }
                switch (msg.what) {
                    case GET_DATA_SUCESS:
                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                            try {
                                AritcleEntity entity = TaskUtils.gson.fromJson(msg.obj.toString(), AritcleEntity.class);
                                if (entity != null) {
                                    if (entity.getResult_id().equals("0")) {
                                        setCache(msg);
                                        setRequestData(entity);
                                    } else {
                                        ToastUtils.showToast(PdfPreviewActivity.this,entity.getResult_msg());
                                    }
                                } else {
                                    loadingView.setVisibility(View.VISIBLE);
                                    ToastUtils.showToast(PdfPreviewActivity.this,"亲，您的网络不顺畅哦！");
                                    loadingView.setEmptyView("亲，您的网络不顺畅哦，\n请检查您的网络，再继续访问！");
                                }
                            } catch (Exception e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case GET_DATA_FAIL:
                        loadingView.setVisibility(View.VISIBLE);
                        ToastUtils.showToast(PdfPreviewActivity.this,"亲，您的网络不顺畅哦！");
                        loadingView.setEmptyView("亲，您的网络不顺畅哦，\n请检查您的网络，再继续访问！");
                        break;
                    case ADD_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setAddRes(new JSONObject(msg.obj.toString()),activity);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case ADD_FAILE:
                        // ToastUtils.showToast(getActivity(), "");
                        break;
                    case COMMENT_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setCommentResult(new JSONObject(msg.obj.toString()));
                                SharedPreferencesUtil.clearCacheById(mcontext, articleid,"commetChache");
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case COMMENT_FAIL:
                        ToastUtils.showToast(PdfPreviewActivity.this, "亲，您的网络不顺畅哦！");
                        break;
                    case CLICK_LIKE_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setClickResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case CLICK_LIKE_FAIL:
                        break;
                    case GET_INFO_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setInfoResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case GET_INFO_FAIL:
                        break;
                    case CLICK_COLLECT_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setcollectResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case CLICK_COLLECT_FAIL:
                        break;
                    case KNOWLEDGEINFO_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                dismissdialog();
                                setKnowLegeInfo(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case KNOWLEDGEINFO_FAIL:
                        dismissdialog();
                        ToastUtil.showMessage("亲，您的网咯不顺畅哦！");
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void setKnowLegeInfo(JSONObject jsonObject) {
        if(jsonObject.optString("result_id").equals("0")){
            if(jsonObject.has("test")){
                JSONObject job=jsonObject.optJSONObject("test");
                if(null==job){
                    return;
                }
                if(job.optInt("conTest")>0){
                    findViewById(R.id.knowledge_btn).setVisibility(View.VISIBLE);
                }
            }
        }else{
            ToastUtil.showMessage(jsonObject.optString("result_msg"));
        }
    }

    private void setCache(Message msg) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(articleid,msg.obj.toString());
        SharedPreferencesUtil.saveString(mcontext,mcontext.getClass().getName(),jsonObject.toString());
    }

    private String pdf_attach;
    private String pdfFileName;
    private String tv_progressStr = "";
    private String collection = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfpreview);
        getIntentData();
        bindViews();
        initBottomView();
        initEnvent();
        showprocessdialog();
        getKnowledgeInfo(myHandler,articleid,"0");
        requestData();
//        getArticleInfo();
        if (!AbStrUtil.isEmpty(pdfFileName)) {
            // 判断文件是否存在
            OpenPdf(pdfFileName, "详情");
        }

    }

    private void getIntentData() {
        collection = getIntent().getStringExtra("mycirclecollection");
        articleid = getIntent().getStringExtra("articleid");
        urlweb = getIntent().getStringExtra("url");
        pdf_attach = getIntent().getStringExtra("pdf_attach");
        if (!AbStrUtil.isEmpty(urlweb)) {
            pdfFileName = urlweb.substring(urlweb.indexOf("pdfid=")+6);
        }

        if (!AbStrUtil.isEmpty(pdf_attach)) {
            pdfFileName = pdf_attach;
        }


    }


    private void bindViews() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        line = (View) findViewById(R.id.line);
//        img_loading = (TextView) findViewById(R.id.img_loading);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_author = (TextView) findViewById(R.id.tv_author);
        tv_like = (TextView) findViewById(R.id.tv_like);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        tv_pdfprogress = (TextView) findViewById(R.id.tv_pdfprogress);
        tv_pdfsize = (TextView) findViewById(R.id.tv_pdfsize);
        tv_digest = (TextView) findViewById(R.id.tv_digest);
        tv_reader = (TextView) findViewById(R.id.tv_reader);
        knowledge_btn= (Button) findViewById(R.id.knowledge_btn);
        loadingView = (LoadingView) findViewById(R.id.loadingView);
        knowledge_btn.setOnClickListener(this);
    }

    private void initEnvent() {
        rl_back.setOnClickListener(this);
        tv_pdfprogress.setOnClickListener(this);
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

    private void requestData() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("article_id", articleid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler,
                this, GET_DATA_SUCESS, GET_DATA_FAIL, job,
                "workCircle/articleDetail");

    }

    private void setRequestData(AritcleEntity entity) {
        if (!AbStrUtil.isEmpty(entity.getInfo().getAbstracts())) {
            tv_digest.setText(Html.fromHtml("<font color=\"#FF9C4B\">摘要：</font>"+entity.getInfo().getAbstracts()));
        }
        if (!AbStrUtil.isEmpty(entity.getInfo().getDraftman())) {
            tv_author.setText(entity.getInfo().getDraftman());
        } else {
            tv_author.setText("匿名");
        }

        if (!AbStrUtil.isEmpty(entity.getInfo().getTitle())) {
            tv_title.setText(entity.getInfo().getTitle());
        }


        tv_reader.setText((Object)(entity.getInfo().getRead_count()) != null?entity.getInfo().getRead_count()+"":0+"");

        tv_like.setText(entity.getInfo().getLike_count() + "赞");

        tv_comment.setText(entity.getInfo().getComment_count()+"评论" );

        if (!AbStrUtil.isEmpty(entity.getInfo().getPdf_size())) {
            tv_pdfsize.setText(entity.getInfo().getPdf_size() + "M");
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
            case R.id.tv_pdfprogress:
                if (tv_pdfprogress.getText().toString().equals("查看")) {
                    startActivityInto();
                } else if(tv_pdfprogress.getText().toString().equals("文件不存在")) {
                    break;
                }else {
                    OpenPdf(pdfFileName,"详情");
                }
                break;

            case R.id.write_compment:
                showCommentEditorPop();
                break;
            case R.id.inputLay:
                write_compment.setVisibility(View.VISIBLE);
                break;
            case R.id.framBg:

                editorLay.setVisibility(View.VISIBLE);
                inputLay.setVisibility(View.GONE);
                AbViewUtil.colseVirtualKeyboard(this);
                break;
            case R.id.zan_num:
                onClickLike();
                break;
            case R.id.zanImg:
                onClickLike();
                break;
            case R.id.shoucangImg:
                onCollection();
                break;
            case R.id.messageLay:
                Intent it=new Intent(mcontext, WebViewPDComentl2.class);
                it.putExtra("url", urlweb);
                it.putExtra("id", articleid);
                startActivity(it);
                break;
            case R.id.shareLay:
                showShare();
                break;
            case R.id.knowledge_btn:
                Intent intent = new Intent(mcontext, KnowLegePrivewActivity.class);
                intent.putExtra("article_id", articleid+"");
                intent.putExtra("article_src", "0");
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    public boolean isFirst = true;
    String subfile="";
    String title="";
    File pdffile ;
    DownloadTask downloadTask= null;
    public void OpenPdf(String fileName,String title) {
        subfile=fileName;
        this.title=title;
        pdffile = new File(getPath() + "/" + fileName + ".pdf");
        download(fileName);
    }


    public boolean checkIsExists(int lenth){

        return pdffile.length()==lenth;
    }

    // 下载部分 ---------
    /**
     * 下载准备工作，获取SD卡路径、开启线程
     */
    private void download(String fileName) {
//        showprocessdialog();
//        isLoading=true;
        // 获取SD卡目录
        String dowloadDir = getPath();
        File file = new File(dowloadDir);
        // 创建下载目录
        if (!file.exists()) {
            file.mkdirs();
        }

        // 读取下载线程数，如果为空，则单线程下载
        int downloadTN = 1;
        // 如果下载文件名为空则获取Url尾为文件名
        String downloadUrl = WebUrl.FILE_LOAD_URL + fileName;
        String pdfName = fileName+ ".pdf";
        // 开始下载前把下载按钮设置为不可用
        // 进度条设为0
        // downloadProgressBar.setProgress(0);
        // 启动文件下载线程
        downloadTask = new DownloadTask(downloadUrl, Integer.valueOf(downloadTN), dowloadDir
                + pdfName);
        downloadTask.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (!isFirst) {
                // 当收到更新视图消息时，计算已完成下载百分比，同时更新进度条信息
                int progress = (Double
                        .valueOf((downloadedSize * 1.0 / fileSize * 100)))
                        .intValue();
                if (progress == 100) {
                    tv_pdfprogress.setText("查看");
                    startActivityInto();
                } else {
                    tv_pdfprogress.setText("下载：" + progress + "%");
                }
            } else {
                isFirst = false;
                tv_pdfprogress.setText(tv_progressStr);
//                float a =  Float.valueOf(fileSize + "");
//                float b = (float)(Math.round(a /1024/1024*100))/100;
//                tv_pdfsize.setText(b+"M" );
            }

        }

    };

    public static final String DOWNLOAD_FOLDER_NAME = "Deya/pdf";
    private int fileSize = 0;
    private int downloadedSize = 0;
    public class DownloadTask extends Thread {
        private int blockSize, downloadSizeMore;
        private int threadNum = 5;
        String urlStr, threadNo, fileName;
        boolean finished = false;
        public DownloadTask (String urlStr, int threadNum, String fileName) {
            this.urlStr = urlStr;
            this.threadNum = threadNum;
            this.fileName = fileName;
        }

        public void stopThread(boolean  stop) {
            this .finished = !stop;
        }

        @Override
        public void run() {
            FileDownloadThread[] fds = new FileDownloadThread[threadNum];
            try {
                URL url = new URL(urlStr);
                URLConnection conn = url.openConnection();
                conn.getHeaderField("Content-Length");
                // 获取下载文件的总大小
                // fileSize = conn.getContentLength();
                fileSize = conn.getContentLength();

                if (isFirst) {
                    if (fileSize == -1) {
                        tv_progressStr = "文件不存在";
                        handler.sendEmptyMessage(0);
                        return;
                    }

                    if (checkIsExists(fileSize)) {
                        tv_progressStr = "查看";
                    } else {
                        tv_progressStr = "下载查看";
                    }
                    handler.sendEmptyMessage(0);
                    return;
                }

                if(checkIsExists(fileSize)){
                    tv_progressStr = "查看";
                    handler.sendEmptyMessage(0);
                }else{
                    // 计算每个线程要下载的数据量
                    blockSize = fileSize / threadNum;
                    // 解决整除后百分比计算误差
                    downloadSizeMore = (fileSize % threadNum);
                    File file = new File(fileName);
                    for (int i = 0; i < threadNum; i++) {
                        // 启动线程，分别下载自己需要下载的部分
                        FileDownloadThread fdt = new FileDownloadThread(url, file,
                                i * blockSize, (i + 1) * blockSize - 1);
                        fdt.setName("Thread" + i);
                        fdt.start();
                        fds[i] = fdt;
                    }

                    while (!finished) {
                        // 先把整除的余数搞定
                        downloadedSize = downloadSizeMore;
                        finished = true;
                        for (int i = 0; i < fds.length; i++) {
                            downloadedSize += fds[i].getDownloadSize();
                            if (!fds[i].isFinished()) {
                                finished = false;
                            }
                        }
                        // 通知handler去更新视图组件
                        handler.sendEmptyMessage(0);
                        // 休息1秒后再读取下载进度
                        sleep(1000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void startActivityInto() {
        Uri uri = Uri.parse(pdffile.toString());
        Intent intent = new Intent();
        if(AbStrUtil.isAndroid5()){
            intent.setClass(PdfPreviewActivity.this, DyPdfActivity.class);
        }else{
            intent.setClass(PdfPreviewActivity.this, MuPDFActivity.class);
        }

        intent.putExtra("title", "详情");
        intent.putExtra("urlweb", urlweb);
        intent.putExtra("from", "tabfragment");
        intent.putExtra("articleid",articleid);
        intent.putExtra("article_id", articleid);
        intent.putExtra("article_src", "0");
        intent.putExtra("pdfid",pdfFileName);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }


    private String getPath() {
        // TODO Auto-generated method stub
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            return new StringBuilder(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()).append(File.separator)
                    .append(DOWNLOAD_FOLDER_NAME).append(File.separator)
                    .toString();
        }
        return "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadTask != null) {
            downloadTask.stopThread(true);
            downloadTask.interrupt();
            downloadTask = null;
        }
    }


    private TextView write_compment;
    private LinearLayout inputLay;
    private LinearLayout editorLay;
    private TextView submiText;

    private String shareTitle = "";
    private String shareContent="";
    TextView zan_num, commentNumTv;
    ImageView zanImg, shoucangImg;

    List<CommentVo> commetList = new ArrayList<CommentVo>();
    private EditText commentEdt;
    LinearLayout messageLay;
    LinearLayout ll_bottom;

    private void  initBottomView(){
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        ll_bottom.setVisibility(View.VISIBLE);
        editorLay = (LinearLayout) findViewById(R.id.editorLay);
        write_compment = (TextView) findViewById(R.id.write_compment);
        write_compment.setOnClickListener(this);
        inputLay = (LinearLayout) findViewById(R.id.inputLay);
        inputLay.setOnClickListener(this);
        findViewById(R.id.framBg).setOnClickListener(this);

        submiText = (TextView) findViewById(R.id.submiText);
        submiText.setOnClickListener(this);
        commentEdt = (EditText) findViewById(R.id.commentEdt);
        zan_num = (TextView) findViewById(R.id.zan_num);
        zan_num.setOnClickListener(this);
        zanImg = (ImageView) findViewById(R.id.zanImg);
        zanImg.setOnClickListener(this);
        shoucangImg = (ImageView) findViewById(R.id.shoucangImg);
        shoucangImg.setOnClickListener(this);
        commentNumTv = (TextView) findViewById(R.id.commentNumTv);
        messageLay = (LinearLayout) findViewById(R.id.messageLay);
        messageLay.setOnClickListener(this);
        findViewById(R.id.shareLay).setOnClickListener(this);

    }
    public static final int GET_INFO_SUCESS = 0x600045;
    public static final int GET_INFO_FAIL = 0x600046;
    public static final int COMMENT_SUCESS = 0x600039;
    public static final int COMMENT_FAIL = 0x600040;
    public static final int GET_COMMENTLIST_SUCESS = 0x600041;
    public static final int GET_COMMENTLIST_FAIL = 0x600042;
    public static final int CLICK_LIKE_SUCESS = 0x600043;
    public static final int CLICK_LIKE_FAIL = 0x600044;
    public static final int CLICK_COLLECT_SUCESS = 0x6000437;
    public static final int CLICK_COLLECT_FAIL = 0x600048;
    PopCircleCommement Commdialog;
    public void showCommentEditorPop() {
        Commdialog = new PopCircleCommement(mcontext, this,articleid, write_compment,
                new PopCircleCommement.OnPopuClick() {

                    @Override
                    public void enter(String text) {
                        doComment(text);

                    }

                    @Override
                    public void cancel() {
                        // TODO Auto-generated method stub

                    }
                });

    }

    /**
     * 点击收藏
     */
    private void onCollection() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("article_id", articleid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, this,
                CLICK_COLLECT_SUCESS, CLICK_COLLECT_FAIL, job,
                "workCircle/setCollect");
    }

    /**
     * 获取文章信息
     */
    public void getArticleInfo() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("article_id", articleid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, this,
                GET_INFO_SUCESS, GET_INFO_FAIL, job,
                "workCircle/getArticleInfo");
    }

    /**
     * 评论接口
     */
    public void doComment(String text) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("article_id", articleid);
            job.put("content", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomRequest(myHandler, this,
                COMMENT_SUCESS, COMMENT_FAIL, job, "workCircle/submitComment");
    }

    // 点赞接口
    public void onClickLike() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("article_id", articleid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance()
                .onComomRequest(myHandler, this, CLICK_LIKE_SUCESS,
                        CLICK_LIKE_FAIL, job, "workCircle/clickLike");

    }

    protected void setCommentResult(JSONObject jsonObject) {
        ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
        if(jsonObject.has("info")){
            CommentVo cv=gson.fromJson(jsonObject.optString("info"), CommentVo.class);
            if(null!=cv){
                String numstr=commentNumTv.getText().toString();
                if(AbStrUtil.isEmpty(numstr)){
                    numstr="0";
                }
                showTipsDialog(jsonObject.optString("integral"));
                int num=Integer.parseInt(numstr)+1;
                commentNumTv.setText(num+"");
                Intent it=new Intent(mcontext, WebViewPDComentl2.class);
                it.putExtra("url", urlweb);
                it.putExtra("id", articleid);
                startActivity(it);
            }
        }
        commentEdt.setText("");
        getArticleInfo();

    }

    @Override
    protected void onResume() {
        getArticleInfo();
        super.onResume();
    }

    protected void setcollectResult(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            int isCollect = jsonObject.optInt("is_collection");
            shoucangImg
                    .setImageResource(isCollect == 1 ? R.drawable.shoucang_select
                            : R.drawable.shouchang_normal);
        }
        ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
    }

    protected void setInfoResult(JSONObject result) {
        if (result.optString("result_id").equals("0")) {
            JSONObject jsonObject = result.optJSONObject("info");
            if(null==jsonObject){
                return;
            }
            int like;
            if (AbStrUtil.isEmpty(jsonObject.optString("like_count"))) {
                like = 0;
            } else {
                like = jsonObject.optInt("like_count");
            }

            if (AbStrUtil.isEmpty(jsonObject.optString("title"))) {
                shareTitle = "";
            } else {
                shareTitle = jsonObject.optString("title");
            }
            zan_num.setText(like + "");
            int isCollect = jsonObject.optInt("is_collection");
            int isLike = jsonObject.optInt("is_like");
            zanImg.setImageResource(isLike == 1 ? R.drawable.zan_select
                    : R.drawable.zan_normal);
            shoucangImg
                    .setImageResource(isCollect == 1 ? R.drawable.shoucang_select
                            : R.drawable.shouchang_normal);
            int comment_num = jsonObject.optInt("comment_count");
            commentNumTv.setText("" + comment_num);
        }

    }

    Gson gson = new Gson();


    private void setClickResult(JSONObject jsonObject) {
        ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
        if (jsonObject.has("is_like")) {
            String zanStr = zan_num.getText().toString();
            if (AbStrUtil.isEmpty(zanStr)) {
                zanStr = "0";
            }
            int num = Integer.parseInt(zanStr);
            if (!AbStrUtil.isEmpty(jsonObject.optString("is_like"))) {
                if (jsonObject.optInt("is_like") == 1) {
                    num = num + 1;
                    zanImg.setImageResource(R.drawable.zan_select);
                } else {
                    num = num - 1;
                    zanImg.setImageResource(R.drawable.zan_normal);
                }
                zan_num.setText(num + "");
            }
        }
    }
    private void showShare() {
        SHARE_MEDIA[] shareMedia = { SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.WEIXIN_CIRCLE };
        if (AbStrUtil.isEmpty(shareTitle)) {
            shareTitle = "工作圈";
            shareContent = "点击查看文章详情";
        } else {
            shareContent = shareTitle;
        }
        showShareDialog( shareTitle,
                shareContent,
                WebUrl.WEB_ARTICAL_DETAIL+"?id="+articleid);
    }

    protected static final int ADD_FAILE = 0x60089;
    protected static final int ADD_SUCESS = 0x60090;
    public void getAddScore(String id){
        tools=new Tools(mcontext, Constants.AC);
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("aid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler,PdfPreviewActivity.this, ADD_SUCESS,
                ADD_FAILE, job,"goods/actionGetIntegral");
    }

    protected void setAddRes(JSONObject jsonObject,Activity activity) {
        if(jsonObject.optString("result_id").equals("0")){
            int score=jsonObject.optInt("integral");
            String str=tools.getValue(Constants.INTEGRAL);
            if(null!=str){
                tools.putValue(Constants.INTEGRAL, Integer.parseInt(str)+score+"");
            }else{
                tools.putValue(Constants.INTEGRAL,score+"");
            }
            if(score>0){
                if(null!=activity){
                    showTipsDialog(score+"");
                }
            }

        }
    }
}
