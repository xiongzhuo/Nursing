package com.im.sdk.dy.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.im.sdk.dy.storage.GroupSqlManager;
import com.im.sdk.dy.ui.ConversationListFragment.GroupItem;
import com.im.sdk.dy.ui.adapter.GKTeamAdapter;
import com.im.sdk.dy.ui.chatting.ChattingActivity;
import com.im.sdk.dy.ui.chatting.ChattingFragment;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.im.sdk.dy.ui.contact.MobileContactSelectActivity;
import com.yuntongxun.ecsdk.im.ECGroup;

public class GKTeamActivity extends BaseActivity {
	private ListView lv_expert;
	private GKTeamAdapter adapter;
	private ArrayList<ECContacts> data = new ArrayList<ECContacts>();
	private CommonTopView topView;
	List<ECGroup> gorups = new ArrayList<ECGroup>();
	List<GroupItem> grouplist = new ArrayList<GroupItem>();
	Gson gson = new Gson();
	GroupItemAdapter itemAdapter;
	Button addGKteam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gkexpert);
		String str = SharedPreferencesUtil.getString(mcontext, "jarrGroup", "");
		itemAdapter = new GroupItemAdapter(mcontext, grouplist);

		gorups.addAll(GroupSqlManager.getJoinGroups());
		lv_expert = (ListView) findViewById(R.id.lv_expert);
//		adapter = new GKTeamAdapter(mcontext, gorups);
		lv_expert.setAdapter(itemAdapter);
		lv_expert.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mcontext, ChattingActivity.class);
				intent.putExtra(ChattingFragment.RECIPIENTS,
						grouplist.get(position).getGroup_id());
				intent.putExtra(ChattingFragment.CONTACT_USER,
						grouplist.get(position).getGroup_name());
				intent.putExtra(ChattingFragment.CUSTOMER_SERVICE, false);
				intent.putExtra(ChattingFragment.CONTACTS_TYPE, 0);
				intent.putExtra(ChattingFragment.IS_MY_DEPART_GROU, false);
				intent.putExtra(ChattingFragment.DEPART_GROU_OWNER,
						grouplist.get(position).getGroup_creator());
				startActivity(intent);
			}
		});

		topView = (CommonTopView) this.findViewById(R.id.topView);
		topView.init(this);
		topView.setTitle("感控团队");
		addGKteam=(Button) this.findViewById(R.id.addGKteam);
		addGKteam.setVisibility(View.VISIBLE);
		addGKteam.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onAddGkTeam();
				
			}
		});
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		getDeFultGroup();
	}

	private void getDeFultGroup() {
		grouplist.clear();
		String str = SharedPreferencesUtil.getString(mcontext, "jarrGroup", "");
		List<GroupItem> list = gson.fromJson(str,
				new TypeToken<List<GroupItem>>() {
				}.getType());
		String str2 = SharedPreferencesUtil.getString(mcontext, "myGroup", "");
		List<GroupItem> list2 = gson.fromJson(str2,
				new TypeToken<List<GroupItem>>() {
				}.getType());
		Log.i("IMtag", str);

//		if (null != list && list.size() > 0) {
//			// mListView.removeAllViews();
//			grouplist.addAll(list);
//			
//		}
		if (null != list2 && list2.size() > 0) {
			for(GroupItem gi:list2){
				ECGroup grop=GroupSqlManager.getECGroup(gi.getGroup_id());
				if(null!=grop){
				gi.setGroup_name(grop.getName());
				gi.setGroup_creator(grop.getOwner());
				grouplist.add(gi);
				}
			}
			
		}
		itemAdapter.notifyDataSetChanged();
	}

	
	public void onAddGkTeam(){
		Intent intent = new Intent(mcontext,
				MobileContactSelectActivity.class);
		intent.putExtra("is_discussion", true);
		intent.putExtra("isFromCreateDiscussion", true);
		intent.putExtra("group_select_need_result", true);
		intent.putExtra("isGkTeam", true);
		startActivity(intent);
		
	}
}
