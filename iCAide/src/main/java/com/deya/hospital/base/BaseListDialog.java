package com.deya.hospital.base;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;

import java.util.List;

public abstract class BaseListDialog<T> extends BaseDialog {
	Context mcontext;
	private Tools tools = null;
	public TextView titleTv,right_txt,left_txt;
	ListView listView;
	public List<T> list;
	LayoutInflater layoutInflater;
	ListDialogAadpter adpter;
	AdapterView.OnItemClickListener listener;
	LinearLayout topView;

	public BaseListDialog(Context context, List<T> list,
						  AdapterView.OnItemClickListener listener) {
		super(context);
		this.mcontext = context;
		this.listener = listener;
		this.list=list;
		layoutInflater=	LayoutInflater.from(context);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_common_list_pop);
		tools = new Tools(mcontext, Constants.AC);
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
		lp.width = LayoutParams.MATCH_PARENT;
		int wh[]= AbViewUtil.getDeviceWH(mcontext);
		lp.height = wh[1] / 100 * 60;
		lp.gravity = Gravity.BOTTOM;
		dialogWindow.setAttributes(lp);
		intView();


	}

	private void intView() {
		topView= (LinearLayout) findViewById(R.id.topView);
		titleTv= (TextView) this.findViewById(R.id.titleTv);
		right_txt= (TextView) this.findViewById(R.id.right_txt);
		left_txt= (TextView) this.findViewById(R.id.left_txt);
		listView= (ListView) this.findViewById(R.id.listView);
		adpter=new ListDialogAadpter();
		listView.setAdapter(adpter);
		listView.setOnItemClickListener(listener);
		intUi();
	}

	protected abstract void intUi();


	public class ListDialogAadpter extends BaseAdapter{

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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(null==convertView){
				viewHolder=new ViewHolder();
				convertView=layoutInflater.inflate(R.layout.fivelist_item,null);
				viewHolder.listtext= (TextView) convertView.findViewById(R.id.listtext);
				viewHolder.tiptxt=(TextView) convertView.findViewById(R.id.tiptxt);
				viewHolder.contentLay= (LinearLayout) convertView.findViewById(R.id.contentLay);
				convertView.setTag(viewHolder);

			}else{
				viewHolder= (ViewHolder) convertView.getTag();
			}
			setListDta(viewHolder,position);
			return convertView;
		}
	}

	public  abstract  void setListDta(ViewHolder viewHolder,int position);
	public class ViewHolder{
		public TextView listtext,numsTv;
		public ImageView chekImg;
		public ImageView img;
		public TextView tiptxt;//括号里面的提示文字
		public LinearLayout contentLay;

	}

	public void showBottom(){
		super.show();
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
		lp.width = LayoutParams.MATCH_PARENT;
		int wh[]= AbViewUtil.getDeviceWH(mcontext);
		lp.height = wh[1] / 100 * 40;
		lp.gravity = Gravity.BOTTOM;
		dialogWindow.setAttributes(lp);
	}
	public void showBottomHightAjust(){
		super.show();
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
		lp.width = LayoutParams.MATCH_PARENT;
		int wh[]= AbViewUtil.getDeviceWH(mcontext);
		lp.height =  LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
		dialogWindow.setAttributes(lp);

		topView.setBackgroundResource(R.drawable.top_round);
	}
	public void showCenter(){
		super.show();
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
		lp.width = LayoutParams.WRAP_CONTENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);
	}

	public void refesh(){
		if(null!=adpter){
		adpter.notifyDataSetChanged();}
	}
}
