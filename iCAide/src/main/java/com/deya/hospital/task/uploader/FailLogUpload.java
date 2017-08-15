package com.deya.hospital.task.uploader;

import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;

import org.json.JSONObject;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/7
 */
public class FailLogUpload {

    public static FailLogUpload getUpload() {
        return new FailLogUpload();
    }

    public boolean upload(String string) {

        return sendLog(string);
    }

    private boolean sendLog(String string) {
        JSONObject job = new JSONObject();

        try {
            job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
            job.put("logs",string);
            String pkName = MyAppliaction.getContext().getPackageName();
            String versionName = MyAppliaction.getContext().getPackageManager()
                    .getPackageInfo(pkName, 0).versionName;
            JSONObject clientInfoJson = new JSONObject();
            clientInfoJson.put("soft_ver", versionName);
            clientInfoJson.put("device_model", android.os.Build.MODEL + "");
            clientInfoJson.put("os_ver", android.os.Build.VERSION.RELEASE + "");
            clientInfoJson.put("device_type", "2");
            job.put("client_info", clientInfoJson);
            JSONObject map = MainBizImpl.getInstance().sendSyncRequest("comm/submitLogs", job);
            if (map.has("result_id") && map.getString("result_id").equals("0")) {
                return true;
            }

        } catch (Exception e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }
        return false;
    }
}
