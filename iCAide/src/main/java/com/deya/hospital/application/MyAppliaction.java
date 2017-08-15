/**
 * 项目名称: . <br/>
 * 文件名称:MyAppliaction.java . <br/>
 * 日期:2015年6月10日下午6:12:28 . <br/>
 *
 * @author sun . <br/>
 */

package com.deya.hospital.application;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.ParamsUtil;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.dbdata.TaskNum;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.im.sdk.dy.ECApplication;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.L;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

;

/**
 * ClassName:. MyAppliaction【第一次初始化保存一些必要的参数】 <br/>
 */
public class MyAppliaction extends ECApplication {
    private File sdDir = null;
    private static DisplayMetrics dm = new DisplayMetrics();

    private static Context context;
    private static Tools tools;
    private static int mainThreadid;
    private static MyAppliaction mInstance = null;
    private static DbUtils db;
    //日历页面的全局参数
    public static List<TaskVo> monthToatalList = new ArrayList<TaskVo>();
    public List<TaskNum> monthNumList = new ArrayList<TaskNum>();
    public static HashMap<String, Integer> mMonthTaskNums = new HashMap<String, Integer>();
    public static HashMap<String, Integer> unDoTastNums = new HashMap<String, Integer>();
    public static Context getContext() {
        return context;
    }

    public static int getMainThreadid() {
        return mainThreadid;
    }

    public static void setContext(Context context) {
        MyAppliaction.context = context;
    }
    public static   JSONObject toolsState;

    /**
     * TODO 程序第一次初始化该方法.
     *
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        MultiDex.install(this);
        tools = new Tools(context, Constants.AC);
        mInstance = this;


        //TODO 初始化IM的  appid 和 token
        if (WebUrl.getInstance().isTest) {
            //测试环境
            SDKCoreHelper.appid = "aaf98f8951a4a43b0151a518cd5e01d7";
            SDKCoreHelper.apptoken = "fa2a20e354587f7632d9e74499a06d20";
        } else {
            //正式环境
            SDKCoreHelper.appid = "aaf98f8951ce5b650151d18a5ad40502";
            SDKCoreHelper.apptoken = "0005a42a4f1cea162eb63fef79761e0e";
        }

        mainThreadid = android.os.Process.myTid();// 主线程id
        clearCookies(getApplicationContext());
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // sd card 可用
                sdDir =
                        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DeYaCache/");
            } else {
                // 当前不可用
                sdDir = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sdDir = null;
        }

        initImageLoader(getApplicationContext());


        CrashReport.initCrashReport(getApplicationContext(), Constants.BUGLY_APP_ID, true);
        // 内存检测工具
    //    LeakCanary.install(this);
//    if (!Constants.UMENGISDEBUG) {
//      // 关闭友盟错误日志
//      MobclickAgent.setCatchUncaughtExceptions(false);
//    }
        getDbUtils(context);
        try {
        if(!AbStrUtil.isEmpty(tools.getValue(Constants.TOOL_SATE))){
            toolsState=new JSONObject(tools.getValue(Constants.TOOL_SATE)) ;
        }else{
            toolsState=new JSONObject();
            toolsState.put("hw",true);
            toolsState.put("dudao",true);
            toolsState.put("kaohe",true);
            toolsState.put("hz",true);
            toolsState.put("zyfh",true);
            toolsState.put("quality",true);
            toolsState.put("envior",true);
            toolsState.put("wast",true);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static  JSONObject getToolsState(){
        return toolsState;
    }

    public static void saveToolsState(){
        tools.putValue(Constants.TOOL_SATE,toolsState.toString());

    }
    /**
     * clearCookies:(清除cookie). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     */
    public void clearCookies(Context context) {
        // Edge case: an illegal state exception is thrown if an instance of
        // CookieSyncManager has not be created. CookieSyncManager is normally
        // created by a WebKit view, but this might happen if you start the
        // app, restore saved state, and click logout before running a UI
        // dialog in a WebView -- in which case the app crashes
        @SuppressWarnings("unused")
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }


    /**
     * initImageLoader:(初始化Universal Image loader). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     */
    public void initImageLoader(Context context) {

        DisplayImageOptions defaultOption =
                new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder build =
                new ImageLoaderConfiguration.Builder(context).threadPoolSize(3)
                        // 线程池内加载的数量
                        .threadPriority(Thread.NORM_PRIORITY - ParamsUtil.TWO)
                        // implementation/你可以通过自己的内存缓存实现
                        .denyCacheImageMultipleSizesInMemory()
                        // 解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
                        // .diskCacheSize(ParamsUtil.FIFTYMB)
                        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5 加密
                        .tasksProcessingOrder(QueueProcessingType.FIFO)
                        // .diskCacheFileCount(ParamsUtil.THREEHUBDRED)
                        // 缓存的文件数量
                        .imageDownloader(
                                new BaseImageDownloader(context, ParamsUtil.FIVETHOUSAND,
                                        ParamsUtil.THRRITGTHOUSAND)).defaultDisplayImageOptions(defaultOption);
        // 配置默认的option

        if (null != sdDir) {
            // 自定义缓存路径
            try {
                build.diskCache(new LruDiscCache(sdDir, null, new Md5FileNameGenerator(), 0,
                        ParamsUtil.THREEHUBDRED));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        ImageLoaderConfiguration config = build.build();// 开始构建
        ImageLoader.getInstance().init(config);
        L.disableLogging();
    }

    /**
     * startLocation:(开始定位的方法). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     */


    /**
     * getFolderSize:(获取文件夹大小). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size / 1048576;
    }

    public static MyAppliaction getmInstance() {
        return mInstance;
    }

    public static void setmInstance(MyAppliaction myInstance) {
        MyAppliaction.mInstance = myInstance;
    }

    /**
     * getDisplayMetrics:【获取手机屏幕分辨率】. <br/>
     * .@return.<br/>
     */
    public static DisplayMetrics getDisplayMetrics() {
        return dm;
    }

    /**
     * 报表路劲
     * @return
     */
    public static String getReportPath() {

        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); //
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String path = "";
        if (sdDir != null) {
            path = sdDir.toString() + "/";
        } else {
            path = "/";
        }

        String basePath = path + "DeYacache/report/";
        File sdDir1 = new File(basePath);
        if (!sdDir1.isDirectory()) {
            sdDir1.mkdirs();
        }
        return basePath;
    }

    public static Tools getTools(){
        return tools;
    }
    public static String getConstantValue(String key) {
        Tools tools = new Tools(getContext(), Constants.AC);
        return tools.getValue(key);
    }


    /**
     * 初始化DbUtils
     */
    static boolean needUpdate;
    public static DbUtils getDbUtils(final Context context) {
       List<TaskVo> historyList = null;
        if (db == null) {
            Log.e("11111111111","db创建了");
            historyList=new ArrayList<>();
            tools=new Tools(context, Constants.AC);
            int lastVersion=tools.getValue_int(Constants.LAST_DB_VERSION);
            if(lastVersion<Constants.DBVERSION){
                historyList.addAll(getHistoryDb(context, lastVersion));
            }
            db = DbUtils.create(context, WebUrl.APP_CODE+Constants.DBVERSION+".db", Constants.DBVERSION,
                    new DbUtils.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbUtils arg0, int arg1, int arg2) {
                            // TODO Auto-generated method stub
                            if (arg1 < arg2) {
                                Log.i("dbversion", arg1 + "--------------"
                                        + arg2);
                                needUpdate = true;

                            }

                        }
                    });

            try {

                db.saveAll(historyList);
                Log.i("dbversion",historyList.size()+"size");
                tools.putValue(Constants.LAST_DB_VERSION, Constants.DBVERSION);
            } catch (DbException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return db;
    }

    public static List<TaskVo> getHistoryDb(Context context, int arg2) {
        List<TaskVo> dbaskList = new ArrayList<TaskVo>();
        DbUtils db2;

            db2 = DbUtils.create(context, WebUrl.APP_CODE+arg2+".db", arg2,
                    new DbUtils.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbUtils arg0, int arg1, int arg2) {

                        }
                    });
        try {
            Log.i("dbversionlast", (null == db2)+"");
            if (null != db2) {

                dbaskList.addAll(db2.findAll(TaskVo.class));
                db2.dropDb();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dbaskList;
    }
}
