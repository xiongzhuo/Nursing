package com.deya.hospital.setting;//package com.deya.hospital.setting;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Message;
//import android.util.Log;
//
//import com.deya.acaide.R;
//import com.deya.hospital.application.MyAppliaction;
//import com.deya.hospital.bizimp.MainBizImpl;
//import com.deya.hospital.util.AbStrUtil;
//import com.deya.hospital.util.Constants;
//import com.deya.hospital.util.MyHandler;
//import com.deya.hospital.util.Tools;
//import com.deya.hospital.vo.ShareVo;
//import com.deya.hospital.widget.popu.TipsDialog;
//import com.deya.hospital.workspace.TaskUtils;
//import com.im.sdk.dy.ui.DyShareActivity;
//import com.umeng.socialize.bean.CustomPlatform;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.bean.SocializeEntity;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.controller.listener.SocializeListeners;
//import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
//import com.umeng.socialize.media.QQShareContent;
//import com.umeng.socialize.media.QZoneShareContent;
//import com.umeng.socialize.media.SinaShareContent;
//import com.umeng.socialize.media.SmsShareContent;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.media.UMVideo;
//import com.umeng.socialize.media.UMusic;
//import com.umeng.socialize.sso.QZoneSsoHandler;
//import com.umeng.socialize.sso.SinaSsoHandler;
//import com.umeng.socialize.sso.SmsHandler;
//import com.umeng.socialize.sso.UMQQSsoHandler;
//import com.umeng.socialize.weixin.controller.UMWXHandler;
//import com.umeng.socialize.weixin.media.CircleShareContent;
//import com.umeng.socialize.weixin.media.WeiXinShareContent;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * ClassName:. ShareUtil【描述该类的作用】 <br/>
// */
//public class UmengShareUtil {
//    // 整个平台的Controller,负责管理整个SDK的配置、操作等处理
//    public UMSocialService mController = null;
//    private MyHandler myHandler;
//    private DoAddScoreInterface addScoreInterface;
//    private static UmengShareUtil shareUtil;
//    public static final String IM_IS_SHARE = "im_is_share";
//    public static final String SHARE_CONTENT = "share_content";
//    Tools tools;
//    String type;
//
//    public static UmengShareUtil getInstance() {
//        if (null == shareUtil) {
//            shareUtil = new UmengShareUtil();
//        }
//        return shareUtil;
//    }
//
//    public UmengShareUtil() {
//        mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR);
//    }
//
//    public void init(Activity activity) {
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        addWXPlatform(activity);
//        addQQQZonePlatform(activity);
//    }
//
//    /**
//     * @return
//     * @功能描述 : 添加微信平台分享
//     */
//    public void addWXPlatform(Activity activity) {
//        // 注意：在微信授权的时候，必须传递appSecret
//        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(activity, Constants.APP_ID,
//                Constants.APP_SECRET);
//        wxHandler.showCompressToast(false);
//        wxHandler.addToSocialSDK();
//
//        // 支持微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(activity,
//                Constants.APP_ID, Constants.APP_SECRET);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//    }
//
//    /**
//     * @return
//     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
//     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
//     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
//     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
//     */
//    public void addQQQZonePlatform(Activity activity) {
//        // 添加QQ支持, 并且设置QQ分享内容的target url
//        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity,
//                Constants.QQ_APP_ID, Constants.QQ_AaPP_KEY);
//        qqSsoHandler.setTargetUrl("http://studio.gkgzj.com");
//        qqSsoHandler.setTitle("");
//        qqSsoHandler.addToSocialSDK();
//
//        // 添加QZone平台
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
//                Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
//        qZoneSsoHandler.setTargetUrl("http://studio.gkgzj.com");
//        qqSsoHandler.setTitle("护理工作间");
//        qZoneSsoHandler.addToSocialSDK();
//    }
//
//    /**
//     * 添加短信平台</br>
//     */
//    private void addSMS() {
//        // 添加短信
//        SmsHandler smsHandler = new SmsHandler();
//        smsHandler.addToSocialSDK();
//    }
//
//    /**
//     * @param shareType         分享的类型
//     * @param activity
//     * @param sharMedia
//     * @param shareTitle        分享的标题
//     * @param shareContent      分享的内容
//     * @param targetUrl         跳转的url
//     * @param addScoreInterface
//     */
//    public void showShareContent(String shareType, Activity activity,
//                                 SHARE_MEDIA[] sharMedia, String shareTitle, String shareContent,
//                                 String targetUrl, DoAddScoreInterface addScoreInterface) {
//        isContinueRequst = true;
//        this.addScoreInterface = addScoreInterface;
//        UMImage resImage = new UMImage(activity, R.drawable.share_logo);
//        showShareContent(activity, sharMedia, shareTitle, shareContent,
//                targetUrl, resImage);
//    }
//
//    /**
//     * 友盟分享
//     *
//     * @param shareTitle    分享标题
//     * @param shareContent  分享内容
//     * @param targetUrl     分享后的跳转路径
//     * @param shareImageURL 分享的图片路径
//     */
//    public void showShareContent(Activity activity, SHARE_MEDIA[] shareMedia,
//                                 String shareTitle, String shareContent, String targetUrl,
//                                 String shareImageURL) {
//        UMImage shareImage = new UMImage(activity, shareImageURL);
//        showShareContent(activity, shareMedia, shareTitle, shareContent,
//                targetUrl, shareImage);
//    }
//
//    /**
//     * 友盟分享
//     *
//     * @param shareTitle   分享标题
//     * @param shareContent 分享内容
//     * @param targetUrl    分享路径
//     * @param umImage      分享图片
//     */
//    public void showShareContent(Activity activity, SHARE_MEDIA[] sharMedia,
//                                 String shareTitle, String shareContent, String targetUrl,
//                                 UMImage umImage) {
//        showShareContent(activity, sharMedia, shareTitle, shareContent,
//                targetUrl, umImage, null);
//        // setShareContent(sharMedia);
//    }
//
//    /**
//     * 第三方分享
//     *
//     * @param shareTitle
//     * 分享标题
//     * @param shareContent
//     * 分享内容
//     * @param targetUrl
//     * 分享后的跳转路径
//     * @param umImage
//     * 分享图片
//     * @param umVideo
//     * 分享视频
//     */
//
//    boolean isContinueRequst = false;
//
//    public void showShareContent(final Activity activity,
//                                 SHARE_MEDIA[] sharMedia,  String shareTitle, String shareContent,
//                                 String targetUrl, UMImage shareImage, UMVideo umVideo) {
//        init(activity);
//        initMyHandler(activity);
//        if (AbStrUtil.isEmpty(shareContent))
//            shareContent = " ";
//        if (mController == null)
//            mController = UMServiceFactory
//                    .getUMSocialService(Constants.DESCRIPTOR);
//        // 配置SSO
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        CustomPlatform mCustomPlatform = new CustomPlatform("护理工作间", "护理工作间",R.drawable.gk_share);
//        mCustomPlatform.mGrayIcon = R.drawable.gk_share;// 灰色图标id
//        final ShareVo shareVo=new ShareVo();
//        shareVo.setShareContent(shareContent);
//        shareVo.setShareTitle(shareTitle);
//        shareVo.setTargetUrl(targetUrl);
//        tools = MyAppliaction.getTools();
//        mCustomPlatform.mClickListener = new SocializeListeners.OnSnsPlatformClickListener() {
//            @Override
//            public void onClick(Context context, SocializeEntity entity,
//
//                                SnsPostListener listener) {
//                Intent intent=new Intent(activity, DyShareActivity.class);
//                tools.putValue(SHARE_CONTENT, TaskUtils.gson.toJson(shareVo));
//                activity.startActivity(intent);
//
//
//            }
//
//        };
//        // 添加微信平台
//        addWXPlatform(activity);
//        // 添加QQ平台
//        addQQQZonePlatform(activity);
//        // 添加印象笔记平台
//        addSMS();
//        mController.setShareContent(shareContent);
//        if (shareImage == null)
//            shareImage = new UMImage(activity, R.drawable.share_logo);
//
//        // UMImage qzoneImage = new
//        // UMImage(this,"http://img1.imgtn.bdimg.com/it/u=1254852154,670308231&fm=23&gp=0.jpg");
//        // UMImage localImage = new UMImage(this, R.drawable.device);
//        // UMImage resImage = new UMImage(this, R.drawable.logo);
//        // 视频分享
//        UMVideo vedio = new UMVideo(
//                "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
//        vedio.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
//        vedio.setTitle("haha");
//        vedio.setThumb(shareImage);
//
//        UMusic uMusic = new UMusic(
//                "http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
//        uMusic.setAuthor("umeng");
//        uMusic.setTitle("天籁之音");
//        uMusic.setThumb(shareImage);
//        uMusic.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");
//
//        // 微信分享
//        UMImage shareImage2 = new UMImage(activity, R.drawable.share_logo);
//        WeiXinShareContent weixinContent = new WeiXinShareContent();
//        weixinContent.setTitle(shareTitle);
//        weixinContent.setShareContent(shareContent);
//        weixinContent.setTargetUrl(targetUrl);
//        weixinContent.setShareImage(shareImage2);
//        mController.setShareMedia(weixinContent);
//
//        // 设置微信朋友圈分享的内容
//        CircleShareContent circleMedia = new CircleShareContent();
//        circleMedia.setTitle(shareTitle);
//        circleMedia.setShareContent(shareContent);
//        circleMedia.setShareImage(shareImage);
//        circleMedia.setTargetUrl(targetUrl);
//        mController.setShareMedia(circleMedia);
//
//        // qzoneImage
//        // .setTargetUrl("http://www.umeng.com/images/pic/social/integrated_3.png");
//
//        // 设置QQ空间分享内容
//        // QZoneShareContent qzone = new QZoneShareContent();
//        // qzone.setTitle(shareTitle);
//        // qzone.setShareContent(shareContent);
//        // qzone.setTargetUrl(targetUrl);
//        // qzone.setShareImage(shareImage);
//        // mController.setShareMedia(qzone);
//
//        // video.setThumb(new UMImage(this, BitmapFactory
//        // .decodeResource(getResources(), R.drawable.device)));
//        // QQ分享
//
//        QQShareContent qqShareContent = new QQShareContent();
//        qqShareContent.setTitle(shareTitle);
//        qqShareContent.setShareContent(shareContent);
//        qqShareContent.setShareImage(shareImage);
//        // qqShareContent.setShareMusic(uMusic);
//        // qqShareContent.setShareVideo(video);
//        qqShareContent.setTargetUrl(targetUrl);
//        mController.setShareMedia(qqShareContent);
//
//        QZoneShareContent qzShareContent = new QZoneShareContent();
//        qzShareContent.setTitle(shareTitle);
//        qzShareContent.setShareContent(shareContent);
//        qzShareContent.setShareImage(shareImage);
//        // qqShareContent.setShareMusic(uMusic);
//        // qqShareContent.setShareVideo(video);
//        qzShareContent.setTargetUrl(targetUrl);
//        mController.setShareMedia(qzShareContent);
//        // // 视频分享
//        // UMVideo umVideo = new UMVideo(
//        // "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
//        // umVideo.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
//        // umVideo.setTitle("友盟社会化组件视频");
//
//        // TencentWbShareContent tencent = new TencentWbShareContent();
//        // tencent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，腾讯微博");
//        // // 设置tencent分享内容
//        // mController.setShareMedia(tencent);
//
//        // 设置短信分享内容
//        SmsShareContent sms = new SmsShareContent();
//        sms.setShareContent(shareContent + targetUrl);
//        // sms.setShareImage(shareImage);
//
//        mController.setShareMedia(sms);
//
//        SinaShareContent sinaContent = new SinaShareContent(shareImage);
//        sinaContent.setTitle(shareTitle);
//        sinaContent.setShareContent(shareContent + targetUrl);
//        // sinaContent.setShareImage(shareImage);
//        sinaContent.setTargetUrl(targetUrl);
//        mController.setShareMedia(sinaContent);
//
//        // 分享按钮图标
//
//
//        // 设置平台的logo和关键字
//
//
//       mController.getConfig().setPlatforms(sharMedia);
//        mController.getConfig().addCustomPlatform(mCustomPlatform);
//
//        mController.openShare(activity, new SnsPostListener() {
//
//            @Override
//            public void onStart() {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onComplete(SHARE_MEDIA platform, int eCode,
//                                   SocializeEntity entity) {
//                String showText = platform.toString();
//                if (eCode == 200) {
//                    Log.i("umeng", "111111111111");
//                        // getAddScore(activity,type);
//                        // isContinueRequst=false;
//                           // addScoreInterface.addScore();
//                            getAddScore("3");
//
//                } else {
//                    showText += "平台分享失败";
//                }
//                // ToastUtils.showToast(activity, showText);
//            }
//        });
//    }
//
//    protected static final int ADD_FAILE = 0x60089;
//    protected static final int ADD_SUCESS = 0x60090;
//
//    public void initMyHandler(Activity activity) {
//        myHandler = new MyHandler(activity) {
//            @Override
//            public void handleMessage(Message msg) {
//                Activity activity = myHandler.mactivity.get();
//                if (null != activity) {
//                    switch (msg.what) {
//                        case ADD_SUCESS:
//                            if (null != msg && null != msg.obj) {
//                                try {
//                                    setAddResult(new JSONObject(msg.obj.toString()));
//
//                                } catch (JSONException e5) {
//                                    e5.printStackTrace();
//                                }
//                            }
//                            break;
//                        case ADD_FAILE:
//                            break;
//
//                        default:
//                            break;
//                    }
//                }
//            }
//        };
//    }
//
//    private void setAddResult(JSONObject jsonObject) {
//        Log.i("share_umeng", "返回次数");
//        Log.i("11111111", jsonObject.toString());
//        if (jsonObject.optString("result_id").equals("0")) {
//            int score = jsonObject.optInt("integral");
//            String str = tools.getValue(Constants.INTEGRAL);
//            if (null != str) {
//                tools.putValue(Constants.INTEGRAL, Integer.parseInt(str)
//                        + score + "");
//            } else {
//                tools.putValue(Constants.INTEGRAL, score + "");
//            }
//            if (score > 0) {
//                if(null!=myHandler.mactivity.get()){
//                    TipsDialog tipdialog = new TipsDialog(myHandler.mactivity.get(), score + "");
//                    tipdialog.show();
//                }
//            }
//
//        }
//    }
//
//
//    public void getAddScore(String id) {
//        Log.i("share_umeng", "111111111111111");
//
//        JSONObject job = new JSONObject();
//        JSONObject json = new JSONObject();
//        try {
//            job.put("authent", tools.getValue(Constants.AUTHENT));
//            job.put("aid", id);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        MainBizImpl.getInstance().onComomRequest(myHandler,
//                MyAppliaction.getContext(), ADD_SUCESS, ADD_FAILE, job, "goods/actionGetIntegral");
//    }
//
//    public interface DoAddScoreInterface {
//        public void addScore();
//    }
//
//}
