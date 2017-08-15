package com.deya.hospital.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.UploadBizImpl;
import com.deya.hospital.login.AgreementActivity;
import com.deya.hospital.services.LogService;
import com.deya.hospital.shop.VipTipsActivity;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.ZipUtil;

/**
 * 我-联系我们
 * @author Administrator
 *
 */
public class ContectUsActivity extends BaseActivity implements OnClickListener {
	RelativeLayout suggestLay;
	RelativeLayout aboutUsLay;
	private RelativeLayout rlBack;
	private RelativeLayout xieyiLay;
	private RelativeLayout uploadLay;
	private TextView tellTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contect);
		intTopView();
		intiVew();
	}

	private void intTopView() {
		TextView titleTv = (TextView) this.findViewById(R.id.title);
		titleTv.setText("联系我们");
		rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
		rlBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	private void intiVew() {
		
		String pkName = this.getPackageName();
		try {
			String versionName = this.getPackageManager().getPackageInfo(
						pkName, 0).versionName;
			TextView tv=(TextView) this.findViewById(R.id.textView1);
			tv.setText("当前版本 V"+versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		suggestLay = (RelativeLayout) this.findViewById(R.id.suggestLay);
		aboutUsLay = (RelativeLayout) this.findViewById(R.id.aboutUsLay);
		xieyiLay=(RelativeLayout)this.findViewById(R.id.xieyi);
		xieyiLay.setOnClickListener(this);
		
		uploadLay = (RelativeLayout) this.findViewById(R.id.uploadlog);
		uploadLay.setOnClickListener(this);
		
		suggestLay.setOnClickListener(this);
		aboutUsLay.setOnClickListener(this);
		tellTv=(TextView) this.findViewById(R.id.tellTv);
		tellTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
		tellTv.setOnClickListener(new OnClickListener() {
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
			
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.suggestLay:
			Intent it=new Intent(mcontext,SuggestActivity.class);
			startActivity(it);
			break;
		case R.id.aboutUsLay:
			Intent it2=new Intent(mcontext,VipTipsActivity.class);
			startActivity(it2);
			break;
		case R.id.xieyi:
			Intent Intent = new Intent(mcontext,
					AgreementActivity.class);
			startActivity(Intent);
			break;
		case R.id.uploadlog:
			uploadLog();
			break;
		default:
			break;
		}

	}
	
	/**
	 * 上传今天的日志
	 */
	private void uploadLog(){
		try{
			String logPath = LogService.getLogPath();
			
			String path = logPath+".zip";
			ZipUtil.compress(logPath, path);
			
			UploadBizImpl.getInstance().propertyUploadPicture(myHandler,
					path,
					UploadMessage.UPLOAD_LOG_SUCCESS,
					UploadMessage.UPLOAD_LOG_FAIL);
		}catch(Exception e){
			Log.e("uploadLog", e.getMessage()+"");
		}
		
	}
	
	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case UploadMessage.UPLOAD_LOG_SUCCESS:// 上传图片成功
					if (null != msg && null != msg.obj) {
						Log.i("UPLOAD_LOG", msg.obj + "");
						
						ToastUtils.showToast(activity, "上传成功");
					}
					break;
				case UploadMessage.UPLOAD_PICTURE_FAIL:// 上传图片失败
					Log.e("UPLOAD_LOG", msg.obj+"");
					break;
				default:
					break;
				}
			}
		}

	};

}
