package com.deya.hospital.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.vo.DepartmentVos;

import java.util.List;

public class ListDialog2 extends BaseDialog {
	Context context;
	List<DepartmentVos> list;
	ListView listView;
	MyDialogInterface dialogInter;
	BaseAdapter adapter;
	TextView title;
	Button chooseBtn;
	public ListDialog2(Context context, MyDialogInterface dialogInter, BaseAdapter adapter) {
		super(context);
		this.context = context;
		this.adapter=adapter;
		this.dialogInter=dialogInter;
	}
	public void setListAdapter(BaseAdapter adapter){
		listView.setAdapter(adapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_mutichoose);
		listView=(ListView) this.findViewById(R.id.dialog_list);
		title=(TextView) this.findViewById(R.id.title);
		chooseBtn=(Button) this.findViewById(R.id.chooseBtn);
		title.setText("问题分类");
		listView.setAdapter(adapter);
		chooseBtn.setVisibility(View.GONE);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dialogInter.onItemSelect(position);
				dismiss();
			}
		});
	}
	
}
