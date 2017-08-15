package com.im.sdk.dy.ui.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.HorizontalListView;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.base.CCPClearEditText;
import com.im.sdk.dy.common.dialog.ECProgressDialog;
import com.im.sdk.dy.common.utils.DemoUtils;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.common.view.TopBarView;
import com.im.sdk.dy.core.ContactsCache;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.storage.GroupSqlManager;
import com.im.sdk.dy.ui.ContactListFragment;
import com.im.sdk.dy.ui.ConversationListFragment;
import com.im.sdk.dy.ui.ConversationListFragment.GroupItem;
import com.im.sdk.dy.ui.ECSuperActivity;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.im.sdk.dy.ui.TabFragment;
import com.im.sdk.dy.ui.TopHoListAdapter;
import com.im.sdk.dy.ui.account.AccountLogic;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.ChattingFragment;
import com.im.sdk.dy.ui.chatting.IMChattingHelper;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.im.sdk.dy.ui.contact.MobileContactSelectActivity.ContactsFragment.ContactsAdapter;
import com.im.sdk.dy.ui.group.CreateGroupActivity;
import com.im.sdk.dy.ui.group.GroupMemberService;
import com.im.sdk.dy.ui.group.GroupMemberService.GroupServiceInterface;
import com.im.sdk.dy.ui.group.GroupMemberService.OnSynsGroupMemberListener;
import com.im.sdk.dy.ui.group.SearchGroupActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;
import com.yuntongxun.ecsdk.ECGroupManager.OnCreateGroupListener;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.im.sdk.dy.ui.contact in ECDemo_Android Created by Jorstin on 2015/4/1.
 */
public class MobileContactSelectActivity extends ECSuperActivity implements
        View.OnClickListener, ContactListFragment.OnContactClickListener,
        OnCreateGroupListener, OnSynsGroupMemberListener {

    /**
     * 查看群组
     */
    public static final int REQUEST_CODE_VIEW_GROUP_OWN = 0x2a;
    private TopBarView mTopBarView;
    private boolean mNeedResult;
    private boolean isAddGk;

    private boolean isFromCreateDiscussion = true;

    public static List<Map<String, Object>> horizontalListViewDataList = new ArrayList<Map<String, Object>>();
    public static TopHoListAdapter tAdapter;
    private static HorizontalListView add_listview;
    private CCPClearEditText searchEdtView;

    private static PinnedHeaderListView mListView;
    private static ContactsAdapter mAdapter;
    /**
     * 当前选择联系人位置
     */
    public static ArrayList<Integer> positions = new ArrayList<Integer>();
    ContactsFragment list;

    /**
     * 创建群
     */
    private static final String TAG = "ECDemo.CreateGroupActivity";
    /**
     * 群组名称
     */
    private EditText mNameEdit;

    /**
     * 创建按钮
     */
    private Button mCreateBtn;
    /**
     * 创建的群组
     */
    private ECGroup group;
    private ECProgressDialog mPostingdialog;
    private ScrollView scroll;

    // private TextView text_name_t;
    static Tools tools;

    class MyTextWatcher implements TextWatcher {
        TextView textView;
        int max;
        int textResId;
        EditText editText;
        private CharSequence temp;

        public MyTextWatcher(EditText editText, TextView textView, int max,
                             int textResId) {
            this.textView = textView;
            this.max = max;
            this.textResId = textResId;
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            if ((temp.length() > max)) {
                editText.setText(temp.subSequence(0, max));
                Selection.setSelection(editText.getText(), editText.getText()
                        .length());
            } else {
                textView.setText(String.format(res.getString(textResId),
                        s.length(), max));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.im_layout_contact_select;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tools = new Tools(this, Constants.AC);
        initView();
        horizontalListViewDataList.clear();
        add_listview = (HorizontalListView) findViewById(R.id.add_listview);
        searchEdtView = (CCPClearEditText) findViewById(R.id.search_flite);
        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = filter;
        searchEdtView.setFilters(inputFilters);
        searchEdtView.addTextChangedListener(textWatcher);
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
                int _position = (Integer) horizontalListViewDataList.get(
                        position).get("hPosition")
                        - headerViewsCount;

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

        // mNeedResult = getIntent().getBooleanExtra("group_select_need_result",
        // false);
        mNeedResult = true;
        isFromCreateDiscussion = true;
        if (getIntent().hasExtra("isGkTeam")) {
            isAddGk = true;
        }
        if (fm.findFragmentById(R.id.contact_container) == null) {
            list = new ContactsFragment();
            fm.beginTransaction().add(R.id.contact_container, list).commit();
        }
        mTopBarView = getTopBarView();
        String actionBtn = getString(R.string.radar_ok_count,
                getString(R.string.dialog_ok_button), 0);
        mTopBarView.setTopBarToStatus(1, R.drawable.btn_back_style,
                R.color.white, null, actionBtn,
                getString(R.string.select_contacts), null, this);
        mTopBarView.setRightBtnEnable(false);

        ECGroupManager ecGroupManager = SDKCoreHelper.getECGroupManager();
        if (ecGroupManager == null) {
            finish();
            return;
        }
    }


    final private TextWatcher textWatcher = new TextWatcher() {

        private int fliteCounts = 20;
        ;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mAdapter.setSearchData(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            LogUtil.d(LogUtil.getLogUtilsTag(textWatcher.getClass()), "fliteCounts=" + fliteCounts);
            fliteCounts = fliteCounts(s);
            if (fliteCounts < 0) {
                fliteCounts = 0;
            }

        }
    };

    public static int fliteCounts(CharSequence text) {
        int count = (30 - Math.round(calculateCounts(text)));
        LogUtil.v(LogUtil.getLogUtilsTag(SearchGroupActivity.class), "count " + count);
        return count;
    }


    final InputFilter filter = new InputFilter() {

        private int limit = 30;

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            LogUtil.i(LogUtil.getLogUtilsTag(SearchGroupActivity.class), source
                    + " start:" + start + " end:" + end + " " + dest
                    + " dstart:" + dstart + " dend:" + dend);
            float count = calculateCounts(dest);
            int overplus = limit - Math.round(count) - (dend - dstart);
            if (overplus <= 0) {
                if ((Float.compare(count, (float) (limit - 0.5D)) == 0)
                        && (source.length() > 0)
                        && (!(DemoUtils.characterChinese(source.charAt(0))))) {
                    return source.subSequence(0, 1);
                }
                return "";
            }

            if (overplus >= (end - start)) {
                return null;
            }
            int tepmCont = overplus + start;
            if ((Character.isHighSurrogate(source.charAt(tepmCont - 1))) && (--tepmCont == start)) {
                return "";
            }
            return source.subSequence(start, tepmCont);
        }

    };

    @Override
    protected void onDestroy() {
        isFromCreateDiscussion = true;
        if (mPostingdialog != null && mPostingdialog.isShowing()) {
            mPostingdialog.dismiss();
        }
        super.onDestroy();

    }

    /**
     * 获取讨论组名称
     */
    private ECGroup getDisGroup() {
        ECGroup group = new ECGroup();
        // 设置讨论组名称
        group.setName(mNameEdit.getText().toString().trim());
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
    String[] split;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
            case R.id.btn_text_left:
                goBack();
                break;
            case R.id.text_right:
                List<Fragment> fragments = getSupportFragmentManager()
                        .getFragments();

                if (fragments.get(0) instanceof MobileContactSelectActivity.ContactsFragment) {
                    String chatuser = ((MobileContactSelectActivity.ContactsFragment) fragments
                            .get(0)).getChatuser();
                    split = chatuser.split(",");
                    String userNameArr = ((MobileContactSelectActivity.ContactsFragment) fragments
                            .get(0)).getChatuserName();
                    memberArrs = userNameArr.split(",");

                    if (memberArrs.length > 0) {
                        // ECGroup group = getGroup();
                        ECGroup group = getDisGroup();
                        if (!checkNameEmpty()) {
                            ToastUtil.showMessage("请输入群组名称");
                            return;
                        }
                        hideSoftKeyboard();
                        ECGroupManager ecGroupManager = SDKCoreHelper
                                .getECGroupManager();
                        if (ecGroupManager == null) {
                            return;
                        }
                        // 调用API创建群组、处理创建群组接口回调

                        // isDiscussion ? R.string.create_dis_posting

                        mPostingdialog = new ECProgressDialog(this,
                                R.string.create_group_posting);
                        // mPostingdialog.show();

                        ecGroupManager.createGroup(group, this);
                    }

                    phoneArr = split;

                    contactlist = ((MobileContactSelectActivity.ContactsFragment) fragments
                            .get(0)).getUsers();

                    if (split.length == 1 && !mNeedResult) {
                        String recipient = split[0];
                        CCPAppManager.startChattingAction(
                                MobileContactSelectActivity.this, recipient,
                                recipient, 0);
                    }

                    return;
                }
                break;

            default:
                break;
        }
    }

    // 设置感控团队
    public void onSetGkGroup(String id) {
        JSONObject job = new JSONObject();
        try {
            job.put(Constants.AUTHENT, tools.getValue(Constants.AUTHENT));
            job.put("group_id", id);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomRequest(myHandler,
                MobileContactSelectActivity.this, GK_SET_SUCCESS, GK_SET_FAIL,
                job, "iminfo/setMyTeam");

    }

    public static final int GK_SET_FAIL = 0x4323;
    public static final int GK_SET_SUCCESS = 0x4324;
    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case GK_SET_SUCCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setDeletResult(new JSONObject(msg.obj.toString()));
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
        GroupMemberService.addListener(this);
    }

    Gson gson = new Gson();


    protected void setDeletResult(JSONObject jsonObject) {
        String str = SharedPreferencesUtil.getString(mcontext, "myGroup", "");
        List<GroupItem> list = gson.fromJson(str,
                new TypeToken<List<GroupItem>>() {
                }.getType());
        List<GroupItem> gkList = new ArrayList<GroupItem>();
        gkList.addAll(list);
        GroupItem gi = new ConversationListFragment().new GroupItem();
        gi.setGroup_id(group.getGroupId());
        gi.setGroup_name(group.getName());
        gi.setGroup_creator(group.getOwner());
        gkList.add(gi);
        String str2 = gson.toJson(gkList);
        SharedPreferencesUtil.saveString(mContext, "myGroup", str2);
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

            String[] selectUser = data.getStringArrayExtra("Select_Conv_User");
            // ArrayList<ECContacts>
            // contacts=(ArrayList<ECContacts>)data.getSerializableExtra("contactlist");
            ArrayList<ECContacts> contacts = contactlist;
            if (selectUser != null && selectUser.length > 0) {
                mPostingdialog = new ECProgressDialog(this,
                        R.string.invite_join_group_posting);
                mPostingdialog.show();
                GroupMemberService.inviteMembers(group.getGroupId(), "",
                        ECGroupManager.InvitationMode.FORCE_PULL, selectUser,
                        contacts, new GroupServiceInterface() {

                            @Override
                            public void onInvateComplet() {
                                // TODO Auto-generated method stub

                            }
                        });
            }

        }
    }

    @Override
    public void onContactClick(int count) {
        mTopBarView.setRightBtnEnable(count > 0 ? true : false);
        mTopBarView.setRightButtonText(getString(R.string.radar_ok_count,
                getString(R.string.dialog_ok_button), count));
    }

    @Override
    public void onSelectGroupClick() {

    }

    private ECGroup mGroup;
    private String[] memberArrs;
    private ArrayList<ECContacts> contactlist;

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

                    if ((Integer) horizontalListViewDataList.get(i).get(
                            "hPosition") == position) {
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
                // ECContacts contacts = mAdapter.getItem(_position);
                // if (contacts == null || contacts.getId() == -1) {
                // ToastUtil.showMessage(R.string.contact_none);
                // return;
                // }
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
            // for (Integer position : positions) {
            // list.add( mAdapter.getItem(position));
            // }
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

            // 筛选
            // for (int i = 0; i < temp.size(); i++) {
            //
            // }
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
                // mListView.addHeaderView(mSelectCardItem);
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
            List<ECContacts> data;
            private int mLocationPosition = -1;

            public ContactsAdapter(Context context) {
                super(context, 0);
                mContext = context;
            }

            public void setData(List<ECContacts> data,
                                CustomSectionIndexer indexer) {
                this.data = data;

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

            public void setSearchData(String str) {
                if (data != null) {
                    setNotifyOnChange(false);
                    clear();
                    setNotifyOnChange(true);
                    for (ECContacts appEntry : data) {
                        ECContacts contacts = ContactSqlManager.getContact(appEntry
                                .getId());
                        boolean macher = contacts.getDepartment().contains(str) || contacts.getNickname().contains(str) || contacts.getRegis_job().contains(str);
                        if (macher) {

                            Log.i("contacts", macher + "");
                            add(appEntry);
                        }
                    }
                    notifyDataSetChanged();
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
                ECContacts contacts = ContactSqlManager.getContact(contacts1
                        .getId());

                // ECContacts contacts = getItem(position);
                if (contacts != null) {

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
                    // mViewHolder.account.setText(contacts.getMobile());

                    if (mViewHolder.checkBox.isEnabled() && positions != null) {
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

    /**
     *
     */
    private void initView() {
        // text_name_t = (TextView) findViewById(R.id.text_name_t);
        mNameEdit = (EditText) findViewById(R.id.group_name);
        // mNameEdit.addTextChangedListener(new MyTextWatcher(mNameEdit,
        // text_name_t, 10, R.string.group_name_t));

        mNameEdit.setText("");
        scroll = (ScrollView) findViewById(R.id.scroll);

    }

    /**
     * @return
     */
    private boolean checkNameEmpty() {
        return mNameEdit != null
                && mNameEdit.getText().toString().trim().length() > 0;
    }

    /**
     * 创建群组参数
     *
     * @return
     */
    private ECGroup getGroup() {
        ECGroup group = new ECGroup();
        // 设置群组名称
        group.setName(mNameEdit.getText().toString().trim());
        // // 设置群组公告
        // group.setDeclare(mNoticeEdit.getText().toString().trim());
        // 临时群组（100人）
        group.setScope(ECGroup.Scope.NORMAL_SENIOR);
        // 群组验证权限，需要身份验证
        group.setPermission(ECGroup.Permission.AUTO_JOIN);
        // 设置群组创建者
        group.setOwner(CCPAppManager.getClientUser().getUserId());
        //
        group.setIsDiscuss(true);

        group.setGroupDomain("DY={'a':'XXX','t':1}");
        return group;
    }

    @Override
    public void onCreateGroupComplete(ECError error, final ECGroup group) {
        if (error.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
            // 创建的群组实例化到数据库
            // 其他的页面跳转逻辑
            group.setIsNotice(true);

            // 修改群组名片
            AccountLogic.setMemberInfo(group.getGroupId(),
                    CCPAppManager.getClientUser());

            GroupSqlManager.insertGroup(group, true, false, false, false);
            this.group = group;

            // Intent intent = new Intent(this,
            // MobileContactSelectActivity.class);
            // intent.putExtra("group_select_need_result", true);
            // intent.putExtra("isFromCreateDiscussion", false);
            // startActivityForResult(intent, 0x2a);

            if (mNeedResult) {
                // Intent intent = new Intent();
                // intent.putExtra("Select_Conv_User", split);
                // intent.putExtra("contactlist", contactlist);
                // setResult(-1, intent);

                String[] selectUser = split;
                // ArrayList<ECContacts>
                // contacts=(ArrayList<ECContacts>)data.getSerializableExtra("contactlist");
                ArrayList<ECContacts> contacts = contactlist;
                if (selectUser != null && selectUser.length > 0) {
                    mPostingdialog = new ECProgressDialog(this,
                            R.string.invite_join_group_posting);
                    mPostingdialog.show();
                    GroupMemberService.inviteMembers(group.getGroupId(), "",
                            ECGroupManager.InvitationMode.FORCE_PULL,
                            selectUser, contacts, new GroupServiceInterface() {

                                @Override
                                public void onInvateComplet() {

                                }
                            });
                    if (!isAddGk) {
                        handleSendTextMessage("我们的群创建成功啦！", group.getGroupId());
                    } else {
                        onSetGkGroup(group.getGroupId());
                    }
                    // finish();
                }
                return;
            }

        } else {
            ToastUtil.showMessage("创建群组失败[" + error.errorCode + "]");
        }
        dismissPostingDialog();
    }

    /**
     * 处理文本发送方法事件通知
     *
     * @param text
     */
    private void handleSendTextMessage(CharSequence text, String mRecipients) {
        if (text == null) {
            return;
        }
        if (text.toString().trim().length() <= 0) {
            return;
        }
        // 组建一个待发送的ECMessage
        ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
        // 设置消息接收者
        msg.setTo(mRecipients);
        msg.setSessionId(mRecipients);
        DebugUtil.debug("iniIm", "to id>>" + mRecipients);

        // 创建一个文本消息体，并添加到消息对象中
        ECTextMessageBody msgBody = new ECTextMessageBody(text.toString());

        msg.setBody(msgBody);
        try {
            // 发送消息，该函数见上
            long rowId = -1;
            rowId = IMChattingHelper.sendECMessage(msg);
            // 通知列表刷新
            msg.setId(rowId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSynsGroupMember(String groupId) {
        dismissPostingDialog();
        if (!AbStrUtil.isEmpty(groupId)) {
            Log.e("", "");
        }
        ECGroup group = GroupSqlManager.getECGroup1(groupId);
        CCPAppManager.startChattingAction(MobileContactSelectActivity.this,
                groupId, group.getName(), 0);
        finish();
    }

    /**
     * @param text
     * @return
     */
    public static int filteCounts(CharSequence text) {
        int count = (30 - Math.round(calculateCounts(text)));
        LogUtil.v(LogUtil.getLogUtilsTag(SearchGroupActivity.class), "count "
                + count);
        return count;
    }

    /**
     * @param text
     * @return
     */
    public static float calculateCounts(CharSequence text) {

        float lengh = 0.0F;
        for (int i = 0; i < text.length(); i++) {
            if (!DemoUtils.characterChinese(text.charAt(i))) {
                lengh += 1.0F;
            } else {
                lengh += 0.5F;
            }
        }

        return lengh;
    }

    class ITextFilter implements InputFilter {
        private int limit = 50;

        public ITextFilter() {
            this(0);
        }

        public ITextFilter(int type) {
            if (type == 1) {
                limit = 128;
            }
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            LogUtil.i(LogUtil.getLogUtilsTag(CreateGroupActivity.class), source
                    + " start:" + start + " end:" + end + " " + dest
                    + " dstart:" + dstart + " dend:" + dend);
            float count = calculateCounts(dest);
            int overplus = limit - Math.round(count) - (dend - dstart);
            if (overplus <= 0) {
                if ((Float.compare(count, (float) (limit - 0.5D)) == 0)
                        && (source.length() > 0)
                        && (!(DemoUtils.characterChinese(source.charAt(0))))) {
                    return source.subSequence(0, 1);
                }
                ToastUtil.showMessage("超过最大限制");
                return "";
            }

            if (overplus >= (end - start)) {
                return null;
            }
            int tepmCont = overplus + start;
            if ((Character.isHighSurrogate(source.charAt(tepmCont - 1)))
                    && (--tepmCont == start)) {
                return "";
            }
            return source.subSequence(start, tepmCont);
        }
    }

}
