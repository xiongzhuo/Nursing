package com.deya.hospital.widget.popu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.widget.view.MultipleTextViewGroup;
import com.deya.hospital.widget.view.TextViewGroup;

/**
 * 过期
* @author  yung
* @date 创建时间：2016年1月15日 上午11:52:53 
* @version 1.0
 */
public class PopuUnTimeRemark implements OnClickListener  {
	private MyPopu myPopu = null;

	private EditText edt_name,edt_remark;
	private Button btn_cancel, btn_enter;
	private LinearLayout lay1,lay_list;
	private OnPopuClick onPopuClick;
	private MultipleTextViewGroup rl ;
	private Context ctx;
	private ListView listivew;
	private MyAdapter adapter;
	
	public PopuUnTimeRemark(Context ctx,Activity _activity, View view,String name,String text,OnPopuClick onPopuClick){
		myPopu = new MyPopu(ctx, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, R.layout.popwindow_remark2);
		
		this.onPopuClick=onPopuClick;
		this.ctx=ctx;
		btn_cancel=(Button)myPopu.getTextView(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		
		btn_enter=(Button)myPopu.getTextView(R.id.btn_enter);
		btn_enter.setOnClickListener(this);
		
		listivew=(ListView)myPopu.getListView(R.id.listivew);
		
		lay1=(LinearLayout)myPopu.getLinearLayout(R.id.lay1);
		lay1.setOnClickListener(this);
		lay_list=(LinearLayout)myPopu.getLinearLayout(R.id.lay_list);
		edt_name=(EditText)myPopu.getEditText(R.id.edt_name);
		edt_name.setText(name);
//		edt_name.setSelection(edt_name.getText().length());
		
		
		rl = (MultipleTextViewGroup) myPopu.getMultipleTextViewGroup(R.id.main_rl);
//		rl.setOnMultipleTVItemClickListener(this);
		
		
		edt_remark=(EditText)myPopu.getEditText(R.id.edt_remark);
		edt_remark.setText(text);
//		edt_remark.setSelection(edt_remark.getText().length());
		myPopu.setAlpha(200);
		myPopu.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
		myPopu.showSoftInput(_activity);
		
		iniMultipleTextView();
		
		initRemarks();
	}
	
	List<HashMap<String, String>> alllists=new ArrayList<HashMap<String, String>>();
	List<HashMap<String, String>> smalllists=new ArrayList<HashMap<String, String>>();
	
	List<HashMap<String, String>> lists=new ArrayList<HashMap<String, String>>();
	
	private void initRemarks() {
		// TODO Auto-generated method stub
		if(null!=alllists&&alllists.size()>0){
			adapter=new MyAdapter();
			listivew.setAdapter(adapter);
			
			listivew.setOnItemClickListener(itemClickListener);
			
			showsmallList();
			
			lay_list.setVisibility(View.VISIBLE);
			
			
		}else{
			lay_list.setVisibility(View.GONE);
		}
		
	}
	
	OnItemClickListener itemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(position==lists.size()){
				//foot 点击
				if(lists.size()-1==small){
					showallList();
				}else{
					showsmallList();
				}
			}
		}
	};
	
	
	TextView footerText;
	int small=2;
	private void showsmallList(){
		if(null==smalllists||smalllists.size()==0){
			if(null!=alllists&&alllists.size()>0){
				for (int i = 0; i < alllists.size(); i++) {
					if(i==small){
						break;
					}
					smalllists.add(alllists.get(i));
				}
			}
		}
		
		if(alllists.size()>small){
			if(null!=footerText){
				listivew.removeFooterView(footerText);
			}
				
			if(null==footerText)
			footerText=new TextView(ctx);
			
			footerText.setText("加载更多");
			
			listivew.addFooterView(footerText);
		}
		
		lists=smalllists;
		adapter.notifyDataSetChanged();
	}
	
	private void showallList(){
		if(alllists.size()>small){
			if(null!=footerText){
				listivew.removeFooterView(footerText);
			}
				
			if(null==footerText)
				footerText=new TextView(ctx);
			
			footerText.setText("收起");
			
			
			listivew.addFooterView(footerText);
		}
		
		lists=alllists;
		adapter.notifyDataSetChanged();
	}
	
	List<TextViewGroup.TextViewGroupItem> dataList = new ArrayList<TextViewGroup.TextViewGroupItem>();
	List<String> reasons = new ArrayList<String>();
	private void iniMultipleTextView(){
		
		reasons.add("洗手设施太远");
		reasons.add("洗手液容器未一次性使用");
		
//	String[] as=	ctx.getResources().getStringArray(R.array.facilities_investigation_string);
//		TextViewGroupItem item=null;
//		for (int i = 0; i < as.length; i++) {
//			item= new TextViewGroupItem();
//			item.setText(as[i]);
//			item.setStatus(reasons.contains(as[i]));
//			item.setPosition(i);
//			dataList.add(item);
//		}
//		rl.setTextViews(dataList);
	}
	
	private void getRemarkLists(){
		reasons.clear();
//		if(null!=dataList&&dataList.size()>0){
//			for (int i = 0; i <dataList.size(); i++) {
//				if(dataList.get(i).isStatus()){
//					reasons.add(dataList.get(i).getText());
//				}
//			}
//		} 
	}

	private void dismiss(){
			if(null!=myPopu){
				myPopu.dismiss();
				myPopu=null;
			}
			String rm=edt_remark.getText().toString().toString();
			String name=edt_name.getText().toString().toString();
			this.onPopuClick.dismiss(name,rm);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
		case R.id.lay1:
			dismiss();
			this.onPopuClick.cancel();
			break;
		case R.id.btn_enter:
			getRemarkLists();
			String rm=edt_remark.getText().toString().toString();
			String name=edt_name.getText().toString().toString();
			this.onPopuClick.enter(name,rm);
			dismiss();
			break;
		default:
			break;
		}
	}

	public interface OnPopuClick {
		public void enter(String name,String text);
		public void cancel();
		public void dismiss(String name,String text);
	}

//	@Override
//	public void onMultipleTVItemClick(View view, TextViewGroupItem position) {
//		// TODO Auto-generated method stub
//		
//	}
	
	
	private class MyAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		public MyAdapter(){
			mInflater = LayoutInflater.from(ctx);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 if (convertView == null) {
				 holder = new Holder();
					convertView = mInflater.inflate(R.layout.remark_list_item, null);
					holder.lay_remark=(LinearLayout)convertView.findViewById(R.id.lay_remark);
					holder.text_job=(TextView)convertView.findViewById(R.id.text_job);
//					holder.text_pro=(TextView)convertView.findViewById(R.id.text_pro);
//					holder.text_res=(TextView)convertView.findViewById(R.id.text_res);
//					holder.text_reason=(TextView)convertView.findViewById(R.id.text_reason);
					
					convertView.setTag(holder);
			 }else{
				 holder = (Holder) convertView.getTag();
				 clearHolder(holder);
			 }
			 
			 if(position<lists.size()){
				 HashMap<String, String> item=lists.get(position);
				 if(item.containsKey("job"))
					 holder.text_job.setText(item.get("job"));
				 
				 if(item.containsKey("pro"))
					 holder.text_pro.setText(item.get("pro"));
				 
				 if(item.containsKey("res"))
					 holder.text_res.setText(item.get("res"));
				 
				 String textReason="";
				 if(item.containsKey("reason")){
					 textReason=item.get("reason");
				 }
				 if(TextUtils.isEmpty(textReason)){
					 holder.lay_remark.setVisibility(View.GONE);
				 }else{
					 holder.lay_remark.setVisibility(View.VISIBLE);
					 holder.text_reason.setText(textReason);
				 }
			 }
			return convertView;
		}
		
		void clearHolder(Holder holder){
			holder.lay_remark.setVisibility(View.GONE);
			holder.text_job.setText("");
			holder.text_pro.setText("");
			holder.text_res.setText("");
			holder.text_reason.setText("");
		}
		Holder holder=null;
		class Holder {
			LinearLayout lay_remark;
			TextView text_job,text_pro,text_res,text_reason;
		}
	}

}
