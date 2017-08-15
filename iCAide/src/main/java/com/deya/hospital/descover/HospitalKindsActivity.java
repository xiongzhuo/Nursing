package com.deya.hospital.descover;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.KindsAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.KindsVo;
import com.deya.hospital.vo.TypesVo;
import com.google.gson.Gson;

public class HospitalKindsActivity extends BaseActivity {
	protected static final int GET_TYPELIST_SUCESS = 0x20200;
	protected static final int GET_TYPELIST_FAIL = 0x20201;
	ListView listView;
	KindsAdapter kAdapter;
	List<KindsVo> kindslist = new ArrayList<KindsVo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kindslist);
		types=getIntent().getStringExtra("types");
		initView();
		getTypeList();
	}

	private String types;
	private String typeId;
	private TextView searchlTv;
	private void initView() {
		ImageView backImg = (ImageView) this.findViewById(R.id.img_back);
		backImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		searchlTv=(TextView) this.findViewById(R.id.et_search);
		searchlTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it=new Intent(mcontext, SearchActivity.class);
				startActivity(it);
				
			}
		});
		listView = (ListView) this.findViewById(R.id.kindsListView);
		kAdapter = new KindsAdapter(mcontext, kindslist);
		listView.setAdapter(kAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(mcontext, DoucmentListActivity.class);
				it.putExtra("types", types);
				it.putExtra("id", typeId);
				it.putExtra("kinds", kindslist.get(position).getKind());
				startActivity(it);

			}
		});

	}

	private MyHandler myHandler = new MyHandler(this) {
			@Override
			public void handleMessage(Message msg) {
				Activity activity = myHandler.mactivity.get();
				if (null != activity) {
					switch (msg.what) {
					case GET_TYPELIST_SUCESS:
						dismissdialog();
						if (null != msg && null != msg.obj) {
							try {
								setTypeListResult(new JSONObject(msg.obj.toString()));
							} catch (JSONException e5) {
								e5.printStackTrace();
							}
						}
						break;
					case GET_TYPELIST_FAIL:
						dismissdialog();
						ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
						break;

					default:
						break;
					}
				}
			}
		};
	List<TypesVo> typeList=new ArrayList<TypesVo>();
	private Gson gson=new Gson();
	public void getTypeList(){
		showprocessdialog();
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("types", types);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onCirclModeRequest(myHandler,HospitalKindsActivity.this, GET_TYPELIST_SUCESS,
				GET_TYPELIST_FAIL, job,"discover/discoverTypesNew");
	}
	
	protected void setTypeListResult(JSONObject jsonObject) {
		Log.i("1111111111111111", jsonObject.toString());
//		JSONArray jarr=jsonObject.optJSONArray("types");
//		JSONArray jarr2=jsonObject.optJSONArray("kinds");
//		if(null!=jarr){
//		 typeList = gson.fromJson(jarr.toString(),
//				new TypeToken<List<TypesVo>>() {
//				}.getType());
//		 adpter.notifyDataSetChanged();
//		}
//		if(null!=jarr2){
//			kindslist=gson.fromJson(jarr2.toString(),
//					new TypeToken<List<KindsVo>>() {
//					}.getType());
//		}
	}
}
