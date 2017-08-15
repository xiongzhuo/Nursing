package com.deya.hospital.form;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.FormSettingPreviewAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.vo.FormDetailListVo;

public class FormSettingPreViewActivity extends BaseActivity{
	ExpandableListView listView;
	FormSettingPreviewAdapter adapter;
	List<FormDetailListVo> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_preview);
		intTopView();
		getData();
		intiView();
	}
	private void intTopView() {
		TextView titleTv = (TextView) this.findViewById(R.id.title);
		titleTv.setText("预览");
		RelativeLayout rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
		rlBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		TextView  rightView=(TextView) this.findViewById(R.id.submit);

	}
	private void getData() {
		list=(List<FormDetailListVo>) getIntent().getSerializableExtra("list");
		
	}
	private void intiView() {
		listView=(ExpandableListView) this.findViewById(R.id.listView);
		adapter=new FormSettingPreviewAdapter(mcontext, list);
		listView.setAdapter(adapter);
		listView.setGroupIndicator(null);
		for (int i = 0; i < list.size(); i++) {
			listView.expandGroup(i);
		}
		
	}

}
