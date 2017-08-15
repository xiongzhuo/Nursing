package com.im.sdk.dy.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.core.ContactsCache;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.storage.DepartContacts;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.im.sdk.dy.ui.contact.BladeView;
import com.im.sdk.dy.ui.contact.ContactDetailActivity;
import com.im.sdk.dy.ui.contact.ContactLogic;
import com.im.sdk.dy.ui.contact.ContactsActivity.ContactsFragment;
import com.im.sdk.dy.ui.contact.CustomSectionIndexer;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.im.sdk.dy.ui.contact.MobileContactActivity;
import com.im.sdk.dy.ui.contact.MobileContactSelectActivity;
import com.im.sdk.dy.ui.contact.PinnedHeaderListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ContactsFragmentNew extends TabFragment {
	private static final String TAG = "IMSDK.ContactsFragment";

	/** 当前联系人列表类型（查看、联系人选择） */
	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_SELECT = 2;
	/** 列表类型 */
	private int mType;
	private String[] sections = { "#", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };
	private static final String ALL_CHARACTER = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/**
	 * 每个字母最开始的位置
	 */
	private HashMap<String, Integer> mFirstLetters;
	/** 当前选择联系人位置 */
	public static ArrayList<Integer> positions = new ArrayList<Integer>();
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
	private PinnedHeaderListView mListView;
	private CustomSectionIndexer mCustomSectionIndexer;
	private ContactsAdapter mAdapter;
	/** 选择联系人 */
	private View mSelectCardItem, newFriendItem, expertItem;
	DisplayImageOptions optionsSquare_men;
	DisplayImageOptions optionsSquare_women;
	Tools tools;

	// 设置联系人点击事件通知
	private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			int headerViewsCount = mListView.getHeaderViewsCount();
			if (position < headerViewsCount) {
				return;
			}
			int _position = position - headerViewsCount;

			if (mAdapter == null || mAdapter.getItem(_position) == null) {
				return;
			}
			if (mType != TYPE_NORMAL) {
				// 如果是选择联系人模式
				Integer object = Integer.valueOf(_position);
				if (positions.contains(object)) {
					positions.remove(object);
				} else {
					positions.add(object);
				}
				notifyClick(positions.size());
				mAdapter.notifyDataSetChanged();
				return;
			}

			ECContacts contacts = mAdapter.getItem(_position);
			if (contacts == null || contacts.getId() == -1) {
				ToastUtil.showMessage(R.string.contact_none);
				return;
			}

			// 如果是点对点聊天
			Intent intent = new Intent(getActivity(),
					ContactDetailActivity.class);
			intent.putExtra(ContactDetailActivity.RAW_ID, contacts.getId());
			startActivity(intent);
		}
	};

	private BladeView mLetterListView;

	private Gson gson=new Gson();

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
		return R.layout.im_contacts_activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tools=new Tools(getActivity(), Constants.AC);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater
				.inflate(R.layout.im_contacts_activity, container, false);
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

	private void notifyClick(int count) {
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerReceiver(new String[] { ContactsCache.ACTION_ACCOUT_DY_CONTACTS });
		if (mListView != null) {
			mListView.setAdapter(null);
		}
		mListView = (PinnedHeaderListView) findViewById(R.id.address_contactlist);
//		mListView.setDividerHeight(0);
		mListView.setOnItemClickListener(onItemClickListener);
		initContactListView();
		findView();
	}

	/**
	 * 初始化联系人列表
	 */
	private void initContactListView() {
		if (mListView != null && mSelectCardItem != null && expertItem != null) {
			mListView.removeHeaderView(mSelectCardItem);
			mListView.removeHeaderView(expertItem);
			mListView.setAdapter(null);
		}

		mAdapter = new ContactsAdapter(getActivity());

		contacts = ContactLogic.getContacts();
		String str=SharedPreferencesUtil.getString(getActivity(), "hospitalFriendList", "");
		List<DepartContacts> list2 = gson.fromJson(str,
				new TypeToken<List<DepartContacts>>() {
				}.getType());
		if(null==list2){
			list2=new ArrayList<DepartContacts>();
		}
		if (null != contacts) {
			counts = new int[sections.length];
			for (ECContacts c : contacts) {
				boolean isHospitalFrieds = false;
				for(DepartContacts dc:list2){//过滤叼be
					if(dc.getDepartment_id().equals(c.getContactid())){
						isHospitalFrieds=true;
						break;
					}
					
				}

				if(!isHospitalFrieds){
				String firstCharacter = c.getSortKey();
				int index = ALL_CHARACTER.indexOf(firstCharacter);
				counts[index]++;
				}
			}
			if (contacts != null && !contacts.isEmpty()) {
				mSortKey = contacts.get(0).getSortKey();
			}
			mCustomSectionIndexer = new CustomSectionIndexer(sections, counts);

			mAdapter.setData(contacts, mCustomSectionIndexer);
		}
		if (mType == TYPE_NORMAL) {
			// mSelectCardItem = View.inflate(getActivity(),
			// R.layout.im_contacts_add_bar, null);
			mSelectCardItem = View.inflate(getActivity(), R.layout.new_friend,
					null);
			/** 新的朋友 */
			mSelectCardItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(),

					MobileContactActivity.class);

					// Intent intent = new Intent(getActivity(),
					// WXEntryActivity.class);
					getActivity().startActivity(intent);
				}
			});
			mListView.addHeaderView(mSelectCardItem);

			/** 感控专家 */
//			expertItem = View
//					.inflate(getActivity(), R.layout.expert_view, null);
//
//			expertItem.setOnClickListener(new OnClickListener() {

//				@Override
//				public void onClick(View v) {
//					 Intent intent = new Intent(getActivity(),
//					 GKExpertActivity.class);
//					
//					 // Intent intent = new Intent(getActivity(),
//					 // WXEntryActivity.class);
//					 Log.i("IMtag", contacts.size()+"");
//					 getActivity().startActivity(intent);
//				}
//			});
//			mListView.addHeaderView(expertItem);
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

		// contacts = ContactLogic.getContacts();

		mLetterListView = (BladeView) findViewById(R.id.mLetterListView);
		showLetter(mLetterListView);
		contacts = ContactLogic.getContacts();// ContactLogic.contacts;
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
			mCustomSectionIndexer = new CustomSectionIndexer(sections, counts);

			mAdapter.setData(contacts, mCustomSectionIndexer);
		}
		if (null != contacts && contacts.size() > 0) {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
				+ ", resultCode=" + resultCode + ", data=" + data);

		// If there's no data (because the user didn't select a picture and
		// just hit BACK, for example), there's nothing to do.
		if (requestCode == 0xa) {
			if (data == null) {
				return;
			}
		} else if (resultCode != -1) {
			LogUtil.d("onActivityResult: bail due to resultCode=" + resultCode);
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
		mLetterListView.setVisibility(showBanView ? View.VISIBLE : View.GONE);
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
		if (ContactsCache.ACTION_ACCOUT_DY_CONTACTS.equals(intent.getAction())) {
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

		public void setData(List<ECContacts> data, CustomSectionIndexer indexer) {
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
				view = View.inflate(mContext, R.layout.im_contacts_list_item,
						null);

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
				mViewHolder.jop=(TextView) view.findViewById(R.id.jop);
				view.setTag(mViewHolder);
			} else {
				view = convertView;
				mViewHolder = (ViewHolder) view.getTag();
			}



			ECContacts contacts1 = getItem(position);
			
			
			if(null!=contacts1){
				mViewHolder.tvCatalog.setText(contacts1.getSortKey());
			}
			Log.i("IMtag", contacts1.getSortKey());
			ECContacts	contacts=ContactSqlManager.getContact(contacts1.getId());
			Log.i("IMtag", contacts1.getNickname()+"-----------"+contacts.getNickname());
			if (contacts != null) {
				int section = mIndexer.getSectionForPosition(position);
				Log.i("IMtag", contacts.getSortKey());
				if (mIndexer.getPositionForSection(section) == position) {
					mViewHolder.tvCatalog.setVisibility(View.VISIBLE);
				
				} else {
					mViewHolder.tvCatalog.setVisibility(View.GONE);
				}
				int sex = contacts.getSex();
				if (!AbStrUtil.isEmpty(contacts.getAvatar())) {
					ImageLoader.getInstance().displayImage(
							WebUrl.FILE_LOAD_URL + contacts.getAvatar(),
							mViewHolder.mAvatar,
							sex == 1 ? optionsSquare_women : optionsSquare_men);

				} else {
					ImageLoader.getInstance().displayImage("",
							mViewHolder.mAvatar,
							sex == 1 ? optionsSquare_women : optionsSquare_men);
				}

				if(!AbStrUtil.isEmpty(contacts.getRegis_job())){
					mViewHolder.jop.setText(contacts.getRegis_job());
				}else{
					mViewHolder.jop.setText("其他");
				}
				mViewHolder.name_tv.setText(contacts.getRname());
				
				

				// String mobile = contacts.getMobile().trim();
				// if (mobile.length() >= 8) {
				// String maskNumber = mobile.substring(0, 3) + "*****"
				// + mobile.substring(8, mobile.length());
				// mViewHolder.account.setText(maskNumber);
				// } else {
				// mViewHolder.account.setText("****");
				// }

				String department = contacts.getDepartment();
				String hospital = contacts.getHospital();
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

				if (mType != TYPE_NORMAL) {
					mViewHolder.checkBox.setVisibility(View.VISIBLE);
					if (mViewHolder.checkBox.isEnabled() && positions != null) {
						mViewHolder.checkBox.setChecked(positions
								.contains(position));
					} else {
						mViewHolder.checkBox.setChecked(false);
					}
				} else {
					mViewHolder.checkBox.setVisibility(View.GONE);
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
		public void configurePinnedHeader(View header, int position, int alpha) {
			int realPosition = position;
			int _position = position - 1;
			if (_position < 0)
				return;
			TextView headView = ((TextView) header
					.findViewById(R.id.contactitem_catalog));
			if (_position == 0) {
				headView.setText(mSortKey);
			}
			ECContacts item = getItem(_position);
			if (item != null) {
				headView.setText(item.getSortKey());
			}
			/*
			 * int section = mIndexer.getSectionForPosition(realPosition);
			 * String title = (String) mIndexer.getSections()[section];
			 */
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
			TextView tvCatalog,jop;
		}
	}

}
