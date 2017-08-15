package com.deya.hospital.shop;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.RuleVo;

public class VipTipsActivity extends BaseActivity implements OnClickListener{
	protected static final int GET_SUCESS = 0x5008;
	protected static final int GET_FAILE = 0x5009;
	List<RuleVo> scoreList=new ArrayList<RuleVo>();
	private RelativeLayout rl_back;
	Tools tools;
	TextView tellTv;
	TextView webTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viprerule);
		tools=new Tools(mcontext, Constants.AC);
		initView();
		
		
		
	}
	private void initView() {
		rl_back = (RelativeLayout) this.findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		((TextView)this.findViewById(R.id.title)).setText("关于我们");
		webTv=(TextView) this.findViewById(R.id.webTv);
		tellTv=(TextView) this.findViewById(R.id.tellTv);
		
		webTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
		webTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				webMethod();
				
			}
		});
		tellTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
		tellTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse("tel:"
									+tellTv.getText().toString()));
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(mcontext, "该设备不支持通话功能",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		TextView tx=(TextView) this.findViewById(R.id.text_top);
		String  vipCode=tools.getValue(Constants.IS_VIP_HOSPITAL);
		if(vipCode.equals("0")){
			tx.setText("");
		}
	}
	private void webMethod() {  
         Intent intent= new Intent();          
         intent.setAction("android.intent.action.VIEW");      
         Uri content_url = Uri.parse("http://www.gkgzj.com");     
         intent.setData(content_url);    
         startActivity(intent);  
}  
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;

		default:
			break;
		}
	}
}