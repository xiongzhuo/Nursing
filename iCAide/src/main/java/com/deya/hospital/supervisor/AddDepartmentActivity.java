package com.deya.hospital.supervisor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.ChildDepartListAdapter;
import com.deya.hospital.adapter.DepartDialogListAdapter;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.ListDialog;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddDepartmentActivity extends BaseActivity implements
		OnClickListener {
	protected static final int ADD_DEPART_SUCESS = 0x40001;
	protected static final int ADD_DEPART_FAIL = 0x40002;

	private EditText departEdt;
	private Button addBtn;
	Tools tools;
	TextView DepartKindTv;
	List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
	int defultPosition=-1;
	DepartDialogListAdapter adapter;
	String department="";
	ListView outoList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_department);
		tools=new Tools(mcontext, Constants.AC);
		getdata();
		intiView();
	}

	private void getdata() {
		departlist=(List<DepartLevelsVo>) getIntent().getSerializableExtra("data");
		defultPosition=getIntent().getIntExtra("firstPostion", -1);


	}
	List<ChildsVo> childList = new ArrayList<ChildsVo>();
	private ChildDepartListAdapter childAdapter;
	private CommonTopView topView;
	private void intiView() {
		departEdt = (EditText) this.findViewById(R.id.departEdt);
		addBtn = (Button) this.findViewById(R.id.addBtn);
		addBtn.setOnClickListener(this);
		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);

		DepartKindTv=(TextView) this.findViewById(R.id.DepartKindTv);
		DepartKindTv.setOnClickListener(this);

		outoList=(ListView) this.findViewById(R.id.outoList);
		String text=defultPosition>=0?departlist.get(defultPosition).getRoot().getName():"";
		department=defultPosition>=0?departlist.get(defultPosition).getRoot().getId():"";
		DepartKindTv.setText(text);
		adapter=new DepartDialogListAdapter(mcontext, departlist);

		childAdapter = new ChildDepartListAdapter(mcontext, childList,1);
		outoList.setAdapter(childAdapter);

		outoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Intent in=new Intent();
				in.putExtra("data", (Serializable) childList.get(position));
				setResult(0x23,in);
				finish();

			}
		});
		departEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()>0){
					addBtn.setEnabled(true);
				}else{
					addBtn.setEnabled(false);
				}

				if (s.toString().trim().length() > 0) {
					childList.clear();
					for (DepartLevelsVo dv : departlist) {
						for (ChildsVo cv : dv.getChilds()) {
							if (cv.getName().contains(s.toString())) {
								childList.add(cv);
							}
						}

					}
					childAdapter.setData(childList);
				} else {
					childList.clear();
					childAdapter.setData(childList);
				}

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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.addBtn:
				if(departEdt.getText().length()<1){
					return;
				}
				getAddDepart();
				addBtn.setEnabled(false);
				break;
			case R.id.img_back:
				finish();
				break;
			case R.id.DepartKindTv:
				ListDialog dialog=new ListDialog(mcontext, new MyDialogInterface() {

					@Override
					public void onItemSelect(int postion) {
						if(departlist.size()<=1){
							return;
						}
						DepartKindTv.setText(departlist.get(postion).getRoot().getName());
						department=departlist.get(postion).getRoot().getId();
					}

					@Override
					public void onEnter() {

					}

					@Override
					public void onCancle() {

					}
				}, adapter);
				adapter.setData(departlist);
				dialog.show();
				break;
			default:
				break;
		}

	}

	public void getAddDepart(){
		String dpartName=departEdt.getText().toString().trim();
		JSONObject job = new JSONObject();
		try {

			job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("oper_type", "1");
			job.put("department_name", dpartName);
			if(AbStrUtil.isEmpty(department)){
				job.put("parent", "1");
			}else{
				job.put("parent", department);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,AddDepartmentActivity.this, ADD_DEPART_SUCESS,
				ADD_DEPART_FAIL, job,"comm/editDepartment");

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
					case ADD_DEPART_SUCESS:
						if (null != msg && null != msg.obj) {
							try {
								setSucessReq(new JSONObject(msg.obj.toString()));
							} catch (JSONException e5) {
								e5.printStackTrace();
							}
						}
						break;
					case ADD_DEPART_FAIL:
						addBtn.setEnabled(true);
						ToastUtils.showToast(AddDepartmentActivity.this, "亲，网络畅通情况下才能添加哦!");
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
					default:
						break;

				}
			}
		}
	};
	ChildsVo childsData=new ChildsVo();
	protected void setSucessReq(JSONObject jsonObject) {

		ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
		if(jsonObject.optString("result_id").equals("0")){

			JSONObject job=jsonObject.optJSONObject("departmentInfo");
			childsData.setName(job.optString("name"));
			childsData.setParent(job.optString("parent"));
			childsData.setId(job.optString("id"));
			getDepartMentList();

		}else{
			addBtn.setEnabled(true);
		}


	}
	private void getDepartMentList() {
		JSONObject job = new JSONObject();
		try {
			job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
			job.put("authent", tools.getValue(Constants.AUTHENT));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		MainBizImpl.getInstance().onComomRequest(myHandler,
				AddDepartmentActivity.this, DEPARTMENT_SUCESS, DEPARTMENT_FAILE,
				job, "comm/departmentInfo");

	}
	protected void setDepartMentList(JSONObject json) {
		if (json.optString("result_id").equals("0")) {


			JSONArray jarr =null;
			if (json.optString("result_id").equals("0")) {
				if (null != json.optJSONArray("levels")) {
					jarr = json.optJSONArray("levels");
					SharedPreferencesUtil.saveString(mcontext, "depart_levels",
							jarr.toString());
				} else {
					SharedPreferencesUtil.saveString(mcontext, "depart_levels", "");
				}
				JSONArray jarr2 = json.optJSONArray("departments");
				SharedPreferencesUtil.saveString(mcontext, "departmentlist", jarr2.toString());

				Intent in=new Intent();
				childsData.setChoosed(true);
				in.putExtra("data",  childsData);
				setResult(0x22,in);
				finish();
//				List<DepartLevelsVo> list = gson.fromJson(jarr.toString(),
//						new TypeToken<List<DepartLevelsVo>>() {
//						}.getType());
//					if (list.size() > 0) {
//						departlist.clear();
//						departlist.addAll(list);
//					}
//					boolean hasparent=false;
//					for(DepartLevelsVo dlv:departlist){
//					for(ChildsVo cv:dlv.getChilds()){
//						if(cv.getId().equals(childsData.getId())){
//							Intent in=new Intent();
//							childsData.setChoosed(true);
//							in.putExtra("data", (Serializable) childsData);
//							in.putExtra("levels", dlv.getRoot().getId());//将二级分类Id传guo'qu
//							setResult(0x22,in);
//							hasparent=true;
//							finish();
//							
//						}
//						
//					}
//					if(!hasparent){
//						Intent in=new Intent();
//						childsData.setChoosed(true);
//						in.putExtra("data", (Serializable) childsData);
//						in.putExtra("levels", "0");//将二级分类Id传guo'qu
//						setResult(0x22,in);
//						finish();
//					}
				//				}


			}

		}
	}
}
