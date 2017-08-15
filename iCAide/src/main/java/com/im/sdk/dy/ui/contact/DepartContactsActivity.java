package com.im.sdk.dy.ui.contact;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.ToastUtils;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.core.ClientUser;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.storage.DepartContacts.FriendsVo;
import com.im.sdk.dy.ui.adapter.GKExpertAdapter;

public class DepartContactsActivity extends BaseActivity  {
	private ListView lv_expert;
	private GKExpertAdapter adapter;
	private ArrayList<ECContacts> data=new ArrayList<ECContacts>();
	private CommonTopView topView;
	List<FriendsVo> list=new ArrayList<FriendsVo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gkexpert);
		List<FriendsVo> list2=(List<FriendsVo>) getIntent().getSerializableExtra("departfrieds");
		list.addAll(list2);
		
		 ArrayList<ECContacts> contactList =ContactSqlManager.getContacts();
		 
		 
		 if(null!=contactList){
				for (int i = 0; i < contactList.size(); i++) {
					for(FriendsVo fv:list){
						if(fv.getFriend_id().equals(contactList.get(i).getContactid())){
							data.add(contactList.get(i));
						}
					}
					
				}
		 }

		
		
		
		lv_expert = (ListView) findViewById(R.id.lv_expert);
		adapter = new GKExpertAdapter(this,data,2);
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
				
				ClientUser clientUser = CCPAppManager.getClientUser();
				if (clientUser == null
						||clientUser.getUserId().equals(data.get(position).getContactid())) {
					ToastUtils.showToast(mcontext, "不能和本人聊天！");
					return;
				}else{
					CCPAppManager.startChattingAction(DepartContactsActivity.this,
							data.get(position).getContactid(), data.get(position).getRname(),
							data.get(position).getAvatar(), true, data.get(position).getType());
				}
				
				
			}
		});

		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);
		topView.setTitle(getIntent().getStringExtra("departName"));

	}

	
}
