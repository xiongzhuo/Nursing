package com.im.sdk.dy.ui.contact;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.common.dialog.ECProgressDialog;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.storage.GroupMemberSqlManager;
import com.im.sdk.dy.ui.CCPListAdapter;
import com.im.sdk.dy.ui.ECSuperActivity;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.im.sdk.dy.ui.group.GroupMemberService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yuntongxun.ecsdk.im.ECGroupMember;

/**
 * com.im.sdk.dy.ui.contact in ECDemo_Android Created by Jorstin on
 * 2015/6/15.
 */
public class AtSomeoneUI extends ECSuperActivity implements
		View.OnClickListener, GroupMemberService.OnSynsGroupMemberListener {

	public static final String EXTRA_GROUP_ID = "at_group_id";
	public static final String EXTRA_CHAT_USER = "at_chat_user";
	public static final String EXTRA_SELECT_CONV_USER = "select_conv_user";

	private String mGroupId;
	private String mChatUser;
	private ListView mListView;
	private AtSomeAdapter mAdapter;
	private ECProgressDialog mPostingdialog;
	static DisplayImageOptions optionsSquare_men;
	static DisplayImageOptions optionsSquare_women;

	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ECGroupMember member = mAdapter.getItem(position);
			if (!TextUtils.isEmpty(member.getVoipAccount())) {
				Intent intent = new Intent();
				intent.putExtra(EXTRA_SELECT_CONV_USER, member.getVoipAccount());
				setResult(RESULT_OK, intent);
			}
			finish();
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.im_at_someone_ui;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		optionsSquare_men = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.im_men_defalut)
		.showImageForEmptyUri(R.drawable.im_men_defalut)
		.showImageOnFail(R.drawable.im_men_defalut)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();

optionsSquare_women = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.im_women_defalut)
		.showImageForEmptyUri(R.drawable.im_women_defalut)
		.showImageOnFail(R.drawable.im_women_defalut)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();
		
		mGroupId = getIntent().getStringExtra(EXTRA_GROUP_ID);
		mChatUser = getIntent().getStringExtra(EXTRA_CHAT_USER);

		GroupMemberService.addListener(this);
		GroupMemberService.synsGroupMember(mGroupId);

		initView();
		
	    
	}

	private void initView() {
		getTopBarView().setTopBarToStatus(1, R.drawable.btn_back_style, -1,
				R.string.room_at_someone, this);

		mListView = (ListView) findViewById(R.id.chatroom_member_lv);
		mListView.setOnItemClickListener(onItemClickListener);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
		case R.id.btn_text_left:
			hideSoftKeyboard();
			finish();
			break;

		default:
			break;
		}
	}

	public static class AtSomeAdapter extends CCPListAdapter<ECGroupMember> {

		private String mGroupId;

		/**
		 * 构造方法
		 * 
		 * @param ctx
		 * @param o
		 */
		public AtSomeAdapter(Context ctx, ECGroupMember o, String groupId) {
			super(ctx, o);
			mGroupId = groupId;
		}

		@Override
		protected void notifyChange() {
			Cursor cursor = GroupMemberSqlManager
					.getGroupMembersByCursorExceptSelf(mGroupId);
			setCursor(cursor);
			super.notifyDataSetChanged();
		}

		@Override
		protected void initCursor() {
			notifyChange();
		}

		@Override
		protected ECGroupMember getItem(ECGroupMember member, Cursor cursor) {
			ECGroupMember person = new ECGroupMember();
			person.setBelong(mGroupId);
			person.setVoipAccount(cursor.getString(0));
			person.setDisplayName(cursor.getString(1));
			person.setRemark(cursor.getString(2));
			person.setRole(cursor.getInt(3));
			person.setBan(cursor.getInt(4) == 2 ? true : false);
			return person;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder mViewHolder;
			if (convertView == null || convertView.getTag() == null) {
				view = View.inflate(mContext, R.layout.im_at_someone_item, null);

				mViewHolder = new ViewHolder();
				mViewHolder.mAvatar = (ImageView) view
						.findViewById(R.id.content);
				mViewHolder.name_tv = (EmojiconTextView) view
						.findViewById(R.id.at_someone_item_nick);

				view.setTag(mViewHolder);
			} else {
				view = convertView;
				mViewHolder = (ViewHolder) view.getTag();
			}
			final ECGroupMember item = getItem(position);
			if (item != null) {
				item.setDisplayName(TextUtils.isEmpty(item.getDisplayName()) ? item
						.getVoipAccount() : item.getDisplayName());
				mViewHolder.name_tv.setText(item.getDisplayName());
				mViewHolder.mAvatar.setImageBitmap(ContactLogic.getPhoto(item
						.getRemark()));
				
			
				
				ECContacts contacts = ContactSqlManager.getContact(item.getVoipAccount()
						);
				int sex = contacts.getSex();
				if (!AbStrUtil.isEmpty(contacts.getAvatar())) {
					ImageLoader.getInstance().displayImage(
							WebUrl.FILE_LOAD_URL + contacts.getAvatar(),
							mViewHolder.mAvatar,
							sex == 1 ? optionsSquare_women
									: optionsSquare_men);

				} else {
					ImageLoader.getInstance().displayImage(
							"",
							mViewHolder.mAvatar,
							sex == 1 ? optionsSquare_women
									: optionsSquare_men);
				}
				
			}

			return view;
		}

		static class ViewHolder {
			/** 头像 */
			ImageView mAvatar;
			/** 名称 */
			EmojiconTextView name_tv;
		}
	}

	@Override
	public void onSynsGroupMember(String groupId) {
		if (groupId == null || !mGroupId.equals(groupId)) {
			return;
		}
		ArrayList<ECGroupMember> members = GroupMemberSqlManager
				.getGroupMemberWithName(mGroupId);

		mAdapter = new AtSomeAdapter(this, new ECGroupMember(), mGroupId);

		mListView.setAdapter(mAdapter);
	}
	
	
	

	
}
