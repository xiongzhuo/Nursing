package com.deya.hospital.form;

import java.util.List;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.FormDetailVo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PreviewItemAdapter extends BaseAdapter{
	LayoutInflater inflater;
	List<FormDetailVo> list;
	ItemInter inter;
	int type;
	Context context;
	int parentPosition;
	public PreviewItemAdapter(Context context,List<FormDetailVo> list,int type,int parentPosition,ItemInter inter) {
		this.list=list;
		inflater=LayoutInflater.from(context);
		this.type=type;
		this.context=context;
		this.inter=inter;
		this.parentPosition=parentPosition;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder;
		if(convertView==null){
			viewholder=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_item_prviewlist, null);
			viewholder.titleTv=(TextView) convertView.findViewById(R.id.titleTv);
			viewholder.remarkTv=(TextView) convertView.findViewById(R.id.normalScoreTv);
			viewholder.judeImg=(ImageView) convertView.findViewById(R.id.judeImg);
			convertView.setTag(viewholder);
			
		}else{
			viewholder=(ViewHolder) convertView.getTag();
		}
		viewholder.titleTv.setText(position+1+"、"+list.get(position).getDescribes());
		if(type==1){
			if(list.get(position).getBase_score()!=list.get(position).getScores()){
				String text1=position+1+"、"+list.get(position).getDescribes();
				viewholder.titleTv.setText(text1+AbStrUtil.reMoveUnUseNumber("(标准分："+list.get(position).getBase_score()+"")+",得分："+AbStrUtil.reMoveUnUseNumber(list.get(position).getScores()+"")+")");
				viewholder.remarkTv.setVisibility(View.VISIBLE);
				
				if(!AbStrUtil.isEmpty(list.get(position).getRemark())){
					viewholder.remarkTv.setText("已备注");
					viewholder.remarkTv.setBackgroundResource(R.drawable.big_green_biankuang02);
					viewholder.remarkTv.setTextColor( context.getResources().getColor(R.color.light_green));
					AbStrUtil.setPiceTextCorlor(context.getResources().getColor(R.color.light_green), viewholder.titleTv, viewholder.titleTv.getText().toString(), viewholder.titleTv.getText().toString().length(), text1.length());
				}else{
					viewholder.remarkTv.setBackgroundResource(R.drawable.big_yellow_biankuang);
					viewholder.remarkTv.setTextColor( context.getResources().getColor(R.color.light_yellow));
					viewholder.remarkTv.setText("加备注");
					AbStrUtil.setPiceTextCorlor(context.getResources().getColor(R.color.light_yellow), viewholder.titleTv, viewholder.titleTv.getText().toString(), viewholder.titleTv.getText().toString().length(), text1.length());
				}
				
			}else{
				viewholder.titleTv.setTextColor(context.getResources().getColor(R.color.black));
				viewholder.titleTv.setText(position+1+"、"+list.get(position).getDescribes());
				if(!AbStrUtil.isEmpty(list.get(position).getRemark())){
					viewholder.remarkTv.setText("已备注");
					viewholder.remarkTv.setBackgroundResource(R.drawable.big_green_biankuang02);
					viewholder.remarkTv.setTextColor( context.getResources().getColor(R.color.light_green));
					viewholder.remarkTv.setVisibility(View.VISIBLE);
				}else{
					viewholder.remarkTv.setVisibility(View.GONE);
				}
			}
			
			
			
		}else if(type==2||type==4){
			if(list.get(position).getResult()==2){
				if(type==4){
					viewholder.judeImg.setVisibility(View.VISIBLE);
					viewholder.judeImg.setBackgroundResource(R.drawable.review_wrong_img);
				}else{
					viewholder.judeImg.setVisibility(View.GONE);
				}
				viewholder.titleTv.setTextColor(context.getResources().getColor(R.color.black));
				if(!AbStrUtil.isEmpty(list.get(position).getRemark())){
					viewholder.remarkTv.setText("已备注");
					viewholder.remarkTv.setVisibility(View.VISIBLE);
					viewholder.remarkTv.setBackgroundResource(R.drawable.big_green_biankuang02);
					viewholder.remarkTv.setTextColor( context.getResources().getColor(R.color.light_green));
				}else{
					viewholder.remarkTv.setVisibility(View.VISIBLE);
					viewholder.remarkTv.setBackgroundResource(R.drawable.big_yellow_biankuang);
					viewholder.remarkTv.setTextColor( context.getResources().getColor(R.color.light_yellow));
					viewholder.remarkTv.setText("加备注");
				}
			}else{
					viewholder.judeImg.setVisibility(View.GONE);
				viewholder.titleTv.setTextColor(context.getResources().getColor(R.color.black));
				if(!AbStrUtil.isEmpty(list.get(position).getRemark())){
					viewholder.remarkTv.setText("已备注");
					viewholder.remarkTv.setVisibility(View.VISIBLE);
					viewholder.remarkTv.setBackgroundResource(R.drawable.big_green_biankuang02);
					viewholder.remarkTv.setTextColor( context.getResources().getColor(R.color.light_green));
				}else{
					viewholder.remarkTv.setVisibility(View.GONE);
				}
			}
		}else{
			viewholder.titleTv.setTextColor(context.getResources().getColor(R.color.black));
			viewholder.remarkTv.setVisibility(View.GONE);
		}
//		if(list.get(position).isRemark()){
//			viewholder.remarkTv.setText("已备注");
//		}else{
//			viewholder.remarkTv.setText("加备注");
//		}
		
		
		viewholder.remarkTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inter.onRemark(parentPosition, position);
			}
		});
		return convertView;
	}
	
	public class ViewHolder{
	TextView titleTv ;
	TextView remarkTv ;
	TextView nowScoreTv;
	ImageView judeImg;
	}
	
	interface ItemInter{
		public void onRemark(int parentPosition,int position);
	}

}
