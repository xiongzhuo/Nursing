package com.deya.hospital.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.deya.acaide.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * 
 * @description APK下载工具类
 * @version 1.0
 * @author 邓浩然
 * @date 2014-10-29 下午4:24:53
 */
public class ImageDownLoad {

	public static final String DOWNLOAD_FOLDER_NAME = "dymd";
	public String filename = "";
	public static final String APK_DOWNLOAD_ID = "apkDownloadId";

	private Context context;
	private String notificationTitle;
	private String notificationDescription;

	public static final String TAG = "MainActivity";
	private NotificationManager manager;
	private Notification notification;
	private RemoteViews contentView;
	private long progress;
	private Message msg;

	Handler mHandler = new Handler() {// 更改进度条的进度
		@Override
		public void handleMessage(Message msg) {
			contentView.setProgressBar(R.id.pb, 100, (int) progress, false);
			contentView.setTextViewText(R.id.tx_title, notificationTitle
					+ "\u3000\u3000" + notificationDescription);
			contentView.setTextViewText(R.id.tx_percent, progress + "%");
			notification.contentView = contentView;
			manager.notify(0, notification);
			super.handleMessage(msg);
		};
	};

	/**
	 * @param context
	 * @param url
	 *            下载apk的url
	 * @param notificationTitle
	 *            通知栏标题
	 * @param notificationDescription
	 *            通知栏描述
	 */
	public ImageDownLoad(final Context context, String url,
			String notificationTitle, String notificationDescription) {
		super();
		System.out.println(url);
		filename = url.substring(url.lastIndexOf("/"), url.length());
		this.context = context;
		this.notificationTitle = notificationTitle;
		this.notificationDescription = notificationDescription;
		try {
			manager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notification = new Notification(R.drawable.share_logo, "开始下载",
					System.currentTimeMillis());

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
						public void onLoading(long total, long current,
								boolean isUploading) {
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
							Toast.makeText(context,
									"图片保存于手机存储的根目录中的云上城的图片文件夹下",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(context, "文件下载失败",
									Toast.LENGTH_SHORT).show();
						}
					});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String getPath() {
		// TODO Auto-generated method stub
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			return new StringBuilder(Environment.getExternalStorageDirectory()
					.getAbsolutePath()).append(File.separator)
					.append(DOWNLOAD_FOLDER_NAME).append(File.separator)
					.append(filename).toString();
		}
		return "";
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
		contentView = new RemoteViews(context.getPackageName(),
				R.layout.layout_updatenotification);
		contentView.setProgressBar(R.id.pb, 100, 0, false);
		notification.contentView = contentView;
		manager.notify(0, notification);
	}
}
