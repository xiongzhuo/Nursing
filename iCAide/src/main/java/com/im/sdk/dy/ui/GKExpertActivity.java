package com.im.sdk.dy.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.CommonTopView;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.ui.adapter.GKExpertAdapter;
import com.im.sdk.dy.ui.contact.ECContacts;

import java.util.ArrayList;

public class GKExpertActivity extends BaseActivity  {
	private ListView lv_expert;
	private GKExpertAdapter adapter;
	private ArrayList<ECContacts> data=new ArrayList<ECContacts>();
	private CommonTopView topView;
	TextView tips;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gkexpert2);
		
		tips=(TextView) this.findViewById(R.id.tips);
		tips.setText("排名不分先后");
		 ArrayList<ECContacts> contactList =ContactSqlManager.getContactsExpert();
		 if(null!=contactList){
				for (int i = 0; i < contactList.size(); i++) {
					if (2==contactList.get(i).getType()) {
						data.add(contactList.get(i));
					}
				}
		 }

		
		
		
		lv_expert = (ListView) findViewById(R.id.lv_expert);
		adapter = new GKExpertAdapter(this,data,1);
		lv_expert.setAdapter(adapter);
		findViewById(R.id.networkView).setVisibility(View.GONE);
		lv_expert.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				// 如果是点对点聊天
//				Intent intent = new Intent(GKExpertActivity.this,
//						ContactDetailActivity.class);
//				intent.putExtra(ContactDetailActivity.RAW_ID, data.get(position).getId());
//				startActivity(intent);
				
				CCPAppManager.startChattingAction(GKExpertActivity.this,
						data.get(position).getContactid(), data.get(position).getRname(),
						data.get(position).getAvatar(), true, data.get(position).getType());
			}
		});

		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);

	}

	
}
