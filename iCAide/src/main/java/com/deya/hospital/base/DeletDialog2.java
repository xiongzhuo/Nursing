package com.deya.hospital.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.deya.acaide.R;

public class DeletDialog2 extends BaseDialog {

	private TextView showBtn;
	private TextView deletBtn;
	private TextView cancleBtn;
	String title="";
	DeletInter deletInter;
	int postion;
	/**
	 * Creates a new instance of MyDialog.
	 */
	public DeletDialog2(Context context, String title, DeletInter deletInter) {
		super(context);
		this.title=title;
		this.deletInter=deletInter;
	}

	public void setDeletPosition(int postion){
		this.postion=postion;
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
		deletBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deletInter.onDelet(postion);
			}
		});
		cancleBtn = (TextView) this.findViewById(R.id.cacle);
		cancleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}

	public   interface DeletInter{
		void onDelet(int position);
	}
}