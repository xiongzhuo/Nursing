package com.im.sdk.dy.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.CommonTopView;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.ui.adapter.GKExpertAdapter;
import com.im.sdk.dy.ui.contact.ECContacts;

public class GKHelperActivity extends BaseActivity  {
	private ListView lv_expert;
	private GKExpertAdapter adapter;
	private ArrayList<ECContacts> data=new ArrayList<ECContacts>();
	private CommonTopView topView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gkexpert);
		
		
		 ArrayList<ECContacts> contactList =ContactSqlManager.getContactsHelper();
//		 if(null!=contactList){
//				for (int i = 0; i < contactList.size(); i++) {
//					if (3==contactList.get(i).getType()) {
//						data.add(contactList.get(i));
//					}
//				}
//		 }
		 if(null!=contactList){
			 data.addAll(contactList);
		 }
		
		 
		
		
		
		lv_expert = (ListView) findViewById(R.id.lv_expert);
		adapter = new GKExpertAdapter(this,data,1);
		lv_expert.setAdapter(adapter);
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
				
				CCPAppManager.startChattingAction(GKHelperActivity.this,
						data.get(position).getContactid(), data.get(position).getRname(),
						data.get(position).getAvatar(), true, data.get(position).getType());
			}
		});

		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);
		topView.setTitle("感控小助手");

	}

	
}
