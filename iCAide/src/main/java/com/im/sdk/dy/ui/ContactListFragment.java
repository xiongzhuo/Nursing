package com.im.sdk.dy.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.ui.contact.ContactDetailActivity;
import com.im.sdk.dy.ui.contact.ContactLogic;
import com.im.sdk.dy.ui.contact.ContactSelectListActivity;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;

/**
 * 通讯录界面
 * Created by yung on 2015/12/18.
 */
public class ContactListFragment extends TabFragment {

    /**当前联系人列表类型（查看、联系人选择）*/
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_SELECT = 2;
    public static final int TYPE_NON_GROUP = 3;
    /**当前选择联系人位置*/
    public static ArrayList<Integer> positions = new ArrayList<Integer>();
    /**列表类型*/
    private int mType;
    private ListView mListView;
    private ContactsAdapter mAdapter;
    private OnContactClickListener mClickListener;
    /**选择群组*/
    private View mGroupCardItem;
    ECHandlerHelper mHandlerHelper = new ECHandlerHelper();


    // 设置联系人点击事件通知
    private final AdapterView.OnItemClickListener onItemClickListener
            = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {

            int headerViewsCount = mListView.getHeaderViewsCount();
            if(position < headerViewsCount) {
                return;
            }
            int _position = position - headerViewsCount;

            if(mAdapter == null || mAdapter.getItem(_position) == null) {
                return ;
            }

            if(mType != TYPE_NORMAL) {
                // 如果是选择联系人模式
                Integer object = Integer.valueOf(_position);
                if(positions.contains(object)) {
                    positions.remove(object);
                } else {
                    positions.add(object);
                }
                notifyClick(positions.size());
                mAdapter.notifyDataSetChanged();
                return ;
            }


            ECContacts contacts = mAdapter.getItem(_position);
            if(contacts == null || contacts.getId() == -1) {
                ToastUtil.showMessage(R.string.contact_none);
                return ;
            }
            Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
            intent.putExtra(ContactDetailActivity.RAW_ID, contacts.getId());
            startActivity(intent);
        }
    };

    final private View.OnClickListener mSelectGroupClickListener
            = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onSelectGroupClick();
            }
        }
    };

    /**
     * Create a new instance of ContactListFragment, providing "type"
     * as an argument.
     */
    public static ContactListFragment newInstance(int type) {
        ContactListFragment f = new ContactListFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("type", type);
        f.setArguments(args);

        return f;
    }

    /**
     * 选择的联系人
     */
    public String getChatuser() {
        StringBuilder selectUser = new StringBuilder();
        for(Integer position : positions) {
            ECContacts item = mAdapter.getItem(position);
            if(item != null ) {
                selectUser.append(item.getContactid()).append(",");
            }
        }

        if(selectUser.length() > 0) {
            selectUser.substring(0, selectUser.length() - 1);
        }
        return selectUser.toString();
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments() != null ? getArguments().getInt("type") : TYPE_NORMAL;
        if(positions == null ) {
            positions = new ArrayList<Integer>();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof ContactSelectListActivity) || mType == TYPE_NORMAL) {
            return;
        }
        try {
            mClickListener = (OnContactClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnContactClickListener");
        }
    }

    private void notifyClick(int count) {
        if(mClickListener != null) {
            mClickListener.onContactClick(count);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mListView != null && mGroupCardItem != null) {
            mListView.removeHeaderView(mGroupCardItem);
            mListView.setAdapter(null);
        }
        mListView = (ListView) findViewById(R.id.address_contactlist);
        View emptyView = findViewById(R.id.empty_tip_tv);
        mListView.setEmptyView(emptyView);

        mListView.setOnItemClickListener(onItemClickListener);
        if(mType == TYPE_SELECT) {
            mGroupCardItem = View.inflate(getActivity(), R.layout.im_contacts_add_bar, null);
           mGroupCardItem.findViewById(R.id.creatbtn).setOnClickListener(mSelectGroupClickListener);
           mGroupCardItem.findViewById(R.id.lay_add).setOnClickListener(mSelectGroupClickListener);
            mListView.addHeaderView(mGroupCardItem);
        }

        mHandlerHelper.getTheadHandler().post(new Runnable() {

            @Override
            public void run() {
                final ArrayList<ECContacts> contacts = ContactSqlManager.getContacts();
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mAdapter = new ContactsAdapter(getActivity());
                        mListView.setAdapter(mAdapter);

                        mAdapter.setData(contacts);
                        findViewById(R.id.loading_tips_area).setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    @Override
    protected void onTabFragmentClick() {

    }

    @Override
    protected void onReleaseTabUI() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.im_contacts_fragment;
    }



    class ContactsAdapter extends ArrayAdapter<ECContacts> {

        Context mContext;
        public ContactsAdapter(Context context) {
            super(context, 0);
            mContext = context;
        }


        public void setData(List<ECContacts> data) {
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
            if(convertView == null || convertView.getTag() == null) {
                view = View.inflate(mContext, R.layout.im_contact_list_item, null);

                mViewHolder = new ViewHolder();
                mViewHolder.mAvatar = (ImageView) view.findViewById(R.id.avatar_iv);
                mViewHolder.name_tv = (TextView) view.findViewById(R.id.name_tv);
                mViewHolder.account = (TextView) view.findViewById(R.id.account);
                mViewHolder.checkBox = (CheckBox) view.findViewById(R.id.contactitem_select_cb);

                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder) view.getTag();
            }

            ECContacts contacts = getItem(position);
            if(contacts != null) {
                mViewHolder.mAvatar.setImageBitmap(ContactLogic.getPhoto(contacts.getRemark()));
                mViewHolder.name_tv.setText(contacts.getNickname());
                mViewHolder.account.setText(contacts.getContactid());
            }

            if(mType != TYPE_NORMAL) {
                mViewHolder.checkBox.setVisibility(View.VISIBLE);
                if(mViewHolder.checkBox.isEnabled() && positions != null ) {
                    mViewHolder.checkBox.setChecked(positions.contains(position));
                } else {
                    mViewHolder.checkBox.setChecked(false);
                }
            } else {
                mViewHolder.checkBox.setVisibility(View.GONE);
            }

            return view;
        }

        class ViewHolder {
            /**头像*/
            ImageView mAvatar;
            /**名称*/
            TextView name_tv;
            /**账号*/
            TextView account;
            /**选择状态*/
            CheckBox checkBox;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(positions != null) {
            positions.clear();
            positions = null;
        }
    }

    public interface OnContactClickListener {
        void onContactClick(int count);
        void onSelectGroupClick();
    }

}

