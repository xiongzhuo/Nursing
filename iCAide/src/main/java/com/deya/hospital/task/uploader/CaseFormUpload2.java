package com.deya.hospital.task.uploader;

import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.CaseListVo2;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.priviewbase.CaseUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class CaseFormUpload2 extends BaseUploader {
    @Override
    public boolean Upload(TaskVo task) {
        return UploadFile(task);
    }

    private boolean UploadFile(TaskVo task) {
        TaskVo tv = gson.fromJson(task.getMain_remark(), TaskVo.class);
        List<Attachments> supervisorFileList = TaskUtils.gson.fromJson(tv.getFileList(),
                new TypeToken<List<Attachments>>() {
                }.getType());
        if (null!=supervisorFileList) {
            for (Attachments att : supervisorFileList) {
                att.setImgId(att.getFile_name());
                if (!att.getState().equals("2")) {
                    if (!uploadAttachment(att)) {
                          return false;
                    }
                }
            }
            tv.setFileList(TaskUtils.gson.toJson(supervisorFileList));
            tv.setAttachments(supervisorFileList);
            task.setMain_remark(gson.toJson(tv));
        }
        return UploadTask(task);
    }

    private boolean UploadTask(TaskVo taskVo) {
        JSONObject job = new JSONObject();
        CaseListVo2.CaseListBean businessBean = gson.fromJson(taskVo.getTaskListBean(), CaseListVo2.CaseListBean.class);
        try {
            job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
            job.put("task_id", taskVo.getTask_id());
            job.put("temp_type_id", businessBean.getTemp_type_id());
            job.put("hospital_id", MyAppliaction.getConstantValue(Constants.HOSPITAL_ID));
            if (AbStrUtil.isEmpty(taskVo.getDepartment())) {
                job.put("department_id", businessBean.getDepartment_id());
                taskVo.setDepartment(businessBean.getDepartment_id());
                taskVo.setDepartmentName(taskVo.getDepartmentName());
            } else {
                job.put("department_id", taskVo.getDepartment());

            }
            job.put("mission_time", businessBean.getTime());
            int type = Integer.parseInt(taskVo.getType());
            setChildJson(type, job);
            JSONObject json = new JSONObject(TaskUtils.gson.toJson(businessBean));

            job.put("case_info", json);
            if (businessBean.getCase_id() > 0) {
                job.put("case_id", businessBean.getCase_id());
            }
            JSONObject supervisorJs = new JSONObject(taskVo.getMain_remark());
            job.put("supervisor_question", supervisorJs);

            JSONArray jarr = json.optJSONArray("resultlist");
            Log.e("jarr---------",jarr.length()+"");
            job.put("temp_result", jarr);
            JSONObject map = MainBizImpl.getInstance().sendSyncRequest("tempTask/submitTask", job);
            if (null!=map&&map.has("result_id") && map.optString("result_id").equals("0")) {
                taskVo.setTask_id(map.getInt("task_id"));
                taskVo.setStatus(0);
//                if (taskVo.getDbid() > 0) {
//                    TaskUtils.onUpdateTaskById(taskVo);
//                } else {
//                    TaskUtils.onAddTaskInDb(taskVo);
//                }
                if (businessBean.getDbid()> 0) {
                    CaseUtil.removeLoacalCase(businessBean.getDbid());
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private void setChildJson(int type, JSONObject job) {
        try {
            switch (type) {
                case 8:
                    job.put("temp_type_id", "1");
                    break;
                case 9:
                    job.put("temp_type_id", "2");
                    break;
                case 10:
                    job.put("temp_type_id", "3");
                    break;
                case 11:
                    job.put("temp_type_id", "5");
                    break;
                case 12:
                    job.put("temp_type_id", "4");
                    break;
                case 13:
                    job.put("temp_type_id", "6");
                    break;
                case 14:
                    job.put("temp_type_id", "7");
                    break;
                case 3:
                    job.put("temp_type_id", "8");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
