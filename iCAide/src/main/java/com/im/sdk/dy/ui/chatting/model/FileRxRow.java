package com.im.sdk.dy.ui.chatting.model;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.deya.acaide.R;
import com.im.sdk.dy.common.utils.DemoUtils;
import com.im.sdk.dy.common.utils.FileAccessor;
import com.im.sdk.dy.common.utils.FileUtils;
import com.im.sdk.dy.storage.IMessageSqlManager;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.holder.BaseHolder;
import com.im.sdk.dy.ui.chatting.holder.FileRowViewHolder;
import com.im.sdk.dy.ui.chatting.view.ChattingItemContainer;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.ecsdk.im.ECVideoMessageBody;
public class FileRxRow extends BaseChattingRow {

	public FileRxRow(int type) {
		super(type);
	}

	@Override
	public View buildChatView(LayoutInflater inflater, View convertView) {
		// we have a don't have a converView so we'll have to create a new one
		if (convertView == null || convertView.getTag() == null) {
			convertView = new ChattingItemContainer(inflater,
					R.layout.im_chatting_item_file_from);

			// use the view holder pattern to save of already looked up subviews
			FileRowViewHolder holder = new FileRowViewHolder(mRowType);
			convertView.setTag(holder.initBaseHolder(convertView, true));
		}
		return convertView;
	}

	@Override
	public void buildChattingData(final Context context, BaseHolder baseHolder,
			ECMessage detail, int position) {
		FileRowViewHolder holder = (FileRowViewHolder) baseHolder;
		OnClickListener onClickListener = ((ChattingActivity) context).mChattingFragment
				.getChattingAdapter().getOnClickListener();
		ViewHolderTag holderTag = ViewHolderTag.createTag(detail,
				ViewHolderTag.TagType.TAG_VIEW_FILE, position);
		if (detail != null) {
			ECMessage msg = detail;
			ECFileMessageBody body = (ECFileMessageBody) msg.getBody();
			holder.contentTv.setText(body.getFileName());
			String fileName = "";
			String userData = msg.getUserData();

			if (TextUtils.isEmpty(userData)) {
			} else {
				fileName = userData.substring(userData.indexOf("fileName=")
						+ "fileName=".length(), userData.length());

			}
			if ("mp4".equals(FileUtils.getFileExt(fileName))) {

				ECVideoMessageBody videoBody = (ECVideoMessageBody) msg
						.getBody();
				holder.contentTv.setVisibility(View.GONE);
				holder.contentTv.setTag(null);
				holder.contentTv.setOnClickListener(null);
				holder.fl.setVisibility(View.VISIBLE);

				holder.ivVideoMp4.setVisibility(View.VISIBLE);
				holder.buPlayVideo.setOnClickListener(onClickListener);
				holder.buPlayVideo.setTag(holderTag);

				holder.tvFile.setVisibility(View.VISIBLE);

				String text = IMessageSqlManager.qureyVideoMsgLength(msg
						.getMsgId());

				if (!TextUtils.isEmpty(text)) {
					holder.tvFile.setText(DemoUtils.bytes2kb(Long.parseLong(text)));
				}
				File file = new File(FileAccessor.getFilePathName(),
						body.getFileName() + "_thum.png");

				if (file.exists()) {
					Bitmap thumbBitmap = DemoUtils.getSuitableBitmap(file.getAbsolutePath());
					holder.ivVideoMp4.setImageBitmap(thumbBitmap);
				}

			} else {
				holder.contentTv.setVisibility(View.VISIBLE);
				holder.ivVideoMp4.setVisibility(View.GONE);
				holder.fl.setVisibility(View.GONE);
				holder.buPlayVideo.setTag(null);
				holder.buPlayVideo.setOnClickListener(null);
				holder.contentTv.setTag(holderTag);
				holder.contentTv.setOnClickListener(onClickListener);
				holder.tvFile.setVisibility(View.GONE);
			}
			
			holder.contentTv.setOnLongClickListener(onLongClickListener);

			getMsgStateResId(position, holder, detail, onClickListener);

		}
	}

	@Override
	public int getChatViewType() {
		return ChattingRowType.FILE_ROW_RECEIVED.ordinal();
	}

	@Override
	public boolean onCreateRowContextMenu(ContextMenu contextMenu,
			View targetView, ECMessage detail) {
		// TODO Auto-generated method stub
		return false;
	}

}
