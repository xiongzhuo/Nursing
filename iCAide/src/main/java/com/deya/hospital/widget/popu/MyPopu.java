package com.deya.hospital.widget.popu;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.RoundProgressBar;
import com.deya.hospital.util.SimpleSwitchButton;
import com.deya.hospital.widget.view.MultipleTextViewGroup;
import com.deya.hospital.widget.view.RadioGroupTextView;

/**
 * PopupWindow 基类
* @author  yung
* @date 创建时间：2016年1月15日 上午11:53:34 
* @version 1.0
 */
public class MyPopu {
	private PopupWindow popupWindow = null;
	
	/**
	 * ͼ
	 */
	private View contentView = null;

	/**
	 * 
	 * @param ctx
	 *            
	 * @param width
	 *            
	 * @param height
	 *            
	 * @param resource
	 *            ID
	 */
	public MyPopu(Context ctx, int width, int height, int resource) {
		contentView = LayoutInflater.from(ctx).inflate(resource, null);
//		contentView.getBackground().setAlpha(100);
		this.popupWindow = new PopupWindow(contentView, width, height);
		this.popupWindow.setContentView(contentView);
		this.popupWindow.setFocusable(true);
		this.popupWindow.setOutsideTouchable(true);
		//popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		//NXInstance.getInstance().AddPopu(this);
		this.popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				if(null!=back){
					back.onDismiss();
				}
				DebugUtil.debug("MyPopu", "dismiss");
			}
		});
		
	}
	
	public void showSoftInput(Activity _activity){
		InputMethodManager imm = (InputMethodManager) _activity.getSystemService(Service.INPUT_METHOD_SERVICE);  
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
	}
	public void setOutsideTouchable(boolean touchable){
		popupWindow.setOutsideTouchable(touchable);
		popupWindow.setFocusable(touchable);

	}
	private PopuIBack back;
	
	public void setPopuIBack(PopuIBack back){
		this.back=back;
	}
	
	public interface PopuIBack{
		public void onDismiss();
	}
	
	public void setFocusable(boolean b){
		popupWindow.setFocusable(b);//
	}
	
	
	public void setAlpha(int alpha){
		contentView.getBackground().setAlpha(alpha);
	}

	/**
	 * ر
	 */
	public void dismiss() {
		//NXInstance.getInstance().RemovePopu(this);
		if (null != this.popupWindow) {
			try{
			this.popupWindow.dismiss();
			}catch(Exception e){
				
			}
			this.popupWindow=null;
			
		}
	}

	public void showAtLocation(View view, int gravity, int x, int y) {
		try {
			
			this.popupWindow.showAtLocation(view, gravity, x, y);
		} catch (Exception e) {
			e.getMessage();
			// TODO: handle exception
		}
	}
	
	public void showAsDropDown(View view) {
		try {
			
			this.popupWindow.showAsDropDown(view);
		} catch (Exception e) {
			e.getMessage();
			// TODO: handle exception
		}
	}

	/**
	 * ť
	 * 
	 * @param paramInt
	 * @return
	 */
	public Button getButton(int paramInt) {
		Button button = (Button) this.contentView.findViewById(paramInt);
		return button;
	}

	public EditText getEditText(int paramInt) {

		return (EditText) this.contentView.findViewById(paramInt);
	}

	public ImageView getImageView(int paramInt) {
		return (ImageView) this.contentView.findViewById(paramInt);
	}

	public DatePicker getDatePicker(int paramInt)
	{
		return (DatePicker) this.contentView.findViewById(paramInt);
	}
	public LinearLayout getLinearLayout(int paramInt) {
		return (LinearLayout) this.contentView.findViewById(paramInt);
	}

	public ProgressBar getProgressBar(int paramInt)
	{
		return (ProgressBar) this.contentView.findViewById(paramInt);
	}

	
	

	public ListView getListView(int paramInt) {
		return (ListView) this.contentView.findViewById(paramInt);
	}

	public RelativeLayout getRelativeLayout(int paramInt) {
		return (RelativeLayout) this.contentView.findViewById(paramInt);
	}

	public TextView getTextView(int paramInt) {
		return (TextView) this.contentView.findViewById(paramInt);
	}
	public SimpleSwitchButton getSwitchBtn(int paramInt) {
		return (SimpleSwitchButton) this.contentView.findViewById(paramInt);
	}
	public GridView getGridView(int paramInt) {
		return (GridView) this.contentView.findViewById(paramInt);
	}
	
	public View getView(int paramInt){
		return (View)this.contentView.findViewById(paramInt);
	}
	
	public Spinner getSpinner(int paramInt){
		return (Spinner)this.contentView.findViewById(paramInt);
	}
	
	public CheckBox getCheckBox(int paramInt){
		return (CheckBox)this.contentView.findViewById(paramInt);
	}
	
	public RadioButton getRadioButton(int paramInt){
		return (RadioButton)this.contentView.findViewById(paramInt);
	}
	public RoundProgressBar getRoundProgressBar(int paramInt) {
		return (RoundProgressBar)this.contentView.findViewById(paramInt);
	}
	
	public MultipleTextViewGroup getMultipleTextViewGroup(int paramInt) {
		return (MultipleTextViewGroup)this.contentView.findViewById(paramInt);
	}

	public RadioGroupTextView getRadioGroupTextView(int paramInt) {
		// TODO Auto-generated method stub
		return (RadioGroupTextView)this.contentView.findViewById(paramInt);
	}
	
	
	
}
