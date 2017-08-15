package com.deya.hospital.widget.view;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.supervisor.JobDialog;
import com.deya.hospital.util.ImageUtil;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.widget.popu.JobDialog2;

/**
 * 自定义spinner
* @author  yung
* @date 创建时间：2016年1月15日 上午11:39:56 
* @version 1.0
 */
public class DySpinner extends LinearLayout implements OnClickListener  {
	private View view;
	private Context mcontext;
	private TextView text_sl;
	private ImageView img_sl;
	private RelativeLayout lay_sl;
	private Resources res;
	
	public DySpinner(Context context) {
		super(context);
		init(context);
	}

	public DySpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init(context);
		
		res=context.getResources();
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.dyspinner);
		int textColor =a.getColor(R.styleable.dyspinner_sptextcolor,
				res.getColor(R.color.black));
		text_sl.setTextColor(textColor);
		//a.getDimensionPixelOffset(index, defValue)
		float textsize=a.getDimension(R.styleable.dyspinner_sptextsize,0);
		if(textsize>0){
			text_sl.setTextSize(textsize);
		}
		
		String text=a.getString(R.styleable.dyspinner_sptext);
		if(null!=text){
			text_sl.setText(text);
		}
		
		Drawable img=a.getDrawable(R.styleable.dyspinner_spimg);
		if(null!=img){
			img_sl.setImageDrawable(img);
		}
		
		Drawable background=a.getDrawable(R.styleable.dyspinner_spbackgroud);
		if(null!=background){
			lay_sl.setBackgroundDrawable(background);
		}
		
		int padding_l=	a.getLayoutDimension(R.styleable.dyspinner_sppadding_l, 0);
		if(padding_l>0){
			lay_sl.setPadding(padding_l, lay_sl.getPaddingTop(), lay_sl.getPaddingRight(), lay_sl.getPaddingBottom());
		}
		
		int padding_r=	a.getLayoutDimension(R.styleable.dyspinner_sppadding_r, 0);
		if(padding_r>0){
			lay_sl.setPadding(lay_sl.getPaddingLeft(), lay_sl.getPaddingTop(), padding_r, lay_sl.getPaddingBottom());
		}
		
		isAnim=a.getBoolean(R.styleable.dyspinner_spanim, false);
		
		int angle=a.getInteger(R.styleable.dyspinner_spangle, 0);
		img_sl.setImageBitmap(ImageUtil.retroflexion(mcontext, R.drawable.job_down, angle));
		a.recycle();
		
	}
	
	public void setTextSize(float textsize){
		text_sl.setTextSize(textsize);
	}
	
	boolean isAnim=false;

	private void init(Context context) {
		this.mcontext = context;
		view = LayoutInflater.from(context)
				.inflate(R.layout.dy_spinner, null);
		text_sl=(TextView)view.findViewById(R.id.text_sl);
		img_sl=(ImageView)view.findViewById(R.id.img_sl);
		lay_sl=(RelativeLayout)view.findViewById(R.id.lay_sl);
		
		text_sl.setOnClickListener(this);
		img_sl.setOnClickListener(this);
		lay_sl.setOnClickListener(this);
		view.setOnClickListener(this);
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
			
		this.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
	}
	
	public void SetText(String text){
		this.text_sl.setText(text);
	}
	
	private DySpinnerListener listener;
	
	public void setOnSpinnerClik(DySpinnerListener listener){
		this.listener=listener;
	}
	
	public interface DySpinnerListener{
		public String OnItemClick(int position,int index,int index2);
	}


	private BaseAdapter adapter;
	public void setAdapter(int position,BaseAdapter adapter){
		this.position=position;
		this.adapter=adapter;
	}
	private int position;
	private List<JobListVo> jobList;
	
	/**
	 * 设置数据
	 * @param position item index
	 * @param jobList  工作LIST
	 * @param jobTypelist  工作性质LIST
	 * @param workId   工作性质ID
	 * @param jobId 工作id
	 */
	public void setAdapter(int position,List<JobListVo> jobList,List<JobListVo> jobTypelist,int workId, int jobId){
		this.position=position;
		this.jobList=jobList;
		this.jobTypelist=jobTypelist;
		this.workId=workId;
		this.jobId=jobId;
	}
	
	/**
	 * 设置数据
	 * @param position item index
	 * @param jobList  工作LIST
	 * @param jobTypelist  工作性质LIST
	 * @param workId   工作性质ID
	 * @param jobId 工作id
	 */
	public void setAdapter(int position,List<JobListVo> jobList,List<JobListVo> jobTypelist,String str_workId, String str_jobId){
		this.position=position;
		this.jobList=jobList;
		this.jobTypelist=jobTypelist;
		
		if(!TextUtils.isEmpty(str_workId)&&TextUtils.isDigitsOnly(str_workId)){
			this.workId=Integer.parseInt(str_workId);
		}
		

		if(!TextUtils.isEmpty(str_jobId)&&TextUtils.isDigitsOnly(str_jobId)){
			this.jobId=Integer.parseInt(str_jobId);
		}
	}
	
	int workId=-1, jobId=-1; 
	
	@Override
	public void onClick(View v) {
		showJobDialog(workId+"", jobId+"");
		
		if(isAnim){
			img_sl.setImageBitmap(ImageUtil.retroflexion(mcontext, R.drawable.job_down, 0));
		}
		
	}
	private List<JobListVo> jobTypelist;


	/**
	 * 显示工作岗位设置框
	 * @param workId
	 * @param jobId
	 */
	public void showJobDialog(String workId,String jobId){
		Dialog dialog=new JobDialog(mcontext,workId,jobId,jobTypelist, jobList, new JobDialog.ChooseItem() {
			@Override
			public void getJobChoosePosition(int positon1, int position2) {

				text_sl.setText(listener.OnItemClick(position,positon1,position2));

			}

			@Override
			public void getJobChoosePosition(String name, int positon1,
					int position2) {
				// TODO Auto-generated method stub
				
			}
		}) ;
		
 		dialog.show();
	}
		
	//TODO 此方法可以自动计算，可更好适配
	/**
	 * 显示工作岗位设置框
	 * @param workId
	 * @param jobId
	 */
	public void showJobDialog2(String workId,String jobId){
		Dialog dialog=new JobDialog2(mcontext,workId,jobId,jobTypelist, jobList, new JobDialog2.ChooseItem() {
			@Override
			public void getJobChoosePosition(int positon1, int position2) {

				text_sl.setText(listener.OnItemClick(position,positon1,position2));
			}

			@Override
			public void getJobChoosePosition(String name, int positon1,
					int position2) {
				// TODO Auto-generated method stub
				
			}
		}) ;
		
 		dialog.show();
	}
	
}
