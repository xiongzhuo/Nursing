package com.deya.hospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.WrongRuleVo;
import com.deya.hospital.workspace.handwash.HandWashTasksActivity;

import java.util.List;

public class MethodsListAdapter extends BaseAdapter {
	private String[] strItem = { "洗手", "卫生手消毒","未采取措施", "戴手套" };
	private int[] imgItem = { R.drawable.fluwater, R.drawable.fastwash,
			R.drawable.handson, R.drawable.no_wash };
	// private int imgArray={};
	private LayoutInflater inflater;
	Context context;
	public int selcection = -1;
	public int layoutPosition = -1;//判断是选中的左边 还是右边
	int methodType = -1;
	boolean cancheck=false;
	boolean canContinue=false;
	boolean needCheck=true;
	private HandWashTasksActivity activity;
	List<WrongRuleVo> ruleList;

	public MethodsListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		activity=(HandWashTasksActivity) context;
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

	public void setIsCheck(int selcection) {
		this.selcection = selcection;
		notifyDataSetChanged();

	}
	public void canCheck(boolean cancheck) {
		this.cancheck = cancheck;
		if(cancheck){
			notifyDataSetChanged();
		}else{
			ResetAdapter();
		}

	}
	public void needCheck(boolean cancheck) {
		this.cancheck = cancheck;
		needCheck=false;
	}
	public boolean getCanCheck(){
		return  cancheck;
	}
	public boolean getNeedCheck(){
		return  needCheck;
	}
	public void ResetAdapter(){
		selcection = -1;
		layoutPosition = -1;
		methodType = -1;
		cancheck=false;
		notifyDataSetChanged();
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(null==convertView){
			viewHolder=new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_handwash_method, null);
			viewHolder.listtext = (TextView) convertView
					.findViewById(R.id.method_text);
			viewHolder.errortext = (TextView) convertView
					.findViewById(R.id.errortext);
			viewHolder.layout = (LinearLayout) convertView
					.findViewById(R.id.layout);
			viewHolder.mehodLayout = (LinearLayout) convertView
					.findViewById(R.id.mehod_layout);
			viewHolder.errorLayout = (LinearLayout) convertView
					.findViewById(R.id.error_layout);
			viewHolder.line=convertView.findViewById(R.id.line);
			viewHolder.img1 = (ImageView) convertView.findViewById(R.id.img_dian1);
			viewHolder.img2 = (ImageView) convertView.findViewById(R.id.img_dian2);
			convertView.setTag(viewHolder);

		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}


		if (position == selcection) {

			if (layoutPosition == 0) {
				viewHolder.img1.setImageResource(R.drawable.right_slect);
				viewHolder.img2.setImageResource(R.drawable.wrong_normal);
			} else {
				if(position==3||position==2){
					viewHolder.img1.setImageResource(R.drawable.undo_normal);
					viewHolder.img2.setImageResource(R.drawable.undo_select);
				}else{
					viewHolder.img1.setImageResource(R.drawable.right_normal);
					viewHolder.img2.setImageResource(R.drawable.wrong_select);
				}
			}
		}else{
			viewHolder.img1.setImageResource(R.drawable.right_normal);

			if(position==3||position==2){
				viewHolder.img2.setImageResource(R.drawable.undo_normal);
			}else{
				viewHolder.img2.setImageResource(R.drawable.wrong_normal);
			}

		}
		if(cancheck){
			viewHolder.listtext.setTextColor(context.getResources().getColor(R.color.list_title));
			viewHolder.line.setBackgroundResource(R.color.check_corlor);
		}else{
			viewHolder.listtext.setTextColor(context.getResources().getColor(R.color.un_check_corlor));
			viewHolder.line.setBackgroundResource(R.color.devider);
		}

		if (position == 3||position == 2||(activity.getFiveCheckPos(2)&&position==1)) {
			viewHolder.mehodLayout.setVisibility(View.INVISIBLE);
			viewHolder.mehodLayout.setEnabled(false);
		} else {
			viewHolder.mehodLayout.setVisibility(View.VISIBLE);
			viewHolder.mehodLayout.setEnabled(true);
		}
		viewHolder.mehodLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!(activity.getFiveCheckPos(2)&&position==1)){//解除患者血液体液后不能选外科手消毒
				if(cancheck){
					layoutPosition = 0;
					selcection = position;
					if(position!=3&&position!=2){
						activity.setContinueBtn(true);
					}
					notifyDataSetChanged();

				}else{

					if(needCheck){
						ToastUtils.showToast(context, "请先选择指征");
					}
				}

			}
			}
		});
		viewHolder.errorLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(cancheck){
					layoutPosition = 1;
					selcection = position;
					if(position!=3&&position!=2){
						activity.showChooseDialog(position);
					}
					activity.setContinueBtn(true);
					notifyDataSetChanged();
				}else{
					if(needCheck){
						ToastUtils.showToast(context, "请先选择指征");
					}
				}


			}
		});
		viewHolder.listtext.setText(strItem[position]);
		return convertView;
	}

	public int getMethodType() {
		switch (selcection) {
			case 0:
				if (layoutPosition == 0) {
					methodType = 1;
				} else {
					methodType = 4;
				}

				break;
			case 1:
				if (layoutPosition == 0) {
					methodType = 2;
				} else {
					methodType = 5;
				}
				break;
			case 2:
				if (layoutPosition == 0) {
					methodType = -1;
				} else {
					methodType = 0;
				}
				break;
			case 3:
				if (layoutPosition == 0) {
					methodType = 3;
				} else {
					methodType = 3;
				}
				break;
			default:
				break;
		}

		return methodType;

	}

	public class ViewHolder{
		public TextView listtext ;
		public  TextView errortext;
		public  LinearLayout layout;
		public  LinearLayout mehodLayout;
		public LinearLayout errorLayout;
		public View line;
		public ImageView img1;
		public ImageView img2;

	}


}
