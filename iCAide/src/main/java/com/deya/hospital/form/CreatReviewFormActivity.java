package com.deya.hospital.form;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.ReViewImageListAdapter;
import com.deya.hospital.adapter.ReviewRecordFileLisAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.NewPhotoMultipleActivity;
import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.UploadBizImpl;
import com.deya.hospital.setting.UploadMessage;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.widget.popu.PopuRecord;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreatReviewFormActivity extends BaseActivity implements
		OnClickListener {
	LinearLayout photoLay;
	LinearLayout recordLay;
	EditText remarkTv;
	GridView photoGv;
	ReViewImageListAdapter imgAdapter;
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
	ReviewRecordFileLisAdapter fileAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creat_review_form);
		tools = new Tools(mcontext, Constants.AC);
		intTopView();
		getData();
		initView();
	}

	private void intTopView() {
		TextView titleTv = (TextView) this.findViewById(R.id.title);
		titleTv.setText("上传自定义模版");
		rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
		rlBack.setOnClickListener(this);

	}

	private void initView() {
		departTv = (TextView) this.findViewById(R.id.departTv);
		photoLay = (LinearLayout) this.findViewById(R.id.photoLay);
		recordLay = (LinearLayout) this.findViewById(R.id.recordLay);
		recordLay.setOnClickListener(this);
		remarkTv = (EditText) this.findViewById(R.id.remarkTv);
		photoGv = (GridView) this.findViewById(R.id.photoGv);
		imgAdapter = new ReViewImageListAdapter(mcontext, imgList);
		photoGv.setAdapter(imgAdapter);
		photoLay.setOnClickListener(this);
		submit = (Button) this.findViewById(R.id.sumbmitBtn);
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("1111111", "什么玩意");
				addData(1);
				

			}
		});
		recordGv = (ListView) this.findViewById(R.id.recordGv);
		fileAdapter = new ReviewRecordFileLisAdapter(mcontext, recordList);
		recordGv.setAdapter(fileAdapter);
	}

	public static final int ADD_PRITRUE_CODE = 9009;
	// 压缩图片的msg的what

	public static final int COMPRESS_IMAGE = 0x17;

	public void tokephote() {
		Intent takePictureIntent = new Intent(CreatReviewFormActivity.this,
				NewPhotoMultipleActivity.class);
		takePictureIntent.putExtra(NewPhotoMultipleActivity.MAX_UPLOAD_NUM, 3);
		takePictureIntent.putExtra("type", "1");
		takePictureIntent.putExtra("size", "0");
		startActivityForResult(takePictureIntent, ADD_PRITRUE_CODE);
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
				case SEND_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							setLoginResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case SEND_FAILE:
					submit.setText("上传");
					submit.setEnabled(true);
					ToastUtils.showToast(CreatReviewFormActivity.this, "亲，您的网络不顺畅哦！");
					break;
				case UploadMessage.SUP_PICTURE_SUCCESS:
					if (null != msg && null != msg.obj) {
						try {
							Log.i("111111111", msg.obj.toString());
							setImagReq(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case UploadMessage.SUP_PICTURE_FAIL:
					fileIndex++;
					if (fileIndex >= fileList.size() - 1) {
						sendForm();
					} else {
						uploadSupFile();
					}
					break;
				default:
					break;
				}
			}
		}

	};
	private int fileIndex=0;
	void uploadSupFile() {
		if (null != fileList && fileList.size() > 0) {
			UploadBizImpl.getInstance().propertyUploadPicture(myHandler,
					fileList.get(fileIndex).getFile_name(),
					UploadMessage.SUP_PICTURE_SUCCESS,
					UploadMessage.SUP_PICTURE_FAIL);
		}
	}
	protected void setImagReq(JSONObject json) {
		fileList.get(fileIndex).setFile_name(json.optString("data"));
		if (fileIndex < fileList.size() - 1) {
			fileIndex++;
			uploadSupFile();
		} else {
			sendForm();
		}
	}
	private List<TaskVo> newTaskList = new ArrayList<TaskVo>();
	private Tools tools;
	private String departmentName;
	private String departmentId;
	protected String orderTime;

	private void setFile(String file, int type, String time) {
		Attachments att = new Attachments();
		att.setFile_name(file);
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
		
	
		case R.id.rl_back:
			// addData(2);
			finish();
			break;
		case R.id.recordLay:
			showRecordPopWindow();
			break;
		default:
			break;
		}

	}


	public void deletPhoto(int position) {
		imgList.remove(position);
		imgAdapter.notifyDataSetChanged();

	}



	private void getData() {
		departmentName = getIntent().getStringExtra("departName");
		departmentId = getIntent().getStringExtra("departId");
		creatTime = getIntent().getStringExtra("time");
		if(getIntent().hasExtra("data")){
			tv=(TaskVo) getIntent().getSerializableExtra("data");
		}
	}

	public void addData(int state) {
		submit.setText("上传中...");
		submit.setEnabled(false);
		fileList.addAll(imgList);
		fileList.addAll(recordList);
		if(fileList.size()<1){
			sendForm();
		}else{
		uploadSupFile();
		}

	}



	
	
	


	public static final int RECORD_STADIO_INITIAL = 0;// 未开始
	public static final int RECORD_STADIO_ISSTART = 1;// 已开始录制
	public static final int RECORD_STADIO_IS_STOPRECORD = 2;//
	public static final int RECORD_STADIO_PLAYING = 3;
	public static final int RECORD_STADIO_STOPPLAY = 4;
	private static final int SEND_SUCESS = 0x7001;
	private static final int SEND_FAILE = 0x7002;

	void showRecordPopWindow() {
		new PopuRecord(mcontext, false,false,
				CreatReviewFormActivity.this.findViewById(R.id.main),
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
	// 请求服务器验证 账号密码
	private void sendForm() {
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			if(fileList.size()>0){
				String str=gson.toJson(fileList);
				JSONArray jarr=new JSONArray(str);
			job.put("attachments",jarr);
			}else{
				job.put("attachments","");
			}
			job.put("remark", remarkTv.getText().toString());
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,CreatReviewFormActivity.this, SEND_SUCESS,
				SEND_FAILE, job,"grid/submitTemplateInfo");
	}


		// 登录请求回调处理
		private void setLoginResult(JSONObject json) {
			if(json.optString("result_id").equals("0")){
				submit.setText("上传成功");
				submit.setEnabled(true);
				setResult(0x111);
				finish();
			}else{
				ToastUtils.showToast(mcontext, json.optString("result_msg"));
				submit.setText("上传");
				submit.setEnabled(true);
			}
			
			}
	
	
	
}
