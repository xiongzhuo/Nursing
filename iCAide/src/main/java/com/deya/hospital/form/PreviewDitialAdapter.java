package com.deya.hospital.form;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.form.PreviewUplodedItemAdapter.ItemAdapterInter;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.FormDetailListVo;

public class PreviewDitialAdapter extends BaseAdapter{
	LayoutInflater inflate;
	List<FormDetailListVo> list;
	AdpterInter inter;
	int type;
	Context context;
	public PreviewDitialAdapter(Context context,List<FormDetailListVo> list, int type,AdpterInter inter)  {
		inflate=LayoutInflater.from(context);
		this.list=list;
		this.context=context;
		this.type=type;
		this.inter=inter;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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

	public void setdata(List<FormDetailListVo> list){
		this.list=list;
		notifyDataSetChanged();
		
	}
	@Override
	public View getView( final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(null==convertView){
			viewHolder=new ViewHolder();
			convertView=inflate.inflate(R.layout.item_prviewlist, null);
			viewHolder.itemListView=(ListView) convertView.findViewById(R.id.itemList);
			viewHolder.titleTv=(TextView) convertView.findViewById(R.id.title);
			viewHolder.layout=(LinearLayout) convertView.findViewById(R.id.layout);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}		
		if(position%2==0){
			viewHolder.layout.setBackgroundResource(R.color.white);
		}else{
			viewHolder.layout.setBackgroundResource(R.color.list_item_corlor);
		}
		viewHolder.titleTv.setText(list.get(position).getName());
		viewHolder.titleTv.setBackgroundResource(R.color.white);
		viewHolder.titleTv.setTextColor(context.getResources().getColor(R.color.black));
		PreviewUplodedItemAdapter itemAdapter=new PreviewUplodedItemAdapter(context, list.get(position).getSub_items(),type,position,new ItemAdapterInter() {
			
			@Override
			public void onToRemark(int parentPosition, int position) {
			inter.onToRemark(parentPosition, position);
			}
		});
		viewHolder.itemListView.setAdapter(itemAdapter);
		
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				inter.onPutData(position);
				Log.i("111111111111", "position="+position);
			}
		});
		
		viewHolder.itemListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position1, long id) {
				inter.onPutData(position);
				Log.i("111111111111", "position="+position);
				
			}
		});
		
		return convertView;
	}

	public class ViewHolder{
		ListView itemListView;
		TextView titleTv;
		LinearLayout layout;
		
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public String getVerticalText(String str) {// 将文字竖向排列
		String title = "";
		for (int i = 0; i < str.length(); i++) {
			char item = str.charAt(i);
			if (48 <= item && item <= 57) {
				title += item;//将相连的数字组合到一起
			} else {
				if (!AbStrUtil.isEmpty(item + "")) {
					title += item + "\n";
				}
			}

		}
		return title.trim();
	}
	public interface AdpterInter{
		public void  onPutData(int position);
		public void  onToRemark(int position,int itemPosition);
	}
}
