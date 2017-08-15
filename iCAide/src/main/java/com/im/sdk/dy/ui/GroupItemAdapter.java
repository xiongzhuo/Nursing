package com.im.sdk.dy.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.DateUtil;
import com.im.sdk.dy.common.utils.DemoUtils;
import com.im.sdk.dy.common.utils.ResourceHelper;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.storage.ConversationSqlManager;
import com.im.sdk.dy.storage.GroupNoticeSqlManager;
import com.im.sdk.dy.ui.ConversationListFragment.GroupItem;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.im.sdk.dy.ui.chatting.model.Conversation;
import com.im.sdk.dy.ui.contact.ContactLogic;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.im.sdk.dy.ui.group.GroupNoticeHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yuntongxun.ecsdk.ECMessage;

public class GroupItemAdapter extends BaseAdapter {
	List<GroupItem> list;
	LayoutInflater infalater;
	private Context mContext;
	private DisplayImageOptions optionsSquare_group;
	ColorStateList[] colorStateLists;

	GroupItemAdapter(Context ctx, List<GroupItem> list) {
		this.list = list;
		this.mContext = ctx;
		infalater = LayoutInflater.from(ctx);
		optionsSquare_group = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.group_head)
				.showImageForEmptyUri(R.drawable.group_head)
				.showImageOnFail(R.drawable.group_head)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		colorStateLists = new ColorStateList[] {
				ResourceHelper
						.getColorStateList(ctx, R.color.normal_text_color),
				ResourceHelper.getColorStateList(ctx,
						R.color.ccp_list_textcolor_three) };
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notifyDataSetChanged() {
		getEditorTextViewUnSaveText();
		
		super.notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder mViewHolder;
		if (convertView == null || convertView.getTag() == null) {
			view = View.inflate(mContext, R.layout.im_conversation_item, null);

			mViewHolder = new ViewHolder();
			mViewHolder.user_avatar = (ImageView) view
					.findViewById(R.id.avatar_iv);
			mViewHolder.prospect_iv = (ImageView) view
					.findViewById(R.id.avatar_prospect_iv);
			mViewHolder.nickname_tv = (EmojiconTextView) view
					.findViewById(R.id.nickname_tv);
			mViewHolder.tipcnt_tv = (TextView) view
					.findViewById(R.id.tipcnt_tv);
			mViewHolder.update_time_tv = (TextView) view
					.findViewById(R.id.update_time_tv);
			mViewHolder.last_msg_tv = (EmojiconTextView) view
					.findViewById(R.id.last_msg_tv);
			mViewHolder.image_input_text = (ImageView) view
					.findViewById(R.id.image_input_text);
			mViewHolder.image_mute = (ImageView) view
					.findViewById(R.id.image_mute);
			mViewHolder.drafTv = (TextView) view.findViewById(R.id.drafTv);
			view.setTag(mViewHolder);
		} else {
			view = convertView;
			mViewHolder = (ViewHolder) view.getTag();
		}
		GroupItem item = (GroupItem) getItem(position);
		mViewHolder.nickname_tv.setText(list.get(position).getGroup_name());
		ImageLoader.getInstance().displayImage("", mViewHolder.user_avatar,
				optionsSquare_group);
//		long mThread = ConversationSqlManager
//				.querySessionIdForBySessionId(mRecipients);
		Conversation conversation = new Conversation();
		Cursor curr=getCursor(item.getGroup_id());
		
		if(null!=curr){
		conversation.setCursor(curr);
	
		
		setConversationUnread(mViewHolder, conversation);
		
		if (!AbStrUtil.isEmpty(getConversationDraft(conversation))) {
			mViewHolder.last_msg_tv.setText(getConversationDraft(conversation));
			mViewHolder.drafTv.setVisibility(View.VISIBLE);
		} else {
			mViewHolder.drafTv.setVisibility(View.GONE);
			mViewHolder.last_msg_tv
					.setText(getConversationSnippet(conversation));
		}
		
		mViewHolder.last_msg_tv.setCompoundDrawables(
				getChattingSnippentCompoundDrawables(mContext, conversation),
				null, null, null);
		// 未读提醒设置
		mViewHolder.image_input_text.setVisibility(View.GONE);
		mViewHolder.update_time_tv.setText(getConversationTime(conversation));
		}
		mViewHolder.image_mute
		.setVisibility(isNotice(conversation) ? View.GONE
				: View.VISIBLE);
		return view;
	}

	static class ViewHolder {
		ImageView user_avatar;
		TextView tipcnt_tv;
		ImageView prospect_iv;
		EmojiconTextView nickname_tv;
		TextView update_time_tv;
		EmojiconTextView last_msg_tv;
		ImageView image_input_text;
		ImageView image_mute;
		TextView drafTv;
	}

	public Cursor getCursor(String id) {
		Cursor curr = ConversationSqlManager.getConversationCursorById(id);
		if(curr != null && curr.moveToFirst()){
			return curr;
		}
		return null;
	}

	/**
	 * 会话时间
	 * 
	 * @param conversation
	 * @return
	 */
	protected final CharSequence getConversationTime(Conversation conversation) {
		if (conversation.getSendStatus() == ECMessage.MessageStatus.SENDING
				.ordinal()) {
			return mContext.getString(R.string.conv_msg_sending);
		}
		if (conversation.getDateTime() <= 0) {
			return "";
		}
		return DateUtil.getDateString(conversation.getDateTime(),
				DateUtil.SHOW_TYPE_CALL_LOG).trim();
	}

	/**
	 * 根据消息类型返回相应的主题描述
	 * 
	 * @param conversation
	 * @return
	 */
	protected final CharSequence getConversationSnippet(
			Conversation conversation) {
		if (conversation == null) {
			return "";
		}
		if (GroupNoticeSqlManager.CONTACT_ID
				.equals(conversation.getSessionId())) {
			return GroupNoticeHelper
					.getNoticeContent(conversation.getContent());
		}

		String fromNickName = "";
		if (conversation.getSessionId().toUpperCase().startsWith("G")) {
			if (conversation.getContactId() != null
					&& CCPAppManager.getClientUser() != null
					&& !conversation.getContactId().equals(
							CCPAppManager.getClientUser().getUserId())) {
				ECContacts contact = ContactSqlManager.getContact(conversation
						.getContactId());
				if (contact != null && contact.getNickname() != null) {
					fromNickName = contact.getNickname() + ": ";
				} else {
					fromNickName = conversation.getContactId() + ": ";
				}
			}
		}

		if (conversation.getMsgType() == ECMessage.Type.VOICE.ordinal()) {
			return fromNickName + mContext.getString(R.string.app_voice);
		} else if (conversation.getMsgType() == ECMessage.Type.FILE.ordinal()) {
			return fromNickName + mContext.getString(R.string.app_file);
		} else if (conversation.getMsgType() == ECMessage.Type.IMAGE.ordinal()) {
			return fromNickName + mContext.getString(R.string.app_pic);
		} else if (conversation.getMsgType() == ECMessage.Type.VIDEO.ordinal()) {
			return fromNickName + mContext.getString(R.string.app_video);
		}
		return fromNickName + conversation.getContent();
	}

	/**
	 * 根据消息发送状态处理
	 * 
	 * @param context
	 * @param conversation
	 * @return
	 */
	public static Drawable getChattingSnippentCompoundDrawables(
			Context context, Conversation conversation) {
		if (conversation.getSendStatus() == ECMessage.MessageStatus.FAILED
				.ordinal()) {
			return DemoUtils.getDrawables(context, R.drawable.msg_state_failed);
		} else if (conversation.getSendStatus() == ECMessage.MessageStatus.SENDING
				.ordinal()) {
			return DemoUtils
					.getDrawables(context, R.drawable.msg_state_sending);
		} else {
			return null;
		}
	}

	private void handleDisplayNameTextColor(EmojiconTextView textView,
			String contactId) {
		if (ContactLogic.isCustomService(contactId)) {
			textView.setTextColor(colorStateLists[1]);
		} else {
			textView.setTextColor(colorStateLists[0]);
		}
	}

	/**
	 * 设置未读图片显示规则
	 * 
	 * @param mViewHolder
	 * @param conversation
	 */
	private void setConversationUnread(ViewHolder mViewHolder,
			Conversation conversation) {
		String msgCount = conversation.getUnreadCount() > 100 ? "..." : String
				.valueOf(conversation.getUnreadCount());
		mViewHolder.tipcnt_tv.setText(msgCount);
		if (conversation.getUnreadCount() == 0) {
			mViewHolder.tipcnt_tv.setVisibility(View.GONE);
			mViewHolder.prospect_iv.setVisibility(View.GONE);
		} else if (conversation.isNotice()) {
			mViewHolder.tipcnt_tv.setVisibility(View.VISIBLE);
			mViewHolder.prospect_iv.setVisibility(View.GONE);
		} else {
			mViewHolder.prospect_iv.setVisibility(View.VISIBLE);
			mViewHolder.tipcnt_tv.setVisibility(View.GONE);
		}
	}

	// 获取会话草稿草稿
	public String getConversationDraft(Conversation conver) {
		if (!AbStrUtil.isEmpty(conver.getSessionId())) {

			long mThread = ConversationSqlManager
					.querySessionIdForBySessionId(conver.getSessionId());
			if (null != drafObj && drafObj.has(mThread +conver.getSessionId())
					&& drafObj.optString(mThread + conver.getSessionId()).trim().length() > 0) {
				return drafObj.optString(mThread +conver.getSessionId());
			}

		}
		return "";

	}
	// 判断是不是系统通知
	private boolean isNotice(Conversation conversation) {
		
		if (null!=conversation.getSessionId()&&conversation.getSessionId().toLowerCase().startsWith("g")) {
			return conversation.isNotice();
		}
		return true;
	}
	public void resetDarfData() {
		getEditorTextViewUnSaveText();
		notifyDataSetChanged();
	}

	Gson gson = new Gson();
	private JSONObject drafObj;

	public void getEditorTextViewUnSaveText() {
		String str = SharedPreferencesUtil.getString(mContext,
				"chat_editor_unsend_text2", "");
		if (str.length() > 0) {
			try {
				drafObj = new JSONObject(str);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}
