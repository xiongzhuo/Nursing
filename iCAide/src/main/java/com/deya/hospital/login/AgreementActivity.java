package com.deya.hospital.login;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ToastUtils;

public class AgreementActivity extends BaseActivity{
	protected static final int GET_SUCESS = 0x007;
	protected static final int GET_FAILE = 0x008;
	private ImageView backImg;
	private LinearLayout bgImg;
	private 	TextView agreementTv;
	private LinearLayout networkView;
	CommonTopView topView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_agreement);
		checkNetWork();
		initView();
	}
	public void checkNetWork(){
		networkView=(LinearLayout) this.findViewById(R.id.networkView);
			if(NetWorkUtils.isConnect(mcontext)){
				networkView.setVisibility(View.GONE);
			}else{
				networkView.setVisibility(View.VISIBLE);
			}
			
		}
	private void initView() {
		agreementTv=(TextView) this.findViewById(R.id.agreement);
//		
//		bgImg=(LinearLayout) this.findViewById(R.id.topbg);
//		 int[] wh = AbViewUtil.getDeviceWH(mcontext);
//	    LayoutParams para2 = bgImg.getLayoutParams();
//	    para2.height = (wh[0] * 1 / 2);
//	    para2.width = wh[0];
//	    bgImg.setLayoutParams(para2);
	    
		topView = (CommonTopView) this.findViewById(R.id.topView);
		topView.init(this);

	    getRequest();
	}
	private void getRequest() {
	
		MainBizImpl.getInstance().getAgreemtnText(myHandler, GET_SUCESS, GET_FAILE);
		}
	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case GET_SUCESS:
					if (null != msg && null != msg.obj) {
						agreementTv.setText(msg.obj.toString());
					}
					break;
				case GET_FAILE:
					ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
					break;
			

				default:
					break;
				}
			}	
		}
		};


}
