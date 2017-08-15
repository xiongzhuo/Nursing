package com.deya.hospital.util;

import android.widget.ImageView;

/**
 * 
 * @author sunpeng
 *
 */
public interface FileActions {
	public void onDeletFile(int position);//删除
	public void onDeletMedia(int position);
	public void onPlayMedia(String fileName,ImageView view);
	public void onStopMedia(int position);

}
