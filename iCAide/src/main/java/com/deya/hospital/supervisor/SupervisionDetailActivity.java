package com.deya.hospital.supervisor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.DetailImageListAdapter;
import com.deya.hospital.adapter.DetailImageListAdapter.ImageAdapterInter;
import com.deya.hospital.adapter.DetailRecordFileLisAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.NewPhotoMultipleActivity;
import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.FileActions;
import com.deya.hospital.util.FileDownloadThread;
import com.deya.hospital.util.FileUtils;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.widget.popu.PopuRecord;
import com.example.calendar.CalendarMainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SupervisionDetailActivity extends BaseActivity implements
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_superversion_detail);
		tools = new Tools(mcontext, Constants.AC);
		tv = (TaskVo) getIntent().getSerializableExtra("data");
		intTopView();
		getData();
		initView();
	}

	private void intTopView() {
		TextView titleTv = (TextView) this.findViewById(R.id.title);
		if (getIntent().hasExtra("isRemark")) {
			titleTv.setText("备注详情");
		} else {
			titleTv.setText("督导详情");
		}

		rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
		rlBack.setOnClickListener(this);

	}

	private void initView() {

		checkContentEdt = (EditText) this.findViewById(R.id.checkContentEdt);
		problemEdt = (EditText) this.findViewById(R.id.problemEdt);
		sugesstEdt = (EditText) this.findViewById(R.id.sugesstEdt);
		photoGv = (GridView) this.findViewById(R.id.photoGv);
		recordLay = (LinearLayout) this.findViewById(R.id.recordLay);
		recordLay.setOnClickListener(this);
		photoLay = (LinearLayout) this.findViewById(R.id.photoLay);
		photoLay.setOnClickListener(this);
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
					}

					@Override
					public void onDeletMedia(int position) {
					}

					@Override
					public void onDeletFile(int position) {
						deletRecord(position);

					}

					@Override
					public void onPlayMedia(String fileName, ImageView view) {
						playRecord(fileName, view);
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
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("1111111", "什么玩意");
				if (getIntent().hasExtra("isRemark")) {
					onUpdateData();
				} else {
					if (checkContentEdt.getText().toString().trim().length() >= 1) {
					onUpdateData();
					} else {
						ToastUtils.showToast(mcontext, "检查内容不能为空");
					}

				}
			}
		});
		setdata();
		shareBtn = (Button) this.findViewById(R.id.shareBtn);
		shareBtn.setOnClickListener(this);
		// shareTv=(TextView) this.findViewById(R.id.submit);
		// shareTv.setVisibility(View.VISIBLE);
		// shareTv.setText("分享");
		// shareTv.setOnClickListener(this);
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
				SupervisionDetailActivity.this, SEND_SUCESS, SEND_FIAL, job,
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
			showShare(WebUrl.LEFT_URL + "/supervisor/supervisorShare?id="
					+ tv.getTask_id());
			Log.i("11111111", WebUrl.LEFT_URL
					+ "/supervisor/supervisorShare?id=" + tv.getTask_id());
			break;
		case R.id.rl_back:
			finish();
			break;
		case R.id.recordLay:
			showRecordPopWindow();
			break;
		default:
			break;
		}

	}

	public void tokephote() {
		Intent takePictureIntent = new Intent(SupervisionDetailActivity.this,
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
		if (getIntent().hasExtra("departId")) {
			departmentName = getIntent().getStringExtra("departName");
			departmentId = getIntent().getStringExtra("departId");
			creatTime = getIntent().getStringExtra("time");
		} else {
			departmentName = tv.getDepartmentName();
			departmentId = tv.getDepartment();
			creatTime = tv.getMission_time();
		}
		supervisorFileList = null;
		String atts = tv.getFileList();
		if (!AbStrUtil.isEmpty(atts)) {
			supervisorFileList = gson.fromJson(atts,
					new TypeToken<List<Attachments>>() {
					}.getType());
		}

		if (null == supervisorFileList) {
			supervisorFileList = new ArrayList<Attachments>();
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
		File pdffile = new File(FileUtils.getPath() + "/" + fileName);
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
		String dowloadDir = FileUtils.getPath();
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
				SupervisionDetailActivity.this, SEND_SUCESS, SEND_FIAL, job,
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
					ToastUtils.showToast(SupervisionDetailActivity.this,
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

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ADD_PRITRUE_CODE && null != data) {
			Log.i("1111111111", data.getExtras() + "");

			for (int i = 0; i < data.getStringArrayListExtra("picList").size(); i++) {
				CompressImageUtil.getCompressImageUtilInstance()
						.startCompressImage(myHandler, COMPRESS_IMAGE,
								data.getStringArrayListExtra("picList").get(i));

			}

		}
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

	public void onUpdateData() {
		Intent it = new Intent(mcontext, CalendarMainActivity.class);
		tv.setCheck_content(checkContentEdt.getText().toString().trim());
		tv.setDeal_suggest(sugesstEdt.getText().toString());
		tv.setExist_problem(problemEdt.getText().toString());
		tv.setImprove_suggest(changeEdt.getText().toString());
		List<Attachments> attachments = new ArrayList<Attachments>();
		attachments.addAll(imgList);
		attachments.addAll(fileList);
		tv.setFileList(gson.toJson(attachments).toString());
		tv.setMobile(tools.getValue(Constants.MOBILE));
		tv.setStatus(1);
		tv.setUpdatedTask(true);
		it.putExtra("data", tv);
		try {
			Log.i("upload", tv.getDbid()+"");
			DataBaseHelper
			.getDbUtilsInstance(mcontext)
			.update(tv, WhereBuilder.b("dbid", "=", tv.getDbid()));
			Intent brodcastIntent = new Intent();
			brodcastIntent.setAction(CalendarMainActivity.UPDATA_ACTION);
			SupervisionDetailActivity.this.sendBroadcast(brodcastIntent);
			finish();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showShare(String url) {
		SHARE_MEDIA[] shareMedia = { SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.WEIXIN_CIRCLE };
		// initCustomPlatforms(shareMedia);
		// showShareContent(shareMedia, shareTitle, shareContent, targetUrl);
		showShareDialog(getString(R.string.app_name), "点击查看",
				url);

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
				SupervisionDetailActivity.this, ADD_SUCESS, ADD_FAILE, job,
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

	// 语音弹出框
	public static final int RECORD_STADIO_INITIAL = 0;// 未开始
	public static final int RECORD_STADIO_ISSTART = 1;// 已开始录制
	public static final int RECORD_STADIO_IS_STOPRECORD = 2;//
	public static final int RECORD_STADIO_PLAYING = 3;
	public static final int RECORD_STADIO_STOPPLAY = 4;

	void showRecordPopWindow() {
		new PopuRecord(mcontext, false,false,
				SupervisionDetailActivity.this.findViewById(R.id.main),
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

}
