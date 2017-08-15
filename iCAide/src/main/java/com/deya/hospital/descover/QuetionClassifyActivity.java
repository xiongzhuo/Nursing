package com.deya.hospital.descover;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.baseui.BaseRefreshListViewActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.HotVo;
import com.deya.hospital.vo.QuestionVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuetionClassifyActivity extends BaseRefreshListViewActivity {
	protected static final int SEARCH_SUCCESS = 0x908;
	protected static final int SEARCH_FAIL = 0x098;
	SearchListAapter adapter;
	String type="";
	HotVo hv;
	private List<QuestionVo> searchList=new ArrayList<QuestionVo>();
	private Gson gson=new Gson();
	LayoutInflater inflater;
	LinearLayout empertyLay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater=LayoutInflater.from(mcontext);
		initView();
	}

	@Override
	public void onListViewPullUp() {
		startSearch();

	}

	@Override
	public void onListViewPullDown() {
		page = 1;
		isFirst = true;
		totalPage = 0;
		startSearch();
	}

	public void initView() {

		page=1;
		hv=(HotVo) getIntent().getSerializableExtra("data");
		type=hv.getId();
		topView.init(this);
		topView.setTitle(hv.getName());
		adapter=new SearchListAapter();
		listview.setAdapter(adapter);
		startSearch();
		
	}

	@Override
	protected void setDataResult(JSONObject jsonObject) {
		Log.i("search", jsonObject.toString());

		if (jsonObject.has("list")) {
			JSONArray jarr = jsonObject.optJSONArray("list");
			List<QuestionVo> list = gson.fromJson(jarr.toString(),
					new TypeToken<List<QuestionVo>>() {
					}.getType());

			if (isFirst) {
				searchList.clear();
				isFirst = false;
			}
			totalPage = jsonObject.optInt("pageTotal", 0);
			if (totalPage > page) {
				listview.setMode(PullToRefreshBase.Mode.BOTH);
			} else {
				listview.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
			}

			page++;
			searchList.addAll(list);
			adapter.notifyDataSetChanged();
			if(searchList.size()<=0){
				listview.setVisibility(View.GONE);
			}else{
				listview.setVisibility(View.VISIBLE);
			}
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onItemclick(int position) {
		Intent it = new Intent(mcontext, QuetionDetailActivity.class);
		it.putExtra("data", searchList.get(position));
		startActivity(it);
	}

	protected void startSearch() {
		JSONObject job = new JSONObject();
		try {

			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("keyword", "");
			job.put("pageIndex", page + "");
			job.put("q_type", type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler,
				this, GET_DATA_SUCESS, GET_DATA_FAIL, job,
				"questions/latestList");

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
						R.layout.question_search_item, null);
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
			return convertView;
		}

	}
	
	class ViewHolder {
		TextView title;
		TextView answerNum;
		RelativeLayout textLay;

	}
}
