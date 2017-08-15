package com.im.sdk.dy.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.WebUrl;
import com.google.gson.Gson;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.DateUtil;
import com.im.sdk.dy.common.utils.DemoUtils;
import com.im.sdk.dy.common.utils.ResourceHelper;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.storage.ConversationSqlManager;
import com.im.sdk.dy.storage.GroupMemberSqlManager;
import com.im.sdk.dy.storage.GroupNoticeSqlManager;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.im.sdk.dy.ui.chatting.model.Conversation;
import com.im.sdk.dy.ui.contact.ContactLogic;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.im.sdk.dy.ui.group.GroupNoticeHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.yuntongxun.ecsdk.ECMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 消息列表adapter
 *
 * @author YUNG
 * @date 2015-12-8
 * @version 4.0
 */
public class ConversationAdapter extends CCPListAdapter<Conversation> {

	private OnListAdapterCallBackListener mCallBackListener;
	int padding;
	private ColorStateList[] colorStateLists;
	public static DisplayImageOptions optionsSquare_women;
	public static DisplayImageOptions optionsSquare_men;
	JSONObject drafObj;
	public String[] ids;//需过滤的数组

	/**
	 * @param ctx
	 */
	public ConversationAdapter(Context ctx,
							   OnListAdapterCallBackListener listener) {
		super(ctx, new Conversation());
		mCallBackListener = listener;
		padding = ctx.getResources()
				.getDimensionPixelSize(R.dimen.OneDPPadding);
		colorStateLists = new ColorStateList[] {
				ResourceHelper
						.getColorStateList(ctx, R.color.normal_text_color),
				ResourceHelper.getColorStateList(ctx,
						R.color.ccp_list_textcolor_three) };
		optionsSquare_women = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.im_women_defalut)
				.showImageForEmptyUri(R.drawable.im_women_defalut)
				.showImageOnFail(R.drawable.im_women_defalut)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new SimpleBitmapDisplayer()).build();
		// .displayer(new FadeInBitmapDisplayer(300)).build();

		optionsSquare_men = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.im_men_defalut)
				.showImageForEmptyUri(R.drawable.im_men_defalut)
				.showImageOnFail(R.drawable.im_men_defalut)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new SimpleBitmapDisplayer()).build();
		getEditorTextViewUnSaveText();// 获取全部草稿
		// .displayer(new FadeInBitmapDisplayer(300)).build();
	}

	// 判断是否为群消息
	@Override
	protected Conversation getItem(Conversation t, Cursor cursor) {
		Conversation conversation = new Conversation();
		conversation.setCursor(cursor);
		if (conversation.getUsername() != null
				&& conversation.getUsername().endsWith("@priategroup.com")) {
			ArrayList<String> member = GroupMemberSqlManager
					.getGroupMemberID(conversation.getSessionId());
			if (member != null) {
				ArrayList<String> contactName = ContactSqlManager
						.getContactName(member.toArray(new String[] {}));
				if (contactName != null && !contactName.isEmpty()) {
					String chatroomName = DemoUtils.listToString(contactName,
							",");
					conversation.setUsername(chatroomName);
				}
			}
		}
		return conversation;
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

		Conversation conversation = getItem(position);
		Log.e("IMconver",
				conversation.getSessionId() + "----"
						+ conversation.getUsername());
		if (conversation != null) {
			if (TextUtils.isEmpty(conversation.getUsername())) {
				mViewHolder.nickname_tv.setText(conversation.getSessionId());
			} else {
				mViewHolder.nickname_tv.setText(conversation.getUsername());
			}
			handleDisplayNameTextColor(mViewHolder.nickname_tv,
					conversation.getSessionId());
			if (!AbStrUtil.isEmpty(getConversationDraft(conversation))) {
				mViewHolder.last_msg_tv
						.setText(getConversationDraft(conversation));
				mViewHolder.drafTv.setVisibility(View.VISIBLE);
			} else {
				mViewHolder.drafTv.setVisibility(View.GONE);
				mViewHolder.last_msg_tv
						.setText(getConversationSnippet(conversation));
			}
			mViewHolder.last_msg_tv
					.setCompoundDrawables(
							getChattingSnippentCompoundDrawables(mContext,
									conversation), null, null, null);
			// 未读提醒设置
			setConversationUnread(mViewHolder, conversation);
			mViewHolder.image_input_text.setVisibility(View.GONE);
			mViewHolder.update_time_tv
					.setText(getConversationTime(conversation));

			if (conversation.getSessionId().toUpperCase().startsWith("G")) {
				Bitmap bitmap = ContactLogic.getChatroomPhoto(conversation
						.getSessionId());
				if (bitmap != null) {
					mViewHolder.user_avatar.setImageBitmap(bitmap);
					mViewHolder.user_avatar.setPadding(padding, padding,
							padding, padding);
//					mViewHolder.user_avatar.setBackgroundColor(Color
//							.parseColor("#d5d5d5"));
				} else {
					mViewHolder.user_avatar
							.setImageResource(R.drawable.group_head);
					mViewHolder.user_avatar.setPadding(0, 0, 0, 0);
					mViewHolder.user_avatar.setBackgroundDrawable(null);
				}
			} else {
				mViewHolder.user_avatar.setBackgroundDrawable(null);
				// ECContacts contact =
				// ContactSqlManager.getContact(conversation
				// .getContactId());

				SetAvatar(mViewHolder.user_avatar, conversation.getAvatar(),
						conversation.getSex(), conversation.getContactsType());
				// }else{
				// SetAvatar(mViewHolder.user_avatar, "",
				// 1, 0);
				// }
				// if(conversation.getSessionId().equals(GroupNoticeSqlManager.CONTACT_ID))
				// {
				// mViewHolder.user_avatar.setImageResource(R.drawable.ic_launcher);
				// } else {
				// ECContacts contact =
				// ContactSqlManager.getContact(conversation.getSessionId());
				//
				// //
				// mViewHolder.user_avatar.setImageBitmap(ContactLogic.getPhoto(contact.getRemark()));
				//
				//
				// }
			}
			mViewHolder.image_mute
					.setVisibility(isNotice(conversation) ? View.GONE
							: View.VISIBLE);
		}

		return view;
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

	@Override
	protected void initCursor() {
		notifyChange();
	}

	@Override
	protected void notifyChange() {
		if (mCallBackListener != null) {
			mCallBackListener.OnListAdapterCallBack();
		}
//		if (null == ids || ids.length < 1) {
//			Cursor cursor = ConversationSqlManager.getConversationCursor3();// 原来是getConversationCursor(),这里过滤掉了为29和31的id的
//			setCursor(cursor);
//		} else {
//			Cursor cursor = ConversationSqlManager
//					.getConversationCursorFilter(ids);// 原来是getConversationCursor(),这里过滤掉了为29和31的id的
//			setCursor(cursor);
//		}
		Cursor cursor = ConversationSqlManager.getConversationCursor5();
		setCursor(cursor);
		super.notifyDataSetChanged();
	}

	public void retdata(String[] ids) {
		if (mCallBackListener != null) {
			mCallBackListener.OnListAdapterCallBack();
		}
		this.ids = ids;
		Cursor cursor = ConversationSqlManager.getConversationCursorFilter(ids);// 原来是getConversationCursor(),这里过滤掉了为29和31的id的
		setCursor(cursor);
		notifyDataSetChanged();
	}

	// 判断是不是系统通知
	private boolean isNotice(Conversation conversation) {
		if (conversation.getSessionId().toLowerCase().startsWith("g")) {
			return conversation.isNotice();
		}
		return true;
	}

	private void SetAvatar(ImageView img, String avatar, int sex, int type) {
		if (type == 999) {
			img.setImageResource(R.drawable.xiaoxi_touxiang);
		} else if(type==1000){
			img.setImageResource(R.drawable.im_task_tips);
		}else {
			if (!AbStrUtil.isEmpty(avatar)) {
				ImageLoader.getInstance().displayImage(
						WebUrl.FILE_LOAD_URL + avatar, img,
						sex == 1 ? optionsSquare_women : optionsSquare_men);
			} else {
				ImageLoader.getInstance().displayImage("", img,
						sex == 1 ? optionsSquare_women : optionsSquare_men);
			}
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

	public void resetDarfData() {
		getEditorTextViewUnSaveText();
		notifyDataSetChanged();
	}

	Gson gson = new Gson();

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
