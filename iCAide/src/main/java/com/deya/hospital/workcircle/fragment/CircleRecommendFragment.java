package com.deya.hospital.workcircle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseViewPagerFragment;
import com.deya.hospital.base.EasyBaseAdapter;
import com.deya.hospital.base.EasyViewHolder;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.descover.QuestionSortActivity;
import com.deya.hospital.dypdf.PdfPreviewActivity;
import com.deya.hospital.shop.ShopGoodsListActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.HomePageBanner;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.vo.AdvEntity;
import com.deya.hospital.vo.WorkCircleRecommentEntity;
import com.deya.hospital.workcircle.WebViewDtail;
import com.deya.hospital.workspace.TaskUtils;
import com.flyco.banner.widget.Banner.base.BaseBanner;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CircleRecommendFragment extends BaseViewPagerFragment implements View.OnClickListener{
	private static  final String TAG = "com.deya.acaide.workcircle.fragment.CircleRecommendFragment";
	private static final int GET_DATA_SUCESS = 0x6050;
	private static final int GET_DATA_FAIL = 0x06051;
	private Tools tools;
	private com.handmark.pulltorefresh.library.PullToRefreshListView listview;
//	private com.deya.acaide.util.LoadingView loadingView;

	private int pageIndex = 1;
	private MyAdapter myAdapter;
	private boolean isRefresh = true;
	private List<WorkCircleRecommentEntity.ListBean> listBeen = new ArrayList<>();

	private MyHandler myHandler = null;
	private LayoutInflater inflaterHead;
	private View headView;
	private HomePageBanner viewPager;
	private List<AdvEntity.ListBean> pagerList = new ArrayList<>();
	private DisplayImageOptions options;


	/**
	 * 传入需要的参数，设置给arguments
	 * @param id
	 * @return
	 */
	public static CircleRecommendFragment newInstance(int id)
	{
		Bundle bundle = new Bundle();
		bundle.putInt(TAG, id);
		CircleRecommendFragment contentFragment = new CircleRecommendFragment();
		contentFragment.setArguments(bundle);
		return contentFragment;
	}

	@Override
	protected int setBaseView() {
		tools = new Tools(getActivity(), Constants.AC);
		inflaterHead = LayoutInflater.from(getActivity());
		return R.layout.frgment_work_recommend;
	}

	@Override
	protected void initView() {
		options= AbViewUtil.getOptions(R.drawable.defult_img);
		bindViews();
		initHandler();
		initEvent();
	}

	@Override
	protected void initData() {
		if (listBeen == null || listBeen.size() <= 0) {
			getCache();
			requestData(pageIndex);
		}
		setAdvPic();
		getCarouselFigure();
	}

	private void bindViews() {
		listview = getViewById(R.id.listview);
//		loadingView = getViewById(R.id.loadingView);
		headView = inflaterHead.inflate(R.layout.workcircle_headview_item, null);
		ListView lv = listview.getRefreshableView();
		viewPager = (HomePageBanner) headView.findViewById(R.id.viewPager);
		lv.addHeaderView(headView);
	}

	private void initHandler() {
		myHandler = new MyHandler(getActivity()) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case GET_DATA_SUCESS:
						listview.onRefreshComplete();
						if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
							try {
								WorkCircleRecommentEntity entity = TaskUtils.gson.fromJson(msg.obj.toString(), WorkCircleRecommentEntity.class);
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
									ToastUtils.showToast(getActivity(),"亲，您的网络不顺畅哦！");
								}
							} catch (Exception e5) {
								e5.printStackTrace();
							}
						}
						break;
					case GET_DATA_FAIL:
						listview.onRefreshComplete();
						ToastUtils.showToast(getActivity(),"亲，您的网络不顺畅哦！");
						break;
					case GETCAROUSELFIGURE_SUCCESS:
						if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
							try {
								AdvEntity entity = TaskUtils.gson.fromJson(msg.obj.toString(), AdvEntity.class);
								if (entity != null) {
									if (entity.getResult_id().equals("0")) {
										setAdvRequestData(entity);
									} else {
										ToastUtils.showToast(getActivity(), entity.getResult_msg());
									}
								} else {
									ToastUtils.showToast(getActivity(), "亲，您的网络不顺畅哦！");
								}
							} catch (Exception e5) {
								e5.printStackTrace();
							} finally {
								setAdvPic();
							}
						}
						break;
					case GETCAROUSELFIGURE_FAIL:
						pagerList.clear();
						setAdvPic();
					default:
						break;
				}

			}
		};
	}

	private void initEvent() {
		myAdapter = new MyAdapter(getActivity(), R.layout.adapter_item_recommend,listBeen);
		listview.setAdapter(myAdapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				int mPosition = position - 2;
				if (mPosition >= listBeen.size()) {
					mPosition = listBeen.size() - 1;
				}
				if (listBeen.size()>0 && position >0) {
					listBeen.get(mPosition).getId();
					String url = "";
					Intent it = new Intent();
					if (listBeen.get(mPosition).getIs_pdf() == 1) {
						if (!AbStrUtil.isEmpty(listBeen.get(mPosition).getPdf_attach())) {
							url = WebUrl.WEB_PDF+"?id="+listBeen.get(mPosition).getId()+"&&pdfid="+listBeen.get(mPosition).getPdf_attach();
						}
						it.putExtra("articleid",listBeen.get(mPosition).getId() + "");
						it.setClass(getActivity(), PdfPreviewActivity.class);
					} else {
						url = WebUrl.WEB_ARTICAL_DETAIL + "?id="+listBeen.get(mPosition).getId();
						it.setClass(getActivity(), WebViewDtail.class);
					}
					it.putExtra("url", url);
					startActivity(it);
				}
			}
		});

		listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				isRefresh = true;
				pageIndex = 1;
				requestData(pageIndex);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				isRefresh = false;
				pageIndex = pageIndex + 1;
				requestData(pageIndex);
			}
		});
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
		MainBizImpl.getInstance().onCirclModeRequest(myHandler,getActivity(), GET_DATA_SUCESS, GET_DATA_FAIL, job,"workCircle/recommendList");

	}

	@Override
	public void onDestroy() {
		if (viewPager != null) {
			viewPager.pauseScroll();
			viewPager = null;
		}
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
//		if (loadingView != null) {
//			loadingView.stopAnimition();
//			loadingView = null;
//		}
	}

	@Override
	public void onClick(View v) {

	}

	private void setRequestData(WorkCircleRecommentEntity entity) {

		if (entity.getList() != null && entity.getList().size() > 0) {
			if (isRefresh) {
				//下拉刷新
				listBeen.clear();
			}
			if (entity.getPageTotal() == pageIndex) {
				listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
			} else {
				listview.setMode(PullToRefreshBase.Mode.BOTH);
			}
			listBeen.addAll(entity.getList());
			myAdapter.updateListView(listBeen);
		}
	}

	public void getCache() {
		String cacheStr = SharedPreferencesUtil.getString(getActivity(), TAG, "");
		if (!AbStrUtil.isEmpty(cacheStr)) {
			WorkCircleRecommentEntity entity =TaskUtils.gson.fromJson (cacheStr, WorkCircleRecommentEntity.class);
			setRequestData(entity);
		}
	}

	public void getCarouselFigure() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("module_id", 2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler, getActivity(),
				GETCAROUSELFIGURE_SUCCESS, GETCAROUSELFIGURE_FAIL, job, "comm/getCarouselFigure");

	}

	private void setAdvRequestData(AdvEntity entity) {
		if (entity.getList() != null && entity.getList().size() > 0) {
			viewPager.setDelay(0).setPeriod(0).setAutoScrollEnable(false)
					.setTouchAble(false).setSource(pagerList).pauseScroll();
			pagerList.clear();
			pagerList.addAll(entity.getList());
		}
	}


	/**
	 * setHomeBanner:【填充广告的数据】. <br/>
	 */
	private static final int GETCAROUSELFIGURE_SUCCESS = 0x11000;
	private static final int GETCAROUSELFIGURE_FAIL = 0x11001;
	private void setAdvPic() {
		if (viewPager == null) {
			return;
		}
		if (!(pagerList.size() > 0)) {
			AdvEntity.ListBean advEntity1 = new AdvEntity.ListBean();
			advEntity1.setDrawable(R.drawable.banner1);
			advEntity1.setType(1);
			advEntity1.setName("");
			pagerList.add(advEntity1);

			AdvEntity.ListBean advEntity2 = new AdvEntity.ListBean();
			advEntity2.setDrawable(R.drawable.banner2);
			advEntity2.setType(2);
			advEntity2.setName("");
			pagerList.add(advEntity2);
			viewPager.pauseScroll();
			viewPager.clearAnimation();
		}
		setHomeBanner();
	}
	/**
	 * setHomeBanner:【填充广告的数据】. <br/>
	 */
	private void setHomeBanner() {
		if (1 == pagerList.size()) {
			viewPager.setDelay(3).setPeriod(3).setAutoScrollEnable(false)
					.setTouchAble(false).setSource(pagerList).startScroll(); // 只有一张广告设置不能滑动可以点击
		} else {// 否则执行自动滚动并设置为可以手动滑动也可以点击
			viewPager.setDelay(3).setPeriod(3).setAutoScrollEnable(true)
					.setTouchAble(true).setSource(pagerList).startScroll();
		}

		viewPager.setOnItemClickL(new BaseBanner.OnItemClickL() {

			@Override
			public void onItemClick(int position) {
					if (pagerList != null && pagerList.size() > 0) {
						int type = pagerList.get(position).getType();
						switch (type) {
							case 0:
								Intent intent = new Intent();
								String url = pagerList.get(position).getHref_url();
								intent.putExtra("url", pagerList.get(position).getHref_url());
								if (url.contains("pdfid")) {
									intent.putExtra("articleid", url.substring(url.indexOf("id=") + 3, url.indexOf("&pdfid=")));
									intent.setClass(getActivity(), PdfPreviewActivity.class);
								} else {
									intent.setClass(getActivity(), WebViewDemo.class);
								}
								startActivity(intent);
								break;
							case 1:
								startActivity(new Intent(getActivity(),ShopGoodsListActivity.class));
								break;
							case 2:
								startActivity(new Intent(getActivity(),QuestionSortActivity.class));
								break;
							default:
								break;
						}
					}
			}
		});
	}

	class MyAdapter extends EasyBaseAdapter<WorkCircleRecommentEntity.ListBean> {

		private TextView titleTv;
		private LinearLayout imgLay;
		private ImageView imgView1;
		private ImageView imgView2;
		private ImageView imgView3;
		private TextView tv_tag;
		private TextView tv_see;
		private TextView tv_like;
		private TextView tv_commont;
		private ImageView imgView;

		public MyAdapter(Context context, int layoutId, List<WorkCircleRecommentEntity.ListBean> list) {
			super(context, layoutId, list);
		}

		@Override
		public void convert(EasyViewHolder viewHolder, WorkCircleRecommentEntity.ListBean list) {
			titleTv = viewHolder.getTextView(R.id.titleTv);
			imgLay = viewHolder.getLinearLayout(R.id.imgLay);
			imgView1 = viewHolder.getImageView(R.id.imgView1);
			imgView2 = viewHolder.getImageView(R.id.imgView2);
			imgView3 = viewHolder.getImageView(R.id.imgView3);
			tv_tag = viewHolder.getTextView(R.id.tv_tag);
			tv_see = viewHolder.getTextView(R.id.tv_see);
			tv_like = viewHolder.getTextView(R.id.tv_like);
			tv_commont = viewHolder.getTextView(R.id.tv_commont);
			imgView = viewHolder.getImageView(R.id.imgView);


			tv_tag.setText(list.getChannel_name());
			tv_tag.setVisibility(View.VISIBLE);
			int tagLength = list.getChannel_name().length();
			String tagStr = " ";
			for (int i = 0; i < tagLength; i++) {
				tagStr = tagStr + ("    ");
			}
			titleTv.setText(tagStr+list.getTitle());
			tv_see.setText(list.getRead_count()+"");
			tv_like.setText(list.getLike_count()+"赞");
			tv_commont.setText(list.getComment_count()+"评论");
//            list_type	列表类型  0：纯文本 1：单图  3：三张图
			switch (list.getList_type()) {
				case 0:
					imgLay.setVisibility(View.GONE);
					imgView.setVisibility(View.GONE);
					break;
				case 1:
					imgLay.setVisibility(View.GONE);
					imgView.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
							+ list.getAttachment().get(0).getFile_name(), imgView, options);
					break;
				case 3:
					imgLay.setVisibility(View.VISIBLE);
					imgView.setVisibility(View.GONE);

					ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
							+ list.getAttachment().get(0).getFile_name(), imgView1, options);

					ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
							+ list.getAttachment().get(1).getFile_name(), imgView2, options);

					ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
							+ list.getAttachment().get(2).getFile_name(), imgView3, options);
					break;
				default:
					break;

			}

		}
	}
}
