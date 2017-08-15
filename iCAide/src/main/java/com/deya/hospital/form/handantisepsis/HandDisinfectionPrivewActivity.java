package com.deya.hospital.form.handantisepsis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.form.BaseFromPriViewActivity;
import com.deya.hospital.form.FormDataCache;
import com.deya.hospital.form.FormSettingActivity;
import com.deya.hospital.form.FormUtils;
import com.deya.hospital.form.PreviewAdapter;
import com.deya.hospital.form.PreviewAdapter.previewAdapterInter;
import com.deya.hospital.supervisor.AddtaskInfoActivity;
import com.deya.hospital.supervisor.PartTimeStaffDialog;
import com.deya.hospital.supervisor.PartTimeStaffDialog.PDialogInter;
import com.deya.hospital.supervisor.SupervisionActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormDetailVo;
import com.deya.hospital.vo.FormVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.workspacemain.TodayDynamicFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class HandDisinfectionPrivewActivity extends BaseFromPriViewActivity implements
		OnClickListener {
	PreviewAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if (getIntent().hasExtra("data")) {
			data = (TaskVo) getIntent().getSerializableExtra("data");
			title = data.getName();
			formtype = data.getFormType();
			totalScore = data.getTotalscore();
			id = data.getFormId();
		}else{
			data=new TaskVo();
		}
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onChooseDepartList(String name, String id) {

	}

	@Override
	protected void initBaseData() {
		String time = getIntent().getStringExtra("time");
		if (data.getDbid() <=0||data.getIsAfterTask()==1) {
			time = TaskUtils.getTaskMissionTime(time);
			data.setMission_time(time);
			String hospitalJob = tools.getValue(Constants.JOB);
			String defultDepart = tools.getValue(Constants.DEFULT_DEPARTID);
			if (null!=hospitalJob&&hospitalJob.equals("3") && !AbStrUtil.isEmpty(defultDepart)) {//
				// 兼职感控人员在设置了默认科室后可以直接跳过选择部分
				if (!AbStrUtil.isEmpty("defultDepart")) {

					data.setDepartmentName(tools.getValue(Constants.DEFULT_DEPART_NAME));
					data.setDepartment(defultDepart);
					departTv.setText(tools.getValue(Constants.DEFULT_DEPART_NAME));
				}
			}
			getDetailData();
		} else {
			List<FormDetailListVo> lis1t = gson.fromJson(data.getItems()
					.toString(), new TypeToken<List<FormDetailListVo>>() {
			}.getType());
			list.addAll(lis1t);
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_prviewlist;
	}

	@Override
	public void initChildView() {
		intTopView();
		initView();
	}

	@Override
	public void saveCache(int status) {

	}

	void getDetailData() {
		try {
			FormVo fv = DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findFirst(
							Selector.from(FormVo.class).where("types", "=", 5));
			if(null==fv){
				return;
			}

			data.setFormType(fv.getType());
			data.setTotalscore(fv.getScore());
			data.setName(fv.getName());
			data.setFormId(fv.getId());
			title = data.getName();
			titleTv.setText(title);

			totalScoreTv2.setText("质量标准" + "(总分："
					+ AbStrUtil.reMoveUnUseNumber(data.getTotalscore() + "")
					+ ")");
			formtype = data.getFormType();
			totalScore = data.getTotalscore();
			id = data.getFormId();

			String str = fv.getCacheItems();
			List<FormDetailListVo> itemList = gson.fromJson(str,
					new TypeToken<List<FormDetailListVo>>() {
					}.getType());
			list.addAll(itemList);
			for (FormDetailListVo fdlv : list) {
				for (FormDetailVo fdv : fdlv.getSub_items()) {
					fdv.setScores(fdv.getBase_score());
				}
			}
			adapter.setdata(list);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initView() {
		sumbmitBtn = (Button) this.findViewById(R.id.sumbmitBtn);
		sumbmitBtn.setOnClickListener(this);
		// sumbmitBtn.setText("保\u3000存");
		findViewById(R.id.departRelay).setBackgroundResource(
				R.drawable.big_round_blue_type_style);
		titleLay = (LinearLayout) this.findViewById(R.id.titleLay);
		titleLay.setVisibility(View.GONE);
		findViewById(R.id.mainRemarkTv).setVisibility(View.GONE);
		findViewById(R.id.departLine).setVisibility(View.GONE);
		departLay = (LinearLayout) this.findViewById(R.id.departLay);
		departLay.setVisibility(View.VISIBLE);
		departLay.setOnClickListener(this);
		departTv = (TextView) this.findViewById(R.id.departTv);
		titleTv.setText(title);

		tipsTv = (TextView) this.findViewById(R.id.tiptv);
		String text = "";
		if (!AbStrUtil.isEmpty(data.getDepartmentName())) {
			text = data.getDepartmentName();

		}
		if (!AbStrUtil.isEmpty(data.getUname())) {
			 text += " " + data.getUname();
		}
		if (!AbStrUtil.isEmpty(data.getAddress())) {
			 text += " " + data.getAddress();
		}
		departTv.setText(text);
		setDepartLay();
		sumbmitlay = (LinearLayout) this.findViewById(R.id.sumbmitlay);
		sumbmitlay.setVisibility(View.VISIBLE);
		formTitleTv = (TextView) this.findViewById(R.id.formTitleTv);
		formTitleTv.setText(title);

		totalScoreTv = (TextView) this.findViewById(R.id.totalScoreTv);
		totalScoreTv2 = (TextView) this.findViewById(R.id.totalScoreTv2);
		if (formtype == 1) {
			totalScoreTv2.setVisibility(View.VISIBLE);
			totalScoreTv2.setText("质量标准" + "(总分："
					+ AbStrUtil.reMoveUnUseNumber(totalScore + "") + ")");
		} else {
			totalScoreTv.setVisibility(View.GONE);
		}
		listView = (ListView) this.findViewById(R.id.ListView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				toFormSetting(position);
			}
		});
		adapter = new PreviewAdapter(mcontext, list, 1,
				new previewAdapterInter() {

					@Override
					public void onputData(int position) {
						putdata(position);// 跳转到操作页面
					}

					@Override
					public void onRemark(int parentPosition, int position) {
						toRemark(parentPosition, position);

					}
				});
		listView.setAdapter(adapter);

	}

	public void toFormSetting(int position) {
		Intent it = new Intent(mcontext, FormSettingActivity.class);
		it.putExtra("data", (Serializable) list);
		it.putExtra("position", position);
		it.putExtra("vo", data);

		startActivity(it);

	}

	TextView titleTv;

	private void intTopView() {
		titleTv = (TextView) this.findViewById(R.id.title);

		rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
		rlBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			savaData();
			break;
		case R.id.sumbmitBtn:
			if (AbStrUtil.isEmpty(data.getDepartment())) {
				Intent it = new Intent(mcontext, AddtaskInfoActivity.class);
				it.putExtra("data", data);
				startActivityForResult(it, 0x55);
				ToastUtils.showToast(mcontext, "请先完善资料再保存");
			} else {
				showTips();
			}

			break;
		case R.id.departLay:
			Intent it = new Intent(mcontext, AddtaskInfoActivity.class);
			it.putExtra("data", data);
			startActivityForResult(it, 0x55);
			break;
		default:
			break;
		}

	}

	private void savaData() {
		if (!needAdd) {
			finish();
			return;
		}
		if (data.getDbid()>0) {
			data.setItems(gson.toJson(list));
			TaskUtils.onUpdateTaskById(data);
		} else {
			addTask(false, 2);

		}
		Intent brodcastIntent = new Intent();
		brodcastIntent.setAction(TodayDynamicFragment.UPDATA_ACTION);
		this.sendBroadcast(brodcastIntent);
		finish();
	}

	private void onSumbmit() {
		if(data.getIsAfterTask()==1){
			TaskUtils.onCommitAfterTask(data);
			data.setScore(totalScore + "");
			data.setGrid_type(formtype + "");
			data.setFormType(formtype);
			data.setType("5");
			data.setTmp_id(id);

			// tv.setName(title);
			FormUtils.addTask(this, data, true, list);

		} else if (data.getStatus() != 2) {
			data=addTask(true, 1);
			data.setStatus(3);//未开始过任务 ——未在数据库保存过数据
		}
		data.setItems(gson.toJson(list));
		sumbmitBtn.setEnabled(false);
		commsubmit(data);
	}



	private Gson gson = new Gson();
	protected void setResult(JSONObject job) {
		JSONObject jsonObject = job.optJSONObject("info");
		if (AbStrUtil.isEmpty(jsonObject.toString())) {
			return;
		}

		data.setFormType(jsonObject.optInt("type"));
		data.setTotalscore(jsonObject.optDouble("score"));
		data.setName(jsonObject.optString("name"));
		data.setFormId(jsonObject.optString("id"));
		title = data.getName();
		titleTv.setText(title);

		totalScoreTv2.setText("质量标准" + "(总分："
				+ AbStrUtil.reMoveUnUseNumber(data.getTotalscore() + "") + ")");
		formtype = data.getFormType();
		totalScore = data.getTotalscore();
		id = data.getFormId();
		Log.i("handd", jsonObject.toString());
		JSONArray jarr = job.optJSONArray("item");
		if (null != jarr) {
			List<FormDetailListVo> lis1t = gson.fromJson(jarr.toString(),
					new TypeToken<List<FormDetailListVo>>() {
					}.getType());
			list.addAll(lis1t);
			for (FormDetailListVo fdlv : list) {
				for (FormDetailVo fdv : fdlv.getSub_items()) {
					fdv.setScores(fdv.getBase_score());
				}
			}
			adapter.setdata(list);
		}
		FormDataCache.saveFormListItemData(mcontext, id, job.toString());
	}

	@Override
	public  void onActivityResult(int requestCode, int resultCode,
			Intent redata) {
		super.onActivityResult(requestCode, resultCode, redata);
		if (resultCode == 0x171 && null != redata) {
			sumbmitlay.setVisibility(View.VISIBLE);
			list = (List<FormDetailListVo>) redata.getSerializableExtra("data");
			double score = 0;
			for (FormDetailListVo fdlv : list) {
				for (FormDetailVo fdv : fdlv.getSub_items()) {
					score += fdv.getScores();
				}
			}
			totalScoreTv2.setText("质量标准" + "(总分："
					+ AbStrUtil.reMoveUnUseNumber(score + "") + ")");
			totalScore = score;
			needAdd = true;
			adapter.setdata(list);
		} else if (resultCode == 0x172 && null != redata) {
			TaskVo remarkVo = (TaskVo) redata
					.getSerializableExtra("remarkdata");
			String remarkStr = gson.toJson(remarkVo);
			Log.i("1111111111", remarkStr);
			list.get(markParentPosition).getSub_items().get(markitemPosition)
					.setRemark(remarkStr);
			list.get(markParentPosition).getSub_items().get(markitemPosition)
					.setRemark(true);
			list.get(markParentPosition).getSub_items().get(markitemPosition)
					.setRemarkVo(remarkVo);
			needAdd = true;
			adapter.setdata(list);
		} else if (resultCode == 0x55 && null != redata) {
			TaskVo resut = (TaskVo) redata.getSerializableExtra("data");

			data.setDepartmentName(resut.getDepartmentName());
			data.setDepartment(resut.getDepartment());
			data.setWork_type(resut.getWork_type());
			data.setAddress(resut.getAddress());
			data.setUname(resut.getUname());
			data.setPpost(resut.getPpost());
			data.setUteam(resut.getUteam());
			data.setMission_time(resut.getMission_time());
			departTv.setText(resut.getDepartmentName() + "  "
					+ resut.getUname() + "  " + data.getAddress());
			setDepartLay();
			needAdd = true;
		}

	}

	public void putdata(int position) {
		Intent it = new Intent(mcontext, FormSettingActivity.class);
		it.putExtra("list", (Serializable) list);
		it.putExtra("position", position);
		it.putExtra("data", data);
		it.putExtra("updated", data.getTask_id()>0);
		it.putExtra("title", title);
		it.putExtra("vo", list.get(position));
		Log.i("11111111", data.getDepartmentName());
		it.putExtra("type", formtype);
		startActivityForResult(it, 0x117);
	}

	private int markParentPosition;
	private int markitemPosition;

	public void toRemark(int position, int itemPosition) {
		this.markParentPosition = position;
		this.markitemPosition = itemPosition;
		Intent it = new Intent(mcontext, SupervisionActivity.class);
		TaskVo remarkVo = list.get(position).getSub_items().get(itemPosition)
				.getRemarkVo();
		if (null != remarkVo) {
			Log.i("111111111", remarkVo.getAttachments().size() + "");
		}
		if (null == remarkVo || AbStrUtil.isEmpty(remarkVo.getCheck_content())) {
			remarkVo = data;
			remarkVo.setCheck_content(list.get(position).getName()
					+ "\n"
					+ list.get(position).getSub_items().get(itemPosition)
							.getDescribes());
		}

		it.putExtra("data", remarkVo);
		it.putExtra("isRemark", "1");
		startActivityForResult(it, 0x117);
	}


	public TaskVo addTask(boolean upload, int state) {
		TaskVo tv = new TaskVo();
		tv.setTypes_info(title);
		tv.setTotalscore(totalScore);
		tv.setFormId(id);
		tv.setName(title);
		tv.setFormType(formtype);
		tv.setHospital(tools.getValue(Constants.HOSPITAL_ID));
		tv.setDepartment(data.getDepartment());
		tv.setWork_type(data.getWork_type());
		tv.setAddress(data.getAddress());
		tv.setUname(data.getUname());
		tv.setPpost(data.getPpost());
		tv.setUteam(data.getUteam());

		tv.setMain_remark(data.getMain_remark());
		String time = "";
		Calendar calendar = null;
		calendar = new GregorianCalendar();// 子类实例化
		time = data.getMission_time() + ":00 ";
//				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
//				+ calendar.get(Calendar.MINUTE) + ":"
//				+ calendar.get(Calendar.SECOND);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		time = sdf.format(date);
		tv.setMission_time(time + "");
		tv.setType("5");
		tv.setTmp_id(id);
		tv.setMobile(tools.getValue(Constants.MOBILE));
		tv.setStatus(state);
		tv.setScore(totalScore + "");
		tv.setGrid_type(formtype + "");
		tv.setFormType(formtype);


		// tv.setName(title);
		tv.setDepartmentName(data.getDepartmentName());
		tv = FormUtils.addTask(this, tv, upload, list);
		if (!upload) {
			TaskUtils.onAddTaskInDb(tv);
		}
		return tv;

	}

	public void setDepartLay() {
		if (departTv.getText().length() > 0) {
			tipsTv.setVisibility(View.GONE);
		}
	}

	PartTimeStaffDialog dialog;

	public void showTips() {
		dialog = new PartTimeStaffDialog(mcontext, false, "是否提交,提交之后就不能修改了哟!",
				new PDialogInter() {

					@Override
					public void onEnter() {
						addMinRemark();
						onSumbmit();;
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
		dialog.show();
	}

	public void addMinRemark() {
		TaskVo remarkData = FormUtils
				.getMainRemark(list, data, title, mcontext);
		if (null != remarkData) {
			String mainRemarkStr = gson.toJson(remarkData);
			data.setMain_remark(mainRemarkStr);
		}
	}

	@Override
	public void doSubmit() {

	}


}
