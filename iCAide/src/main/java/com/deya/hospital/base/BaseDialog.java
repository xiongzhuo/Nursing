package com.deya.hospital.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.deya.acaide.R;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;

public class BaseDialog extends Dialog {
	public Tools tools;
	Context context;
	/**
	 * Creates a new instance of MyDialog.
	 */
	public BaseDialog(Context context) {
		super(context, R.style.SelectDialog);
		this.context=context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		tools=new Tools(context, Constants.AC);

	}
	public    <T extends View> T findView(int id){
		return (T) findViewById(id);
	}
}
