package com.deya.hospital.setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.ComomListPopWindow;
import com.deya.hospital.base.NewPhotoMultipleActivity;
import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.UploadBizImpl;
import com.deya.hospital.supervisor.ChooseDepartActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CircleImageView;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.workspace.BootomSelectDialog;
import com.deya.hospital.workspace.BootomSelectDialog.BottomDialogInter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.im.sdk.dy.common.CCPAppManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AccountSettingsActivity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout rel_name;
	private RelativeLayout rel_supervision;
	private RelativeLayout rel_sex;
	private RelativeLayout rel_hospital;
	private RelativeLayout rel_professional_title;
	private RelativeLayout rel_position;
	TextView rightTv;
	private CircleImageView avatar;
	private TextView name;
	private TextView sex;
	private TextView hospital;
	private TextView professional_title;
	private TextView zhichengTv;
	private TextView supervision;
	private RelativeLayout top;
	public static final int ADD_PRITRUE_CODE = 9009;
	public int uploadStatus = 0;
	public static final int COMPRESS_IMAGE = 0x17;
	DisplayImageOptions optionsSquare,optionsSquare2;
	private Tools tools;
	String sexId = "0";
	String sexY = "男";
	int popIndex = -1;
	public static final int REQUSET = 1;
	private static final int SEND_SUCESS = 0x03201;
	private static final int SEND_FAILE = 0x3202;
	private static final int SEND_SUCESS2 = 0x03203;
	private static final int SEND_FAILE2 = 0x3204;
	Gson gson = new Gson();
	List<JobListVo> plist = new ArrayList<JobListVo>();
	List<JobListVo> jadamiclist = new ArrayList<JobListVo>();
	List<JobListVo> jobList = new ArrayList<JobListVo>();
	String plistId = "";// 职称Id
	String aId = "";// 职位Id
	String hId = "";// 院内职位Id
	private ComomListPopWindow dWindow;
	LinearLayout frambg;
	private MyHandler myHandler;
	RelativeLayout defultdepartlay;
	TextView defultdepartTv;
	private String hospitalJob;
	private String defultDepart;
	private RelativeLayout changepassRl;
	private CommonTopView topView;
	private BootomSelectDialog sexDilog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tools = new Tools(AccountSettingsActivity.this, Constants.AC);
		optionsSquare = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.men_defult)
				.showImageForEmptyUri(R.drawable.men_defult)
				.showImageOnFail(R.drawable.men_defult)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		optionsSquare2 = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.women_defult)
		.showImageForEmptyUri(R.drawable.women_defult)
		.showImageOnFail(R.drawable.women_defult)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();
		
		setContentView(R.layout.account_settings_activity);
		plistId = tools.getValue(Constants.PJOB);
		aId = tools.getValue(Constants.AJOB);
		hId = tools.getValue(Constants.HOSPITAL_JOB);
		getSexList();
		initMyHandler();
		initView();
	}

	private void initView() {
		
		defultdepartlay=(RelativeLayout) this.findViewById(R.id.defultdepartlay);
		defultdepartlay.setOnClickListener(this);
		defultdepartTv=(TextView) this.findViewById(R.id.defultdeparTv);
		hospitalJob = tools.getValue(Constants.JOB);
		defultDepart = tools.getValue(Constants.DEFULT_DEPARTID);
		if(hospitalJob.equals("3") && !AbStrUtil.isEmpty(defultDepart)&&!defultDepart.equals("1")){//兼职感控人员在设置了默认科室后可以直接跳过选择部分
			defultdepartTv.setText(tools.getValue(Constants.DEFULT_DEPART_NAME));
		}
		if(!hospitalJob.equals("3")){
			defultdepartlay.setVisibility(View.GONE);
		}
		
		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.onbackClick(this, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendEditor();
				
			}
		});
		// 头像
		avatar = (CircleImageView) this.findViewById(R.id.avatar);
		avatar.setOnClickListener(this);
		// 名字
		rel_name = (RelativeLayout) this.findViewById(R.id.rel_name);
		name = (TextView) this.findViewById(R.id.name);

		rel_name.setOnClickListener(this);
		// 性别
		rel_sex = (RelativeLayout) this.findViewById(R.id.rel_sex);
		sex = (TextView) this.findViewById(R.id.sex);
		rel_sex.setOnClickListener(this);
		// 督导岗位
		rel_supervision = (RelativeLayout) this
				.findViewById(R.id.rel_supervision);
		supervision = (TextView) this.findViewById(R.id.supervision);
		rel_supervision.setOnClickListener(this);
		// 院内岗位
		rel_hospital = (RelativeLayout) this.findViewById(R.id.rel_hospital);
		hospital = (TextView) this.findViewById(R.id.hospital);
		rel_hospital.setOnClickListener(this);
		// 职称
		rel_professional_title = (RelativeLayout) this
				.findViewById(R.id.rel_professional_title);
		professional_title = (TextView) this
				.findViewById(R.id.professional_title);
		rel_professional_title.setOnClickListener(this);
		// 职位
		rel_position = (RelativeLayout) this.findViewById(R.id.rel_position);
		rel_position.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		zhichengTv = (TextView) this.findViewById(R.id.position);
		rel_position.setOnClickListener(this);

		
		name.setText(tools.getValue(Constants.NAME));
		String localsex = tools.getValue(Constants.SEX);
		if (!AbStrUtil.isEmpty(localsex) && localsex.equals("0")) {
			sex.setText("男");
			ImageLoader.getInstance().displayImage(
					WebUrl.FILE_LOAD_URL + tools.getValue(Constants.HEAD_PIC),
					avatar, optionsSquare);
			sexId="0";
		} else {
			sex.setText("女");
			ImageLoader.getInstance().displayImage(
					WebUrl.FILE_LOAD_URL + tools.getValue(Constants.HEAD_PIC),
					avatar, optionsSquare2);
			sexId="1";
		}
		rightTv = (TextView) this.findViewById(R.id.tv_right);
		String regis_job = tools.getValue(Constants.JOB);
		if (!AbStrUtil.isEmpty(regis_job)) {
			if (regis_job.equals("1")) {
				supervision.setText("感控科主任");
			} else if (regis_job.equals("2")) {
				supervision.setText("专职感控人员");
			} else if (regis_job.equals("3")) {
				supervision.setText("兼职感控人员");
			} else if (regis_job.equals("4")) {
				supervision.setText("其他");
			}
		}

		
		String titles[] = { "男", "女" };
		sexDilog = new BootomSelectDialog(mcontext, titles,
				new BottomDialogInter() {

					@Override
					public void onClick3() {
				
					}

					@Override
					public void onClick2() {
						ImageLoader.getInstance().displayImage(
								WebUrl.FILE_LOAD_URL + tools.getValue(Constants.HEAD_PIC),
								avatar, optionsSquare2);
						sexId = 1 + "";
						sex.setText("女");
					}

					@Override
					public void onClick1() {
						ImageLoader.getInstance().displayImage(
								WebUrl.FILE_LOAD_URL
										+ tools.getValue(Constants.HEAD_PIC),
								avatar, optionsSquare);
						sexId = 0 + "";
						sex.setText("男");
					}

					@Override
					public void onClick4() {
						// TODO Auto-generated method stub
						
					}
				});
		frambg = (LinearLayout) this.findViewById(R.id.frambg);
		dialog = new MyDialog(mcontext, R.style.SelectDialog);
		getCacheData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.avatar:
			tokephote();
			break;
		case R.id.rel_name:
			Intent it = new Intent(AccountSettingsActivity.this,
					SettingNameActivity.class);
			startActivityForResult(it, REQUSET);
			break;
		case R.id.rel_sex:
			sexDilog.show();
			break;
		case R.id.rel_supervision:
			break;
		case R.id.rel_hospital:
			dWindow = new ComomListPopWindow(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					hospital.setText(jobList.get(position).getName());
					hId = jobList.get(position).getId();
					dWindow.dismiss();

				}
			}, hospitalJobList, "请选择院内岗位", mcontext, frambg);
			// 显示窗口
			dWindow.showAtLocation(
					AccountSettingsActivity.this.findViewById(R.id.main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	

			break;
		case R.id.rel_professional_title:
			dWindow = new ComomListPopWindow(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					professional_title.setText(jadamiclist.get(position).getName());
					aId = jadamiclist.get(position).getId();
					dWindow.dismiss();

				}
			}, zhiyeList, "请选择职位", mcontext, frambg);
			// 显示窗口
			dWindow.showAtLocation(
					AccountSettingsActivity.this.findViewById(R.id.layout),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
	
		case R.id.rel_position:
			
			
			dWindow = new ComomListPopWindow(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					zhichengTv.setText(plist.get(position).getName());
					plistId = plist.get(position).getId();
					dWindow.dismiss();

				}
			}, zhichengList, "请选择您的职称", mcontext, frambg);
			// 显示窗口
			dWindow.showAtLocation(
					AccountSettingsActivity.this.findViewById(R.id.layout),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.defultdepartlay:
			Intent todepart=new Intent(AccountSettingsActivity.this, ChooseDepartActivity.class);
			todepart.putExtra("isAfter", false);
			todepart.putExtra("type", "6");
			startActivityForResult(todepart, 0x71);
			break;
		default:
			break;
		}

	}


	public void tokephote() {
		Intent takePictureIntent = new Intent(AccountSettingsActivity.this,
				NewPhotoMultipleActivity.class);
		takePictureIntent.putExtra(NewPhotoMultipleActivity.MAX_UPLOAD_NUM, 1);
		takePictureIntent.putExtra("type", "1");
		takePictureIntent.putExtra("size", "0");
		startActivityForResult(takePictureIntent, ADD_PRITRUE_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ADD_PRITRUE_CODE && null != data) {
			Log.i("1111111111", data.getExtras() + "");
			uploadStatus = 1;
			for (int i = 0; i < data.getStringArrayListExtra("picList").size(); i++) {
				CompressImageUtil.getCompressImageUtilInstance()
						.startCompressImage(myHandler, COMPRESS_IMAGE,
								data.getStringArrayListExtra("picList").get(i));
				Log.i("1111111111", data.getStringArrayListExtra("picList")
						.get(i));
			}
		} else if (requestCode == AccountSettingsActivity.REQUSET
				&& resultCode == RESULT_OK) {
			name.setText(data.getStringExtra(SettingNameActivity.KEY_USER_ID));

		}else if(resultCode==0x71){
			defultdepartTv.setText(tools.getValue(Constants.DEFULT_DEPART_NAME));
			sendEditor2();
		}
	}

	/**
	 * .本来所有消息的接收对象
	 */
	private void initMyHandler() {
		myHandler = new MyHandler(AccountSettingsActivity.this) {
			@Override
			public void handleMessage(Message msg) {
				Activity activity = myHandler.mactivity.get();
				if (null != activity) {
					switch (msg.what) {
					case UploadMessage.SHOP_UPLOAD_PICTURE_SUCCESS:// 上传图片成功
						if (null != msg && null != msg.obj) {
							Log.i("1111", msg.obj + "");
							try {
								setHeadImg(new JSONObject(msg.obj.toString()));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						break;
					case UploadMessage.SHOP_UPLOAD_PICTURE_FAIL:
						uploadStatus = 0;
						ToastUtils.showToast(AccountSettingsActivity.this,
								"上传失败");
						break;
					case COMPRESS_IMAGE:// 压缩从选择图片界面返回的图片
						uploadStatus = 1;
						Log.i("1111", msg.obj + "");
						File file = new File(msg.obj + "");
						Log.i("1111", file.exists() + "");
						if (null != msg && null != msg.obj) {
							UploadBizImpl.getInstance().propertyUploadPicture(
									myHandler, msg.obj.toString(),
									UploadMessage.SHOP_UPLOAD_PICTURE_SUCCESS,
									UploadMessage.SHOP_UPLOAD_PICTURE_FAIL);
						}
						break;
					case SEND_SUCESS:
						if (null != msg && null != msg.obj) {
							Log.i("1111", msg.obj + "");
							try {
								setEditorRes(new JSONObject(msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						break;
					case SEND_FAILE:
						finish();
						Toast.makeText(mcontext, "亲您的网络不顺畅，资料未修改成功哦", 2000).show();
						break;
					default:
						break;
					}
				}
			}

			private void setHeadImg(JSONObject jsonObject) {
				ImageLoader.getInstance().displayImage(
						WebUrl.FILE_LOAD_URL
								+ jsonObject.optString("data").toString(),
						avatar, optionsSquare);
				tools.putValue(Constants.HEAD_PIC,
						(jsonObject.optString("data").toString()));
				CCPAppManager.getClientUser().setAvatar(jsonObject.optString("data").toString());

			}

		};
	}

	// 设置返回结果
	protected void setEditorRes(JSONObject jsonObject) {
		if (jsonObject.optString("result_id").equals("0")) {
			tools.putValue(Constants.PJOB, plistId);
			tools.putValue(Constants.AJOB, aId);
			tools.putValue(Constants.HOSPITAL_JOB, hId);
			tools.putValue(Constants.SEX, sexId);
			tools.putValue(Constants.NAME, name.getText().toString());
			finish();
		} else {
			finish();
			ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
		}

	}

	public List<String> getSexList() {
		sexList.clear();
		sexList.add("男");
		sexList.add("女");
		return sexList;
	}

	List<String> sexList = new ArrayList<String>();

	// 提交个人资料
	// 提交資料
	public void sendEditor() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("name", name.getText().toString());
			job.put("sex", sexId);
			job.put("avatar", tools.getValue(Constants.HEAD_PIC));
			job.put("department", tools.getValue(Constants.DEFULT_DEPARTID));
			if(!TextUtils.isEmpty(plistId)){
				job.put("post", plistId);
				
			}
			if(!TextUtils.isEmpty(hId)){
				job.put("in_job", hId);
			}
			if(!TextUtils.isEmpty(aId)){
				job.put("title", aId);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,AccountSettingsActivity.this, SEND_SUCESS,
				SEND_FAILE, job,"member/modifyUser");

	}

	public void sendEditor2() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("department", tools.getValue(Constants.DEFULT_DEPARTID));
			if(!TextUtils.isEmpty(plistId)){
				job.put("post", plistId);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,AccountSettingsActivity.this, SEND_SUCESS2,
				SEND_FAILE2, job,"member/modifyUser");

	}

	List<String> hospitalJobList = new ArrayList<String>();// 院内岗位
	List<String> zhichengList = new ArrayList<String>();// 职称
	List<String> zhiyeList = new ArrayList<String>();// 职业

	public void getCacheData() {
		// type1 位督导岗位 type4为职称 type3为职位
		String jsonStr = SharedPreferencesUtil.getString(mcontext,
				"jobinfolist", null);
		if (!TextUtils.isEmpty(jsonStr)) {
			try {
				JSONObject jsonObject = new JSONObject(jsonStr);
				JSONArray jarr4 = jsonObject.optJSONArray("jobType4");
				plist = gson.fromJson(jarr4.toString(),
						new TypeToken<List<JobListVo>>() {
						}.getType());
				JSONArray jarr3 = jsonObject.optJSONArray("jobType3");
				jadamiclist = gson.fromJson(jarr3.toString(),
						new TypeToken<List<JobListVo>>() {
						}.getType());
				JSONArray jarr2 = jsonObject.optJSONArray("jobType2");
				jobList = gson.fromJson(jarr2.toString(),
						new TypeToken<List<JobListVo>>() {
						}.getType());

				for (JobListVo jv : jobList) {
					Log.i("111111111", jv.getId() + "----" + hId);
					if (!AbStrUtil.isEmpty(hId) && jv.getId().equals(hId)) {
						Log.i("111111111", hId);
						hospital.setText(jv.getName());
					}
					hospitalJobList.add(jv.getName());
				}
				for (JobListVo jv : jadamiclist) {
					if (!AbStrUtil.isEmpty(aId) && jv.getId().equals(aId)) {
						professional_title.setText(jv.getName());
					}
					zhiyeList.add(jv.getName());
				}
				for (JobListVo jv : plist) {
					if (!AbStrUtil.isEmpty(plistId)
							&& jv.getId().equals(plistId)) {
						zhichengTv.setText(jv.getName());
					}
					zhichengList.add(jv.getName());

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("111111111111", "back");
			sendEditor();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	Dialog dialog;

	public class MyDialog extends Dialog {

		private TextView showBtn;
		private TextView deletBtn;
		private TextView cancleBtn;

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

			setContentView(R.layout.dialog_delet);

			showBtn = (TextView) this.findViewById(R.id.show);
			showBtn.setText("您的资料未提交成功，确认退出？");
			deletBtn = (TextView) this.findViewById(R.id.yes);
			deletBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
			cancleBtn = (TextView) this.findViewById(R.id.cacle);
			cancleBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
		}
	}
}
