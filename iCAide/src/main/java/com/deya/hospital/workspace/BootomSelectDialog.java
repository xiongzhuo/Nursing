package com.deya.hospital.workspace;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseDialog;

public class BootomSelectDialog extends BaseDialog implements
		android.view.View.OnClickListener {
	String[] titles;
	TextView textView1, textView2, textView3,textView4, cancleTv;
	BottomDialogInter inter;
	View view_1,view_2,view_3;

	public BootomSelectDialog(Context context, String[] titles,BottomDialogInter inter) {
		super(context);
		this.titles = titles;
		this.inter=inter;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottom_pop_dialog);
		initView();
	}

	private void initView() {
		textView1 = (TextView) this.findViewById(R.id.textView1);

		textView2 = (TextView) this.findViewById(R.id.textView_2);

		textView3 = (TextView) this.findViewById(R.id.textView_3);
		textView4 = (TextView) this.findViewById(R.id.textView_4);
		cancleTv=(TextView) this.findViewById(R.id.cancleTv);
		textView1.setOnClickListener(this);
		textView2.setOnClickListener(this);
		textView3.setOnClickListener(this);
		cancleTv.setOnClickListener(this);
		textView4.setOnClickListener(this);
		view_2=this.findViewById(R.id.view_2);
		view_1=this.findViewById(R.id.view_1);
		view_3=this.findViewById(R.id.view_3);
		if (titles.length < 2) {
			textView1.setText(titles[0]);
			textView2.setVisibility(View.GONE);
			textView3.setVisibility(View.GONE);
			textView4.setVisibility(View.GONE);
			view_2.setVisibility(View.GONE);
			view_1.setVisibility(View.GONE);
			view_3.setVisibility(View.GONE);
		} else if (titles.length < 3) {
			textView1.setText(titles[0]);
			textView2.setText(titles[1]);
			textView3.setVisibility(View.GONE);
			textView4.setVisibility(View.GONE);
			view_2.setVisibility(View.GONE);
			view_3.setVisibility(View.GONE);
		} else if (titles.length < 4){
			textView1.setText(titles[0]);
			textView2.setText(titles[1]);
			textView3.setText(titles[2]);
			textView4.setVisibility(View.GONE);
			view_3.setVisibility(View.GONE);
		}else{
			textView1.setText(titles[0]);
			textView2.setText(titles[1]);
			textView3.setText(titles[2]);
			textView4.setText(titles[3]);
		}

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		dialogWindow.setAttributes(lp);

	}

	public interface BottomDialogInter {
		public void onClick1();

		public void onClick2();

		public void onClick3();
		public void onClick4();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView1:
			inter.onClick1();
			dismiss();
			break;
		case R.id.textView_2:
			inter.onClick2();
			dismiss();
			break;
		case R.id.textView_3:
			inter.onClick3();
			dismiss();
			break;
		case R.id.textView_4:
			inter.onClick4();
			dismiss();
			break;
		case R.id.cancleTv:
			dismiss();
			break;

		default:
			break;
		}

	}

}
