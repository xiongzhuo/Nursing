/**
 * 日期:2015年6月20日下午4:53:13 . <br/>
 * 
 *
 */

package com.deya.hospital.descover;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.HotVo;
import com.deya.hospital.vo.QuestionVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:. SurrounFragment【周边主界面】 <br/>
 */
public class QuestionSortFragment extends BaseFragment implements
		OnClickListener {
	// 缓存Fragment的View

	private static final int SEARCH_TYPE_SUCCESS = 0x70007;
	private static final int SEARCH_TYPE_FAIL = 0x70008;
	private RadioButton recommendRadio;
	private RadioButton newsRadio;
	private RadioButton minsRadio;
	private Button questionRadio;
	private RadioGroup group;
	private ViewPager shoppager;
	private SurroundFragemtsAdapter myadapter;
	public int correctposleft = 0;
	public int correctposright = 0;
	private List<Fragment> listfragment;
	private DrawerLayout mdrawerLayout = null;
	private EditText imgSearch;
	private DisplayMetrics dm;
	LinearLayout rlPrarent;
	ListView mlistView;
	private SearchListAapter sortAdapter;
	RelativeLayout imgBack;
	TextView cancleSeach;
	List<QuestionVo> searchList = new ArrayList<QuestionVo>();
	Tools tools;
	TextView searchTv, empertyView;
	private int[] wh;
	LinearLayout searchLay;
	GridView searchGv;
	HotSearchAdapter hotAdapter;
	LayoutInflater inflater;
	List<HotVo> hotlist = new ArrayList<HotVo>();
	String searchType = "";
	String keyWords = "";
//	RelativeLayout top1;
	LinearLayout top2;
	int currentposition=0;
	private View view;
	private Context mcontext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		if (view == null) {
			view = inflater.inflate(R.layout.magzine_search_activity, container, false);
			tools = new Tools(getActivity(), Constants.AC);
			mcontext=getActivity();
			wh = AbViewUtil.getDeviceWH(getActivity());
			initMyHandler();
			initViews();
			initSearchView();
		
		} else {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}

		}
	
		return view;
		
	}

	
	
	void checkNetWork(){
		if(NetWorkUtils.isConnect(mcontext)){
			networkView.setVisibility(View.GONE);
		}else{
			networkView.setVisibility(View.VISIBLE);
		}
	}
	int depatCheckPosition = 0;
	boolean isSelecte=true;
	private LinearLayout networkView;
	private void initSearchView() {
		searchLay = (LinearLayout) view.findViewById(R.id.searchLay);

		searchGv = (GridView) view.findViewById(R.id.searchGv);

		hotAdapter = new HotSearchAdapter(mcontext, hotlist);
		searchGv.setAdapter(hotAdapter);
		searchGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				hotAdapter.setChooseItem(position);
				searchType = hotlist.get(position).getId();
			
				if (depatCheckPosition==position) {
					
				
				if (isSelecte) {
				
					hotAdapter.setChooseItem(position);
					hotAdapter.notifyDataSetChanged();
					isSelecte=false;
				}else {
					searchType="";
					hotAdapter.setChooseItem(-1);
					hotAdapter.notifyDataSetChanged();
					isSelecte=true;
				}
				}
				depatCheckPosition=position;
				startSearch();
			}
		});
		
		

		mlistView = (ListView) view.findViewById(R.id.searlist);
		sortAdapter = new SearchListAapter();
		mlistView.setAdapter(sortAdapter);
		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(mcontext, QuetionDetailActivity.class);
				it.putExtra("data", searchList.get(position));
				startActivity(it);
			}
		});

	}

	public void initViews() {

		rlPrarent = (LinearLayout) view.findViewById(R.id.rl_rarent);
		imgBack = (RelativeLayout) view.findViewById(R.id.rl_back);
		cancleSeach = (TextView) view.findViewById(R.id.cancle_seach);
		networkView=(LinearLayout) view.findViewById(R.id.networkView);
		checkNetWork();
		cancleSeach.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imgSearch.setText("");
				// 先隐藏键盘
				((InputMethodManager) imgSearch.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(getActivity().getCurrentFocus()
								.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				onSetSearchVisible(false);
				cancleSeach.setVisibility(View.GONE);
				imgSearch.setCursorVisible(false);
				hotAdapter.setChooseItem(-1);
//				top1.setVisibility(View.GONE);
				top2.setVisibility(View.VISIBLE);
				imgSearch.setHint("");
			}
		});
		top2=(LinearLayout) view.findViewById(R.id.top2);
		top2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSetSearchVisible(true);
				imgSearch.setCursorVisible(true);
				cancleSeach.setVisibility(View.VISIBLE);
				imgSearch.requestFocus();
				top2.setVisibility(View.GONE);
				imgSearch.setHint("搜索/问题");
			}
		});
		searchTv = (TextView) view.findViewById(R.id.searchTv);
		searchTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startSearch();

			}
		});
	

		listfragment = new ArrayList<Fragment>();
		listfragment.add(new SortByRecommendFragment());
		listfragment.add(new SortByNewFragment());
		listfragment.add(new SortByMineFragment());
		dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		shoppager = (ViewPager) view.findViewById(R.id.order_pager);
		shoppager.setSaveEnabled(false);
		myadapter = new SurroundFragemtsAdapter(getChildFragmentManager(),
				listfragment);
		shoppager.setAdapter(myadapter);

		imgSearch = (EditText) view.findViewById(R.id.et_search);

		imgSearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView view, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					((InputMethodManager) imgSearch.getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(getActivity().getCurrentFocus()
									.getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					if (imgSearch.getText().toString().trim().length() > 0) {
						startSearch();

					}
					
					return true;
				}
				return false;
			}
		});

		imgSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSetSearchVisible(true);
				imgSearch.setCursorVisible(true);
				cancleSeach.setVisibility(View.VISIBLE);
				top2.setVisibility(View.GONE);
				imgSearch.requestFocus();
				imgSearch.setHint("搜索/问题");
			}
		});
		
		imgSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
//					onSetSearchVisible(true);
					keyWords = imgSearch.getText().toString().trim();
					startSearch();
					
				} else {
//					onSetSearchVisible(false);
					searchList.removeAll(searchList);
					sortAdapter.notifyDataSetChanged();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		group = (RadioGroup) view.findViewById(R.id.group);
		recommendRadio = (RadioButton) view.findViewById(R.id.radio_down);
		newsRadio = (RadioButton) view.findViewById(R.id.radio_hit);
		minsRadio = (RadioButton) view.findViewById(R.id.radio_luyong);
		questionRadio = (Button) view.findViewById(R.id.radio_ques);
		questionRadio.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		recommendRadio.setChecked(true);
		recommendRadio.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int view) {
				onSetSearchVisible(false);
				switch (view) {
				case R.id.radio_down:
					shoppager.setCurrentItem(0);// 设置当前显示标签页为第一页
					hotAdapter.setChooseItem(-1);
					imgSearch.setText("");
					break;
				case R.id.radio_hit:
					shoppager.setCurrentItem(1);// 设置当前显示标签页为第一页
					hotAdapter.setChooseItem(-1);
					imgSearch.setText("");
					break;
				case R.id.radio_luyong:
					shoppager.setCurrentItem(2);// 设置当前显示标签页为第一页
					hotAdapter.setChooseItem(-1);
					imgSearch.setText("");
					break;
				default:
					break;
				}
			}
		});
		shoppager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				recommendRadio.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
				newsRadio.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
				minsRadio.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
				switch (arg0) {
			
				case 0:
					recommendRadio.setChecked(true);
					recommendRadio.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
					hotAdapter.setChooseItem(-1);
					imgSearch.setText("");
					currentposition=0;
					break;
				case 1:
					newsRadio.setChecked(true);
					newsRadio.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
					hotAdapter.setChooseItem(-1);
					imgSearch.setText("");
					break;
				case 2:
					minsRadio.setChecked(true);
					minsRadio.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
					hotAdapter.setChooseItem(-1);
					imgSearch.setText("");
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		view.findViewById(R.id.radio_ques).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),
						AskQustionActivity.class));
			}
		});

		getSearchType();
	}

	private void getSearchType() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			Log.i("1111", job.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler,
				getActivity(), SEARCH_TYPE_SUCCESS,
				SEARCH_TYPE_FAIL, job, "questions/questionTypeList");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		default:
			break;
		}
	}

	public ViewPager getPager() {
		return shoppager;
	}

	/**
	 * .注册相关广播
	 */
	@Override
	public void onResume() {
		super.onResume();

	}

	/**
	 * .注销广播或者注销线程等操作
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void onSetSearchVisible(boolean visible) {
		if (visible) {
			shoppager.setVisibility(View.GONE);
			searchLay.setVisibility(View.VISIBLE);
		} else {
			searchLay.setVisibility(View.GONE);
			shoppager.setVisibility(View.VISIBLE);
		}
	}

	private static final int SEARCH_SUCCESS = 0x009912;
	private static final int SEARCH_FAIL = 0x009913;

	protected void startSearch() {
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {

			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("keyword", keyWords);
			job.put("q_type", searchType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("11111", "----------------" + json.toString());
		MainBizImpl.getInstance().onCirclModeRequest(myHandler,
				getActivity(), SEARCH_SUCCESS, SEARCH_FAIL, job,
				"questions/latestList");

	}

	
	
	
	private void initMyHandler() {
		myHandler = new MyHandler(getActivity()) {
			@Override
			public void handleMessage(Message msg) {
				Activity activity = myHandler.mactivity.get();
				if (null != activity) {
					switch (msg.what) {
					case SEARCH_SUCCESS:
						if (null != msg && null != msg.obj) {
							try {
								setSearchData(new JSONObject(msg.obj.toString()));
							} catch (JSONException e5) {
								e5.printStackTrace();
							}
						}
						break;
					case SEARCH_FAIL:
						ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
						break;
					case SEARCH_TYPE_SUCCESS:
						if (null != msg && null != msg.obj) {
							try {
								setSearchTypeData(new JSONObject(msg.obj.toString()));
							} catch (JSONException e5) {
								e5.printStackTrace();
							}
						}
						break;

					default:
						break;
					}
				}
			}
		};
	}

	private MyHandler myHandler = new MyHandler(getActivity()) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case SEARCH_SUCCESS:
					if (null != msg && null != msg.obj) {
						try {
							setSearchData(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case SEARCH_FAIL:
					ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
					break;
				case SEARCH_TYPE_SUCCESS:
					if (null != msg && null != msg.obj) {
						try {
							setSearchTypeData(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				default:
					break;
				}
			}

		}

	};

	protected void setSearchData(JSONObject jsonObject) {
		Log.i("search", jsonObject.toString());
		searchList.clear();

		if (jsonObject.has("list")) {
			JSONArray jarr = jsonObject.optJSONArray("list");
			List<QuestionVo> list = gson.fromJson(jarr.toString(),
					new TypeToken<List<QuestionVo>>() {
					}.getType());
			searchList.addAll(list);
			sortAdapter.notifyDataSetChanged();
		}

	}

	Gson gson = new Gson();

	protected void setSearchTypeData(JSONObject jsonObject) {

		if (jsonObject.has("list")) {
			JSONArray jarr = jsonObject.optJSONArray("list");
			Gson gson = new Gson();
			List<HotVo> list = gson.fromJson(jarr.toString(),
					new TypeToken<List<HotVo>>() {
					}.getType());
			hotlist.addAll(list);
			sortAdapter.notifyDataSetChanged();

		}

	}

	public class SearchListAapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return searchList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return searchList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (null == convertView) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(
						R.layout.question_search_list_item, null);
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.title);
				viewHolder.answerNum = (TextView) convertView
						.findViewById(R.id.answerNum);
				viewHolder.textLay=(RelativeLayout) convertView.findViewById(R.id.textLay);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			QuestionVo qVo = searchList.get(position);
			viewHolder.title.setText(qVo.getTitle());
			viewHolder.answerNum.setText("(" + qVo.getAnswer_count() + "个回答)");
			setTextView(viewHolder.title, viewHolder.answerNum, viewHolder.textLay);
			return convertView;
		}

	}
	public void setTextView(final TextView tx1, final TextView tx2,
			final RelativeLayout layout) {

		tx1.post(new Runnable() {
			@SuppressLint("NewApi")
			@Override
			public void run() {
				final int with = tx1.getMeasuredWidth()
						+ tx2.getMeasuredWidth();
				final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.FILL_PARENT);
				Layout layout2 = tx1.getLayout();

				int lineCount = tx1.getLineCount();
				if (lineCount == 1 && wh[0] < with) {
					layoutParams.height = tx1.getMeasuredHeight()
							+ AbViewUtil.dp2Px(getActivity(), 25);
					layoutParams.width = ViewGroup.LayoutParams.FILL_PARENT;
					layout.setLayoutParams(layoutParams);
					Log.i("Adapter",
							tx1.getLineCount() + "------" + tx1.getText()
									+ "执行1");
				} else if (lineCount > 1
						&& layout2.getLineWidth(0)
								- layout2.getLineWidth(lineCount - 1) < (tx2
								.getMeasuredWidth() - AbViewUtil.dp2Px(getActivity(), 15))) {
					layoutParams.height = tx1.getMeasuredHeight()
							+ AbViewUtil.dp2Px(getActivity(), 25);
					layoutParams.width = ViewGroup.LayoutParams.FILL_PARENT;
					Log.i("Adapter",
							tx1.getLineCount() + "------" + tx1.getText()
									+ "执行2");
					layout.setLayoutParams(layoutParams);
				} else {
					layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
					layoutParams.width = ViewGroup.LayoutParams.FILL_PARENT;
					layout.setLayoutParams(layoutParams);
					Log.i("Adapter",
							tx1.getLineCount() + "------" + tx1.getText()
									+ "执行3");
				}
				Log.i("Adapter", tx1.getLineCount() + "------" + tx1.getText());
			}

		});

	}
	class ViewHolder {
		TextView title;
		TextView answerNum;
		RelativeLayout textLay;

	}
}
