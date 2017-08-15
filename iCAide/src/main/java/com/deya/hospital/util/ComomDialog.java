package com.deya.hospital.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.deya.acaide.R;

public class ComomDialog extends Dialog {

	private TextView showBtn;
	private TextView deletBtn;
	private TextView cancleBtn;
	View.OnClickListener listener;
	String title="";
	/**
	 * Creates a new instance of MyDialog.
	 */
	public ComomDialog(Context context, String title,int theme,View.OnClickListener listener) {
		super(context, theme);
		this.listener=listener;
		this.title=title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.dialog_delet);

		showBtn = (TextView) this.findViewById(R.id.show);
		showBtn.setText(title);
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