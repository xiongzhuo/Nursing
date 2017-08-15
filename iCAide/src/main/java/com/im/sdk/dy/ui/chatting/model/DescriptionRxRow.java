package com.im.sdk.dy.ui.chatting.model;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.hospital.dypdf.PdfPreviewActivity;
import com.deya.acaide.R;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.workcircle.WebViewDtail;
import com.im.sdk.dy.core.DyMessage;
import com.im.sdk.dy.ui.chatting.holder.BaseHolder;
import com.im.sdk.dy.ui.chatting.holder.DescriptionViewHolder;
import com.im.sdk.dy.ui.chatting.view.ChattingItemContainer;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * <p>Title: DescriptionRxRow.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 * @author Jorstin Chan
 * @date 2014-4-17
 * @version 1.0
 */
public class DescriptionRxRow extends BaseChattingRow {
	int contactsType;
	private int drawables[] = {R.drawable.task_type_round_blue,
			R.drawable.task_type_round_blue, R.drawable.task_type_round_green,
			R.drawable.task_type_round_blue, R.drawable.task_type_round_green,
			R.drawable.task_type_round_green ,R.drawable.task_type_round_blue};
	private String Strings[] = {"改进列表提醒"};
	
	public DescriptionRxRow(int type,int contactsType){
		super(type);
		this.contactsType=contactsType;
	}
	
	@Override
	public View buildChatView(LayoutInflater inflater, View convertView) {
        //we have a don't have a converView so we'll have to create a new one
        if (convertView == null ) {
            convertView = new ChattingItemContainer(inflater, R.layout.im_chatting_item_from);

            
            //use the view holder pattern to save of already looked up subviews
            DescriptionViewHolder holder = new DescriptionViewHolder(mRowType,contactsType);
            convertView.setTag(holder.initBaseHolder(convertView, true));
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i("1111111","11111111");
				}
			});
        }
		return convertView;
	}

	@Override
	public void buildChattingData(final Context context, BaseHolder baseHolder,
			ECMessage message, int position) {
		
		DescriptionViewHolder holder = (DescriptionViewHolder) baseHolder;
		if(message != null) {
			final ECTextMessageBody textBody = (ECTextMessageBody) message.getBody();
			final DyMessage dyMessage=new DyMessage(message.getUserData());
			if(null==dyMessage){
				holder.setnormalRow();
				return;
			}
			holder.getDescTextView().setText(textBody.getMessage());
			if(null==dyMessage.getValue()){
				holder.setnormalRow();
				return;
			}
			setShare(context, holder, textBody, dyMessage);
			holder.getDescTextView().setMovementMethod(LinkMovementMethod.getInstance());

			holder.getDescTextView().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					JSONObject jsonObject= null;
					try {
						jsonObject = new JSONObject(dyMessage.getValue());
						JSONObject data =jsonObject.optJSONObject("data");
						if(data==null||!data.has("type")){
							return;
						}
						int type=data.optInt("type");
						int p=jsonObject.has("p")?jsonObject.optInt("p"):0;
						if(type==0&&p==8){
							int aticalId=jsonObject.optInt("id");
							String pdf_attach=data.optString("pdf_attach");
							if(data.optInt("is_pdf")==1){
								Intent in = new Intent(context, PdfPreviewActivity.class);
								in.putExtra("url", WebUrl.WEB_PDF+"?id="+aticalId+"&&pdfid="+pdf_attach);
								in.putExtra("articleid", aticalId+"");

								context.startActivity(in);
							}else{
								Intent in = new Intent(context, WebViewDtail.class);
								in.putExtra("id", aticalId+"");
								context.startActivity(in);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}else{
			holder.setnormalRow();
		}
	}

	private void setShare(final Context context, DescriptionViewHolder holder, final ECTextMessageBody textBody, DyMessage dyMessage) {
		JSONObject jsonObject = null;
		try {
            jsonObject = new JSONObject(dyMessage.getValue());
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
                shareLay.setOnClickListener(new View.OnClickListener() {
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
	}

	@Override
	public int getChatViewType() {

		return ChattingRowType.DESCRIPTION_ROW_RECEIVED.ordinal();
	}

	@Override
	public boolean onCreateRowContextMenu(ContextMenu contextMenu,
			View targetView, ECMessage detail) {

		return false;
	}
}
