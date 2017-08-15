package com.im.sdk.dy.ui.group;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.dialog.ECAlertDialog;
import com.im.sdk.dy.common.dialog.ECProgressDialog;
import com.im.sdk.dy.common.utils.DemoUtils;
import com.im.sdk.dy.common.utils.DensityUtil;
import com.im.sdk.dy.common.utils.ECPreferenceSettings;
import com.im.sdk.dy.common.utils.ECPreferences;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.common.view.RoundedImageView;
import com.im.sdk.dy.common.view.SettingItem;
import com.im.sdk.dy.core.ClientUser;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.storage.GroupMemberSqlManager;
import com.im.sdk.dy.storage.GroupSqlManager;
import com.im.sdk.dy.storage.IMessageSqlManager;
import com.im.sdk.dy.ui.ConversationListFragment;
import com.im.sdk.dy.ui.ConversationListFragment.GroupItem;
import com.im.sdk.dy.ui.ECSuperActivity;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.ChattingFragment;
import com.im.sdk.dy.ui.contact.ContactDetailActivity;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.im.sdk.dy.ui.contact.GoupMenberDetailActivity;
import com.im.sdk.dy.ui.group.GroupMemberService.GroupServiceInterface;
import com.im.sdk.dy.ui.settings.EditConfigureActivity;
import com.im.sdk.dy.ui.settings.SettingsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;
import com.yuntongxun.ecsdk.ECGroupManager.OnGetGroupDetailListener;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECGroupMember;
import com.yuntongxun.ecsdk.im.ECGroupOption;
import com.yuntongxun.ecsdk.im.ESpeakStatus;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;

/**
 * 群组详情界面
 * 
 * @author Jorstin Chan@容联•云通讯
 * @date 2014-12-29
 * @version 4.0
 */
public class GroupInfoActivity extends ECSuperActivity implements
		View.OnClickListener, GroupMemberService.OnSynsGroupMemberListener,
		GroupService.Callback {

	/** 新修改的，移除成员的时候，判断是否删除的自己。。。。。此修改可能会引起错误 */
	ClientUser clientUser;

	private static final String TAG = "ECDemo.GroupInfoActivity";
	public final static String GROUP_ID = "group_id";
	public static final String EXTRA_RELOAD = "com.im.sdk.dy_reload";
	public static final String EXTRA_QUEIT = "com.im.sdk.dy_quit";

	private ScrollView mScrollView;
	// private TextView mGroupCountTv;
	/** 群组ID */
	private DemoGroup mGroup;
	/** 群组公告 */
	private EditText mNotice;
	/** 群组成员列表 */
	private GridView mListView;
	/** 群组成员适配器 */
	private GroupInfoAdapter mAdapter;
	private ECProgressDialog mPostingdialog;
	private boolean mClearChatmsg = false;
	private SettingItem mNoticeItem;
	private SettingItem mGroupCardItem;
	private SettingItem mNameItem;
	private SettingItem mNewMsgNotify;
	private  SettingItem includeGkteam;
	private SettingItem mRoomDisplayname;
	private int mEditMode = -1;
	ECGroup newGroup2;
	private boolean isMyDepartGroup = false;
	String departGroupOwnerId = "";

	private ArrayList<String> existMemberId = new ArrayList<String>();
	private String adminId;

	private final AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ECGroupMember item = mAdapter.getItem(position);
			if (item == null) {
				return;
			}

			if ("add@yuntongxun.com".equals(item.getVoipAccount())) {
				// if (isOwner()) {
				Intent intent = new Intent(
						GroupInfoActivity.this,
						com.im.sdk.dy.ui.group.MobileContactSelectActivity.class);
				intent.putExtra("group_select_need_result", true);
				intent.putExtra("select_type", false);
				intent.putExtra("members", members);
				startActivityForResult(intent, 0x2a);
				// }

				return;
			}

			if ("delete@yuntongxun.com".equals(item.getVoipAccount())) {

				if (CCPAppManager.getClientUser().getUserId().equals(adminId)) {
					existMemberId.clear();
					for (int i = 0; i < members.size(); i++) {
						if (members.get(i).getVoipAccount().length() < 10) {
							existMemberId.add(members.get(i).getVoipAccount());
						}
					}
					Intent intent = new Intent(GroupInfoActivity.this,
							com.im.sdk.dy.ui.group.DeletfriendActivity.class);
					intent.putExtra("group_select_need_result", true);
					intent.putExtra("select_type", false);
					intent.putExtra("groupId", groupId);
					// intent.putExtra("members", members);
					intent.putStringArrayListExtra("existMemberId",
							existMemberId);
					startActivityForResult(intent, 0x2a);
				}

				return;
			}

			// GroupMemberService.queryGroupMemberCard(mGroup.getGroupId() ,
			// item.getVoipAccount());
			// GroupMemberService.modifyGroupMemberCard(mGroup.getGroupId(),
			// item.getVoipAccount());

			ECContacts contact = ContactSqlManager.getContact(item
					.getVoipAccount());
			if (contact == null || contact.getId() == -1) {

				Log.i("IMtag", contact.getMobile() + "");
				return;
			}

			Intent intent = new Intent(GroupInfoActivity.this,
					GoupMenberDetailActivity.class);
			long rawId = Long.parseLong(item.getVoipAccount());

			intent.putExtra(ContactDetailActivity.RAW_ID, rawId);
			startActivity(intent);
			finish();
		}
	};

	private String groupId;
	private boolean isDis = true;

	@Override
	protected int getLayoutId() {
		return R.layout.im_group_info_activity;
	}

	protected void toditailActivity(JSONObject job) {

	}

	@Override
	protected boolean isEnableSwipe() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		groupId = getIntent().getStringExtra(GROUP_ID);
		mGroup = GroupSqlManager.getDemoGroup(groupId);
		getGKdepartGroup();
		getGKteamGroup();
		departGroupOwnerId = getIntent().getStringExtra(
				ChattingFragment.DEPART_GROU_OWNER);
		isMyDepartGroup = getIntent().getBooleanExtra(
				ChattingFragment.IS_MY_DEPART_GROU, false);
		adminId = getIntent().getStringExtra("adminId");

		// isDefalut = mGroup.isDefalut();
		isDefalut = false;
		// isLocalDiscussion =true;

		if (mGroup == null) {
			finish();
			return;
		}
		isLocalDiscussion = GroupSqlManager.isDiscussionGroup(mGroup
				.getGroupId());
		initView();
		refeshGroupInfo();

		/** 解决消息免打扰问题，注释掉的代码 */
		GroupService.syncGroupInfo(mGroup.getGroupId());
		GroupMemberService.synsGroupMember(mGroup.getGroupId());

		registerReceiver(new String[] { IMessageSqlManager.ACTION_GROUP_DEL });

	}

	public static final int DELET_MENBER_FAIL = 0x4321;
	public static final int DELET_MENBER_SUCESS = 0x4320;
	public static final int GK_SET_FAIL = 0x4323;
	public static final int GK_SET_SUCCESS = 0x4324;
	// 请求服务器删除本院群成员
	public void onDepartGroupDelet(String ids) {
		JSONObject job = null;
		try {
			job = new JSONObject(ids);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				GroupInfoActivity.this, DELET_MENBER_SUCESS, DELET_MENBER_FAIL,
				job, "iminfo/initalizeGroupMember");

	}
	// 设置感控团队
	public void onSetGkGroup() {
		JSONObject job = new JSONObject();
		try {
			job.put(Constants.AUTHENT, tools.getValue(Constants.AUTHENT));
			job.put("group_id", groupId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MainBizImpl.getInstance().onComomRequest(myHandler,
				GroupInfoActivity.this, GK_SET_SUCCESS, GK_SET_FAIL,
				job, "iminfo/setMyTeam");

	}
	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case DELET_MENBER_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							setDeletResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case DELET_MENBER_FAIL:
					ToastUtils.showToast(GroupInfoActivity.this, "亲，您的网络不顺畅哦！");
					break;
				case GK_SET_SUCCESS:
					if (null != msg && null != msg.obj) {
						try {
							setGkresult(new JSONObject(msg.obj.toString()));
							
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case GK_SET_FAIL:
					break;
				default:
					break;
				}
			}
		}

	};

	@Override
	protected void onResume() {
		super.onResume();
		GroupService.addListener(this);
		GroupMemberService.addListener(this);
		// Log.e("aaaaaaaaa",
		// mGroup.getName()+"+++++++"+mGroup.getOwner()+"+++++++"+mGroup.getOwnerName());

		// ECGroupManager groupManager = ECDevice.getECGroupManager();
		// groupManager.queryGroupMembers(groupId,
		// new ECGroupManager.OnQueryGroupMembersListener() {
		// @Override
		// public void onQueryGroupMembersComplete(ECError error,
		// List members111) {
		// if (error.errorCode == SdkErrorCode.REQUEST_SUCCESS
		// && members111 != null) {
		// members.clear();
		//
		// members.addAll((ArrayList<ECGroupMember>) members111);
		// mAdapter.notifyDataSetChanged();
		//
		// return;
		// }
		// // 群组成员获取失败
		// Log.e("ECSDK_Demo", "sync group detail fail "
		// + ", errorCode=" + error.errorCode);
		//
		// }
		//
		// public void onComplete(ECError error) {
		// // 忽略不处理
		// }
		// });

		try {
			new Thread().sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ECGroupManager groupManager = ECDevice.getECGroupManager();
		groupManager.getGroupDetail(groupId, new OnGetGroupDetailListener() {

			@Override
			public void onGetGroupDetailComplete(ECError arg0, ECGroup arg1) {
				// TODO Auto-generated method stub
				if (arg0.errorCode == SdkErrorCode.REQUEST_SUCCESS
						&& arg1 != null) {
					// members.clear();
					boolean isNewNotice = !arg1.isNotice();
					mNewMsgNotify.setChecked(isNewNotice);
					boolean checked = mNewMsgNotify.isChecked();
					showProcessDialog(getString(R.string.login_posting_submit));
					ECGroupOption option = new ECGroupOption();
					option.setGroupId(mGroup.getGroupId());
					option.setRule(checked ? ECGroupOption.Rule.SILENCE
							: ECGroupOption.Rule.NORMAL);
					GroupService.setGroupMessageOption(option);

					if (arg1 != null) {
						newGroup2 = arg1;
						setActionBarTitle("聊天信息" + "(" + members.size() + ")");
						// SpannableString charSequence =
						// setNewMessageMute(!ecGroup
						// .isNotice());
						// if (charSequence != null) {
						// getTopBarView().setTitle(charSequence);
						// }
					}
					dismissdialog();
					return;
				}
			}
		});

	}

	protected void setGkresult(JSONObject jsonObject) {
		isGkTeam=!isGkTeam;
		upDateGkGroupState();
		
	}

	protected void setDeletResult(JSONObject jsonObject) {
		// TODO Auto-generated method stub

	}

	/**
     *
     */
	private void initView() {
		mNotice = (EditText) findViewById(R.id.group_notice);
		// mGroupCountTv = (TextView) findViewById(R.id.group_count);
		mNoticeItem = (SettingItem) findViewById(R.id.group_notice2);
		mGroupCardItem = (SettingItem) findViewById(R.id.group_card);
		tvShowMoreMember = (TextView) findViewById(R.id.group_showmember_btn);
		if (isDefalut) {
			findViewById(R.id.lay_bottom_red).setVisibility(View.GONE);
		}

		tvShowMoreMember.setText("查看更多");
		tvShowMoreMember.setOnClickListener(this);

		mGroupCardItem.setOnClickListener(this);
		mNoticeItem.getCheckedTextView().setSingleLine(false);
		// mNoticeItem.getCheckedTextView().setMaxLines(5);
		mNameItem = (SettingItem) findViewById(R.id.group_name);
		mNameItem.getCheckedTextView().setSingleLine(false);
		mListView = (GridView) findViewById(R.id.member_lv);
		mListView.setOnItemClickListener(mItemClickListener);
		mScrollView = (ScrollView) findViewById(R.id.info_content);
		mAdapter = new GroupInfoAdapter(GroupInfoActivity.this);
		mListView.setAdapter(mAdapter);

		int columnWidth = DensityUtil.getMetricsDensity(mContext, 73.0F);
		int column = mListView.getWidth() / columnWidth;
		mListView.setNumColumns(4);

		mNewMsgNotify = (SettingItem) findViewById(R.id.settings_new_msg_notify);
		
		includeGkteam= (SettingItem) findViewById(R.id.settings_is_include_gkteam);//添加或者撤销到感控团队
		if(isDepartTeam){
			includeGkteam.setVisibility(View.GONE);
		}
		mNewMsgNotify.findViewById(R.id.right_row).setVisibility(View.GONE);
		mRoomDisplayname = (SettingItem) findViewById(R.id.settings_room_my_displayname);
		mNewMsgNotify.getCheckedTextView().setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						updateGroupNewMsgNotify();

					}
				});
		includeGkteam.getCheckedTextView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSetGkGroup();
				
				
			}
		});
		includeGkteam.findViewById(R.id.right_row).setVisibility(View.GONE);
		includeGkteam.setChecked(isGkTeam);
		mShowChatName = (SettingItem) findViewById(R.id.settings_group_show_nickname);
		mShowChatName.getCheckedTextView().setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						updateShowChatName();
					}
				});

		boolean isShowChatName = ECPreferences
				.getSharedPreferences()
				.getBoolean(
						ECPreferenceSettings.SETTINGS_SHOW_CHATTING_NAME
								.getId(),
						false);

		mShowChatName.setChecked(isShowChatName);

		findViewById(R.id.red_btn).setOnClickListener(this);
		findViewById(R.id.clear_msg).setOnClickListener(this);
		mNotice.setEnabled(isOwner() ? true : false);
		button = (TextView) findViewById(R.id.red_btn);
		button.setText(R.string.quit_discussion);
		// if (GroupSqlManager.isDiscussionGroup(groupId)) {
		//
		// button.setText(R.string.quit_discussion);
		// mNameItem.setTitleText(getString(R.string.dis_name));
		// mNoticeItem.setTitleText(getString(R.string.dis_notice));
		//
		// } else {
		//
		// button.setText(isOwner() ? R.string.str_group_dissolution
		// : R.string.str_group_quit);
		// }
		onSynsGroupMember(mGroup.getGroupId());

		if (isLocalDiscussion) {

			/** 判断是否是自己创建的群，如果不是则不允许修改群名和群公告 */
			// if (isOwner()) {
			mNameItem.setOnClickListener(new OnItemClickListener(
					SettingsActivity.CONFIG_TYPE_GROUP_NAME));

			mNoticeItem.setOnClickListener(new OnItemClickListener(
					SettingsActivity.CONFIG_TYPE_GROUP_NOTICE));
			// }

		}
	}

	private void updateGroupNewMsgNotify() {
		if (mGroup == null || mGroup.getGroupId() == null) {
			return;
		}
		// 处理消息免打扰
		try {
			if (mNewMsgNotify == null) {
				return;
			}
			mNewMsgNotify.toggle();
			boolean checked = mNewMsgNotify.isChecked();
			showProcessDialog(getString(R.string.login_posting_submit));
			ECGroupOption option = new ECGroupOption();
			option.setGroupId(mGroup.getGroupId());
			option.setRule(checked ? ECGroupOption.Rule.SILENCE
					: ECGroupOption.Rule.NORMAL);
			GroupService.setGroupMessageOption(option);
			LogUtil.d(TAG, "updateGroupNewMsgNotify: " + checked);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	private void updateShowChatName() {
		if (mGroup == null || mGroup.getGroupId() == null) {
			return;
		}
		try {
			if (mShowChatName == null) {
				return;
			}
			mShowChatName.toggle();
			showCommonProcessDialog("");
			boolean checked = mShowChatName.isChecked();
			GroupService.setGroupIsAnonymity(groupId, checked);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     *
     */
	private void refeshGroupInfo() {
		if (mGroup == null) {
			return;
		}
		mNotice.setText(mGroup.getDeclare());
		mNoticeItem.setCheckText(mGroup.getDeclare());
		mNotice.setSelection(mNotice.getText().length());
		if (mGroup.getName() != null
				&& mGroup.getName().endsWith("@priategroup.com")) {
			ArrayList<String> member = GroupMemberSqlManager
					.getGroupMemberID(mGroup.getGroupId());
			if (member != null) {
				ArrayList<String> contactName = ContactSqlManager
						.getContactName(member.toArray(new String[] {}));
				String chatroomName = DemoUtils.listToString(contactName, ",");
				mGroup.setName(chatroomName);
			}
		}
		getTopBarView().setTopBarToStatus(1, R.drawable.btn_back_style, -1,
				mGroup.getName(), this);
		getTopBarView().setRightVisibleGone();
		mNameItem.setCheckText(mGroup.getName());
		mNewMsgNotify.setChecked(!mGroup.isNotice());

		// ECGroupOption option = new ECGroupOption();
		// option.setGroupId(mGroup.getGroupId());
		// option.setRule(getIntent().getBooleanExtra("isNotice", true) ?
		// ECGroupOption.Rule.SILENCE
		// : ECGroupOption.Rule.NORMAL);
		// GroupService.setGroupMessageOption(option);
		// mNewMsgNotify.setChecked(getIntent().getBooleanExtra("isNotice",
		// true));

		DemoGroup ecGroup = GroupSqlManager.getDemoGroup(mGroup.getGroupId());
		if (ecGroup != null) {
			if (newGroup2 != null) {
				setActionBarTitle("聊天信息" + "(" + members.size() + ")");
			} else {
				setActionBarTitle("聊天信息" + "(" + members.size() + ")");
			}

			SpannableString charSequence = setNewMessageMute(!ecGroup
					.isNotice());
			if (charSequence != null) {
				getTopBarView().setTitle(charSequence);
			}
		}

		// if (GroupSqlManager.isDiscussionGroup(groupId)) {
		//
		//
		// button.setText(R.string.quit_discussion);
		// mNameItem.setTitleText(getString(R.string.dis_name));
		// mNoticeItem.setTitleText(getString(R.string.dis_notice));
		//
		// } else {
		//
		// button.setText(isOwner() ? R.string.str_group_dissolution
		// : R.string.str_group_quit);
		// }
	}

	private void refeshGroupInfo2() {
		if (mGroup == null) {
			return;
		}
		mNotice.setText(mGroup.getDeclare());
		mNoticeItem.setCheckText(mGroup.getDeclare());
		mNotice.setSelection(mNotice.getText().length());
		if (mGroup.getName() != null
				&& mGroup.getName().endsWith("@priategroup.com")) {
			ArrayList<String> member = GroupMemberSqlManager
					.getGroupMemberID(mGroup.getGroupId());
			if (member != null) {
				ArrayList<String> contactName = ContactSqlManager
						.getContactName(member.toArray(new String[] {}));
				String chatroomName = DemoUtils.listToString(contactName, ",");
				mGroup.setName(chatroomName);
			}
		}
		getTopBarView().setTopBarToStatus(1, R.drawable.btn_back_style, -1,
				mGroup.getName(), this);
		getTopBarView().setRightVisibleGone();
		mNameItem.setCheckText(mGroup.getName());
		mNewMsgNotify.setChecked(mGroup.isNotice());

		// ECGroupOption option = new ECGroupOption();
		// option.setGroupId(mGroup.getGroupId());
		// option.setRule(getIntent().getBooleanExtra("isNotice", true) ?
		// ECGroupOption.Rule.SILENCE
		// : ECGroupOption.Rule.NORMAL);
		// GroupService.setGroupMessageOption(option);
		// mNewMsgNotify.setChecked(getIntent().getBooleanExtra("isNotice",
		// true));

		DemoGroup ecGroup = GroupSqlManager.getDemoGroup(mGroup.getGroupId());
		if (ecGroup != null) {
			setActionBarTitle(ecGroup.getName() != null ? ecGroup.getName()
					+ "(" + members.size() + ")" : ecGroup.getGroupId() + "("
					+ mGroup.getCount() + ")");
			SpannableString charSequence = setNewMessageMute(!ecGroup
					.isNotice());
			if (charSequence != null) {
				getTopBarView().setTitle(charSequence);
			}
		}

		// if (GroupSqlManager.isDiscussionGroup(groupId)) {
		//
		//
		// button.setText(R.string.quit_discussion);
		// mNameItem.setTitleText(getString(R.string.dis_name));
		// mNoticeItem.setTitleText(getString(R.string.dis_notice));
		//
		// } else {
		//
		// button.setText(isOwner() ? R.string.str_group_dissolution
		// : R.string.str_group_quit);
		// }
	}

	/**
	 * 是否是群组创建者
	 * 
	 * @return
	 */
	private boolean isOwner() {

		return CCPAppManager.getClientUser().getUserId()
				.equals(mGroup.getOwner());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
		case R.id.btn_text_left:
			goBack();
			break;
		case R.id.group_showmember_btn:

			if (tvShowMoreMember.getText().equals("查看更多")) {
				tvShowMoreMember.setText("收起");
				mAdapter.setData(members);
				mAdapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(mListView);
			} else {
				tvShowMoreMember.setText("查看更多");
				if (subMemberList != null) {
					mAdapter.setData(subMemberList);
					setListViewHeightBasedOnChildren(mListView);
				}
				mAdapter.notifyDataSetChanged();
			}

			break;
		case R.id.red_btn:

			mPostingdialog = new ECProgressDialog(this,
					R.string.group_exit_posting);
			mPostingdialog.show();

			// if (isOwner()) {
			//
			// GroupService.disGroup(mGroup.getGroupId());
			// return;
			// }

			// if (GroupSqlManager.isDiscussionGroup(groupId)) {
			GroupService.quitGroup(mGroup.getGroupId());
			// return;
			// }

			// GroupService.quitGroup(mGroup.getGroupId());
			break;
		case R.id.clear_msg:
			ECAlertDialog buildAlert = ECAlertDialog.buildAlert(
					GroupInfoActivity.this,
					R.string.fmt_delcontactmsg_confirm_group, null,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							mPostingdialog = new ECProgressDialog(
									GroupInfoActivity.this, R.string.clear_chat);
							mPostingdialog.show();
							ECHandlerHelper handlerHelper = new ECHandlerHelper();
							handlerHelper.postRunnOnThead(new Runnable() {
								@Override
								public void run() {
									IMessageSqlManager
											.deleteChattingMessage(mGroup
													.getGroupId());
									ToastUtil
											.showMessage(R.string.clear_msg_success);
									mClearChatmsg = true;
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											dismissPostingDialog();
										}
									});
								}
							});

						}

					});
			buildAlert.setTitle(R.string.app_tip);
			buildAlert.show();
			break;
		case R.id.btn_middle:
			if (mScrollView != null) {
				getTopBarView().post(new Runnable() {
					@Override
					public void run() {
						mScrollView.fullScroll(View.FOCUS_UP);
					}
				});
			}
			break;

		case R.id.group_card:
			startActivity(new Intent(this, GroupMemberCardActivity.class)
					.putExtra("groupId", groupId));

			break;
		default:
			break;
		}
	}

	void showProcessDialog(String tips) {
		mPostingdialog = new ECProgressDialog(GroupInfoActivity.this,
				R.string.login_posting_submit);
		mPostingdialog.show();
	}

	/**
	 * 关闭对话框
	 */
	private void dismissPostingDialog() {
		if (mPostingdialog == null ) {
			return;
		}
		mPostingdialog.dismiss();
		mPostingdialog = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
				+ ", resultCode=" + resultCode + ", data=" + data);

		// If there's no data (because the user didn't select a picture and
		// just hit BACK, for example), there's nothing to do.
		if (requestCode == 0x2a || requestCode == 0xa) {
			if (data == null) {
				return;
			}
		} else if (resultCode != RESULT_OK) {
			LogUtil.d("onActivityResult: bail due to resultCode=" + resultCode);
			return;
		}
		if (requestCode == 0x2a) {
			final String[] selectUser = data
					.getStringArrayExtra("Select_Conv_User");
			if (selectUser != null && selectUser.length > 0) {
				mPostingdialog = new ECProgressDialog(this,
						R.string.invite_join_group_posting);
				mPostingdialog.show();
				String reason = getString(R.string.group_invite_reason,
						CCPAppManager.getClientUser().getUserName(),
						mGroup.getName());

				// new Thread(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// GroupMemberService.inviteMembers(mGroup, selectUser);
				// mPostingdialog.dismiss();
				// }
				// }).start();
				// 发送个人信息进行邀请

				// if(mGroup.getOwner().equals(CCPAppManager.getClientUser().getUserId())){
				GroupMemberService.inviteMembers(mGroup.getGroupId(), reason,
						ECGroupManager.InvitationMode.FORCE_PULL, selectUser,
						new ArrayList<ECContacts>(),
						new GroupServiceInterface() {

							@Override
							public void onInvateComplet() {
								// TODO Auto-generated method stub

							}
						});
				// }else{
				// //调用后台
				// }

				// if (GroupSqlManager.isDiscussionGroup(groupId)) {
				//
				// GroupMemberService.inviteMembers(mGroup.getGroupId(),
				// reason, ECGroupManager.InvitationMode.FORCE_PULL,
				// selectUser);
				//
				// } else {
				// GroupMemberService.inviteMembers(mGroup.getGroupId(),
				// reason, ECGroupManager.InvitationMode.NEED_CONFIRM,
				// selectUser);
				// }

			}
		} else if (requestCode == 0xa) {
			String result_data = data.getStringExtra("result_data");
			if (mGroup == null) {
				return;
			}
			if (TextUtils.isEmpty(result_data)) {
				ToastUtil.showMessage("不允许为空");
				return;
			}
			if (mEditMode == SettingsActivity.CONFIG_TYPE_GROUP_NAME) {
				mGroup.setName(result_data);
			} else {
				mGroup.setDeclare(result_data);
			}

			doModifyGroup();
		}
	}

	private void doModifyGroup() {
		// 修改群组信息请求
		showProcessDialog(getString(R.string.login_posting_submit));
		GroupService.modifyGroup(mGroup);
	}

	@Override
	public void onSynsGroupMember(String groupId) {
		dismissPostingDialog();
		if (mGroup == null || !mGroup.getGroupId().equals(groupId)) {
			return;
		}

		int count = mAdapter.getCount();
		members.clear();
		ArrayList<ECGroupMember> memberscache = GroupMemberSqlManager
				.getGroupMemberList(mGroup.getGroupId());
		if(null!=memberscache){
		for (ECGroupMember menber : memberscache) {
			if (menber.getVoipAccount().length() < 10) {
				members.add(menber);
			}

		}
		}
		if (members == null) {
			members = new ArrayList<ECGroupMember>();
		}
		boolean hasSelf = false;
		clientUser = CCPAppManager.getClientUser();
		for (ECGroupMember member : members) {
			if (clientUser.getUserId().equals(member.getVoipAccount())) {
				hasSelf = true;
				break;
			}
		}
		if (!hasSelf) {
			ECContacts contact = ContactSqlManager.getContact(clientUser
					.getUserId());
			if (contact != null) {
				ECGroupMember member = new ECGroupMember();
				member.setVoipAccount(contact.getContactid());
				member.setRemark(contact.getRemark());
				member.setDisplayName(contact.getNickname());
				members.add(member);
			}
		}

		int num = 200;
		if (isDefalut) {
			num = 200;
		}

		if (members != null && members.size() > num) {
			subMemberList = members.subList(0, num);
			mAdapter.setData(subMemberList);
			tvShowMoreMember.setVisibility(View.VISIBLE);
			tvShowMoreMember.setText("查看更多");
		} else {
			mAdapter.setData(members);
			tvShowMoreMember.setVisibility(View.GONE);
		}

		int memCount = members.size();
		if (isDis || isLocalDiscussion) {
			// mGroupCountTv.setText(getString(R.string.str_discussion_members_tips,
			// memCount));
		} else {
			// mGroupCountTv.setText(getString(R.string.str_group_members_tips,
			// memCount));
		}

		if (members != null && count <= members.size()) {
			setListViewHeightBasedOnChildren(mListView);
		}

		getTopBarView().setTitle(
				mGroup.getName()
						+ getString(R.string.str_group_members_titletips,
								memCount));
		setActionBarTitle("聊天信息" + "(" + members.size() + ")");
		setListViewHeightBasedOnChildren(mListView);

	}

	private boolean isDefalut = false;

	public boolean isRefresh = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private void goBack() {
		Intent intent = new Intent(this, ChattingActivity.class);
		intent.putExtra(EXTRA_RELOAD, mClearChatmsg);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		if (mAdapter != null) {
			mAdapter.setData(null);
			mAdapter.clear();
			mAdapter = null;
		}
		dismissPostingDialog();
		GroupMemberService.addListener(null);
		mGroup = null;
		mPostingdialog = null;
		super.onDestroy();

	}

	/**
	 * 动态改变ListView 高度
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(GridView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;

		int num = 4;
		View listItem = listAdapter.getView(0, null, listView);
		listItem.measure(0, 0);
		totalHeight = listItem.getMeasuredHeight();

		int row = listAdapter.getCount() / num
				+ (listAdapter.getCount() % num > 0 ? 1 : 0);
		totalHeight = totalHeight * row;

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getVerticalScrollbarWidth() * (listAdapter
						.getCount() + 2));
		// ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		listView.setLayoutParams(params);

		getActivityLayoutView().invalidate();
	}

	boolean isManager = false;
	private TextView button;
	private boolean isLocalDiscussion;
	private List<ECGroupMember> subMemberList;
	private TextView tvShowMoreMember;
	private ArrayList<ECGroupMember> members = new ArrayList<ECGroupMember>();
	private SettingItem mShowChatName;
	DisplayImageOptions optionsSquare_men;
	DisplayImageOptions optionsSquare_women;

	public class GroupInfoAdapter extends ArrayAdapter<ECGroupMember> {
		Context mContext;
		boolean mHandler;

		public GroupInfoAdapter(Context context) {
			super(context, 0);
			mContext = context;

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
		}

		public void setData(List<ECGroupMember> data) {
			mHandler = false;
			clear();
			if (data != null) {
				for (ECGroupMember appEntry : data) {

					if (CCPAppManager.getUserId().equals(
							appEntry.getVoipAccount())) {
						mHandler = (appEntry.getRole() == 1 || appEntry
								.getRole() == 2);
					}
					add(appEntry);
				}
			}

			isManager = mHandler;
			if (!isDefalut) {

				ECGroupMember add = new ECGroupMember();
				add.setVoipAccount("add@yuntongxun.com");
				add(add);

				if (CCPAppManager.getClientUser().getUserId().equals(adminId)) {
					ECGroupMember delete = new ECGroupMember();
					delete.setVoipAccount("delete@yuntongxun.com");
					add(delete);
				}
			}

			// if (isOwner()) {
			mNameItem.setOnClickListener(new OnItemClickListener(
					SettingsActivity.CONFIG_TYPE_GROUP_NAME));

			mNoticeItem.setOnClickListener(new OnItemClickListener(
					SettingsActivity.CONFIG_TYPE_GROUP_NOTICE));
			// }

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = null;
			ViewHolder mViewHolder;

			if (convertView == null || convertView.getTag() == null) {

				view = View
						.inflate(mContext, R.layout.im_group_grid_item, null);
				mViewHolder = new ViewHolder();
				mViewHolder.mAvatar = (RoundedImageView) view
						.findViewById(R.id.group_card_item_avatar_iv);
				mViewHolder.name_tv = (TextView) view
						.findViewById(R.id.group_card_item_nick);
				mViewHolder.lay = (LinearLayout) view.findViewById(R.id.lay);

				view.setTag(mViewHolder);
			} else {
				view = convertView;
				mViewHolder = (ViewHolder) view.getTag();
			}

			ECGroupMember item = getItem(position);
			// List<ECGroupMember> item2
			// =GroupMemberSqlManager.getGroupMembers(groupId);

			if (item != null) {
				item.setDisplayName(TextUtils.isEmpty(item.getDisplayName()) ? item
						.getVoipAccount() : item.getDisplayName());

				String aString = item.getVoipAccount();

				int a;
				if (CCPAppManager.getClientUser().getUserId().equals(adminId)) {
					a = getCount() - 2;
				} else {
					a = getCount() - 1;
				}

				if ("add@yuntongxun.com".equals(item.getVoipAccount())

				&& position == a) {
					// mViewHolder.lay.setBackgroundResource(R.drawable.add_contact_selector);
					// if (isOwner()) {
					mViewHolder.mAvatar.setImageResource(R.drawable.add_row_2);
					mViewHolder.name_tv.setText("");
					mViewHolder.mAvatar.setVisibility(View.VISIBLE);
					mViewHolder.name_tv.setVisibility(View.VISIBLE);
					// }
					// mViewHolder.mAvatar
					// .setImageResource(R.drawable.add);
					// mViewHolder.name_tv.setText("");

				} else if ("delete@yuntongxun.com"
						.equals(item.getVoipAccount())
						&& position == getCount() - 1) {
					if (CCPAppManager.getClientUser().getUserId()
							.equals(adminId)) {
						mViewHolder.mAvatar.setImageResource(R.drawable.delet);
						mViewHolder.name_tv.setText("");
					} else {
						mViewHolder.mAvatar.setVisibility(View.GONE);
						mViewHolder.name_tv.setVisibility(View.GONE);
					}

				} else {

					mViewHolder.name_tv.setVisibility(View.VISIBLE);
					int sex = item.getSex();
					String avatar = item.getRemark();
					if (!AbStrUtil.isEmpty(avatar)) {
						ImageLoader.getInstance().displayImage(
								WebUrl.FILE_LOAD_URL + avatar,
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

					// Log.e("mGroup.getOwner()", item.getRole()+"");
					// if (item.getVoipAccount().equals(mGroup.getOwner())) {
					// mViewHolder.name_tv
					// .setText(item.getDisplayName()+"(管理员)" );
					// }else {
					mViewHolder.name_tv.setText(item.getDisplayName());
					// }

				}
			}
			return view;
		}

		class ViewHolder {
			/** 头像 */
			RoundedImageView mAvatar;
			/** 名称 */
			TextView name_tv;
			LinearLayout lay;
		}
	}

	/**
	 * 设置群组成员禁言状态
	 * 
	 * @param item
	 */
	private void doSetMemberSpeakStatus(final ECGroupMember item) {
		String msg = getString(R.string.str_group_member_speak_tips,
				item.getDisplayName());
		if (item.isBan()) {
			msg = getString(R.string.str_group_member_unspeak_tips,
					item.getDisplayName());
		}
		ECAlertDialog buildAlert = ECAlertDialog.buildAlert(this, msg,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						showProcessDialog(getString(R.string.login_posting_submit));
						forbidMemberSpeakStatus(mGroup.getGroupId(),
								item.getVoipAccount(), !item.isBan());

					}
				});

		buildAlert.setTitle(R.string.app_tip);
		buildAlert.show();
	}

	private boolean isSuccess(ECError error) {
		if (error.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
			return true;
		}
		return false;
	}

	public void forbidMemberSpeakStatus(final String groupId,
			final String member, final boolean enabled) {

		ESpeakStatus speakStatus = new ESpeakStatus();
		speakStatus.setOperation(enabled ? 2 : 1);
		SDKCoreHelper.getECGroupManager().forbidMemberSpeakStatus(groupId,
				member, speakStatus,
				new ECGroupManager.OnForbidMemberSpeakStatusListener() {
					@Override
					public void onForbidMemberSpeakStatusComplete(
							ECError error, String groupId, String member) {

						isRefresh = true;
						dismissPostingDialog();
						if (isSuccess(error)) {
							GroupMemberSqlManager.updateMemberSpeakState(
									groupId, member, enabled);
							GroupMemberService.synsGroupMember(mGroup
									.getGroupId());

						} else {
							ToastUtil.showMessage("设置失败[" + error.errorCode
									+ "]");
						}
					}

				});
	}

	/**
	 * 移除群组成员
	 * 
	 * @param item
	 */
	private void doRemoveMember(final ECGroupMember item) {
		ECAlertDialog buildAlert = ECAlertDialog.buildAlert(
				this,
				getString(R.string.str_group_member_remove_tips,
						item.getDisplayName()),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						showProcessDialog(getString(R.string.group_remove_member_posting));
						Log.e("aaaaaaaa", item.getVoipAccount() + "++++");
						GroupMemberService.removerMember(mGroup.getGroupId(),
								item.getVoipAccount());
						mClearChatmsg = true;
					}
				});

		buildAlert.setTitle(R.string.app_tip);
		buildAlert.show();
	}

	public void removerMember(String groupid, String member) {

		SDKCoreHelper.getECGroupManager().deleteGroupMember(groupid, member,
				new ECGroupManager.OnDeleteGroupMembersListener() {

					@Override
					public void onDeleteGroupMembersComplete(ECError error,
							String groupId, String members) {
						dismissPostingDialog();
						isRefresh = true;
						if (isSuccess(error)) {
							GroupMemberSqlManager.delMember(groupId, members);
							int position = 0;
							for (int i = 0; i < GroupInfoActivity.this.members
									.size(); i++) {

								ECGroupMember item = GroupInfoActivity.this.members
										.get(i);
								if (item != null
										&& members.equalsIgnoreCase(item
												.getVoipAccount())) {
									position = i;
									break;
								}
							}
							GroupInfoActivity.this.members.remove(position);
							mAdapter.notifyDataSetChanged();

						} else {
							ToastUtil.showMessage("移除成员失败[" + error.errorCode
									+ "]");
						}
					}
				});

	}

	@Override
	public void onSyncGroup() {

	}

	@Override
	public void onSyncGroupInfo(String groupId) {
		dismissPostingDialog();
		if (mGroup == null || !mGroup.getGroupId().equals(groupId)) {
			return;
		}
		mGroup = GroupSqlManager.getDemoGroup(groupId);
		isDis = GroupSqlManager.isDiscussionGroup(groupId);
		// isDis = true;
		isDefalut = false;
		// isDefalut=false;

		refeshGroupInfo();
	}

	@Override
	public void onGroupDel(String groupId) {
		if (mGroup == null || !mGroup.getGroupId().equals(groupId)) {
			return;
		}
		dismissPostingDialog();
		DemoGroup ecGroup = GroupSqlManager.getDemoGroup(mGroup.getGroupId());
		Intent intent = new Intent(this, ChattingActivity.class);
		intent.putExtra(EXTRA_QUEIT, true);
		setResult(RESULT_OK, intent);
		if (ecGroup == null) {
			// 群组被解散
			finish();
			return;
		}
		finish();
		// 更新群组界面 已经退出群组
	}

	@Override
	public void onError(ECError error) {
		dismissPostingDialog();
	}

	@Override
	public void onUpdateGroupAnonymitySuccess(String groupId,
			boolean isAnonymity) {
		// TODO Auto-generated method stub
		dismissCommonPostingDialog();
		ToastUtil.showMessage(R.string.modify_success);
		try {
			ECPreferences.savePreference(
					ECPreferenceSettings.SETTINGS_SHOW_CHATTING_NAME,
					isAnonymity, true);
		} catch (InvalidClassException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final class OnItemClickListener implements View.OnClickListener {

		private int mType;

		public OnItemClickListener(int type) {
			this.mType = type;
		}

		@Override
		public void onClick(View v) {

			if (EditConfigureActivity.isTop) {
				return;
			}

			mEditMode = this.mType;
			Intent intent = new Intent(GroupInfoActivity.this,
					EditConfigureActivity.class);
			if (mEditMode == SettingsActivity.CONFIG_TYPE_GROUP_NAME) {

				if (!isDis) {
					intent.putExtra("edit_title",
							getString(R.string.edit_group_name));
				} else {
					intent.putExtra("edit_title",
							getString(R.string.edit_discussion_name));

				}

				intent.putExtra("edit_default_data", mGroup.getName());
			} else {
				if (isDis) {
					intent.putExtra("edit_title",
							getString(R.string.edit_discussion_notice));

				} else {
					intent.putExtra("edit_title",
							getString(R.string.edit_group_notice));
				}
				intent.putExtra("edit_default_data", mGroup.getDeclare());
			}
			startActivityForResult(intent, 0xa); 
		}
	}

	@Override
	protected void handleReceiver(Context context, Intent intent) {
		super.handleReceiver(context, intent);
		if (IMessageSqlManager.ACTION_GROUP_DEL.equals(intent.getAction())
				&& intent.hasExtra("group_id")) {
			String id = intent.getStringExtra("group_id");
			if (id != null && id.equals(mGroup.getGroupId())) {
				finish();
			}

		}
	}

	public void onAddInGkTeam(boolean isAdd){
		if(!isAdd){
			int deletIndex=-1;
			for(int i=0;i<gkTeamList.size();i++){
				if(groupId.equals(gkTeamList.get(i).getGroup_id())){
					deletIndex=i;
				}
			}
			if(deletIndex!=-1){
				gkTeamList.remove(deletIndex);
			}
		}else{
			GroupItem gi=new ConversationListFragment().new GroupItem();
			gi.setGroup_id(groupId);
			gi.setGroup_name(mGroup.getName());
			gi.setGroup_creator(mGroup.getOwner());
			gkTeamList.add(gi);
		}
		String str=gson.toJson(gkTeamList);
		SharedPreferencesUtil.saveString(mContext, "myGroup", str);
	}
	Gson gson=new Gson();
	boolean isGkTeam=false;
	boolean isDepartTeam=false;
	List<GroupItem> gkList=new ArrayList<GroupItem>();
	List<GroupItem> gkTeamList=new ArrayList<GroupItem>();
	private void getGKdepartGroup() {
		String str = SharedPreferencesUtil.getString(mcontext, "jarrGroup", "");
		List<GroupItem> list = gson.fromJson(str,
				new TypeToken<List<GroupItem>>() {
				}.getType());
		gkList.addAll(list);
		
		for(GroupItem gi:gkList){
			if(groupId.equals(gi.getGroup_id())){
				isDepartTeam=true;
				
			}
		}
		
	}
	private void getGKteamGroup() {
		String str = SharedPreferencesUtil.getString(mcontext, "myGroup", "");
		List<GroupItem> list = gson.fromJson(str,
				new TypeToken<List<GroupItem>>() {
				}.getType());
		gkTeamList.addAll(list);
		
		for(GroupItem gi:gkTeamList){
			if(groupId.equals(gi.getGroup_id())){
				isGkTeam=true;
				
			}
		}
		
	}
	public void upDateGkGroupState(){
		includeGkteam.setChecked(isGkTeam);
		onAddInGkTeam(isGkTeam);
	}
	
	
	
}
