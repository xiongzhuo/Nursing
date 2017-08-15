package com.deya.hospital.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.deya.acaide.R;

public class DeletDialog extends Dialog {

	private TextView showBtn;
	private TextView deletBtn;
	private TextView cancleBtn;
	View.OnClickListener listener;

	/**
	 * Creates a new instance of MyDialog.
	 */
	public DeletDialog(Context context, int theme, View.OnClickListener listener) {
		super(context, theme);
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.dialog_delet);

		showBtn = (TextView) this.findViewById(R.id.show);
		showBtn.setText("确定删除这条数据？");
		deletBtn = (TextView) this.findViewById(R.id.yes);
		deletBtn.setOnClickListener(listener);

		cancleBtn = (TextView) this.findViewById(R.id.cacle);
		cancleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}
}
