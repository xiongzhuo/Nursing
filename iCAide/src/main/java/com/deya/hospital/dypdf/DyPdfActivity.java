package com.deya.hospital.dypdf;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.CommentVo;
import com.deya.hospital.widget.popu.PopCircleCommement;
import com.deya.hospital.workcircle.WebViewPDComentl2;
import com.deya.hospital.workcircle.knowledge.KnowLegePrivewActivity;
import com.google.gson.Gson;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/5/2
 */
public class DyPdfActivity extends PDFViewActivity implements View.OnClickListener {
    public static final int GET_INFO_SUCESS = 0x600045;
    public static final int COMMENT_SUCESS = 0x600039;
    public static final int CLICK_LIKE_SUCESS = 0x600043;
    public static final int CLICK_COLLECT_SUCESS = 0x6000437;
    private static final int KNOWLEDGEINFO_SUCESS = 0x6000439;
    PopCircleCommement dialog;
    protected TextView write_compment;
    protected LinearLayout inputLay;
    protected LinearLayout editorLay;
    protected TextView submiText;
    String articalId = "";

    protected String shareTitle = "";
    protected String shareContent = "";
    TextView zan_num, commentNumTv;
    ImageView zanImg, shoucangImg;
    protected EditText commentEdt;
    LinearLayout messageLay;
    LinearLayout ll_bottom;
    Button knowledgeBtn;
    String title = "";
    String url = "";
    protected String from = "";

    protected void getBaseData() {
        title = getIntent().getStringExtra("title");

        url = getIntent().getStringExtra("urlweb");
        from = getIntent().getStringExtra("from");
        if (!AbStrUtil.isEmpty(url)) {
            articalId = url.substring(url.indexOf("id=") + 3, url.indexOf("&"));
        }
        if (!AbStrUtil.isEmpty(getIntent().getStringExtra("articleid"))) {
            articalId = getIntent().getStringExtra("articleid");
        }
        if (!AbStrUtil.isEmpty(getIntent().getStringExtra("pdfid"))) {
            url = WebUrl.WEB_ARTICAL + articalId;
        } else if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
        }
    }

    @Override
    public void initOwnViews() {
        getBaseData();
        ll_bottom = findView(R.id.ll_bottom);
        ll_bottom.setVisibility(View.VISIBLE);
        findViewById(R.id.shareLay).setOnClickListener(this);
        if (!AbStrUtil.isEmpty(from) && from.equals("tabfragment")) {
            initBottomView();
            getArticleInfo();
        }
        knowledgeBtn = findView(R.id.knowledge_btn);
        knowledgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, KnowLegePrivewActivity.class);
                intent.putExtra("article_id", getIntent().getStringExtra("article_id"));
                intent.putExtra("article_src", getIntent().getStringExtra("article_src"));
                startActivity(intent);
            }
        });

        getKnowledgeInfo(getIntent().getStringExtra("article_id"), getIntent().getStringExtra("article_src"));
    }

    protected void initBottomView() {

        findViewById(R.id.circlelay).setVisibility(View.VISIBLE);
        editorLay = findView(R.id.editorLay);
        write_compment = findView(R.id.write_compment);
        write_compment.setOnClickListener(this);
        inputLay = findView(R.id.inputLay);
        inputLay.setOnClickListener(this);
        findView(R.id.framBg).setOnClickListener(this);

        submiText = findView(R.id.submiText);
        submiText.setOnClickListener(this);
        commentEdt = findView(R.id.commentEdt);
        zan_num = findView(R.id.zan_num);
        zan_num.setOnClickListener(this);
        zanImg = findView(R.id.zanImg);
        zanImg.setOnClickListener(this);
        shoucangImg = findView(R.id.shoucangImg);
        shoucangImg.setOnClickListener(this);
        commentNumTv = findView(R.id.commentNumTv);
        messageLay = findView(R.id.messageLay);
        messageLay.setOnClickListener(this);


    }

    /**
     * 获取文章信息
     */
    void getArticleInfo() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("article_id", articalId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomCirclModReq(this, this,
                GET_INFO_SUCESS, job,
                "workCircle/getArticleInfo");
    }

    public void showCommentEditorPop() {
        dialog = new PopCircleCommement(mcontext, this, articalId, write_compment,
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
            job.put("article_id", articalId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomCirclModReq(this, this,
                CLICK_COLLECT_SUCESS, job,
                "workCircle/setCollect");
    }


    /**
     * 评论接口
     */
    public void doComment(String text) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("article_id", articalId);
            job.put("content", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomCirclModReq(this, this,
                COMMENT_SUCESS, job, "workCircle/submitComment");
    }

    // 点赞接口
    public void onClickLike() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("article_id", articalId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomCirclModReq(this, this, CLICK_LIKE_SUCESS,
                job, "workCircle/clickLike");

    }

    protected void setCommentResult(JSONObject jsonObject) {
        ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
        if (jsonObject.has("info")) {
            CommentVo cv = gson.fromJson(jsonObject.optString("info"), CommentVo.class);
            if (null != cv) {
                String numstr = commentNumTv.getText().toString();
                if (AbStrUtil.isEmpty(numstr)) {
                    numstr = "0";
                }
                showTipsDialog(jsonObject.optString("integral"));
                int num = Integer.parseInt(numstr) + 1;
                commentNumTv.setText(num + "");
                Intent it = new Intent(mcontext, WebViewPDComentl2.class);
                it.putExtra("url", url);
                it.putExtra("id", articalId);
                startActivity(it);
            }
        }
        commentEdt.setText("");

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
            if (null == jsonObject) {
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

    /**
     * origin 来自哪个模块
     */

    private void showShare() {

        if(getIntent().hasExtra("origin")&&!AbStrUtil.isEmpty(shareTitle)){
            showShareDialog(getIntent().getStringExtra("origin"),
                    shareTitle,
                    url);
        }
        if (AbStrUtil.isEmpty(shareTitle)) {
            shareTitle = "工作圈";
            shareContent = "点击查看文章详情";
        } else {
            shareContent = shareTitle;
        }
        showShareDialog(shareTitle,
                shareContent,
                url);
    }


    public void getKnowledgeInfo(String id, String article_src) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("id", id);
            job.put("article_src", article_src);
            MainBizImpl.getInstance().onComomCirclModReq(this, this, KNOWLEDGEINFO_SUCESS, job, "subject/test/testDetail");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_compment:
                showCommentEditorPop();
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
                Intent it = new Intent(mcontext, WebViewPDComentl2.class);
                it.putExtra("url", url);
                it.putExtra("id", articalId);
                startActivity(it);
                break;
            case R.id.shareLay:
                showShare();
                break;

            default:
                break;
        }

    }
    private void setKnowLegeInfo(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            if (jsonObject.has("test")) {
                JSONObject job = jsonObject.optJSONObject("test");
                if (null == job) {
                    return;
                }
                if (job.optInt("conTest") > 0) {
                    knowledgeBtn.setVisibility(View.VISIBLE);

                }
            }
        } else {
            ToastUtil.showMessage(jsonObject.optString("result_msg"));
        }
    }
    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        switch (code) {

            case COMMENT_SUCESS:
                setCommentResult(jsonObject);
                SharedPreferencesUtil.clearCacheById(mcontext, articalId, "commetChache");
                break;
            case CLICK_LIKE_SUCESS:
                setClickResult(jsonObject);
                break;
            case GET_INFO_SUCESS:
                setInfoResult(jsonObject);
                break;
            case CLICK_COLLECT_SUCESS:
                setcollectResult(jsonObject);
                break;
            case KNOWLEDGEINFO_SUCESS:
                dismissdialog();
                setKnowLegeInfo(jsonObject);
                break;
            default:
                break;
        }
    }
}
