package com.deya.hospital.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MyTextWatcher implements TextWatcher {

	private EditText editText = null;
	private int positon;
	
	public MyTextWatcher() {
		
	}

	public void afterTextChanged(Editable arg0) {
			String text=arg0.toString();
			changeLinstener.setText(text, positon);
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
	//	editText.setSelection(editText.getText().length());
	}
	
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}
	
	
	private TextChangeLinstener changeLinstener;
	public void setOnChange(TextChangeLinstener changeLinstener){
		this.changeLinstener=changeLinstener;
	}
	
	public void setData(int positon){
		this.positon=positon;
	}
	
	public int position(){
		return this.positon;
	}
	
	public interface TextChangeLinstener{
		public void setText(String text,int index);
	}

}
