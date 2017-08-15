package com.deya.hospital.supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.SimpleSwitchButton;
import com.deya.hospital.widget.view.MultipleTextViewGroup;
import com.deya.hospital.widget.view.RadioGroupTextView2;
import com.deya.hospital.widget.view.SingleLineTextViewGroup;
import com.deya.hospital.widget.view.TextViewGroup;
import com.deya.hospital.widget.view.TextViewGroup.OnTextViewGroupItemClickListener;
import com.deya.hospital.widget.view.TextViewGroup2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 现场反馈
 * 
 * @author yung
 * @date 创建时间：2016年1月15日 上午11:54:19
 * @version 1.0
 */
public class SupervisorFeedBackActivity extends BaseActivity implements
		OnClickListener {
	private EditText edt_name, edt_remark;
	private Button btn_enter;
	private LinearLayout lay_list, lay_times, lay_ftime;
	private MultipleTextViewGroup rl;
	private ListView listivew1, listivew2, listivew3, listivew4;
	private MyAdapter adapter;

	private TextView text_yes, text_no, text_t1, text_t2, text_t3, text_t4,
			text_none;
	private ImageView img_yes, img_no;

	private RadioGroupTextView2 groupTextView;

	/**
	 * 标识 传值用
	 * 已选择的卫生用品设施调查 item id list
	 * 
	 */
	public static final String INTENT_REASONS = "reasons";
	public static final String INTENT_REASONS_NAME = "reasonName";
	/**
	 * 标识 传值用
	 * 培训时间 id
	 */
	public static final String INTENT_TIMEINDEX = "timeIndex";
	/**
	 * 标识 传值用
	 * 反馈记录文本
	 */
	public static final String INTENT_REMARK = "remark";
	/**
	 * 是否反饋到科室
	 */
	public static  final String FEEDBACK_DEPARTMENT="feedback_department";
	/**
	 * 是否再次督導
	 */
	public static  final String AGAIN_SUP="again_sup";
	/**
	 * 标识 传值用
	 * 被反馈人
	 */
	public static final String INTENT_NANE = "name";
	/**
	 * 标识 传值用
	 *  卫生手消毒不规范list
	 */
	public static final String INTENT_RECORDALLLIST_DISINFECTION = "record_disinfection";
	/**
	 * 标识 传值用
	 *  洗手不规范list
	 */
	public static final String INTENT_RECORDALLLIST_WASHHANDS = "record_washhands";
	/**
	 * 标识 传值用
	 *  带手套不规范list
	 */
	public static final String INTENT_RECORDALLLIST_GLOVE = "record_glove";
	/**
	 * 标识 传值用
	 *  未采取措施不规范list
	 */
	public static final String INTENT_RECORDALLLIST_NOTHING = "record_nothing";
	/**
	 * 标识 传值用
	 *  用品设施list
	 */
	public static final String INTENT_PARAMS_EQUIP = "params_equip";
	/**
	 * 标识 传值用
	 * 培训时间list
	 */
	public static final String INTENT_PARAMS_TRAIN = "params_train";

	/**
	 * 标识 传值用
	 *  是否基本版
	 */
	public static final String INTENT_ISWHO = "iswho";
	private SingleLineTextViewGroup linegroupview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_super_feedback);
		initViews();
		initSingleView();
		initdata();
		iniMultipleTextView();
		initRecords();
		initTimes();
		setRecordAdapter();
	}

	/**
	 * 初始化选项卡 洗手 卫生手消毒 戴手套 未采取措施
	 */
	private void initSingleView() {
		// TODO Auto-generated method stub
		List<TextViewGroup.TextViewGroupItem> recrodTabList = new ArrayList<TextViewGroup.TextViewGroupItem>();

		linegroupview
				.setOnTextViewGroupItemClickListener(new OnTextViewGroupItemClickListener() {

					@Override
					public void OnTextViewGroupClick(View view,
							List<TextViewGroup.TextViewGroupItem> _dataList,
							TextViewGroup.TextViewGroupItem item) {
						// TODO Auto-generated method stub
						if (null != item.getTag())
							changeReCordAdapter(item.getTag().toString());
					}
				});

		TextViewGroup.TextViewGroupItem item = linegroupview
				.NewTextViewGroupItem();
		item.setStatus(true);
		item.setText(res.getString(R.string.record_washhands_string));
		item.setTag("4");
		recrodTabList.add(item);

		item = linegroupview.NewTextViewGroupItem();
		item.setStatus(false);
		item.setText(res.getString(R.string.record_disinfection_string));
		item.setTag("5");
		recrodTabList.add(item);

		item = linegroupview.NewTextViewGroupItem();
		item.setStatus(false);
		item.setText(res.getString(R.string.record_glove_string));
		item.setTag("6");
		recrodTabList.add(item);

		item = linegroupview.NewTextViewGroupItem();
		item.setStatus(false);
		item.setText(res.getString(R.string.record_nothing_string));
		item.setTag("0");
		recrodTabList.add(item);

		linegroupview.setTextViews(recrodTabList);
	}

	/**
	 * 培训时间 id
	 */
	int timeIndex = -1;
	boolean isTimes;

	/**
	 * 初始化培训时间数据 圆形控件
	 */
	private void initTimes() {

		showTimes(timeIndex >= 0);

		ArrayList<TextViewGroup2.TextViewGroupItem> list = new ArrayList<TextViewGroup2.TextViewGroupItem>();

		if (null != trainingList && trainingList.size() > 0) {

			TextViewGroup2.TextViewGroupItem item = null;
			for (int i = 0; i < trainingList.size(); i++) {
				item = groupTextView.NewTextViewGroupItem();
				item.setText(trainingList.get(i).get("name").toString());

				if (trainingList.get(i).containsKey("id")
						&& null != trainingList.get(i).get("id")) {
					item.setTag(trainingList.get(i).get("id"));
					if (trainingList.get(i).get("id").toString()
							.equals(timeIndex + "")) {
						item.setStatus(true);
					}
				}

				list.add(item);
			}
			groupTextView.setTextViews(list);
		}

	}

	/**
	 * 用品设施
	 */
	private ArrayList<HashMap<String, Object>> equipList = new ArrayList<HashMap<String, Object>>();
	/**
	 * 时间周期
	 */
	private ArrayList<HashMap<String, Object>> trainingList = new ArrayList<HashMap<String, Object>>();

	/**
	 * 不规范列表 adapter
	 */
	MyAdapter nothindAdapter, disnifectionAdapter, washhandsAdapter,
			gloveAdapter;
	
	/**
	 * 是否基本版
	 */
	boolean isWho;
	private CommonTopView topView;
int is_feedback_department;
	int setIs_again_supervisor;
	/**
	 * 初始化数据
	 */
	private void initdata() {
		Intent intent = getIntent();
		reasonIds = intent.getIntegerArrayListExtra(INTENT_REASONS);
		timeIndex = intent.getIntExtra("timeIndex", -1);

		recordDisinfectionList = (ArrayList<HashMap<String, String>>) getIntent()
				.getSerializableExtra(INTENT_RECORDALLLIST_DISINFECTION);
		recordWashHandsList = (ArrayList<HashMap<String, String>>) getIntent()
				.getSerializableExtra(INTENT_RECORDALLLIST_WASHHANDS);
		recordGloveList = (ArrayList<HashMap<String, String>>) getIntent()
				.getSerializableExtra(INTENT_RECORDALLLIST_GLOVE);
		recordNothingList = (ArrayList<HashMap<String, String>>) getIntent()
				.getSerializableExtra(INTENT_RECORDALLLIST_NOTHING);

		equipList = (ArrayList<HashMap<String, Object>>) getIntent()
				.getSerializableExtra(INTENT_PARAMS_EQUIP);
		trainingList = (ArrayList<HashMap<String, Object>>) getIntent()
				.getSerializableExtra(INTENT_PARAMS_TRAIN);

		disnifectionAdapter = new MyAdapter(recordDisinfectionList);
		nothindAdapter = new MyAdapter(recordNothingList);
		washhandsAdapter = new MyAdapter(recordWashHandsList);
		gloveAdapter = new MyAdapter(recordGloveList);

		edt_name.setText(intent.getStringExtra(INTENT_NANE));
		edt_remark.setText(intent.getStringExtra(INTENT_REMARK));

		isWho = getIntent().getBooleanExtra(INTENT_ISWHO, true);
		if (isWho) {
			lay_list.setVisibility(View.GONE);
		} else {
			lay_list.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 动态设置培训时间显示
	 * 
	 * @param istime
	 */
	private void showTimes(boolean istime) {
		this.isTimes = istime;

		if (isTimes) {
			lay_times.setVisibility(View.VISIBLE);

			img_yes.setImageResource(R.drawable.img_yes_p);
			img_no.setImageResource(R.drawable.img_no_d);

		} else {
			lay_times.setVisibility(View.GONE);

			img_yes.setImageResource(R.drawable.img_yes_d);
			img_no.setImageResource(R.drawable.img_no_p);
			timeIndex = -1;
		}
	}

	/**
	 * 动态选择培训时间
	 * 
	 */
	private void setTimes(Object tag) {
		if (lay_ftime == null || tag == null)
			return;
		for (int i = 0; i < lay_ftime.getChildCount(); i++) {
			View v = lay_ftime.getChildAt(i);
			if (v instanceof TextView) {
				if (v.getTag() != null
						&& tag.toString().equals(v.getTag().toString())) {
					((TextView) v)
							.setBackgroundResource(R.drawable.round_orange);
					((TextView) v).setTextColor(res.getColor(R.color.white));
				} else {
					((TextView) v)
							.setBackgroundResource(R.drawable.sharp_btn_blue);
					((TextView) v).setTextColor(res.getColor(R.color.blue_));
				}
			}
		}

		if (null != tag && TextUtils.isDigitsOnly(tag.toString())) {
			timeIndex = Integer.parseInt(tag.toString());
		} else {
			timeIndex = -1;
		}
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		topView=(CommonTopView) findViewById(R.id.topView);
		topView.init(this);

		btn_enter = (Button) findViewById(R.id.btn_enter);
		btn_enter.setOnClickListener(this);

		lay_list = (LinearLayout) findViewById(R.id.lay_list);
		lay_times = (LinearLayout) findViewById(R.id.lay_times);

		lay_ftime = (LinearLayout) findViewById(R.id.lay_ftime);

		groupTextView = (RadioGroupTextView2) findViewById(R.id.lay_time_radio);
		groupTextView
				.setOnTextViewGroupItemClickListener(new TextViewGroup2.OnTextViewGroupItemClickListener2() {

					@Override
					public void OnTextViewGroupClick(View view,
							List<TextViewGroup2.TextViewGroupItem> _dataList,
							TextViewGroup2.TextViewGroupItem item) {
						// TODO Auto-generated method stub
						if (null != item.getTag()) {
							if (TextUtils
									.isDigitsOnly(item.getTag().toString())) {
								timeIndex = Integer.parseInt(item.getTag()
										.toString());
							}
						}
					}
				});

		edt_name = (EditText) findViewById(R.id.edt_name);

		rl = (MultipleTextViewGroup) findViewById(R.id.main_rl);
		rl.setOnTextViewGroupItemClickListener(new OnTextViewGroupItemClickListener() {

			@Override
			public void OnTextViewGroupClick(View view,
					List<TextViewGroup.TextViewGroupItem> _dataList,
					TextViewGroup.TextViewGroupItem item) {
				// TODO Auto-generated method stub

			}
		});
		edt_remark = (EditText) findViewById(R.id.edt_remark);

		text_yes = (TextView) findViewById(R.id.text_yes);
		text_no = (TextView) findViewById(R.id.text_no);

		text_none = (TextView) findViewById(R.id.text_none);

		img_yes = (ImageView) findViewById(R.id.img_yes);
		img_no = (ImageView) findViewById(R.id.img_no);

		linegroupview = (SingleLineTextViewGroup) findViewById(R.id.linegroupview);

		text_yes.setOnClickListener(timeClick);
		text_no.setOnClickListener(timeClick);
		img_yes.setOnClickListener(timeClick);
		img_no.setOnClickListener(timeClick);

		text_t1 = (TextView) findViewById(R.id.text_t1);
		text_t2 = (TextView) findViewById(R.id.text_t2);
		text_t3 = (TextView) findViewById(R.id.text_t3);
		text_t4 = (TextView) findViewById(R.id.text_t4);

		text_t1.setOnClickListener(recordClick);
		text_t2.setOnClickListener(recordClick);
		text_t3.setOnClickListener(recordClick);
		text_t4.setOnClickListener(recordClick);

		listivew1 = (ListView) findViewById(R.id.listivew1);
		listivew2 = (ListView) findViewById(R.id.listivew2);
		listivew3 = (ListView) findViewById(R.id.listivew3);
		listivew4 = (ListView) findViewById(R.id.listivew4);
		SimpleSwitchButton feedbackSwitch = (SimpleSwitchButton) this.findViewById(R.id.feedbackSwitch);

		feedbackSwitch.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
			@Override
			public void onCheckChange(boolean ischeck) {
				is_feedback_department=ischeck ? 1 : 0;

			}
		});
		is_feedback_department=getIntent().getIntExtra(FEEDBACK_DEPARTMENT,0);
		feedbackSwitch.setCheck(is_feedback_department==1?true:false);

      setIs_again_supervisor=getIntent().getIntExtra(AGAIN_SUP,0);
		SimpleSwitchButton reSupSwitch = (SimpleSwitchButton) this.findViewById(R.id.reSupSwitch);
		reSupSwitch.setText("再次督导");

		reSupSwitch.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
			@Override
			public void onCheckChange(boolean ischeck) {
			setIs_again_supervisor=ischeck ? 1 : 0;

			}
		});
		reSupSwitch.setCheck(setIs_again_supervisor==1?true:false);
	}

	/**
	 * 操作项目点击事件
	 */
	OnClickListener recordClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.text_t1:
				setRecordList(v.getTag().toString());
				break;
			case R.id.text_t2:
				setRecordList(v.getTag().toString());
				break;
			case R.id.text_t3:
				setRecordList(v.getTag().toString());
				break;
			case R.id.text_t4:
				setRecordList(v.getTag().toString());
				break;
			default:
				break;
			}
		}
	};

	Handler h = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			setRecordList(msg.what + "");
		};
	};

	/**
	 * 培训时间点击事件
	 */
	OnClickListener timeClick2 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setTimes(v.getTag());
		}
	};

	/**
	 * 培训时间点击事件
	 */
	OnClickListener timeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.text_yes:
			case R.id.img_yes:
				showTimes(true);
				break;
			case R.id.text_no:
			case R.id.img_no:
				showTimes(false);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 设置不规范list adapter
	 */
	private void setRecordAdapter() {
		setRecordAdapter(listivew1, washhandsAdapter);
		setRecordAdapter(listivew2, disnifectionAdapter);
		setRecordAdapter(listivew3, gloveAdapter);
		setRecordAdapter(listivew4, nothindAdapter);

	}

	/**
	 * 设置操作项目数据 不规范原因记录
	 * 
	 * @param index
	 */
	private void setRecordList(String index) {

		text_t1.setTextColor(res.getColor(R.color.blue_));
		text_t1.setBackgroundResource(R.drawable.btn_feedback_record_d);
		text_t2.setTextColor(res.getColor(R.color.blue_));
		text_t2.setBackgroundResource(R.drawable.btn_feedback_record_d);
		text_t3.setTextColor(res.getColor(R.color.blue_));
		text_t3.setBackgroundResource(R.drawable.btn_feedback_record_d);
		text_t4.setTextColor(res.getColor(R.color.blue_));
		text_t4.setBackgroundResource(R.drawable.btn_feedback_record_d);

		if (index.equals(text_t1.getTag().toString())) {
			text_t1.setTextColor(res.getColor(R.color.white));
			text_t1.setBackgroundResource(R.drawable.btn_feedback_record_s);
		} else if (index.equals(text_t2.getTag().toString())) {
			text_t2.setTextColor(res.getColor(R.color.white));
			text_t2.setBackgroundResource(R.drawable.btn_feedback_record_s);
		} else if (index.equals(text_t3.getTag().toString())) {
			text_t3.setTextColor(res.getColor(R.color.white));
			text_t3.setBackgroundResource(R.drawable.btn_feedback_record_s);
		} else if (index.equals(text_t4.getTag().toString())) {
			text_t4.setTextColor(res.getColor(R.color.white));
			text_t4.setBackgroundResource(R.drawable.btn_feedback_record_s);
		}

		changeReCordAdapter(index);
	}

	/**
	 * 不规范原因列表选项卡切换
	 * 
	 * @param index
	 */
	private void changeReCordAdapter(String index) {
		listivew1.setVisibility(View.GONE);
		listivew2.setVisibility(View.GONE);
		listivew3.setVisibility(View.GONE);
		listivew4.setVisibility(View.GONE);

		text_none.setVisibility(View.GONE);

		if (index.equals("5")) {
			if (null == recordDisinfectionList
					|| recordDisinfectionList.size() == 0) {
				text_none.setVisibility(View.VISIBLE);
			} else {

				listivew2.setVisibility(View.VISIBLE);
			}
		} else if (index.equals("4")) {
			if (null == recordWashHandsList || recordWashHandsList.size() == 0) {
				text_none.setVisibility(View.VISIBLE);
			} else {
				listivew1.setVisibility(View.VISIBLE);
			}
		} else if (index.equals("6")) {
			if (null == recordGloveList || recordGloveList.size() == 0) {
				text_none.setVisibility(View.VISIBLE);
			} else {
				listivew3.setVisibility(View.VISIBLE);
			}
		} else if (index.equals("0")) {
			if (null == recordNothingList || recordNothingList.size() == 0) {
				text_none.setVisibility(View.VISIBLE);
			} else {
				listivew4.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 不规范原因记录 设置listview 底部view
	 */
	private void setRecordAdapter(ListView listview, MyAdapter myAdapter) {

		if (myAdapter.getCount() == 0) {
			TextView footerView = new TextView(mcontext);
			footerView.setText("没有记录！");
			footerView.setTextColor(res.getColor(R.color.greytext));
			footerView.setPadding(10, 20, 10, 20);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			footerView.setLayoutParams(lp);
			footerView.setGravity(Gravity.CENTER);
			footerView.setBackgroundResource(R.color.white);
			listview.addFooterView(footerView);
		} else {
			listview.setAdapter(myAdapter);
		}
	}

	// 卫生消毒操作的不规范记录
	List<HashMap<String, String>> recordDisinfectionList = new ArrayList<HashMap<String, String>>();
	// 洗手操作的不规范记录
	List<HashMap<String, String>> recordWashHandsList = new ArrayList<HashMap<String, String>>();
	// 戴手套操作的不规范记录
	List<HashMap<String, String>> recordGloveList = new ArrayList<HashMap<String, String>>();
	// 未采取措施的不规范记录
	List<HashMap<String, String>> recordNothingList = new ArrayList<HashMap<String, String>>();

	// 相应操作的不规范记录
	// List<HashMap<String, String>> recordList = new ArrayList<HashMap<String,
	// String>>();

	/**
	 * 初始化操作不规范记录
	 */
	private void initRecords() {
		if (isWho)
			return;
		if (null == recordDisinfectionList)
			recordDisinfectionList = new ArrayList<HashMap<String, String>>();
		if (null == recordWashHandsList)
			recordWashHandsList = new ArrayList<HashMap<String, String>>();
		if (null == recordGloveList)
			recordGloveList = new ArrayList<HashMap<String, String>>();
		if (null == recordNothingList)
			recordNothingList = new ArrayList<HashMap<String, String>>();

		int r_couts = recordDisinfectionList.size()
				+ recordWashHandsList.size() + recordGloveList.size()
				+ recordNothingList.size();
		if (r_couts > 0) {
			lay_list.setVisibility(View.VISIBLE);

			text_t1.setText(res.getString(R.string.record_washhands_string));
			text_t1.setTag("4");

			text_t2.setText(res.getString(R.string.record_disinfection_string));
			text_t2.setTag("5");

			text_t3.setText(res.getString(R.string.record_glove_string));
			text_t3.setTag("6");

			text_t4.setText(res.getString(R.string.record_nothing_string));
			text_t4.setTag("0");

			setRecordList(text_t1.getTag().toString());

		} else {
			lay_list.setVisibility(View.GONE);
		}
	}

	// 设施调查
	List<TextViewGroup.TextViewGroupItem> reasonList = new ArrayList<TextViewGroup.TextViewGroupItem>();
	// 已选择的设施调查
	ArrayList<String> reasons = new ArrayList<>();
	// 已选择的设施调查ID
	ArrayList<Integer> reasonIds = new ArrayList<Integer>();

	/**
	 * 设置不规则排序控件数据
	 */
	private void iniMultipleTextView() {
		// String[] as = mcontext.getResources().getStringArray(
		// R.array.facilities_investigation_string);
		if (null != equipList && equipList.size() > 0) {
			TextViewGroup.TextViewGroupItem item = null;
			for (int i = 0; i < equipList.size(); i++) {
				item = rl.NewTextViewGroupItem();
				item.setText(equipList.get(i).get("name").toString());
				String id_str = equipList.get(i).get("id").toString();
				if (TextUtils.isDigitsOnly(id_str)) {
					item.setStatus(reasonIds.contains(Integer.parseInt(id_str)));
					item.setPosition(Integer.parseInt(id_str));
				}
				reasonList.add(item);
			}
			rl.setTextViews(reasonList);
		}
	}

	/*
	 * 保存反馈
	 */
	private void setFeedBack() {
		// reasons.clear();
		reasonIds.clear();
		if (null != reasonList && reasonList.size() > 0) {
			for (int i = 0; i < reasonList.size(); i++) {
				if (reasonList.get(i).isStatus()) {
					reasons.add(reasonList.get(i).getText());
					reasonIds.add(reasonList.get(i).getPosition());
				}
			}
		}

		String rm = edt_remark.getText().toString().toString();
		String name = edt_name.getText().toString().toString();

		Intent it = new Intent();
		it.putExtra(INTENT_REMARK, rm);
		it.putExtra(INTENT_NANE, name);
		it.putIntegerArrayListExtra(INTENT_REASONS, reasonIds);
		it.putExtra(INTENT_TIMEINDEX, timeIndex);
		it.putStringArrayListExtra(INTENT_REASONS_NAME,reasons);
		it.putExtra(FEEDBACK_DEPARTMENT,is_feedback_department);
		it.putExtra(AGAIN_SUP,setIs_again_supervisor);
		setResult(RESULT_OK, it);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_enter:
			setFeedBack();
			this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 不规范原因记录适配
	 * 
	 * @author yung
	 * @date 创建时间：2016年1月15日 下午3:27:04
	 * @version 1.0
	 */
	private class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		List<HashMap<String, String>> recordList;

		public MyAdapter(List<HashMap<String, String>> recordList) {
			this.recordList = recordList;
			if (null == recordList) {
				this.recordList = new ArrayList<HashMap<String, String>>();
			}
			mInflater = LayoutInflater.from(mcontext);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return recordList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return recordList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new Holder();
				convertView = mInflater
						.inflate(R.layout.remark_list_item, null);
				holder.text_job = (TextView) convertView
						.findViewById(R.id.text_job);
				holder.text_remark = (TextView) convertView
						.findViewById(R.id.text_remark);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
				clearHolder(holder);
			}

			if (position < recordList.size()) {
				HashMap<String, String> item = recordList.get(position);
				String left = "";
				if (item.containsKey("hos")
						&& !AbStrUtil.isEmpty(item.get("hos")))
					left = "\u2000" + item.get("hos");

				if (item.containsKey("job")
						&& !AbStrUtil.isEmpty(item.get("job")))
					left = left + "\u2000" + item.get("job");
				if (item.containsKey("name")
						&& !AbStrUtil.isEmpty(item.get("name")))
					left = left + "\u2000" + item.get("name");

				if (left.length() == 0) {
					left = "\u2000" + left;
				}
				left = left + "\u2000";
				holder.text_job.setText(left);
				if (item.containsKey("reason")
						&& !AbStrUtil.isEmpty(item.get("reason"))) {
					holder.text_remark.setText(left + "\u3000"
							+ item.get("reason"));
				} else {
					holder.text_remark.setText(left + "\u3000" + "不规范");
				}
			}
			return convertView;
		}

		void clearHolder(Holder holder) {
			holder.text_job.setText("");
			holder.text_remark.setText("");
		}

		Holder holder = null;

		class Holder {
			TextView text_job, text_remark;
		}

	}

}
