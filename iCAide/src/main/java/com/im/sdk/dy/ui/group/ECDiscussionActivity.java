package com.im.sdk.dy.ui.group;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.deya.acaide.R;
import com.im.sdk.dy.ui.ECSuperActivity;
import com.im.sdk.dy.ui.GroupListFragment;

public class ECDiscussionActivity extends ECSuperActivity implements OnClickListener {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getTopBarView().setTopBarToStatus(1, R.drawable.btn_back_style, -1, "讨论组列表", this);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.im_discussion_activity;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
        case R.id.btn_left:
		case R.id.btn_text_left:
            hideSoftKeyboard();
            finish();
            GroupListFragment.sync=false;
            break;
        
        default:
            break;
    }
		
	}

	

}
