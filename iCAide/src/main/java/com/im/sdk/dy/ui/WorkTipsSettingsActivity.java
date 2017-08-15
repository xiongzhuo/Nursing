package com.im.sdk.dy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.setting.AccountSecurityActivity;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.SimpleSwitchButton;
import com.deya.hospital.util.Tools;

public class WorkTipsSettingsActivity extends BaseActivity implements
		OnClickListener {
	
	boolean dynamic = true;
	Tools tools;
	private CommonTopView topView;
	public static  final  String OPQUESTIONLIST="open_questionlist";
	public static  final  String REGISTERTIPS="open_questionlist";
	SimpleSwitchButton messegeSwitch,messegeSwitch2,messegeSwitch3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.worktip_settings_activity);
		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);
		messegeSwitch= (SimpleSwitchButton) this.findViewById(R.id.messegeSwitch);
		messegeSwitch2= (SimpleSwitchButton) this.findViewById(R.id.messegeSwitch2);
		messegeSwitch3= (SimpleSwitchButton) this.findViewById(R.id.messegeSwitch3);
		messegeSwitch3.setVisibility(View.GONE);
		tools=new Tools(mcontext, Constants.AC);
		messegeSwitch.setCheck((tools.getValue_int(OPQUESTIONLIST)==1)?false:true);
		messegeSwitch.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
			@Override
			public void onCheckChange(boolean ischeck) {

				tools.putValue(OPQUESTIONLIST,ischeck?0:1);
			}
		});
		messegeSwitch.setText("改进列表提醒");

		messegeSwitch2.setCheck((tools.getValue_int(REGISTERTIPS)==1)?false:true);
		messegeSwitch2.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
			@Override
			public void onCheckChange(boolean ischeck) {

				tools.putValue(REGISTERTIPS,ischeck?0:1);
			}
		});
		messegeSwitch2.setText("注册提醒");


		messegeSwitch3.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
			@Override
			public void onCheckChange(boolean ischeck) {

				tools.putValue(REGISTERTIPS,ischeck?0:1);
			}
		});
		messegeSwitch3.setText("督导任务提醒");
		messegeSwitch3.setCheck((tools.getValue_int(REGISTERTIPS)==1)?false:true);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.login_out:
			break;
		case R.id.accountSafety:
			Intent in = new Intent(mcontext, AccountSecurityActivity.class);
			startActivity(in);
			break;
		default:
			break;
		}
	}
	
	


}
