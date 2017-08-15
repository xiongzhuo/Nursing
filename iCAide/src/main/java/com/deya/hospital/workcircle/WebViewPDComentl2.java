package com.deya.hospital.workcircle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.CommentVo;
import com.deya.hospital.widget.popu.PopCircleCommement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WebViewPDComentl2 extends BaseActivity implements
		View.OnClickListener {

	private TextView write_compment;
	private LinearLayout inputLay;
	CommentListAdapter adapter;
	private LinearLayout editorLay;
	private TextView submiText;
	String articalId = "";
	TextView zan_num, commentNumTv;
	ImageView zanImg, shoucangImg;

	List<CommentVo> commetList = new ArrayList<CommentVo>();
	private EditText commentEdt;
	PullToRefreshListView commentListView;
	View headWebView;
	int headWebHight;
	boolean isFirst = true;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ducument_comm_detail);

		editorLay = (LinearLayout) this.findViewById(R.id.editorLay);
		write_compment = (TextView) this.findViewById(R.id.write_compment);
		this.findViewById(R.id.ll_com).setOnClickListener(this);
		write_compment.setOnClickListener(this);
//		inputLay = (LinearLayout) this.findViewById(R.id.inputLay);
//		inputLay.setOnClickListener(this);
	//	findViewById(R.id.framBg).setOnClickListener(this);

//		submiText = (TextView) this.findViewById(R.id.submiText);
//		submiText.setOnClickListener(this);
		commentListView = (PullToRefreshListView) this
				.findViewById(R.id.commentListView);
		commentEdt = (EditText) this.findViewById(R.id.commentEdt);
		zan_num = (TextView) this.findViewById(R.id.zan_num);
		zan_num.setOnClickListener(this);
		zanImg = (ImageView) this.findViewById(R.id.zanImg);
		zanImg.setOnClickListener(this);
		shoucangImg = (ImageView) this.findViewById(R.id.shoucangImg);
		shoucangImg.setOnClickListener(this);
		commentNumTv = (TextView) this.findViewById(R.id.commentNumTv);
		findViewById(R.id.shareLay).setOnClickListener(this);
		// commentListView.getRefreshableView().addHeaderView(headWebView);
		adapter = new CommentListAdapter(commetList, mcontext);
		commentListView.setAdapter(adapter);


		try {
			if (getIntent().hasExtra("id") && !AbStrUtil.isEmpty(getIntent().getStringExtra("id"))) {
                articalId = getIntent().getStringExtra("id");
                url = WebUrl.WEB_ARTICAL_DETAIL + "?id=" + articalId;
            } else if (getIntent().hasExtra("url") && !AbStrUtil.isEmpty(getIntent().getStringExtra("url"))) {
                url = getIntent().getStringExtra("url");
                articalId = url.substring(url.indexOf("id=") + 3, url.indexOf("&"));
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		InitTopView();

		if (getIntent().hasExtra("10086")) {
			editorLay.setVisibility(View.GONE);
		}
		getCommentList();
	}

	String title = "";
	String url = "";

	private CommonTopView topView;

	private void InitTopView() {
		title = getIntent().getStringExtra("title");
		topView = (CommonTopView) this.findViewById(R.id.topView);
		topView.init(this);
		topView.setTitle("评论");
	}
	PopCircleCommement dialog;

	public void showCommentEditorPop() {
		dialog = new PopCircleCommement(mcontext, this, articalId,commentListView,
				new PopCircleCommement.OnPopuClick() {

					@Override
					public void enter(String text) {
						doComment(text);

					}

					@Override
					public void cancel() {
						// TODO Auto-generated method stub

					}
				});

	}
	
	/**
	 * 评论接口
	 */
	public void doComment(String text) {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("article_id", articalId);
			job.put("content", text);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		MainBizImpl.getInstance().onCirclModeRequest(myHandler, this,
				COMMENT_SUCESS, COMMENT_FAIL, job, "workCircle/submitComment");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_com:

			showCommentEditorPop();
			break;
		case R.id.inputLay:
			write_compment.setVisibility(View.VISIBLE);
			break;
//		case R.id.submiText:
//			if (commentEdt.getText().toString().trim().length() <= 0) {
//				ToastUtils.showToast(mcontext, "评论内容不能为空");
//				return;
//			}
//
//			editorLay.setVisibility(View.VISIBLE);
//			inputLay.setVisibility(View.GONE);
//			AbViewUtil.colseVirtualKeyboard(this);
//			doComment();
//			break;
		case R.id.framBg:

			editorLay.setVisibility(View.VISIBLE);
			//inputLay.setVisibility(View.GONE);
			AbViewUtil.colseVirtualKeyboard(this);
			break;
		case R.id.zan_num:
			onClickLike();
			break;
		case R.id.zanImg:
			onClickLike();
			break;
		case R.id.shoucangImg:
			onCollection();
			break;
		case R.id.messageLay:
			if (commentListView.getRefreshableView().getChildCount() > 0) {
				commentListView.getRefreshableView().setSelection(2);
			}
			// if(commentListView.getChildCount()>5){
			// commentListView.smoothScrollToPosition(5);
			// }else{
			// commentListView.smoothScrollToPosition(commentListView.getChildCount()-1);
			// }
			break;
		case R.id.shareLay:
			showShare();
			break;

		default:
			break;
		}

	}

	public static final int GET_INFO_SUCESS = 0x600045;
	public static final int GET_INFO_FAIL = 0x600046;
	public static final int COMMENT_SUCESS = 0x600039;
	public static final int COMMENT_FAIL = 0x600040;
	public static final int GET_COMMENTLIST_SUCESS = 0x600041;
	public static final int GET_COMMENTLIST_FAIL = 0x600042;
	public static final int CLICK_LIKE_SUCESS = 0x600043;
	public static final int CLICK_LIKE_FAIL = 0x600044;
	public static final int CLICK_COLLECT_SUCESS = 0x6000437;
	public static final int CLICK_COLLECT_FAIL = 0x600048;

	/**
	 * 点击收藏
	 */
	private void onCollection() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("article_id", articalId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler, this,
				CLICK_COLLECT_SUCESS, CLICK_COLLECT_FAIL, job,
				"workCircle/setCollect");
	}

	/**
	 * 获取文章信息
	 */
	public void getArticleInfo() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("article_id", articalId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler, this,
				GET_INFO_SUCESS, GET_INFO_FAIL, job,
				"workCircle/getArticleInfo");
	}

	/**
	 * 评论接口
	 */
	public void doComment() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("article_id", articalId);
			job.put("content", commentEdt.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		MainBizImpl.getInstance().onCirclModeRequest(myHandler, this,
				COMMENT_SUCESS, COMMENT_FAIL, job, "workCircle/submitComment");
	}

	/**
	 * 获取评论列表接口
	 */
	public void getCommentList() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("article_id", articalId);
			job.put("pageIndex", page);
			//job.put("content", commentEdt.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		MainBizImpl.getInstance().onCirclModeRequest(myHandler, this,
				GET_COMMENTLIST_SUCESS, GET_COMMENTLIST_FAIL, job,
				"workCircle/commentList");
	}

	// 点赞接口
	public void onClickLike() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("article_id", articalId);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		MainBizImpl.getInstance()
				.onCirclModeRequest(myHandler, this, CLICK_LIKE_SUCESS,
						CLICK_LIKE_FAIL, job, "workCircle/clickLike");

	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case COMMENT_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							setCommentResult(new JSONObject(msg.obj.toString()));
							SharedPreferencesUtil.clearCacheById(mcontext, articalId,"commetChache");
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case COMMENT_FAIL:
					ToastUtils.showToast(WebViewPDComentl2.this, "亲，您的网络不顺畅哦！");
					break;
				case GET_COMMENTLIST_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							getCommentListResult(new JSONObject(
									msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case GET_COMMENTLIST_FAIL:
					commentListView.onRefreshComplete();
					break;
				case CLICK_LIKE_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							setClickResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case CLICK_LIKE_FAIL:
					break;
				case GET_INFO_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							setInfoResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case GET_INFO_FAIL:
					break;
				case CLICK_COLLECT_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							setcollectResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case CLICK_COLLECT_FAIL:
					break;
				default:
					break;
				}
			}
		}
	};

	protected void setCommentResult(JSONObject jsonObject) {
		ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
//		if (jsonObject.has("info")) {
//			CommentVo cv = gson.fromJson(jsonObject.optString("info"),
//					CommentVo.class);
//			if (null != cv) {
//				commetList.add(0, cv);
//				adapter.notifyDataSetChanged();
//				int num = Integer.parseInt(commentNumTv.getText().toString()) + 1;
//				commentNumTv.setText(num + "");
//			}
//		}
		commentEdt.setText("");
		page = 1;
		commetList.clear();
		getCommentList();
	}

	protected void setcollectResult(JSONObject jsonObject) {
if (jsonObject.optString("result_id").equals("0")) {
			
			int isCollect = 0;
			if(jsonObject.has("is_collection")){
				isCollect= jsonObject.optInt("is_collection");
			}
			
			shoucangImg
					.setImageResource(isCollect == 1 ? R.drawable.shoucang_select
							: R.drawable.shouchang_normal);
		}
		ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
	}

	protected void setInfoResult(JSONObject result) {

		if (result.optString("result_id").equals("0")) {
			JSONObject jsonObject = result.optJSONObject("info");
			if(null==jsonObject){
				return;
			}
			int like;
			if (AbStrUtil.isEmpty(jsonObject.optString("like_count"))) {
				like = 0;
			} else {
				like = jsonObject.optInt("like_count");
			}
			zan_num.setText(like + "");
			int isCollect = jsonObject.optInt("is_collection");
			int isLike = jsonObject.optInt("is_like");
			zanImg.setImageResource(isLike == 1 ? R.drawable.zan_select
					: R.drawable.zan_normal);
			shoucangImg
					.setImageResource(isCollect == 1 ? R.drawable.shoucang_select
							: R.drawable.shouchang_normal);
			int comment_num = jsonObject.optInt("comment_count");
			commentNumTv.setText("" + comment_num);
		}

	}

	Gson gson = new Gson();

	protected void getCommentListResult(JSONObject jsonObject) {
		if (jsonObject.optString("result_id").equals("0")) {
			onRefrshSucess(jsonObject);
		} else {
			commentListView.onRefreshComplete();
		}

	}

	int totalpage = 0;
	int page = 1;

	public void onRefrshSucess(JSONObject jsonObject) {
		commentListView.onRefreshComplete();
		JSONArray jarr = jsonObject.optJSONArray("list");
		List<CommentVo> list = gson.fromJson(jarr.toString(),
				new TypeToken<List<CommentVo>>() {
				}.getType());
		if (list == null) {
			list = new ArrayList<CommentVo>();
		}
		if (jsonObject.has("pageTotal")) {
			totalpage = jsonObject.optInt("pageTotal");
		}
		if (page >= totalpage) {
			commentListView.setMode(Mode.DISABLED);
		} else {
			commentListView.setMode(Mode.PULL_UP_TO_REFRESH);
		}
		commetList.addAll(list);
		adapter.notifyDataSetChanged();
		commentListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page++;
				getCommentList();

			}
		});
	}

	private void setClickResult(JSONObject jsonObject) {
		ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
		if (jsonObject.has("is_like")) {
			int num = Integer.parseInt(zan_num.getText().toString());
			if (jsonObject.optInt("is_like") == 1) {
				num = num + 1;
				zanImg.setImageResource(R.drawable.zan_select);
			} else {
				num = num - 1;
				zanImg.setImageResource(R.drawable.zan_normal);
			}
			zan_num.setText(num + "");
		}
	}

	private void showShare() {
		SHARE_MEDIA[] shareMedia = { SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.WEIXIN_CIRCLE };
		// initCustomPlatforms(shareMedia);
		// showShareContent(shareMedia, shareTitle, shareContent, targetUrl);
		showShareDialog("工作圈", "点击查看文章详情", url);
	}
}
