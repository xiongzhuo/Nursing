package com.deya.hospital.setting;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.login.AgreementActivity;

public class AboutActivity extends BaseActivity implements OnClickListener {

	private TextView agreement;
	private RelativeLayout back_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		
		initView();
	}

	public void initView() {
		back_btn = (RelativeLayout) this.findViewById(R.id.rl_back);
		back_btn.setOnClickListener(this);
		agreement = (TextView) this.findViewById(R.id.agreement);
		agreement.setOnClickListener(this);
		String pkName = this.getPackageName();
		try {
			String versionName = this.getPackageManager().getPackageInfo(
						pkName, 0).versionName;
			TextView tv=(TextView) this.findViewById(R.id.textView1);
			tv.setText(getString(R.string.app_name)+"v"+versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.agreement:
			Intent Intent = new Intent(AboutActivity.this,
					AgreementActivity.class);
			startActivity(Intent);
			break;
		case R.id.rl_back:
			AboutActivity.this.finish();
			break;
		default:
			break;
		}

	}

}
