package com.im.sdk.dy.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.vo.ShareVo;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/15
 */
public class DyShareDialog extends BaseDialog implements View.OnClickListener{
    TextView shareTitleTv;
    TextView shareContentTv;
    TextView shareMan;
    ShareVo shareVo;
    TextView confirm,cancle;
    DyShareListener dyShareLis;
    /**
     * Creates a new instance of MyDialog.
     *
     * @param context
     */
    public DyShareDialog(Context context, ShareVo shareVo,DyShareListener dyShareLis) {
        super(context);
        this.shareVo=shareVo;
        this.dyShareLis=dyShareLis;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dy_share_dilog);
        initView();
    }

    private void initView() {
        shareTitleTv= (TextView) this.findViewById(R.id.title);
        shareContentTv= (TextView) this.findViewById(R.id.content);
        shareMan= (TextView) this.findViewById(R.id.shareMan);
        confirm= (TextView) this.findViewById(R.id.confirm);
        cancle= (TextView) this.findViewById(R.id.cancle);
        confirm.setOnClickListener(this);
        cancle.setOnClickListener(this);
        shareTitleTv.setText(shareVo.getShareTitle());
        shareContentTv.setText(shareVo.getShareContent());
        shareMan.setText("分享给: "+shareVo.getShareMan());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm:

                dyShareLis.onShareSucess();
                dismiss();
                break;
            case R.id.cancle:

                dyShareLis.onShareCancle();
                dismiss();
                break;
            default:
                break;
        }

    }
    public interface DyShareListener{
        abstract  void onShareSucess();
        abstract  void onShareCancle();
    }
}
