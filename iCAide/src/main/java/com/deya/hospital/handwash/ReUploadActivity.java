package com.deya.hospital.handwash;

import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseCommonTopActivity;
import com.deya.hospital.task.Tasker;
import com.deya.hospital.task.Worker;
import com.deya.hospital.task.notify.WorkerFinishedNotify;
import com.deya.hospital.vo.dbdata.TaskVo;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/10
 */
public class ReUploadActivity extends BaseCommonTopActivity implements WorkerFinishedNotify {
    TextView contentTxt;
    LinearLayout hasTaskLay,notHaveTaskLay;
    Button conmitBtn;
    private Worker worker;

    @Override
    public String getTopTitle() {
        return "未成功上传";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_fail;
    }

    @Override
    public void initBaseData() {

    }

    @Override
    public void initView() {
        contentTxt=findView(R.id.contentTxt);
        hasTaskLay=findView(R.id.hasFailTask);
        notHaveTaskLay=findView(R.id.haveNotTask);
        contentTxt.setText(getText("1"));
        conmitBtn=findView(R.id.conmitBtn);
        conmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUncacleBleProcessdialog();
                worker = new Worker(ReUploadActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    worker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    worker.execute();
                }
            }
        });
    }

    private  String getText(String type){
        String str="";
        switch (type){
            case "1":
                str=  "WHO手卫生模板\n";
                break;
            case "2":
                str=  "消耗量模板";
                break;
        }
        if(Tasker.getFailUploadTaskByType(type).size()>0){
            hasTaskLay.setVisibility(View.VISIBLE);
            notHaveTaskLay.setVisibility(View.GONE);
        }
        return getContenText(str, Tasker.getFailUploadTaskByType(type).size()+"");
    }

   String getContenText(String text,String num){
        return text+"有"+num+"个手卫生时机未上传";
    }

    @Override
    public void Finshed(TaskVo task) {

    }

    @Override
    public void RefreshNetwork() {

    }

    @Override
    public void workFinish() {
        contentTxt.setText(getText("1"));
        dismissdialog();
        if (null != worker) {
            worker.cancel(true);
            worker = null;
        }
    }
}
