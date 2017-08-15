package com.im.sdk.dy.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.core.ContactsCache;
import com.im.sdk.dy.storage.DepartContacts;
import com.im.sdk.dy.ui.contact.BladeView;
import com.im.sdk.dy.ui.contact.ContactsActivity;
import com.im.sdk.dy.ui.contact.ContactsActivity.ContactsFragment;
import com.im.sdk.dy.ui.contact.CustomSectionIndexer;
import com.im.sdk.dy.ui.contact.DepartContactsActivity;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.im.sdk.dy.ui.contact.MobileContactActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactsDepartmentFragment extends TabFragment {
	private static final String TAG = "IMSDK.ContactsFragment";

	/** 当前联系人列表类型（查看、联系人选择） */
	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_SELECT = 2;
	/** 列表类型 */
	private int mType;
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
	private ListView mListView;
	private CustomSectionIndexer mCustomSectionIndexer;
	private ContactsDepartAdapter mAdapter;
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

			if (mAdapter == null || mAdapter.getItem(_position) == null||_position>=mAdapter.getHasMemberSize()) {
				return;
			}
			
			Intent intent=new Intent(getActivity(), DepartContactsActivity.class);
			intent.putExtra("departfrieds",(Serializable)list.get(_position).getDepartmentFriends());
			intent.putExtra("departName",(Serializable)list.get(_position).getDepartment());
			startActivity(intent);

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
		return R.layout.im_contacts_department_fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tools = new Tools(getActivity(), Constants.AC);
		mType = getArguments() != null ? getArguments().getInt("type")
				: TYPE_NORMAL;
		if (positions == null) {
			positions = new ArrayList<Integer>();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.layoutInflate=inflater;
		return inflater.inflate(R.layout.im_contacts_department_fragment,
				container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerReceiver(new String[] { ContactsCache.ACTION_ACCOUT_DY_CONTACTS });
		if (mListView != null) {
			mListView.setAdapter(null);
		}
		mListView = (ListView) findViewById(R.id.address_contactlist);
		// mListView.setDividerHeight(0);
		mListView.setOnItemClickListener(onItemClickListener);
		initContactListView();
	}

	/**
	 * 初始化联系人列表
	 */
	Gson gson=new Gson();
	private void initContactListView() {
		if (mListView != null && mSelectCardItem != null && expertItem != null) {
			mListView.removeHeaderView(mSelectCardItem);
			mListView.removeHeaderView(expertItem);
			mListView.setAdapter(null);
		}

		mAdapter = new ContactsDepartAdapter();
		
		String str=SharedPreferencesUtil.getString(getActivity(), "hospitalFriendList", "");
		list.clear();
		List<DepartContacts> list2 = gson.fromJson(str,
				new TypeToken<List<DepartContacts>>() {
				}.getType());
		if(null!=list2){
		list.addAll(list2);
		}
		if (mType == TYPE_NORMAL) {
			// mSelectCardItem = View.inflate(getActivity(),
			// R.layout.im_contacts_add_bar, null);
			mSelectCardItem = View.inflate(getActivity(), R.layout.new_friend,
					null);
			newFriendItem=View.inflate(getActivity(), R.layout.new_friend,
					null);
			ImageView img=(ImageView) newFriendItem.findViewById(R.id.img);
			img.setImageResource(R.drawable.contact_img);
			TextView text=(TextView) newFriendItem.findViewById(R.id.text);
			text.setText("感控圈好友");
			newFriendItem.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent it=new Intent(getActivity(), ContactsActivity.class);
					startActivity(it);
					
				}
			});
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
			mListView.addHeaderView(newFriendItem);
			
		}
		mListView.setAdapter(mAdapter);
		// 設置頂部固定頭部
		mListView.addHeaderView(LayoutInflater.from(getActivity())
				.inflate(R.layout.im_header_item_cator, mListView, false));
		findViewById(R.id.loading_tips_area).setVisibility(View.GONE);
	}

	@Override
	public void onResume() {
		super.onResume();

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
		if (ContactsCache.ACTION_ACCOUT_DY_CONTACTS.equals(intent.getAction())) {
			
			initContactListView();
		}
	}

	LayoutInflater layoutInflate;
	List<DepartContacts> list=new ArrayList<DepartContacts>();
	
	public class ContactsDepartAdapter extends BaseAdapter {
		int size=tools.getValue_int("haveMemberSize");
		int defultSize=tools.getValue_int("haveMemberSize");
		@Override
		public int getCount() {
			return size;
		}

		public void resetSize(int size){
			this.size=size;
		}
		public int getHasMemberSize(){
			return defultSize;
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(null==convertView){
				viewHolder=new ViewHolder();
				convertView=layoutInflate.inflate(R.layout.item_depart_contacts, null);
				viewHolder.name=(TextView) convertView.findViewById(R.id.nameTv);
				viewHolder.numTv=(TextView) convertView.findViewById(R.id.numTv);
				viewHolder.hospitalTv=(TextView) convertView.findViewById(R.id.hospitalTv);
				viewHolder.hospitalLay=(RelativeLayout) convertView.findViewById(R.id.hospitalLay);
				viewHolder.moreLay=(LinearLayout) convertView.findViewById(R.id.moreLay);
				viewHolder.line=(ImageView) convertView.findViewById(R.id.line);
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag();
			}
			viewHolder.name.setText(list.get(position).getDepartment());
			viewHolder.numTv.setText(list.get(position).getTotal()+"人");
			
			if(position==0){
				viewHolder.hospitalLay.setVisibility(View.VISIBLE);
				viewHolder.hospitalTv.setText(tools.getValue(Constants.HOSPITAL_NAME));
			}else{
				viewHolder.hospitalLay.setVisibility(View.GONE);
			}
			if(position==size-1&&size<list.size()){
				viewHolder.moreLay.setVisibility(View.VISIBLE);
			}else{
				viewHolder.moreLay.setVisibility(View.GONE);
			}
			if(position<defultSize){
				viewHolder.line.setVisibility(View.VISIBLE);
			}else{
				viewHolder.line.setVisibility(View.INVISIBLE);
			}
			viewHolder.moreLay.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					size=list.size();
					notifyDataSetChanged();
					
				}
			});
			//ECContacts contacts=ContactSqlManager.getContact(list.get(position).get)
			return convertView;
		}

	}

	public class ViewHolder {
		ImageView headImg,line;
		TextView name;
		TextView numTv,hospitalTv,moreTv;
		RelativeLayout hospitalLay;
		LinearLayout moreLay;

	}

}
