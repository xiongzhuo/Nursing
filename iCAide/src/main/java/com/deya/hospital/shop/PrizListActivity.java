package com.deya.hospital.shop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.PrizeItemAdapter;
import com.deya.hospital.adapter.PrizeItemAdapter.ItemListener;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.Prize;
import com.deya.hospital.widget.popu.TipsDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 已兑换奖品列表
 * 
 * @author yung
 * @date 创建时间：2016年1月15日 下午2:54:47
 * @version 1.0
 */
public class PrizListActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout rl_back;
	private ImageView img_back;

	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private TextView backText, submit;

	private PrizeItemAdapter adapter;
	private List<Prize> list = new ArrayList<Prize>();
	private Tools tools;
	private LinearLayout networkView;
	private TextView empertyText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prize);
		tools = new Tools(mcontext, Constants.AC);
		checkNetWork();
		initView();
	}

	public void checkNetWork() {
		networkView = (LinearLayout) this.findViewById(R.id.networkView);
		empertyText = (TextView) this.findViewById(R.id.empertyText);
		if (NetWorkUtils.isConnect(mcontext)) {
			networkView.setVisibility(View.GONE);
		} else {
			networkView.setVisibility(View.VISIBLE);
		}

	}

	TipsDialog dialog;

	private void initView() {
		dialog = new TipsDialog(mcontext, "");
		rl_back = (RelativeLayout) this.findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		img_back = (ImageView) this.findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		backText = (TextView) this.findViewById(R.id.backText);
		backText.setOnClickListener(this);
		((TextView) this.findViewById(R.id.title)).setText(res
				.getString(R.string.credit_my));
		this.findViewById(R.id.lay_tel).setOnClickListener(this);
		pullToRefreshListView = (PullToRefreshListView) this
				.findViewById(R.id.list);
		pullToRefreshListView.setMode(Mode.PULL_FROM_END);
		listView = pullToRefreshListView.getRefreshableView();
		initPull();

		setAdapter();
		getData();
	}

	private Prize fx_prize;

	private void setAdapter() {
		adapter = new PrizeItemAdapter(mcontext, list);
		listView.setAdapter(adapter);
		adapter.SetOnItemListener(new ItemListener() {

			@Override
			public void OnButonClick(Prize prize) {
				// TODO Auto-generated method stub
				fx_prize = prize;
				fx();

			}
		});
	}

	int page = 1;

	/**
	 * 刷新数据
	 */
	private void initPull() {
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							com.handmark.pulltorefresh.library.PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						getData();
					}
				});
	}

	private static final int SUCCESS = 0x1055;
	private static final int FAIL = 0x1056;

	/**
	 * 分享
	 */
	private void fx() {
		showShareDialog(getString(R.string.app_name),
				"点击下载并安装,你也有机会兑换商品哦！", "http://studio.gkgzj.com");
	}

	/**
	 * 获取数据
	 */
	private void getData() {

		showprocessdialog();
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("device_type", "2");
			job.put("pageIndex", page);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				PrizListActivity.this, SUCCESS, FAIL, job, "goods/myGoodsList");
	}

	/**
	 * 刷新数据，并显示
	 * 
	 * @param msg
	 */
	private void refreshData(String msg) {
		DebugUtil.debug("getgoodslist", msg);
		if (page == 1) {
			list.clear();
		}
		try {
			JSONObject jsonObject = new JSONObject(msg);
			String r = jsonObject.getString("result_id");
			if (null != r && r.equals("0")) {
				// String goods=jsonObject.getString("goods");
				JSONArray jsonArray = jsonObject.getJSONArray("goods");
				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						Prize goods = new Prize();
						JSONObject itemJson = jsonArray.getJSONObject(i);
						goods.setId(itemJson.getInt("id"));
						goods.setConvert_status(itemJson
								.getInt("convert_status"));
						goods.setConvert_time(itemJson
								.getString("convert_time"));
						goods.setGoods_id(itemJson.getInt("goods_id"));
						goods.setGoodsname(itemJson.getString("goodsname"));
						goods.setUid(itemJson.getInt("uid"));
						goods.setIntegral(itemJson.getInt("integral"));
						goods.setPicture(itemJson.getString("picture"));
						list.add(goods);
					}
					page++;
				}
				if (list.size() <= 0) {
					networkView.setVisibility(View.VISIBLE);
					empertyText.setText("亲，您还没有对换记录哦！");
				} else {
					networkView.setVisibility(View.GONE);
				}
			}
		} catch (JSONException e5) {
			e5.printStackTrace();
		}
		adapter.notifyDataSetChanged();
	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case SUCCESS:
					dismissdialog();
					networkView.setVisibility(View.GONE);
					if (null != msg && null != msg.obj) {
						refreshData(msg.obj.toString());
					}
					pullToRefreshListView.onRefreshComplete();
					break;
				case FAIL:
					dismissdialog();
					networkView.setVisibility(View.VISIBLE);
					ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
					break;
				case ADD_SUCESS:
					if (null != msg && null != msg.obj) {
						Log.i("1111msg", msg.obj + "");
						try {
							setAddRes(new JSONObject(msg.obj.toString()),
									activity);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				case ADD_FAILE:
					Log.i("1111msg", msg.obj + "");
				default:
					break;
				}
			}
		}
	};

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.img_back:
		case R.id.rl_back:
		case R.id.backText:
			finish();
			break;
		case R.id.lay_tel:
			String tel = res.getString(R.string.credit_tel);
			Intent phoneIntent = new Intent("android.intent.action.CALL",
					Uri.parse("tel:" + tel));
			startActivity(phoneIntent);
			break;
		default:
			break;
		}

	}

	protected static final int ADD_FAILE = 0x60089;
	protected static final int ADD_SUCESS = 0x60090;

	public void getAddScore(String id) {
		Log.i("share_umeng", "111111111111111");
		tools = new Tools(mcontext, Constants.AC);
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("aid", id);
			Log.i("1111", job.toString());
			String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
					.parseStrToMd5L32(job.toString())
					+ "goods/actionGetIntegral");
			json.put("token", jobStr);
			json.put("msg_content", job.toString());
			Log.i("11111111", json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				PrizListActivity.this, ADD_SUCESS, ADD_FAILE, job,
				"goods/actionGetIntegral");
	}

	protected void setAddRes(JSONObject jsonObject, Activity activity) {
		Log.i("share_umeng", "返回次数");
		Log.i("11111111", jsonObject.toString());
		if (jsonObject.optString("result_id").equals("0")) {
			int score = jsonObject.optInt("integral");
			String str = tools.getValue(Constants.INTEGRAL);
			if (null != str) {
				tools.putValue(Constants.INTEGRAL, Integer.parseInt(str)
						+ score + "");
			} else {
				tools.putValue(Constants.INTEGRAL, score + "");
			}
			if (score > 0) {
				if (null != activity) {
					dialog.setScoreText(score + "");
					dialog.show();
				}
			}

		}
	}
	
	@Override
	protected void onDestroy() {
		if(null!=dialog){
			dialog.dismiss();
		}
		super.onDestroy();
	}
}
