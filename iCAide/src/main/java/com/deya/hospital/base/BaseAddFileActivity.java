package com.deya.hospital.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.base.img.PhotoNumsBean;
import com.deya.hospital.base.img.PictureUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.widget.popu.PopuRecord;
import com.deya.hospital.workspace.BootomSelectDialog;
import com.deya.hospital.workspace.TaskUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public abstract class BaseAddFileActivity extends BaseFragmentActivity {
    public MyHandler myHandler;
    String titles2[] = {"拍照", "本地图片"};
    protected BootomSelectDialog bootomSelectDialog;
    // 拍照
    private static final int REQUEST_TAKE_PHOTO = 0x0; // 拍照
    private static final int REQUEST_SELECT_LOCAL_PHOTO = 0x2; // 选择本地图片
    public static final int COMPRESS_IMAGE = 0x17;
    List<String> resullist = new ArrayList<String>();
    PopuRecord popuRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bootomSelectDialog=new BootomSelectDialog(mcontext, titles2, new BootomSelectDialog.BottomDialogInter() {
            @Override
            public void onClick1() {
                takePhoto();
            }

            @Override
            public void onClick2() {
                getloacalimg();
            }

            @Override
            public void onClick3() {

            }

            @Override
            public void onClick4() {

            }
        });

    }

    public void showRecordPopWindow(int parentId,boolean showPoto,boolean showSwitch) {
//        popuRecord= new PopuRecord(mcontext, showPoto,showSwitch,
//                this.findViewById(parentId),
//                new PopuRecord.OnPopuClick() {
//
//                    @Override
//                    public void cancel() {
//                    }
//
//                    @Override
//                    public void enter(String filename, double totalTime) {
//                        AddRecordFile(filename, totalTime);
//                    }
//
//                    @Override
//                    public void photograph(File file, List<String> result) {
//                        // TODO Auto-generated method stub
//                        resullist = result;
//                        Intent takePictureIntent = new Intent(
//                                MediaStore.ACTION_IMAGE_CAPTURE);
//                        takePictureIntent.putExtra("output", Uri.fromFile(file));
//                        startActivityForResult(takePictureIntent,
//                                REQUEST_TAKE_PHOTO);
//                    }
//
//                    @Override
//                    public void album() {
//                        // TODO Auto-generated method stub
//                        Intent intent = new Intent(mcontext,
//                                SelectPhotoActivity.class);
//                        intent.putExtra("size", "0");
//                        PhotoNumsBean.getInstant().setNumber(1);
//                        startActivityForResult(intent,
//                                REQUEST_SELECT_LOCAL_PHOTO);
//                    }
//
//                    @Override
//                    public void record() {
//                        // TODO Auto-generated method stub
//
//                    }
//
//                    @Override
//                    public void dismiss() {
//                    }
//
//                    @Override
//                    public void play() {
//
//                    }
//
//                    @Override
//                    public void lonclick() {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//        popuRecord.setSimpleSwichCheckListener(new SimpleSwitchButton.SimpleSwitchInter() {
//            @Override
//            public void onCheckChange(boolean ischeck) {
//                onCheckAll(ischeck);
//            }
//        });
//        popuRecord.setDefultButtonState(getdefultState());

        bootomSelectDialog.show();
    }

    @Override
    protected void onDestroy() {
        if(null!= bootomSelectDialog){
            bootomSelectDialog.dismiss();
        }
        super.onDestroy();
    }

    public abstract  boolean getdefultState();
    public abstract void onCheckAll(boolean ischeck);

    private void startUploadActivity() {
        Intent data = new Intent();
        data.putExtra("picList", (Serializable) resullist);
        Log.i("111111111", TaskUtils.gson.toJson(resullist) + "");
        CompressImageUtil.getCompressImageUtilInstance().startCompressImage(
                myHandler, COMPRESS_IMAGE, resullist.get(resullist.size()-1));
        setResult(RESULT_OK, data);
    }



    private int size;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(null!=intent&&intent.getAction()==DepartChooseActivity.MUTICHOOSE&&resultCode == DepartChooseActivity.CHOOSE_SUC){
            String   departIds="";

            List<DepartVos.DepartmentListBean> departs = (List<DepartVos.DepartmentListBean>) intent.getSerializableExtra("departs");
            String names = "";
            for (int i = 0; i < departs.size(); i++) {
                DepartVos.DepartmentListBean bean = departs.get(i);

                if (i < departs.size() - 1) {
                    departIds += bean.getId() + ",";
                    names += bean.getName() + ",";
                } else {
                    departIds += bean.getId();
                    names += bean.getName();
                }
            }
            onChooseSuc(names,departIds);
        }else if(null!=intent&&resultCode == DepartChooseActivity.CHOOSE_SUC){
            DepartVos.DepartmentListBean bean= (DepartVos.DepartmentListBean) intent.getSerializableExtra("departData");
            onChooseSuc(bean);
        }else{
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                // 拍照返回结果
                if (resultCode == Activity.RESULT_OK) {
                    startUploadActivity();
                }
                break;
            case REQUEST_SELECT_LOCAL_PHOTO:
                // 选择图片返回结果
                if (intent != null) {
                    size += intent.getIntExtra("size", 0);
                    if (intent.hasExtra("result")) {
                        resullist = (List<String>) intent.getExtras()
                                .getSerializable("result");
                        startUploadActivity();
                    }
                }
                break;
        }
        }
    }


    public abstract void AddImgFile(String name);

    public abstract void AddRecordFile(String name, double totalTime);

    public void takePhoto(){
        try {
            // 指定存放拍摄照片的位置
            File file = createImageFile();
            Intent takePictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra("output", Uri.fromFile(file));
            startActivityForResult(takePictureIntent,
                    REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public void getloacalimg(){
        Intent intent = new Intent(mcontext,
                SelectPhotoActivity.class);
        intent.putExtra("size", "0");
        PhotoNumsBean.getInstant().setNumber(1);
        startActivityForResult(intent,
                REQUEST_SELECT_LOCAL_PHOTO);

    }
    private static String mCurrentPhotoPath;
    /**
     * createImageFile:【把程序拍摄的照片放到 SD卡的 Pictures目录中 sheguantong 文件夹中
     * 照片的命名规则为：sheqing_20130125_173729.jpg】. <br/>
     * .@return .@throws IOException.<br/>
     */

    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = format.format(new Date());
        String imageFileName = "activity_" + timeStamp + ".jpg";

        File image = new File(PictureUtil.getAlbumDir(), imageFileName);
        mCurrentPhotoPath = image.getAbsolutePath();
        resullist.add(mCurrentPhotoPath);
        return image;
    }


    /**
     * 单选科室
     */
    public void onChooseDepart(){
        Intent intent=new Intent(mcontext, DepartChooseActivity.class);
        startActivityForResult(intent,DepartChooseActivity.CHOOSE_SUC);
    }

    /**
     * 多选科室
     */
    public void onMutiChoose(){
        Intent intent = new Intent(mcontext, DepartChooseActivity.class);
        intent.putExtra(DepartChooseActivity.MUTICHOOSE, 1);
        startActivityForResult(intent, DepartChooseActivity.CHOOSE_SUC);
    }
    protected   void onChooseSuc(DepartVos.DepartmentListBean bean){

    }
    protected   void onChooseSuc(String names,String ids){

    }
}