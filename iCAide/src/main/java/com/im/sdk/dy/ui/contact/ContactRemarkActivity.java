package com.im.sdk.dy.ui.contact;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.ui.ECSuperActivity;

/**
 * 修改备注
 * Created by yung on 2015/12/20.
 */
public class ContactRemarkActivity extends ECSuperActivity implements
		View.OnClickListener {

	private EditText edt_remark, edt_name;
	private ECContacts contacts;

	private TextView text_remark_t, text_name_t;
	private final int name_len=10;
	private final int remark_len=120;

	@Override
	protected int getLayoutId() {
		return R.layout.im_activity_contact_remark;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contacts = getIntent().getParcelableExtra("contacts");

		initView();
		getTopBarView().setTopBarToStatus(1, R.drawable.btn_back_style, "完成",
				getString(R.string.mobile_contact_remark), this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		edt_remark = (EditText) findViewById(R.id.edt_remark);
		edt_name = (EditText) findViewById(R.id.edt_name);

		text_remark_t = (TextView) findViewById(R.id.text_remark_t);
		text_name_t = (TextView) findViewById(R.id.text_name_t);

		edt_name.addTextChangedListener(new MyTextWatcher(edt_name,
				text_name_t, name_len, R.string.contacts_rname_t));
		edt_remark.addTextChangedListener(new MyTextWatcher(edt_remark,
				text_remark_t, remark_len, R.string.contacts_remark_t));
		edt_name.setText(contacts.getRname() == null
				|| contacts.getRname().equals("") ? contacts.getNickname()
				: contacts.getRname());
		edt_remark.setText(contacts.getRemark());
		Selection.setSelection(edt_name.getText(), edt_name.getText().length());
		// text_remark_t
		// .setText(String.format(
		// res.getString(R.string.contacts_remark_t),
		// edt_remark.length()));
		// text_name_t.setText(String.format(
		// res.getString(R.string.contacts_rname_t), edt_name.length()));

	}

	class MyTextWatcher implements TextWatcher {
		TextView textView;
		int max;
		int textResId;
		EditText editText;
		private CharSequence temp;

		public MyTextWatcher(EditText editText, TextView textView, int max,
				int textResId) {
			this.textView = textView;
			this.max = max;
			this.textResId = textResId;
			this.editText = editText;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			temp = s;
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if ((temp.length() > max)) {
			//	editText.setText(temp.subSequence(0, max));
				Selection.setSelection(editText.getText(), editText.getText()
						.length());
//				textView.setTextColor(Color.RED);
				
			} 
//			else {
//				textView.setTextColor(Color.GRAY);
				textView.setText(String.format(res.getString(textResId),
						s.length(), max));
//			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

	}

	String remark;
	String name;

	private void enter() {
		remark = edt_remark.getText().toString().trim();
		name = edt_name.getText().toString().trim();
		
		if(name.length()>name_len){
			ToastUtils.showToast(mContext, "备注名称字数超过限制！");
			return;
		}
		if(remark.length()>remark_len){
			ToastUtils.showToast(mContext, "描述字数超过限制！");
			return;
		}
		
		if ((contacts.getRemark()==null&&remark.length()>0)||(contacts.getRname()==null&&name.length()>0)||(contacts.getRemark()!=null&&!contacts.getRemark().equals(remark))
				||(contacts.getRname()!=null&&!contacts.getRname().equals(name)) ) {

			updatedata();
		}else{
			finish();
		}
	}

	private static final int SUCCESS = 0x1055;
	private static final int FAIL = 0x1056;

	private void updatedata() {
		showprocessdialog();
		JSONObject json = new JSONObject();
		try {
			json.put("friend_id", contacts.getContactid());
			json.put("rname", name);
			json.put("remark", remark);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().getData(myHandler, this, SUCCESS, FAIL, json,
				WebUrl.UPDATE_FRIEND_REMARK_PATH,
				tools.getValue(Constants.AUTHENT));
	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case SUCCESS:

					if (null != msg && null != msg.obj) {
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(msg.obj.toString());
							String r = jsonObject.getString("result_id");
							if (null != r && r.equals("0")) {
								edt_name.setText(name);
								edt_remark.setText(remark);
								Selection.setSelection(edt_name.getText(),
										edt_name.getText().length());
								contacts.setRemark(remark);
								contacts.setRname(name);
								ContactSqlManager.updateContactRemark(
										contacts.getContactid(), name, remark);
								ContactLogic.updateContacts(contacts);

								ToastUtils.showToast(mContext, "修改成功！");
								
								Intent intents = new Intent();
								intents.putExtra("contacts", contacts);
								setResult(RESULT_OK, intents);
								finish();
							}
							dismissdialog();
							return;
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					dismissdialog();
					ToastUtils.showToast(mContext, "修改失败！");
					break;
				case FAIL:
					dismissdialog();
					ToastUtils.showToast(mContext, "亲，您的网络不顺畅哦！");
					break;
				default:
					break;
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_left:case R.id.btn_text_left:
			hideSoftKeyboard();
			Intent intents = new Intent();
			intents.putExtra("contacts", contacts);
			setResult(RESULT_OK, intents);
			finish();
			break;
		case R.id.text_right:
			if ("".equals(edt_name.getText()+"")) {
				finish();
			}else {
				enter();
			}
			
			break;
		default:
			break;
		}
	}
}
