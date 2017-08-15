package com.deya.hospital.descover;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.QuestionVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 感控回答-我
 * 
 * @author sunp
 *
 */
public class SortByMineFragment extends BaseFragment {
	protected static final int GET_LIST_SUCESS = 0x200;
	protected static final int GET_LIST_FAIL = 0x201;
	private static final int ZAN_SUCCESS = 0x70001;
	private static final int ZAN_FAIL = 0x70001;
	private static final int DELETE_SUCCESS = 0x203;
	private static final int DELETE_FAIL = 0x204;
	private View view;
	Tools tools;
	private Gson gson;
	private PullToRefreshListView listview;
	private SortMineListAdapter sortAdapter;
	List<QuestionVo> infoList = new ArrayList<QuestionVo>();
	private MyHandler myHandler;
	JSONArray jarr;
	int zanPosition = 0;
	LinearLayout empertyView;
	MyBrodcastReceiver reciReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstance) {
	
		if (view == null) {
			view = inflater.inflate(R.layout.shop_fragment_collection,
					container, false);
			tools = new Tools(getActivity(), Constants.AC);
			gson = new Gson();
			initMyHandler();
			initview();
		} else {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
		}

	


		return view;
	}

	private void initview() {
		listview = (PullToRefreshListView) view.findViewById(R.id.listview);
		sortAdapter = new SortMineListAdapter(getActivity(), infoList,
				myHandler);
		listview.setAdapter(sortAdapter);
		empertyView = (LinearLayout) view.findViewById(R.id.empertyView);
		listview.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent toGuide = new Intent(getActivity(),
								QuetionDetailActivity.class);
						QuestionVo qv = infoList.get(position - 1);
						toGuide.putExtra("data", qv);
						getActivity().startActivity(toGuide);

					}
				});
		listview.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// page=1;
				// sendSortReq();
				listview.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
			}
		});
		
		reciReceiver = new MyBrodcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				super.onReceive(context, intent);
				if (intent.getAction().equals(
						QuestionUpdateBrodcast.ADD_QUESTION)) {
					QuestionVo vo = (QuestionVo) intent
							.getSerializableExtra("data");
					infoList.add(0, vo);
					sortAdapter.notifyDataSetChanged();
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(QuestionUpdateBrodcast.ADD_QUESTION);
		intentFilter.addAction(QuestionUpdateBrodcast.UPDATE_QUESTION);
		getActivity().registerReceiver(reciReceiver, intentFilter);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		page = 1;
		sendSortReq(true);
	}

	protected void doUnclect(String aId) {
		JSONObject job2 = new JSONObject();
		try {
			job2.put("authent", tools.getValue(Constants.AUTHENT));
			// job2.put("article_id", infoList.get(position).getId());
			job2.put("a_id", aId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler, getActivity(),
				ZAN_SUCCESS, ZAN_FAIL, job2, "questions/clickLike");
	}

	public int dp2px(int dp) {
		final float scale = getActivity().getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}

	private void initMyHandler() {
		myHandler = new MyHandler(getActivity()) {

			@Override
			public void handleMessage(Message msg) {
				Activity activity = myHandler.mactivity.get();
				if (null != activity) {
					switch (msg.what) {
					case GET_LIST_SUCESS:
						listview.onRefreshComplete();
						sortAdapter.notifyDataSetChanged();
						Log.i("yug", msg.obj.toString());
						if (null != msg && null != msg.obj) {
							try {
								setMagezineListRes(new JSONObject(
										msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						break;
					case GET_LIST_FAIL:
						listview.onRefreshComplete();
						sortAdapter.notifyDataSetChanged();
						ToastUtil.showMessage("亲，您的网络不顺畅哦！");
						break;

					case ZAN_SUCCESS:
						Log.i("1111", msg.obj.toString());
						if (null != msg && null != msg.obj) {
							try {
								setUnclectReq(new JSONObject(msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						break;
					case DELETE_SUCCESS:
						Log.i("yug", msg.obj.toString());
						if (null != msg && null != msg.obj) {
							try {
								deleteReq(new JSONObject(msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						break;
					case DELETE_FAIL:
						Log.i("yug", msg.obj.toString());
						if (null != msg && null != msg.obj) {
							try {
								deleteReq(new JSONObject(msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
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

	protected void deleteReq(JSONObject jsonObject) {
		if (jsonObject.optString("result_id").equals("0")) {
			// infoList.remove(sortAdapter.getmPosition());
			// sortAdapter.notifyDataSetChanged();
			sendSortReq(true);
		} else {
			ToastUtils.showToast(getActivity(),
					jsonObject.optString("result_msg"));
		}
	}

	protected void setUnclectReq(JSONObject jsonObject) {
		if (jsonObject.optString("result_id").equals("0")) {

			infoList.get(zanPosition).getAnswer()
					.setIs_like(jsonObject.optInt("is_like"));
			infoList.get(zanPosition).getAnswer()
					.setLike_count(jsonObject.optInt("like_count"));
			sortAdapter.notifyDataSetChanged();
		} else {
			ToastUtils.showToast(getActivity(),
					jsonObject.optString("result_msg"));
		}
	}

	private void sendSortReq(boolean isClear) {
		isClear2 = isClear;
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {

			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("pageIndex", page + "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("11111", "----------------" + json.toString());
		MainBizImpl.getInstance()
				.onCirclModeRequest(myHandler, getActivity(), GET_LIST_SUCESS,
						GET_LIST_FAIL, job, "questions/myQuestionList");

	}

	int totalPage = 1;
	int page = 1;
	Boolean isClear2 = false;

	private void setMagezineListRes(JSONObject jsonObject) {
		// infoList.removeAll(infoList);
		if (isClear2) {
			infoList.clear();
		}
		Log.i("11111", "-------推荐列表数据--------" + jsonObject.toString());
		try {
			totalPage = Integer.parseInt(jsonObject.optString("pageTotal"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (jsonObject.has("list")) {
			JSONArray jarr = jsonObject.optJSONArray("list");
			this.jarr = jarr;
			List<QuestionVo> list = gson.fromJson(jarr.toString(),
					new TypeToken<List<QuestionVo>>() {
					}.getType());
			infoList.addAll(list);
			sortAdapter.notifyDataSetChanged();
		}

		if (totalPage > page) {
			listview.setMode(Mode.PULL_UP_TO_REFRESH);
		} else {
			listview.setMode(Mode.PULL_DOWN_TO_REFRESH);

		}

		listview.onRefreshComplete();
		listview.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				infoList.clear();
				page = 1;
				// getTaskByType(searchType);
				sendSortReq(false);
				// listview.onRefreshComplete();
				// listview.setMode(Mode.PULL_UP_TO_REFRESH);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page++;
				// getTaskList(searchType);
				sendSortReq(false);
			}
		});

		if (infoList.size() > 0) {
			empertyView.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
		} else {
			//empertyView.setVisibility(View.VISIBLE);
			listview.setEmptyView(empertyView);
		}

	}

}
