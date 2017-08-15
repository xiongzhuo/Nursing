package com.deya.hospital.supervisor;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.timepicker.ScreenInfo;
import com.baoyz.timepicker.WheelMain;
import com.deya.acaide.R;
import com.deya.hospital.adapter.MyImageListAdapter;
import com.deya.hospital.adapter.RecordFileLisAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.DepartChoosePop;
import com.deya.hospital.base.DepartChoosePop.OnDepartPopuClick;
import com.deya.hospital.base.NewPhotoMultipleActivity;
import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.supervisor.PartTimeStaffDialog.PDialogInter;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.widget.popu.PopuRecord;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.notification.NotificationReceiver;
import com.example.calendar.CalendarMainActivity;
import com.google.gson.Gson;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SupervisionActivity extends BaseActivity implements
		OnClickListener {
	LinearLayout photoLay;
	LinearLayout recordLay;
	EditText checkContentEdt;
	EditText problemEdt;
	EditText sugesstEdt;
	GridView photoGv;
	MyImageListAdapter imgAdapter;
	TaskVo tv = new TaskVo();
	List<Attachments> imgList = new ArrayList<Attachments>();
	List<Attachments> recordList = new ArrayList<Attachments>();
	List<Attachments> fileList = new ArrayList<Attachments>();
	private Button submit;
	private String creatTime = "";
	private String missonTime = "";
	TextView timeTv;
	Gson gson = new Gson();
	private RelativeLayout rlBack;
	TextView departTv;
	ListView recordGv;
	RecordFileLisAdapter fileAdapter;
	boolean isRemark = false;
	LinearLayout timesLay;
	private RelativeLayout timelay;
	List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
	private DepartChoosePop departDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_superversion);
		tools = new Tools(mcontext, Constants.AC);
		TaskUtils.getDepartList(mcontext, departlist);
		getData();
		checkDbisCreated();
		tv.setType("2");
		intTopView();
		initView();
	}

	private void intTopView() {
		TextView titleTv = (TextView) this.findViewById(R.id.title);
		if (isRemark) {
			titleTv.setText("备注");
		} else {
			titleTv.setText("督导详情");
		}
		rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
		rlBack.setOnClickListener(this);

	}

	private void initView() {
		departTv = (TextView) this.findViewById(R.id.departTv);
		departTv.setOnClickListener(this);
		timesLay = (LinearLayout) this.findViewById(R.id.timesLay);
		timeTv = (TextView) this.findViewById(R.id.timeTv);
		timeTv.setOnClickListener(this);
		photoLay = (LinearLayout) this.findViewById(R.id.photoLay);
		recordLay = (LinearLayout) this.findViewById(R.id.recordLay);
		recordLay.setOnClickListener(this);
		checkContentEdt = (EditText) this.findViewById(R.id.checkContentEdt);
		problemEdt = (EditText) this.findViewById(R.id.problemEdt);
		sugesstEdt = (EditText) this.findViewById(R.id.sugesstEdt);
		photoGv = (GridView) this.findViewById(R.id.photoGv);
		timelay = (RelativeLayout) this.findViewById(R.id.timelay);
		imgAdapter = new MyImageListAdapter(mcontext, imgList);
		photoGv.setAdapter(imgAdapter);
		photoLay.setOnClickListener(this);

		if (isRemark) {// 如果是从每月督导模板里面来的，则默认有值
			checkContentEdt.setText(tv.getCheck_content());
			checkContentEdt.setFocusable(false);
			sugesstEdt.setText(tv.getDeal_suggest());
			problemEdt.setText(tv.getExist_problem());
			timesLay.setVisibility(View.GONE);
		}

		switchBtn = (ImageView) this.findViewById(R.id.switchBtn);
		switchBtn.setOnClickListener(this);
		submit = (Button) this.findViewById(R.id.sumbmitBtn);
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("1111111", "什么玩意");
				// if(checkContentEdt.getText().toString().trim().length()<1){
				// ToastUtils.showToast(mcontext, "检查内容不能为空");
				// return;
				// }
				// else if(problemEdt.getText().toString().trim().length()<1){
				// ToastUtils.showToast(mcontext, "发现问题不能为空");
				// return;
				// }else if(sugesstEdt.getText().toString().trim().length()<1){
				// ToastUtils.showToast(mcontext, "处理意见不能为空");
				// return;
				// }
				if (!isRemark) {
					if (departmentName.equals("")) {
						ToastUtils.showToast(mcontext, "科室不能为空！");
						return;
					}
				}
				addData(1);

			}
		});
		setShowType();
		recordGv = (ListView) this.findViewById(R.id.recordGv);
		fileAdapter = new RecordFileLisAdapter(mcontext, recordList);
		recordGv.setAdapter(fileAdapter);
		timedialog = new MyDialog(mcontext, R.style.SelectDialog);
		timeTv.setText(tv.getRemind_date());

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

		String hospitalJob = tools.getValue(Constants.JOB);
		String defultDepart = tools.getValue(Constants.DEFULT_DEPARTID);
		if (null != hospitalJob && hospitalJob.equals("3")
				&& !AbStrUtil.isEmpty(defultDepart)) {//
			// 兼职感控人员在设置了默认科室后可以直接跳过选择部分
			if (!AbStrUtil.isEmpty("defultDepart")) {
				departTv.setText(tools.getValue(Constants.DEFULT_DEPART_NAME));
				departmentName = tools.getValue(Constants.DEFULT_DEPART_NAME);
				departmentId = defultDepart;
			}
		}
	}

	public static final int ADD_PRITRUE_CODE = 9009;
	// 压缩图片的msg的what

	public static final int COMPRESS_IMAGE = 0x17;

	public void tokephote() {
		Intent takePictureIntent = new Intent(SupervisionActivity.this,
				NewPhotoMultipleActivity.class);
		takePictureIntent.putExtra(NewPhotoMultipleActivity.MAX_UPLOAD_NUM, 9);
		takePictureIntent.putExtra("type", "1");
		takePictureIntent.putExtra("size", "0");
		startActivityForResult(takePictureIntent, ADD_PRITRUE_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent redata) {
		super.onActivityResult(requestCode, resultCode, redata);
		if (requestCode == ADD_PRITRUE_CODE && null != redata) {
			Log.i("1111111111", redata.getExtras() + "");

			for (int i = 0; i < redata.getStringArrayListExtra("picList")
					.size(); i++) {
				CompressImageUtil.getCompressImageUtilInstance()
						.startCompressImage(
								myHandler,
								COMPRESS_IMAGE,
								redata.getStringArrayListExtra("picList")
										.get(i));

			}

		} else if (resultCode == 0x22 && null != redata) {
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
			departmentName = dv2.getName();
			departmentId = dv2.getId();
		}
	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case COMPRESS_IMAGE:
					if (null != msg && null != msg.obj) {
						File file = new File(msg.obj + "");
						Log.i("1111", file.exists() + "");
						// if (file.exists() && file.length() > 6.5 * 1024) {
						setFile(file.toString(), 1, "");
						// } else {
						// ToastUtils.showToast(mcontext, "非法图片");
						// }
					}
					break;
				default:
					break;
				}
			}
		}

	};

	private List<TaskVo> newTaskList = new ArrayList<TaskVo>();
	private Tools tools;
	private String departmentName = "";
	private String departmentId = "";
	protected String orderTime = "";
	private boolean isShowALLTask;
	private ImageView switchBtn;

	private void setFile(String file, int type, String time) {
		Attachments att = new Attachments();
		att.setFile_name(file);
		att.setState("1");
		att.setFile_type(type + "");
		if (!AbStrUtil.isEmpty(time) && type == 2) {
			att.setTime(time);

		} else {
			att.setTime("");
		}
		imgList.add(att);
		imgAdapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photoLay:
			tokephote();
			break;
		case R.id.submit:
			break;
		case R.id.timeTv:
			showTimeDialog();
			break;
		case R.id.switchBtn:
			setShowType();
			break;
		case R.id.rl_back:
			String sugesstion = sugesstEdt.getText().toString().trim();
			String problem = problemEdt.getText().toString().trim();
			if (isRemark) {
				if(sugesstion.length()>0||problem.length()>0||imgList.size()>0||fileList.size()>0){
					showTips();
				}else{
					finish();
				}
			}else{
			finish();
			}
			break;
		case R.id.recordLay:
			showRecordPopWindow();
			break;
		case R.id.departTv:
			departDialog.show();
			break;
		default:
			break;
		}

	}

	PartTimeStaffDialog tipdialog;

	public void showTips() {
		tipdialog = new PartTimeStaffDialog(mcontext, false, "是否保存本次编辑？",
				new PDialogInter() {
					@Override
					public void onEnter() {
						addData(1);
					}

					@Override
					public void onCancle() {
						finish();

					}
				});
		tipdialog.show();
	}

	Dialog timedialog;

	private void showTimeDialog() {

		timedialog.show();
	}

	@Override
	protected void onDestroy() {
		if (null != timedialog) {
			timedialog.dismiss();
		}
		super.onDestroy();

	}

	public void deletPhoto(int position) {
		imgList.remove(position);
		imgAdapter.notifyDataSetChanged();

	}

	public void deletRecord(int position) {
		recordList.remove(position);
		fileAdapter.notifyDataSetChanged();

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

	private void getData() {
		if (getIntent().hasExtra("data")) {
			tv = (TaskVo) getIntent().getSerializableExtra("data");
		}
		if (getIntent().hasExtra("isRemark")) {
			findViewById(R.id.departRelay).setVisibility(View.GONE);
			isRemark = true;
			departmentName = tv.getDepartmentName();
			departmentId = tv.getDepartment();
			missonTime = tv.getMission_time();
			creatTime = tv.getMission_time();
			if (null != tv.getAttachments()) {
				for (Attachments att : tv.getAttachments()) {
					if (att.getFile_type().equals("1")) {
						imgList.add(att);
					} else {
						recordList.add(att);
					}
				}
			}
		} else {
			creatTime = getIntent().getStringExtra("time");
		}
	}

	public void addData(int state) {
		if (timeTv.getText().length() > 0) {
			String isshow = tv.getIsShow();
			setReminder(timeTv.getText().toString(), isshow);
		}
		String sugesstion = sugesstEdt.getText().toString();
		String problem = problemEdt.getText().toString();
		String checkContent = checkContentEdt.getText().toString();
		TaskVo pdb = new TaskVo();
		pdb.setDepartmentName(departmentName);
		pdb.setMobile(tools.getValue(Constants.MOBILE));
		pdb.setDepartment(departmentId);
		pdb.setStatus(state);// 0 代表已同步，1代表未同步，2代表未完成,3代表未开始
		pdb.setType("2");
		pdb.setHospital(tools.getValue(Constants.HOSPITAL_ID));
		fileList.addAll(imgList);
		fileList.addAll(recordList);
		pdb.setCheck_content(checkContent);
		pdb.setExist_problem(problem);
		pdb.setRemind_date(tv.getRemind_date());
		pdb.setIsShow(tv.getIsShow());
		pdb.setDeal_suggest(sugesstion);
		Calendar calendar = null;
		calendar = new GregorianCalendar();// 子类实例化
		// pdb.setFiveTasks(pdblist);

		String time = "";

		if (isRemark) {
			pdb.setAttachments(fileList);
			pdb.setMission_time(tv.getMission_time());
		} else {
			pdb.setFileList(gson.toJson(fileList).toString());
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
		}

		if (isRemark) {
			Intent reviewIn = new Intent();
			reviewIn.putExtra("remarkdata", pdb);
			setResult(0x172, reviewIn);
			finish();
			return;
		}

		if (getIntent().hasExtra("data") && !isRemark) {// 预设任务的督导需要先删除原来的再保存，可以改进成直接通过数据库delet
			int i = -1;
			for (TaskVo vo : newTaskList) {
				i++;
				if (tv.getDbid() == vo.getDbid()) {
					try {
						DataBaseHelper
								.getDbUtilsInstance(mcontext)
								.update(pdb,
										WhereBuilder.b("dbid", "=",
												tv.getDbid()));
						Intent brodcastIntent = new Intent();
						brodcastIntent
								.setAction(CalendarMainActivity.UPDATA_ACTION);
						SupervisionActivity.this.sendBroadcast(brodcastIntent);
						finish();
						return;
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		} else {
			try {
				DataBaseHelper.getDbUtilsInstance(mcontext)
						.save(pdb);

				newTaskList = DataBaseHelper
						.getDbUtilsInstance(mcontext).findAll(TaskVo.class);

				// 创建后要发广播通知已更新
				Intent brodcastIntent = new Intent();
				brodcastIntent.setAction(CalendarMainActivity.UPDATA_ACTION);
				SupervisionActivity.this.sendBroadcast(brodcastIntent);
				finish();
			} catch (DbException e) {
				e.printStackTrace();
			}

		}

		Log.i("1111111", pdb.getDepartment());

	}

	// public void addData(int state) {
	// try {
	// if(isAfter){
	// if(null!=departmentIds){
	// for (int i = 0; i < departmentIds.length; i++) {
	// createData(type,departmentIds[i],departmentNames[i]);
	// DebugUtil.debug("addddae", departmentIds[i]+"  "+departmentNames[i]);
	// Intent brodcastIntent = new Intent();
	// brodcastIntent.setAction(CalendarMainActivity.UPDATA_ACTION);
	// SupervisionActivity.this.sendBroadcast(brodcastIntent);
	// }
	//
	// finish();
	// }
	// }else{
	// TaskVo pdb= createData(type,departmentId,departmentName);
	// newTaskList = DataBaseHelper
	// .getDbUtilsInstance(mcontext).findAll(TaskVo.class);
	//
	// int taskId=newTaskList.get(0).getTask_id();
	//
	// // 创建后要发广播通知已更新
	// Intent brodcastIntent = new Intent();
	// brodcastIntent.setAction(CalendarMainActivity.UPDATA_ACTION);
	// SupervisionActivity.this.sendBroadcast(brodcastIntent);
	// pdb.setTask_id(taskId);
	// startDotasks(type,pdb);
	// }
	//
	// } catch (DbException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// 发送通知
	private void setReminder(String time, String isSend) {

		// get the AlarmManager instance
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		// create a PendingIntent that will perform a broadcast
		PendingIntent pi = PendingIntent.getBroadcast(SupervisionActivity.this,
				0, new Intent(this, NotificationReceiver.class), 0);

		if (isSend.equals("0")) {
			// just use current time + 10s as the Alarm time.
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			// 可以根据项目要求修改，秒、分钟、提前、延后
			c.add(Calendar.SECOND, 10);
			c.add(Calendar.DAY_OF_MONTH, 10);
			// schedule an alarm
			Date date = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if (time == null) {
				return;
			}
			try {
				date = sdf.parse(time);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			am.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
		} else {
			// cancel current alarm
			am.cancel(pi);
		}

	}

	// 选时间
	/*
	 * 创建PopupWindow
	 */
	private void initTimePopuptWindow() {
		// 隐藏软键盘
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		View timepickerview = View.inflate(mcontext, R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(SupervisionActivity.this);
		final WheelMain wheelMain = new WheelMain(timepickerview, true);
		wheelMain.screenheight = screenInfo.getHeight();
		Time curTime = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
		curTime.setToNow(); // 取得系统时间。
		int year = curTime.year;
		int month = curTime.month;
		int day = curTime.monthDay;
		int hour = curTime.hour; // 0-23
		int minute = curTime.minute;
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.4f;
		getWindow().setAttributes(lp);
		wheelMain.initDateTimePicker(year, month, day, hour, minute);
		final PopupWindow mPopupWindow = new PopupWindow(timepickerview,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		Drawable draw = new ColorDrawable(getResources().getColor(
				android.R.color.transparent));
		mPopupWindow.setBackgroundDrawable(draw);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
		mPopupWindow.showAtLocation(this.findViewById(R.id.sumbmitBtn),
				Gravity.CENTER, 0, 0);
		timepickerview.findViewById(R.id.btn_ok).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						orderTime = wheelMain.getTime().toString();
						timeTv.setText(orderTime);

						mPopupWindow.dismiss();
						WindowManager.LayoutParams lp = getWindow()
								.getAttributes();
						lp.alpha = 0.5f;
						getWindow().setAttributes(lp);
					}
				});
		mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
	}

	private void setShowType() {
		String isshow = tv.getIsShow();
		if (null != isshow && isshow.equals("1")) {
			switchBtn.setImageResource(R.drawable.dynamic_but2);
			timelay.setVisibility(View.VISIBLE);
			if (AbStrUtil.isEmpty(tv.getRemind_date())) {
				timeTv.setText(TaskUtils.getTaskMissionTime(""));
			}
			tv.setIsShow("0");
		} else {
			switchBtn.setImageResource(R.drawable.dynamic_but1);
			timelay.setVisibility(View.GONE);
			tv.setIsShow("1");
		}

	}

	public static final int RECORD_STADIO_INITIAL = 0;// 未开始
	public static final int RECORD_STADIO_ISSTART = 1;// 已开始录制
	public static final int RECORD_STADIO_IS_STOPRECORD = 2;//
	public static final int RECORD_STADIO_PLAYING = 3;
	public static final int RECORD_STADIO_STOPPLAY = 4;

	void showRecordPopWindow() {
		new PopuRecord(mcontext, false,false,
				SupervisionActivity.this.findViewById(R.id.main),
				new PopuRecord.OnPopuClick() {

					@Override
					public void cancel() {
						// TODO Auto-generated method stub

					}

					@Override
					public void enter(String filename, double totalTime) {
						Attachments att = new Attachments();
						att.setFile_name(filename);
						att.setTime(totalTime + "");
						att.setFile_type("2");
						att.setState("1");
						recordList.add(att);
						fileAdapter.notifyDataSetChanged();
					}

					@Override
					public void photograph(File file, List<String> result) {
					}

					@Override
					public void album() {

					}

					@Override
					public void record() {
						// TODO Auto-generated method stub

					}

					@Override
					public void dismiss() {
					}

					@Override
					public void play() {

					}

					@Override
					public void lonclick() {
						// TODO Auto-generated method stub

					}
				});
	}

	// 时间选择器
	MyDialog dialog;
	private RelativeLayout pushstting;
	private RelativeLayout above;
	private TextView jobs;
	private TextView count;

	public class MyDialog extends Dialog {

		private Button showBtn;

		/**
		 * Creates a new instance of MyDialog.
		 */
		public MyDialog(Context context, int theme) {
			super(context, theme);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO 自动生成的方法存根
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);

			setContentView(R.layout.timepicker);

			LinearLayout timepickerview = (LinearLayout) this
					.findViewById(R.id.timePicker1);
			ScreenInfo screenInfo = new ScreenInfo(SupervisionActivity.this);
			final WheelMain wheelMain = new WheelMain(timepickerview, true);
			wheelMain.screenheight = screenInfo.getHeight();
			Time curTime = new Time(); // or Time t=new Time("GMT+8"); 加上Time
										// Zone资料
			curTime.setToNow(); // 取得系统时间。
			int year = curTime.year;
			int month = curTime.month;
			int day = curTime.monthDay;
			int hour = curTime.hour; // 0-23
			int minute = curTime.minute;
			wheelMain.initDateTimePicker(year, month, day, hour, minute);
			showBtn = (Button) this.findViewById(R.id.btn_ok);

			showBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					orderTime = wheelMain.getTime().toString();
					timeTv.setText(orderTime);
					tv.setRemind_date(orderTime);
					dismiss();

				}
			});

		}
	}
}
