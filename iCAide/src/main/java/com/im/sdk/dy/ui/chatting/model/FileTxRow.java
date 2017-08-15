package com.im.sdk.dy.ui.chatting.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.deya.acaide.R;
import com.im.sdk.dy.common.utils.FileUtils;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.holder.BaseHolder;
import com.im.sdk.dy.ui.chatting.holder.FileRowViewHolder;
import com.im.sdk.dy.ui.chatting.view.ChattingItemContainer;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.ecsdk.im.ECVideoMessageBody;

/**
 * @author Jorstin Chan
 * @date 2014-4-17
 * @version 1.0
 */
public class FileTxRow extends BaseChattingRow {

	public FileTxRow(int type) {
		super(type);
	}

	@Override
	public View buildChatView(LayoutInflater inflater, View convertView) {
		// we have a don't have a converView so we'll have to create a new one
		if (convertView == null || convertView.getTag() == null) {
			convertView = new ChattingItemContainer(inflater,
					R.layout.im_chatting_item_file_to);

			// use the view holder pattern to save of already looked up subviews
			FileRowViewHolder holder = new FileRowViewHolder(mRowType);
			convertView.setTag(holder.initBaseHolder(convertView, false));
		}
		return convertView;
	}

	@Override
	public void buildChattingData(Context context, BaseHolder baseHolder,
			ECMessage detail, int position) {

		FileRowViewHolder holder = (FileRowViewHolder) baseHolder;
		ViewHolderTag holderTag = ViewHolderTag.createTag(detail,
				ViewHolderTag.TagType.TAG_VIEW_FILE, position);
		OnClickListener onClickListener = ((ChattingActivity) context).mChattingFragment
				.getChattingAdapter().getOnClickListener();
		if (detail != null) {
			ECMessage message = detail;
			ECFileMessageBody fileBody = (ECFileMessageBody) message.getBody();
			String localPath = fileBody.getLocalUrl();
			String fileName = "";

			if (message.getType() == com.yuntongxun.ecsdk.ECMessage.Type.FILE) {
				fileName = fileBody.getFileName();
				holder.contentTv.setText(fileName);
			}else if (message.getType() == com.yuntongxun.ecsdk.ECMessage.Type.VIDEO) {
				ECVideoMessageBody videoFileBody = (ECVideoMessageBody) message
						.getBody();
				String videoPath = videoFileBody.getLocalUrl();

				fileName = videoPath;
			}
		
			if ("mp4".equals(FileUtils.getFileExt(fileName))) {
				holder.contentTv.setVisibility(View.GONE);
				holder.contentTv.setTag(null);
				holder.contentTv.setOnClickListener(null);
				holder.fl.setVisibility(View.VISIBLE);

				holder.ivVideoMp4.setVisibility(View.VISIBLE);
				holder.buPlayVideo.setOnClickListener(onClickListener);
				holder.buPlayVideo.setTag(holderTag);
				
				
				Bitmap createVideoThumbnail = FileUtils
						.createVideoThumbnail(fileBody.getLocalUrl());
				if (createVideoThumbnail != null) {
					holder.ivVideoMp4.setImageBitmap(createVideoThumbnail);

				}
			} else {
				holder.contentTv.setVisibility(View.VISIBLE);
				holder.ivVideoMp4.setVisibility(View.GONE);
				holder.fl.setVisibility(View.GONE);
				holder.buPlayVideo.setTag(null);
				holder.buPlayVideo.setOnClickListener(null);
				holder.contentTv.setTag(holderTag);
				holder.contentTv.setOnClickListener(onClickListener);
			}
			holder.contentTv.setOnLongClickListener(onLongClickListener);
			getMsgStateResId(position, holder, detail, onClickListener);
		}
	}

	@Override
	public int getChatViewType() {

		return ChattingRowType.FILE_ROW_TRANSMIT.ordinal();
	}

	@Override
	public boolean onCreateRowContextMenu(ContextMenu contextMenu,
			View targetView, ECMessage detail) {
		// TODO Auto-generated method stub
		return false;
	}

}
