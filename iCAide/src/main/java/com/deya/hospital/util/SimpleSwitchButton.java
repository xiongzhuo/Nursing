package com.deya.hospital.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;


/**
 * @author sunp
 */
public class SimpleSwitchButton extends LinearLayout {

    LinearLayout view;
    ImageView img;
    boolean isCheck;
    TextView text,text2;
    SimpleSwitchInter inter;

    public SimpleSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.switch_button_img,
                this, true);
        text2 = (TextView) view.findViewById(R.id.text2);

        img = (ImageView) view.findViewById(R.id.imgView);
        text = (TextView) view.findViewById(R.id.text);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SimpleSwitchButton);
        CharSequence tittle = a.getText(R.styleable.SimpleSwitchButton_android_text);
        if (text != null){
            text.setText(tittle);
        }


        isCheck  = a.getBoolean(R.styleable.SimpleSwitchButton_isOpen,
                true);
        img.setImageResource(isCheck?R.drawable.dynamic_but2:R.drawable.dynamic_but1);
        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheck = !isCheck;
                img.setImageResource(isCheck ? R.drawable.dynamic_but2
                        : R.drawable.dynamic_but1);
                if (null != inter) {
                    inter.onCheckChange(isCheck);
                }
            }
        });
        a.recycle();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        img.setOnClickListener(null);
    }

    public void setCheck(boolean ischek){

        if(this.isCheck==ischek){
            return;
        }
       this.isCheck = ischek;
        img.setImageResource(isCheck ? R.drawable.dynamic_but2
                : R.drawable.dynamic_but1);
        if(null!=inter){
            inter.onCheckChange(isCheck);
        }
    }
    public void  setOncheckChangeListener(SimpleSwitchInter inter){
        this.inter=inter;

    }
    public  void setText(String str){
        text.setText(str);
    }
    public  void setText2(String str){
        text2.setVisibility(VISIBLE);
        text2.setText(str);
    }
    public boolean getCheckState() {
        return isCheck;
    }


    public interface SimpleSwitchInter {
        public abstract void onCheckChange(boolean ischeck);

    }

}
