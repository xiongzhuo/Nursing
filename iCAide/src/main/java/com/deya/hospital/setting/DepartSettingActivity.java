package com.deya.hospital.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.DepartListAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.supervisor.AddDepartmentActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.DepartLevelsVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DepartSettingActivity extends BaseActivity implements
		OnClickListener {


	String departmentName = "";
	String departmentId = "";
	private EditText departmentTv;
	ImageView cancleTv;
	Tools tools;
	public static final int CREATRESULT = 0x107;
	Gson gson;
	ListView popListView;
	ImageView imgBack;
	TextView addDepart,backText, framCancle;
	RelativeLayout framLayout;
	private LinearLayout lay_next;
	private Button btn_next;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_depart);
		tools = new Tools(mcontext, Constants.AC);
		
		lay_next=(LinearLayout)findViewById(R.id.lay_next);
		btn_next=(Button)findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		
		gson = new Gson();
		getDepartList();
	}
	
	List<DepartLevelsVo> autoList = new ArrayList<DepartLevelsVo>();

	private void getDepartList() {
		try {
			if (null != DataBaseHelper
					.getDbUtilsInstance(mcontext).findAll(DepartLevelsVo.class)
					&& DataBaseHelper
							.getDbUtilsInstance(mcontext)
							.findAll(DepartLevelsVo.class).size() > 0) {
				List<DepartLevelsVo> list = DataBaseHelper
						.getDbUtilsInstance(mcontext)
						.findAll(DepartLevelsVo.class);
				if (list.size() > 0) {
					departlist.clear();
					departlist.addAll(list);

				}else{
					getDepartMentList();// 网络
				}
				Log.i("111111", departlist.size() + "");
//				for (int i = 0; i < departlist.size(); i++) {
//					DepartLevelsVos vos=new DepartLevelsVos();
//					vos.toS(departlist.get(i));
//					autoList.add(vos);
//				}
				autoList.addAll(departlist);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		// adAdapter=new AutoDepartAdaoter(departlist, mcontext);
		departmentTv = (EditText) this.findViewById(R.id.departmentTv);
		TextView backText=(TextView) this.findViewById(R.id.backText);
		backText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Drawable drawable1 = getResources().getDrawable(R.drawable.search_icon);
		drawable1.setBounds(0, 0, dp2Px(20), dp2Px(20));// 第一0是距左边距离，第二0是距上边距离，40分别是长宽
		departmentTv.setCompoundDrawables(drawable1, null, null, null);// 只放左边
		departmentTv.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (s.toString().trim().length() > 0) {
					autoList.clear();
					for (DepartLevelsVo dv : departlist) {
						if (dv.getRoot().getName().contains(s.toString())) {
							autoList.add(dv);
							Log.i("111111111", dv.getRoot().getName());
						}
					}
				} else {
					autoList.clear();
					autoList.addAll(departlist);
					
				}
				departAdapter.notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		initView();
	}

	public int dp2Px(int dp) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}
	
	private HashMap<String, DepartLevelsVo> selectMap=new HashMap<String, DepartLevelsVo>();

	private void initView() {
		addDepart = (TextView) this.findViewById(R.id.add_depart);
		addDepart.setOnClickListener(this);
		imgBack = (ImageView) this.findViewById(R.id.tv_top_location);
		imgBack.setOnClickListener(this);
		popListView = (ListView) this
				.findViewById(R.id.poplist);
		cancleTv = (ImageView) this.findViewById(R.id.cancle);
		cancleTv.setOnClickListener(this);
		departAdapter = new DepartListAdapter(mcontext, autoList);
		popListView.setAdapter(departAdapter);
		popListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
					departmentName = autoList.get(arg2).getRoot().getName();
					departmentId = autoList.get(arg2).getRoot().getId()+"";
					sendEditor(departmentId);
					tools.putValue(Constants.DEFULT_DEPARTID,
							departmentId);
					tools.putValue(Constants.DEFULT_DEPART_NAME,
							departmentName);
					finish();
						
				}
		});
	}

	private static final int SEND_SUCESS = 0x03201;
	private static final int SEND_FAILE = 0x3202;

	public void sendEditor(String departId) {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("department", departId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				DepartSettingActivity.this, SEND_SUCESS, SEND_FAILE,
				job,"member/modifyUser");

	}



	List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
	DepartListAdapter departAdapter;

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			AbViewUtil.colseVirtualKeyboard(DepartSettingActivity.this);
			break;
		case R.id.tv_top_location:
			finish();
			break;
		case R.id.add_depart:
			Intent it = new Intent(mcontext, AddDepartmentActivity.class);
			startActivityForResult(it, 0x22);

			break;
		case R.id.btn_next:
		
			break;
		default:
			break;
		}
	}

	// 重新获取科室列表
	private static final int DEPARTMENT_SUCESS = 0x40003;
	private static final int DEPARTMENT_FAILE = 0x40004;
	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
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
						Log.i("departSetting",msg.obj.toString());
					}
					break;
				default:
					break;
				}
			}
		}
	};

	private void getDepartMentList() {
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {
			job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
			job.put("authent", tools.getValue(Constants.AUTHENT));
			String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
					.parseStrToMd5L32(job.toString()) + "comm/departmentInfo");
			json.put("token", jobStr);
			json.put("msg_content", job.toString());
			Log.i("11111111", json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		MainBizImpl.getInstance().onComomRequest(myHandler,DepartSettingActivity.this,
				DEPARTMENT_SUCESS, DEPARTMENT_FAILE, job,"comm/departmentInfo");

	}

	protected void setDepartMentList(JSONObject json) {
		Log.i("111111", json + "");
		if (json.optString("result_id").equals("0")) {
			JSONArray jarr = json.optJSONArray("departments");
			List<DepartLevelsVo> list = gson.fromJson(jarr.toString(),
					new TypeToken<List<DepartLevelsVo>>() {
					}.getType());

			try {
				if (list.size() > 0){
					departlist.clear();
					autoList.clear();
					departlist.addAll(list);
				if (null != DataBaseHelper
						.getDbUtilsInstance(mcontext)
						.findAll(DepartLevelsVo.class)
						&& DataBaseHelper
								.getDbUtilsInstance(mcontext)
								.findAll(DepartLevelsVo.class).size() >= 0) {
					DataBaseHelper.getDbUtilsInstance(mcontext)
							.deleteAll(DepartLevelsVo.class);
					DataBaseHelper.getDbUtilsInstance(mcontext)
					.saveAll(departlist);
				} else {
					DataBaseHelper.getDbUtilsInstance(mcontext)
							.saveAll(departlist);
					

				}
				if (null != departlist) {
					autoList.addAll(departlist);
					
				}
				}
			} catch (DbException e) {
				e.printStackTrace();

			}
			
			departAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (null != data && requestCode == 0x22) {
			getDepartMentList();

		}
	}
}
