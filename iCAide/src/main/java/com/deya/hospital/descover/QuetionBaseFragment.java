package com.deya.hospital.descover;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.vo.QuestionVo;

public abstract class QuetionBaseFragment extends BaseFragment {
	MyBrodcastReceiver reciReceiver;
	IntentFilter intentFilter;
	public static final String ADD_QUESTION="add_question";
	public static final String UPDATE_QUESTION="update_question";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		reciReceiver = new MyBrodcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				onaddListData((QuestionVo) intent
						.getSerializableExtra("data"));
				if (intent.getAction().equals(ADD_QUESTION)) {
		
				} else {
					onaddupdate();
				}

			}
		};
		
		intentFilter = new IntentFilter();
		intentFilter.addAction(QuestionUpdateBrodcast.ADD_QUESTION);
		intentFilter.addAction(QuestionUpdateBrodcast.UPDATE_QUESTION);
		
	}

	public abstract void onaddListData(QuestionVo vo);

	public abstract void onaddupdate();

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(reciReceiver);
		super.onDestroy();
	}
}
