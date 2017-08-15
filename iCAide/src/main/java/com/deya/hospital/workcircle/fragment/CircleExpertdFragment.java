package com.deya.hospital.workcircle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseViewPagerFragment;
import com.deya.hospital.base.EasyBaseAdapter;
import com.deya.hospital.base.EasyViewHolder;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.CircleImageView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.LoadingView;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.CircleExpertEntity;
import com.deya.hospital.workspace.TaskUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作圈-专家首页
 */
public class CircleExpertdFragment extends BaseViewPagerFragment implements View.OnClickListener{
	private static  final String TAG = "com.deya.hospital.workcircle.fragment.CircleExpertdFragment";
	private static final int GET_DATA_SUCESS = 0x6050;
	private static final int GET_DATA_FAIL = 0x06051;
	private Tools tools;
	private com.handmark.pulltorefresh.library.PullToRefreshListView listview;
	private LoadingView loadingView;

	private int pageIndex = 1;
	private MyAdapter myAdapter;
	private boolean isRefresh = true;
	private List<CircleExpertEntity.ListBean> listBeen = new ArrayList<>();

	private MyHandler myHandler = null;

	/**
	 * 传入需要的参数，设置给arguments
	 * @param id
	 * @return
	 */
	public static CircleExpertdFragment newInstance(int id)
	{
		Bundle bundle = new Bundle();
		bundle.putInt(TAG, id);
		CircleExpertdFragment contentFragment = new CircleExpertdFragment();
		contentFragment.setArguments(bundle);
		return contentFragment;
	}

	@Override
	protected int setBaseView() {
		tools = new Tools(getActivity(), Constants.AC);
		return R.layout.frgment_work_recommend;
	}

	@Override
	protected void initView() {
		bindViews();
		initHandler();
		initEvent();
	}

	@Override
	protected void initData() {
//		if (listBeen == null || listBeen.size() <= 0) {
//			getCache();
//			requestData(pageIndex);
//		}
	}

	private void bindViews() {
		listview = getViewById(R.id.listview);
		loadingView = getViewById(R.id.loadingView);
	}

	private void initHandler() {
		myHandler = new MyHandler(getActivity()) {
			@Override
			public void handleMessage(Message msg) {
				loadingView.stopAnimition();
				loadingView.setVisibility(View.GONE);
				switch (msg.what) {
					case GET_DATA_SUCESS:
						listview.onRefreshComplete();
						if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
							try {

								CircleExpertEntity entity = TaskUtils.gson.fromJson(msg.obj.toString(), CircleExpertEntity.class);
								if (entity != null) {
									if (entity.getResult_id().equals("0")) {
										if (isRefresh) {
											SharedPreferencesUtil.saveString(getActivity(),TAG,msg.obj.toString());
										}
										setRequestData(entity);
									} else {
										ToastUtils.showToast(getActivity(),entity.getResult_msg());
									}
								} else {
//									if (isRefresh) {
//										loadingView.setVisibility(View.VISIBLE);
//										ToastUtils.showToast(getActivity(),"亲，您的网络不顺畅哦！");
//										loadingView.setEmptyView("亲，您的网络不顺畅哦，\n请检查您的网络，再继续访问！");
//									}
								}
							} catch (Exception e5) {
								e5.printStackTrace();
							}
						}
						break;
					case GET_DATA_FAIL:
						listview.onRefreshComplete();
//						if (isRefresh) {
//							loadingView.setVisibility(View.VISIBLE);
//							ToastUtils.showToast(getActivity(),"亲，您的网络不顺畅哦！");
//							loadingView.setEmptyView("亲，您的网络不顺畅哦，\n请检查您的网络，再继续访问！");
//						}
						break;

					default:
						break;
				}

			}
		};
	}

	private void initEvent() {
//		loadingView.setLoadingListener(new LoadingView.LoadingStateInter() {
//
//			@Override
//			public void onloadingStart() {
//				loadingView.setVisibility(View.VISIBLE);
//
//			}
//
//			@Override
//			public void onloadingFinish() {
//				loadingView.setVisibility(View.GONE);
//
//			}
//		});
		loadingView.setVisibility(View.VISIBLE);
		listview.setVisibility(View.GONE);
//		loadingView.startAnimition();
		loadingView.setEmptyView("正在建设中...");
//		myAdapter = new MyAdapter(getActivity(), R.layout.adapter_item_circleoexpert,listBeen);

//		listview.setAdapter(myAdapter);
//		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//				int mPosition = position - 1;
//				if (mPosition >= listBeen.size()) {
//					mPosition = listBeen.size() - 1;
//				}
//				if (listBeen.size()>0 && position >0) {
//					String url = WebUrl.WEB_EXPERT_DETAIL+"?id="+listBeen.get(mPosition).getId();
//					Intent it = new Intent();
//					it.putExtra("url", url);
//					it.setClass(getActivity(), WebViewExpertDtail.class);
//					startActivity(it);
//				}
//			}
//		});

//		listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//				isRefresh = true;
//				pageIndex = 1;
//				requestData(pageIndex);
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//				isRefresh = false;
//				pageIndex = pageIndex + 1;
//				requestData(pageIndex);
//			}
//		});
	}


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	private void requestData( int pageIndex) {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("pageIndex", pageIndex);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler,getActivity(),
				GET_DATA_SUCESS, GET_DATA_FAIL, job,"workCircle/expertList");

	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (loadingView != null) {
			loadingView.stopAnimition();
			loadingView = null;
		}
	}

	@Override
	public void onClick(View v) {

	}

	private void setRequestData(CircleExpertEntity entity) {

		if (entity.getList() != null && entity.getList().size() > 0) {
			if (isRefresh) {
				//下拉刷新
				listBeen.clear();
			}
//			if (entity.getPageTotal() == pageIndex) {
				listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//			} else {
//				listview.setMode(PullToRefreshBase.Mode.BOTH);
//			}
			listBeen.addAll(entity.getList());
			myAdapter.updateListView(listBeen);
		} else {
			if (isRefresh) {
				loadingView.setVisibility(View.VISIBLE);
				loadingView.setEmptyView("抱歉，没有找到相关的内容，\n请稍后再试");
			}
		}
	}

//	public void getCache() {
//		String cacheStr = SharedPreferencesUtil.getString(getActivity(), TAG, "");
//		if (!AbStrUtil.isEmpty(cacheStr)) {
//			CircleExpertEntity entity = GsonImpl.get().toObject (cacheStr, CircleExpertEntity.class);
//			setRequestData(entity);
//		}
//	}


	class MyAdapter extends EasyBaseAdapter<CircleExpertEntity.ListBean> {

		private CircleImageView imgView;
		private TextView tv_author;
		private TextView tv_job;
		private TextView tv_hospital;
		private TextView tv_content;

		public MyAdapter(Context context, int layoutId, List<CircleExpertEntity.ListBean> list) {
			super(context, layoutId, list);
		}

		@Override
		public void convert(EasyViewHolder viewHolder, CircleExpertEntity.ListBean list) {
			tv_author = viewHolder.getTextView(R.id.tv_author);
			tv_content = viewHolder.getTextView(R.id.tv_content);
			tv_job = viewHolder.getTextView(R.id.tv_job);
			imgView = viewHolder.getCircleImageView(R.id.imgView);

			if (!AbStrUtil.isEmpty(list.getName())) {
				tv_author.setText(list.getName()+"\u3000");
			}
			if (!AbStrUtil.isEmpty(list.getPost())) {
				tv_job.setText(list.getPost()+"\u3000");
			}
			tv_hospital.setVisibility(View.VISIBLE);
			if (!AbStrUtil.isEmpty(list.getHospital())) {
				if (!AbStrUtil.isEmpty(list.getDepartment())) {
					tv_hospital.setText(list.getHospital() + list.getDepartment());
				} else {
					tv_hospital.setText(list.getHospital());
				}
			} else if (!AbStrUtil.isEmpty(list.getDepartment())) {
				tv_hospital.setText(list.getDepartment());
			} else {
				tv_hospital.setVisibility(View.GONE);
			}

			if (!AbStrUtil.isEmpty(list.getIntroduce())) {
				tv_content.setText("简介："+list.getIntroduce());
			} else {
				tv_content.setText("简介：");
			}
			if (!AbStrUtil.isEmpty(list.getAvatar())) {
				ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
						+list.getAvatar(), imgView, AbViewUtil.getOptions(R.drawable.contect_loggo));
			} else {
				imgView.setImageResource(R.drawable.contect_loggo);
			}

		}
	}
}
