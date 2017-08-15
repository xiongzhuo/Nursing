package com.deya.hospital.message;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;

public class MessageListActivity extends BaseActivity {

	private ImageView userhead;
	private TextView chatcontent;
	private TextView sendtime;
	private String time;
	private String content;
	private RelativeLayout back_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatting_item_msg_text_left);
		time = getIntent().getStringExtra("time");
		content = getIntent().getStringExtra("content");
		initView();
	}

	public void initView() {
		userhead = (ImageView) this.findViewById(R.id.iv_userhead);
		chatcontent = (TextView) this.findViewById(R.id.tv_chatcontent);
		sendtime = (TextView) this.findViewById(R.id.tv_sendtime);
		back_img = (RelativeLayout) this.findViewById(R.id.rl_back);
		back_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MessageListActivity.this.finish();
			}
		});

		chatcontent.setText(content);
		sendtime.setText(time);
	}

}
