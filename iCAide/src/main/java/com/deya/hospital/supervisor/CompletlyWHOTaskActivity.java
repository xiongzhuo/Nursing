package com.deya.hospital.supervisor;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;

public class CompletlyWHOTaskActivity extends BaseActivity implements View.OnClickListener{
	private TextView webTv;
	private TextView tellTv,text_top;
	private RelativeLayout rl_back;
	Tools tools;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tools=new Tools(mcontext, Constants.AC);
		setContentView(R.layout.activity_completly_task_notvip);
		rl_back = (RelativeLayout) this.findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		
		webTv=(TextView) this.findViewById(R.id.webTv);
		tellTv=(TextView) this.findViewById(R.id.tellTv);
		text_top=(TextView) this.findViewById(R.id.text_top);
		String type=getIntent().getStringExtra("type");
		if(type.equals("3")){
			((TextView)this.findViewById(R.id.title)).setText("更多");
			}
		if(tools.getValue(Constants.IS_VIP_HOSPITAL).equals("1")){
			if(type.equals("3")){
			text_top.setText("定制版只有签约医院才可显示哦\n快签约我们发现惊喜吧!");
			((TextView)this.findViewById(R.id.title)).setText("WHO手卫生观察(定制版)");
			}else if(type.equals("2")){
				text_top.setText("签约医院可显示\n请联系客服，或登录官网了解详情。");
				((TextView)this.findViewById(R.id.title)).setText("本院资料库");
			}
		}else if(type.equals("1")){
			text_top.setText("暂无文章信息，如需添加文章\n请联系客服，或登录官网了解详情。");
			((TextView)this.findViewById(R.id.title)).setText("本院资料库");
		}
		
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
