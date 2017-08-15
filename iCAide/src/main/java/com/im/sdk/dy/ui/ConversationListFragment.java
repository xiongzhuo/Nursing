package com.im.sdk.dy.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.base.OverflowAdapter;
import com.im.sdk.dy.common.base.OverflowHelper;
import com.im.sdk.dy.common.dialog.ECListDialog;
import com.im.sdk.dy.common.dialog.ECProgressDialog;
import com.im.sdk.dy.common.utils.ECPreferenceSettings;
import com.im.sdk.dy.common.utils.ECPreferences;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.common.view.NetWarnBannerView;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.storage.ConversationSqlManager;
import com.im.sdk.dy.storage.GroupNoticeSqlManager;
import com.im.sdk.dy.storage.GroupSqlManager;
import com.im.sdk.dy.storage.IMessageSqlManager;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.ChattingFragment;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.im.sdk.dy.ui.chatting.model.Conversation;
import com.im.sdk.dy.ui.contact.ContactLogic;
import com.im.sdk.dy.ui.contact.ContactsActivity;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.im.sdk.dy.ui.group.DemoGroup;
import com.im.sdk.dy.ui.group.GroupNoticeActivity;
import com.im.sdk.dy.ui.group.GroupService;
import com.im.sdk.dy.ui.group.GroupsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECGroupOption;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息列表界面
 *
 * @author yung
 * @version 1.0
 * @date 创建时间：2015年12月3日 下午4:02:32
 */
public class ConversationListFragment extends BaseFragment implements
        CCPListAdapter.OnListAdapterCallBackListener, OnClickListener,
        GroupService.Callback {

    private static final String TAG = "IMSDK.ConversationListFragment";

    protected static final int HELPER_SUCESS = 0x31902;

    protected static final int HELPER_FAILE = 0x31903;

    private OverflowHelper mOverflowHelper;
    private OverflowAdapter.OverflowItem[] mItems;

    /**
     * 会话消息列表ListView
     */
    private ListView mListView;
    private NetWarnBannerView mBannerView;
    private ConversationAdapter mAdapter;
    private OnUpdateMsgUnreadCountsListener mAttachListener;
    private ECProgressDialog mPostingdialog;
    // private TopBarView barView;
    private GroupItemAdapter itemAdapter;
    private TextView tipcnt_tv, tipcnt_tv02;
    List<ECGroup> departGropList = new ArrayList<ECGroup>();
    LinearLayout empertyView;

    /*
     * 消息列表 item 点击事件
     */
    final private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View visew,
                                int position, long id) {

            if (mAdapter != null) {
                int headerViewsCount = mListView.getHeaderViewsCount();
                if (position < headerViewsCount) {
                    return;
                }
                int _position = position - headerViewsCount;

                if (mAdapter == null || mAdapter.getItem(_position) == null) {
                    return;
                }

                Conversation conversation = mAdapter.getItem(_position);

                if (GroupNoticeSqlManager.CONTACT_ID.equals(conversation
                        .getSessionId())) {
                    Intent intent = new Intent(getActivity(),
                            GroupNoticeActivity.class);
                    startActivity(intent);
                    return;
                }

                if (conversation.getContactsType() == 1000) {//工作提醒
                    Intent intent = new Intent(getActivity(), TaskTipsActivity.class);

                    long mThread = ConversationSqlManager
                            .querySessionIdForBySessionId(conversation.getSessionId());
                    ConversationSqlManager.setChattingSessionRead(mThread);
                    intent.putExtra("mThread", mThread);
                    startActivity(intent);

                    return;
                }

                if (ContactLogic.isCustomService(conversation.getSessionId())) {
                    showProcessDialog();
                    return;
                }


                // 跳转到聊天界面
                CCPAppManager.startChattingAction(getActivity(),
                        conversation.getSessionId(),
                        conversation.getUsername(),
                        conversation.getContactsType());

                // CCPAppManager.startChattingAction(getActivity(), "29",
                // "感控助手",
                // 1);
            }
        }
    };

    /**
     * 消息长按事件
     */
    private final AdapterView.OnItemLongClickListener mOnLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            if (mAdapter != null) {
                int headerViewsCount = mListView.getHeaderViewsCount();
                if (position < headerViewsCount) {
                    return false;
                }
                int _position = position - headerViewsCount;

                if (mAdapter == null || mAdapter.getItem(_position) == null) {
                    return false;
                }
                Conversation conversation = mAdapter.getItem(_position);
                final int itemPosition = position;
                String[] menu = buildMenu(conversation);
                ECListDialog dialog = new ECListDialog(getActivity(), /*
                                                                     * new
																	 * String
																	 * []{
																	 * getString
																	 * (
																	 * R.string.
																	 * main_delete
																	 * )}
																	 */menu);
                dialog.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
                    @Override
                    public void onDialogItemClick(Dialog d, int position) {
                        handleContentMenuClick(itemPosition, position);
                    }
                });
                dialog.setTitle(conversation.getUsername());
                dialog.show();
                return true;
            }
            return false;
        }
    };

    private String[] buildMenu(Conversation conversation) {
        if (conversation != null && conversation.getSessionId() != null) {
            if (conversation.getSessionId().toLowerCase().startsWith("g")) {
                DemoGroup ecGroup = GroupSqlManager.getDemoGroup(conversation
                        .getSessionId());
                if (ecGroup == null
                        || !GroupSqlManager.getJoinState(ecGroup.getGroupId())) {
                    return new String[]{getString(R.string.main_delete)};
                }
                if (ecGroup.isNotice()) {
                    return new String[]{getString(R.string.main_delete),
                            getString(R.string.menu_mute_notify)};
                }
                return new String[]{getString(R.string.main_delete),
                        getString(R.string.menu_notify)};
            }
        }
        return new String[]{getString(R.string.main_delete)};
    }

    // @Override
    // protected void onTabFragmentClick() {
    //
    // }
    //
    // @Override
    // protected void onReleaseTabUI() {
    //
    // }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private ContactsReceiver contactsReceiver;

    private class ContactsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                return;
            }
            if (SDKCoreHelper.ACTION_CONTACTS_CHANGE.equals(intent.getAction())) {
                LogUtil.d("ACTION_CONTACTS_CHANGE", "mAdapter notifyChange>>");
                mAdapter.notifyChange();
            }

            if ("com.deyamanda.gkgj".equals(intent.getAction())) {

                showNotice();

            }
        }
    }

    /**
     * 未读提醒
     */
    public void showNotice() {
        int a = SharedPreferencesUtil.getInt(getActivity(), "gk29", 0);
        if (a > 0) {
            tipcnt_tv.setVisibility(View.VISIBLE);
            tipcnt_tv.setText("" + a);
        } else {
            tipcnt_tv.setVisibility(View.GONE);
        }

        // int b=SharedPreferencesUtil.getInt(getActivity(), "gk31", 0);
        // if (b > 0) {
        // tipcnt_tv02.setVisibility(View.VISIBLE);
        // tipcnt_tv02.setText("" + b);
        // }else{
        // tipcnt_tv02.setVisibility(View.GONE);
        // }
    }

    @Override
    public void onDestroy() {
        mAttachListener = null;
        defultGroupListView=null;
        defultGroupView=null;
        super.onDestroy();
    }

    public Activity activity;
    protected View rootView;
    private LayoutInflater inflater;
    private View helpView;
    private Tools tools;
    LinearLayout expertsLay, helperLay, teamLay;// 顶部感控专家，小助手，感控团队
    private View defultGroupView;
    private ListView defultGroupListView;
    List<GroupItem> defultGrouplist = new ArrayList<GroupItem>();

    private DisplayImageOptions optionsSquare_women;

    private DisplayImageOptions optionsSquare_group;

    private boolean sync = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.im_conversation, container,
                    false);
            this.inflater = inflater;
            GroupService.syncGroup(this);
            initView();
        } else {
            // 缓存的rootView需要判断是否已经被加过parent
            // 如果有parent需要从parent删除
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        mOverflowHelper = new OverflowHelper(getActivity());
        optionsSquare_women = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.im_women_defalut)
                .showImageForEmptyUri(R.drawable.im_women_defalut)
                .showImageOnFail(R.drawable.im_women_defalut)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .considerExifParams(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();

        tools = new Tools(getActivity(), Constants.AC);
        return rootView;
    }

    private void getDeFultGroup() {
        String str = SharedPreferencesUtil.getString(getActivity(),
                "jarrGroup", "");
        List<GroupItem> list = gson.fromJson(str,
                new TypeToken<List<GroupItem>>() {
                }.getType());
        List<String> IdSTR = GroupSqlManager.getAllGroupId();

        Log.i("IMtag", str);

        if (null != list && list.size() > 0) {
            // mListView.removeAllViews();
            defultGrouplist.clear();
            defultGrouplist.addAll(list);
            itemAdapter.notifyDataSetChanged();
            String ids[] = new String[list.size()];
            for (GroupItem item : list) {

            }
            for (int i = 0; i < list.size(); i++) {
                ids[i] = list.get(i).getGroup_id();

            }
            mAdapter.retdata(ids);

        }

    }

    public class GroupItem {
        String group_name;
        String group_id;
        String group_creator;

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getGroup_creator() {
            return group_creator;
        }

        public void setGroup_creator(String group_creator) {
            this.group_creator = group_creator;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        showNotice();

        // getUnreadNum();

        // long sessionId = ConversationSqlManager
        // .querySessionIdForBySessionId("29");
        // ECMessage
        // message=IMessageSqlManager.getLastHelperECMessage(sessionId+"");

        // IMessageSqlManager.getLastECMessage();
        updateConnectState();
        IMessageSqlManager.registerMsgObserver(mAdapter);
        mAdapter.resetDarfData();

        registerReceiver(new String[]{
                getActivity().getPackageName() + ".inited",
                IMessageSqlManager.ACTION_GROUP_DEL});

        if (!sync) {
            GroupService.syncDiscussionGroup(this);
            sync = true;
        }
        getDeFultGroup();
        // if(null==MyAppliaction.picMeaasgelist||MyAppliaction.picMeaasgelist.size()==0)
        // sendPicMessagerequst();

//		IntentFilter intentfilter = new IntentFilter();
//		intentfilter.addAction(SDKCoreHelper.ACTION_CONTACTS_CHANGE);
//		if (contactsReceiver == null) {
//			contactsReceiver = new ContactsReceiver();
//		}
//		ECContacts contact = ContactSqlManager.getContact("29");
//		SetAvatar(helper1HeadImg, contact.getAvatar());
//		ECContacts contact2 = ContactSqlManager.getContact("31");
//		SetAvatar(helper2HeadImg, contact2.getAvatar());
//		getActivity().registerReceiver(contactsReceiver, intentfilter);
//
//		/** 消息未读广播 */
//		IntentFilter intentFilter = new IntentFilter("com.deyamanda.gkgj");
//		getActivity().registerReceiver(contactsReceiver, intentFilter);

        //
        showChatting();

        updateConnectState();
        // IMessageSqlManager.registerMsgObserver(mAdapter);
        mAdapter.notifyChange();
    }

    private void getUnreadNum() {
        long ownThreadId = ConversationSqlManager
                .querySessionIdForBySessionId("29");
        if (ownThreadId == 0) {
            try {
                ECMessage message = ECMessage
                        .createECMessage(ECMessage.Type.NONE);
                message.setForm("29");
                message.setSessionId("29");
                ownThreadId = ConversationSqlManager
                        .insertSessionRecord(message);

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG + " " + e.toString());
            }
        }

    }

    /**
     * 处理notify 跳转过来的消息，跳转到响应的页面
     */
    private void showChatting() {
        Intent intent = getActivity().getIntent();
        if (null != intent) {
            ECContacts mContacts = intent.getParcelableExtra("contacts");
            if (null != mContacts) {
                CCPAppManager.startChattingAction(getActivity(),
                        mContacts.getContactid(), mContacts.getRname(),
                        mContacts.getAvatar(), true, mContacts.getType());
                intent.removeExtra("contacts");
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mAttachListener = (OnUpdateMsgUnreadCountsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUpdateMsgUnreadCountsListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        IMessageSqlManager.unregisterMsgObserver(mAdapter);
        if (contactsReceiver != null) {
            getActivity().unregisterReceiver(contactsReceiver);
        }
    }

    private void disPostingLoading() {
        if (mPostingdialog != null && mPostingdialog.isShowing()) {
            mPostingdialog.dismiss();
        }
    }

    /**
     *
     */
    private void initView() {

        // submit=(TextView)rootView.findViewById(R.id.submit);
        // submit.setText("添加");
        // submit.setOnClickListener(this);

        // if(mListView != null) {
        // mListView.setAdapter(null);
        //
        // if(mBannerView != null) {
        // mListView.removeHeaderView(mBannerView);
        // }
        // }
        // barView = (TopBarView) rootView.findViewById(R.id.lay_top);
        // rootView.findViewById(R.id.lay_search).setOnClickListener(this);
        // barView.setTopBarToStatus(1, -1, getString(R.string.text_add),
        // getString(R.string.str_tab_home), this);

        // barView.setRightVisible();
        // barView.setRightButtonText("添加");
        // barView.setTopBarToStatus(1, -1, -1,
        // getString(R.string.str_tab_home),
        // this);

        mListView = (ListView) rootView.findViewById(R.id.main_chatting_lv);
        View mEmptyView = rootView.findViewById(R.id.empty_conversation_tv);
        // mListView.setEmptyView(mEmptyView);
        mListView.setDrawingCacheEnabled(false);
        mListView.setScrollingCacheEnabled(false);

        mListView.setOnItemLongClickListener(mOnLongClickListener);
        mListView.setOnItemClickListener(mItemClickListener);

        mBannerView = new NetWarnBannerView(getActivity());
        mBannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reTryConnect();
            }
        });
        empertyView= (LinearLayout) rootView.findViewById(R.id.empertyView);
        mListView.setEmptyView(empertyView);
        // mListView.addHeaderView(mBannerView);

        /** 通讯录 */
        // View v_header = View.inflate(getActivity(),
        // R.layout.im_conversation_item, null);
        // RoundedImageView img = (RoundedImageView) v_header
        // .findViewById(R.id.avatar_iv);
        // img.setImageResource(R.drawable.im_contacts);
        // img.setOnClickListener(contactsClick);
        // EmojiconTextView text1 = (EmojiconTextView) v_header
        // .findViewById(R.id.nickname_tv);
        // text1.setText(getString(R.string.main_contact));
        // text1.setOnClickListener(contactsClick);
        // v_header.findViewById(R.id.lay_item).setOnClickListener(contactsClick);
        //
        // EmojiconTextView text2 = (EmojiconTextView) v_header
        // .findViewById(R.id.last_msg_tv);
        // text2.setOnClickListener(contactsClick);
        // text2.setText(getString(R.string.main_contact_str));
        // v_header.setOnClickListener(contactsClick);
        // mListView.addHeaderView(v_header);

        /** 群组 */
        // View v_header2 = View.inflate(getActivity(),
        // R.layout.im_conversation_item, null);
        // ((RoundedImageView) v_header2.findViewById(R.id.avatar_iv))
        // .setImageResource(R.drawable.im_contacts);
        // ((EmojiconTextView) v_header2.findViewById(R.id.nickname_tv))
        // .setText(getString(R.string.main_group));
        // ((EmojiconTextView) v_header2.findViewById(R.id.last_msg_tv))
        // .setText(getString(R.string.main_group_str));
        // v_header2.setOnClickListener(groupClick);
        // mListView.addHeaderView(v_header2);

        /** 感控助手 */
        // ConversationSqlManager.querySessionIdForBySessionId("29");

        // mAdapter.getConversationSnippet(conversation);

        // 初始化 感控专家、团队、小助手
//        expertsLay = (LinearLayout) rootView.findViewById(R.id.expertsLay);
//        expertsLay.setOnClickListener(this);
//        helperLay = (LinearLayout) rootView.findViewById(R.id.helperLay);
//        helperLay.setOnClickListener(this);
//        teamLay = (LinearLayout) rootView.findViewById(R.id.teamLay);
//        teamLay.setOnClickListener(this);

        ECContacts contact = ContactSqlManager.getContact("29");// 通过friendId获取联系人
        helpView = View.inflate(getActivity(), R.layout.im_conversation_item,
                null);

        tipcnt_tv = (TextView) helpView.findViewById(R.id.tipcnt_tv);
        helper1HeadImg = ((ImageView) helpView.findViewById(R.id.avatar_iv));
//		((EmojiconTextView) helpView.findViewById(R.id.nickname_tv))
//				.setText("感控小助手");
//		SetAvatar(helper1HeadImg, contact.getAvatar());
//		((EmojiconTextView) helpView.findViewById(R.id.last_msg_tv))
//				.setText("感控小助手");
//		((TextView) helpView.findViewById(R.id.update_time_tv)).setText("");
//
//		helpView.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 跳转到聊天界面
//				SharedPreferencesUtil.saveInt(getActivity(), "gk29", 0);
//				CCPAppManager.startChattingAction(getActivity(), "29", "感控小助手",
//
//				1);
//			}
//		});
        defultGroupView = View.inflate(getActivity(),
                R.layout.item_defultgrouplist, null);
        defultGroupListView = (ListView) defultGroupView
                .findViewById(R.id.degultgrouListView);
        itemAdapter = new GroupItemAdapter(getActivity(), defultGrouplist);
        defultGroupListView.setAdapter(itemAdapter);
        defultGroupListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 跳转到群组聊天界面

                Intent intent = new Intent(getActivity(),
                        ChattingActivity.class);
                intent.putExtra(ChattingFragment.RECIPIENTS, defultGrouplist
                        .get(position).getGroup_id());
                intent.putExtra(ChattingFragment.CONTACT_USER, defultGrouplist
                        .get(position).getGroup_name());
                intent.putExtra(ChattingFragment.CUSTOMER_SERVICE, false);
                intent.putExtra(ChattingFragment.CONTACTS_TYPE, 0);
                intent.putExtra(ChattingFragment.IS_MY_DEPART_GROU, false);
                intent.putExtra(ChattingFragment.DEPART_GROU_OWNER,
                        defultGrouplist.get(position).getGroup_creator());

                getActivity().startActivity(intent);

            }
        });
        /** 感控专家 */
        ECContacts contact2 = ContactSqlManager.getContact("31");
        View helpView2 = View.inflate(getActivity(),
                R.layout.im_conversation_item, null);

        tipcnt_tv02 = (TextView) helpView2.findViewById(R.id.tipcnt_tv);
        helper2HeadImg = ((ImageView) helpView2.findViewById(R.id.avatar_iv));
        SetAvatar(helper2HeadImg, contact2.getAvatar());
        ((EmojiconTextView) helpView2.findViewById(R.id.nickname_tv))
                .setText("感控专家");
        ((EmojiconTextView) helpView2.findViewById(R.id.last_msg_tv))
                .setText("感控专家");
        ((TextView) helpView2.findViewById(R.id.update_time_tv)).setText("");

        // helpView2.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // 跳转到聊天界面
        // SharedPreferencesUtil.saveInt(getActivity(), "gk31", 0);
        // CCPAppManager.startChattingAction(getActivity(), "31", "感控专家",
        // 1);
        // }
        // });

        //mListView.addHeaderView(helpView);
        // mListView.addHeaderView(helpView2);
        //mListView.addHeaderView(defultGroupView);
        mAdapter = new ConversationAdapter(getActivity(), this);
        if (null != mAdapter && null != mListView) {
            mListView.setAdapter(mAdapter);
            registerForContextMenu(mListView);
        }
    }

    ImageView helper1HeadImg;
    ImageView helper2HeadImg;

    private void SetAvatar(ImageView img, String avatar) {
        if (!AbStrUtil.isEmpty(avatar)) {
            ImageLoader.getInstance().displayImage(
                    WebUrl.FILE_LOAD_URL + avatar, img, optionsSquare_women);
        } else {
            ImageLoader.getInstance()
                    .displayImage("", img, optionsSquare_women);
        }
    }

    private static final int PICMSSAGE_SUCESS = 0x30003;
    private static final int PICMSSAGE_FAILE = 0x30004;

    Gson gson = new Gson();

    OnClickListener contactsClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ContactsActivity.class);
            startActivity(intent);
        }
    };

    OnClickListener groupClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), GroupsActivity.class);
            startActivity(intent);
        }
    };

    private String getAutoRegistAccount() {
        SharedPreferences sharedPreferences = ECPreferences
                .getSharedPreferences();
        ECPreferenceSettings registAuto = ECPreferenceSettings.SETTINGS_REGIST_AUTO;
        String registAccount = sharedPreferences.getString(registAuto.getId(),
                (String) registAuto.getDefaultValue());
        return registAccount;
    }

    private void reTryConnect() {
        // 初始化，获取服务器链接状态
        // ECDevice.ECConnectState connectState =
        // SDKCoreHelper.getConnectState();
        // if(connectState == null || connectState ==
        // ECDevice.ECConnectState.CONNECT_FAILED) {
        //
        // if(!TextUtils.isEmpty(getAutoRegistAccount())){
        // SDKCoreHelper.init(getActivity());
        // }
        // }
    }

    public void updateConnectState() {
        if (!isAdded()) {
            return;
        }
        // 获取服务器链接状态
        // if(连接中) {
        // mBannerView.setNetWarnText(getString(R.string.connecting_server));
        // mBannerView.reconnect(true);
        // } else if (链接失败) {
        // mBannerView.setNetWarnText(getString(R.string.connect_server_error));
        // mBannerView.reconnect(false);
        // } else if (链接成功) {
        // mBannerView.hideWarnBannerView();
        // }

        mBannerView.hideWarnBannerView();
    }

    private Boolean handleContentMenuClick(int convresion, int position) {
        if (mAdapter != null) {
            int headerViewsCount = mListView.getHeaderViewsCount();
            if (convresion < headerViewsCount) {
                return false;
            }
            int _position = convresion - headerViewsCount;

            if (mAdapter == null || mAdapter.getItem(_position) == null) {
                return false;
            }
            final Conversation conversation = mAdapter.getItem(_position);
            switch (position) {
                case 0:
                    showProcessDialog();
                    ECHandlerHelper handlerHelper = new ECHandlerHelper();
                    handlerHelper.postRunnOnThead(new Runnable() {
                        @Override
                        public void run() {
                            IMessageSqlManager.deleteChattingMessage(conversation
                                    .getSessionId());
                            ToastUtil.showMessage(R.string.clear_msg_success);
                            ConversationListFragment.this.getActivity()
                                    .runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismissPostingDialog();
                                            mAdapter.notifyChange();
                                        }
                                    });
                        }
                    });
                    break;
                case 1:
                    showProcessDialog();
                    final boolean notify = GroupSqlManager
                            .isGroupNotify(conversation.getSessionId());
                    ECGroupOption option = new ECGroupOption();
                    option.setGroupId(conversation.getSessionId());
                    option.setRule(notify ? ECGroupOption.Rule.SILENCE
                            : ECGroupOption.Rule.NORMAL);
                    GroupService.setGroupMessageOption(option,
                            new GroupService.GroupOptionCallback() {
                                @Override
                                public void onComplete(String groupId) {
                                    if (mAdapter != null) {
                                        mAdapter.notifyChange();
                                    }
                                    ToastUtil
                                            .showMessage(notify ? R.string.new_msg_mute_notify
                                                    : R.string.new_msg_notify);
                                    dismissPostingDialog();
                                }

                                @Override
                                public void onError(ECError error) {
                                    dismissPostingDialog();
                                    ToastUtil.showMessage("设置失败");
                                }
                            });
                    break;
                default:
                    break;
            }
        }
        return null;
    }

    // @Override
    // protected int getLayoutId() {
    // return R.layout.im_conversation;
    // }

    @Override
    public void OnListAdapterCallBack() {
        if (mAttachListener != null) {
            mAttachListener.OnUpdateMsgUnreadCounts();
        }
    }

    public interface OnUpdateMsgUnreadCountsListener {
        void OnUpdateMsgUnreadCounts();
    }

    // @Override
    // protected void handleReceiver(Context context, Intent intent) {
    // super.handleReceiver(context, intent);
    // if(GroupService.ACTION_SYNC_GROUP.equals(intent.getAction())
    // || IMessageSqlManager.ACTION_SESSION_DEL.equals(intent.getAction())) {
    // if(mAdapter != null) {
    // mAdapter.notifyChange();
    // }
    // }
    // }

    void showProcessDialog() {
        mPostingdialog = new ECProgressDialog(
                ConversationListFragment.this.getActivity(),
                R.string.login_posting_submit);
        mPostingdialog.show();
    }

    /**
     * 关闭对话框
     */
    private void dismissPostingDialog() {
        if (mPostingdialog == null || !mPostingdialog.isShowing()) {
            return;
        }
        mPostingdialog.dismiss();
        mPostingdialog = null;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            // case R.id.lay_search:
            // Intent intent = new Intent(getActivity(), SearchActivity.class);
            // intent.putExtra(SearchActivity.FROM,
            // SearchActivity.FROM_CONVERSATION);
            // startActivity(intent);
            // break;
            // case R.id.text_right:
            // // Intent intentAdd = new Intent(getActivity(),
            // // CreateGroupActivity.class);
            // // startActivity(intentAdd);
            // controlPlusSubMenu();
            // break;
//            case R.id.expertsLay:
//                onStartActivity(GKExpertActivity.class);
//                break;
//            case R.id.helperLay:
//                onStartActivity(GKHelperActivity.class);
//                break;
//            case R.id.teamLay:
//                onStartActivity(GKTeamActivity.class);
//                break;
            default:
                break;
        }
    }

    public void onStartActivity(Class<?> T) {
        Intent intent = new Intent(getActivity(), T);
        getActivity().startActivity(intent);
    }

    // private void controlPlusSubMenu() {
    // if (mOverflowHelper == null) {
    // return;
    // }
    //
    // if (mOverflowHelper.isOverflowShowing()) {
    // mOverflowHelper.dismiss();
    // return;
    // }
    //
    // if(mItems == null) {
    // initOverflowItems();
    // }
    // LogUtil.d(mOverflowHelper+"");
    // mOverflowHelper.setOverflowItems(mItems);
    // mOverflowHelper
    // .setOnOverflowItemClickListener(mOverflowItemCliclListener);
    // mOverflowHelper.showAsDropDown(barView.findViewById(R.id.text_right));
    // }

    // private final AdapterView.OnItemClickListener mOverflowItemCliclListener
    // = new AdapterView.OnItemClickListener() {
    //
    // @Override
    // public void onItemClick(AdapterView<?> parent, View view, int position,
    // long id) {
    // controlPlusSubMenu();
    //
    // OverflowItem overflowItem= mItems[position];
    // String title=overflowItem.getTitle();
    //
    // if (getString(R.string.main_plus_groupchat).equals(title)) {
    // // 创建群组
    // startActivity(new Intent(getActivity(),
    // CreateGroupActivity.class));
    // }
    // // else if (getString(R.string.main_plus_querygroup).equals(title)) {
    // // // 群组搜索
    // // startActivity(new Intent(getActivity(),BaseSearch.class));
    // // } else if (getString(R.string.main_plus_mcmessage).equals(title)) {
    // // handleStartServiceEvent();
    // //
    // // } else if (getString(R.string.main_plus_settings).equals(title)) {
    // // // 设置;
    // // startActivity(new Intent(getActivity(),SettingsActivity.class));
    // //
    // //
    // // }
    // else if(getString(R.string.create_discussion).equals(title)){
    //
    // Intent intent=new Intent(getActivity(),
    // MobileContactSelectActivity.class);
    // intent.putExtra("is_discussion", true);
    // intent.putExtra("isFromCreateDiscussion", true);
    // intent.putExtra("group_select_need_result", true);
    // startActivity(intent);
    //
    //
    // }
    // // else if(getString(R.string.query_discussion).equals(title)){
    // //
    // // Intent intent=new Intent(getActivity(), ECDiscussionActivity.class);
    // // intent.putExtra("is_discussion", true);
    // //
    // // startActivity(intent);
    // //
    // // }
    // }
    //
    // };

    /**
     * 根据底层库是否支持voip加载相应的子菜单
     */
    void initOverflowItems() {
        if (mItems == null) {
            if (SDKCoreHelper.getInstance().isSupportMedia()) {
                mItems = new OverflowAdapter.OverflowItem[7];
                mItems[0] = new OverflowAdapter.OverflowItem(
                        getString(R.string.main_plus_inter_phone));
                mItems[1] = new OverflowAdapter.OverflowItem(
                        getString(R.string.main_plus_meeting_voice));
                mItems[2] = new OverflowAdapter.OverflowItem(
                        getString(R.string.main_plus_meeting_video));
                mItems[3] = new OverflowAdapter.OverflowItem(
                        getString(R.string.main_plus_groupchat));
                mItems[4] = new OverflowAdapter.OverflowItem(
                        getString(R.string.main_plus_querygroup));

                mItems[5] = new OverflowAdapter.OverflowItem(
                        getString(R.string.create_discussion));

                mItems[6] = new OverflowAdapter.OverflowItem(
                        getString(R.string.main_plus_settings));

            } else {
                mItems = new OverflowAdapter.OverflowItem[4];
                mItems[0] = new OverflowAdapter.OverflowItem(
                        getString(R.string.main_plus_groupchat));
                mItems[1] = new OverflowAdapter.OverflowItem(
                        getString(R.string.main_plus_querygroup));

                mItems[2] = new OverflowAdapter.OverflowItem(
                        getString(R.string.create_discussion));

                mItems[3] = new OverflowAdapter.OverflowItem(
                        getString(R.string.main_plus_settings));

            }
        }

    }

    @Override
    public void onSyncGroup() {
        getDeFultGroup();

    }

    @Override
    public void onSyncGroupInfo(String groupId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGroupDel(String groupId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError(ECError error) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdateGroupAnonymitySuccess(String groupId,
                                              boolean isAnonymity) {
        // TODO Auto-generated method stub

    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.im_conversation;
    }

}
