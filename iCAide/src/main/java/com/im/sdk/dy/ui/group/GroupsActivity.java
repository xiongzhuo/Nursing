package com.im.sdk.dy.ui.group;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.core.ContactsCache;
import com.im.sdk.dy.ui.ECSuperActivity;
import com.im.sdk.dy.ui.GroupListFragment;
import com.im.sdk.dy.ui.SearchActivity;
import com.im.sdk.dy.ui.TabFragment;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.im.sdk.dy.ui.contact.PinnedHeaderListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by yung on 2015/12/20.
 */
public class GroupsActivity extends ECSuperActivity implements
		View.OnClickListener {

	@Override
	protected int getLayoutId() {
		return R.layout.im_mobile_contacts_list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fm = getSupportFragmentManager();
		// Create the list fragment and add it as our sole content.
		if (savedInstanceState == null) {
			GroupListFragment list = new GroupListFragment();
			//GroupsFragment list = new GroupsFragment();
			fm.beginTransaction().add(R.id.mobile_content, list).commit();
		}
		
		findViewById(R.id.lay_search).setOnClickListener(this);

		getTopBarView().setTopBarToStatus(1, R.drawable.btn_back_style, -1,
				getString(R.string.main_group), this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_search:
			Intent intent=new Intent(GroupsActivity.this,SearchActivity.class);
			intent.putExtra(SearchActivity.FROM, SearchActivity.FROM_CONTACTS);
			startActivity(intent);
			break;
		case R.id.btn_left:
		case R.id.btn_text_left:
			hideSoftKeyboard();
			finish();
			break;
		default:
			break;
		}
	}

	public static class GroupsFragment extends TabFragment {
		private static final String TAG = "IMSDK.ContactsFragment";

		private List<DemoGroup> groups;
		private ListView mListView;
		private GroupsAdapter mAdapter;
		DisplayImageOptions optionsSquare;


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

				DemoGroup group = mAdapter.getItem(_position);
				if (group == null ||null== group.getGroupId()) {
					ToastUtil.showMessage(R.string.contact_none);
					return;
				}
				ToastUtil.showMessage(group.getGroupId());
				  // 如果是点对点聊天
//                Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
//                intent.putExtra(ContactDetailActivity.RAW_ID, contacts.getId());
//                startActivity(intent);
              //  startActivityForResult(intent, REQUEST_VIEW_CARD);

				CCPAppManager.startChattingAction(getActivity(),
						group.getGroupId(), group.getName(),
						
						"", true,0);
			}
		};


		/**
		 * Create a new instance of ContactListFragment, providing "type" as an
		 * argument.
		 */
		public static GroupsFragment newInstance(int type) {
			GroupsFragment f = new GroupsFragment();

			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putInt("type", type);
			f.setArguments(args);

			return f;
		}

		
		@Override
		protected int getLayoutId() {
			return R.layout.im_groups_activity;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			optionsSquare = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.im_men_defalut)
					.showImageForEmptyUri(R.drawable.im_men_defalut)
					.showImageOnFail(R.drawable.im_men_defalut)
					.resetViewBeforeLoading(true).cacheOnDisk(true)
					.considerExifParams(true).cacheInMemory(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new FadeInBitmapDisplayer(300)).build();
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
		
			try {
//				mClickListener = (GroupsFragment.OnContactClickListener) activity;
			} catch (ClassCastException e) {
				throw new ClassCastException(activity.toString()
						+ " must implement OnContactClickListener");
			}
		}


		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			registerReceiver(new String[] { ContactsCache.ACTION_ACCOUT_DY_CONTACTS });
			if (mListView != null) {
				mListView.setAdapter(null);
			}
			mListView = (ListView) findViewById(R.id.group_list);
			mListView.setOnItemClickListener(onItemClickListener);
			initGroupListView();
		}

		View mSelectCardItem;
		private void initGroupListView() {
			if (mListView != null && mSelectCardItem != null) {
				mListView.removeHeaderView(mSelectCardItem);
				mListView.setAdapter(null);
			}

			mAdapter = new GroupsAdapter(getActivity());

			groups = GroupLogic.getGroups();
			

			mAdapter.setData(groups);
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
			mListView.setAdapter(mAdapter);
			mListView.setOnScrollListener(mAdapter);
			findViewById(R.id.loading_tips_area).setVisibility(View.GONE);
		}

		@Override
		public void onResume() {
			super.onResume();
			
			groups = GroupLogic.groupList;
			if(null!=groups&&groups.size()>0){
				mAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			 GroupListFragment.sync=false;
			LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
					+ ", resultCode=" + resultCode + ", data=" + data);

			// If there's no data (because the user didn't select a picture and
			// just hit BACK, for example), there's nothing to do.
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


		@Override
		protected void onTabFragmentClick() {

		}

		@Override
		protected void onReleaseTabUI() {

		}

		@Override
		public void onDetach() {
			super.onDetach();
		}

		@Override
		protected void handleReceiver(Context context, Intent intent) {
			super.handleReceiver(context, intent);
			if (ContactsCache.ACTION_ACCOUT_DY_CONTACTS.equals(intent
					.getAction())) {
				LogUtil.d("handleReceiver ACTION_ACCOUT_DY_CONTACTS");
				initGroupListView();
			}
		}

		class GroupsAdapter extends ArrayAdapter<DemoGroup> implements
				PinnedHeaderListView.PinnedHeaderAdapter,
				AbsListView.OnScrollListener {
			Context mContext;
			private int mLocationPosition = -1;

			public GroupsAdapter(Context context) {
				super(context, 0);
				mContext = context;
			}

			public void setData(List<DemoGroup> data) {
				setNotifyOnChange(false);
				clear();
				setNotifyOnChange(true);
				if (data != null) {
					for (DemoGroup appEntry : data) {
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
					view.setTag(mViewHolder);
				} else {
					view = convertView;
					mViewHolder = (ViewHolder) view.getTag();
				}

				DemoGroup group = getItem(position);
				if (group != null) {
					if (!AbStrUtil.isEmpty(group.getGroupDomain())) {
						ImageLoader.getInstance().displayImage(
								WebUrl.FILE_LOAD_URL + group.getGroupDomain(),
								mViewHolder.mAvatar,
								optionsSquare);

					} else {
						ImageLoader.getInstance().displayImage(
								"",
								mViewHolder.mAvatar,
								optionsSquare);
					}

					mViewHolder.name_tv.setText(group.getName());
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
				return PINNED_HEADER_VISIBLE;
			}

			@Override
			public void configurePinnedHeader(View header, int position,
					int alpha) {
			}

			class ViewHolder {
				/** 头像 */
				ImageView mAvatar;
				/** 名称 */
				EmojiconTextView name_tv;
				/** 账号 */
				TextView account;
				/** 选择状态 */
				CheckBox checkBox;
				TextView tvCatalog;
			}
		}

	}
}
