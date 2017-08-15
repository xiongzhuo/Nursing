package com.deya.hospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.supervisor.NewAccomplishTasksActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.vo.dbdata.planListDb;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

public class PersonAdapter extends BaseAdapter {
	// List<PicListVo> list;
	Context mcontext;
	private LayoutInflater inflater;
	LayoutParams para;
	List<planListDb> list;
	NewAccomplishTasksActivity activity;
	DisplayImageOptions optionsSquare;
	float x1=0;
	float x2=0;
	int chooseIndex=0;
	String text="";
	int item_width=0;
	
	/**
	 * Creates a new instance of MyImageListAdapter.
	 * @param jobList 
	 */
	public PersonAdapter(Context context, List<planListDb> list, List<JobListVo> jobList) {
		inflater = LayoutInflater.from(context);
		mcontext = context;
	
		activity=(NewAccomplishTasksActivity) context;
	    int[] wh = AbViewUtil.getDeviceWH(mcontext);
	    item_width=(wh[0] - dp2Px(mcontext, 110)) /3;
	    para = new LayoutParams(item_width ,dp2Px(context, 30));
	   
		this.list = list;
	}
	
	
	public int item_x(){
		return item_width/2;
	}
	
	public PersonAdapter(Context context) {
		
	}


	public void setList(List<planListDb> list){
		this.list=list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
	//	return list == null ? 0 : list.size();
         return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO 自动生成的方法存根
		return 0;
	}

	public void setJobText(int position,String text){
		chooseIndex=position;
		this.text=text;
		notifyDataSetChanged();
	}
	
	public void setJobText(int position){
		chooseIndex=position;
		notifyDataSetChanged();
	}
	
	public void setCheck(int position){
		this.chooseIndex=position;
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder2 mviewHolder;
		if (convertView == null) {
			mviewHolder = new ViewHolder2();
			convertView = inflater.inflate(R.layout.adapter_item_person_job, null);
			mviewHolder.button = (TextView) convertView
					.findViewById(R.id.button);
			mviewHolder.num = (TextView) convertView.findViewById(R.id.num);
			mviewHolder.layout = (LinearLayout) convertView
					.findViewById(R.id.layout);
			mviewHolder.joblayout=(RelativeLayout) convertView.findViewById(R.id.joblayout);
			mviewHolder.tv_job=(TextView) convertView.findViewById(R.id.tv_job);
			mviewHolder.lay_item = (LinearLayout) convertView
					.findViewById(R.id.lay_item);
			
			convertView.setTag(mviewHolder);
		} else {
			mviewHolder = (ViewHolder2) convertView.getTag();
		}
		mviewHolder.layout.setBackgroundResource(R.drawable.round_grey_btn);
		if(AbStrUtil.isEmpty(list.get(position).getPpostName())){
			mviewHolder.tv_job.setText("岗位");
		}else{
		mviewHolder.tv_job.setText(list.get(position).getPpostName());
		}
		
	
		mviewHolder.tv_job.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//activity.showPopWindow(mcontext, mviewHolder.joblayout);
				activity.showJobDialog(position,list.get(position).getPname(),list.get(position).getWork_type(),list.get(position).getPpost());
				
				
			}
		});
		if(list.get(position).isSelect_status()){
		//if(position==chooseIndex){
			mviewHolder.layout.setBackgroundResource(R.drawable.roundbg_btn);
			mviewHolder.layout.setLayoutParams(para);
			mviewHolder.tv_job.setTextColor(mcontext.getResources().getColor(R.color.top_color));
			mviewHolder.joblayout.setBackgroundResource(R.drawable.big_round_blue_type_style);
		
		}else{
			mviewHolder.layout.setBackgroundResource(R.drawable.round_grey_btn);
			mviewHolder.layout.setLayoutParams(para);
			mviewHolder.tv_job.setTextColor(mcontext.getResources().getColor(R.color.edt_broad));
			mviewHolder.joblayout.setBackgroundResource(R.drawable.big_gray_biankuang);
			
		}
//		mviewHolder.tv_job.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				
//				
//				if(event.getAction()== MotionEvent.ACTION_DOWN){
//					x1=event.getX();
//					return true;
//				}else if(event.getAction()== MotionEvent.ACTION_MOVE){
//					return true;
//				}else if(event.getAction()== MotionEvent.ACTION_UP){
//					x2=event.getX();
//					Log.i("1111111", Math.abs(x1-x2)+"");
//					if(Math.abs(x1-x2)<15){
//						activity.showPopWindow(mcontext, mviewHolder.tv_job);
//						return false;
//					}else{
//						return true;
//					}
//				}else{
//					return true;
//				}
//				
//			}
//		});
//
				mviewHolder.lay_item.setTag(R.id.person_item_position, position);
		
		
				mviewHolder.button.setText(list.get(position).getPname());
				
				int num=list.get(position).getSubTasks().size();
				
				mviewHolder.num.setText("("
						+ num+ ")");
				
//		if (position == selection) {
//			mviewHolder.layout.setBackgroundResource(R.drawable.roundbg_btn);
//		} else {
//			mviewHolder.layout.setBackgroundResource(R.drawable.round_grey_btn);
//		}
		return convertView;
	}

	int selection = -1;

	public void serBgColor(int selection) {
		this.selection = selection;
		notifyDataSetChanged();
	}

	class ViewHolder2 {

		TextView button;
		TextView num;
		LinearLayout layout,lay_item;
		RelativeLayout joblayout;
		TextView tv_job;
	}

	public int dp2Px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}
	public PersonAdapter() {
		// TODO Auto-generated constructor stub
	}
	
}
