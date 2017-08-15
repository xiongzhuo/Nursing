package com.im.sdk.dy.ui.group;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.HorizontalListView;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.dialog.ECProgressDialog;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.common.view.TopBarView;
import com.im.sdk.dy.core.ContactsCache;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.storage.GroupSqlManager;
import com.im.sdk.dy.ui.ContactListFragment;
import com.im.sdk.dy.ui.ECSuperActivity;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.im.sdk.dy.ui.TabFragment;
import com.im.sdk.dy.ui.TopHoListAdapter;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.ChattingFragment;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.im.sdk.dy.ui.contact.BladeView;
import com.im.sdk.dy.ui.contact.ContactLogic;
import com.im.sdk.dy.ui.contact.CustomSectionIndexer;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.im.sdk.dy.ui.contact.MobileContactActivity;
import com.im.sdk.dy.ui.contact.PinnedHeaderListView;
import com.im.sdk.dy.ui.group.GroupMemberService.GroupServiceInterface;
import com.im.sdk.dy.ui.group.GroupMemberService.OnSynsGroupMemberListener;
import com.im.sdk.dy.ui.group.MobileContactSelectActivity.ContactsFragment.ContactsAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;
import com.yuntongxun.ecsdk.ECGroupManager.OnCreateGroupListener;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECGroupMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.im.sdk.dy.ui.contact in ECDemo_Android
 * Created by Jorstin on 2015/4/1.
 */
public class MobileContactSelectActivity extends ECSuperActivity implements
        View.OnClickListener, ContactListFragment.OnContactClickListener, OnCreateGroupListener, OnSynsGroupMemberListener {
    private ECProgressDialog mPostingdialog;
    private static final String TAG = "IM_PATH.ContactSelectListActivity";
    /**
     * 查看群组
     */
    public static final int REQUEST_CODE_VIEW_GROUP_OWN = 0x2a;
    private TopBarView mTopBarView;
    private boolean mNeedResult;

    private boolean isFromCreateDiscussion = false;


    public static List<Map<String, Object>> horizontalListViewDataList = new ArrayList<Map<String, Object>>();
    public static TopHoListAdapter tAdapter;
    private static HorizontalListView add_listview;
    private static ArrayList<ECGroupMember> members;


    private static PinnedHeaderListView mListView;
    private static ContactsAdapter mAdapter;
    /**
     * 当前选择联系人位置
     */
    public static ArrayList<Integer> positions = new ArrayList<Integer>();
    ContactsFragment list;
    static Tools tools;

    @Override
    protected int getLayoutId() {
        return R.layout.im_layout_contact_select2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tools = new Tools(this, Constants.AC);
        /**群里已有人员*/
        members = (ArrayList<ECGroupMember>) getIntent().getSerializableExtra("members");

        horizontalListViewDataList.clear();
        add_listview = (HorizontalListView) findViewById(R.id.add_listview);
        tAdapter = new TopHoListAdapter(horizontalListViewDataList, this);
        add_listview.setAdapter(tAdapter);
        add_listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                int headerViewsCount = mListView.getHeaderViewsCount();
                if (position < headerViewsCount) {
                    return;
                }
                int _position = (Integer) horizontalListViewDataList.get(position).get("hPosition") - headerViewsCount;

                if (mAdapter == null || mAdapter.getItem(_position) == null) {
                    return;
                }
                // 如果是选择联系人模式
                Integer object = Integer.valueOf(_position);
                if (positions.contains(object)) {
                    positions.remove(object);
                } else {
                    positions.add(object);
                }
                list.notifyClick(positions.size());
                mAdapter.notifyDataSetChanged();
                horizontalListViewDataList.remove(position);
                tAdapter.notifyDataSetChanged();
            }
        });

        FragmentManager fm = getSupportFragmentManager();

        mNeedResult = getIntent().getBooleanExtra("group_select_need_result", false);
        isFromCreateDiscussion = getIntent().getBooleanExtra("isFromCreateDiscussion", false);
        if (fm.findFragmentById(R.id.contact_container) == null) {
            list = new ContactsFragment();
            fm.beginTransaction().add(R.id.contact_container, list).commit();
        }
        mTopBarView = getTopBarView();
        String actionBtn = getString(R.string.radar_ok_count, getString(R.string.dialog_ok_button), 0);
        mTopBarView.setTopBarToStatus(1, R.drawable.btn_back_style, R.color.white, null, actionBtn, getString(R.string.select_contacts), null, this);
        mTopBarView.setRightBtnEnable(false);


        ECGroupManager ecGroupManager = SDKCoreHelper.getECGroupManager();
        if (ecGroupManager == null) {
            finish();
            return;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        isFromCreateDiscussion = false;
    }

    /**
     * 获取讨论组名称
     */
    private ECGroup getDisGroup() {
        ECGroup group = new ECGroup();
        // 设置讨论组名称
        group.setName(getDisGroupName());
        // 设置讨论组公告
        group.setDeclare("");
        group.setScope(ECGroup.Scope.TEMP);
        // 讨论组验证权限，需要身份验证
        group.setPermission(ECGroup.Permission.AUTO_JOIN);
        // 设置讨论组创建者
        group.setOwner(CCPAppManager.getClientUser().getUserId());

        group.setProvince("");
        group.setCity("");
        group.setIsDiscuss(true);
        return group;
    }


    private String getDisGroupName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CCPAppManager.getClientUser().getUserName());
        stringBuilder.append("、");
        for (int i = 0; i < memberArrs.length; i++) {
            if (i == 5) {
                break;
            }
            stringBuilder.append(memberArrs[i]);
            if (!(i == memberArrs.length - 1)) {
                stringBuilder.append("、");
            }
        }
        stringBuilder.append("创建的讨论组");

        return stringBuilder.toString();
    }

    private String[] phoneArr;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
            case R.id.btn_text_left:
                goBack();
                break;
            case R.id.text_right:
                List<Fragment> fragments = getSupportFragmentManager().getFragments();

                if (fragments.get(0) instanceof MobileContactSelectActivity.ContactsFragment) {
                    String chatuser = ((MobileContactSelectActivity.ContactsFragment) fragments.get(0)).getChatuser();
                    String[] split = chatuser.split(",");
                    String userNameArr = ((MobileContactSelectActivity.ContactsFragment) fragments.get(0)).getChatuserName();
                    memberArrs = userNameArr.split(",");
                    phoneArr = split;

                    contactlist = ((MobileContactSelectActivity.ContactsFragment) fragments.get(0)).getUsers();

                    if (split.length == 1 && !mNeedResult) {
                        String recipient = split[0];
                        CCPAppManager.startChattingAction(MobileContactSelectActivity.this, recipient, recipient, 0);
                        finish();
                        return;
                    }

                    if (mNeedResult && isFromCreateDiscussion) {
                        if (split != null && split.length > 0) {
                            if (isFromCreateDiscussion) {
                                SDKCoreHelper.getECGroupManager().createGroup(getDisGroup(), this);
                            }
                            return;
                        }
                    }
                    if (mNeedResult) {
                        Intent intent = new Intent();
                        intent.putExtra("Select_Conv_User", split);
                        intent.putExtra("contactlist", contactlist);
                        setResult(-1, intent);
                        finish();
                        return;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GroupMemberService.addListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBack() {
        hideSoftKeyboard();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
                + ", resultCode=" + resultCode + ", data=" + data);

        // If there's no data (because the user didn't select a picture and
        // just hit BACK, for example), there's nothing to do.
        if (requestCode == REQUEST_CODE_VIEW_GROUP_OWN) {
            if (data == null) {
                return;
            }
        } else if (resultCode != RESULT_OK) {
            LogUtil.d("onActivityResult: bail due to resultCode=" + resultCode);
            return;
        }

        String contactId = data.getStringExtra(ChattingFragment.RECIPIENTS);
        String contactUser = data.getStringExtra(ChattingFragment.CONTACT_USER);
        if (contactId != null && contactId.length() > 0) {
            Intent intent = new Intent(this, ChattingActivity.class);
            intent.putExtra(ChattingFragment.RECIPIENTS, contactId);
            intent.putExtra(ChattingFragment.CONTACT_USER, contactUser);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onContactClick(int count) {
        mTopBarView.setRightBtnEnable(count > 0 ? true : false);
        mTopBarView.setRightButtonText(getString(R.string.radar_ok_count, getString(R.string.dialog_ok_button), count));
    }

    @Override
    public void onSelectGroupClick() {

    }

    private ECGroup mGroup;
    private String[] memberArrs;
    public static ArrayList<ECContacts> contactlist;

    @Override
    public void onCreateGroupComplete(ECError error, ECGroup group) {


        if (error.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
            // 创建的群组实例化到数据库
            // 其他的页面跳转逻辑
            group.setIsNotice(true);
            this.mGroup = group;
            GroupSqlManager.insertGroup(group, true, false, true, false);

            showCommonProcessDialog("");

            GroupMemberService.inviteMembers(mGroup.getGroupId(), "",
                    ECGroupManager.InvitationMode.FORCE_PULL, phoneArr, contactlist, new GroupServiceInterface() {

                        @Override
                        public void onInvateComplet() {
                            // TODO Auto-generated method stub

                        }
                    });
        } else {
            ToastUtil.showMessage("创建讨论组失败[" + error.errorCode + "]");
            finish();
        }
    }

    @Override
    public void onSynsGroupMember(String groupId) {
        dismissCommonPostingDialog();
        CCPAppManager.startChattingAction(MobileContactSelectActivity.this, groupId,
                mGroup.getName(), 0);
        finish();

    }

    public static class ContactsFragment extends TabFragment {
        private static final String TAG = "IMSDK.ContactsFragment";

        /**
         * 当前联系人列表类型（查看、联系人选择）
         */
        public static final int TYPE_NORMAL = 1;
        public static final int TYPE_SELECT = 2;
        /**
         * 列表类型
         */
        private int mType;
        private String[] sections = {"#", "A", "B", "C", "D", "E", "F", "G",
                "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
                "T", "U", "V", "W", "X", "Y", "Z"};
        private static final String ALL_CHARACTER = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        /**
         * 每个字母最开始的位置
         */
        private HashMap<String, Integer> mFirstLetters;

        /**
         * 每个首字母对应的position
         */
        private String[] mLetterPos;
        private List<ECContacts> contacts;
        private List<ECContacts> contacts_conta;
        private ContactListFragment.OnContactClickListener mClickListener;
        /**
         * 每个姓氏第一次出现对应的position
         */
        private int[] counts;
        private String mSortKey = "#";

        private CustomSectionIndexer mCustomSectionIndexer;

        /**
         * 选择联系人
         */
        private View mSelectCardItem;
        DisplayImageOptions optionsSquare_men;
        DisplayImageOptions optionsSquare_women;

        // 设置联系人点击事件通知
        private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                if (contacts.get(position).getIsAlreadyAdd() == 0) {


                    int headerViewsCount = mListView.getHeaderViewsCount();
                    if (position < headerViewsCount) {
                        return;
                    }
                    int _position = position - headerViewsCount;

                    if (mAdapter == null || mAdapter.getItem(_position) == null) {
                        return;
                    }
                    // 如果是选择联系人模式
                    Integer object = Integer.valueOf(_position);
                    if (positions.contains(object)) {
                        positions.remove(object);
                    } else {
                        positions.add(object);
                    }
                    notifyClick(positions.size());
                    mAdapter.notifyDataSetChanged();


                    add_listview.setVisibility(View.VISIBLE);
                    boolean needAdd = true;
                    int removePosition = 0;
                    for (int i = 0; i < horizontalListViewDataList.size(); i++) {


                        if ((Integer) horizontalListViewDataList.get(i).get("hPosition") == position) {
                            removePosition = i;
                            needAdd = false;
                        }
                    }

                    if (needAdd) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("hIv", contacts.get(position).getAvatar());
                        map.put("hSex", contacts.get(position).getSex());
                        map.put("hPosition", position);
                        horizontalListViewDataList.add(map);
                    } else {
                        horizontalListViewDataList.remove(removePosition);
                    }

                    tAdapter.notifyDataSetChanged();
                }
//				ECContacts contacts = mAdapter.getItem(_position);
//				if (contacts == null || contacts.getId() == -1) {
//					ToastUtil.showMessage(R.string.contact_none);
//					return;
//				}
            }
        };

        private BladeView mLetterListView;

        /**
         * Create a new instance of ContactListFragment, providing "type" as an
         * argument.
         */
        public static ContactsFragment newInstance(int type) {
            ContactsFragment f = new ContactsFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("type", type);
            f.setArguments(args);

            return f;
        }

        @Override
        protected int getLayoutId() {
            return R.layout.im_mobile_contacts_activity;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mType = getArguments() != null ? getArguments().getInt("type")
                    : TYPE_NORMAL;
            if (positions == null) {
                positions = new ArrayList<Integer>();
            }

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

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            if (!(activity instanceof MobileContactSelectActivity)
                    || mType == TYPE_NORMAL) {
                return;
            }
            try {
                mClickListener = (ContactListFragment.OnContactClickListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnContactClickListener");
            }
        }

        public void notifyClick(int count) {
            if (mClickListener != null) {
                mClickListener.onContactClick(count);
            }
        }

        /**
         * 选择的联系人
         */
        public String getChatuser() {
            StringBuilder selectUser = new StringBuilder();
            for (Integer position : positions) {
                ECContacts item = mAdapter.getItem(position);
                ContactSqlManager.insertContact(item);
                if (item != null) {
                    selectUser.append(item.getContactid()).append(",");
                }
            }

            if (selectUser.length() > 0) {
                selectUser.substring(0, selectUser.length() - 1);
            }
            return selectUser.toString();
        }

        public String getChatuserName() {
            StringBuilder selectUser = new StringBuilder();
            for (Integer position : positions) {
                ECContacts item = mAdapter.getItem(position);
                ContactSqlManager.insertContact(item);
                if (item != null) {
                    selectUser.append(item.getNickname()).append(",");
                }
            }

            if (selectUser.length() > 0) {
                selectUser.substring(0, selectUser.length() - 1);
            }
            return selectUser.toString();
        }

        public ArrayList<ECContacts> getUsers() {
            ArrayList<ECContacts> list = new ArrayList<ECContacts>();
//			for (Integer position : positions) {
//				list.add( mAdapter.getItem(position));
//			}
            return list;

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            registerReceiver(new String[]{ContactsCache.ACTION_ACCOUT_DY_CONTACTS});
            if (mListView != null) {
                mListView.setAdapter(null);
            }
            mListView = (PinnedHeaderListView) findViewById(R.id.address_contactlist);
            mListView.setOnItemClickListener(onItemClickListener);
            initContactListView();
            findView();
        }

        /**
         * 初始化联系人列表
         */
        private void initContactListView() {
            if (mListView != null && mSelectCardItem != null) {
                mListView.removeHeaderView(mSelectCardItem);
                mListView.setAdapter(null);
            }

            mAdapter = new ContactsAdapter(getActivity());

            ArrayList<ECContacts> temp = ContactLogic.getContacts();

            //筛选
            for (int i = 0; i < temp.size(); i++) {

            }
            contacts = temp;

            if (null != contacts) {
                counts = new int[sections.length];
                for (ECContacts c : contacts) {

                    String firstCharacter = c.getSortKey();
                    int index = ALL_CHARACTER.indexOf(firstCharacter);
                    counts[index]++;
                }
                if (contacts != null && !contacts.isEmpty()) {
                    mSortKey = contacts.get(0).getSortKey();
                }
                mCustomSectionIndexer = new CustomSectionIndexer(sections,
                        counts);

                mAdapter.setData(contacts, mCustomSectionIndexer);

                contacts_conta = temp;


//				for (ECGroupMember m:members) {
//					if (m.getVoipAccount().equals(ContactsFragment.this.contacts.get(position).getContactid())) {
//						ContactsFragment.this.contacts.get(position).setIsAlreadyAdd(1);
//					}
//				}
                for (int i = 0; i < contacts.size(); i++) {

                    for (int j = 0; j < members.size(); j++) {
                        if (members.get(j).getVoipAccount().equals(contacts.get(i).getContactid())) {
                            contacts.get(i).setIsAlreadyAdd(1);
                        }
                    }
                }

            }
            if (mType == TYPE_NORMAL) {
                mSelectCardItem = View.inflate(getActivity(),
                        R.layout.im_contacts_add_bar, null);
                mSelectCardItem.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(getActivity(),
                                MobileContactActivity.class);
                        getActivity().startActivity(intent);
                    }
                });
                //	mListView.addHeaderView(mSelectCardItem);
            }
            mListView.setAdapter(mAdapter);
            mListView.setOnScrollListener(mAdapter);
            // 設置頂部固定頭部
            mListView.setPinnedHeaderView(LayoutInflater.from(getActivity())
                    .inflate(R.layout.im_header_item_cator, mListView, false));
            findViewById(R.id.loading_tips_area).setVisibility(View.GONE);
        }

        @Override
        public void onResume() {
            super.onResume();
            mLetterListView = (BladeView) findViewById(R.id.mLetterListView);
            showLetter(mLetterListView);
            contacts = ContactLogic.contacts;
            if (null != contacts && contacts.size() > 0) {
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode,
                                     Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
                    + ", resultCode=" + resultCode + ", data=" + data);

            if (requestCode == 0xa) {
                if (data == null) {
                    return;
                }
            } else if (resultCode != RESULT_OK) {
                LogUtil.d("onActivityResult: bail due to resultCode="
                        + resultCode);
                return;
            }
            if (requestCode == 0xa) {
                String result_data = data.getStringExtra("result_data");
                if (TextUtils.isEmpty(result_data)
                        || result_data.trim().length() == 0) {
                    ToastUtil.showMessage(R.string.mobile_list_empty);
                    return;
                }
                // IMAppManager.startChattingAction(ContactsFragment.this.getActivity()
                // , result_data , result_data , true);
            }
        }

        private void findView() {

            BladeView mLetterListView = (BladeView) findViewById(R.id.mLetterListView);
            showLetter(mLetterListView);
            mLetterListView
                    .setOnItemClickListener(new BladeView.OnItemClickListener() {

                        @Override
                        public void onItemClick(String s) {
                            if (s != null && ALL_CHARACTER != null
                                    && mCustomSectionIndexer != null) {
                                int section = ALL_CHARACTER.indexOf(s);
                                int position = mCustomSectionIndexer
                                        .getPositionForSection(section);
                                if (position != -1) {
                                    if (position != 0) {
                                        position = position + 1;
                                    }
                                    mListView.setSelection(position);
                                }
                            }

                        }
                    });

        }

        private void showLetter(BladeView mLetterListView) {
            if (mLetterListView == null) {
                return;
            }
            boolean showBanView = (contacts != null && !contacts.isEmpty());
            mLetterListView.setVisibility(showBanView ? View.VISIBLE
                    : View.GONE);
        }

        @Override
        protected void onTabFragmentClick() {

        }

        @Override
        protected void onReleaseTabUI() {

        }

        @Override
        public void onDetach() {
            super.onDetach();
            if (positions != null) {
                positions.clear();
                positions = null;
            }
            if (mLetterListView != null) {
                mLetterListView.removeDis();
            }
        }

        @Override
        protected void handleReceiver(Context context, Intent intent) {
            super.handleReceiver(context, intent);
            if (ContactsCache.ACTION_ACCOUT_DY_CONTACTS.equals(intent
                    .getAction())) {
                LogUtil.d("handleReceiver ACTION_ACCOUT_DY_CONTACTS");
                initContactListView();
            }
        }

        class ContactsAdapter extends ArrayAdapter<ECContacts> implements
                PinnedHeaderListView.PinnedHeaderAdapter,
                AbsListView.OnScrollListener {
            CustomSectionIndexer mIndexer;
            Context mContext;
            private int mLocationPosition = -1;

            public ContactsAdapter(Context context) {
                super(context, 0);
                mContext = context;
            }

            public void setData(List<ECContacts> data,
                                CustomSectionIndexer indexer) {
                mIndexer = indexer;
                setNotifyOnChange(false);
                clear();
                setNotifyOnChange(true);
                if (data != null) {
                    for (ECContacts appEntry : data) {
                        add(appEntry);
                    }
                }


            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view;
                ViewHolder mViewHolder;
                if (convertView == null || convertView.getTag() == null) {
                    view = View.inflate(mContext,
                            R.layout.im_contacts_list_item, null);

                    mViewHolder = new ViewHolder();
                    mViewHolder.mAvatar = (ImageView) view
                            .findViewById(R.id.avatar_iv);
                    mViewHolder.name_tv = (EmojiconTextView) view
                            .findViewById(R.id.name_tv);
                    mViewHolder.account = (TextView) view
                            .findViewById(R.id.account);
                    mViewHolder.checkBox = (CheckBox) view
                            .findViewById(R.id.contactitem_select_cb);
                    mViewHolder.tvCatalog = (TextView) view
                            .findViewById(R.id.contactitem_catalog);
                    mViewHolder.jop = (TextView) view.findViewById(R.id.jop);
                    view.setTag(mViewHolder);
                } else {
                    view = convertView;
                    mViewHolder = (ViewHolder) view.getTag();
                }
                mViewHolder.checkBox.setVisibility(View.VISIBLE);
//				ECContacts contacts = getItem(position);


//				contactlist.get();
                ECContacts contacts1 = getItem(position);

                int section = mIndexer.getSectionForPosition(position);
                if (mIndexer.getPositionForSection(section) == position) {
                    mViewHolder.tvCatalog.setVisibility(View.VISIBLE);
                    mViewHolder.tvCatalog.setText(contacts1.getSortKey());
                } else {
                    mViewHolder.tvCatalog.setVisibility(View.GONE);
                }

                if (null != contacts1) {
                    mViewHolder.tvCatalog.setText(contacts1.getSortKey());
                }
                Log.i("IMtag", contacts1.getSortKey());
                ECContacts contacts = ContactSqlManager.getContact(contacts1.getId()
                );


                if (contacts != null) {

                    if (ContactsFragment.this.contacts.get(position).getIsAlreadyAdd() == 1) {
                        mViewHolder.checkBox.setBackgroundResource(R.drawable.un_check);
                    } else {
                        mViewHolder.checkBox.setBackgroundResource(R.drawable.chekbox_selector);
                    }


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

                    mViewHolder.name_tv.setText(contacts.getRname());
//					mViewHolder.account.setText(contacts.getMobile());
                    String department = contacts.getDepartment();
                    String hospital = contacts.getHospital();
                    if (!AbStrUtil.isEmpty(hospital)) {
                        Log.i("IMtag", hospital);
                    }
                    if (!AbStrUtil.isEmpty(hospital)
                            && hospital.equals(tools
                            .getValue(Constants.HOSPITAL_NAME))) {
                        mViewHolder.account.setText(department);
                        if ("".equals(department)) {
                            mViewHolder.account.setText(hospital);
                        }
                    } else {
                        mViewHolder.account.setText(hospital);
                    }


                    if (!AbStrUtil.isEmpty(contacts.getRegis_job())) {
                        mViewHolder.jop.setText(contacts.getRegis_job());
                    } else {
                        mViewHolder.jop.setText("其他");
                    }

                    if (mViewHolder.checkBox.isEnabled()
                            && positions != null) {
                        mViewHolder.checkBox.setChecked(positions
                                .contains(position));
                    } else {
                        mViewHolder.checkBox.setChecked(false);
                    }
                }

                return view;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (view instanceof PinnedHeaderListView) {
                    ((PinnedHeaderListView) view)
                            .configureHeaderView(firstVisibleItem);
                }
            }

            @Override
            public int getPinnedHeaderState(int position) {
                int realPosition = position - 1;
                if (realPosition < 0
                        || (mLocationPosition != -1 && mLocationPosition == realPosition)) {
                    return PINNED_HEADER_GONE;
                }
                mLocationPosition = -1;
                int section = mIndexer.getSectionForPosition(realPosition);
                int nextSectionPosition = mIndexer
                        .getPositionForSection(section + 1);
                if (nextSectionPosition != -1
                        && realPosition == nextSectionPosition - 1) {
                    return PINNED_HEADER_PUSHED_UP;
                }
                return PINNED_HEADER_VISIBLE;
            }

            @Override
            public void configurePinnedHeader(View header, int position,
                                              int alpha) {
                int realPosition = position;
                int _position = position - 1;
                if (_position < 0)
                    return;
                TextView headView = ((TextView) header
                        .findViewById(R.id.contactitem_catalog));
                if (_position == 0) {
                    headView.setText(mSortKey);
                    return;
                }
                ECContacts item = getItem(_position);
                if (item != null) {
                    headView.setText(item.getSortKey());
                }
            }

            class ViewHolder {
                /**
                 * 头像
                 */
                ImageView mAvatar;
                /**
                 * 名称
                 */
                EmojiconTextView name_tv;
                /**
                 * 账号
                 */
                TextView account;
                /**
                 * 选择状态
                 */
                CheckBox checkBox;
                TextView tvCatalog, jop;
            }
        }

    }

}
