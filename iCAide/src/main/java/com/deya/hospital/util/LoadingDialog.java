package com.deya.hospital.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.deya.acaide.R;


public class LoadingDialog extends Dialog{

	private ImageView dialog_img;
	private Animation animation;

	public LoadingDialog(Context context) {
		super(context);
	}
	
    public LoadingDialog(Context context, int theme, boolean cancelable) {  
        super(context, theme); 
        this.setCancelable(cancelable);
    }  
  
    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {  
        super(context, cancelable, cancelListener);  
    } 
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        
        this.setContentView(R.layout.loading_dialog);
		dialog_img = (ImageView) this.findViewById(R.id.img);
		// ���ض���
		animation = AnimationUtils.loadAnimation(getContext(), R.anim.book_rotate_loading);		
		//TextView tipTextView = (TextView) this.findViewById(R.id.dialog_txt);// ��ʾ����
		//tipTextView.setText("");// ���ü�����Ϣ
		
    } 
    
    @Override
    public void show() {
    	super.show();
    	dialog_img.startAnimation(animation);
    }
    
    @Override
    public void cancel() {
    	super.cancel();
    	if(animation!=null){
    		animation.cancel();
    	}
    	
    }

}