package com.deya.hospital.form.xy;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.FormDetailVo;
import com.deya.hospital.vo.Subrules;

public class XiangyaFormSettingAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<Subrules> list;
	TextContral contral;
	SwipeMenuListView listview;
	boolean isUpdated = false;
	Context mcContext;
	boolean editorbal;
	int chooseItem=-1;
	public XiangyaFormSettingAdapter(Context context, boolean isUpdated,boolean editorbal,
			List<Subrules> list, TextContral contral,
			SwipeMenuListView listview) {
		inflater = LayoutInflater.from(context);
		this.contral = contral;
		this.isUpdated = isUpdated;
		this.mcContext = context;
		this.list = list;
		this.editorbal=editorbal;
		this.listview = listview;
	}


	public List<Subrules> getList() {
		return this.list;
	}

	public void setChooseItem(int item){
	//	this.chooseItem=item;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_form_xiangya_editor,
					null);
			viewHolder.judeBtn = (Button) convertView
					.findViewById(R.id.judeBtn);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.optionTv = (TextView) convertView
					.findViewById(R.id.optionTv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Subrules sbl=list.get(position);
		viewHolder.title .setText(sbl.getName()+"("+AbStrUtil.reMoveUnUseNumber(sbl.getScore()+"")+"分)");
		viewHolder.optionTv.setText(sbl.getChoose());
		viewHolder.judeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(list.get(position).isChoosed()){
					return;
				}
				if(!isUpdated){
				contral.setPageChooseItem(position,true);
				setStatus(position, 1);
				
				notifyDataSetChanged();
			} 
				}
		});
		
		if(!sbl.isChoosed()){
			viewHolder.judeBtn.setBackgroundResource(R.drawable.choose_box_normal);
		}else{
			viewHolder.judeBtn.setBackgroundResource(R.drawable.choose_box_select);
		}
		if (list.get(position).getOpenState() == 1) {
			
			listview.smoothOpenMenu(position);
		} else {
			View view = parent.getChildAt(position);
			if (view instanceof SwipeMenuLayout) {
				SwipeMenuLayout mTouchView = (SwipeMenuLayout) view;
				mTouchView.smoothCloseMenu();
			}
		}
		return convertView;
	}

	public class ViewHolder {
		Button subtractionBtn, judeBtn;
		TextView numTv, title;
		TextView optionTv;

	}

	public void setStatus(int positon, int status) {
		for (int i = 0; i < list.size(); i++) {
			if (positon == i) {
				list.get(i).setOpenState(status);
			}else{
				list.get(i).setOpenState(2);
			}
		}
	}
	
	public interface TextContral {
		public void setText(List<FormDetailVo> list);
		public void setPageChooseItem(int position,boolean choosed);
	}
}
