package com.deya.hospital.descover;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.KindsAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.vo.KindsVo;

public class KindsActivity extends BaseActivity {
	ListView listView;
	KindsAdapter kAdapter;
	List<KindsVo> kindslist = new ArrayList<KindsVo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kindslist);
		initView();
	}

		
	private String types;
	private String typeId;
	private TextView searchlTv;
	private void initView() {
		RelativeLayout rl_back=(RelativeLayout) this.findViewById(R.id.rl_back);
		rl_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		searchlTv=(TextView) this.findViewById(R.id.et_search);
		searchlTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it=new Intent(mcontext, SearchActivity.class);
				startActivity(it);
				
			}
		});
		listView = (ListView) this.findViewById(R.id.kindsListView);
		kindslist = (List<KindsVo>) getIntent().getSerializableExtra(
				"kindslist");
		if(kindslist.size()<=0){
			listView.setVisibility(View.GONE);
		}
		kAdapter = new KindsAdapter(mcontext, kindslist);
		listView.setAdapter(kAdapter);
		types = getIntent().getStringExtra("types");
		typeId = getIntent().getStringExtra("id");
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(mcontext, DoucmentListActivity.class);
				it.putExtra("types", types);
				it.putExtra("id", typeId);
				it.putExtra("kinds", kindslist.get(position).getKind());
				startActivity(it);

			}
		});

	}

}
