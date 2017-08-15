package com.im.sdk.dy.ui.group;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.DebugUtil;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.dialog.ECProgressDialog;
import com.im.sdk.dy.common.utils.DemoUtils;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.storage.GroupSqlManager;
import com.im.sdk.dy.ui.ActivityTransition;
import com.im.sdk.dy.ui.ECSuperActivity;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.im.sdk.dy.ui.account.AccountLogic;
import com.im.sdk.dy.ui.chatting.IMChattingHelper;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.im.sdk.dy.ui.contact.MobileContactSelectActivity;
import com.im.sdk.dy.ui.group.GroupMemberService.GroupServiceInterface;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

/**
 * 群组创建功能
 * @date 2014-12-27
 * @version 4.0
 */
@ActivityTransition(5)
public class CreateGroupActivity extends ECSuperActivity implements
		View.OnClickListener, ECGroupManager.OnCreateGroupListener,
		GroupMemberService.OnSynsGroupMemberListener {

	private static final String TAG = "ECDemo.CreateGroupActivity";
	/** 群组名称 */
	private EditText mNameEdit;
	/** 群组公告 */
	private EditText mNoticeEdit;
	/** 创建按钮 */
	private Button mCreateBtn;
	/** 创建的群组 */
	private ECGroup group;
	private ECProgressDialog mPostingdialog;
	private ScrollView scroll;
	
	
	private TextView text_remark_t, text_name_t;

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
				editText.setText(temp.subSequence(0, max));
				Selection.setSelection(editText.getText(), editText.getText()
						.length());
			} else {
				textView.setText(String.format(res.getString(textResId),
						s.length(), max));
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.im_new_group;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int resId = R.string.app_title_create_new_group;
//			resId = R.string.discussion_create;
		getTopBarView().setTopBarToStatus(1, R.drawable.btn_back_style, -1,
				resId, this);

		initView();
	}

	/**
	 * 关闭对话框
	 */
	private void dismissPostingDialog() {
		if (mPostingdialog == null || !mPostingdialog.isShowing()) {
			return;
		}
		mPostingdialog.dismiss();
		mPostingdialog = null;
	}

	/**
     *
     */
	private void initView() {
		text_remark_t = (TextView) findViewById(R.id.text_remark_t);
		text_name_t = (TextView) findViewById(R.id.text_name_t);
		mNameEdit = (EditText) findViewById(R.id.group_name);
		mNoticeEdit= (EditText) findViewById(R.id.group_notice);
		
//			mNameEdit.setHint(R.string.discussion_name);
//		mNoticeEdit.setHint(R.string.discussion_notice);

		mCreateBtn = (Button) findViewById(R.id.create);
		mCreateBtn.setOnClickListener(this);
		
		mNameEdit.addTextChangedListener(new MyTextWatcher(mNameEdit,
				text_name_t, 10, R.string.group_name_t));
		mNoticeEdit.addTextChangedListener(new MyTextWatcher(mNoticeEdit,
				text_remark_t, 100, R.string.group_notice_t));
		mNameEdit.setText("");
		mNoticeEdit.setText("");
		
		scroll=(ScrollView)findViewById(R.id.scroll);
		
	}

	/**
	 * @return
	 */
	private boolean checkNameEmpty() {
		return mNameEdit != null
				&& mNameEdit.getText().toString().trim().length() > 0;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
		case R.id.btn_text_left:
			hideSoftKeyboard();
			finish();
			break;
		case R.id.create:
			ECGroup group=getGroup();
			if(!checkNameEmpty()){
				ToastUtil.showMessage("请输入群组名称");
				return;
			}
			hideSoftKeyboard();
			ECGroupManager ecGroupManager = SDKCoreHelper.getECGroupManager();
			if (ecGroupManager == null) {
				return;
			}
			// 调用API创建群组、处理创建群组接口回调

			//isDiscussion ? R.string.create_dis_posting
			
			mPostingdialog = new ECProgressDialog(this,
					 R.string.create_group_posting);
			mPostingdialog.show();
			
			ecGroupManager.createGroup(group, this);
			

			break;
		default:
			break;
		}
	}

	/**
	 * 创建群组参数
	 * 
	 * @return
	 */
	private ECGroup getGroup() {
		ECGroup group = new ECGroup();
		// 设置群组名称
		group.setName(mNameEdit.getText().toString().trim());
		// 设置群组公告
		group.setDeclare(mNoticeEdit.getText().toString().trim());
		// 临时群组（100人）
		group.setScope(ECGroup.Scope.NORMAL_SENIOR);
		// 群组验证权限，需要身份验证
		group.setPermission(ECGroup.Permission.AUTO_JOIN);
		// 设置群组创建者
		group.setOwner(CCPAppManager.getClientUser().getUserId());
		//
		group.setIsDiscuss(true);
		
		group.setGroupDomain("DY={'a':'XXX','t':1}");
		return group;
	}

	@Override
	protected void onResume() {
		super.onResume();
		GroupMemberService.addListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
				+ ", resultCode=" + resultCode + ", data=" + data);

		// If there's no data (because the user didn't select a picture and
		// just hit BACK, for example), there's nothing to do.
		if (requestCode == 0x2a) {
			if (data == null) {
				
				finish();
				return;
			}else{
				handleSendTextMessage("欢迎加入我们的聊天群",group.getGroupId());
			}
		} else if (resultCode != RESULT_OK) {
			LogUtil.d("onActivityResult: bail due to resultCode=" + resultCode);
			finish();
			return;
		}

		String[] selectUser = data.getStringArrayExtra("Select_Conv_User");
		ArrayList<ECContacts> contacts=(ArrayList<ECContacts>)data.getSerializableExtra("contactlist");
		if (selectUser != null && selectUser.length > 0) {
			mPostingdialog = new ECProgressDialog(this,
					R.string.invite_join_group_posting);
			mPostingdialog.show();
			GroupMemberService.inviteMembers(group.getGroupId(), "",
					ECGroupManager.InvitationMode.FORCE_PULL, selectUser,contacts,new GroupServiceInterface() {
						
						@Override
						public void onInvateComplet() {
							// TODO Auto-generated method stub
							
						}
					});
		}

	}
	/**
	 * 处理文本发送方法事件通知
	 * 
	 * @param text
	 */
	private void handleSendTextMessage(CharSequence text,String mRecipients) {
		if (text == null) {
			return;
		}
		// 组建一个待发送的ECMessage
		ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
		// 设置消息接收者
		msg.setTo(mRecipients);
		msg.setSessionId(mRecipients);
		DebugUtil.debug("iniIm", "to id>>" + mRecipients);

		// 创建一个文本消息体，并添加到消息对象中
		ECTextMessageBody msgBody = new ECTextMessageBody(text.toString());

		msg.setBody(msgBody);
		try {
			// 发送消息，该函数见上
			long rowId = -1;
				rowId = IMChattingHelper.sendECMessage(msg);
			// 通知列表刷新
			msg.setId(rowId);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void onCreateGroupComplete(ECError error, ECGroup group) {
		if (error.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
			// 创建的群组实例化到数据库
			// 其他的页面跳转逻辑
			group.setIsNotice(true);
			
			//修改群组名片
        	AccountLogic.setMemberInfo(group.getGroupId(), CCPAppManager.getClientUser());
        	

			GroupSqlManager.insertGroup(group, true, false, false,false);
			this.group = group;
			
			Intent intent = new Intent(this, MobileContactSelectActivity.class);
			intent.putExtra("group_select_need_result", true);
			intent.putExtra("isFromCreateDiscussion", false);
			startActivityForResult(intent, 0x2a);
			
			
			
		} else {
				ToastUtil.showMessage("创建群组失败[" + error.errorCode + "]");
		}
		dismissPostingDialog();
	}

	@Override
	public void onSynsGroupMember(String groupId) {
		dismissPostingDialog();
		CCPAppManager.startChattingAction(CreateGroupActivity.this, groupId,
				group.getName(),0);
		finish();
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static int filteCounts(CharSequence text) {
		int count = (30 - Math.round(calculateCounts(text)));
		LogUtil.v(LogUtil.getLogUtilsTag(SearchGroupActivity.class), "count "
				+ count);
		return count;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static float calculateCounts(CharSequence text) {

		float lengh = 0.0F;
		for (int i = 0; i < text.length(); i++) {
			if (!DemoUtils.characterChinese(text.charAt(i))) {
				lengh += 1.0F;
			} else {
				lengh += 0.5F;
			}
		}

		return lengh;
	}

	class ITextFilter implements InputFilter {
		private int limit = 50;

		public ITextFilter() {
			this(0);
		}

		public ITextFilter(int type) {
			if (type == 1) {
				limit = 128;
			}
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			LogUtil.i(LogUtil.getLogUtilsTag(CreateGroupActivity.class), source
					+ " start:" + start + " end:" + end + " " + dest
					+ " dstart:" + dstart + " dend:" + dend);
			float count = calculateCounts(dest);
			int overplus = limit - Math.round(count) - (dend - dstart);
			if (overplus <= 0) {
				if ((Float.compare(count, (float) (limit - 0.5D)) == 0)
						&& (source.length() > 0)
						&& (!(DemoUtils.characterChinese(source.charAt(0))))) {
					return source.subSequence(0, 1);
				}
				ToastUtil.showMessage("超过最大限制");
				return "";
			}

			if (overplus >= (end - start)) {
				return null;
			}
			int tepmCont = overplus + start;
			if ((Character.isHighSurrogate(source.charAt(tepmCont - 1)))
					&& (--tepmCont == start)) {
				return "";
			}
			return source.subSequence(start, tepmCont);
		}
	}
}
