package com.deya.hospital.widget.popu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.widget.view.RadioGroupTextView;
import com.deya.hospital.widget.view.TextViewGroup;
import com.deya.hospital.widget.view.TextViewGroup.OnTextViewGroupItemClickListener;
import com.deya.hospital.widget.view.TextViewGroup.TextViewGroupItem;

public class JobDialog2 extends BaseDialog {
	
	List<JobListVo> jobList;
	List<JobListVo> jobTypeList;// 工作性质
	ChooseItem chooseInter;
	Context context;
	private String name;
	LinearLayout nameLay;
	int[] wh;
	boolean showNameEditor;
	private EditText nameEditor;
	private LayoutParams para;
	private LayoutParams para2;
	TextView titleTv;
	private RadioGroupTextView groupTextView1, groupTextView2;
	int position1 = -1;
	int position2 = -1;
	String workId = "";
	String jobId = "";
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
	public JobDialog2(Context context, String workId, String jobId,
			List<JobListVo> jobTypeList, List<JobListVo> jobList,
			ChooseItem chooseInter) {
		super(context);
		this.context = context;
		this.jobList = jobList;
		this.chooseInter = chooseInter;
		this.jobTypeList = jobTypeList;
		this.workId=workId;
		this.jobId=jobId;
//		if(TextUtils.isDigitsOnly(workId)){
//			position1=Integer.parseInt(workId);
//		}
//		if(TextUtils.isDigitsOnly(jobId)){
//			position2=Integer.parseInt(jobId);
//		}
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
	public JobDialog2(Context context, String name, String workId, String jobId,
			List<JobListVo> jobTypeList, List<JobListVo> jobList,
			ChooseItem chooseInter) {
		super(context);
		this.name = name;
		this.context = context;
		this.jobList = jobList;
		this.chooseInter = chooseInter;
		this.jobTypeList = jobTypeList;
		this.workId=workId;
		this.jobId=jobId;
//		if(TextUtils.isDigitsOnly(workId)){
//			position1=Integer.parseInt(workId);
//		}
//		if(TextUtils.isDigitsOnly(jobId)){
//			position2=Integer.parseInt(jobId);
//		}
		showNameEditor = true;
		wh = AbViewUtil.getDeviceWH(context);

	}

	public void setChoosePosition(int position) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_choose_job2);
		nameLay = (LinearLayout) this.findViewById(R.id.nameLay);
		 titleTv = (TextView) this.findViewById(R.id.titleTv);
		nameEditor = (EditText) this.findViewById(R.id.nameEditor);
		nameEditor.setText(name);
		
		if (!showNameEditor) {
			nameLay.setVisibility(View.GONE);
		} else {
			titleTv.setText("修改信息");
		}
		
		groupTextView1 = (RadioGroupTextView) this
				.findViewById(R.id.groupTextView1);
		groupTextView2 = (RadioGroupTextView) this
				.findViewById(R.id.groupTextView2);

		inigroupTextView1();
		inigroupTextView2();
		
		Button choose = (Button) this.findViewById(R.id.chooseBtn);
		choose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (showNameEditor) {
					chooseInter.getJobChoosePosition(nameEditor.getText()
							.toString(), position1, position2);
				} else {
					chooseInter.getJobChoosePosition( position1, position2);
				}
				dismiss();

			}
		});
	}
	
	private void inigroupTextView1() {


		List<TextViewGroup.TextViewGroupItem> list = new ArrayList<TextViewGroup.TextViewGroupItem>();

		if (null != jobTypeList && jobTypeList.size() > 0) {

			TextViewGroupItem item = null;
			for (int i = 0; i < jobTypeList.size(); i++) {
				item = groupTextView1.NewTextViewGroupItem();
				item.setText(jobTypeList.get(i).getName());
				if(jobTypeList.get(i).getId().equals(workId)){
					item.setStatus(true);
					position1=i;
				}
				
				list.add(item);
			}
			groupTextView1.setTextViews(list);
		}
		
		groupTextView1
				.setOnTextViewGroupItemClickListener(new OnTextViewGroupItemClickListener() {

					@Override
					public void OnTextViewGroupClick(View view,
							List<TextViewGroupItem> _dataList,
							TextViewGroupItem item) {
						// TODO Auto-generated method stub
						if(item.isStatus())
							position1 = item.getPosition();
						else
							position1=-1;
					}
				});

	}
	
	private void inigroupTextView2() {
		List<TextViewGroup.TextViewGroupItem> list = new ArrayList<TextViewGroup.TextViewGroupItem>();

		if (null != jobList && jobList.size() > 0) {

			TextViewGroupItem item = null;
			for (int i = 0; i < jobList.size(); i++) {
				item = groupTextView2.NewTextViewGroupItem();
				item.setText(jobList.get(i).getName());
				if(jobList.get(i).getId().equals(jobId)){
					item.setStatus(true);
					position2=i;
				}
				list.add(item);
			}
			groupTextView2.setTextViews(list);
		}
		
		groupTextView2
				.setOnTextViewGroupItemClickListener(new OnTextViewGroupItemClickListener() {

					@Override
					public void OnTextViewGroupClick(View view,
							List<TextViewGroupItem> _dataList,
							TextViewGroupItem item) {
						// TODO Auto-generated method stub
						if(item.isStatus())
							position2 = item.getPosition();
						else
							position2=-1;
					}
				});

	}

	public interface ChooseItem {
		public void getJobChoosePosition(int positon1, int position2);

		public void getJobChoosePosition(String name, int positon1,
				int position2);
	}
}
