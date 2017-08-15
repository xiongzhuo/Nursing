package com.deya.hospital.util;

import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/12/5
 */
public class GsonUtils {

    public static  <T> JsonArray ListToJsonArray(List<T> list){
     return  TaskUtils.gson.toJsonTree(list, new TypeToken<List<T>>() {}.getType()).getAsJsonArray();
    }
    public static  <T> List<T> JsonArrayToList(String str){
     return    TaskUtils.gson.fromJson(str,
                new TypeToken<List<T>>() {
                }.getType());
    }
    public static  <T>  T JsonToObject(String json, Class t){
        return (T) TaskUtils.gson.fromJson(json,t);
    }

    public static JSONObject creatJsonObj(String s){
        JSONObject object=null;
        try {
            object=new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
            object=new JSONObject();
        }
        finally {
            return object;
        }

    }
}
