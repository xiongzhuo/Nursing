package com.im.sdk.dy.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.deya.acaide.R;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.TabBaseFragment;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;
import com.im.sdk.dy.common.base.OverflowAdapter;
import com.im.sdk.dy.common.base.OverflowAdapter.OverflowItem;
import com.im.sdk.dy.common.base.OverflowHelper;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.service.IMNotifyReceiver;
import com.im.sdk.dy.ui.contact.MobileContactActivity;
import com.im.sdk.dy.ui.contact.MobileContactSelectActivity;
import com.im.sdk.dy.ui.group.CreateGroupActivity;

public class FirstFragment extends TabBaseFragment implements OnClickListener {
	private ViewPager mPager;
	private RadioGroup mGroup;
	private View view;
//	private TopBarView barView;
	private OverflowHelper mOverflowHelper;
	private OverflowAdapter.OverflowItem[] mItems;
	private ContactsDepartmentFragment contactsFragment;
	ConversationListFragment conversationListFragment;
	private ImageView text_right1;
	private Animation animation;
	private int currIndex = 0;
	private int position_one;
	private ImageView iv_movebg;
	Tools tools;
	
	
	private int iv_movebgWidth;
	public final static int OPTION_SYNC_CONTACTS = 0x11;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		contactsFragment = new ContactsDepartmentFragment();
		conversationListFragment = new ConversationListFragment();
		tools=new Tools(getActivity(), Constants.AC);
		view = inflater.inflate(R.layout.fragment_first, null);
//		barView = (TopBarView) view.findViewById(R.id.lay_top);
		mOverflowHelper = new OverflowHelper(getActivity());
//		barView.setTopBarToStatus(1, -1, -1, getString(R.string.str_tab_home),
//				this);
		mPager = (ViewPager) view.findViewById(R.id.content);
		mGroup = (RadioGroup) view.findViewById(R.id.group);
		mGroup.setOnCheckedChangeListener(new CheckedChangeListener());
		mGroup.check(R.id.one);
		mPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
		mPager.setOnPageChangeListener(new PageChangeListener());
		mPager.setOffscreenPageLimit(2);
		view.findViewById(R.id.lay_search).setOnClickListener(this);

		text_right1=(ImageView) view.findViewById(R.id.text_right1);
		
		/**群聊和添加新朋友*/
		setDePopWindow(inflater);
		
//		initWidth(view);
		

		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		isSignedSucUser(tools);//判断是否为认证用户
		//synContact();//更新联系人
	}
	
//	private void initWidth(View mView) {
//		iv_movebg = (ImageView) mView.findViewById(R.id.iv_movebg);
//		iv_movebg.setAlpha(100);
//		iv_movebgWidth = iv_movebg.getLayoutParams().width;
//		Log.d("ViewPageFragment", "cursor imageview width=" + iv_movebgWidth);
//		DisplayMetrics dm = new DisplayMetrics();
//		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int screenW = dm.widthPixels;
//		position_one = (int) (screenW / 5.0);
//		
//	}
	

	private void synContact() {
		//发送查询广播
		Intent intent=new Intent();
    	intent.setAction(IMNotifyReceiver.ACTION_NOTIRECEIVER_IM);
    	intent.putExtra(IMNotifyReceiver.RECEIVER_OPTION, IMNotifyReceiver.OPTION_SYNC_CONTACTS);
    	MyAppliaction.getContext().sendBroadcast(intent);
		
	}



	private void setDePopWindow(LayoutInflater inflater) {
		// TODO Auto-generated method stub

		
		
        // 引入窗口配置文件 
        View view_screen = inflater.inflate(R.layout.screen_layout, null); 
        // 创建PopupWindow对象 
        final PopupWindow pop = new PopupWindow(view_screen, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false); 
        pop.setAnimationStyle(R.style.popwin_anim_style);
       
        // 需要设置一下此参数，点击外边可消失 
        pop.setBackgroundDrawable(new BitmapDrawable()); 
        //设置点击窗口外边窗口消失 
        pop.setOutsideTouchable(true); 
        // 设置此参数获得焦点，否则无法点击 
        pop.setFocusable(true); 
        text_right1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(pop.isShowing()) { 
                  // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏 
                  pop.dismiss(); 
              } else { 
                  // 显示窗口 
                  pop.showAsDropDown(v); 
              } 
			}
		});
        
        view_screen.findViewById(R.id.chating_group).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(getActivity(),
//						MobileContactSelectActivity.class));

				Intent intent = new Intent(getActivity(),
						MobileContactSelectActivity.class);
				intent.putExtra("is_discussion", true);
				intent.putExtra("isFromCreateDiscussion", true);
				intent.putExtra("group_select_need_result", true);
				startActivity(intent);
				
				 pop.dismiss(); 
			}
		});
        
        view_screen.findViewById(R.id.add_friend).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						MobileContactActivity.class);

				// Intent intent = new Intent(getActivity(),
				// WXEntryActivity.class);
				getActivity().startActivity(intent);
				 pop.dismiss(); 
			}
		});
        
        
	}

	private class CheckedChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.one:
				mPager.setCurrentItem(0);
				
				break;
			case R.id.two:
				
				mPager.setCurrentItem(1);
				break;

			}
			
		
		}
	}

	private class PageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				mGroup.check(R.id.one);
//				if (currIndex == 1) {
//					animation = new TranslateAnimation(position_one, 0, 0,
//							0);
//				}
				break;
			case 1:
				mGroup.check(R.id.two);
//				if (currIndex == 0) {
//					animation = new TranslateAnimation(0, position_one, 0,
//							0);
//				} 
				
				break;

			}
			
//			currIndex = position;
//			animation.setFillAfter(true);// 动画结束后停留在最后位置
//			animation.setDuration(300);// 动画持续时间为300毫秒
//			iv_movebg.startAnimation(animation);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
//			 ConversationListFragment frag = new ConversationListFragment();
//			 Bundle bundle = new Bundle();
//			 bundle.putString("key", "hello world " + position);
//			 frag.setArguments(bundle);
			return position==0?new ConversationListFragment():new ContactsDepartmentFragment();
//			return position==0?new ContactsFragmentNew():new ContactsFragmentNew();
		}

		@Override
		public int getCount() {
			return 2;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lay_search:
			Intent intent = new Intent(getActivity(), SearchActivity.class);
			intent.putExtra(SearchActivity.FROM,
					SearchActivity.FROM_CONVERSATION);
			startActivity(intent);
			break;
		case R.id.text_right1:
			// Intent intentAdd = new Intent(getActivity(),
			// CreateGroupActivity.class);
			// startActivity(intentAdd);
			controlPlusSubMenu();
			break;
		default:
			break;
		}
	}

	private void controlPlusSubMenu() {
		if (mOverflowHelper == null) {
			return;
		}

		if (mOverflowHelper.isOverflowShowing()) {
			mOverflowHelper.dismiss();
			return;
		}

		if (mItems == null) {
			initOverflowItems();
		}
		LogUtil.d(mOverflowHelper + "");
		mOverflowHelper.setOverflowItems(mItems);
		mOverflowHelper
				.setOnOverflowItemClickListener(mOverflowItemCliclListener);
		mOverflowHelper.showAsDropDown(view.findViewById(R.id.text_right1));
	}

	private final AdapterView.OnItemClickListener mOverflowItemCliclListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			controlPlusSubMenu();

			OverflowItem overflowItem = mItems[position];
			String title = overflowItem.getTitle();

			if (getString(R.string.main_plus_groupchat).equals(title)) {
				// 创建群组
				startActivity(new Intent(getActivity(),
						CreateGroupActivity.class));
			}
			// else if (getString(R.string.main_plus_querygroup).equals(title))
			// {
			// // 群组搜索
			// startActivity(new Intent(getActivity(),BaseSearch.class));
			// } else if (getString(R.string.main_plus_mcmessage).equals(title))
			// {
			// handleStartServiceEvent();
			//
			// } else if (getString(R.string.main_plus_settings).equals(title))
			// {
			// // 设置;
			// startActivity(new Intent(getActivity(),SettingsActivity.class));
			//
			//
			// }
			else if (getString(R.string.create_discussion).equals(title)) {

				Intent intent = new Intent(getActivity(),
						MobileContactSelectActivity.class);
				intent.putExtra("is_discussion", true);
				intent.putExtra("isFromCreateDiscussion", true);
				intent.putExtra("group_select_need_result", true);
				startActivity(intent);

			}
			// else if(getString(R.string.query_discussion).equals(title)){
			//
			// Intent intent=new Intent(getActivity(),
			// ECDiscussionActivity.class);
			// intent.putExtra("is_discussion", true);
			//
			// startActivity(intent);
			//
			// }
		}

	};

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

}
