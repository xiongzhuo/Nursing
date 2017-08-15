package com.im.sdk.dy.ui.contact;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.common.view.RoundedImageView;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.ui.ECSuperActivity;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * 联系人详情页面
 * Created by yung on 2015/12/18.
 */
public class GoupMenberDetailActivity extends ECSuperActivity implements
		View.OnClickListener {

	public final static String RAW_ID = "raw_id";
	public final static String MOBILE = "mobile";
	public final static String CONTACTS="contacts"; 
	public final static String DISPLAY_NAME = "display_name";

	private RoundedImageView mPhotoView;
	private EmojiconTextView mUsername;
	private TextView mNumber, text_addr, text_hos, text_dep, text_desc;
	private RelativeLayout lay_remark;
	private Button btn_enter;

	private ECContacts mContacts;
    String contactId="";
    Context mContext;
	@Override
	protected int getLayoutId() {
		return R.layout.im_layout_contact_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=this;
		contactId=getIntent().getStringExtra("contact_id");
		initView();
		initActivityState(savedInstanceState);
		getTopBarView().setTopBarToStatus(1, R.drawable.btn_back_style, -1,
				R.string.contact_contactDetail, this);
	}

	/**
	 * @param savedInstanceState
	 */
	private void initActivityState(Bundle savedInstanceState) { 
	long rawId = getIntent().getLongExtra(RAW_ID, -1);
//		if (rawId == -1) {
//			String mobile = getIntent().getStringExtra(MOBILE);
//			String displayname = getIntent().getStringExtra(DISPLAY_NAME);
//			
//			mContacts = ContactSqlManager.getCacheContact(mobile);
//			if (mContacts == null) {
//				mContacts = new ECContacts(mobile);
//				mContacts.setNickname(displayname);
//			}
//		}
//		
//		if(mContacts == null && rawId != -1){
//			mContacts=getIntent().getParcelableExtra(CONTACTS);
//		}
//
//		if (mContacts == null && rawId != -1) {
//			mContacts = ContactSqlManager.getContact(rawId);
//		}
	//	sysContacts();

		if (mContacts == null) {
			
			getContactInfo(rawId+"");
			return;
		}

		setUi();
	}

	protected static final int ADD_FAILE = 0x60089;
	  protected static final int ADD_SUCESS = 0x60090;
	  public void getContactInfo(String id){
		  tools=new Tools(mContext, Constants.AC);
			JSONObject job = new JSONObject();
			try {
				job.put("authent", tools.getValue(Constants.AUTHENT));
				job.put("uid", id);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			MainBizImpl.getInstance().onComomRequest(myHandler,GoupMenberDetailActivity.this, ADD_SUCESS,
					ADD_FAILE, job,"iminfo/queryFriendById");
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
								JSONObject job=new JSONObject((msg.obj).toString());
								toditailActivity(job);
							} catch (Exception e) {
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
	
	public void setmContacts(String mobile) {
		mContacts = ContactSqlManager.getCacheContact(mobile);
//		if (mContacts == null) {
//			mContacts = new ECContacts(mobile);
//			mContacts.setNickname(displayname);
//		}
//	
//	if(mContacts == null && rawId != -1){
//		mContacts=getIntent().getParcelableExtra(CONTACTS);
//	}
//
//	if (mContacts == null && rawId != -1) {
//		mContacts = ContactSqlManager.getContact(rawId);
//	}
//	sysContacts();

	if (mContacts == null) {
		ToastUtil.showMessage(R.string.contact_none);
		finish();
		return;
	}

	setUi();
	}
	protected void toditailActivity(JSONObject job) {
			ECContacts contacts = new ECContacts();
			JSONObject itemJson = job.optJSONObject("friend");
			if(itemJson.has("friend_id")&&!AbStrUtil.isEmpty(itemJson.optString("friend_id"))){
			contacts.setContactid(itemJson.optInt("friend_id") + "");
			}else{
				return;
			}
			
			if(itemJson.has("name")&&!AbStrUtil.isEmpty(itemJson.optString("name"))){
			contacts.setNickname(itemJson.optString("name"));
			}else{
				contacts.setNickname("");
			}

			if(itemJson.has("friend_type")&&!AbStrUtil.isEmpty(itemJson.optString("friend_type"))){
			contacts.setType(itemJson.optInt("friend_type"));
			}else{
				contacts.setType(0);;
			}
			
			if(itemJson.has("mobile")&&!AbStrUtil.isEmpty(itemJson.optString("mobile"))){
			contacts.setMobile(itemJson.optString("mobile"));
			}else{
				return;
			}
			
			if(itemJson.has("avatar")&&!AbStrUtil.isEmpty(itemJson.optString("avatar"))){
			contacts.setAvatar(itemJson.optString("avatar"));
			}else{
				contacts.setAvatar("");
			}
			
			if(itemJson.has("city")&&!AbStrUtil.isEmpty(itemJson.optString("city"))){
			contacts.setCity(itemJson.optString("city"));
			}else{
				contacts.setCity("");
			}
			
			if(itemJson.has("department")&&!AbStrUtil.isEmpty(itemJson.optString("department"))){
			contacts.setDepartment(itemJson.optString("department"));
			}else{
				contacts.setDepartment("");;
			}
			
			if(itemJson.has("hospital")&&!AbStrUtil.isEmpty(itemJson.optString("hospital"))){
			contacts.setHospital(itemJson.optString("hospital"));
			}else{
				contacts.setHospital("");
			}

			if(itemJson.has("sex")&&!AbStrUtil.isEmpty(itemJson.optString("sex"))){
			contacts.setSex(itemJson.optInt("sex"));
			}else{
				contacts.setSex(0);	
			}
			
			if(itemJson.has("province")&&!AbStrUtil.isEmpty(itemJson.optString("province"))){
			contacts.setProvince(itemJson.optString("province"));
			}else{
				contacts.setProvince("");
			}
			
			
			if(itemJson.has("remark")&&!AbStrUtil.isEmpty(itemJson.optString("remark"))){
				contacts.setRemark(itemJson.optString("remark"));
				}else{
					contacts.setRemark("");
				}
			String rname = null ;
			if(itemJson.has("rname")&&!AbStrUtil.isEmpty(itemJson.optString("rname"))){
				 rname = itemJson.optString("rname");
				
			}else{
				 rname = "";
			}
			
			rname = rname == null || rname.equals("") ? contacts
					.getNickname() : rname;
					
					contacts.setRname(rname);
			//LogUtil.d("cmdContactsss", "sex>>" + itemJson.getInt("sex"));

			mContacts=contacts;
			setUi();
		
	}

	private void setUi() {
		int sex = mContacts.getSex();

		DisplayImageOptions optionsSquare = new DisplayImageOptions.Builder()
				.showImageOnLoading(
						sex == 1 ? R.drawable.im_women_defalut
								: R.drawable.im_men_defalut)
				.showImageForEmptyUri(
						sex == 1 ? R.drawable.im_women_defalut
								: R.drawable.im_men_defalut)
				.showImageOnFail(
						sex == 1 ? R.drawable.im_women_defalut
								: R.drawable.im_men_defalut)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new SimpleBitmapDisplayer()).build();
			//	.displayer(new FadeInBitmapDisplayer(300)).build();

		if (!AbStrUtil.isEmpty(mContacts.getAvatar())) {
			ImageLoader.getInstance().displayImage(
					WebUrl.FILE_LOAD_URL + mContacts.getAvatar(), mPhotoView,
					optionsSquare);

		} else {
			ImageLoader.getInstance().displayImage("", mPhotoView,
					optionsSquare);
		}

		// mPhotoView.setImageBitmap(ContactLogic.getPhoto(mContacts.getRemark()));

		mUsername.setText(TextUtils.isEmpty(mContacts.getRname()) ? mContacts
				.getNickname() : mContacts.getRname());
		if(null==mUsername){
			initView();
		}
//		if(null!=contactId&&contactId.equals("29")){
//			mUsername.setText("感控小助手");
//		}
//		if(null!=contactId&&contactId.equals("31")){
//			mUsername.setText("感控专家");
//		}
		
		String mobile = mContacts.getMobile();
		String maskNumber = mobile.substring(0, 3) + "*****"
				+ mobile.substring(8, mobile.length());
		mNumber.setText(maskNumber);

		text_addr.setText(mContacts.getProvince() + " " + mContacts.getCity());
		text_hos.setText(mContacts.getHospital());
		text_dep.setText(mContacts.getDepartment());

		if (!TextUtils.isEmpty(mContacts.getRemark())) {
			text_desc.setText(mContacts.getRemark());
			findViewById(R.id.lay_desc).setVisibility(View.VISIBLE);
			findViewById(R.id.view_line_2).setVisibility(View.VISIBLE);
			findViewById(R.id.view_line_1).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.lay_desc).setVisibility(View.GONE);
			findViewById(R.id.view_line_2).setVisibility(View.GONE);
			findViewById(R.id.view_line_1).setVisibility(View.GONE);
		}
	}

	private static final int SUCCESS = 0x1055;
	private static final int FAIL = 0x1056;

	// 检查用户信息
	private void sysContacts() {

		JSONObject json = new JSONObject();
		if(null!=mContacts){

		try {
			json.put("uid", mContacts.getContactid());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().getData(new ContactsHandler(this), SUCCESS,
				FAIL, json, WebUrl.CONTACT_FIND_ID_PATH,
				tools.getValue(Constants.AUTHENT));
		}
	}

	class ContactsHandler extends MyHandler {
		public ContactsHandler(Activity leakActivity) {
			super(leakActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			DebugUtil.debug("gerfriends", " msg>>" + msg.obj.toString());
			switch (msg.what) {
			case SUCCESS:
				if (null != msg.obj) {
					cmdContact(msg.obj.toString());

				}
				break;
			case FAIL:
				break;
			default:
				break;
			}
		}

	}

	private void cmdContact(String json) {

//		try {
//			JSONObject jsonObject = new JSONObject(json);
//			String r = jsonObject.getString("result_id");
//			if (null != r && r.equals("1")) {
//			} else if (null != r && r.equals("2")) {
//			} else {
//				JSONObject itemJson = jsonObject.getJSONObject("friend");
//				ContactSqlManager.insertContact(ContactLogic
//						.GetContacts(itemJson));
//			}
//
//			return;
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		try {
			JSONObject jsonObject = new JSONObject(json);
			String r = jsonObject.getString("result_id");
			if (null != r && r.equals("1")) {
			} else if (null != r && r.equals("2")) {
			} else {
				LogUtil.d("cmdContacts", " cmdContact json>>" + json);
				JSONObject itemJson = jsonObject.getJSONObject("friend");

				mContacts = ContactLogic.GetContacts(itemJson);
				if (null != mContacts) {
					ContactSqlManager.insertContact(mContacts);

					// 更改UI

					setUi();
				}

			}
		} catch (JSONException e5) {
			e5.printStackTrace();
		}
	}

	/**
     *
     */
	private void initView() {
		mPhotoView = (RoundedImageView) findViewById(R.id.desc);

		mUsername = (EmojiconTextView) findViewById(R.id.contact_nameTv);

		mNumber = (TextView) findViewById(R.id.contact_numer);

		text_addr = (TextView) findViewById(R.id.text_addr);
		text_hos = (TextView) findViewById(R.id.text_hos);
		text_dep = (TextView) findViewById(R.id.text_dep);
		text_desc = (TextView) findViewById(R.id.text_desc);
		lay_remark = (RelativeLayout) findViewById(R.id.lay_remark);

		lay_remark.setOnClickListener(this);

		mNumber.setOnClickListener(this);
		
		btn_enter = (Button) findViewById(R.id.btn_enter);
		btn_enter.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPhotoView != null) {
			mPhotoView.setImageDrawable(null);
		}
		mContacts = null;

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.lay_remark:
			Intent intent = new Intent(mContext, ContactRemarkActivity.class);
			intent.putExtra("contacts", mContacts);
			startActivityForResult(intent, 1);
			break;
		case R.id.btn_left:
		case R.id.btn_text_left:
			hideSoftKeyboard();
			Intent intents = new Intent();
			intents.putExtra("contacts", mContacts);
			setResult(RESULT_OK, intents);
			finish();
			break;
		case R.id.btn_enter:
			CCPAppManager.startChattingAction(GoupMenberDetailActivity.this,
					mContacts.getContactid(), mContacts.getRname(),
					mContacts.getAvatar(), true, mContacts.getType());
			finish();
			break;
		case R.id.contact_numer:
			String tel=mNumber.getText().toString();
			String regex = "1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(tel);

			if (m.find()) {
				Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + tel)); 
				startActivity(phoneIntent);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			mContacts = data.getParcelableExtra("contacts");
			mUsername
					.setText(TextUtils.isEmpty(mContacts.getRname()) ? mContacts
							.getNickname() : mContacts.getRname());
			text_desc.setText(mContacts.getRemark());
			if (!TextUtils.isEmpty(mContacts.getRemark())) {
				text_desc.setText(mContacts.getRemark());
				findViewById(R.id.lay_desc).setVisibility(View.VISIBLE);
				findViewById(R.id.view_line_2).setVisibility(View.VISIBLE);
				findViewById(R.id.view_line_1).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.lay_desc).setVisibility(View.GONE);
				findViewById(R.id.view_line_2).setVisibility(View.GONE);
				findViewById(R.id.view_line_1).setVisibility(View.GONE);
			}
			
		}
	}
}
