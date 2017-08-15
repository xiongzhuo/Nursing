package com.deya.hospital.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.deya.acaide.R;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.AddScoreToast;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.vo.ShareVo;
import com.deya.hospital.widget.popu.TipsDialog;
import com.deya.hospital.workspace.TaskUtils;
import com.im.sdk.dy.ui.DyShareActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/30
 */
public class DyShareDialog extends BaseDialog implements View.OnClickListener ,RequestInterface{
    public static final String SHARE_CONTENT = "share_content";
    private static final int ADD_SUCESS =0x901 ;
    Activity context;
    String shareTitle;
    String shareContent;
    String targetUrl;
    UMImage shareImage;
    private UMSocialService mController;
    Tools tools;
    MyHandler myHandler;
    TipsDialog tipdialog;
    /**
     * Creates a new instance of MyDialog.
     *
     * @param context
     */
    public DyShareDialog(Activity context, String title, String content, String url) {
        super(context);
        this.context = context;
        this.shareTitle = title;
        this.shareContent = content;
        this.targetUrl = url;
    }

    public void setShareImg(UMImage ImgId) {
        this.shareImage = ImgId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dy_share_layout);
         tipdialog = new TipsDialog(context);
        tools = new Tools(context, Constants.AC);
        Window dialogWindow = getWindow();
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        ;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        init();
        initShare();
        initHandler();
        initView();

    }

    private void initView() {
        findViewById(R.id.dyshare).setOnClickListener(this);
        findViewById(R.id.qqshare).setOnClickListener(this);
        findViewById(R.id.weixingshare).setOnClickListener(this);
        findViewById(R.id.weixingFshare).setOnClickListener(this);

    }

    private void init() {
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE); //按需求设置想要的平台


    }

    public void show(String title, String content,
                String url){
        super.show();
        this.shareTitle = title;
        this.shareContent = content;
        this.targetUrl = url;
        initShare();


    }
    private void initShare() {
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, Constants.APP_ID, Constants.APP_SECRET);
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, Constants.APP_ID, Constants.APP_SECRET);
        wxCircleHandler.setToCircle(true);
        //QQ分享
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
        wxHandler.addToSocialSDK();
        wxCircleHandler.addToSocialSDK();
        qqSsoHandler.addToSocialSDK();
        if (shareImage == null) {
            shareImage = new UMImage(context, R.drawable.share_logo);
        }
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setTitle(shareTitle);
        qqShareContent.setShareContent(shareContent);
        qqShareContent.setShareImage(shareImage);
        // qqShareContent.setShareMusic(uMusic);
        // qqShareContent.setShareVideo(video);
        qqShareContent.setTargetUrl(targetUrl);
        mController.setShareMedia(qqShareContent);


        UMImage shareImage2 = new UMImage(context, R.drawable.share_logo);
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setTitle(shareTitle);
        weixinContent.setShareContent(shareContent);
        weixinContent.setTargetUrl(targetUrl);
        weixinContent.setShareImage(shareImage2);
        mController.setShareMedia(weixinContent);

        // 设置微信朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setTitle(shareTitle);
        circleMedia.setShareContent(shareContent);
        circleMedia.setShareImage(shareImage);
        circleMedia.setTargetUrl(targetUrl);
        mController.setShareMedia(circleMedia);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dyshare:
                final ShareVo shareVo = new ShareVo();
                shareVo.setShareContent(shareContent);
                shareVo.setShareTitle(shareTitle);
                shareVo.setTargetUrl(targetUrl);
                Intent intent = new Intent(context, DyShareActivity.class);
                tools.putValue(SHARE_CONTENT, TaskUtils.gson.toJson(shareVo));
                context.startActivity(intent);
                dismiss();
                break;
            case R.id.qqshare:
                dismiss();
                openShare(SHARE_MEDIA.QQ);
                break;
            case R.id.weixingshare:
                openShare(SHARE_MEDIA.WEIXIN);
                dismiss();
                break;
            case R.id.weixingFshare:
                openShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                dismiss();
                break;
        }

    }

    public void openShare(SHARE_MEDIA var2) {
        mController.postShare(context, var2, new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA arg0, int eCode, SocializeEntity arg2) {
                String showText = arg0.toString();
                if (eCode == 200) {
                    Log.i("umeng", "111111111111");
                    // getAddScore(activity,type);
                    // isContinueRequst=false;
                    // addScoreInterface.addScore();
                    getAddScore("3");

                } else {
                    showText += "平台分享失败";
                }

            }
        });
    }


    public void getAddScore(String id) {
        Log.i("share_umeng", "111111111111111");

        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("aid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this,
                MyAppliaction.getContext(), ADD_SUCESS, job, "goods/actionGetIntegral");
    }
    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        Message message=new Message();
        message.obj=jsonObject.toString();
        message.what=0x111;
        myHandler.sendMessage(message);
        setScore(jsonObject);

    }

    private void setScore(JSONObject jsonObject) {
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
                if(context!=null&&null!=tipdialog){
                    AddScoreToast.showToast("+"+score);
                }
            }

        }
    }

    @Override
    public void onRequestErro(String message) {

    }

    @Override
    public void onRequestFail(int code) {

    }
    private void initHandler() {
        final Activity activity= (Activity) context;
        myHandler = new MyHandler(activity) {
            @Override
            public void handleMessage(Message msg) {
                if(null==activity){
                    return;
                }
                switch (msg.what) {
                    case 0x111:
                        try {
                            JSONObject jsonObject=new JSONObject(msg.obj.toString());
                            setScore(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;


                }
            }
        };
    }



}
