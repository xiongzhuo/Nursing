package com.deya.hospital.base;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.downloader.DownLoaderTask;
import com.deya.hospital.downloader.DownLoaderTask.DownLoadInter;
import com.deya.hospital.downloader.ZipExtractorTask;
import com.deya.hospital.downloader.ZipExtractorTask.ZipTofilesInter;
import com.deya.hospital.login.LoginActivity;
import com.deya.hospital.services.BaseDataInitService;
import com.deya.hospital.setting.SystemSettingFragment;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.ApkDownLoad;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.UpdateInfo;
import com.deya.hospital.workspace.workspacemain.WorkWebFragment;
import com.google.gson.Gson;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.ECPreferenceSettings;
import com.im.sdk.dy.common.utils.ECPreferences;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.core.ClientUser;
import com.im.sdk.dy.service.IMNotifyReceiver;
import com.im.sdk.dy.ui.ConversationListFragment;
import com.im.sdk.dy.ui.ConversationListFragment.OnUpdateMsgUnreadCountsListener;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.im.sdk.dy.ui.account.AccountLogic;
import com.im.sdk.dy.ui.chatting.IMChattingHelper;
import com.im.sdk.dy.ui.settings.SettingPersionInfoActivity;
import com.umeng.analytics.MobclickAgent;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainActivity extends BaseFragmentActivity implements
        OnUpdateMsgUnreadCountsListener {
    private static final int SDK_VERSION = 10;
    public FragmentTabHost mtabhost;
    private LayoutInflater layoutInflater;
    Tools tools;
    private long exitTime = 0;// 记录上一次点击返回按钮的时间
    public Class<?>[] fragmentArray = {ConversationListFragment.class,
            WorkWebFragment.class,SystemSettingFragment.class};
    private int[] mimageviewarray = {R.drawable.bottom_choice_bn_style,
            R.drawable.bottom_shopping_bn_style,
            R.drawable.bottom_service_bn_style};
    // Tab选项卡的文字
    private int[] mtextviewarray = {R.string.str_tab_home,
            R.string.str_tab_shop,
            R.string.str_tab_setting};
    protected UpdateInfo updateInfo;
    private static final int CHECK_UPDATE_SUCCESS = 0x1051;
    private static final int CHECK_UPDATE_FAIL = 0x1052;
    private static final int FILE_SUCESS = 0x20045;// 获取文件资源信息
    private static final int FILE_FAIL = 0x20046;
    private static final int TELL_SUCCESS = 0x10151;
    private static final int TELL_FAIL = 0x10152;
    Gson gson;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_activity_main);
        gson = new Gson();
        tools = new Tools(mcontext, Constants.AC);
        layoutInflater = LayoutInflater.from(this);

        initView();

        initIntent();
        checkUpdate();//APP更新
        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        initDevice();
        initBaseData();

        // 安装日志服务，随MainActivity生存周期
  //     Intent intent = new Intent(MainActivity.this, LogService.class);
//        this.bindService(intent, conn, Service.BIND_AUTO_CREATE);
        //后台加载基础数据
        serviceIntent = new Intent(MainActivity.this, BaseDataInitService.class);
        serviceIntent.putExtra(BaseDataInitService.INTENTCODE, BaseDataInitService.REFRESH_ALL);
        try {
            startService(serviceIntent);
        }catch (Exception e){
            e.printStackTrace();
        }

        MobclickAgent.openActivityDurationTrack(false);

    }

    // 连接对象
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("Service", "连接成功！");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("Service", "断开连接！");
        }
    };

    private void initBaseData() {
        // 发送广播到后台服务，获取基本数据
        // BASE_DATA
        synGroup();
        Intent intent = new Intent();
        intent.setAction(IMNotifyReceiver.ACTION_NOTIRECEIVER_IM);
        intent.putExtra(IMNotifyReceiver.RECEIVER_OPTION,
                IMNotifyReceiver.BASE_DATA);
        sendBroadcast(intent);
        DebugUtil.debug("IMNotifyReceiver_base", "send broadcast");
    }

    private void synContact() {
        Log.i("IMtag", "发广播了");
        // 发送查询广播
        Intent intent = new Intent();
        intent.setAction(IMNotifyReceiver.ACTION_NOTIRECEIVER_IM);
        intent.putExtra(IMNotifyReceiver.RECEIVER_OPTION,
                IMNotifyReceiver.OPTION_SYNC_CONTACTS);
        MyAppliaction.getContext().sendBroadcast(intent);

    }

    private void synGroup() {
        Log.i("IMtag", "发广播了");
        // 发送查询广播
        Intent intent = new Intent();
        intent.setAction(IMNotifyReceiver.ACTION_NOTIRECEIVER_IM);
        intent.putExtra(IMNotifyReceiver.RECEIVER_OPTION,
                IMNotifyReceiver.OPTION_SYNC_GROUPS);
        MyAppliaction.getContext().sendBroadcast(intent);

    }


    private void initView() {
        // 实例化布局对象

        mtabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mtabhost.setup(this, getSupportFragmentManager(), R.id.container);
        // 去掉分割线方法1
        TabWidget mtabwidget = (TabWidget) findViewById(android.R.id.tabs);
        mtabwidget.setBackgroundColor(Color.WHITE);
        if (android.os.Build.VERSION.SDK_INT > SDK_VERSION) {
            mtabwidget.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        }
        mtabwidget.setGravity(Gravity.CENTER_VERTICAL);
        for (int i = 0; i < fragmentArray.length; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabSpec taSpec = mtabhost.newTabSpec(getString(mtextviewarray[i]))
                    .setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mtabhost.addTab(taSpec, fragmentArray[i], null);
        }

        if (getIntent().hasExtra("activityPage")) {
            getIntent().getStringExtra("activityPage").equals("LOGIN_MYREGISTERACTIVITY");
            mtabhost.setCurrentTab(1);
        } else {
            mtabhost.setCurrentTab(1);
        }


    }

    // 给Tab按钮设置图标和文字
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.init_activity_item_view,
                null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tabimageview);
        // Log.i("1111", imageView+"----"+layoutInflater+"");
        imageView.setImageResource(mimageviewarray[index]);
        TextView tx = (TextView) view.findViewById(R.id.tabtextview);
        tx.setText(mtextviewarray[index]);
        return view;
    }

    /**
     * 获取 intent 可以处理跳转
     */
    private void initIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            String tab_tag = intent.getStringExtra("main_tab_index");// tab_msg_one
            if (null != tab_tag && tab_tag.equals("tab_msg_one")) {
                // 跳转到ConVersationListFragment
                mtabhost.setCurrentTab(0);
                intent.removeExtra("main_tab_index");
                return;
            }
        }
        mtabhost.setCurrentTab(1);
    }

    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case CHECK_UPDATE_SUCCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setUpdate(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case CHECK_UPDATE_FAIL:

                        break;


                    case FILE_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                JugeSrcIsChanged(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case FILE_FAIL:
                        break;
                    default:
                        break;
                }
            }
        }

    };


    String digest = "";

    protected void JugeSrcIsChanged(JSONObject jsonObject) {
        JSONObject job = jsonObject.optJSONObject("report.zip");
        String url = job.optString("url");
        digest = job.optString("digest");
        String loacalId = tools.getValue(Constants.ZIP_ID);
        if (null != loacalId && loacalId.equals(digest)) {
            Log.i("Mainactivity", "资源已经加载！" + loacalId + digest);
            return;
        } else {
            Log.i("Mainactivity", "资源开始加载！");
            String path = MyAppliaction.getReportPath();
            File file = new File(path);
            if (file.exists()) {

            } else {
                file.mkdirs();
            }
            doDownLoadWork(url);
        }

    }


    /**
     * TODO 复写按键监听，点击返回键两次即退出程序.
     *
     * @see android.support.v4.app.FragmentActivity#onKeyDown(int,
     * KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * exit:(退出程序). <br/>
     */
    public void exit() {
        // 判断当前点击返回键的时间与上一次保存的时间的差值是否大于2秒
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            // 当前点击返回键的时间与上一次保存的时间的差值大于2秒 则提示再次点击返回键关闭程序
            ToastUtils.showToast(MyAppliaction.getContext(),
                    getString(R.string.str_press_again_to_exit));
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onActivityResult(int requstCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requstCode, resultCode, data);

        if (null != data && resultCode == 1) {

        }
    }

    private void checkUpdate() {
        JSONObject job = new JSONObject();
        try {

            job.put("type", "2");
            String pkName = MyAppliaction.getContext().getPackageName();
            String versionName = null;
            try {
                versionName = mcontext.getPackageManager()
                        .getPackageInfo(pkName, 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            JSONObject clientInfoJson = new JSONObject();
            clientInfoJson.put("soft_ver", versionName);
            clientInfoJson.put ("device_model", android.os.Build.MODEL + "");
            clientInfoJson.put("os_ver", android.os.Build.VERSION.RELEASE + "");
            clientInfoJson.put("device_type", "2");
            clientInfoJson.put("app_code",WebUrl.APP_CODE);
            job.put("client_info", clientInfoJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, MyAppliaction.getContext(),
                CHECK_UPDATE_SUCCESS, CHECK_UPDATE_FAIL, job,
                "version/checkVersion");
    }

    /**
     * disposecheckUpdate:【这里用一句话描述这个方法的作用】. <br/>
     * ..<br/>
     */
    private void setUpdate(JSONObject result) {

        if (!AbStrUtil.isEmpty(result.toString())) {
            try {
                Log.i("111111111", result.toString());
                JSONObject jsonObject = result.optJSONObject("latestVersion");
                updateInfo = gson.fromJson(jsonObject.toString(),
                        UpdateInfo.class);
                if (null != updateInfo) {
                    PackageManager manager = getPackageManager();
                    PackageInfo info = manager.getPackageInfo(getPackageName(),
                            0);
                    if (Long.parseLong(updateInfo.getVer_number()) > info.versionCode) {
                        showHasNewDialodAndForcedUpgrade(updateInfo.getContent().toString());

                    } else {
                        loadWebSrc();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    UpdateTipsDialog baseTipsDialog;
    private void showHasNewDialodAndForcedUpgrade(String text) {
        baseTipsDialog =new UpdateTipsDialog(mcontext, new MyDialogInterface() {
            @Override
            public void onItemSelect(int postion) {

            }

            @Override
            public void onEnter() {
                ToastUtil.showMessage( getString(R.string.str_start_download));
                new ApkDownLoad(getApplicationContext(),
                     updateInfo.getUrl(),
                        getString(R.string.app_name),
                        getString(R.string.str_version_update));
            }

            @Override
            public void onCancle() {

            }
        });
        baseTipsDialog.show();
        baseTipsDialog.setTitleTv("新版更新，是否更新？");
        baseTipsDialog.setContentTiltle("更新内容：");
        baseTipsDialog.setContentTv(text);
        baseTipsDialog.setButton("确认更新");
        baseTipsDialog.setCancelable(false);
    }


    public void resetCasche() {
        try {
            tools.putValue(Constants.AUTHENT, "");
            tools.putValue(Constants.NAME, "");
            tools.putValue(Constants.HOSPITAL_NAME, "");
            tools.putValue(Constants.HOSPITAL_ID, "");
            tools.putValue(Constants.AGE, "");
            tools.putValue(Constants.SEX, "");
            tools.putValue(Constants.STATE, "");
            tools.putValue(Constants.IS_SIGN_OLD, "");
            tools.putValue(Constants.MOBILE, "");
            tools.putValue(Constants.HEAD_PIC, "");
            tools.putValue(Constants.EMAIL, "");
            tools.putValue(Constants.JOB, "");
            tools.putValue(Constants.JOB_NAME, "");
            tools.putValue(Constants.USER_ID, "");

            if (ECDevice.isInitialized())
                try {
                    ECDevice.unInitial();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            SDKCoreHelper.logout(false);

            CCPAppManager.setClientUser(null);

            MainActivity.mInit = false;

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private boolean isContinue = true;


    @Override
    protected void onDestroy() {
        isContinue = false;
        gson = null;
        tools = null;

        if (internalReceiver != null) {
            unregisterReceiver(internalReceiver);
        }
        if(null!=baseTipsDialog&&baseTipsDialog.isShowing()){
            baseTipsDialog.dismiss();
        }
        this.stopService(serviceIntent);
        super.onDestroy();
    }


    @Override
    public void OnUpdateMsgUnreadCounts() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onResume() {
        LogUtil.i(LogUtil.getLogUtilsTag(LauncherActivity.class),
                "onResume start");
        super.onResume();
        if (internalReceiver == null) {
            internalReceiver = new InternalReceiver();
            IntentFilter intentfilter = new IntentFilter();
            intentfilter.addAction(SDKCoreHelper.ACTION_SDK_CONNECT);
            intentfilter.addAction(TabBaseFragment.TOWORKCIRCLEFRAGMENT);
            intentfilter.addAction(TabBaseFragment.TOSETTINGFRAGMENT);
            intentfilter.addAction(Constants.AUTHENT_LOSE);
            registerReceiver(internalReceiver, intentfilter);
        }


        initUser();

        initIm();

    }

    public static boolean mInit = false;

    private Runnable initRunnable = new Runnable() {
        @Override
        public void run() {
            mInit = false;
            doInitAction();
        }
    };

    private void initDevice() {
        if (!mInit) {
            mInit = true;
            // 程序启动开始创建一个splash用来初始化程序基本数据
            ECHandlerHelper.postDelayedRunnOnUI(initRunnable, 3000);
        }
    }

    /**
     * 处理一些初始化操作
     */
    private void doInitAction() {

        LogUtil.d("doInitAction_status", "SDKCoreHelper.getConnectState()>>"
                + SDKCoreHelper.getConnectState().ordinal());
        if (SDKCoreHelper.getConnectState() == ECDevice.ECConnectState.CONNECT_SUCCESS
                && !mInitActionFlag) {

            AccountLogic.setAccount(CCPAppManager.getClientUser());

            IMChattingHelper.getInstance().getPersonInfo();
            // settingPersionInfo();
            // 检测离线消息
            checkOffineMessage();

            mInitActionFlag = true;
        }
    }

    private InternalReceiver internalReceiver;

    private class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TabBaseFragment.TOWORKCIRCLEFRAGMENT)) {//认证中用户去工作间
                mtabhost.setCurrentTab(2);
                return;
            }
            if (intent.getAction().equals(TabBaseFragment.TOSETTINGFRAGMENT)) {//被拒绝用户选中个人设置
                mtabhost.setCurrentTab(3);
                return;
            }
            if (intent.getAction().equals(Constants.AUTHENT_LOSE)) {//被拒绝用户选中个人设置
                resetCasche();
                Intent intent2 = new Intent(mcontext,
                        LoginActivity.class);
                startActivity(intent2);
                finish();
                return;
            }
            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                return;
            }
            if (SDKCoreHelper.ACTION_SDK_CONNECT.equals(intent.getAction())) {
                doInitAction();

            }
        }
    }

    private boolean mInitActionFlag;

    private void initIm() {
        ClientUser user = CCPAppManager.getClientUser();
        if (!mInitActionFlag) {
            // mInitActionFlag=true;
            // initUser();

            // ECContentObservers.getInstance().initContentObserver();
            Intent intent = new Intent();
            intent.setAction(IMNotifyReceiver.ACTION_NOTIRECEIVER_IM);
            intent.putExtra(IMNotifyReceiver.RECEIVER_OPTION,
                    IMNotifyReceiver.OPTION_SYNC_CONTACTS);
            sendBroadcast(intent);
            if (SDKCoreHelper.getConnectState() != ECDevice.ECConnectState.CONNECT_SUCCESS
                    && !SDKCoreHelper.isKickOff()) {
                SDKCoreHelper.init(this);
            }
            if (!mInit) {
                doInitAction();
            }
        }
        OnUpdateMsgUnreadCounts();
        tellDyOurState();// 告知服务器已更新
    }

    private void tellDyOurState() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomRequest(myHandler, MyAppliaction.getContext(),
                TELL_FAIL, TELL_SUCCESS, job, "iminfo/initalizeGroupMember");

    }

    private void initUser() {
        ClientUser user = CCPAppManager.getClientUser();
        if (null == user) {
            user = new ClientUser();
            try {
                user.setSex(Integer.parseInt(tools.getValue(Constants.SEX)));
            } catch (Exception e) {
                // TODO: handle exception
            }

            DebugUtil.debug("initIm",
                    "phone>>" + tools.getValue(Constants.USER_ID));
            user.setUserId(tools.getValue(Constants.USER_ID));
            user.setUserName(tools.getValue(Constants.NAME));
            user.setMoblie(tools.getValue(Constants.MOBILE));
            user.setAvatar(tools.getValue(Constants.HEAD_PIC));
            if (null != tools.getValue(Constants.SEX)
                    && !AbStrUtil.isEmpty(tools.getValue(Constants.SEX))
                    && tools.getValue(Constants.SEX).equals("0")) {
                user.setSex(0);
            } else {
                user.setSex(1);
            }

            CCPAppManager.setClientUser(user);
        } else {
            if (!AbStrUtil.isEmpty(tools.getValue(Constants.SEX))
                    && tools.getValue(Constants.SEX).equals("0")) {
                user.setSex(0);
            } else {
                user.setSex(1);
            }
        }
    }


    /**
     * 检查是否需要自动登录
     *
     * @return
     */
    private String getAutoRegistAccount() {
        SharedPreferences sharedPreferences = ECPreferences
                .getSharedPreferences();
        ECPreferenceSettings registAuto = ECPreferenceSettings.SETTINGS_REGIST_AUTO;
        String registAccount = sharedPreferences.getString(registAuto.getId(),
                (String) registAuto.getDefaultValue());
        return registAccount;
    }

    private void settingPersionInfo() {
        if (IMChattingHelper.getInstance().mServicePersonVersion == 0
                && CCPAppManager.getClientUser().getpVersion() == 0) {
            Intent settingAction = new Intent(this,
                    SettingPersionInfoActivity.class);
            settingAction.putExtra("from_regist", true);
            startActivityForResult(settingAction, 0x2a);
            return;
        }
    }

    /**
     * 检测离线消息
     */
    private void checkOffineMessage() {
        if (SDKCoreHelper.getConnectState() != ECDevice.ECConnectState.CONNECT_SUCCESS) {
            return;
        }
        ECHandlerHelper handlerHelper = new ECHandlerHelper();
        handlerHelper.postDelayedRunnOnThead(new Runnable() {
            @Override
            public void run() {
                boolean result = IMChattingHelper.isSyncOffline();
                if (!result) {
                    ECHandlerHelper.postRunnOnUI(new Runnable() {
                        @Override
                        public void run() {
                            // disPostingLoading();
                        }
                    });
                    IMChattingHelper.checkDownFailMsg();
                }
            }
        }, 1000);
    }





    public void loadWebSrc() {
        // MainBizImpl.getInstance().getWebFiles(myHandler, FILE_SUCESS,
        // FILE_FAIL);;
    }

    public void doZipExtractorWork() {
        // ZipExtractorTask task = new
        // ZipExtractorTask("/storage/usb3/system.zip",
        // "/storage/emulated/legacy/", this, true);

        ZipExtractorTask task = new ZipExtractorTask(
                MyAppliaction.getReportPath() + "/report.zip",
                MyAppliaction.getReportPath(), this, true,
                new ZipTofilesInter() {

                    @Override
                    public void onComplet() {
                        tools.putValue(Constants.ZIP_ID, digest);
                        Log.i("Mainactivity", "保存资源ID成功！digest=" + digest);
                    }
                });
        task.execute();
    }

    private void doDownLoadAPP(final String name) {
        File pathFile = new File(getPath());
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        DownLoaderTask task = new DownLoaderTask("手卫生更新",
                WebUrl.FILE_LOAD_URL + name, getPath(), this,
                new DownLoadInter() {

                    @Override
                    public void onCompelet() {
                        Log.i("Mainactivity", "资源加载完成了！");
                        File file = new File(getPath());
                        install(mcontext, file.toString() + "/" + name);
                    }
                });
        // DownLoaderTask task = new
        // DownLoaderTask("http://192.168.9.155/johnny/test.h264",
        // getCacheDir().getAbsolutePath()+"/", this);
        task.execute();
    }

    public boolean install(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            i.setDataAndType(Uri.parse("file://" + filePath),
                    "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID
            System.exit(0); // 常规java、c#的标准退出法，返回值为0代表正常退出
            return true;
        }
        return false;
    }

    private void doDownLoadWork(String url) {
        DownLoaderTask task = new DownLoaderTask("资源更新中", url,
                MyAppliaction.getReportPath(), this, new DownLoadInter() {
            @Override
            public void onCompelet() {
                Log.i("Mainactivity", "资源加载完成了！");
                doZipExtractorWork();
            }
        });
        // DownLoaderTask task = new
        // DownLoaderTask("http://192.168.9.155/johnny/test.h264",
        // getCacheDir().getAbsolutePath()+"/", this);
        task.execute();
    }

    private File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath());
        }
        return sdDir;
    }

    public static final String DOWNLOAD_FOLDER_NAME = "deya/apkfile";
    public static final String DOWNLOAD_FILE_NAME = "gankong.apk";
    public static final String APK_DOWNLOAD_ID = "apkDownloadId";

    private String getPath() {
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
}
