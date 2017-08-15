package com.deya.hospital.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class HospitalShareActivity extends BaseActivity implements View.OnClickListener{

    TextView tellTv;
    Button shareBtn;
    TextView codeTxt;
    TextView expiryDateTxt;
    CommonTopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_hospital);
        initView();
    }

    private void initView() {
        topView= (CommonTopView) this.findViewById(R.id.topView);
        tellTv= (TextView) this.findViewById(R.id.tellTv);
        shareBtn= (Button) this.findViewById(R.id.shareBtn);
        codeTxt= (TextView) this.findViewById(R.id.codeTxt);
        expiryDateTxt= (TextView) this.findViewById(R.id.expiryDateTxt);
        shareBtn.setOnClickListener(this);
        tellTv.setOnClickListener(this);
        topView.init(this);
        codeTxt.setText("授权码:"+tools.getValue(Constants.AUTH_CODE));
        expiryDateTxt.setText("有效期："+tools.getValue(Constants.AUTH_VALID_DATE));
    }


    private void showShare() {
        SHARE_MEDIA[] shareMedia = { SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.WEIXIN_CIRCLE };
        // initCustomPlatforms(shareMedia);
        // showShareContent(shareMedia, shareTitle, shareContent, targetUrl);
        showShareDialog( getString(R.string.app_name), "注册授权码："+tools.getValue(Constants.AUTH_CODE)+",点击下载并安装,感控医务工作者免费使用的移动应用工具",
                "http://studio.gkgzj.com/download");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shareBtn:
                showShare();
                break;
            case R.id.tellTv:
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                            .parse("tel:"
                                    +tellTv.getText().toString()));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mcontext, "该设备不支持通话功能",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
