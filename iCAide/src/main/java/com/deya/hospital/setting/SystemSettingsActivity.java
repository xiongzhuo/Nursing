package com.deya.hospital.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.login.LoginActivity;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.SimpleSwitchButton;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.DepartmentVo;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.lidroid.xutils.exception.DbException;
import com.yuntongxun.ecsdk.ECDevice;

public class SystemSettingsActivity extends BaseActivity implements
		OnClickListener {
	
	boolean dynamic = true;
	Tools tools;
	private CommonTopView topView;
	SimpleSwitchButton messegeSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_settings_activity);
		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);
		findViewById(R.id.accountSafety).setOnClickListener(this);
		findViewById(R.id.login_out).setOnClickListener(this);
		messegeSwitch= (SimpleSwitchButton) this.findViewById(R.id.messegeSwitch);
		dialog = new MyDialog(mcontext, R.style.SelectDialog);
		tools=new Tools(mcontext, Constants.AC);
		messegeSwitch.setCheck((tools.getValue_int(Constants.ONLY_WIFI_UPLOAD)==1)?true:false);
		messegeSwitch.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
			@Override
			public void onCheckChange(boolean ischeck) {

				tools.putValue(Constants.ONLY_WIFI_UPLOAD,ischeck?1:0);
			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.login_out:
			loginOut();
			break;
		case R.id.accountSafety:
			Intent in = new Intent(mcontext, AccountSecurityActivity.class);
			startActivity(in);
			break;
		default:
			break;
		}
	}
	
	
	public void loginOut() {
		dialog.show();
	}

	
	MyDialog dialog;
	private RelativeLayout pushstting;
	private RelativeLayout above;
	private TextView jobs;
	private TextView count;

	public class MyDialog extends Dialog {

		private TextView showBtn;
		private TextView deletBtn;
		private TextView cancleBtn;

		/**
		 * Creates a new instance of MyDialog.
		 */
		public MyDialog(Context context, int theme) {
			super(context, theme);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO 自动生成的方法存根
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);

			setContentView(R.layout.dialog_delet);

			showBtn = (TextView) this.findViewById(R.id.show);
			showBtn.setText("退出登录？");
			deletBtn = (TextView) this.findViewById(R.id.yes);
			deletBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					tools.putValue(Constants.AUTHENT, "");
					tools.putValue(Constants.NAME, "");
					tools.putValue(Constants.HOSPITAL_NAME, "");
					tools.putValue(Constants.AGE, "");
					tools.putValue(Constants.SEX, "");
					tools.putValue(Constants.STATE, "");
					tools.putValue(Constants.MOBILE, "");
					tools.putValue(Constants.HEAD_PIC, "");
					tools.putValue(Constants.EMAIL, "");
					tools.putValue(Constants.JOB, "");
					tools.putValue(Constants.USER_ID, "");
					tools.putValue(Constants.IS_VIP_HOSPITAL, "");
					SharedPreferencesUtil.saveString(mcontext, "discoverData",
							"");
					SharedPreferencesUtil.saveString(mcontext, "chat_editor_unsend_text1", "");
					try {
						if (null != DataBaseHelper
								.getDbUtilsInstance(mcontext)
								.findAll(DepartmentVo.class)) {
							DataBaseHelper
									.getDbUtilsInstance(mcontext)
									.deleteAll(DepartmentVo.class);
						}
					} catch (DbException e1) {
						e1.printStackTrace();
					}
					if (ECDevice.isInitialized())
						try {
							ECDevice.unInitial();
						} catch (Exception e) {
							e.printStackTrace();
						}
					SDKCoreHelper.logout(true);

					CCPAppManager.setClientUser(null);

					MainActivity.mInit = false;
					Intent intent = new Intent(mcontext,
							LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					intent.setAction(Constants.AUTHENT_LOSE);
//					sendBroadcast(intent);
					finish();

				}
			});
			cancleBtn = (TextView) this.findViewById(R.id.cacle);
			cancleBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(null!=dialog&&dialog.isShowing()){
			dialog.dismiss();
		}
	}
}
