package com.deya.hospital.workspace.handwash;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.JobListAdapter;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.base.EasyBaseAdapter;
import com.deya.hospital.base.EasyViewHolder;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.vo.HistoryCache;
import com.deya.hospital.vo.JobListVo;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

public class JobDialogHis extends BaseDialog {
	GridView listView;
	List<JobListVo> jobList;
	List<JobListVo> jobTypeList;// 工作性质
	JobListAdapter adapter;
	GridView gv;
	ChooseItem chooseInter;
	Context context;
	GvAdapter gvAdapter;
	String workId = "";
	String jobId = "";
	private String name;
	LinearLayout nameLay;
	int[] wh;
	boolean showNameEditor;
	private LayoutParams para;
	private LayoutParams para2;
	TextView titleTv;
	private EditText autoCompleteTextView;
	private ListView auto_list;
	private List<HistoryCache> nameLists = new ArrayList<>();
	private List<String> names = new ArrayList();
	private MAdapter mArrayAdapter = null;
	Button choose;
	/**
	 *
	 * @param context
	 * @param workId
	 *            工作性质ID
	 * @param jobId
	 *            岗位ID
	 * @param jobTypeList
	 *            工作性质数据列表
	 * @param jobList
	 *            岗位列表
	 * @param chooseInter
	 *            回调接口
	 */
	public JobDialogHis(Context context, String workId, String jobId,
						List<JobListVo> jobTypeList, List<JobListVo> jobList,
						ChooseItem chooseInter) {
		super(context);
		this.context = context;
		this.jobList = jobList;
		this.chooseInter = chooseInter;
		this.jobTypeList = jobTypeList;
		this.workId = workId;
		this.jobId = jobId;
	}

	public void setTitleTv(String title){
		titleTv.setText(title);
	}

	/**
	 *
	 * @param context
	 * @param name
	 *            被调查人姓名
	 * @param workId
	 *            工作性质ID
	 * @param jobId
	 *            岗位ID
	 * @param jobTypeList
	 *            工作性质数据列表
	 * @param jobList
	 *            岗位列表
	 * @param chooseInter
	 *            回调接口
	 */
	public JobDialogHis(Context context, String name, String workId, String jobId,
						List<JobListVo> jobTypeList, List<JobListVo> jobList,
						ChooseItem chooseInter) {
		super(context);
		this.name = name;
		this.context = context;
		this.jobList = jobList;
		this.chooseInter = chooseInter;
		this.jobTypeList = jobTypeList;
		this.workId = workId;
		this.jobId = jobId;
		showNameEditor = true;
		wh = AbViewUtil.getDeviceWH(context);
		getHistoryData();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_choose_his);
		nameLay = (LinearLayout) this.findViewById(R.id.nameLay);
		 titleTv = (TextView) this.findViewById(R.id.titleTv);
		autoCompleteTextView = (EditText) findViewById(R.id.autoCompleteTextView);
		auto_list = (ListView) findViewById(R.id.auto_list);
		autoCompleteTextView.setText(name);
		if (!showNameEditor) {
			nameLay.setVisibility(View.GONE);
		} else {
			titleTv.setText("修改信息");
		}
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int with = WindowManager.LayoutParams.MATCH_PARENT;
		lp.width=with;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);

		para2 = new LayoutParams((wh[0]*3/4 - 60)/3,
				(wh[0] - 60)/4);
		adapter = new JobListAdapter(context, jobList, para2);
		para = new LayoutParams((wh[0] - dp2Px(160)) / 4,
				(wh[0] - dp2Px(160)) / 4);
		listView = (GridView) this.findViewById(R.id.dialog_list);
		gv = (GridView) this.findViewById(R.id.gv);
		gvAdapter = new GvAdapter();
		gv.setAdapter(gvAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				
				gvAdapter.setChooseItem(position);
			}
		});
		if (!AbStrUtil.isEmpty(workId)) {
			for (int j = 0; j < jobTypeList.size(); j++) {
				if (workId.equals(jobTypeList.get(j).getId())) {
					chooseIndex = j;
					gvAdapter.notifyDataSetChanged();
				}
			}
		}
		choose= (Button) this.findViewById(R.id.chooseBtn);
		choose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setHistoryCache();
				if (showNameEditor) {
					if(adapter.getChooseIndex()<0){
						ToastUtil.showMessage("请选择岗位！");
						return;
					}
					chooseInter.getJobChoosePosition(autoCompleteTextView.getText()
							.toString(), chooseIndex, adapter.getChooseIndex());
				} else {
					chooseInter.getJobChoosePosition(chooseIndex, adapter.getChooseIndex());
				}
				dismiss();

			}
		});

		listView.setAdapter(adapter);
		if (!AbStrUtil.isEmpty(jobId)) {
			for (int i = 0; i < jobList.size(); i++) {
				if (jobId.equals(jobList.get(i).getId())) {
					adapter.chooseItem(i);
					choose.setEnabled(true);
				}
			}
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				adapter.chooseItem(position);
				AbViewUtil.colseVirtualKeyboard(context);
				if(adapter.getChooseIndex()>=0){
					choose.setEnabled(true);
				}else{

				}


			}
		});


		for (int i = 0; i < nameLists.size(); i++) {
			names.add(nameLists.get(i).getKey());
		}


		if (names.size() > 0 ) {
			mArrayAdapter  = new MAdapter(context,R.layout.item_handtask_txt,names);
			auto_list.setAdapter(mArrayAdapter);

			autoCompleteTextView.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					auto_list.setVisibility(View.VISIBLE);
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

					names.clear();
					if (s.toString().trim().length() > 0) {
						for (HistoryCache dv : nameLists) {
							if (dv.getKey().contains(s.toString())) {
								names.add(dv.getKey());
							}
						}
					} else {
						for (HistoryCache dv : nameLists) {
							names.add(dv.getKey());
						}
					}
					mArrayAdapter.notifyDataSetChanged();
				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});
			auto_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					autoCompleteTextView.setText(names.get(position));
					auto_list.setVisibility(View.GONE);
				}
			});
		}
	}

	class MAdapter extends EasyBaseAdapter<String> {

		private TextView btn_name;

		public MAdapter(Context context, int layoutId, List<String> list) {
			super(context, layoutId, list);
		}

		@Override
		public void convert(EasyViewHolder viewHolder, String s) {
			btn_name = viewHolder.getTextView(R.id.btn_name);
			btn_name.setText(s);
		}
	}

	public void getHistoryData() {
		try {
			if (null != DataBaseHelper.getDbUtilsInstance(context).findAll(HistoryCache.class)) {
				nameLists = DataBaseHelper.getDbUtilsInstance(context).findAll(HistoryCache.class);
			} else {
				DataBaseHelper.getDbUtilsInstance(context).saveAll(nameLists);
			}

		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	private void setHistoryCache() {
		String text = autoCompleteTextView.getText().toString().trim();
		boolean needAdd = true;
		if (!AbStrUtil.isEmpty(text)) {
			for (HistoryCache sv : nameLists) {
				if (sv.getKey().equals(text+"")) {
					needAdd = false;
					break;
				}
			}
		} else {
			needAdd = false;
		}

		if (needAdd) {
			HistoryCache sv = new HistoryCache();
			sv.setKey(text);
			nameLists.add(0, sv);
			try {
				DataBaseHelper.getDbUtilsInstance(context)
						.deleteAll(HistoryCache.class);
				DataBaseHelper.getDbUtilsInstance(context)
						.saveAll(nameLists);
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
	}


	public interface ChooseItem {
		 void getJobChoosePosition(int positon1, int position2);

		 void getJobChoosePosition(String name, int positon1,
								   int position2);
	}

	int chooseIndex = -1;
	public class GvAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jobTypeList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		
		public void setChooseItem(int position){
			 if(chooseIndex==position){
				 chooseIndex=-1;
			 }else{
				 chooseIndex=position;
			 }
			 notifyDataSetChanged();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_job_type, null);
			TextView tx = (TextView) convertView.findViewById(R.id.textView);
			if (chooseIndex != position) {
				tx.setBackgroundResource(R.drawable.circle_sharp_blue);
				tx.setTextColor(context.getResources().getColor(
						R.color.top_color));
			} else {
				tx.setBackgroundResource(R.drawable.round_orange);
				tx.setTextColor(context.getResources().getColor(R.color.white));
			}
			tx.setText(jobTypeList.get(position).getName());
			//tx.setLayoutParams(para);
			return convertView;
		}

	}

	public int dp2Px(int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}
}
