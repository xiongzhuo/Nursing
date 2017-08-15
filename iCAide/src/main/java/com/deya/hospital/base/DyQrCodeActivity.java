package com.deya.hospital.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.deya.acaide.R;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.CommonUtils;
import com.deya.hospital.util.QRCodeUtil;
import com.im.sdk.dy.common.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/7/18
 */
public class DyQrCodeActivity extends BaseActivity implements View.OnClickListener{
    LinearLayout qrFram;
    ImageView qrCodeImg;

    /**
     * Creates a new instance of MyDialog.
     *
     * @param context
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.qr_code_lay);
        CommonTopView topView=findView(R.id.topView);
        topView.init(this);
        qrCodeImg=findView(R.id.qrCodeImg);
        qrFram=findView(R.id.qrFram);
        qrFram.setOnClickListener(this);
        int  wi []=AbViewUtil.getDeviceWH(mcontext);
        Resources r=getResources();
        Bitmap bmp= BitmapFactory.decodeResource(r, R.drawable.share_logo);
        qrCodeImg.setImageBitmap(  QRCodeUtil.createQRCodeWithLogo5(getIntent().getStringExtra("content"),wi[0]*4/5 ,bmp));

    }

    @Override
    public void onClick(View v) {

        final String[] mItems =
                {CommonUtils.getString(R.string.common_image_save),
                        CommonUtils.getString(R.string.photo_folder_choice_cancle)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(CommonUtils.getString(R.string.common_image_operator));
        builder.setItems(mItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击后弹出窗口选择了第几项
                switch (which) {
                    case 0:
                        dialog.dismiss();
                        int  wi []=AbViewUtil.getDeviceWH(mcontext);
                        Resources r=getResources();
                        Bitmap bmp= BitmapFactory.decodeResource(r, R.drawable.share_logo);
                        saveImage(QRCodeUtil.createQRCodeWithLogo5(getIntent().getStringExtra("content"),wi[0]*4/5 ,bmp));
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();

    }

    public  void saveImage( Bitmap bmp) {
        // 首先保存图片
        File appDir = getSDPath();
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ToastUtil.showMessage("保存成功");
        // 最后通知图库更新

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
    }

    private static File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {

            sdDir = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/DeyaCache/");
            if (!sdDir.exists()) {
                sdDir.mkdirs();
                Log.i("1111", sdDir + "不存在");
            }
            Log.i("1111", sdDir + "");
        }
        return sdDir;
    }
}
