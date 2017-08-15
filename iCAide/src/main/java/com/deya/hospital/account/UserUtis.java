package com.deya.hospital.account;

import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;

import org.json.JSONObject;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class UserUtis {
    public static void setEditorRes(Tools tools,JSONObject job) {
        if (AbStrUtil.isEmpty(job.optString("id"))) {
            tools.putValue(Constants.USER_ID, "");
        } else {
            tools.putValue(Constants.USER_ID, job.optString("id"));
        }
        if (AbStrUtil.isEmpty(job.optString("regis_job_name"))) {
            tools.putValue(Constants.JOB_NAME, "");
        } else {
            tools.putValue(Constants.JOB_NAME, job.optString("regis_job_name"));
        }
        if (AbStrUtil.isEmpty(job.optString("regis_job"))) {
            tools.putValue(Constants.JOB, "");
        } else {
            tools.putValue(Constants.JOB, job.optString("regis_job"));
        }
        if (!AbStrUtil.isEmpty(job.optString("username"))) {
            tools.putValue(Constants.USER_NAME,
                    job.optString("username"));
        } else {
            tools.putValue(Constants.USER_NAME, job.optString("mobile"));
        }
        if (!AbStrUtil.isEmpty(job.optString("a_note"))) {
            tools.putValue(Constants.A_NOTE,
                    job.optString("a_note"));
        } else {
            tools.putValue(Constants.A_NOTE, job.optString(""));
        }
        if (AbStrUtil.isEmpty(job.optString("email"))) {
            tools.putValue(Constants.EMAIL, job.optString(""));
        } else {
            tools.putValue(Constants.EMAIL, job.optString("email"));
        }
        if(AbStrUtil.isEmpty(job.optString("auth_code"))){
            tools.putValue(Constants.AUTH_CODE, "");
        }else{
            tools.putValue(Constants.AUTH_CODE, job.optString("auth_code") + "");
        }
        if(AbStrUtil.isEmpty(job.optString("auth_valid_date"))){
            tools.putValue(Constants.AUTH_VALID_DATE, "");
        }else{
            tools.putValue(Constants.AUTH_VALID_DATE, job.optString("auth_valid_date") + "");
        }

        if (AbStrUtil.isEmpty(job.optString("sex"))) {
            tools.putValue(Constants.SEX, "");
        } else {
            tools.putValue(Constants.SEX, job.optString("sex"));
        }
        tools.putValue(Constants.STATE, job.optString("state"));
        if (!AbStrUtil.isEmpty(job.optString("is_sign_old"))) {
            tools.putValue(Constants.IS_SIGN_OLD, job.optString("is_sign_old") + "");
        }
        if (AbStrUtil.isEmpty(job.optString("hospital_name"))) {
            tools.putValue(Constants.HOSPITAL_NAME, "");
        } else {
            tools.putValue(Constants.HOSPITAL_NAME,
                    job.optString("hospital_name"));
        }
        if(AbStrUtil.isEmpty(job.optString("name"))){
            tools.putValue(Constants.NAME, "");
        }else{
            tools.putValue(Constants.NAME, job.optString("name"));
        }
        if (AbStrUtil.isEmpty(job.optString("hospital"))) {
            tools.putValue(Constants.HOSPITAL_ID, "");
        } else {
            tools.putValue(Constants.HOSPITAL_ID,
                    job.optString("hospital"));
        }
        if (AbStrUtil.isEmpty(job.optString("avatar"))) {
            tools.putValue(Constants.HEAD_PIC, "");
        } else {
            tools.putValue(Constants.HEAD_PIC, job.optString("avatar"));
        }
        if (AbStrUtil.isEmpty(job.optString("auth_name"))) {
            tools.putValue(Constants.AUTH_NAME, "");
        } else {
            tools.putValue(Constants.HEAD_PIC, job.optString("auth_name"));
        }
        String departmentId = job.optString("department");
        if (!AbStrUtil.isEmpty(departmentId)) {
            tools.putValue(Constants.DEFULT_DEPARTID, job.optString("department"));
        } else {
            tools.putValue(Constants.DEFULT_DEPARTID, "");
        }

        if (!AbStrUtil.isEmpty(job.optString("departmentName"))) {
            tools.putValue(Constants.DEFULT_DEPART_NAME, job.optString("departmentName"));
        } else {
            tools.putValue(Constants.DEFULT_DEPART_NAME, "");
        }

        if (!AbStrUtil.isEmpty(job.optString("is_sign"))) {
            tools.putValue(Constants.IS_VIP_HOSPITAL,
                    job.optString("is_sign"));
        } else {
            tools.putValue(Constants.IS_VIP_HOSPITAL, "1");
        }
        if (!AbStrUtil.isEmpty(job.optString("mobile"))) {
            tools.putValue(Constants.MOBILE, job.optString("mobile"));
        } else {
            tools.putValue(Constants.MOBILE, "0");
        }
    }
}
