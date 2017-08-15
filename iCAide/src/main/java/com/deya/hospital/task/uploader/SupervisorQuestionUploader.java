package com.deya.hospital.task.uploader;

import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class SupervisorQuestionUploader extends BaseUploader {
    @Override
    public boolean Upload(TaskVo task) {
        return prepairSendSupervisorTask(task);
    }

    private boolean prepairSendSupervisorTask(TaskVo tavo) {
        String str = tavo.getFileList();
        List<Attachments> supervisorFileList = gson.fromJson(str,
                new TypeToken<List<Attachments>>() {
                }.getType());

        if (null == supervisorFileList) {
            return sendSupervisorTask(tavo, supervisorFileList);
        }
        for (Attachments att : supervisorFileList) {
            att.setImgId(att.getFile_name());
            if (!att.getState().equals("2")) {

                if (!uploadAttachment(att)) {
                    return false;
                }
            }
        }

        return sendSupervisorTask(tavo, supervisorFileList);
    }

    boolean sendSupervisorTask(TaskVo tv, List<Attachments> supervisorFileList) {
        try {
            JSONObject job = new JSONObject();
            try {

                job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
                job.put("check_content", tv.getCheck_content());
                job.put("exist_problem", tv.getExist_problem());
                job.put("deal_suggest", tv.getDeal_suggest());
                job.put("improve_suggest", tv.getImprove_suggest());
                job.put("is_feedback_department", tv.getIs_feedback_department() + "");
                job.put("is_again_supervisor", tv.getIs_again_supervisor() + "");
                job.put("remind_time", tv.getRemind_date() + "");
                job.put("department", tv.getDepartment());
                job.put("create_time", tv.getMission_time());
                job.put("attachments", tv.getFileList());
                job.put("hospital", tv.getHospital());
                job.put("origin", tv.getOrigin());
                if (tv.getId() != -1) {
                    job.put("id", tv.getId());//再次编辑需要传ID
                }


            } catch (Exception e) {
                Log.e("upload", e.getMessage() + "");
                e.printStackTrace();
            }
            if (tv.getTask_id() != 0) {
                job.put("task_id", tv.getTask_id() + "");
            }
            Log.i("111111", tv.getTask_id() + "--------------" + tv.getDbid());

            if (supervisorFileList.size() > 0) {
                String str = gson.toJson(supervisorFileList);
                tv.setFileList(str);
                JSONArray jarr = new JSONArray(str);
                job.put("attachments", jarr);
                DataBaseHelper
                        .getDbUtilsInstance(MyAppliaction.getContext())
                        .update(tv, WhereBuilder.b("dbid", "=", tv.getDbid()));// 防止文件重复上传
            }

            JSONObject map = MainBizImpl.getInstance().sendSyncRequest(
                    "supervisorQuestion/submitQuestion", job);
            if (map != null && map.has("result_id")
                    && map.getString("result_id").equals("0")) {
                JSONObject jsonObject = map.optJSONObject("info");
                setResult(tv,jsonObject);
                return true;
            }

        } catch (Exception e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
            return false;
        }

        return false;

    }

    protected void setResult(TaskVo tv,JSONObject jsonObject) {
            TaskVo tv2 = gson.fromJson(jsonObject.toString(), TaskVo.class);
            tv.setId(tv2.getId());
            tv.setType("7");
            tv.setStatus(0);
            tv.setState(tv2.getState());
            tv.setTask_id(tv2.getId());



    }
}
