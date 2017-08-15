package com.deya.hospital.consumption;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.JobListAdapter;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.vo.JobListVo;

public class JobDialog extends BaseDialog {
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
	private EditText nameEditor;
	private LayoutParams para;
	private LayoutParams para2;
	TextView titleTv;
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
	public JobDialog(Context context, String workId, String jobId,
			List<JobListVo> jobTypeList, List<JobListVo> jobList,
			ChooseItem chooseInter) {
		super(context);
		this.context = context;
		this.jobList = jobList;
		this.chooseInter = chooseInter;
		this.jobTypeList = jobTypeList;
		this.workId = workId;
		this.jobId = jobId;
		wh = AbViewUtil.getDeviceWH(context);
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
	public JobDialog(Context context, String name, String workId, String jobId,
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

	}

	public void setChoosePosition(int position) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_choose_job);
		nameLay = (LinearLayout) this.findViewById(R.id.nameLay);
		 titleTv = (TextView) this.findViewById(R.id.titleTv);
		nameEditor = (EditText) this.findViewById(R.id.nameEditor);
		nameEditor.setText(name);
		if (!showNameEditor) {
			nameLay.setVisibility(View.GONE);
		} else {
			titleTv.setText("修改信息");
		}
		para2 = new LayoutParams((wh[0] - dp2Px(150)) / 3,
				(wh[0] - dp2Px(170)) / 4);
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
		Button choose = (Button) this.findViewById(R.id.chooseBtn);
		choose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (showNameEditor) {
					chooseInter.getJobChoosePosition(nameEditor.getText()
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
				}
			}
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.chooseItem(position);

			}
		});
	}

	public interface ChooseItem {
		public void getJobChoosePosition(int positon1, int position2);

		public void getJobChoosePosition(String name, int positon1,
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
			tx.setLayoutParams(para);
			return convertView;
		}

	}

	public int dp2Px(int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}
}
