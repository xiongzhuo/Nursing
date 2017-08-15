package com.im.sdk.dy.ui.chatting.model;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.WebViewDemo;
import com.im.sdk.dy.core.DyMessage;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.holder.BaseHolder;
import com.im.sdk.dy.ui.chatting.holder.DescriptionViewHolder;
import com.im.sdk.dy.ui.chatting.view.ChattingItemContainer;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

import org.json.JSONException;
import org.json.JSONObject;

public class DescriptionTxRow extends BaseChattingRow {
    int contactType;

    public DescriptionTxRow(int type) {
        super(type);
        this.contactType = contactType;
    }

    /* (non-Javadoc)
     * @see com.hisun.cas.model.im.ChattingRow#buildChatView(android.view.LayoutInflater, android.view.View)
     */
    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        //we have a don't have a converView so we'll have to create a new one
        if (convertView == null || ((BaseHolder) convertView.getTag()).getType() != mRowType) {

            convertView = new ChattingItemContainer(inflater, R.layout.im_chatting_item_to);

            //use the view holder pattern to save of already looked up subviews
            DescriptionViewHolder holder = new DescriptionViewHolder(mRowType, contactType);
            convertView.setTag(holder.initBaseHolder(convertView, false));
        }
        return convertView;
    }

    @Override
    public void buildChattingData(final Context context, BaseHolder baseHolder,
                                  ECMessage message, int position) {
        final DescriptionViewHolder holder = (DescriptionViewHolder) baseHolder;
        if (message != null) {
            final ECTextMessageBody textBody = (ECTextMessageBody) message.getBody();
            final DyMessage dyMessage = new DyMessage(message.getUserData());
            if (null == dyMessage) {
                holder.setnormalRow();
                return;
            }
            OnClickListener onClickListener = ((ChattingActivity) context).mChattingFragment.getChattingAdapter().getOnClickListener();
            getMsgStateResId(position, holder, message, onClickListener);
            holder.getDescTextView().setText(textBody.getMessage());
            holder.getDescTextView().setMovementMethod(LinkMovementMethod.getInstance());
            if (null == dyMessage.getValue()) {
                holder.setnormalRow();
                return;
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(dyMessage.getValue());
                Log.e("111111",jsonObject.toString());
                JSONObject data = jsonObject.optJSONObject("data");
                int p = jsonObject.has("p") ? jsonObject.optInt("p") : 0;
                if (p == 888) {
                    holder.setShareRow();
                    RelativeLayout shareLay = holder.getShareView();
                    final String url = jsonObject.optString("url");
                    final String content=jsonObject.optString("text");
                    TextView shareContent = (TextView) shareLay.findViewById(R.id.shareContent);
                    shareContent.setText(content);
                    TextView titleTV = (TextView) shareLay.findViewById(R.id.shareTitle);
                    titleTV.setText(textBody.getMessage());
                    shareLay.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent in = new Intent(context, WebViewDemo.class);
                            in.putExtra("url", url);
                            in.putExtra("title", textBody.getMessage());
                            context.startActivity(in);
                        }
                    });
                }else{
                    holder.setnormalRow();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            holder.setnormalRow();
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
