package com.im.sdk.dy.ui.chatting.model;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.deya.acaide.R;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.holder.BaseHolder;
import com.im.sdk.dy.ui.chatting.holder.DescriptionViewHolder;
import com.im.sdk.dy.ui.chatting.view.ChattingItemContainer;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

public class ShareRow extends BaseChattingRow{
	int contactType;

	public ShareRow(int type){
		super(type);
		this.contactType=contactType;
	}
	
	/* (non-Javadoc)
	 * @see com.hisun.cas.model.im.ChattingRow#buildChatView(android.view.LayoutInflater, android.view.View)
	 */
	@Override
	public View buildChatView(LayoutInflater inflater, View convertView) {
        //we have a don't have a converView so we'll have to create a new one
        if (convertView == null || ((BaseHolder)convertView.getTag()).getType() != mRowType) {
        	
        	convertView = new ChattingItemContainer(inflater, R.layout.im_chatting_item_to);

            //use the view holder pattern to save of already looked up subviews
        	DescriptionViewHolder holder = new DescriptionViewHolder(mRowType,contactType);
        	convertView.setTag(holder.initBaseHolder(convertView, false));
        }
		return convertView;
	}

	@Override
	public void buildChattingData(Context context, BaseHolder baseHolder,
			ECMessage msg, int position) {
		DescriptionViewHolder holder = (DescriptionViewHolder) baseHolder;
		if(msg != null) {
			ECTextMessageBody textBody = (ECTextMessageBody) msg.getBody();
			holder.getDescTextView().setText(textBody.getMessage());
			holder.getDescTextView().setMovementMethod(LinkMovementMethod.getInstance());
			OnClickListener onClickListener = ((ChattingActivity) context).mChattingFragment.getChattingAdapter().getOnClickListener();
			getMsgStateResId(position, holder, msg, onClickListener);

           // ((ChattingActivity) context).registerForContextMenu(holder.getDescTextView());
		}
	}


	@Override
	public int getChatViewType() {
		return ChattingRowType.DESCRIPTION_ROW_TRANSMIT.ordinal();
	}
	
	@Override
	public boolean onCreateRowContextMenu(ContextMenu contextMenu,
			View targetView, ECMessage detail) {
		
		return false;
	}
	

}
