/**
 * Project Name:Vicinity3.1.<br/>
 * File Name:CompressImageUtil.java.<br/>
 * Package Name:com.trisun.vicinity.util.<br/>
 * Date:2015年4月27日下午5:50:05.<br/>
 * Copyright (c) 2015, 广东云上城网络科技有限公司.
 *
 */

package com.deya.hospital.base.img;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ThreadPoolUtil;

/**
 * .ClassName: CompressImageUtil(用一句话描述这个类的作用是做什么) <br/>
 * date: 2015年4月27日 下午5:50:05 <br/>
 * 
 * @author Administrator
 */
public class CompressImageUtil {
	// 注意，这里没有final
	private static CompressImageUtil single;

	public static CompressImageUtil getCompressImageUtilInstance() {
		if (null == single) {
			single = new CompressImageUtil();
		}
		return single;
	}

	/**
	 * startCompressImage:【这里用一句话描述这个方法的作用】. <br/>
	 * ..<br/>
	 */
	public void startCompressImage(final MyHandler handler, final int what,
			final String file) {

		try {
			File f1 = new File(file);
			if (f1.length() < 300 * 1024) {
				Message msg = new Message();
				msg.what = what;
				msg.obj = f1;
				handler.sendMessage(msg);
			} else {
				String newPath = getSDPath()
						+ file.substring(file.lastIndexOf("/"), file.length());
				File f = new File(newPath);
				copyFile(file, newPath);
				Bitmap bm = PictureUtil.getSmallBitmap(newPath);
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(f));
				bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				bos.close();
				Message msg = new Message();
				msg.what = what;
				msg.obj = newPath;
				handler.sendMessage(msg);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * startCompressImages:【同时压缩多张图片,接收消息时注意判断张数】. <br/>
	 * .@param handler .@param what .@param files.<br/>
	 */
	public void startCompressImages(final MyHandler handler, final int what,
			final List<String> files) {
		for (String path : files) {// 这里的线程池为单行,不用考虑错序
			final String file = path;

			ThreadPoolUtil.getInstant().execute(new Runnable() {
				@Override
				public void run() {
					try {
						String newPath = getSDPath()
								+ file.substring(file.lastIndexOf("/"),
										file.length());
						copyFile(file, newPath);
						File f = new File(newPath);
						Bitmap bm = PictureUtil.getSmallBitmap(newPath);
						BufferedOutputStream bos = new BufferedOutputStream(
								new FileOutputStream(f));
						bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
						bos.flush();
						bos.close();
						Message msg = new Message();
						msg.what = what;
						msg.obj = newPath;
						handler.sendMessage(msg);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {

			sdDir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/DeyaCache/");
			if (!sdDir.exists()) {
				sdDir.mkdirs();
				Log.i("1111", sdDir + "不存在");
			}
			Log.i("1111", sdDir + "");
		}
		return sdDir;
	}

}
