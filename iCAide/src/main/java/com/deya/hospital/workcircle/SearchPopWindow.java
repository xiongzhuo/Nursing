package com.deya.hospital.workcircle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.deya.acaide.R;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.HotVo;
import com.deya.hospital.vo.SearchCacheVo;
import com.deya.hospital.widget.view.ButtonTextViewGroup;
import com.deya.hospital.widget.view.TextViewGroup;
import com.deya.hospital.widget.view.TextViewGroup.OnTextViewGroupItemClickListener;
import com.deya.hospital.widget.view.TextViewGroup.TextViewGroupItem;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

public class SearchPopWindow extends PopupWindow implements OnClickListener {

	private View mMenuView;
	private ListView popLv;
	LayoutInflater inflater;
	private TextView yesTv, cancelTv, titleTv;
	Context mcontext;
	private ImageView imgBack, imgClear;
	private TextView imgSearch;
	private EditText edtsearch;
	private ListView historyListView;
	HistoryAdapter hAdapter;
	List<SearchCacheVo> historyList = new ArrayList<SearchCacheVo>();
	Gson gson = new Gson();
	private ButtonTextViewGroup departgv;
	// 分页
	Tools tools;
	LinearLayout cacheView;
	String key = "";
	List<HotVo> list;
	private LinearLayout networkView;
	String channelId="";
	String channeName="";
	List<TextViewGroup.TextViewGroupItem> reasonList = new ArrayList<TextViewGroup.TextViewGroupItem>();

	public SearchPopWindow(OnItemClickListener listener, Context contex,List<HotVo> list,String channelId,String channeName) {
		super(contex);
		reasonList.clear();
		mcontext = contex;
		inflater = LayoutInflater.from(contex);
		mMenuView = inflater.inflate(R.layout.circle_search_activity, null);
		tools = new Tools(mcontext, Constants.AC);
		this.list=list;
		getHistoryData();
		this.channelId=channelId;
		this.channeName=channeName;
		initView();

		titleTv = (TextView) mMenuView.findViewById(R.id.title);
		// 设置按钮监听
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.popupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(mcontext.getResources().getDrawable(
				android.R.color.transparent));
		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
			}
		});

	}

	private void initView() {
		imgBack = (ImageView) mMenuView.findViewById(R.id.img_back);
		imgSearch = (TextView) mMenuView.findViewById(R.id.searchImg);
		edtsearch = (EditText) mMenuView.findViewById(R.id.et_search);
		edtsearch.setHint("搜索"+(channelId.equals("0")?"":channeName));
		imgClear = (ImageView) mMenuView.findViewById(R.id.clear);
		imgClear.setOnClickListener(this);
		cacheView = (LinearLayout) mMenuView.findViewById(R.id.searchView);
		historyListView = (ListView) mMenuView.findViewById(R.id.listView);
		hAdapter = new HistoryAdapter(historyList);
		historyListView.setAdapter(hAdapter);
		imgSearch.setOnClickListener(this);

		edtsearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView view, int actionId,
										  KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String text = edtsearch.getText().toString().trim();
					startSearch(text);
					return true;
				}
				return false;
			}
		});

		historyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				startSearch(historyList.get(position).getKey().toString());
			}
		});
		departgv = (ButtonTextViewGroup) mMenuView.findViewById(R.id.hotRadio);
		TextViewGroup.TextViewGroupItem item = null;
		for (HotVo hv : list) {
			item = departgv.NewTextViewGroupItem();
			item.setText(hv.getName());
			reasonList.add(item);
		}
		departgv.setTextViews(reasonList);
		departgv.setOnTextViewGroupItemClickListener(new OnTextViewGroupItemClickListener() {

			@Override
			public void OnTextViewGroupClick(View view,
											 List<TextViewGroupItem> _dataList, TextViewGroupItem item) {

				startSearch(item.getText());

			}
		});

	}

	public void startSearch(String text) {
		boolean needAdd = true;
		for (SearchCacheVo sv : historyList) {
			if (sv.getKey().equals(text)) {
				needAdd = false;
				break;
			}
		}
		if (needAdd) {
			SearchCacheVo sv = new SearchCacheVo();
			sv.setKey(text);
			if (historyList.size() >= 10) {
				historyList.remove(10);
			}
			historyList.add(0, sv);
			hAdapter.notifyDataSetChanged();
			try {
				DataBaseHelper.getDbUtilsInstance(mcontext)
						.deleteAll(SearchCacheVo.class);
				DataBaseHelper.getDbUtilsInstance(mcontext)
						.saveAll(historyList);
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		edtsearch.setText(text);
		AbViewUtil.colseVirtualKeyboard(mcontext);
		hAdapter.notifyDataSetChanged();
		Intent it = new Intent(mcontext, CircleSearchActivity.class);
		it.putExtra("key", text);
		it.putExtra("chanelName",channeName);
		it.putExtra("chanelId", this.channelId);
		mcontext.startActivity(it);
		dismiss();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				break;
			case R.id.searchImg:
				dismiss();
				break;
			case R.id.clear:
				cacheView.setVisibility(View.VISIBLE);
				edtsearch.setText("");
				break;
			default:
				break;
		}

	}

	//
	public class HistoryAdapter extends BaseAdapter {
		List<SearchCacheVo> list;

		public HistoryAdapter(List<SearchCacheVo> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return historyList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(null==convertView){
				viewHolder=new ViewHolder();
				convertView=inflater.inflate(R.layout.list_item_left_text, null);
				viewHolder.lefText = (TextView) convertView.findViewById(R.id.listtext);
				viewHolder.centerText = (LinearLayout) convertView.findViewById(R.id.listtextCenter);
				convertView.setTag(viewHolder);

			}else{
				viewHolder=(ViewHolder) convertView.getTag();
			}
			viewHolder.lefText.setText(list.get(position).getKey());
			if(position==historyList.size()-1){
				viewHolder.centerText.setVisibility(View.VISIBLE);
			}else{

				viewHolder.centerText.setVisibility(View.GONE);
			}
			viewHolder.centerText.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					onClearCache();

				}
			});
			return convertView;
		}

	}

	public void onClearCache(){
		historyList.clear();
		hAdapter.notifyDataSetChanged();
		try {
			DataBaseHelper.getDbUtilsInstance(mcontext)
					.deleteAll(SearchCacheVo.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	private class ViewHolder{
		TextView lefText;
		LinearLayout centerText;

	}
	public void getHistoryData() {
		try {
			if (null != DataBaseHelper
					.getDbUtilsInstance(mcontext).findAll(SearchCacheVo.class)) {
				historyList = DataBaseHelper
						.getDbUtilsInstance(mcontext)
						.findAll(SearchCacheVo.class);
				Log.i("1111111111", historyList.size() + "");

			} else {
				DataBaseHelper.getDbUtilsInstance(mcontext)
						.saveAll(historyList);
			}

		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * colseVirtualKeyboard:【隐藏软键盘】. <br/>
	 * .@param activity.<br/>
	 */
	public static void closeVirtualKeyboard(Activity activity) {
		View view = activity.getWindow()
				.findViewById(Window.ID_ANDROID_CONTENT);
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}