package com.deya.hospital.workcircle.knowledge;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.deya.acaide.R;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.Tools;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

public class WebDocmentDetail extends KnowledgeInfoSearchActivity {

    private WebView webView;

    Activity activity;
    //	private ProgressDialog progDailog;
    private FrameLayout fl_webview;


    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_doucment_detail);

        activity = this;

        showprocessdialog();

//		progDailog = ProgressDialog.show(activity, "加载中", "...", true);
//		progDailog.setCancelable(true);

        webView = (WebView) this.findViewById(R.id.webView);
        fl_webview = (FrameLayout) findViewById(R.id.fl_webview);

        findViewById(R.id.knowledge_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, KnowLegePrivewActivity.class);
                intent.putExtra("article_id", getIntent().getStringExtra("article_id"));
                intent.putExtra("article_src", getIntent().getStringExtra("article_src"));
                startActivity(intent);
            }
        });
        url = getIntent().getStringExtra("url");
        InitTopView();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				progDailog.show();
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                view.reload();
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                dismissdialog();
            }
        });
        webView.clearCache(true);
        webView.loadUrl(url);
        if (getIntent().hasExtra("article_id")) {
            getKnowledgeInfo(myHandler, getIntent().getStringExtra("article_id"), getIntent().getStringExtra("article_src"));
        }
        if(getIntent().hasExtra("shareUrl")){
            shareUrl=getIntent().getStringExtra("shareUrl");
        }
    }

    String title = "";
    String url = "";
    String shareUrl = "";
    private Tools tools;

    private CommonTopView topView;

    private void InitTopView() {
        title = getIntent().getStringExtra("title");
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);
        topView.setTitle(title);
        topView.setRigtext("分享");
        topView.onbackClick(this, new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean bool = false;
                try {
                    bool = webView.canGoBack();
                    if (bool) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                } catch (Exception localException) {
                }

            }
        });
        if (getIntent().hasExtra("type")) {
            topView.onRightClick(this, new OnClickListener() {

                @Override
                public void onClick(View v) {
                    showShare();

                }
            });
        }
        if (getIntent().hasExtra("doucmentType")) {

        }

    }

    private void showShare() {
        SHARE_MEDIA[] shareMedia = {SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.WEIXIN_CIRCLE};
        // initCustomPlatforms(shareMedia);
        // showShareContent(shareMedia, shareTitle, shareContent, targetUrl);
        showShareDialog(getString(R.string.app_name), title, shareUrl);
    }

    protected static final int ADD_FAILE = 0x60089;
    protected static final int ADD_SUCESS = 0x60090;

    public void getAddScore(String id) {
        tools = new Tools(mcontext, Constants.AC);
        JSONObject job = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("aid", id);
            String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
                    .parseStrToMd5L32(job.toString())
                    + "goods/actionGetIntegral");
            json.put("token", jobStr);
            json.put("msg_content", job.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onCirclModeRequest(myHandler, WebDocmentDetail.this,
                ADD_SUCESS, ADD_FAILE, job, "goods/actionGetIntegral");
    }

    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case ADD_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setAddRes(new JSONObject(msg.obj.toString()),
                                        activity);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case ADD_FAILE:
                        // ToastUtils.showToast(getActivity(), "");
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
        if (jsonObject.optString("result_id").equals("0")) {
            if (jsonObject.has("test")) {
                JSONObject job = jsonObject.optJSONObject("test");
                if (null == job) {
                    return;
                }
                if (job.optInt("conTest") > 0) {
                    findViewById(R.id.knowledge_btn).setVisibility(View.VISIBLE);
                }
            }
        } else {
            ToastUtil.showMessage(jsonObject.optString("result_msg"));
        }
    }

    protected void setAddRes(JSONObject jsonObject, Activity activity) {
        if (jsonObject.optString("result_id").equals("0")) {
            int score = jsonObject.optInt("integral");
            String str = tools.getValue(Constants.INTEGRAL);
            if (null != str) {
                tools.putValue(Constants.INTEGRAL, Integer.parseInt(str)
                        + score + "");
            } else {
                tools.putValue(Constants.INTEGRAL, score + "");
            }
            if (score > 0) {
                if (null != activity) {
                    showTipsDialog(score + "");
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        webView.loadUrl("javascript:closeIframe(" + ")");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dealJavascriptLeak();
        }
        if (webView != null) {
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void dealJavascriptLeak() {
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
    }
}
