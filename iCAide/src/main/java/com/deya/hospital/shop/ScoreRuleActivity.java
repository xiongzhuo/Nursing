package com.deya.hospital.shop;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.RuleVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ScoreRuleActivity extends BaseActivity implements OnClickListener{
	protected static final int GET_SUCESS = 0x5008;
	protected static final int GET_FAILE = 0x5009;
	ListView listView;
	LayoutInflater inflater;
	List<RuleVo> scoreList=new ArrayList<RuleVo>();
	ScoreAdapter sAdapter;
	private RelativeLayout rl_back;
	Tools tools;
	private LinearLayout networkView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scorerule);
		inflater=LayoutInflater.from(mcontext);
		tools=new Tools(mcontext, Constants.AC);
		checkNetWork();
		initView();
		RuleRequest();
		
		
		
	}
	public void checkNetWork(){
		networkView=(LinearLayout) this.findViewById(R.id.networkView);
			if(NetWorkUtils.isConnect(mcontext)){
				networkView.setVisibility(View.GONE);
			}else{
				networkView.setVisibility(View.VISIBLE);
			}
			
		}
	private void initView() {
		listView=(ListView) this.findViewById(R.id.listView);
		sAdapter=new ScoreAdapter();
		listView.setAdapter(sAdapter);
		rl_back = (RelativeLayout) this.findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		((TextView)this.findViewById(R.id.title)).setText("获取规则");
		listView = (ListView) this.findViewById(R.id.list);
		
		if(getIntent().getBooleanExtra("notop", false)){
			this.findViewById(R.id.text_top).setVisibility(View.GONE);
			this.findViewById(R.id.view_top).setVisibility(View.GONE);
		}
		
	}

	
	public class ScoreAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return scoreList.size();
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
			convertView=inflater.inflate(R.layout.score_rule_item, null);
			TextView ruleTv=(TextView) convertView.findViewById(R.id.ruleTv);
			ruleTv.setText(scoreList.get(position).getProject());
			TextView scoreNum=(TextView) convertView.findViewById(R.id.scoreNum);
			scoreNum.setText(scoreList.get(position).getIntegral()+"橄榄/"+scoreList.get(position).getRule());
			TextView intruduce=(TextView) convertView.findViewById(R.id.intruduce);
			intruduce.setText(scoreList.get(position).getTitle()+"");
			return convertView;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;

		default:
			break;
		}
		
		
	}
	
	
	private void RuleRequest() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,ScoreRuleActivity.this, GET_SUCESS,
				GET_FAILE, job,"goods/integralRulesList");
	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case GET_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							setListResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case GET_FAILE:
					networkView.setVisibility(View.VISIBLE);
					ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
					break;

				default:
					break;
				}
			}
		}
		};
		
		Gson gson=new Gson();
	protected void setListResult(JSONObject jsonObject) {
		Log.i("1111111", jsonObject.toString());
		if(jsonObject.optString("result_id").equals("0")){
			JSONArray jarr=jsonObject.optJSONArray("goods");
			scoreList = gson.fromJson(jarr.toString(),
					new TypeToken<List<RuleVo>>() {
					}.getType());
			sAdapter.notifyDataSetChanged();
		}
		
	}
	
}

