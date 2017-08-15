package com.deya.hospital.supervisor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.UnTimeAdapter;
import com.deya.hospital.adapter.UnTimeAdapter.UnTimeAdapterListener;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.task.uploader.BaseUploader;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ReportWebViewDemo;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.vo.dbdata.planListDb;
import com.deya.hospital.vo.dbdata.subTaskVo;
import com.deya.hospital.widget.view.UnTimeItem2.UnTimeItemTextChangListener;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.handwash.HandWashTasksActivity;
import com.deya.hospital.workspace.priviewbase.FormCommitSucTipsActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务提交
 *
 * @author yung
 * @date 创建时间：2016年1月15日 上午11:56:11
 * @version 1.0
 */
public class UnTime2Activity extends BaseActivity implements OnClickListener {
	private TextView text_depart;
	private TextView text_time_res, text_dependence_res, text_correct_res,
			text_rate_res;
	private ListView listivew_ob, listivew_disinfection, listivew_washhands,
			listivew_glove, listivew_nothing;

	private LinearLayout lay_feed_body, lay_teach_times, lay_records,
			lay_disinfection, lay_washhands, lay_glove, lay_nothing,
			lay_feed_remark;
	private TextView text_feed_body, text_teach_time, text_records,
			text_record_remark;

	private Button btn_commit;

	private UnTimeAdapter adapter;
	// private RelativeLayout main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_untime2);
		initViews();

		findDbJobList();

		initData();

		initFeedData();

		// setViewData();

		// setRecordAdapter();

		lay_glove.setVisibility(View.GONE);
		lay_washhands.setVisibility(View.GONE);
		lay_nothing.setVisibility(View.GONE);
		lay_disinfection.setVisibility(View.GONE);
	}

	private RelativeLayout framLayout;
	private Button btn_commitfram;
	private CommonTopView topView;

	/**
	 * 初始化控件
	 */
	@SuppressWarnings("deprecation")
	private void initViews() {
		// main = (RelativeLayout) findViewById(R.id.main);
		topView=(CommonTopView) findViewById(R.id.topView);
		topView.onbackClick(this, new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackSaveData();
			}
		});
		topView.showRightView(View.GONE);
		btn_commit = (Button) findViewById(R.id.btn_commit);
		btn_commitfram = (Button) findViewById(R.id.btn_commitfram);
		text_depart = (TextView) findViewById(R.id.text_depart);
		text_depart.setOnClickListener(this);
		btn_commit.setOnClickListener(this);
		btn_commitfram.setOnClickListener(this);

		text_time_res = (TextView) findViewById(R.id.text_time_res);
		text_dependence_res = (TextView) findViewById(R.id.text_dependence_res);
		text_correct_res = (TextView) findViewById(R.id.text_correct_res);
		text_rate_res = (TextView) findViewById(R.id.text_rate_res);

		listivew_ob = (ListView) findViewById(R.id.listivew_ob);
		listivew_disinfection = (ListView) findViewById(R.id.listivew_disinfection);
		listivew_washhands = (ListView) findViewById(R.id.listivew_washhands);
		listivew_glove = (ListView) findViewById(R.id.listivew_glove);
		listivew_nothing = (ListView) findViewById(R.id.listivew_nothing);

		text_feed_body = (TextView) findViewById(R.id.text_feed_body);
		text_teach_time = (TextView) findViewById(R.id.text_teach_time);
		text_records = (TextView) findViewById(R.id.text_records);
		text_record_remark = (TextView) findViewById(R.id.text_record_remark);

		lay_feed_body = (LinearLayout) findViewById(R.id.lay_feed_body);
		lay_teach_times = (LinearLayout) findViewById(R.id.lay_teach_times);
		lay_records = (LinearLayout) findViewById(R.id.lay_records);
		lay_disinfection = (LinearLayout) findViewById(R.id.lay_disinfection);
		lay_washhands = (LinearLayout) findViewById(R.id.lay_washhands);
		lay_glove = (LinearLayout) findViewById(R.id.lay_glove);
		lay_nothing = (LinearLayout) findViewById(R.id.lay_nothing);
		lay_feed_remark = (LinearLayout) findViewById(R.id.lay_feed_remark);
		// initRecordAdpter();

		lay_feed_remark.setVisibility(View.GONE);
		lay_feed_body.setVisibility(View.GONE);
		lay_teach_times.setVisibility(View.GONE);
		lay_records.setVisibility(View.GONE);
	}

	List<planListDb> dataList = new ArrayList<planListDb>();

	/**
	 * 卫生消毒操作的不规范记录
	 */
	ArrayList<HashMap<String, String>> recordDisinfectionList = new ArrayList<HashMap<String, String>>();
	/**
	 * 洗手操作的不规范记录
	 */
	ArrayList<HashMap<String, String>> recordWashHandsList = new ArrayList<HashMap<String, String>>();
	/**
	 * 戴手套操作的不规范记录
	 */
	ArrayList<HashMap<String, String>> recordGloveList = new ArrayList<HashMap<String, String>>();
	/**
	 * 未采取措施的不规范记录
	 */
	ArrayList<HashMap<String, String>> recordNothingList = new ArrayList<HashMap<String, String>>();

	/**
	 * 初始化数据
	 */
	private void initData() {
		setTitle(getIntent().getStringExtra("timetype"));

		tv = (TaskVo) getIntent().getSerializableExtra("data");
		if(null==tv){
			tv=new TaskVo();
		}
		//topView.showRightView(View.GONE);
		topView.setRigtext("现场反馈");
		String str = tv.getFiveTasks();
		Log.i("UnTime", "tv.getFiveTasks()>>" + str);
		String colType = getIntent().getStringExtra("colType");
		colType = tv.getTotalNum();

		String depe_rate_str = tv.getYc_rate();// 依从性
		String curr_rate_str = tv.getValid_rate();// 正确率
		String yc = tv.getYc();// 正确数

		if (null == str) {
			str = "";
		}

		JSONArray jarr = null;
		try {
			jarr = new JSONArray(str);
			DebugUtil.debug("untime_list",
					"jarr.toString()>>" + jarr.toString());
			List<planListDb> list1 = gson.fromJson(jarr.toString(),
					new TypeToken<List<planListDb>>() {
					}.getType());
			dataList.addAll(list1);
			// for (planListDb pdb : list1) {
			// if (pdb.getSubTasks().size() > 0) {
			//
			//
			// }
			// }

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setAdapter();

		text_time_res.setText(colType);// 时机
		text_dependence_res.setText(depe_rate_str + "%");// 依从性
		text_correct_res.setText(yc + "");// 正确数
		text_rate_res.setText(curr_rate_str + "%");// 正确率

		str_remark = tv.getRemark();

		if (null != tv.getTraining_recycle()) {
			String _timeIndex = tv.getTraining_recycle();
			if(AbStrUtil.isEmpty(_timeIndex)){
				_timeIndex="-1";
			}
			if (TextUtils.isDigitsOnly(_timeIndex))
				timeIndex = Integer.parseInt(_timeIndex);
		}

		text_depart.setText(tv.getDepartmentName());
		str_name = tv.getFeedback_obj();

		String equip_examine = tv.getEquip_examine();
		if (null != equip_examine && equip_examine.length() > 0) {
			String[] eq_s = equip_examine.split(",");
			for (int i = 0; i < eq_s.length; i++) {
				if (TextUtils.isDigitsOnly(eq_s[i])) {
					reasonIds.add(Integer.parseInt(eq_s[i]));
				}
			}
		}

		// reason_strs = mcontext.getResources().getStringArray(
		// R.array.facilities_investigation_string);
		// train_times = mcontext.getResources().getStringArray(
		// R.array.train_times_string);

		String json = tools.getValue("equipExamineParams");
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			String r = jsonObject.getString("result_id");
			if (null != r && r.equals("0")) {

				JSONArray jsonArray1 = jsonObject
						.getJSONArray("equipExamineParams");
				HashMap<String, Object> map = null;
				for (int i = 0; i < jsonArray1.length(); i++) {
					JSONObject itemJson = jsonArray1.getJSONObject(i);
					map = new HashMap<String, Object>();
					map.put("id", itemJson.getInt("id"));
					map.put("name", itemJson.getString("name"));
					equipList.add(map);
				}

				JSONArray jsonArray2 = jsonObject
						.getJSONArray("trainingParams");
				for (int i = 0; i < jsonArray2.length(); i++) {
					JSONObject itemJson = jsonArray2.getJSONObject(i);
					map = new HashMap<String, Object>();
					map.put("id", itemJson.getInt("id"));
					map.put("name", itemJson.getString("name"));
					trainingList.add(map);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 用品设施
	 */
	private ArrayList<HashMap<String, Object>> equipList = new ArrayList<HashMap<String, Object>>();
	/**
	 * 时间周期
	 */
	private ArrayList<HashMap<String, Object>> trainingList = new ArrayList<HashMap<String, Object>>();

	/**
	 * 初始化反馈数据
	 */
	private void initFeedData() {
		if (null != recordNothingList)
			recordNothingList.clear();
		if (null != recordDisinfectionList)
			recordDisinfectionList.clear();
		if (null != recordWashHandsList)
			recordWashHandsList.clear();
		if (null != recordGloveList)
			recordGloveList.clear();
		for (int k = 0; k < dataList.size(); k++) {

			planListDb pdb = dataList.get(k);
			// { "4卫生手消毒", "5洗手", "6戴手套", "0未采取措施" };

			for (subTaskVo sv : pdb.getSubTasks()) {
				if (null == sv)
					continue;
				HashMap<String, String> map = new HashMap<String, String>();
				// sv.setPname(pdb.getPname());//名称
				// sv.setPname(pdb.getPpostName());//岗位
				map.put("id", "" + k);
				map.put("job", pdb.getPpostName());
				map.put("job_type", pdb.getPpost());
				map.put("hos", pdb.getWorkName());
				map.put("hos_type", pdb.getWork_type());

				map.put("name", pdb.getPname());

				pdb.get_id();

				if (null != sv.getUnrules() && sv.getUnrules().size() > 0) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < sv.getUnrules().size(); i++) {

						if (!TextUtils
								.isEmpty(sv.getUnrules().get(i).getName())) {
							sb.append("\u2000"
									+ sv.getUnrules().get(i).getName());
						}
					}
					if (sb.length() > 0) {
						map.put("reason", sb.toString());
					}
				}
				map.put("type", sv.getResults());
				if (sv.getResults().equals("0")) {
					recordNothingList.add(map);
				} else if (sv.getResults().equals("4")) {
					recordWashHandsList.add(map);
				} else if (sv.getResults().equals("5")) {
					recordDisinfectionList.add(map);
				} else if (sv.getResults().equals("6")) {
					recordGloveList.add(map);
				}
			}
		}

	}

	MyAdapter nothindAdapter, disnifectionAdapter, washhandsAdapter,
			gloveAdapter;

	/**
	 * 初始化adapter
	 */
	private void initRecordAdpter() {
		disnifectionAdapter = new MyAdapter(recordDisinfectionList);
		listivew_disinfection.setAdapter(disnifectionAdapter);
		washhandsAdapter = new MyAdapter(recordWashHandsList);

		listivew_washhands.setAdapter(washhandsAdapter);
		gloveAdapter = new MyAdapter(recordGloveList);

		listivew_glove.setAdapter(gloveAdapter);
		nothindAdapter = new MyAdapter(recordNothingList);

		listivew_nothing.setAdapter(nothindAdapter);
	}

	private void setRecordAdapter() {
		recordNothingList = new ArrayList<HashMap<String, String>>();
		recordDisinfectionList = new ArrayList<HashMap<String, String>>();
		recordWashHandsList = new ArrayList<HashMap<String, String>>();
		recordGloveList = new ArrayList<HashMap<String, String>>();

		initFeedData();
		setFeedListview();
		disnifectionAdapter.notifyDataSetChanged();
		nothindAdapter.notifyDataSetChanged();
		washhandsAdapter.notifyDataSetChanged();
		gloveAdapter.notifyDataSetChanged();
	}

	/**
	 * 设置数据
	 */
	private void setFeedData(Intent data) {
		str_remark = data
				.getStringExtra(SupervisorFeedBackActivity.INTENT_REMARK);
		str_name = data.getStringExtra(SupervisorFeedBackActivity.INTENT_NANE);
		timeIndex = data.getIntExtra(
				SupervisorFeedBackActivity.INTENT_TIMEINDEX, -1);
		reasonIds = data
				.getIntegerArrayListExtra(SupervisorFeedBackActivity.INTENT_REASONS);
		ArrayList<String> reasons=data.getStringArrayListExtra(SupervisorFeedBackActivity.INTENT_REASONS_NAME);
		String reasonStr="";
		tv.setIs_training(timeIndex > 0 ? "1" : "0");
		tv.setTraining_recycle(timeIndex + "");
		tv.setIs_again_supervisor(data.getIntExtra(SupervisorFeedBackActivity.AGAIN_SUP,0));
		tv.setIs_feedback_department(data.getIntExtra(SupervisorFeedBackActivity.FEEDBACK_DEPARTMENT,0));
		if (null != reasonIds && reasonIds.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < reasonIds.size(); i++) {
				sb.append(reasonIds.get(i));
				reasonStr+=reasons.get(i);
				if (i < reasonIds.size() - 1) {
					sb.append(",");
					reasonStr+=",";
				}
			}
			tv.setEquip_examine(sb.toString());

		}



		String unrulesStr="洗手不规范记录:";
		tv.setCheck_content("手卫生调查");
		if(recordDisinfectionList.size()>0){
		for(Map map:recordDisinfectionList){
			unrulesStr+=map.get("name").toString()+map.get("reason").toString();
		}
		}else{
			unrulesStr="";
		}
		String	xiaoduUnRules="卫生手消毒不规范记录:";
		if(recordWashHandsList.size()>0){
		for(Map map:recordWashHandsList){
			xiaoduUnRules+=map.get("reason").toString()+";";
		}
		}else{
			xiaoduUnRules="";
		}
		String isTraned=timeIndex>0?"是":"否";
		tv.setFeedback_obj(str_name);
		tv.setExist_problem("科室被反馈人:"+str_name+"\n"+unrulesStr+"\n"+xiaoduUnRules+"\n"+
				"是否培训过:"+isTraned+"\n"+
				"手卫生用品设施调查:"+reasonStr
		);
		tv.setDeal_suggest(str_remark);
		tv.setRemark(str_remark);
	}

	/**
	 * 设置不规范记录数据
	 */
	private void setFeedListview() {
		if (recordDisinfectionList != null && recordDisinfectionList.size() > 0) {
			lay_disinfection.setVisibility(View.VISIBLE);

		} else {
			lay_disinfection.setVisibility(View.GONE);
		}

		if (recordWashHandsList != null && recordWashHandsList.size() > 0) {
			lay_washhands.setVisibility(View.VISIBLE);

		} else {
			lay_washhands.setVisibility(View.GONE);

		}

		if (recordGloveList != null && recordGloveList.size() > 0) {
			lay_glove.setVisibility(View.VISIBLE);

		} else {
			lay_glove.setVisibility(View.GONE);
		}
		if (recordNothingList != null && recordNothingList.size() > 0) {
			lay_nothing.setVisibility(View.VISIBLE);

		} else {
			lay_nothing.setVisibility(View.GONE);
		}

	}

	// // 设施调查数据
	// String[] reason_strs = null;
	// // 培训时间数据
	// String[] train_times = null;

	// 反馈记录
	private String str_remark;
	// 被反馈对象
	private String str_name;
	// 培训时间序号
	int timeIndex = -1;
	// 已选择的设施调查ID
	ArrayList<Integer> reasonIds = new ArrayList<Integer>();

	/**
	 * 设置相应控件数据
	 */
	private void setViewData() {
		if (!TextUtils.isEmpty(str_remark)) {
			lay_feed_remark.setVisibility(View.VISIBLE);
			text_record_remark.setText(str_remark);
		} else {
			lay_feed_remark.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(str_name)) {
			lay_feed_body.setVisibility(View.VISIBLE);
			text_feed_body.setText(str_name);
		} else {
			lay_feed_body.setVisibility(View.GONE);
		}

		// /**
		// * 用品设施
		// */
		// private ArrayList<HashMap<String, Object>> equipList = new
		// ArrayList<HashMap<String, Object>>();
		// /**
		// * 时间周期
		// */
		// private ArrayList<HashMap<String, Object>> trainingList = new
		// ArrayList<HashMap<String, Object>>();

		if (trainingList != null && trainingList.size() > 0 && timeIndex >= 0) {
			for (int i = 0; i < trainingList.size(); i++) {
				if ((trainingList.get(i).get("id")).equals(timeIndex + "")) {
					lay_teach_times.setVisibility(View.VISIBLE);
					text_teach_time.setText("是\u3000"
							+ trainingList.get(i).get("name"));
				}
			}
		} else {
			lay_teach_times.setVisibility(View.GONE);
		}

		if (null != reasonIds && reasonIds.size() > 0 && null != equipList
				&& equipList.size() > 0) {
			lay_records.setVisibility(View.VISIBLE);
			StringBuffer sb = new StringBuffer("");

			for (int k = 0; k < equipList.size(); k++) {
				for (int i = 0; i < reasonIds.size(); i++) {
					if (equipList.get(k).get("id").equals(reasonIds.get(i))) {
						sb.append(equipList.get(k).get("name"));
						sb.append("\u3000");
					}
				}
			}

			text_records.setText(sb.toString().trim());
		} else {
			lay_records.setVisibility(View.GONE);
		}

	}

	/**
	 * 设置标题
	 *
	 * @param title
	 */
	private void setTitle(String title) {
		topView.setTitle(title);
	}

	/*
	 * 设置被观察者
	 */
	private void setAdapter() {
		this.adapter = new UnTimeAdapter(this, dataList, tv);
		this.adapter.setListener(new UnTimeAdapterListener() {

			@Override
			public void OnClick() {
				// showRemarkPopu();
			}

			@Override
			public String OnSpinnerClick(int position, int index, int index2) {
				// TODO Auto-generated method stub

				String name = "";
				if (index2 >= 0) {
					dataList.get(position).setPpostName(
							jobList.get(index2).getName());

					dataList.get(position)
							.setPpost(jobList.get(index2).getId());
					name = jobList.get(index2).getName();
				} else {
					dataList.get(position).setPpostName("");

					dataList.get(position).setPpost("11");
				}
				// 设置院内员外
				if (index >= 0) {
					dataList.get(position).setWorkName(
							jobTypelist.get(index).getName());
					String tid = jobTypelist.get(index).getId();
					dataList.get(position).setWork_type(tid);
				} else {
					dataList.get(position).setWorkName("");
					dataList.get(position).setWork_type("");
				}

				initFeedData();
				// setRecordAdapter();
				adapter.notifyDataSetChanged();
				return name;
			}
		}, new UnTimeItemTextChangListener() {

			@Override
			public void OnEditChange(int position, String text) {
				// TODO Auto-generated method stub
				if (text != null && !text.equals("")) {
					dataList.get(position).setPname(text);
				}
				dataList.get(position).setTemp_pname(text);

				initFeedData();
				// setRecordAdapter();
			}
		});
		listivew_ob.setAdapter(adapter);
		adapter.setSpinnerAdapter(jobList, jobTypelist);
	}

	public void onBackSaveData(){
		Intent it = new Intent();
		for (planListDb pdb : dataList) {

			for (subTaskVo sv : pdb.getSubTasks()) {
				sv.setPname(pdb.getPname());
				sv.setPpoName(pdb.getPpostName());
				sv.setWorkName(pdb.getWorkName());
			}
		}
		String str = gson.toJson(dataList);
		tv.setFiveTasks(str);
		Log.e("111111111111", str);
		it.putExtra("data", tv);
		setResult(HandWashTasksActivity.REQUEST_SAVE_REMARK, it);
		this.finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_commitfram:
			case R.id.btn_commit:
				// 提交,先剔除没有做任务的对象

				updateDb();

				break;
			case R.id.text_depart:
				Intent in = new Intent(mcontext, ChooseDepartActivity.class);
				in.putExtra("isAfter", false);
				in.putExtra("type", "5");
				startActivityForResult(in, 0x189);
				break;
			default:
				break;
		}

	}

	/**
	 * 跳转设置 intent 设置反馈数据
	 */
	private void setFeedBack() {
		Intent intent = new Intent(mcontext, SupervisorFeedBackActivity.class);
		// 设置培训时间序号
		intent.putExtra(SupervisorFeedBackActivity.INTENT_TIMEINDEX, timeIndex);

		// 设置设施调查序号
		intent.putExtra(SupervisorFeedBackActivity.INTENT_REASONS, reasonIds);

		intent.putExtra(SupervisorFeedBackActivity.INTENT_REMARK, str_remark);
		intent.putExtra(SupervisorFeedBackActivity.INTENT_NANE, str_name);
		intent.putExtra(
				SupervisorFeedBackActivity.INTENT_RECORDALLLIST_DISINFECTION,
				recordDisinfectionList);
		intent.putExtra(
				SupervisorFeedBackActivity.INTENT_RECORDALLLIST_WASHHANDS,
				recordWashHandsList);
		intent.putExtra(SupervisorFeedBackActivity.INTENT_RECORDALLLIST_GLOVE,
				recordGloveList);
		intent.putExtra(
				SupervisorFeedBackActivity.INTENT_RECORDALLLIST_NOTHING,
				recordNothingList);

		intent.putExtra(SupervisorFeedBackActivity.INTENT_PARAMS_EQUIP,
				equipList);
		intent.putExtra(SupervisorFeedBackActivity.INTENT_PARAMS_TRAIN,
				trainingList);
		intent.putExtra(SupervisorFeedBackActivity.INTENT_ISWHO, tv.isWho());
		intent.putExtra(SupervisorFeedBackActivity.FEEDBACK_DEPARTMENT, tv.getIs_feedback_department());
		intent.putExtra(SupervisorFeedBackActivity.AGAIN_SUP, tv.getIs_again_supervisor());
		startActivityForResult(intent, 0x11);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0x11 && resultCode == RESULT_OK) {
			setFeedData(data);
			// setViewData();
		} else if (resultCode == 0x189) {
			String departName = data.getStringExtra("departmentName");
			String departId = data.getStringExtra("departmentId");
			tv.setDepartment(departId);
			tv.setDepartmentName(departName);
			text_depart.setText(departName);
			for (planListDb pdb : dataList) {
				pdb.setDepartment(departId);
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 更新反馈数据
	 */
	public void updateDb() {
		// TODO Auto-generated method stub
		showprocessdialog();
		List<planListDb> pdblist = new ArrayList<planListDb>();
		pdblist.addAll(dataList);
		dataList.clear();
		for (int i = 0; i < pdblist.size(); i++) {
			if (pdblist.get(i).getSubTasks().size() > 0) {
				dataList.add(pdblist.get(i));
			}
		}

		if (null != tv.getSaveAttachmentsList()) {
			List<Attachments> saveAttachmentsList = gson.fromJson(tv
							.getSaveAttachmentsList().toString(),
					new TypeToken<List<Attachments>>() {
					}.getType());
			if (null != saveAttachmentsList && saveAttachmentsList.size() > 0)
				for (planListDb pdb : dataList) {
					if (pdb.getSubTasks().size() > 0) {
						for (subTaskVo sb : pdb.getSubTasks()) {
							sb.getAttachments().addAll(saveAttachmentsList);
							tv.setSaveAttachmentsList("");
							break;
						}
						break;
					}
				}
		}

		String str = gson.toJson(dataList);
		Log.i("1111111111", str);
		tv.setFiveTasks(str);

		// tv.setRemark(text_remark.getText().toString());
		TaskUtils.onCommitAfterTask(tv);


		if (NetWorkUtils.isConnect(mcontext)) {
			if (!NetWorkUtils.isWifiState(MyAppliaction.getContext())) {
				tv.setStatus(1);
				onSendFinish(FormCommitSucTipsActivity.ONLY_WIFI);
			} else {
				btn_commit.setEnabled(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						showprocessdialog();
						if(BaseUploader.getUploader(1).Upload(tv)){
							tv.setStatus(0);
							onSendFinish(FormCommitSucTipsActivity.UPLOAD_SUC);

						}else{
							tv.setStatus(1);
							onSendFinish(FormCommitSucTipsActivity.UPLOAD_FIAL);
						}
						dismissdialog();


					}
				}).start();

				Message ms=new Message();
				ms.what=FormCommitSucTipsActivity.UPLOAD_SUC;
				myHandler.sendMessage(ms);
			}

		} else {
			tv.setStatus(1);
			onSendFinish(FormCommitSucTipsActivity.NET_WORK_DISCONECT);
			return;
		}


		addScore("2");
	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
					case FormCommitSucTipsActivity.UPLOAD_SUC:
						btn_commit.setEnabled(true);
						break;
					default:
						break;
				}
			}
		}

	};
	private void onSendFinish(int state){
		dismissdialog();
		TaskUtils.onUpdateTaskById(tv);
		if(tv.getStatus()==0){
			Intent toWeb = new Intent(mcontext, ReportWebViewDemo.class);
			toWeb.putExtra("task_url",
					WebUrl.TASKDETAILURL + tv.getTask_id());
			toWeb.putExtra("title", "督导统计");
			toWeb.putExtra("showbottom", "show");
			toWeb.putExtra("task_id", tv.getTask_id() + "");
			toWeb.putExtra("data", tv);
			Log.i("11111111", tv.getDbid() + "----");
			startActivity(toWeb);
		}else {
			Intent brodcastIntent = new Intent(mcontext, FormCommitSucTipsActivity.class);
			brodcastIntent
					.setAction(HandWashTasksActivity.CLOSE_ACTIVITY);// 关闭前一个页面
			sendBroadcast(brodcastIntent);
			brodcastIntent.putExtra("data", tv);
			brodcastIntent.putExtra("commit_status", state);
			startActivity(brodcastIntent);
		}

		setResult(HandWashTasksActivity.CODE_RESULT_CANCEL);
		finish();
	}
	List<JobListVo> jobList = new ArrayList<JobListVo>();
	List<JobListVo> jobTypelist = new ArrayList<JobListVo>();
	String jobs[];

	/**
	 * 初始化工作岗位数据
	 */
	private void findDbJobList() {
		try {
			if (null != DataBaseHelper
					.getDbUtilsInstance(mcontext).findAll(JobListVo.class)) {
				jobList = DataBaseHelper
						.getDbUtilsInstance(mcontext).findAll(JobListVo.class);
				Log.i("11111111111joblist", jobList.size() + "");

				jobs = new String[jobList.size()];

				List<JobListVo> temp = new ArrayList<JobListVo>();
				JobListVo other = new JobListVo();
				int k = 0;
				for (int i = 0, j = jobList.size(); i < j; i++) {
					JobListVo jv = jobList.get(i);
					if (jv.getName().equals("其他")) {
						other = jv;
					} else {
						temp.add(jv);
						jobs[k] = jv.getName();
						k++;
					}

				}

				jobList.clear();
				jobList.addAll(temp);
				jobList.add(other);
				jobs[jobList.size() - 1] = other.getName();

				// type1 位督导岗位 type4为职称 type3为职位
				String jsonStr = SharedPreferencesUtil.getString(mcontext,
						"jobinfolist", null);
				if (!TextUtils.isEmpty(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						JSONArray jarr4 = jsonObject.optJSONArray("jobType5");
						jobTypelist = gson.fromJson(jarr4.toString(),
								new TypeToken<List<JobListVo>>() {
								}.getType());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	TaskVo tv;
	private Gson gson = new Gson();

	private class MyAdapter extends BaseAdapter {
		public int colors[] = { R.color.line1_corlor, R.color.line2_corlor,
				R.color.line3_corlor, R.color.line4_corlor,
				R.color.line5_corlor, R.color.line6_corlor };
		private LayoutInflater mInflater;
		private List<HashMap<String, String>> recordList;

		public MyAdapter(List<HashMap<String, String>> recordList) {
			mInflater = LayoutInflater.from(mcontext);
			this.recordList = recordList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return recordList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return recordList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new Holder();
				convertView = mInflater
						.inflate(R.layout.remark_list_item, null);
				holder.text_job = (TextView) convertView
						.findViewById(R.id.text_job);
				holder.text_job.setBackgroundColor(res.getColor(R.color.white));
				holder.text_remark = (TextView) convertView
						.findViewById(R.id.text_remark);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
				clearHolder(holder);
			}

			if (position < recordList.size()) {
				HashMap<String, String> item = recordList.get(position);

				String left = "";
				if (item.containsKey("hos")
						&& !AbStrUtil.isEmpty(item.get("hos")))
					left = item.get("hos");
				if (item.containsKey("job")
						&& !AbStrUtil.isEmpty(item.get("job")))
					left = left + "\u2000" + item.get("job");
				// if (item.containsKey("name"))
				// left = left + "\u2000" + item.get("name");
				left = left + "\u2000";
				holder.text_job.setText(left);

				if (item.containsKey("reason")
						&& !AbStrUtil.isEmpty(item.get("reason"))) {
					holder.text_remark.setText(left // + "\u3000"
							+ item.get("reason"));
				} else {
					holder.text_remark.setText(left + "\u3000" + "不规范");
				}

				String type = "";
				if (item.containsKey("job_type"))
					type = item.get("job_type");

				if (TextUtils.isDigitsOnly(type)) {
					int _type = Integer.parseInt(type);
					int colorIndex = _type % 6;
					holder.text_job.setTextColor(res
							.getColor(colors[colorIndex]));
					// holder.text_job.setTextColor(res.getColor(R.color.red));
				}
			}
			return convertView;
		}

		void clearHolder(Holder holder) {
			holder.text_job.setText("");
			holder.text_remark.setText("");
		}

		Holder holder = null;

		class Holder {
			TextView text_job, text_remark;
		}
	}

}
