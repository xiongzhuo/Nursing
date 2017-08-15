package com.deya.hospital.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseTableFragment;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.dypdf.PdfPreviewActivity;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.ArticalVo;
import com.deya.hospital.workcircle.WebViewDtail;
import com.deya.hospital.workcircle.WebViewPDF;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyCircleCollectionFragment extends BaseTableFragment implements
		OnItemClickListener ,PullToRefreshBase.OnRefreshListener2{
	protected static final int GET_DATA_SUCESS = 0x6050;
	protected static final int GET_DATA_FAIL = 06051;
	public PullToRefreshListView listView;
	public CommonTopView topView;
	CollectionAapter adapter;
	List<ArticalVo> articalList = new ArrayList<ArticalVo>();
	private LinearLayout networkView;





	public  void initView() {
		listView = (PullToRefreshListView) rootView.findViewById(R.id.list);
		listView.setOnItemClickListener(this);
		listView.setOnRefreshListener(this);
		TextView empertyView=(TextView) rootView.findViewById(R.id.empertyView);
		adapter = new CollectionAapter(getActivity(), articalList);
		listView.setEmptyView(empertyView);
		listView.setAdapter(adapter);
		listView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
		networkView=(LinearLayout) rootView.findViewById(R.id.networkView);
		checkNetWork();
		initHandler();
		getCollectionData();
	}

	@Override
	public int getLayoutId() {
		return R.layout.circle_collection_activity;
	}

	void checkNetWork(){
		if(NetWorkUtils.isConnect(getActivity())){
			networkView.setVisibility(View.GONE);
		}else{
			networkView.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void onResume() {
		super.onResume();
	}

private void 	initHandler(){
	myHandler = new MyHandler(getActivity()) {

		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
					case GET_DATA_SUCESS:
						if (null != msg && null != msg.obj) {
							try {
								setDataResult(new JSONObject(msg.obj.toString()));
							} catch (JSONException e5) {
								e5.printStackTrace();
							}
						}
						break;
					case GET_DATA_FAIL:
						break;

					default:
						break;
				}
			}
		}
	};
	}
	private MyHandler myHandler;

	public void getCollectionData() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler, getActivity(),
				GET_DATA_SUCESS, GET_DATA_FAIL, job, "workCircle/myCollection");
	}

	Gson gson = new Gson();

	int pageIndex=1;
	protected void setDataResult(JSONObject jsonObject) {
		listView.onRefreshComplete();
		JSONArray jarr = jsonObject.optJSONArray("list");
		List<ArticalVo> list = gson.fromJson(jarr.toString(),
				new TypeToken<List<ArticalVo>>() {
				}.getType());
		if (null != list) {
			articalList.clear();
			articalList.addAll(list);
			adapter.notifyDataSetChanged();
		}
		pageIndex++;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(articalList.get(position-1).getIs_pdf()==1){

			Intent in = new Intent(getActivity(), WebViewPDF.class);
			String url= WebUrl.WEB_PDF+"?id="+articalList.get(position-1).getId()+"&&pdfid="+articalList.get(position-1).getPdf_attach();
			in.putExtra("articleid",articalList.get(position-1).getId());
			in.putExtra("url",url);
			in.setClass(getActivity(), PdfPreviewActivity.class);
			startActivityForResult(in, 0x116);
		}else{
		Intent in = new Intent(getActivity(), WebViewDtail.class);
		in.putExtra("id", articalList.get(position-1).getId());
		startActivityForResult(in, 0x116);
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		pageIndex=1;
		articalList.clear();
		getCollectionData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {

	}
}
