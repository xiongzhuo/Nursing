package com.deya.hospital.workspace;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;
import com.deya.hospital.widget.popu.MyPopu;

public class AddTaskPopWindow extends BaseDialog implements OnClickListener {
	View view;

	public AddTaskPopWindow(Context context, View view, OnPopuClick onPopuClick) {
		super(context);
		this.ctx = context;
		tools = new Tools(ctx, Constants.AC);
		this.onPopuClick = onPopuClick;
		// myPopu = new MyPopu(ctx, LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT, R.layout.popwindow_addtask);

		this.onPopuClick = onPopuClick;
		this.view = view;
		// myPopu.setAlpha(200);
		// myPopu.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popwindow_addtask);
		btn_cancel = (TextView) this.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);

		handhygieneTv = (TextView) this.findViewById(R.id.Handhygiene);
		reviewForm = (TextView) this.findViewById(R.id.reviewForm);
		reviewForm.setOnClickListener(this);
		whoTv = (TextView) this.findViewById(R.id.whoTv);
		whoTv.setOnClickListener(this);

		totallyTaskTv = (TextView) this.findViewById(R.id.totallyTaskTv);
		totallyTaskTv.setOnClickListener(this);

		handhygieneTv.setOnClickListener(this);
		SupervisionTv = (TextView) this.findViewById(R.id.SupervisionTv);
		SupervisionTv.setOnClickListener(this);

		lay1 = (LinearLayout) this.findViewById(R.id.lay1);
		lay1.setOnClickListener(this);

		consumptionTv = (TextView) this.findViewById(R.id.consumptionTv);
		consumptionTv.setOnClickListener(this);

		surgicalFromTv = (TextView) this.findViewById(R.id.surgicalFromTv);
		surgicalFromTv.setOnClickListener(this);
		
		xiangyaFromTv=(TextView) this.findViewById(R.id.xiangyaFromTv);
		xiangyaFromTv.setOnClickListener(this);
		// showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
		/** 新手指引 */
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		dialogWindow.setAttributes(lp);
		calendarOperationHint();
	}

	private MyPopu myPopu = null;

	private EditText edt_email_res;
	private TextView btn_cancel, handhygieneTv, SupervisionTv, reviewForm,
			whoTv, totallyTaskTv, consumptionTv, surgicalFromTv,xiangyaFromTv;
	private LinearLayout lay1;
	private OnPopuClick onPopuClick;
	private Context ctx;
	private LinearLayout calendar_opration_hint_up,
			calendar_opration_hint_down, calendar_opration_hint_middle;
	private Tools tools;

	/** 新手指引 */
	private void calendarOperationHint() {
		// TODO Auto-generated method stub
		calendar_opration_hint_up = (LinearLayout) this
				.findViewById(R.id.calendar_opration_hint_up);
		calendar_opration_hint_down = (LinearLayout) this
				.findViewById(R.id.calendar_opration_hint_down);
		calendar_opration_hint_middle = (LinearLayout) this
				.findViewById(R.id.calendar_opration_hint_middle);
//		if (0 == tools.getValue_int("pop_hint")) {
//			calendar_opration_hint_up.setVisibility(View.VISIBLE);
//			calendar_opration_hint_down.setVisibility(View.VISIBLE);
//			calendar_opration_hint_middle.setVisibility(View.VISIBLE);
//		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
		case R.id.lay1:
				dismiss();
				onPopuClick.cancel();
			break;
		case R.id.Handhygiene:// 手卫生按钮
				onPopuClick.handhygiene();
				dismiss();
			break;
		case R.id.SupervisionTv:// 督导本
				onPopuClick.supervision();
				dismiss();
			break;
		case R.id.reviewForm:
				onPopuClick.reviewForm();
				dismiss();
			break;
		case R.id.whoTv:
			onPopuClick.DowhoTask();
			dismiss();
			break;
		case R.id.totallyTaskTv:
				onPopuClick.doToatalTask();
				dismiss();
			break;
		case R.id.consumptionTv:
				onPopuClick.onConsumption();
				dismiss();
				break;
		case R.id.surgicalFromTv:
				onPopuClick.surgicalFrom();
				dismiss();
			break;
		case R.id.xiangyaFromTv:
				onPopuClick.xiangyaFrom();
				dismiss();
		default:
			break;
		}

	}

	public interface OnPopuClick {
		public void handhygiene();

		public void supervision();

		public void reviewForm();

		public void DowhoTask();

		public void cancel();

		public void doToatalTask();

		public void onConsumption();

		public void surgicalFrom();
		public void xiangyaFrom();
	}

}
