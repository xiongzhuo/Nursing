/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.im.sdk.dy.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.DemoUtils;
import com.im.sdk.dy.storage.GroupSqlManager;
import com.im.sdk.dy.storage.IMessageSqlManager;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.im.sdk.dy.ui.contact.ContactLogic;
import com.im.sdk.dy.ui.group.ApplyWithGroupPermissionActivity;
import com.im.sdk.dy.ui.group.CreateGroupActivity;
import com.im.sdk.dy.ui.group.DemoGroup;
import com.im.sdk.dy.ui.group.GroupInfoActivity;
import com.im.sdk.dy.ui.group.GroupService;
import com.yuntongxun.ecsdk.ECError;


/**
 * 群组界面
 * @author 容联•云通讯
 * @date 2014-12-4
 * @version 4.0
 */
public class GroupListFragment extends TabFragment implements GroupService.Callback{

    /**群组列表*/
    private ListView mListView;
    /**群组列表信息适配器*/
    private GroupAdapter mGroupAdapter;
    public static boolean sync = false;
    
    boolean isDiscussion=false;
    
    private boolean isRefresh=false;
    /**
     * 群组列表点击事件
     */
    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if(mGroupAdapter != null) {
            	int headerViewsCount = mListView.getHeaderViewsCount();
				if (position < headerViewsCount) {
					return;
				}
				int _position = position - headerViewsCount;
				
                DemoGroup dGroup = mGroupAdapter.getItem(_position);
                if(dGroup.isJoin()) {
                    CCPAppManager.startChattingAction(getActivity() , dGroup.getGroupId() , dGroup.getName(),0);
                    return ;
                }
                Intent intent = new Intent(getActivity() , ApplyWithGroupPermissionActivity.class);
                intent.putExtra(GroupInfoActivity.GROUP_ID, dGroup.getGroupId());
                startActivity(intent);
            }
        }

    };

    @Override
    protected void onTabFragmentClick() {

    }

    @Override
    protected void onReleaseTabUI() {

    }

    @Override
    protected int getLayoutId() {

        return R.layout.im_groups_activity;
    }
    View mSelectCardItem;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mListView != null && mSelectCardItem != null) {
			mListView.removeHeaderView(mSelectCardItem);
			mListView.setAdapter(null);
		}
        
        if(mListView != null) {
            mListView.setAdapter(null);
        }
       
        mListView = (ListView) findViewById(R.id.group_list);
        View emptyView = findViewById(R.id.empty_tip_tv);
       // mListView.setEmptyView(emptyView);
        mSelectCardItem = View.inflate(getActivity(),
				R.layout.im_contacts_add_bar, null);
		mSelectCardItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						CreateGroupActivity.class);
				getActivity().startActivity(intent);
			}
		});
		mListView.addHeaderView(mSelectCardItem);
		
        mListView.setOnItemClickListener(onItemClickListener);
        mGroupAdapter = new GroupAdapter(getActivity());
        mListView.setAdapter(mGroupAdapter);

        findViewById(R.id.loading_tips_area).setVisibility(View.GONE);

        registerReceiver(new String[]{getActivity().getPackageName() + ".inited" ,IMessageSqlManager.ACTION_GROUP_DEL});
    }
    @Override
    public void onResume() {
        super.onResume();
        
        GroupSqlManager.registerGroupObserver(mGroupAdapter);
        mGroupAdapter.notifyChange();

        if(!sync) {
            GroupService.syncGroup(this);
            sync = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        GroupSqlManager.unregisterGroupObserver(mGroupAdapter);

    }

    @Override
    protected void handleReceiver(Context context, Intent intent) {
        super.handleReceiver(context, intent);
        if(intent.getAction().equals(new String[]{getActivity().getPackageName()+".inited"})){
            GroupService.syncGroup(this);
        } else if (IMessageSqlManager.ACTION_GROUP_DEL.equals(intent.getAction())) {
            onSyncGroup();
        }
    }

    public void onGroupFragmentVisible(boolean visible) {
    	
    	if(visible&&isVisible()&&!isRefresh){
    	   onSyncGroup();
    	}
    }

    public class GroupAdapter extends CCPListAdapter<DemoGroup> {
        int padding;
        /**
         * @param ctx
         */
        public GroupAdapter(Context ctx) {
            super(ctx, new DemoGroup());
            padding = ctx.getResources().getDimensionPixelSize(R.dimen.OneDPPadding);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder mViewHolder = null;
            if(convertView == null || convertView.getTag() == null) {
                view = View.inflate(mContext , R.layout.im_group_item, null);

                mViewHolder = new ViewHolder();
                mViewHolder.groupitem_avatar_iv = (ImageView) view.findViewById(R.id.groupitem_avatar_iv);
                mViewHolder.group_name = (EmojiconTextView) view.findViewById(R.id.group_name);
                mViewHolder.group_id = (TextView) view.findViewById(R.id.group_id);
                mViewHolder.join_state = (TextView) view.findViewById(R.id.join_state);
                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder) view.getTag();
            }

            DemoGroup group = getItem(position);
            if(group != null) {
                Bitmap bitmap = ContactLogic.getChatroomPhoto(group.getGroupId());
                if(bitmap != null) {
                    mViewHolder.groupitem_avatar_iv.setImageBitmap(bitmap);
                    mViewHolder.groupitem_avatar_iv.setPadding(padding, padding, padding, padding);

                } else {
                    mViewHolder.groupitem_avatar_iv.setImageResource(R.drawable.group_head);
                    mViewHolder.groupitem_avatar_iv.setPadding(0, 0, 0, 0);
                }
                mViewHolder.group_name.setText(TextUtils.isEmpty(group.getName()) ? group.getGroupId() : group.getName());
                mViewHolder.group_id.setText(getString(R.string.str_group_id_fmt, DemoUtils.getGroupShortId(group.getGroupId())));
//                mViewHolder.join_state.setText(group.isJoin() ?"已加入" :"");
//                mViewHolder.join_state.setVisibility(group.isJoin()? View.VISIBLE:View.GONE);
            }

            return view;
        }

        @Override
        protected void notifyChange() {
            Cursor cursor = GroupSqlManager.getGroupCursor(true);
            setCursor(cursor);
            super.notifyDataSetChanged();
        }

        @Override
        protected void initCursor() {
            notifyChange();
        }

        @Override
        protected DemoGroup getItem(DemoGroup t, Cursor cursor) {
            DemoGroup group = new DemoGroup();
            group.setCursor(cursor);
            return group;
        }


        class ViewHolder {
            ImageView groupitem_avatar_iv;
            EmojiconTextView group_name;
            TextView group_id;
            TextView join_state;
        }

    }


    @Override
    public void onSyncGroup() {
        mGroupAdapter.notifyChange();
        isRefresh=true;
    }

    @Override
    public void onSyncGroupInfo(String groupId) {

    }

    @Override
    public void onGroupDel(String groupId) {
        onSyncGroup();
    }

    @Override
    public void onError(ECError error) {
    }

	@Override
	public void onUpdateGroupAnonymitySuccess(String groupId,
			boolean isAnonymity) {
		
	}

	
}
