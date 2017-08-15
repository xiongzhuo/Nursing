package com.deya.hospital.form;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.timepicker.ScreenInfo;
import com.baoyz.timepicker.WheelMain;
import com.deya.acaide.R;
import com.deya.hospital.adapter.DetailImageListAdapter;
import com.deya.hospital.adapter.DetailImageListAdapter.ImageAdapterInter;
import com.deya.hospital.adapter.DetailRecordFileLisAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.NewPhotoMultipleActivity;
import com.deya.hospital.base.SelectPhotoActivity;
import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.base.img.PhotoNumsBean;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.supervisor.PartTimeStaffDialog;
import com.deya.hospital.supervisor.PartTimeStaffDialog.PDialogInter;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.FileActions;
import com.deya.hospital.util.FileDownloadThread;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.widget.popu.PopuRecord;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.WorkSpaceFragment;
import com.deya.notification.NotificationReceiver;
import com.example.calendar.CalendarMainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.DbException;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FormSupervisoryBookAcitivity extends BaseActivity implements
		OnClickListener {
	EditText checkContentEdt;
	EditText problemEdt;
	EditText sugesstEdt;
	GridView photoGv;
	DetailImageListAdapter imgAdapter;
	TaskVo tv = new TaskVo();
	List<Attachments> imgList = new ArrayList<Attachments>();
	List<Attachments> fileList = new ArrayList<Attachments>();
	private Button submit;
	private String creatTime = "";
	private String missonTime = "";
	RelativeLayout rlBack;
	private ListView recordGv;
	private DetailRecordFileLisAdapter fileAdapter;
	String recordName = "";
	EditText changeEdt;
	TextView departTv;
	TextView shareTv;
	Button shareBtn;
	LinearLayout recordLay;
	private LinearLayout photoLay;
	private CommonTopView topView;
	private TextView timeTv;
	private ImageView switchBtn;
	TaskVo formData;
	private RelativeLayout timelay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_superversion);
		tools = new Tools(mcontext, Constants.AC);

		if (getIntent().hasExtra("formdata")) {// 未上传过的任务，防止点击此处提交按钮重复提交//
												// ，绑定在表格任务中的督导本还没提交 id没生成
			formData = (TaskVo) getIntent().getSerializableExtra("formdata");
			tv = gson.fromJson(formData.getMain_remark(), TaskVo.class);
			findViewById(R.id.suggestlay).setVisibility(View.GONE);
		} else {
			tv = (TaskVo) getIntent().getSerializableExtra("data");
		}
		getData();
		intTopView();
		initView();
	}

	private void intTopView() {
		topView = (CommonTopView) this.findViewById(R.id.topView);
		topView.setTitle(AbStrUtil.isEmpty(tv.getName()) ? tv
				.getMain_remark_name() : tv.getName());
		topView.onbackClick(this, new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != formData) {
					formData.setMain_remark(gson.toJson(tv));
					if (formData.getStatus() == 3) {
						formData.setStatus(1);
						try {
							DataBaseHelper
									.getDbUtilsInstance(mcontext)
									.save(formData);
						} catch (DbException e) {
							finish();
							e.printStackTrace();
						}
						// TaskUtils.onAddTaskInDb(formData);
					} else {
						formData.setStatus(1);
						TaskUtils.onUpdateTaskById(formData);
					}
					Intent brodcastIntent = new Intent();
					brodcastIntent
							.setAction(WorkSpaceFragment.UPDATA_ACTION);
					FormSupervisoryBookAcitivity.this
							.sendBroadcast(brodcastIntent);
					finish();
				} else {
					String sugesstion = sugesstEdt.getText().toString();
					if (!tv.getCheck_content().equals(checkContentEdt.getText().toString())
							|| !tv.getDeal_suggest().equals(sugesstion)
							|| !problemEdt.getText().toString()
									.equals(tv.getExist_problem())
							|| !changeEdt.getText().toString()
									.equals(tv.getImprove_suggest())) {
						isChanged = true;
						
					}
					if(isChanged){
						showTips();
					}else{
						finish();
					}
				}

			}
		});
		topView.onRightClick(this, new OnClickListener() {

			@Override
			public void onClick(View v) {
				showRecordPopWindow();
			}
		});
	}
	PartTimeStaffDialog tipdialog;
	public void showTips() {
		tipdialog = new PartTimeStaffDialog(mcontext, false, "是否保存本次编辑？",
				new PDialogInter() {
					@Override
					public void onEnter() {
						resetMainRemark();
						tv.setUpdatedTask(true);
						TaskUtils.onUpdateTaskById(tv);
						Intent brodcastIntent = new Intent();
						brodcastIntent
								.setAction(CalendarMainActivity.UPDATA_ACTION);
						FormSupervisoryBookAcitivity.this
								.sendBroadcast(brodcastIntent);
						setResult(0x136);
						finish();
					}

					@Override
					public void onCancle() {
						finish();
					}
				});
		tipdialog.show();
	}
	boolean isChanged = false;

	private void initView() {

		checkContentEdt = (EditText) this.findViewById(R.id.checkContentEdt);
		problemEdt = (EditText) this.findViewById(R.id.problemEdt);
		sugesstEdt = (EditText) this.findViewById(R.id.sugesstEdt);
		photoGv = (GridView) this.findViewById(R.id.photoGv);
		timeTv = (TextView) this.findViewById(R.id.timeTv);
		timeTv.setText(tv.getRemind_date());
		timeTv.setOnClickListener(this);
		timelay = (RelativeLayout) this.findViewById(R.id.timelay);
		switchBtn = (ImageView) this.findViewById(R.id.switchBtn);
		switchBtn.setOnClickListener(this);
		imgAdapter = new DetailImageListAdapter(mcontext, imgList,
				new ImageAdapterInter() {

					@Override
					public void ondeletImage(int position) {
						imgList.remove(position);
						imgAdapter.setImageList(imgList);

					}
				});
		photoGv.setAdapter(imgAdapter);
		recordGv = (ListView) this.findViewById(R.id.recordGv);
		fileAdapter = new DetailRecordFileLisAdapter(mcontext, fileList,
				new FileActions() {

					@Override
					public void onStopMedia(int position) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPlayMedia(String fileName, ImageView view) {
						playRecord(fileName, view);
					}

					@Override
					public void onDeletMedia(int position) {

					}

					@Override
					public void onDeletFile(int position) {
						isChanged = true;
						deletRecord(position);

					}
				});
		recordGv.setAdapter(fileAdapter);
		changeEdt = (EditText) this.findViewById(R.id.sugesst_need_change_Edt);
		if (null != tv.getImprove_suggest()) {
			changeEdt.setText(tv.getImprove_suggest());
		}
		departTv = (TextView) this.findViewById(R.id.departTv);
		departTv.setText(tv.getDepartmentName());

		submit = (Button) this.findViewById(R.id.sumbmitBtn);
		// if(!tv.isUpdatedTask()){
		// submit.setEnabled(false);
		// }
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("1111111", "什么玩意");
				// if (checkContentEdt.getText().toString().trim().length() >=
				// 1) {
				submit.setEnabled(false);
				resetMainRemark();
				if (null != formData) {
					formData.setMain_remark(gson.toJson(tv));
					if (formData.getStatus() == 3) {
						formData.setStatus(1);
						try {
							DataBaseHelper
									.getDbUtilsInstance(mcontext)
									.save(formData);
						} catch (DbException e) {
							finish();
							e.printStackTrace();
						}
						// TaskUtils.onAddTaskInDb(formData);
					} else {
						formData.setStatus(1);
						TaskUtils.onUpdateTaskById(formData);
					}
				} else {
					tv.setUpdatedTask(true);
					TaskUtils.onUpdateTaskById(tv);
				}

				Intent brodcastIntent = new Intent();
				brodcastIntent.setAction(WorkSpaceFragment.UPDATA_ACTION);
				FormSupervisoryBookAcitivity.this.sendBroadcast(brodcastIntent);
				finish();
				// } else {
				// ToastUtils.showToast(mcontext, "检查内容不能为空");
				// }

			}
		});
		setdata();
		shareBtn = (Button) this.findViewById(R.id.shareBtn);

		if (tv.getStatus() == 0) {
			shareBtn.setVisibility(View.VISIBLE);
		} else if (tv.getStatus() == 1 && getIntent().hasExtra("detail")) {
			shareBtn.setVisibility(View.VISIBLE);
		} else {
			shareBtn.setVisibility(View.GONE);
		}
		shareBtn.setOnClickListener(this);
		setShowType();
	}

	public void deletRecord(int position) {
		fileList.remove(position);
		fileAdapter.notifyDataSetChanged();
	}

	protected void submitRemark() {
		String stredt = changeEdt.getText().toString();
		if (stredt.trim().length() <= 0) {
			ToastUtils.showToast(mcontext, "改进意见不能为空！");
			return;
		}
		JSONObject job = new JSONObject();

		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("check_content", checkContentEdt.getText().toString());
			job.put("exist_problem", problemEdt.getText().toString());
			job.put("deal_suggest", sugesstEdt.getText().toString());
			job.put("id", tv.getTmp_id());
			job.put("improve_suggest", stredt);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				FormSupervisoryBookAcitivity.this, SEND_SUCESS, SEND_FIAL, job,
				"grid/submitGridRemark");
	}

	public static final int ADD_PRITRUE_CODE = 9009;
	// 压缩图片的msg的what

	public static final int COMPRESS_IMAGE = 0x17;

	private List<TaskVo> newTaskList = new ArrayList<TaskVo>();
	private Tools tools;
	private String departmentName;
	private String departmentId;
	private List<Attachments> supervisorFileList = new ArrayList<Attachments>();
	private Gson gson = new Gson();
	private boolean isLoading;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photoLay:
			tokephote();
			break;
		case R.id.shareBtn:
			if (tv.getStatus() == 0) {
				showShare(WebUrl.LEFT_URL + "/supervisor/supervisorShare?id="
						+ tv.getTask_id());
				Log.i("11111111", WebUrl.LEFT_URL
						+ "/supervisor/supervisorShare?id=" + tv.getTask_id());
			} else {
				Toast.makeText(mcontext, "请到工作间首页选择上传后的督导本进行分享", 2000).show();
			}
			break;
		case R.id.recordLay:

			break;

		case R.id.timeTv:
			showTimeDialog();
			break;
		case R.id.switchBtn:
			setShowType();
			break;
		default:
			break;
		}

	}

	Dialog timedialog;

	private void showTimeDialog() {
		timedialog = new MyDialog(mcontext, R.style.SelectDialog);
		timedialog.show();
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
			tv.setRemind_date("");
			tv.setIsShow("1");
		}

	}

	@Override
	protected void onDestroy() {
		if (null != timedialog) {
			timedialog.dismiss();
		}
		super.onDestroy();

	}

	public void tokephote() {
		Intent takePictureIntent = new Intent(
				FormSupervisoryBookAcitivity.this,
				NewPhotoMultipleActivity.class);
		takePictureIntent.putExtra(NewPhotoMultipleActivity.MAX_UPLOAD_NUM, 9);
		takePictureIntent.putExtra("type", "1");
		takePictureIntent.putExtra("size", "0");
		startActivityForResult(takePictureIntent, ADD_PRITRUE_CODE);
	}

	void setdata() {
		sugesstEdt.setText(tv.getDeal_suggest());
		problemEdt.setText(tv.getExist_problem());
		checkContentEdt.setText(tv.getCheck_content());
	}

	private void getData() {
		departmentName = tv.getDepartmentName();
		departmentId = tv.getDepartment();
		creatTime = tv.getMission_time();
		String atts = tv.getFileList();
		if (!AbStrUtil.isEmpty(atts)) {
			supervisorFileList = gson.fromJson(atts,
					new TypeToken<List<Attachments>>() {
					}.getType());
		}
		if (null == supervisorFileList || supervisorFileList.size() < 1) {
			supervisorFileList = tv.getAttachments();
		}
		for (Attachments att : supervisorFileList) {
			if (null == att.getState() || !att.getState().equals("1")) {
				att.setState("2");
			}
			if (att.getFile_type().equals("1")) {
				imgList.add(att);
				Log.i("super", att.getFile_name());
			} else if (att.getFile_type().equals("2")) {
				fileList.add(att);
			}
		}

	}

	// 播放语音
	ImageView view;

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
			if (progress == 100) {
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

	public void dosubmit() {
		String stredt = changeEdt.getText().toString();

		if (stredt.trim().length() <= 0) {
			ToastUtils.showToast(mcontext, "改进意见不能为空！");
			return;
		}
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();

		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("task_id", tv.getTask_id());
			job.put("improve_suggest", stredt);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				FormSupervisoryBookAcitivity.this, SEND_SUCESS, SEND_FIAL, job,
				"supervisor/improveSuggest");
	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case SEND_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							setResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case SEND_FIAL:
					ToastUtils.showToast(FormSupervisoryBookAcitivity.this,
							"亲，您的网络不顺畅哦！");
					break;
				case ADD_SUCESS:
					if (null != msg && null != msg.obj) {
						Log.i("1111msg", msg.obj + "");
						try {
							setAddRes(new JSONObject(msg.obj.toString()),
									activity);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				case ADD_FAILE:
					Log.i("1111msg", msg.obj + "");
					// ToastUtils.showToast(getActivity(), "");
					break;
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

				}
			}
		}
	};

	private void setFile(String file, int type, String time) {
		Attachments att = new Attachments();
		att.setFile_name(file);
		att.setFile_type(type + "");
		att.setTime("");
		att.setState("1");
		imgList.add(att);

		imgAdapter.setImageList(imgList);
		isChanged = true;

	}

	List<String> resullist = new ArrayList<String>();
	private int size;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// if (requestCode == ADD_PRITRUE_CODE && null != data) {
		// Log.i("1111111111", data.getExtras() + "");
		//
		// for (int i = 0; i < data.getStringArrayListExtra("picList").size();
		// i++) {
		// CompressImageUtil.getCompressImageUtilInstance()
		// .startCompressImage(myHandler, COMPRESS_IMAGE,
		// data.getStringArrayListExtra("picList").get(i));
		//
		// }
		//
		// }
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

		default:
			break;
		}
	}

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

	protected void setResult(JSONObject jsonObject) {
		if (jsonObject.optString("result_id").equals("0")) {
			Intent it = new Intent(mcontext, CalendarMainActivity.class);
			tv.setCheck_content(checkContentEdt.getText().toString());
			tv.setDeal_suggest(sugesstEdt.getText().toString());
			tv.setExist_problem(problemEdt.getText().toString());
			tv.setImprove_suggest(changeEdt.getText().toString());
			it.putExtra("data", tv);
			setResult(0x136, it);
			finish();
		}
		ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
	}

	public void resetMainRemark() {
		if (timeTv.getText().length() > 0) {
			String isshow = tv.getIsShow();
			setReminder(timeTv.getText().toString(), isshow);
		}
		tv.setCheck_content(checkContentEdt.getText().toString().trim());
		tv.setDeal_suggest(sugesstEdt.getText().toString());
		tv.setExist_problem(problemEdt.getText().toString());
		tv.setImprove_suggest(changeEdt.getText().toString());
		List<Attachments> attachments = new ArrayList<Attachments>();
		attachments.addAll(imgList);
		attachments.addAll(fileList);
		tv.setRemind_date(timeTv.getText().toString());
		tv.setFileList(gson.toJson(attachments).toString());
		tv.setMobile(tools.getValue(Constants.MOBILE));
		tv.setType("2");
		tv.setHospital(tools.getValue(Constants.HOSPITAL_ID));
		// tv.setUpdatedTask(tv.getStatus() == 0 ? true : false);
		tv.setStatus(1);
		tv.setIs_main_remark("1");
	}

	// 发送通知
	private void setReminder(String time, String isSend) {

		// get the AlarmManager instance
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		// create a PendingIntent that will perform a broadcast
		PendingIntent pi = PendingIntent.getBroadcast(mcontext, 0, new Intent(
				this, NotificationReceiver.class), 0);
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

	private void showShare(String url) {
		SHARE_MEDIA[] shareMedia = { SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.WEIXIN_CIRCLE };
		// initCustomPlatforms(shareMedia);
		// showShareContent(shareMedia, shareTitle, shareContent, targetUrl);
		showShareDialog(tv.getMain_remark_name(), tv.getCheck_content(), url);
	}

	protected static final int ADD_FAILE = 0x60089;
	protected static final int ADD_SUCESS = 0x60090;

	public void getAddScore(String id) {
		Log.i("share_umeng", "111111111111111");
		tools = new Tools(mcontext, Constants.AC);
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("aid", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				FormSupervisoryBookAcitivity.this, ADD_SUCESS, ADD_FAILE, job,
				"goods/actionGetIntegral");
	}

	protected void setAddRes(JSONObject jsonObject, Activity activity) {
		Log.i("share_umeng", "返回次数");
		Log.i("11111111", jsonObject.toString());
		if (jsonObject.optString("result_id").equals("0")) {
			int score = jsonObject.optInt("integral");
			String str = tools.getValue(Constants.INTEGRAL);
			if (null != str) {
				tools.putValue(Constants.INTEGRAL, Integer.parseInt(str)
						+ score + "");
			} else {
				tools.putValue(Constants.INTEGRAL, score + "");
			}
			if (score > 0) {
				if (null != activity) {
					showTipsDialog(score + "");
				}
			}

		}
	}

	// 拍照
	private static final int REQUEST_TAKE_PHOTO = 0x0; // 拍照
	private static final int REQUEST_SELECT_LOCAL_PHOTO = 0x2; // 选择本地图片
	// 语音弹出框
	public static final int RECORD_STADIO_INITIAL = 0;// 未开始
	public static final int RECORD_STADIO_ISSTART = 1;// 已开始录制
	public static final int RECORD_STADIO_IS_STOPRECORD = 2;//
	public static final int RECORD_STADIO_PLAYING = 3;
	public static final int RECORD_STADIO_STOPPLAY = 4;

	void showRecordPopWindow() {
		new PopuRecord(mcontext, true,false,
				FormSupervisoryBookAcitivity.this.findViewById(R.id.main),
				new PopuRecord.OnPopuClick() {

					@Override
					public void cancel() {
					}

					@Override
					public void enter(String filename, double totalTime) {
						Attachments att = new Attachments();
						att.setFile_name(filename);
						att.setTime(totalTime + "");
						att.setFile_type("2");
						att.setState("1");
						fileList.add(att);
						fileAdapter.setRecordList(fileList);
					}

					@Override
					public void photograph(File file, List<String> result) {
						// TODO Auto-generated method stub
						resullist = result;
						Intent takePictureIntent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						takePictureIntent.putExtra("output", Uri.fromFile(file));
						startActivityForResult(takePictureIntent,
								REQUEST_TAKE_PHOTO);
					}

					@Override
					public void album() {
						// TODO Auto-generated method stub
						Intent intent = new Intent(mcontext,
								SelectPhotoActivity.class);
						intent.putExtra("size", "0");
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
		protected String orderTime;

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
			ScreenInfo screenInfo = new ScreenInfo(
					FormSupervisoryBookAcitivity.this);
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
					dismiss();

				}
			});

		}
	}
}
