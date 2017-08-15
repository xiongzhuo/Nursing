package com.deya.hospital.form.xy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.form.FormSettingActivity;
import com.deya.hospital.form.xy.XyPreviewAdapter.previewAdapterInter;
import com.deya.hospital.supervisor.SupervisionDetailActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormDetailVo;
import com.deya.hospital.workspace.supervisorquestion.SupervisorQesDetitalActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class XyPreViewActivityDetial extends BaseActivity implements
		OnClickListener {
	ListView listView;
	XyPreviewAdapter adapter;
	List<FormDetailListVo> list = new ArrayList<FormDetailListVo>();
	private RelativeLayout rlBack;
	private String id;
	private int type;
	LinearLayout sumbmitlay;
	TextView totalScoreTv, formTitleTv;
	int totalScore = 0;
	String title = "";
	TextView mainRemarkTv;
	TaskVo data;
	TextView departTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prviewlist);
		data = (TaskVo) getIntent().getSerializableExtra("data");
		id = data.getTask_id() + "";
		type = Integer.parseInt(data.getGrid_type());
		intTopView();
		initView();
		getFormDitail();
	}

	private void initView() {
		sumbmitlay = (LinearLayout) this.findViewById(R.id.sumbmitlay);
		sumbmitlay.setVisibility(View.GONE);
		departTv = (TextView) this.findViewById(R.id.departTv);
		departTv.setText(data.getDepartmentName());
		findViewById(R.id.img).setVisibility(View.GONE);
		mainRemarkTv=(TextView) this.findViewById(R.id.mainRemarkTv);
		mainRemarkTv.setOnClickListener(this);
		formTitleTv = (TextView) this.findViewById(R.id.formTitleTv);
		formTitleTv.setText(data.getDepartmentName() + "的临床考核");

		totalScoreTv = (TextView) this.findViewById(R.id.totalScoreTv);
		if (type == 1) {
			totalScoreTv.setVisibility(View.VISIBLE);
			totalScoreTv.setText("总分："
					+ AbStrUtil.reMoveUnUseNumber(data.getScore()) + "分");
		} else {
			totalScoreTv.setVisibility(View.GONE);
		}

		listView = (ListView) this.findViewById(R.id.ListView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Intent it = new Intent(mcontext, FormDetailActivity.class);
				// it.putExtra("data", (Serializable) list);
				// it.putExtra("position", position);
				//
				// startActivity(it);

			}
		});
		adapter = new XyPreviewAdapter(mcontext, true, list,
				new previewAdapterInter() {

					@Override
					public void onputData(int parentposition, int position) {
						putdata(parentposition, position);

					}

					@Override
					public void onRemark(int parentPosition, int position) {
						toRemark(parentPosition, position);

					}
				});
		listView.setAdapter(adapter);

	}

	private void intTopView() {
		rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
		rlBack.setOnClickListener(this);
		TextView titleTv = (TextView) this.findViewById(R.id.title);
		titleTv.setText(data.getName());
		// /saldhnasldhaskldhkoashdla
		TextView submit = (TextView) this.findViewById(R.id.submit);
		submit.setVisibility(View.GONE);
		// submit.setText("编辑");
		// submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;
		case R.id.submit:
			Intent it = new Intent(mcontext, FormSettingActivity.class);
			it.putExtra("list", (Serializable) list);
			it.putExtra("position", 0);
			it.putExtra("data", data);
			it.putExtra("title", title);
			it.putExtra("updated", true);
			it.putExtra("vo", list.get(0));
			it.putExtra("type", type);
			data.setUpdatedTask(true);
			startActivity(it);
			break;
		case R.id.mainRemarkTv:
			Intent toWeb = new Intent(mcontext,
					SupervisorQesDetitalActivity.class);
			toWeb.putExtra("id", data.getMain_remark_id());
			startActivityForResult(toWeb, 0x136);
			break;
		default:
			break;
		}

	}

	private void getFormDitail() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("template_type", "2");
			job.put("task_id", id);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("111111111111", job.toString());
		MainBizImpl.getInstance().onComomRequest(myHandler,
				XyPreViewActivityDetial.this, GET_SUCESS, GET_FAILE, job,
				"grid/queryGridTaskDetailByTaskId");
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
							setResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case GET_FAILE:
					ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
					break;

				default:
					break;
				}
			}
		}

	};
	private Gson gson = new Gson();
	private static final int GET_SUCESS = 0x70010;
	private static final int GET_FAILE = 0x70011;

	protected void setResult(JSONObject jsonObject) {
		Log.i("1111111111111", jsonObject + "");
		if (AbStrUtil.isEmpty(jsonObject.optString("main_remark_id"))||jsonObject.optInt("main_remark_id",0)==0) {
			mainRemarkTv.setVisibility(View.GONE);
		}else{
			data.setMain_remark_id(jsonObject.optInt("main_remark_id",0)+"");
		}
		//data.setMain_remark(jsonObject.optString("main_remark"));
		data.setDepartment(jsonObject.optString("department"));
		departTv.setText(jsonObject.optString("departmentName"));
		JSONArray jarr = jsonObject.optJSONArray("items");
		if (null != jarr) {
			list = gson.fromJson(jarr.toString(),
					new TypeToken<List<FormDetailListVo>>() {
					}.getType());

			for (FormDetailListVo fdlv : list) {
				for (FormDetailVo fdv : fdlv.getSub_items()) {
					fdv.setScores(fdv.getBase_score());
					for(int k=0;k<fdv.getSub_rule().size();k++){
						if(fdv.getSub_rule().get(k).getIs_choosed()==1){
							fdv.setChooseItem(k);
						}
					}
				}
			}
			adapter.setdata(list);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0x136 && null != data) {
			if (resultCode == 0x136 && null != data) {
				TaskVo remarkVo = gson.fromJson(list.get(markParentPosition)
						.getSub_items().get(markitemPosition).getRemark(),
						TaskVo.class);
				String improveStr = data.getStringExtra("str");
				remarkVo.setImprove_suggest(improveStr);
				list.get(markParentPosition).getSub_items()
						.get(markitemPosition).setRemark(gson.toJson(remarkVo));
			}
		} else if (resultCode == 0x171 && null != data) {
			// sumbmitlay.setVisibility(View.VISIBLE);
			list.clear();
			list.addAll((List<FormDetailListVo>) data
					.getSerializableExtra("data"));
			adapter.notifyDataSetChanged();

		}else if(resultCode == 0x136){
			finish();
		}
		adapter.setdata(list);
	}

	public void putdata(int parentposition, int position) {
		Intent it = new Intent(mcontext, XiangYaFormSettingActivity.class);
		it.putExtra("list", (Serializable) list);
		it.putExtra("position", position);
		it.putExtra("parentposition", parentposition);
		it.putExtra("data", data);
		it.putExtra("updated", true);
		it.putExtra("title", title);
		it.putExtra("vo", list.get(parentposition));
		startActivityForResult(it, 0x117);
	}

	private int markParentPosition;
	private int markitemPosition;

	public void toRemark(int position, int itemPosition) {
		this.markParentPosition = position;
		this.markitemPosition = itemPosition;
		Intent it = new Intent(mcontext, SupervisionDetailActivity.class);
		FormDetailListVo flv = list.get(position);
		TaskVo remarkVo;
		String remarkStr = list.get(position).getSub_items().get(itemPosition)
				.getRemark();
		if (!AbStrUtil.isEmpty(remarkStr)) {
			remarkVo = gson.fromJson(remarkStr, TaskVo.class);
			remarkVo.setTmp_id(flv.getSub_items().get(itemPosition).getId());
			remarkVo.setDepartment(data.getDepartment());
			remarkVo.setDepartmentName(data.getDepartmentName());
			remarkVo.setType("2");

		} else {
			remarkVo = data;
		}
		it.putExtra("data", remarkVo);
		it.putExtra("isRemark", "1");
		it.putExtra("remarkId",
				list.get(position).getSub_items().get(itemPosition).getId());
		startActivityForResult(it, 0x117);
	}

}
