package com.deya.hospital.util;

import java.io.File;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.webkit.MimeTypeMap;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.deya.acaide.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 
 * @description 下载工具类
 * @version 1.0
 * @author 孙鹏
 * @date 2015-6-21 下午4:24:53
 */
@SuppressLint("NewApi")
public class FileDownLoad {

  public static final String DOWNLOAD_FOLDER_NAME = "Deya/pdfFile";
  public String fileName = "";
  public static final String DOWNLOAD_ID = "apkDownloadId";

  private Context context;
  private String url;
  private String notificationTitle;
  private String notificationDescription;

  private DownloadManager downloadManager;
  public static final String TAG = "MainActivity";

  private NotificationManager manager;

  private Notification notification;

  private RemoteViews contentView;

  private long progress;

  private Message msg;

  private Handler mHandler = new Handler() {// 更改进度条的进度
        @Override
        public void handleMessage(Message msg) {
          contentView.setProgressBar(R.id.pb, 100, (int) progress, false);
          contentView.setTextViewText(R.id.tx_title, notificationTitle + "\u3000\u3000"
              + notificationDescription);
          contentView.setTextViewText(R.id.tx_percent, progress + "%");
          notification.contentView = contentView;
          manager.notify(0, notification);
          super.handleMessage(msg);
        };
      };

  /**
   * @param context
   * @param url 下载apk的url
   * @param notificationTitle 通知栏标题
   * @param notificationDescription 通知栏描述
   */
  public FileDownLoad(final Context context, String url, String notificationTitle,
      String notificationDescription) {
    super();
    if (getAvailableExternalMemorySize() > 30) {
      this.context = context;
      this.url = url;
      this.notificationTitle = notificationTitle;
      this.notificationDescription = notificationDescription;
      File file = new File(getPath());
      if (file.exists()) {
        file.delete();
      }
      try {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        execute();
      } catch (Exception ex) {
        ex.printStackTrace();
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification(R.drawable.share_logo, "开始下载", System.currentTimeMillis());

        HttpUtils http = new HttpUtils();
        if (!isExternalStorageAvaliable()) {
          return;
        }
        String apkPath = getPath();
        File f = new File(apkPath);
        if (f.exists()) {
          f.delete();
        }
        HttpHandler handler = http.download(url, apkPath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
            true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
            new RequestCallBack<File>() {
              @Override
              public void onStart() {
                sendNotification();
              }

              @Override
              public void onLoading(long total, long current, boolean isUploading) {
                if (current != total && current != 0) {
                  progress = (int) (current / (float) total * 100);
                } else {
                  progress = 100;
                }
                mHandler.handleMessage(msg);
              }

              @Override
              public void onSuccess(ResponseInfo<File> responseInfo) {
                manager.cancel(0);
                String apkFilePath = getPath();
              //  install(context, apkFilePath);
                ToastUtils.showToast(context, apkFilePath+"");
              }

              @Override
              public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "文件下载失败", Toast.LENGTH_SHORT).show();
              }
            });
      }
    } else {
      Toast.makeText(context, "设备剩余存储内存不足，请删除部分无用文件后继续。", Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * 
   * @return 内置SDCard剩余存储空间MB数
   */
  private float getAvailableExternalMemorySize() {
    StatFs stat = new StatFs(getSDPath().getAbsolutePath());
    return calculateSizeInMB(stat);
  }

  /**
   * 
   * @param stat 文件StatFs对象
   * @return 剩余存储空间的MB数
   * 
   */
  private float calculateSizeInMB(StatFs stat) {
    if (stat != null) {
      return stat.getAvailableBlocks() * (stat.getBlockSize() / (1024f * 1024f));
    }
    return 0.0f;
  }

  /**
   * @description 获取内存卡路径，设置图片的本地缓存路径
   * @version 1.0
   * @author 邓浩然
   * @date 2014年9月15日 下午4:20:48
   * @return
   * @return File
   * @throws
   */
  private static File getSDPath() {
    File sdDir = null;
    boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
    if (sdCardExist) {
      sdDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
    }
    return sdDir;
  }

  private String getPath() {
    // TODO Auto-generated method stub
    boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
    if (sdCardExist) {
      return new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
          .append(File.separator).append(DOWNLOAD_FOLDER_NAME).append(File.separator)
          .append(fileName).toString();
    }
    return "";
  }

  public boolean checkDownloadprovider(String packageName) {
    if (packageName == null || "".equals(packageName))
      return false;
    try {
      ApplicationInfo info =
          context.getPackageManager().getApplicationInfo(packageName,
              PackageManager.GET_UNINSTALLED_PACKAGES);
      return true;
    } catch (NameNotFoundException e) {
      return false;
    }
  }

  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
  public void execute() {
    // 清除已下载的内容重新下载
    long downloadId = PreferencesUtils.getLong(context, DOWNLOAD_ID);
    if (downloadId != -1) {
      downloadManager.remove(downloadId);
      PreferencesUtils.removeSharedPreferenceByKey(context, DOWNLOAD_ID);
    }
    Request request = new Request(Uri.parse(url));
    // 设置Notification中显示的文字
    request.setTitle(notificationTitle);
    request.setDescription(notificationDescription);
    // 设置可用的网络类型
    request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
    // 设置状态栏中显示Notification
    if (11 <= getAndroidSDKVersion()) {
      request
          .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    }
    // 不显示下载界面
    request.setVisibleInDownloadsUi(false);
    // 设置下载后文件存放的位置
    File folder = Environment.getExternalStoragePublicDirectory(DOWNLOAD_FOLDER_NAME);
    if (!folder.exists() || !folder.isDirectory()) {
      folder.mkdirs();
    }
    request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME, fileName);
    // 设置文件类型
    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
    String mimeString =
        mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
    request.setMimeType(mimeString);
    // 保存返回唯一的downloadId
    PreferencesUtils.putLong(context, DOWNLOAD_ID, downloadManager.enqueue(request));
  }

  public int getAndroidSDKVersion() {
    int version = 0;
    try {
      version = Integer.valueOf(VERSION.SDK);
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    return version;
  }

 
  /**
   * 判断SD卡是否可用
   * 
   * @return
   */
  public boolean isExternalStorageAvaliable() {

    String state = Environment.getExternalStorageState();

    if (Environment.MEDIA_MOUNTED.equals(state)) {
      return true;
    } else {
      Toast.makeText(context, "未检测到SD卡...", Toast.LENGTH_SHORT).show();
      return false;
    }

  }

  /**
   * 发送通知
   */
  public void sendNotification() {
    contentView = new RemoteViews(context.getPackageName(), R.layout.layout_updatenotification);
    contentView.setProgressBar(R.id.pb, 100, 0, false);
    notification.contentView = contentView;
    manager.notify(0, notification);
  }

  /** 查询下载状态 */
  public static int queryDownloadStatus(DownloadManager downloadManager, long downloadId) {
    int result = -1;
    DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
    Cursor c = null;
    try {
      c = downloadManager.query(query);
      if (c != null && c.moveToFirst()) {
        result = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
      }
    } finally {
      if (c != null) {
        c.close();
      }
    }
    return result;
  }

}
