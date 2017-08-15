package com.deya.hospital.workspace.handwash;

import android.content.Context;
import android.text.TextUtils;

import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.vo.WrongRuleVo;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/10/9
 */
public class HandWashUtils {
    public static List<WrongRuleVo> getWrongRules(Context mcontext) {
        String jsonStr = SharedPreferencesUtil.getString(mcontext,
                "rules_json", null);
        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray jarr = jsonObject.optJSONArray("unrules");
                return TaskUtils.gson.fromJson(jarr.toString(),
                        new TypeToken<List<WrongRuleVo>>() {
                        }.getType());
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }



    public static List<JobListVo> getJobCacheData(Context mcontext) {
        // type1 位督导岗位 type4为职称 type3为职位
        String jsonStr = SharedPreferencesUtil.getString(mcontext,
                "jobinfolist", null);
        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray jarr4 = jsonObject.optJSONArray("jobType5");
                return TaskUtils.gson.fromJson(jarr4.toString(),
                        new TypeToken<List<JobListVo>>() {
                        }.getType());

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }

}
