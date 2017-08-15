package com.im.sdk.dy.ui.contact;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.deya.acaide.R;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.ui.ECSuperActivity;

/**
 * Created by yung on 2015/12/20.
 */
public class ContactMobileAddActivity extends ECSuperActivity implements View.OnClickListener{

	private EditText edt_mobile,edt_name;
	private Button btn_enter;
	ImageView cacleImg;

    @Override
    protected int getLayoutId() {
        return R.layout.im_activity_mobile_add;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getTopBarView().setTopBarToStatus(1, R.drawable.btn_back_style, R.color.white,"手机号码添加", this);
        getTopBarView().setRightButtonText("完成");
        getTopBarView().setRightClick(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				enter();	
				
			}
		});

    }

    private void initView() {
		// TODO Auto-generated method stub
    	edt_mobile=(EditText)findViewById(R.id.edt_mobile);
//    	edt_name=(EditText)findViewById(R.id.edt_name);
    	findViewById(R.id.btn_enter).setOnClickListener(this);
    	findViewById(R.id.cacleImg).setOnClickListener(this);
	}
    
    String phone;
//	String name;
    private void enter(){
    	phone = edt_mobile.getText().toString().trim();
//		name = edt_name.getText().toString().trim();
		Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
		Matcher m = p.matcher(phone);
		if (!m.find()) {
			ToastUtils.showToast(mContext,
					res.getString(R.string.edt_check_phone));
			return;
		}
//		if (name.length() < 1) {
//			ToastUtils.showToast(mContext,
//					res.getString(R.string.edt_check_linkman));
//			return;
//		}
		
		ECContacts c=ContactSqlManager.getContactByPhone(phone);
		if(null!=c){
		
		 Intent intent = new Intent(mContext, ContactDetailActivity.class);
         intent.putExtra(ContactDetailActivity.RAW_ID, c.getId());
         startActivity(intent);
         finish();
         
		}else{
		sendmsg();
		}
    }
    
    private void sendmsg() {
    	showprocessdialog();
		JSONObject json = new JSONObject();
		try {
			json.put("mobile", phone);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	//判断是否存在
    	MainBizImpl.getInstance().getData(new ContactsHandler(this), this, SUCCESS, FAIL, json,
				WebUrl.CONTACT_FIND_PATH,
				tools.getValue(Constants.AUTHENT));
		// TODO Auto-generated method stub
	}
    
    private static final int SUCCESS = 0x1055;
	private static final int FAIL = 0x1056;
	
	class ContactsHandler extends MyHandler{
		public ContactsHandler(Activity leakActivity) {
			super(leakActivity);
		}
		@Override
		public void handleMessage(Message msg) {
			DebugUtil.debug("gerfriends"," msg>>"+msg.obj.toString());
				switch (msg.what) {
				case SUCCESS:
					dismissdialog();
					if (null != msg && null != msg.obj) {
						JSONObject jsonObject;
						DebugUtil.debug("gerfriends",msg.obj.toString());
						try {
							jsonObject = new JSONObject(msg.obj.toString());
							String r = jsonObject.getString("result_id");
							if (null != r && r.equals("1")) {
								
							}else if (null != r && r.equals("2")) {
							//	contacts.getMobile()
								ContactLogic.sendSystemMessage(ContactMobileAddActivity.this,phone);
							}else{
								 JSONObject  itemJson = jsonObject.getJSONObject("friend");
								 ECContacts contacts=ContactLogic.GetContacts(itemJson);
								 if(null!=contacts){
									
									 if(CCPAppManager.getClientUser().getUserId().equals(contacts.getContactid())){
										ToastUtils.showToast(mContext, "不可添加自己为好友");
									 }else{
										 ContactSqlManager.insertContact(contacts);
										 Intent intent = new Intent(mContext, ContactDetailActivity.class);
								            intent.putExtra(ContactDetailActivity.RAW_ID, contacts.getId());
								            intent.putExtra(ContactDetailActivity.CONTACTS,contacts);
								            startActivity(intent);
								            finish();
									 }
								 }
							}
						
							return;
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					break;
				case FAIL:
					dismissdialog();
					ToastUtils.showToast(ContactMobileAddActivity.this, "亲，您的网络不顺畅哦！");
					break;
				default:
					break;
				}
			}
		
	}

	@Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
		case R.id.btn_left:
		case R.id.btn_text_left:
			hideSoftKeyboard();
			finish();
			break;
		case R.id.cacleImg:
			edt_mobile.setText("");
			break;
		default:
			break;
		}
    }


}
