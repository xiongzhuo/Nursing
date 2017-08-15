package com.deya.hospital.workcircle;//package com.deya.acaide.workcircle;
//
//import android.content.Intent;
//import android.graphics.Typeface;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.deya.acaide.R;
//import com.deya.acaide.base.BaseFragment;
//import com.deya.acaide.descover.QuestionSortActivity;
//import com.deya.acaide.util.AbStrUtil;
//import com.deya.acaide.util.AbViewUtil;
//import com.deya.acaide.util.Constants;
//import com.deya.acaide.util.PagerTab;
//import com.deya.acaide.util.SharedPreferencesUtil;
//import com.deya.acaide.util.Tools;
//import com.deya.acaide.vo.HotVo;
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CircleMainFragment extends BaseFragment implements
//		OnPageChangeListener, OnClickListener {
//	private View view;
//	protected static final int GET_SUCESS = 0x6030;
//	protected static final int GET_FAILE = 0x6031;
//	Gson gson = new Gson();
//	Tools tools;
//	List<HotVo> list = new ArrayList<>();
//	private ViewPager mViewPager;
//	private PagerTab mIndicator;
//	private TabAdapter mAdapter;
//	private LinearLayout ic_knowing;
//	private SearchPopWindow dWindow;
//	List<HotVo> hotList = new ArrayList<>();
//	EditText et_search;
//	boolean isRefresh = false;
//	int currentItem = 0;
//	LinearLayout searchView;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//							 Bundle savedInstance) {
//		if (view == null) {
//			view = inflater.inflate(R.layout.circle_main, container, false);
////			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
//
//			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//				view.setPadding(0, AbViewUtil.dp2Px(getActivity(),25), 0,0);
//			}
//			tools = new Tools(activity, Constants.AC);
//			String chanelStr = SharedPreferencesUtil.getString(getActivity(),
//					"circle_channelList", "");
//			HotVo hv1 = new HotVo();
//			hv1.setId("0");
//			hv1.setName("推荐");
//			list.clear();
//
//			list.add(hv1);
//			if (!AbStrUtil.isEmpty(chanelStr)) {
//				try {
//					List<HotVo> listCache = gson.fromJson(chanelStr,
//                            new TypeToken<List<HotVo>>() {
//                            }.getType());
//					if(null!=listCache){
//                        list.addAll(listCache);
//                    }
//				} catch (JsonSyntaxException e) {
//					e.printStackTrace();
//				}
//
//			}
//			initView();
//			getHotCache();
//		} else {
//			ViewGroup parent = (ViewGroup) view.getParent();
//			if (parent != null) {
//				parent.removeView(view);
//			}
//		}
//		mAdapter.notifyDataSetChanged();
//		isRefresh = true;// 判断是不是点击tab 刷新的
//		return view;
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		if (isRefresh) {
//			mIndicator.selectTab(0);
//			mViewPager.setCurrentItem(0);
//			if(null!=searchView){
//				searchView.setVisibility(View.VISIBLE);
//			}
//			isRefresh = false;
//		}
//
//		// 预留
////		for (int i = 0; i < list.size(); i++) {
////			mIndicator.selectTab(i);
////			mViewPager.setCurrentItem(i);
////		}
//
//	}
//
//	public void getHotCache() {
//		hotList.clear();
//		String str = SharedPreferencesUtil.getString(getActivity(), "hotkey","");
//		List<HotVo> cachelist = null;
//		try {
//			cachelist = gson.fromJson(str,new TypeToken<List<HotVo>>() {}.getType());
//		} catch (JsonSyntaxException e) {
//			e.printStackTrace();
//		}
//		if(null!=cachelist){
//			hotList.addAll(cachelist);
//		}
//	}
//
//	private void initView() {
//		searchView=(LinearLayout) view.findViewById(R.id.searchLay);
//		mIndicator = (PagerTab) view.findViewById(R.id.id_indicator);
//		mViewPager = (ViewPager) view.findViewById(R.id.id_pager);
//		mAdapter = new TabAdapter(this.getChildFragmentManager(),list);
//		mViewPager.setAdapter(mAdapter);
////		mViewPager.setOffscreenPageLimit(3);
//		mIndicator.setViewPager(mViewPager);
//		mIndicator.selectTab(0);
//		TextView textview = (TextView) mIndicator.getChildAt(0);
//		textview.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));// 加粗
//		mIndicator.setOnPageChangeListener(this);
//		ic_knowing = (LinearLayout) view.findViewById(R.id.ic_knowing);
//		ic_knowing.setOnClickListener(this);
//		view.findViewById(R.id.radio_ques).setOnClickListener(this);
//		et_search = (EditText) view.findViewById(R.id.et_search);
//
//
//		et_search.setOnClickListener(this);
//		// 显示窗口
//	}
//
//	public void showSearchPop() {
//		dWindow = new SearchPopWindow(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//									int position, long id) {
//				dWindow.dismiss();
//
//			}
//		}, getActivity(), hotList,list.get(currentItem).getId(),list.get(currentItem).getName());
//		dWindow.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM
//				| Gravity.CENTER_HORIZONTAL, 0, 0);
//	}
//
//	@Override
//	public void onDestroy() {
//		if (null != dWindow && dWindow.isShowing()) {
//			dWindow.dismiss();
//		}
//		super.onDestroy();
//	}
//
//	@Override
//	public void onPageScrollStateChanged(int arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onPageScrolled(int arg0, float arg1, int arg2) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onPageSelected(int arg0) {
//		mViewPager.setCurrentItem(arg0);
//		if (currentItem != arg0) {
//			TextView textview = (TextView) mIndicator.getChildAt(arg0);
//			textview.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));// 加粗
//			TextView textview2 = (TextView) mIndicator.getChildAt(currentItem);
//			textview2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));// 加粗
//		}
//		currentItem = arg0;
//		et_search.setHint("搜索"+(currentItem==0?"":list.get(currentItem).getName()));
//		mAdapter.notifyDataSetChanged();
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//			case R.id.radio_ques:
//				Intent in = new Intent(getActivity(), QuestionSortActivity.class);
//				startActivity(in);
//				break;
//			case R.id.et_search:
//				showSearchPop();
//				break;
//
//			default:
//				break;
//		}
//
//	}
//
//}
