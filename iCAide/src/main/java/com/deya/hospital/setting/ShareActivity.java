package com.deya.hospital.setting;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.Tools;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

public class ShareActivity extends BaseActivity {
	private RelativeLayout img_back;
	private CommonTopView topView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TextView yes;
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_);
		yes = (TextView) this.findViewById(R.id.yes);
		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showShare();

			}
		});
		topView=(CommonTopView)this.findViewById(R.id.topView);
		topView.init(this);
		TextView mobile = (TextView) this.findViewById(R.id.visitorCode);
		mobile.setText(tools.getValue(Constants.INVITATION_CODE));
	}

	private void showShare() {
		SHARE_MEDIA[] shareMedia = { SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.WEIXIN_CIRCLE };
	}
	
	
	  protected static final int ADD_FAILE = 0x60089;
	  protected static final int ADD_SUCESS = 0x60090;
	  public void getAddScore(String id){
		  Log.i("share_umeng","111111111111111" );
		  tools=new Tools(mcontext, Constants.AC);
			JSONObject job = new JSONObject();
			try {
				job.put("authent", tools.getValue(Constants.AUTHENT));
				job.put("aid", id);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			MainBizImpl.getInstance().onComomRequest(myHandler,ShareActivity.this, ADD_SUCESS,
					ADD_FAILE, job,"goods/actionGetIntegral");
		}
		
		
		private MyHandler myHandler = new MyHandler(this) {
			@Override
			public void handleMessage(Message msg) {
				Activity activity = myHandler.mactivity.get();
				if (null != activity) {
					switch (msg.what) {
					case ADD_SUCESS:
						if (null != msg && null != msg.obj) {
							Log.i("1111msg", msg.obj + "");
							try {
								setAddRes(new JSONObject(msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						break;
					case ADD_FAILE:
						Log.i("1111msg", msg.obj + "");
						// ToastUtils.showToast(getActivity(), "");
						break;
					default:
						break;
					}
				}
			}
			};
		protected void setAddRes(JSONObject jsonObject) {
			Log.i("share_umeng","返回次数" );
			Log.i("11111111", jsonObject.toString());
			if(jsonObject.optString("result_id").equals("0")){
				int score=jsonObject.optInt("integral");
				String str=tools.getValue(Constants.INTEGRAL);
				 if(null!=str){
					 tools.putValue(Constants.INTEGRAL, Integer.parseInt(str)+score+"");
				 }else{
					 tools.putValue(Constants.INTEGRAL,score+"");
				 }
				if(score>0){
					showTipsDialog(score+"");
				}
			
			}
		}
}
