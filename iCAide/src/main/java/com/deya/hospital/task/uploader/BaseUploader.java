package com.deya.hospital.task.uploader;

import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.UploadBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.google.gson.Gson;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseUploader {
    Gson gson = new Gson();

    public static BaseUploader getUploader(int type) {
        switch (type) {
            case 1:
                return new HandTaskUploader();
            case 2:
                return new SupervisorNoteUploader();
            case 4:
                return new ConsumptionUploder();
            case 3:
            case 5:
            case 6:
                return new FormUploader();
            case 7:
                return new SupervisorQuestionUploader();
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                return new CaseFormUpload2();

            case 16:
            case 17:
            case 21:
                return new CaseFormUpload();
        }
        return null;
    }

    public static BaseUploader getUploader(TaskVo taskVo) {
        switch (taskVo.getType()) {
            case "1":
                return new HandTaskUploader();
            case "2":
                return new SupervisorNoteUploader();
            case "4":
                return new ConsumptionUploder();
            case "3":
                if (taskVo.getIs_temp_task() == 1) {
                    return new CaseFormUpload();
                }
            case "5":
            case "6":
                return new FormUploader();
            case "7":
                return new SupervisorQuestionUploader();
            case "8":
            case "9":
            case "10":
            case "11":
            case "12":
            case "13":
            case "14":
                return new CaseFormUpload2();
            case "16":

            case "17":
            case "21":
                return new CaseFormUpload();
        }
        return null;
    }

    boolean uploadAttachment(Attachments att) {
        String remoteFileName = null;
        try {
            JSONObject map = UploadBizImpl.getInstance().syncUploadPicture(att.getFile_name());
            if (null != map && map.has("data")) {
                remoteFileName = map.getString("data");
            }
        } catch (Exception e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }

        if (remoteFileName == null) {
            return false;
        } else {
            att.setFile_name(remoteFileName);
            att.setState("2");
            att.setDate("");
            att.setImgId("");
            return true;
        }
    }

    void uploadSucessed(TaskVo task, String... columns) throws DbException {
        Log.i("upload", task.getDbid() + "----------");
        task.setStatus(0);
        DataBaseHelper
                .getDbUtilsInstance(MyAppliaction.getContext())
                .update(task, WhereBuilder.b("dbid", "=", task.getDbid()), columns);
        TaskVo tv = DataBaseHelper
                .getDbUtilsInstance(MyAppliaction.getContext()).findById(TaskVo.class, task.getDbid());
        Log.i("upload", task.getDbid() + "----------" + tv.getStatus());
        addScore("2");
    }


    public void addScore(String id) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
            job.put("aid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject=MainBizImpl.getInstance().sendSyncRequest("goods/actionGetIntegral", job);

        int score=jsonObject.optInt("integral");
        String str=MyAppliaction.getTools().getValue(Constants.INTEGRAL);
        if(null!=str){
            MyAppliaction.getTools().putValue(Constants.INTEGRAL, Integer.parseInt(str)+score+"");
        }else{
            MyAppliaction.getTools().putValue(Constants.INTEGRAL,score+"");
        }
    }

    public abstract boolean Upload(TaskVo task);
}
