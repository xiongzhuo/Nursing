package com.deya.hospital.util;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

/*
 * 监听输入内容是否超出最大长度，并设置光标位置
 * */
public class MaxLengthWatcher implements TextWatcher {
	

     

	private int maxLen = 0;
	private EditText editText = null;
	
	
	public MaxLengthWatcher(int maxLen, EditText editText) {
		this.maxLen = maxLen;
		this.editText = editText;
	}

	public void afterTextChanged(Editable arg0) {
		String t=editText.getText().toString();
//		editText.requestFocus();
//		Selection.setSelection(arg0, t.length());
//		editText.setSelection(t.length());
		
		
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
	//	editText.setSelection(editText.getText().length());
	}
	
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		Editable editable = editText.getText();
		int len = editable.length();
		String str = editable.toString();
		String newStr=str;
		int selEndIndex = Selection.getSelectionEnd(editable);
		if(len > maxLen)
		{
			//截取新字符串
			 newStr = str.substring(0,maxLen);
			editText.setText(newStr);
			editable = editText.getText();
			
			//新字符串的长度
			int newLen = editable.length();
			//旧光标位置超过字符串长度
			if(selEndIndex > newLen)
			{
				selEndIndex = editable.length();
			}
			//设置新光标所在的位置
			
		}
		Selection.setSelection(editable, newStr.length());
//		if(null!=this.changeLinstener){
//			this.changeLinstener.setText(editText,newStr);
//		}
	}
	
	
	private TextChangeLinstener changeLinstener;
	public void setOnChange(TextChangeLinstener changeLinstener){
		this.changeLinstener=changeLinstener;
	}
	
	public interface TextChangeLinstener{
		public void setText(EditText editText,String text);
	}

}
