package com.im.sdk.dy.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.core.ECArrayLists;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.storage.GroupSqlManager;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.im.sdk.dy.ui.contact.ContactDetailActivity;
import com.im.sdk.dy.ui.contact.ContactLogic;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.im.sdk.dy.ui.group.DemoGroup;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yuntongxun.ecsdk.im.ECGroup;

/**
 * im 搜索
* @author  yung
* @date 创建时间：2016年1月15日 下午12:00:31 
* @version 1.0
 */
public class SearchActivity extends BaseActivity implements OnClickListener {
	private Button btn_cancle;
	private EditText et_search;

	private ListView listview;

	private Tools tools;

	private List<Item> list = new ArrayList<Item>();

	private ArrayList<ECContacts> contactsList = new ArrayList<ECContacts>();
	private ECArrayLists<ECContacts> mobileList = new ECArrayLists<ECContacts>();

	private List<ECGroup> groupList = new ArrayList<ECGroup>();
	private ItemsAdapter itemsAdapter;

	DisplayImageOptions optionsSquare_men;
	DisplayImageOptions optionsSquare_women;
	DisplayImageOptions optionsSquare_group;
	public static final String FROM = "from";
	/**
	 * 消息列表页面标识
	 */
	public static final int FROM_CONVERSATION = 0x01;
	/**
	 * 联系人列表页面标识
	 */
	public static final int FROM_CONTACTS = 0x02;
	/**
	 * 手机联系人列表页面标识
	 */
	public static final int FROM_MOBAILCONTACTS = 0x03;

	private int from = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		tools = new Tools(mcontext, Constants.AC);

		from = getIntent().getIntExtra(FROM, 0);
		initView();

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
		optionsSquare_group = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.group_head)
		.showImageForEmptyUri(R.drawable.group_head)
		.showImageOnFail(R.drawable.group_head)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	private View bottomView;

	private void initView() {
		btn_cancle = (Button) this.findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(this);

		et_search = (EditText) this.findViewById(R.id.et_search);
		et_search.addTextChangedListener(new MyTextWatcher());

		listview = (ListView) this.findViewById(R.id.listview);

		itemsAdapter = new ItemsAdapter();
		listview.setAdapter(itemsAdapter);

		bottomView = View.inflate(this, R.layout.im_search_bottom, null);

		bottomView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchOnline();
			}
		});

		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				hideSoftKeyboard();
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			}
		});

		if (from == FROM_CONTACTS || from == FROM_CONVERSATION) {
			listview.setOnItemClickListener(itemClick);
		}

	}

	OnItemClickListener itemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (null != list && list.size() > 0) {
				Item item = list.get(position);
				if (item.getType().equals("0")) {
					try {
						Intent intent = new Intent(SearchActivity.this,
								ContactDetailActivity.class);

						intent.putExtra(ContactDetailActivity.RAW_ID,
								Long.parseLong(item.getId()));

						startActivity(intent);
						finish();
					} catch (Exception e) {
						// TODO: handle exception
					}

				}else if(item.getType().equals("1")){
					CCPAppManager.startChattingAction(SearchActivity.this,
							item.getId(), item.getName(),
							
							"", true,0);
				}
			}
		}
	};

	private void hideSoftKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			View localView = this.getCurrentFocus();
			if (localView != null && localView.getWindowToken() != null) {
				IBinder windowToken = localView.getWindowToken();
				inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
			}
		}
	}

	protected void searchOnline() {
		// TODO Auto-generated method stub
		ToastUtil.showMessage("online>>" + search_str);
	}

	class MyTextWatcher implements TextWatcher {
		private CharSequence temp;

		public MyTextWatcher() {
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
			search(temp.toString().trim());
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

	}

	private String search_str = "";

	/**
	 * 查找联系人方法
	 * @param string
	 */
	private void search(String string) {
		// TODO Auto-generated method stub
		// listview.removeFooterView(bottomView);
		if (string.length() == 0) {
			list.clear();
		} else {
			// listview.addFooterView(bottomView);
			search_str = string;
			// 异步查找
			try {
				new SearchContactTask().execute();

				// if (groupTask == null) {
				// groupTask = new SearchGroupTask();
				// }
				// groupTask.execute();
				//
				// if (converTask == null) {
				// converTask = new SearchConverTask();
				// }
				// converTask.execute();
				//
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private class SearchContactTask extends AsyncTask<Intent, Void, Long> {

		public SearchContactTask() {

		}

		@Override
		protected Long doInBackground(Intent... intents) {
			if (from == FROM_CONVERSATION) {
				contactsList = ContactLogic.getSearchContacts();
			} else if (from == FROM_CONTACTS) {
				contactsList = ContactLogic.getSearchContacts();
			} else if (from == FROM_MOBAILCONTACTS) {
				mobileList = ContactLogic
						.getSearchPhoneContacts(SearchActivity.this);
			}

			cmdList();
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			handler.sendEmptyMessage(0);
		}

		@Override
		protected void onCancelled() {
		}
	}

	private class SearchGroupTask extends AsyncTask<Intent, Void, Long> {

		public SearchGroupTask() {

		}

		@Override
		protected Long doInBackground(Intent... intents) {
			groupList = GroupSqlManager.getLikeGroups(search_str);
			// cmdList();
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			handler.sendEmptyMessage(0);
		}

		@Override
		protected void onCancelled() {
		}
	}

	private class SearchConverTask extends AsyncTask<Intent, Void, Long> {

		public SearchConverTask() {

		}

		@Override
		protected Long doInBackground(Intent... intents) {
			// converList
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {

			handler.sendEmptyMessage(0);
		}

		@Override
		protected void onCancelled() {
		}
	}

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			itemsAdapter.notifyDataSetChanged();
			super.dispatchMessage(msg);
		}
	};

	HashMap<String, Integer> typeMap = new HashMap<String, Integer>();

	/**
	 * 处理不同页面的查找方法
	 */
	private synchronized void cmdList() {
		list.clear();
		typeMap.clear();
		if (from == FROM_CONVERSATION) {
			cmdContacts();
		} else if (from == FROM_CONTACTS) {
			cmdContacts();
		} else if (from == FROM_MOBAILCONTACTS) {
			cmdMobile();
		}

//		 if(null!=groupList&&groupList.size()>0){
//		 typeMap.put("1", list.size());
//		 for (int i = 0; i <groupList.size(); i++) {
//		 ECGroup group=groupList.get(i);
//		 Item item=new Item();
//		 item.setId(group.getGroupId());
//		 item.setName(group.getName());
//		 item.setType("1");
//		 list.add(item);
//		 }
//		 }
	}

	/**
	 * 处理查找到的联系人数据
	 */
	private void cmdContacts() {
		if (null != contactsList && contactsList.size() > 0) {
			Item item = null;
			for (int i = 0; i < contactsList.size(); i++) {
				ECContacts contacts = contactsList.get(i);
				if ((null != contacts.getRname() && contacts.getRname()
						.contains(search_str))
						|| (null != contacts.getNickname() && contacts
								.getNickname().contains(search_str))
						 ||
						(null!=contacts.getMobile()&&contacts.getMobile().contains(search_str))
						|| (null != contacts.getQpNameStr() && searchPinYin(
								search_str, contacts.getQpNameStr()))) {
					item = new Item();
					item.setId(contacts.getId() + "");
					item.setAvatar(contacts.getAvatar());
					item.setDec(contacts.getMobile());
					item.setName(contacts.getRname());

					if (contacts.getRname() == null
							|| contacts.getRname().equals("")) {
						item.setName(contacts.getNickname());
					}

					item.setSex(contacts.getSex());
					item.setType("0");
					list.add(item);

				}
			}
			List<DemoGroup> groups=GroupSqlManager.getGroups();
			Log.i("IMsearch", groups.size()+"");
			for (int i = 0; i < groups.size(); i++) {
				DemoGroup contacts = groups.get(i);
				Log.i("IMsearch", contacts.getName());
				if ((null != contacts.getName() && contacts.getName()
						.contains(search_str))
				) {
					item = new Item();
					item.setId(contacts.getGroupId() + "");
					item.setItemType(2);
					item.setName(contacts.getName());
					item.setAvatar("");
					item.setType("1");
					list.add(item);

				}
			}
			
			if (list.size() > 0)
				typeMap.put("0", 0);
		}
//		 ECGroupManager ecGroupManager = SDKCoreHelper.getECGroupManager();
//		  ECGroupMatch match = new ECGroupMatch(ECGroupMatch.SearchType.GROUPNAME);
//		//查询群组
//        match.setkeywords(search_str);
//        // 调用API创建群组、处理创建群组接口回调
//        ecGroupManager.searchPublicGroups(match , new ECGroupManager.OnSearchPublicGroupsListener() {
//            @Override
//            public void onSearchPublicGroupsComplete(ECError error, List<ECGroup> groups) {
//               
//                if(error.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
//                    GroupSqlManager.insertGroupInfos(groups, -1);
//                   Log.i("IMsearch", groups.size()+"");
//                    return ;
//                }
//                ToastUtil.showMessage("查询失败[" + error.errorCode + "]");
//            }
//
//        });
	}

	/**
	 * 处理查找到的手机联系人数据
	 */
	private void cmdMobile() {
		if (null != mobileList && mobileList.size() > 0) {
			Item item = null;
			for (int i = 0; i < mobileList.size(); i++) {
				ECContacts contacts = mobileList.get(i);
				if ((null != contacts.getRname() && contacts.getRname()
						.contains(search_str))
						|| (null != contacts.getNickname() && contacts
								.getNickname().contains(search_str))
						||(null!=contacts.getMobile()&&contacts.getMobile().contains(search_str))
						|| (null != contacts.getQpNameStr() && searchPinYin(
								search_str, contacts.getQpNameStr()))) {
					item = new Item();
					item.setId(contacts.getId() + "");
					item.setAvatar(contacts.getAvatar());
					item.setDec(contacts.getMobile());
					item.setName(contacts.getRname());
					item.setStatus(contacts.getStatus());
					if (contacts.getRname() == null
							|| contacts.getRname().equals("")) {
						item.setName(contacts.getNickname());
					}

					item.setSex(contacts.getSex());
					item.setType("0");
					list.add(item);

				}
			}
			if (list.size() > 0)
				typeMap.put("0", 0);
		}
	}

	class ItemsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (list.size() > 0) {
				return list.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder mViewHolder;
			if (convertView == null || convertView.getTag() == null) {
				view = View.inflate(mcontext, R.layout.im_search_list_item,
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
				mViewHolder.text_status = (TextView) view
						.findViewById(R.id.text_status);
				mViewHolder.text_status
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (null != v.getTag()) {
									try {
										Item item = (Item) v.getTag();
										searchContacts(item.getDec());
									} catch (Exception e) {
										// TODO: handle exception
									}
								}
							}
						});
				view.setTag(mViewHolder);
			} else {
				view = convertView;
				mViewHolder = (ViewHolder) view.getTag();
			}

			Item item = list.get(position);
//				if (typeMap.get(item.getType()) == position) {
//					mViewHolder.tvCatalog.setVisibility(View.VISIBLE);
//					if (item.getType().equals("0")) {
//						mViewHolder.tvCatalog.setText("联系人");
//					} else if (item.getType().equals("1")) {
//						mViewHolder.tvCatalog.setText("群组");
//					} else if (item.getType().equals("2")) {
//						mViewHolder.tvCatalog.setText("聊天记录");
//					}
//				} else {
//					mViewHolder.tvCatalog.setVisibility(View.GONE);
//				}
				if (item.getType().equals("0")) {
					mViewHolder.tvCatalog.setText("联系人");
				} else if (item.getType().equals("1")) {
					mViewHolder.tvCatalog.setText("群组");
				} else if (item.getType().equals("2")) {
					mViewHolder.tvCatalog.setText("聊天记录");
				}
				mViewHolder.name_tv.setText(item.getName());

				if (item.getType().equals("0")) {
					mViewHolder.account.setVisibility(View.VISIBLE);
					mViewHolder.account.setText(item.dec);

					int sex = item.getSex();
					if (!AbStrUtil.isEmpty(item.getAvatar())) {
						ImageLoader.getInstance().displayImage(
								WebUrl.FILE_LOAD_URL + item.getAvatar(),
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
					if (from == FROM_MOBAILCONTACTS) {
						mViewHolder.text_status.setVisibility(View.VISIBLE);
						if (item.getStatus() == 0) {
							mViewHolder.text_status.setText("已添加");
							mViewHolder.text_status.setTextColor(res
									.getColor(R.color.gray));
							mViewHolder.text_status
									.setBackgroundColor(Color.TRANSPARENT);
							mViewHolder.text_status.setTag(null);

						} else {
							mViewHolder.text_status.setText("添加");
							mViewHolder.text_status.setTextColor(res
									.getColor(R.color.orange_red));
							mViewHolder.text_status
									.setBackgroundResource(R.drawable.btn_shape_orange_all);
							mViewHolder.text_status.setTag(item);
						}
					} else {
						mViewHolder.text_status.setVisibility(View.GONE);
					}

				} else if (item.getType().equals("1")) {
					mViewHolder.account.setVisibility(View.GONE);
					ImageLoader.getInstance().displayImage(
							"",
							mViewHolder.mAvatar,
						optionsSquare_group);
					mViewHolder.text_status.setText("");
				} else if (item.getType().equals("2")) {

				}

			return view;
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
			TextView text_status;
		}
	}

	class Item {
		private String id;
		private String type;
		private String avatar;
		private String name;
		private String dec;
		private int sex;
		private int status;
		private int itemType;//0为联系人，1为会话，2为群组

		public int getStatus() {
			return status;
		}

		public int getItemType() {
			return itemType;
		}

		public void setItemType(int itemType) {
			this.itemType = itemType;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDec() {
			return dec;
		}

		public void setDec(String dec) {
			this.dec = dec;
		}

		public int getSex() {
			return sex;
		}

		public void setSex(int sex) {
			this.sex = sex;
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_cancle:
			finish();
			break;
		default:
			break;
		}

	}

	boolean searchPinYin(String searchText, String pinyin) {
		if (searchText.isEmpty() || pinyin.isEmpty()) {
			return false;
		}

		searchText = searchText.toUpperCase();
		pinyin = pinyin.toUpperCase();

		String[] pinyinAry = pinyin.split(" ");

		for (int i = 0; i < pinyinAry.length; ++i) {
			if (matchPinYin(pinyinAry, i, searchText)) {
				return true;
			}
		}
		return false;
	}

	boolean matchPinYin(String[] pinyinAry, int index, String searchText) {
		// 尾部了返回
		if (searchText.isEmpty()) {
			return true;
		}

		if (index >= pinyinAry.length) {
			return false;
		}

		String word = pinyinAry[index];
		// 跳过空格
		if (word.isEmpty()) {
			return matchPinYin(pinyinAry, index + 1, searchText);
		}

		// 全拼音匹配
		if (searchText.startsWith(word)) {
			return matchPinYin(pinyinAry, index + 1,
					searchText.substring(word.length()));
		}

		// 只有首字母匹配
		if (word.charAt(0) == searchText.charAt(0)) {
			return matchPinYin(pinyinAry, index + 1, searchText.substring(1));
		}

		return false;
	}

	private void searchContacts(String moblie) {
		// TODO Auto-generated method stub

		showprocessdialog();
		JSONObject json = new JSONObject();
		try {
			json.put("mobile", moblie);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().getData(
				new ContactsHandler(SearchActivity.this, moblie),
				SearchActivity.this, SUCCESS, FAIL, json, WebUrl.CONTACT_PATH,
				tools.getValue(Constants.AUTHENT));
	}

	private static final int SUCCESS = 0x1055;
	private static final int FAIL = 0x1056;

	class ContactsHandler extends MyHandler {
		private String mobile;

		public ContactsHandler(Activity leakActivity, String mobile) {
			super(leakActivity);
			this.mobile = mobile;
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
							ContactLogic.sendSystemMessage(SearchActivity.this,
									mobile);
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
				ToastUtils.showToast(SearchActivity.this, "亲，您的网络不顺畅哦！");
				break;
			default:
				break;
			}
		}

	}
}
