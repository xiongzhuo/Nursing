package com.deya.hospital.util;

import java.io.File;

import android.os.Environment;

public class FileUtils {
	public static final String DOWNLOAD_FOLDER_NAME = "Deya/pdf";
	public static String getPath() {
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
}
