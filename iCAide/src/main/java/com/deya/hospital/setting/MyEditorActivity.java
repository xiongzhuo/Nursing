package com.deya.hospital.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.SexListAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.ComomListPopWindow;
import com.deya.hospital.base.DepartChoosePop;
import com.deya.hospital.base.DepartChoosePop.OnDepartPopuClick;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.base.NewPhotoMultipleActivity;
import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.UploadBizImpl;
import com.deya.hospital.login.LoginActivity;
import com.deya.hospital.supervisor.AddDepartmentActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.DepartmentVo;
import com.deya.hospital.vo.HospitalInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyEditorActivity extends BaseActivity implements OnClickListener {
	private EditText nameTv;
	private TextView emailTv;
	Tools tools;
	String name = "";
	String age = "";
	String sex = "男";
	String hospital = "";
	String hospitalId = "";
	String departId = ""; //job id
	String sectionId = "";//科室ID
	String sexId = "0";
	String hos = "";
	Button submit;
	Gson gson = new Gson();
	public String[] meItems = { "男", "女" };
	String[] HospitalItems = { "andexplorer", "astro" };
	private TextView hosText;
	private TextView departmentTv;
	TextView sectionTv;
	private DisplayImageOptions optionsSquare;
	LinearLayout frambg;
	String province="";
	String city="";
	View sectionLayout;
	DepartChoosePop departDialog;

	private String mobile;
	List<HospitalInfo> hospitallist = new ArrayList<HospitalInfo>();
	List<DepartmentVo> joblist = new ArrayList<DepartmentVo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_userinfo_editor);
		tools = new Tools(mcontext, Constants.AC);
		optionsSquare = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.user_img1)
				.showImageForEmptyUri(R.drawable.user_img1)
				.showImageOnFail(R.drawable.user_img1)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		mobile = tools.getValue(Constants.MOBILE);
		getSexList();
		getJobLists();
		getHospitalList();
		initView();
	}

	private void initView() {
		CommonTopView topView = (CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);
		
		nameTv = (EditText) this.findViewById(R.id.tv_nicheng);
		emailTv = (TextView) this.findViewById(R.id.my_email);
		hosText = (TextView) this.findViewById(R.id.my_hospital);
		hosText.setOnClickListener(this);
		departmentTv = (TextView) this.findViewById(R.id.departmentTv);
		departmentTv.setOnClickListener(this);
		
		sectionTv = (TextView) this.findViewById(R.id.sectionTv);
		sectionTv.setOnClickListener(this);
		
		submit = (Button) this.findViewById(R.id.reg_submit);
		submit.setOnClickListener(this);
		frambg = (LinearLayout) this.findViewById(R.id.frambg);
		
		sectionLayout = this.findViewById(R.id.sectionLayout);
		sectionLayout.setVisibility(View.INVISIBLE);
		
		ImageView image_hospital = (ImageView) findViewById(R.id.image_hospital);
		image_hospital.setOnClickListener(this);
		
		
	}

	////// 科室处理 begin
	List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
	
	
	private void getSaveLevels(String depart_levels, String departmentlist) {
		departlist.clear();
		
		String str = depart_levels;
		String childsStr = departmentlist;
		List<ChildsVo> list2 = gson.fromJson(childsStr,
				new TypeToken<List<ChildsVo>>() {
				}.getType());
		List<ChildsVo> otherList = new ArrayList<ChildsVo>();
		if (!AbStrUtil.isEmpty(str)) {
			for (ChildsVo cv : list2) {
				if (cv.getParent().equals("0") || cv.getParent().equals("1")) {
					otherList.add(cv);
				}
			}
			List<DepartLevelsVo> list = gson.fromJson(str,
					new TypeToken<List<DepartLevelsVo>>() {
					}.getType());
			departlist.addAll(list);
			for (DepartLevelsVo dlv : departlist) {
				if (dlv.getRoot().getId().equals("0")) {
					if (dlv.getChilds().size() == 0) {
						dlv.getChilds().addAll(otherList);
						break;
					}
				}
			}
			// TODO Auto-generated catch block
		} else {

			DepartLevelsVo dlv = new DepartLevelsVo();
			dlv.getRoot().setId("0");
			dlv.getRoot().setName("全部");
			dlv.getChilds().addAll(list2);
			departlist.add(dlv);
		}
		
		if(!departlist.isEmpty()){
			departDialog = new DepartChoosePop(mcontext, departlist,
					new OnDepartPopuClick() {
						@Override
						public void onChooseDepart(String name, String id) {
							Log.i("section", "name:"+name + " id:" +id);
							sectionTv.setText(name);
							sectionId = id;
						}

						@Override
						public void onAddDepart() {
							Intent it = new Intent(mcontext,
									AddDepartmentActivity.class);
							it.putExtra("data", (Serializable) departlist);
							startActivityForResult(it, 0x22);
						}
					});	
			departDialog.show();
		}
		
	}
	
	private void getDepartMentList(String hospitalId) {
		JSONObject job = new JSONObject();
		try {
			job.put("hospital_id", hospitalId);
			job.put("authent", tools.getValue(Constants.AUTHENT));
		} catch (JSONException e) {
			Log.e("departmentInfo", ""+e);
			e.printStackTrace();
		}

		MainBizImpl.getInstance().onComomRequest(myHandler,
				MyEditorActivity.this, DEPARTMENT_SUCESS, DEPARTMENT_FAILE,
				job,"comm/departmentInfo");
	}
	
	protected void setDepartMentList(JSONObject json) {
		Log.i("111111", json + "");
		if (json.optString("result_id").equals("0")) {
			String depart_levels ="", departmentlist="";
			
			JSONArray jarr = json.optJSONArray("departments");
			if (null != json.optJSONArray("levels")) {
				JSONArray jarr2 = json.optJSONArray("levels");
				depart_levels = jarr2.toString();
			} 
			departmentlist = jarr.toString();
			getSaveLevels(depart_levels, departmentlist);
		} else {
			ToastUtils.showToast(mcontext, "获取科室失败");
		}
	}
	
	//// 科室处理end

	ComomListPopWindow dWindow ;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_hospital:
			Intent it=new Intent(mcontext, HospitalListActivity.class);
			startActivityForResult(it,0x18);;
			break;
		case R.id.sectionTv:
			if(this.hospitalId ==null || this.hospitalId.isEmpty()){
				ToastUtils.showToast(mcontext, "请先选择医院");
				return;
			}
			ToastUtils.showToast(mcontext, "科室加载中.....");
			this.getDepartMentList(this.hospitalId);
			
			break;
		case R.id.departmentTv:
			if(joblist.size()<=0){
				getJobLists();
				ToastUtils.showToast(mcontext, "加载中……");
				return;
			}
          dWindow = new ComomListPopWindow(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					departmentTv.setText(joblist.get(position).getName());
					departId=joblist.get(position).getId();
					dWindow.dismiss();
					
					if("3".equals(departId)){ //兼职感控人员
						sectionLayout.setVisibility(View.VISIBLE);
					} else {
						sectionLayout.setVisibility(View.INVISIBLE);
					}
				}
			},getDepartList(),"请选择职业",mcontext,frambg);
			// 显示窗口
			dWindow.showAtLocation(
					MyEditorActivity.this.findViewById(R.id.main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.reg_submit:
			hos = hosText.getText().toString().trim();
			name = nameTv.getText().toString().trim();
			// if (checkInfo()) {
			checkInfo();
			// }
			break;
		case R.id.img_back:
			back();
			break;
		default:
			break;
		}
	}

	
	List<String> namelist = new ArrayList<String>();

	public List<String> getDepartList() {
		if (namelist.size() > 0) {
			return namelist;
		} else {
			namelist.size();
			for (DepartmentVo dv : joblist) {
				namelist.add(dv.getName());
			}
			return namelist;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			frambg.setVisibility(View.GONE);
			back();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void back() {
		Intent intent = new Intent(MyEditorActivity.this, LoginActivity.class);
		startActivity(intent);
		MyEditorActivity.this.finish();
	}

	private int uploadStatus = 0;
	public static final int ADD_PRITRUE_CODE = 9009;
	// 压缩图片的msg的what

	public static final int COMPRESS_IMAGE = 0x17;
	private static final int HOSPITAL_SUCESS = 0x20002;
	private static final int HOSPITAL_FAILE = 0x20003;
	private static final int JOB_SUCESS = 0x20004;
	private static final int JOB_FAILE = 0x20005;
	private static final int SEND_SUCESS = 0x20006;
	private static final int SEND_FAILE = 0x20007;
	private static final int DEPARTMENT_SUCESS = 0x20008;
	private static final int DEPARTMENT_FAILE = 0x20009;

	public void tokephote() {
		Intent takePictureIntent = new Intent(MyEditorActivity.this,
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
		} else if (resultCode == 0x18 && data != null) {
			hospitalId = data.getStringExtra("hospital_id");
			hosText.setText(data.getStringExtra("hospital_name"));
			province=data.getStringExtra("province");
			city=data.getStringExtra("city");
			
			sectionTv.setText("");
			sectionId = "";
		}
	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				
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
				case HOSPITAL_SUCESS:
					if (null != msg && null != msg.obj) {
						Log.i("1111", msg.obj + "");
						try {
							setHospitalLisRes(new JSONObject(msg.obj.toString()));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				case HOSPITAL_FAILE:
					break;
				case JOB_SUCESS:
					if (null != msg && null != msg.obj) {
						Log.i("1111", msg.obj + "");
						try {
							setDepartlLisRes(new JSONObject(msg.obj.toString()));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				case JOB_FAILE:
					departId = "";
					departmentTv.setText("");
					joblist.clear();
					getJobLists();
					break;
					
					
				case DEPARTMENT_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							setDepartMentList(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case DEPARTMENT_FAILE:

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
					if (null != msg && null != msg.obj) {
						Log.i("1111", msg.obj + "");
						Log.i("11111111", msg.obj.toString());
					}
					break;
				default:
					break;
				}
			}
		}
	};
	
	protected void setEditorRes(JSONObject jsonObject) {
		Log.i("111111", jsonObject.toString());
		if (jsonObject.optString("result_id").equals("0")) {
			JSONObject job = jsonObject.optJSONObject("member");
			if (AbStrUtil.isEmpty(job.optString("hospital_name"))) {
				tools.putValue(Constants.HOSPITAL_NAME, "");
			} else {
				tools.putValue(Constants.HOSPITAL_NAME,
						job.optString("hospital_name"));
			}
			// 3
			if (AbStrUtil.isEmpty(job.optString("regis_job"))) {
				tools.putValue(Constants.JOB, "");
			} else {
				tools.putValue(Constants.JOB, job.optString("regis_job")
						.toString());
			}
			if (!AbStrUtil.isEmpty(job.optString("username"))) {
				tools.putValue(Constants.USER_NAME,
						job.optString("username"));
			} else {
				tools.putValue(Constants.USER_NAME,
						job.optString("mobile"));
			}
			if (AbStrUtil.isEmpty(job.optString("hospital"))) {
				tools.putValue(Constants.HOSPITAL_ID, "");
			} else {
				tools.putValue(Constants.HOSPITAL_ID, job.optString("hospital")
						.toString());
			}
			if (AbStrUtil.isEmpty(name)) {
				tools.putValue(Constants.NAME, "");
			} else {
				tools.putValue(Constants.NAME, name);
			}
			if (AbStrUtil.isEmpty(job.optString("mobile"))) {
				tools.putValue(Constants.MOBILE, "");
			} else {
				tools.putValue(Constants.MOBILE, job.optString("mobile")
						.toString());
			}
			String departmentId=job.optString("department");
			if(!AbStrUtil.isEmpty(departmentId)&&!departmentId.equals("0")&&!departmentId.equals("1")){
				tools.putValue(Constants.DEFULT_DEPARTID,
						job.optString("department"));
			}else{
				tools.putValue(Constants.DEFULT_DEPARTID,
						"");
			}
			
			if(!AbStrUtil.isEmpty(job.optString("departmentName"))){
				tools.putValue(Constants.DEFULT_DEPART_NAME,
						job.optString("departmentName"));
			}else{
				tools.putValue(Constants.DEFULT_DEPART_NAME,
						"");
			}
			
			if (AbStrUtil.isEmpty(job.optString("id"))) {
				tools.putValue(Constants.USER_ID,
						"");
			} else {
				tools.putValue(Constants.USER_ID,
						job.optString("id"));
			}
			
			if (AbStrUtil.isEmpty(job.optString("is_admin"))) {
				tools.putValue(Constants.IS_ADMIN, "1");
			} else {
				tools.putValue(Constants.IS_ADMIN,
						job.optString("is_admin"));
			}
			if (AbStrUtil.isEmpty(sex)) {
				tools.putValue(Constants.SEX, "");
			} else {
				tools.putValue(Constants.SEX, sexId);
			}
			if (AbStrUtil.isEmpty(job.optString("task_count"))) {
				tools.putValue(Constants.TASK_COUNT, "0");
			} else {
				tools.putValue(Constants.TASK_COUNT,
						job.optString("task_count"));
			}
			if (!AbStrUtil.isEmpty(job.optString("integral"))) {
				tools.putValue(Constants.INTEGRAL, job.optString("integral"));
			} else {
				tools.putValue(Constants.INTEGRAL, "0");
			}
			if (AbStrUtil.isEmpty(tools.getValue(Constants.IS_CLOSE_TIPS))) {
				tools.putValue(Constants.IS_CLOSE_TIPS, "0");
			}
			if (!AbStrUtil.isEmpty(job.optString("is_sign"))) {
				tools.putValue(Constants.IS_VIP_HOSPITAL,
						job.optString("is_sign"));
			} else {
				tools.putValue(Constants.IS_VIP_HOSPITAL,
						"1");
			}
			
			
			tools.putValue(Constants.USER_ID,
					job.optInt("id")+"");
			
			
			ToastUtils.showToast(mcontext, "注册成功");
			Intent it = new Intent(mcontext, MainActivity.class);
			startActivity(it);
			finish();
		} else {
			ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
		}

	}

	protected void setDepartlLisRes(JSONObject jsonObject) {
		joblist.clear();
		if (jsonObject.optString("result_id").equals("0")) {
			JSONObject job = jsonObject.optJSONObject("jobInfos");
			JSONArray jarr = job.optJSONArray("jobType1");
			Log.i("111111岗位信息", jarr.toString());
			List<DepartmentVo> list = gson.fromJson(jarr.toString(),
					new TypeToken<List<DepartmentVo>>() {
					}.getType());
			Log.i("11111岗位", gson.toJson(list));
			joblist.addAll(list);

		}
	}

	protected void setHospitalLisRes(JSONObject jsonObject) {
		Log.i("QAQ", jsonObject + "");
		hospitallist.clear();
		if (jsonObject.optString("result_id").equals("0")) {
			JSONArray jarr = jsonObject.optJSONArray("hospitals");
			List<HospitalInfo> list = gson.fromJson(jarr.toString(),
					new TypeToken<List<HospitalInfo>>() {
					}.getType());
			hospitallist.addAll(list);

		}
	}

	// 获取医院信息接口
	public void getHospitalList() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", Constants.AUTHENT);
			job.put("keywords", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		MainBizImpl.getInstance().onComomRequest(myHandler,MyEditorActivity.this, HOSPITAL_SUCESS,
				HOSPITAL_FAILE, job,"comm/hospitalInfo");
	}

	// 获取岗位列表
	public void getJobLists() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		MainBizImpl.getInstance().onComomRequest(myHandler,MyEditorActivity.this, JOB_SUCESS,
				JOB_FAILE, job,"comm/jobInfosByType");
	}

	public List<String> getSexList() {
		sexList.clear();
		sexList.add("男");
		sexList.add("女");
		return sexList;
	}

	List<String> sexList = new ArrayList<String>();
	SexListAdapter sAdapter;

	// 提交資料
	public void sendEditor() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("name", name);
			job.put("regis_job", departId);
			job.put("hospital", hosText.getText().toString());
			if(!AbStrUtil.isEmpty(city)){
				job.put("province", province);
				job.put("city", city);
			}
			String email = emailTv.getText().toString();
			if(!email.isEmpty()){
				job.put("email", email);
			}
			if(!sectionId.isEmpty()){
				job.put("department", sectionId);
			}
		} catch (JSONException e) {
			Log.e("modifyUser", ""+e);
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,MyEditorActivity.this, SEND_SUCESS,
				SEND_FAILE, job,"member/modifyUser");
	}
@Override
protected void onDestroy() {
	if(null!=dWindow&&dWindow.isShowing()){
		dWindow.dismiss();
	}
	
	super.onDestroy();
}
	public void checkInfo() {
		String emailText = emailTv.getText().toString().trim();
		if (name.equals("")) {
			ToastUtils.showToast(mcontext, "请输入您的姓名");
			return;
		} else if (hos.equals("")) {
			ToastUtils.showToast(mcontext, "请选择医院");
			return;
		} else if (departId.equals("")) {
			ToastUtils.showToast(mcontext, "请选择您的岗位");
			return;
		}
		sendEditor();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
