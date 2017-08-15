package com.deya.hospital.supervisor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.FileLisAdapter;
import com.deya.hospital.adapter.FinishTimesAdapter;
import com.deya.hospital.adapter.FiveMethodsListAdapter;
import com.deya.hospital.adapter.FramMethodsListAdapter;
import com.deya.hospital.adapter.HospitalListAdapter;
import com.deya.hospital.adapter.JobListAdapter;
import com.deya.hospital.adapter.JobTimesListAdapter;
import com.deya.hospital.adapter.MethodsListAdapter;
import com.deya.hospital.adapter.MutichooseListAdapter;
import com.deya.hospital.adapter.PersonAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.base.DeletDialog;
import com.deya.hospital.base.SelectPhotoActivity;
import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.base.img.PhotoNumsBean;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.FileDownloadThread;
import com.deya.hospital.util.HorizontalListView;
import com.deya.hospital.util.HorizontalListView.OnScrollStateChangedListener;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.RoundProgressBar;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ThreadPoolUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.HospitalInfo;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.vo.RulesVo;
import com.deya.hospital.vo.WrongRuleVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.vo.dbdata.planListDb;
import com.deya.hospital.vo.dbdata.subTaskVo;
import com.deya.hospital.widget.popu.PopuAddHospital;
import com.deya.hospital.widget.popu.PopuAddHospital.OnPopuClick;
import com.deya.hospital.widget.popu.PopuRecord;
import com.deya.hospital.widget.view.DySpinner;
import com.deya.hospital.widget.view.DySpinner.DySpinnerListener;
import com.example.calendar.CalendarMainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewAccomplishTasksActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout group;
	HorizontalListView personlistView;
	LinearLayout jobTimesListView;
	PersonAdapter pAdapter;
	ListView typelistView;
	ListView methodListView;
	ListView finishListView, fileListView;
	TextView moreTv;
	ScrollView scroll;
	MethodsListAdapter mAdapter;
	FramMethodsListAdapter framAdapter;
	FiveMethodsListAdapter metodsAdapter;
	int personIndex = 0;
	Button continueBtn, finishBtn, nameBtn;
	private DySpinner dySpinner;
	TextView titleTv, departTv, personTv, qipaoTv, typeTv;
	View unchooseline, choosline, methodline;// 间隔线
	List<subTaskVo> finishList = new ArrayList<subTaskVo>();
	FinishTimesAdapter finishAdapter;
	private TextView totalTv;
	ImageView imgBack;
	LinearLayout groupLayout;
	RelativeLayout joblayout;
	TextView jobTv, pointTv;
	FileLisAdapter fileAdapter;
	Dialog nameDialog = null;
	List<Attachments> file = new ArrayList<Attachments>();
	TextView totalfileTv, totalfileTvQipao;
	View taskLine, fileLine;
	int subtaskId = 0;
	MultiChooseDialog chooseDialog;
	MutichooseListAdapter chooseAdapter;
	List<JobListVo> jobTimesList = new ArrayList<JobListVo>();
	JobTimesListAdapter jobTimeAdapter;
	public List<Attachments> temporaryFiles = new ArrayList<Attachments>();
	public boolean showFram = true;
	String str;

	public static String[] personItem = { "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	boolean isCheck[] = { false, false, false, false, false };
	private LayoutParams para;
	private Animation animation;
	Timer timer;
	TimerTask timerTask;
	static int minute = 30;
	static int second = 0;
	final static String tag = "tag";
	Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			System.out.println("handle!");
			if (minute == 0) {
				if (second == 0) {
					titleTv.setText("时间结束!  完成:" + tv.getTotalNum());
					if (timer != null) {
						timer.cancel();
						timer = null;
					}
					if (timerTask != null) {
						timerTask = null;
					}
				} else {
					second--;
					if (second >= 10) {
						titleTv.setText("0" + minute + ":" + second + "完成:"
								+ tv.getTotalNum());
					} else {
						titleTv.setText("0" + minute + ":0" + second + "完成:"
								+ tv.getTotalNum());
					}
				}
			} else {
				if (second == 0) {
					second = 59;
					minute--;
					if (minute >= 10) {
						titleTv.setText(minute + ":" + second + "完成:"
								+ tv.getTotalNum());
					} else {
						titleTv.setText("0" + minute + ":" + second + "完成:"
								+ tv.getTotalNum());
					}
				} else {
					second--;
					if (second >= 10) {
						if (minute >= 10) {
							titleTv.setText(minute + ":" + second + "完成:"
									+ tv.getTotalNum());
						} else {
							titleTv.setText("0" + minute + ":" + second + "完成:"
									+ tv.getTotalNum());
						}
					} else {
						if (minute >= 10) {
							titleTv.setText(minute + ":0" + second + "完成:"
									+ tv.getTotalNum());
						} else {
							titleTv.setText("0" + minute + ":0" + second
									+ "完成:" + tv.getTotalNum());
						}
					}
				}
			}
		};
	};
	Handler handler3 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SETETITLE:
				if (null != msg && null != msg.obj) {
					Log.i("111111", msg.obj.toString());
					int num = Integer.parseInt(msg.obj.toString());
					titleTv.setText("完成：" + num + "/" + colType);
					setTitleTv(Integer.parseInt(msg.obj.toString()));
				}
				break;

			default:
				break;
			}
		}
	};
	CountDownTimer countTimer;
	public void startCutDownTimer(){
		countTimer=new CountDownTimer(minute*60*1000+second*1000,1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				minute=(int) ((millisUntilFinished/1000)/60);
				second=(int) ((millisUntilFinished/1000)%60);
				if(second<10){
					titleTv.setText(minute + ":" + "0"+second + "完成:"
							+ tv.getTotalNum());	
				}else{
				titleTv.setText(minute + ":" + second + "完成:"
						+ tv.getTotalNum());
				}
				
			}
			
			@Override
			public void onFinish() {
				minute=0;
				second=0;
				titleTv.setText("时间结束!  完成:" + tv.getTotalNum());
				countTimer.cancel();
				
			}
		};
		countTimer.start();
		
	}
	void startTimer() {
		if (null == tv.getTotalNum()) {
			tv.setTotalNum(0 + "");
		}
		second = tv.getSeconds();
		minute = tv.getMinute();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 0;
				handler2.sendMessage(msg);
			}
		};

		timer = new Timer();
		timer.schedule(timerTask, 0, 1000);

	}

	@Override
	protected void onDestroy() {
		Log.v(tag, "log---------->onDestroy!");
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask = null;
		}
		if (null != countTimer) {
			countTimer.cancel();
		}
		if (null != brodcast) {
			unregisterReceiver(brodcast);
		}
		super.onDestroy();
	}

	TextView backText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dotasks_activity);

		inflater = LayoutInflater.from(mcontext);
		int[] wh = AbViewUtil.getDeviceWH(mcontext);
		para = new LayoutParams((wh[0] - dp2Px(mcontext, 70)) / 3, dp2Px(
				mcontext, 70));
		tools = new Tools(mcontext, Constants.AC);
		departTv = (TextView) this.findViewById(R.id.departTv);

		animation = AnimationUtils
				.loadAnimation(this, R.anim.applaud_animation);

		dySpinner = (DySpinner) this.findViewById(R.id.dySpinner);
		totalTv = (TextView) this.findViewById(R.id.totalTv);
		titleTv = (TextView) this.findViewById(R.id.titleTv);
		totalTv.setOnClickListener(this);
		totalfileTv = (TextView) this.findViewById(R.id.totalfileTv);
		totalfileTv.setOnClickListener(this);
		imgBack = (ImageView) this.findViewById(R.id.img_back);
		imgBack.setOnClickListener(this);
		typeTv = (TextView) this.findViewById(R.id.typeTv);
		pointTv = (TextView) this.findViewById(R.id.pointTv);
		backText = (TextView) this.findViewById(R.id.backText);
		taskLine = this.findViewById(R.id.taskline);
		fileLine = this.findViewById(R.id.fileline);
		backText.setOnClickListener(this);
		findDbJobList();
		getJobCacheData();

		if (savedInstanceState != null) {
			tv = (TaskVo) savedInstanceState.getSerializable("tv");
			minute = savedInstanceState.getInt("minute");
			second = savedInstanceState.getInt("second");
		}
		getData();
		initViews();

		ThreadPoolUtil.getInstant().execute(new Runnable() {

			@Override
			public void run() {

				checkDbisCreated();
				getWrongRules();

			}
		});
		registerBroadcast();

		popAdapter = new JobListAdapter(mcontext, jobList);

		setJobListAdapter();
		dySpinner.setOnSpinnerClik(new DySpinnerListener() {

			@Override
			public String OnItemClick(int position, int index, int index2) {
				DebugUtil.debug("dySpinner", "get position>" + position
						+ "  index>" + index);
				String text = jobList.get(index2).getName();
				list.get(position).setPpostName(text);
				list.get(position).setPpost(jobList.get(index2).getId());
				return text;
			}
		});

		qipaoTv = (TextView) this.findViewById(R.id.qipaoTv);

	}

	private void setJobListAdapter() {
		if (null != list && list.size() > 0) {
			dySpinner.setAdapter(personIndex, popAdapter);
			String name = list.get(personIndex).getPpostName();
			name = null == name || name.equals("") ? "岗位" : name;
			dySpinner.SetText(name);
		}
	}

	ListView typelistViewFram;
	LinearLayout framguide1;
	LinearLayout framguide2, framlayout2;
	ListView listView2Fram;
	LinearLayout framsubmitlay;
	LinearLayout continueBtnFramLay, finishBtnFramlay;
	private LinearLayout framlayout;
	boolean isShowFram = true;
	private LinearLayout framguide3, framguid5;
	private LinearLayout framlistlay;
	TextView continueBtnFram, finishBtnFram;

	private void setFramView() {
		this.showFram = false;
		framlayout = (LinearLayout) this.findViewById(R.id.framlayout);
		if (isShowFram) {
			framlayout.setVisibility(View.VISIBLE);
			scroll.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					return true;
				}
			});
			framguide1 = (LinearLayout) this.findViewById(R.id.framguid1);
			framguide2 = (LinearLayout) this.findViewById(R.id.framguid2);
			framguide3 = (LinearLayout) this.findViewById(R.id.framguid3);
			framlayout2 = (LinearLayout) this.findViewById(R.id.framlayout2);
			framlistlay = (LinearLayout) this.findViewById(R.id.framlistlay);
			continueBtnFramLay = (LinearLayout) this
					.findViewById(R.id.continueBtnFramLay);
			finishBtnFramlay = (LinearLayout) this
					.findViewById(R.id.finishBtnFramlay);
			framguid5 = (LinearLayout) this.findViewById(R.id.framguid5);
			typelistViewFram = (ListView) this
					.findViewById(R.id.typelistViewFram);
			listView2Fram = (ListView) this.findViewById(R.id.listView2Fram);
			listView2Fram.setAdapter(framAdapter);
			typelistViewFram.setAdapter(metodsAdapter);
			continueBtnFram = (TextView) this
					.findViewById(R.id.continueBtnFram);
			finishBtnFram = (TextView) this.findViewById(R.id.finishBtnFram);
			framsubmitlay = (LinearLayout) this
					.findViewById(R.id.framsubmitlay);
			setFramViewState(2);
			typelistViewFram.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (personIndex != -1) {
						if (position == 3) {
							if (isCheck[4]) {
								// ToastUtils.showToast(mcontext,
								// "接触患者后和接触患者周边环境后不能同时选择");
								// return;
								isCheck[4] = false;
							}
						}
						if (position == 4) {
							if (isCheck[3]) {
								// ToastUtils.showToast(mcontext,
								// "接触患者后和接触患者周边环境后不能同时选择");
								// return;
								isCheck[3] = false;
							}
						}
						if (isCheck[position]) {
							isCheck[position] = false;
						} else {
							isCheck[position] = true;
						}
						metodsAdapter.setIsCheck(position, isCheck[position]);
						boolean cancheck = false;
						for (int i = 0; i < isCheck.length; i++) {
							if (isCheck[i]) {
								cancheck = true;
							}

						}
						if (cancheck) {
							unchooseline.setVisibility(View.GONE);
							choosline.setVisibility(View.VISIBLE);
							methodline
									.setBackgroundResource(R.color.check_corlor);
						} else {
							setContinueBtn(false);
							methodline.setBackgroundResource(R.color.devider);
						}
						mAdapter.canCheck(cancheck);
					} else {
						ToastUtils.showToast(mcontext, "请先选择被观察对象！");
						return;

					}
					setFramViewState(1);
				}

			});

		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putSerializable("tv", tv);
		outState.putInt("minute", minute);
		outState.putInt("second", second);

		super.onSaveInstanceState(outState);
	}

	int framguidHigt = 0;
	LayoutParams layoutparam, layoutparam2;

	public void setFramViewState(int sate) {
		if (!isShowFram) {
			return;
		}
		if (sate == 1) {
			framlistlay.setVisibility(View.VISIBLE);
			framguide3.setVisibility(View.GONE);
			framguidHigt = typelistView.getMeasuredHeight();
			layoutparam = framguide3.getLayoutParams();
			layoutparam2 = framguid5.getLayoutParams();
			layoutparam.height = framguidHigt;
			layoutparam2.height = framguidHigt;
			WindowManager wm = this.getWindowManager();

			int width = wm.getDefaultDisplay().getWidth();
			layoutparam2.width = width;

			layoutparam.width = framguidHigt;
			framguide3.setLayoutParams(layoutparam);
			framguid5.setLayoutParams(layoutparam2);
			Log.i("111111111111111111111",
					"-------------------" + typelistView.getMeasuredHeight());
			typelistViewFram.setVisibility(View.GONE);
			framguide1.setVisibility(View.GONE);
			framguide2.setVisibility(View.VISIBLE);
			framlayout2.setVisibility(View.VISIBLE);
			framsubmitlay.setVisibility(View.GONE);
			typelistView.setEnabled(false);
			methodListView.setEnabled(true);
		} else if (sate == 2) {
			framlistlay.setVisibility(View.VISIBLE);
			framguide3.setVisibility(View.GONE);
			typelistViewFram.setVisibility(View.VISIBLE);
			framguide1.setVisibility(View.VISIBLE);
			framguide2.setVisibility(View.GONE);
			framlayout2.setVisibility(View.GONE);
			framsubmitlay.setVisibility(View.GONE);
			typelistView.setEnabled(true);
			methodListView.setEnabled(false);
		} else if (sate == 3) {
			framlistlay.setVisibility(View.GONE);
			framguide3.setVisibility(View.VISIBLE);
			framsubmitlay.setVisibility(View.VISIBLE);
			typelistView.setEnabled(false);
			methodListView.setEnabled(false);
			mAdapter.needCheck(false);
		} else if (sate == 4) {
			framlistlay.setVisibility(View.VISIBLE);
			framguide3.setVisibility(View.GONE);
			typelistViewFram.setVisibility(View.GONE);
			framguide1.setVisibility(View.GONE);
			framguide2.setVisibility(View.GONE);
			framlayout2.setVisibility(View.INVISIBLE);
			framsubmitlay.setVisibility(View.GONE);
			typelistView.setEnabled(false);
			methodListView.setEnabled(false);
			mAdapter.needCheck(false);
		} else if (sate == 5) {
			framlistlay.setVisibility(View.GONE);
			framguide3.setVisibility(View.GONE);
			framguid5.setVisibility(View.VISIBLE);
			framsubmitlay.setVisibility(View.VISIBLE);
			typelistView.setEnabled(false);
			methodListView.setEnabled(false);
			mAdapter.needCheck(false);
			continueBtnFram.setEnabled(false);
			continueBtnFramLay.setVisibility(View.INVISIBLE);
			finishBtnFram.setEnabled(true);
			finishBtnFramlay.setVisibility(View.VISIBLE);
		}

	}

	int[] wh;
	private View addButton;
	private Tools tools;
	boolean scroll_status = false;

	private void initViews() {

		wh = AbViewUtil.getDeviceWH(mcontext);
		metodsAdapter = new FiveMethodsListAdapter(mcontext);
		mAdapter = new MethodsListAdapter(mcontext);
		framAdapter = new FramMethodsListAdapter(mcontext);
		personlistView = (HorizontalListView) this
				.findViewById(R.id.personlistView);
		jobTimesListView = (LinearLayout) this
				.findViewById(R.id.jobTimesListView);
		ResetJobTimesListView();
		addButton = this.findViewById(R.id.addButton);
		fileAdapter = new FileLisAdapter(mcontext, fileList);
		addButton.setOnClickListener(this);

		for (int i = 0; i < list.size(); i++) {
			list.get(i).setSelect_status(i == 0);
		}
		pAdapter = new PersonAdapter(mcontext, list, jobList);
		jobTimeAdapter = new JobTimesListAdapter(mcontext, jobTimesList);
		personlistView.setAdapter(pAdapter);
		moreTv = (TextView) this.findViewById(R.id.moreTv);
		moreTv.setOnClickListener(this);
		typelistView = (ListView) this.findViewById(R.id.typelistView);
		methodListView = (ListView) this.findViewById(R.id.listView2);
		methodListView.setAdapter(mAdapter);
		typelistView.setAdapter(metodsAdapter);

		personTv = (TextView) this.findViewById(R.id.person);
		personTv.setText("(选中被调查人：" + list.get(personIndex).getPname() + ")");

		unchooseline = this.findViewById(R.id.unchooseline);
		choosline = this.findViewById(R.id.chooseline);
		methodline = this.findViewById(R.id.methodline);
		groupLayout = (LinearLayout) this.findViewById(R.id.personGroup);
		joblayout = (RelativeLayout) this.findViewById(R.id.joblayout);
		joblayout.setOnClickListener(this);
		jobTv = (TextView) this.findViewById(R.id.jobTv);
		nameBtn = (Button) this.findViewById(R.id.nameBtn);
		nameBtn.setOnClickListener(this);
		finishAdapter = new FinishTimesAdapter(mcontext, finishList, jobList);

		if (null != list && list.size() > 0) {
			setPintX(0, pAdapter.item_x());
		}

		personlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setPintX((int) view.getX(), view.getWidth() / 2);

				if (position != personIndex) {
					selectItem(position);
				} else {
					return;
				}
			}
		});
		personlistView
				.setOnScrollStateChangedListener(new OnScrollStateChangedListener() {

					@Override
					public void onScrollStateChanged(ScrollState scrollState) {

						// personlistView.getChildAt(0);
						// TODO Auto-generated method stub
						switch (scrollState) {
						case SCROLL_STATE_IDLE:// 空闲状态
							if (!isclick) {
								int p_index = personIndex;
								View v = personlistView.getChildAt(1);
								try {
									p_index = Integer.parseInt(v.getTag(
											R.id.person_item_position)
											.toString());
								} catch (Exception e) {
									// TODO: handle exception
								}
								Message msg = new Message();
								msg.what = MOVE_POINT;
								msg.obj = v;
								myHandler.sendMessage(msg);
								// myHandler.sendEmptyMessage(MOVE_POINT);
								if (p_index != personIndex) {
									// Toast.makeText(mcontext, "tag>>"+p_index,
									// 500).show();
									selectItem(p_index);

								}
							}
							isclick = false;
							break;
						case SCROLL_STATE_FLING:// 滚动状态
							break;
						case SCROLL_STATE_TOUCH_SCROLL:// 触摸后滚动
							break;
						}
					}
				});

		typelistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (personIndex != -1) {
					if (position == 3) {
						if (isCheck[4]) {
							// ToastUtils.showToast(mcontext,
							// "接触患者后和接触患者周边环境后不能同时选择");
							// return;
							isCheck[4] = false;
						}
					}
					if (position == 4) {
						if (isCheck[3]) {
							// ToastUtils.showToast(mcontext,
							// "接触患者后和接触患者周边环境后不能同时选择");
							// return;
							isCheck[3] = false;
						}
					}
					if (isCheck[position]) {
						isCheck[position] = false;
					} else {
						isCheck[position] = true;
					}
					metodsAdapter.setIsCheck(position, isCheck[position]);
					boolean cancheck = false;
					for (int i = 0; i < isCheck.length; i++) {
						if (isCheck[i]) {
							cancheck = true;
						}

					}
					if (cancheck) {
						unchooseline.setVisibility(View.GONE);
						choosline.setVisibility(View.VISIBLE);
						methodline.setBackgroundResource(R.color.check_corlor);
					} else {
						setContinueBtn(false);
						methodline.setBackgroundResource(R.color.devider);
					}
					mAdapter.canCheck(cancheck);
				} else {
					ToastUtils.showToast(mcontext, "请先选择被观察对象！");
					return;

				}

			}

		});
		methodListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}

		});

		finishListView = (ListView) this.findViewById(R.id.finishTimesList);
		fileListView = (ListView) this.findViewById(R.id.fileListView);
		finishListView.setAdapter(finishAdapter);
		fileListView.setAdapter(fileAdapter);
		finishListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					Dialog dialog;

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, final int position, long id) {

						dialog = new DeletDialog(mcontext,
								R.style.SelectDialog,
								new View.OnClickListener() {
									int deleti = 0;
									int deletj = 0;

									@Override
									public void onClick(View v) {
										for (int i = 0; i < list.size(); i++) {
											for (int j = 0; j < list.get(i)
													.getSubTasks().size(); j++) {
												if (list.get(i).getSubTasks()
														.get(j).getSubtaskId() == finishList
														.get(position)
														.getSubtaskId()) {
													deleti = i;
													deletj = j;
													Log.i("111111", "111111");
												}
											}

										}
										if (null != list.get(deleti)
												.getSubTasks().get(deletj)) {
											list.get(deleti).getSubTasks()
													.remove(deletj);
										}
										finishList.remove(position);
										tv.setTotalNum(finishList.size() + "");

										totalTv.setText("已完成("
												+ finishList.size() + ")");
										finishAdapter.notifyDataSetChanged();
										if (!colType.equals("1")) {
											titleTv.setText("完成:"
													+ finishList.size() + "/"
													+ colType);
										} else {
											if (tv.isUpdatedTask()) {
												titleTv.setText("完成:"
														+ finishList.size());
											}
										}
										String str = gson.toJson(list);
										tv.setFiveTasks(str);
										updataDb(personIndex);
										pAdapter.notifyDataSetChanged();
										dialog.dismiss();
									}

								});
						// finishList.remove(position);
						// finishAdapter.notifyDataSetChanged();

						dialog.show();
						return false;
					}
				});
		typelistView.setFocusable(false);
		scroll = (ScrollView) this.findViewById(R.id.scroll);
		scroll.smoothScrollTo(0, 20);

		continueBtn = (Button) this.findViewById(R.id.continueBtn);
		continueBtn.setEnabled(false);
		finishBtn = (Button) this.findViewById(R.id.finishBtn);

		totalfileTvQipao = (TextView) this.findViewById(R.id.totalfileTvpop);
		setFramView();
		continueBtn.setOnClickListener(this);
		finishBtn.setOnClickListener(this);
		// setPersonLayout();
		setEndButton();
		departs = new ArrayList<HospitalInfo>();
		for (int i = 0; i < 10; i++) {
			HospitalInfo dv = new HospitalInfo();
			dv.setName("戴手套");
			departs.add(dv);
		}

	}

	List<HospitalInfo> departs;

	public void showChooseDialog(int position) {
		if (!isWho) {
			MultiChooseDialog dialog = new MultiChooseDialog(mcontext, ruleList
					.get(position).getItems());
			dialog.show();
		}
	}

	List<RulesVo> chooseRules = new ArrayList<RulesVo>();

	// 不规范弹出框
	public class MultiChooseDialog extends BaseDialog {
		ListView listView;
		HospitalListAdapter adapter;
		List<RulesVo> ruleItems2;

		public MultiChooseDialog(Context context, List<RulesVo> ruleItems) {
			super(context);
			this.ruleItems2 = ruleItems;
			chooseAdapter = new MutichooseListAdapter(mcontext, ruleItems2);

		}

		public void setChoosePosition(int position) {

		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.dialog_mutichoose);
			listView = (ListView) this.findViewById(R.id.dialog_list);

			Button choose = (Button) this.findViewById(R.id.chooseBtn);
			choose.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();

				}
			});
			listView.setAdapter(chooseAdapter);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (ruleItems2.get(position).isChoosed()) {
						ruleItems2.get(position).setChoosed(false);
					} else {
						ruleItems2.get(position).setChoosed(true);
					}
					chooseAdapter.notifyDataSetChanged();
				}
			});

			choose.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					chooseRules.clear();
					for (RulesVo rv : ruleItems2) {
						if (rv.isChoosed()) {
							chooseRules.add(rv);
							rv.setChoosed(false);
						}
					}
					setContinudata();
					setContinueBtn(false);
					dismiss();
				}
			});
		}

	}

	private void selectItem(int position) {
		setContinudata();
		setContinueBtn(false);
		// resetFiveMethodAdapter();
		personIndex = position;
		personTv.setText("(选中被调查人：" + list.get(personIndex).getPname() + ")");
		// pAdapter.setCheck(personIndex);

		String t = list.get(personIndex).getPpostName();
		resetFiveMethodAdapter();
		jobTv.setText(list.get(personIndex).getPpostName());
		t = null == t || "".equals(t) ? "岗位" : t;

		jobTv.setText(t);
		nameBtn.setText(list.get(personIndex).getPname());

		for (int i = 0; i < list.size(); i++) {
			list.get(i).setSelect_status(false);
		}
		list.get(position).setSelect_status(true);
		myHandler.sendEmptyMessage(CHANGE_PERSON_ADAPTER);
	}

	private void setPintX(int x, int width) {
		DebugUtil.debug("setPintX", x + " " + width);
		MarginLayoutParams margin = new MarginLayoutParams(
				pointTv.getLayoutParams());
		margin.setMargins(x + width, margin.topMargin, x + margin.width,
				margin.bottomMargin);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				margin);
		pointTv.setLayoutParams(layoutParams);
	}

	// 重置指针选中状态
	public void resetFiveMethodAdapter() {
		for (int i = 0; i < isCheck.length; i++) {
			isCheck[i] = false;
		}
		metodsAdapter.reset();// 重置指针选中状态
		mAdapter.ResetAdapter();
		framAdapter.ResetAdapter();

	}

	public int dp2Px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}

	// 设置继续按钮的状态
	public void setContinueBtn(boolean enable) {
		continueBtn.setEnabled(enable);
		if (isShowFram && enable) {
			setFramViewState(3);
		}
		if (!enable) {
			unchooseline.setVisibility(View.VISIBLE);
			choosline.setVisibility(View.GONE);
		}
	}

	public int listViewType = 1;// 默认1为任务列表，2为文件列表

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addButton:
			setListScroll();
			break;

		case R.id.moreTv:
			fileAdapter.stoPplayMedia();
			new PopuRecord(mcontext, true,false,
					NewAccomplishTasksActivity.this.findViewById(R.id.main),
					new PopuRecord.OnPopuClick() {

						@Override
						public void cancel() {
							// TODO Auto-generated method stub

						}

						@Override
						public void enter(String filename, double totalTime) {
							// TODO Auto-generated method stub
							Log.i("1111111", filename);
							File file = new File(filename);
							if (file.exists()) {
								addFile(filename, 2, totalTime + "");
							}

						}

						@Override
						public void photograph(File file, List<String> result) {
							// TODO Auto-generated method stub
							resullist = result;
							Intent takePictureIntent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							takePictureIntent.putExtra("output",
									Uri.fromFile(file));
							startActivityForResult(takePictureIntent,
									REQUEST_TAKE_PHOTO);
						}

						@Override
						public void album() {
							// TODO Auto-generated method stub
							Intent intent = new Intent(mcontext,
									SelectPhotoActivity.class);
							intent.putExtra("size", size);
							PhotoNumsBean.getInstant().setNumber(1);
							startActivityForResult(intent,
									REQUEST_SELECT_LOCAL_PHOTO);
						}

						@Override
						public void record() {
							// TODO Auto-generated method stub

						}

						@Override
						public void dismiss() {
							// TODO Auto-generated method stub
							recordStadio = RECORD_STADIO_INITIAL;
						}

						@Override
						public void play() {

						}

						@Override
						public void lonclick() {
							// TODO Auto-generated method stub

						}
					});
			break;
		case R.id.continueBtn:
			setContinueBtn(false);
			setFramViewState(5);
			setContinudata();

			break;
		case R.id.finishBtn:
			finishTask();
			break;
		case R.id.img_back:
		case R.id.backText:
			if (isShowFram) {
				return;
			} else if (tv.isUpdatedTask()) {
				finish();
				return;
			}
			setJob();
			updateDb(tv);
			finish();
			break;
		case R.id.totalfileTv:
			dialToatalFileTv();
			break;
		case R.id.totalTv:
			dialToaltalTaskTV();
			break;
		case R.id.nameBtn:
			new PopuAddHospital(mcontext, _activity, "请输入被观察对象姓名", v, "",
					new OnPopuClick() {

						@Override
						public void enter(String text) {
							// TODO Auto-generated method stub
							list.get(personIndex).setPname(text);
							pAdapter.notifyDataSetChanged();
							nameBtn.setText(text);
							AbViewUtil
									.colseVirtualKeyboard(NewAccomplishTasksActivity.this);
						}

						@Override
						public void cancel() {
							// TODO Auto-generated method stub
						}

						@Override
						public void dismiss() {
							// TODO Auto-generated method stub
							scroll.fullScroll(ScrollView.FOCUS_UP);

						}
					});
			// if (null == nameDialog) {
			// nameDialog = new AddDialog(mcontext, R.style.SelectDialog);
			// }
			// nameDialog.show();

			break;
		default:

			break;
		}

	}

	public void setLastTimes() {
		tv.setMinute(minute);
		tv.setSeconds(second);
	}

	private void dialToaltalTaskTV() {
		if (finishListView.getVisibility() != 0) {
			scroll.scrollTo(100, 150);
			finishListView.setVisibility(View.VISIBLE);
			fileListView.setVisibility(View.GONE);
			totalTv.setBackgroundResource(R.color.white);
			totalfileTv.setBackgroundResource(R.color.color_f5f5f5);
			fileLine.setVisibility(View.VISIBLE);
			taskLine.setVisibility(View.GONE);
		}
	}

	private void dialToatalFileTv() {
		if (fileListView.getVisibility() != 0) {
			scroll.scrollTo(100, 100);
			finishListView.setVisibility(View.GONE);
			fileListView.setVisibility(View.VISIBLE);
			totalfileTv.setBackgroundResource(R.color.white);
			totalTv.setBackgroundResource(R.color.color_f5f5f5);
			taskLine.setVisibility(View.VISIBLE);
			fileLine.setVisibility(View.GONE);
		}
	}

	private void setJob() {
		if (list.size() > 0) {
			str = gson.toJson(list);
		}

		tv.setStatus(2);
		tv.setFiveTasks(str);

	}

	public void finishTask() {
		setContinudata();
		setContinueBtn(false);
		Intent it = new Intent(mcontext, UnTime2Activity.class);
		String str = gson.toJson(list);
		tv.setFiveTasks(str);
		Log.i("task", str);
		it.putExtra("data", tv);
		it.putExtra("colType", colType);
		if (colType.equals("1")) {
			it.putExtra("timetype", "不限时机");
		} else {
			it.putExtra("timetype", typeTv.getText().toString());
		}
		startActivityForResult(it, REQUEST_SAVE_REMARK);
		// startActivity(it);
	}

	// 执行切换后保存数据
	public void setContinudata() {
		// 重置页面，将已选数据添加到list
		dialToaltalTaskTV();
		List<subTaskVo> sblist = new ArrayList<subTaskVo>();
		String strType = "";
		for (int i = 0; i < isCheck.length; i++) {
			if (isCheck[i]) {
				if (null != list.get(personIndex).getSubTasks()) {
					sblist = list.get(personIndex).getSubTasks();
					switch (i) {
					case 0:
						strType += i;
						break;
					case 1:
						strType += 2;
						break;
					case 2:
						strType += 3;
						break;
					case 3:
						strType += 1;
						break;
					case 4:
						strType += i;
						break;
					default:
						break;
					}
				
				}

			}
		}
		subTaskVo sb = new subTaskVo();
		if (!isShowFram) {
			if (strType.length() < 1 || mAdapter.getMethodType() == -1) {
				return;
			}
			sb.setResults(mAdapter.getMethodType() + "");
		} else {

			if (strType.length() < 1 || framAdapter.getMethodType() == -1) {
				return;
			}
			sb.setResults(framAdapter.getMethodType() + "");
		}

		sb.setCol_type(strType + "");

		int result = Integer.parseInt(sb.getResults());

		// 存放不规则选项
		if (result == 4 || result == 5 || result == 6) {
			if (chooseRules.size() > 0) {
				List<RulesVo> wrongRules = new ArrayList<RulesVo>();
				wrongRules.addAll(chooseRules);
				sb.setUnrules(wrongRules);

			}
		}

		sb.setSubtaskId(subtaskId);
		subtaskId++;
		if (saveAttachmentsList.size() > 0) {
			List<Attachments> attList = new ArrayList<Attachments>();
			attList.addAll(saveAttachmentsList);
			sb.setAttachments(attList);
			saveAttachmentsList.clear();// 清除临时保存时机的文件
		}
		Log.i("filelist", saveAttachmentsList.size() + "");

		sblist.add(sb);
		finishList.add(0, sb);

		// qipaoTv.setText("+" + strType.length());
		qipaoTv.setVisibility(View.VISIBLE);
		qipaoTv.startAnimation(animation);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				qipaoTv.setVisibility(View.GONE);
			}
		}, 1000);
		list.get(personIndex).setSubTasks(sblist);
		Log.i("task", gson.toJson(list));
		resetFiveMethodAdapter();
		updataDb(personIndex);

		tv.setSaveAttachmentsList("");
		chooseRules.clear();
	}

	public void resetDialogChoose() {
		// departs.
	}

	public void setChooseList(int position) {
		switch (position) {
		case 0:

			break;

		default:
			break;
		}
	}

	// 设置结束观察按钮
	private void setEndButton() {
		int num1 = 0;
		if (AbStrUtil.isEmpty(tv.getTotalNum())) {
			num1 = 0;
		} else {
			num1 = Integer.parseInt(tv.getTotalNum());
		}

		int num2 = Integer.parseInt(colType);
		if (num1 >= num2) {
			finishBtn.setEnabled(true);
			if (!iscontinuTask && !colType.equals("1") && !tv.isUpdatedTask()) {
				new EndDialog(mcontext, R.style.SelectDialog).show();
			}

		} else {
			finishBtn.setEnabled(false);

		}

	}

	double rate;
	double rt_rate;
	private int state = 2;
	String progress = "0";
	public static final int SETENDBUTTION = 0x500102;
	public static final int SETETOATAL = 0x500103;
	public static final int SETETITLE = 0x500109;
	public static final int FRESHADAPTER = 0x500105;

	private void updataDb(int index) {

		int num = 0;

		int unDoNum = 0;// 没洗次数
		int errorNum = 0;
		jobTimesList.clear();
		for (JobListVo jCache : jobList) {
			JobListVo jv = new JobListVo();
			jv.setId(jCache.getId());
			jv.setName(jCache.getName());
			jobTimesList.add(jv);
		}
		for (planListDb pdb : list) {
			for (JobListVo jTL : jobTimesList) {
				if (pdb.getPpost().equals(jTL.getId())) {
					jTL.setTaskNum(jTL.getTaskNum() + pdb.getSubTasks().size());
				} else if (pdb.getPpost().equals("0")) {
					if (jTL.getId().equals("11")) {
						jTL.setTaskNum(jTL.getTaskNum()
								+ pdb.getSubTasks().size());
					}

				}
			}
			refreshJobTimesAdapter();
			// jobTimeAdapter.setdata(jobTimesList);
			for (subTaskVo sv : pdb.getSubTasks()) {
				sv.setPname(pdb.getPname());
				sv.setPpoName(pdb.getPpostName());
				sv.setWorkName(pdb.getWorkName());
				num = num + 1;
				if (sv.getResults().equals("0") || sv.getResults().equals("3")
						|| sv.getResults().equals("6")) {
					unDoNum = unDoNum + 1;
				}
				if (sv.getResults().equals("4") || sv.getResults().equals("5")) {
					errorNum = errorNum + 1;
				}

			}

		}
		tv.setTotalNum(finishList.size() + "");

		// totalTv.setText("已完成(" + finishList.size() + ")");
		int size = finishList.size();
		Message msg = new Message();
		msg.what = SETETOATAL;
		msg.obj = size;
		myHandler.handleMessage(msg);
		myHandler.sendEmptyMessage(FRESHADAPTER);
		double a = Double.parseDouble((num - unDoNum) + "")
				/ Double.parseDouble(num + "");
		// String b = new DecimalFormat("###,###,###.####").format(a);
		double b = Double.parseDouble((num - unDoNum - errorNum) + "")
				/ Double.parseDouble((num - unDoNum) + "");
		rate = Math.round(a * 10000);
		rt_rate = Math.round(b * 10000);
		if (colType.equals("1")) {

			// titleTv.setText(num + "");

			if (num >= 1) {
				progress = "100";
			}
			if (Integer.parseInt(progress) > 100) {
				progress = "100";
			}
			if (tv.isUpdatedTask()) {
				titleTv.setText("完成：" + num);
			}
		} else {
			progress = ((num * 100 / Integer.parseInt(colType)) + "");
			Log.i("11111progress", progress + "");
			if (Integer.parseInt(progress) >= 100) {
				progress = "100";
			}
			if (num / Integer.parseInt(colType) >= 1) {
			}
			titleTv.setText("完成：" + num + "/" + colType);
		}
		// if (progress.equals("100")) {
		// save.setTextColor(getResources().getColor(R.color.white));
		// }
		String str = gson.toJson(list);
		tv.setStatus(state);
		tv.setFiveTasks(str);
		Log.i("11111111", str);
		int rb = (int) Math.floor(rate / 100);
		tv.setYc_rate(rb + "");
		tv.setYc((num - errorNum - unDoNum) + "");
		tv.setValid_rate((int) Math.floor(rt_rate / 100) + "");
		Log.i("1111111111",
				rt_rate / 100 + "----" + (int) Math.floor(rt_rate / 100));
		tv.setProgress(progress);
		myHandler.sendEmptyMessage(SETENDBUTTION);
		if (!tv.isUpdatedTask()) {
			updateDb(tv);
		}
	}

	public void handleMyMessage(int what, Object obj) {
		Message myMsg = new Message();
		myMsg.what = what;
		if (null != obj) {
			myMsg.obj = obj;
		}
		myHandler.handleMessage(myMsg);
	}

	public void setTitleTv(int num) {
		titleTv.setText("完成：" + num + "/" + colType);
	}

	public void setToatleTv(int num) {
		totalTv.setText("已完成(" + num + ")");
	}

	public void refreshAdapter() {
		finishAdapter.setdata(finishList);
		pAdapter.notifyDataSetChanged();
	}

	// 更新数据库
	public void updateDb(TaskVo tv) {
		setLastTimes();
		try {
			if (!tv.isUpdatedTask() || tv.isAdd()) {
				DataBaseHelper.getDbUtilsInstance(mcontext)
						.update(tv, WhereBuilder.b("dbid", "=", tv.getDbid()));
			}
			Log.i("update", tv.getDbid() + "");

			Intent brodcastIntent = new Intent();
			brodcastIntent.setAction(CalendarMainActivity.UPDATA_ACTION);
			NewAccomplishTasksActivity.this.sendBroadcast(brodcastIntent);
			Log.i("update", tv.getFiveTasks() + "");
		} catch (DbException e) {
			DebugUtil.debug("updateDb", e.getMessage() + "");
			e.printStackTrace();
		}

	}

	// 添加时右移
	int x = 0;
	private List<TaskVo> newTaskList = new ArrayList<TaskVo>();

	boolean isclick = false;

	public void setListScroll() {
		if (list.size() > 25) {
			ToastUtils.showToast(mcontext, "已达人数上限");
			return;
		}
		addperson();
		// list.get(list.size()-1).setSelect_status(true);
		pAdapter.notifyDataSetChanged();
		isclick = true;
		selectItem(list.size() - 1);
		new Handler().post(new Runnable() {
			public void run() {
				// personlistView.setSelection(list.size()-1);

			}
		});

		DebugUtil.debug("setPintX", personlistView.getSelectedItemPosition()
				+ "");
		if (x == 0) {
			x = personlistView.getScrollX();
		}
		int x2 = (wh[0] - dp2Px(mcontext, 60)) / 3;
		personlistView.scrollTo(x + x2);
		x = x + x2;
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

	List<JobListVo> jobList = new ArrayList<JobListVo>();
	String jobs[];

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

	JobListAdapter popAdapter;

	public void setPersonIndex(int posintion) {
		personIndex = posintion;
		personTv.setText("(选中被调查人：" + list.get(personIndex).getPname() + ")");
		pAdapter.setCheck(personIndex);
	}

	public void setPersonIndex(String name) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getPname().equals(name)) {
				personIndex = i;
			}
		}

		personTv.setText("(选中被调查人：" + list.get(personIndex).getPname() + ")");
		pAdapter.setCheck(personIndex);
	}

	TaskVo tv;
	private String date;
	private String departId = "";
	private String hosId = "";
	private String task_id = "";
	private String taskType = "";
	private String colType = "";
	List<planListDb> list = new ArrayList<planListDb>();
	private Gson gson = new Gson();
	private boolean isWho = false;

	private void getData() {
		if (tv == null) {
			tv = (TaskVo) getIntent().getSerializableExtra("data");
		}

		if (tv.isTranning()) {
			isShowFram = true;
		} else {
			isShowFram = false;
		}
		String str = tv.getFiveTasks();
		// progress = tv.getProgress();
		// if (!AbStrUtil.isEmpty(progress) && progress.equals("100")) {
		// save.setTextColor(getResources().getColor(R.color.white));
		// }
		date = getIntent().getStringExtra("time");
		if (null == str) {
			str = "";
		}
		JSONArray jarr = null;
		try {
			jarr = new JSONArray(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (null != tv.getSaveAttachmentsList()
				&& tv.getSaveAttachmentsList().length() > 0) {
			saveAttachmentsList = gson.fromJson(tv.getSaveAttachmentsList()
					.toString(), new TypeToken<List<Attachments>>() {
			}.getType());
			fileList.addAll(saveAttachmentsList);
		}

		departId = tv.getDepartment();
		hosId = tools.getValue(Constants.HOSPITAL_ID);
		task_id = getIntent().getStringExtra("task_id");
		departTv.setText(tv.getDepartmentName());

		if (!AbStrUtil.isEmpty(getIntent().getStringExtra("type"))) {
			taskType = getIntent().getStringExtra("type");
			int type = Integer.parseInt(getIntent().getStringExtra("type"));
			switch (type) {
			case 0:
				colType = "1";

				break;
			case 1:
				colType = "10";

				break;
			case 2:
				colType = "15";

				break;
			case 3:
				colType = "20";

				break;
			case 4:
				colType = "40";

				break;

			default:
				colType = type - 10 + "";
				break;
			}

			if (colType.equals("1")) {
				typeTv.setText("不限时机");
				typeTv.setVisibility(View.GONE);
			} else {
				typeTv.setText(colType + "个时机");
			}
		}

		if (null == hosId) {
			hosId = "";
		}

		if (tv.isWho()) {
			isWho = true;
		}

		if (tv.getFiveTasksInfo().size() <= 0
				&& (jarr == null || str.equals("[]"))) {
			titleTv.setText("完成:0" + "/" + colType);

			// pdb.setHospital(tools.getValue(Constants.HOSPITAL_ID));
			for (int i = 0; i < 3; i++) {
				addperson();
			}

		} else {
			if (tv.getTask_id() != 0) {
				tv.setUpdatedTask(true);
				backText.setText("取消编辑");
				tv.setStatus(2);
				task_id = tv.getTask_id() + "";
				list = tv.getFiveTasksInfo();// 从服务器获取的
				if (null == list || list.size() == 0) {
					List<planListDb> cachelist = gson.fromJson(
							tv.getFiveTasks(),
							new TypeToken<List<planListDb>>() {
							}.getType());
					list.addAll(cachelist);
				}
				if (tv.getFlag() == 1) {
					tv.setWho(true);
					isWho = true;
				}
				List<planListDb> catheList = new ArrayList<planListDb>();
				catheList.addAll(list);
				titleTv.setText("完成:0" + "/" + colType);
				if (list.size() < 3) {
					int size = catheList.size();
					for (int i = size; i < 3; i++) {
						addperson();
					}
				}
			} else {
				list = gson.fromJson(jarr.toString(),
						new TypeToken<List<planListDb>>() {
						}.getType());
			}
		}

		if (list.size() > 0) {
			jobTimesList.clear();

			for (JobListVo jCache : jobList) {
				JobListVo jv = new JobListVo();
				jv.setId(jCache.getId());
				jv.setName(jCache.getName());
				jobTimesList.add(jv);

			}

			for (planListDb pdb : list) {
				if (tv.isUpdatedTask()) {
					for (JobListVo jv : jobList) {
						if (pdb.getPpost().equals(jv.getId())
								&& !pdb.getPpost().equals("11")) {
							pdb.setPpostName(jv.getName());
						}

					}
					for (JobListVo jv : jobTypelist) {
						if (pdb.getWork_type().equals(jv.getId())) {
							pdb.setWorkName(jv.getName());
						}

					}
				}

				for (JobListVo jTL : jobTimesList) {

					if (pdb.getPpost().equals(jTL.getId())) {
						jTL.setTaskNum(jTL.getTaskNum()
								+ pdb.getSubTasks().size());
					} else if (pdb.getPpost().equals("0")) {
						if (jTL.getId().equals("11")) {
							jTL.setTaskNum(jTL.getTaskNum()
									+ pdb.getSubTasks().size());
						}

					}

				}

				for (subTaskVo sv : pdb.getSubTasks()) {

					sv.setPname(pdb.getPname());
					sv.setPpoName(pdb.getPpostName());
					sv.setWorkName(pdb.getWorkName());

					sv.setSubtaskId(subtaskId);
					subtaskId++;
					finishList.add(sv);

					if (null == sv.getAttachments()) {
						// List<Attachments> list=new ArrayList<Attachments>()
						// Attachments att=new Attachments();
						// sv.getAttachments().add(att);
					} else {
						for (Attachments att : sv.getAttachments()) {
							if (AbStrUtil.isEmpty(att.getState())) {
								att.setState("2");
							}
							fileList.add(att);

						}
					}
				}

			}
			totalTv.setText("已完成(" + finishList.size() + ")");
			totalfileTv.setText("照片与录音(" + fileList.size() + ")");
			tv.setTotalNum(finishList.size() + "");
		}

		if (!colType.equals("1")) {
			titleTv.setText("完成:" + finishList.size() + "/" + colType);
		} else {
			if (tv.isUpdatedTask()) {
				titleTv.setText("完成:" + finishList.size());
			} else {
				startTimer();
				//startCutDownTimer();
			}
		}
		Log.i("111111", list.size() + "");

	}

	public void ResetJobTimesListView() {
		jobTimesListView.removeAllViews();
		for (JobListVo jTL2 : jobTimesList) {

			if (jTL2.getTaskNum() > 0) {

				TextView textView = new TextView(mcontext);
				textView.setTextColor(getResources().getColor(R.color.black));
				textView.setText(jTL2.getName() + ":" + jTL2.getTaskNum());
				textView.setPadding(10, 0, 10, 0);
				jobTimesListView.addView(textView);

			}
		}
		TextView textView2 = new TextView(mcontext);
		textView2.setPadding(dp2Px(mcontext, 23), 0, dp2Px(mcontext, 10), 0);
		jobTimesListView.addView(textView2);
	}

	public void addperson() {
		planListDb pdb = new planListDb();
		int x = 0;
		for (int j = 0; j < personItem.length; j++) {
			boolean needContinue = true;
			for (planListDb pb : list) {
				if (pb.getPname().equals(personItem[j])) {
					needContinue = false;
					break;
				}
			}
			if (needContinue) {
				x = j;
				break;
			}

		}
		pdb.setPname(personItem[x]);
		List<subTaskVo> list1 = new ArrayList<subTaskVo>();
		pdb.setSubTasks(list1);
		pdb.setDepartment(departId);
		pdb.setHospital(hosId);
		pdb.setTask_id(task_id);
		pdb.setMission_time(date);
		pdb.setTask_type(taskType);
		if (AbStrUtil.isEmpty(tv.getDefaltJobId())) {
			pdb.setPpost("0");
		} else {
			pdb.setPpost(tv.getDefaltJobId());
			pdb.setPpostName(tv.getDefaltJobName());
		}
		if (AbStrUtil.isEmpty(tv.getDefaltWorkType())) {
			pdb.setWork_type("");
			pdb.setWorkName("");
		} else {
			pdb.setWork_type(tv.getDefaltWorkType());
			pdb.setWorkName(tv.getDefaltWorkTypeName());
		}
		list.add(pdb);
	}

	// 添加文件
	List<Attachments> fileList = new ArrayList<Attachments>();// 总的文件

	public List<Attachments> saveAttachmentsList = new ArrayList<Attachments>();// 待保存的数据//
																				// 时机对应的文件

	public void addFile(String file, int type, String time) {
		dialToatalFileTv();
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		Attachments att = new Attachments();
		att.setDate(date);
		att.setState("1");
		att.setFile_name(file);
		att.setFile_type(type + "");
		if (!AbStrUtil.isEmpty(time) && type == 2) {
			att.setTime(time);
		} else {
			att.setTime("");
		}
		totalfileTvQipao.setVisibility(View.VISIBLE);
		totalfileTvQipao.startAnimation(animation);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				totalfileTvQipao.setVisibility(View.GONE);
			}
		}, 1000);
		fileList.add(0, att);
		saveAttachmentsList.add(att);
		totalfileTv.setText("照片与录音(" + fileList.size() + ")");
		Log.i("1111111", gson.toJson(saveAttachmentsList));
		tv.setSaveAttachmentsList(gson.toJson(saveAttachmentsList));
		fileAdapter.notifyDataSetChanged();

	}

	// 更多弹出框
	boolean isStartRecord = true;
	public int recordStadio = 0;
	private Timer timerReciprocal;
	LayoutInflater inflater;
	private TextView yesTv, cancelTv, stateTv;
	private Button mBtnRcd;
	RelativeLayout playLayout;
	RoundProgressBar progressBar;
	Button playButton;
	LinearLayout recordLay, takephotolay, chooseLocal, lay1;

	MediaRecorder recorder;
	public static final int RECORD_STADIO_INITIAL = 0;// 未开始
	public static final int RECORD_STADIO_ISSTART = 1;// 已开始录制
	public static final int RECORD_STADIO_IS_STOPRECORD = 2;//
	public static final int RECORD_STADIO_PLAYING = 3;
	public static final int RECORD_STADIO_STOPPLAY = 4;
	public static final String MAX_UPLOAD_NUM = "maxUploadNum";
	public static int maxPhotoNums = 4;
	private static String mCurrentPhotoPath;
	private static final int REQUEST_TAKE_PHOTO = 0x0; // 拍照
	private static final int REQUEST_UPLOAD_PHOTO = 0x1; // 上传图片
	private static final int REQUEST_SELECT_LOCAL_PHOTO = 0x2; // 选择本地图片
	private static final int REQUEST_CUT_PHOTO = 0x3; // 剪切图片

	public static final int REQUEST_SAVE_REMARK = 0X10; // 保存小结

	private static String imageId; // 图片id

	List<String> mimageIdList = new ArrayList<String>(); // 图片id
	List<String> msamllPathList = new ArrayList<String>(); // 图片路径

	private Uri mphotoUri; // 获取到的图片URI
	private int size = 0;

	// 拍照
	/**
	 * onPhotographClick:【拍照按钮事件】. <br/>
	 * .@param arg0.<br/>
	 */
	// private void onPhotographClick() {
	// Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	// try {
	// // 指定存放拍摄照片的位置
	// File file = createImageFile();
	// takePictureIntent.putExtra("output", Uri.fromFile(file));
	// startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * onLocalClick:【选择本地相册】. <br/>
	 * .@param arg0.<br/>
	 */
	// private void onLocalClick() {
	// Intent intent = new Intent(this, SelectPhotoActivity.class);
	// intent.putExtra("size", size);
	// PhotoNumsBean.getInstant().setNumber(1);
	// startActivityForResult(intent, REQUEST_SELECT_LOCAL_PHOTO);
	// }

	private static final int CODE_RESULT_CANCEL = -0x2; // 取消
	List<String> resullist = new ArrayList<String>();

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.i("test", "requestCode:" + requestCode);
		if (resultCode == CODE_RESULT_CANCEL) {
			// 返回结果异常
			finish();
		} else {
			switch (requestCode) {
			case REQUEST_TAKE_PHOTO:
				// 拍照返回结果
				if (resultCode == Activity.RESULT_OK) {
					startUploadActivity();
				}
				break;
			case REQUEST_SELECT_LOCAL_PHOTO:
				// 选择图片返回结果
				if (intent != null) {
					size += intent.getIntExtra("size", 0);
					if (intent.hasExtra("result")) {
						resullist = (List<String>) intent.getExtras()
								.getSerializable("result");
						startUploadActivity();
					} else {
						finish();
					}
				}
				break;
			case REQUEST_SAVE_REMARK:
				if (intent != null) {
					tv = (TaskVo) intent.getSerializableExtra("data");

					JSONArray jarr = null;
					try {
						jarr = new JSONArray(tv.getFiveTasks());
						DebugUtil.debug("untime_list", "jarr.toString()>>"
								+ jarr.toString());
						List<planListDb> list2 = gson.fromJson(jarr.toString(),
								new TypeToken<List<planListDb>>() {
								}.getType());
						list.clear();
						list.addAll(list2);
						personTv.setText("(选中被调查人："
								+ list.get(personIndex).getPname() + ")");
						pAdapter.notifyDataSetChanged();
						resetFinishAdapter();

						departTv.setText(tv.getDepartmentName());

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				// String r = intent.getStringExtra("remark");
				// r = null == r ? "" : r;
				// tv.setRemark(r);
				//
				// String is_training= intent.getStringExtra("is_training");
				// if(!TextUtils.isEmpty(is_training)){
				// tv.setIs_training(is_training);
				// }
				//
				// String training_recycle=
				// intent.getStringExtra("training_recycle");
				// if(!TextUtils.isEmpty(training_recycle)){
				// tv.setTraining_recycle(training_recycle);
				// }
				//
				// String equip_examine= intent.getStringExtra("equip_examine");
				// if(!TextUtils.isEmpty(equip_examine)){
				// tv.setEquip_examine(equip_examine);
				// }
				//
				// String feedback_obj= intent.getStringExtra("feedback_obj");
				// if(!TextUtils.isEmpty(feedback_obj)){
				// tv.setFeedback_obj(feedback_obj);
				// }
				//
				// }
				break;
			default:
				break;
			}
		}
	}

	public void refreshJobTimesAdapter() {
		jobTimesList.clear();
		for (JobListVo jCache : jobList) {
			JobListVo jv = new JobListVo();
			jv.setId(jCache.getId());
			jv.setName(jCache.getName());
			jobTimesList.add(jv);
		}
		for (planListDb pdb : list) {
			for (JobListVo jTL : jobTimesList) {
				if (pdb.getPpost().equals(jTL.getId())) {
					jTL.setTaskNum(jTL.getTaskNum() + pdb.getSubTasks().size());
				} else if (pdb.getPpost().equals("0")) {
					if (jTL.getId().equals("11")) {
						jTL.setTaskNum(jTL.getTaskNum()
								+ pdb.getSubTasks().size());
					}

				}
			}

		}
		ResetJobTimesListView();
		// jobTimeAdapter.setdata(jobTimesList);
	}

	public void resetFinishAdapter() {
		finishList.clear();
		for (planListDb pdb : list) {
			for (subTaskVo sv : pdb.getSubTasks()) {
				sv.setPname(pdb.getPname());
				sv.setPpoName(pdb.getPpostName());
				sv.setWorkName(pdb.getWorkName());
				finishList.add(sv);
			}
		}

		finishAdapter.notifyDataSetChanged();
	}

	/**
	 * startUploadActivity:【开启上传图片activity】. <br/>
	 * ..<br/>
	 */
	public static final int COMPRESS_IMAGE = 0x17;
	public static final int CHANGE_PERSON_ADAPTER = 0x20;
	public static final int MOVE_POINT = 0x21;
	public static final String CLOSE_ACTIVITY = "close";
	int item_width = 0;
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
						addFile(file.toString(), 1, "");
						// } else {
						// ToastUtils.showToast(mcontext, "非法图片");
						// }
					}
					break;
				case CHANGE_PERSON_ADAPTER:
					pAdapter.notifyDataSetChanged();
					DebugUtil.debug("dySpinner", "set position>" + personIndex);
					setJobListAdapter();
					break;
				case MOVE_POINT:
					try {
						View v = (View) msg.obj;
						if (item_width == 0) {
							item_width = v.getWidth() / 2;
						}
						setPintX((int) v.getX(), item_width);
					} catch (Exception e) {
						// TODO: handle exception
					}
					break;
				case SETENDBUTTION:
					setEndButton();
					break;
				case SETETOATAL:
					// ToastUtils.showToast(mcontext, msg.obj.toString());
					setToatleTv(Integer.parseInt(msg.obj.toString()));
					break;
				case FRESHADAPTER:
					refreshAdapter();
					break;
				default:
					break;
				}
			}
		}

	};

	private void startUploadActivity() {
		Intent data = new Intent();
		data.putExtra("picList", (Serializable) resullist);
		Log.i("111111111", resullist.size() + "");
		CompressImageUtil.getCompressImageUtilInstance().startCompressImage(
				myHandler, COMPRESS_IMAGE, resullist.get(0));

		// File file = new File(resullist.get(0) + "");
		// Log.i("1111", file.exists() + "");
		// // if (file.exists() && file.length() > 6.5 * 1024) {
		// addFile(resullist.get(0).toString(), 1, "");
		setResult(RESULT_OK, data);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	public boolean iscontinuTask = false;
	private MyBrodcastReceiver brodcast;

	public class EndDialog extends Dialog {

		private TextView showBtn;
		private TextView btn_enter;
		private TextView cancleBtn;
		View.OnClickListener listener;
		LinearLayout framView, guidView;

		/**
		 * Creates a new instance of MyDialog.
		 */
		public EndDialog(Context context, int theme) {
			super(context, theme);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO 自动生成的方法存根
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);

			setContentView(R.layout.dialog_enddialog);
			setCancelable(false);
			showBtn = (TextView) this.findViewById(R.id.show);
			showBtn.setText("您的督导已达设定值，是否提交？");
			btn_enter = (TextView) this.findViewById(R.id.yes);
			btn_enter.setText("提交");
			btn_enter.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finishTask();
					dismiss();
				}
			});
			framView = (LinearLayout) this.findViewById(R.id.framView);
			guidView = (LinearLayout) this.findViewById(R.id.guidView);
			cancleBtn = (TextView) this.findViewById(R.id.cacle);
			framView.setVisibility(View.GONE);
			guidView.setVisibility(View.GONE);
			cancleBtn.setText("继续");

			cancleBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					iscontinuTask = true;
					dismiss();
				}
			});

		}
	}

	private void registerBroadcast() {
		brodcast = new MyBrodcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(CLOSE_ACTIVITY)) {
					finish();
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CLOSE_ACTIVITY);
		registerReceiver(brodcast, intentFilter);
	}

	DeletDialog deletDialog;

	public void showDeletFile(final String file, final int position) {
		deletDialog = new DeletDialog(mcontext, R.style.SelectDialog,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						deletFile(file, position);
						deletDialog.dismiss();
					}
				});
		deletDialog.show();

	}

	public void deletFile(String file, int position) {
		for (planListDb pdb : list) {
			for (subTaskVo sb : pdb.getSubTasks()) {
				if (null != sb.getAttachments()) {
					int index = -1;
					int deletIndex = -1;
					boolean needRemove = false;
					for (Attachments att : sb.getAttachments()) {
						index++;
						try {

							if (null != att.getFile_name()
									&& att.getFile_name().equals(file)) {
								deletIndex = index;
								needRemove = true;
							}
						} catch (Exception e) {
							// TODO: handle exception
						}

					}
					if (needRemove) {
						sb.getAttachments().remove(deletIndex);
					}
				}
			}

		}
		fileList.remove(position);
		totalfileTv.setText("照片与录音(" + fileList.size() + ")");

		fileAdapter.notifyDataSetChanged();

	}

	public void showJobDialog(final int position, String name, String workId,
			String jobId) {
		Dialog dialog = new JobDialog(mcontext, name, workId, jobId,
				jobTypelist, jobList, new JobDialog.ChooseItem() {
					@Override
					public void getJobChoosePosition(int positon1, int position2) {

					}

					@Override
					public void getJobChoosePosition(String name, int positon1,
							int position2) {
						if (position2 >= 0) {
							list.get(position).setPpostName(
									jobList.get(position2).getName());
							list.get(position).setPpost(
									jobList.get(position2).getId());
						} else {
							list.get(position).setPpostName("");
							list.get(position).setPpost("11");
						}
						if (positon1 >= 0) {
							list.get(position).setWork_type(
									jobTypelist.get(positon1).getId());
							list.get(position).setWorkName(
									jobTypelist.get(positon1).getName());
						} else {
							list.get(position).setWork_type("");
							list.get(position).setWorkName("");
						}
						if (!AbStrUtil.isEmpty(name)) {
							list.get(position).setPname(name.trim());
							if (position == personIndex) {
								personTv.setText("(选中被调查人："
										+ list.get(personIndex).getPname()
										+ ")");
							}
						}
						pAdapter.notifyDataSetChanged();
						refreshJobTimesAdapter();
						// jobTv.setText(jobList.get(arg2).getName());
						resetFinishAdapter();

					}
				});

		dialog.show();
	}

	private List<JobListVo> jobTypelist;

	public void getJobCacheData() {
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

	List<WrongRuleVo> ruleList = new ArrayList<WrongRuleVo>();

	public void getWrongRules() {
		String jsonStr = SharedPreferencesUtil.getString(mcontext,
				"rules_json", null);
		if (!TextUtils.isEmpty(jsonStr)) {
			try {
				JSONObject jsonObject = new JSONObject(jsonStr);
				JSONArray jarr = jsonObject.optJSONArray("unrules");
				ruleList = gson.fromJson(jarr.toString(),
						new TypeToken<List<WrongRuleVo>>() {
						}.getType());
				Log.i("111111111", ruleList.size() + "");
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	// 下载语音

	// 播放语音
	ImageView view;
	private boolean isLoading;
	private String recordName = "";

	public void playRecord(String fileName, ImageView view) {
		showprocessdialog();
		File pdffile = new File(getPath() + "/" + fileName);
		this.recordName = pdffile.toString();
		this.view = view;
		if (isLoading) {
			return;
		}
		if (pdffile.exists()) {
			// playRecord();
			pdffile.delete();
		}
		download(fileName);
	}

	// 下载部分
	private void download(String fileName) {
		showprocessdialog();
		isLoading = true;
		// 获取SD卡目录
		String dowloadDir = getPath();
		File file = new File(dowloadDir);
		Log.i("11111111", file.toString());
		// 创建下载目录
		if (!file.exists()) {
			file.mkdirs();
			Log.i("11111111", file.exists() + "");
		}

		// 读取下载线程数，如果为空，则单线程下载
		int downloadTN = 1;
		// 如果下载文件名为空则获取Url尾为文件名
		String downloadUrl = WebUrl.FILE_LOAD_URL + fileName;
		String pdfName = fileName;
		Log.i("11111111", downloadUrl);
		// 开始下载前把下载按钮设置为不可用
		// 进度条设为0
		// downloadProgressBar.setProgress(0);
		// 启动文件下载线程
		new downloadTask(downloadUrl, Integer.valueOf(downloadTN), dowloadDir
				+ pdfName).start();
	}

	// 下载语音部分
	Handler handler = new Handler() {
		private boolean isLoading;

		@Override
		public void handleMessage(Message msg) {
			// 当收到更新视图消息时，计算已完成下载百分比，同时更新进度条信息
			int progress = (Double
					.valueOf((downloadedSize * 1.0 / fileSize * 100)))
					.intValue();
			if (progress >= 100) {
				isLoading = false;
				dismissdialog();
				fileAdapter.playMusic(recordName, view);
				//
			} else {
				// ToastUtils.showToast(mcontext, "当前进度:" + progress + "%");
			}
			// downloadProgressBar.setProgress(progress);
		}

	};
	private int fileSize = 0;
	private int downloadedSize = 0;

	public class downloadTask extends Thread {
		private int blockSize, downloadSizeMore;
		private int threadNum = 1;
		String urlStr, threadNo, fileName;

		public downloadTask(String urlStr, int threadNum, String fileName) {
			this.urlStr = urlStr;
			this.threadNum = threadNum;
			this.fileName = fileName;
		}

		@Override
		public void run() {
			FileDownloadThread[] fds = new FileDownloadThread[threadNum];
			try {
				URL url = new URL(urlStr);
				URLConnection conn = url.openConnection();
				conn.getHeaderField("Content-Length");
				// 获取下载文件的总大小
				// fileSize = conn.getContentLength();
				fileSize = conn.getContentLength();
				Log.i("1111111",
						fileSize + "" + conn.getHeaderField("Content-Length"));
				// 计算每个线程要下载的数据量
				blockSize = fileSize / threadNum;
				// 解决整除后百分比计算误差
				downloadSizeMore = (fileSize % threadNum);
				File file = new File(fileName);
				for (int i = 0; i < threadNum; i++) {
					// 启动线程，分别下载自己需要下载的部分
					FileDownloadThread fdt = new FileDownloadThread(url, file,
							i * blockSize, (i + 1) * blockSize - 1);
					fdt.setName("Thread" + i);
					fdt.start();
					fds[i] = fdt;
				}
				boolean finished = false;
				while (!finished) {
					// 先把整除的余数搞定
					downloadedSize = downloadSizeMore;
					finished = true;
					for (int i = 0; i < fds.length; i++) {
						downloadedSize += fds[i].getDownloadSize();
						if (!fds[i].isFinished()) {
							finished = false;
						}
					}
					// 通知handler去更新视图组件
					handler.sendEmptyMessage(0);
					// 休息1秒后再读取下载进度
					sleep(1000);
				}
			} catch (Exception e) {

			}

		}
	}

	public static final String DOWNLOAD_FOLDER_NAME = "Deya/pdf";
	private static final int SEND_FIAL = 0x200045;
	private static final int SEND_SUCESS = 0x200046;

	public String getPath() {
		// TODO Auto-generated method stub
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			return new StringBuilder(Environment.getExternalStorageDirectory()
					.getAbsolutePath()).append(File.separator)
					.append(DOWNLOAD_FOLDER_NAME).append(File.separator)
					.toString();
		}
		return "";
	}

}
