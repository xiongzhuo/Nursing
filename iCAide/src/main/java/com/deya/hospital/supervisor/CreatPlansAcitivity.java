package com.deya.hospital.supervisor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.DepartChoosePop;
import com.deya.hospital.base.DepartChoosePop.OnDepartPopuClick;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.vo.dbdata.planListDb;
import com.deya.hospital.supervisor.JobDialog.ChooseItem;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.ScrollViewIncludeGridView;
import com.deya.hospital.util.ScrollViewIncludeListView;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.workspace.TaskUtils;
import com.example.calendar.CalendarMainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.DbException;

public class CreatPlansAcitivity extends BaseActivity implements
		OnClickListener {
	private List<TaskVo> newTaskList = new ArrayList<TaskVo>();
	planListDb planDb = new planListDb();

	String typename = "5";// 0为不限时机，1为5个时机，2位10个时机，3为20时机，4为30时机
	String type = "";// 0为不限时机，1为5个时机，2位10个时机，3为20时机，4为30时机
	String handHygieneMethod = "0"; // 督查结果：0-没洗，1-流动水，2-手消，3-手套，4-不规范
	String departmentName = "";
	String departmentId = "";
	private EditText departmentTv;
	TextView cancleTv;
	Tools tools;
	public static final int CREATRESULT = 0x107;
	Gson gson;
	ScrollViewIncludeListView popListView;
	private ScrollViewIncludeGridView typeLv;
	LinearLayout randomTimesLongLay, randomTimesNumLay;
	EditText randomTimesLongTv;

	LinearLayout addDepart;
	String creatTime = "";
	ImageView fivetime, fifteenTimes, twentyTimes, tweentyfivetimes,
			tirtyfivetimes, fortyTimes, limitTimes;
	LinearLayout unlimiteLay;
	public static final String departName = "departName";
	public static final String departId = "departId";
	public static final String departNames = "departNames";
	public static final String departIds = "departIds";
	String missonTime = "";

	private String[] departmentNames;
	private String[] departmentIds;

	EditText randomTimes;
	TextView jobTv;
	private Button nextButton;
	private RelativeLayout palns_relative;
	boolean isShowFram = false;
	List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
	private DepartChoosePop departDialog;
	protected TextView departTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creatplan);
		tools = new Tools(mcontext, Constants.AC);
		gson = new Gson();
		TaskUtils.getDepartList(mcontext, departlist);
		creatTime = getIntent().getStringExtra("time");
		getCacheData();
		checkDbisCreated();
		initView();
		departDialog = new DepartChoosePop(mcontext, departlist,
				new OnDepartPopuClick() {

					@Override
					public void onChooseDepart(String name, String id) {

						departTv.setText(name);
						departmentName = name;
						departmentId = id;
					}

					@Override
					public void onAddDepart() {
						Intent it = new Intent(mcontext,
								AddDepartmentActivity.class);
						it.putExtra("data", (Serializable) departlist);
						startActivityForResult(it, 0x22);

					}
				});
	}

	public int dp2Px(int dp) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}

	public int[] normalDrawable = { R.drawable.times_alltimes_normal,
			R.drawable.times_five_normal, R.drawable.times_fiftween_normal,
			R.drawable.times_twenty_normal, R.drawable.times_twentyfive_normal,
			R.drawable.times_tirtyfive_normal, R.drawable.times_forty_normal };
	public int[] selectDrawable = { R.drawable.times_alltimes_selelct,
			R.drawable.times_five_select, R.drawable.times_fiftween_select,
			R.drawable.times_twenty_selectl,
			R.drawable.times_tweentyfive_select,
			R.drawable.times_tirtyfive_select, R.drawable.times_fortty_select };
	public ImageView[] imgViews = { limitTimes, fivetime, fifteenTimes,
			twentyTimes, tweentyfivetimes, tirtyfivetimes, fortyTimes };
	public int times[] = { 0, 5, 15, 20, 25, 35, 40 };
	private CommonTopView topView;

	private void initView() {
		departTv = (TextView) this.findViewById(R.id.departTv);
		departTv.setOnClickListener(this);

		String hospitalJob = tools.getValue(Constants.JOB);
		String defultDepart = tools.getValue(Constants.DEFULT_DEPARTID);
		if (null!=hospitalJob&&hospitalJob.equals("3") && !AbStrUtil.isEmpty(defultDepart)) {//
			// 兼职感控人员在设置了默认科室后可以直接跳过选择部分
			if (!AbStrUtil.isEmpty("defultDepart")) {
				departTv.setText(tools.getValue(Constants.DEFULT_DEPART_NAME));
				departmentName = tools.getValue(Constants.DEFULT_DEPART_NAME);
				departmentId = defultDepart;
			}
		}

		randomTimesLongLay = (LinearLayout) this
				.findViewById(R.id.randomTimesLongLay);
		randomTimesNumLay = (LinearLayout) this
				.findViewById(R.id.randomTimesNumLay);
		randomTimesLongTv = (EditText) this
				.findViewById(R.id.randomTimesLongTv);
		randomTimes = (EditText) this.findViewById(R.id.randomTimes);
		randomTimes.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!AbStrUtil.isEmpty(s.toString())) {
					boolean select = false;
					for (int i = 0; i < times.length; i++) {
						if (Integer.parseInt(s.toString()) == times[i]) {
							setChooseTimesButton(i);
							select = true;
						}
					}
					if (!select) {
						setChooseTimesButton(-1);
					}
				} else {
					if (canNext) {
						setChooseTimesButton(0);
						canNext = false;
					} else {
						setChooseTimesButton(-1);
					}

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		jobTv = (TextView) this.findViewById(R.id.jobTv);
		jobTv.setOnClickListener(this);
		nextButton = (Button) this.findViewById(R.id.nextBtn);
		nextButton.setOnClickListener(this);

		imgViews[1] = (ImageView) this.findViewById(R.id.fiveTimes);
		imgViews[2] = (ImageView) this.findViewById(R.id.fifteen);
		imgViews[3] = (ImageView) this.findViewById(R.id.twentytimes);
		imgViews[4] = (ImageView) this.findViewById(R.id.twenetyfive);
		imgViews[5] = (ImageView) this.findViewById(R.id.thirtyfivetimes);
		imgViews[6] = (ImageView) this.findViewById(R.id.fortytimes);
		imgViews[0] = (ImageView) this.findViewById(R.id.allTimes);
		for (int i = 0; i < selectDrawable.length; i++) {
			final int index = i;
			imgViews[i].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					setChooseTimesButton(index);
					setRudomNum(index);
				}
			});
		}
		topView = (CommonTopView) CreatPlansAcitivity.this
				.findViewById(R.id.topView);
		topView.init(this);

		findDbJobList();
	}

	public void setVisibleWState(int state) {
		switch (state) {
		case 0:
			randomTimesLongLay.setVisibility(View.VISIBLE);
			randomTimesNumLay.setVisibility(View.GONE);
			break;
		case 1:
			randomTimesLongLay.setVisibility(View.GONE);
			randomTimesNumLay.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	private String timesType = "";

	protected void setRudomNum(int index) {
		if (index == 0) {
			randomTimes.setText("");
			setVisibleWState(0);
		} else {
			randomTimes.setText(times[index] + "");
			setVisibleWState(1);
		}

		// if(index==1){
		// addData("1");
		// randomTimes.setText("5");
		// }else if(index==3){
		// addData("3");
		// randomTimes.setText("20");
		// }else if(index==6){
		// addData("4");
		// randomTimes.setText("40");
		// }else if(index==0){
		// //addData("0");
		// randomTimes.setText("不限次数");
		// }else{
		// addData(index+10+"");
		// }

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			AbViewUtil.colseVirtualKeyboard(CreatPlansAcitivity.this);
			break;
		case R.id.tv_top_location:
			finish();
			break;
		case R.id.add_depart:
			Intent it = new Intent(mcontext, AddDepartmentActivity.class);
			startActivityForResult(it, 0x22);

			break;
		// case R.id.fiveTimes:
		// setChooseTimesButton(1);
		// break;
		// case R.id.fifteen:
		// setChooseTimesButton(2);
		// break;
		// case R.id.twentytimes:
		// setChooseTimesButton(3);
		// break;
		// case R.id.twenetyfive:
		// setChooseTimesButton(4);
		// break;
		// case R.id.thirtyfivetimes:
		// setChooseTimesButton(5);
		// break;
		// case R.id.fortytimes:
		// setChooseTimesButton(6);
		// break;
		// case R.id.allTimes:
		// setChooseTimesButton(0);
		// break;
		case R.id.jobTv:
			showJobDialog(defultWorkType, defultJobId);
			break;
		case R.id.nextBtn:
			if(AbStrUtil.isEmpty(departmentId)){
				ToastUtils.showToast(mcontext, "请填写科室！");
				departDialog.show();
				return;
			}

			Intent brodcastIntent = new Intent();
			brodcastIntent
					.setAction(ChooseDepartActivity.CLOSE_DEPART_ACTIVITY);// 关闭前一个页面

			if (AbStrUtil.isEmpty(randomTimes.getText().toString())) {
				if (choosePosition >= 0) {

					this.sendBroadcast(brodcastIntent);
					addData("0");
					nextButton.setEnabled(false);// 防止重复点击
				} else {
					ToastUtils.showToast(mcontext, "请先选择或填写时机数");
				}
			} else {
				int num1 = Integer.parseInt(randomTimes.getText().toString());
				int num = num1 + 10;// 改版后的时机在基础填写时机上加10
				if (num1 == 0) {
					ToastUtils.showToast(mcontext, "请填写大于0的时机数！");
					return;
				}

				this.sendBroadcast(brodcastIntent);// 关闭前一个页面
				addData(num + "");
				nextButton.setEnabled(false);// 防止重复点击
			}
			break;
		case R.id.departTv:
			departDialog.show();
			break;
		default:
			break;
		}
	}

	void startDotasks(String type, TaskVo tv) {
		Intent it = new Intent(mcontext, NewAccomplishTasksActivity.class);
		it.putExtra("type", type);
		it.putExtra("time", missonTime);
		it.putExtra(CreatPlansAcitivity.departName, departmentName);
		it.putExtra(CreatPlansAcitivity.departId, departmentId);
		it.putExtra("data", tv);
		startActivity(it);
		finish();
	}

	private void checkDbisCreated() {
		try {
			if (null != DataBaseHelper
					.getDbUtilsInstance(mcontext).findAll(TaskVo.class)) {
				newTaskList = DataBaseHelper
						.getDbUtilsInstance(mcontext).findAll(TaskVo.class);
				Log.i("1111133", newTaskList.size() + "");
			} else {
				DataBaseHelper.getDbUtilsInstance(mcontext)
						.saveAll(newTaskList);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	boolean canNext = false;
	int choosePosition = -1;
	private boolean isAfter;

	public void setChooseTimesButton(int position) {
		choosePosition = position;
		for (int i = 0; i < selectDrawable.length; i++) {
			if (position == i) {
				if (position == 0) {
					canNext = true;
				}
				imgViews[i].setImageResource(selectDrawable[i]);
			} else {
				imgViews[i].setImageResource(normalDrawable[i]);
			}
		}

	}

	// 以前有预设任务接受数据
	private void getData() {
		departmentName = getIntent().getStringExtra(departName);
		departmentId = getIntent().getStringExtra(departId);
		creatTime = getIntent().getStringExtra("time");

		isAfter = getIntent().getBooleanExtra("isAfter", false);
		departmentNames = getIntent().getStringArrayExtra(departNames);
		departmentIds = getIntent().getStringArrayExtra(departIds);

	}

	public void addData(String type) {
		// if (isAfter) {
		// if (null != departmentIds) {
		// for (int i = 0; i < departmentIds.length; i++) {
		// createData(type, departmentIds[i], departmentNames[i]);
		// DebugUtil.debug("addddae", departmentIds[i] + "  "
		// + departmentNames[i]);
		//
		// }
		// Intent brodcastIntent = new Intent();
		// brodcastIntent.setAction(CalendarMainActivity.UPDATA_ACTION);
		// CreatPlansAcitivity.this.sendBroadcast(brodcastIntent);
		// Toast.makeText(mcontext,
		// "您已成功预设" + departmentIds.length + "个督导", 2000).show();
		// finish();
		// }
		// } else {
		createData2(type, departmentId, departmentName);

		// }

	}

	private TaskVo createData(String type, String id, String name) {
		List<planListDb> pdblist = new ArrayList<planListDb>();
		TaskVo pdb = new TaskVo();
		pdb.setDepartmentName(name);
		pdb.setTask_type(type);
		pdb.setType("1");
		pdb.setMobile(tools.getValue(Constants.MOBILE));
		pdb.setDepartment(id);
		if (getIntent().hasExtra("isWho")) {
			pdb.setWho(true);
		} else {
			pdb.setWho(false);
		}
		pdb.setDefaltWorkType(defultWorkType);
		pdb.setDefaltWorkTypeName(defultWorkTypeName);
		pdb.setDefaltJobId(defultJobId);
		pdb.setDefaltJobName(defultJobName);
		pdb.setStatus(2);// 0 代表已同步，1代表未同步，2代表未完成,3代表未开始
		String jsonStr = gson.toJson(pdblist.toString());
		pdb.setFiveTasks(jsonStr.toString());
		Calendar calendar = null;
		calendar = new GregorianCalendar();// 子类实例化
		// pdb.setFiveTasks(pdblist);

		String time = "";

		time = creatTime + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + ":"
				+ calendar.get(Calendar.SECOND);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		missonTime = sdf.format(date);
		pdb.setMission_time(missonTime + "");
		try {
			DataBaseHelper.getDbUtilsInstance(mcontext).save(pdb);
			return pdb;
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	private TaskVo createData2(String type, String id, String name) {
		List<planListDb> pdblist = new ArrayList<planListDb>();
		TaskVo pdb = new TaskVo();
		pdb.setDepartmentName(name);
		pdb.setTask_type(type);
		pdb.setType("1");
		pdb.setMobile(tools.getValue(Constants.MOBILE));
		pdb.setDepartment(id);
		if (getIntent().hasExtra("isWho")) {
			pdb.setWho(true);
		} else {
			pdb.setWho(false);
		}

		if (type.equals("0")) {
			String textLong = randomTimesLongTv.getText().toString();
			if (!AbStrUtil.isEmpty(textLong)) {
				int minite = Integer.parseInt(textLong);
				pdb.setMinute(minite);
			} else {
				pdb.setMinute(30);
			}
		}
		pdb.setHours(111111);
		pdb.setDefaltWorkType(defultWorkType);
		pdb.setDefaltWorkTypeName(defultWorkTypeName);
		pdb.setDefaltJobId(defultJobId);
		pdb.setDefaltJobName(defultJobName);
		if (isShowFram) {
			pdb.setTranning(true);
		}
		pdb.setStatus(2);// 0 代表已同步，1代表未同步，2代表未完成,3代表未开始
		String jsonStr = gson.toJson(pdblist.toString());
		pdb.setFiveTasks(jsonStr.toString());
		Calendar calendar = null;
		calendar = new GregorianCalendar();// 子类实例化
		// pdb.setFiveTasks(pdblist);

		String time = "";

		time = creatTime + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + ":"
				+ calendar.get(Calendar.SECOND);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		missonTime = sdf.format(date);
		pdb.setMission_time(missonTime + "");
		try {
			DataBaseHelper.getDbUtilsInstance(mcontext).save(pdb);
			newTaskList = DataBaseHelper
					.getDbUtilsInstance(mcontext).findAll(TaskVo.class);

			int taskId = newTaskList.get(newTaskList.size() - 1).getDbid();

			// 创建后要发广播通知已更新
			Intent brodcastIntent = new Intent();
			brodcastIntent.setAction(CalendarMainActivity.UPDATA_ACTION);
			CreatPlansAcitivity.this.sendBroadcast(brodcastIntent);
			pdb.setDbid(taskId);
			startDotasks(type, pdb);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String defultWorkType = "";
	private String defultWorkTypeName = "";
	private String defultJobName = "";
	private String defultJobId = "";

	public void showJobDialog(String workId, String jobId) {
		JobDialog dialog = new JobDialog(mcontext, workId, jobId, jobTypelist,
				jobList, new ChooseItem() {
					@Override
					public void getJobChoosePosition(int positon1, int position2) {
						if (positon1 >= 0) {
							defultWorkType = jobTypelist.get(positon1).getId();
							defultWorkTypeName = jobTypelist.get(positon1)
									.getName();
						} else {
							defultWorkType = "";
							defultWorkTypeName = "";
						}

						if (position2 >= 0) {
							defultJobName = jobList.get(position2).getName();
							defultJobId = jobList.get(position2).getId();

						} else {
							defultJobName = "";
							defultJobId = "";
						}
						jobTv.setText(defultWorkTypeName + " " + defultJobName);
					}

					@Override
					public void getJobChoosePosition(String name, int positon1,
							int position2) {
						// TODO Auto-generated method stub

					}
				});
		dialog.show();
		dialog.setTitleTv("选择默认调查岗位");
	}

	List<JobListVo> jobList = new ArrayList<JobListVo>();
	String jobs[];
	private List<JobListVo> jobTypelist;

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

			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getCacheData() {
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent redata) {
		super.onActivityResult(requestCode, resultCode, redata);
		if (resultCode == 0x22 && null != redata) {
			ChildsVo dv = (ChildsVo) redata.getSerializableExtra("data");
			String rooId = dv.getParent();
			for (DepartLevelsVo dlv : departlist) {
				if (rooId.equals("1") && dlv.getRoot().getId().equals("0")) {
					// dlv.setChooseNum(dlv.getChooseNum() + 1);
					dlv.getChilds().add(0, dv);
				} else if (dlv.getRoot().getId().equals(rooId)) {
					// dlv.setChooseNum(dlv.getChooseNum() + 1);
					dlv.getChilds().add(0, dv);
				}
			}
			departDialog.setdata(departlist);

		} else if (resultCode == 0x23 && null != redata) {
			ChildsVo dv2 = (ChildsVo) redata.getSerializableExtra("data");
			departTv.setText(dv2.getName());
			departmentId = dv2.getId();
			departmentName = dv2.getName();
		}
	}
}