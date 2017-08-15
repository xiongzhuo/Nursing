package com.deya.hospital.workspace.handwash;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;


public class EndDialog extends Dialog {

    private TextView showBtn;
    private TextView btn_enter;
    private TextView cancleBtn;
    View.OnClickListener listener;
    LinearLayout framView, guidView;
    FinishInter finishInter;

    /**
     * Creates a new instance of MyDialog.
     */
    public EndDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 自动生成的方法存根
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_enddialog);
        setCancelable(false);
        showBtn = (TextView) this.findViewById(R.id.show);
        showBtn.setText("您的督导已达设定值，是否提交？");
        btn_enter = (TextView) this.findViewById(R.id.yes);
        btn_enter.setText("提交");
        btn_enter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                finishInter.onEnter();
            }
        });
        framView = (LinearLayout) this.findViewById(R.id.framView);
        guidView = (LinearLayout) this.findViewById(R.id.guidView);
        cancleBtn = (TextView) this.findViewById(R.id.cacle);
        framView.setVisibility(View.GONE);
        guidView.setVisibility(View.GONE);
        cancleBtn.setText("继续");

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                dismiss();
                finishInter.onCancle();
            }
        });

    }

    public void setClickInter(FinishInter finishInter){
        this.finishInter=finishInter;
    }

    public void setTitleTxt(String s) {
        showBtn.setText(s);
    }

    public interface FinishInter{
        abstract void onEnter();
        abstract  void onCancle();
    }
}
