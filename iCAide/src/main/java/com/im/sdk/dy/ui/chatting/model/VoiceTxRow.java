package com.im.sdk.dy.ui.chatting.model;

import java.io.File;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.deya.acaide.R;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.holder.BaseHolder;
import com.im.sdk.dy.ui.chatting.holder.VoiceRowViewHolder;
import com.im.sdk.dy.ui.chatting.view.ChattingItemContainer;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;

public class VoiceTxRow extends BaseChattingRow {

    public VoiceTxRow(int type) {
        super(type);
    }

    /* (non-Javadoc)
     * @see com.hisun.cas.model.im.ChattingRow#buildChatView(android.view.LayoutInflater, android.view.View)
     */
    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        //we have a don't have a converView so we'll have to create a new one
        if (convertView == null) {
            convertView = new ChattingItemContainer(inflater, R.layout.im_chatting_item_to_voice);

            //use the view holder pattern to save of already looked up subviews
            VoiceRowViewHolder holder = new VoiceRowViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, false));
        }
        return convertView;
    }

    @Override
    public void buildChattingData(Context context, BaseHolder baseHolder,
                                  final ECMessage detail, int position) {

        final VoiceRowViewHolder holder = (VoiceRowViewHolder) baseHolder;
        holder.voiceAnim.setVoiceFrom(false);
        holder.voiceAnim.setOnLongClickListener(new LongClick(holder));
        holder.voicePlayAnim.setOnLongClickListener(new LongClick(holder));
        if(detail != null) {
            if(detail.getMsgStatus() == ECMessage.MessageStatus.SENDING) {
                holder.voiceSending.setVisibility(View.VISIBLE);
            } else {
                holder.voiceSending.setVisibility(View.GONE);
            }

            File file = new File(((ECFileMessageBody)detail.getBody()).getLocalUrl());
            long length = file.length();

            VoiceRowViewHolder.initVoiceRow(holder, detail, position, (ChattingActivity)context, false);
            OnClickListener onClickListener = ((ChattingActivity) context).mChattingFragment.getChattingAdapter().getOnClickListener();
            getMsgStateResId(position, holder, detail, onClickListener);
        }
    }
    
   private class LongClick implements OnLongClickListener{
	   private VoiceRowViewHolder holder;
	   public LongClick(VoiceRowViewHolder holder){
		   this.holder=holder;
	   }
	@Override
	public boolean onLongClick(View v) {
		 holder.voiceAnim.stopVoiceAnimation();
         holder.voiceAnim.setVisibility(View.GONE);
		return false;
	}
	   
   }
    
    @Override
    public int getChatViewType() {
        // return type
        return ChattingRowType.VOICE_ROW_TRANSMIT.ordinal();
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu,
                                          View targetView, ECMessage detail) {
        // TODO Auto-generated method stub
        return false;
    }

}
