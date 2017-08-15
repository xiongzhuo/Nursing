package com.example.calendar.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.TaskLisAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.consumption.ConsumptionFormActivity;
import com.deya.hospital.form.FormListActivity;
import com.deya.hospital.form.handantisepsis.HandDisinfectionPrivewActivity;
import com.deya.hospital.form.xy.XyFormListActivity;
import com.deya.hospital.supervisor.CompletlyWHOTaskActivity;
import com.deya.hospital.supervisor.CreatPlansAcitivity;
import com.deya.hospital.supervisor.SupervisionActivity;
import com.deya.hospital.task.Tasker;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.ComomDialog;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.dbdata.TaskNum;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.vo.dbdata.planListDb;
import com.deya.hospital.workspace.AddTaskPopWindow;
import com.deya.hospital.workspace.AddTaskPopWindow.OnPopuClick;
import com.deya.hospital.workspace.TaskUtils;
import com.example.calendar.AddAfterTaskActivity;
import com.example.calendar.CalendarPagerAdapter;
import com.example.calendar.CollapseCalendarView;
import com.example.calendar.CollapseCalendarView.OnDateSelect;
import com.example.calendar.manager.CalendarManager;
import com.example.calendar.manager.CalendarManager.OnStateChange;
import com.example.calendar.manager.CalendarManager.State;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CalendarStoreActivity extends BaseActivity implements
		OnStateChange, OnPageChangeListener, OnDateSelect, View.OnClickListener {

	public static final String UPDATA_ACTION = "updateList";
	public static final String UPLOAD_ACTION = "updateList";
	public static final String ISCLOSE_TIPS = "updatestate";// 是否关闭软文
	public static final String UPLOAD_TASK_SUCESS = "upload_task_sucess";
	public static final String DELET_TASK_SUCESS = "delet_task_sucess";
	public static final String SYC_SERVICE = "syc_service";

	public static final int DELETE_SUCESS = 0x20060;
	public static final int DELETE_FAIL = 0x20061;// 是否关闭软文
	private TextView tvDate;
	private ImageView backToToday;

	private LayoutInflater inflater;
	TextView staticsImg;
	private State mState;
	private CalendarManager manager;
	private ViewPagerHeightWrapContent content_viewpager;
	private CalendarPagerAdapter mPagerAdapter;

	private Gson gson;
	private Tools tools;
	FrameLayout frame_layout;

	// 软文
	private MyBrodcastReceiver brodcast;
	private ListView planLv;
	public static LocalDate currentday;
	// 上次的月份
	private LinearLayout creatBtn;
	boolean isNeedRequst = true;
	TaskLisAdapter adapter;
	ImageView switchBtn;
	ImageView img_add;
	//RelativeLayout empertyView;
	Context mcontext;
//	private TextView text_add;
	private LinearLayout lay_add0, lay_add, lay_add1;
	private boolean isAfter;// 判断是否为预设任务
	private LocalDate localToday;
	RelativeLayout rl_back;
	Button addTaskBtn;
//	View headView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_calendar);

		mcontext = this;
		gson = new Gson();
		this.inflater = LayoutInflater.from(mcontext);

		initCalendarManager();
		initMyHandler();
		initView();
		initData();
		setListener();
		initListView();
		re_page();
		calendarOperationHint();
	}

	/** 新手指引 */
	private void calendarOperationHint() {
		tools.putValue("calendar_hint", 1);
	}

	private void re_page() {
		int initPosition = Integer.MAX_VALUE / 2;
		mState = State.WEEK;
		mPagerAdapter = new CalendarPagerAdapter(inflater, manager);
		content_viewpager.setOnPageChangeListener(this);
		content_viewpager.setAdapter(mPagerAdapter);
		manager.setRelativePosition(initPosition);
		manager.setStateChangedListener(this);
		content_viewpager.setCurrentItem(initPosition);
		mPagerAdapter.notifyDataSetChanged();
		content_viewpager.post(new Runnable() {
			@Override
			public void run() {
				backToToday();
			}
		});
	}

	LinearLayout switchView;

	private void initView() {
		tools = new Tools(mcontext, Constants.AC);
		rl_back = (RelativeLayout) this.findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		CalendarLinearLayoutWithListView Layout = (CalendarLinearLayoutWithListView) this
				.findViewById(R.id.calendarView);
		Layout.initChildViews(R.id.content_viewpager, R.id.planlist);
		tvDate = (TextView) findViewById(R.id.tv_date);
		backToToday = (ImageView) this.findViewById(R.id.backToToday);
		content_viewpager = (ViewPagerHeightWrapContent) findViewById(R.id.content_viewpager);

		creatBtn = (LinearLayout) this.findViewById(R.id.creatbtn);
		// creatBtn.setOnClickListener(this);

		switchView = (LinearLayout) this.findViewById(R.id.switchView);
//		headView = inflater.inflate(R.layout.calendarlistview_headview, null);
//		lay_add = (LinearLayout) headView.findViewById(R.id.lay_add);
//		lay_add.setOnClickListener(this);
//		text_add = (TextView) headView.findViewById(R.id.text_add);
//		// text_add_img = (TextView) findViewById(R.id.text_add_img);
//		img_add = (ImageView) headView.findViewById(R.id.img_add);
//
//		lay_add0 = (LinearLayout) headView.findViewById(R.id.lay_add0);
//		lay_add0.setOnClickListener(this);
//
//		lay_add1 = (LinearLayout) headView.findViewById(R.id.lay_add1);
//		lay_add1.setOnClickListener(this);

		switchBtn = (ImageView) this.findViewById(R.id.switchBtn);
		switchBtn.setOnClickListener(this);
		isShowALLTask = true;
		switchBtn.setImageResource(R.drawable.dynamic_but1);
		staticsImg = (TextView) this.findViewById(R.id.staticsImg);
		staticsImg.setVisibility(View.GONE);
		addTaskBtn=findView(R.id.addTaskBtn);
		setLis(addTaskBtn,this);

	}

	private void initData() {
		setCurrentData();
		registerBroadcast();
		initViewPagerData();
	}

	private void setListener() {
		backToToday.setOnClickListener(this);
	}

	public static boolean isShowALLTask = true;

	private void initViewPagerData() {
		int initPosition = Integer.MAX_VALUE / 2;
		mState = State.WEEK;
		mPagerAdapter = new CalendarPagerAdapter(inflater, manager);
		content_viewpager.setOnPageChangeListener(this);
		content_viewpager.setAdapter(mPagerAdapter);
		manager.setRelativePosition(initPosition);
		content_viewpager.setCurrentItem(initPosition);
		mPagerAdapter.notifyDataSetChanged();

		content_viewpager.post(new Runnable() {
			@Override
			public void run() {
				Log.i("pagechange", content_viewpager + "====change");
				// content_viewpager.requestLayout();
				onPageSelected(content_viewpager.getCurrentItem());
			}
		});
	}

	private void initCalendarManager() {
		manager = new CalendarManager(LocalDate.now(),
				State.WEEK, LocalDate.now().minusYears(2),
				LocalDate.now().plusYears(2));
		localToday = LocalDate.now();
		manager.setOnDateSelect(this);
		manager.setRelativeDate(localToday);
	}

	@Override
	public void onDestroy() {
		if (null != brodcast) {
			mcontext.unregisterReceiver(brodcast);
		}
		gson = null;
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		isAfter = localToday.compareTo(getCurrentSelectedDate()) < 0;
		switch (id) {
		case R.id.rl_back:
			finish();
			break;
		case R.id.backToToday:
			if (currentday.toString().equals(localToday + "")) {
				return;
			}
			backToToday();
			break;

			case R.id.addTaskBtn:
				Intent intent=new Intent(mcontext,AddAfterTaskActivity.class);
				intent.putExtra("time",currentday);
				startActivity(intent);
				break;
		case R.id.lay_add:
			new AddTaskPopWindow(mcontext, creatBtn, new OnPopuClick() {

				@Override
				public void supervision() {
					addTask(2, false);
				}

				@Override
				public void handhygiene() {
					addTask(1, false);
				}

				@Override
				public void cancel() {
				}

				@Override
				public void reviewForm() {
					addTask(3, false);
				}

				@Override
				public void DowhoTask() {
					addTask(1, true);
				}

				@Override
				public void doToatalTask() {
					Intent it = new Intent(mcontext,
							CompletlyWHOTaskActivity.class);
					it.putExtra("type", "3");
					startActivity(it);
				}

				@Override
				public void onConsumption() {
					addTask(4, false);
				}

				@Override
				public void surgicalFrom() {
					addTask(5, false);
				}

				@Override
				public void xiangyaFrom() {
					addTask(6, false);
				}
			}).show();
			;

			break;
		case R.id.switchBtn:
			setTaskShowType();
			break;
		default:
			break;
		}
	}

	public void addTask(int type, boolean iswho) {
		String time = getCurrentSelectedDate() + "";
		Intent it = new Intent();
		it.putExtra("time", time + "");
		if (type != 4) {
			it.putExtra("type", type + "");
		}

		if (iswho) {
			it.putExtra("isWho", "1");
		}

		if(isAfter){
			StartActivity(AddAfterTaskActivity.class);
			return;
		}
		it.putExtra("isAfter", false);
		switch (type) {
		case 1:
			it.setClass(mcontext, CreatPlansAcitivity.class);
			break;
		case 2:
			it.setClass(mcontext, SupervisionActivity.class);
			break;
		case 4:
			it.setClass(mcontext, ConsumptionFormActivity.class);
			TaskVo tv = new TaskVo();
			tv.setMission_time(localToday + "");
			tv.setStatus(2);
			it.putExtra("data", tv);
			break;
		case 3:
			it.setClass(mcontext, FormListActivity.class);
			it.putExtra("time", localToday + "");
			break;
		case 5:
			it.setClass(mcontext, HandDisinfectionPrivewActivity.class);
			break;

		case 6:
			it.setClass(mcontext, XyFormListActivity.class);

			it.putExtra("time", localToday + "");
			break;

		default:
			break;
		}
		startActivity(it);

	}

	/**
	 * 在表提出显示当前日期，并判断是否是预设任务
	 */
	private void changeTimeText() {

		Log.i("selecteday",
				currentday + "-----------" + manager.getSelectedDay());
		currentday = manager.getSelectedDay();
		tvDate.setText(currentday.getYear() + "." + currentday.getMonthOfYear()
				+ "." + currentday.getDayOfMonth());

		isAfter = currentday.isAfter(localToday);
//		if (isAfter) {
//			text_add.setText(R.string.text_preinstall1);
//			img_add.setImageResource(R.drawable.preset_supervisor_selector);
//		} else {
//			text_add.setText(R.string.text_add1);
//			img_add.setImageResource(R.drawable.add_supervisor_selector);
//		}

	}

	private void setTaskShowType() {
		if (isShowALLTask) {
			isShowALLTask = false;
			switchBtn.setImageResource(R.drawable.dynamic_but2);
			setCurrentData();
			refreshViewPager(false);
		} else {
			isShowALLTask = true;
			switchBtn.setImageResource(R.drawable.dynamic_but1);
			setCurrentData();
			refreshViewPager(false);
		}

	}

	boolean toToday = false;

	/**
	 * 回到今天的日期
	 */
	public void backToToday() {

		if (null != manager) {
			toToday = true;
			LocalDate today = LocalDate.now();
			manager.selectDay(today);
			tvDate.setText(today.getYear() + "." + today.getMonthOfYear() + "."
					+ today.getDayOfMonth());
			currentday = today;
			;
			isAfter = false;
			//text_add.setText(R.string.text_add1);
			//img_add.setImageResource(R.drawable.add_supervisor_selector);
			manager.setRelativeDate(today);
			manager.setRelativePosition(content_viewpager.getCurrentItem());
			this.onDateSelected(today);
		}
	}

	public static List<TaskVo> monthToatalList = new ArrayList<TaskVo>();
	public List<TaskNum> monthNumList = new ArrayList<TaskNum>();
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	public static HashMap<String, Integer> mMonthTaskNums = new HashMap<String, Integer>();
	public static HashMap<String, Integer> unDoTastNums = new HashMap<String, Integer>();

	private void setMonthkData(List<TaskVo> list) {
		monthToatalList.clear();
		monthToatalList.addAll(list);
		mMonthTaskNums.clear();
		for (int i = 0; i < monthToatalList.size(); i++) {
			TaskVo taskVo = monthToatalList.get(i);
			String str = taskVo.getMission_time();// 2015

			try {
				str = df.parse(str).toString();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Integer num = mMonthTaskNums.get(str);
			if (null != num) {
				mMonthTaskNums.put(str, num + 1);
			} else {
				mMonthTaskNums.put(str, 1);
			}
		}
		// refreshViewPager(false);
	}

	/**
	 * 获取当前选中的日期
	 * 
	 * @return 返回当前选中日期
	 */
	public boolean isCurrentMonth = false;// 判断是否在当前月

	public LocalDate getCurrentSelectedDate() {
		View view = content_viewpager.getCurrentChild();
		if (view instanceof CollapseCalendarView) {
			currentday = ((CollapseCalendarView) view).getSelectedDate();
			Log.i("selecteday",
					currentday + "=========" + manager.getSelectedDay());
			return ((CollapseCalendarView) view).getSelectedDate();
		}
		return null;
	}

	public LocalDate getNextSelectedDate() {
		View view = content_viewpager.getLeftChild();
		Log.i("444444444444444444", content_viewpager.getCurrentId() + "");
		if (view instanceof CollapseCalendarView) {
			return ((CollapseCalendarView) view).getMonthMaxDay();
		}
		return null;
	}

	public LocalDate getMaxDate() {
		View view = content_viewpager.getCurrentChild();
		if (view instanceof CollapseCalendarView) {
			return ((CollapseCalendarView) view).getMonthMaxDay();
		}
		return null;
	}

	public LocalDate getMinDate() {
		View view = content_viewpager.getCurrentChild();
		if (view instanceof CollapseCalendarView) {
			return ((CollapseCalendarView) view).getMonthMinDay();
		}
		return null;
	}

	/**
	 * 日期切换了，需要重新加载任务数据
	 */
	LocalDate date;
	LocalDate date2;
	public boolean needToGetLoacalData = true;

	public void onDateChanged2() {
		date = this.getCurrentSelectedDate();
		refreshUi2();
		changeTimeText();
	}

	/**
	 * 刷新日历视图
	 * 
	 * @param isAll
	 *            true：更新viewpager中所有子日历；false：仅更新当前可见日历
	 */
	private void refreshViewPager(boolean isAll) {
		if (isAll) {
			resetViewPager();
		} else {
			final CollapseCalendarView view = (CollapseCalendarView) content_viewpager
					.getCurrentChild();
			if (null != view)
				new Runnable(){
					@Override
					public void run() {
						view.reset();
					}
				};

		}
	}
	private void resetViewPager() {
		for (int i = 0; i < content_viewpager.getChildCount(); ++i) {
			CollapseCalendarView view = (CollapseCalendarView) content_viewpager
					.getChildAt(i);
			view.reset();
		}
		mPagerAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		DebugUtil.debug("onPageScrollStateChanged", "arg0>>" + arg0);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		Log.i("55555555555", arg0 + "-----------" + arg1 + arg2);
	}

	private boolean isInit = true;

	@Override
	public void onPageSelected(int arg0) {

		// 翻页
		if (null != tvDate) {
			// tvDate.setText(manager.getHeaderText(arg0));
		}
		if (null != manager && null != content_viewpager
				&& content_viewpager.getChildCount() > 0) {
			// if(isInit||manager.selectDay(getCurrentSelectedDate())){
			// isInit=false;

			DebugUtil.debug("pagechange", "change p");
			manager.selectDay(getCurrentSelectedDate());
			onDateChanged2();
			// }

		}

		DebugUtil.debug("datachange", "page end>>");
	}

	/** 日历点击事件 */
	@Override
	public void onDateSelected(LocalDate date) {
		date = manager.getSelectedDay();// 重置当前选中时间，以前就是少了这函数，坑
		if (toToday) {
			// date = this.getCurrentSelectedDate();
			DebugUtil.debug("onDateSelected_today", " end>>" + date);
			refreshUi2();
			refreshViewPager(true);
		} else {
			DebugUtil.debug("datachange", "s>>");
			setCurrentData();
			resetViewPager();
		}
		changeTimeText();
		toToday = false;

	}

	// 注册广播
	private void registerBroadcast() {
		brodcast = new MyBrodcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(SYC_SERVICE)) {
					getTaskList();
				} else {
					setCurrentData();
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SYC_SERVICE);
		intentFilter.addAction(UPDATA_ACTION);
		intentFilter.addAction(UPLOAD_ACTION);
		intentFilter.addAction(UPLOAD_TASK_SUCESS);
		mcontext.registerReceiver(brodcast, intentFilter);
	}

	private void initListView() {

		planLv = (ListView) findViewById(R.id.planlist);
		//planLv.addHeaderView(headView);
		adapter = new TaskLisAdapter(mcontext, dayTaskLi);
		planLv.setAdapter(adapter);

		planLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					return;
				}
				TaskVo tv = dayTaskLi.get(position);
				TaskUtils.onStaractivity(CalendarStoreActivity.this, tv,
						dayTaskLi);
			}
		});
		planLv.setOnItemLongClickListener(new OnItemLongClickListener() {
			Dialog deletDialog;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				deletDialog = new ComomDialog(mcontext, "确认删除此任务？",
						R.style.SelectDialog, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (position == 0) {
									return;
								}
								doDeleteTask(position - 1);
								deletDialog.dismiss();
							}
						});
				deletDialog.show();
				return true;
			}
		});
	//	empertyView = (RelativeLayout) headView.findViewById(R.id.empertyView);

	}

	public String getFeedback(TaskVo tv) {
		JSONObject job = new JSONObject();
		try {
			job.put("remark", tv.getRemark());
			job.put("is_training", tv.getIs_training());
			job.put("training_recycle", tv.getTraining_recycle());
			job.put("equip_examine", tv.getEquip_examine());
			job.put("feedback_obj", tv.getFeedback_obj());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return job.toString();
	}

	public String getHtml(TaskVo tv) {
		List<planListDb> pdblist = gson.fromJson(tv.getFiveTasks().toString(),
				new TypeToken<List<planListDb>>() {
				}.getType());

		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		String str = gson.toJson(pdblist);
		JSONArray jarr2 = null;

		try {
			jarr2 = new JSONArray(str);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("fiveTasks", jarr2);
			job.put("department", tv.getDepartmentName());
			String jobStr = AbStrUtil
					.parseStrToMd5L32(AbStrUtil.parseStrToMd5L32(job.toString())
							+ "task/saveFiveTaskInfo");
			json.put("token", jobStr);
			json.put("msg_content", job.toString());
			Log.i("11111111sendTask", job.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return job.toString();
	}

	void doDeleteTask(int index) {
		if (index >= dayTaskLi.size()) {
			return;
		}
		TaskVo tv = dayTaskLi.get(index);
		if (tv.getStatus() == 1) {
			ToastUtils.showToast(mcontext, "正在同步的任务暂时无法删除");
			return;
		} else if (tv.getStatus() != 0) {
			Tasker.deleteTask(tv);
			Intent intent = new Intent();
			intent.setAction(DELET_TASK_SUCESS);
			intent.putExtra("dbid", tv.getDbid());
			sendBroadcast(intent);
			setCurrentData();
			ToastUtils.showToast(mcontext, "删除成功！");
		} else {
			for (TaskVo vo : taskList) {
				if (vo.getTask_id() == tv.getTask_id()) {
					Log.i("taskId", tv.getTask_id() + "");
					deletTasks(vo.getTask_id(), tv.getType());
					break;
				}

			}

		}
	}

	public int dp2Px(int dp) {
		final float scale = mcontext.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}

	// 获取服务器任务数据
	// 获取同步任务列表

	List<TaskVo> taskList = new ArrayList<TaskVo>();
	private String nextMonthMaxDay;
	private String currentMothMinDay;

	public void getTaskList() {
		taskList.clear();
		String startTime = currentMothMinDay;
		String endTime = nextMonthMaxDay + "";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date time1 = df.parse(startTime);
			Date time2 = df.parse(endTime);
			if (time1.getDate() > time2.getDate()) {
				time2.setMonth(time2.getMonth() + 1);
			}
		} catch (ParseException e) {
			e.printStackTrace();

		}
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("start_date", startTime);
			job.put("end_date", endTime);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler, mcontext,
				TASKLIST_SUCESS, TASKLIST_FAILE, job, "task/syncTaskInfos");

	}

	public void getTaskList_v2() {
		taskList.clear();
		String startTime = currentMothMinDay;
		String endTime = nextMonthMaxDay + "";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date time1 = df.parse(startTime);
			Date time2 = df.parse(endTime);
			if (time1.getDate() > time2.getDate()) {
				time2.setMonth(time2.getMonth() + 1);
			}
		} catch (ParseException e) {
			e.printStackTrace();

		}
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("start_date", startTime);
			job.put("end_date", endTime);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler, mcontext,
				TASKLIST_SUCESS, TASKLIST_FAILE, job, "task/syncTaskInfos");

	}

	public int uploadStatus = 0;
	public static final int ADD_PRITRUE_CODE = 9009;
	// 压缩图片的msg的what
	private static final int TASKLIST_SUCESS = 0x20010;
	private static final int TASKLIST_FAILE = 0x20011;
	private static final int SEND_TASK_SUCESS = 0x20012;
	private static final int SEND_TASK_FIAL = 0x20013;
	protected static final int UPLOADFILE_SUCESS = 0;
	public static final int COMPRESS_IMAGE = 0x17;

	public static final int REFRESHVIEWPAGER = 0x30;
	public static final int REFRESHVIEWPAGER_NOTIFY = 0x31;
	public static final int REFRESHVIEWPAGER_NOTIFY2 = 0x32;

	public static final int REFRESHVIEWPAGER_TASKADAPTER = 0X34;
	protected static final int REFRESHVIEWPAGER_CUREENTDATA = 0x35;
	protected static final int REFRESH_CALENDAR = 0x36;

	int picIndex = 0;
	private MyHandler myHandler;

	private void initMyHandler() {
		myHandler = new MyHandler(this) {
			@Override
			public void handleMessage(Message msg) {
				Activity activity = myHandler.mactivity.get();
				if (null != activity) {
					switch (msg.what) {
					case TASKLIST_SUCESS:
						if (null != msg && null != msg.obj) {
							Log.i("gettask", msg.obj + "");
							try {
								setTaskList(new JSONObject(msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
						break;
					case TASKLIST_FAILE:
						if (null != mcontext) {
							ToastUtils.showToast(mcontext, "亲！您的网络不给力哦！");
						}
						break;

					case DELETE_SUCESS:
						if (null != msg && null != msg.obj) {
							Log.i("1111", msg.obj + "");
							try {

								setDeletRes(new JSONObject(msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
						break;
					case REFRESHVIEWPAGER_NOTIFY:
						adapter.notifyDataSetChanged();
						mPagerAdapter.notifyDataSetChanged();
						// refreshViewPager(false);
						break;
					case REFRESHVIEWPAGER_TASKADAPTER:
						adapter.notifyDataSetChanged();
						break;
					case REFRESHVIEWPAGER:
						setCalenderText();// ok
						setMonthkData(taskList);
						adapter.notifyDataSetChanged();
						refreshViewPager(false);
						break;
					case REFRESHVIEWPAGER_CUREENTDATA:
						setCurrentData();
						break;
					default:
						break;
					}
				}
			}
		};
	}

	protected void setTaskList(JSONObject jsonObject) {
		Log.i("111111111tasklist", jsonObject + "");
		if (jsonObject.optString("result_id").equals("0")) {
			JSONArray jarr = jsonObject.optJSONArray("syncTaskInfos");
			if(null==jarr){
				return;
			}
			final List<TaskVo> list = TaskUtils.gson.fromJson(jarr.toString(),
					new TypeToken<List<TaskVo>>() {
					}.getType());
			new Thread(new Runnable() {
				public void run() {
					Tasker.syncNetworkTask(list);
					myHandler.sendEmptyMessage(REFRESHVIEWPAGER_CUREENTDATA);
				}
			}).start();
			;

		}

	}

	private void setCalenderText() {
		unDoTastNums.clear();
		for (TaskVo taskVo : taskList) {
			String str = taskVo.getMission_time();// 2015
			try {
				str = df.parse(str).toString();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			int statu = taskVo.getStatus();
			Integer num2 = unDoTastNums.get(str);
			if (statu == 2 || statu == 3) {
				unDoTastNums.put(str, null != num2 ? num2 + 1 : 1);
			} else if (null == num2) {
				unDoTastNums.put(str, 0);
			}
			Log.i("444444444", str + "=============" + unDoTastNums.get(str)
					+ "");
		}
	}

	LocalDate requestDay;// 当前请求的日期

	public void refreshUi_v2() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				LocalDate startMonthday = currentday.minusMonths(2);
				LocalDate endMonthday = currentday.plusMonths(2);

				currentMothMinDay = startMonthday.getYear() + "-"
						+ startMonthday.getMonthOfYear() + "-"
						+ startMonthday.dayOfMonth().getMinimumValue();
				// nextMonthMaxDay=getNextSelectedDate()+"";

				nextMonthMaxDay = endMonthday.getYear() + "-"
						+ (endMonthday.getMonthOfYear()) + "-"
						+ endMonthday.dayOfMonth().getMinimumValue();

				if (null != requestDay) {
					// 判断本次请求日期是否在上次请求的日期区间之内
					LocalDate requestDaystartMonthday = requestDay
							.minusMonths(2);
					LocalDate requestDayendMonthday = requestDay.plusMonths(2);
					if (compareDate(requestDaystartMonthday,
							requestDayendMonthday)) {
						requestDay = currentday;// 记录请求游标
						getTaskList();
					} else {
						setCurrentData();
					}
				} else {
					requestDay = currentday;
					getTaskList();
				}
			}
		}).start();

	}

	public void refreshUi() {

		LocalDate startMonthday = currentday.minusMonths(2);
		LocalDate endMonthday = currentday.plusMonths(2);

		currentMothMinDay = startMonthday.getYear() + "-"
				+ startMonthday.getMonthOfYear() + "-"
				+ startMonthday.dayOfMonth().getMinimumValue();
		// nextMonthMaxDay=getNextSelectedDate()+"";

		nextMonthMaxDay = endMonthday.getYear() + "-"
				+ (endMonthday.getMonthOfYear()) + "-"
				+ endMonthday.dayOfMonth().getMinimumValue();

		if (null != requestDay) {
			// 判断本次请求日期是否在上次请求的日期区间之内
			LocalDate requestDaystartMonthday = requestDay.minusMonths(2);
			LocalDate requestDayendMonthday = requestDay.plusMonths(2);
			if (compareDate(requestDaystartMonthday, requestDayendMonthday)) {
				requestDay = currentday;// 记录请求游标
				getTaskList();
			} else {
				setCurrentData();
				// setCurrentData_v2();
			}
		} else {
			requestDay = currentday;
			getTaskList();
		}
	}

	public void refreshUi2() {
		LocalDate startMonthday = currentday.minusMonths(2);
		LocalDate endMonthday = currentday.plusMonths(2);

		currentMothMinDay = startMonthday.getYear() + "-"
				+ startMonthday.getMonthOfYear() + "-"
				+ startMonthday.dayOfMonth().getMinimumValue();
		// nextMonthMaxDay=getNextSelectedDate()+"";

		nextMonthMaxDay = endMonthday.getYear() + "-"
				+ (endMonthday.getMonthOfYear()) + "-"
				+ endMonthday.dayOfMonth().getMinimumValue();

		if (null != requestDay) {
			// 判断本次请求日期是否在上次请求的日期区间之内
			LocalDate requestDaystartMonthday = requestDay.minusMonths(2);
			LocalDate requestDayendMonthday = requestDay.plusMonths(2);
			if (compareDate(requestDaystartMonthday, requestDayendMonthday)) {
				requestDay = currentday;// 记录请求游标
				getTaskList();
			} else {
				setCurrentData();
				// setCurrentData_v2();
			}
		} else {
			requestDay = currentday;
			getTaskList();
		}
	}

	public boolean compareDate(LocalDate date, LocalDate date2) {
		return currentday.compareTo(date) < 0
				|| currentday.compareTo(date2) > 0;
	}

	// 设置当前数据，服务器数据+本地数据
	List<TaskVo> dayTaskLi = new ArrayList<TaskVo>();

	private void setCurrentData() {
		taskList.clear();
		taskList.addAll(isShowALLTask ? Tasker.getAllLocalTask() : Tasker
				.getNotInserviceTask());
		dayTaskLi.clear();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (TaskVo tv : taskList) {
			String datatime = tv.getMission_time();
			Date time1;
			try {
				time1 = df.parse(datatime);
				Date time2 = df.parse(getCurrentSelectedDate() + "");
				if (time1.getDate() == time2.getDate()
						&& time1.getMonth() == time2.getMonth()) {
					dayTaskLi.add(tv);

				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
//		if (dayTaskLi.size() <= 0) {
//			empertyView.setVisibility(View.VISIBLE);
//		} else {
//			empertyView.setVisibility(View.GONE);
//		}

		setMonthkData(taskList);
		setCalenderText();
		if(null!=adapter){
			adapter.notifyDataSetChanged();
			refreshViewPager(false);
		}

	}

	LocalDate ld = null;

	/**
	 * 如果出现重复提交，那么可以通过对比上传前后taskId 进行对比
	 * 只有在任务开始后才去给taskId赋值，否则当任务上传成功时无法校验，删除本地任务
	 */

	/**
	 * 删除已提交任务请求
	 */
	int deletTaskId = 0;

	public void deletTasks(int taskId, String type) {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			deletTaskId = taskId;
			job.put("task_id", taskId + "");
			job.put("type", type);// 手卫生的1，督导本2
		} catch (JSONException e) {
			e.printStackTrace();
		}

		MainBizImpl.getInstance().onComomRequest(myHandler, mcontext,
				DELETE_SUCESS, DELETE_FAIL, job, "task/deleteTaskById");
	}

	// 删除任务处理
	protected void setDeletRes(JSONObject jsonObject) {
		if (jsonObject.optString("result_id").equals("0")) {
			int dbid = -1;
			for (TaskVo vo : taskList) {
				if (vo.getTask_id() == deletTaskId) {
						Tasker.deleteTask(vo);
						dbid = vo.getDbid();

					break;
				}

			}
			setCurrentData();
			ToastUtils.showToast(mcontext, "任务删除成功");
			Intent intent = new Intent();
			if (dbid != -1) {
				intent.putExtra("dbid", dbid);
				intent.setAction(DELET_TASK_SUCESS);
				sendBroadcast(intent);
			}
		} else {
			ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
		}

	}

	@Override
	public void OnStateChanged(State state) {// 周月切换
		mState = state;
		manager.setRelativeDate(getCurrentSelectedDate());
		manager.setRelativePosition(content_viewpager.getCurrentItem());
		adapter.notifyDataSetChanged();
		resetViewPager();
	}

}
