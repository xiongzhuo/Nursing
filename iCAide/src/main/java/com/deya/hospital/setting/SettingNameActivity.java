package com.deya.hospital.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.EasyBaseAdapter;
import com.deya.hospital.base.EasyViewHolder;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.ToastUtils;
import com.im.sdk.dy.common.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class SettingNameActivity extends BaseActivity {

	private EditText nameEdt;
	private CommonTopView topView;
	public static final String KEY_USER_ID = "KEY_USER_ID";
	public static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
	ImageView cancle;
	private String title = "";
	private String type = "";
	private ListView auto_list;
	private List<String> names = new ArrayList();
	private MAdapter mArrayAdapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_name_activity);
		nameEdt = (EditText) this.findViewById(R.id.nameEdt);
		cancle=(ImageView) this.findViewById(R.id.cancle);
		topView=(CommonTopView)this.findViewById(R.id.topView);
		auto_list = (ListView) findViewById(R.id.auto_list);
		if (getIntent().hasExtra("typename") && getIntent().getStringExtra("typename").equals("name")) {
			title = "姓名";
			nameEdt.setHint("请输入您的姓名");
			type = "name";
		}
		if (getIntent().hasExtra("typename") && getIntent().getStringExtra("typename").equals("email")) {
			title = "邮箱";
			nameEdt.setHint("请输入您的邮箱");
			nameEdt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			type = "email";
			mArrayAdapter  = new MAdapter(this,R.layout.item_email_tip,names);
			auto_list.setAdapter(mArrayAdapter);
			nameEdt.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					LogUtil.i("addTextChangedListener",s.toString()+ "<===beforeTextChanged");
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					LogUtil.i("addTextChangedListener",s.toString()+ "<===onTextChanged");
				}

				@Override
				public void afterTextChanged(Editable s) {
					LogUtil.i("addTextChangedListener",s.toString() + "<===afterTextChanged");
					if (!AbStrUtil.isEmpty(s.toString()) && !s.toString().contains("@")) {
						auto_list.setVisibility(View.VISIBLE);
						names.clear();
						names.add(s.toString() + "@qq.com");
						names.add(s.toString() + "@163.com");
						names.add(s.toString() + "@189.cn");
						names.add(s.toString() + "@sina.cn");
						names.add(s.toString() + "@hotmail.com");
						names.add(s.toString() + "@gmail.cn");
						names.add(s.toString() + "@sohu.com");
						names.add(s.toString() + "@21cn.com");
						mArrayAdapter.updateListView(names);
					} else {
						auto_list.setVisibility(View.GONE);
					}

				}
			});
		}

		topView.setTitle(title);
		topView.init(this);
		topView.onRightClick(this, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (type.equals("email")) {
					String email = nameEdt.getText().toString().trim();
					if (email.equals("")) {
						ToastUtils.showToast(mcontext, "请输入邮箱");
					} else if (!AbStrUtil.isEmail(email)) {
						ToastUtils.showToast(mcontext, "请输入正确邮箱");
					} else {
						Intent intent = new Intent();
						intent.putExtra(KEY_USER_EMAIL, email);
						SettingNameActivity.this.setResult(RESULT_OK, intent);
						SettingNameActivity.this.finish();
					}
				} else {
					String name = nameEdt.getText().toString().trim();
					if (name.equals("")) {
						ToastUtils.showToast(mcontext, "请输入姓名");
					} else {
						Intent intent = new Intent();
						intent.putExtra(KEY_USER_ID, name);
						SettingNameActivity.this.setResult(RESULT_OK, intent);
						SettingNameActivity.this.finish();
					}

				}

			}
		});

		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nameEdt.setText("");

			}
		});
		auto_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!AbStrUtil.isEmpty(names.get(position))) {
					nameEdt.setText(names.get(position));
					names.clear();
					auto_list.setVisibility(View.GONE);
				}
			}
		});
	}

	class MAdapter extends EasyBaseAdapter<String> {

		private TextView btn_name;

		public MAdapter(Context context, int layoutId, List<String> list) {
			super(context, layoutId, list);
		}

		@Override
		public void convert(EasyViewHolder viewHolder, String s) {
			btn_name = viewHolder.getTextView(R.id.btn_name);
			btn_name.setText(s);
		}
	}

}
