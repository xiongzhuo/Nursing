package com.deya.hospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;

public class FiveMethodsListAdapter extends BaseAdapter {
	private String[] strItem = { "接触患者前", "无菌操作前", "接触血液体液后", "接触患者后",
			"接触患者周边环境后" };
	
	
//	private String[] strItem = { "接触患者前", "接触患者后", "无菌操作前", "接触血液体液后",
//	"接触患者周边环境后" };
	private int[] imgItem = {R.drawable.times_before,  R.drawable.times_aseptic,R.drawable.time_getblood,
			R.drawable.times_after,R.drawable.times_getsuround };
	private int[] imgItem2 = {R.drawable.times_before_down, R.drawable.times_aseptic_down,R.drawable.time_getblood_down,
			R.drawable.times_after_down, R.drawable.times_getsuround_down };
	private LayoutInflater inflater;
	Context context;
	public  int selcection = -1;
	boolean isCheck []={false,false,false,false,false};

	public FiveMethodsListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return strItem.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setIsCheck(int selcection,boolean ischeck) {
		this.selcection = selcection;
		if(selcection==3){
			if(isCheck[4]){
//				ToastUtils.showToast(mcontext, "接触患者后和接触患者周边环境后不能同时选择");
//				return;
				isCheck[4]=false;
			}
		}
		if(selcection==4){
			if(isCheck[3]){
//				ToastUtils.showToast(mcontext, "接触患者后和接触患者周边环境后不能同时选择");
//				return;
				isCheck[3]=false;
			}
		}
	isCheck[selcection]=ischeck;
		notifyDataSetChanged();

	}
	public void reset() {
		this.selcection = -1;
		
		for(int i=0;i<isCheck.length;i++){
			isCheck[i]=false;
		}
		notifyDataSetChanged();

	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder;
		if(null==convertView){
	    viewholder=new ViewHolder();
		convertView = inflater.inflate(R.layout.fivelist_type_item, null);
		viewholder.listtext = (TextView) convertView.findViewById(R.id.listtext);
		viewholder.rightLine=convertView.findViewById(R.id.rightLine);
		viewholder.img=(ImageView) convertView.findViewById(R.id.img);
		convertView.setTag(viewholder);
		}else{
			viewholder=(ViewHolder) convertView.getTag();
		}
		if (isCheck[position]) {
			convertView.setBackgroundResource(R.color.blue_);
			viewholder.img.setImageResource(imgItem2[position]);
			viewholder.listtext.setTextColor(context.getResources().getColor(R.color.white));
			viewholder.rightLine.setVisibility(View.GONE);
			
		} else {
			convertView.setBackgroundResource(R.color.white);
			viewholder.img.setImageResource(imgItem[position]);
			viewholder.listtext.setTextColor(context.getResources().getColor(R.color.list_title));
			viewholder.rightLine.setVisibility(View.VISIBLE);
		}
		viewholder.listtext.setText(strItem[position]);
		
		return convertView;
	}
	public int dp2Px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}
	public class ViewHolder {
		public TextView listtext;
		public View rightLine;
		public ImageView img;
		
	}
}
