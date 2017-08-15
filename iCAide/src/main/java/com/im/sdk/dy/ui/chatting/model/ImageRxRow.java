/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.im.sdk.dy.ui.chatting.model;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.deya.acaide.R;
import com.im.sdk.dy.common.utils.DemoUtils;
import com.im.sdk.dy.common.utils.FileAccessor;
import com.im.sdk.dy.common.utils.ResourceHelper;
import com.im.sdk.dy.storage.IMessageSqlManager;
import com.im.sdk.dy.storage.ImgInfoSqlManager;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.holder.BaseHolder;
import com.im.sdk.dy.ui.chatting.holder.ImageRowViewHolder;
import com.im.sdk.dy.ui.chatting.view.ChattingItemContainer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.yuntongxun.ecsdk.ECMessage;

/**
 * <p>
 * Title: ImageRxRow.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Beijing Speedtong Information Technology Co.,Ltd
 * </p>
 * 
 * @author Jorstin Chan
 * @date 2014-4-17
 * @version 1.0
 */
public class ImageRxRow extends BaseChattingRow {

	public ImageRxRow(int type) {
		super(type);
	}

	@Override
	public View buildChatView(LayoutInflater inflater, View convertView) {

		// we have a don't have a converView so we'll have to create a new one
		if (convertView == null) {
			convertView = new ChattingItemContainer(inflater,
					R.layout.im_chatting_item_from_picture);

			// use the view holder pattern to save of already looked up subviews
			ImageRowViewHolder holder = new ImageRowViewHolder(mRowType);
			convertView.setTag(holder.initBaseHolder(convertView, true));

		}
		// actually setup the view
		return convertView;
	}

	@Override
	public void buildChattingData(final Context context, BaseHolder baseHolder,
			ECMessage detail, int position) {

		String isRead = IMessageSqlManager.getMsgReadStatus(detail.getMsgId());

		boolean isFireMsg = IMessageSqlManager.isFireMsg(detail.getMsgId());

		final ImageRowViewHolder holder = (ImageRowViewHolder) baseHolder;
		String userData = detail.getUserData();
		ViewHolderTag holderTag = ViewHolderTag.createTag(detail,
				ViewHolderTag.TagType.TAG_VIEW_PICTURE, position);
		View.OnClickListener onClickListener = ((ChattingActivity) context).mChattingFragment
				.getChattingAdapter().getOnClickListener();
		holder.chattingContentIv.setTag(holderTag);
		holder.chattingContentIv.setOnClickListener(onClickListener);
		
		holder.chattingContentIv.setOnLongClickListener(onLongClickListener);
		
		if (isFireMsg && "1".equals(isRead)) {
			holder.chattingContentIv.setOnClickListener(null);
			holder.chattingContentIv.setOnLongClickListener(null);
			
		}
		
		holder.chattingContentIv.setImageResource(0);
		holder.chattingContentIv.invalidate();

		if (TextUtils.isEmpty(userData)) {
			return;
		}
		int start = userData.indexOf("THUMBNAIL://");
		int gif = userData.indexOf(",PICGIF://");
		boolean isGif = userData.contains("PICGIF://true");
		if (start != -1) {
			String thumbnail;
			if (gif == -1) {
				thumbnail = userData.substring(start);
			} else {
				thumbnail = userData.substring(start, gif);
			}
			Bitmap thumbBitmap = ImgInfoSqlManager.getInstance()
					.getThumbBitmap(thumbnail, 2);
			final ImgInfo imgInfo = ImgInfoSqlManager.getInstance().getImgInfo(
					detail.getMsgId());
			DisplayImageOptions.Builder optionsBuilder = DemoUtils
					.getChatDisplayImageOptionsBuilder();
			optionsBuilder.showImageOnLoading(new BitmapDrawable(thumbBitmap));
			if (imgInfo != null && !TextUtils.isEmpty(imgInfo.getBigImgPath())) {
				if (imgInfo.getBigImgPath().startsWith("http:")) {
					if (isFireMsg) {
						if ("1".equals(isRead)) {
//							holder.chattingContentIv.setImageResource(R.drawable.msg_fire_readed);
							ImageLoader.getInstance().displayImage("assets://msg_fire_readed.png", holder.chattingContentIv);
							
						} else {
							ImageLoader.getInstance().displayImage("assets://msg_fire_unread.png", holder.chattingContentIv);
//							holder.chattingContentIv.setImageResource(R.drawable.msg_fire_unread);
						}
					} else {
						ImageLoader.getInstance().displayImage(
								imgInfo.getBigImgPath(),
								holder.chattingContentIv,
								optionsBuilder.build(),
								new SimpleImageLoadingListener() {
									@Override
									public void onLoadingComplete(
											String imageUri, View view,
											Bitmap loadedImage) {
										super.onLoadingComplete(imageUri, view,
												loadedImage);
										if (imageUri.startsWith("http:")) {
											File imgCacheFile = DiskCacheUtils
													.findInCache(
															imageUri,
															ImageLoader
																	.getInstance()
																	.getDiskCache());
											if (imgCacheFile != null) {
												imgInfo.setBigImgPath(imgCacheFile
														.getAbsolutePath()
														.substring(
																imgCacheFile
																		.getAbsolutePath()
																		.lastIndexOf(
																				"/")));
												ImgInfoSqlManager.getInstance()
														.updateImageInfo(
																imgInfo);
											}
										}
									}
								});
					}

				} else {
//					if (isFireMsg) {
//						if ("1".equals(isRead)) {
////							holder.chattingContentIv.setImageResource(R.drawable.msg_fire_readed);
////							ImageLoader.getInstance().displayImage("", holder.chattingContentIv);
//							ImageLoader.getInstance().displayImage("assets://msg_fire_readed.png", holder.chattingContentIv);
//						} else {
////							holder.chattingContentIv.setImageResource(R.drawable.msg_fire_unread);
//							ImageLoader.getInstance().displayImage("assets://msg_fire_unreaded.png", holder.chattingContentIv);
//						}
//					} else {
						ImageLoader.getInstance().displayImage(
								"file://" + FileAccessor.getImagePathName()
										+ "/" + imgInfo.getBigImgPath(),
								holder.chattingContentIv,
								optionsBuilder.build());
						
//					}
				}
			}
		}
		
		
		
		

		if (isGif) {
			boolean showGif = detail.getMsgStatus() == ECMessage.MessageStatus.SUCCESS
					|| detail.getMsgStatus() == ECMessage.MessageStatus.RECEIVE;
			if (holder.mGifIcon != null) {
				holder.mGifIcon.setVisibility(!showGif ? View.GONE
						: View.VISIBLE);
			}
		} else {
			holder.mGifIcon.setVisibility(View.GONE);
		}
		int startWidth = userData.indexOf("outWidth://");
		int startHeight = userData.indexOf(",outHeight://");
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.chattingContentIv
				.getLayoutParams();
		if (startWidth != -1 && startHeight != -1 && start != -1) {
			int imageMinWidth = ResourceHelper.fromDPToPix(context, isGif ? 200
					: 100);
			int width = DemoUtils.getInt(userData.substring(startWidth
					+ "outWidth://".length(), startHeight), imageMinWidth);
			int height = DemoUtils.getInt(userData.substring(startHeight
					+ ",outHeight://".length(), start - 1), imageMinWidth);
			holder.chattingContentIv.setMinimumWidth(imageMinWidth);
			params.width = imageMinWidth;
			int _height = height * imageMinWidth / width;
			if (_height > ResourceHelper.fromDPToPix(context, 230)) {
				_height = ResourceHelper.fromDPToPix(context, 230);
				holder.chattingContentIv
						.setScaleType(ImageView.ScaleType.CENTER_CROP);
			}
			if (width != 0) {
				holder.chattingContentIv.setMinimumHeight(_height);
				params.height = _height;
			} else {
				holder.chattingContentIv.setMinimumHeight(imageMinWidth);
				params.height = imageMinWidth;
			}
			
			holder.chattingContentIv.setLayoutParams(params);
		}

	}

	@Override
	public int getChatViewType() {

		return ChattingRowType.IMAGE_ROW_RECEIVED.ordinal();
	}

	@Override
	public boolean onCreateRowContextMenu(ContextMenu contextMenu,
			View targetView, ECMessage detail) {
		// TODO Auto-generated method stub
		return false;
	}

}
