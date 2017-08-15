package com.deya.hospital.supervisor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.adapter.ChildDepartListAdapter;
import com.deya.hospital.adapter.DepartListAdapter;
import com.deya.hospital.base.BaseFragmentActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.consumption.ConsumptionFormActivity;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.form.FormListActivity;
import com.deya.hospital.supervisor.PartTimeStaffDialog.PDialogInter;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.DepartmentVo;
import com.deya.hospital.widget.view.MultipleTextViewGroup;
import com.deya.hospital.widget.view.TextViewGroup;
import com.deya.hospital.widget.view.TextViewGroup.OnTextViewGroupItemClickListener;
import com.example.calendar.CalendarMainActivity;
import com.example.calendar.DepartmentHintFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.DbException;

public class ChooseDepartActivity extends BaseFragmentActivity implements
		OnClickListener {

	String departmentName = "";
	String departmentId = "";
	String time = "";
	private EditText departmentTv;
	ImageView cancleTv;
	Tools tools;
	public static final int CREATRESULT = 0x107;
	public static final String CLOSE_DEPART_ACTIVITY = "close_depart";
	Gson gson;
	ListView popListView;
	TextView addDepart, framCancle;
	String creatTime = "";
	String type = "";
	private boolean isAfter;
	RelativeLayout framLayout;
	private LinearLayout lay_next;
	private Button btn_next;
	String hospitalJob;
	String defultDepart;
	public String TAG = "ChooseDepartActivity";
	boolean showPartTimeStaffDialog = false;
	LinearLayout historyLay, control2;
	RelativeLayout control1;
	LayoutInflater layoutinfla;
	List<DepartmentVo> levelList = new ArrayList<DepartmentVo>();
	List<ChildsVo> childList = new ArrayList<ChildsVo>();
	ChildDepartListAdapter childAdapter;
	ListView childListView;
	int firstPosition = 0;
	private int listviewHeight;
	private int totalHeight;
	private String str;
	private MyBrodcastReceiver brodcast;
	boolean unNormalJob = false;
	DepartmentHintFragment rightFragment;
	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AbStrUtil.isManul = true;
		if (getIntent().hasExtra("time")) {
			time = getIntent().getStringExtra("time");
		}
		type = getIntent().getStringExtra("type");
		isAfter = getIntent().getBooleanExtra("isAfter", false);
		tools = new Tools(mcontext, Constants.AC);
		hospitalJob = tools.getValue(Constants.JOB);
		defultDepart = tools.getValue(Constants.DEFULT_DEPARTID);
		showPartTimeStaffDialog = showPartTimeStaff();
		if (!(type.equals("5") || type.equals("6"))) {
			if (hospitalJob.equals("3") && !AbStrUtil.isEmpty(defultDepart)) {//
				// 兼职感控人员在设置了默认科室后可以直接跳过选择部分
				unNormalJob = true;
				doAfaterTask(tools.getValue(Constants.DEFULT_DEPART_NAME),
						tools.getValue(Constants.DEFULT_DEPARTID));
				return;
			}
		}
		setContentView(R.layout.activity_choose_depart);

		layoutinfla = LayoutInflater.from(mcontext);
		historyLay = (LinearLayout) this.findViewById(R.id.historyLay);
		control1 = (RelativeLayout) this.findViewById(R.id.control1);
		control2 = (LinearLayout) this.findViewById(R.id.control2);

		lay_next = (LinearLayout) findViewById(R.id.lay_next);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);

		gson = new Gson();
		checkDbisCreated();
		getSaveLevels();
		childAdapter = new ChildDepartListAdapter(mcontext, childList);
		getDepartList();
		getHistoryDepart();

		Log.i(TAG, showPartTimeStaff() + "");
		if (isAfter) {
			historyLay.setVisibility(View.GONE);
			// setFramView();
			lay_next.setVisibility(View.VISIBLE);
		}

		// getContolHeightWidth(control1);
		// getContolHeightWidth(control2);
		// getContolHeightWidth(historyLay);
		// getContolHeightWidth(departgv);

		/** 新手指引fragment */
		tools.putValue("calendar_hint", 1);
		if (tools.getValue_int("calendar_hint") == 0) {
			// ScrollView scrollView = (ScrollView)
			// this.findViewById(R.id.scroll);
			// scrollView.setOnTouchListener(new View.OnTouchListener() {
			// @Override
			// public boolean onTouch(View arg0, MotionEvent arg1) {
			// return true;
			// }
			// });
			departmentTv.setEnabled(false);
			addDepart.setEnabled(false);
			departgv.setEnabled(false);
			popListView.setEnabled(false);

			/** 历史记录viewgroup不可点击 */
			// Log.e("departgv.getChildCount()", departgv.getChildCount()+"");
			// for (int i = 0; i <departgv.getChildCount(); i++) {
			// departgv.getChildAt(i).setEnabled(false);
			// }
			departgv.setVisibility(View.GONE);
			// historyLay.setVisibility(View.GONE);

			FragmentManager fragmentManager = getSupportFragmentManager();
			transaction = fragmentManager.beginTransaction();

			rightFragment = new DepartmentHintFragment();
			Bundle args = new Bundle();
			args.putInt("listviewHeight", listviewHeight);
			args.putInt("height", totalHeight);
			// if(departlist.size()==1){
			// String name=departlist.get(0).getChilds().get(0).getName();
			// args.putString("str", name);
			// }else{
			args.putString("str", childList.get(0).getName());
			// }
			rightFragment.setArguments(args);
			transaction.add(R.id.layout, rightFragment);
			transaction.commit();
		}

	}

	private void registerBroadcast() {
		brodcast = new MyBrodcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(CLOSE_DEPART_ACTIVITY)) {
					finish();
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CLOSE_DEPART_ACTIVITY);
		registerReceiver(brodcast, intentFilter);
	}

	/***/
	private void getContolHeightWidth(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		int height = view.getMeasuredHeight();
		int width = view.getMeasuredWidth();
		Log.e("aaaaaaaaaaaaaa", height + "");
		totalHeight += height;

	}

	private void getSaveLevels() {
		String str = SharedPreferencesUtil.getString(mcontext, "depart_levels",
				"");
		String childsStr = SharedPreferencesUtil.getString(mcontext,
				"departmentlist", "");
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
			if(list2!=null){
				dlv.getChilds().addAll(list2);
			}
			departlist.add(dlv);

		}

	}

	// 设置历史使用记录
	/**
	 * 设置不规则排序控件数据
	 */
	List<TextViewGroup.TextViewGroupItem> reasonList = new ArrayList<TextViewGroup.TextViewGroupItem>();

	private void iniMultipleTextView() {
		// String[] as = mcontext.getResources().getStringArray(
		// R.array.facilities_investigation_string);
		if (null != historyList && historyList.size() > 0) {

			int count = 0;
			if (historyList.size() > 10) {
				count = 10;
			} else {
				count = historyList.size();
			}
			for (int i = 0; i < count; i++) {

			}
			departgv.setTextViews(reasonList);

		}
	}

	List<ChildsVo> historyList = new ArrayList<ChildsVo>();

	public void getHistoryDepart() {

		String str = SharedPreferencesUtil.getString(mcontext,
				tools.getValue(Constants.MOBILE) + "history_used_departlist1",
				"");
		if (null != str && str.length() > 0) {
			List<ChildsVo> hyList = gson.fromJson(str,
					new TypeToken<List<ChildsVo>>() {
					}.getType());
			TextViewGroup.TextViewGroupItem item = null;
			for (DepartLevelsVo dv : departlist) {
				for (ChildsVo cv : dv.getChilds()) {
					for (ChildsVo hv : hyList) {
						if (cv.getId().equals(hv.getId())) {
							historyList.add(hv);
							item = departgv.NewTextViewGroupItem();
							item.setText(hv.getName());
							item.setDepartment(hv.getId());
							if (reasonList.size() < 3) {
								reasonList.add(item);
							}
						}
					}
				}
			}
			departgv.setTextViews(reasonList);
		}
		if (historyList.size() <= 0) {
			historyLay.setVisibility(View.GONE);
		} else {
			historyLay.setVisibility(View.VISIBLE);
		}
	}

	public void setHistoryList(ChildsVo dv) {
		boolean needToAdd = true;
		for (ChildsVo dpv : historyList) {
			if (dpv.getId().equals(dv.getId())) {
				needToAdd = false;
			}
		}
		if (needToAdd) {
			historyList.add(0, dv);
			if (historyList.size() > 3) {
				historyList.remove(3);
			}
			SharedPreferencesUtil.saveString(mcontext,
					tools.getValue(Constants.MOBILE)
							+ "history_used_departlist1",
					gson.toJson(historyList));
		}
		if (historyList.size() <= 0) {
			historyLay.setVisibility(View.GONE);
		} else {
			historyLay.setVisibility(View.VISIBLE);
		}
	}

	public boolean showPartTimeStaff() {
		return hospitalJob.equals("3") && AbStrUtil.isEmpty(defultDepart);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("111111", departlist.size() + "");
	}

	private void setFramView() {
		String showfram = tools.getValue(Constants.SHOWCHOICESSUP);
		int num = 0;
		if (AbStrUtil.isEmpty(showfram)) {
			num = 0;
		} else {
			num = Integer.parseInt(showfram);
		}
		if (num < 5) {
			framLayout.setVisibility(View.VISIBLE);
			tools.putValue(Constants.SHOWCHOICESSUP, (num + 1) + "");
		} else {
			framLayout.setVisibility(View.GONE);
		}
		framCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				framLayout.setVisibility(View.GONE);

			}
		});
	}

	List<ChildsVo> autoList = new ArrayList<ChildsVo>();

	private void getDepartList() {

		// adAdapter=new AutoDepartAdaoter(departlist, mcontext);
		departmentTv = (EditText) this.findViewById(R.id.departmentTv);
		CommonTopView topView=(CommonTopView) this.findViewById(R.id.topView);
		topView.init(this);

		Drawable drawable1 = getResources().getDrawable(R.drawable.search_icon);
		drawable1.setBounds(0, 0, dp2Px(20), dp2Px(20));// 第一0是距左边距离，第二0是距上边距离，40分别是长宽
		departmentTv.setCompoundDrawables(drawable1, null, null, null);// 只放左边
		departmentTv.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (s.toString().trim().length() > 0) {
					popListView.setVisibility(View.GONE);
					childList.clear();
					for (DepartLevelsVo dv : departlist) {
						for (ChildsVo cv : dv.getChilds()) {
							if (cv.getName().contains(s.toString())) {
								childList.add(cv);
								Log.i("111111111", dv.getRoot().getName());
							}
						}

					}
					childAdapter.setData(childList);
				} else {
					autoList.clear();
					popListView.setVisibility(View.VISIBLE);
					childList.addAll(departlist.get(firstPosition).getChilds());
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
		initView();
	}

	public int dp2Px(int dp) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}

	private HashMap<String, ChildsVo> selectMap = new HashMap<String, ChildsVo>();
	private MultipleTextViewGroup departgv;

	private void initView() {

		departgv = (MultipleTextViewGroup) this.findViewById(R.id.main_rl);
		departgv.setOnTextViewGroupItemClickListener(new OnTextViewGroupItemClickListener() {

			@Override
			public void OnTextViewGroupClick(View view,
					List<TextViewGroup.TextViewGroupItem> _dataList,
					final TextViewGroup.TextViewGroupItem item) {
				Log.i(TAG, item.getText() + "----" + item.getDepartment());

				if (showPartTimeStaffDialog) {
					PartTimeStaffDialog dialog = new PartTimeStaffDialog(
							mcontext, true, "", new PDialogInter() {

								@Override
								public void onEnter() {
									sendEditor(item.getDepartment());
									tools.putValue(Constants.DEFULT_DEPARTID,
											item.getDepartment());
									tools.putValue(
											Constants.DEFULT_DEPART_NAME,
											item.getText());
									doAfaterTask(item.getText(),
											item.getDepartment());
								}

								@Override
								public void onCancle() {
									doAfaterTask(item.getText(),
											item.getDepartment());
								}
							});
					dialog.show();
				} else {
					doAfaterTask(item.getText(), item.getDepartment());
				}
			}

		});
		addDepart = (TextView) this.findViewById(R.id.add_depart);
		addDepart.setOnClickListener(this);
		popListView = (ListView) this.findViewById(R.id.poplist);
		if (!isAfter) {
			// popListView.addHeaderView(historyLay);
			historyLay.setVisibility(View.VISIBLE);
		}
		cancleTv = (ImageView) this.findViewById(R.id.cancle);
		cancleTv.setOnClickListener(this);
		departAdapter = new DepartListAdapter(mcontext, departlist);
		popListView.setAdapter(departAdapter);
		if (departlist.size() > 0) {
			if(firstPosition>=0 && firstPosition<departlist.size()){
				childList.addAll(departlist.get(firstPosition).getChilds());
			}
		}
		childListView = (ListView) this.findViewById(R.id.levelList);
		childListView.setAdapter(childAdapter);
		childListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
					long id) {
				if (arg2 == childList.size()) {

					Intent it = new Intent(mcontext,
							AddDepartmentActivity.class);
					it.putExtra("firstPostion", firstPosition);
					it.putExtra("data", (Serializable) departlist);
					startActivityForResult(it, 0x22);
					return;
				}
				if (isAfter) {

					boolean isSelect = !childList.get(arg2).isChoosed();
					loop:for (DepartLevelsVo dlv : departlist) {
						for (ChildsVo pcv : dlv.getChilds()) {
							if (pcv.getId().equals(childList.get(arg2).getId())) {
								pcv.setChoosed(isSelect);
								if (isSelect) {
									dlv.setChooseNum(dlv.getChooseNum() + 1);
								} else {
									dlv.setChooseNum(dlv.getChooseNum() - 1);
								}
								break loop;
							}

						}

					}
					childList.get(arg2).setChoosed(isSelect);
					Log.i(TAG, isSelect + "");
					childAdapter.setData(childList);
					if (isSelect) {
						selectMap.put(childList.get(arg2).getId() + "",
								childList.get(arg2));

					} else {
						selectMap.remove(childList.get(arg2).getId() + "");
					}
					departAdapter.notifyDataSetChanged();

				} else {

					onNormalItemClick(arg2);

				}
			}
		});
		popListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				childList.clear();
				DepartLevelsVo dv = departlist.get(arg2);
				firstPosition = arg2;
				childList.addAll(dv.getChilds());
				childAdapter.setData(childList);
				departAdapter.setIsCheck(arg2);

			}

		});

		/** 获取listview的高度 */
		for (int i = 0, len = childAdapter.getCount(); i < len; i++) {
			View listItem = childAdapter.getView(i, null, childListView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			int list_child_item_height = listItem.getMeasuredHeight()
					+ childListView.getDividerHeight();
			listviewHeight = list_child_item_height;
			Log.e("ListView Child Height", "child height="
					+ list_child_item_height);
			// totalHeight += list_child_item_height; // 统计所有子项的总高度
		}

	}

	public void onNormalItemClick(final int arg2) {
		if (type.equals("5")) {// 手卫生已新增任务修改科室
			Intent intent = new Intent();
			intent.putExtra("departmentId", childList.get(arg2).getId());
			intent.putExtra("departmentName", childList.get(arg2).getName());
			setResult(0x189, intent);
			finish();
		}
		if (type.equals("6")) {// 重设默认科室
			Intent intent = new Intent();
			intent.putExtra("departmentId", childList.get(arg2).getId());
			intent.putExtra("departmentName", childList.get(arg2).getName());
			tools.putValue(Constants.DEFULT_DEPARTID, childList.get(arg2)
					.getId());
			tools.putValue(Constants.DEFULT_DEPART_NAME, childList.get(arg2)
					.getName());
			setResult(0x71, intent);
			finish();
		}

		if (showPartTimeStaffDialog) {
			if (arg2 < 0) {// 防止点击headView空白处
				return;
			}
			PartTimeStaffDialog dialog = new PartTimeStaffDialog(mcontext,
					true, "", new PDialogInter() {

						@Override
						public void onEnter() {
							if (arg2 < 0) {// 防止点击headView空白处
								return;
							}
							sendEditor(childList.get(arg2).getId());
							tools.putValue(Constants.DEFULT_DEPARTID, childList
									.get(arg2).getId());
							tools.putValue(Constants.DEFULT_DEPART_NAME,
									childList.get(arg2).getName());
							doAfaterTask(childList.get(arg2).getName(),
									childList.get(arg2).getId());
							setHistoryList(childList.get(arg2));
						}

						@Override
						public void onCancle() {
							if (arg2 < 0) {// 防止点击headView空白处
								return;
							}
							doAfaterTask(childList.get(arg2).getName(),
									childList.get(arg2).getId());
							setHistoryList(childList.get(arg2));
						}
					});
			dialog.show();
		} else {
			if (arg2 < 0) {
				return;
			}
			doAfaterTask(childList.get(arg2).getName(), childList.get(arg2)
					.getId());
			setHistoryList(childList.get(arg2));
		}

	}

	public void doAfaterTask(String departName, String departId) {
		departmentName = departName;
		departmentId = departId;

		if (type.equals("1")) {
			Intent it = new Intent(ChooseDepartActivity.this,
					CreatPlansAcitivity.class);
			it.putExtra("time", time);
			it.putExtra(CreatPlansAcitivity.departName, departmentName);
			it.putExtra(CreatPlansAcitivity.departId, departmentId);
			if (getIntent().hasExtra("isWho")) {
				it.putExtra("isWho", "1");
			}
			if (!unNormalJob) {
				registerBroadcast();
				startActivity(it);
			} else {
				startActivity(it);
				finish();
			}

		} else if (type.equals("2")) {
			Intent it = new Intent(ChooseDepartActivity.this,
					SupervisionActivity.class);
			it.putExtra("time", time);
			it.putExtra(CreatPlansAcitivity.departName, departmentName);
			it.putExtra(CreatPlansAcitivity.departId, departmentId);

			finish();
			startActivity(it);
		} else if (type.equals("3")) {
			Intent it = new Intent(mcontext, FormListActivity.class);
			TaskVo data = new TaskVo();
			data.setMission_time(time);
			data.setDepartment(departmentId);
			data.setDepartmentName(departmentName);
			it.putExtra("data", data);
			registerBroadcast();
			startActivity(it);
		} else if (type.equals("4")) {
			Intent it = new Intent();
			it.setClass(ChooseDepartActivity.this,
					ConsumptionFormActivity.class);
			TaskVo data = new TaskVo();
			data.setStatus(2);
			data.setMission_time(time);
			data.setDepartment(departmentId);
			data.setDepartmentName(departmentName);
			it.putExtra("data", data);
			// startActivity(it);
			if (!unNormalJob) {
				registerBroadcast();
				startActivity(it);
			} else {
				startActivity(it);
				finish();
			}
			finish();

		}
	}

	List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
	DepartListAdapter departAdapter;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			departmentTv.setText("");
			AbViewUtil.colseVirtualKeyboard(ChooseDepartActivity.this);
			break;
		case R.id.tv_top_location:
			finish();
			break;
		case R.id.add_depart:
			Intent it = new Intent(mcontext, AddDepartmentActivity.class);
			it.putExtra("firstPostion", firstPosition);
			it.putExtra("data", (Serializable) departlist);
			startActivityForResult(it, 0x22);

			break;
		case R.id.btn_next:
			if (isAfter) {
				if (selectMap.size() > 0) {
					String[] departmentNames = new String[selectMap.size()];
					String[] departmentIds = new String[selectMap.size()];

					Iterator<String> iter = selectMap.keySet().iterator();

					int i = 0;
					while (iter.hasNext()) {
						ChildsVo value = selectMap.get(iter.next());
						departmentIds[i] = value.getId() + "";
						departmentNames[i] = value.getName();

						DebugUtil.debug("gogogo", departmentIds[i] + "  "
								+ departmentNames[i]);

						i++;

					}

					if (type.equals("1")) {
						Intent itent = new Intent(ChooseDepartActivity.this,
								CreatPlansAcitivity.class);
						itent.putExtra("time", time);
						itent.putExtra("isAfter", isAfter);
						if(getIntent().hasExtra("isWho")){
						itent.putExtra("isWho", "1");
						}
						itent.putExtra(CreatPlansAcitivity.departNames,
								departmentNames);
						itent.putExtra(CreatPlansAcitivity.departIds,
								departmentIds);
						finish();
						startActivity(itent);
					} else {
						for (int j = 0; j < departmentIds.length; j++) {
							Log.i("1111111111", departmentIds[j]);
							addData(2, departmentIds[j] + "",
									departmentNames[j] + "",
									departmentIds.length, j);
						}

						Toast.makeText(mcontext,
								"您已成功预设" + departmentIds.length + "个督导", 2000)
								.show();

					}

				} else {
					ToastUtils.showToast(mcontext, "请选择科室！");
				}

			}
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

					break;
				case SEND_FAILE:
					break;
				default:
					break;
				}
			}
		}
	};
	private List<TaskVo> newTaskList = new ArrayList<TaskVo>();

	// private void getDepartMentList() {
	// JSONObject job = new JSONObject();
	// JSONObject json = new JSONObject();
	// try {
	// job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
	// job.put("authent", tools.getValue(Constants.AUTHENT));
	// String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
	// .parseStrToMd5L32(job.toString()) + "comm/departmentInfo");
	// json.put("token", jobStr);
	// json.put("msg_content", job.toString());
	// Log.i("11111111", json.toString());
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// MainBizImpl.getInstance().onComomRequest(myHandler,
	// ChooseDepartActivity.this, DEPARTMENT_SUCESS, DEPARTMENT_FAILE,
	// job,"comm/departmentInfo");
	//
	// }

	protected void setDepartMentList(JSONObject json) {
		Log.i("111111", json + "");
		JSONArray jarr = new JSONArray();
		if (json.optString("result_id").equals("0")) {
			if (null != json.optJSONArray("levels")) {
				jarr = json.optJSONArray("levels");
				SharedPreferencesUtil.saveString(mcontext, "depart_levels",
						jarr.toString());
			} else {
				SharedPreferencesUtil.saveString(mcontext, "depart_levels", "");
			}
			List<DepartLevelsVo> list = gson.fromJson(jarr.toString(),
					new TypeToken<List<DepartLevelsVo>>() {
					}.getType());
			if (list.size() > 0) {
				departlist.clear();
				departlist.addAll(list);
			}
			departAdapter.notifyDataSetChanged();
			childList.addAll(departlist.get(firstPosition).getChilds());
			childAdapter.setData(childList);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (null != data && resultCode == 0x22) {
			ChildsVo dv = (ChildsVo) data.getSerializableExtra("data");
			String rooId = dv.getParent();
			if (isAfter) {
				for (DepartLevelsVo dlv : departlist) {
					if (rooId.equals("1") && dlv.getRoot().getId().equals("0")) {
						dlv.setChooseNum(dlv.getChooseNum() + 1);
						dlv.getChilds().add(0, dv);
						selectMap.put(dv.getId() + "", dv);
					} else if (dlv.getRoot().getId().equals(rooId)) {
						dlv.setChooseNum(dlv.getChooseNum() + 1);
						dlv.getChilds().add(0, dv);
						selectMap.put(dv.getId() + "", dv);
					}
				}
				departAdapter.setData(departlist);
				childList.addAll(departlist.get(firstPosition).getChilds());
				childAdapter.setData(childList);

			} else {
				dv.setChoosed(false);
				for (DepartLevelsVo dlv : departlist) {
					if (rooId.equals("1") && dlv.getRoot().getId().equals("0")) {
						// dlv.setChooseNum(dlv.getChooseNum() + 1);
						dlv.getChilds().add(0, dv);
					} else if (dlv.getRoot().getId().equals(rooId)) {
						// dlv.setChooseNum(dlv.getChooseNum() + 1);
						dlv.getChilds().add(0, dv);
					}
				}
				departAdapter.setData(departlist);
				childList.clear();
				childList.addAll(departlist.get(firstPosition).getChilds());
				childAdapter.setData(childList);
				if (type.equals("5")) {// 手卫生已新增任务修改科室
					Intent intent = new Intent();
					intent.putExtra("departmentId", dv.getId());
					intent.putExtra("departmentName", dv.getName());
					setResult(0x189, intent);
					finish();
				} else if (type.equals("6")) {// 重设默认科室
					Intent intent = new Intent();
					intent.putExtra("departmentId", dv.getId());
					intent.putExtra("departmentName", dv.getName());
					tools.putValue(Constants.DEFULT_DEPARTID, dv.getId());
					tools.putValue(Constants.DEFULT_DEPART_NAME, dv.getName());
					setResult(0x71, intent);
					finish();
				} else {
					doAfaterTask(dv.getName(), dv.getId());
				}
			}
		} else if (null != data && resultCode == 0x23) {
			ChildsVo dv = (ChildsVo) data.getSerializableExtra("data");
			String rooId = dv.getParent();
			if (isAfter) {
				for (DepartLevelsVo dlv : departlist) {

					for (ChildsVo clv : dlv.getChilds()) {
						if (clv.getId().equals(dv)) {
							if (!clv.isChoosed()) {
								if (rooId.equals("1")
										&& dlv.getRoot().getId().equals("0")) {
									dlv.setChooseNum(dlv.getChooseNum() + 1);
									dlv.getChilds().add(0, dv);
									selectMap.put(dv.getId() + "", dv);
								} else if (dlv.getRoot().getId().equals(rooId)) {
									dlv.setChooseNum(dlv.getChooseNum() + 1);
									dlv.getChilds().add(0, dv);
									selectMap.put(dv.getId() + "", dv);
								}
							}
						}
					}

				}
				departAdapter.setData(departlist);
				childList.clear();
				childList.addAll(departlist.get(firstPosition).getChilds());
				childAdapter.setData(childList);

			} else {
				dv.setChoosed(false);
				childAdapter.setData(childList);
				if (type.equals("5")) {// 手卫生已新增任务修改科室
					Intent intent = new Intent();
					intent.putExtra("departmentId", dv.getId());
					intent.putExtra("departmentName", dv.getName());
					setResult(0x189, intent);
					finish();
				} else if (type.equals("6")) {// 重设默认科室
					Intent intent = new Intent();
					intent.putExtra("departmentId", dv.getId());
					intent.putExtra("departmentName", dv.getName());
					tools.putValue(Constants.DEFULT_DEPARTID, dv.getId());
					tools.putValue(Constants.DEFULT_DEPART_NAME, dv.getName());
					setResult(0x71, intent);
					finish();
				} else {
					doAfaterTask(dv.getName(), dv.getId());
				}
			}
		}
	}

	// 预设督导本
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

	public void addData(int state, String id, String name, int length,
			int position) {
		Log.i("111111111", "执行了");
		TaskVo pdb = new TaskVo();
		pdb.setDepartmentName(name);
		pdb.setMobile(tools.getValue(Constants.MOBILE));
		pdb.setDepartment(id);
		pdb.setStatus(state);// 0 代表已同步，1代表未同步，2代表未完成,3代表未开始
		pdb.setType("2");
		;
		pdb.setHospital(tools.getValue(Constants.HOSPITAL_ID));
		pdb.setCheck_content("");
		pdb.setExist_problem("");
		pdb.setDeal_suggest("");
		Calendar calendar = null;
		calendar = new GregorianCalendar();// 子类实例化
		// pdb.setFiveTasks(pdblist);
		time = time + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
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
		time = sdf.format(date);
		pdb.setMission_time(time + "");
		newTaskList.add(0, pdb);

		try {

			DataBaseHelper.getDbUtilsInstance(mcontext).save(pdb);

			// 创建后要发广播通知已更新
			if (length == position + 1) {
				Intent brodcastIntent = new Intent();
				brodcastIntent.setAction(CalendarMainActivity.UPDATA_ACTION);
				ChooseDepartActivity.this.sendBroadcast(brodcastIntent);
				finish();
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	private static final int SEND_SUCESS = 0x03201;
	private static final int SEND_FAILE = 0x3202;

	public void sendEditor(String departId) {
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("department", departId);

			String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
					.parseStrToMd5L32(job.toString()) + "member/modifyUser");
			json.put("token", jobStr);
			json.put("msg_content", job.toString());
			Log.i("11111111", json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				ChooseDepartActivity.this, SEND_SUCESS, SEND_FAILE, job,
				"member/modifyUser");

	}

	@Override
	protected void onDestroy() {
		if (null != brodcast) {
			unregisterReceiver(brodcast);
		}
		super.onDestroy();
	}
}
