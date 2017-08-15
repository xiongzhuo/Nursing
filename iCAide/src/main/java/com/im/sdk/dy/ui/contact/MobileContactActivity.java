package com.im.sdk.dy.ui.contact;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.acaide.wxapi.WeixiShareUtil;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.core.ContactsCache;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.ui.ContactListFragment;
import com.im.sdk.dy.ui.ECSuperActivity;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.im.sdk.dy.ui.SearchActivity;
import com.im.sdk.dy.ui.TabFragment;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 手机联系人列表
 * Created by yung on 2015/12/20.
 */
public class MobileContactActivity extends ECSuperActivity implements
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
			// Do first time initialization -- add initial fragment.
			MobileContactFragment list = new MobileContactFragment();

			// ContactListFragment list = new ContactListFragment();
			fm.beginTransaction().add(R.id.mobile_content, list).commit();
		}

		findViewById(R.id.lay_search).setOnClickListener(this);

		getTopBarView().setTopBarToStatus(1, R.drawable.btn_back_style, -1,
				getString(R.string.mobile_contact_add), this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_search:
			Intent intent = new Intent(MobileContactActivity.this,
					SearchActivity.class);
			intent.putExtra(SearchActivity.FROM,
					SearchActivity.FROM_MOBAILCONTACTS);
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

	public static class MobileContactFragment extends TabFragment {
		private static final String TAG = "IMSDK.MobileContactFragment";

		/** 当前联系人列表类型（查看、联系人选择） */
		public static final int TYPE_NORMAL = 1;
		public static final int TYPE_SELECT = 2;
		/** 列表类型 */
		private int mType;
		private String[] sections = { "#", "A", "B", "C", "D", "E", "F", "G",
				"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
				"T", "U", "V", "W", "X", "Y", "Z" };
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
		private View mMobileHeader;
		boolean isActive=true;
		
		final private View.OnClickListener mSelectClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new
				// Intent(MobileContactFragment.this.getActivity() ,
				// EditConfigureActivity.class);
				// intent.putExtra(EditConfigureActivity.EXTRA_EDIT_TITLE ,
				// getString(R.string.edit_add_contacts));
				// intent.putExtra(EditConfigureActivity.EXTRA_EDIT_HINT ,
				// getString(R.string.edit_add_contacts));
				// startActivityForResult(intent , 0xa);
			}
		};

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
				// Intent intent = new Intent(getActivity(),
				// ContactDetailActivity.class);
				// intent.putExtra(ContactDetailActivity.MOBILE,
				// contacts.getContactid());
				// intent.putExtra(ContactDetailActivity.DISPLAY_NAME,
				// contacts.getNickname());
				// startActivity(intent);

				// 查询
				// searchContacts(contacts);
			}
		};

		private void searchContacts(ECContacts contacts) {
			// TODO Auto-generated method stub

			showprocessdialog();
			JSONObject json = new JSONObject();
			try {
				json.put("mobile", contacts.getMobile());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			MainBizImpl.getInstance().getData(
					new ContactsHandler(getActivity(), contacts), activity,
					SUCCESS, FAIL, json, WebUrl.CONTACT_PATH,
					tools.getValue(Constants.AUTHENT));
		}

		private static final int SUCCESS = 0x1055;
		private static final int FAIL = 0x1056;

		class ContactsHandler extends MyHandler {
			private ECContacts contacts;

			public ContactsHandler(Activity leakActivity, ECContacts contacts) {
				super(leakActivity);
				this.contacts = contacts;
			}

			@Override
			public void handleMessage(Message msg) {
				DebugUtil.debug("gerfriends", " msg>>" + msg.obj.toString());
				switch (msg.what) {
				case SUCCESS:
					dismissdialog();
					if (null != msg && null != msg.obj) {
						JSONObject jsonObject;
						DebugUtil.debug("gerfriends", msg.obj.toString());
						try {
							jsonObject = new JSONObject(msg.obj.toString());
							String r = jsonObject.getString("result_id");
							if (null != r && r.equals("0")) {
								JSONObject itemJson = jsonObject
										.getJSONObject("friend");

								ContactSqlManager.insertContact(ContactLogic
										.GetContacts(itemJson));
							} else if (null != r && r.equals("1")) {
								// contacts.getMobile()
								ContactLogic.sendSystemMessage(getActivity(),
										contacts.getMobile());
							}

							return;
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					break;
				case FAIL:
					dismissdialog();
					ToastUtils.showToast(getActivity(), "亲，您的网络不顺畅哦！");
					break;
				default:
					break;
				}
			}

		}

		private BladeView mLetterListView;

		/**
		 * Create a new instance of ContactListFragment, providing "type" as an
		 * argument.
		 */
		public static MobileContactFragment newInstance(int type) {
			MobileContactFragment f = new MobileContactFragment();

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

		private Tools tools;
		private IWXAPI wxApi;  
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			tools = new Tools(getActivity(), Constants.AC);
			mType = getArguments() != null ? getArguments().getInt("type")
					: TYPE_NORMAL;
			if (positions == null) {
				positions = new ArrayList<Integer>();
			}
			
			
			//实例化  
			wxApi = WXAPIFactory.createWXAPI(getActivity(), WeixiShareUtil.getWeixinAppId(getActivity()));  
			wxApi.registerApp(WeixiShareUtil.getWeixinAppId(getActivity()));
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			isActive=true;
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
			registerReceiver(new String[] { ContactsCache.ACTION_ACCOUT_INIT_CONTACTS });
			if (mListView != null) {
				mListView.setAdapter(null);
			}
			mListView = (PinnedHeaderListView) findViewById(R.id.address_contactlist);
			mListView.setOnItemClickListener(onItemClickListener);
			initContactListView();
			findView();
		}
		
		private void initContacts(){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					//对比手机联系人数据
					int page=0;
					int oldPage=-1;
					while(page>oldPage){
						oldPage=page;
						page=ContactLogic.getPhoneContacts2(getActivity(),100,page);
						LogUtil.d("getPhoneContacts2", "oldPage now>>"+oldPage);
						LogUtil.d("getPhoneContacts2", "page next>>"+page);
						cHandler.sendEmptyMessage(0);
					}
				}
			}).start();
			
			
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					contacts = ContactLogic.getPhoneContactsSearch(getActivity());
//					cHandler.sendEmptyMessage(0);
//				}
//			}).start();
		}

		Handler cHandler =new Handler(){
			@Override
			public void dispatchMessage(Message msg) {
				
				 contacts = ContactLogic.getPhoneContactsSearch2(getActivity());
				if (contacts == null) {
					return;
				}
				
				LogUtil.d("getPhoneContacts2", "contacts size>>"+contacts.size());
				
				counts = new int[sections.length];
				for (int i = 0; i < contacts.size(); i++) {
					ECContacts c=contacts.get(i);
					String firstCharacter = c.getSortKey();
					int index = ALL_CHARACTER.indexOf(firstCharacter);
					counts[index]++;
				}
				if (contacts != null && !contacts.isEmpty()) {
					mSortKey = contacts.get(0).getSortKey();
				}
				
				mCustomSectionIndexer = new CustomSectionIndexer(sections, counts);
				if(isActive){
					mAdapter.setData(contacts, mCustomSectionIndexer);
					findViewById(R.id.loading_tips_area).setVisibility(View.GONE);
				}
			}
		};

		/**
		 * 初始化联系人列表
		 */
		private void initContactListView() {
			if (mListView != null && mMobileHeader != null) {
				mListView.removeHeaderView(mMobileHeader);
				mListView.setAdapter(null);
			}
			// contacts = ContactsCache.getInstance().getContacts();

		//	contacts = ContactLogic.getPhoneContacts(getActivity());
			initContacts();
			
			
			
			if (mType == TYPE_NORMAL) {
				mMobileHeader = View.inflate(getActivity(),
						R.layout.im_contacts_mobile_header, null);
				// TextView startCard = (TextView)
				// mSelectCardItem.findViewById(R.id.card_item_tv);
				// startCard.setGravity(Gravity.CENTER);
				// startCard.setText(R.string.edit_add_contacts);
				// if (startCard != null) {
				// startCard.setOnClickListener(mSelectClickListener);
				// }
				mMobileHeader.findViewById(R.id.lay_tel).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								//跳转到手机号添加页面
								// TODO Auto-generated method stub
								Intent intent = new Intent(getActivity(),
										ContactMobileAddActivity.class);
								getActivity().startActivity(intent);
							}
						});
				mMobileHeader.findViewById(R.id.lay_wx).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								try {
									//微信分享
									
									    wechatShare(0,String.format(getString(
												R.string.share_wx_contacts_title), CCPAppManager
												.getClientUser().getUserName()),
												getString(
														R.string.share_wx_contacts_content), getString(
																R.string.share_wx_contacts_url));//分享到微信好友  
									
									
									
								} catch (ActivityNotFoundException e) {
									// TODO: handle exception
									ToastUtil.showMessage("未安装微信");
								}

							}
						});
				mListView.addHeaderView(mMobileHeader);
			}
			mAdapter = new ContactsAdapter(getActivity());
			mListView.setAdapter(mAdapter);
			
			mListView.setOnScrollListener(mAdapter);
			// 設置頂部固定頭部
			mListView.setPinnedHeaderView(LayoutInflater.from(getActivity())
					.inflate(R.layout.im_header_item_cator, mListView, false));
			
		}
		
		private void wechatShare(int flag,String title,String content,String url){  
		    WXWebpageObject webpage = new WXWebpageObject();  
		    webpage.webpageUrl = url;  
		    WXMediaMessage msg = new WXMediaMessage(webpage);  
		    msg.title = title;  
		    msg.description = content;  
		    //这里替换一张自己工程里的图片资源  
		    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.share_logo);  
		    msg.setThumbImage(thumb);  
		      
		    SendMessageToWX.Req req = new SendMessageToWX.Req();  
		    req.transaction = String.valueOf(System.currentTimeMillis());  
		    req.message = msg;  
		    req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;  
		    wxApi.sendReq(req);  
		}  

		@Override
		public void onResume() {
			super.onResume();
			
//			IntentFilter intentfilter = new IntentFilter();
//			intentfilter.addAction(SDKCoreHelper.ACTION_MOBILE_CONTACTS_CHANGE);
//			if (contactsReceiver == null) {
//				contactsReceiver = new ContactsReceiver();
//			}
//
//			getActivity().registerReceiver(contactsReceiver, intentfilter);
			
			mLetterListView = (BladeView) findViewById(R.id.mLetterListView);
			showLetter(mLetterListView);
		}
		
		@Override
		public void onPause() {
//			try {
//				
//				getActivity().unregisterReceiver(contactsReceiver);
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
			
			super.onPause();
		}
		private ContactsReceiver contactsReceiver;

		private class ContactsReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent == null || TextUtils.isEmpty(intent.getAction())) {
					return;
				}
				if (SDKCoreHelper.ACTION_MOBILE_CONTACTS_CHANGE.equals(intent.getAction())) {
					 LogUtil.d("ACTION_MOBILE_CONTACTS_CHANGE","mAdapter notifyChange>>");
					 contacts = ContactLogic.getPhoneContactsSearch(getActivity());
						cHandler.sendEmptyMessage(0);
				}
			}
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
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
				// IMAppManager.startChattingAction(MobileContactFragment.this.getActivity()
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
			isActive=false;
		}

		@Override
		protected void handleReceiver(Context context, Intent intent) {
			super.handleReceiver(context, intent);
			if (ContactsCache.ACTION_ACCOUT_INIT_CONTACTS.equals(intent
					.getAction())) {
				LogUtil.d("handleReceiver ACTION_ACCOUT_INIT_CONTACTS");
				initContactListView();
			}
		}

		class ContactsAdapter extends ArrayAdapter<ECContacts> implements
				PinnedHeaderListView.PinnedHeaderAdapter,
				AbsListView.OnScrollListener {
			CustomSectionIndexer mIndexer;
			Context mContext;
			private int mLocationPosition = -1;
			int[] im_contacts_drawable;
			int len = 0;
			Resources res;

			public ContactsAdapter(Context context) {
				super(context, 0);
				mContext = context;
				// im_contacts_drawable=context.getResources().getIntArray(R.array.im_contacts_drawable);
				TypedArray ar = context.getResources().obtainTypedArray(
						R.array.im_contacts_drawable);
				len = ar.length();
				im_contacts_drawable = new int[len];
				res = context.getResources();
				for (int i = 0; i < len; i++)
					im_contacts_drawable[i] = ar.getResourceId(i, 0);
			}

			public void setData(List<ECContacts> data,
					CustomSectionIndexer indexer) {
				mIndexer = indexer;
				setNotifyOnChange(false);
				clear();
				setNotifyOnChange(true);
				if (data != null) {
					for (int i = 0; i <data.size(); i++) {
						ECContacts appEntry=data.get(i);
						add(appEntry);
					}
				}
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				ViewHolder mViewHolder;
				if (convertView == null || convertView.getTag() == null) {
					mViewHolder=new ViewHolder();
					convertView = View.inflate(mContext,
							R.layout.im_mobile_contact_list_item, null);

					mViewHolder.text_name_l = (TextView) convertView
							.findViewById(R.id.text_name_l);
					mViewHolder.lay_name = (RelativeLayout) convertView
							.findViewById(R.id.lay_name);
					mViewHolder.name_tv = (EmojiconTextView) convertView
							.findViewById(R.id.name_tv);
					mViewHolder.account = (TextView) convertView
							.findViewById(R.id.account);
					mViewHolder.checkBox = (CheckBox) convertView
							.findViewById(R.id.contactitem_select_cb);
					mViewHolder.tvCatalog = (TextView) convertView
							.findViewById(R.id.contactitem_catalog);
					mViewHolder.text_status = (TextView) convertView
							.findViewById(R.id.text_status);
					mViewHolder.text_status
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (null != v.getTag()) {
										try {

											ECContacts contacts = (ECContacts) v
													.getTag();
											searchContacts(contacts);
										} catch (Exception e) {
											// TODO: handle exception
										}
									}
								}
							});

					convertView.setTag(mViewHolder);
				} else {
					mViewHolder = (ViewHolder) convertView.getTag();
				}

				ECContacts contacts = getItem(position);
				if (contacts != null) {
					int section = mIndexer.getSectionForPosition(position);
					if (mIndexer.getPositionForSection(section) == position) {
						mViewHolder.tvCatalog.setVisibility(View.VISIBLE);
						mViewHolder.tvCatalog.setText(contacts.getSortKey());
					} else {
						mViewHolder.tvCatalog.setVisibility(View.GONE);
					}
					mViewHolder.name_tv.setText(contacts.getRname());
					mViewHolder.account.setText(contacts.getMobile());
					mViewHolder.text_name_l.setText(contacts.getSimpName());
					mViewHolder.lay_name
							.setBackgroundResource(im_contacts_drawable[position
									% len]);
					if (contacts.getStatus() == 0) {
						mViewHolder.text_status.setText("已添加");
						mViewHolder.text_status.setTextColor(res
								.getColor(R.color.gray));
						mViewHolder.text_status
								.setBackgroundColor(Color.TRANSPARENT);
						mViewHolder.text_status
						.setBackgroundResource(R.drawable.listselect_btn);
						mViewHolder.text_status.setTag(null);

					} else {
						mViewHolder.text_status.setText("添加");
						mViewHolder.text_status.setTextColor(res
								.getColor(R.color.blue));
						mViewHolder.text_status
								.setBackgroundResource(R.drawable.big_round_blue_type_style);
						mViewHolder.text_status.setTag(contacts);
					}

				}else{
					mViewHolder.text_status.setText("");
					mViewHolder.text_status.setTextColor(res
							.getColor(R.color.blue));
					mViewHolder.text_status
					.setBackgroundResource(R.drawable.listselect_btn);
					mViewHolder.text_status.setTag(contacts);
				}

				return convertView;
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
				/** 头像 */
				TextView text_name_l;
				/** 名称 */
				EmojiconTextView name_tv;
				/** 账号 */
				TextView account;
				/** 选择状态 */
				CheckBox checkBox;
				TextView tvCatalog;
				RelativeLayout lay_name;
				TextView text_status;

			}
		}
		

	}

}
