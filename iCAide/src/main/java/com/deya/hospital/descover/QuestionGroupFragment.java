package com.deya.hospital.descover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.TypeAdapter;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.HotVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionGroupFragment extends BaseFragment {
	protected static final int TYPE_FAILE = 0x1238;
	protected static final int TYPE_SUCESS = 0x1239;
	private Tools tools;
	private Gson gson;
	private MyHandler myHandler;
	View view;
	private LayoutInflater inflater;
	TypeAdapter adapter;
	private List<HotVo> list=new ArrayList<HotVo>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstance) {
		tools = new Tools(getActivity(), Constants.AC);
		gson = new Gson();
		initMyHandler();
		this.inflater = inflater;
		if (view == null) {
			view = inflater.inflate(R.layout.im_contacts_department_fragment,
					container, false);
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
		adapter=new TypeAdapter(getActivity(), list);
		ListView listView=(ListView) view.findViewById(R.id.address_contactlist);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(getActivity(), QuetionClassifyActivity.class);
				intent.putExtra("data", (Serializable)list.get(position));
				startActivity(intent);
				
			}
		});
		listView.setAdapter(adapter);
		getTypeList();
	}

	private void initMyHandler() {
		myHandler = new MyHandler(getActivity()) {

			@Override
			public void handleMessage(Message msg) {
				Activity activity = myHandler.mactivity.get();
				if (null != activity) {
					switch (msg.what) {
					case TYPE_SUCESS:
						Log.i("yug", msg.obj.toString());
						if (null != msg && null != msg.obj) {
							try {
								setListRes(new JSONObject(msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						break;
					case TYPE_FAILE:
						break;
					default:
						break;
					}
				}
			}

		};
	}

	protected void setListRes(JSONObject jsonObject) {
		JSONArray jarr = null;
		try {
			jarr = jsonObject.getJSONArray("list");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(jarr==null){
			return;
		}
		List<HotVo> cachelist = gson.fromJson(jarr.toString(),
				new TypeToken<List<HotVo>>() {
				}.getType());

		if(null!=cachelist){
			list.clear();
			list.addAll(cachelist);
			adapter.notifyDataSetChanged();
		}
	}

	/** 获取数据 */
	public void getTypeList() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("watchActivity", job.toString());
		MainBizImpl.getInstance().onCirclModeRequest(myHandler, getActivity(),
				TYPE_SUCESS, TYPE_FAILE, job, "questions/questionTypeList");

	}
}
